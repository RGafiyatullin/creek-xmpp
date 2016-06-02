package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid

trait StanzaSetXml[StanzaType] extends Stanza {
  def setXml(newXml: Element): StanzaType

  def setId(id: Option[String]) = setXml(xml.setAttribute("id", id))
  def setFrom(from: Option[Jid]) = setXml(xml.setAttribute("from", from.map(_.toString)))
  def setTo(to: Option[Jid]) = setXml(xml.setAttribute("to", to.map(_.toString)))
}
