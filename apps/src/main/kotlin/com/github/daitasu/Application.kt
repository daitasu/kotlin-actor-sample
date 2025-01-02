package com.github.daitasu

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.system.measureTimeMillis

/**
 * キューに追加されるメッセージクラス
 */
data class GreetingMessage(val message: String)

/**
 * Channel を利用したアクターモデルっぽいサンプル
 */
class CoroutineGreetingActor(
    private val actorName: String,
    private val scope: CoroutineScope
    ) {
    private val channel = Channel<GreetingMessage>(capacity = 100)

    init {
        GlobalScope.launch {
            for (msg in channel) {
                process(msg)
            }
        }
    }

    suspend fun tell(message: GreetingMessage) {
        channel.send(message)
    }

    private suspend fun process(msg: GreetingMessage) {
        println("[$actorName] Received: ${msg.message}")
        delay(2000)
        println("[$actorName] Done: ${msg.message}")
    }
}


fun main() {
  runBlocking {
    // アクターを生成
    val actor1 = CoroutineGreetingActor("Actor1", this)
    val actor2 = CoroutineGreetingActor("Actor2", this)

    // メッセージを送信
    actor1.tell(GreetingMessage("Hello #1"))
    actor2.tell(GreetingMessage("Hello #2"))
    actor1.tell(GreetingMessage("Hello #3"))
    actor2.tell(GreetingMessage("Hello #4"))

    println("All messages sent from main")
  }

  Thread.sleep(10000)
}
