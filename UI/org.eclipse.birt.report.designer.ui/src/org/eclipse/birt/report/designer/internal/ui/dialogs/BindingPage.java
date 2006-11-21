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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.DataSetColumnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
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
import org.eclipse.swt.widgets.Label;
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
	private CCombo combo;

	private Button bindingButton;
	/**
	 * The TableViewer of the table widget.
	 */
	// private TableViewer tableViewer;
	/**
	 * The column list.
	 */
	private static final String[] columnNames = {
			Messages.getString( "BindingPage.TableColumn.Parameter" ), //$NON-NLS-1$
			Messages.getString( "BindingPage.TableColumn.DataType" ), //$NON-NLS-1$
			Messages.getString( "BindingPage.TableColumn.Value" ),}; //$NON-NLS-1$

	private static final String NONE = Messages.getString( "BindingPage.None" );//$NON-NLS-1$

	private transient boolean enableAutoCommit = true;

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( );

	private static final String DEFAULT_VALUE_LABEL = Messages.getString( "label.defaultValue" ); //$NON-NLS-1$

	private static final String DATA_SET_LABEL = Messages.getString( "Element.ReportItem.dataSet" ); //$NON-NLS-1$

	private static final String BUTTON_BINDING = Messages.getString( "parameterBinding.title" ); //$NON-NLS-1$
	private DataSetColumnBindingsFormPage columnBindingsFormPage;

	private ModuleHandle model;

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

		Label title = new Label( this, SWT.NONE );
		title.setText( DATA_SET_LABEL );

		combo = new CCombo( this, SWT.READ_ONLY | SWT.BORDER );
		combo.setBackground( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
		combo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				String value = combo.getText( );

				if ( value.equals( NONE ) )
				{
					value = null;
				}

				int ret = 0;

				// If current data set name is None and no column binding
				// existing, pop up dilog doesn't need.
				if ( !NONE.equals( getDataSetName( ) )
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
						combo.setText( getDataSetName( ) );
				}
			}

			private void resetDataSetReference( String value,
					boolean clearHistory )
			{
				try
				{
					startTrans( "" ); //$NON-NLS-1$
					DataSetHandle dataSet = null;
					if ( value != null )
					{
						dataSet = SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.findDataSet( value );
					}
					getReportItemHandle( ).setDataSet( dataSet );
					if ( clearHistory )
					{
						getReportItemHandle( ).getColumnBindings( )
								.clearValue( );
						getReportItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
								.clearValue( );
					}
					columnBindingsFormPage.generateAllBindingColumns( );
					commit( );
				}
				catch ( SemanticException e )
				{
					rollback( );
					ExceptionHandler.handle( e );
				}

			}
		} );
		data = new FormData( );
		data.left = new FormAttachment( title, 0, SWT.RIGHT );
		data.top = new FormAttachment( title, 0, SWT.CENTER );
		data.right = new FormAttachment( 50 );
		combo.setLayoutData( data );

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
		data.left = new FormAttachment( combo, 0, SWT.RIGHT );
		data.top = new FormAttachment( title, 0, SWT.CENTER );
		// data.right = new FormAttachment( 50 );
		bindingButton.setLayoutData( data );

		// create table and tableViewer
		// table = new Table( this, SWT.SINGLE
		// | SWT.BORDER
		// | SWT.H_SCROLL
		// | SWT.V_SCROLL
		// | SWT.FULL_SELECTION );
		// table.setLinesVisible( true );
		// table.setHeaderVisible( true );
		// for ( int i = 0; i < columnNames.length; i++ )
		// {
		// TableColumn column = new TableColumn( table, SWT.LEFT );
		// column.setText( columnNames[i] );
		// column.setWidth( 200 );
		// }
		//
		// // layout table
		// data = new FormData( );
		// data.top = new FormAttachment( combo, 0, SWT.BOTTOM );
		// data.left = new FormAttachment( title, 0, SWT.LEFT );
		// data.right = new FormAttachment( 100 );
		// data.bottom = new FormAttachment( 30 );
		// table.setLayoutData( data );
		//
		// createTableViewer( );
		try
		{
			columnBindingsFormPage = new DataSetColumnBindingsFormPage( this,
					new DataSetColumnBindingsFormHandleProvider( ) );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}
		data = new FormData( );
		data.top = new FormAttachment( combo, 0, SWT.BOTTOM );
		data.left = new FormAttachment( title, 0, SWT.LEFT );
		data.right = new FormAttachment( 100 );
		data.bottom = new FormAttachment( 100 );
		columnBindingsFormPage.setLayoutData( data );
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
	protected void refreshValues( )
	{
		// Binding Not support multi-selection.
		if ( input.size( ) != 1 )
		{
			combo.setEnabled( false );
			combo.deselectAll( );
			// table.removeAll( );
			// table.setEnabled( false );
			return;
		}
		combo.setEnabled( true );
		// table.setEnabled( true );

		String selectedDataSetName = combo.getText( );
		String[] oldList = combo.getItems( );
		String[] dataSets = ChoiceSetFactory.getDataSets( );
		String[] newList = new String[dataSets.length + 1];
		newList[0] = NONE;
		System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
		if ( !Arrays.asList( oldList ).equals( Arrays.asList( newList ) ) )
		{
			combo.setItems( newList );
			combo.setText( selectedDataSetName );
		}
		String dataSetName = getDataSetName( );
		if ( !dataSetName.equals( selectedDataSetName ) )
		{
			combo.deselectAll( );
			combo.setText( dataSetName );
		}
		bindingButton.setEnabled( !dataSetName.equals( NONE ) );
		// reconstructTable( );
		columnBindingsFormPage.setInput( input );
	}

	private ReportItemHandle getReportItemHandle( )
	{
		return (ReportItemHandle) input.get( 0 );
	}

	private String getDataSetName( )
	{
		if ( getReportItemHandle( ).getDataSet( ) == null )
		{
			return NONE;
		}
		String dataSetName = getReportItemHandle( ).getDataSet( )
				.getQualifiedName( );
		if ( StringUtil.isBlank( dataSetName ) )
		{
			dataSetName = NONE;
		}
		return dataSetName;
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
	/**
	 * Creates a new ParamBinding Handle.
	 * 
	 * @return ParamBinding Handle.
	 * @throws SemanticException
	 */
	private ParamBindingHandle createBindingHandle( String name )
			throws SemanticException
	{
		PropertyHandle propertyHandle = getPropertyHandle( );
		ParamBinding binding = StructureFactory.createParamBinding( );
		binding.setParamName( name );
		propertyHandle.addItem( binding );
		return (ParamBindingHandle) binding.getHandle( propertyHandle );
	}

	/**
	 * Gets the PropertyHandle of PARAM_BINDINGS_PROP property.
	 * 
	 * @return PropertyHandle
	 */
	private PropertyHandle getPropertyHandle( )
	{
		ReportItemHandle handle = (ReportItemHandle) input.get( 0 );
		return handle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if(this.isDisposed( ))return;
		if ( ev.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		{
			PropertyEvent event = (PropertyEvent) ev;
			String propertyName = event.getPropertyName( );
			if ( ReportItemHandle.PARAM_BINDINGS_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_SET_PROP.equals( propertyName ) )
			{
				refreshValues( );
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
					refreshValues( );
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

	private boolean canChangeDataSet( String newName )
	{
		String currentDataSetName = getDataSetName( );
		if ( NONE.equals( currentDataSetName ) )
		{
			return true;
		}
		else if ( !currentDataSetName.equals( newName ) )
		{
			return MessageDialog.openQuestion( null,
					Messages.getString( "dataBinding.title.changeDataSet" ), Messages.getString( "dataBinding.message.changeDataSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return false;
	}

	private void enableUI( boolean enabled )
	{
		// if ( tableViewer != null )
		// {
		// combo.setEnabled( enabled );
		// table.setEnabled( enabled );
		// }
	}

	public void setInput( List elements )
	{
		
		if ( elements.size( ) != 1 )
		{
			enableUI( false );
			return;
		}
		enableUI( true );
		deRegisterListeners( );
		input = elements;
		refreshValues( );
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
}