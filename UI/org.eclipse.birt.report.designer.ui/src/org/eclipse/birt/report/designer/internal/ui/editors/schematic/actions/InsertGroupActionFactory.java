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

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.jface.action.Action;

/**
 * The factory for creating actions to insert table or list group in the
 * position
 */

public class InsertGroupActionFactory
{

	private static InsertPositionGroupAction[] instances = new InsertPositionGroupAction[]{
			new InsertAboveGroupAction( null,
					Messages.getString( "InsertPositionGroupAction.Label.Above" ) ), //$NON-NLS-1$
			new InsertBelowGroupAction( null,
					Messages.getString( "InsertPositionGroupAction.Label.Below" ) ), //$NON-NLS-1$
//			new InsertIntoGroupAction( null,
//					Messages.getString( "InsertPositionGroupAction.Label.Into" ) ) //$NON-NLS-1$
	};

	/**
	 * Gets actions array
	 * 
	 * @param selection
	 *            selected editparts
	 * @return actions array
	 */
	public static Action[] getInsertGroupActions( List selection )
	{
		initInstances( selection );
		return instances;
	}

	private static void initInstances( List selection )
	{
		for ( int i = 0; i < instances.length; i++ )
		{
			instances[i].setSelection( selection );
		}
	}
}

abstract class InsertPositionGroupAction extends Action
{

	private static final String STACK_MSG_ADD_GROUP = Messages.getString( "AddGroupAction.stackMsg.addGroup" ); //$NON-NLS-1$

	private Object currentModel;

	private List selection;

	protected static final int POSITION_TOP_LEVEL = 0;
	protected static final int POSITION_INNERMOST = -1;

	protected InsertPositionGroupAction( List selection, String text )
	{
		super( );
		this.selection = selection;
		setText( text );
	}

	public void setSelection( List selection )
	{
		this.selection = selection;
		this.currentModel = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		return getTableEditPart( ) != null || getListEditPart( ) != null;
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		CommandStack stack = getActiveCommandStack( );
		stack.startTrans( STACK_MSG_ADD_GROUP );
		boolean retValue = false;
		if ( getTableEditPart( ) != null )
		{
			retValue = getTableEditPart( ).insertGroup( getPosition( ) );
		}
		else
		{
			retValue = getListEditPart( ).insertGroup( getPosition( ) );
		}
		if ( retValue )
		{
			stack.commit( );
		}
		else
		{
			stack.rollbackAll( );
		}
	}

	protected boolean isGroup( )
	{
		if ( getRowHandle( ) != null )
		{
			return getRowHandle( ).getContainer( ) instanceof GroupHandle;
		}
		else if ( getListBandProxy( ) != null )
		{
			return getListBandProxy( ).getElemtHandle( ) instanceof GroupHandle;
		}
		return false;
	}

	/**
	 * Returns if the order is reverse
	 * 
	 * @return true when slot is not footer
	 */
	protected boolean isNotReverse( )
	{
		if ( currentModel != null )
		{
			if ( isGroup( ) )
			{
				if ( getRowHandle( ) != null )
				{
					return getRowHandle( ).getContainerSlotHandle( )
							.getSlotID( ) != IGroupElementModel.FOOTER_SLOT;
				}
				else if ( getListBandProxy( ) != null )
				{
					return getListBandProxy( ).getSlotId( ) != IGroupElementModel.FOOTER_SLOT;
				}
			}
			else
			{
				if ( getRowHandle( ) != null )
				{
					return getRowHandle( ).getContainerSlotHandle( )
							.getSlotID( ) != TableHandle.FOOTER_SLOT;
				}
				else if ( getListBandProxy( ) != null )
				{
					return getListBandProxy( ).getSlotId( ) != ListHandle.FOOTER_SLOT;
				}
			}
		}
		return true;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part
	 *         is selected.
	 */
	protected TableEditPart getTableEditPart( )
	{
		if ( getSelection( ) == null || getSelection( ).isEmpty( ) )
			return null;
		List list = getSelection( );
		int size = list.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelection( ).get( i );
			if ( i == 0 && obj instanceof ReportElementEditPart )
			{
				currentModel = ( (ReportElementEditPart) obj ).getModel( );
			}

			if ( obj instanceof TableEditPart )
			{
				part = (TableEditPart) obj;
				break;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
				break;
			}
		}
		//Only table permitted
		if ( part instanceof GridEditPart )
			return null;
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
		if ( getSelection( ) == null || getSelection( ).isEmpty( ) )
			return null;
		List list = getSelection( );
		int size = list.size( );
		ListEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelection( ).get( i );
			if ( i == 0 && obj instanceof ReportElementEditPart )
			{
				currentModel = ( (ReportElementEditPart) obj ).getModel( );
			}

			if ( obj instanceof ListEditPart )
			{
				part = (ListEditPart) obj;
				break;
			}
			else if ( obj instanceof ListBandEditPart )
			{
				part = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
				break;
			}
		}
		return part;
	}

	public List getSelection( )
	{
		return selection;
	}

	protected RowHandle getRowHandle( )
	{
		if ( currentModel instanceof RowHandle )
		{
			return (RowHandle) currentModel;
		}
		else if ( currentModel instanceof CellHandle )
		{
			return (RowHandle) ( (CellHandle) currentModel ).getContainer( );
		}
		return null;
	}

	protected ListBandProxy getListBandProxy( )
	{
		if ( currentModel instanceof ListBandProxy )
		{
			return (ListBandProxy) currentModel;
		}
		return null;
	}

	/**
	 * Gets the activity stack of the report
	 * 
	 * @return returns the stack
	 */
	protected CommandStack getActiveCommandStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack();
	}

	/**
	 * Returns the current position of the selected part
	 * 
	 * @return the current position of the selected part
	 */
	protected int getCurrentPosition( )
	{
		if ( currentModel != null && isGroup( ) )
		{
			if ( getRowHandle( ) != null )
			{
				DesignElementHandle group = getRowHandle( ).getContainer( );
				TableHandle table = (TableHandle) group.getContainer( );
				return DEUtil.findInsertPosition( table.getGroups( )
						.getElementHandle( ), group, table.getGroups( )
						.getSlotID( ) );
			}
			else if ( getListBandProxy( ) != null )
			{
				DesignElementHandle group = getListBandProxy( ).getElemtHandle( );
				ListHandle list = (ListHandle) group.getContainer( );
				return DEUtil.findInsertPosition( list.getGroups( )
						.getElementHandle( ), group, list.getGroups( )
						.getSlotID( ) );
			}
		}
		return POSITION_INNERMOST;
	}

	/**
	 * Returns the insert position
	 * 
	 * @return the insert position
	 */
	abstract protected int getPosition( );
}

class InsertAboveGroupAction extends InsertPositionGroupAction
{

	/**
	 * @param editParts
	 */
	public InsertAboveGroupAction( List editParts, String text )
	{
		super( editParts, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition( )
	{
		if ( isGroup( ) )
		{
			if ( isNotReverse( ) )
			{
				return getCurrentPosition( );
			}
			return getCurrentPosition( ) + 1;
		}
		if ( isNotReverse( ) )
		{
			return POSITION_TOP_LEVEL;
		}
		return POSITION_INNERMOST;
	}

}

class InsertBelowGroupAction extends InsertPositionGroupAction
{

	/**
	 * @param editParts
	 */
	public InsertBelowGroupAction( List editParts, String text )
	{
		super( editParts, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition( )
	{
		if ( isGroup( ) )
		{
			if ( isNotReverse( ) )
			{
				return getCurrentPosition( ) + 1;
			}
			return getCurrentPosition( );
		}
		if ( isNotReverse( ) )
		{
			return POSITION_INNERMOST;
		}
		return POSITION_TOP_LEVEL;
	}
}

/**
 * Insert table or list group in the position
 */

class InsertIntoGroupAction extends InsertPositionGroupAction
{

	/**
	 * @param editParts
	 */
	public InsertIntoGroupAction( List editParts, String text )
	{
		super( editParts, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertPositionGroupAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		return super.isEnabled( ) && isGroup( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition( )
	{
		return POSITION_INNERMOST;
	}
}