package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.{Attribute, HighLevelEvent, Position}
import com.github.rgafiyatullin.creek_xml.stream_writer.high_level_writer.HighLevelWriter
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams.StreamError

import scala.collection.immutable.Queue

object OutputStream {
  def empty: OutputStream =
    OutputStream(HighLevelWriter.empty, Queue.empty)
}

case class OutputStream(writer: HighLevelWriter, outputStash: Queue[String]) {
  private val emptyPosition: Position = Position.withoutPosition

  def out: (Queue[String], OutputStream) = {
    val (writerOutput, nextWriter) = writer.out
    val output = writerOutput.foldLeft(outputStash)(_.enqueue(_))
    val nextOutputStream = copy(writer = nextWriter, outputStash = Queue.empty)
    (output, nextOutputStream)
  }

  def in(streamEvent: StreamEvent): OutputStream =
    streamEvent match {
      case re: StreamEvent.RemoteError =>
        ???

      case StreamEvent.StreamOpen(attributes) =>
        val attributesWithImport = attributes.find {
          case Attribute.NsImport(_, XmppConstants.names.streams.ns) => true
          case _ => false
        } match {
          case None =>
            Seq(
              Attribute.NsImport(
                "", XmppConstants.names.streams.ns)
            ) ++ attributes
          case Some(_) =>
            attributes
        }
        val hleStreamOpen = HighLevelEvent.ElementOpen(
          emptyPosition, "", "stream",
          XmppConstants.names.streams.ns, attributesWithImport)
        val nextWriter = HighLevelWriter.empty.in(hleStreamOpen)
        val nextOutputStash = writer.out._1
          .foldLeft(outputStash)(_.enqueue(_))
        copy(writer = nextWriter, outputStash = nextOutputStash)

      case StreamEvent.StreamClose() =>
        val hleStreamClose = HighLevelEvent.ElementClose(
          emptyPosition, "", "stream", XmppConstants.names.streams.ns)
        val nextWriter = writer.in(hleStreamClose)
        copy(writer = nextWriter)

      case StreamEvent.Stanza(stanzaXml) =>
        val stanzaHLEvents = stanzaXml.toEvents
        val nextWriter = stanzaHLEvents.foldLeft(writer)(_.in(_))
        copy(writer = nextWriter)

      case StreamEvent.LocalError(xmppStreamError) =>
        val streamErrorStanza = StreamError.create(xmppStreamError)
        val event = StreamEvent.Stanza(streamErrorStanza)
        in(event)
    }
}
