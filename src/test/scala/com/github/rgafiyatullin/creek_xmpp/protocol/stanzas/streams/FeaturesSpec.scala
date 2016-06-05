package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams

import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.XmlFromStringHelper
import org.scalatest.{FlatSpec, Matchers}

class FeaturesSpec extends FlatSpec with Matchers with XmlFromStringHelper {
  "Features.fromXml" should "return Some(features)" in {
    Features.fromXml(
      xml("<features xmlns='http://etherx.jabber.org/streams'><starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'/></features>"))
  }
}
