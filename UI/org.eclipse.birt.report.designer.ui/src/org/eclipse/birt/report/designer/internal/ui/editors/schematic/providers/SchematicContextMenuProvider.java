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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddStyleRuleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ApplyStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteColumnAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteListGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteRowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditBindingAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeDetailAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeFooterAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeHeaderAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeListDetailAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeListFooterAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeListHeaderAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnLeftAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnRightAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertListGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.MergeAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Schematic context menu provider
 */
public class SchematicContextMenuProvider extends ContextMenuProvider
{

	private static final String EDIT_GROUP_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.EditGroup" ); //$NON-NLS-1$

	private static final String APPLY_STYLE_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Apply" ); //$NON-NLS-1$

	private static final String STYLE_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Style" ); //$NON-NLS-1$

	private static final String LIST_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.List" ); //$NON-NLS-1$

	private static final String INSERT_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Insert" ); //$NON-NLS-1$

	private static final String SHOW_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Show" ); //$NON-NLS-1$

	private static final String ELEMENT_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.insertElement" ); //$NON-NLS-1$

	/** the action registry */
	private final ActionRegistry actionRegistry;

	/**
	 * Constructs a new WorkflowEditorContextMenuProvider instance.
	 * 
	 * @param viewer
	 *            the edit part view
	 * @param actionRegistry
	 *            the actions registry
	 */
	public SchematicContextMenuProvider( EditPartViewer viewer,
			ActionRegistry actionRegistry )
	{
		super( viewer );
		this.actionRegistry = actionRegistry;
	}

	/**
	 * Gets the action registry.
	 * 
	 * @return the action registry
	 */
	public ActionRegistry getActionRegistry( )
	{
		return actionRegistry;
	}

	/**
	 * Retrieves action item( value ) from the action registry with the given
	 * action ID( key ).
	 * 
	 * @param actionID
	 *            the given atcion ID.
	 * @return The retrieved action item.
	 */
	protected IAction getAction( String actionID )
	{
		IAction action = getActionRegistry( ).getAction( actionID );
		return action;
	}

	/**
	 * Gets the current selection.
	 * 
	 * @return The current selection
	 */
	protected ISelection getSelection( )
	{
		return getViewer( ).getSelection( );
	}

	/**
	 * Returns a <code>List</code> containing the currently selected objects.
	 * 
	 * @return A List containing the currently selected objects
	 */
	protected List getSelectedObjects( )
	{
		if ( !( getSelection( ) instanceof IStructuredSelection ) )
			return Collections.EMPTY_LIST;
		return ( (IStructuredSelection) getSelection( ) ).toList( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface.action.IMenuManager)
	 */
	public void buildContextMenu( IMenuManager menuManager )
	{
		GEFActionConstants.addStandardActionGroups( menuManager );

		// undo , redo
		appendActionToUndoGroup( getAction( ActionFactory.UNDO.getId( ) ),
				menuManager );
		appendActionToUndoGroup( getAction( ActionFactory.REDO.getId( ) ),
				menuManager );

		appendActionToEditGroup( getAction( GEFActionConstants.DIRECT_EDIT ),
				menuManager );
		appendActionToEditGroup( getAction( MergeAction.ID ), menuManager );
		appendActionToEditGroup( getAction( SplitAction.ID ), menuManager );
		appendActionToAddGroup( getAction( InsertListGroupAction.ID ),
				menuManager );
		appendActionToAddGroup( getAction( DeleteListGroupAction.ID ),
				menuManager );

		/** Gets the current selected object, local */
		Object selectedFirstElement = getFirstElement( );
		Object selectedElements = getSelectedElement( );

		// Adds cut, copy, paste group actions
		appendActionToCopyGroup( new CutAction( selectedElements ), menuManager );
		appendActionToCopyGroup( new CopyAction( selectedElements ),
				menuManager );
		appendActionToCopyGroup( new PasteAction( selectedElements ),
				menuManager );
		// adds save
		appendActionToSaveGroup( getAction( ActionFactory.SAVE.getId( ) ),
				menuManager );

		// selecting a row
		if ( selectedFirstElement instanceof RowHandle )
		{
			createInsertRowMenu( menuManager, GEFActionConstants.GROUP_EDIT );

			if ( !( getTableEditParts( ).get( 0 ) instanceof GridEditPart ) )
			{
				createShowMenu( menuManager, GEFActionConstants.GROUP_EDIT );
				appendActionToAddGroup( getAction( InsertGroupAction.ID ),
						menuManager );
				Separator separator = new Separator( EditBindingAction.ID );
				menuManager.add( separator );
				menuManager.appendToGroup( EditBindingAction.ID,
						getAction( EditBindingAction.ID ) );
			}
			appendActionToEditGroup( getAction( DeleteRowAction.ID ),
					menuManager );
			appendActionToAddGroup( getAction( DeleteGroupAction.ID ),
					menuManager );

		}
		// selecting a column
		else if ( selectedFirstElement instanceof ColumnHandle )
		{
			createInsertColumnMenu( menuManager, GEFActionConstants.GROUP_EDIT );
			appendActionToEditGroup( getAction( DeleteColumnAction.ID ),
					menuManager );
		}
		// selecting a cell of table, grid or list.
		else if ( selectedFirstElement instanceof CellHandle
				|| selectedFirstElement instanceof SlotHandle )
		{
			createInsertElementMenu( menuManager, GEFActionConstants.GROUP_EDIT );
		}
		else
		{
			appendActionToCopyGroup( new DeleteAction( selectedElements ),
					menuManager );
		}

		// selecting a list
		// to be considered.
		//if ( getListEditParts( ) != null && getListEditParts( ).size( ) == 1)
		if ( getListEditParts( ) != null )
		{
			createListMenu( menuManager, GEFActionConstants.GROUP_EDIT );
		}

		createGroupMenu( menuManager, GEFActionConstants.GROUP_ADD );
		createStyleMenu( menuManager, GEFActionConstants.GROUP_REST );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createShowMenu( IMenuManager menuManager, String group_name )
	{
		MenuManager subMenu = new MenuManager( SHOW_MENU_ITEM_TEXT );
		subMenu.add( getAction( IncludeHeaderAction.ID ) );
		subMenu.add( getAction( IncludeDetailAction.ID ) );
		subMenu.add( getAction( IncludeFooterAction.ID ) );
		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createInsertRowMenu( IMenuManager menuManager,
			String group_name )
	{
		MenuManager subMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
		subMenu.add( getAction( InsertRowAboveAction.ID ) );
		subMenu.add( getAction( InsertRowBelowAction.ID ) );
		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createInsertColumnMenu( IMenuManager menuManager,
			String group_name )
	{
		MenuManager subMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
		subMenu.add( getAction( InsertColumnRightAction.ID ) );
		subMenu.add( getAction( InsertColumnLeftAction.ID ) );
		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createInsertElementMenu( IMenuManager menuManager,
			String group_name )
	{
		MenuManager subMenu = new MenuManager( ELEMENT_MENU_ITEM_TEXT );

		IAction action = getAction( GeneralInsertMenuAction.INSERT_TEXT_ID );
		action.setText( GeneralInsertMenuAction.INSERT_TEXT_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_LABEL_ID );
		action.setText( GeneralInsertMenuAction.INSERT_LABEL_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_DATA_ID );
		action.setText( GeneralInsertMenuAction.INSERT_DATA_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_IMAGE_ID );
		action.setText( GeneralInsertMenuAction.INSERT_IMAGE_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_GRID_ID );
		action.setText( GeneralInsertMenuAction.INSERT_GRID_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_LIST_ID );
		action.setText( GeneralInsertMenuAction.INSERT_LIST_DISPLAY_TEXT );
		subMenu.add( action );

		action = getAction( GeneralInsertMenuAction.INSERT_TABLE_ID );
		action.setText( GeneralInsertMenuAction.INSERT_TABLE_DISPLAY_TEXT );
		subMenu.add( action );

		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createListMenu( IMenuManager menuManager, String group_name )
	{
		MenuManager subMenu = new MenuManager( LIST_MENU_ITEM_TEXT );
		subMenu.add( getAction( IncludeListHeaderAction.ID ) );
		subMenu.add( getAction( IncludeListDetailAction.ID ) );
		subMenu.add( getAction( IncludeListFooterAction.ID ) );
		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createStyleMenu( IMenuManager menuManager, String group_name )
	{
		MenuManager menu = new MenuManager( STYLE_MENU_ITEM_TEXT );
		MenuManager subMenu = new MenuManager( APPLY_STYLE_MENU_ITEM_TEXT );
		Iterator iter = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getStyles( )
				.iterator( );
		SharedStyleHandle oldStyle = getStyleHandle( );
		while ( iter.hasNext( ) )
		{
			SharedStyleHandle handle = (SharedStyleHandle) iter.next( );
			ApplyStyleAction action = new ApplyStyleAction( handle );
			action.setSelection( getSelection( ) );
			if ( oldStyle == handle )
			{
				action.setChecked( true );
			}
			else
			{
				action.setChecked( false );
			}
			subMenu.add( action );
		}

		menu.add( subMenu );
		if ( subMenu.getItems( ) != null )
		{
			menu.add( new Separator( ) );
		}
		menu.add( getAction( AddStyleRuleAction.ID ) );
		appendMenuToGroup( menu, group_name, menuManager );
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu
	 * manager.
	 * 
	 * @param menuManager
	 *            The menu manager contains the action group.
	 * @param group_name
	 *            The action group contains the sub menu.
	 */
	private void createGroupMenu( IMenuManager menuManager, String group_name )
	{
		//If select on Group, no need to provide cascade menu
		if ( getFirstElement( ) instanceof RowHandle )
		{
			DesignElementHandle container = ( (RowHandle) getFirstElement( ) ).getContainer( );
			if ( container instanceof TableGroupHandle )
			{
				Action action = new EditGroupAction( null,
						(TableGroupHandle) container );
				action.setText( EDIT_GROUP_MENU_ITEM_TEXT );
				appendActionToAddGroup( action, menuManager );
				return;
			}
		}

		if ( getFirstElement( ) instanceof SlotHandle )
		{
			DesignElementHandle container = ( (SlotHandle) getFirstElement( ) ).getElementHandle( );
			if ( container instanceof ListGroupHandle )
			{
				Action action = new EditGroupAction( null,
						(ListGroupHandle) container );
				action.setText( EDIT_GROUP_MENU_ITEM_TEXT );
				appendActionToAddGroup( action, menuManager );
				return;
			}
		}

		MenuManager subMenu = new MenuManager( EDIT_GROUP_MENU_ITEM_TEXT );
		ListingHandle parentHandle = null;

		if ( ( getTableEditParts( ) != null )
				&& !( getTableEditParts( ).get( 0 ) instanceof GridEditPart ) )

		{
			if ( getTableEditParts( ).size( ) > 1 )
			{
				return;
			}
			parentHandle = (ListingHandle) ( (TableEditPart) getTableEditParts( ).get( 0 ) ).getModel( );
		}
		else if ( getListEditParts( ) != null )
		{
			if ( getListEditParts( ).size( ) > 1 )
			{
				return;
			}
			parentHandle = (ListingHandle) ( (ListEditPart) getListEditParts( ).get( 0 ) ).getModel( );
		}
		else
		{
			return;
		}
		SlotHandle handle = parentHandle.getGroups( );
		Iterator iter = handle.iterator( );
		while ( iter.hasNext( ) )
		{
			GroupHandle groupHandle = (GroupHandle) iter.next( );
			subMenu.add( new EditGroupAction( null, groupHandle ) );
		}
		appendMenuToGroup( subMenu, group_name, menuManager );
	}

	/**
	 * Gets the current selected object.
	 * 
	 * @return The current selected object array. If length is one, return the
	 *         first
	 */
	protected Object getSelectedElement( )
	{
		Object[] array = getElements( );
		if ( array.length == 1 )
		{
			return array[0];
		}
		return array;
	}

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement( )
	{
		Object[] array = getElements( );
		if ( array.length > 0 )
		{
			return array[0];
		}
		return null;
	}

	/**
	 * Gets elements.
	 * 
	 * @return elements in the form of array
	 */
	protected Object[] getElements( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;

		List result = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof ReportElementEditPart )
			{
				Object model = ( (ReportElementEditPart) obj ).getModel( );
				if ( model instanceof ListBandProxy )
				{
					model = ( (ListBandProxy) model ).getSlotHandle( );
				}
				result.add( model );
			}
		}
		return result.toArray( );
	}

	/**
	 * Gets style of the selected elements.
	 * 
	 * @return the style handle of the selected elements
	 */
	protected SharedStyleHandle getStyleHandle( )
	{
		Object[] elements = getElements( );
		if ( elements.length > 0 && elements[0] instanceof DesignElementHandle )
		{
			SharedStyleHandle style = ( (DesignElementHandle) elements[0] ).getStyle( );
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( !( elements[i] instanceof DesignElementHandle ) )
				{
					return null;
				}

				SharedStyleHandle handle = ( (DesignElementHandle) elements[i] ).getStyle( );
				if ( handle != style )
				{
					return null;
				}
			}
			return style;
		}
		return null;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part
	 *         is selected.
	 */
	protected ArrayList getTableEditParts( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;
		int size = list.size( );

		ArrayList tableParts = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );

			if ( obj instanceof DummyEditpart )
			{
				// do nothing.
			}
			else if ( obj instanceof TableEditPart )
			{
				if ( !( tableParts.contains( obj ) ) )
				{
					tableParts.add( obj );
				}
			}
			else if ( obj instanceof TableCellEditPart )
			{
				Object parent = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
				if ( !( tableParts.contains( parent ) ) )
				{
					tableParts.add( parent );
				}
			}
			else
			{
				return null;
			}
		}
		return tableParts;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ArrayList getListEditParts( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;
		int size = list.size( );

		// creates a arrayList to contain the single selected or multi selected
		// listEditPart(s).
		ArrayList listParts = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );

			if ( obj instanceof ListEditPart )
			{
				// if listParts already contains this listEditPart since the sub
				// listBandEditPart was selected before, then do not contains
				// this listEditPart again.
				if ( !( listParts.contains( obj ) ) )
				{
					listParts.add( (ListEditPart) obj );
				}
			}
			else if ( obj instanceof ListBandEditPart )
			{
				// if the parent listEditPart of this listBandEditPart has been
				// already contained in the ArryList, then do not contain the
				// parent again.
				Object parent = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
				if ( !( listParts.contains( parent ) ) )
				{
					listParts.add( parent );
				}
			}
			else
			{
				listParts = null;
				return null;
			}
		}
		return listParts;
	}

	/**
	 * Appends a sub menu into a specified group of a menu with given the group
	 * Name and the menuManager.
	 * 
	 * @param subMenu
	 *            the sub menu item to be added into the group of the menu.
	 * @param groupName
	 *            the group contains the sub menu.
	 * @param menu
	 *            the menuManager contains the action group.
	 */
	private void appendMenuToGroup( IContributionItem subMenu,
			String groupName, IMenuManager menu )

	{
		if ( null != subMenu && subMenu.isEnabled( ) )
			menu.appendToGroup( groupName, subMenu );
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToUndoGroup( IAction action, IMenuManager menu )
	{
		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_UNDO, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToCopyGroup( IAction action, IMenuManager menu )
	{
		if ( null != action
				&& ( action.isEnabled( ) || action instanceof PasteAction ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_COPY, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToEditGroup( IAction action, IMenuManager menu )
	{

		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_EDIT, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToAddGroup( IAction action, IMenuManager menu )
	{

		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_ADD, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToRestGroup( IAction action, IMenuManager menu )
	{

		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_REST, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToAddtionGroup( IAction action, IMenuManager menu )
	{

		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.MB_ADDITIONS, action );
		}
	}

	/**
	 * Appends the specified action to the specified menu group.
	 * 
	 * @param menu
	 *            The menu manager containing menu groups and action items
	 * @param action
	 *            The action
	 */
	private void appendActionToSaveGroup( IAction action, IMenuManager menu )
	{

		if ( null != action && action.isEnabled( ) )
		{
			menu.appendToGroup( GEFActionConstants.GROUP_SAVE, action );
		}
	}
}