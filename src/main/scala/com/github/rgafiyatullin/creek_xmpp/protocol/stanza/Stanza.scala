package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid

trait Stanza {
  val xml: Element

  def id: Option[String] = xml.attribute("id")
  def from: Option[Jid] = xml.attribute("from").map(Jid.parse)
  def to: Option[Jid] = xml.attribute("to").map(Jid.parse)
  def children: Seq[Node] = xml.children
}
