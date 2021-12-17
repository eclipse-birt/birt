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

import java.io.Serializable;

import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;

/**
 * RMVariable
 */
public class RMVariable implements VMVariable, Serializable, VMConstants {

	private static final long serialVersionUID = 1L;

	private String name;
	private String typeName;
	private VMValue value;

	public RMVariable(VMValue value, String name, String typeName) {
		this.name = name;
		this.value = value;
		this.typeName = typeName;
	}

	public String getName() {
		return name;
	}

	public VMValue getValue() {
		return value;
	}

	public String getTypeName() {
		return typeName;
	}
}
