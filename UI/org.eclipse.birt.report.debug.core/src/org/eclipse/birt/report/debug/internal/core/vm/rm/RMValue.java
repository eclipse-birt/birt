/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  Others: See git history
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm.rm;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMException;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;

/**
 * RMValue
 */
public class RMValue implements VMValue, Serializable, VMConstants {

	private static final long serialVersionUID = 1L;

	private long rid;
	private String valueString, typeName;
	private VMVariable[] members;

	private transient RMClient vm;

	public RMValue(long id, String valueString, String typeName, VMVariable[] members) {
		this.rid = id;
		this.valueString = valueString;
		this.typeName = typeName;
		this.members = members;
	}

	public void attach(RMClient vm) {
		this.vm = vm;
	}

	public VMVariable[] getLocalMembers() {
		return members;
	}

	public VMVariable[] getMembers() {
		if (members == null) {
			if (vm != null) {
				try {
					members = vm.getMembers(rid);

					hookVM(members);

					return members;
				} catch (VMException e) {
					StringWriter sw = new StringWriter();
					e.printStackTrace(new PrintWriter(sw));

					members = new VMVariable[] { new RMVariable(
							new RMValue(-1, sw.toString(), e.getClass().getName(), NO_CHILD), ERROR_LITERAL, "null") //$NON-NLS-1$
					};

					return members;
				}
			}

			members = NO_CHILD;
		}

		hookVM(members);

		return members;
	}

	private void hookVM(VMVariable[] vars) {
		if (vars instanceof RMVariable[]) {
			RMVariable[] rvars = (RMVariable[]) vars;

			for (int i = 0; i < rvars.length; i++) {
				RMValue val = (RMValue) vars[i].getValue();

				if (val != null) {
					val.attach(vm);

					hookVM(val.getLocalMembers());
				}
			}
		}
	}

	public String getTypeName() {
		return typeName;
	}

	public String getValueString() {
		return valueString;
	}
}
