package com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client

import com.github.rgafiyatullin.creek_xml.dom.{Element, NodeBuilder}
import com.github.rgafiyatullin.creek_xml.stream_parser.high_level_parser.HighLevelParser
import org.scalatest.{FlatSpec, Matchers}

class XmlFromStringHelperSpec extends FlatSpec with Matchers with XmlFromStringHelper {
  "An xml method" should "produce Element" in {
    xml("<a xmlns='test'><b/></a>") should be (Element("test", "a", Seq(), Seq(Element("test", "b", Seq(), Seq()))))
  }
}

trait XmlFromStringHelper {
  protected def xml(string: String) = {
    def composeElement(b: NodeBuilder, p: HighLevelParser): Element = {
      if (b.nodeOption.isDefined)
        b.nodeOption.get.asInstanceOf[Element]
      else {
        val (e, p1) = p.out
        val b1 = b.in(e)
        composeElement(b1, p1)
      }
    }

    val p = HighLevelParser.empty.withoutPosition.in(string)
    composeElement(NodeBuilder.empty, p)
  }
}
