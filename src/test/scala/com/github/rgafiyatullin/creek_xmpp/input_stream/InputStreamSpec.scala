package com.github.rgafiyatullin.creek_xmpp.input_stream

import com.github.rgafiyatullin.creek_xml.common.Attribute
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
}
