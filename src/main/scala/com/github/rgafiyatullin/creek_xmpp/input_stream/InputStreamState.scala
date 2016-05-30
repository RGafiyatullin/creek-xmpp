package com.github.rgafiyatullin.creek_xmpp.input_stream

import javax.xml.namespace.QName

import com.github.rgafiyatullin.creek_xml.dom.{Element, NodeBuilder}
import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.HighLevelEvent
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

private[input_stream] sealed trait InputStreamState {
  protected def qn(ns: String, ln: String): QName = new QName(ns, ln)

  def eventOption: Option[InputStreamEvent] = None

  def handleEvent: PartialFunction[HighLevelEvent, InputStreamState]
}

private[input_stream] object InputStreamState {

  case class ExpectStreamOpen(
                               override val eventOption: Option[InputStreamEvent]
                             ) extends InputStreamState
  {
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case HighLevelEvent.ElementOpen(_, _, localName, ns, streamAttributes)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        val event = InputStreamEvent.StreamOpen(streamAttributes)
        ExpectStanza(event)

      case HighLevelEvent.ElementClose(_, _, localName, ns)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        val event = InputStreamEvent.StreamClose()
        StreamClosed(event)

      case _ =>
        LocalError(XmppStreamError.InvalidXml())
    }
  }


  case class ExpectStanza(
                           event: InputStreamEvent
                         ) extends InputStreamState
  {
    override def eventOption: Option[InputStreamEvent] = Some(event)

    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case HighLevelEvent.ElementOpen(_, _, localName, ns, _)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        LocalError(XmppStreamError.InvalidXml())

      case elementOpen: HighLevelEvent.ElementOpen =>
        val builder = NodeBuilder.empty.in(elementOpen)
        BuildingStanza(builder)

      case HighLevelEvent.ElementClose(_, _, localName, ns)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        StreamClosed(InputStreamEvent.StreamClose())
    }
  }


  case class BuildingStanza(
                             stanzaBuilder: NodeBuilder
                           ) extends InputStreamState
  {
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case HighLevelEvent.Whitespace(_, _) => this

      case hle: HighLevelEvent =>
        val nextStanzaBuilder = stanzaBuilder.in(hle)
        nextStanzaBuilder.nodeOption match {
          case None =>
            copy(stanzaBuilder = nextStanzaBuilder)

          case Some(stanza: Element) =>
            val event = InputStreamEvent.Stanza(stanza)
            ExpectStanza(event)

          case Some(nonElement) =>
            LocalError(XmppStreamError.InvalidXml())
        }
    }
  }


  // Terminal states

  case class StreamClosed(event: InputStreamEvent.StreamClose) extends InputStreamState {
    override def eventOption: Option[InputStreamEvent] = Some(event)
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = { case _ => this }
  }

  case class LocalError(xmppStreamError: XmppStreamError) extends InputStreamState {
    override def eventOption: Option[InputStreamEvent] = Some(InputStreamEvent.LocalError(xmppStreamError))

    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case _ => this
    }
  }

  case class RemoteError(xmppStreamError: XmppStreamError) extends InputStreamState {
    override def eventOption: Option[InputStreamEvent] = Some(InputStreamEvent.RemoteError(xmppStreamError))

    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case _ => this
    }
  }
}
