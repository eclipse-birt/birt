/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/
package org.eclipse.birt.report.model.api.activity;

/**
 * The interface receives events about activity stack changes. The listener can
 * listen to the activity stack. A listener follows the adapter pattern: it
 * adapts a "client" object (usually a UI object) to receive notifications from
 * activity stack (the one from which to receive events.)
 *
 */

public interface ActivityStackListener {
	/**
	 * Invoked when stack size changed, which is caused after the completion of each
	 * execution, transaction, undo, or redo.
	 *
	 * @param event the activity stack event
	 */

	void stackChanged(ActivityStackEvent event);

}
