package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.common.QName

object MucConstants {
  private def qn(ns: String, ln: String): QName = QName(ns, ln)

  object mucStatus {
    val configChangedNonAnonymous = 100
    val affiliationChanged = 101
    val configChangedShowUnavailableUsers = 102
    val configChangedHideUnavailableUsers = 103
    val configChangedGeneric = 104

    val self = 110
    val configChangedLoggingEnabled = 170
    val configChangedLoggingDisabled = 171
    val configChangedSemiAnonymous = 173

    val created = 201
    val nicknameLockdown = 210
    val banned = 301
    val seeOtherNickname = 303
    val kicked = 307
    val removedDueToAffiliationChange = 321
    val removedDueToRoomConfigChange = 322
    val removedDueToShutdown = 332
  }

  object names {
    object wargamingNet {
      object xmpp {
        object filterMucRoomsDisco {
          val ns = "http://wargaming.net/xmpp#filter-muc-rooms-disco"

          val filter = qn(ns, "filter")
          val criterion = qn(ns, "criterion")
          val fuzziness = qn(ns, "fuzziness")
        }
      }
    }

    object jabber {
      object x {
        object data {
          val ns = "jabber:x:data"

          val x = qn(ns, "x")
          val title = qn(ns, "title")
          val instruction = qn(ns, "instruction")
          val field = qn(ns, "field")
          val value = qn(ns, "value")
          val option = qn(ns, "option")
        }
      }
    }

    object jabberOrg {
      object protocol {
        object discoInfo {
          val ns = "http://jabber.org/protocol/disco#info"
          val query = qn(ns, "query")
          val identity = qn(ns, "identity")
          val feature = qn(ns, "feature")
        }

        object discoItems {
          val ns = "http://jabber.org/protocol/disco#items"
          val query = qn(ns, "query")
          val item = qn(ns, "item")
        }


        object muc {
          val ns = "http://jabber.org/protocol/muc"

          val x = qn(ns, "x")
          val history = qn(ns, "history")
          val password = qn(ns, "password")
        }

        object mucUser {
          val ns = "http://jabber.org/protocol/muc#user"

          val x = qn(ns, "x")
          val decline = qn(ns, "decline")
          val destroy = qn(ns, "destroy")
          val invite = qn(ns, "invite")
          val item = qn(ns, "item")
          val actor = qn(ns, "actor")
          val continue = qn(ns, "continue")
          val status = qn(ns, "status")
          val reason = qn(ns, "reason")

          val unsubscribe = qn(ns, "unsubscribe")
        }

        object mucAdmin {
          val ns = "http://jabber.org/protocol/muc#admin"

          val query = qn(ns, "query")
          val item = qn(ns, "item")
          val actor = qn(ns, "actor")
          val reason = qn(ns, "reason")
        }

        object mucOwner {
          val ns = "http://jabber.org/protocol/muc#owner"

          val query = qn(ns, "query")
          val destroy = qn(ns, "destroy")
        }
      }
    }
  }
}
