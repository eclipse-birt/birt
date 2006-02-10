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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ListenerElementVisitor;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.dnd.LibraryDragListener;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.ElementDeletedEvent;
import org.eclipse.birt.report.model.api.command.LibraryEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
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
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.osgi.service.prefs.BackingStoreException;

/**
 * This class represents the tree view page of the data view
 * 
 */
public class LibraryExplorerTreeViewPage extends LibraryExplorerViewPage implements
		Listener,
		IValidationListener,
		ActivityStackListener,
		IPreferenceChangeListener
{

//	private static final String LABEL_DOUBLE_CLICK = Messages.getString( "DataViewTreeViewerPage.tooltip.DoubleClickToEdit" ); //$NON-NLS-1$
	private ListenerElementVisitor visitor ;
	private List dataSetsToRefresh = new ArrayList( );
	private ILibraryProvider libraryProvider;
	private IEclipsePreferences reportPreferenceNode;
	private TreeViewer treeViewer;

	/**
	 * constructor
	 * 
	 * @param reportHandle
	 *            the handle of the report design
	 */
	public LibraryExplorerTreeViewPage( )
	{
		super( );
		SessionHandleAdapter.getInstance( )
				.getCommandStack( )
				.addListener( this );
	}

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
				| SWT.V_SCROLL ) {

			protected void inputChanged( Object input, Object oldInput )
			{
				super.inputChanged( input, oldInput );
				removeListener( oldInput );
				addListener( input );
			}
		};
		configTreeViewer( );
		initPage( );
		return treeViewer;
	}

	/**
	 * Configures the tree viewer.
	 */
	protected void configTreeViewer( )
	{

		ViewsTreeProvider provider = new ViewsTreeProvider( ) {

			public Image getImage( Object element )
			{
				if ( element instanceof LibraryHandle )
				{
					Image image = libraryProvider.getDisplayIcon( (LibraryHandle) element );
					if ( image == null )
					{
						image = ReportPlatformUIImages.getImage( element );
					}
					return image;
				}
				return super.getImage( element );
			}
		};
		treeViewer.setContentProvider( provider );
		treeViewer.setLabelProvider( provider );

		refreshRoot( );

		// add inline renaming support

		// Adds drag and drop support
		int ops = DND.DROP_COPY;
		Transfer[] transfers = new Transfer[]{
			TemplateTransfer.getInstance( )
		};
		treeViewer.addDragSupport( ops,
				transfers,
				new LibraryDragListener( treeViewer ) );

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
//			if ( object instanceof DataSourceHandle
//					|| object instanceof ParameterGroupHandle )
//			{
//				return LABEL_DOUBLE_CLICK;
//			}
//			StringBuffer tooltip = new StringBuffer( );
//			boolean canInsert = InsertInLayoutUtil.handleValidateInsertToLayout( object,
//					UIUtil.getCurrentEditPart( ) );
//			String text = "(" + item.getText( ) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
//			if ( object instanceof DataSetHandle )
//			{
//				if ( canInsert )
//					tooltip.append( Messages.getString( "DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns" ) //$NON-NLS-1$
//							+ text
//							+ "; " ); //$NON-NLS-1$
//				tooltip.append( LABEL_DOUBLE_CLICK );
//			}
//			if ( object instanceof DataSetItemModel )
//			{
//				if ( canInsert )
//					tooltip.append( Messages.getString( "DataViewTreeViewerPage.tooltip.DragToInsertColumn" ) + text ); //$NON-NLS-1$
//			}
//			if ( object instanceof ParameterHandle )
//			{
//				if ( canInsert )
//					tooltip.append( Messages.getString( "DataViewTreeViewerPage.tooltip.DragToInsertParameter" ) ); //$NON-NLS-1$
//				tooltip.append( LABEL_DOUBLE_CLICK );
//			}
//			return tooltip.toString( );

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
		removeListener( treeViewer.getInput( ) );
		if ( visitor != null )
		{
			visitor.removeListener( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) );
			visitor.dispose( );
			visitor = null;
		}

		if ( reportPreferenceNode != null )
			reportPreferenceNode.removePreferenceChangeListener( this );

		SessionHandleAdapter.getInstance( )
				.getCommandStack( )
				.removeListener( this );
		super.dispose( );
	}

	/**
	 * Refreshes the focus and the focus container of the tree view. And applies
	 * the visitor to the given focus.
	 * 
	 * @param focus
	 *            the design element
	 * @param ev
	 *            the notification event
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( focus instanceof DataSetHandle )
		{
			if ( !( ev instanceof ElementDeletedEvent ) )
			{
				if ( !dataSetsToRefresh.contains( focus ) )
					dataSetsToRefresh.add( focus );
			}
			else
			{
				if ( dataSetsToRefresh.contains( focus ) )
					dataSetsToRefresh.remove( focus );
			}
		}
		else if ( focus instanceof ModuleHandle
				&& ev.getEventType( ) == NotificationEvent.CONTENT_EVENT )
		{
			IDesignElement element = ( (ContentEvent) ev ).getContent( );
			if ( ( (ContentEvent) ev ).getAction( ) == ContentEvent.REMOVE
					&& element.getDefn( )
							.getName( )
							.equals( ReportDesignConstants.ODA_DATA_SET ) )
			{
				DataSetManager.getCurrentInstance( )
						.removeCachedColumns( element.getHandle( SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getModule( ) ) );
			}
			treeViewer.refresh( );
		}
		else if ( ev instanceof LibraryEvent )
		{
			refreshRoot( );
		}
		else
		{
			treeViewer.refresh( );
		}
		getListenerElementVisitor( ).addListener( focus );
	}

	/**
	 * Gets the visitor.
	 * 
	 * @return the visitor
	 */
	private ListenerElementVisitor getListenerElementVisitor( )
	{
		if ( visitor == null )
		{
			visitor = new ListenerElementVisitor( this );
		}
		return visitor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.ActivityStackListener#stackChanged(org.eclipse.birt.report.model.activity.ActivityStackEvent)
	 */
	public void stackChanged( ActivityStackEvent event )
	{
		if ( !dataSetsToRefresh.isEmpty( ) )
		{
			PlatformUI.getWorkbench( )
					.getDisplay( )
					.asyncExec( new Runnable( ) {

						public void run( )
						{
							if ( !isDisposed( ) )
							{
								for ( int i = 0; i < dataSetsToRefresh.size( ); i++ )
								{
									DataSetHandle dataSetToRefresh = (DataSetHandle) dataSetsToRefresh.get( i );
									DataSetManager.getCurrentInstance( )
											.refresh( dataSetToRefresh );
									treeViewer.refresh( dataSetToRefresh );
								}
								dataSetsToRefresh.clear( );
							}
						}
					} );
		}
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
		treeViewer.setInput( libraryProvider.getLibraries( ) );
	}

	public void setLibraryProvider( ILibraryProvider provider )
	{
		this.libraryProvider = provider;
	}

	private void addListener( Object input )
	{
		if ( input instanceof Object[] )
		{
			Object[] libs = (Object[]) input;
			for ( int i = 0; i < libs.length; i++ )
			{
				if ( libs[i] instanceof LibraryHandle )
				{
					getListenerElementVisitor().addListener( (LibraryHandle) libs[i] );
				}
			}
		}
	}

	private void removeListener( Object input )
	{
		if ( input instanceof Object[] )
		{
			Object[] libs = (Object[]) input;
			for ( int i = 0; i < libs.length; i++ )
			{
				if ( libs[i] instanceof LibraryHandle )
				{
					getListenerElementVisitor().addListener( (LibraryHandle) libs[i] );
				}
			}
		}
	}

	public void preferenceChange( PreferenceChangeEvent event )
	{
		if ( ReportPlugin.LIBRARY_PREFERENCE.equals( event.getKey( ) ) )
			Display.getDefault( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					refreshRoot( );
				}
			} );
	}
}