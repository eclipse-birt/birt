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
	public int getCompatibleDataType(String type) {
		return 0;
	}
}
