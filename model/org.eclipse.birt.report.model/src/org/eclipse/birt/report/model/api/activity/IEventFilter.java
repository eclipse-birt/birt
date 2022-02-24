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
