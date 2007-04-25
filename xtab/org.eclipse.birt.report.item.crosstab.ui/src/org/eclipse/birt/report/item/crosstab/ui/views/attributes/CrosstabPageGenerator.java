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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BaseAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabFilterHandleProvider;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.CrosstabSortingHandleProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 *
 */
public class CrosstabPageGenerator extends AbstractPageGenerator {
	
	protected BindingPage bindingPage;
	
	protected BaseAttributePage RowAreaPage;
	
	protected BaseAttributePage ColumnAreaPage;

//	protected DetailAreaPage detailAreaPage;
	
	protected FormPage filterPage;

	protected FormPage sortingPage;
	
	protected PreviewPage mapPage;
	
	protected PreviewPage highlightsPage;



	
	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			switch ( tabFolder.getSelectionIndex( ) )
			{
				case 1 :
					bindingPage = new BindingPage( );
					bindingPage.setDataSetSectionVisible(false);
					setPageInput( bindingPage );
					refresh(tabFolder,bindingPage, true);
					item.setControl( bindingPage.getControl( ) );
					itemMap.put( item, bindingPage );
					break;
					
//				case 2:
//				case 3:
					// RowArea, ColumnArea
//					break;
				case 2:
					// filter;
					filterPage = new FormPage(
							FormPropertyDescriptor.NO_UP_DOWN,
							new CrosstabFilterHandleProvider( ),
							true,
							true );
					setPageInput( filterPage );
					refresh(tabFolder,filterPage, true);
					item.setControl( filterPage.getControl( ) );
					itemMap.put( item, filterPage );
					break;
				case 3:
					// sorting;
					sortingPage = new FormPage(
							FormPropertyDescriptor.NO_UP_DOWN,
							new CrosstabSortingHandleProvider( ),
							true,
							true );
					setPageInput( sortingPage );
					refresh(tabFolder,sortingPage, true);
					item.setControl( sortingPage.getControl( ) );
					itemMap.put( item, sortingPage );
					break;
				case 4 :
					mapPage = new PreviewPage( true );
					mapPage.setPreview( new MapPropertyDescriptor( true ) );
					mapPage.setProvider( new MapDescriptorProvider( ) );
					setPageInput( mapPage );
					refresh(tabFolder,mapPage, true);
					item.setControl( mapPage.getControl( ) );
					itemMap.put( item, mapPage );
					break;
				case 5 :
					highlightsPage = new PreviewPage( true );
					highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
					highlightsPage.setProvider( new HighlightDescriptorProvider( ) );
					setPageInput( highlightsPage );
					refresh(tabFolder,highlightsPage, true);
					item.setControl( highlightsPage.getControl( ) );
					itemMap.put( item, highlightsPage );
					break;
				default :
					break;
			}
		}
		else if ( itemMap.get( item ) != null ){
			setPageInput( itemMap.get( item ) );
			refresh(tabFolder,itemMap.get( item ), false);
		}
	}

	
	public void createTabItems( List input )
	{
//		if ( basicPage == null || basicPage.getControl( ).isDisposed( ))
//		{
//			ISafeRunnable runnable = new ISafeRunnable( ) {
//
//				public void run( ) throws Exception
//				{
//					CTabItem[] oldPages = tabFolder.getItems( );
//					int index = tabFolder.getSelectionIndex( );
//					for ( int i = 0; i < oldPages.length; i++ )
//					{
//						if ( oldPages[i].isDisposed( ) )
//							continue;
//						if ( index == i )
//							continue;
//						if ( oldPages[i].getControl( ) != null )
//						{
//							oldPages[i].getControl( ).dispose( );
//						}
//						oldPages[i].dispose( );
//					}
//					if ( index > -1 && !oldPages[index].isDisposed( ) )
//					{
//						oldPages[index].getControl( ).dispose( );
//						oldPages[index].dispose( );
//					}
//				}
//
//				public void handleException( Throwable exception )
//				{
//					/* not used */
//				}
//			};
//			Platform.run( runnable );
//			tabFolder.setLayout( new FillLayout( ) );
//			basicPage = new BaseAttributePage( );
//			basicPage.buildUI( tabFolder  );
//			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
//			tabItem.setText( Messages.getString( "CategoryPageGenerator.TabItem.Attributes" ) ); //$NON-NLS-1$
//			tabItem.setControl( basicPage.getControl( ) );
//
//			basicPage.setCategoryProvider( CrosstabCategoryProviderFactory.getInstance( ).getCategoryProvider( input ) );
//		}
		super.createTabItems( input );
		this.input = input;
		basicPage.setInput( input );
		addSelectionListener( this );
		basicPage.refresh( );
		createTabItem( 1, Messages.getString( "CrosstabPageGenerator.TabItem.Binding" ) );
//		createRowArea();
//		createColumnArea();
//		createTabItem( 2, Messages.getString( "CrosstabPageGenerator.TabItem.RowArea" ) );
//		createTabItem( 3, Messages.getString( "CrosstabPageGenerator.TabItem.ColumnArea" ) );
		createTabItem( 2, Messages.getString( "CrosstabPageGenerator.TabItem.Filters" ) );
		createTabItem( 3, Messages.getString( "CrosstabPageGenerator.TabItem.Sorting" ) );
		createTabItem( 4, Messages.getString( "CrosstabPageGenerator.TabItem.map" ) );
		createTabItem( 5, Messages.getString( "CrosstabPageGenerator.TabItem.Highlights" ) );
		
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}

	protected void createRowArea()
	{
		if ( RowAreaPage == null || RowAreaPage.getControl( ).isDisposed( ))
		{
			tabFolder.setLayout( new FillLayout( ) );
			RowAreaPage = new BaseAttributePage( );
			RowAreaPage.buildUI( tabFolder  );
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( Messages.getString( "CrosstabPageGenerator.TabItem.RowArea" ) ); //$NON-NLS-1$
			tabItem.setControl( RowAreaPage.getControl( ) );

			RowAreaPage.setCategoryProvider( RowAreaCategoryProviderFactory.getInstance( ).getCategoryProvider( input ) );
		}
		RowAreaPage.setInput( input );
		RowAreaPage.refresh( );
	}
	
	protected void createColumnArea()
	{
		if ( ColumnAreaPage == null || ColumnAreaPage.getControl( ).isDisposed( ))
		{
			tabFolder.setLayout( new FillLayout( ) );
			ColumnAreaPage = new BaseAttributePage( );
			ColumnAreaPage.buildUI( tabFolder  );
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( Messages.getString( "CrosstabPageGenerator.TabItem.ColumnArea" ) ); //$NON-NLS-1$
			tabItem.setControl( ColumnAreaPage.getControl( ) );

			ColumnAreaPage.setCategoryProvider( RowAreaCategoryProviderFactory.getInstance( ).getCategoryProvider( input ) );
		}
		ColumnAreaPage.setInput( input );
		ColumnAreaPage.refresh( );
	}
	
	public void createControl( Composite parent, Object input )
	{
		setCategoryProvider( CrosstabCategoryProviderFactory.getInstance( ).getCategoryProvider( input )  );
		super.createControl( parent, input );
		createTabItems( (List)input );
	}
	
}
