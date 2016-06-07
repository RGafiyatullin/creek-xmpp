package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.{Attribute, QName}
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import org.scalatest.{FlatSpec, Matchers}

class MessageSpec extends FlatSpec with Matchers with XmlFromStringHelper {
  "Message.Type" should "render/parse" in {
    Seq(
      Message.Chat,
      Message.Error,
      Message.Groupchat,
      Message.Headline,
      Message.Normal
    ).foreach {
      t => Message.Type.fromString(Some(t.toString)) should be (t)
    }
  }

  "Message.fromXml" should "return Some(message)" in {
    Message.fromXml(xml("<message xmlns='jabber:client' type='groupchat' id='test' />")) should be (Some(Message(Element(XmppConstants.names.jabber.client.message, Seq(
      Attribute.NsImport("", XmppConstants.names.jabber.client.ns),
      Attribute.Unprefixed("type", "groupchat"),
      Attribute.Unprefixed("id", "test")
    ), Seq()))))
  }

  it should "return None upon non-Message FQN" in {
    IQ.fromXml(xml("<message xmlns='invalid' type='groupchat' id='test' />")) should be (None)
  }

  it should "return None upon non-Message type-attribute value" in {
    IQ.fromXml(xml("<message xmlns='jabber:client' type='invalid' id='test' />")) should be (None)
  }

  "Message.create" should "#1" in {
    val message = Message.create()
    message.stanzaType should be (Message.Chat)
    message.to should be (None)
    message.from should be (None)
    message.children should be (Seq())
  }

  it should "#2" in {
    val j1 = Jid.parse("a@im.localhost/desktop")
    val j2 = Jid.parse("b@im.localhost/laptop")
    val testXml = Element(QName("x", "x"), Seq(), Seq())

    val message = Message.create(
      messageType = Message.Headline,
      to = Some(j1),
      from = Some(j2),
      children = Seq(testXml))

    message.stanzaType should be (Message.Headline)
    message.to should be (Some(j1))
    message.from should be (Some(j2))
    message.children should be (Seq(testXml))
  }

}
