package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.{Attribute, HighLevelEvent}
import com.github.rgafiyatullin.creek_xml.dom.{Element, NodeBuilder}
import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.HighLevelParser
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.Queue

class IQSpec extends FlatSpec with Matchers with XmlFromStringHelper {
  "IQ.Type" should "parse and render" in {
    Seq(
      IQ.Get,
      IQ.Set,
      IQ.Result,
      IQ.Error
    ).foreach {
      t => IQ.Type.fromString(Some(t.toString)) should be (t)
    }
  }

  "IQ.fromXml" should "return Some(iq)" in {
    IQ.fromXml(xml("<iq xmlns='jabber:client' type='result' id='test' />")) should be (Some(IQ(Element("jabber:client", "iq", Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.Unprefixed("type", "result"),
      Attribute.Unprefixed("id", "test")
    ), Seq()))))
  }

  it should "return None upon non-IQ FQN" in {
    IQ.fromXml(xml("<iq xmlns='invalid' type='get' id='test' />")) should be (None)
  }

  it should "return None upon non-IQ type-attribute value" in {
    IQ.fromXml(xml("<iq xmlns='jabber:client' type='invalid' id='test' />")) should be (None)
  }
}
