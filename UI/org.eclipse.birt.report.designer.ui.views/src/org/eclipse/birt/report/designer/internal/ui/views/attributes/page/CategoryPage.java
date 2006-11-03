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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class CategoryPage implements ICategoryPage
{

	private String displayLabel;
	private Class pageClass;

	public CategoryPage( String displayLabel, Class pageClass )
	{
		this.displayLabel = displayLabel;
		Assert.isLegal( TabPage.class.isAssignableFrom( pageClass ) );
		this.pageClass = pageClass;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage#getDisplayLabel()
	 */
	public String getDisplayLabel( )
	{
		return displayLabel;
	}

	/**
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage#createPage(org.eclipse.swt.widgets.Composite,
	 *      int)
	 */
	public TabPage createPage(  )
	{
		try
		{
			return (TabPage) pageClass.getConstructor( null )
					.newInstance( null );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
			return null;
		}
	}
}
