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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

/**
 * The Binding attribute page of DE element. Note: Binding Not support
 * multi-selection.
 */
public class BindingPage extends Composite implements Listener
{

	protected List input = new ArrayList( );
	/**
	 * The Binding properties table.
	 */
	// private Table table;
	/**
	 * The DataSet choose control.
	 */
	private CCombo datasetCombo;

	private Button bindingButton;
	/**
	 * The TableViewer of the table widget.
	 */

	private static final String NONE = Messages.getString( "BindingPage.None" );//$NON-NLS-1$

	private transient boolean enableAutoCommit = true;

	private static final String DATA_SET_LABEL = Messages.getString( "Element.ReportItem.dataSet" ); //$NON-NLS-1$
	private static final String REPORT_ITEM__LABEL = Messages.getString( "BindingPage.ReportItem.Label" ); //$NON-NLS-1$
	private static final String BUTTON_BINDING = Messages.getString( "parameterBinding.title" ); //$NON-NLS-1$
	private DataSetColumnBindingsFormPage columnBindingsFormPage;

	private ModuleHandle model;
	private Button datasetButton;
	private Button reportItemButton;
	private CCombo reportItemCombo;

	/**
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            The style of widget to construct
	 */
	public BindingPage( Composite parent, int style )
	{
		super( parent, style );
		buildUI( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage#buildUI()
	 */
	protected void buildUI( )
	{
		// sets the layout
		FormLayout layout = new FormLayout( );
		layout.marginHeight = WidgetUtil.SPACING;
		layout.marginWidth = WidgetUtil.SPACING;
		layout.spacing = WidgetUtil.SPACING;
		setLayout( layout );

		FormData data;

		datasetButton = new Button( this, SWT.RADIO );
		datasetButton.setText( DATA_SET_LABEL );
		datasetButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshBinding( );
				if ( datasetButton.getSelection( ) )
					saveBinding( );
			}

		} );

		datasetCombo = new CCombo( this, SWT.READ_ONLY | SWT.BORDER );
		datasetCombo.setBackground( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
		datasetCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				saveBinding( );
			}
		} );

		bindingButton = new Button( this, SWT.PUSH );
		bindingButton.setText( BUTTON_BINDING );
		bindingButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ParameterBindingDialog dialog = new ParameterBindingDialog( UIUtil.getDefaultShell( ),
						( (DesignElementHandle) input.get( 0 ) ) );
				startTrans( "" ); //$NON-NLS-1$
				if ( dialog.open( ) == Window.OK )
				{
					commit( );
				}
				else
				{
					rollback( );
				}
			}
		} );
		data = new FormData( );
		data.left = new FormAttachment( datasetCombo, 0, SWT.RIGHT );
		data.top = new FormAttachment( datasetButton, 0, SWT.CENTER );
		// data.right = new FormAttachment( 50 );
		bindingButton.setLayoutData( data );

		reportItemButton = new Button( this, SWT.RADIO );
		reportItemButton.setText( REPORT_ITEM__LABEL );
		data = new FormData( );
		data.top = new FormAttachment( datasetButton, 0, SWT.BOTTOM );
		data.left = new FormAttachment( datasetButton, 0, SWT.LEFT );
		reportItemButton.setLayoutData( data );
		reportItemButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshBinding( );
			}

		} );

		data = new FormData( );
		if ( UIUtil.getStringWidth( datasetButton.getText( ), datasetButton ) > UIUtil.getStringWidth( reportItemButton.getText( ),
				reportItemButton ) )
			data.left = new FormAttachment( datasetButton, 0, SWT.RIGHT );
		else
			data.left = new FormAttachment( reportItemButton, 0, SWT.RIGHT );
		data.top = new FormAttachment( datasetButton, 0, SWT.CENTER );
		data.right = new FormAttachment( 50 );
		datasetCombo.setLayoutData( data );

		reportItemCombo = new CCombo( this, SWT.READ_ONLY | SWT.BORDER );
		reportItemCombo.setBackground( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
		data = new FormData( );
		data.top = new FormAttachment( reportItemButton, 0, SWT.CENTER );
		data.left = new FormAttachment( datasetCombo, 0, SWT.LEFT );
		data.right = new FormAttachment( datasetCombo, 0, SWT.RIGHT );
		reportItemCombo.setLayoutData( data );
		reportItemCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				saveBinding( );
			}
		} );

		try
		{
			columnBindingsFormPage = new DataSetColumnBindingsFormPage( this,
					new DataSetColumnBindingsFormHandleProvider( ) );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		data = new FormData( );
		data.top = new FormAttachment( reportItemCombo, 0, SWT.BOTTOM );
		data.left = new FormAttachment( reportItemButton, 0, SWT.LEFT );
		data.right = new FormAttachment( 100 );
		data.bottom = new FormAttachment( 100 );
		columnBindingsFormPage.setLayoutData( data );
	}

	private void saveBinding( )
	{
		BindingInfo info = new BindingInfo( );
		if ( datasetButton.getSelection( ) )
		{
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_DATA );
			info.setBindingValue( datasetCombo.getText( ) );
		}
		else
		{
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF );
			info.setBindingValue( reportItemCombo.getText( ) );
		}
		try
		{
			save( info );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void refreshBinding( )
	{
		if ( datasetButton.getSelection( ) )
		{
			datasetButton.setSelection( true );
			datasetCombo.setEnabled( true );
			bindingButton.setEnabled( !datasetCombo.getText( ).equals( NONE ) );
			reportItemButton.setSelection( false );
			reportItemCombo.setEnabled( false );
			if ( datasetCombo.getSelectionIndex( ) == -1 )
			{
				datasetCombo.setItems( getAvailableDatasetItems( ) );
				datasetCombo.select( 0 );
			}
		}
		else
		{
			datasetButton.setSelection( false );
			datasetCombo.setEnabled( false );
			bindingButton.setEnabled( false );
			reportItemButton.setSelection( true );
			reportItemCombo.setEnabled( true );
			if ( reportItemCombo.getSelectionIndex( ) == -1 )
			{
				reportItemCombo.setItems( getReferences( ) );
				reportItemCombo.select( 0 );
			}
		}
	}

	public String[] getAvailableDatasetItems( )
	{
		String[] dataSets = ChoiceSetFactory.getDataSets( );
		String[] newList = new String[dataSets.length + 1];
		newList[0] = NONE;
		System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
		return newList;
	}

	public String[] getReferences( )
	{
		ReportItemHandle element = getReportItemHandle( );
		List referenceList = element.getAvailableDataBindingReferenceList( );
		String[] references = new String[referenceList.size( ) + 1];
		references[0] = NONE;
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			references[i + 1] = ( (ReportItemHandle) referenceList.get( i ) ).getName( );
		}
		return references;
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 */
	// private void createTableViewer( )
	// {
	// tableViewer = new TableViewer( table );
	// tableViewer.setUseHashlookup( true );
	// tableViewer.setColumnProperties( columnNames );
	// expressionCellEditor = new ExpressionDialogCellEditor( table );
	// tableViewer.setCellEditors( new CellEditor[]{
	// null, null, expressionCellEditor
	// } );
	// tableViewer.setContentProvider( new BindingContentProvider( ) );
	// tableViewer.setLabelProvider( new BindingLabelProvider( ) );
	// tableViewer.setCellModifier( new BindingCellModifier( ) );
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage#refreshValues(java.util.Set)
	 */
	/*
	 * protected void refreshValues( ) { // Binding Not support multi-selection.
	 * if ( input.size( ) != 1 ) { datasetCombo.setEnabled( false );
	 * datasetCombo.deselectAll( ); // table.removeAll( ); // table.setEnabled(
	 * false ); return; } datasetCombo.setEnabled( true ); // table.setEnabled(
	 * true );
	 * 
	 * String selectedDataSetName = datasetCombo.getText( ); String[] oldList =
	 * datasetCombo.getItems( ); String[] dataSets =
	 * ChoiceSetFactory.getDataSets( ); String[] newList = new
	 * String[dataSets.length + 1]; newList[0] = NONE; System.arraycopy(
	 * dataSets, 0, newList, 1, dataSets.length ); if ( !Arrays.asList( oldList
	 * ).equals( Arrays.asList( newList ) ) ) { datasetCombo.setItems( newList );
	 * datasetCombo.setText( selectedDataSetName ); } String dataSetName =
	 * getDataSetName( ); if ( !dataSetName.equals( selectedDataSetName ) ) {
	 * datasetCombo.deselectAll( ); datasetCombo.setText( dataSetName ); }
	 * bindingButton.setEnabled( !dataSetName.equals( NONE ) ); //
	 * reconstructTable( ); columnBindingsFormPage.setInput( input ); }
	 */

	private ReportItemHandle getReportItemHandle( )
	{
		return (ReportItemHandle) input.get( 0 );
	}

	/**
	 * reconstruct the content of the table to show the last parameters in
	 * DataSet.
	 */
	// private void reconstructTable( )
	// {
	// ReportItemHandle reportItemHandle = (ReportItemHandle) input.get( 0 );
	// tableViewer.refresh( );
	// expressionCellEditor.setDataSetList( DEUtil.getDataSetList(
	// reportItemHandle ) );
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( this.isDisposed( ) )
			return;
		if ( ev.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		{
			PropertyEvent event = (PropertyEvent) ev;
			String propertyName = event.getPropertyName( );
			if ( ReportItemHandle.PARAM_BINDINGS_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_SET_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_BINDING_REF_PROP.equals( propertyName ) )
			{
				load( );
			}
		}

		// report design 's oda data set change event.
		if ( ev.getEventType( ) == NotificationEvent.CONTENT_EVENT )
		{
			if ( ev instanceof ContentEvent )
			{
				ContentEvent ce = (ContentEvent) ev;
				if ( ce.getContent( ) instanceof DataSet )
				{
					load( );
				}
			}
		}
	}

	/**
	 * Gets the DE CommandStack instance
	 * 
	 * @return CommandStack instance
	 */
	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	private void startTrans( String name )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).startTrans( name );
		}
	}

	private void commit( )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).commit( );
		}
	}

	private void rollback( )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).rollback( );
		}
	}

	/**
	 * @return Returns the enableAutoCommit.
	 */
	public boolean isEnableAutoCommit( )
	{
		return enableAutoCommit;
	}

	/**
	 * @param enableAutoCommit
	 *            The enableAutoCommit to set.
	 */
	public void setEnableAutoCommit( boolean enableAutoCommit )
	{
		this.enableAutoCommit = enableAutoCommit;
	}

	public void setInput( List elements )
	{
		deRegisterListeners( );
		input = elements;
		load( );
		registerListeners( );
		columnBindingsFormPage.setInput( elements );
		this.model = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
	}

	protected void registerListeners( )
	{
		if ( input == null )
			return;
		for ( int i = 0; i < input.size( ); i++ )
		{
			Object obj = input.get( i );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle element = (DesignElementHandle) obj;
				element.addListener( this );
			}
		}
		SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.addListener( this );
	}

	protected void deRegisterListeners( )
	{
		if ( input == null )
			return;
		for ( int i = 0; i < input.size( ); i++ )
		{
			Object obj = input.get( i );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle element = (DesignElementHandle) obj;
				element.removeListener( this );
			}
		}
		if ( this.model != null )
		{
			this.model.removeListener( this );
		}
	}

	public void dispose( )
	{
		deRegisterListeners( );
		super.dispose( );
	}

	public static class BindingInfo
	{

		private int bindingType;
		private Object bindingValue;

		public BindingInfo( int type, Object value )
		{
			this.bindingType = type;
			this.bindingValue = value;
		}

		public BindingInfo( )
		{
		}

		public int getBindingType( )
		{
			return bindingType;
		}

		public Object getBindingValue( )
		{
			return bindingValue;
		}

		public void setBindingType( int bindingType )
		{
			this.bindingType = bindingType;
		}

		public void setBindingValue( Object bindingValue )
		{
			this.bindingValue = bindingValue;
		}
	}

	public void load( )
	{
		datasetButton.setEnabled( true );
		reportItemButton.setEnabled( true );
		BindingInfo info = (BindingInfo) loadValue( );
		if ( info != null )
		{
			refreshBindingInfo( info );
		}
	}

	private void refreshBindingInfo( BindingInfo info )
	{
		int type = info.getBindingType( );
		Object value = info.getBindingValue( );
		datasetCombo.setItems( getAvailableDatasetItems( ) );
		reportItemCombo.setItems( getReferences( ) );
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_NONE :
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				datasetButton.setSelection( true );
				datasetCombo.setEnabled( true );
				datasetCombo.setText( value.toString( ) );
				bindingButton.setEnabled( !value.toString( ).equals( NONE ) );
				reportItemButton.setSelection( false );
				reportItemCombo.setEnabled( false );
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				datasetButton.setSelection( false );
				datasetCombo.setEnabled( false );
				bindingButton.setEnabled( false );
				reportItemButton.setSelection( true );
				reportItemCombo.setEnabled( true );
				reportItemCombo.setText( value.toString( ) );
		}
	}

	public Object loadValue( )
	{
		ReportItemHandle element = getReportItemHandle( );
		int type = element.getDataBindingType( );
		Object value;
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				DataSetHandle dataset = element.getDataSet( );
				if ( dataset == null )
					value = NONE;
				else
					value = dataset.getQualifiedName( );
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				ReportItemHandle reference = element.getDataBindingReference( );
				if ( reference == null )
					value = NONE;
				else
					value = reference.getQualifiedName( );
				break;
			default :
				value = NONE;
		}
		BindingInfo info = new BindingInfo( type, value );
		return info;
	}

	public void save( Object saveValue ) throws SemanticException
	{
		if ( saveValue instanceof BindingInfo )
		{
			BindingInfo info = (BindingInfo) saveValue;
			int type = info.getBindingType( );
			String value = info.getBindingValue( ).toString( );
			switch ( type )
			{
				case ReportItemHandle.DATABINDING_TYPE_DATA :
					if ( value.equals( NONE ) )
					{
						value = null;
					}
					int ret = 0;
					if ( !NONE.equals( ( (BindingInfo) loadValue( ) ).getBindingValue( )
							.toString( ) )
							|| getReportItemHandle( ).getColumnBindings( )
									.iterator( )
									.hasNext( ) )
					{
						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
								MessageDialog.INFORMATION,
								new String[]{
										Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.No" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$

						ret = prefDialog.open( );
					}

					switch ( ret )
					{
						// Clear binding info
						case 0 :
							resetDataSetReference( value, true );
							break;
						// Doesn't clear binding info
						case 1 :
							resetDataSetReference( value, false );
							break;
						// Cancel.
						case 2 :
							load( );
					}
					break;
				case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
					if ( value.equals( NONE ) )
					{
						value = null;
					}
					int ret1 = 0;
					if ( !NONE.equals( ( (BindingInfo) loadValue( ) ).getBindingValue( )
							.toString( ) )
							|| getReportItemHandle( ).getColumnBindings( )
									.iterator( )
									.hasNext( ) )
					{
						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
								MessageDialog.INFORMATION,
								new String[]{
										Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$

						ret1 = prefDialog.open( );
					}

					switch ( ret1 )
					{
						// Clear binding info
						case 0 :
							resetReference( value, true );
							break;
						// Cancel.
						case 1 :
							load( );
					}
			}
		}
	}

	private void resetDataSetReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			DataSetHandle dataSet = null;
			if ( value != null )
			{
				dataSet = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findDataSet( value.toString( ) );
			}
			if ( getReportItemHandle( ).getDataBindingType( ) != ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{

				getReportItemHandle( ).setDataSet( dataSet );
				if ( clearHistory )
				{
					getReportItemHandle( ).getColumnBindings( ).clearValue( );
					getReportItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
							.clearValue( );
				}
				columnBindingsFormPage.generateAllBindingColumns( );
			}
			else
			{
				getReportItemHandle( ).setDataBindingReference( null );
				getReportItemHandle( ).setDataSet( dataSet );
			}
			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		load( );
	}

	private void resetReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			ReportItemHandle element = null;
			if ( value != null )
			{
				element = (ReportItemHandle) SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findElement( value.toString( ) );
			}
			getReportItemHandle( ).setDataBindingReference( element );
			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		load( );
	}

}