package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza._
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.Presence.Type

object Presence extends StanzaFromXml[Presence] {
  sealed trait Type

  private val qn = XmppConstants.names.jabberClient.presence

  object Type {
    val fromString: PartialFunction[Option[String], Type] = {
      case None => Available
      case Some(Available.toString) => Available
      case Some(Unavailable.toString) => Unavailable
      case Some(Probe.toString) => Probe
      case Some(Subscribe.toString) => Subscribe
      case Some(Unsubscribe.toString) => Unsubscribe
      case Some(Subscribed.toString) => Subscribed
      case Some(Unsubscribed.toString) => Unsubscribed
    }
  }

  case object Available extends Type { override val toString = "available" }
  case object Unavailable extends Type { override val toString = "unavailable" }
  case object Probe extends Type { override val toString = "probe" }
  case object Subscribe extends Type { override val toString = "subscribe" }
  case object Unsubscribe extends Type { override val toString = "unsubscribe" }
  case object Subscribed extends Type { override val toString = "subscribed" }
  case object Unsubscribed extends Type { override val toString = "unsubscribed" }

  override def fromXml(xml: Element): Option[Presence] =
    validateXml(xml, Presence(xml))(
      qName = Some(qn),
      typeAttribute = Some(Type.fromString)
    )

  def create(
              presenceType: Type = Available,
              to: Option[Jid] = None,
              from: Option[Jid] = None,
              children: Seq[Node] = Seq()
            ): Presence =
  {
    val presenceXml =
      Element(qn, Seq(), children)
        .setAttribute("type", Some(presenceType.toString))
        .setAttribute("to", to.map(_.toString))
        .setAttribute("from", from.map(_.toString))

    Presence(presenceXml)
  }
}

case class Presence(xml: Element) extends Stanza with StanzaSetXml[Presence] with StanzaTypeWithDefault[Presence.Type] {
  override def defaultStanzaType: Type = Presence.Available
  override def stanzaTypeFromString = Presence.Type.fromString

  override def setXml(newXml: Element): Presence =
    copy(xml = newXml)
}
