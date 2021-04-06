/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.report.model.api.extension.ChoiceDefinition;

/**
 *  
 */
public final class ChartChoiceDefinitionImpl extends ChoiceDefinition {
	/**
	 *  
	 */
	private final String sDisplayNameID;

	/**
	 *  
	 */
	private final String sName;

	/**
	 *  
	 */
	private final Object oValue;

	/**
	 * 
	 * @param sDisplayNameID
	 * @param sName
	 * @param oValue
	 */
	ChartChoiceDefinitionImpl(String sDisplayNameID, String sName, Object oValue) {
		this.sDisplayNameID = sDisplayNameID;
		this.sName = sName;
		this.oValue = oValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IChoiceDefinition#getDisplayNameID()
	 */
	public String getDisplayNameID() {
		return sDisplayNameID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getName()
	 */
	public String getName() {
		return sName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getValue()
	 */
	public Object getValue() {
		return oValue;
	}

}