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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * DefaultPageGenerator removes all <code>TabPage</code> from
 * <code>TabFolder</code>.
 */
public class TabPageGenerator implements IPageGenerator
{

	protected int tabIndex = 0;

	protected String tabText;

	/**
	 * Creates attribute pages
	 * 
	 * @param tabFolder
	 *            The attribute tabFolder.
	 * @param input
	 *            The current selection.
	 */

	public void createTabItems( final List input )
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

	}

	protected List input;
	protected CTabFolder tabFolder;

	public void createControl( Composite parent, Object input )
	{
		this.input = (List) input;
		if ( tabFolder == null || tabFolder.isDisposed( ) )
		{
			tabFolder = new CTabFolder( parent, SWT.TOP );
			tabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			createTabItems( this.input );
		}
		showPropertiesPage( );

	}

	public Control getControl( )
	{
		return tabFolder;
	}

	protected void showPropertiesPage( )
	{
		if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) != null )
		{
			if ( tabFolder == null || tabFolder.isDisposed( ) )
				return;
			selectStickyTab( );
			tabFolder.getParent( ).layout( true );
		}
	}

	/**
	 * Sticky tab behaviour. We try to set the default selection on the previous
	 * chosen tab by the user or the nearest one.
	 */
	private void selectStickyTab( )
	{
		CTabItem[] items = tabFolder.getItems( );
		boolean tabFound = false;
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].getText( ).equals( tabText ) )
			{
				tabFolder.setSelection( i );
				tabFound = true;
				break;
			}
		}
		// we didn't find the tab, select the one with the closest tabIndex
		// instead
		if ( !tabFound )
		{
			if ( tabIndex > tabFolder.getItemCount( ) - 1 )
			{
				tabFolder.setSelection( tabFolder.getItemCount( ) - 1 );
			}
			else
			{
				tabFolder.setSelection( tabIndex );
			}
		}
	}

	protected FolderSelectionAdapter listener;

	protected void addSelectionListener( TabPageGenerator generator )
	{
		if ( listener == null )
		{
			listener = new FolderSelectionAdapter( generator );
			tabFolder.addSelectionListener( listener );
		}
		else {
			tabFolder.removeSelectionListener( listener );
			tabFolder.addSelectionListener( listener );
		}
	}

	class FolderSelectionAdapter extends SelectionAdapter
	{

		TabPageGenerator generator;

		public FolderSelectionAdapter( TabPageGenerator generator )
		{
			this.generator = generator;
		}

		public void widgetSelected( SelectionEvent e )
		{
			if ( tabFolder != null )
			{
				tabIndex = tabFolder.getSelectionIndex( );
				if ( tabFolder.getSelection( ) != null )
				{
					tabText = tabFolder.getSelection( ).getText( );
					generator.createTabItems( input );
				}
			}
		}
	}
}