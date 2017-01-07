package com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error
import scala.reflect.{classTag, ClassTag}

trait ToXmppStanzaError[To <: XmppStanzaErrorBase[To]] extends Throwable {
  def toStanzaError(implicit tag: ClassTag[To]): To = {
    val se = classTag[To].runtimeClass.newInstance().asInstanceOf[To]
    se.withReason(this)
  }
}

object ToXmppStanzaError {
  implicit class ToInternalServerError(error: Throwable) extends ToXmppStanzaError[XmppStanzaError.InternalServerError]
}



