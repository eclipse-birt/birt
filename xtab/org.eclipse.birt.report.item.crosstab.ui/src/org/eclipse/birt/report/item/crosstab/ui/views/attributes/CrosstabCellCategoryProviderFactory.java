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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AdvancePropertyPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPaddingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FontPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.page.CrosstabCellPage;

/**
 * @author Administrator
 * 
 */
public class CrosstabCellCategoryProviderFactory extends
		CategoryProviderFactory
{

	private static ICategoryProviderFactory instance = new CrosstabCellCategoryProviderFactory( );

	// public static final String CATEGORY_KEY_EVENTHANDLER = "EventHandler";
	// //$NON-NLS-1$

	protected CrosstabCellCategoryProviderFactory( )
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
				CATEGORY_KEY_GENERAL,
				CATEGORY_KEY_PADDING,
				CATEGORY_KEY_FONT,
				CATEGORY_KEY_BORDERS,
				CATEGORY_KEY_USERPROPERTIES,
				CATEGORY_KEY_NAMEDEXPRESSIONS,
				CATEGORY_KEY_ADVANCEPROPERTY,
		}, new String[]{
				"CellPageGenerator.List.General", //$NON-NLS-1$
				"CellPageGenerator.List.CellPadding", //$NON-NLS-1$
				"CellPageGenerator.List.Font", //$NON-NLS-1$
				"CellPageGenerator.List.Borders", //$NON-NLS-1$
				"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
				"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
				"ReportPageGenerator.List.AdvancedProperty",
		}, new Class[]{
				CrosstabCellPage.class,
				CellPaddingPage.class,
				FontPage.class,
				BordersPage.class,
				UserPropertiesPage.class,
				NamedExpressionsPage.class,
				AdvancePropertyPage.class,
		} );

		// Because model has not implemented eventhandle, mark the code as
		// comment first.
		// if ( AttributesUtil.containCategory( AttributesUtil.EVENTHANDLER ) )
		// {
		// provider.addCategory( CATEGORY_KEY_EVENTHANDLER,
		// AttributesUtil.getCategoryDisplayName( AttributesUtil.EVENTHANDLER ),
		// CrosstabEventHandlerPage.class );
		// }
		return provider;
	}
}
