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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeTableGroupAction;
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
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
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

		Object firstSelectedElement = getFirstElement( );
		Object selectedElements = getSelectedElement( );

		if ( firstSelectedElement instanceof DesignElementHandle )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.UNDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.REDO.getId( ) ) );
			IAction action = getAction( ActionFactory.SAVE.getId( ) );
			if ( action != null )
			{
				action.setEnabled( action.isEnabled( ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_SAVE,
						action );
			}

			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CutAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CopyAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new PasteAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new DeleteAction( selectedElements ) );

			createStyleMenu( menuManager, GEFActionConstants.GROUP_REST );

			if ( firstSelectedElement instanceof ReportDesignHandle )
			{
			}
			if ( firstSelectedElement instanceof MasterPageHandle )
			{
			}
			if ( firstSelectedElement instanceof ReportItemHandle )
			{
				if ( firstSelectedElement instanceof ListingHandle )
				{
					if ( firstSelectedElement instanceof ListHandle )
					{
					}
					else if ( firstSelectedElement instanceof TableHandle )
					{
					}
					else if ( firstSelectedElement instanceof GridHandle )
					{
					}
					else
					{
						//
					}
				}
			}
			else if ( firstSelectedElement instanceof RowHandle )
			{
				MenuManager insertMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
				insertMenu.add( getAction( InsertRowAboveAction.ID ) );
				insertMenu.add( getAction( InsertRowBelowAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						insertMenu );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( DeleteRowAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( MergeAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( SplitAction.ID ) );
			}
			else if ( firstSelectedElement instanceof ColumnHandle )
			{
				MenuManager subMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
				subMenu.add( getAction( InsertColumnRightAction.ID ) );
				subMenu.add( getAction( InsertColumnLeftAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						subMenu );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( DeleteColumnAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( MergeAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( SplitAction.ID ) );
			}
			else if ( firstSelectedElement instanceof CellHandle )
			{
				createInsertElementMenu( menuManager,
						GEFActionConstants.GROUP_EDIT );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( MergeAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( SplitAction.ID ) );
			}
			else
			{
				//
			}
		}
		else if ( firstSelectedElement instanceof SlotHandle )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.UNDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.REDO.getId( ) ) );
			IAction action = getAction( ActionFactory.SAVE.getId( ) );
			if ( action != null )
			{
				action.setEnabled( action.isEnabled( ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_SAVE,
						action );
			}

			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CutAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CopyAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new PasteAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new DeleteAction( selectedElements ) );

			createInsertElementMenu( menuManager, GEFActionConstants.GROUP_EDIT );
		}
		else
		{
			//
		}
		if ( getTableEditParts( ) != null )
		{
			if ( !( getTableEditParts( ).get( 0 ) instanceof GridEditPart ) )
			{
				menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
						getAction( InsertGroupAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
						getAction( DeleteGroupAction.ID ) );
				Separator separator = new Separator( EditBindingAction.ID );
				menuManager.add( separator );
				menuManager.appendToGroup( EditBindingAction.ID,
						getAction( EditBindingAction.ID ) );
				createGroupMenu( menuManager, GEFActionConstants.GROUP_ADD );
			}
		}
		if ( getListEditParts( ) != null )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
					getAction( InsertListGroupAction.ID ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
					getAction( DeleteListGroupAction.ID ) );
			createGroupMenu( menuManager, GEFActionConstants.GROUP_ADD );
		}
	}

	/**
	 * @param menuManager
	 */
	private void createShowMenu( IMenuManager menuManager )
	{
		MenuManager subMenu = new MenuManager( SHOW_MENU_ITEM_TEXT );

		if ( isSelectedGroup( ) )
		{
			subMenu.add( new IncludeTableGroupAction.IncludeTableGroupHeaderAction( getFirstElement( ) ) );
			subMenu.add( new IncludeTableGroupAction.IncludeTableGroupFooterAction( getFirstElement( ) ) );
		}
		else
		{
			subMenu.add( getAction( IncludeHeaderAction.ID ) );
			subMenu.add( getAction( IncludeDetailAction.ID ) );
			subMenu.add( getAction( IncludeFooterAction.ID ) );
		}
		menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT, subMenu );
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

		menuManager.appendToGroup( group_name, subMenu );
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

		SharedStyleHandle oldStyle = getStyleHandle( );

		ApplyStyleAction reset = new ApplyStyleAction( null );
		reset.setSelection( getSelection( ) );
		if ( oldStyle == null )
		{
			reset.setChecked( true );
		}
		subMenu.add( reset );
		subMenu.add( new Separator( ) );

		Iterator iter = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getStyles( )
				.iterator( );
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
		menu.add( new Separator( ) );
		menu.add( getAction( AddStyleRuleAction.ID ) );
		menuManager.appendToGroup( group_name, menu );
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
				menuManager.appendToGroup( GEFActionConstants.GROUP_ADD, action );
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
				menuManager.appendToGroup( GEFActionConstants.GROUP_ADD, action );
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
		menuManager.appendToGroup( group_name, subMenu );
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

	private boolean isSelectedGroup( )
	{
		if ( getFirstElement( ) instanceof RowHandle )
		{
			DesignElementHandle container = ( (RowHandle) getFirstElement( ) ).getContainer( );
			if ( container instanceof TableGroupHandle )
			{
				return true;
			}
		}
		if ( getFirstElement( ) instanceof SlotHandle )
		{
			DesignElementHandle container = ( (SlotHandle) getFirstElement( ) ).getElementHandle( );
			if ( container instanceof ListGroupHandle )
			{
				return true;
			}
		}
		return false;
	}
}