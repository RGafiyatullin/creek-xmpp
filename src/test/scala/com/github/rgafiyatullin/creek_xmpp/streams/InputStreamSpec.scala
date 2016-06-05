package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.{Attribute, QName}
import com.github.rgafiyatullin.creek_xml.dom.Element
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError
import org.scalatest.{FlatSpec, Matchers}

class InputStreamSpec extends FlatSpec with Matchers {
  "An empty stream" should "return None initially" in {
    val is0 = InputStream.empty
    is0.out should be (None, is0)
  }

  it should "return StreamOpen event" in {
    val is0 = InputStream.empty.in(
      "<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>")
    val (event, is1) = is0.out
    event should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  }

  it should "return StreamOpen event when there are newlines inside openning tag" in {
    val is0 = InputStream.empty.in(
      "<stream:stream\n\txmlns='jabber:client'\n   xmlns:stream='http://etherx.jabber.org/streams'>")
    val (event, is1) = is0.out
    event should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  }

  it should "return StreamOpen event when fed in two pieces" in {
    val piece1 = "<stream:stream xmlns='jabber:"
    val piece2 = "client' xmlns:stream='http://etherx.jabber.org/streams'>"

    val is0 = InputStream.empty.in(piece1)
    val (none, is1) = is0.out
    none should be (None)

    val is2 = is1.in(piece2)
    val (some, is3) = is2.out
    some should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  }

  "An open stream" should "trigger stanza-events" in {
    val is0 = InputStream.empty.in(
      """<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
        |<stream:features>
        |<feature xmlns='test'/>
        |</stream:features>
        |<presence/>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
    val (stanzaStreamFeatures, is2) = is1.out
    stanzaStreamFeatures should be (Some(
        StreamEvent.Stanza(
          Element(
            XmppConstants.names.streams.features,
            Seq(), Seq(
              Element(QName("test", "feature"), Seq(), Seq())
            )))))
    val (stanzaPresence, is3) = is2.out
    stanzaPresence should be (Some(
      StreamEvent.Stanza(
        Element(
          XmppConstants.names.jabberClient.presence,
          Seq(), Seq()))))
    val (none, is4) = is3.out
    none should be (None)
  }

  it should "trigger stream error upon unexpected stream-reopen" in {
    val is0 = InputStream.empty.in(
      """<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
        |<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  val (localErrorInvalidXml, is2) = is1.out
    localErrorInvalidXml should be (Some(StreamEvent.LocalError(XmppStreamError.InvalidXml())))
  }

  it should "result in stream-open and stream-closed events" in {
    val is0 = InputStream.empty.in(
      """<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
        |</stream:stream>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
    val (streamClosed, is2) = is1.out
    streamClosed should be (Some(StreamEvent.StreamClose()))
  }

  it should "result in local-error upon receiving non-stream open when ExpectStreamOpen" in {
    val is0 = InputStream.empty.in("<stanza xmlns='jabber:client' />")
    val (localError, is1) = is0.out
    localError should be (Some(StreamEvent.LocalError(XmppStreamError.InvalidXml())))
  }

  it should "result in local-error upon receiving CData on stanza-level" in {
    val is0 = InputStream.empty.in(
      """<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
        |<![CDATA[cdata]]>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
    val (localError, is2) = is1.out
    localError should be (Some(StreamEvent.LocalError(XmppStreamError.InvalidXml())))
  }

  it should "successfully parse <?xml version='1.0'>" in {
    val is0 = InputStream.empty.in(
      """<?xml version='1.0'?><streams:stream xmlns:streams='http://etherx.jabber.org/streams' xmlns='jabber:client'>
        |<streams:features><router xmlns='http://wargaming.net/xmpp/router-service'/></streams:features>
        |<iq id='register-ruleset' type='error'><error><service-unavailable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/></error></iq>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(StreamEvent.StreamOpen(Seq(
      Attribute.NsImport("streams", "http://etherx.jabber.org/streams"),
      Attribute.NsImport("", "jabber:client")
    ))))
  }
}
