/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.activity;

import java.util.Stack;

/**
 * The action done after the execution of records.
 * 
 */

public abstract class RecordTask {

	/**
	 * The design element or <code>ReferencableStructure</code> that the task focus
	 * on.
	 */

	private Object target;

	/**
	 * Constructor.
	 * 
	 * @param target the target design element
	 */

	RecordTask(Object target) {
		this.target = target;
	}

	/**
	 * Returns the target: the part of the design that actually changed. Can be
	 * design element or <code>ReferencableStructure</code>.
	 * 
	 * @return the target.
	 */

	public Object getTarget() {
		return target;
	}

	/**
	 * Performs the task after the execution of <code>record</code> with the
	 * transaction stack status.
	 * 
	 * @param record     the record executed
	 * @param transStack the current transaction stack
	 */

	public abstract void doTask(ActivityRecord record, Stack<CompoundRecord> transStack);
}
