package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid

trait StanzaBase {
  val xml: Element

  def id: Option[String] = xml.attribute("id")
  def from: Option[Jid] = xml.attribute("from").map(Jid.parse)
  def to: Option[Jid] = xml.attribute("to").map(Jid.parse)
  def children: Seq[Node] = xml.children
}

trait Stanza[StanzaType] extends StanzaBase {
  def setXml(newXml: Element): StanzaType

  def setId(id: Option[String]): StanzaType = setXml(xml.setAttribute("id", id))
  def setId(id: String): StanzaType = setId(Some(id))

  def setFrom(from: Option[Jid]): StanzaType = setXml(xml.setAttribute("from", from.map(_.toString)))
  def setFrom(from: Jid): StanzaType = setFrom(Some(from))

  def setTo(to: Option[Jid]): StanzaType = setXml(xml.setAttribute("to", to.map(_.toString)))
  def setTo(to: Jid): StanzaType = setTo(Some(to))

  def setChildren(children: Seq[Node]): StanzaType = setXml(xml.copy(children = children))

  def setAttributes(attributes: Seq[Attribute]): StanzaType = setXml(xml.copy(attributes = attributes))
}
