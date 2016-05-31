package com.github.rgafiyatullin.creek_xmpp.input_stream

import com.github.rgafiyatullin.creek_xml.common.Attribute
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
    event should be (Some(InputStreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  }

  "An open stream" should "trigger stanza-events" in {
    val is0 = InputStream.empty.in(
      """<stream:stream xmlns='jabber:client' xmlns:stream='http://etherx.jabber.org/streams'>
        |<stream:features/>
        |<presence/>
      """.stripMargin)
    val (streamOpen, is1) = is0.out
    streamOpen should be (Some(InputStreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
    val (stanzaStreamFeatures, is2) = is1.out
    stanzaStreamFeatures should be (Some(
        InputStreamEvent.Stanza(
          Element(
            XmppConstants.names.streams.ns,
            "features",
            Seq(), Seq()))))
    val (stanzaPresence, is3) = is2.out
    stanzaPresence should be (Some(
      InputStreamEvent.Stanza(
        Element(
          XmppConstants.names.jabberClient.ns,
          "presence",
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
    streamOpen should be (Some(InputStreamEvent.StreamOpen(Seq(
      Attribute.NsImport("", "jabber:client"),
      Attribute.NsImport("stream", "http://etherx.jabber.org/streams")
    ))))
  val (localErrorInvalidXml, is2) = is1.out
    localErrorInvalidXml should be (Some(InputStreamEvent.LocalError(XmppStreamError.InvalidXml())))
  }
}
