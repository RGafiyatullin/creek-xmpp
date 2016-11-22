package com.github.rgafiyatullin.creek_xmpp_muc

import com.github.rgafiyatullin.creek_xml.dom.Node

object MucStanza {
  def isStanzaChildAllowedToClient: Function[Node, Boolean] = {
//    case e if e.qName.ns == MucConstants.names.jabberOrg.protocol.muc.ns => false
    case e if e.qName.ns == MucConstants.names.jabberOrg.protocol.mucUser.ns => false
    case e if e.qName.ns == MucConstants.names.jabberOrg.protocol.mucAdmin.ns => false
    case e if e.qName.ns == MucConstants.names.jabberOrg.protocol.mucOwner.ns => false
    case _ => true
  }

}
