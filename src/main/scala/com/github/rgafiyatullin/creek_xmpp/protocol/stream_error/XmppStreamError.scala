package com.github.rgafiyatullin.creek_xmpp.protocol.stream_error

sealed trait XmppStreamError extends Throwable {
  def reason: Option[Throwable] = None
  def definedCondition: String

  override def toString: String =
    "XmppStreamError(%s): %s".format(definedCondition, reason)
}

object XmppStreamError {

  object conditions {
    val badFormat = "bad-format"
    val badNamespacePrefix = "bad-namespace-prefix"
    val conflict = "conflict"
    val connectionTimeout = "connection-timeout"
    val hostGone = "host-gone"
    val hostUnknown = "host-unknown"
    val improperAddressing = "improper-addressing"
    val internalServerError = "internal-server-error"
    val invalidFrom = "invalid-from"
    val invalidNamespace = "invalid-namespace"
    val invalidXml = "invalid-xml"
    val notAuthorized = "not-authorized"
    val notWellFormed = "not-well-formed"
    val policyViolation = "policy-violation"
    val remoteConnectionFailed = "remote-connection-failed"
    val reset = "reset"
    val resourceConstraint = "resource-constraint"
    val restrictedXml = "restricted-xml"
    val seeOtherHost = "see-other-host"
    val systemShutdown = "system-shutdown"
    val undefinedCondition = "undefined-condition"
    val unsupportedEncoding = "unsupported-encoding"
    val unsupportedFeature = "unsupported-feature"
    val unsupportedStanzaType = "unsupported-stanza-type"
    val unsupportedVersion = "unsupported-version"
  }

  val fromDefinedCondition: PartialFunction[String, (Option[Throwable]) => XmppStreamError] = {
    case conditions.badFormat => BadFormat(_)
    case conditions.badNamespacePrefix => BadNamespacePrefix(_)
    case conditions.conflict => Conflict(_)
    case conditions.connectionTimeout => ConnectionTimeout(_)
    case conditions.hostGone => HostGone(_)
    case conditions.hostUnknown => HostUnknown(_)
    case conditions.improperAddressing => ImproperAddressing(_)
    case conditions.internalServerError => InternalServerError(_)
    case conditions.invalidFrom => InvalidFrom(_)
    case conditions.invalidNamespace => InvalidNamespace(_)
    case conditions.invalidXml => InvalidXml(_)
    case conditions.notAuthorized => NotAuthorized(_)
    case conditions.notWellFormed => NotWellFormed(_)
    case conditions.policyViolation => PolicyViolation(_)
    case conditions.remoteConnectionFailed => RemoteConnectionFailed(_)
    case conditions.reset => Reset(_)
    case conditions.resourceConstraint => ResourceConstraint(_)
    case conditions.restrictedXml => RestrictedXml(_)
    case conditions.seeOtherHost => SeeOtherHost("", _: Option[Throwable]) // FIXME: should we do something with that?
    case conditions.systemShutdown => SystemShutdown(_)
    case conditions.undefinedCondition => UndefinedCondition(_)
    case conditions.unsupportedEncoding => UnsupportedEncoding(_)
    case conditions.unsupportedFeature => UnsupportedFeature(_)
    case conditions.unsupportedStanzaType => UnsupportedStanzaType(_)
    case conditions.unsupportedVersion => UnsupportedVersion(_)
  }


  /**
    * RFC-6120 4.9.3.  Defined Conditions
    */


  /**
    * Represents stream level error with condition "bad-format".
    *
    * The entity has sent XML that cannot be processed.
    *
    * @param reason underlying exception
    */
  final case class BadFormat(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.badFormat
  }

  /**
    * Represents stream level error with condition "bad-namespace-prefix"
    *
    * The entity has sent a namespace prefix that is unsupported,
    * or has sent no namespace prefix on an element that needs such a prefix
    *
    * @param reason underlying exception
    */
  final case class BadNamespacePrefix(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.badNamespacePrefix
  }

  /**
    * Represents stream level error with condition "bad-namespace-prefix"
    *
    * The server either (1) is closing the existing stream for this entity because a new stream has been initiated that conflicts with the existing stream,
    * or (2) is refusing a new stream for this entity because allowing the new stream would conflict with an existing stream
    * (e.g., because the server allows only a certain number of connections from the same IP address or allows only one server-to-server stream for a given domain pair as a way
    * of helping to ensure in-order processing as described under Section 10.1).
    *
    * @param reason underlying exception
    */
  final case class Conflict(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.conflict
  }


  /**
    * Represents stream level error with condition "connection-timeout"
    *
    * One party is closing the stream because it has reason to believe that the other party has permanently lost the ability to communicate over the stream.
    * The lack of ability to communicate can be discovered using various methods, such as whitespace keepalives as specified under Section 4.4,
    * XMPP-level pings as defined in [XEP‑0199], and XMPP Stream Management as defined in [XEP‑0198].
    *
    * @param reason underlying exception
    */
  final case class ConnectionTimeout(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.connectionTimeout
  }

  /**
    * Represents stream level error with condition "host-gone"
    *
    * The value of the 'to' attribute provided in the initial stream header corresponds to an FQDN
    * that is no longer serviced by the receiving entity.
    *
    * @param reason underlying exception
    */
  final case class HostGone(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.hostGone
  }

  /**
    * Represents stream level error with condition "host-unknown"
    *
    * The value of the 'to' attribute provided in the initial stream header does not correspond to an FQDN
    * that is serviced by the receiving entity.
    *
    * @param reason underlying exception
    */
  final case class HostUnknown(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.hostUnknown
  }

  /**
    * Represents stream level error with condition "improper-addressing"
    *
    * A stanza sent between two servers lacks a 'to' or 'from' attribute, the 'from' or 'to' attribute has no value,
    * or the value violates the rules for XMPP addresses [XMPP‑ADDR].
    *
    * @param reason underlying exception
    */
  final case class ImproperAddressing(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.improperAddressing
  }

  /**
    * Represents stream level error with condition "internal-server-error"
    *
    * The server has experienced a misconfiguration or other internal error that prevents it from servicing the stream.
    *
    * @param reason underlying exception
    */
  final case class InternalServerError(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.internalServerError
  }

  /**
    * Represents stream level error with condition "invalid-from"
    *
    * The data provided in a 'from' attribute does not match an authorized JID or validated domain as negotiated (1)
    * between two servers using SASL or Server Dialback, or (2) between a client and a server via
    * SASL authentication and resource binding.
    *
    * @param reason underlying exception
    */
  final case class InvalidFrom(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.invalidFrom
  }

  /**
    * Represents stream level error with condition "invalid-namespace"
    *
    * The stream namespace name is something other than "http://etherx.jabber.org/streams" (see Section 11.2) or
    * the content namespace declared as the default namespace is not supported
    * (e.g., something other than "jabber:client" or "jabber:server").
    *
    * @param reason underlying exception
    */
  final case class InvalidNamespace(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.invalidNamespace
  }

  /**
    * Represents stream level error with condition "invalid-xml"
    *
    * The entity has sent invalid XML over the stream to a server that performs validation
    *
    * @param reason underlying exception
    */
  final case class InvalidXml(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.invalidXml
  }

  /**
    * Represents stream level error with condition "not-authorized"
    *
    * The entity has attempted to send XML stanzas or other outbound data before the stream has been authenticated,
    * or otherwise is not authorized to perform an action related to stream negotiation; the receiving entity
    * MUST NOT process the offending data before sending the stream error.
    *
    * @param reason underlying exception
    */
  final case class NotAuthorized(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.notAuthorized
  }

  /**
    * Represents stream level error with condition "not-well-formed"
    *
    * The initiating entity has sent XML that violates the well-formedness rules of [XML] or [XML‑NAMES].
    *
    * @param reason underlying exception
    */
  final case class NotWellFormed(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.notWellFormed
  }

  /**
    * Represents stream level error with condition "policy-violation"
    *
    * The entity has violated some local service policy (e.g., a stanza exceeds a configured size limit);
    * the server MAY choose to specify the policy in the <text/> element or in an application-specific condition element.
    *
    * @param reason underlying exception
    */
  final case class PolicyViolation(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.policyViolation
  }

  /**
    * Represents stream level error with condition "remote-connection-failed"
    *
    * The server is unable to properly connect to a remote entity that is needed for authentication or authorization
    * (e.g., in certain scenarios related to Server Dialback [XEP‑0220]); this condition is not to be used when the
    * cause of the error is within the administrative domain of the XMPP service provider, in which case the
    * "internal-server-error" condition is more appropriate.
    *
    * @param reason underlying exception
    */
  final case class RemoteConnectionFailed(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.remoteConnectionFailed
  }

  /**
    * Represents stream level error with condition "reset"
    *
    * The server is closing the stream because it has new (typically security-critical) features to offer,
    * because the keys or certificates used to establish a secure context for the stream have expired or have been
    * revoked during the life of the stream (Section 13.7.2.3),
    * because the TLS sequence number has wrapped (Section 5.3.5), etc.
    * The reset applies to the stream and to any security context established for that stream (e.g., via TLS and SASL),
    * which means that encryption and authentication need to be negotiated again for the new stream
    * (e.g., TLS session resumption cannot be used).
    *
    * @param reason underlying exception
    */
  final case class Reset(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.reset
  }

  /**
    * Represents stream level error with condition "resource-constraint"
    *
    * The server lacks the system resources necessary to service the stream.
    *
    * @param reason underlying exception
    */
  final case class ResourceConstraint(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.resourceConstraint
  }

  /**
    * Represents stream level error with condition "restricted-xml"
    *
    * The entity has attempted to send restricted XML features such as a comment,
    * processing instruction, DTD subset, or XML entity reference (see Section 11.1).
    *
    * @param reason underlying exception
    */
  final case class RestrictedXml(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.restrictedXml
  }

  /**
    * Represents stream level error with condition "see-other-host"
    *
    * The server will not provide service to the initiating entity but is redirecting traffic to another host under
    * the administrative control of the same service provider.
    * The XML character data of the "see-other-host" element returned by the server MUST specify the alternate
    * FQDN or IP address at which to connect, which MUST be a valid domainpart or a domainpart plus port number
    * (separated by the ':' character in the form "domainpart:port").
    * If the domainpart is the same as the source domain, derived domain, or resolved IPv4 or IPv6 address to which the
    * initiating entity originally connected (differing only by the port number), then the initiating entity SHOULD
    * simply attempt to reconnect at that address.
    * (The format of an IPv6 address MUST follow [IPv6‑ADDR], which includes the enclosing the IPv6 address in
    * square brackets '[' and ']' as originally defined by [URI].) Otherwise, the initiating entity MUST resolve
    * the FQDN specified in the "see-other-host" element as described under Section 3.2.
    *
    * @param otherHost the host to proceed to
    * @param reason    underlying exception
    */
  final case class SeeOtherHost(otherHost: String, override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.seeOtherHost
  }

  /**
    * Represents stream level error with condition "system-shutdown"
    *
    * The server is being shut down and all active streams are being closed.
    *
    * @param reason underlying exception
    */
  final case class SystemShutdown(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.systemShutdown
  }

  /**
    * Represents stream level error with condition "undefined-condition"
    *
    * The error condition is not one of those defined by the other conditions in this list;
    * this error condition SHOULD NOT be used except in conjunction with an application-specific condition.
    *
    * @param reason underlying exception
    */
  final case class UndefinedCondition(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.undefinedCondition
  }

  /**
    * Represents stream level error with condition "unsupported-encoding"
    *
    * The initiating entity has encoded the stream in an encoding that is not supported by the server (see Section 11.6)
    * or has otherwise improperly encoded the stream (e.g., by violating the rules of the [UTF‑8] encoding).
    *
    * @param reason underlying exception
    */
  final case class UnsupportedEncoding(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.unsupportedEncoding
  }

  /**
    * Represents stream level error with condition "unsupported-feature"
    *
    * The receiving entity has advertised a mandatory-to-negotiate stream feature that the initiating entity does
    * not support, and has offered no other mandatory-to-negotiate feature alongside the unsupported feature.
    *
    * @param reason underlying exception
    */
  final case class UnsupportedFeature(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.unsupportedFeature
  }

  /**
    * Represents stream level error with condition "unsupported-stanza-type"
    *
    * The initiating entity has sent a first-level child of the stream that is not supported by the server,
    * either because the receiving entity does not understand the namespace or because the receiving entity
    * does not understand the element name for the applicable namespace
    * (which might be the content namespace declared as the default namespace).
    *
    * @param reason underlying exception
    */
  final case class UnsupportedStanzaType(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.unsupportedStanzaType
  }

  /**
    * Represents stream level error with condition "unsupported-version"
    *
    * The 'version' attribute provided by the initiating entity in the stream header specifies
    * a version of XMPP that is not supported by the server.
    *
    * @param reason underlying exception
    */
  final case class UnsupportedVersion(override val reason: Option[Throwable] = None) extends XmppStreamError {
    override def definedCondition: String = XmppStreamError.conditions.unsupportedVersion
  }

}
