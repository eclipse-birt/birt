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
