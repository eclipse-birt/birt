/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.activity;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * The base class of the notification hierarchy. Every notification describes
 * one change to one element called the <em>target element</em>.
 * <p>
 * A notification specifies the kind of change by its class. Subclasses provide
 * additional context information appropriate for that specific event.
 * <p>
 * Notifications are routed to listeners though a number of <em>delivery
 * paths</em>. For example, a listener may hear about changes to an element
 * itself, to an ancestor element, to an associated style, and so on. Some
 * listeners may want to react differently depending on the element that
 * actually changed. The {@link #getDeliveryPath getDeliveryPath( )}method lets
 * the listener determine which path this event has taken.
 * <p>
 * Notifications are created in response to a model change. The UI that triggers
 * the change can identify itself as the <em>sender</em> of the event. This
 * allows the UI to ignore events that represent changes that it, itself, made.
 * Using the sender attribute is purely optional, and is for the convenience of
 * each particular bit of UI.
 * <p>
 * There are several constants defined in this class. Each of them is
 * corresponding to a kind of Event. When doing some operations, specified
 * command and record are invoked and the record will eventually send out a kind
 * of NotificationEvent, when calling the {@link #getEventType()}of the event,
 * the return value will be one of the constants defined here.
 *
 */

public abstract class NotificationEvent {

	/**
	 * The event type of ContentEvent.
	 */
	public static final int CONTENT_EVENT = 0;

	/**
	 * The event type of ElementDeletedEvent.
	 */
	public static final int ELEMENT_DELETE_EVENT = 1;

	/**
	 * The event type of ExtendsEvent.
	 */
	public static final int EXTENDS_EVENT = 2;

	/**
	 * The event type of NameEvent.
	 */
	public static final int NAME_EVENT = 3;

	/**
	 * The event type of NameSpaceEvent.
	 *
	 * @deprecated since BIRT 2.1
	 */

	@Deprecated
	public static final int NAME_SPACE_EVENT = 4;

	/**
	 * The event type of PrpertyEvent.
	 */
	public static final int PROPERTY_EVENT = 5;

	/**
	 * The event type of StyleEvent.
	 */
	public static final int STYLE_EVENT = 6;

	/**
	 * The event type of UserPropertyEvent.
	 */
	public static final int USER_PROP_EVENT = 7;

	/**
	 * The event type of CustomMsgEvent.
	 */
	public static final int CUSTOM_MSG_EVENT = 8;

	/**
	 * The event type of PropertyListEvent.
	 */

	public static final int EXTENSION_PROPERTY_DEFINITION_EVENT = 9;

	/**
	 * The event type of ValidationEvent
	 */

	public static final int VALIDATION_EVENT = 10;

	/**
	 * The event type of LibraryEvent
	 */

	public static final int LIBRARY_EVENT = 11;

	/**
	 * The event type of attributeEvent.
	 */

	public static final int ATTRIBUTE_EVENT = 12;

	/**
	 * The event type of DISPOSE_EVENT.
	 */

	public static final int DISPOSE_EVENT = 13;

	/**
	 * The event type of LAYOUT_CHANGED_EVENT.
	 */

	public static final int LAYOUT_CHANGED_EVENT = 14;

	/**
	 * The event type of THEME_EVENT.
	 */

	public static final int THEME_EVENT = 15;

	/**
	 * The event type of CONTENT_REPLACE_EVENT.
	 */

	public static final int CONTENT_REPLACE_EVENT = 16;

	/**
	 * The event type of TEMPLATE_TRANSFORM_EVENT.
	 */

	public static final int TEMPLATE_TRANSFORM_EVENT = 17;

	/**
	 * The event type of ELEMENT_LOCALIZE_EVENT
	 */

	public static final int ELEMENT_LOCALIZE_EVENT = 18;

	/**
	 * The event type of LIBRARY_RELOADED_EVENT
	 */

	public static final int LIBRARY_RELOADED_EVENT = 19;

	/**
	 * The event type of LIBRARY_CHANGE_EVENT
	 */

	public static final int LIBRARY_CHANGE_EVENT = 20;

	/**
	 * The event type of css reload event
	 */

	public static final int CSS_RELOADED_EVENT = 21;

	/**
	 * The event type of css CRUD event
	 */

	public static final int CSS_EVENT = 22;

	/**
	 * The event type of encryption change event.
	 */
	public static final int ENCRYPTION_EVENT = 23;

	/**
	 * The event type of multiple views event.
	 */

	public static final int VIEWS_CONTENT_EVENT = 24;

	/**
	 * The event type of event that the data design is reloaded.
	 */
	public static final int DATA_DESIGN_RELOADED_EVENT = 25;

	/**
	 * The event type of DataMartPropertyEvent.
	 */
	public static final int DATA_MART_PROPERTY_EVENT = 26;

	// List of delivery modes. The modes tell the listener the route
	// by which the event reached that listener.

	/**
	 * Event is being sent to the listeners of the object itself.
	 */

	public static final int DIRECT = 0;

	/**
	 * Event is being sent to the descendants of the object. descendants are those
	 * that extend the target, directly or indirectly.
	 */

	public static final int DESCENDENT = 1;

	/**
	 * Event is being sent to elements that use a style.
	 */

	public static final int STYLE_CLIENT = 2;

	/**
	 * Event is being sent to the contents of the target.
	 */

	public static final int CONTENTS = 3;

	/**
	 * Event is being sent to the elements that use an element.
	 */

	public static final int ELEMENT_CLIENT = 4;

	/**
	 * Event is being sent to the elements that use a structure defined in report
	 * design.
	 */

	public static final int STRUCTURE_CLIENT = 5;

	/**
	 * Event is being sent to the elements that contains the current element.
	 */

	public static final int CONTAINER = 6;

	/**
	 * The design element that changed.
	 */

	protected DesignElement target = null;

	/**
	 * The sender of the event. This is generally the UI that made the change. This
	 * allows the UI to ignore, if it chooses, events that indicate changes that the
	 * that UI made itself.
	 */

	protected Object sender = null;

	/**
	 * The current delivery path for the notification. Updated as the event works
	 * though the delivery system.
	 */

	protected int deliveryPath = DIRECT;

	/**
	 * Default constructor.
	 */

	public NotificationEvent() {
	}

	/**
	 * Convenience constructor that specifies the target element.
	 *
	 * @param obj the target element.
	 */

	public NotificationEvent(DesignElement obj) {
		target = obj;
	}

	/**
	 * Returns the delivery path by which the event was sent to the listener.
	 *
	 * @return the Delivery path. One of:
	 *
	 *         <ul>
	 *         <li>DIRECT</li>
	 *         <li>DESCENDENT</li>
	 *         <li>STYLE_CLIENT</li>
	 *         <li>CONTENTS</li>
	 *         </ul>
	 */

	public int getDeliveryPath() {
		return deliveryPath;
	}

	/**
	 * Sets the delivery path. Called by the sender to indicate the context.
	 *
	 * @param path the delivery path to set.
	 */

	public void setDeliveryPath(int path) {
		deliveryPath = path;
	}

	/**
	 * Returns the sender: the UI or other application object that caused the event
	 * to be sent. The UI component can use this to avoid responding to changes that
	 * the UI component itself caused.
	 *
	 * @return the sender.
	 */

	public Object getSender() {
		return sender;
	}

	/**
	 * Sets the sender based on the information provided to the command.
	 *
	 * @param sender the sender to set.
	 */

	public void setSender(Object sender) {
		this.sender = sender;
	}

	/**
	 * Returns the target element: the part of the design that actually changed.
	 *
	 * @return the target.
	 */

	public DesignElement getTarget() {
		return target;
	}

	/**
	 * Sets the target element.
	 *
	 * @param target the target element to set.
	 */

	public void setTarget(DesignElement target) {
		this.target = target;
	}

	/**
	 * Returns the event type. The following event types are defined:
	 * <ul>
	 * <li>CONTENT_EVENT</li>
	 * <li>ELEMENT_DELETE_EVENT</li>
	 * <li>EXTENDS_EVENT</li>
	 * <li>NAME_EVENT</li>
	 * <li>NAME_SPACE_EVENT</li>
	 * <li>PROPERTY_EVENT</li>
	 * <li>STYLE_EVENT</li>
	 * <li>USER_PROP_EVENT</li>
	 * <li>CUSTOM_MSG_EVENT</li>
	 * <li>EXTENSION_PROPERTY_DEFINITION_EVENT</li>
	 * <li>NOTIFICATION_EVENT</li>
	 * <li>VALIDATION_EVENT</li>
	 * <li>LIBRARY_EVENT</li>
	 * <li>ATTRIBUTE_EVENT</li>
	 * <li>DISPOSE_EVENT</li>
	 * <li>CONTENT_REPLACE_EVENT</li>
	 * <li>TEMPLATE_TRANSFORM_EVENT</li>
	 * </ul>
	 *
	 * @return the event type.
	 */

	public abstract int getEventType();

	/**
	 * Compares and justifies whether this event and the given event is the same.
	 *
	 * @param event the event to compare
	 * @return true if the two events are the same, otherwise false
	 *
	 */

	public boolean isSame(NotificationEvent event) {
		if ((event == null) || event.getEventType() != getEventType() || target != event.getTarget()) {
			return false;
		}
		return true;
	}

}
