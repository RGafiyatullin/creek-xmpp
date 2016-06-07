package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{StanzaBase}

sealed trait StreamEvent

object StreamEvent {
  object Stanza {
    def apply(stanza: StanzaBase): Stanza =
      Stanza(stanza.xml)
  }


  case class StreamOpen(attributes: Seq[Attribute]) extends StreamEvent

  case class Stanza(element: Element) extends StreamEvent

  case class StreamClose() extends StreamEvent

  case class LocalError(xmppStreamError: XmppStreamError) extends StreamEvent

  case class RemoteError(xmppStreamError: XmppStreamError) extends StreamEvent
}
