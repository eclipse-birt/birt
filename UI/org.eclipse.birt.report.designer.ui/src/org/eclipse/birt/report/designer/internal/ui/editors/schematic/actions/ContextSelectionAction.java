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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Selection action within context menu.
 */

public class ContextSelectionAction extends SelectionAction
{

	/**
	 * @param part
	 * @param style
	 */
	public ContextSelectionAction( IWorkbenchPart part, int style )
	{
		super( part, style );
	}

	/**
	 * @param part
	 */
	public ContextSelectionAction( IWorkbenchPart part )
	{
		super( part );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return false;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
	 */
	protected TableEditPart getTableEditPart( )
	{
		List list = getSelectedObjects( );
		if ( list.isEmpty( ) )
			return null;
		TableEditPart part = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof TableEditPart )
			{
				part = (TableEditPart) obj;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
			}
		}
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart( )
	{
		List list = getSelectedObjects( );
		if ( list.isEmpty( ) )
			return null;
		ListEditPart part = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof ListEditPart )
			{
				part = (ListEditPart) obj;
			}
			else if ( obj instanceof ListBandEditPart )
			{
				part = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
			}
		}
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected Object getListGroup( )
	{
		List list = getSelectedObjects( );
		if ( list.size( ) != 1 )
		{
			return null;
		}
		if ( list.get( 0 ) instanceof ListBandEditPart )
		{
			ListBandProxy group = (ListBandProxy) ( (ListBandEditPart) list.get( 0 ) ).getModel( );
			if ( group.getElemtHandle( ) instanceof ListGroupHandle )
			{
				return group;
			}
		}
		return null;
	}

	/**
	 * Gets the current selected table group object.
	 * 
	 * @return the selected table group object
	 */
	protected Object getTableGroup( )
	{
		List list = getSelectedObjects( );
		if ( list.size( ) != 1 )
		{
			return null;
		}
		if ( list.get( 0 ) instanceof DummyEditpart )
		{
			DummyEditpart part = (DummyEditpart) list.get( 0 );
			if ( part.getModel( ) instanceof RowHandle )
			{
				RowHandle group = (RowHandle) part.getModel( );
				if ( group.getContainer( ) instanceof TableGroupHandle )
				{
					return group;
				}
			}
		}
		return null;
	}

	/**
	 * Gets the current selected column objects.
	 * 
	 * @return The current column objects
	 */
	protected List getColumnHandles( )
	{
		List list = getSelectedObjects( );
		if ( list.isEmpty( ) )
			return Collections.EMPTY_LIST;
		List columnHandles = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof DummyEditpart )
			{
				if ( ( (DummyEditpart) obj ).getModel( ) instanceof ColumnHandle )
				{
					columnHandles.add( ( (DummyEditpart) obj ).getModel( ) );
				}
			}
		}
		return columnHandles;
	}

	/**
	 * Gets the current selected row objects.
	 * 
	 * @return The current selected row objects.
	 */

	protected List getRowHandles( )
	{
		List list = getSelectedObjects( );
		if ( list.isEmpty( ) )
			return Collections.EMPTY_LIST;
		List rowHandles = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof DummyEditpart )
			{
				if ( ( (DummyEditpart) obj ).getModel( ) instanceof RowHandle )
				{
					rowHandles.add( ( (DummyEditpart) obj ).getModel( ) );
				}
			}
		}
		return rowHandles;
	}

	/**
	 * Gets models of selected elements
	 * 
	 * @return
	 */
	protected List getElementHandles( )
	{
		List list = getSelectedObjects( );
		if ( list.isEmpty( ) )
			return Collections.EMPTY_LIST;
		List handles = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			handles.add( ( (EditPart) ( list.get( i ) ) ).getModel( ) );
		}
		return handles;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesign( )
				.getActivityStack( );
	}

}