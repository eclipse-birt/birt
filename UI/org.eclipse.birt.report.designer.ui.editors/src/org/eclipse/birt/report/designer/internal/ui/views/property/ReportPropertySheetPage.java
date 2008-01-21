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
import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.core.util.mediator.IColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyTitle;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.AlphabeticallyViewSorter;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoBuilder;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
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
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchPage;
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

	private CustomTreeViewer viewer;
	private ReportPropertySheetContentProvider contentProvider;
	private ReportPropertySheetLabelProvider labelProvider;
	private ISelection selection;

	private CellEditor cellEditor;
	private Tree tableTree;
	private TreeEditor tableTreeEditor;

	private int columnToEdit = 1;
	private ICellEditorListener editorListener;
	private Object model;
	private List list;
	private TabbedPropertyTitle title;
	private Composite container;
	private IMemento propertySheetMemento;
	private IMemento viewerMemento;
	protected String propertyViewerID = "Report_Property_Sheet_Page_Viewer_ID";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl( Composite parent )
	{
		container = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout( layout );
		title = new TabbedPropertyTitle( container,
				FormWidgetFactory.getInstance( ) );
		title.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite viewerContainer = new Composite( container, SWT.NONE );
		layout = new GridLayout( );
		layout.marginWidth = 10;
		layout.marginHeight = 3;
		viewerContainer.setLayout( layout );
		viewerContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		viewer = new CustomTreeViewer( viewerContainer, SWT.FULL_SELECTION );
		tableTree = viewer.getTree( );
		tableTree.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		tableTree.setHeaderVisible( true );
		tableTree.setLinesVisible( true );

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
		tableTreeEditor = new TreeEditor( tableTree );

		// create the editor listener
		createEditorListener( );

		handleGlobalAction( );

		// suport the mediator
		SessionHandleAdapter.getInstance( ).getMediator( ).addColleague( this );

		FormWidgetFactory.getInstance( ).paintFormStyle( parent );
		FormWidgetFactory.getInstance( ).adapt( parent );

		IWorkbenchPage page = getSite( ).getPage( );

		MementoBuilder builder = new MementoBuilder( );
		if ( ( propertySheetMemento = builder.getRootMemento( )
				.getChild( IPageLayout.ID_PROP_SHEET ) ) == null )
		{
			propertySheetMemento = builder.getRootMemento( )
					.createChild( IPageLayout.ID_PROP_SHEET,
							MementoElement.Type_View );
		}

		if ( ( viewerMemento = propertySheetMemento.getChild( propertyViewerID ) ) == null )
		{
			viewerMemento = propertySheetMemento.createChild( propertyViewerID,
					MementoElement.Type_Viewer );
		}

		handleSelectionChanged( page.getSelection( ) );
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
			}

			public void applyEditorValue( )
			{
				applyValue( );
				if ( changed )
					refresh( );
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

				handleSelect( (TreeItem) e.item );
			}
		} );
		// Part2: handle single click activation of cell editor
		tableTree.addMouseListener( new MouseAdapter( ) {

			public void mouseDown( MouseEvent event )
			{
				// only activate if there is a cell editor
				Point pt = new Point( event.x, event.y );
				TreeItem item = tableTree.getItem( pt );
				if ( item != null )
				{
					handleSelect( item );
				}
			}
		} );

		tableTree.addKeyListener( new KeyAdapter( ) {

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

		tableTree.addTreeListener( new TreeListener( ) {

			public void treeCollapsed( TreeEvent e )
			{
				if ( e.item instanceof TreeItem )
				{
					TreeItem item = (TreeItem) e.item;
					if ( viewer.getInput( ) != null
							&& viewer.getInput( ) instanceof GroupElementHandle )
					{
						GroupElementHandle handle = (GroupElementHandle) viewer.getInput( );
						Object obj = handle.getElements( ).get( 0 );
						if ( obj instanceof DesignElementHandle )
						{
							Memento element = (Memento) viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) obj ) );
							if ( element != null )
							{
								MementoElement[] path = createItemPath( item );
								PropertyMementoUtil.removeNode( element, path );
							}
						}
					}
					viewer.getTree( ).setSelection( item );
					saveSelection( item );
				}
			}

			public void treeExpanded( TreeEvent e )
			{
				if ( e.item instanceof TreeItem )
				{
					TreeItem item = (TreeItem) e.item;
					if ( viewer.getInput( ) != null
							&& viewer.getInput( ) instanceof GroupElementHandle )
					{
						GroupElementHandle handle = (GroupElementHandle) viewer.getInput( );
						Object obj = handle.getElements( ).get( 0 );
						if ( obj instanceof DesignElementHandle )
						{
							Memento element = (Memento) viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) obj ) );
							if ( element != null )
							{
								MementoElement[] path = createItemPath( item );
								PropertyMementoUtil.addNode( element, path );
							}
						}
					}
					viewer.getTree( ).setSelection( item );
					saveSelection( item );
				}

			}

		} );
	}

	protected MementoElement[] createItemPath( TreeItem item )
	{
		MementoElement tempMemento = null;
		while ( item.getParentItem( ) != null )
		{
			TreeItem parent = item.getParentItem( );
			for ( int i = 0; i < parent.getItemCount( ); i++ )
			{
				if ( parent.getItem( i ) == item )
				{
					MementoElement memento = new MementoElement( item.getText( ),
							new Integer( i ),
							MementoElement.Type_Element );
					if ( tempMemento != null )
						memento.addChild( tempMemento );
					tempMemento = memento;
					item = parent;
					break;
				}
			}
		}
		MementoElement memento = new MementoElement( item.getText( ),
				new Integer( 0 ),
				MementoElement.Type_Element );
		if ( tempMemento != null )
			memento.addChild( tempMemento );
		return PropertyMementoUtil.getNodePath( memento );
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
	protected void handleSelect( TreeItem selection )
	{
		// deactivate the current cell editor
		if ( cellEditor != null )
		{
			// applyValue( );
			deactivateCellEditor( );
		}

		// get the new selection
		TreeItem[] sel = new TreeItem[]{
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

		saveSelection( selection );
	}

	protected void saveSelection( TreeItem selection )
	{
		MementoElement[] selectPath = createItemPath( selection );
		if ( viewer.getInput( ) != null
				&& viewer.getInput( ) instanceof GroupElementHandle )
		{
			GroupElementHandle handle = (GroupElementHandle) viewer.getInput( );
			Object obj = handle.getElements( ).get( 0 );
			if ( obj instanceof DesignElementHandle )
			{
				Memento element = (Memento) viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) obj ) );
				if ( element != null )
				{
					element.getMementoElement( )
							.setAttribute( MementoElement.ATTRIBUTE_SELECTED,
									selectPath );
				}
			}
		}
	}

	/**
	 * 
	 */
	private void applyValue( )
	{
		if ( cellEditor == null || !cellEditor.isDirty( ) )
		{
			return;
		}

		if ( model instanceof GroupPropertyHandleWrapper )
		{
			try
			{
				( (GroupPropertyHandle) ((GroupPropertyHandleWrapper)model).getModel( ) ).setValue( cellEditor.getValue( ) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );

				// get the new selection
				TreeItem[] sel = viewer.getTree( ).getSelection( );
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
	private void activateCellEditor( TreeItem sel )
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
		if ( data instanceof GroupPropertyHandleWrapper
				&& ( (GroupPropertyHandle) ( ( (GroupPropertyHandleWrapper) data ) ).getModel( ) ).isVisible( ) )
		{
			editor = PropertyEditorFactory.getInstance( )
					.createPropertyEditor( tableTree,
							( (GroupPropertyHandleWrapper) data ).getModel( ) );

			if ( editor instanceof ExpressionDialogCellEditor )
			{
				Object arrays[] = list.toArray( );
				int len = arrays.length;
				if ( len > 0 )
				{
					( (ExpressionDialogCellEditor) editor ).setExpressionProvider( new ExpressionProvider( (DesignElementHandle) arrays[0] ) );
				}
				// for ( int i = 1; i < len; i++ )
				// {
				// dataSetList.retainAll( DEUtil.getDataSetList(
				// (DesignElementHandle) arrays[i] ) );
				// if ( dataSetList.size( ) == 0 )
				// {
				// break;
				// }
				// }
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

		TreeColumn column1 = new TreeColumn( tableTree, SWT.LEFT );
		column1.setText( COLUMN_TITLE_PROPERTY );
		TreeColumn column2 = new TreeColumn( tableTree, SWT.LEFT );
		column2.setText( COLUMN_TITLE_VALUE );

		// property column
		ColumnLayoutData c1Layout = new ColumnWeightData( 40, false );

		// value column
		ColumnLayoutData c2Layout = new ColumnWeightData( 60, true );

		// set columns in Table layout
		TableLayout layout = new TableLayout( );
		layout.addColumnData( c1Layout );
		layout.addColumnData( c2Layout );
		tableTree.setLayout( layout );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl( )
	{
		if ( container == null )
			return null;
		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus( )
	{
		getControl( ).setFocus( );

		if ( changed )
			refresh( );

	}

	protected void refresh( )
	{
		viewer.refresh( true );

		if ( viewer.getInput( ) != null
				&& viewer.getInput( ) instanceof GroupElementHandle )
		{
			GroupElementHandle handle = (GroupElementHandle) viewer.getInput( );
			Object obj = handle.getElements( ).get( 0 );
			if ( obj instanceof DesignElementHandle )
			{
				IMemento memento = viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) obj ) );
				if ( memento == null )
				{
					expandToDefaultLevel( );
					if ( viewer.getTree( ).getItemCount( ) > 0 )
					{
						Memento elementMemento = (Memento) viewerMemento.createChild( PropertyMementoUtil.getElementType( (DesignElementHandle) obj ),
								MementoElement.Type_Element );
						elementMemento.getMementoElement( )
								.setValue( new Integer( 0 ) );
					}
				}
				if ( memento != null && memento instanceof Memento )
				{
					expandToDefaultLevel( );
					expandTreeFromMemento( (Memento) memento );
				}
			}
		}

		changed = false;
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
		if ( this.selection != null && this.selection.equals( selection ) )
			return;
		this.selection = selection;
		deRegisterListeners( );

		list = getModelList( selection );

		GroupElementHandle handle = DEUtil.getMultiSelectionHandle( list );

		if ( handle != null && !handle.isSameType( ) )
		{
			viewer.setInput( null );
			setTitleDisplayName( null );
			return;
		}

		viewer.setInput( handle );

		setTitleDisplayName( handle );

		registerListeners( );

		Object element = handle.getElements( ).get( 0 );
		if ( element instanceof DesignElementHandle )
		{
			IMemento memento = viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) element ) );
			if ( memento == null )
			{
				expandToDefaultLevel( );
				if ( viewer.getTree( ).getItemCount( ) > 0 )
				{
					Memento elementMemento = (Memento) viewerMemento.createChild( PropertyMementoUtil.getElementType( (DesignElementHandle) element ),
							MementoElement.Type_Element );
					elementMemento.getMementoElement( )
							.setValue( new Integer( 0 ) );
				}
			}
			else if ( memento instanceof Memento )
			{
				expandToDefaultLevel( );
				expandTreeFromMemento( (Memento) memento );
			}
		}

	}

	private void expandTreeFromMemento( Memento memento )
	{
		if ( viewer.getTree( ).getItemCount( ) == 0 )
			return;
		TreeItem root = viewer.getTree( ).getItem( 0 );
		if ( memento.getMementoElement( ).getKey( ).equals( root.getText( ) ) )
		{
			restoreExpandedMemento( root, memento.getMementoElement( ) );
			Object obj = memento.getMementoElement( )
					.getAttribute( MementoElement.ATTRIBUTE_SELECTED );
			if ( obj != null )
				restoreSelectedMemento( root, (MementoElement[]) obj );
		}
	}

	private void restoreSelectedMemento( TreeItem root,
			MementoElement[] selectedPath )
	{
		if ( selectedPath.length <= 1 )
			return;
		for ( int i = 1; i < selectedPath.length; i++ )
		{
			MementoElement element = selectedPath[i];
			if ( !root.getExpanded( ) )
			{
				viewer.createChildren( root );
				root.setExpanded( true );
			}
			if ( root.getItemCount( ) > ( (Integer) element.getValue( ) ).intValue( ) )
			{
				root = root.getItem( ( (Integer) element.getValue( ) ).intValue( ) );
			}
			else
				return;
		}
		viewer.getTree( ).setSelection( root );

	}

	private void restoreExpandedMemento( TreeItem root, MementoElement memento )
	{
		if ( memento.getKey( ).equals( root.getText( ) ) )
		{
			if ( !root.getExpanded( ) )
				viewer.createChildren( root );
			if ( root.getItemCount( ) > 0 )
			{
				if ( !root.getExpanded( ) )
					root.setExpanded( true );
				MementoElement[] children = memento.getChildren( );
				for ( int i = 0; i < children.length; i++ )
				{
					MementoElement child = children[i];
					TreeItem item = root.getItem( ( (Integer) child.getValue( ) ).intValue( ) );
					restoreExpandedMemento( item, child );
				}
			}
		}
	}

	private void setTitleDisplayName( GroupElementHandle handle )
	{

		String displayName = null;

		if ( handle != null )
		{
			Object element = handle.getElements( ).get( 0 );

			if ( element instanceof DesignElementHandle )
			{
				displayName = PropertyMementoUtil.getElementType( (DesignElementHandle) element );
			}
		}

		if ( displayName == null || "".equals( displayName ) )//$NON-NLS-1$ 
		{
			displayName = Messages.getString( "ReportPropertySheetPage.Root.Default.Title" );
		}
		title.setTitle( displayName, null );
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

	private boolean changed = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Listener#elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( !viewer.getTree( ).isDisposed( ) )
		{
			// viewer.refresh( true );
			if ( getControl( ).isFocusControl( ) )
			{
				IMemento memento = viewerMemento.getChild( PropertyMementoUtil.getElementType( (DesignElementHandle) focus ) );
				if ( memento == null )
				{
					expandToDefaultLevel( );
					if ( viewer.getTree( ).getItemCount( ) > 0 )
					{
						Memento elementMemento = (Memento) viewerMemento.createChild( PropertyMementoUtil.getElementType( (DesignElementHandle) focus ),
								MementoElement.Type_Element );
						elementMemento.getMementoElement( )
								.setValue( new Integer( 0 ) );
					}
				}
				if ( memento != null && memento instanceof Memento )
				{
					expandToDefaultLevel( );
					expandTreeFromMemento( (Memento) memento );
				}
				changed = false;
			}
			else
			{
				changed = true;
				if ( changed == true )
					refresh( );
			}
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
			// Remove null from the list. That fix the bug 139422
			ArrayList selections = new ArrayList( );
			selections.add( null );
			selections.addAll( request.getSelectionModelList( ) );

			ArrayList nullList = new ArrayList( );
			nullList.add( null );
			selections.removeAll( nullList );
			// end

			handleSelectionChanged( new StructuredSelection( selections ) );
		}
	}

	class CustomTreeViewer extends TreeViewer
	{

		public CustomTreeViewer( Composite parent, int style )
		{
			super( parent, style );
		}

		public void createChildren( Widget widget )
		{
			super.createChildren( widget );
		}
	};
}