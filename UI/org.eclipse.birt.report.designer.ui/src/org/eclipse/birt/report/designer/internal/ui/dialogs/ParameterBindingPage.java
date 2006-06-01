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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionDialogCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * The Binding attribute page of DE element. Note: Binding Not support
 * multi-selection.
 */
public class ParameterBindingPage extends AttributePage
{

	private Label dataSetName;

	/**
	 * The Binding properties table.
	 */
	private Table table;

	/**
	 * The TableViewer of the table widget.
	 */
	private TableViewer tableViewer;

	/**
	 * The column list.
	 */
	private static final String[] columnNames = {
			Messages.getString( "BindingPage.TableColumn.Parameter" ), //$NON-NLS-1$
			Messages.getString( "BindingPage.TableColumn.DataType" ), //$NON-NLS-1$
			Messages.getString( "BindingPage.TableColumn.Value" ),}; //$NON-NLS-1$

	private static final String NONE = Messages.getString( "BindingPage.None" );//$NON-NLS-1$

	private transient boolean enableAutoCommit = true;

	private ExpressionDialogCellEditor expressionCellEditor;

	private static IChoiceSet DataTypes = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE );

	private static final String DEFAULT_VALUE_LABEL = Messages.getString( "label.defaultValue" ); //$NON-NLS-1$

	private static final String DATA_SET_LABEL = Messages.getString( "Element.ReportItem.dataSet" ); //$NON-NLS-1$

	/**
	 * @param parent
	 *            A widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            The style of widget to construct
	 */
	public ParameterBindingPage( Composite parent, int style )
	{
		super( parent, style );
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

		dataSetName = new Label( this, SWT.NONE );
		data = new FormData( );
		data.left = new FormAttachment( title, 0, SWT.RIGHT );
		data.top = new FormAttachment( title, 0, SWT.CENTER );
		data.right = new FormAttachment( 50 );
		dataSetName.setLayoutData( data );

		// create table and tableViewer
		table = new Table( this, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		for ( int i = 0; i < columnNames.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setText( columnNames[i] );
			column.setWidth( 200 );
		}

		// layout table
		data = new FormData( );
		data.top = new FormAttachment( title, 0, SWT.BOTTOM );
		data.left = new FormAttachment( title, 0, SWT.LEFT );
		data.right = new FormAttachment( 100 );
		data.bottom = new FormAttachment( 100 );
		table.setLayoutData( data );

		createTableViewer( );
	}

	/**
	 * Creates the TableViewer and set all kinds of processors.
	 */
	private void createTableViewer( )
	{
		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( columnNames );
		expressionCellEditor = new ExpressionDialogCellEditor( table );
		tableViewer.setCellEditors( new CellEditor[]{
				null, null, expressionCellEditor
		} );
		tableViewer.setContentProvider( new BindingContentProvider( ) );
		tableViewer.setLabelProvider( new BindingLabelProvider( ) );
		tableViewer.setCellModifier( new BindingCellModifier( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AttributePage#refreshValues(java.util.Set)
	 */
	protected void refreshValues( Set propertiesSet )
	{
		if ( input.size( ) != 1 )
		{
			return;
		}
		dataSetName.setText( ( (ReportItemHandle) input.get( 0 ) ).getDataSet( )
				.getName( ) );
		reconstructTable( );
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
	private void reconstructTable( )
	{
		ReportItemHandle reportItemHandle = (ReportItemHandle) input.get( 0 );
		tableViewer.refresh( );
		expressionCellEditor.setExpressionProvider( new ExpressionProvider( reportItemHandle.getContainer( ) ) );
	}

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
		if ( ev.getEventType( ) == NotificationEvent.PROPERTY_EVENT )
		{
			PropertyEvent event = (PropertyEvent) ev;
			String propertyName = event.getPropertyName( );
			if ( ReportItemHandle.PARAM_BINDINGS_PROP.equals( propertyName )
					|| ReportItemHandle.DATA_SET_PROP.equals( propertyName ) )
			{
				HashSet set = new HashSet( );
				set.add( propertyName );
				refreshValues( set );
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
					refreshValues( null );
				}
			}
		}
	}

	private class BindingLabelProvider extends LabelProvider implements
			ITableLabelProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
		 *      int)
		 */

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 *      int)
		 */
		public String getColumnText( Object element, int columnIndex )
		{
			String text = ""; //$NON-NLS-1$
			DataSetParameterHandle parameter = (DataSetParameterHandle) ( (Object[]) element )[0];
			ParamBindingHandle bindingParameter = (ParamBindingHandle) ( (Object[]) element )[1];
			switch ( columnIndex )
			{
				case 0 :
					if ( parameter.getName( ) != null )
					{
						text = parameter.getName( );
					}
					break;
				case 1 :
					if ( parameter.getDataType( ) != null )
					{
						text = ChoiceSetFactory.getDisplayNameFromChoiceSet( parameter.getDataType( ),
								DataTypes );
					}
					break;
				case 2 :
					if ( bindingParameter != null
							&& bindingParameter.getExpression( ) != null )
					{
						text = bindingParameter.getExpression( );
					}
					else if ( parameter.getDefaultValue( ) != null )
					{
						text = parameter.getDefaultValue( ) + " " //$NON-NLS-1$
								+ DEFAULT_VALUE_LABEL;
					}
					break;
			}
			return text;
		}
	}

	private class BindingCellModifier implements ICellModifier
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
		 *      java.lang.String)
		 */
		public boolean canModify( Object element, String property )
		{
			return property.equals( columnNames[2] );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
		 *      java.lang.String)
		 */
		public Object getValue( Object element, String property )
		{
			ParamBindingHandle bindingParameter = (ParamBindingHandle) ( (Object[]) element )[1];
			if ( bindingParameter != null )
			{
				return bindingParameter.getExpression( );
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
		 *      java.lang.String, java.lang.Object)
		 */
		public void modify( Object element, String property, Object value )
		{
			if ( value == null )
			{// The edit cancelled
				return;
			}
			if ( element instanceof Item )
			{
				element = ( (Item) element ).getData( );
			}
			ParamBindingHandle bindingParameter = (ParamBindingHandle) ( (Object[]) element )[1];
			startTrans( Messages.getString( "BindingPage.MessageDlg.SaveParamBinding" ) ); //$NON-NLS-1$			
			if ( ( (String) value ).length( ) == 0 )
			{
				if ( bindingParameter != null )
				{
					try
					{
						getPropertyHandle( ).removeItem( bindingParameter.getStructure( ) );
					}
					catch ( PropertyValueException e )
					{
						ExceptionHandler.handle( e );
						rollback( );
					}
				}
			}
			else
			{
				if ( bindingParameter == null )
				{
					DataSetParameterHandle parameter = (DataSetParameterHandle) ( (Object[]) element )[0];
					try
					{
						bindingParameter = createBindingHandle( parameter.getName( ) );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
						rollback( );
					}
				}
				bindingParameter.setExpression( (String) value );
			}
			commit( );
		}

	}

	private class BindingContentProvider implements IStructuredContentProvider
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements( Object inputElement )
		{
			if ( inputElement == null )
			{
				return new Object[0];
			}
			ReportItemHandle inputHandle = (ReportItemHandle) inputElement;
			DataSetHandle dataHandle = inputHandle.getDataSet( );
			if ( dataHandle == null )
			{
				return new Object[0];
			}
			List bindingParametersList = new ArrayList( );
			List bindingParametersNameList = new ArrayList( );
			List resultList = new ArrayList( );
			for ( Iterator iterator = inputHandle.paramBindingsIterator( ); iterator.hasNext( ); )
			{
				ParamBindingHandle handle = (ParamBindingHandle) iterator.next( );
				bindingParametersList.add( handle );
				bindingParametersNameList.add( handle.getParamName( ) );
			}
			for ( Iterator iterator = dataHandle.parametersIterator( ); iterator.hasNext( ); )
			{
				DataSetParameterHandle handle = (DataSetParameterHandle) iterator.next( );
				if ( handle.isInput( ) )
				{
					Object[] result = new Object[]{
							handle, null
					};
					int index = bindingParametersNameList.indexOf( handle.getName( ) );
					if ( index != -1 )
					{
						result[1] = bindingParametersList.get( index );
					}
					resultList.add( result );
				}
			}
			return resultList.toArray( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose( )
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
		 *      java.lang.Object, java.lang.Object)
		 */
		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
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
		if ( tableViewer != null )
		{
			table.setEnabled( enabled );
		}
	}

	public void setInput( List elements )
	{
		if ( elements.size( ) != 1 )
		{
			enableUI( false );
			return;
		}
		enableUI( true );
		tableViewer.setInput( elements.get( 0 ) );
		super.setInput( elements );
	}

	protected void registerListeners( )
	{
		super.registerListeners( );
		SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.addListener( this );
	}

	protected void deRegisterListeners( )
	{
		super.deRegisterListeners( );
		if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) != null )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.removeListener( this );
		}
	}
}