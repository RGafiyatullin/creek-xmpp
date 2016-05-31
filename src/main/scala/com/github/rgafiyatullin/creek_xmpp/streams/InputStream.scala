package com.github.rgafiyatullin.creek_xmpp.streams

import com.github.rgafiyatullin.creek_xml.common.HighLevelEvent
import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.{HighLevelParser, HighLevelParserError}
import com.github.rgafiyatullin.creek_xml.stream_parser.low_level_parser.LowLevelParserError
import com.github.rgafiyatullin.creek_xml.stream_parser.tokenizer.TokenizerError
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

import scala.annotation.tailrec

object InputStream {
  def empty: InputStream =
    InputStream(
      InputStreamState.ExpectStreamOpen(None),
      HighLevelParser.empty.withoutPosition)
}


case class InputStream(state: InputStreamState, parser: HighLevelParser) {
  def in(char: Char): InputStream =
    copy(parser = parser.in(char))

  def in(string: String): InputStream =
    copy(parser = parser.in(string))

  @tailrec
  final def out: (Option[StreamEvent], InputStream) = {
    getNextParserEvent(parser) match {
      case Right(nextParser) =>
        (None, copy(parser = nextParser))

      case Left((hle, nextParser)) =>
        val nextState = state.handleEvent.applyOrElse(hle, unexpectedParserEvent _)
        val nextStream = copy(state = nextState, parser = nextParser)
        nextState.eventOption match {
          case None =>
            nextStream.out

          case Some(streamEvent) =>
            (Some(streamEvent), nextStream)
        }
    }
  }

  private def getNextParserEvent(parser0: HighLevelParser): Either[(HighLevelEvent, HighLevelParser), HighLevelParser] =
    try Left(parser0.out)
    catch {
      case HighLevelParserError.LowLevel(
        parser1,
        LowLevelParserError.TokError(
          _, TokenizerError.InputBufferUnderrun(_)))
      =>
        Right(parser1)
    }



  private def unexpectedParserEvent(highLevelEvent: HighLevelEvent) =
    InputStreamState.LocalError(XmppStreamError.InternalServerError())

}


