package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.stream_writer.high_level_writer.HighLevelWriter
import org.scalatest.{FlatSpec, Matchers}

class OutputStreamSpec extends FlatSpec with Matchers {
  private def streamOpen: OutputStream = {
    val os0 = OutputStream.empty
    val os1 = os0.in(StreamEvent.StreamOpen(Seq()))
    val (streamOpen,  os2) = os1.out
    val streamOpenStrings = streamOpen.foldLeft(HighLevelWriter.empty)(_.in(_)).out._1

    streamOpenStrings.mkString should be ("<stream xmlns='http://etherx.jabber.org/streams'>")
    os2
  }

  "An OutputStream" should "process StreamOpen-event" in {
    streamOpen
  }

//  it should "process Stanza-event" in {
//    val os0 = streamOpen
//    val os1 = os0.in(StreamEvent.Stanza(???))
//  }
}
