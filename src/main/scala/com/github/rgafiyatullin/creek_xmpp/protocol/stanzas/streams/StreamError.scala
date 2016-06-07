package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams

import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml}
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

object StreamError extends StanzaFromXml[StreamError] {
  private val qn = XmppConstants.names.streams.error

  override def fromXml(xml: Element): Option[StreamError] =
    validateXml(xml, StreamError(xml))(
      qName = Some(XmppConstants.names.streams.error))

  def create(xmppStreamError: XmppStreamError): StreamError = {

    val xml = Element(qn, Seq(), Seq())

    StreamError(xml)
  }
}

case class StreamError(xml: Element) extends Stanza[StreamError] {
  override def setXml(newXml: Element): StreamError =
    copy(xml = newXml)
}
