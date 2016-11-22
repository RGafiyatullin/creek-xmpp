package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xml.dom_query.Implicits._
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.IQ

object DiscoInfoIQ {
  val nsDiscoInfo = MucConstants.names.jabberOrg.protocol.discoInfo

  object DiscoFeature {
    def fromXml(node: Node): DiscoFeature =
      DiscoFeature(node.attribute("var").get, node.children)
  }

  final case class DiscoFeature(
    name: String,
    children: Seq[Node] = Seq.empty)
  {
    def withName(n: String): DiscoFeature = copy(name = n)
    def withChildren(chs: Seq[Node]): DiscoFeature = copy(children = chs)

    def toXml: Node = Element(
      nsDiscoInfo.feature, Seq(
        Attribute.Unprefixed("var", name)
      ), children)
  }

  object DiscoIdentity {
    def fromXml(node: Node): DiscoIdentity =
      DiscoIdentity(
        node.attribute("category").get,
        node.attribute("type").get,
        node.attribute("name"),
        node.children)
  }

  final case class DiscoIdentity(
    category: String,
    identityType: String,
    nameOption: Option[String] = None,
    children: Seq[Node] = Seq.empty)
  {
    def withCategory(c: String): DiscoIdentity = copy(category = c)
    def withType(t: String): DiscoIdentity = copy(identityType = t)
    def withName(n: Option[String]): DiscoIdentity = copy(nameOption = n)
    def withName(n: String): DiscoIdentity = withName(Some(n))
    def withChildren(chs: Seq[Node]): DiscoIdentity = copy(children = chs)

    def toXml: Node = Element(
      nsDiscoInfo.identity,
      Seq(
        Attribute.Unprefixed("category", category),
        Attribute.Unprefixed("type", identityType)
      ) ++ nameOption.map(Attribute.Unprefixed("name", _)).toSeq,
      children)
  }

  object implicits {
    implicit class IQWithDiscoInfo(iq: IQ) extends DiscoInfoIQ(iq)
  }
}

class DiscoInfoIQ(iq: IQ) {
  import DiscoInfoIQ.{nsDiscoInfo, DiscoFeature, DiscoIdentity}

  object discoInfo {
    lazy val queryNodeOption: Option[Node] =
      (iq.xml select nsDiscoInfo.query).headOption

    lazy val featureNodes: Seq[Node] =
      iq.xml select nsDiscoInfo.query / nsDiscoInfo.feature

    lazy val identityNodeOption: Option[Node] =
      (iq.xml select nsDiscoInfo.query / nsDiscoInfo.identity).headOption

    lazy val features: Seq[DiscoFeature] =
      featureNodes.map(DiscoFeature.fromXml)

    lazy val identityOption: Option[DiscoIdentity] =
      identityNodeOption.map(DiscoIdentity.fromXml)
  }
}
