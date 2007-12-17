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

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AggregateOnBindingPage;
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
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;

/**
 * Instances of TablePageGenerator take change of creating attribute page
 * correspond to DE Table element.
 */
public class ListPageGenerator extends AbstractPageGenerator
{

	protected FormPage sortingPage;
	protected FormPage filterPage;
	protected FormPage groupPage;
	protected AggregateOnBindingPage bindingPage;
	protected PreviewPage highlightsPage;
	protected PreviewPage mapPage;

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			String title = tabFolder.getSelection( ).getText( );
			if ( title.equals( BINDINGTITLE ) )
			{
				bindingPage = new AggregateOnBindingPage( );
				setPageInput( bindingPage );
				refresh( tabFolder, bindingPage, true );
				item.setControl( bindingPage.getControl( ) );
				itemMap.put( item, bindingPage );
			}
			else if ( title.equals( SORTINGTITLE ) )
			{
				sortingPage = new FormPage( FormPropertyDescriptor.FULL_FUNCTION,
						new SortingHandleProvider( ),
						true,
						true );
				setPageInput( sortingPage );
				refresh( tabFolder, sortingPage, true );
				item.setControl( sortingPage.getControl( ) );
				itemMap.put( item, sortingPage );
			}
			else if ( title.equals( GROUPSSTITLE ) )
			{
				groupPage = new FormPage( FormPropertyDescriptor.FULL_FUNCTION,
						new GroupHandleProvider( ),
						true,
						true );
				setPageInput( groupPage );
				refresh( tabFolder, groupPage, true );
				item.setControl( groupPage.getControl( ) );
				itemMap.put( item, groupPage );
			}
			else if ( title.equals( MAPTITLE ) )
			{
				mapPage = new PreviewPage( true );
				mapPage.setPreview( new MapPropertyDescriptor( true ) );
				mapPage.setProvider( new MapDescriptorProvider( ) );
				setPageInput( mapPage );
				refresh( tabFolder, mapPage, true );
				item.setControl( mapPage.getControl( ) );
				itemMap.put( item, mapPage );
			}
			else if ( title.equals( HIGHLIGHTSTITLE ) )
			{
				highlightsPage = new PreviewPage( true );
				highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
				highlightsPage.setProvider( new HighlightDescriptorProvider( ) );
				setPageInput( highlightsPage );
				refresh( tabFolder, highlightsPage, true );
				item.setControl( highlightsPage.getControl( ) );
				itemMap.put( item, highlightsPage );
			}
			else if ( title.equals( FILTERTITLE ) )
			{
				filterPage = new FormPage( FormPropertyDescriptor.FULL_FUNCTION,
						new FilterHandleProvider( ),
						true,
						true );
				setPageInput( filterPage );
				refresh( tabFolder, filterPage, true );
				item.setControl( filterPage.getControl( ) );
				itemMap.put( item, filterPage );
			}
		}
		else if ( itemMap.get( item ) != null )
		{
			setPageInput( itemMap.get( item ) );
			refresh( tabFolder, itemMap.get( item ), false );
		}
	}

	public void createTabItems( List input )
	{
		super.createTabItems( input );
		this.input = input;
		addSelectionListener( this );
		createTabItems( );
		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}

	protected void createTabItems( )
	{
		createTabItem( BINDINGTITLE, ATTRIBUTESTITLE );
		createTabItem( GROUPSSTITLE, BINDINGTITLE );
		createTabItem( MAPTITLE, GROUPSSTITLE );
		createTabItem( HIGHLIGHTSTITLE, MAPTITLE );
		createTabItem( SORTINGTITLE, HIGHLIGHTSTITLE );
		createTabItem( FILTERTITLE, SORTINGTITLE );
	}

	public void createControl( Composite parent, Object input )
	{
		super.createControl( parent, input );
		createTabItems( (List) input );
	}
}