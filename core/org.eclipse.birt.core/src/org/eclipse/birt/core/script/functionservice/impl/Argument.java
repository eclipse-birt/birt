
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
package org.eclipse.birt.core.script.functionservice.impl;

import org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument;

/**
 * This is an implementation of IScriptFunctionArgument interface.
 */

public class Argument implements IScriptFunctionArgument {
	//
	private String name;
	private String dataType;
	private String desc;
	private boolean isOptional;

	/**
	 * Constructor.
	 *
	 * @param name
	 * @param dataType
	 * @param desc
	 */
	public Argument(String name, String dataType, String desc, boolean isOptional) {
		this.name = name;
		this.dataType = dataType;
		this.desc = desc;
		this.isOptional = isOptional;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.functionservice.IScriptFunctionArgument#
	 * getDataType()
	 */
	@Override
	public String getDataTypeName() {
		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.script.functionservice.INamedObject#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.core.script.functionservice.IDescribable#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.desc;
	}

	@Override
	public boolean isOptional() {
		return this.isOptional;
	}
}
