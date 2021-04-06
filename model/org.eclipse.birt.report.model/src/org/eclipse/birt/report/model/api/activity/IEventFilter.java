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

package org.eclipse.birt.report.model.api.activity;

import java.util.List;

import org.eclipse.birt.report.model.activity.RecordTask;

/**
 * Class provides a event filter logic for a transaction.
 */

public interface IEventFilter {

	/**
	 * Apply filter conditions to the chain of events. The notifications that should
	 * be filtered is flagged as "filtered"
	 * 
	 * @param events the event chain that is to be filtered.
	 * @return Returns an event chain, the notifications that should be filtered is
	 *         flagged as "filtered"
	 */

	public List<RecordTask> filter(List<RecordTask> events);
}
