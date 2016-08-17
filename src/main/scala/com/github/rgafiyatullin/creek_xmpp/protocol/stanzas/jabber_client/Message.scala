package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.dom.{Element, Node}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml, StanzaTypeWithDefault, StanzaTypeWithError}

object Message extends StanzaFromXml[Message] {
  private val qn = XmppConstants.names.jabber.client.message

  sealed trait Type
  object Type {
    val fromString: PartialFunction[Option[String], Type] = {
      case None => Normal
      case Some(Chat.toString) => Chat
      case Some(Error.toString) => Error
      case Some(Groupchat.toString) => Groupchat
      case Some(Headline.toString) => Headline
      case Some(Normal.toString) => Normal
    }
  }

  case object Chat extends Type { override val toString = "chat" }
  case object Error extends Type { override val toString = "error" }
  case object Groupchat extends Type { override val toString = "groupchat" }
  case object Headline extends Type { override val toString = "headline" }
  case object Normal extends Type { override val toString = "normal" }

  override def fromXml(xml: Node): Option[Message] =
    validateXml(xml, Message(xml))(
      qName = Some(qn),
      typeAttribute = Some(Type.fromString))

  def create(
              messageType: Type = Chat,
              to: Option[Jid] = None,
              from: Option[Jid] = None,
              children: Seq[Node] = Seq()
            ): Message =
  {
    val messageXml =
      Element(qn, Seq(), children)
        .setAttribute("type", Some(messageType.toString))
        .setAttribute("to", to.map(_.toString))
        .setAttribute("from", from.map(_.toString))

    Message(messageXml)
  }
}

case class Message(xml: Node)
  extends Stanza[Message]
    with StanzaTypeWithDefault[Message.Type, Message]
    with StanzaTypeWithError[Message.Type, Message]
{
  override def errorStanzaType: Message.Type = Message.Error
  override def defaultStanzaType: Message.Type = Message.Chat
  override def stanzaTypeFromString: PartialFunction[Option[String], Message.Type] = Message.Type.fromString

  override def setXml(newXml: Node): Message = copy(xml = newXml)
}
