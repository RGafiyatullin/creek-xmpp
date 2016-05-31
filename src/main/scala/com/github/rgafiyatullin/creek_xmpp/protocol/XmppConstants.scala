package com.github.rgafiyatullin.creek_xmpp.protocol

import com.github.rgafiyatullin.creek_xml.common.QName

object XmppConstants {
  object names {
    private def qn(ns: String, ln: String): QName = new QName(ns, ln)

    object streams {
      val ns = "http://etherx.jabber.org/streams"

      val stream = qn(ns, "stream")
      val features = qn(ns, "features")
      val error = qn(ns, "error")
    }

    object jabberClient {
      val ns = "jabber:client"

      val error = qn(ns, "error")
      val presence = qn(ns, "presence")
      val iq = qn(ns, "iq")
      val message = qn(ns, "message")
    }
  }

}