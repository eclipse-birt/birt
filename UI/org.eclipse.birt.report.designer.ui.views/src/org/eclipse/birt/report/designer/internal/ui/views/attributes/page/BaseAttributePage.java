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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import java.util.HashMap;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.Tab;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyList;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyTitle;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyList.ListElement;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormWidgetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.ICategoryPage;
import org.eclipse.birt.report.designer.ui.views.attributes.TabPage;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ICategoryProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * The container page of AttributePages.
 */
public class BaseAttributePage extends TabPage
{

	/**
	 * The list control contains categories of a DE element attributes.
	 */
	private TabbedPropertyList categoryList;

	/**
	 * Container that material category attribute page will reside in.
	 */
	private Composite infoPane;

	/**
	 * ICategoryProvider instance, responds to the selection changing in the
	 * categoryList.
	 */
	private ICategoryProvider categoryProvider;

	/**
	 * The Last Selected index in the list of categories
	 */
	private static int s_lastSelectedIndex = 0;

	/**
	 * The current selection.
	 */
	private Object input;

	/**
	 * The map keeps the relationships between category label & pane
	 */
	private HashMap pageMap;

	private ScrolledComposite sComposite;

	private TabPage currentPage = null;

	private TabbedPropertyTitle title;


	/**
	 * Creates UI control.
	 * 
	 */
	Composite container;
	public void buildUI( Composite parent )
	{
		container = new Composite(parent,SWT.NONE);
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layout.numColumns = 2;
		container.setLayout( layout );

		categoryList = new TabbedPropertyList( container,
				FormWidgetFactory.getInstance( ) );
		GridData gd = new GridData( GridData.FILL_VERTICAL );
		gd.verticalSpan = 2;
		categoryList.setLayoutData( gd );
		categoryList.addListener( SWT.Selection, new Listener( ) {

			public void handleEvent( Event event )
			{
				if ( categoryList.getSelectionIndex( ) > -1 )
				{
					BaseAttributePage.s_lastSelectedIndex = categoryList.getSelectionIndex( );
				}
				processListSelected( );

			}
		} );
		setCategoryProvider(categoryProvider);
		title = new TabbedPropertyTitle( container, FormWidgetFactory.getInstance( ) );
		title.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sComposite = new ScrolledComposite( container, SWT.H_SCROLL | SWT.V_SCROLL );
		sComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		sComposite.setExpandHorizontal( true );
		sComposite.setExpandVertical( true );
		sComposite.addControlListener( new ControlAdapter( ) {

			public void controlResized( ControlEvent e )
			{
				computeSize( );
			}
		} );
		infoPane = new Composite( sComposite, SWT.NONE );
		sComposite.setContent( infoPane );
		layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		infoPane.setLayout( layout );
	}

	private void computeSize( )
	{
		sComposite.setMinSize( infoPane.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
		infoPane.layout( );
	}

	/**
	 * When selection changed in the category list, re-sets the top-level
	 * category pane correspond to the current selected category.
	 */
	private void processListSelected( )
	{
		if ( categoryProvider == null )
		{
			return;
		}
		int index = categoryList.getSelectionIndex( );
		if ( index == -1 )
		{
			return;
		}
		ListElement element = (ListElement) categoryList.getElementAt( categoryList.getSelectionIndex( ) );
		title.setTitle( element.getText( ), null );
		TabPage page = getCategoryPane( categoryList.getSelectionIndex( ) );
		if ( page == null )
		{
			return;
		}
		showPage( page );
	}

	private void showPage( TabPage page )
	{
		if ( page != currentPage )
		{
			if ( currentPage != null )
			{
				( (GridData) currentPage.getControl( ).getLayoutData( ) ).exclude = true;
				currentPage.getControl( ).setVisible( false );
			}
			( (GridData) page.getControl( ).getLayoutData( ) ).exclude = false;
			page.getControl( ).setVisible( true );
			currentPage = page;
			computeSize( );
		}
	}

	protected void selectStickyCategory( )
	{
		// select the last item that was selected by the user. If out of
		// bounds select the last one
		if ( s_lastSelectedIndex != -1 )
		{
			categoryList.setSelection( s_lastSelectedIndex );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.attributes.component.TabPage#setInput(java.util.List)
	 */
	public void setInput( Object input )
	{
		this.input = input;
	}
	
	public void refresh(){
		selectStickyCategory( );
		processListSelected( );
	}

	/**
	 * Sets CategoryProvider
	 * 
	 * @param categoryProvider
	 *            The categoryListener to set.
	 */
	public void setCategoryProvider( ICategoryProvider categoryProvider )
	{
		this.categoryProvider = categoryProvider;
		if ( categoryProvider == null )
		{
			return;
		}
		if(categoryList == null)return;
		ICategoryPage[] pages = categoryProvider.getCategories( );
		if ( pages.length != 0 )
		{
			Tab[] categoryLabels = new Tab[pages.length];
			for ( int i = 0; i < pages.length; i++ )
			{
				categoryLabels[i] = new Tab( );
				categoryLabels[i].setText( pages[i].getDisplayLabel( ) );
			}
			categoryList.setElements( categoryLabels );
			if ( categoryList.getTabList( ).length > 0 )
			{
				categoryList.setSelection( 0 );
			}
		}
	}

	private TabPage getCategoryPane( int index )
	{
		if ( pageMap == null )
		{
			pageMap = new HashMap( categoryProvider.getCategories( ).length );
		}
		String key = Integer.toString( index );
		TabPage page = (TabPage) pageMap.get( key );
		if ( page == null )
		{
			page = categoryProvider.getCategories( )[index].createPage( );
			page.setInput( input );
			page.buildUI( infoPane );
		}
		else
			page.setInput( input );
		page.getControl( ).setLayoutData( new GridData( GridData.FILL_BOTH ) );
		page.refresh( );
		pageMap.put( key, page );
		return page;
	}

	

	public void dispose( )
	{
		container.dispose( );
	}

	public Control getControl( )
	{
		return container;
	}


}