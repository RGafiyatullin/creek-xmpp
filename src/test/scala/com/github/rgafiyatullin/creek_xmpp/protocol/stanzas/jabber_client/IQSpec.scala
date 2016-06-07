package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.{Attribute, HighLevelEvent, QName}
import com.github.rgafiyatullin.creek_xml.dom.{Element, NodeBuilder}
import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.HighLevelParser
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
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
    IQ.fromXml(xml("<iq xmlns='jabber:client' type='result' id='test' />")) should be (Some(IQ(Element(XmppConstants.names.jabber.client.iq, Seq(
      Attribute.NsImport("", XmppConstants.names.jabber.client.ns),
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

  "IQ.create" should "#1" in {
    val iq = IQ.create(iqType = IQ.Get)
    iq.stanzaTypeOption should be (Some(IQ.Get))
    iq.from should be (None)
    iq.to should be (None)
    iq.children should be (Seq())
  }

  it should "#2" in {
    val j1 = Jid.parse("a@im.localhost/desktop")
    val j2 = Jid.parse("b@im.localhost/laptop")
    val testXml = Element(QName("x", "x"), Seq(), Seq())

    val iq = IQ.create(
      iqType = IQ.Set,
      to = Some(j1),
      from = Some(j2),
      children = Seq(testXml))

    iq.stanzaTypeOption should be (Some(IQ.Set))
    iq.to should be (Some(j1))
    iq.from should be (Some(j2))
    iq.children should be (Seq(testXml))
  }
}
