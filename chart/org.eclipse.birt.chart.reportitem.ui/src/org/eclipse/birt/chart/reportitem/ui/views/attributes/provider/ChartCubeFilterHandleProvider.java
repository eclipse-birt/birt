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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IFormProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider;


/**
 * The filter provider is used for cube set.
 * @since 2.3
 */
public class ChartCubeFilterHandleProvider extends
		CrosstabFilterHandleProvider
{
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.IFormHandleProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement )
	{
		Object[] elements = new Object[0];
		return elements;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider#isEditable()
	 */
	public boolean isEditable()
	{
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider#getConcreteFilterProvider()
	 */
	public IFormProvider getConcreteFilterProvider( )
	{
		if ( input == null ) {
			return this;
		}

		return ChartFilterProviderDelegate.createFilterProvider( input, getInput() );
	}
}
