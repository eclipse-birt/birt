/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * Receives events about a Design Element. Any one listener can listen to any
 * number of design elements. A listener follows the adapter pattern: it adapts
 * a "client" object (usually a UI object) to receive notifications from the
 * "focus" object (the one from which to receive events.)
 * <p>
 * The typical life cycle is:
 * <p>
 * <ol>
 * <li>Create the listener.</li>
 * <li>Register interest in one or more focus design elements.</li>
 * <li>Receive events on those objects. Use the information in the notification
 * event to decide what action to take, if any.</li>
 * <li>When destroying the client object, deregister the listener from each of
 * design element registered above. (This step is vital for to prevent memory
 * leaks and performance degredation from notifying unused listeners.)</li>
 * </ol>
 * <p>
 * The application uses this interface in one of two ways. First, the client
 * object can simply implement the listener interface. Second, it can create a
 * "helper object" that implements the interface.
 * <p>
 * In either case, the class must override the notify( ) method. Look at the
 * type of the event to find those of interest. Any one client generally cares
 * about a specific subset of events. Then, look at the sender, target or other
 * members to decide what to do with the particular event. Finally, perform the
 * client-specific action.
 * 
 */

public interface Listener {
	/**
	 * Notifies the listener about a Design Engine event.
	 * 
	 * @param focus The design element that has changed.
	 * @param ev    The notification event that describes the change.
	 */

	void elementChanged(DesignElementHandle focus, NotificationEvent ev);
}
