package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error.{XmppStanzaError, XmppStanzaErrorType}
import org.scalatest.{FlatSpec, Matchers}

class StanzaErrorSpec extends FlatSpec with Matchers {
  "stanza error" should "be parsed" in {
    val expectedErrorText = "remote-server-not-found"
    val reqIq = IQ.create(IQ.Get)
    for {
      respIq <- reqIq.responseError(
        XmppStanzaError.RemoteServerTimeout()
          .withErrorType(XmppStanzaErrorType.Continue)
          .withText(expectedErrorText))
    } {
      val maybeStanzaError = XmppStanzaError.fromStanza(respIq)
      maybeStanzaError.isDefined should be (true)
      val stanzaError = maybeStanzaError.get

      stanzaError match {
        case e: XmppStanzaError.RemoteServerTimeout =>
          val (reason, errorType, text) = (e.reason, e.errorType, e.text)
          errorType should be (XmppStanzaErrorType.Continue)
          text should be (Some(expectedErrorText))

        case e =>
          throw e
      }
    }
  }
}
