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

import java.util.Arrays;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SimpleComboPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;

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
		return Messages.getString("PageLayoutPropertyDescriptorProvider.PageLayout");
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

	private static IChoiceSet choiceSet;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		this.input = input;
		GroupElementHandle multiSelectionHandle = DEUtil.getMultiSelectionHandle( DEUtil.getInputElements( input ) );
		choiceSet = multiSelectionHandle.getPropertyHandle( getProperty( ) )
				.getPropertyDefn( )
				.getChoices( );
	}

	public String[] getItems() {
		IChoice choice[]= choiceSet.getChoices( );
		String[] items = new String[choice.length];
		for(int i = 0; i < choice.length; i ++)
		{
			items[i] = choice[i].getDisplayName( );
		}
		return items;
	}

	public String[] getValues() {
		IChoice choice[]= choiceSet.getChoices( );
		String[] items = new String[choice.length];
		for(int i = 0; i < choice.length; i ++)
		{
			items[i] = choice[i].getName( );
		}
		return items;
	}
}
