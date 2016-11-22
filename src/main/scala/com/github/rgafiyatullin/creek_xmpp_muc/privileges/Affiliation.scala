package com.github.rgafiyatullin.creek_xmpp_muc.privileges

sealed trait Affiliation extends Ordered[Affiliation] {
  def toOrderingInt: Int

  override def compare(that: Affiliation): Int =
    toOrderingInt.compare(that.toOrderingInt)
}

object Affiliation {
  case object Owner extends Affiliation {
    override val toString: String = "owner"
    override val toOrderingInt: Int = 3
  }
  case object Admin extends Affiliation {
    override val toString: String = "admin"
    override val toOrderingInt: Int = 2
  }
  case object Member extends Affiliation {
    override val toString: String = "member"
    override val toOrderingInt: Int = 1
  }
  case object None extends Affiliation {
    override val toString: String = "none"
    override val toOrderingInt: Int = 0
  }
  case object Outcast extends Affiliation {
    override val toString: String = "outcast"
    override val toOrderingInt: Int = -1
  }

  def fromString: PartialFunction[String, Affiliation] = {
    case Owner.toString => Owner
    case Admin.toString => Admin
    case Member.toString => Member
    case None.toString => None
    case Outcast.toString => Outcast
  }
}
