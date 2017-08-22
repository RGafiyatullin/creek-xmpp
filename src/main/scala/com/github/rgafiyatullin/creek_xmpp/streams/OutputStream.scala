package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.{Attribute, HighLevelEvent, Position}
import com.github.rgafiyatullin.creek_xml.stream_writer.high_level_writer.HighLevelWriter
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.streams.StreamError

import scala.collection.immutable.Queue

object OutputStream {
  def empty: OutputStream =
    OutputStream(Queue.empty)
}

case class OutputStream(outputStash: Queue[HighLevelEvent], streamsNsPrefix: String = "") {
  private val emptyPosition: Position = Position.withoutPosition

  def out: (Queue[HighLevelEvent], OutputStream) = {
    (outputStash, copy(outputStash = Queue.empty))
  }

  def in(streamEvent: StreamEvent): OutputStream =
    streamEvent match {
      case re: StreamEvent.RemoteError =>
        ???

      case StreamEvent.StreamOpen(attributes) =>
        val streamsNsPrefixProvidedOption = attributes
          .collectFirst { case Attribute.NsImport(p, XmppConstants.names.streams.ns) => p }
        val streamsNsPrefixToUse = streamsNsPrefixProvidedOption.getOrElse("")
        val attributesNsImportsAdjusted =
          attributes
            .filter(_ match {
              case Attribute.NsImport(`streamsNsPrefixToUse`, _) => false
              case _ => true
            }) :+ Attribute.NsImport(streamsNsPrefixToUse, XmppConstants.names.streams.ns)

        val hleStreamOpen = HighLevelEvent.ElementOpen(
          emptyPosition, streamsNsPrefixToUse, "stream",
          XmppConstants.names.streams.ns, attributesNsImportsAdjusted)
        val nextWriter = HighLevelWriter.empty.in(hleStreamOpen)
        copy(
          outputStash = outputStash.enqueue(hleStreamOpen),
          streamsNsPrefix = streamsNsPrefixToUse)

      case StreamEvent.StreamClose() =>
        val hleStreamClose = HighLevelEvent.ElementClose(
          emptyPosition, streamsNsPrefix, "stream", XmppConstants.names.streams.ns)
        copy(
          outputStash = outputStash.enqueue(hleStreamClose))

      case StreamEvent.Stanza(stanzaXml) =>
        val stanzaHLEvents = stanzaXml.toEvents
        copy(outputStash = stanzaHLEvents.foldLeft(outputStash)(_.enqueue(_)))

      case StreamEvent.LocalError(xmppStreamError) =>
        val streamErrorStanza = StreamError.create(xmppStreamError)
        val event = StreamEvent.Stanza(streamErrorStanza)
        in(event)
    }
}
