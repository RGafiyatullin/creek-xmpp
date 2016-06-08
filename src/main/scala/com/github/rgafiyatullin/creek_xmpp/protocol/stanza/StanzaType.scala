package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error.XmppStanzaError

trait StanzaType[T, S] extends Stanza[S] {
  def stanzaTypeFromString: PartialFunction[Option[String], T]

  def stanzaTypeOption: Option[T] =
    stanzaTypeFromString.lift.apply(xml.attribute("type"))


  def setStanzaType(t: Option[T]): S =
    setXml(xml.setAttribute("type", t.map(_.toString)))

  def setStanzaType(t: T): S =
    setStanzaType(Some(t))
}

trait StanzaTypeWithDefault[T, S] extends StanzaType[T, S] {
  def defaultStanzaType: T

  def stanzaType: T =
    stanzaTypeFromString
      .applyOrElse(
        xml.attribute("type"),
        { _: Option[String] => defaultStanzaType })
}

trait StanzaTypeWithError[T, S] extends StanzaType[T, S] {
  def errorStanzaType: T

  def isError: Boolean = stanzaTypeOption.contains(errorStanzaType)
  def errorResponse(xmppStanzaError: XmppStanzaError): S =
    setXml(
      Element(
        xml.qName,
        Seq(),
        Seq(xmppStanzaError.toXml)
      )
        .setAttribute(
          "type", Some(errorStanzaType.toString))
        .setAttribute(
          "from", to.map(_.toString))
        .setAttribute(
          "to", from.map(_.toString))
        .setAttribute(
          "id", id))

}
