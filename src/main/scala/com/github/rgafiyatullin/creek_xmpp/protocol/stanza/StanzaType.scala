package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

trait StanzaType[T] extends Stanza {
  def stanzaTypeFromString: PartialFunction[Option[String], T]

  def stanzaTypeOption: Option[T] =
    stanzaTypeFromString.lift.apply(xml.attribute("xml"))
}

trait StanzaTypeWithDefault[T] extends StanzaType[T] {
  def defaultStanzaType: T

  def stanzaType: T =
    stanzaTypeFromString
      .applyOrElse(
        xml.attribute("xml"),
        { _: Option[String] => defaultStanzaType })
}
