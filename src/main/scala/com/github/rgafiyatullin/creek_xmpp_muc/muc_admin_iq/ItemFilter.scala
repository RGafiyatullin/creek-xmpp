package com.github.rgafiyatullin.creek_xmpp_muc.muc_admin_iq

import com.github.rgafiyatullin.creek_xmpp_muc.privileges.{Affiliation, Role}

sealed trait ItemFilter
object ItemFilter {
  final case class A(affiliation: Affiliation) extends ItemFilter
  final case class R(role: Role) extends ItemFilter
  final case class AR(affiliation: Affiliation, role: Role) extends ItemFilter
}
