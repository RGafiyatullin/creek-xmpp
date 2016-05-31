package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza.{Stanza, StanzaFromXml, StanzaType}

object IQ extends StanzaFromXml[IQ] {
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
      qName = Some(XmppConstants.names.jabberClient.iq),
      typeAttribute = Some(IQ.Type.fromString)
    )
}

case class IQ(xml: Element) extends Stanza with StanzaType[IQ.Type] {
  override def stanzaTypeFromString = IQ.Type.fromString
}
