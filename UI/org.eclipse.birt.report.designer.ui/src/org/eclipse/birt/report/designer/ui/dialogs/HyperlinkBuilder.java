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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * The builder for hyper link
 */

public class HyperlinkBuilder extends BaseDialog
{

	private static final String TITLE = Messages.getString( "HyperlinkBuilder.DialogTitle" );
	private static final String LABEL_SELECT_TYPE = Messages.getString( "HyperlinkBuilder.Label.SelectType" ); //$NON-NLS-1$
	private static final String LABEL_LOCATION = Messages.getString( "HyperlinkBuilder.Label.Location" ); //$NON-NLS-1$
	private static final String LABEL_TARGET = Messages.getString( "HyperlinkBuilder.Label.Target" ); //$NON-NLS-1$
	private static final String LABEL_BOOKMARK = Messages.getString( "HyperlinkBuilder.Label.Bookmark" ); //$NON-NLS-1$
	private static final String LABEL_REPORT = Messages.getString( "HyperlinkBuilder.Label.Report" ); //$NON-NLS-1$
	private static final String LABEL_REPORT_PARAMETER = Messages.getString( "HyperlinkBuilder.Label.Parameters" ); //$NON-NLS-1$
	private static final String LABEL_FORMAT = Messages.getString( "HyperlinkBuilder.Label.Format" ); //$NON-NLS-1$

	private static final String RADIO_NONE = Messages.getString( "HyperlinkBuilder.Radio.None" ); //$NON-NLS-1$
	private static final String RADIO_URI = Messages.getString( "HyperlinkBuilder.Radio.Uri" ); //$NON-NLS-1$
	private static final String RADIO_BOOKMARK = Messages.getString( "HyperlinkBuilder.Radio.Bookmark" ); //$NON-NLS-1$
	private static final String RADIO_DRILLTHROUGH = Messages.getString( "HyperlinkBuilder.Radio.DrillThrough" ); //$NON-NLS-1$

	private static final String COLUMN_PARAMETER = Messages.getString( "HyperlinkBuilder.Column.Parameters" ); //$NON-NLS-1$
	private static final String COLUMN_VALUE = Messages.getString( "HyperlinkBuilder.Column.Values" ); //$NON-NLS-1$

	private static final Image IMAGE_OPEN_FILE = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_OPEN_FILE );

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );

	private static final String ERROR_MSG_REPORT_REQUIRED = Messages.getString( "HyperlinkBuilder.ErrorMessage.ReportReqired" ); //$NON-NLS-1$
	private static final String ERROR_MSG_INVALID_REPORT = Messages.getString( "HyperlinkBuilder.ErrorMessage.InvalidReport" ); //$NON-NLS-1$

	private static final String TOOLTIP_BROWSE_FILE = "Browse for File"; //$NON-NLS-1$
	private static final String TOOLTIP_EXPRESSION = "Open Expression Builder"; //$NON-NLS-1$

	private static final String REQUIED_MARK = "*";

	private static final IChoiceSet CHOICESET_TARGET = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_TARGET_NAMES_TYPE );

	private static final IChoiceSet CHOICESET_FORMAT = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_ACTION_FORMAT_TYPE );

	private static final ParamBinding dummyParameterBinding = StructureFactory.createParamBinding( );

	private ActionHandle inputHandle;
	private List dataSetList;

	private Composite displayArea;

	private String selectedType;
	// Radios
	private Button noneRadio, uriRadio, bookmarkRadio, drillRadio;

	private Combo targetChooser, formatChooser;

	private Text locationEditor, bookmarkEditor;

	private CLabel messageLine;

	private TableViewer paramBindingTable;

	private ComboBoxCellEditor paramterChooser;

	private ArrayList bindingList = new ArrayList( );
	private ArrayList parameterList = new ArrayList( );

	private ReportDesignHandle reportHandle;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			ArrayList list = ( (ArrayList) inputElement );
			ArrayList elementsList = (ArrayList) list.clone( );
			if ( bindingList.size( ) != parameterList.size( ) )
			{
				// To check if all parameters have been bound.
				elementsList.add( dummyParameterBinding );
			}
			return elementsList.toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			String text = null;
			ParamBinding parameterBinding = ( (ParamBinding) element );
			if ( parameterBinding != dummyParameterBinding )
			{
				if ( columnIndex == 0 )
				{
					text = parameterBinding.getParamName( );

				}
				else
				{
					text = parameterBinding.getExpression( );
				}
			}
			if ( text == null )
			{
				text = ""; //$NON-NLS-1$
			}
			return text;
		}

		public void addListener( ILabelProviderListener listener )
		{
		}

		public void dispose( )
		{
		}

		public boolean isLabelProperty( Object element, String property )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
		}

	};

	private ICellModifier cellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( element == dummyParameterBinding
					&& COLUMN_VALUE.equals( property ) )
			{
				return false;
			}
			return true;
		}

		public Object getValue( Object element, String property )
		{
			ParamBinding paramBinding = ( (ParamBinding) element );
			Object value = null;
			if ( COLUMN_VALUE.equals( property ) )
			{
				value = paramBinding.getExpression( );
				if ( value == null )
				{
					value = ""; //$NON-NLS-1$
				}
			}
			else if ( COLUMN_PARAMETER.equals( property ) )
			{
				buildParameterChoices( paramBinding.getParamName( ) );
				int index = -1;
				for ( int i = 0; i < paramterChooser.getItems( ).length; i++ )
				{
					if ( paramterChooser.getItems( )[i].equals( paramBinding.getParamName( ) ) )
					{
						index = i;
						break;
					}
				}
				value = new Integer( index );
			}
			return value;
		}

		public void modify( Object element, String property, Object value )
		{
			if ( element instanceof Item )
			{
				element = ( (Item) element ).getData( );
			}
			ParamBinding paramBinding = ( (ParamBinding) element );
			if ( COLUMN_VALUE.equals( property ) )
			{
				paramBinding.setExpression( (String) value );
			}
			else if ( COLUMN_PARAMETER.equals( property ) )
			{
				int index = ( (Integer) value ).intValue( );
				if ( index != -1 )
				{
					if ( paramBinding == dummyParameterBinding )
					{
						paramBinding = StructureFactory.createParamBinding( );
						bindingList.add( paramBinding );
					}
					paramBinding.setParamName( paramterChooser.getItems( )[index] );
				}
			}
			paramBindingTable.refresh( );
		}
	};

	public HyperlinkBuilder( Shell parentShell )
	{
		super( parentShell, TITLE ); //$NON-NLS-1$

	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		createSelectionArea( composite );
		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		displayArea = new Composite( composite, SWT.NONE );
		displayArea.setLayoutData( new GridData( 450, 250 ) );
		displayArea.setLayout( new GridLayout( 3, false ) );
		new Label( composite, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		return composite;
	}

	private void createSelectionArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		composite.setLayout( new GridLayout( 2, false ) );

		new Label( composite, SWT.NONE ).setText( LABEL_SELECT_TYPE );

		noneRadio = new Button( composite, SWT.RADIO );
		noneRadio.setText( RADIO_NONE );
		addRadioListener( noneRadio,
				DesignChoiceConstants.ACTION_LINK_TYPE_NONE );

		UIUtil.createBlankLabel( composite );

		uriRadio = new Button( composite, SWT.RADIO );
		uriRadio.setText( RADIO_URI );
		addRadioListener( uriRadio,
				DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK );

		UIUtil.createBlankLabel( composite );

		bookmarkRadio = new Button( composite, SWT.RADIO );
		bookmarkRadio.setText( RADIO_BOOKMARK );
		addRadioListener( bookmarkRadio,
				DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK );

		UIUtil.createBlankLabel( composite );

		drillRadio = new Button( composite, SWT.RADIO );
		drillRadio.setText( RADIO_DRILLTHROUGH );
		addRadioListener( drillRadio,
				DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH );

	}

	private void switchTo( String type )
	{
		selectedType = type;
		clearArea( );
		closeReport( );

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( type ) )
		{
			switchToURI( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( type ) )
		{
			switchToBookmark( );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( type ) )
		{
			switchToDrillthrough( );
		}
		initDisplayArea( );
		displayArea.layout( );
	}

	private void switchToURI( )
	{
		new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK
				+ LABEL_LOCATION );
		locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		locationEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		Composite buttonArea = new Composite( displayArea, SWT.NONE );
		buttonArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		buttonArea.setLayoutData( new GridData( ) );
		createBrowerButton( buttonArea, locationEditor, true, false );
		createExpressionButton( buttonArea, locationEditor );
		createTargetBar( );
	}

	private void switchToBookmark( )
	{
		createBookmarkBar( true );
	}

	private void switchToDrillthrough( )
	{
		new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK + LABEL_REPORT );
		locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		locationEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				initParamterBindings( );
				updateButtons( );
			}
		} );
		createBrowerButton( displayArea, locationEditor, false, true );

		UIUtil.createBlankLabel( displayArea );
		messageLine = new CLabel( displayArea, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		messageLine.setLayoutData( gd );

		createBindingTable( );
		createBookmarkBar( false );
		createTargetBar( );
		createFormatBar( );
	}

	private void createBindingTable( )
	{
		Label label = new Label( displayArea, SWT.NONE );
		label.setText( LABEL_REPORT_PARAMETER );
		label.setLayoutData( new GridData( GridData.VERTICAL_ALIGN_BEGINNING ) );
		paramBindingTable = new TableViewer( displayArea, SWT.BORDER
				| SWT.SINGLE
				| SWT.FULL_SELECTION );
		Table table = paramBindingTable.getTable( );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 2;
		table.setLayoutData( gd );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		TableColumn parameterColumn = new TableColumn( table, SWT.LEFT );
		parameterColumn.setText( COLUMN_PARAMETER );
		parameterColumn.setResizable( true );
		parameterColumn.setWidth( 150 );

		TableColumn valueColumn = new TableColumn( table, SWT.LEFT );
		valueColumn.setText( COLUMN_VALUE );
		valueColumn.setResizable( true );
		valueColumn.setWidth( 180 );

		table.addKeyListener( new KeyAdapter( ) {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			public void keyReleased( KeyEvent e )
			{
				// If Delete pressed, delete the selected row
				if ( e.keyCode == SWT.DEL )
				{
					deleteRow( );
				}
			}

		} );

		paramBindingTable.setColumnProperties( new String[]{
				COLUMN_PARAMETER, COLUMN_VALUE
		} );

		paramterChooser = new ComboBoxCellEditor( table,
				new String[0],
				SWT.READ_ONLY );
		ExpressionCellEditor valueEditor = new ExpressionCellEditor( table );
		valueEditor.setDataSetList( dataSetList );
		paramBindingTable.setCellEditors( new CellEditor[]{
				paramterChooser, valueEditor
		} );
		paramBindingTable.setContentProvider( contentProvider );
		paramBindingTable.setLabelProvider( labelProvider );
		paramBindingTable.setCellModifier( cellModifier );
		paramBindingTable.setInput( bindingList );
	}

	private void clearArea( )
	{
		Control[] controls = displayArea.getChildren( );
		for ( int i = 0; i < controls.length; i++ )
		{
			controls[i].dispose( );
		}
	}

	private void createExpressionButton( Composite parent, final Text text )
	{
		Button button = new Button( parent, SWT.PUSH );
		button.setLayoutData( new GridData( ) );
		button.setText( "..." ); //$NON-NLS-1$
		button.setToolTipText( TOOLTIP_EXPRESSION );
		button.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder builder = new ExpressionBuilder( text.getText( ) );
				builder.setExpressionProvier( new ExpressionProvider( inputHandle.getElementHandle( )
						.getModuleHandle( ),
						dataSetList ) );
				if ( builder.open( ) == Dialog.OK )
				{
					text.setText( builder.getResult( ) );
					updateButtons( );
				}
			}

		} );

	}

	private void createBrowerButton( Composite parent, final Text text,
			final boolean needQuote, final boolean needFilter )
	{
		Button button = new Button( parent, SWT.PUSH );
		button.setLayoutData( new GridData( ) );
		button.setImage( IMAGE_OPEN_FILE );
		button.setToolTipText( TOOLTIP_BROWSE_FILE );
		button.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				FileDialog dialog = new FileDialog( UIUtil.getDefaultShell( ) );
				if ( needFilter )
				{
					dialog.setFilterExtensions( new String[]{
						"*.rptdesign" //$NON-NLS-1$
						} );
				}
				try
				{
					String filename = dialog.open( );

					if ( filename != null )
					{
						filename = Path.fromOSString( filename )
								.toPortableString( );
						if ( needQuote )
						{
							filename = "\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
						}
						text.setText( filename );
					}
					else
					{
						text.setText( "" ); //$NON-NLS-1$
					}
					updateButtons( );
				}
				catch ( Exception ex )
				{
					ExceptionHandler.handle( ex );
				}
			}

		} );

	}

	private void createTargetBar( )
	{
		new Label( displayArea, SWT.NONE ).setText( LABEL_TARGET );
		targetChooser = new Combo( displayArea, SWT.READ_ONLY | SWT.BORDER );
		targetChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		targetChooser.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( CHOICESET_TARGET ) );
		UIUtil.createBlankLabel( displayArea );
	}

	private void createBookmarkBar( boolean isRequired )
	{
		String label;
		if ( isRequired )
		{
			label = REQUIED_MARK + LABEL_BOOKMARK;
		}
		else
		{
			label = LABEL_BOOKMARK;
		}
		new Label( displayArea, SWT.NONE ).setText( label );
		bookmarkEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		bookmarkEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		bookmarkEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		createExpressionButton( displayArea, bookmarkEditor );
	}

	private void createFormatBar( )
	{
		new Label( displayArea, SWT.NONE ).setText( LABEL_FORMAT );
		formatChooser = new Combo( displayArea, SWT.READ_ONLY | SWT.BORDER );
		formatChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatChooser.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( CHOICESET_FORMAT ) );
		UIUtil.createBlankLabel( displayArea );
	}

	protected void okPressed( )
	{
		try
		{
			// Remove original settings
			inputHandle.setURI( null );
			inputHandle.setTargetBookmark( null );
			inputHandle.setTargetWindow( null );
			inputHandle.setReportName( null );
			inputHandle.setFormatType( null );
			inputHandle.getMember( Action.PARAM_BINDINGS_MEMBER )
					.setValue( null );

			if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
			{
				inputHandle.setURI( locationEditor.getText( ).trim( ) );
				inputHandle.setTargetWindow( ChoiceSetFactory.getValueFromChoiceSet( targetChooser.getText( ),
						CHOICESET_TARGET ) );
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
			{
				inputHandle.setTargetBookmark( bookmarkEditor.getText( ).trim( ) );
			}
			else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
			{
				inputHandle.setReportName( locationEditor.getText( ).trim( ) );
				for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
				{
					inputHandle.addParamBinding( (ParamBinding) iter.next( ) );
				}
				if ( !StringUtil.isBlank( bookmarkEditor.getText( ) ) )
				{
					inputHandle.setTargetBookmark( bookmarkEditor.getText( )
							.trim( ) );
				}
				inputHandle.setTargetWindow( ChoiceSetFactory.getValueFromChoiceSet( targetChooser.getText( ),
						CHOICESET_TARGET ) );
				inputHandle.setFormatType( ChoiceSetFactory.getValueFromChoiceSet( formatChooser.getText( ),
						CHOICESET_FORMAT ) );
			}
			inputHandle.setLinkType( selectedType );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
		setResult( inputHandle );
		super.okPressed( );
	}

	public boolean close( )
	{
		closeReport( );
		return super.close( );
	}

	public void setInput( ActionHandle input )
	{
		inputHandle = input;
	}

	protected boolean initDialog( )
	{
		dataSetList = DEUtil.getDataSetList( inputHandle.getElementHandle( ) );

		switchTo( inputHandle.getLinkType( ) );

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			uriRadio.setSelection( true );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			bookmarkRadio.setSelection( true );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			drillRadio.setSelection( true );
		}
		else
		{
			noneRadio.setSelection( true );
		}
		return super.initDialog( );
	}

	private void initDisplayArea( )
	{
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			if ( inputHandle.getURI( ) != null )
			{
				locationEditor.setText( inputHandle.getURI( ) );
			}
			if ( inputHandle.getTargetWindow( ) != null )
			{
				targetChooser.setText( ChoiceSetFactory.getDisplayNameFromChoiceSet( inputHandle.getTargetWindow( ),
						CHOICESET_TARGET ) );
			}
			else
			{
				targetChooser.select( 0 );
			}
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			if ( inputHandle.getTargetBookmark( ) != null )
			{
				bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			}
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			if ( inputHandle.getReportName( ) != null )
			{
				locationEditor.setText( inputHandle.getReportName( ) );
			}
			else
			{
				initParamterBindings( );
			}
			if ( inputHandle.getTargetBookmark( ) != null )
			{
				bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			}
			if ( inputHandle.getTargetWindow( ) != null )
			{
				targetChooser.setText( ChoiceSetFactory.getDisplayNameFromChoiceSet( inputHandle.getTargetWindow( ),
						CHOICESET_TARGET ) );
			}
			else
			{
				targetChooser.select( 0 );
			}
			if ( inputHandle.getFormatType( ) != null )
			{
				formatChooser.setText( ChoiceSetFactory.getDisplayNameFromChoiceSet( inputHandle.getFormatType( ),
						CHOICESET_FORMAT ) );
			}
			else
			{
				formatChooser.select( 0 );
			}
		}
		updateButtons( );
	}

	private void initParamterBindings( )
	{
		bindingList.clear( );
		parameterList.clear( );

		String errorMessage = null;
		String newFilename = locationEditor.getText( ).trim( );
		if ( newFilename.length( ) == 0 )
		{
			errorMessage = ERROR_MSG_REPORT_REQUIRED;
		}
		else
		{
			try
			{
				reportHandle = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.openDesign( newFilename );
				for ( Iterator iter = reportHandle.getAllParameters( )
						.iterator( ); iter.hasNext( ); )
				{
					Object obj = iter.next( );
					if ( obj instanceof ParameterHandle )
					{
						parameterList.add( obj );
					}
					else if ( obj instanceof ParameterGroupHandle )
					{
						parameterList.addAll( ( (ParameterGroupHandle) obj ).getParameters( )
								.getContents( ) );
					}
				}
				if ( newFilename.equals( inputHandle.getReportName( ) ) )
				{
					for ( Iterator iter = inputHandle.paramBindingsIterator( ); iter.hasNext( ); )
					{
						ParamBindingHandle handle = (ParamBindingHandle) iter.next( );
						bindingList.add( handle.getStructure( ) );
					}
				}
			}
			catch ( DesignFileException e )
			{
				errorMessage = ERROR_MSG_INVALID_REPORT;
			}
		}
		if ( errorMessage != null )
		{
			messageLine.setText( errorMessage );
			messageLine.setImage( ERROR_ICON );
		}
		else
		{
			messageLine.setText( "" ); //$NON-NLS-1$
			messageLine.setImage( null );
		}

		paramBindingTable.refresh( );
		paramBindingTable.getTable( ).setEnabled( !parameterList.isEmpty( ) );

		updateButtons( );
	}

	private void addRadioListener( Button radio, final String type )
	{
		radio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( selectedType != type )
				{
					switchTo( type );
				}
			}
		} );
	}

	private void updateButtons( )
	{
		boolean okEnable = true;
		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals( selectedType ) )
		{
			okEnable = !StringUtil.isBlank( locationEditor.getText( ) );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals( selectedType ) )
		{
			okEnable = !StringUtil.isBlank( bookmarkEditor.getText( ) );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals( selectedType ) )
		{
			okEnable = !StringUtil.isBlank( locationEditor.getText( ) )
					&& messageLine.getImage( ) == null;
		}
		getOkButton( ).setEnabled( okEnable );
	}

	private void buildParameterChoices( String selectedParameter )
	{
		ArrayList avaliavleList = new ArrayList( );
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ParameterHandle parameter = (ParameterHandle) iter.next( );
			avaliavleList.add( parameter.getQualifiedName( ) );
		}
		for ( Iterator iter = bindingList.iterator( ); iter.hasNext( ); )
		{
			ParamBinding paramBinding = (ParamBinding) iter.next( );
			if ( !paramBinding.getParamName( ).equals( selectedParameter ) )
			{
				avaliavleList.remove( paramBinding.getParamName( ) );
			}
		}
		paramterChooser.setItems( (String[]) avaliavleList.toArray( new String[0] ) );
	}

	private void deleteRow( )
	{
		ParamBinding paramBinding = getSelectedBinding( );
		if ( paramBinding != null )
		{
			bindingList.remove( paramBinding );
			paramBindingTable.refresh( );
		}
	}

	private ParamBinding getSelectedBinding( )
	{
		IStructuredSelection selection = (IStructuredSelection) paramBindingTable.getSelection( );
		if ( selection.size( ) == 1 )
		{
			return (ParamBinding) selection.getFirstElement( );
		}
		return null;
	}

	private void closeReport( )
	{
		if ( reportHandle != null )
		{
			reportHandle.close( );
			reportHandle = null;
		}
	}

	/**
	 * Serializes an action.
	 * 
	 * @param action
	 *            the action to serialize
	 * 
	 * @return The serialize result.
	 */
	public static String serialize( ActionHandle action )
	{
		return null;
	}

	/**
	 * Deserializes an action.
	 * 
	 * @param serialize
	 *            the serialize of action
	 * 
	 * @return the action deserialized.
	 */
	public static ActionHandle deseriaize( String serialize )
	{
		return null;
	}
}
