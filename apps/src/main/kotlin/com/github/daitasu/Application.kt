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
      println("[Address: ${member.address()}] Message: ${message.message}")
      Behaviors.same<GreetingMessage>()
  }
}

fun main() {
    val port = System.getenv("CLUSTER_PORT")?.toIntOrNull() ?: 2551
    val hostname = System.getenv("CLUSTER_IP") ?: "127.0.0.1"

    // Pekkoのプロパティを設定
    System.setProperty("pekko.remote.artery.canonical.hostname", hostname)
    System.setProperty("pekko.remote.artery.canonical.port", port.toString())

    // application.conf を読み込む
    val config = ConfigFactory.load()

    // アクターシステムの起動
    val system = ActorSystem.create(
        GreetingActor.create(),
        "MyActorSystem",
        config
    )

    // クラスタにノードをJOIN する
    Cluster.get(system).manager().tell(Join.create(Cluster.get(system).selfMember().address()))

    if (port != 2551) {
        Thread.sleep(5000)
        val message = GreetingMessage("Hello World")
        system.tell(message)
    }

    println("Started ${Cluster.get(system).selfMember().address()}")
}
