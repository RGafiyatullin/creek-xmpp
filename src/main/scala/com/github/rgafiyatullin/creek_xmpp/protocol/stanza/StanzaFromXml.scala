package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.common.QName
import com.github.rgafiyatullin.creek_xml.dom.Element

trait StanzaFromXml[StanzaType <: Stanza] {
  def validateXml(xml: Element, stanza: => StanzaType)(
                     qName: Option[QName] = None,
                     typeAttribute: Option[PartialFunction[Option[String], Any]] = None
  ): Option[StanzaType] = {
    val validators = Seq(
        { qName.forall(_ == xml.qName) },
        { typeAttribute.forall(_.isDefinedAt(xml.attribute("type"))) }
      )

    if (validators.forall(identity))
      Some(stanza)
    else
      None
  }

  def fromXml(xml: Element): Option[StanzaType]

  def checkXml(xml: Element): Boolean =
    fromXml(xml).isDefined
}
