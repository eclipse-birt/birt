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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.DateFormatISO8601;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.script.JSDocumentProvider;
import org.eclipse.birt.report.designer.internal.ui.script.JSEditorInput;
import org.eclipse.birt.report.designer.internal.ui.script.JSSourceViewerConfiguration;
import org.eclipse.birt.report.designer.internal.ui.script.PreferenceNames;
import org.eclipse.birt.report.designer.internal.ui.script.ScriptValidator;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.source.AnnotationModel;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.IVerticalRulerColumn;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
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

	private static final String TOOL_TIP_TEXT_VALIDATE = Messages.getString( "ExpressionBuilder.toolTipText.validate" ); //$NON-NLS-1$

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
				return provider.getCategory( );
			}
			// does not show groups/measures in third column.
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
			return provider.getChildren( inputElement );
		}

		private List getChildren( Object inputElement )
		{
			List childrenList = new ArrayList( );
			Object[] children = provider.getChildren( inputElement );
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
					message = message.replaceAll( "&", "&&" ); //$NON-NLS-1$//$NON-NLS-2$
					messageLine.setText( message );
				}
				else
				{
					messageLine.setText( "" );//$NON-NLS-1$	
				}
			}
		}

	};

	private class ExpressionLabelProvider implements
			ITableLabelProvider,
			ILabelProvider
	{

		public Image getColumnImage( Object element, int columnIndex )
		{
			return provider.getImage( element );
		}

		public String getColumnText( Object element, int columnIndex )
		{
			return provider.getDisplayText( element );
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

		public Image getImage( Object element )
		{
			return provider.getImage( element );
		}

		public String getText( Object element )
		{
			return provider.getDisplayText( element );
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
						ReportItemHandle handle = (ReportItemHandle) inputArray[1];
						handle.getModuleHandle( )
								.getCommandStack( )
								.startTrans( Messages.getString( "DataEditPart.stackMsg.edit" ) ); //$NON-NLS-1$
						ColumnBindingDialog dialog = new ColumnBindingDialog( Messages.getString( "DataColumBindingDialog.title.EditDataBinding" ),
								handle instanceof ListingHandle );
						dialog.setInput( handle );
						if ( dialog.open( ) == Dialog.OK )
						{
							handle.getModuleHandle( )
									.getCommandStack( )
									.commit( );
							functionTable.refresh( );
						}
						else
						{
							handle.getModuleHandle( )
									.getCommandStack( )
									.rollback( );
						}
						return;
					}
				}
				String insertText = provider.getInsertText( selection.getFirstElement( ) );
				if ( insertText != null )
				{
					insertText( insertText );
				}
				return;
			}
		}
	};
	private TableViewer categoryTable, functionTable;
	private TreeViewer subCategoryTable;
	private IExpressionProvider provider;
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
		if ( provider == null )
		{
			provider = new ExpressionProvider( );
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
		final ToolBar toolBar = new ToolBar( parent, SWT.FLAT );
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

		ToolItem validate = new ToolItem( toolBar, SWT.NONE );
		validate.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_EXPRESSION_VALIDATE ) );
		validate.setToolTipText( TOOL_TIP_TEXT_VALIDATE );
		validate.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				validateScript( );
			}
		} );

		// final ToolItem calendar = new ToolItem( toolBar, SWT.NONE );
		// calendar.setText( "Calendar" );
		// calendar.setToolTipText( "Calendar" );
		// calendar.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetSelected( SelectionEvent e )
		// {
		// generateDate( toolBar, calendar );
		// }
		// } );
	}

	private void createExpressionField( Composite parent )
	{
		Composite expressionArea = new Composite( parent, SWT.NONE );
		expressionArea.setLayout( new GridLayout( 2, false ) );
		expressionArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		sourceViewer = createSourceViewer( expressionArea );

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

	}

	private void createOperatorsBar( Composite parent )
	{
		Operator[] operators = provider.getOperators( );
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
				button.setData( operators[i].insertString );
				String text = operators[i].symbol;
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

		treeViewer.setLabelProvider( new ExpressionLabelProvider( ) );
		treeViewer.setContentProvider( new ITreeContentProvider( ) {

			public Object[] getChildren( Object parentElement )
			{
				return provider.getChildren( parentElement );
			}

			public Object getParent( Object element )
			{
				return null;
			}

			public boolean hasChildren( Object element )
			{
				return provider.hasChildren( element );
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

		final TableColumn column = new TableColumn( table, SWT.NONE );
		column.setWidth( 200 );
		table.getShell( ).addControlListener( new ControlAdapter( ) {

			public void controlResized( ControlEvent e )
			{
				Display.getCurrent( ).asyncExec( new Runnable( ) {

					public void run( )
					{
						column.setWidth( table.getSize( ).x > 204 ? table.getSize( ).x - 4
								: 200 );
					}
				} );

			}

		} );

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
						table.setToolTipText( provider.getTooltipText( item.getData( ) ) );
					}
				}
			}
		} );

		tableViewer.setLabelProvider( new ExpressionLabelProvider( ) );
		tableViewer.setContentProvider( new TableContentProvider( tableViewer ) );
		tableViewer.addSelectionChangedListener( selectionListener );
		tableViewer.addDoubleClickListener( doubleClickListener );
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
		if ( !validateScript( ) )
		{
			MessageDialog dialog = new MessageDialog( getShell( ),
					Messages.getString( "ExpressionBuilder.Script.Warning" ), //$NON-NLS-1$
					null, // Accept the default window icon.
					Messages.getString( "ExpressionBuilder.Script.Confirm" ), //$NON-NLS-1$
					MessageDialog.WARNING,
					new String[]{
							IDialogConstants.OK_LABEL,
							IDialogConstants.CANCEL_LABEL
					},
					1 ); // Cancel is the default.
			if ( dialog.open( ) != 0 )
			{
				return;
			}
		}
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
		this.provider = provider;
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
	 * Creates the source viewer to be used by this editor.
	 * 
	 * @param parent
	 *            the parent control
	 * @return the source viewer
	 */
	protected SourceViewer createSourceViewer( Composite parent )
	{
		IVerticalRuler ruler = createVerticalRuler( );
		Composite composite = new Composite( parent, SWT.BORDER
				| SWT.LEFT_TO_RIGHT );

		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );

		int styles = SWT.V_SCROLL
				| SWT.H_SCROLL
				| SWT.MULTI
				| SWT.BORDER
				| SWT.FULL_SELECTION;

		SourceViewer viewer = new SourceViewer( composite, ruler, styles );

		viewer.configure( new JSSourceViewerConfiguration( ) );

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

		viewer.setDocument( documentProvider.getDocument( editorInput ),
				ruler == null ? null : ruler.getModel( ) );

		return viewer;
	}

	/**
	 * Creates a new line number ruler column that is appropriately initialized.
	 * 
	 * @param annotationModel
	 * 
	 * @return the created line number column
	 */
	private IVerticalRulerColumn createLineNumberRulerColumn( )
	{
		LineNumberRulerColumn column = new LineNumberRulerColumn( );

		column.setForeground( JSSourceViewerConfiguration.getColorByCategory( PreferenceNames.P_LINENUMBER_COLOR ) );
		return column;
	}

	/**
	 * Creates a new line number ruler column that is appropriately initialized.
	 * 
	 * @return the created line number column
	 */
	private CompositeRuler createCompositeRuler( )
	{
		CompositeRuler ruler = new CompositeRuler( );

		ruler.setModel( new AnnotationModel( ) );
		return ruler;
	}

	/**
	 * Creates the vertical ruler to be used by this editor.
	 * 
	 * @return the vertical ruler
	 */
	private IVerticalRuler createVerticalRuler( )
	{
		IVerticalRuler ruler = createCompositeRuler( );

		if ( ruler instanceof CompositeRuler )
		{
			CompositeRuler compositeRuler = (CompositeRuler) ruler;

			compositeRuler.addDecorator( 0, createLineNumberRulerColumn( ) );
		}
		return ruler;
	}

	/**
	 * Validates the current script.
	 * 
	 * @return <code>true</code> if no error was found, <code>false</code>
	 *         otherwise.
	 */
	protected boolean validateScript( )
	{
		if ( sourceViewer == null )
		{
			return false;
		}

		String errorMessage = null;

		try
		{
			new ScriptValidator( sourceViewer ).validate( true, true );
			setMessage( Messages.getString( "ExpressionBuilder.Script.NoError" ), IMessageProvider.INFORMATION ); //$NON-NLS-1$
			return true;
		}
		catch ( ParseException e )
		{
			int offset = e.getErrorOffset( );
			int row = sourceViewer.getTextWidget( ).getLineAtOffset( offset ) + 1;
			int column = offset
					- sourceViewer.getTextWidget( ).getOffsetAtLine( row - 1 )
					+ 1;

			errorMessage = Messages.getFormattedString( "ExpressionBuilder.Script.Error", new Object[]{Integer.toString( row ), Integer.toString( column ), e.getLocalizedMessage( )} ); //$NON-NLS-1$
			return false;
		}
		finally
		{
			setErrorMessage( errorMessage );
		}
	}

	private void generateDate( final ToolBar toolBar, final ToolItem calendar )
	{
		final Shell shell = new Shell( UIUtil.getDefaultShell( ),
				SWT.NO_FOCUS );
		shell.setText( "Calendar" );
		shell.addShellListener( new ShellAdapter( ) {

			public void shellDeactivated( ShellEvent e )
			{
				shell.close( );
			}

			public void shellIconified( ShellEvent e )
			{
				shell.close( );
			}

		} );
		Point point = toolBar.toDisplay( 0, 0 );
		point.y += toolBar.getBounds( ).height;

		for ( int i = 0; i < toolBar.getItemCount( ); i++ )
		{
			ToolItem item = toolBar.getItem( i );
			if ( item != calendar )
				point.x += item.getWidth( );
			else
				break;
		}
		shell.setLocation( point );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = layout.verticalSpacing = 0;
		layout.numColumns = 1;
		shell.setLayout( layout );
		final DateTime colorDialog = new DateTime( shell, SWT.CALENDAR
				| SWT.FLAT );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 1;
		colorDialog.setLayoutData( gd );

		new Label( shell, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite container = new Composite( shell, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = 5;
		layout.numColumns = 2;
		container.setLayout( layout );

		new Label( container, SWT.NONE ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Button okBtn = new Button( container, SWT.FLAT );
		okBtn.setText( "OK" );
		gd = new GridData( );
		gd.widthHint = okBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x < 60 ? 60
				: okBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		okBtn.setLayoutData( gd );
		okBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				try
				{
					Calendar cal = Calendar.getInstance( );
					cal.set( colorDialog.getYear( ),
							colorDialog.getMonth( ),
							colorDialog.getDay( ) );
					insertText( DateFormatISO8601.format( cal.getTime( ) ) );
					shell.close( );
				}
				catch ( BirtException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
			}

		} );

		shell.pack( );
		shell.open( );
	}
}
