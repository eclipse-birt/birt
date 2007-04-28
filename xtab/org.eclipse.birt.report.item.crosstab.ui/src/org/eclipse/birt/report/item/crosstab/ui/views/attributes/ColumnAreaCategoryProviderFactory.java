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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.ColumnGrandTotalPage;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.ColumnPageBreak;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.ColumnSubTotalPage;


/**
 * 
 */

public class ColumnAreaCategoryProviderFactory extends CategoryProviderFactory
{
	private static ICategoryProviderFactory instance = new ColumnAreaCategoryProviderFactory( );

	public final static String SUB_TOTLES = "SubTotals";
	public final static String GRAND_TOTALS = "GrandTotals";
	public final static String PAGE_BREAK = "PageBreak";

	protected ColumnAreaCategoryProviderFactory( )
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
				RowAreaCategoryProviderFactory.SUB_TOTLES,
				RowAreaCategoryProviderFactory.GRAND_TOTALS,
				RowAreaCategoryProviderFactory.PAGE_BREAK,
		}, new String[]{
				"CrosstabPageGenerator.List.SubTotals", //$NON-NLS-1$
				"CrosstabPageGenerator.List.GrandTotals", //$NON-NLS-1$
				"CrosstabPageGenerator.List.PageBreak", //$NON-NLS-1$
		}, new Class[]{
				ColumnSubTotalPage.class,
				ColumnGrandTotalPage.class,
				ColumnPageBreak.class,
		} );
		return provider;
	}
}