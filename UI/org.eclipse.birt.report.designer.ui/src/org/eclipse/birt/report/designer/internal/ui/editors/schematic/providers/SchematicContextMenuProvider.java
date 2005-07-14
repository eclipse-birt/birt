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

import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddStyleRuleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteColumnAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteListGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteRowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteTableGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditBindingAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeDetailAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeFooterAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeHeaderAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnLeftAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnRightAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupActionFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.MergeAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ImageEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.ApplyStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertPasteColumnAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.designer.ui.actions.NoneAction;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Schematic context menu provider
 */
public class SchematicContextMenuProvider extends ContextMenuProvider
{

	private static final String INSERT_ROW_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.insertRow" ); //$NON-NLS-1$

	private static final String EDIT_GROUP_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.EditGroup" ); //$NON-NLS-1$

	private static final String APPLY_STYLE_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Apply" ); //$NON-NLS-1$

	private static final String STYLE_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Style" ); //$NON-NLS-1$

	private static final String INSERT_MENU_ITEM_TEXT = Messages.getString( "SchematicContextMenuProvider.Menu.Insert" ); //$NON-NLS-1$

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
		Object multiSelection = getMultiSelectedElement( );

		// special for dealing with multi selected elements (items).
		if ( multiSelection == Object.class // report design and slot
				// multi ?
				|| multiSelection == DesignElementHandle.class
				// report design
				|| multiSelection == ReportDesignHandle.class
				// saveral report items
				|| multiSelection == ReportItemHandle.class
				// table and list
				|| multiSelection == ListHandle.class )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.UNDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.REDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CutAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CopyAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new PasteAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new DeleteAction( selectedElements ) );

			if ( multiSelection == ReportDesignHandle.class )
			{
				createInsertElementMenu( menuManager,
						GEFActionConstants.GROUP_EDIT );
			}
			createStyleMenu( menuManager, GEFActionConstants.GROUP_REST );
			if ( Policy.TRACING_MENU_SHOW )
			{
				System.out.println( "Menu(for Editor) >> Shows for multi-selcetion." ); //$NON-NLS-1$
			}
		}

		// -----------------------------------------------------------------
		else if ( firstSelectedElement instanceof DesignElementHandle )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.UNDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.REDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CutAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CopyAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new PasteAction( selectedElements ) );

			createStyleMenu( menuManager, GEFActionConstants.GROUP_REST );

			if ( ( (IStructuredSelection) getSelection( ) ).size( ) == 1 )
			{
				Object element = ( (IStructuredSelection) getSelection( ) ).getFirstElement( );

				if ( element instanceof LabelEditPart
						|| element instanceof ImageEditPart )
				{
					IAction action = getAction( GEFActionConstants.DIRECT_EDIT );
					action.setAccelerator( SWT.F2 );
					if ( element instanceof DataEditPart )
					{
						action.setText( Messages.getString( "SchematicContextMenuProvider.ActionText.editData" ) ); //$NON-NLS-1$
					}
					else
					{
						action.setText( Messages.getString( "SchematicContextMenuProvider.ActionText.editLabel" ) ); //$NON-NLS-1$
					}
					menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
							action );
				}
			}

			if ( firstSelectedElement instanceof RowHandle )
			{
				if ( getRowHandles( ).size( ) != 0 )
				{
					MenuManager insertMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
					MenuManager rowMenu = new MenuManager( INSERT_ROW_MENU_ITEM_TEXT );
					rowMenu.add( getAction( InsertRowAboveAction.ID ) );
					rowMenu.add( getAction( InsertRowBelowAction.ID ) );

					RowHandle row = (RowHandle) getRowHandles( ).get( 0 );
					if ( !( row.getContainer( ) instanceof GridHandle ) )
					{
						insertMenu.add( getAction( IncludeHeaderAction.ID ) );
						insertMenu.add( getAction( IncludeDetailAction.ID ) );
						insertMenu.add( getAction( IncludeFooterAction.ID ) );
					}
					insertMenu.add( rowMenu );
					menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
							insertMenu );
				}
				// delete row action.
				menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
						getAction( DeleteRowAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( MergeAction.ID ) );
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						getAction( SplitAction.ID ) );
			}
			else if ( firstSelectedElement instanceof ColumnHandle )
			{
				menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
						new InsertPasteColumnAction( selectedElements ) );
				if ( getColumnHandles( ).size( ) != 0 )
				{
					MenuManager subMenu = new MenuManager( INSERT_MENU_ITEM_TEXT );
					subMenu.add( getAction( InsertColumnRightAction.ID ) );
					subMenu.add( getAction( InsertColumnLeftAction.ID ) );
					menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
							subMenu );
				}
				// delete column action.
				menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
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
				// delete action in cell
				menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
						new DeleteAction( selectedElements ) );
			}
			else
			{
				// common delete action
				menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
						new DeleteAction( selectedElements ) );
			}
			if ( Policy.TRACING_MENU_SHOW )
			{
				System.out.println( "Menu(for Editor) >> Shows for " //$NON-NLS-1$
						+ ( (DesignElementHandle) firstSelectedElement ).getDefn( )
								.getDisplayName( ) );
			}
		}
		else if ( firstSelectedElement instanceof SlotHandle )
		{
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.UNDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_UNDO,
					getAction( ActionFactory.REDO.getId( ) ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CutAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new CopyAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new PasteAction( selectedElements ) );
			menuManager.appendToGroup( GEFActionConstants.GROUP_COPY,
					new DeleteAction( selectedElements ) );

			createInsertElementMenu( menuManager, GEFActionConstants.GROUP_EDIT );
			if ( Policy.TRACING_MENU_SHOW )
			{
				System.out.println( "Menu(for Editor) >> Shows for the slot " //$NON-NLS-1$
						+ ( (SlotHandle) firstSelectedElement ).getSlotID( )
						+ " of " //$NON-NLS-1$
						+ ( (SlotHandle) firstSelectedElement ).getElementHandle( )
								.getDefn( )
								.getDisplayName( ) );
			}
		}
		else
		{
			//
		}

		if ( !getTableEditParts( ).isEmpty( ) )
		{
			createInsertGroupMenu( menuManager );
			menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
					getAction( DeleteTableGroupAction.ID ) );
			if ( getTableEditParts( ).size( ) == 1 )
			{
				createEditGroupMenu( menuManager, GEFActionConstants.GROUP_ADD );
				Separator separator = new Separator( EditBindingAction.ID );
				menuManager.add( separator );
				menuManager.appendToGroup( EditBindingAction.ID,
						getAction( EditBindingAction.ID ) );
			}
		}

		if ( !getListEditParts( ).isEmpty( ) )
		{
			createInsertGroupMenu( menuManager );
			menuManager.appendToGroup( GEFActionConstants.GROUP_ADD,
					getAction( DeleteListGroupAction.ID ) );
			if ( getListEditParts( ).size( ) == 1 )
			{
				createEditGroupMenu( menuManager, GEFActionConstants.GROUP_ADD );
			}
		}
	}

	/**
	 * @param menuManager
	 */
	private void createInsertGroupMenu( IMenuManager menuManager )
	{
		MenuManager subMenu = new MenuManager( Messages.getString( "InsertGroupAction.actionMsg.group" ) ); //$NON-NLS-1$
		Action[] actions = InsertGroupActionFactory.getInsertGroupActions( getSelectedObjects( ) );
		for ( int i = 0; i < actions.length; i++ )
		{
			subMenu.add( actions[i] );
		}
		menuManager.appendToGroup( GEFActionConstants.GROUP_ADD, subMenu );
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

		/*
		 * Extended Items insert actions
		 */

		List points = ExtensionPointManager.getInstance( )
				.getExtendedElementPoints( );
		for ( Iterator iter = points.iterator( ); iter.hasNext( ); )
		{
			ExtendedElementUIPoint point = (ExtendedElementUIPoint) iter.next( );
			action = getAction( point.getExtensionName( ) );
			if ( action != null )
			{
				action.setText( point.getExtensionName( ) );
				subMenu.add( action );
			}
		}

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
		subMenu.add( NoneAction.getInstance( ) );
		subMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( ApplyStyleMenuAction.ID, manager );
			}
		} );

		menu.add( subMenu );
		menu.add( new Separator( ) );
		menu.add( getAction( AddStyleRuleAction.ID ) );
		menuManager.appendToGroup( group_name, menu );
	}

	private void updateDynamicItems( String actionId, IMenuManager menu )
	{
		IAction action = getAction( actionId );
		if ( action != null && action instanceof MenuUpdateAction )
		{
			( (MenuUpdateAction) action ).updateMenu( (MenuManager) menu );
		}
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
	private void createEditGroupMenu( IMenuManager menuManager,
			String group_name )
	{
		// If select on Group, no need to provide cascade menu
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

		if ( !getTableEditParts( ).isEmpty( ) )

		{
			parentHandle = (ListingHandle) ( (TableEditPart) getTableEditParts( ).get( 0 ) ).getModel( );
		}
		else if ( !getListEditParts( ).isEmpty( ) )
		{
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
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements( )
	{
		return InsertInLayoutUtil.editPart2Model( getSelection( ) ).toList( );
	}

	/**
	 * Gets the current selected object.
	 * 
	 * @return The current selected object array. If length is one, return the
	 *         first
	 */
	protected Object getSelectedElement( )
	{
		Object[] array = getElements( ).toArray( );
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
		Object[] array = getElements( ).toArray( );
		if ( array.length > 0 )
		{
			return array[0];
		}
		return null;
	}

	/**
	 * Gets multiple selected elements
	 * 
	 * @return The (base) class type all the multi selected elements
	 */
	private Object getMultiSelectedElement( )
	{
		List list = getElements( );
		Object baseHandle = list.get( 0 );
		Class base = baseHandle.getClass( );

		for ( int i = 1; i < list.size( ); i++ )
		{
			Object obj = list.get( i );
			if ( base.isInstance( obj ) )
			{
				continue;
			}
			// Ensure multi selected elements are instance of the "base" class.
			while ( !base.isInstance( obj ) )
			{
				base = base.getSuperclass( );
			}
			continue;
		}
		return base;
	}

	/**
	 * Gets the current selected row objects.
	 * 
	 * @return The current selected row objects.
	 */

	public List getRowHandles( )
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
	 * Gets the current selected column objects.
	 * 
	 * @return The current column objects
	 */
	public List getColumnHandles( )
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
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part
	 *         is selected.
	 */
	protected List getTableEditParts( )
	{
		List tableParts = new ArrayList( );
		for ( Iterator itor = getSelectedObjects( ).iterator( ); itor.hasNext( ); )
		{
			Object obj = itor.next( );
			if ( obj instanceof DummyEditpart )
			{
				// Column or Row indicators
				// ignore, do nothing.
			}
			else if ( obj instanceof TableEditPart )
			{
				if ( obj instanceof GridEditPart )
				{
					return Collections.EMPTY_LIST;
				}
				if ( !( tableParts.contains( obj ) ) )
				{
					tableParts.add( obj );
				}
			}
			else if ( obj instanceof TableCellEditPart )
			{
				Object parent = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
				if ( parent instanceof GridEditPart )
				{
					return Collections.EMPTY_LIST;
				}
				if ( !( tableParts.contains( parent ) ) )
				{
					tableParts.add( parent );
				}
			}
			else
			{
				return Collections.EMPTY_LIST;
			}
		}
		return tableParts;
	}

	/**
	 * Gets list edit parts.
	 * 
	 * @return The current selected list edit parts, null if no list edit part
	 *         is selected.
	 */
	protected List getListEditParts( )
	{
		List listParts = new ArrayList( );
		for ( Iterator iter = getSelectedObjects( ).iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof ListEditPart )
			{
				if ( !( listParts.contains( obj ) ) )
				{
					listParts.add( obj );
				}
			}
			else if ( obj instanceof ListBandEditPart )
			{
				Object parent = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
				if ( !( listParts.contains( parent ) ) )
				{
					listParts.add( parent );
				}
			}
			else
			{
				return Collections.EMPTY_LIST;
			}
		}
		return listParts;
	}
}