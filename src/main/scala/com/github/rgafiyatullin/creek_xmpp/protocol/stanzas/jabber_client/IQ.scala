package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml, StanzaSetXml, StanzaType}

object IQ extends StanzaFromXml[IQ] {
  private val qn = XmppConstants.names.jabberClient.iq

  sealed trait Type
  object Type {
    val fromString: PartialFunction[Option[String], Type] = {
      case Some(Get.toString) => Get
      case Some(Set.toString) => Set
      case Some(Result.toString) => Result
      case Some(Error.toString) => Error
    }
  }

  case object Get extends Type { override val toString = "get" }
  case object Set extends Type { override val toString = "set" }
  case object Result extends Type { override val toString = "result" }
  case object Error extends Type { override val toString = "error" }

  override def fromXml(xml: Element): Option[IQ] =
    validateXml(xml, IQ(xml))(
      qName = Some(qn),
      typeAttribute = Some(IQ.Type.fromString)
    )

  def create(
            iqType: Type,
            to: Option[Jid] = None,
            from: Option[Jid] = None,
            children: Seq[Node] = Seq()
            ): IQ =
  {
    val iqXml =
      Element(qn, Seq(), children)
        .setAttribute("type", Some(iqType.toString))
        .setAttribute("to", to.map(_.toString))
        .setAttribute("from", from.map(_.toString))

    IQ(iqXml)
  }
}

case class IQ(xml: Element) extends Stanza with StanzaSetXml[IQ] with StanzaType[IQ.Type] {
  override def setXml(newXml: Element): IQ = copy(xml = newXml)

  override def stanzaTypeFromString = IQ.Type.fromString
}
