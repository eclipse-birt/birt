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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.ToggleBreadcrumbAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CopyCellContentsContextAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CreateChartAction;
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
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.CategorizedElementSorter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportLibraryAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.ApplyStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.DeleteStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.EditGroupMenuAction;
import org.eclipse.birt.report.designer.ui.actions.EditStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertAggregationAction;
import org.eclipse.birt.report.designer.ui.actions.InsertGroupMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertRelativeTimePeriodAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.designer.ui.actions.NoneAction;
import org.eclipse.birt.report.designer.ui.actions.ToggleMarginVisibilityAction;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.SubActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.menus.IMenuService;

import com.ibm.icu.text.Collator;

/**
 * Toolbar and menu contributor for designer
 * 
 */
public class DesignerActionBarContributor extends
		org.eclipse.gef.ui.actions.ActionBarContributor
{

	/**
	 * RegisterAction
	 */
	static class RegisterAction
	{

		private String id, displayName;

		private int style;

		public RegisterAction( String id, String displayName )
		{
			this( id, displayName, IAction.AS_UNSPECIFIED );
		}

		public RegisterAction( String id, String displayName, int style )
		{
			this.id = id;
			this.displayName = displayName;
			this.style = style;
		}
	}

	private RegisterAction[] insertElementActions = null;

	private boolean isBuilt;

	private static RegisterAction[] insertActions = new RegisterAction[]{
	new RegisterAction( GeneralInsertMenuAction.INSERT_LABEL_ID,
			GeneralInsertMenuAction.INSERT_LABEL_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_TEXT_ID,
					GeneralInsertMenuAction.INSERT_TEXT_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_ID,
					GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_DATA_ID,
					GeneralInsertMenuAction.INSERT_DATA_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_IMAGE_ID,
					GeneralInsertMenuAction.INSERT_IMAGE_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_GRID_ID,
					GeneralInsertMenuAction.INSERT_GRID_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_LIST_ID,
					GeneralInsertMenuAction.INSERT_LIST_DISPLAY_TEXT ),
			new RegisterAction( GeneralInsertMenuAction.INSERT_TABLE_ID,
					GeneralInsertMenuAction.INSERT_TABLE_DISPLAY_TEXT )
	};

	private static final RegisterAction[] elementActions = new RegisterAction[]{
	new RegisterAction( InsertRowAboveAction.ID,
			Messages.getString( "DesignerActionBarContributor.element.InsertRowAbove" ) ), //$NON-NLS-1$
			new RegisterAction( InsertRowBelowAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertRowBelow" ) ), //$NON-NLS-1$
			null,
			new RegisterAction( InsertColumnLeftAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertColumnLeft" ) ), //$NON-NLS-1$
			new RegisterAction( InsertColumnRightAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.InsertColumnRight" ) ), //$NON-NLS-1$
			null,
			new RegisterAction( MergeAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.merge" ) ), //$NON-NLS-1$
			new RegisterAction( SplitAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.split" ) ), //$NON-NLS-1$
			new RegisterAction( CopyCellContentsContextAction.ID,
					Messages.getString( "CopyCellContentsContextAction.actionText" ) ), //$NON-NLS-1$
			null,
			new RegisterAction( CreatePlaceHolderPartAction.ID,
					Messages.getString( "CreatePlaceHolderAction.text" ) ), //$NON-NLS-1$
			new RegisterAction( RevertToReportItemPartAction.ID,
					Messages.getString( "RevertToReportItemAction.text" ) ), //$NON-NLS-1$
			null,
			new RegisterAction( CreateChartAction.ID,
					Messages.getString( "CreateChartAction.text" ) ), //$NON-NLS-1$
			null,
			new RegisterAction( AddGroupAction.ID,
					Messages.getString( "DesignerActionBarContributor.element.group" ) ), //$NON-NLS-1$,
	};
	private ToggleBreadcrumbAction toggleBreadcrumbAction;
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

	private IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener( ) {

		public void propertyChange( PropertyChangeEvent event )
		{
			RegisterAction[] actions = getInsertElementActions( );
			if ( actions != null )
			{
				for ( int i = 0; i < actions.length; i++ )
				{
					if ( event.getProperty( )
							.equals( SubActionBars.P_ACTION_HANDLERS ) )
					{
						if ( getAction( actions[i].id ) instanceof ReportRetargetAction )
						{
							( (ReportRetargetAction) getAction( actions[i].id ) ).propagateChange( event );
						}
					}
				}
			}
		}
	};

	public void init( IActionBars bars )
	{
		super.init( bars );
		if ( bars instanceof SubActionBars )
		{
			( (SubActionBars) bars ).addPropertyChangeListener( propertyChangeListener );
		}
	}

	/*
	 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
	 */
	protected void buildActions( )
	{
		if ( isBuilt )
			return;
		isBuilt = true;
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
		addRetargetAction( new RetargetAction( DeleteStyleMenuAction.ID, null ) );
		addRetargetAction( new RetargetAction( EditGroupMenuAction.ID, null ) );
		addRetargetAction( new RetargetAction( InsertGroupMenuAction.ID, null ) );

		registerActions( new RegisterAction[]{
			new RegisterAction( GEFActionConstants.TOGGLE_RULER_VISIBILITY,
					Messages.getString( "DesignerActionBarContributor.menu.element-showRuler" ), //$NON-NLS-1$
					IAction.AS_CHECK_BOX )
		} );
		registerActions( new RegisterAction[]{
			new RegisterAction( ToggleMarginVisibilityAction.ID,
					ToggleMarginVisibilityAction.LABEL,
					IAction.AS_CHECK_BOX )
		} );

		registerActions( getInsertElementActions( ) );
		registerActions( elementActions );

		addRetargetAction( new RetargetAction( ImportLibraryAction.ID,
				ImportLibraryAction.ACTION_TEXT ) );
		addRetargetAction( new RetargetAction( InsertAggregationAction.ID,
				InsertAggregationAction.TEXT ) );
		addRetargetAction( new RetargetAction( InsertRelativeTimePeriodAction.ID,
				InsertRelativeTimePeriodAction.TEXT ) );
	}

	/**
	 * Gets insert elements actions including extension points.
	 */
	private RegisterAction[] getInsertElementActions( )
	{
		if ( insertElementActions == null )
		{
			List extensionPoints = ExtensionPointManager.getInstance( )
					.getExtendedElementPoints( );

			CategorizedElementSorter<RegisterAction> elementSorter = new CategorizedElementSorter<RegisterAction>( );

			if ( !extensionPoints.isEmpty( ) )
			{
				for ( int k = 0; k < extensionPoints.size( ); k++ )
				{
					ExtendedElementUIPoint point = (ExtendedElementUIPoint) extensionPoints.get( k );
					if ( !UIUtil.isVisibleExtensionElement( point ) )
						continue;

					IElementDefn extension = DEUtil.getMetaDataDictionary( )
							.getExtension( point.getExtensionName( ) );

					String menuLabel = (String) point.getAttribute( IExtensionConstants.ATTRIBUTE_EDITOR_MENU_LABEL );

					RegisterAction extAction = new RegisterAction( extension.getName( ),
							menuLabel == null ? extension.getDisplayName( )
									: menuLabel );

					elementSorter.addElement( (String) point.getAttribute( IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY ),
							extAction );
				}
			}

			// experimental
			PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries( );

			for ( int i = 0; i < entries.length; i++ )
			{
				RegisterAction extAction = new RegisterAction( entries[i].getItemName( ),
						entries[i].getMenuLabel( ) );

				elementSorter.addElement( entries[i].getCategory( ), extAction );
			}

			List<RegisterAction> actions = elementSorter.getSortedElements( );

			Collections.sort( actions, new Comparator<RegisterAction>( ) {

				public int compare( RegisterAction o1, RegisterAction o2 )
				{
					return Collator.getInstance( ).compare( o1.displayName,
							o2.displayName );
				}
			} );

			insertElementActions = new RegisterAction[insertActions.length
					+ actions.size( )];

			for ( int i = 0; i < insertActions.length; i++ )
			{
				insertElementActions[i] = insertActions[i];
			}

			for ( int i = 0; i < actions.size( ); i++ )
			{
				insertElementActions[insertActions.length + i] = actions.get( i );
			}

		}
		return insertElementActions;
	}

	private void registerActions( RegisterAction[] actions )
	{
		for ( int i = 0; i < actions.length; i++ )
		{
			if ( actions[i] != null )
				addRetargetAction( new ReportRetargetAction( actions[i].id,
						actions[i].displayName,
						actions[i].style ) );
		}
	}

	private static class ReportRetargetAction extends RetargetAction
	{

		public ReportRetargetAction( String actionID, String text, int style )
		{
			super( actionID, text, style );
		}

		public void propagateChange( PropertyChangeEvent event )
		{
			super.propagateChange( event );
		}
	}

	/*
	 * @see
	 * org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
	 */
	protected void declareGlobalActionKeys( )
	{
		addGlobalActionKey( ActionFactory.PRINT.getId( ) );
		addGlobalActionKey( ActionFactory.SELECT_ALL.getId( ) );
	}

	/*
	 * @seeorg.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(
	 * IToolBarManager)
	 */
	public void contributeToToolBar( IToolBarManager tbm )
	{

		tbm.add( new Separator( ) );
		String[] zoomStrings = new String[]{
				ZoomManager.FIT_ALL,
				ZoomManager.FIT_HEIGHT,
				ZoomManager.FIT_WIDTH
		};
		ZoomComboContributionItem zoomComboContributionItem = new ZoomComboContributionItem( getPage( ),
				zoomStrings ) {

			protected Control createControl( Composite parent )
			{
				Control control = super.createControl( parent );
				control.setToolTipText( Messages.getString( "DesignerActionBarContributor.menu.zoomCombo.tooltip" ) );
				return control;
			}
		};

		if ( getPage( ) != null && getPage( ).getActivePart( ) != null )
		{
			zoomComboContributionItem.setZoomManager( (ZoomManager) getPage( ).getActivePart( )
					.getAdapter( ZoomManager.class ) );
		}

		zoomComboContributionItem.setVisible( true );
		tbm.add( zoomComboContributionItem );

		toggleBreadcrumbAction = new ToggleBreadcrumbAction( getPage( ) );
		tbm.add( toggleBreadcrumbAction );
	}

	/*
	 * @see
	 * org.eclipse.ui.part.EditorActionBarContributor#contributeToMenu(IMenuManager
	 * )
	 */
	public void contributeToMenu( IMenuManager menubar )
	{
		super.contributeToMenu( menubar );
		updateEditMenu( menubar );
		// Insert Menu
		MenuManager insertMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.insert" ), M_INSERT ); //$NON-NLS-1$
		createInsertMenu( insertMenu );

		insertMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				manager.removeAll( );
				insertElementActions = null;
				createInsertMenu( manager );
			}
		} );

		// insertMenu.add( getAction( ImportLibraryAction.ID ) );
		menubar.insertAfter( IWorkbenchActionConstants.M_EDIT, insertMenu );

		// Element Menu
		MenuManager elementMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element" ), M_ELEMENT ); //$NON-NLS-1$
		contributeElementMenu( elementMenu );
		menubar.insertAfter( M_INSERT, elementMenu );

		// Data Menu
		MenuManager dataMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.data" ), M_DATA ); //$NON-NLS-1$

		// the data actions are now registered through eclipse menu extensions
		IMenuService menuService = (IMenuService) PlatformUI.getWorkbench( )
				.getService( IMenuService.class );
		menuService.populateContributionManager( dataMenu, "menu:birtData" ); //$NON-NLS-1$
		menubar.insertAfter( M_ELEMENT, dataMenu );

		menubar.update( );
	}

	protected void createInsertMenu( IMenuManager insertMenu )
	{
		contributeActionsToMenu( insertMenu, getInsertElementActions( ) );
		insertMenu.add( new Separator( ) );
		insertMenu.add( getAction( InsertAggregationAction.ID ) );
		insertMenu.add( getAction( InsertRelativeTimePeriodAction.ID ) );
		insertMenu.add( new Separator( ) );
	}

	private void contributeElementMenu( MenuManager elementMenu )
	{
		MenuManager insertMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element.insert" ) ); //$NON-NLS-1$
		insertMenu.add( getAction( InsertRowAboveAction.ID ) );
		insertMenu.add( getAction( InsertRowBelowAction.ID ) );
		insertMenu.add( getAction( InsertColumnLeftAction.ID ) );
		insertMenu.add( getAction( InsertColumnRightAction.ID ) );
		elementMenu.add( insertMenu );
		elementMenu.add( new Separator( ) );

		elementMenu.add( getAction( MergeAction.ID ) );
		elementMenu.add( getAction( SplitAction.ID ) );
		elementMenu.add( new Separator( ) );

		MenuManager groupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element.group" ) ); //$NON-NLS-1$
		MenuManager insertGroupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.element.group" ), InsertGroupMenuAction.ID ); //$NON-NLS-1$
		insertGroupMenu.add( NoneAction.getInstance( ) );
		insertGroupMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateInsertGroupMenu( InsertGroupMenuAction.ID, manager );
			}
		} );
		groupMenu.add( insertGroupMenu );

		MenuManager editGroupMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element-EditGroup" ) ); //$NON-NLS-1$
		editGroupMenu.add( NoneAction.getInstance( ) );
		editGroupMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( EditGroupMenuAction.ID, manager );
			}
		} );
		groupMenu.add( editGroupMenu );
		elementMenu.add( groupMenu );
		elementMenu.add( new Separator( ) );

		elementMenu.add( getAction( CreatePlaceHolderPartAction.ID ) );
		elementMenu.add( getAction( RevertToReportItemPartAction.ID ) );

		elementMenu.add( new Separator( ) );
		elementMenu.add( getAction( CreateChartAction.ID ) );
		elementMenu.add( new Separator( ) );

		MenuManager styleMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.element.style" ) ); //$NON-NLS-1$
		contributeStyleMenu( styleMenu );
		elementMenu.add( styleMenu );

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

	}

	private void contributeActionsToMenu( IMenuManager menu,
			RegisterAction[] actions )
	{
		for ( int i = 0; i < actions.length; i++ )
		{
			if ( actions[i] != null )
			{
				IAction action = getAction( actions[i].id );

				menu.add( action );

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

		newMenu.add( getAction( AddStyleAction.ID ) );

		newMenu.add( new Separator( ) );

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

		// delete
		MenuManager delStyleMenu = new MenuManager( Messages.getString( "DesignerActionBarContributor.menu.style-rule-remove" ) ); //$NON-NLS-1$
		delStyleMenu.add( NoneAction.getInstance( ) );
		delStyleMenu.addMenuListener( new IMenuListener( ) {

			public void menuAboutToShow( IMenuManager manager )
			{
				updateDynamicItems( DeleteStyleMenuAction.ID, manager );
			}
		} );
		newMenu.add( delStyleMenu );

		newMenu.add( new Separator( ) );

		newMenu.add( getAction( ImportCSSStyleAction.ID ) );
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

	@Override
	public void dispose( )
	{
		if ( toggleBreadcrumbAction != null )
		{
			toggleBreadcrumbAction.dispose( );
		}
		if ( getActionBars( ) instanceof SubActionBars )
		{
			( (SubActionBars) getActionBars( ) ).removePropertyChangeListener( propertyChangeListener );
		}
		super.dispose( );
	}
}