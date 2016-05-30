package com.github.rgafiyatullin.creek_xmpp.input_stream

import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.{HighLevelEvent, HighLevelParser, HighLevelParserError}
import com.github.rgafiyatullin.creek_xml.stream_parser.low_level_parser.LowLevelParserError
import com.github.rgafiyatullin.creek_xml.stream_parser.tokenizer.TokenizerError
import com.github.rgafiyatullin.creek_xmpp.protocol.stream_error.XmppStreamError

case class InputStream(state: InputStreamState, parser: HighLevelParser) {
  def in(char: Char): InputStream =
    copy(parser = parser.in(char))

  def in(string: String): InputStream =
    copy(parser = parser.in(string))

  def out: (Option[InputStreamEvent], InputStream) = {
    try {
      val (parserEvent, nextParser) = parser.out
      val nextState = state.handleEvent.applyOrElse(parserEvent, unexpectedParserEvent _)
      (nextState.eventOption, copy(state = nextState, parser = nextParser))
    }
    catch {
      case HighLevelParserError.LowLevel(
        nextParser,
        LowLevelParserError.TokError(
          _, TokenizerError.InputBufferUnderrun(_)))
      =>
        (None, copy(parser = nextParser))
    }

  }

  private def unexpectedParserEvent(highLevelEvent: HighLevelEvent) =
    InputStreamState.LocalError(XmppStreamError.InternalServerError())

}

object InputStream {
  def empty: InputStream =
    InputStream(
      InputStreamState.ExpectStreamOpen(None),
      HighLevelParser.empty.withoutPosition)
}

