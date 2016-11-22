package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.dom_query.Implicits._
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error.XmppStanzaError
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.IQ
import com.github.rgafiyatullin.creek_xmpp_muc.muc_admin_iq.{Item, ItemFilter}
import com.github.rgafiyatullin.creek_xmpp_muc.privileges.{Affiliation, Role}

object MucAdminIQ {

  sealed trait Request
  object Request {
    final case class SetAffiliations(affiliations: Map[Jid, Affiliation]) extends Request
    final case class SetRoles(roles: Map[String, Role]) extends Request
    final case class GetPrivileges(filters: Seq[ItemFilter]) extends Request
  }

  object implicits {
    implicit class IQWithMucAdminIQ(iq: IQ) extends MucAdminIQ(iq)
  }
}

class MucAdminIQ(iq: IQ) {
  object mucAdmin {
    private val nsMucAdmin = MucConstants.names.jabberOrg.protocol.mucAdmin

    lazy val items: Seq[Item] =
      for {itemNode <- iq.xml select nsMucAdmin.query / nsMucAdmin.item}
        yield Item(
          jid = itemNode.attribute("jid").map(Jid.parse),
          nick = itemNode.attribute("nick"),
          affiliation = itemNode.attribute("affiliation").map(Affiliation.fromString),
          role = itemNode.attribute("role").map(Role.fromString))

    lazy val itemFilter: Seq[ItemFilter] =
      if (items.isEmpty)
        Seq()
      else
        items.map {
          case Item(_, _, Some(a), Some(r)) => ItemFilter.AR(a, r)
          case Item(_, _, None, Some(r)) => ItemFilter.R(r)
          case Item(_, _, Some(a), None) => ItemFilter.A(a)
          case _ =>
            throw XmppStanzaError
              .BadRequest()
              .withText("filter item-elements should contain either affiliation- or role- or both attributes")
        }

    lazy val setAffiliations: Map[Jid, Affiliation] =
      items.collect {
        case Item(Some(jid), None, Some(affiliation), None) if jid.isBare && !jid.isHost =>
          (jid, affiliation)
      }.toMap

    lazy val setRoles: Map[String, Role] =
      items.collect {
        case Item(None, Some(nick), None, Some(role)) => (nick, role)
      }.toMap

    lazy val request: Option[MucAdminIQ.Request] =
      iq.stanzaTypeOption match {
        case Some(IQ.Get) =>
          Some(MucAdminIQ.Request.GetPrivileges(itemFilter))

        case Some(IQ.Set) =>
          if (items.headOption.exists(_.isAffiliation))
            Some(MucAdminIQ.Request.SetAffiliations(setAffiliations))
          else if (items.headOption.exists(_.isRole))
            Some(MucAdminIQ.Request.SetRoles(setRoles))
          else
            throw XmppStanzaError
              .BadRequest()
              .withText("request of type 'set' should either change affiliations or change roles")

        case _ =>
          None
      }
  }
}
