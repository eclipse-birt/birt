/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
