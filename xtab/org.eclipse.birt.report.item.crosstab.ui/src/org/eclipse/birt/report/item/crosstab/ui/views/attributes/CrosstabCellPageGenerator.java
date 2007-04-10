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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.PreviewPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.HighlightPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MapPropertyDescriptor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AbstractPageGenerator;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Administrator
 * 
 */
public class CrosstabCellPageGenerator extends AbstractPageGenerator
{

	protected PreviewPage mapPage;
	protected PreviewPage highlightsPage;

	public void createControl( Composite parent, Object input )
	{
		super.createControl( parent, input );
		createTabItems( (List) input );
	}

	protected void buildItemContent( CTabItem item )
	{
		if ( itemMap.containsKey( item ) && itemMap.get( item ) == null )
		{
			switch ( tabFolder.getSelectionIndex( ) )
			{
				case 1 :
					mapPage = new PreviewPage( true );
					mapPage.setPreview( new MapPropertyDescriptor( true ) );
					mapPage.setProvider( new MapDescriptorProvider( ) );
					setPageInput( mapPage );
					refresh( tabFolder, mapPage, true );
					item.setControl( mapPage.getControl( ) );
					itemMap.put( item, mapPage );
					break;
				case 2 :
					highlightsPage = new PreviewPage( true );
					highlightsPage.setPreview( new HighlightPropertyDescriptor( true ) );
					highlightsPage.setProvider( new HighlightDescriptorProvider( ) );
					setPageInput( highlightsPage );
					refresh( tabFolder, highlightsPage, true );
					item.setControl( highlightsPage.getControl( ) );
					itemMap.put( item, highlightsPage );
					break;
				default :
					break;
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
		if ( basicPage == null || basicPage.getControl( ).isDisposed( ) )
		{
			ISafeRunnable runnable = new ISafeRunnable( ) {

				public void run( ) throws Exception
				{
					CTabItem[] oldPages = tabFolder.getItems( );
					int index = tabFolder.getSelectionIndex( );
					for ( int i = 0; i < oldPages.length; i++ )
					{
						if ( oldPages[i].isDisposed( ) )
							continue;
						if ( index == i )
							continue;
						if ( oldPages[i].getControl( ) != null )
						{
							oldPages[i].getControl( ).dispose( );
						}
						oldPages[i].dispose( );
					}
					if ( index > -1 && !oldPages[index].isDisposed( ) )
					{
						oldPages[index].getControl( ).dispose( );
						oldPages[index].dispose( );
					}
				}

				public void handleException( Throwable exception )
				{
					/* not used */
				}
			};
			Platform.run( runnable );
			tabFolder.setLayout( new FillLayout( ) );
			basicPage = new BaseAttributePage( );
			basicPage.buildUI( tabFolder );
			CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
			tabItem.setText( Messages.getString( "CategoryPageGenerator.TabItem.Attributes" ) ); //$NON-NLS-1$
			tabItem.setControl( basicPage.getControl( ) );

			basicPage.setCategoryProvider( CrosstabCellCategoryProviderFactory.getInstance( )
					.getCategoryProvider( input ) );
		}
		this.input = input;
		basicPage.setInput( input );
		addSelectionListener( this );
		basicPage.refresh( );
		createTabItem( 1,
				Messages.getString( "CrosstabPageGenerator.TabItem.map" ) );
		createTabItem( 2,
				Messages.getString( "CrosstabPageGenerator.TabItem.Highlights" ) );

		if ( tabFolder.getSelection( ) != null )
			buildItemContent( tabFolder.getSelection( ) );
	}
}
