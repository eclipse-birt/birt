/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.AlphabeticallyViewSorter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AdvancePropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoBuilder;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.widget.ExpressionDialogCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IPageLayout;

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
public class AdvancePropertyDescriptor extends PropertyDescriptor implements
		IFastConsumerProcessor
{

	private boolean isFormStyle;
	private static final String COLUMN_TITLE_PROPERTY = Messages.getString( "ReportPropertySheetPage.Column.Title.Property" ); //$NON-NLS-1$
	private static final String COLUMN_TITLE_VALUE = Messages.getString( "ReportPropertySheetPage.Column.Title.Value" ); //$NON-NLS-1$

	private CustomTreeViewer viewer;

	private CellEditor cellEditor;
	private Tree tableTree;
	private TreeEditor tableTreeEditor;

	private int columnToEdit = 1;
	private ICellEditorListener editorListener;
	private Object model;
	private Composite container;
	private IMemento propertySheetMemento;
	private IMemento viewerMemento;
	protected String propertyViewerID = "Report_Property_Sheet_Page_Viewer_ID"; //$NON-NLS-1$

	public AdvancePropertyDescriptor( boolean formStyle )
	{
		this.isFormStyle = formStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl( Composite parent )
	{
		container = new Composite( parent, SWT.NONE );
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin( 2, false );
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		layout.horizontalSpacing = 10;
		container.setLayout( layout );
		container.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		viewer = new CustomTreeViewer( container, SWT.FULL_SELECTION );
		// viewer.addSelectionChangedListener( new ISelectionChangedListener(){
		//
		// public void selectionChanged( SelectionChangedEvent event )
		// {
		// if ( cellEditor != null )
		// deactivateCellEditor( );
		// }
		//			
		// } );
		tableTree = viewer.getTree( );
		tableTree.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		tableTree.setHeaderVisible( true );
		tableTree.setLinesVisible( true );

		viewer.setContentProvider( provider.getContentProvier( ) );

		TreeViewerColumn tvc1 = new TreeViewerColumn( viewer, SWT.NONE );
		tvc1.getColumn( ).setText( COLUMN_TITLE_PROPERTY ); //$NON-NLS-1$
		tvc1.getColumn( ).setWidth( 300 );
		tvc1.setLabelProvider( new DelegatingStyledCellLabelProvider( provider.getNameLabelProvier( ) ) );

		TreeViewerColumn tvc2 = new TreeViewerColumn( viewer, SWT.NONE );
		tvc2.getColumn( ).setText( COLUMN_TITLE_VALUE ); //$NON-NLS-1$
		tvc2.getColumn( ).setWidth( 400 );
		tvc2.setLabelProvider( new DelegatingStyledCellLabelProvider( provider.getValueLabelProvier( ) ) );

		AlphabeticallyViewSorter sorter = new AlphabeticallyViewSorter( );
		sorter.setAscending( true );
		viewer.setSorter( sorter );

		hookControl( );

		// create a new table tree editor
		tableTreeEditor = new TreeEditor( tableTree );

		// create the editor listener
		createEditorListener( );

		if ( isFormStyle )
		{
			FormWidgetFactory.getInstance( ).paintFormStyle( parent );
			FormWidgetFactory.getInstance( ).adapt( parent );
		}

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

		return container;
	}

	public void setInput( Object input )
	{

		this.input = input;
		getDescriptorProvider( ).setInput( input );
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
					viewer.setInput( input );
				}
			}
		} );

		tableTree.addTreeListener( new TreeListener( ) {

			public void treeCollapsed( TreeEvent e )
			{
				if ( e.item instanceof TreeItem )
				{
					TreeItem item = (TreeItem) e.item;
					if ( input != null )
					{
						Object obj = DEUtil.getInputFirstElement( input );
						if ( obj instanceof DesignElementHandle )
						{
							Memento element = (Memento) viewerMemento.getChild( provider.getElementType( ) );
							if ( element != null )
							{
								MementoElement[] path = createItemPath( item );
								provider.removeNode( element, path );
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
					if ( input != null )
					{
						Object obj = DEUtil.getInputFirstElement( input );
						if ( obj instanceof DesignElementHandle )
						{
							Memento element = (Memento) viewerMemento.getChild( provider.getElementType( ) );
							if ( element != null )
							{
								MementoElement[] path = createItemPath( item );
								provider.addNode( element, path );
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
		return provider.getNodePath( memento );
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
		if ( input != null )
		{
			Object obj = DEUtil.getInputFirstElement( input );
			if ( obj instanceof DesignElementHandle )
			{
				Memento element = (Memento) viewerMemento.getChild( provider.getElementType( ) );
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
				( (GroupPropertyHandle) ( (GroupPropertyHandleWrapper) model ).getModel( ) ).setValue( cellEditor.getValue( ) );
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
				if ( DEUtil.getInputSize( input ) > 0 )
				{
					( (ExpressionDialogCellEditor) editor ).setExpressionProvider( new ExpressionProvider( (DesignElementHandle) DEUtil.getInputFirstElement( input ) ) );
				}
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
		deactivateCellEditor( );
		if ( input != null )
		{
			Object obj = DEUtil.getInputFirstElement( input );
			if ( obj instanceof DesignElementHandle )
			{
				execMemento( );
			}
		}

		changed = false;
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

	protected void deRegisterEventManager( )
	{
		if ( UIUtil.getModelEventManager( ) != null )
			UIUtil.getModelEventManager( ).removeModelEventProcessor( this );
	}

	/**
	 * Registers model change listener to DE elements.
	 */
	protected void registerEventManager( )
	{
		if ( UIUtil.getModelEventManager( ) != null )
			UIUtil.getModelEventManager( ).addModelEventProcessor( this );
	}

	static class CustomTreeViewer extends TreeViewer
	{

		public CustomTreeViewer( Composite parent, int style )
		{
			super( parent, style );
		}

		public void createChildren( Widget widget )
		{
			super.createChildren( widget );
		}
	}

	public void load( )
	{
		deRegisterEventManager( );
		if ( !provider.isEnable( ) )
		{
			viewer.setInput( null );
			return;
		}

		if ( input == null || viewer.getInput( ) == null )
		{
			viewer.setInput( input );
		}
		else if ( input.equals( viewer.getInput( ) ) )
		{
			viewer.refresh( );
		}
		else
			viewer.setInput( input );

		registerEventManager( );
		execMemento( );
	}

	private boolean execMemento = false;

	private void execMemento( )
	{
		if ( !execMemento )
		{
			execMemento = true;

			Display.getDefault( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					if ( !viewer.getTree( ).isDisposed( ) )
					{
						IMemento memento = viewerMemento.getChild( provider.getElementType( ) );
						if ( memento == null )
						{
							expandToDefaultLevel( );
							if ( viewer.getTree( ).getItemCount( ) > 0 )
							{
								Memento elementMemento = (Memento) viewerMemento.createChild( provider.getElementType( ),
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
					execMemento = false;
				}
			} );

		}

	}

	public void save( Object obj ) throws SemanticException
	{
		// TODO Auto-generated method stub

	};

	private AdvancePropertyDescriptorProvider provider;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor#setDescriptorProvider(org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider)
	 */
	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( getDescriptorProvider( ) instanceof AdvancePropertyDescriptorProvider )
		{
			this.provider = (AdvancePropertyDescriptorProvider) getDescriptorProvider( );
		}
	}

	public void setVisible( boolean isVisible )
	{
		getControl( ).setVisible( isVisible );

	}

	public void setHidden( boolean isHidden )
	{
		WidgetUtil.setExcludeGridData( getControl( ), isHidden );
	}

	public void addElementEvent( DesignElementHandle focus, NotificationEvent ev )
	{

	}

	public void clear( )
	{

	}

	public boolean isOverdued( )
	{
		return viewer.getTree( ) == null || viewer.getTree( ).isDisposed( );
	}

	private boolean changed = false;

	public void postElementEvent( )
	{
		if ( !viewer.getTree( ).isDisposed( ) )
		{
			// viewer.refresh( true );
			if ( getControl( ).isFocusControl( ) )
			{
				IMemento memento = viewerMemento.getChild( provider.getElementType( ) );
				if ( memento == null )
				{
					expandToDefaultLevel( );
					if ( viewer.getTree( ).getItemCount( ) > 0 )
					{
						Memento elementMemento = (Memento) viewerMemento.createChild( provider.getElementType( ),
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
			}
		}
		else
		{
			deRegisterEventManager( );
		}
	}

	public Object getAdapter( Class adapter )
	{
		return null;
	}
}
