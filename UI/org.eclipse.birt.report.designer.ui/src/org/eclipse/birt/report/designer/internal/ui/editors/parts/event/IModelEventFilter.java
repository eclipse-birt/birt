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
