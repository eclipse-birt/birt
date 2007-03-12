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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * @author Administrator
 * 
 */
public class PageLayoutPropertyDescriptorProvider extends
		SimpleComboPropertyDescriptorProvider {

	// protected CrosstabReportItemHandle crosstabHandle;

	public PageLayoutPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#getDisplayName()
	 */
	public String getDisplayName() {
		return "Page Layout:";
	}

	public Object load( )
	{
		String value = (String) super.load();
		if(value != null)
		{
			int index = -1;
			index = Arrays.asList(getValues()).indexOf(value);
			if(index < 0)
			{
				return value;
			}else
			{
				return getItems()[index];
			}
		}
		return value;
	}

	public void save(Object value) throws SemanticException {		
		if(value != null)
		{
			int index = -1;
			index = Arrays.asList(getItems()).indexOf(value);
			if(index >= 0)
			{
				value = getValues()[index];
			}
		}
		
		super.save(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		this.input = input;

	}

	public String[] getItems() {
		String[] items = new String[] { "Over, then Down", "Down, then Over" };
		return items;
	}

	public String[] getValues() {
		String[] items = new String[] {
				ICrosstabConstants.PAGE_LAYOUT_OVER_THEN_DOWN,
				ICrosstabConstants.PAGE_LAYOUT_DOWN_THEN_OVER };
		return items;
	}

	//
	// protected void initializeCrosstab() {
	// crosstabHandle = null;
	// if ((input == null)) {
	// return;
	// }
	//
	// if ((!(input instanceof List && DEUtil.getMultiSelectionHandle(
	// (List) input).isExtendedElements()))
	// && (!(input instanceof ExtendedItemHandle))) {
	// return;
	// }
	//
	// ExtendedItemHandle handle;
	// if (((List) input).size() > 0) {
	// handle = (ExtendedItemHandle) (((List) input).get(0));
	// } else // input instanceof ExtendedItemHandle
	// {
	// handle = (ExtendedItemHandle) input;
	// }
	//
	// try {
	// crosstabHandle = (CrosstabReportItemHandle) handle.getReportItem();
	// return;
	// } catch (ExtendedElementException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return;
	// }
	// }
}
