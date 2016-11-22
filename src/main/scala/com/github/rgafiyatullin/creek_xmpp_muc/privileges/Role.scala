package com.github.rgafiyatullin.creek_xmpp_muc.privileges

sealed trait Role extends Ordered[Role] {
  def toOrderingInt: Int

  override def compare(that: Role): Int =
    toOrderingInt.compare(that.toOrderingInt)
}

object Role {
  case object Moderator extends Role {
    override val toString: String = "moderator"
    override val toOrderingInt: Int = 3
  }
  case object Participant extends Role {
    override val toString: String = "participant"
    override val toOrderingInt: Int = 2
  }
  case object Visitor extends Role {
    override val toString: String = "visitor"
    override val toOrderingInt: Int = 1
  }
  case object None extends Role {
    override val toString: String = "none"
    override val toOrderingInt: Int = 0
  }

  def fromString: PartialFunction[String, Role] = {
    case Moderator.toString => Moderator
    case Participant.toString => Participant
    case Visitor.toString => Visitor
    case None.toString => None
  }
}
