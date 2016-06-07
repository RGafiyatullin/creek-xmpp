package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

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
