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
import org.eclipse.core.runtime.IAdaptable;

/**
 * Process the model event, When the model event trans is commit, then post the
 * event, and the processot union and ignore by the filter the event to improve
 * the performance.
 */
public interface IModelEventProcessor extends IAdaptable {

	/**
	 * Collect the event when the model element change.
	 *
	 * @param focus
	 * @param ev
	 */
	void addElementEvent(DesignElementHandle focus, NotificationEvent ev);

	/**
	 * Post the event when the model event trans commit;
	 */
	void postElementEvent();

	/**
	 * Clear the event, when the model event trans roll back;
	 */
	void clear();
}
