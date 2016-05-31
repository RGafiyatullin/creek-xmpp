package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.dom.{Element, NodeBuilder}
import com.github.rgafiyatullin.creek_xml.common.{HighLevelEvent, QName}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

private[streams] sealed trait InputStreamState {
  protected def qn(ns: String, ln: String): QName = new QName(ns, ln)

  def eventOption: Option[StreamEvent] = None

  def handleEvent: PartialFunction[HighLevelEvent, InputStreamState]
}

private[streams] object InputStreamState {

  case class ExpectStreamOpen(
                               override val eventOption: Option[StreamEvent]
                             ) extends InputStreamState
  {
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case HighLevelEvent.ElementOpen(_, _, localName, ns, streamAttributes)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        val event = StreamEvent.StreamOpen(streamAttributes)
        ExpectStanza(Some(event))

//      case HighLevelEvent.ElementClose(_, _, localName, ns)
//        if qn(ns, localName) == XmppConstants.names.streams.stream
//      =>
//        val event = StreamEvent.StreamClose()
//        StreamClosed(event)

      case _ =>
        LocalError(XmppStreamError.InvalidXml())
    }
  }


  case class ExpectStanza(
                           override val eventOption: Option[StreamEvent]
                         ) extends InputStreamState
  {
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case HighLevelEvent.Whitespace(_, _) =>
        copy(eventOption = None)

      case HighLevelEvent.ElementOpen(_, _, localName, ns, _)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        LocalError(XmppStreamError.InvalidXml())

      case selfClosing: HighLevelEvent.ElementSelfClosing =>
        val stanza = NodeBuilder
          .empty.in(selfClosing)
          .nodeOption.get.asInstanceOf[Element]

        ExpectStanza(Some(StreamEvent.Stanza(stanza)))

      case elementOpen: HighLevelEvent.ElementOpen =>
        val builder = NodeBuilder.empty.in(elementOpen)
        BuildingStanza(builder)

      case HighLevelEvent.ElementClose(_, _, localName, ns)
        if qn(ns, localName) == XmppConstants.names.streams.stream
      =>
        StreamClosed(StreamEvent.StreamClose())
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
            val event = StreamEvent.Stanza(stanza)
            ExpectStanza(Some(event))

          case Some(nonElement) =>
            LocalError(XmppStreamError.InvalidXml())
        }
    }
  }


  // Terminal states

  case class StreamClosed(event: StreamEvent.StreamClose) extends InputStreamState {
    override def eventOption: Option[StreamEvent] = Some(event)
    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = { case _ => this }
  }

  case class LocalError(xmppStreamError: XmppStreamError) extends InputStreamState {
    override def eventOption: Option[StreamEvent] = Some(StreamEvent.LocalError(xmppStreamError))

    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case _ => this
    }
  }

  case class RemoteError(xmppStreamError: XmppStreamError) extends InputStreamState {
    override def eventOption: Option[StreamEvent] = Some(StreamEvent.RemoteError(xmppStreamError))

    override def handleEvent: PartialFunction[HighLevelEvent, InputStreamState] = {
      case _ => this
    }
  }
}
