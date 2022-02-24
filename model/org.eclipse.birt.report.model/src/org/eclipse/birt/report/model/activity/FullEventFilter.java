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

package org.eclipse.birt.report.model.activity;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.IEventFilter;

/**
 * Provides an event filter. This filter will filter all the notification events
 * and send nothing.
 */

public class FullEventFilter implements IEventFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.activity.IEventFilter#filter(java.util
	 * .List)
	 */

	public List<RecordTask> filter(List<RecordTask> events) {
		return Collections.emptyList();
	}
}
