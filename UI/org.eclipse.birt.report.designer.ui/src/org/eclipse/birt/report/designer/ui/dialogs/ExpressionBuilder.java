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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DateFormatISO8601;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
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
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.expressions.ISortableExpressionProvider;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
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
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import com.ibm.icu.text.Collator;

/**
 * The expression builder
 */

public class ExpressionBuilder extends BaseTitleAreaDialog
{

	private static final String DIALOG_TITLE = Messages.getString( "ExpressionBuidler.Dialog.Title" ); //$NON-NLS-1$

	private static final String TITLE = Messages.getString( "ExpressionBuidler.Title" ); //$NON-NLS-1$

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

	private static final String TOOL_TIP_TEXT_CALENDAR = Messages.getString( "ExpressionBuilder.toolTipText.calendar" ); //$NON-NLS-1$

	private static final Object[] EMPTY = new Object[0];

	private static final String SORTING_PREFERENCE_KEY = "ExpressionBuilder.preference.enable.sorting"; //$NON-NLS-1$

	private TableViewer categoryTable, functionTable;
	private TreeViewer subCategoryTable;
	private IExpressionProvider provider;
	private SourceViewer sourceViewer;
	private JSSourceViewerConfiguration sourceViewerConfiguration = new JSSourceViewerConfiguration( );

	private IPreferenceStore preferenceStore;
	private Color backgroundColor;
	private Color foregroundColor;

	private FormText messageLine;

	protected String expression = null;
	protected String title;

	private boolean useSorting = false;
	private boolean showLeafOnlyInFunctionTable = false;
	private Object[] defaultSelection;

	private Map<ToolItem, Integer> toolItemType = new HashMap<ToolItem, Integer>( );

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
		this.preferenceStore = new ScopedPreferenceStore( new InstanceScope( ),
				"org.eclipse.ui.editors" ); //$NON-NLS-1$
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

	/**
	 * TableContentProvider
	 */
	private class TableContentProvider implements IStructuredContentProvider
	{

		private Viewer viewer;
		private boolean leafOnly;

		public TableContentProvider( Viewer viewer, boolean leafOnly )
		{
			this.viewer = viewer;
			this.leafOnly = leafOnly;
		}

		public Object[] getElements( Object inputElement )
		{
			if ( viewer == categoryTable )
			{
				return provider.getCategory( );
			}
			// does not show groups/measures in third column.

			if ( inputElement instanceof IAdaptable )
			{
				inputElement = DNDUtil.unwrapToModel( inputElement );
			}

			if ( inputElement instanceof PropertyHandle
					|| inputElement instanceof TabularMeasureGroupHandle
					|| inputElement instanceof DimensionHandle )
			{
				return EMPTY;
			}
			// ignore items that cannot be inserted as (part of) expression.
			else if ( inputElement instanceof DesignElementHandle
					&& getAdapter() != null
					&& getAdapter( ).resolveExtendedData( (DesignElementHandle) inputElement ) != null
					&& provider.getChildren(inputElement).length > 1
					&& provider.getChildren(inputElement)[0] instanceof ReportElementHandle
					&& !getAdapter().isExtendedDataItem( (ReportElementHandle) provider.getChildren(inputElement)[0] ))
			{
				return EMPTY;
			}
			else if (inputElement instanceof ReportElementHandle
					&& getAdapter( ) != null
					&& getAdapter( ).isExtendedDataItem( (ReportElementHandle) inputElement ))
			{
				return new Object[]{inputElement};
			}
			else if ( inputElement instanceof LevelHandle )
			{
				List<Object> childrenList = new ArrayList<Object>( );
				childrenList.add( inputElement );

				List<LevelAttributeHandle> attribs = new ArrayList<LevelAttributeHandle>( );

				for ( Iterator iterator = ( (LevelHandle) inputElement ).attributesIterator( ); iterator.hasNext( ); )
				{
					attribs.add( (LevelAttributeHandle) iterator.next( ) );
				}

				if ( useSorting )
				{
					// sort attribute list
					Collections.sort( attribs,
							new Comparator<LevelAttributeHandle>( ) {

								public int compare( LevelAttributeHandle o1,
										LevelAttributeHandle o2 )
								{
									return Collator.getInstance( )
											.compare( o1.getName( ),
													o2.getName( ) );
								}

							} );
				}

				childrenList.addAll( attribs );

				return childrenList.toArray( );
			}
			else if ( inputElement instanceof TabularMeasureHandle )
			{
				return new Object[]{
					inputElement
				};
			}

			if ( useSorting && provider instanceof ISortableExpressionProvider )
			{
				return ( (ISortableExpressionProvider) provider ).getSortedChildren( inputElement );
			}

			Object[] elements = provider.getChildren( inputElement );
			if ( leafOnly && !isLeaf( elements ) )
			{
				return new Object[0];
			}
			return elements;
		}

		private boolean isLeaf( Object[] elements )
		{
			for ( Object element : elements )
			{
				if ( provider.hasChildren( element ) )
				{
					return false;
				}
			}
			return true;
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
					messageLine.getParent( ).setVisible( true );
					String message = provider.getDisplayText( table.getSelection( )[0].getData( ) );
					message = message.replaceAll( "&", "&amp;" ); //$NON-NLS-1$//$NON-NLS-2$
					message = message.replaceAll( "<", "&lt;" ); //$NON-NLS-1$ //$NON-NLS-2$
					message = message.replaceAll( ">", "&gt;" ); //$NON-NLS-1$//$NON-NLS-2$
					messageLine.setText( "<form><p> <b>" //$NON-NLS-1$
							+ Messages.getString( "ExpressionBuilder.Label.Hint" ) //$NON-NLS-1$
							+ "</b>: " //$NON-NLS-1$
							+ message
							+ "</p></form>", true, false ); //$NON-NLS-1$
					messageLine.getParent( ).layout( );
				}
				else
				{
					messageLine.getParent( ).setVisible( false );
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
				insertSelection( selection );
				return;
			}
		}
	};

	private void insertSelection( IStructuredSelection selection )
	{
		if ( selection.getFirstElement( ) instanceof Object[] )
		{
			Object[] inputArray = (Object[]) selection.getFirstElement( );
			if ( inputArray.length == 2 && inputArray[1] instanceof ReportItemHandle )
			{
				ReportItemHandle handle = (ReportItemHandle) inputArray[1];
				handle.getModuleHandle( )
						.getCommandStack( )
						.startTrans( Messages.getString( "DataEditPart.stackMsg.edit" ) ); //$NON-NLS-1$
				ColumnBindingDialog dialog = new ColumnBindingDialog( handle,
						Messages.getString( "DataColumBindingDialog.title.EditDataBinding" ) ); //$NON-NLS-1$
				if ( dialog.open( ) == Dialog.OK )
				{
					handle.getModuleHandle( ).getCommandStack( ).commit( );
					functionTable.refresh( );
				}
				else
				{
					handle.getModuleHandle( ).getCommandStack( ).rollback( );
				}
				return;
			}
		}
		String insertText = provider.getInsertText( selection.getFirstElement( ) );
		if ( insertText != null )
		{
			insertText( insertText );
		}
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
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
		Composite container = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = layout.marginWidth = 4;
		layout.numColumns = 2;
		container.setLayout( layout );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		messageLine = new FormText( container, SWT.NONE );
		new FormToolkit( Display.getDefault( ) ) {

			class BorderPainter implements PaintListener
			{

				public void paintControl( PaintEvent event )
				{
					Composite composite = (Composite) event.widget;
					Control[] children = composite.getChildren( );
					for ( int i = 0; i < children.length; i++ )
					{
						Control c = children[i];
						if ( c.isVisible( ) && c instanceof FormText )
						{
							Rectangle b = c.getBounds( );
							GC gc = event.gc;
							gc.setForeground( c.getBackground( ) );
							gc.drawRectangle( b.x - 2,
									b.y - 2,
									b.width + 4,
									b.height + 4 );
							gc.setForeground( getColors( ).getBorderColor( ) );
							gc.drawRectangle( b.x - 2,
									b.y - 2,
									b.width + 4,
									b.height + 4 );
						}
					}
				}
			}

			private BorderPainter borderPainter;

			public void paintBordersFor( Composite parent )
			{
				if ( borderPainter == null )
					borderPainter = new BorderPainter( );
				parent.addPaintListener( borderPainter );
			}

		}.paintBordersFor( container );
		messageLine.setText( "<form><p></p></form>", true, false ); //$NON-NLS-1$
		container.setVisible( false );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalIndent = 6;
		messageLine.setLayoutData( gridData );
	}

	private void createToolbar( Composite parent )
	{
		final ToolBar toolBar = new ToolBar( parent, SWT.FLAT );
		toolBar.setLayoutData( new GridData( ) );
		
		toolBar.getAccessible().addAccessibleListener(new AccessibleAdapter() {

			public void getName( AccessibleEvent e )
			{
				if ( e.childID != ACC.CHILDID_SELF )
				{
					Accessible accessible = (Accessible) e.getSource( );
					ToolBar tb = (ToolBar) accessible.getControl( );
					ToolItem item = tb.getItem( e.childID );
					if ( item != null )
					{
						e.result = item.getToolTipText( );
					}
				}
			}
		} );

		ToolItem copy = createToolItem( toolBar, ITextOperationTarget.COPY );
		copy.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_COPY ) );
		copy.setToolTipText( TOOL_TIP_TEXT_COPY );

		ToolItem cut = createToolItem( toolBar, ITextOperationTarget.CUT );
		cut.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_CUT ) );
		cut.setToolTipText( TOOL_TIP_TEXT_CUT );

		ToolItem paste = createToolItem( toolBar, ITextOperationTarget.PASTE );
		paste.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_PASTE ) );
		paste.setToolTipText( TOOL_TIP_TEXT_PASTE );

		ToolItem delete = createToolItem( toolBar, ITextOperationTarget.DELETE );
		delete.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_DELETE ) );
		delete.setToolTipText( TOOL_TIP_TEXT_DELETE );

		ToolItem undo = createToolItem( toolBar, ITextOperationTarget.UNDO );
		undo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_UNDO ) );
		undo.setToolTipText( TOOL_TIP_TEXT_UNDO );

		ToolItem redo = createToolItem( toolBar, ITextOperationTarget.REDO );
		redo.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_REDO ) );
		redo.setToolTipText( TOOL_TIP_TEXT_REDO );

		ToolItem validate = new ToolItem( toolBar, SWT.NONE );
		validate.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_EXPRESSION_VALIDATE ) );
		validate.setToolTipText( TOOL_TIP_TEXT_VALIDATE );
		validate.addSelectionListener( new SelectionAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse
			 * .swt.events.SelectionEvent)
			 */
			public void widgetSelected( SelectionEvent e )
			{
				validateScript( );
			}
		} );

		final ToolItem calendar = new ToolItem( toolBar, SWT.NONE );
		calendar.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_TOOL_CALENDAR ) );
		calendar.setToolTipText( TOOL_TIP_TEXT_CALENDAR );
		calendar.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				generateDate( toolBar, calendar );
			}
		} );
	}

	private ToolItem createToolItem( ToolBar toolBar, final int operationType )
	{
		final ToolItem item = new ToolItem( toolBar, SWT.NONE );
		toolItemType.put( item, operationType );
		item.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sourceViewer.doOperation( operationType );
				updateToolItems( );
			}
		} );
		return item;
	}

	private void createExpressionField( Composite parent )
	{
		Composite expressionArea = new Composite( parent, SWT.NONE );
		expressionArea.setLayout( new GridLayout( ) );
		expressionArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		createToolbar( expressionArea );

		sourceViewer = createSourceViewer( expressionArea );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		sourceViewer.getControl( ).setLayoutData( gd );
		JSSourceViewerConfiguration.updateSourceFont( sourceViewer );
		sourceViewer.getTextWidget( ).addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				updateToolItems( );
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

		sourceViewer.getTextWidget( ).addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				resetOkButtonStatus( true );
			}
		} );
		
		sourceViewer.getTextWidget( ).addSelectionListener( new SelectionAdapter( ) {
			
			public void widgetSelected( SelectionEvent e )
			{
				updateToolItems( );
			}
		});
		
		updateToolItems( );
	}

	protected void updateToolItems( )
	{
		for ( ToolItem item : toolItemType.keySet( ) )
		{
			if ( !item.isDisposed( ) )
			{
				item.setEnabled( sourceViewer.canDoOperation( toolItemType.get( item ) ) );
			}
		}
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
		int width = lable.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		width = width > 70 ? width : 70;
		GridData gd = new GridData( );
		gd.widthHint = width;
		lable.setLayoutData( gd );
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

	private void initSorting( )
	{
		// read setting from preference
		useSorting = PreferenceFactory.getInstance( )
				.getPreferences( ReportPlugin.getDefault( ) )
				.getBoolean( SORTING_PREFERENCE_KEY );
	}

	private void toggleSorting( boolean sorted )
	{
		useSorting = sorted;

		// update preference
		PreferenceFactory.getInstance( )
				.getPreferences( ReportPlugin.getDefault( ) )
				.setValue( SORTING_PREFERENCE_KEY, useSorting );

		functionTable.refresh( );
	}

	private void createListArea( Composite parent )
	{
		Composite listArea = new Composite( parent, SWT.NONE );
		listArea.setLayout( new GridLayout( 3, true ) );
		listArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		Label categoryLabel = new Label( listArea, SWT.NONE );
		categoryLabel.setText( LABEL_CATEGORY );
		categoryLabel.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_MNEMONIC && e.doit )
				{
					e.detail = SWT.TRAVERSE_NONE;
					categoryTable.getControl( ).setFocus( );
				}
			}
		} );

		Label subCategoryLabel = new Label( listArea, SWT.NONE );
		subCategoryLabel.setText( LABEL_SUB_CATEGORY );
		subCategoryLabel.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_MNEMONIC && e.doit )
				{
					e.detail = SWT.TRAVERSE_NONE;
					subCategoryTable.getControl( ).setFocus( );
				}
			}
		} );

		Composite functionHeader = new Composite( listArea, SWT.NONE );
		functionHeader.setLayout( new GridLayout( 2, false ) );
		functionHeader.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label functionLabel = new Label( functionHeader, SWT.NONE );
		functionLabel.setText( LABEL_FUNCTIONS );
		functionLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		functionLabel.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_MNEMONIC && e.doit )
				{
					e.detail = SWT.TRAVERSE_NONE;
					functionTable.getControl( ).setFocus( );
				}
			}
		} );
		ToolBar toolBar = new ToolBar( functionHeader, SWT.FLAT );
		toolBar.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		final ToolItem sortBtn = new ToolItem( toolBar, SWT.CHECK );
		sortBtn.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ALPHABETIC_SORT ) );
		sortBtn.setToolTipText( Messages.getString( "ExpressionBuilder.tooltip.Sort" ) ); //$NON-NLS-1$
		sortBtn.addSelectionListener( new SelectionAdapter( ) {

			@Override
			public void widgetSelected( SelectionEvent e )
			{
				toggleSorting( sortBtn.getSelection( ) );
			}
		} );

		if ( provider instanceof ISortableExpressionProvider )
		{
			initSorting( );

			sortBtn.setSelection( useSorting );
		}
		else
		{
			sortBtn.setEnabled( false );
		}

		int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.SINGLE;
		categoryTable = new TableViewer( listArea, style );
		subCategoryTable = new TreeViewer( listArea, style );
		functionTable = new TableViewer( listArea, style );

		functionTable.getControl( ).addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
			}

			public void keyReleased( KeyEvent e )
			{
				if ( e.character == ' ' )
				{
					IStructuredSelection selection = (IStructuredSelection) functionTable.getSelection( );
					if ( !selection.isEmpty( ) )
					{
						insertSelection( selection );
					}
				}
			}

		} );

		initTable( categoryTable, false );
		initTree( subCategoryTable );
		initTable( functionTable, showLeafOnlyInFunctionTable);
	}

	private void handleDefaultSelection( )
	{
		if ( defaultSelection == null || defaultSelection.length == 0 )
		{
			return;
		}

		if ( defaultSelection.length > 0 && defaultSelection[0] != null )
		{
			categoryTable.setSelection( new StructuredSelection( defaultSelection[0] ),
					true );

			if ( defaultSelection.length > 1 && defaultSelection[1] != null )
			{
				subCategoryTable.setSelection( new StructuredSelection( defaultSelection[1] ),
						true );

				if ( defaultSelection.length > 2 && defaultSelection[2] != null )
				{
					functionTable.setSelection( new StructuredSelection( defaultSelection[2] ),
							true );
				}
			}
		}
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

	private void initTable( TableViewer tableViewer, boolean leafOnly )
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
						if ( column != null && !column.isDisposed( ) )
						{
							column.setWidth( table.getSize( ).x > 204 ? table.getSize( ).x - 4
									: 200 );
						}
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
		tableViewer.setContentProvider( new TableContentProvider( tableViewer, leafOnly ) );
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
		getShell( ).setText( title );
		setTitle( TITLE );
		setMessage( PROMRT_MESSAGE );

		categoryTable.setInput( "Dummy" ); //$NON-NLS-1$

		handleDefaultSelection( );

		//getShell( ).setDefaultButton( null );
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
	public void setExpressionProvider( IExpressionProvider provider )
	{
		this.provider = provider;
	}

	/**
	 * Sets the expression provider for the expression builder
	 * 
	 * @param provider
	 *            the expression provider
	 * 
	 * @deprecated use {@link #setExpressionProvider(IExpressionProvider)}
	 */
	public void setExpressionProvier( IExpressionProvider provider )
	{
		setExpressionProvider( provider );
	}

	/**
	 * Sets default seletion for the expression builder
	 * 
	 * @param selection
	 * 
	 * @since 2.3.1
	 */
	public void setDefaultSelection( Object... selection )
	{
		this.defaultSelection = selection;
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

		viewer.configure( sourceViewerConfiguration );

		updateStyledTextColors( viewer.getTextWidget( ) );

		JSEditorInput editorInput = new JSEditorInput( expression,
				getEncoding( ) );
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

	private void updateStyledTextColors( StyledText styledText )
	{
		if ( preferenceStore != null )
		{
			styledText.setForeground( getForegroundColor( preferenceStore ) );
			styledText.setBackground( getBackgroundColor( preferenceStore ) );
		}
	}

	private Color getForegroundColor( IPreferenceStore preferenceStore )
	{
		Color color = preferenceStore.getBoolean( AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND_SYSTEM_DEFAULT ) ? null
				: createColor( preferenceStore,
						AbstractTextEditor.PREFERENCE_COLOR_FOREGROUND,
						Display.getCurrent( ) );
		foregroundColor = color;
		return color;
	}

	private Color getBackgroundColor( IPreferenceStore preferenceStore )
	{
		Color color = preferenceStore.getBoolean( AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT ) ? null
				: createColor( preferenceStore,
						AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND,
						Display.getCurrent( ) );
		backgroundColor = color;
		return color;
	}

	/**
	 * Creates a color from the information stored in the given preference
	 * store. Returns <code>null</code> if there is no such information
	 * available.
	 */
	private Color createColor( IPreferenceStore store, String key,
			Display display )
	{
		RGB rgb = null;
		if ( store.contains( key ) )
		{
			if ( store.isDefault( key ) )
			{
				rgb = PreferenceConverter.getDefaultColor( store, key );
			}
			else
			{
				rgb = PreferenceConverter.getColor( store, key );
			}
			if ( rgb != null )
			{
				return new Color( display, rgb );
			}
		}
		return null;
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
		final Shell shell = new Shell( UIUtil.getDefaultShell( ), SWT.NO_FOCUS );
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
		okBtn.setText( Messages.getString( "ExpressionBuilder.Calendar.Button.OK" ) ); //$NON-NLS-1$
		gd = new GridData( );
		gd.widthHint = okBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x < 60 ? 60
				: okBtn.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		okBtn.setLayoutData( gd );
		okBtn.setFocus( );
		okBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				try
				{
					Calendar cal = Calendar.getInstance( );
					cal.set( colorDialog.getYear( ),
							colorDialog.getMonth( ),
							colorDialog.getDay( ) );
					insertText( DEUtil.addQuote( DateFormatISO8601.format( cal.getTime( ) ) ) );
					if ( !shell.isDisposed( ) )
						shell.close( );
					if ( sourceViewer != null
							&& !sourceViewer.getTextWidget( ).isDisposed( ) )
						sourceViewer.getTextWidget( ).setFocus( );
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

	private String getEncoding( )
	{
		String encoding = ""; //$NON-NLS-1$
		ModuleHandle module = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		if ( module != null )
		{
			encoding = module.getFileEncoding( );
		}
		else
		{
			encoding = UnicodeUtil.SIGNATURE_UTF_8;
		}

		return encoding;
	}

	@Override
	public boolean close( )
	{
		if ( foregroundColor != null )
		{
			foregroundColor.dispose( );
		}
		if ( backgroundColor != null )
		{
			backgroundColor.dispose( );
		}
		return super.close( );
	}

	private boolean isEditModel = false;

	public void setEditModal( boolean isEditModel )
	{
		this.isEditModel = isEditModel;
	}

	public boolean isEditModal( )
	{
		return isEditModel;
	}

	protected void resetOkButtonStatus( Boolean enabled )
	{
		Button okButton = getButton( OK );
		if ( okButton != null && okButton.isEnabled( ) != enabled )
			okButton.setEnabled( enabled );
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		if ( isEditModal( ) )
			resetOkButtonStatus( false );
	}
	
	protected IExtendedDataModelUIAdapter getAdapter()
	{
		return ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( );
	}

	public void setShowLeafOnlyInThirdColumn( boolean leafOnly )
	{
		this.showLeafOnlyInFunctionTable = leafOnly;
	}

}
