package com.github.rgafiyatullin.creek_xmpp_muc

import java.util.UUID

import com.github.rgafiyatullin.creek_xml.common.Attribute
import com.github.rgafiyatullin.creek_xml.dom.{CData, Element, Node}
import com.github.rgafiyatullin.creek_xml.dom_query.Implicits._
import com.github.rgafiyatullin.creek_xml.dom_query.Predicate
import com.github.rgafiyatullin.creek_xmpp.protocol.jid.Jid
import com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error.XmppStanzaError
import com.github.rgafiyatullin.creek_xmpp.protocol.stanzas.jabber_client.Message

object MucMessageMediatedInvitation {
  object implicits {
    implicit class MessageWithMediatedInvitation(m: Message) extends MucMessageMediatedInvitation(m)
  }

  val xmu = MucConstants.names.jabberOrg.protocol.mucUser

  final case class Invite(room: Jid, from: Jid, to: Jid, reason: Option[String], customChildren: Seq[Node]) {
    def errorOption: Option[XmppStanzaError] =
      if (!room.isBare || room.isHost)
        Some(XmppStanzaError.BadRequest().withText("Invalid room-jid"))
      else
        None

    def toMessageFromRoom: Message =
      Message.create(Message.Chat)
        .setId(UUID.randomUUID().toString)
        .setFrom(room)
        .setTo(to)
        .setChildren(Seq(
          Element(xmu.x, Seq(), Seq(
            Element(xmu.invite, Seq(
                Attribute.Unprefixed("from", from.toString)
              ), reason.map(CData).toSeq ++ customChildren)
          ))
        ))
  }

  final case class Decline(room: Jid, from: Jid, to: Jid, reason: Option[String], customChildren: Seq[Node]) {
    def errorOption: Option[XmppStanzaError] =
      if (!room.isBare || room.isHost)
        Some(XmppStanzaError.BadRequest().withText("Invalid room-jid"))
      else
        None

    def toMessageFromRoom: Message =
      Message.create(Message.Chat)
        .setId(UUID.randomUUID().toString)
        .setFrom(room)
        .setTo(to)
        .setChildren(Seq(
          Element(xmu.x, Seq(), Seq(
            Element(xmu.decline, Seq(
              Attribute.Unprefixed("from", from.toString)
            ), reason.map(CData).toSeq ++ customChildren)
          ))
        ))
  }

}

class MucMessageMediatedInvitation(message: Message) {
  import MucMessageMediatedInvitation.{Decline, Invite, xmu}

  object mucMediatedInvitation {
    def inviteNodeOption: Option[Node] =
      (message.xml select (xmu.x / xmu.invite)).headOption

    def declineNodeOption: Option[Node] =
      (message.xml select (xmu.x / xmu.decline)).headOption

    def inviteReasonOption: Option[String] =
      (message.xml select (xmu.x / xmu.invite / xmu.reason)).headOption.map(_.text)

    def declineReasonOption: Option[String] =
      (message.xml select (xmu.x / xmu.decline / xmu.reason)).headOption.map(_.text)

    def inviteCustomChildren: Seq[Node] =
      message.xml select (xmu.x / xmu.invite / Predicate.Neg(xmu.reason))

    def declineCustomChildren: Seq[Node] =
      message.xml select (xmu.x / xmu.decline / Predicate.Neg(xmu.reason))

    def clientInviteOption: Option[Invite] =
      for {
        inviteNode <- inviteNodeOption
        roomJid <- message.to
        fromJid <- message.from
        toJid <- inviteNode.attribute("to").map(Jid.parse)
      }
        yield Invite(
          roomJid, fromJid.toBare, toJid.toBare,
          inviteReasonOption, inviteCustomChildren)

    def clientDeclineOption: Option[Decline] =
      for {
        declineNode <- declineNodeOption
        roomJid <- message.to
        fromJid <- message.from
        toJid <- declineNode.attribute("to").map(Jid.parse)
      }
        yield Decline(
          roomJid, fromJid.toBare, toJid.toBare,
          declineReasonOption, inviteCustomChildren)

    def roomInviteOption: Option[Invite] =
      for {
        inviteNode <- inviteNodeOption
        roomJid <- message.from
        toJid <- message.to
        fromJid <- inviteNode.attribute("from").map(Jid.parse)
      }
        yield Invite(
          roomJid, fromJid.toBare, toJid.toBare,
          inviteReasonOption, inviteCustomChildren)

    def roomDeclineOption: Option[Decline] =
      for {
        declineNode <- declineNodeOption
        roomJid <- message.from
        toJid <- message.to
        fromJid <- declineNode.attribute("from").map(Jid.parse)
      }
        yield Decline(
          roomJid, fromJid.toBare, toJid.toBare,
          declineReasonOption, inviteCustomChildren)
  }
}
