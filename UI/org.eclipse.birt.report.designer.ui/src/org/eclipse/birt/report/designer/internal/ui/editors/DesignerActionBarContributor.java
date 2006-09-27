/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CreatePlaceHolderPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnLeftAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnRightAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.MergeAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.RevertToReportItemPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportLibraryAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.ApplyStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.EditGroupMenuAction;
import org.eclipse.birt.report.designer.ui.actions.EditStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertGroupMenuAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.actions.NewDataSourceAction;
import org.eclipse.birt.report.designer.ui.actions.NewParameterAction;
import org.eclipse.birt.report.designer.ui.actions.NoneAction;
import org.eclipse.birt.report.designer.ui.actions.ToggleMarginVisibilityAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

/**
 * Toolbar and menu contributor for designer
 * 
 */
public class DesignerActionBarContributor extends
		org.eclipse.gef.ui.actions.ActionBarContributor
{

	static class RegisterActions
	{

		String id, displayName;

		int style;

		public RegisterActions( String id, String displayName )
		{
			this( id, displayName, IAction.AS_UNSPECIFIED );
		}

		public RegisterActions( String id, String displayName, int style )
		{
			this.id = id;
			this.displayName = displayName;
			this.style = style;
		}
	}

	private RegisterActions[] insertElementActions = null;

	private static RegisterActions[] insertActions = new RegisterActions[]{
			new RegisterActions( GeneralInsertMenuAction.INSERT_TEXT_ID,
					GeneralInsertMenuAction.INSERT_TEXT_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_LABEL_ID,
					GeneralInsertMenuAction.INSERT_LABEL_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_DATA_ID,
					GeneralInsertMenuAction.INSERT_DATA_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_IMAGE_ID,
					GeneralInsertMenuAction.INSERT_IMAGE_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_GRID_ID,
					GeneralInsertMenuAction.INSERT_GRID_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_LIST_ID,
					GeneralInsertMenuAction.INSERT_LIST_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_TABLE_ID,
					GeneralInsertMenuAction.INSERT_TABLE_DISPLAY_TEXT ),
			new RegisterActions( GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_ID,
					GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_DISPLAY_TEXT ),
	};

	private static final RegisterActions[] elementActions = new RegisterActions[]{
			new RegisterActions( InsertRowAboveAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertRowAbove" ) ), //$NON-NLS-1$
			new RegisterActions( InsertRowBelowAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertRowBelow" ) ), //$NON-NLS-1$
			null,
			new RegisterActions( InsertColumnLeftAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertColumnLeft" ) ), //$NON-NLS-1$
			new RegisterActions( InsertColumnRightAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertColumnRight" ) ), //$NON-NLS-1$
			null,
			new RegisterActions( MergeAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.merge" ) ), //$NON-NLS-1$
			new RegisterActions( SplitAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.split" ) ), //$NON-NLS-1$
			null,
			new RegisterActions( CreatePlaceHolderPartAction.ID,
					Messages.getString( "CreatePlaceHolderAction.text" ) ),
			new RegisterActions( RevertToReportItemPartAction.ID,
					Messages.getString( "RevertToReportItemAction.text" ) ),
			null,
			new RegisterActions( AddGroupAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.group" ) ), //$NON-NLS-1$,
	};

	private static final RegisterActions[] dataActions = new RegisterActions[]{
			new RegisterActions( NewDataSourceAction.ID,
					Messages.getString( "designerActionBarContributor.menu.data-newdatasource" ) ),//$NON-NLS-1$
			new RegisterActions( NewDataSetAction.ID,
					Messages.getString( "designerActionBarContributor.menu.data-newdataset" ) ),//$NON-NLS-1$

	};

	private static final RegisterActions[] parameterActions = new RegisterActions[]{
			new RegisterActions( NewParameterAction.INSERT_SCALAR_PARAMETER,
					Messages.getString( "ParametersNodeProvider.menu.text.parameter" ) ),//$NON-NLS-1$
			new RegisterActions( NewParameterAction.INSERT_CASCADING_PARAMETER_GROUP,
					Messages.getString( "ParametersNodeProvider.menu.text.cascadingParameter" ) ),//$NON-NLS-1$
			new RegisterActions( NewParameterAction.INSERT_PARAMETER_GROUP,
					Messages.getString( "ParametersNodeProvider.menu.text.group" ) ),//$NON-NLS-1$

	};

	/**
	 * The name of the insert menu
	 */
	public static final String M_INSERT = "birtInsert"; //$NON-NLS-1$

	/**
	 * The name of the element menu
	 */
	public static final String M_ELEMENT = "birtElement"; //$NON-NLS-1$

	/**
	 * The name of the data menu
	 */
	public static final String M_DATA = "birtData"; //$NON-NLS-1$

	/*
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions( )
	{
		addRetargetAction( new UndoRetargetAction( ) );
		addRetargetAction( new RedoRetargetAction( ) );
		addRetargetAction( new DeleteRetargetAction( ) );

		addRetargetAction( new ZoomInRetargetAction( ) );
		addRetargetAction( new ZoomOutRetargetAction( ) );

		addRetargetAction( new RetargetAction( ActionFactory.CUT.getId( ), null ) );
		addRetargetAction( new RetargetAction( ActionFactory.COPY.getId( ),
				null ) );
		addRetargetAction( new RetargetAction( ActionFactory.PASTE.getId( ),
				null ) );

		addRetargetAction( new RetargetAction( ImportCSSStyleAction.ID,
				ImportCSSStyleAction.ACTION_TEXT ) );
		addRetargetAction( new RetargetAction( AddStyleAction.ID,
				Messages.getString( "DesignerActionBarContributor.style.new" ) ) ); //$NON-NLS-1$
		addRetargetAction( new RetargetAction( ApplyStyleMenuAction.ID, null ) );
		addRetargetAction( new RetargetAction( EditStyleMenuAction.ID, null ) );
		addRetargetAction( new RetargetAction( EditGroupMenuAction.ID, null ) );
		addRetargetAction( new RetargetAction( InsertGroupMenuAction.ID, null ) );

		registerActions( new RegisterActions[]{
			new RegisterActions( GEFActionConstants.TOGGLE_RULER_VISIBILITY,
					Messages.getString( "DesignerActionBarContributor.menu.element-showRuler" ), //$NON-NLS-1$
					IAction.AS_CHECK_BOX )
		} );
		registerActions( new RegisterActions[]{
			new RegisterActions( ToggleMarginVisibilityAction.ID,
					ToggleMarginVisibilityAction.LABEL,
					IAction.AS_CHECK_BOX )
		} );

		registerActions( getInsertElementActions( ) );
		registerActions( elementActions );
		registerActions( dataActions );

		addRetargetAction( new RetargetAction( ImportLibraryAction.ID,
				ImportLibraryAction.ACTION_TEXT ) );
		registerActions( parameterActions );

	}

	/**
	 * Gets insert elements actions including extension points.
	 */
	private RegisterActions[] getInsertElementActions( )
	{
		if ( insertElementActions == null )
		{
			insertElementActions = insertActions;
			List extensionPoints = ExtensionPointManager.getInstance( )
					.getExtendedElementPoints( );
			if ( !extensionPoints.isEmpty( ) )
			{
				insertElementActions = new RegisterActions[insertActions.length
						+ extensionPoints.size( )];
				for ( int i = 0; i < insertActions.length; i++ )
				{
					insertElementActions[i] = insertActions[i];
				}
				for ( int k = 0; k < extensionPoints.size( ); k++ )
				{
					ExtendedElementUIPoint point = (ExtendedElementUIPoint) extensionPoints.get( k );
					IElementDefn extension = DEUtil.getMetaDataDictionary( )
							.getExtension( point.getExtensionName( ) );
					String displayName = new String( );
					displayName = extension.getDisplayName( );
					if ( displayName.equalsIgnoreCase( "Chart" ) ) //$NON-NLS-1$
					{
						displayName = "&" + displayName; //$NON-NLS-1$
					}

					RegisterActions extAction = new RegisterActions( extension.getName( ),
							displayName );

					insertElementActions[insertActions.length + k] = extAction;
				}
			}
		}
		return insertElementActions;
	}

	private void registerActions( RegisterActions[] actions )
	{
		for ( int i = 0; i < actions.length; i++ )
		{
			if ( actions[i] != null )
				addRetargetAction( new RetargetAction( actions[i].id,
						actions[i].displayName,
						actions[i].style ) );
		}
	}

	/*
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys( )
	{
		addGlobalActionKey( ActionFactory.PRINT.getId( ) );
		addGlobalActionKey( ActionFactory.SELECT_ALL.getId( ) );
	}

	/*
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(IToolBarManager)
	 */
	public void contributeToToolBar( IToolBarManager tbm )
	{
		tbm.add( new Separator( ) );
		String[] zoomStrings = new String[]{
				ZoomManager.FIT_ALL,
				ZoomManager.FIT_HEIGHT,
				ZoomManager.FIT_WIDTH
		};
		tbm.add( new ZoomComboContributionItem( getPage( ), zoomStrings ) );
	}

	/*
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(IMenuManager)
	 */
	public void contributeToMenu( IMenuManager menubar )
	{
		super.contributeToMenu( menubar );
		updateEditMenu( menubar );
		// Insert Menu
		MenuManager insertMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.insert" ), M_INSERT ); //$NON-NLS-1$
		contributeActionsToMenu( insertMenu, getInsertElementActions( ) );
		insertMenu.add( new Separator( ) );
		insertMenu.add( getAction( ImportLibraryAction.ID ) );
		menubar.insertAfter( IWorkbenchActionConstants.M_EDIT, insertMenu );

		// Element Menu
		MenuManager elementMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element" ), M_ELEMENT ); //$NON-NLS-1$
		contributeActionsToMenu( elementMenu, elementActions );

		MenuManager insertGroupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.element.group" ), InsertGroupMenuAction.ID ); //$NON-NLS-1$
		insertGroupMenu.add( NoneAction.getInstance( ) );
		insertGroupMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateInsertGroupMenu( InsertGroupMenuAction.ID, manager );
			}
		} );
		elementMenu.add( insertGroupMenu );

		MenuManager editGroupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element-EditGroup" ) ); //$NON-NLS-1$
		editGroupMenu.add( NoneAction.getInstance( ) );
		editGroupMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( EditGroupMenuAction.ID, manager );
			}
		} );
		elementMenu.add( editGroupMenu );
		elementMenu.add( new Separator( ) );
		contributeStyleMenu( elementMenu );
		elementMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				IContributionItem addGroupMenu = manager.findUsingPath( AddGroupAction.ID );
				IContributionItem insertGroupMenus = manager.findUsingPath( InsertGroupMenuAction.ID );

				if ( addGroupMenu == null || insertGroupMenus == null )
				{
					return;
				}
				RetargetAction action = (RetargetAction) getAction( AddGroupAction.ID );
				if ( action != null
						&& action.getActionHandler( ) instanceof AddGroupAction )
				{
					if ( action.getActionHandler( ).isEnabled( ) )
					{
						addGroupMenu.setVisible( true );
						insertGroupMenus.setVisible( false );
					}
					else
					{
						addGroupMenu.setVisible( false );
						insertGroupMenus.setVisible( true );
					}
					manager.update( true );
				}
			}
		} );

		menubar.insertAfter( M_INSERT, elementMenu );

		// Data Menu
		MenuManager dataMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.data" ), M_DATA ); //$NON-NLS-1$
		dataMenu.add( getAction( dataActions[0].id ) );
		dataMenu.add( getAction( dataActions[1].id ) );
		editGroupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.data-NewParameter" ) ); //$NON-NLS-1$
		contributeActionsToMenu( editGroupMenu, parameterActions );

		// Add new parameter action

		dataMenu.add( editGroupMenu );

		menubar.insertAfter( M_ELEMENT, dataMenu );

		menubar.update( );
	}

	private void contributeActionsToMenu( MenuManager menu,
			RegisterActions[] actions )
	{
		for ( int i = 0; i < actions.length; i++ )
		{
			if ( actions[i] != null )
			{
				menu.add( getAction( actions[i].id ) );
			}
			else
			{
				menu.add( new Separator( ) );
			}
		}
	}

	private void contributeStyleMenu( MenuManager newMenu )
	{
		// add
		newMenu.add( getAction( ImportCSSStyleAction.ID ) );

		newMenu.add( getAction( AddStyleAction.ID ) );

		// edit
		MenuManager editStyleMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.style-rule-edit" ) ); //$NON-NLS-1$
		editStyleMenu.add( NoneAction.getInstance( ) );
		editStyleMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( EditStyleMenuAction.ID, manager );
			}
		} );
		newMenu.add( editStyleMenu );

		// apply
		MenuManager applyStyleMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.style-rule-apply" ) ); //$NON-NLS-1$
		applyStyleMenu.add( NoneAction.getInstance( ) );
		applyStyleMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( ApplyStyleMenuAction.ID, manager );
			}
		} );
		newMenu.add( applyStyleMenu );
	}

	private void updateDynamicItems( String actionId, IMenuManager menu )
	{
		RetargetAction action = (RetargetAction) getAction( actionId );
		if ( action != null
				&& action.getActionHandler( ) instanceof MenuUpdateAction )
		{
			( (MenuUpdateAction) action.getActionHandler( ) ).updateMenu( (MenuManager) menu );
		}
	}

	private void updateInsertGroupMenu( String actionId, IMenuManager menu )
	{
		RetargetAction action = (RetargetAction) getAction( actionId );
		if ( action != null
				&& action.getActionHandler( ) instanceof InsertGroupMenuAction )
		{
			( (InsertGroupMenuAction) action.getActionHandler( ) ).updateMenu( (MenuManager) menu );
		}
	}

	private void updateEditMenu( IContributionManager menubar )
	{
		IContributionItem editMenu = menubar.find( IWorkbenchActionConstants.M_EDIT );
		if ( editMenu instanceof IMenuManager )
		{
			( (IMenuManager) editMenu ).addMenuListener( new IMenuListener( ) {

				public void menuAboutToShow( IMenuManager manager )
				{
					refreshUpdateAction( ActionFactory.CUT.getId( ) );
					refreshUpdateAction( ActionFactory.COPY.getId( ) );
					refreshUpdateAction( ActionFactory.PASTE.getId( ) );
					refreshUpdateAction( ActionFactory.DELETE.getId( ) );
				}

				private void refreshUpdateAction( String actionId )
				{
					if ( getActionRegistry( ) != null )
					{
						RetargetAction action = (RetargetAction) getAction( actionId );
						if ( action != null
								&& action.getActionHandler( ) != null
								&& action.getActionHandler( ) instanceof UpdateAction )
						{
							( (UpdateAction) action.getActionHandler( ) ).update( );
						}
					}
				}
			} );
		}
	}
}