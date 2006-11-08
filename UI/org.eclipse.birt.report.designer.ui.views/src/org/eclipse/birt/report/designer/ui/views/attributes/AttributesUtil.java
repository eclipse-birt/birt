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

package org.eclipse.birt.report.designer.ui.views.attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BookMarkExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CategoryPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPaddingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FontPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HyperLinkPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ItemMarginPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.SectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TOCExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VisibilityPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IPropertyTabUI;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * Utility class to help creating standard attribute pages.
 */
public class AttributesUtil
{

	/**
	 * Category name for standard Font page.
	 */
	public static final String FONT = "font"; //$NON-NLS-1$
	/**
	 * Category name for standard Padding page.
	 */
	public static final String PADDING = "padding"; //$NON-NLS-1$
	/**
	 * Category name for standard alt_text page.
	 */
	public static final String ALT = "alt"; //$NON-NLS-1$
	/**
	 * Category name for standard Border page.
	 */
	public static final String BORDER = "border"; //$NON-NLS-1$
	/**
	 * Category name for standard Margin page.
	 */
	public static final String MARGIN = "margin"; //$NON-NLS-1$
	/**
	 * Category name for standard Hyperlink page.
	 */
	public static final String HYPERLINK = "hyperlink"; //$NON-NLS-1$
	/**
	 * Category name for standard Section page.
	 */
	public static final String SECTION = "section"; //$NON-NLS-1$
	/**
	 * Category name for standard Visibility page.
	 */
	public static final String VISIBILITY = "visibility"; //$NON-NLS-1$
	/**
	 * Category name for standard TOC page.
	 */
	public static final String TOC = "toc"; //$NON-NLS-1$
	/**
	 * Category name for standard Bookmark page.
	 */
	public static final String BOOKMARK = "bookmark"; //$NON-NLS-1$
	/**
	 * Category name for standard UserProperties page.
	 */
	public static final String USERPROPERTIES = "userproperties"; //$NON-NLS-1$
	/**
	 * Category name for standard NamedExpression page.
	 */
	public static final String NAMEDEXPRESSIONS = "namedexpressions"; //$NON-NLS-1$
	/**
	 * Category name for standard EventHandler page.
	 */
	public static final String EVENTHANDLER = "eventhandler"; //$NON-NLS-1$

	private static Map categoryMap = new HashMap( );
	private static Map paneClassMap = new HashMap( );

	static
	{
		addCategory( FONT, "GridPageGenerator.List.Font", FontPage.class );//$NON-NLS-1$		
		addCategory( PADDING,
				"DataPageGenerator.List.Padding", CellPaddingPage.class ); //$NON-NLS-1$
		addCategory( ALT, "ImagePageGenerator.List.AltText", AlterPage.class ); //$NON-NLS-1$
		addCategory( BORDER,
				"DataPageGenerator.List.Borders", BordersPage.class ); //$NON-NLS-1$
		addCategory( MARGIN,
				"DataPageGenerator.List.Margin", ItemMarginPage.class ); //$NON-NLS-1$
		addCategory( HYPERLINK,
				"DataPageGenerator.List.HyperLink", HyperLinkPage.class ); //$NON-NLS-1$
		addCategory( SECTION,
				"DataPageGenerator.List.Section", SectionPage.class ); //$NON-NLS-1$
		addCategory( VISIBILITY,
				"DataPageGenerator.List.Visibility", VisibilityPage.class ); //$NON-NLS-1$
		addCategory( TOC, "DataPageGenerator.List.TOC", TOCExpressionPage.class ); //$NON-NLS-1$
		addCategory( BOOKMARK,
				"DataPageGenerator.List.Bookmark", BookMarkExpressionPage.class ); //$NON-NLS-1$
		addCategory( USERPROPERTIES,
				"ReportPageGenerator.List.UserProperties", UserPropertiesPage.class ); //$NON-NLS-1$
		addCategory( NAMEDEXPRESSIONS,
				"ReportPageGenerator.List.NamedExpressions", NamedExpressionsPage.class ); //$NON-NLS-1$
	}

	/**
	 * Add category for a Attribute Page
	 * 
	 * @param id
	 * @param displayLabelKey
	 * @param pageClass
	 */
	public static void addCategory( String id, String displayLabelKey,
			Class pageClass )
	{
		Assert.isNotNull( id );
		categoryMap.put( id, displayLabelKey );
		paneClassMap.put( id, pageClass );
	}

	/**
	 * Creates the standard categoried property page.
	 * 
	 * @param parent
	 * @param standardCategories
	 *            A standard category id array, the contained value must be the
	 *            category name constants defined within the AttributeUtil
	 *            class, such as AttributesUtil.FONT, AttributesUtil.MARGIN. A
	 *            null value means reserve this position for custom pages.
	 * @param customCategories
	 *            A custom categories name array.
	 * @param customPageWrappers
	 *            A custom page wrapper array.
	 * @return Page object.
	 */
	public static IPropertyTabUI buildGeneralPage( Composite parent,
			String[] standardCategories, String[] customCategories,
			PageWrapper[] customPageWrappers, Object input )
	{
		BaseAttributePage basicPage = new BaseAttributePage( );
		List categoryList = new ArrayList( );
		List paneClassList = new ArrayList( );

		if ( standardCategories == null )
		{
			if ( customCategories != null && customPageWrappers != null )
			{
				for ( int i = 0; i < customCategories.length; i++ )
				{
					categoryList.add( customCategories[i] );
					paneClassList.add( customPageWrappers[i] );
				}
			}
		}
		else
		{
			int customIndex = 0;

			for ( int i = 0; i < standardCategories.length; i++ )
			{
				if ( standardCategories[i] == null )
				{
					if ( customCategories != null
							&& customPageWrappers != null
							&& customCategories.length > customIndex )
					{
						categoryList.add( customCategories[customIndex] );
						paneClassList.add( customPageWrappers[customIndex] );
						customIndex++;
					}
				}
				else
				{
					Object cat = categoryMap.get( standardCategories[i] );

					if ( cat instanceof String )
					{
						categoryList.add( cat );
						paneClassList.add( paneClassMap.get( standardCategories[i] ) );
					}
				}
			}

			if ( customCategories != null
					&& customPageWrappers != null
					&& customCategories.length > customIndex )
			{
				for ( int i = customIndex; i < customCategories.length; i++ )
				{
					categoryList.add( customCategories[customIndex] );
					paneClassList.add( customPageWrappers[customIndex] );
					customIndex++;
				}
			}
		}

		String[] cats = (String[]) categoryList.toArray( new String[0] );
		Object[] clss = paneClassList.toArray( new Object[0] );

		basicPage.setCategoryProvider( new ExtendedCategoryProvider( cats, clss ) );

		basicPage.setInput( input );
		basicPage.buildUI( parent );

		return basicPage;
	}

	/**
	 * Returns standard category page display name.
	 * 
	 * @return
	 */
	public static String getGeneralPageDisplayName( )
	{
		return Messages.getString( "CategoryPageGenerator.TabItem.Attributes" ); //$NON-NLS-1$
	}

	/**
	 * Creates the standard data binding page.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @return Page object.
	 */
	public static IPropertyTabUI buildBindingPage( Composite parent, Object input )
	{
		GridLayout gl = new GridLayout( );
		parent.setLayout( gl );
		BindingPage page = new BindingPage( );
		page.setInput( input );
		page.buildUI( parent );
		return page;
	}

	/**
	 * Returns standard data binding page display name.
	 * 
	 * @return
	 */
	public static String getBindingPageDisplayName( )
	{
		return Messages.getString( "TablePageGenerator.TabItem.Binding" ); //$NON-NLS-1$
	}

	/**
	 * Creates standard data filter page.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @return Page object.
	 */
	public static IPropertyTabUI buildFilterPage( Composite parent, Object input )
	{
		GridLayout gl = new GridLayout( );
		parent.setLayout( gl );
		FormPage page = new FormPage( FormPropertyDescriptor.NORMAL_FUNCTION,
				new FilterHandleProvider( ),
				false,
				true );
		page.setInput( input );
		page.buildUI( parent );

		return page;
	}

	/**
	 * Returns standard data filter page display name.
	 * 
	 * @return
	 */
	public static String getFilterPageDisplayName( )
	{
		return Messages.getString( "TablePageGenerator.TabItem.Filters" ); //$NON-NLS-1$
	}

	/**
	 * Creates standard data filter page.
	 * 
	 * @param parent
	 *            Parent composite.
	 * @return Page object.
	 */
	public static IPropertyTabUI buildHighlightPage( Composite parent, Object input )
	{
		GridLayout gl = new GridLayout( );
		parent.setLayout( gl );
		PreviewPage page = new PreviewPage( true );
		page.setPreview( new HighlightPropertyDescriptor( true ) );
		page.setProvider( new HighlightDescriptorProvider( ) );
		page.setInput( input );
		page.buildUI( parent );
		
		return page;
	}

	/**
	 * Returns standard data filter page display name.
	 * 
	 * @return
	 */
	public static String getHighlightPageDisplayName( )
	{
		return Messages.getString( "TablePageGenerator.TabItem.Highlights" ); //$NON-NLS-1$
	}

	/**
	 * Sets input to the page.
	 * 
	 * @param page
	 *            This must be the result object returned by
	 *            AttributesUtil.buildXXXPage().
	 * @param input
	 *            input objects.
	 */
	public static void setPageInput( IPropertyTabUI page, Object input )
	{
		page.setInput( input );
		if ( page instanceof TabPage && page.getControl( )!=null)
		{	
			( (TabPage) page ).refresh( );
		}
	}

	/**
	 * Convenient method to handle property page exceptions.
	 */
	public static void handleError( Throwable e )
	{
		ExceptionHandler.handle( e );
	}

	/**
	 * ExtendedCategoryProvider
	 */
	static class ExtendedCategoryProvider implements ICategoryProvider
	{

		private String[] categories;
		private Object[] paneObjects;

		ExtendedCategoryProvider( String[] categories, Object[] paneObjects )
		{
			Assert.isNotNull( categories );
			Assert.isNotNull( paneObjects );
			Assert.isLegal( categories.length == paneObjects.length );
			this.categories = categories;
			this.paneObjects = paneObjects;
		}

		public ICategoryPage[] getCategories( )
		{
			List pageList = new ArrayList( categories.length );
			for ( int i = 0; i < categories.length; i++ )
			{
				final String displayLabel = Messages.getString( categories[i] );
				final Object pane = paneObjects[i];
				if ( pane instanceof Class )
				{
					pageList.add( new CategoryPage( displayLabel, (Class) pane ) );
				}
				else if ( pane instanceof PageWrapper )
				{
					pageList.add( new ICategoryPage( ) {

						public String getDisplayLabel( )
						{
							return displayLabel;
						}

						public TabPage createPage( )
						{
							return ( (PageWrapper) pane ).getPage( );
						}
					} );
				}
			}
			return (ICategoryPage[]) pageList.toArray( new ICategoryPage[0] );
		}

	}

	/**
	 * This class wraps the custom page content and communicates with internal
	 * pages. It also provide the capability to integrate the PropertyProcessor
	 * mechanism with user customized pages.
	 */
	public abstract static class PageWrapper
	{

		private AttributePage page;

		/**
		 * Internal used to return the wrapped page.
		 * 
		 * @param parent
		 * @return
		 */
		public TabPage getPage( )
		{
			if ( page == null )
				page = new AttributePage( ) {

					public void buildUI( Composite parent )
					{
						super.buildUI( parent );
						PageWrapper.this.buildUI( parent );
					}

					public void elementChanged( DesignElementHandle focus,
							NotificationEvent ev )
					{
						super.elementChanged( focus, ev );

						PageWrapper.this.elementChanged( ev );
					}

					public void refresh( )
					{
						PageWrapper.this.refresh( );
						super.refresh( );
					}

					public void setInput( Object elements )
					{
						super.setInput( elements );
						PageWrapper.this.setInput( elements );
					}

					public void dispose( )
					{
						PageWrapper.this.dispose( );
						super.dispose( );
					}

				};
			return page;
		}

		/**
		 * Creates property page user content.
		 * 
		 * @param parent
		 *            property UI parent.
		 * @param propertyMap
		 *            build-in property map. User can put their own
		 *            PropertyDescriptor in to integrate with build-in property
		 *            framework. Usage: propertyMap.put(propertyName,
		 *            propertyDescriptor).
		 */
		public abstract void buildUI( Composite parent );

		/**
		 * Sets page input.
		 * 
		 * @param elements
		 */
		public void setInput( Object input )
		{
			// default doing nothing;
		}

		public void refresh( )
		{
			// default doing nothing;
		}

		/**
		 * Notifies if element changed.
		 * 
		 * @param ev
		 */
		public void elementChanged( NotificationEvent ev )
		{
			// default doing nothing.
		}

		/**
		 * Notifies if parent UI disposed.
		 */
		public void dispose( )
		{
			// default doing nothing.
		}

	}
}