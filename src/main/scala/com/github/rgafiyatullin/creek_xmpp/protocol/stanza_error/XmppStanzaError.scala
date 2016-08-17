package com.github.rgafiyatullin.creek_xmpp.protocol.stanza_error

import com.github.rgafiyatullin.creek_xml.common.{Attribute, QName}
import com.github.rgafiyatullin.creek_xml.dom.{CData, Element}
import com.github.rgafiyatullin.creek_xmpp.protocol.XmppConstants

sealed trait XmppStanzaError extends Throwable {
  def reason: Option[Throwable] = None
  def definedCondition: String
  def errorType: XmppStanzaErrorType
  def text: Option[String] = None

  def withText(t: String): XmppStanzaError
  def withText(t: Option[String]): XmppStanzaError

  override def toString: String =
    "XmppStanzaError(%s): %s".format(definedCondition, reason)

  def toXml: Element = {
    val noText = Element(
      XmppConstants.names.jabber.client.error,
      Seq(
        Attribute.Unprefixed("type", errorType.toString)
      ),
      Seq(
        Element(
          QName(XmppConstants.names.urn.ietf.params.xmlNs.xmppStanzas.ns, definedCondition),
          Seq(), Seq())))
    text match {
      case None => noText
      case Some(t) =>
        val textChild = Element(
          XmppConstants.names.urn.ietf.params.xmlNs.xmppStanzas.text,
          Seq(), Seq(CData(t)))
        noText.setChildren(noText.children ++ Seq(textChild))
    }
  }
}

object XmppStanzaError {

  object conditions {
    val badRequest = "bad-request"
    val conflict = "conflict"
    val featureNotImplemented = "feature-not-implemented"
    val forbidden = "forbidden"
    val gone = "gone"
    val internalServerError = "internal-server-error"
    val itemNotFound = "item-not-found"
    val jidMalformed = "jid-malformed"
    val notAcceptable = "not-acceptable"
    val notAllowed = "not-allowed"
    val notAuthorized = "not-authorized"
    val policyViolation = "policy-violation"
    val recipientUnavailable = "recipient-unavailable"
    val redirect = "redirect"
    val registrationRequired = "registration-required"
    val remoteServerNotFound = "remote-server-not-found"
    val remoteServerTimeout = "remote-server-timeout"
    val resourceConstraint = "resource-constraint"
    val serviceUnavailable = "service-unavailable"
    val subscriptionRequired = "subscription-required"
    val undefinedCondition = "undefined-condition"
    val unexpectedRequest = "unexpected-request"
  }

  val fromDefinedCondition: PartialFunction[String, (Option[Throwable], XmppStanzaErrorType) => XmppStanzaError] = {
    case conditions.badRequest => BadRequest(_, _)
    case conditions.conflict => Conflict(_, _)
    case conditions.featureNotImplemented => FeatureNotImplemented(_, _)
    case conditions.forbidden => Forbidden(_, _)
    case conditions.gone => Gone(_, _)
    case conditions.internalServerError => InternalServerError(_, _)
    case conditions.itemNotFound => ItemNotFound(_, _)
    case conditions.jidMalformed => JidMalformed(_, _)
    case conditions.notAcceptable => NotAcceptable(_, _)
    case conditions.notAllowed => NotAllowed(_, _)
    case conditions.notAuthorized => NotAuthorized(_, _)
    case conditions.policyViolation => PolicyViolation(_, _)
    case conditions.recipientUnavailable => RecipientUnavailable(_, _)
    case conditions.redirect => Redirect(_, _)
    case conditions.registrationRequired => RegistrationRequired(_, _)
    case conditions.remoteServerNotFound => RemoteServerNotFound(_, _)
    case conditions.remoteServerTimeout => RemoteServerTimeout(_, _)
    case conditions.resourceConstraint => ResourceConstraint(_, _)
    case conditions.serviceUnavailable => ServiceUnavailable(_, _)
    case conditions.subscriptionRequired => SubscriptionRequired(_, _)
    case conditions.undefinedCondition => UndefinedCondition(_, _)
    case conditions.unexpectedRequest => UnexpectedRequest(_, _)
  }


  /**
    * Represents stanza level error with defined condition "bad-requiest"
    *
    * The sender has sent a stanza containing XML that does not conform to the appropriate schema or that cannot
    * be processed (e.g., an IQ stanza that includes an unrecognized value of the 'type' attribute, or an element that
    * is qualified by a recognized namespace but that violates the defined syntax for the element);
    * the associated error type SHOULD be "modify".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class BadRequest(
                               override val reason: Option[Throwable] = None,
                               override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Modify,
                               override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.badRequest
    def withText(t: String): BadRequest = withText(Some(t))
    def withText(t: Option[String]): BadRequest = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "conflict"
    *
    * Access cannot be granted because an existing resource exists with the same name or address;
    * the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class Conflict(
                             override val reason: Option[Throwable] = None,
                             override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                             override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.conflict
    def withText(t: String): Conflict = withText(Some(t))
    def withText(t: Option[String]): Conflict = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "feature-not-implemented"
    *
    * The feature represented in the XML stanza is not implemented by the intended recipient or an intermediate server
    * and therefore the stanza cannot be processed (e.g., the entity understands the namespace but does not recognize
    * the element name); the associated error type SHOULD be "cancel" or "modify".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class FeatureNotImplemented(
                                          override val reason: Option[Throwable] = None,
                                          override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                          override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.featureNotImplemented
    def withText(t: String): FeatureNotImplemented = withText(Some(t))
    def withText(t: Option[String]): FeatureNotImplemented = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "forbidden"
    *
    * The requesting entity does not possess the necessary permissions to perform an action that only certain authorized
    * roles or individuals are allowed to complete (i.e., it typically relates to authorization rather than
    * authentication); the associated error type SHOULD be "auth".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class Forbidden(
                              override val reason: Option[Throwable] = None,
                              override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Auth,
                              override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.forbidden
    def withText(t: String): Forbidden = withText(Some(t))
    def withText(t: Option[String]): Forbidden = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "gone"
    *
    * The recipient or server can no longer be contacted at this address, typically on a permanent basis
    * (as opposed to the "redirect" error condition, which is used for temporary addressing failures);
    * the associated error type SHOULD be "cancel" and the error stanza SHOULD include a new address
    * (if available) as the XML character data of the "gone" element (which MUST be a Uniform Resource Identifier [URI]
    * or Internationalized Resource Identifier [IRI] at which the entity can be contacted,
    * typically an XMPP IRI as specified in [XMPP‑URI]).
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class Gone(
                         override val reason: Option[Throwable] = None,
                         override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                         override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.gone
    def withText(t: String): Gone = withText(Some(t))
    def withText(t: Option[String]): Gone = copy(text = t)

  }


  /**
    * Represents stanza level error with defined condition "internal-server-error"
    *
    * The server has experienced a misconfiguration or other internal error that prevents it from processing the stanza;
    * the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class InternalServerError(
                                        override val reason: Option[Throwable] = None,
                                        override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                        override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.internalServerError
    def withText(t: String): InternalServerError = withText(Some(t))
    def withText(t: Option[String]): InternalServerError = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "item-not-found"
    *
    * The addressed JID or item requested cannot be found; the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class ItemNotFound(
                                 override val reason: Option[Throwable] = None,
                                 override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                 override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.itemNotFound
    def withText(t: String): ItemNotFound = withText(Some(t))
    def withText(t: Option[String]): ItemNotFound = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "jid-malformed"
    *
    * The sending entity has provided (e.g., during resource binding) or communicated
    * (e.g., in the 'to' address of a stanza) an XMPP address or aspect thereof that violates the rules defined
    * in [XMPP‑ADDR]; the associated error type SHOULD be "modify".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class JidMalformed(
                                 override val reason: Option[Throwable] = None,
                                 override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Modify,
                                 override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.jidMalformed
    def withText(t: String): JidMalformed = withText(Some(t))
    def withText(t: Option[String]): JidMalformed = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "not-acceptable"
    *
    * The recipient or server understands the request but cannot process it because the request does not meet criteria
    * defined by the recipient or server (e.g., a request to subscribe to information that does not simultaneously
    * include configuration parameters needed by the recipient); the associated error type SHOULD be "modify".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class NotAcceptable(
                                  override val reason: Option[Throwable] = None,
                                  override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Modify,
                                  override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.notAcceptable
    def withText(t: String): NotAcceptable = withText(Some(t))
    def withText(t: Option[String]): NotAcceptable = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "not-allowed"
    *
    * The recipient or server does not allow any entity to perform the action
    * (e.g., sending to entities at a blacklisted domain); the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class NotAllowed(
                               override val reason: Option[Throwable] = None,
                               override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                               override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.notAllowed
    def withText(t: String): NotAllowed = withText(Some(t))
    def withText(t: Option[String]): NotAllowed = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "not-authorized"
    *
    * The sender needs to provide credentials before being allowed to perform the action,
    * or has provided improper credentials (the name "not-authorized", which was borrowed from the "401 Unauthorized"
    * error of [HTTP], might lead the reader to think that this condition relates to authorization,
    * but instead it is typically used in relation to authentication); the associated error type SHOULD be "auth".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class NotAuthorized(
                                  override val reason: Option[Throwable] = None,
                                  override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Auth,
                                  override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.notAuthorized
    def withText(t: String): NotAuthorized = withText(Some(t))
    def withText(t: Option[String]): NotAuthorized = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "policy-violation"
    *
    * The entity has violated some local service policy
    * (e.g., a message contains words that are prohibited by the service) and the server MAY choose to specify the policy
    * in the "text" element or in an application-specific condition element;
    * the associated error type SHOULD be "modify" or "wait" depending on the policy being violated.
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class PolicyViolation(
                                    override val reason: Option[Throwable] = None,
                                    override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Modify,
                                    override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.policyViolation
    def withText(t: String): PolicyViolation = withText(Some(t))
    def withText(t: Option[String]): PolicyViolation = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "recipient-unavailable"
    *
    * The intended recipient is temporarily unavailable, undergoing maintenance, etc.;
    * the associated error type SHOULD be "wait".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class RecipientUnavailable(
                                         override val reason: Option[Throwable] = None,
                                         override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Wait,
                                         override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.recipientUnavailable
    def withText(t: String): RecipientUnavailable = withText(Some(t))
    def withText(t: Option[String]): RecipientUnavailable = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "redirect"
    *
    * The intended recipient is temporarily unavailable, undergoing maintenance, etc.;
    * the associated error type SHOULD be "wait".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class Redirect(
                             override val reason: Option[Throwable] = None,
                             override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Wait,
                             override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.redirect
    def withText(t: String): Redirect = withText(Some(t))
    def withText(t: Option[String]): Redirect = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "registration-required"
    *
    * The requesting entity is not authorized to access the requested service because prior registration is necessary
    * (examples of prior registration include members-only rooms in XMPP multi-user chat [XEP‑0045] and gateways to
    * non-XMPP instant messaging services, which traditionally required registration in order to use the gateway
    * [XEP‑0100]); the associated error type SHOULD be "auth".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class RegistrationRequired(
                                         override val reason: Option[Throwable] = None,
                                         override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Auth,
                                         override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.registrationRequired
    def withText(t: String): RegistrationRequired = withText(Some(t))
    def withText(t: Option[String]): RegistrationRequired = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "remote-server-not-found"
    *
    * A remote server or service specified as part or all of the JID of the intended recipient does not exist or
    * cannot be resolved (e.g., there is no _xmpp-server._tcp DNS SRV record, the A or AAAA fallback resolution fails,
    * or A/AAAA lookups succeed but there is no response on the IANA-registered port 5269);
    * the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class RemoteServerNotFound(
                                         override val reason: Option[Throwable] = None,
                                         override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                         override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.remoteServerNotFound
    def withText(t: String): RemoteServerNotFound = withText(Some(t))
    def withText(t: Option[String]): RemoteServerNotFound = copy(text = t)
  }

  /**
    * Represents stanza level error with defined condition "remote-server-timeout"
    *
    * A remote server or service specified as part or all of the JID of the intended recipient
    * (or needed to fulfill a request) was resolved but communications could not be established within a
    * reasonable amount of time (e.g., an XML stream cannot be established at the resolved IP address and port,
    * or an XML stream can be established but stream negotiation fails because of problems with TLS, SASL,
    * Server Dialback, etc.); the associated error type SHOULD be "wait" (unless the error is of a more permanent
    * nature, e.g., the remote server is found but it cannot be authenticated or it violates security policies).
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class RemoteServerTimeout(
                                        override val reason: Option[Throwable] = None,
                                        override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Wait,
                                        override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.remoteServerTimeout
    def withText(t: String): RemoteServerTimeout = withText(Some(t))
    def withText(t: Option[String]): RemoteServerTimeout = copy(text = t)
  }


  /**
    * Represents stanza level error with defined condition "resource-constraint"
    *
    * The server or recipient is busy or lacks the system resources necessary to service the request;
    * the associated error type SHOULD be "wait".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class ResourceConstraint(
                                       override val reason: Option[Throwable] = None,
                                       override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Wait,
                                       override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.resourceConstraint
    def withText(t: String): ResourceConstraint = withText(Some(t))
    def withText(t: Option[String]): ResourceConstraint = copy(text = t)
  }

  /**
    * Represents stanza level error with defined condition "???"
    *
    * The server or recipient does not currently provide the requested service;
    * the associated error type SHOULD be "cancel".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class ServiceUnavailable(
                                       override val reason: Option[Throwable] = None,
                                       override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                       override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.serviceUnavailable
    def withText(t: String): ServiceUnavailable = withText(Some(t))
    def withText(t: Option[String]): ServiceUnavailable = copy(text = t)
  }

  /**
    * Represents stanza level error with defined condition "subscription-required"
    *
    * The requesting entity is not authorized to access the requested service because a prior subscription is
    * necessary (examples of prior subscription include authorization to receive presence information as defined
    * in [XMPP‑IM] and opt-in data feeds for XMPP publish-subscribe as defined in [XEP‑0060]);
    * the associated error type SHOULD be "auth".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class SubscriptionRequired(
                                         override val reason: Option[Throwable] = None,
                                         override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Auth,
                                         override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.subscriptionRequired
    def withText(t: String): SubscriptionRequired = withText(Some(t))
    def withText(t: Option[String]): SubscriptionRequired = copy(text = t)
  }

  /**
    * Represents stanza level error with defined condition "undefined-condition"
    *
    * The error condition is not one of those defined by the other conditions in this list;
    * any error type can be associated with this condition, and it SHOULD NOT be used except
    * in conjunction with an application-specific condition.
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class UndefinedCondition(
                                       override val reason: Option[Throwable] = None,
                                       override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Cancel,
                                       override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.undefinedCondition
    def withText(t: String): UndefinedCondition = withText(Some(t))
    def withText(t: Option[String]): UndefinedCondition = copy(text = t)
  }

  /**
    * Represents stanza level error with defined condition "unexpected-request"
    *
    * The recipient or server understood the request but was not expecting it at this time
    * (e.g., the request was out of order); the associated error type SHOULD be "wait" or "modify".
    *
    * @param reason    underlying exception
    * @param errorType associated error-type
    */
  final case class UnexpectedRequest(
                                      override val reason: Option[Throwable] = None,
                                      override val errorType: XmppStanzaErrorType = XmppStanzaErrorType.Wait,
                                      override val text: Option[String] = None)
    extends XmppStanzaError {
    def definedCondition = XmppStanzaError.conditions.unexpectedRequest
    def withText(t: String): UnexpectedRequest = withText(Some(t))
    def withText(t: Option[String]): UnexpectedRequest = copy(text = t)
  }

}
