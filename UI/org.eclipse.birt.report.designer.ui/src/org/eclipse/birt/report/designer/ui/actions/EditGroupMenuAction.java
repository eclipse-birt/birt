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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Updates "Edit group" menu.
 */

public class EditGroupMenuAction extends MenuUpdateAction
{

	public static final String ID = "edit group menu"; //$NON-NLS-1$

	/**
	 * @param part
	 */
	public EditGroupMenuAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction#getItems()
	 */
	protected List getItems( )
	{
		ListingHandle parentHandle = null;
		if ( ( getTableEditPart( ) != null )
				&& !( getTableEditPart( ) instanceof GridEditPart ) )
		{
			parentHandle = (ListingHandle) getTableEditPart( ).getModel( );
		}
		else if ( getListEditPart( ) != null )
		{
			parentHandle = (ListingHandle) getListEditPart( ).getModel( );
		}
		else
		{
			return new ArrayList( );
		}

		SlotHandle handle = parentHandle.getGroups( );
		Iterator iter = handle.iterator( );
		ArrayList actionList = new ArrayList( );
		while ( iter.hasNext( ) )
		{
			GroupHandle groupHandle = (GroupHandle) iter.next( );
			actionList.add( new EditGroupAction( null, groupHandle ) );
		}
		return actionList;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part
	 *         is selected.
	 */
	protected TableEditPart getTableEditPart( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelectedObjects( ).get( i );

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
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		ListEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelectedObjects( ).get( i );

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
}