package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

sealed trait StreamEvent

object StreamEvent {
  case class StreamOpen(attributes: Seq[Attribute]) extends StreamEvent

  case class Stanza(element: Element) extends StreamEvent

  case class StreamClose() extends StreamEvent

  case class LocalError(xmppStreamError: XmppStreamError) extends StreamEvent

  case class RemoteError(xmppStreamError: XmppStreamError) extends StreamEvent
}
