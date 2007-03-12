/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * @author Administrator
 * 
 */
public class GrandTotalProvider implements IDescriptorProvider {

	protected Object input;
	protected int axisType;
	protected CrosstabReportItemHandle crosstabHandle;

	public GrandTotalProvider(int axisType) {
		this.axisType = axisType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#getDisplayName()
	 */
	public String getDisplayName() {
		return "Grand totals for columns";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#load()
	 */
	public Object load() {
		Object grandTotal = null;
		if (crosstabHandle != null) {
			grandTotal = crosstabHandle.getGrandTotal(axisType);
		}
		return (grandTotal != null)  ? "true" : "false"; 

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#save(java.lang.Object)
	 */
	public void save(Object value) throws SemanticException {
		String stringValue = (String)value;
		Object grandTotal = crosstabHandle.getGrandTotal(axisType);
		if(stringValue != null && stringValue.equalsIgnoreCase("true"))
		{			
			if(grandTotal == null)
			{
				crosstabHandle.addGrandTotal(axisType);
			}
		}else // false;
		{
			if(grandTotal != null)
			{
				crosstabHandle.removeGrandTotal(axisType);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		this.input = input;
	}

	protected void initializeCrosstab() {
		crosstabHandle = null;
		if ((input == null)) {
			return;
		}

		if ((!(input instanceof List && DEUtil.getMultiSelectionHandle(
				(List) input).isExtendedElements()))
				&& (!(input instanceof ExtendedItemHandle))) {
			return;
		}

		ExtendedItemHandle handle;
		if (((List) input).size() > 0) {
			handle = (ExtendedItemHandle) (((List) input)
					.get(0));
		}else // input instanceof ExtendedItemHandle
		{
			handle = (ExtendedItemHandle)input;
		}
		
		try {
			crosstabHandle = (CrosstabReportItemHandle) handle
					.getReportItem();
			return;
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

}
