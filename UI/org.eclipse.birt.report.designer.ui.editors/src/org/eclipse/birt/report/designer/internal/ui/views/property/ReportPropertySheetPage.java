/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.AlphabeticallyViewSorter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.widget.ExpressionDialogCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableTree;
import org.eclipse.swt.custom.TableTreeEditor;
import org.eclipse.swt.custom.TableTreeItem;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * The customized implementation of property sheet page which presents a table
 * of property names and values obtained from the current selection in the
 * active workbench part. This page uses TableTreeViewer as the control to avoid
 * the complicated problem of the default property sheet.
 * <p>
 * This page obtains the information about what to properties display from the
 * current selection (which it tracks).
 * </p>
 * <p>
 * The model for this page is DE model which is selected in the active workbench
 * part. The page is a listener implementation to get notified by model changes.
 * The page may be configured with a custom model by setting the root input.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @see IPropertySource
 */
public class ReportPropertySheetPage extends Page implements
		IPropertySheetPage,
		Listener,
		IColleague
{

	private static final String COLUMN_TITLE_PROPERTY = Messages.getString( "ReportPropertySheetPage.Column.Title.Property" ); //$NON-NLS-1$
	private static final String COLUMN_TITLE_VALUE = Messages.getString( "ReportPropertySheetPage.Column.Title.Value" ); //$NON-NLS-1$

	private TableTreeViewer viewer;
	private ReportPropertySheetContentProvider contentProvider;
	private ReportPropertySheetLabelProvider labelProvider;
	private ISelection selection;

	private CellEditor cellEditor;
	private TableTree tableTree;
	private TableTreeEditor tableTreeEditor;

	private int columnToEdit = 1;
	private ICellEditorListener editorListener;
	private Object model;
	private List list;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		viewer = new TableTreeViewer( parent, SWT.FULL_SELECTION );
		tableTree = viewer.getTableTree( );
		tableTree.getTable( ).setHeaderVisible( true );
		tableTree.getTable( ).setLinesVisible( true );

		// configure the columns
		addColumns( );

		contentProvider = new ReportPropertySheetContentProvider( );
		viewer.setContentProvider( contentProvider );
		labelProvider = new ReportPropertySheetLabelProvider( );
		viewer.setLabelProvider( labelProvider );

		viewer.setColumnProperties( new String[]{
				COLUMN_TITLE_PROPERTY, COLUMN_TITLE_VALUE
		} );

		AlphabeticallyViewSorter sorter = new AlphabeticallyViewSorter( );
		sorter.setAscending( true );
		viewer.setSorter( sorter );

		hookControl( );

		// create a new table tree editor
		tableTreeEditor = new TableTreeEditor( tableTree );

		// create the editor listener
		createEditorListener( );

		handleGlobalAction( );

		// suport the mediator
		SessionHandleAdapter.getInstance( ).getMediator( ).addColleague( this );

		expandToDefaultLevel( );
	}

	/**
	 * 
	 */
	private void expandToDefaultLevel( )
	{
		// open the root node by default
		viewer.expandToLevel( 2 );
	}

	/**
	 * Creates a new cell editor listener.
	 */
	private void createEditorListener( )
	{
		editorListener = new ICellEditorListener( ) {

			public void cancelEditor( )
			{
				deactivateCellEditor( );
			}

			public void editorValueChanged( boolean oldValidState,
					boolean newValidState )
			{
				// Do nothing
			}

			public void applyEditorValue( )
			{
				applyValue( );
			}
		};
	}

	/**
	 * Establish this viewer as a listener on the control
	 */
	private void hookControl( )
	{
		// Handle selections in the TableTree
		// Part1: Double click only (allow traversal via keyboard without
		// activation
		tableTree.addSelectionListener( new SelectionAdapter( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{

				handleSelect( (TableTreeItem) e.item );
			}
		} );
		// Part2: handle single click activation of cell editor
		tableTree.getTable( ).addMouseListener( new MouseAdapter( ) {

			public void mouseDown( MouseEvent event )
			{
				// only activate if there is a cell editor
				Point pt = new Point( event.x, event.y );
				TableTreeItem item = tableTree.getItem( pt );
				if ( item != null )
				{
					handleSelect( item );
				}
			}
		} );

		tableTree.getTable( ).addKeyListener( new KeyAdapter( ) {

			public void keyReleased( KeyEvent e )
			{
				if ( e.character == SWT.ESC )
					deactivateCellEditor( );
				else if ( e.keyCode == SWT.F5 )
				{
					// Refresh the table when F5 pressed
					// The following will simulate a reselect
					viewer.setInput( viewer.getInput( ) );
				}
			}
		} );
	}

	/**
	 * Call redo on command stack
	 */
	protected void redo( )
	{
		try
		{
			if ( SessionHandleAdapter.getInstance( )
					.getCommandStack( )
					.canRedo( ) )
			{
				SessionHandleAdapter.getInstance( ).getCommandStack( ).redo( );
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void deactivateCellEditor( )
	{
		tableTreeEditor.setEditor( null, null, columnToEdit );
		if ( cellEditor != null )
		{
			cellEditor.deactivate( );
			cellEditor.removeListener( editorListener );
			cellEditor = null;
		}
	}

	/**
	 * @param item
	 */
	protected void handleSelect( TableTreeItem selection )
	{
		// deactivate the current cell editor
		if ( cellEditor != null )
		{
			applyValue( );
			deactivateCellEditor( );
		}

		// get the new selection
		TableTreeItem[] sel = new TableTreeItem[]{
			selection
		};
		if ( sel.length == 0 )
		{

		}
		else
		{
			// activate a cell editor on the selection
			// assume single selection
			activateCellEditor( sel[0] );
		}
	}

	/**
	 * 
	 */
	private void applyValue( )
	{
		if ( !cellEditor.isDirty( ) )
		{
			return;
		}

		if ( model instanceof GroupPropertyHandle )
		{
			try
			{
				( (GroupPropertyHandle) model ).setValue( cellEditor.getValue( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );

				// get the new selection
				TableTreeItem[] sel = viewer.getTableTree( ).getSelection( );
				if ( sel.length == 0 )
				{
					// Do nothing
				}
				else
				{
					// activate a cell editor on the selection
					// assume single selection
					activateCellEditor( sel[0] );
				}
			}
		}
	}

	/**
	 * @param item
	 */
	private void activateCellEditor( TableTreeItem sel )
	{

		if ( sel.isDisposed( ) )
			return;
		model = sel.getData( );

		// ensure the cell editor is visible
		tableTree.showSelection( );

		cellEditor = createCellEditor( model );

		if ( cellEditor == null )
			// unable to create the editor
			return;

		// set the created editor as current editor
		tableTreeEditor.setEditor( cellEditor.getControl( ) );

		// activate the cell editor
		cellEditor.activate( );

		// if the cell editor has no control we can stop now
		Control control = cellEditor.getControl( );
		if ( control == null )
		{
			cellEditor.deactivate( );
			cellEditor = null;
			return;
		}

		// add our editor listener
		cellEditor.addListener( editorListener );

		// set the layout of the table tree editor to match the cell editor
		CellEditor.LayoutData layout = cellEditor.getLayoutData( );
		tableTreeEditor.horizontalAlignment = layout.horizontalAlignment;
		tableTreeEditor.grabHorizontal = layout.grabHorizontal;
		tableTreeEditor.minimumWidth = layout.minimumWidth;
		tableTreeEditor.setEditor( control, sel, columnToEdit );

		// give focus to the cell editor
		cellEditor.setFocus( );

	}

	/**
	 * @param data
	 */
	private CellEditor createCellEditor( Object data )
	{
		CellEditor editor = null;
		if ( data instanceof GroupPropertyHandle
				&& ( (GroupPropertyHandle) ( data ) ).isVisible( ) )
		{
			editor = PropertyEditorFactory.getInstance( )
					.createPropertyEditor( tableTree.getTable( ), data );

			if ( editor instanceof ExpressionDialogCellEditor )
			{
				List dataSetList = null;
				Object arrays[] = list.toArray( );
				int len = arrays.length;
				if ( len > 0 )
				{
					( (ExpressionDialogCellEditor) editor ).setExpressionProvider( new ExpressionProvider( (DesignElementHandle) arrays[0] ) );
				}
//				for ( int i = 1; i < len; i++ )
//				{
//					dataSetList.retainAll( DEUtil.getDataSetList( (DesignElementHandle) arrays[i] ) );
//					if ( dataSetList.size( ) == 0 )
//					{
//						break;
//					}
//				}
			}

		}

		return editor;
	}

	/**
	 * Create default columns for property sheet page. The default columns are
	 * Property and value.
	 */
	private void addColumns( )
	{
		Table table = tableTree.getTable( );

		TableColumn column1 = new TableColumn( table, SWT.LEFT );
		column1.setText( COLUMN_TITLE_PROPERTY );
		TableColumn column2 = new TableColumn( table, SWT.LEFT );
		column2.setText( COLUMN_TITLE_VALUE );

		// property column
		ColumnLayoutData c1Layout = new ColumnWeightData( 40, false );

		// value column
		ColumnLayoutData c2Layout = new ColumnWeightData( 60, true );

		// set columns in Table layout
		TableLayout layout = new TableLayout( );
		layout.addColumnData( c1Layout );
		layout.addColumnData( c2Layout );
		table.setLayout( layout );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl( )
	{
		if ( viewer == null )
			return null;
		return viewer.getControl( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus( )
	{
		getControl( ).setFocus( );
	}

	public void selectionChanged( IWorkbenchPart part, ISelection selection )
	{
		deactivateCellEditor( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void handleSelectionChanged( ISelection selection )
	{
		if ( this.selection != null &&  this.selection.equals( selection )  )
			return;
		this.selection = selection;
		deRegisterListeners( );

		list = getModelList( selection );

		GroupElementHandle handle = DEUtil.getMultiSelectionHandle( list );

		if ( handle != null && !handle.isSameType( ) )
		{
			viewer.setInput( null );
			return;
		}

		viewer.setInput( handle );
		registerListeners( );

		expandToDefaultLevel( );
	}

	/**
	 * @param selection
	 * @return
	 */
	private List getModelList( ISelection selection )
	{
		List list = new ArrayList( );
		if ( selection == null )
			return list;
		if ( !( selection instanceof StructuredSelection ) )
			return list;

		StructuredSelection structured = (StructuredSelection) selection;
		if ( structured.getFirstElement( ) instanceof ReportElementEditPart )
		{
			for ( Iterator it = structured.iterator( ); it.hasNext( ); )
			{
				ReportElementEditPart object = (ReportElementEditPart) it.next( );
				if ( object instanceof DummyEditpart )
				{
					list.clear( );
					list.add( object.getModel( ) );
					break;
				}
				list.add( object.getModel( ) );

			}
		}
		else
		{
			list = structured.toList( );
		}
		return list;
	}

	/**
	 * Removes model change listener.
	 */
	protected void deRegisterListeners( )
	{
		if ( viewer == null )
			return;
		Object input = viewer.getInput( );
		if ( input == null )
			return;
		if ( input instanceof DesignElementHandle )
		{
			DesignElementHandle element = (DesignElementHandle) input;
			element.removeListener( this );
		}
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void registerListeners( )
	{
		if ( viewer == null )
			return;
		Object input = viewer.getInput( );
		if ( input == null )
			return;
		if ( input instanceof GroupElementHandle )
		{
			GroupElementHandle element = (GroupElementHandle) input;
			( (DesignElementHandle) element.getElements( ).get( 0 ) ).addListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Listener#elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if(!viewer.getTableTree( ).isDisposed( ))
		{
			viewer.refresh( true );
			expandToDefaultLevel( );
		}
	}

	/**
	 * Handles all global actions
	 */
	private void handleGlobalAction( )
	{
		for ( int i = 0; i < GlobalActionFactory.GLOBAL_STACK_ACTIONS.length; i++ )
		{
			String id = GlobalActionFactory.GLOBAL_STACK_ACTIONS[i];
			getSite( ).getActionBars( ).setGlobalActionHandler( id,
					GlobalActionFactory.createStackAction( id,
							SessionHandleAdapter.getInstance( )
									.getCommandStack( ) ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#dispose()
	 */
	public void dispose( )
	{
		// remove the mediator listener
		SessionHandleAdapter.getInstance( )
				.getMediator( )
				.removeColleague( this );
		unregisterListeners( );
		super.dispose( );
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void unregisterListeners( )
	{
		if ( viewer == null )
			return;
		Object input = viewer.getInput( );
		if ( input == null )
			return;
		if ( input instanceof GroupElementHandle )
		{
			GroupElementHandle element = (GroupElementHandle) input;
			( (DesignElementHandle) element.getElements( ).get( 0 ) ).removeListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.util.mediator.IColleague#performRequest(org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest)
	 */
	public void performRequest( ReportRequest request )
	{
		if ( ReportRequest.SELECTION.equals( request.getType( ) ) )
		{
			//Remove null from the list. That fix the bug 139422
			ArrayList selections = new ArrayList();
			selections.add(null);
			selections.addAll(request.getSelectionModelList());
			
			ArrayList nullList = new ArrayList();
			nullList.add(null);
			selections.removeAll(nullList);
			//end
			
			handleSelectionChanged( new StructuredSelection(selections));
		}
	}
}