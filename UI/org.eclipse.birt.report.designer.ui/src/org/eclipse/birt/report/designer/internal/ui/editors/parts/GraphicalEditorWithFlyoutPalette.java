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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportSelectionSynchronizer;
import org.eclipse.birt.report.designer.internal.ui.editors.notification.DeferredRefreshManager;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.ToggleRulerVisibilityAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.CustomizeAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.LayoutAction;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.PinDrawerAction;
import org.eclipse.gef.ui.palette.SettingsAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.customize.PaletteSettingsDialog;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class serves as a quick starting point for clients who are new to GEF.
 * It will create an Editor with a flyout palette. The flyout palette will only
 * be visible when the palette view is not open.
 * <p>
 * <EM>IMPORTANT</EM> This class should only be used as a reference for
 * creating your own EditorPart implementation. This class will not suit
 * everyone's needs, and may change in the future. Clients may copy the
 * implementation.
 * 
 * @author Pratik Shah
 * @since 3.0
 * @version $Revision: 1.1 $ $Date: 2005/02/05 06:30:14 $
 */
public abstract class GraphicalEditorWithFlyoutPalette extends GraphicalEditor implements
		EditorSelectionProvider
{

	private PaletteViewerProvider provider;
	private FlyoutPaletteComposite splitter;
	private CustomPalettePage page;
	private SelectionSynchronizer synchronizer;

	private ButtonPaneComposite bPane;

	/**
	 * the list of action ids that are to CommandStack actions
	 */
	private List stackActionIDs = new ArrayList( );
	/**
	 * the list of action ids that are editor actions
	 *  
	 */
	private List editorActionIDs = new ArrayList( );

	/**
	 * the list of action ids that are to EditPart actions
	 */
	private List editPartActionIDs = new ArrayList( );

	/**
	 * @see GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer( )
	{
		splitter.hookDropTargetListener( getGraphicalViewer( ) );
		createActions( );
		updateActions( stackActionIDs );
		// add selection change listener
		getSite( ).getWorkbenchWindow( )
				.getSelectionService( )
				.addSelectionListener( getSelectionListener( ) );
	}

	/**
	 *  @return the wrapper command stack for GEF framework
	 */
	public WrapperCommandStack getWrapperCommandStack( )
	{
		return (WrapperCommandStack) getMultiPageEditor( ).getAdapter( CommandStack.class );

	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
	 */
	protected void firePropertyChange( int propertyId )
	{
		super.firePropertyChange( propertyId );
		updateActions( editorActionIDs );
	}
	/**
	 * the selection listener
	 */
	private ISelectionListener selectionListener = new ISelectionListener( ) {

		public void selectionChanged( IWorkbenchPart part, ISelection selection )
		{
			updateActions( editPartActionIDs );
		}
	};

	/**
	 * Creates a PaletteViewerProvider that will be used to create palettes for
	 * the view and the flyout.
	 * 
	 * @return the palette provider
	 */
	protected PaletteViewerProvider createPaletteViewerProvider( )
	{
		return new PaletteViewerProvider( getEditDomain( ) ) {

			protected void configurePaletteViewer( PaletteViewer viewer )
			{
				//super.configurePaletteViewer( viewer );

				/**
				 * Hack to remove the Change Icon Size menu item and relative
				 * dialog components.
				 */
				viewer.setContextMenu( new PaletteContextMenuProvider( viewer ) {

					public void buildContextMenu( IMenuManager menu )
					{
						GEFActionConstants.addStandardActionGroups( menu );

						Object selectedPart = getPaletteViewer( ).getSelectedEditParts( )
								.get( 0 );
						if ( selectedPart instanceof DrawerEditPart
								&& ( (DrawerEditPart) selectedPart ).canBePinned( ) )
						{
							menu.appendToGroup( GEFActionConstants.MB_ADDITIONS,
									new PinDrawerAction( (DrawerEditPart) selectedPart ) );
						}
						menu.appendToGroup( GEFActionConstants.GROUP_VIEW,
								new LayoutAction( getPaletteViewer( ).getPaletteViewerPreferences( ) ) );
						if ( getPaletteViewer( ).getCustomizer( ) != null )
						{
							menu.appendToGroup( GEFActionConstants.GROUP_REST,
									new CustomizeAction( getPaletteViewer( ) ) );
						}

						final PaletteViewer paletteViewer = getPaletteViewer( );

						menu.appendToGroup( GEFActionConstants.GROUP_REST,
								new SettingsAction( paletteViewer ) {

									public void run( )
									{
										final PaletteViewerPreferences prefs = paletteViewer.getPaletteViewerPreferences( );

										Dialog settings = new PaletteSettingsDialog( paletteViewer.getControl( )
												.getShell( ),
												prefs ) {

											protected Control createLayoutSettings(
													Composite parent )
											{
												Composite composite = new Composite( parent,
														SWT.NONE );
												composite.setFont( parent.getFont( ) );
												GridLayout layout = new GridLayout( 1,
														false );
												composite.setLayout( layout );

												Control layoutOptions = createLayoutOptions( composite );
												GridData data = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
												layoutOptions.setLayoutData( data );

												handleLayoutSettingChanged( prefs.getLayoutSetting( ) );

												return composite;
											}

											protected void handleLayoutSettingChanged(
													int newSetting )
											{
												prefs.setLayoutSetting( newSetting );
											}
										};

										settings.open( );
									}
								} );
					}
				} );

				viewer.addDragSourceListener( new TemplateTransferDragSourceListener( viewer ) );
			}

			protected void hookPaletteViewer( PaletteViewer viewer )
			{
				super.hookPaletteViewer( viewer );
				//					final CopyTemplateAction copy = (CopyTemplateAction)
				// getActionRegistry( )
				//							.getAction( ActionFactory.COPY.getId( ) );
				//					if(copy !=null)
				//					{
				//						viewer.addSelectionChangedListener( copy );
				//					}
				//					if ( menuListener == null )
				//						menuListener = new IMenuListener( ) {
				//	
				//							public void menuAboutToShow( IMenuManager manager )
				//							{
				//								manager.appendToGroup(
				//										GEFActionConstants.GROUP_COPY, copy );
				//							}
				//						};
				//					viewer.getContextMenu( ).addMenuListener( menuListener );
			}
		};

	}

	/**
	 * @return a newly-created {@link CustomPalettePage}
	 */
	protected CustomPalettePage createPalettePage( )
	{
		return new CustomPalettePage( getPaletteViewerProvider( ) );
	}

	/**
	 * Creates actions and registers them to the ActionRegistry.
	 */
	protected void createActions( )
	{
		addStackAction( new UndoAction( this ) );
		addStackAction( new RedoAction( this ) );
		addEditPartAction( new DeleteAction( (IWorkbenchPart) this ) );

		SaveAction saveAction = new SaveAction( this );
		saveAction.setLazyEnablementCalculation( true );
		addEditorAction( saveAction );

		addAction( new CopyTemplateAction( this ) );
		
	}

	/**
	 * @return button pane that provides function to change pages.
	 */
	public ButtonPaneComposite getButtonPane( )
	{
		return bPane;
	}
	
	protected boolean hasRuler()
	{
		return false;
	}

	/**
	 * @see GraphicalEditor#createPartControl(Composite)
	 */
	public void createPartControl( Composite parent )
	{
		bPane = new ButtonPaneComposite( parent, 0, hasRuler() );

		splitter = new FlyoutPaletteComposite( bPane,
				SWT.NONE,
				getSite( ).getPage( ),
				getPaletteViewerProvider( ),
				getPalettePreferences( ) );
		super.createPartControl( splitter );

		Control ctrl = getGraphicalControl( );

		splitter.setGraphicalControl( ctrl );

		bPane.setGraphicalControl( splitter );

		if ( page != null )
		{
			splitter.setExternalViewer( page.getPaletteViewer( ) );
			page = null;
		}
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose( )
	{

		// remove selection listener
		getSite( ).getWorkbenchWindow( )
				.getSelectionService( )
				.removeSelectionListener( getSelectionListener( ) );
		// dispose the ActionRegistry (will dispose all actions)
		getActionRegistry( ).dispose( );
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class type )
	{
		if ( type == PalettePage.class )
		{
			if ( splitter == null )
			{
				page = createPalettePage( );
				return page;
			}
			return createPalettePage( );
		}

		if ( type == PaletteRoot.class )
		{
			return getPaletteRoot( );
		}

		if ( type == ZoomManager.class )
		{
			return getGraphicalViewer( ).getProperty( ZoomManager.class.toString( ) );
		}
		return super.getAdapter( type );
	}

	protected abstract IEditorPart getMultiPageEditor( );

	/**
	 * @return the graphical viewer's control
	 */
	protected Control getGraphicalControl( )
	{
		return getGraphicalViewer( ).getControl( );
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getGraphicalViewer()
	 */
	public GraphicalViewer getGraphicalViewer( )
	{
		return super.getGraphicalViewer( );
	}

	/**
	 * @return the FlyoutPreferences object used to save the flyout palette's
	 *         preferences
	 */
	protected abstract FlyoutPreferences getPalettePreferences( );

	/**
	 * Returns the PaletteRoot for the palette viewer.
	 * 
	 * @return the palette root
	 */
	protected abstract PaletteRoot getPaletteRoot( );

	/**
	 * Returns the palette viewer provider that is used to create palettes for
	 * the view and the flyout. Creates one if it doesn't already exist.
	 * 
	 * @return the PaletteViewerProvider that can be used to create
	 *         PaletteViewers for this editor
	 * @see #createPaletteViewerProvider()
	 */
	protected final PaletteViewerProvider getPaletteViewerProvider( )
	{
		if ( provider == null )
			provider = createPaletteViewerProvider( );
		return provider;
	}

	/**
	 * Sets the edit domain for this editor.
	 * 
	 * @param ed
	 *            The new EditDomain
	 */
	protected void setEditDomain( DefaultEditDomain ed )
	{
		super.setEditDomain( ed );
		getEditDomain( ).setPaletteRoot( getPaletteRoot( ) );
	}

	/**
	 * Creates the GraphicalViewer on the specified <code>Composite</code>.
	 * 
	 * @param parent
	 *            the parent composite
	 */
	protected void createGraphicalViewer( Composite parent )
	{
		DeferredGraphicalViewer viewer = new DeferredGraphicalViewer( );
		viewer.hookRefreshListener( getRefreshManager( ) );
		viewer.createControl( parent );
		setGraphicalViewer( viewer );
		configureGraphicalViewer( );
		hookGraphicalViewer( );
		initializeGraphicalViewer( );
		addAction ( new ToggleRulerVisibilityAction( this.getGraphicalViewer() ));
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getSelectionSynchronizer()
	 */
	public SelectionSynchronizer getSelectionSynchronizer( )
	{
		if ( synchronizer == null )
			synchronizer = new ReportSelectionSynchronizer( getGraphicalViewer( ) );
		return synchronizer;
	}

	/**
	 * Fires selection change event when part was changed.
	 */
	public void editActivate( )
	{
		SelectionChangedEvent event = new SelectionChangedEvent( (ReportSelectionSynchronizer) getSelectionSynchronizer( ),
				new StructuredSelection( this.getGraphicalViewer( )
						.getSelectedEditParts( ) ) );
		( (ReportSelectionSynchronizer) getSelectionSynchronizer( ) ).selectionChanged( event );

	}

	/**
	 * @return the DeferredRefreshManager
	 */
	protected abstract DeferredRefreshManager getRefreshManager( );

	/**
	 * A custom PalettePage that helps GraphicalEditorWithFlyoutPalette keep the
	 * two PaletteViewers (one displayed in the editor and the other displayed
	 * in the PaletteView) in sync when switching from one to the other (i.e.,
	 * it helps maintain state across the two viewers).
	 * 
	 * @author Pratik Shah
	 * @since 3.0
	 */
	protected class CustomPalettePage extends PaletteViewerPage
	{

		/**
		 * Constructor
		 * 
		 * @param provider
		 *            the provider used to create a PaletteViewer
		 */
		public CustomPalettePage( PaletteViewerProvider provider )
		{
			super( provider );
		}

		/**
		 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl( Composite parent )
		{
			super.createControl( parent );
			if ( splitter != null )
				splitter.setExternalViewer( viewer );
		}

		/**
		 * @see org.eclipse.ui.part.IPage#dispose()
		 */
		public void dispose( )
		{
			if ( splitter != null )
				splitter.setExternalViewer( null );
			super.dispose( );
		}

		/**
		 * @return the PaletteViewer created and displayed by this page
		 */
		public PaletteViewer getPaletteViewer( )
		{
			return viewer;
		}
	}

	protected void hookZoom( ScalableFreeformRootEditPart root )
	{
		List zoomLevels = new ArrayList( 3 );
		zoomLevels.add( ZoomManager.FIT_ALL );
		zoomLevels.add( ZoomManager.FIT_WIDTH );
		zoomLevels.add( ZoomManager.FIT_HEIGHT );
		root.getZoomManager( ).setZoomLevelContributions( zoomLevels );

		IAction zoomIn = new ZoomInAction( root.getZoomManager( ) );
		IAction zoomOut = new ZoomOutAction( root.getZoomManager( ) );
		getActionRegistry( ).registerAction( zoomIn );
		getActionRegistry( ).registerAction( zoomOut );
		getSite( ).getKeyBindingService( ).registerAction( zoomIn );
		getSite( ).getKeyBindingService( ).registerAction( zoomOut );
	}

	/**
	 * Returns the selection listener.
	 * 
	 * @return the <code>ISelectionListener</code>
	 */
	protected ISelectionListener getSelectionListener( )
	{
		return selectionListener;
	}

	/**
	 * Adds an action to this editor's <code>ActionRegistry</code>. (This is
	 * a helper method.)
	 * 
	 * @param action
	 *            the action to add.
	 */
	protected void addAction( IAction action )
	{
		getActionRegistry( ).registerAction( action );
	}

	/**
	 * Adds an editor action to this editor.
	 * 
	 * <p>
	 * <Editor actions are actions that depend and work on the editor.
	 * 
	 * @param action
	 *            the editor action
	 */
	protected void addEditorAction( EditorPartAction action )
	{
		getActionRegistry( ).registerAction( action );
		editorActionIDs.add( action.getId( ) );
	}

	/**
	 * Adds an <code>EditPart</code> action to this editor.
	 * 
	 * <p>
	 * <code>EditPart</code> actions are actions that depend and work on the
	 * selected <code>EditPart</code>s.
	 * 
	 * @param action
	 *            the <code>EditPart</code> action
	 */
	protected void addEditPartAction( SelectionAction action )
	{
		getActionRegistry( ).registerAction( action );
		editPartActionIDs.add( action.getId( ) );
	}

	/**
	 * Adds an <code>CommandStack</code> action to this editor.
	 * 
	 * <p>
	 * <code>CommandStack</code> actions are actions that depend and work on
	 * the <code>CommandStack</code>.
	 * 
	 * @param action
	 *            the <code>CommandStack</code> action
	 */
	protected void addStackAction( StackAction action )
	{
		getActionRegistry( ).registerAction( action );
		stackActionIDs.add( action.getId( ) );
	}

	/**
	 * Updates the specified actions.
	 * 
	 * @param actionIds
	 *            the list of ids of actions to update
	 */
	protected void updateActions( List actionIds )
	{
		for ( Iterator ids = actionIds.iterator( ); ids.hasNext( ); )
		{
			IAction action = getActionRegistry( ).getAction( ids.next( ) );
			if ( null != action && action instanceof UpdateAction )
				( (UpdateAction) action ).update( );
		}
	}

	/**
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.EditorSelectionProvider#updateStackActions()
	 */
	public void updateStackActions( )
	{
		updateActions( stackActionIDs );

	}
}