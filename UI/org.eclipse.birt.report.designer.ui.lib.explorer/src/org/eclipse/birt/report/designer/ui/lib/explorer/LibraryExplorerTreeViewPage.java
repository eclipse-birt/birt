/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.dnd.LibraryDragListener;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class represents the tree view page of the data view
 * 
 */
public class LibraryExplorerTreeViewPage extends LibraryExplorerViewPage implements
		IValidationListener,
		IPreferenceChangeListener
{

	// private static final String LABEL_DOUBLE_CLICK = Messages.getString(
	// "DataViewTreeViewerPage.tooltip.DoubleClickToEdit" ); //$NON-NLS-1$

	/**
	 * @deprecated
	 */
	private ILibraryProvider libraryProvider;

	private IEclipsePreferences reportPreferenceNode;
	private TreeViewer treeViewer;

	/**
	 * Creates the tree view
	 * 
	 * @param parent
	 *            the parent
	 */
	protected TreeViewer createTreeViewer( Composite parent )
	{
		treeViewer = new TreeViewer( parent, SWT.MULTI
				| SWT.H_SCROLL
				| SWT.V_SCROLL );
		configTreeViewer( );
		initPage( );
		return treeViewer;
	}

	/**
	 * Configures the tree viewer.
	 */
	protected void configTreeViewer( )
	{

		ViewsTreeProvider provider = new LibraryExplorerProvider( );

		treeViewer.setContentProvider( provider );
		treeViewer.setLabelProvider( provider );

		refreshRoot( );

		// Adds drag and drop support
		int ops = DND.DROP_COPY;
		Transfer[] transfers = new Transfer[]{
			TemplateTransfer.getInstance( )
		};
		treeViewer.addDragSupport( ops,
				transfers,
				new LibraryDragListener( treeViewer ) );

		treeViewer.getControl( ).addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{

			}

			public void keyReleased( KeyEvent e )
			{
				if ( e.keyCode == SWT.F5 )
				{
					treeViewer.refresh( );
				}
			}
		} );
	}

	/**
	 * Initializes the data view page.
	 */
	protected void initPage( )
	{
		createContextMenus( );

		treeViewer.setSorter( new ItemSorter( ) );
		treeViewer.getTree( ).addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == treeViewer.getTree( ) )
				{
					Point pt = new Point( event.x, event.y );
					TreeItem item = treeViewer.getTree( ).getItem( pt );
					treeViewer.getTree( ).setToolTipText( getTooltip( item ) );
				}
			}
		} );

		// Listen to preference change.
		IEclipsePreferences rootNode = (IEclipsePreferences) Platform.getPreferencesService( )
				.getRootNode( )
				.node( InstanceScope.SCOPE );
		final String reportName = ReportPlugin.getDefault( )
				.getBundle( )
				.getSymbolicName( );
		try
		{
			if ( rootNode.nodeExists( reportName ) )
			{
				reportPreferenceNode = (IEclipsePreferences) rootNode.node( reportName );
				reportPreferenceNode.addPreferenceChangeListener( this );
			}
		}
		catch ( BackingStoreException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Creates the context menu
	 */
	private void createContextMenus( )
	{
		MenuManager menuManager = new LibraryExplorerContextMenuProvider( treeViewer );

		Menu menu = menuManager.createContextMenu( treeViewer.getControl( ) );

		treeViewer.getControl( ).setMenu( menu );
		getSite( ).registerContextMenu( "#Pop up", menuManager, //$NON-NLS-1$
				getSite( ).getSelectionProvider( ) );
	}

	private String getTooltip( TreeItem item )
	{
		if ( item != null )
		{
			Object object = item.getData( );
			if ( object instanceof DataSourceHandle
					|| object instanceof DataSetHandle
					|| object instanceof ParameterHandle
					|| object instanceof ParameterGroupHandle
					|| object instanceof EmbeddedImageHandle )
			{
				return Messages.getString( "LibraryExplorerTreeViewPage.toolTips.DragAndDrapOutline" ); //$NON-NLS-1$
			}
			else if ( object instanceof ReportItemHandle
					|| object instanceof ThemeHandle )
			{
				return Messages.getString( "LibraryExplorerTreeViewPage.toolTips.DragAndDrapLayout" ); //$NON-NLS-1$
			}
			if ( object instanceof LibraryHandle )
			{
				return ( (LibraryHandle) object ).getFileName( );
			}
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code>
	 * method disposes of this page's control (if it has one and it has not
	 * already been disposed). Disposes the visitor of the element
	 */
	public void dispose( )
	{
		if ( reportPreferenceNode != null )
			reportPreferenceNode.removePreferenceChangeListener( this );

		super.dispose( );
	}

	protected boolean isDisposed( )
	{
		Control ctrl = getControl( );
		return ( ctrl == null || ctrl.isDisposed( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.validators.IValidationListener#elementValidated(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.api.validators.ValidationEvent)
	 */
	public void elementValidated( DesignElementHandle targetElement,
			ValidationEvent ev )
	{
		treeViewer.refresh( );
	}

	private void refreshRoot( )
	{
		treeViewer.setInput( new File( ReportPlugin.getDefault( )
				.getResourcePreference( ) ) );
	}

	/**
	 * @deprecated
	 */
	public void setLibraryProvider( ILibraryProvider provider )
	{
		this.libraryProvider = provider;
	}

	public void preferenceChange( PreferenceChangeEvent event )
	{
		if ( ReportPlugin.RESOURCE_PREFERENCE.equals( event.getKey( ) ) )
			Display.getDefault( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					refreshRoot( );
				}
			} );
	}
}