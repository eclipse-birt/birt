/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor.optimize;

import java.util.HashSet;

import org.eclipse.birt.report.engine.ir.ReportItemDesign;

public class ExecutionPolicy {

	private boolean suppressDuplicate;
	private HashSet policies = new HashSet();

	public ExecutionPolicy() {

	}

	public boolean needExecute(ReportItemDesign design) {
		return policies.contains(design);
	}

	public void setExecute(ReportItemDesign design) {
		policies.add(design);
	}

	public boolean needSuppressDuplicate() {
		return suppressDuplicate;
	}

	public void enableSuppressDuplicate() {
		suppressDuplicate = true;
	}
}
