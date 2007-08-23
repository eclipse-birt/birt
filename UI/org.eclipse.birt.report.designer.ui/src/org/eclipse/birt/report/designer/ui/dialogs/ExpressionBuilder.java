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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.js.JSDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.js.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.dialogs.js.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.ISharedImages;

/**
 * The expression builder
 */

public class ExpressionBuilder extends TitleAreaDialog
{

	private static final String DIALOG_TITLE = Messages.getString( "ExpressionBuidler.Dialog.Title" ); //$NON-NLS-1$

	private static final String PROMRT_MESSAGE = Messages.getString( "ExpressionBuilder.Message.Prompt" ); //$NON-NLS-1$

	private static final String LABEL_FUNCTIONS = Messages.getString( "ExpressionBuilder.Label.Functions" ); //$NON-NLS-1$

	private static final String LABEL_SUB_CATEGORY = Messages.getString( "ExpressionBuilder.Label.SubCategory" ); //$NON-NLS-1$

	private static final String LABEL_CATEGORY = Messages.getString( "ExpressionBuilder.Label.Category" ); //$NON-NLS-1$

	private static final String LABEL_OPERATORS = Messages.getString( "ExpressionBuilder.Label.Operators" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_REDO = Messages.getString( "TextEditDialog.toolTipText.redo" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_UNDO = Messages.getString( "TextEditDialog.toolTipText.undo" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_DELETE = Messages.getString( "TextEditDialog.toolTipText.delete" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_PASTE = Messages.getString( "TextEditDialog.toolTipText.paste" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_CUT = Messages.getString( "TextEditDialog.toolTipText.cut" ); //$NON-NLS-1$

	private static final String TOOL_TIP_TEXT_COPY = Messages.getString( "TextEditDialog.toolTipText.copy" ); //$NON-NLS-1$

	// Defines item types

	/** The item type for provider */
	private static final int ITEM_IMAGE = 1;

	/** The display text type for provider */
	private static final int ITEM_DISPLAY_TEXT = 2;
	
	/** The insert text type for provider */
	private static final int ITEM_INSERT_TEXT = 3;
	
	/** The tooltip text type for provider */
	private static final int ITEM_TOOLTIP_TEXT = 4;

	/** The operators type for provider */
	private static final int ITEM_OPERATORS = 5;
	
	/** The children type for provider */
	private static final int ITEM_CHILDREN = 6;
	
	/** The category type for provider */
	private static final int ITEM_CATEGORY = 7;
	
	/** The has children type for provider */
	private static final int ITEM_HAS_CHILDREN = 8;

	/** the providers */
	private final Collection providers = new HashSet();
	
	private class TableContentProvider implements IStructuredContentProvider
	{

		private Viewer viewer;

		private final Object[] EMPTY = new Object[0];

		public TableContentProvider( Viewer viewer )
		{
			this.viewer = viewer;
		}

		public Object[] getElements( Object inputElement )
		{
			if ( viewer == categoryTable )
			{
				return ( (List) getProviderValue( ITEM_CATEGORY ) ).toArray( );
			}
			//does not show groups/measures in third column. 
			if ( inputElement instanceof PropertyHandle
					|| inputElement instanceof TabularMeasureGroupHandle
					|| inputElement instanceof TabularDimensionHandle )
			{
				return EMPTY;
			}
			else if ( inputElement instanceof LevelHandle )
			{
				List childrenList = new ArrayList( );
				childrenList.add( inputElement );
				for ( Iterator iterator = ( (LevelHandle) inputElement ).attributesIterator( ); iterator.hasNext( ); )
				{
					childrenList.add( iterator.next( ) );
				}
				return childrenList.toArray( );
			}
			else if ( inputElement instanceof TabularMeasureHandle )
			{
				List childrenList = new ArrayList( );
				childrenList.add( inputElement );
				return childrenList.toArray( );
			}
			return ( (List) getProviderValue( ITEM_CHILDREN, inputElement ) ).toArray( );
		}

		private List getChildren( Object inputElement )
		{
			List childrenList = new ArrayList( );
			Object[] children = ( (List) getProviderValue( ITEM_CHILDREN,
					inputElement ) ).toArray( );

			childrenList.addAll( Arrays.asList( children ) );
			for ( int i = 0; i < children.length; i++ )
			{
				childrenList.addAll( getChildren( children[i] ) );
			}
			return childrenList;
		}

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			if ( viewer == subCategoryTable )
			{
				functionTable.setInput( null );
			}
		}
	}

	private ISelectionChangedListener selectionListener = new ISelectionChangedListener( ) {

		public void selectionChanged( SelectionChangedEvent event )
		{
			IStructuredSelection selection = (IStructuredSelection) event.getSelection( );
			Viewer target = null;

			if ( event.getSource( ) == categoryTable )
			{
				target = subCategoryTable;
			}
			else if ( event.getSource( ) == subCategoryTable )
			{
				target = functionTable;
			}
			if ( target != null )
			{
				target.setInput( selection == null ? null
						: selection.getFirstElement( ) );
			}

			if ( event.getSource( ) == functionTable )
			{
				Table table = functionTable.getTable( );
				if ( table.getSelectionCount( ) == 1 )
				{
					String message = table.getSelection( )[0].getText( );
					message = message.replaceAll( "&", "&&" );  //$NON-NLS-1$//$NON-NLS-2$
					messageLine.setText( message );
				}
				else
				{
					messageLine.setText( "" );//$NON-NLS-1$	
				}
			}
		}

	};

	private ITableLabelProvider tableLabelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return (Image) getProviderValue( ITEM_IMAGE, element );
		}

		public String getColumnText( Object element, int columnIndex )
		{
			return (String) getProviderValue( ITEM_DISPLAY_TEXT, element );
		}

		public void addListener( ILabelProviderListener listener )
		{
		}

		public void dispose( )
		{
		}

		public boolean isLabelProperty( Object element, String property )
		{
			return true;
		}

		public void removeListener( ILabelProviderListener listener )
		{
		}
	};

	private IDoubleClickListener doubleClickListener = new IDoubleClickListener( ) {

		public void doubleClick( DoubleClickEvent event )
		{
			IStructuredSelection selection = (IStructuredSelection) event.getSelection( );
			if ( selection.isEmpty( ) )
			{
				return;
			}
			if ( event.getSource( ) == functionTable )
			{
				if ( selection.getFirstElement( ) instanceof Object[] )
				{
					Object[] inputArray = (Object[]) selection.getFirstElement( );
					if ( inputArray.length == 2
							&& inputArray[1] instanceof ReportItemHandle )
					{
						ColumnBindingDialog dialog = new ColumnBindingDialog( );
						dialog.setInput( (ReportItemHandle) inputArray[1] );
						if ( dialog.open( ) == Window.OK )
						{
							functionTable.refresh( );
						}
						return;
					}
				}

				String insertText = (String) getProviderValue( ITEM_INSERT_TEXT,
						selection.getFirstElement( ) );

				if ( insertText != null )
				{
					insertText( insertText );
				}
				return;
			}
		}
	};

	private Composite buttonBar;
	private TableViewer categoryTable, functionTable;
	private TreeViewer subCategoryTable;
	private SourceViewer sourceViewer;
	private String expression = null;
	private Label messageLine;
	private String title;

	/**
	 * Create an expression builder under the given parent shell with the given
	 * initial expression
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param initExpression
	 *            the initial expression
	 */
	public ExpressionBuilder( Shell parentShell, String initExpression )
	{
		super( parentShell );
		title = DIALOG_TITLE;
		this.expression = UIUtil.convertToGUIString( initExpression );
	}

	protected void setShellStyle( int newShellStyle )
	{
		newShellStyle |= SWT.MAX | SWT.RESIZE;
		super.setShellStyle( newShellStyle );
	}

	/**
	 * Create an expression builder under the default parent shell with the
	 * given initial expression
	 * 
	 * @param initExpression
	 *            the initial expression
	 */
	public ExpressionBuilder( String initExpression )
	{
		this( UIUtil.getDefaultShell( ), initExpression );
	}

	/**
	 * Create an expression builder under the default parent shell without an
	 * initail expression
	 * 
	 */
	public ExpressionBuilder( )
	{
		this( null );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		createToolbar( composite );
		createExpressionField( composite );

		if ( providers.isEmpty( ) )
		{
			providers.add( new ExpressionProvider( ) );
		}

		createOperatorsBar( composite );
		createMessageLine( composite );
		createListArea( composite );

		UIUtil.bindHelp( parent, IHelpContextIds.EXPRESSION_BUILDER_ID );
		return composite;

	}

	private void createMessageLine( Composite parent )
	{
		messageLine = new Label( parent, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalIndent = 6;
		messageLine.setLayoutData( gridData );

	}

	private void createToolbar( Composite parent )
	{
		ToolBar toolBar = new ToolBar( parent, SWT.FLAT );
		toolBar.setLayoutData( new GridData( ) );

		ToolItem copy = new ToolItem( toolBar, SWT.NONE );
		copy.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_COPY ) );
		copy.setToolTipText( TOOL_TIP_TEXT_COPY );
		copy.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.COPY );
			}
		} );

		ToolItem cut = new ToolItem( toolBar, SWT.NONE );
		cut.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_CUT ) );
		cut.setToolTipText( TOOL_TIP_TEXT_CUT );
		cut.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.CUT );
			}
		} );

		ToolItem paste = new ToolItem( toolBar, SWT.NONE );
		paste.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_PASTE ) );
		paste.setToolTipText( TOOL_TIP_TEXT_PASTE );
		paste.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.PASTE );
			}
		} );

		ToolItem delete = new ToolItem( toolBar, SWT.NONE );
		delete.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_DELETE ) );
		delete.setToolTipText( TOOL_TIP_TEXT_DELETE );
		delete.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.DELETE );
			}
		} );

		ToolItem undo = new ToolItem( toolBar, SWT.NONE );
		undo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_UNDO ) );
		undo.setToolTipText( TOOL_TIP_TEXT_UNDO );
		undo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.UNDO );
			}
		} );

		ToolItem redo = new ToolItem( toolBar, SWT.NONE );
		redo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_REDO ) );
		redo.setToolTipText( TOOL_TIP_TEXT_REDO );
		redo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( ITextOperationTarget.REDO );
			}
		} );
	}

	private void createExpressionField( Composite parent )
	{
		Composite expressionArea = new Composite( parent, SWT.NONE );
		expressionArea.setLayout( new GridLayout( 2, false ) );
		expressionArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Composite composite = new Composite( expressionArea, SWT.BORDER
				| SWT.LEFT_TO_RIGHT );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );

		CompositeRuler ruler = new CompositeRuler( );
		ruler.addDecorator( 0, new LineNumberRulerColumn( ) );
		sourceViewer = new SourceViewer( composite, ruler, SWT.H_SCROLL
				| SWT.V_SCROLL );
		// JSSyntaxContext context = new JSSyntaxContext( );
		// try
		// {
		// context.setVariable( IReportGraphicConstants.REPORT_KEY_WORD,
		// IReportDesign.class ); //$NON-NLS-1$
		// }
		// catch ( ClassNotFoundException e )
		// {
		// }
		// sourceViewer.configure( new JSSourceViewerConfiguration( context ) );
		sourceViewer.configure( new JSSourceViewerConfiguration( ) );
		// Document doc = new Document( expression );
		// sourceViewer.setDocument( doc );
		if ( expression != null )
		{
			JSEditorInput editorInput = new JSEditorInput( expression );
			JSDocumentProvider documentProvider = new JSDocumentProvider( );
			try
			{
				documentProvider.connect( editorInput );
			}
			catch ( CoreException e )
			{
				ExceptionHandler.handle( e );
			}

			IDocument document = documentProvider.getDocument( editorInput );
			sourceViewer.setDocument( document );
		}
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		sourceViewer.getControl( ).setLayoutData( gd );
		if ( Platform.getOS( ).equals( Platform.WS_WIN32 ) )
		{
			Font font = sourceViewer.getTextWidget( ).getFont( );
			FontData data = font.getFontData( )[0];
			Font newFont = FontManager.getFont( data.getName( ),
					data.getHeight( ) + 1,
					data.getStyle( ) );
			sourceViewer.getTextWidget( ).setFont( newFont );
		}
		sourceViewer.getTextWidget( ).addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( isUndoKeyPress( e ) )
				{
					sourceViewer.doOperation( ITextOperationTarget.UNDO );
				}
				else if ( isRedoKeyPress( e ) )
				{
					sourceViewer.doOperation( ITextOperationTarget.REDO );
				}
			}

			private boolean isUndoKeyPress( KeyEvent e )
			{
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'z' ) || ( e.keyCode == 'Z' ) );
			}

			private boolean isRedoKeyPress( KeyEvent e )
			{
				return ( ( e.stateMask & SWT.CONTROL ) > 0 )
						&& ( ( e.keyCode == 'y' ) || ( e.keyCode == 'Y' ) );
			}

		} );

		sourceViewer.getTextWidget( )
				.addBidiSegmentListener( new BidiSegmentListener( ) {

					public void lineGetSegments( BidiSegmentEvent event )
					{
						event.segments = UIUtil.getExpressionBidiSegments( event.lineText );
					}
				} );

		buttonBar = new Composite( expressionArea, SWT.NONE );
		buttonBar.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		buttonBar.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
	}

	private void createOperatorsBar( Composite parent )
	{
		Object[] operators = ((List) getProviderValue( ITEM_OPERATORS ) ).toArray( );

		if ( operators == null || operators.length == 0 )
		{
			return;
		}

		Composite operatorsBar = new Composite( parent, SWT.NONE );
		operatorsBar.setLayout( new GridLayout( 2, false ) );
		operatorsBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		Label lable = new Label( operatorsBar, SWT.NONE );
		lable.setText( LABEL_OPERATORS );
		lable.setLayoutData( new GridData( 70, SWT.DEFAULT ) );
		Composite operatorsArea = new Composite( operatorsBar, SWT.NONE );
		operatorsArea.setLayout( UIUtil.createGridLayoutWithoutMargin( operators.length,
				true ) );
		SelectionAdapter selectionAdapter = new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				Button button = (Button) e.getSource( );
				insertText( (String) button.getData( ) );
			}

		};
		for ( int i = 0; i < operators.length; i++ )
		{
			Button button = new Button( operatorsArea, SWT.PUSH );
			button.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			if ( operators[i] != IExpressionProvider.OPERATOR_SEPARATOR )
			{
				button.setData( ( (Operator) operators[i] ).insertString );
				String text = ( (Operator) operators[i] ).symbol;
				if ( text.indexOf( "&" ) != -1 ) //$NON-NLS-1$
				{
					text = text.replaceAll( "&", "&&" ); //$NON-NLS-1$ //$NON-NLS-2$
				}
				button.setText( text );
				// button.setToolTipText( operators[i].tooltip );
				button.addSelectionListener( selectionAdapter );
			}
			else
			{
				button.setVisible( false );
			}
		}
	}

	private void createListArea( Composite parent )
	{
		Composite listArea = new Composite( parent, SWT.NONE );
		listArea.setLayout( new GridLayout( 3, true ) );
		listArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		new Label( listArea, SWT.NONE ).setText( LABEL_CATEGORY );
		new Label( listArea, SWT.NONE ).setText( LABEL_SUB_CATEGORY );
		new Label( listArea, SWT.NONE ).setText( LABEL_FUNCTIONS );

		int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE;
		categoryTable = new TableViewer( listArea, style );
		subCategoryTable = new TreeViewer( listArea, style );
		functionTable = new TableViewer( listArea, style );

		initTable( categoryTable );
		initTree( subCategoryTable );
		initTable( functionTable );

	}

	private void initTree( TreeViewer treeViewer )
	{
		final Tree tree = treeViewer.getTree( );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		tree.setLayoutData( gd );
		tree.setToolTipText( null );

		treeViewer.setLabelProvider( tableLabelProvider );
		treeViewer.setContentProvider( new ITreeContentProvider( ) {

			public Object[] getChildren( Object parentElement )
			{
				return ( (List) getProviderValue( ITEM_CHILDREN, parentElement ) ).toArray( );
			}

			public Object getParent( Object element )
			{
				return null;
			}

			public boolean hasChildren( Object element )
			{
				return ( (Boolean) getProviderValue( ITEM_HAS_CHILDREN, element ) ).booleanValue( );
			}

			public Object[] getElements( Object inputElement )
			{
				return getChildren( inputElement );
			}

			public void dispose( )
			{
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}
		} );
		treeViewer.addSelectionChangedListener( selectionListener );
		treeViewer.addDoubleClickListener( doubleClickListener );
	}

	private void initTable( TableViewer tableViewer )
	{
		final Table table = tableViewer.getTable( );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		table.setLayoutData( gd );
		table.setToolTipText( null );

		new TableColumn( table, SWT.NONE ).setWidth( 200 );

		table.addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == table )
				{
					Point pt = new Point( event.x, event.y );
					TableItem item = table.getItem( pt );
					if ( item == null )
					{

						table.setToolTipText( null );
					}
					else
					{
						table.setToolTipText( (String) getProviderValue( ITEM_TOOLTIP_TEXT,
								item.getData( ) ) );
					}
				}
			}
		} );

		tableViewer.setLabelProvider( tableLabelProvider );
		tableViewer.setContentProvider( new TableContentProvider( tableViewer ) );
		tableViewer.addSelectionChangedListener( selectionListener );
		tableViewer.addDoubleClickListener( doubleClickListener );
	}

	protected Control createButtonBar( Composite parent )
	{
		Composite composite = (Composite) super.createButtonBar( buttonBar );
		// createButton(
		// composite,IDialogConstants.HELP_ID,IDialogConstants.HELP_LABEL,false
		// );
		GridLayout layout = (GridLayout) composite.getLayout( );
		layout.numColumns = 1;
		composite.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );
		return composite;
	}

	/**
	 * Sets the layout data of the button to a GridData with appropriate heights
	 * and widths.
	 * <p>
	 * The <code>BaseDialog</code> override the method in order to make Help
	 * button split with other buttons.
	 * 
	 * @param button
	 *            the button to be set layout data to
	 */
	protected void setButtonLayoutData( Button button )
	{
		GridData gridData;
		if ( button.getText( ).equals( IDialogConstants.HELP_LABEL ) )
		{
			gridData = new GridData( GridData.VERTICAL_ALIGN_END
					| GridData.HORIZONTAL_ALIGN_CENTER );
			gridData.grabExcessVerticalSpace = true;
		}
		else
		{
			gridData = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );

		}
		int widthHint = convertHorizontalDLUsToPixels( IDialogConstants.BUTTON_WIDTH );
		gridData.widthHint = Math.max( widthHint,
				button.computeSize( SWT.DEFAULT, SWT.DEFAULT, true ).x );
		button.setLayoutData( gridData );
	}

	protected Control createContents( Composite parent )
	{
		Control control = super.createContents( parent );
		setTitle( DIALOG_TITLE );
		setMessage( PROMRT_MESSAGE );
		getShell( ).setText( title );
		categoryTable.setInput( "Dummy" ); //$NON-NLS-1$
		getShell( ).setDefaultButton( null );
		sourceViewer.getTextWidget( ).setFocus( );
		return control;
	}

	protected void okPressed( )
	{
		expression = sourceViewer.getTextWidget( ).getText( ).trim( );
		super.okPressed( );
	}

	/**
	 * Returns the result of the expression builder.
	 * 
	 * @return the result
	 */
	public String getResult( )
	{
		return expression;
	}

	/**
	 * Sets the expression provider for the expression builder
	 * 
	 * @param provider
	 *            the expression provider
	 */
	public void setExpressionProvier( IExpressionProvider provider )
	{
		providers.clear( );
		providers.add( provider );
	}

	/**
	 * Sets the expression providers for the expression builder
	 * 
	 * @param providers
	 *            the expression providers
	 */
	public void setExpressionProviers( Collection providers )
	{
		this.providers.clear( );
		this.providers.addAll( providers );
	}

	/**
	 * Sets the dialog title of the expression builder
	 * 
	 * @param newTitle
	 *            the new dialog title
	 */
	public void setDialogTitle( String newTitle )
	{
		title = newTitle;
	}

	/**
	 * Returns the dialog title of the expression builder
	 */
	public String getDialogTitle( )
	{
		return title;
	}

	/**
	 * Insert a text string into the text area
	 * 
	 * @param text
	 */
	protected void insertText( String text )
	{
		StyledText textWidget = sourceViewer.getTextWidget( );
		if ( !textWidget.isEnabled( ) )
		{
			return;
		}
		int selectionStart = textWidget.getSelection( ).x;
		textWidget.insert( text );
		textWidget.setSelection( selectionStart + text.length( ) );
		textWidget.setFocus( );

		if ( text.endsWith( "()" ) ) //$NON-NLS-1$
		{
			textWidget.setCaretOffset( textWidget.getCaretOffset( ) - 1 ); // Move
		}
	}

	/**
	 * Returns the value with the specified item, default value if the results
	 * are over one.
	 * 
	 * @param item
	 *            the specified item
	 * @return the provider value
	 */
	private Object getProviderValue( int item )
	{
		return getProviderValue( item, null );
	}

	/**
	 * Returns the value with the specified item and element, default value if
	 * the results are over one.
	 * 
	 * @param item
	 *            the specified item
	 * @param element
	 *            the specified element
	 * @return the provider value
	 */
	private Object getProviderValue( int item, Object element )
	{
		Collection values = new HashSet( );
		Object defultValue = getDefaultValue( item );

		for ( Iterator iterator = providers.iterator( ); iterator.hasNext( ); )
		{
			Object object = iterator.next( );
			Object value = object instanceof IExpressionProvider ? getProviderValue( item,
					element,
					(IExpressionProvider) object )
					: null;

			values.add( value == null ? defultValue : value );
		}

		if ( values.size( ) == 1 )
		{
			return values.iterator( ).next( );
		}
		else if ( values.size( ) > 1 )
		{
			List results = null;

			for ( Iterator iterator = values.iterator( ); iterator.hasNext( ); )
			{
				Object value = (Object) iterator.next( );

				if ( value instanceof List )
				{
					if ( results == null )
					{
						results = new ArrayList( (List) value );
					}
					else
					{
						results.retainAll( (List) value );
					}
				}
				else
				{
					break;
				}
			}
			return results;
		}
		return defultValue;
	}

	/**
	 * Returns the value with the specified item, element and action handle,
	 * <code>null</code> if a item is not found.
	 * 
	 * @param item
	 *            the specified item
	 * @param element
	 *            the specified element
	 * @param provider
	 *            the specified expression provider
	 * @return the provider value
	 */
	private Object getProviderValue( int item, Object element,
			IExpressionProvider provider )
	{
		Object value = null;

		switch ( item )
		{
			case ITEM_IMAGE :
				value = provider.getImage( element );
				break;

			case ITEM_DISPLAY_TEXT :
				value = provider.getDisplayText( element );
				break;

			case ITEM_INSERT_TEXT :
				value = provider.getInsertText( element );
				break;

			case ITEM_TOOLTIP_TEXT :
				value = provider.getTooltipText( element );
				break;

			case ITEM_OPERATORS :
				value = Arrays.asList( provider.getOperators( ) );
				break;
				
			case ITEM_CHILDREN :
				value = Arrays.asList( provider.getChildren( element ) );
				break;

			case ITEM_CATEGORY :
				value = Arrays.asList( provider.getCategory( ) );
				break;

			case ITEM_HAS_CHILDREN :
				value = new Boolean( provider.hasChildren( element ) );
				break;

			default :
				value = null;
				break;
		}

		return value;
	}

	/**
	 * Returns the default value with the specified item.
	 * 
	 * @param item
	 *            the specified item
	 * @return the default value
	 */
	private Object getDefaultValue( int item )
	{
		Object value = null;

		switch ( item )
		{
			case ITEM_DISPLAY_TEXT :
			case ITEM_INSERT_TEXT :
			case ITEM_TOOLTIP_TEXT :
				value = "";
				break;

			case ITEM_OPERATORS :
			case ITEM_CHILDREN :
			case ITEM_CATEGORY :
				value = Collections.EMPTY_LIST;
				break;

			case ITEM_HAS_CHILDREN :
				value = new Boolean( false );
				break;

			case ITEM_IMAGE :
			default :
				break;
		}
		return value;
	}
}
