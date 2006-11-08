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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BindingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.GroupHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.SortingHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;

/**
 * Instances of TablePageGenerator take change of creating attribute page
 * correspond to DE Table element.
 */
public class ListPageGenerator extends AbstractPageGenerator
{

	/**
	 * SWT widgets presented Sorting properties of DE Table element
	 */
	protected FormPage sortingPage;

	/**
	 * SWT widgets presented filter properties of DE Table element
	 */
	protected FormPage filterPage;

	/**
	 * SWT widgets presented Groups properties of DE Table element
	 */
	protected FormPage groupPage;

	/**
	 * SWT widgets presented binding properties of DE Table element
	 */
	protected BindingPage bindingPage;

	/**
	 * SWT widgets presented highlights properties of DE Table element
	 */
	protected PreviewPage highlightsPage;

	protected PreviewPage mapPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			switch ( tabFolder.getSelectionIndex( ) )
			{
				case 1 :
					bindingPage = new BindingPage( );
					setPageInput( bindingPage );
					refresh(tabFolder,bindingPage, true);
					item.setControl( bindingPage.getControl( ) );
					itemMap.put( item, bindingPage );
					break;
				case 2 :
					sortingPage = new FormPage(
							FormPropertyDescriptor.FULL_FUNCTION,
							new SortingHandleProvider( ),
							false,
							true );
					setPageInput( sortingPage );
					refresh(tabFolder,sortingPage, true);
					item.setControl( sortingPage.getControl( ) );
					itemMap.put( item, sortingPage );
					break;
				case 3 :
					groupPage = new FormPage( 
							FormPropertyDescriptor.FULL_FUNCTION,
							new GroupHandleProvider( ),
							true,
							true );
					setPageInput( groupPage );
					refresh(tabFolder,groupPage, true);
					item.setControl( groupPage.getControl( ) );
					itemMap.put( item, groupPage );
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
				case 6 :
					filterPage = new FormPage(
							FormPropertyDescriptor.FULL_FUNCTION,
							new FilterHandleProvider( ),
							false,
							true );
					setPageInput( filterPage );
					refresh(tabFolder,filterPage, true);
					item.setControl( filterPage.getControl( ) );
					itemMap.put( item, filterPage );
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

	public void createTabItems( CTabFolder tabFolder, List input )
	{
		super.createTabItems( tabFolder, input );
		this.input = input;
		this.tabFolder = tabFolder;
		createTabItem( 1,
				Messages.getString( "ListPageGenerator.TabItem.Binding" ) );
		createTabItem( 2,
				Messages.getString( "ListPageGenerator.TabItem.Sorting" ) );
		createTabItem( 3,
				Messages.getString( "ListPageGenerator.TabItem.Groups" ) );
		createTabItem( 4, Messages.getString( "ListPageGenerator.TabItem.map" ) );
		createTabItem( 5,
				Messages.getString( "ListPageGenerator.TabItem.Highlights" ) );
		createTabItem( 6,
				Messages.getString( "ListPageGenerator.TabItem.Filters" ) );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}
}