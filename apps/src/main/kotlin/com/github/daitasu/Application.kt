package com.github.daitasu

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * キューに格納されるメッセージ用インターフェース
 */
interface Message : Cloneable {
    public override fun clone(): Message
}

/**
 * Messageを実装した具体的なメッセージクラス。
 */
data class GreetingMessage(var message: String) : Message {
    override fun clone(): Message {
        // データクラスなので copy() を使えば簡単に複製できます
        return copy(message = this.message)
    }
}

/**
 * Actor の抽象クラス
 *
 * - Runnable を実装してスレッドで動かせるようにする
 * - メッセージを保持するキューを用意する
 * - tell() によってメッセージを受け取り、キューに格納する
 * - run() 内でキューからメッセージを取り出し、onReceive() で処理を委譲する
 */
abstract class AbstractActor : Runnable {
    private val thread = Thread(this)
    private val messages: Queue<Message> = ConcurrentLinkedQueue()

    /**
     * キューにメッセージを格納し、必要に応じてスレッドを起動する
     */
    fun tell(message: Message) {
        // clone() を呼び出し、メッセージの変更がキュー内のデータに影響しないようにする
        messages.add(message.clone())

        // スレッドの状態をチェックし、NEW または TERMINATED なら再度起動
        when (thread.state) {
            Thread.State.NEW, Thread.State.TERMINATED -> thread.start()
            else -> { /* 既に動作中なら何もしない */ }
        }
    }

    /**
     * スレッドのメインループ
     * キューからメッセージを取り出して onReceive() で処理させる
     */
    override fun run() {
        while (messages.isNotEmpty()) {
            val msg = messages.poll() ?: continue
            onReceive(msg)
        }
    }

    /**
     * 受け取ったメッセージをどのように処理するかは継承先で実装する
     */
    protected abstract fun onReceive(msg: Message)
}

/**
 * GreetingMessage を受け取ってコンソールに表示するだけのシンプルな Actor
 */
class GreetingActor : AbstractActor() {
    override fun onReceive(msg: Message) {
        if (msg is GreetingMessage) {
            println("GreetingActor received: ${msg.message}")
        } else {
            println("GreetingActor received unknown message: $msg")
        }
    }
}

fun main() {
    // アクターを生成
    val actor1 = GreetingActor()
    // メッセージを送信
    actor1.tell(GreetingMessage("hello"))
    actor1.tell(GreetingMessage("hello2"))
}
