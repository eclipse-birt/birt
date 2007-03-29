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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.schematic.RowHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.CustomizeAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.LayoutAction;
import org.eclipse.gef.ui.palette.PaletteContextMenuProvider;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.SettingsAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.customize.PaletteSettingsDialog;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class serves as a quick starting point for clients who are new to GEF.
 * It will create an Editor with a flyout palette. The flyout palette will only
 * be visible when the palette view is not open.
 * <p>
 */
public abstract class GraphicalEditorWithFlyoutPalette extends GraphicalEditor implements
		EditorSelectionProvider,
		IColleague
{

	private PaletteViewerProvider provider;
	private FlyoutPaletteComposite splitter;
	private CustomPalettePage page;
	// private ButtonPaneComposite bPane;

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
		// createActions( );
		updateActions( stackActionIDs );
		// add selection change listener
		getSite( ).getWorkbenchWindow( )
				.getSelectionService( )
				.addSelectionListener( getSelectionListener( ) );
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

			protected void configurePaletteViewer( final PaletteViewer viewer )
			{
				// super.configurePaletteViewer( viewer );

				/**
				 * Hack to remove the Change Icon Size menu item and relative
				 * dialog components.
				 */
				viewer.setContextMenu( new PaletteContextMenuProvider( viewer ) {

					public void buildContextMenu( IMenuManager menu )
					{
						GEFActionConstants.addStandardActionGroups( menu );

						List lst = getPaletteViewer( ).getSelectedEditParts( );

						if ( lst.size( ) == 0 )
						{
							return;
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

				viewer.getControl( ).addMouseListener( new MouseListener( ) {

					public void mouseDoubleClick( MouseEvent e )
					{
						EditPart editPart = viewer.findObjectAt( new Point( e.x,
								e.y ) );
						CombinedTemplateCreationEntry entry = null;
						if ( editPart != null
								&& editPart.getModel( ) instanceof CombinedTemplateCreationEntry )
						{
							entry = (CombinedTemplateCreationEntry) editPart.getModel( );
						}
						if ( entry == null )
							return;
						ReportCreationTool tool = (ReportCreationTool) entry.createTool( );

						final EditDomain domain = UIUtil.getLayoutEditPartViewer( )
								.getEditDomain( );
						tool.setEditDomain( domain );
						tool.setViewer( UIUtil.getLayoutEditPartViewer( ) );
						tool.performCreation( UIUtil.getCurrentEditPart( ) );

						Display.getCurrent( ).asyncExec( new Runnable( ) {

							public void run( )
							{
								domain.loadDefaultTool( );
							}
						} );
					}

					public void mouseDown( MouseEvent e )
					{
					}

					public void mouseUp( MouseEvent e )
					{
					}
				} );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.ui.palette.PaletteViewerProvider#createPaletteViewer(org.eclipse.swt.widgets.Composite)
			 */
			public PaletteViewer createPaletteViewer( Composite parent )
			{
				PaletteViewer pViewer = new PaletteViewer( );

				// Replace with new factory
				pViewer.setEditPartFactory( new PaletteEditPartFactory( ) );
				pViewer.createControl( parent );
				configurePaletteViewer( pViewer );
				hookPaletteViewer( pViewer );
				return pViewer;
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
		addEditPartAction( new DeleteAction( (IWorkbenchPart) this ) {

//			public Command createDeleteCommand( List objects )
//			{
//				if ( objects.isEmpty( ) )
//					return null;
//				if ( !( objects.get( 0 ) instanceof EditPart ) )
//					return null;
//
//				GroupRequest deleteReq = new GroupRequest( RequestConstants.REQ_DELETE );
//				deleteReq.setEditParts( objects );
//
//				List temp = TableUtil.filletCellModel(objects);
//				List list = new ArrayList( );
//				for ( int i = 0; i < temp.size( ); i++ )
//				{
//					EditPart object = (EditPart) temp.get( i );
//					list.add( object.getModel( ) );
//				}
//				return new DeleteCommand( list.toArray( ) );
//			}
		} );

		SaveAction saveAction = new SaveAction( this );
		saveAction.setLazyEnablementCalculation( true );
		addEditorAction( saveAction );

		addAction( new CopyTemplateAction( this ) );

	}

	/**
	 * @return button pane that provides function to change pages.
	 */
	// public ButtonPaneComposite getButtonPane( )
	// {
	// return bPane;
	// }
	protected boolean hasRuler( )
	{
		return false;
	}

	protected boolean hasButtonPane( )
	{
		return true;
	}

	/**
	 * @see GraphicalEditor#createPartControl(Composite)
	 */
	public void createPartControl( Composite parent )
	{
		// if ( hasButtonPane( ) )
		// {
		// bPane = new ButtonPaneComposite( parent, 0, hasRuler( ) );
		// parent = bPane;
		// }

		splitter = new FlyoutPaletteComposite( parent,
				SWT.NONE,
				getSite( ).getPage( ),
				getPaletteViewerProvider( ),
				getPalettePreferences( ) );
		super.createPartControl( splitter );

		Control ctrl = getGraphicalControl( );

		splitter.setGraphicalControl( ctrl );

		// if ( hasButtonPane( ) )
		// {
		// bPane.setGraphicalControl( splitter );
		// }

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
		// remove the mediator listener
		SessionHandleAdapter.getInstance( )
				.getMediator( )
				.removeColleague( this );
			
		// remove selection listener

		getSite( ).getWorkbenchWindow( )
				.getSelectionService( )
				.removeSelectionListener( getSelectionListener( ) );
		// dispose the ActionRegistry (will dispose all actions)
		super.dispose();
		if ( splitter != null )
			splitter.setExternalViewer( null );
		splitter = null;
		getSelectionActions().clear();
		getActionRegistry( ).dispose( );
		List list = new ArrayList();
		Iterator actions = getActionRegistry( ).getActions();
		while (actions.hasNext()) {
			IAction action = (IAction)actions.next();
			list.add(action);
			//getActionRegistry( ).removeAction(action);
		}
		for (int i=0;i<list.size(); i++)
		{
			getActionRegistry( ).removeAction((IAction)list.get(i));
		}
		//( (ReportMultiPageEditorSite)getSite()).dispose();
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
		
		viewer.createControl( parent );
		setGraphicalViewer( viewer );
		configureGraphicalViewer( );
		hookGraphicalViewer( );
		initializeGraphicalViewer( );
		// addAction ( new ToggleRulerVisibilityAction(
		// this.getGraphicalViewer() ));

		// suport the mediator
		SessionHandleAdapter.getInstance( ).getMediator( ).addColleague( this );
	}

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

	// add supoet the report media, may be use a helpler
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest(org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest)
	 */
	public void performRequest( ReportRequest request )
	{
		if ( ReportRequest.SELECTION.equals( request.getType( ) ) )
		{
			handleSelectionChange( request );
		}
		else if ( ReportRequest.CREATE_ELEMENT.equals( request.getType( ) ) )
		{
			handleCreateElement( request );
		}
	}

	/**
	 * @param request
	 */
	protected void handleCreateElement( ReportRequest request )
	{
		final GraphicalViewer viewer = getGraphicalViewer( );
		if ( !viewer.getControl( ).isVisible( ) )
		{
			return;
		}

		final List list = request.getSelectionModelList( );
		if ( list.size( ) != 1 )
		{
			return;
		}
		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{

				Object part = viewer.getEditPartRegistry( ).get( list.get( 0 ) );
				if ( part instanceof EditPart )
				{
					Request directEditRequest = new Request( ReportRequest.CREATE_ELEMENT );
					if ( ( (EditPart) part ).understandsRequest( directEditRequest ) )
					{
						( (EditPart) part ).performRequest( directEditRequest );
					}
				}
			}
		} );

	}

	/**
	 * @param request
	 */
	protected void handleSelectionChange( ReportRequest request )
	{
		List select = convertEventToGFE( request );
		if ( select == null )
		{
			return;
		}
		getGraphicalViewer( ).setSelection( new StructuredSelection( select ) );

		if ( select.size( ) > 0 )
			getGraphicalViewer( ).reveal( (EditPart) select.get( select.size( ) - 1 ) );
	}

	/**
	 * Returns the created event if the given event is editpart event
	 * 
	 * @param event
	 *            the selection changed event
	 * @return the created event
	 */
	private List convertEventToGFE( ReportRequest event )
	{
		if ( event.getSource( ) == getGraphicalViewer())
		{
			return null;
		}
		ArrayList tempList = new ArrayList( );
		List list = event.getSelectionModelList( );
		int size = list.size( );

		if ( size != 0 && list.get( 0 ) instanceof RowHandle )
		{
			// Fix Bugzilla Bug 109571
			RowHandle handle = (RowHandle) list.get( 0 );

			RowHandleAdapter adapter = HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( handle );

			Object tableParent = adapter.getTableParent( );
			if (tableParent == null)
			{
				return null;
			}
			TableEditPart part = (TableEditPart) getGraphicalViewer( ).getEditPartRegistry( )
					.get( tableParent );
			int[] selectRows = new int[]{
				adapter.getRowNumber( )
			};
			for ( int i = 1; i < size; i++ )
			{
				Object o = list.get( i );
				if ( o instanceof RowHandle )
				{
					handle = (RowHandle) o;
					adapter = HandleAdapterFactory.getInstance( )
							.getRowHandleAdapter( handle );
					// not sample table, return null
					if ( tableParent != adapter.getTableParent( ) )
					{
						return null;
					}

					int len = selectRows.length;
					int temp[] = new int[len + 1];
					System.arraycopy( selectRows, 0, temp, 0, len );
					temp[len] = adapter.getRowNumber( );
					selectRows = temp;
				}
				else
				// not suport this kind of selection
				{
					return null;
				}
			}

			if ( handle.getRoot( ) == null )
			{
				return null;
			}
			// end

			if ( part != null )
			{
				Arrays.sort( selectRows );
				int len = selectRows.length;
				if ( len > 1 )
				{
					for ( int i = 0; i < len - 1; i++ )
					{
						if ( selectRows[i + 1] - selectRows[i] != 1 )
						{
							return null;
						}
					}
				}
				part.selectRow( selectRows );
			}
			return null;
		}
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof EditPart )
			{
				tempList.add( obj );
			}
			else
			{
				Object part = null;
//				if ( obj instanceof ReportElementModel )
//				{
//					obj = ( ( (ReportElementModel) obj ).getSlotHandle( ) );
//					part = getGraphicalViewer( ).getEditPartRegistry( )
//							.get( new ListBandProxy( (SlotHandle) obj ) );
//				}else
					if ( obj instanceof SlotHandle )
					{
						obj = ( (SlotHandle) obj );
						part = getGraphicalViewer( ).getEditPartRegistry( )
								.get( new ListBandProxy( (SlotHandle) obj ) );
					}
				else
				{
					part = getGraphicalViewer( ).getEditPartRegistry( )
							.get( obj );
					if (part == null)
					{
						part = getInterestEditPart( getGraphicalViewer( ).getRootEditPart( ), obj );
					}
				}
				if ( part instanceof EditPart )
				{
					tempList.add( part );
				}
			}
		}

		if ( tempList.isEmpty( ) )
		{
			return null;
		}

		return tempList;
	}
	
	private EditPart getInterestEditPart(EditPart part, Object obj)
	{
		List chList = part.getChildren( );
		for (int i=0; i<chList.size( ); i++)
		{
			ReportElementEditPart reportEditPart = (ReportElementEditPart)chList.get( i );
			if (reportEditPart.isinterestSelection( obj ))
			{
				return reportEditPart;
			}
			else
			{
				EditPart retValue = getInterestEditPart(reportEditPart, obj);
				if (retValue != null)
				{
					return retValue;
				}
			}
		}
		return null;
	}
	public void setFocus( )
	{
		if(getGraphicalViewer()!=null && getGraphicalViewer().getControl()!=null)
		{
			super.setFocus( );
		}
		return;
	}

}