package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams

import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml}

object Features extends StanzaFromXml[Features] {
  private val qn = XmppConstants.names.streams.features

  override def fromXml(xml: Node): Option[Features] =
    validateXml(xml, Features(xml))(qName = Some(qn))
}

case class Features(xml: Node) extends Stanza[Features] {
  override def setXml(newXml: Node): Features = copy(xml = newXml)
}
