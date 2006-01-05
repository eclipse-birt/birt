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

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.Page;

/**
 * Basic class for represents the library view page.
 * 
 */
public abstract class LibraryExplorerViewPage extends Page implements
		ISelectionProvider
{

	private TreeViewer treeViewer;

	private ListenerList selectionChangedListeners = new ListenerList( );

	/**
	 * Creates the SWT control for this page under the given parent control.
	 * 
	 * @param parent
	 *            the parent control
	 */
	public void createControl( Composite parent )
	{
		treeViewer = createTreeViewer( parent );
//		getTreeViewer( ).addSelectionChangedListener( new ISelectionChangedListener( ) {
//
//			public void selectionChanged( SelectionChangedEvent event )
//			{
//				//treeSelect( event );
//			}
//
//		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPageBookViewPage#init(org.eclipse.ui.part.IPageSite)
	 */
	public void init( IPageSite pageSite )
	{
		super.init( pageSite );
		pageSite.setSelectionProvider( this );
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code>
	 * method returns <code>null</code> if the tree viewer is null. Returns
	 * the tree viewer's control if tree viewer is null
	 */
	public Control getControl( )
	{
		if ( treeViewer == null )
			return null;
		return treeViewer.getControl( );
	}

	/**
	 * Sets the focus to the tree viewer's control
	 */
	public void setFocus( )
	{
		treeViewer.getControl( ).setFocus( );
	}

	/**
	 * create the tree viewer of this page.
	 * 
	 * @param parent
	 *            this view page's parent.
	 * @return
	 */
	protected abstract TreeViewer createTreeViewer( Composite parent );

	/**
	 * Returns the tree viewer
	 * 
	 * @return the tree viewer
	 */
	public TreeViewer getTreeViewer( )
	{
		return treeViewer;
	}

	/**
	 * Selects the node
	 * 
	 * @param event
	 *            the selection changed event
	 */
	protected void treeSelect( SelectionChangedEvent event )
	{
		fireSelectionChanged( event.getSelection( ) );
	}

	/**
	 * Fires a selection changed event.
	 * 
	 * @param selection
	 *            the new selection
	 */
	protected void fireSelectionChanged( ISelection selection )
	{
		final SelectionChangedEvent event = new SelectionChangedEvent( this,
				selection );
		ReportRequest request = new ReportRequest( this );
		List list = new ArrayList( );
		if ( selection instanceof IStructuredSelection )
		{
			list = ( (IStructuredSelection) selection ).toList( );
		}
		request.setSelectionObject( list );
		request.setType( ReportRequest.SELECTION );
		// no convert
		// request.setRequestConvert(new EditorReportRequestConvert());
		// SessionHandleAdapter.getInstance().getMediator().pushState();
		SessionHandleAdapter.getInstance( )
				.getMediator( )
				.notifyRequest( request );

		// create an event
		// fire the event
		Object[] listeners = selectionChangedListeners.getListeners( );
		for ( int i = 0; i < listeners.length; ++i )
		{
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			Platform.run( new SafeRunnable( ) {

				public void run( )
				{
					l.selectionChanged( event );
				}
			} );
		}
	}

	/**
	 * Notifies that the selection has changed.
	 * 
	 * @param event
	 *            event object describing the change
	 */
	public void selectionChanged( SelectionChangedEvent event )
	{
		setSelection( event.getSelection( ) );
	}

	/**
	 * Adds a listener for selection changes in this selection provider. Has no
	 * effect if an identical listener is already registered.
	 * 
	 * @param listener
	 *            a selection changed listener
	 */
	public void addSelectionChangedListener( ISelectionChangedListener listener )
	{
		selectionChangedListeners.add( listener );
	}

	/**
	 * Returns the current selection for this provider.
	 * 
	 * @return the current selection
	 */
	public ISelection getSelection( )
	{
		if ( getTreeViewer( ) == null )
		{
			return StructuredSelection.EMPTY;
		}
		return getTreeViewer( ).getSelection( );
	}

	/**
	 * Removes the given selection change listener from this selection provider.
	 * Has no affect if an identical listener is not registered.
	 * 
	 * @param listener
	 *            a selection changed listener
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener )
	{
		selectionChangedListeners.remove( listener );
	}

	/**
	 * Sets the current selection for this selection provider.
	 * 
	 * @param selection
	 *            the new selection
	 */
	public void setSelection( ISelection selection )
	{
		if ( getTreeViewer( ) != null )
		{
			getTreeViewer( ).setSelection( selection );
		}

	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code>
	 * method disposes of this page's control (if it has one and it has not
	 * already been disposed).
	 */
	public void dispose( )
	{
		selectionChangedListeners.clear( );
		treeViewer = null;

		super.dispose( );
	}

}