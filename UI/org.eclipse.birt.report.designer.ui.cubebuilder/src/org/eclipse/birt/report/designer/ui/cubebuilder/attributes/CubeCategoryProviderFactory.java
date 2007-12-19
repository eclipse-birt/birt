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

package org.eclipse.birt.report.designer.ui.cubebuilder.attributes;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AdvancePropertyPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CommentsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.attributes.page.CubePage;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;

/**
 * @author Administrator
 * 
 */
public class CubeCategoryProviderFactory extends CategoryProviderFactory
{

	private static ICategoryProviderFactory instance = new CubeCategoryProviderFactory( );

	protected CubeCategoryProviderFactory( )
	{
	}

	/**
	 * 
	 * @return The unique CategoryProviderFactory instance
	 */
	public static ICategoryProviderFactory getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory#getCategoryProvider(java.lang.Object)
	 */
	public ICategoryProvider getCategoryProvider( Object input )
	{
		CategoryProvider provider = new CategoryProvider( new String[]{
				CategoryProviderFactory.CATEGORY_KEY_GENERAL,
				CategoryProviderFactory.CATEGORY_KEY_COMMENTS,
				CategoryProviderFactory.CATEGORY_KEY_ADVANCEPROPERTY,
		}, new String[]{
				"CubePageGenerator.List.General", //$NON-NLS-1$
				"CubePageGenerator.List.Comments", //$NON-NLS-1$
				"CubePageGenerator.List.AdvancedProperty", //$NON-NLS-1$
		}, new Class[]{
				CubePage.class,
				CommentsPage.class,
				AdvancePropertyPage.class,
		} );
		return provider;
	}
}
