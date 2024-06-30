/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.api;

import java.util.List;

import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;

public class ArgumentInfo implements IArgumentInfo {
	private String name, description;
	private boolean isOptional = false;

	private List<Period_Type> periodType;
	private String displayName;

	public ArgumentInfo(String name, String displayName, String description, boolean isOptional) {
		this.name = name;
		this.displayName = displayName;
		this.description = description;
		this.isOptional = isOptional;
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.IArgumentInfo#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.IArgumentInfo#isOptional()
	 */
	@Override
	public boolean isOptional() {
		return this.isOptional;
	}

	/*
	 * @see org.eclipse.birt.report.data.adapter.api.IArgumentInfo#getDescription()
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/*
	 * @see
	 * org.eclipse.birt.report.data.adapter.api.IArgumentInfo#getPeriodChoices()
	 */
	@Override
	public List<Period_Type> getPeriodChoices() {
		return this.periodType;
	}

	/**
	 * set period choice for arguments
	 *
	 * @param type
	 */
	public void setPeriodChoices(List<Period_Type> type) {
		this.periodType = type;
	}

	/**
	 * get display name for argument
	 */
	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	/**
	 * set display name for argument
	 */
	public void setDisplayname(String displayName) {
		this.displayName = displayName;
	}
}
