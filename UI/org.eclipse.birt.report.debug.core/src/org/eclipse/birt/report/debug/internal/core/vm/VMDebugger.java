/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm;

/**
 * VMDebugger
 */
public abstract class VMDebugger {

	protected ReportVM vm;

	protected VMDebugger(ReportVM vm) {
		this.vm = vm;
	}

	protected int vmState() {
		return vm.currentState();
	}

	protected void vmInterrupt(VMContextData contextData, int interruptState) {
		vm.interrupt(contextData, interruptState);
	}
}
