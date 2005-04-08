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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.notification.DeferredRefreshManager;
import org.eclipse.birt.report.designer.internal.ui.editors.notification.ReportDeferredUpdateManager;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 * @author David Michonneau
 */
public class DeferredGraphicalViewer extends ScrollingGraphicalViewer
{
	private DomainEventDispatcher eventDispatcher;
	/**
	 * The actual layout area size.
	 */
	public static final String LAYOUT_SIZE = "Layout Size"; //$NON-NLS-1$
	
	public static final String REPORT_SIZE = "Report Size"; //$NON-NLS-1$

	public static final String PROPERTY_MARGIN_VISIBILITY = "Property Margin Visibility"; //$NON-NLS-1$
	
	public void hookRefreshListener( DeferredRefreshManager refreshManager )
	{
		ReportDeferredUpdateManager updateManager = new ReportDeferredUpdateManager( );
		updateManager.setRefreshManager( refreshManager );
		getLightweightSystem( ).setUpdateManager( updateManager );
	}

	/**
	 * Sets the selection to the given selection and fires selection changed.
	 * The ISelection should be an {@link IStructuredSelection}or it will be
	 * ignored.
	 * 
	 * @see ISelectionProvider#setSelection(ISelection)
	 */
	public void setSelection( ISelection newSelection )
	{
		if ( !( newSelection instanceof IStructuredSelection ) )
			return;

		List editparts = ( (IStructuredSelection) newSelection ).toList( );
		List selection = primGetSelectedEditParts( );

		setFocus( null );
		for ( int i = 0; i < selection.size( ); i++ )
			( (EditPart) selection.get( i ) )
					.setSelected( EditPart.SELECTED_NONE );
		selection.clear( );

		editparts = flitterEditpart( editparts );
		//for create handle
		selection.addAll( editparts );

		for ( int i = 0; i < editparts.size( ); i++ )
		{
			EditPart part = (EditPart) editparts.get( i );
			if ( i == editparts.size( ) - 1 )
				part.setSelected( EditPart.SELECTED_PRIMARY );
			else
				part.setSelected( EditPart.SELECTED );

		}

		fireSelectionChanged( );
	}

	/**
	 * @param editparts
	 */
	private List flitterEditpart( List editparts )
	{
		int size = editparts.size( );

		boolean hasCell = false;
		boolean hasOther = false;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = ( (EditPart) editparts.get( i ) ).getModel( );
			if ( obj instanceof CellHandle || obj instanceof RowHandle
					|| obj instanceof ColumnHandle )
			{
				hasCell = true;
			}
			else
			{
				hasOther = true;
			}
		}
		if ( hasCell && hasOther )
		{
			List copy = new ArrayList( editparts );
			for ( int i = 0; i < size; i++ )
			{
				EditPart part = (EditPart) editparts.get( i );
				Object obj = part.getModel( );

				if ( obj instanceof CellHandle || obj instanceof RowHandle
						|| obj instanceof ColumnHandle )
				{
					copy.remove( part );
				}
			}
			editparts = copy;
		}
		return editparts;
	}

	/**
	 * @see GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
	 */
	public Handle findHandleAt( Point p )
	{
		LayerManager layermanager = (LayerManager) getEditPartRegistry( ).get(
				LayerManager.ID );
		if ( layermanager == null )
			return null;
		List list = new ArrayList( 3 );
		//list.add(layermanager.getLayer(LayerConstants.PRIMARY_LAYER));
		list.add( layermanager.getLayer( LayerConstants.CONNECTION_LAYER ) );
		list.add( layermanager.getLayer( LayerConstants.FEEDBACK_LAYER ) );
		IFigure handle = getLightweightSystem( ).getRootFigure( )
				.findFigureAtExcluding( p.x, p.y, list );
		if ( handle instanceof Handle )
			return (Handle) handle;
		return null;
	}

	/**
	 * Exposes it to public.
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.ScrollingGraphicalViewer#getFigureCanvas()
	 */
	public FigureCanvas getFigureCanvas( )
	{
		return super.getFigureCanvas( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#appendSelection(org.eclipse.gef.EditPart)
	 */
	public void appendSelection( EditPart editpart )
	{
		if ( editpart != focusPart ) 
			setFocus( null );
		List list = primGetSelectedEditParts( );
		list.remove( editpart );
		list.add( editpart );

		setSelection( new StructuredSelection( list ) );
	}
	
	public void setEditDomain(EditDomain domain) 
	{
		super.setEditDomain(domain);
		eventDispatcher = new ReportDomainEventDispatcher(domain, this);
		eventDispatcher.setEnableKeyTraversal(true);
		getLightweightSystem()
		.setEventDispatcher(eventDispatcher);
		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerImpl#getEventDispatcher()
	 */
	protected DomainEventDispatcher getEventDispatcher( )
	{
		return eventDispatcher;
	}
}