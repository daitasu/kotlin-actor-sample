package com.github.daitasu

import org.apache.pekko.actor.AbstractActor
import org.apache.pekko.actor.ActorRef
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.actor.Props

/**
 * キューに追加されるメッセージクラス
 */
data class GreetingMessage(val message: String)

/**
 * Pekko のアクタークラス
 */
class GreetingActor : AbstractActor() {
  /**
   * 受信するメッセージとその処理を定義する
   */
  override fun createReceive(): Receive {
      return receiveBuilder()
          .match(GreetingMessage::class.java) { msg ->
              println("[${self.path().name()}] Received: ${msg.message}")
              // 疑似的に重い処理: 2 秒待つ
              Thread.sleep(2000)
              println("[${self.path().name()}] Done: ${msg.message}")
          }
          .build()
  }
}

/**
 * main 関数。
 * Pekko の ActorSystem を生成し、
 * GreetingActor を2つ生成してメッセージを送信する。
 */
fun main() {
  // ActorSystem を作成
  val system = ActorSystem.create("MyActorSystem")

  // GreetingActor のインスタンスを生成 (ActorRef を取得)
  val actor1 = system.actorOf(Props.create(GreetingActor::class.java), "Actor1")
  val actor2 = system.actorOf(Props.create(GreetingActor::class.java), "Actor2")

  // メッセージを送る
  actor1.tell(GreetingMessage("Hello #1"), ActorRef.noSender())
  actor2.tell(GreetingMessage("Hello #2"), ActorRef.noSender())
  actor1.tell(GreetingMessage("Hello #3"), ActorRef.noSender())
  actor2.tell(GreetingMessage("Hello #4"), ActorRef.noSender())

  println("All messages sent from main")

  // しばらく待って実行ログを確認
  Thread.sleep(10000)

  // 4) アクターシステムをシャットダウン
  system.terminate()
}