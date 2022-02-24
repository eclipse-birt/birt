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

package org.eclipse.birt.report.designer.internal.ui.editors.parts.event;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * Filter the event type
 */

public interface IModelEventFilter {
	/**
	 * Filter the event type
	 *
	 * @param focus
	 * @param ev
	 * @return
	 */
	// Now model fire some event and the is not use for GUI, foe example the
	// Name_Sapce event, Table_layout Event,
	// Ther are the inner event in the model
	boolean filterModelEvent(DesignElementHandle focus, NotificationEvent ev);
}
