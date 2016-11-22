package com.github.rgafiyatullin.creek_xmpp_muc.muc_admin_iq

import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp_muc.privileges.{Affiliation, Role}

final case class Item(
  jid: Option[Jid],
  nick: Option[String],
  affiliation: Option[Affiliation],
  role: Option[Role])
{
  def isAffiliation: Boolean = jid.isDefined && affiliation.isDefined && nick.isEmpty && role.isEmpty
  def isRole: Boolean = nick.isDefined && role.isDefined && jid.isEmpty && affiliation.isEmpty
}