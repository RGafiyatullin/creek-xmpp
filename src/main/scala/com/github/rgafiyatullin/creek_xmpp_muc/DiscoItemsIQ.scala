package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xml.dom_query.Implicits._
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.IQ

object DiscoItemsIQ {
  val nsDiscoItems = MucConstants.names.jabberOrg.protocol.discoItems

  object implicits {
    implicit class IQWithDiscoItems(iq: IQ) extends DiscoItemsIQ(iq)
  }


  object DiscoItem {
    def fromXml(node: Node): DiscoItem =
      DiscoItem(
        node.attribute("jid").map(Jid.parse).get,
        node.attribute("name"),
        node.attribute("node"),
        node.children)
  }

  final case class DiscoItem(jid: Jid, name: Option[String] = None, node: Option[String] = None, children: Seq[Node] = Seq.empty) {
    def withJid(j: Jid): DiscoItem = copy(jid = j)

    def withName(name: Option[String]): DiscoItem = copy(name = name)
    def withName(name: String): DiscoItem = withName(Some(name))

    def withNode(node: Option[String]): DiscoItem = copy(node = node)
    def withNode(node: String): DiscoItem = withNode(Some(node))

    def withChildren(children: Seq[Node]): DiscoItem = copy(children = children)
    def appendChild(child: Node): DiscoItem = withChildren(children ++ Seq(child))

    def toXml: Node =
      Element(
        nsDiscoItems.item,
        Seq(Attribute.Unprefixed("jid", jid.toString)) ++
          node.map(Attribute.Unprefixed("node", _)).toSeq ++
          name.map(Attribute.Unprefixed("name", _)).toSeq,
        children)
  }
}

class DiscoItemsIQ(iq: IQ) {
  import DiscoItemsIQ.{nsDiscoItems, DiscoItem}

  object discoItems {
    lazy val queryNodeOption: Option[Node] =
      (iq.xml select
        nsDiscoItems.query)
        .headOption

    lazy val itemNodes: Seq[Node] =
      iq.xml select
        nsDiscoItems.query /
          nsDiscoItems.item

    lazy val items: Seq[DiscoItem] =
      itemNodes.map(DiscoItem.fromXml)
  }
}
