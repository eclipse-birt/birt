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

package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BookMarkExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPaddingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CommentsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataSetPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataSourcePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DescriptionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FontPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatDateTimeAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatNumberAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatStringAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GridPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HyperLinkPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ImagePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ItemMarginPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LabelI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LabelPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LibraryPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ListPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ListingSectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ReferencePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ReportPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.RowPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.SectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TOCExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TablePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TextI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TextPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VisibilityPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.CategoryProviderFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProviderFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * 
 */

public class IDECategoryProviderFactory extends CategoryProviderFactory
{

	private static ICategoryProviderFactory instance = new IDECategoryProviderFactory( );

	protected IDECategoryProviderFactory( )
	{
	}

	public static ICategoryProviderFactory getInstance( )
	{
		return instance;
	}

	public ICategoryProvider getCategoryProvider( String elementName )
	{
		if ( ReportDesignConstants.CELL_ELEMENT.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"CellPageGenerator.List.General", //$NON-NLS-1$
					"CellPageGenerator.List.CellPadding", //$NON-NLS-1$
					"CellPageGenerator.List.Font", //$NON-NLS-1$
					"CellPageGenerator.List.Borders", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					CellPage.class,
					CellPaddingPage.class,
					FontPage.class,
					BordersPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.DATA_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"DataPageGenerator.List.General", //$NON-NLS-1$
			//		"DataPageGenerator.List.Expression", //$NON-NLS-1$
					"DataPageGenerator.List.Padding", //$NON-NLS-1$
					"DataPageGenerator.List.Borders", //$NON-NLS-1$
					"DataPageGenerator.List.Margin", //$NON-NLS-1$
					"DataPageGenerator.List.formatNumber", //$NON-NLS-1$
					"DataPageGenerator.List.formatDateTime", //$NON-NLS-1$
					"DataPageGenerator.List.formatString", //$NON-NLS-1$
					"DataPageGenerator.List.HyperLink", //$NON-NLS-1$
					"DataPageGenerator.List.Section", //$NON-NLS-1$
					"DataPageGenerator.List.Visibility",//$NON-NLS-1$
					"DataPageGenerator.List.TOC",//$NON-NLS-1$
					"DataPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler" //$NON-NLS-1$
			}, new Class[]{
					DataPage.class,
			//		ExpressionPage.class,
					CellPaddingPage.class,
					BordersPage.class,
					ItemMarginPage.class,
					FormatNumberAttributePage.class,
					FormatDateTimeAttributePage.class,
					FormatStringAttributePage.class,
					HyperLinkPage.class,
					SectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.GRID_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"GridPageGenerator.List.General", //$NON-NLS-1$
					"GridPageGenerator.List.Margin", //$NON-NLS-1$
					"GridPageGenerator.List.Font", //$NON-NLS-1$
					"GridPageGenerator.List.Borders", //$NON-NLS-1$
					"GridPageGenerator.List.Section", //$NON-NLS-1$
					"GridPageGenerator.List.Visibility", //$NON-NLS-1$
					"GridPageGenerator.List.TOC", //$NON-NLS-1$
					"GridPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					GridPage.class,
					ItemMarginPage.class,
					FontPage.class,
					BordersPage.class,
					SectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.IMAGE_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"ImagePageGenerator.List.General", //$NON-NLS-1$
					"ImagePageGenerator.List.Reference", //$NON-NLS-1$
					"ImagePageGenerator.List.HyperLink", //$NON-NLS-1$
					"ImagePageGenerator.List.AltText", //$NON-NLS-1$
					"ImagePageGenerator.List.Borders", //$NON-NLS-1$
					"ImagePageGenerator.List.Section", //$NON-NLS-1$
					"ImagePageGenerator.List.Visibility", //$NON-NLS-1$
					"ImagePageGenerator.List.TOC", //$NON-NLS-1$
					"ImagePageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					ImagePage.class,
					ReferencePage.class,
					HyperLinkPage.class,
					AlterPage.class,
					BordersPage.class,
					SectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.LABEL_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"LabelPageGenerator.List.General", //$NON-NLS-1$

					"LabelPageGenerator.List.Padding", //$NON-NLS-1$
					"LabelPageGenerator.List.Borders", //$NON-NLS-1$
					"LabelPageGenerator.List.Margin", //$NON-NLS-1$
					"LabelPageGenerator.List.HyperLink", //$NON-NLS-1$
					"LabelPageGenerator.List.Section", //$NON-NLS-1$
					"LabelPageGenerator.List.Visibility", //$NON-NLS-1$
					"LabelPageGenerator.List.I18n", //$NON-NLS-1$
					"LabelPageGenerator.List.TOC", //$NON-NLS-1$
					"LabelPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					LabelPage.class,
					CellPaddingPage.class,
					BordersPage.class,
					ItemMarginPage.class,
					HyperLinkPage.class,
					SectionPage.class,
					VisibilityPage.class,
					LabelI18nPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.LIBRARY_ELEMENT.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"ReportPageGenerator.List.General", //$NON-NLS-1$
					"ReportPageGenerator.List.Description", //$NON-NLS-1$
					"ReportPageGenerator.List.Comments", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					LibraryPage.class,
					DescriptionPage.class,
					CommentsPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class,
			} );
		}
		if ( ReportDesignConstants.LIST_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"ListPageGenerator.List.General", //$NON-NLS-1$
					"ListPageGenerator.List.Font", //$NON-NLS-1$
					"ListPageGenerator.List.Borders", //$NON-NLS-1$
					"ListPageGenerator.List.Section", //$NON-NLS-1$
					"ListPageGenerator.List.Visibility", //$NON-NLS-1$
					"ListPageGenerator.List.TOC", //$NON-NLS-1$
					"ListPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					ListPage.class,
					FontPage.class,
					BordersPage.class,
					ListingSectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.REPORT_DESIGN_ELEMENT.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"ReportPageGenerator.List.General", //$NON-NLS-1$
					"ReportPageGenerator.List.Description", //$NON-NLS-1$
					"ReportPageGenerator.List.Comments", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					ReportPage.class,
					DescriptionPage.class,
					CommentsPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class,
			} );
		}
		if ( ReportDesignConstants.ROW_ELEMENT.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"RowPageGenerator.List.General", //$NON-NLS-1$
					// "RowPageGenerator.List.CellPadding" //$NON-NLS-1$
					"RowPageGenerator.List.Font", //$NON-NLS-1$
					// "RowPageGenerator.List.Borders", //$NON-NLS-1$
					"RowPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					RowPage.class,
					// CellPaddingPage.class,
					FontPage.class,
					// BordersPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.TABLE_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"TablePageGenerator.List.General", //$NON-NLS-1$
					"TablePageGenerator.List.Marign", //$NON-NLS-1$
					"TablePageGenerator.List.Font", //$NON-NLS-1$
					"TablePageGenerator.List.Borders", //$NON-NLS-1$
					"TablePageGenerator.List.Section", //$NON-NLS-1$
					"TablePageGenerator.List.Visibility", //$NON-NLS-1$
					"TablePageGenerator.List.TOC", //$NON-NLS-1$
					"TablePageGenerator.List.Bookmark",//$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					TablePage.class,
					ItemMarginPage.class,
					FontPage.class,
					BordersPage.class,
					ListingSectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.TEXT_DATA_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"TextPageGenerator.List.General", //$NON-NLS-1$
					"DataPageGenerator.List.Expression", //$NON-NLS-1$
					"TextPageGenerator.List.Padding", //$NON-NLS-1$
					"TextPageGenerator.List.Borders", //$NON-NLS-1$
					"TextPageGenerator.List.Margin", //$NON-NLS-1$
					"TextPageGenerator.List.Section", //$NON-NLS-1$
					"TextPageGenerator.List.Visibility", //$NON-NLS-1$
					"TextPageGenerator.List.TOC", //$NON-NLS-1$
					"TextPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					TextPage.class,
					ExpressionPage.class,
					CellPaddingPage.class,
					BordersPage.class,
					ItemMarginPage.class,
					SectionPage.class,
					VisibilityPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		if ( ReportDesignConstants.TEXT_ITEM.equals( elementName ) )
		{
			return new CategoryProvider( new String[]{
					"TextPageGenerator.List.General", //$NON-NLS-1$
					"TextPageGenerator.List.Padding", //$NON-NLS-1$
					"TextPageGenerator.List.Borders", //$NON-NLS-1$
					"TextPageGenerator.List.Margin", //$NON-NLS-1$
					"TextPageGenerator.List.Section", //$NON-NLS-1$
					"TextPageGenerator.List.Visibility", //$NON-NLS-1$
					"TextPageGenerator.List.I18n", //$NON-NLS-1$
					"TextPageGenerator.List.TOC", //$NON-NLS-1$
					"TextPageGenerator.List.Bookmark", //$NON-NLS-1$
					"ReportPageGenerator.List.UserProperties", //$NON-NLS-1$
					"ReportPageGenerator.List.NamedExpressions", //$NON-NLS-1$
					"ReportPageGenerator.List.EventHandler", //$NON-NLS-1$
			}, new Class[]{
					TextPage.class,
					CellPaddingPage.class,
					BordersPage.class,
					ItemMarginPage.class,
					SectionPage.class,
					VisibilityPage.class,
					TextI18nPage.class,
					TOCExpressionPage.class,
					BookMarkExpressionPage.class,
					UserPropertiesPage.class,
					NamedExpressionsPage.class,
					HandlerPage.class
			} );
		}
		return super.getCategoryProvider( elementName );
	}

	public ICategoryProvider getCategoryProvider( DesignElementHandle handle )
	{

		if ( handle instanceof DataSourceHandle )
		{
			return new CategoryProvider( new String[]{
					"DataSourcePageGenerator.List.General", "ReportPageGenerator.List.EventHandler"},//$NON-NLS-1$
					new Class[]{
							DataSourcePage.class, HandlerPage.class
					} );
		}
		if ( handle instanceof DataSetHandle )
		{
			return new CategoryProvider( new String[]{
					"DataSetPageGenerator.List.General", "ReportPageGenerator.List.EventHandler"}, //$NON-NLS-1$
					new Class[]{
							DataSetPage.class, HandlerPage.class
					} );
		}

		return super.getCategoryProvider( handle );
	}
}
