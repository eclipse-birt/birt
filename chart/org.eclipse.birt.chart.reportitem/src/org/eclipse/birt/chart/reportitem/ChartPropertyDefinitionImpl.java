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

import java.util.List;

import org.eclipse.birt.report.model.api.extension.PropertyDefinition;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;

/**
 * This class defines all the chart properties definition for reportItem
 * extension.
 */
public final class ChartPropertyDefinitionImpl extends PropertyDefinition {

	private String sGroupNameID = null;

	private String sName = null;

	private String sDisplayNameID = null;

	private boolean bList = false;

	private int iType = -1;

	private List liChoices = null;

	private List liMembers = null;

	private Object oDefaultValue = null;

	private IMethodInfo mi = null;

	public ChartPropertyDefinitionImpl(String sGroupNameID, String sName, String sDisplayNameID, boolean bList,
			int iType, List liChoices, List liMembers, Object oDefaultValue) {
		this(sGroupNameID, sName, sDisplayNameID, bList, iType, liChoices, liMembers, oDefaultValue, null);
	}

	public ChartPropertyDefinitionImpl(String sGroupNameID, String sName, String sDisplayNameID, boolean bList,
			int iType, List liChoices, List liMembers, Object oDefaultValue, IMethodInfo mi) {
		this.sGroupNameID = sGroupNameID;
		this.sName = sName;
		this.sDisplayNameID = sDisplayNameID;
		this.bList = bList;
		this.iType = iType;
		this.liChoices = liChoices;
		this.liMembers = liMembers;
		this.oDefaultValue = oDefaultValue;
		this.mi = mi;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getGroupNameID()
	 */
	@Override
	public String getGroupNameID() {
		return sGroupNameID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getName()
	 */
	@Override
	public String getName() {
		return sName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getDisplayNameID(
	 * )
	 */
	@Override
	public String getDisplayNameID() {
		return sDisplayNameID;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getType()
	 */
	@Override
	public int getType() {
		return iType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#isList()
	 */
	@Override
	public boolean isList() {
		return bList;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getChoices()
	 */
	@Override
	public List getChoices() {
		return liChoices;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IPropertyDefinition#getMembers()
	 */
	@Override
	public List getMembers() {
		return liMembers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.extension.IPropertyDefinition#getDefaultValue()
	 */
	@Override
	public Object getDefaultValue() {
		return oDefaultValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IPropertyDefinition#isVisible()
	 */
	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public IMethodInfo getMethodInfo() {
		return mi;
	}

}
