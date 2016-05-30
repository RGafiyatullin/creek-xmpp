package com.github.rgafiyatullin.creek_xmpp.input_stream

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

sealed trait InputStreamEvent

object InputStreamEvent {
  case class StreamOpen(attributes: Seq[Attribute]) extends InputStreamEvent

  case class Stanza(element: Element) extends InputStreamEvent

  case class StreamClose() extends InputStreamEvent

  case class LocalError(xmppStreamError: XmppStreamError) extends InputStreamEvent

  case class RemoteError(xmppStreamError: XmppStreamError) extends InputStreamEvent
}
