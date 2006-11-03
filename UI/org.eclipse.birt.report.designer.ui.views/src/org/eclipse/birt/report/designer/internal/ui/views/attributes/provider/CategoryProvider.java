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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CategoryPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.jface.text.Assert;

/**
 * 
 */

public class CategoryProvider implements ICategoryProvider
{

	private ICategoryPage[] categories;

	public CategoryProvider( String category, Class pageClass )
	{
		this( new String[]{
			category
		}, new Class[]{
			pageClass
		} );
	}

	public CategoryProvider( String[] categories, Class[] pageClasses )
	{
		Assert.isLegal( categories.length == pageClasses.length );
		this.categories = new ICategoryPage[categories.length];
		for ( int i = 0; i < categories.length; i++ )
		{
			String displayLabel = Messages.getString( categories[i] );
			this.categories[i] = new CategoryPage( displayLabel, pageClasses[i] );
		}
	}

	public ICategoryPage[] getCategories( )
	{
		return categories;
	}
}
