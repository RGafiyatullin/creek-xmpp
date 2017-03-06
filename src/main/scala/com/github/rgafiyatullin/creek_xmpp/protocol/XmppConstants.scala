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

    object jabber {
      object client {
        val ns = "jabber:client"

        val error = qn(ns, "error")
        val presence = qn(ns, "presence")
        val iq = qn(ns, "iq")
        val message = qn(ns, "message")
      }
    }

    object urn {
      object ietf {
        object params {
          object xmlNs {
            object xmppStanzas {
              val ns = "urn:ietf:params:xml:ns:xmpp-stanzas"

              val text = qn(ns, "text")
            }
            object xmppStreams {
              val ns = "urn:ietf:params:xml:ns:xmpp-streams"

              val text = qn(ns, "text")
            }
          }
        }
      }

      object xmpp {
        object ping {
          val ns = "urn:xmpp:ping"

          val ping = qn(ns, "ping")
        }
      }
    }
  }
}