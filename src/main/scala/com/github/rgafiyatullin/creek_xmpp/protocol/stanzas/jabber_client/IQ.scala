package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml, StanzaType, StanzaTypeWithError}
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error.XmppStanzaError

object IQ extends StanzaFromXml[IQ] {
  private val qn = XmppConstants.names.jabber.client.iq

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

  override def fromXml(xml: Node): Option[IQ] =
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

case class IQ(xml: Node)
  extends Stanza[IQ]
    with StanzaType[IQ.Type, IQ]
    with StanzaTypeWithError[IQ.Type, IQ]
{
  override def errorStanzaType: IQ.Type = IQ.Error

  override def setXml(newXml: Node): IQ = copy(xml = newXml)

  override def stanzaTypeFromString = IQ.Type.fromString

  def isRequest: Boolean =
    stanzaTypeOption.contains(IQ.Get) || stanzaTypeOption.contains(IQ.Set)

  def isResponse: Boolean =
    !isRequest

  def bodyOption: Option[Node] = children.headOption.flatMap {
    case e: Node => Some(e)
    case _ => None
  }
  def body: Node = bodyOption.get

  def responseResult(body: Option[Node] = None): Option[IQ] =
    Some(isRequest)
      .collect {
        case true =>
          IQ(Element(IQ.qn, Seq(), Seq()))
            .setId(id)
            .setFrom(to)
            .setTo(from)
            .setChildren(body.toSeq)
            .setStanzaType(IQ.Result)
      }

  def responseError(xmppStanzaError: XmppStanzaError): Option[IQ] =
    Some(isRequest)
      .collect {
        case true =>
          IQ(Element(IQ.qn, Seq(), Seq()))
            .setId(id)
            .setFrom(to)
            .setTo(from)
            .setChildren(Seq(
              xmppStanzaError.toXml
            ))
            .setStanzaType(IQ.Error)
      }
}
