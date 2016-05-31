package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.Element
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
    presence should be (Presence(Element("jabber:client", "presence", Seq(
      Attribute.NsImport("", "jabber:client")
    ), Seq())))
    presence.stanzaType should be (Presence.Available)
  }

  it should "return None upon non-IQ FQN" in {
    Presence.fromXml(xml("<presence xmlns='invalid' type='available' />")) should be (None)
  }

  it should "return None upon non-IQ type-attribute value" in {
    Presence.fromXml(xml("<presence xmlns='jabber:client' type='invalid' />")) should be (None)
  }
}
