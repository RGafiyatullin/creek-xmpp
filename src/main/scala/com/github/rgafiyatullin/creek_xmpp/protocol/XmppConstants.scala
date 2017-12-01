package com.github.rgafiyatullin.creek_xmpp.protocol

import com.github.rgafiyatullin.creek_xml.common.QName

object XmppConstants {
  object names {
    private def qn(ns: String, ln: String): QName = new QName(ns, ln)

    object streams {
      val ns = "http://etherx.jabber.org/streams"

      val stream: QName = qn(ns, "stream")
      val features: QName = qn(ns, "features")
      val error: QName = qn(ns, "error")
    }

    object jabber {
      object client {
        val ns = "jabber:client"

        val error: QName = qn(ns, "error")
        val presence: QName = qn(ns, "presence")
        val iq: QName = qn(ns, "iq")
        val message: QName = qn(ns, "message")
      }
    }

    object urn {
      object ietf {
        object params {
          object xmlNs {
            object xmppStanzas {
              val ns = "urn:ietf:params:xml:ns:xmpp-stanzas"

              val text: QName = qn(ns, "text")
            }
            object xmppStreams {
              val ns = "urn:ietf:params:xml:ns:xmpp-streams"

              val text: QName = qn(ns, "text")
            }
            object xmppBind {
              val ns = "urn:ietf:params:xml:ns:xmpp-bind"

              val bind: QName = qn(ns, "bind")
              val jid: QName = qn(ns, "jid")
            }
          }
        }
      }

      object xmpp {
        object ping {
          val ns = "urn:xmpp:ping"

          val ping: QName = qn(ns, "ping")
        }
      }
    }
  }
}