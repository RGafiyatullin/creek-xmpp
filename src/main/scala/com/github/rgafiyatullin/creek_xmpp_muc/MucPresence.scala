package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom_query.Implicits._
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.Presence
import com.github.rgafiyatullin.creek_xmpp_muc.privileges.{Affiliation, Role}

object MucPresence {
  private object names {
    val qnXM = MucConstants.names.jabberOrg.protocol.muc.x
    val qnXMPassword = MucConstants.names.jabberOrg.protocol.muc.password
    val qnXMU = MucConstants.names.jabberOrg.protocol.mucUser.x
    val qnItem = MucConstants.names.jabberOrg.protocol.mucUser.item
    val qnStatus = MucConstants.names.jabberOrg.protocol.mucUser.status
  }

  private object attrs {
    def code(i: Int): Attribute.Unprefixed = Attribute.Unprefixed("code", i.toString)
  }

  object implicits {
    implicit class ImplicitMucPresence(p: Presence) extends MucPresence(p)
  }
}

class MucPresence(presence: Presence) {
  object muc {
    import MucPresence.attrs
    import MucPresence.names._

    def sanitizeClientRequest: Presence =
      presence.setChildren(
        presence.children.filter(
          MucStanza.isStanzaChildAllowedToClient))

    def addStatusCode(code: Int): Presence =
      presence.setXml(
        presence.xml
          .upsert(qnXMU / (qnStatus and attrs.code(code)))(Some(_)))

    def checkStatusCode(code: Int): Boolean =
      (presence.xml select
        qnXMU / (qnStatus and attrs.code(code))).nonEmpty

    def removeStatusCode(code: Int): Presence =
      presence.setXml(
        presence.xml
          .upsert(qnXMU / (qnStatus and attrs.code(code)))(_ => None))

    def setRole(role: Role): Presence =
      presence.setXml(
        presence.xml
          .upsert(qnXMU / qnItem)(i => Some(i.setAttribute("role", role.toString))))

    def setAffiliation(affiliation: Affiliation): Presence =
      presence.setXml(
        presence.xml
          .upsert(qnXMU / qnItem)(i => Some(i.setAttribute("affiliation", affiliation.toString))))

    def password: Option[String] =
      (presence.xml
        select qnXM / qnXMPassword)
        .headOption
        .map(_.text)
  }
}
