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

package org.eclipse.birt.report.item.crosstab.internal.ui;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.NormalCrosstabCellAdapter;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

/**
 * 
 */

public class CrosstabCellMenuAdapterFactory implements IAdapterFactory
{

	private ActionRegistry actionRegistry;

	public Object getAdapter( Object adaptableObject, Class adapterType )
	{
		if ( adaptableObject instanceof NormalCrosstabCellAdapter
				&& adapterType == IMenuListener.class )
		{
			return new IMenuListener(){

				public void menuAboutToShow( IMenuManager manager )
				{

					MenuManager subMenu = new MenuManager( Messages.getString( "SchematicContextMenuProvider.Menu.insertElement" ) );
					
					IAction action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_TEXT_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_LABEL_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_DATA_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_IMAGE_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_GRID_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_LIST_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_TABLE_DISPLAY_TEXT );
					subMenu.add( action );

					action = new Action(){};
					action.setText( GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_DISPLAY_TEXT );
					subMenu.add( action );
					
					manager.add( subMenu );
				}};
		}
		return null;
	}

	protected IAction getAction( String actionID )
	{
		IAction action = getActionRegistry( ).getAction( actionID );
		if ( action instanceof UpdateAction )
		{
			( (UpdateAction) action ).update( );
		}
		return action;
	}

	private ActionRegistry getActionRegistry( )
	{
		if (actionRegistry == null)
			actionRegistry = new ActionRegistry();
		return actionRegistry;
	}

	public Class[] getAdapterList( )
	{
		// TODO Auto-generated method stub
		return null;
	}

}
