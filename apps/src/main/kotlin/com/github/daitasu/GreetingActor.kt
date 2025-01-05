package com.github.daitasu

import org.apache.pekko.actor.typed.Behavior
import org.apache.pekko.actor.typed.javadsl.Behaviors
import org.apache.pekko.cluster.typed.Cluster

data class GreetingMessage(val message: String)

object GreetingActor {
  fun create(): Behavior<GreetingMessage> =
    Behaviors.receive<GreetingMessage> { context, message ->
      val member = Cluster.get(context.system).selfMember()
      println("[Address: ${member.address()}] Status: ${member.status()} Message: ${message.message}")

      Behaviors.same<GreetingMessage>()
  }
}