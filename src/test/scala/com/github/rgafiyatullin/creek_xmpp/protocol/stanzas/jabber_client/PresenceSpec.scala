package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.{Attribute, QName}
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import org.scalatest.{FlatSpec, Matchers}

class PresenceSpec extends FlatSpec with Matchers with XmlFromStringHelper {
  "Presence.Type" should "parse and render" in {
    Seq(
      Presence.Available,
      Presence.Unavailable,
      Presence.Probe,
      Presence.Subscribe,
      Presence.Unsubscribe,
      Presence.Subscribed,
      Presence.Unsubscribed
    ).foreach {
      t => Presence.Type.fromString(Some(t.toString)) should be (t)
    }
  }

  "Presence.fromXml" should "return Some(presence)" in {
    val somePresence = Presence.fromXml(xml("<presence xmlns='jabber:client' />"))
    somePresence.isDefined should be (true)
    val presence = somePresence.get
    presence should be (Presence(Element(XmppConstants.names.jabber.client.presence, Seq(
      Attribute.NsImport("", XmppConstants.names.jabber.client.ns)
    ), Seq())))
    presence.stanzaType should be (Presence.Available)
  }

  it should "return None upon non-IQ FQN" in {
    Presence.fromXml(xml("<presence xmlns='invalid' type='available' />")) should be (None)
  }

  it should "return None upon non-IQ type-attribute value" in {
    Presence.fromXml(xml("<presence xmlns='jabber:client' type='invalid' />")) should be (None)
  }

  "Presence.create" should "#1" in {
    val presence = Presence.create()
    presence.stanzaType should be (Presence.Available)
    presence.from should be (None)
    presence.to should be (None)
    presence.children should be (Seq())
  }

  it should "#2" in {
    val j1 = Jid.parse("a@im.localhost")
    val j2 = Jid.parse("b@im.localhost")
    val testXml = Element(QName("x", "x"), Seq(), Seq())
    val presence = Presence.create(
      presenceType = Presence.Subscribe,
      from = Some(j1),
      to = Some(j2),
      children = Seq(testXml)
    )

    presence.stanzaType should be (Presence.Subscribe)
    presence.from should be (Some(j1))
    presence.to should be (Some(j2))
    presence.children should be (Seq(testXml))
  }
}
