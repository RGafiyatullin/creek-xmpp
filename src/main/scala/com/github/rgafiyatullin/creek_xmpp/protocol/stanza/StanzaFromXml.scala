package com.github.rgafiyatullin.creek_xmpp.protocol.stanza

import com.github.rgafiyatullin.creek_xml.common.QName
import com.github.rgafiyatullin.creek_xml.dom.Node

import scala.util.{Failure, Success, Try}

object StanzaFromXml {
  final case class XmlValidationError(
    expectedQN: Option[QName],
    expectedTypeAttr: Option[PartialFunction[Option[String], Any]],
    actualQN: QName,
    actualTypeAttr: Option[String]
  ) extends Throwable
}

trait StanzaFromXml[StanzaType <: Stanza[StanzaType]] {
  def validateXml(xml: Node, stanza: => StanzaType)(
                     qName: Option[QName] = None,
                     typeAttribute: Option[PartialFunction[Option[String], Any]] = None
  ): Try[StanzaType] = {
    val validators = Seq(
        { qName.forall(_ == xml.qName) },
        { typeAttribute.forall(_.isDefinedAt(xml.attribute("type"))) }
      )

    if (validators.forall(identity))
      Success(stanza)
    else
      Failure(
        StanzaFromXml.XmlValidationError(
          qName, typeAttribute,
          xml.qName, xml.attribute("type")
        ))
  }

  def fromXml(xml: Node): Try[StanzaType]

  def checkXml(xml: Node): Boolean =
    fromXml(xml).isSuccess
}
