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

package org.eclipse.birt.chart.datafeed;

/**
 * The abstract class implements an adapter for subclass.
 *
 */
public abstract class AbstractDataPointDefinition implements IDataPointDefinition {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IDataPointDefinition#getDataPointTypes()
	 */
	@Override
	public String[] getDataPointTypes() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataPointDefinition#getDisplayText(java.lang
	 * .String)
	 */
	@Override
	public String getDisplayText(String type) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.datafeed.IDataPointDefinition#getCompatibleDataType(
	 * java.lang.String)
	 */
	@Override
	public int getCompatibleDataType(String type) {
		return 0;
	}
}
