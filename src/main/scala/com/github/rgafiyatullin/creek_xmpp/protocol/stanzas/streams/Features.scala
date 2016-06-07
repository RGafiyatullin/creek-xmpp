package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams

import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml}

object Features extends StanzaFromXml[Features] {
  private val qn = XmppConstants.names.streams.features

  override def fromXml(xml: Element): Option[Features] =
    validateXml(xml, Features(xml))(qName = Some(qn))
}

case class Features(xml: Element) extends Stanza[Features] {
  override def setXml(newXml: Element): Features = copy(xml = newXml)
}
