/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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
	@Override
	public String getDisplayNameID() {
		return sDisplayNameID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getName()
	 */
	@Override
	public String getName() {
		return sName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IChoiceDefinition#getValue()
	 */
	@Override
	public Object getValue() {
		return oValue;
	}

}
