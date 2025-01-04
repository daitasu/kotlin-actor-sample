package com.github.daitasu

import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.javadsl.Behaviors
import org.apache.pekko.cluster.typed.Cluster
import org.apache.pekko.cluster.typed.Join
import com.typesafe.config.ConfigFactory

data class GreetingMessage(val message: String)

object GreetingActor {
  fun create(): Behavior<GreetingMessage> =
    Behaviors.receive<GreetingMessage> { context, message ->
      val member = Cluster.get(context.system).selfMember()
      println("[${message.message}] Received by: ${member.address()}")
      Behaviors.same<GreetingMessage>()
  }
}

fun main(args: Array<String>) {
    val port = if (args.isNotEmpty()) args[0].toInt() else 2551

    // 環境変数を設定
    System.setProperty("PEKKO_CANONICAL_HOSTNAME", if (port == 2551) "node-1" else "node-2")
    System.setProperty("PEKKO_CANONICAL_PORT", port.toString())

    // application.conf を読み込む
    val config = ConfigFactory.load()

    val system = ActorSystem.create(
        GreetingActor.create(),
        "MyActorSystem",
        config
    )

    Cluster.get(system).manager().tell(Join.create(Cluster.get(system).selfMember().address()))

    if (port != 2551) {
        Thread.sleep(5000) // 他のノードが起動するのを待つ
        val message = GreetingMessage("Hello from node 1")
        system.tell(message)
    }

    println("Started ${Cluster.get(system).selfMember().address()}")
}