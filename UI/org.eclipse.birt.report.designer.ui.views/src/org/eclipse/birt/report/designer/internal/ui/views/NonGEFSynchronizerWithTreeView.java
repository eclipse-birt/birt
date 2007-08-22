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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * Provides synchronizing between tree view and graphical views.
 */
public class NonGEFSynchronizerWithTreeView implements IColleague
{

	private AbstractTreeViewer viewer;

	// private ListenerList selectionChangedListeners = new ListenerList( );

	private Object source;

	/**
	 * @return Returns the source.
	 */
	public Object getSource( )
	{
		return source;
	}

	/**
	 * @param source
	 *            The source to set.
	 */
	public void setSource( Object source )
	{
		this.source = source;
	}

	public NonGEFSynchronizerWithTreeView( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection( )
	{
		if ( getTreeViewer( ) == null )
		{
			return StructuredSelection.EMPTY;
		}
		return getTreeViewer( ).getSelection( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection( ISelection selection )
	{
		if ( getTreeViewer( ) != null )
		{
			getTreeViewer( ).setSelection( selection, true );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	public void selectionChanged( SelectionChangedEvent event )
	{
		setSelection( event.getSelection( ) );
	}

	/**
	 * select the node
	 * 
	 * @param event
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
		ReportRequest request = new ReportRequest( getSource( ) );
		List list = new ArrayList( );
		if ( selection instanceof IStructuredSelection )
		{
			list = ( (IStructuredSelection) selection ).toList( );
		}
		request.setSelectionObject( list );
		request.setType( ReportRequest.SELECTION );
		// no convert
		// request.setRequestConvert(new EditorReportRequestConvert());

		SessionHandleAdapter.getInstance( )
				.getMediator( )
				.notifyRequest( request );
	}

	/**
	 * gets the Tree viewer that hooks on this synchronizer.
	 * 
	 * @return tree viewer.
	 */
	public AbstractTreeViewer getTreeViewer( )
	{
		return viewer;
	}

	/**
	 * Hook the tree view need to synchronized
	 * 
	 * @param viewer
	 */
	public void setTreeViewer( AbstractTreeViewer viewer )
	{
		this.viewer = viewer;
		getTreeViewer( ).addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				treeSelect( event );
			}

		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.Page#dispose()
	 */
	public void dispose( )
	{
		viewer = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest(org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest)
	 */
	public void performRequest( ReportRequest request )
	{
		if ( ReportRequest.SELECTION.equals( request.getType( ) ) )
		{
			handleSelectionChange( request );
		}
		else if ( ReportRequest.CREATE_ELEMENT.equals( request.getType( ) ) )
		{
			handleCreateElement( request );
		}
	}

	protected void handleCreateElement( ReportRequest request )
	{
		final List list = request.getSelectionObject( );
		if ( list.size( ) == 1 )
		{
			Display.getCurrent( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					viewer.refresh( );
					StructuredSelection selection = new StructuredSelection( list );
					viewer.setSelection( selection );
					//fireSelectionChanged( selection );
				}

			} );
		}

	}

	/**
	 * Handles the selection request
	 * 
	 * @param request
	 */
	protected void handleSelectionChange( ReportRequest request )
	{
		if ( request.getSource( ) == getSource( ) )
		{
			return;
		}
		List list = request.getSelectionModelList( );
		boolean canSetSelection = false;
		for ( Iterator iter = list.iterator( ); iter.hasNext( ); )
		{
			Object element = iter.next( );
			if ( UIUtil.containElement( getTreeViewer( ), element ) )
			{
				canSetSelection = true;
				break;
			}
		}
		if ( canSetSelection )
		{
			setSelection( new StructuredSelection( list ) );
		}
	}

}