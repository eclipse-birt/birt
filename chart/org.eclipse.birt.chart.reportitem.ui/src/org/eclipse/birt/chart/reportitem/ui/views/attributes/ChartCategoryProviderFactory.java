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

package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartEventHandlerPage;
import org.eclipse.birt.chart.reportitem.ui.views.attributes.page.ChartGeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AdvancePropertyPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BookMarkExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CommentsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ItemMarginPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.SectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TOCExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VisibilityPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributesUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;

/**
 * The default implement of ICategoryProviderFactory
 */

public class ChartCategoryProviderFactory extends CategoryProviderFactory
{

	private static ICategoryProviderFactory instance = new ChartCategoryProviderFactory( );

	public static final String CATEGORY_KEY_EVENTHANDLER = "EventHandler"; //$NON-NLS-1$

	protected ChartCategoryProviderFactory( )
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
				CategoryProviderFactory.CATEGORY_KEY_BORDERS,
				CategoryProviderFactory.CATEGORY_KEY_MARGIN,
				CategoryProviderFactory.CATEGORY_KEY_ALTTEXT,
				CategoryProviderFactory.CATEGORY_KEY_SECTION,
				CategoryProviderFactory.CATEGORY_KEY_VISIBILITY,
				CategoryProviderFactory.CATEGORY_KEY_TOC,
				CategoryProviderFactory.CATEGORY_KEY_BOOKMARK,
				CategoryProviderFactory.CATEGORY_KEY_COMMENTS,
				CategoryProviderFactory.CATEGORY_KEY_USERPROPERTIES,
				CategoryProviderFactory.CATEGORY_KEY_NAMEDEXPRESSIONS,
				CategoryProviderFactory.CATEGORY_KEY_ADVANCEPROPERTY,
		}, new String[]{
				"DataPageGenerator.List.General", //$NON-NLS-1$
				"DataPageGenerator.List.Borders", //$NON-NLS-1$
				"DataPageGenerator.List.Margin", //$NON-NLS-1$
				"ImagePageGenerator.List.AltText", //$NON-NLS-1$
				"DataPageGenerator.List.Section", //$NON-NLS-1$
				"DataPageGenerator.List.Visibility", //$NON-NLS-1$
				"DataPageGenerator.List.TOC", //$NON-NLS-1$
				"DataPageGenerator.List.Bookmark", //$NON-NLS-1$
				"ReportPageGenerator.List.Comments", //$NON-NLS-1$
				"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
				"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
				"ReportPageGenerator.List.AdvancedProperty", //$NON-NLS-1$
		}, new Class[]{
				ChartGeneralPage.class,
				BordersPage.class,
				ItemMarginPage.class,
				AlterPage.class,
				SectionPage.class,
				VisibilityPage.class,
				TOCExpressionPage.class,
				BookMarkExpressionPage.class,
				CommentsPage.class,
				UserPropertiesPage.class,
				NamedExpressionsPage.class,
				AdvancePropertyPage.class,
		} );
		if ( AttributesUtil.containCategory( AttributesUtil.EVENTHANDLER ) )
		{
			provider.addCategory( CATEGORY_KEY_EVENTHANDLER,
					AttributesUtil.getCategoryDisplayName( AttributesUtil.EVENTHANDLER ),
					ChartEventHandlerPage.class,
					provider.getCategoryIndex( CategoryProviderFactory.CATEGORY_KEY_ADVANCEPROPERTY ) );
		}
		return provider;
	}
}