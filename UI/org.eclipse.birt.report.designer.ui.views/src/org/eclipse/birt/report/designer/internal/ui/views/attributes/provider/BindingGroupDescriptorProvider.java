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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSetBindingSelector;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ParameterBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BindingGroupSection;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;

public class BindingGroupDescriptorProvider extends AbstractDescriptorProvider
{

	public static final String NONE = Messages.getString( "BindingPage.None" ); //$NON-NLS-1$

	public static final BindingInfo NullDatasetChoice = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_NONE,
			NONE,
			true );

	public ILabelProvider getDataSetLabelProvider( )
	{
		return new LabelProvider( ) {

			public String getText( Object element )
			{
				BindingInfo info = (BindingInfo) element;
				String datasetName = info.getBindingValue( );
				if ( !info.isDataSet() )
				{
					if ( !NONE.equals( datasetName ) )
					{
						datasetName += Messages.getString("BindingGroupDescriptorProvider.Flag.DataModel"); //$NON-NLS-1$
					}
				}
				return datasetName;
			}
		};
	}

	public static class ContentProvider implements IStructuredContentProvider
	{

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof List )
			{
				return ( (List) inputElement ).toArray( );
			}
			else if ( inputElement instanceof Object[] )
			{
				return (Object[]) inputElement;
			}
			return new Object[0];
		}

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}
	}

	public IContentProvider getDataSetContentProvider( )
	{
		return new ContentProvider( );
	}

	public String getDisplayName( )
	{
		return null;
	}

	protected List getAvailableDataBindingReferenceList(
			ReportItemHandle element )
	{
		List bindingRef = new ArrayList( );
		bindingRef.addAll( element.getAvailableDataSetBindingReferenceList( ) );

		if ( ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( ) != null )
		{
			List temp = ( ExtendedDataModelUIAdapterHelper.getInstance( )
					.getAdapter( ).getAvailableBindingReferenceList( element ) );
			bindingRef.removeAll( temp );
			bindingRef.addAll( temp );
		}
		return bindingRef;
	}

	private Map<String, ReportItemHandle> referMap = new HashMap<String, ReportItemHandle>( );
	private String NullReportItemChoice = NONE;


	public Object load( )
	{
		referMap.clear( );
		ReportItemHandle element = getReportItemHandle( );
		boolean isDataSet = false;;
		int type = element.getDataBindingType( );
		if ( type == ReportItemHandle.DATABINDING_TYPE_NONE )
			type = DEUtil.getBindingHolder( element ).getDataBindingType( );
		List referenceList = getAvailableDataBindingReferenceList( element );
		references = new String[referenceList.size( ) + 1];
		references[0] = NONE;
		referMap.put( references[0], null );
		int j = 0;
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			ReportItemHandle item = ( (ReportItemHandle) referenceList.get( i ) );
			if ( item.getName( ) != null )
			{
				references[++j] = item.getQualifiedName( );
				referMap.put( references[j], item );
			}
		}
		int tmp = j + 1;
		Arrays.sort( references, 1, tmp );
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			ReportItemHandle item = ( (ReportItemHandle) referenceList.get( i ) );
			if ( item.getName( ) == null )
			{
				references[++j] = item.getElement( )
						.getDefn( )
						.getDisplayName( )
						+ " (ID " //$NON-NLS-1$
						+ item.getID( )
						+ ") - " //$NON-NLS-1$
						+ Messages.getString( "BindingPage.ReportItem.NoName" ); //$NON-NLS-1$
				referMap.put( references[j], item );
			}
		}
		Arrays.sort( references, tmp, referenceList.size( ) + 1 );

		String value;
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				DataSetHandle dataset = element.getDataSet( );
				if ( dataset == null )
				{
					value = NullDatasetChoice.bindingValue;
					isDataSet = true;
				}
				else
				{
					List datasets = element.getModuleHandle( ).getAllDataSets( );
					if ( datasets != null )
					{
						for ( int i = 0; i < datasets.size( ); i++ )
						{
							if ( datasets.get( i ) == dataset )
							{
								isDataSet = true;
								break;
							}
						}
					}
					value = dataset.getQualifiedName( );
				}
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				ReportItemHandle reference = element.getDataBindingReference( );
				if ( reference == null )
					value = NullReportItemChoice;
				else
					value = reference.getQualifiedName( );
				break;
			default :
			{
				value = NullDatasetChoice.bindingValue;
				isDataSet = true;
			}
		}
		BindingInfo info = new BindingInfo( type, value, isDataSet );
		return info;
	}

	public void save( Object saveValue ) throws SemanticException
	{
		if ( saveValue instanceof BindingInfo )
		{
			BindingInfo info = (BindingInfo) saveValue;
			int type = info.getBindingType( );
			BindingInfo oldValue = (BindingInfo) load( );
			switch ( type )
			{
				case ReportItemHandle.DATABINDING_TYPE_NONE :
				case ReportItemHandle.DATABINDING_TYPE_DATA :
					if ( info.equals( NullDatasetChoice ) )
					{
						info = null;
					}
					int ret = 0;
					if ( !NullDatasetChoice.equals( info ) )
						ret = 4;
					if ( ( !NullDatasetChoice.equals( oldValue ) || getReportItemHandle( ).getColumnBindings( )
							.iterator( )
							.hasNext( ) )
							&& !( info != null && info.equals( oldValue ) ) )
					{
						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
								MessageDialog.QUESTION,
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
							resetDataSetReference( info, true );
							break;
						// Doesn't clear binding info
						case 1 :
							resetDataSetReference( info, false );
							break;
						// Cancel.
						case 2 :
							section.load( );
							break;
						case 4 :
							updateDataSetReference( info );
							break;
					}
					break;
				case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
					String value = info.getBindingValue( ).toString( );
					if ( value.equals( NONE ) || value == null )
					{
						value = null;
					}
					else if ( referMap.get( value ).getName( ) == null )
					{
						MessageDialog dialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.haveNoName" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.haveNoName" ),//$NON-NLS-1$
								MessageDialog.QUESTION,
								new String[]{
									Messages.getString( "dataBinding.button.OK" )//$NON-NLS-1$
								},
								0 );

						dialog.open( );
						section.load( );
						return;
					}
					int ret1 = 0;
					if ( !NullReportItemChoice.equals( ( (BindingInfo) load( ) ).getBindingValue( )
							.toString( ) )
							|| getReportItemHandle( ).getColumnBindings( )
									.iterator( )
									.hasNext( ) )
					{

						MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
								Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
								null,
								Messages.getString( "dataBinding.message.changeReference" ),//$NON-NLS-1$
								MessageDialog.QUESTION,
								new String[]{
										Messages.getString( "AttributeView.dialg.Message.Yes" ),//$NON-NLS-1$
										Messages.getString( "AttributeView.dialg.Message.Cancel" )}, 0 );//$NON-NLS-1$
						ret1 = prefDialog.open( );
					}

					switch ( ret1 )
					{
					// Clear binding info
						case 0 :
							resetReference( value );
							break;
						// Cancel.
						case 1 :
							section.load( );
					}
			}
		}
	}
	private Object input;

	private String[] references;

	public String[] getReferences( )
	{
		return references;
	}

	public void setReferences( String[] references )
	{
		this.references = references;
	}

	public List<BindingInfo> getVisibleDataSetHandles( ModuleHandle handle )
	{
		ArrayList<BindingInfo> list = new ArrayList<BindingInfo>( );
		for ( Iterator iterator = handle.getVisibleDataSets( ).iterator( ); iterator.hasNext( ); )
		{
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next( );
			BindingInfo info = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA,
					dataSetHandle.getQualifiedName( ),
					true );
			list.add( info );
		}
		LinkedDataSetAdapter adapter = new LinkedDataSetAdapter( );
		for ( Iterator iterator = adapter.getVisibleLinkedDataSetsDataSetHandles( handle )
				.iterator( ); iterator.hasNext( ); )
		{
			DataSetHandle dataSetHandle = (DataSetHandle) iterator.next( );
			BindingInfo info = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA,
					dataSetHandle.getQualifiedName( ),
					false );
			list.add( info );
		}
		return list;
	}

	public BindingInfo[] getAvailableDatasetItems( )
	{
		BindingInfo[] dataSets = getVisibleDataSetHandles( SessionHandleAdapter.getInstance( )
				.getModule( ) ).toArray( new BindingInfo[0] );
		BindingInfo[] newList = new BindingInfo[dataSets.length + 1];
		newList[0] = NullDatasetChoice;
		System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
		return newList;
	}

	public void setInput( Object input )
	{
		this.input = input;
	}

	public Object getInput( )
	{
		return input;
	}

	public static class BindingInfo
	{

		private int bindingType;
		private String bindingValue;
		private boolean isDataSet;
		private boolean isReadOnly = false;

		public BindingInfo( int type, String value )
		{
			this.bindingType = type;
			this.bindingValue = value;
		}

		public BindingInfo( int type, String value, boolean isDataSet )
		{
			this.bindingType = type;
			this.bindingValue = value;
			this.isDataSet = isDataSet;
		}

		public void setDataSet( boolean isDataSet )
		{
			this.isDataSet = isDataSet;
		}

		public boolean isDataSet( )
		{
			return isDataSet;
		}

		public BindingInfo( )
		{
		}

		public int getBindingType( )
		{
			return bindingType;
		}

		public String getBindingValue( )
		{
			return bindingValue;
		}

		public void setBindingType( int bindingType )
		{
			this.bindingType = bindingType;
		}

		public void setBindingValue( String bindingValue )
		{
			this.bindingValue = bindingValue;
		}

		public boolean isReadOnly( )
		{
			return isReadOnly;
		}

		public void setReadOnly( boolean isReadOnly )
		{
			this.isReadOnly = isReadOnly;
		}

		public boolean equals( Object obj )
		{
			if ( !( obj instanceof BindingInfo ) )
			{
				return false;
			}
			else
			{
				BindingInfo info = (BindingInfo) obj;
				if ( ( this.bindingValue == null && info.bindingValue != null )
						|| ( this.bindingValue != null && !this.bindingValue.equals( info.bindingValue ) ) )
				{
					return false;
				}
				if ( this.bindingType != info.bindingType )
				{
					return false;
				}
				if ( this.isDataSet != info.isDataSet )
				{
					return false;
				}
				if ( this.isReadOnly != info.isReadOnly )
				{
					return false;
				}
				return true;
			}
		}

		public int hashCode( )
		{
			int code = 13;
			if ( this.bindingValue != null )
				code += this.bindingValue.hashCode( ) * 7;
			code += this.bindingType * 5;
			code += Boolean.valueOf( this.isDataSet( ) ).hashCode( ) * 3;
			code += Boolean.valueOf( this.isReadOnly( ) ).hashCode( ) * 11;
			return code;
		}
	}

	public boolean isEnable( )
	{
		if ( DEUtil.getInputSize( input ) != 1 )
		{
			return false;
		}
		return true;
	}

	private void resetDataSetReference( BindingInfo info, boolean clearHistory )
	{
		try
		{
			startTrans( Messages.getString( "DataColumBindingDialog.stackMsg.resetReference" ) ); //$NON-NLS-1$

			DataSetHandle dataSet = null;
			if ( info != null && info.isDataSet( ) )
			{
				dataSet = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findDataSet( info.getBindingValue( ) );
			}

			if ( getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				getReportItemHandle( ).setDataBindingReference( null );
			}

			boolean isExtendedDataModel = false;
			if ( dataSet == null && info != null )
			{
				getReportItemHandle( ).setDataSet( null );
				isExtendedDataModel = new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						info.getBindingValue( ) );
			}
			else
			{
				new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						null );
				getReportItemHandle( ).setDataSet( dataSet );
			}
			if ( clearHistory )
			{
				getReportItemHandle( ).getColumnBindings( ).clearValue( );
				getReportItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
			}

			if ( info != null )
			{
				DataSetBindingSelector selector = new DataSetBindingSelector( UIUtil.getDefaultShell( ),
						isExtendedDataModel ? Messages.getString( "BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel" )//$NON-NLS-1$
								: Messages.getString( "BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet" ) ); //$NON-NLS-1$
				selector.setDataSet( info.getBindingValue( ), info.isDataSet( ) );
				if ( selector.open( ) == Dialog.OK )
				{
					Object[] columns = (Object[]) ( (Object[]) selector.getResult( ) )[1];
					dataSetProvider.generateBindingColumns( columns );
				}
			}

			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionUtil.handle( e );
		}
		section.load( );
	}

	private void updateDataSetReference( BindingInfo info )
	{
		try
		{
			startTrans( "Update Reference" ); //$NON-NLS-1$
			DataSetHandle dataSet = null;
			if ( info != null && info.isDataSet( ) )
			{
				dataSet = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findDataSet( info.getBindingValue( ) );
			}
			if ( getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				getReportItemHandle( ).setDataBindingReference( null );
			}
			boolean isExtendedDataModel = false;
			if ( dataSet == null && info != null )
			{
				getReportItemHandle( ).setDataSet( null );
				isExtendedDataModel = new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						info.getBindingValue( ) );
			}
			else
			{
				new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						null );
				getReportItemHandle( ).setDataSet( dataSet );
			}

			if ( info != null )
			{
				DataSetBindingSelector selector = new DataSetBindingSelector( UIUtil.getDefaultShell( ),
						isExtendedDataModel ? Messages.getString( "BindingGroupDescriptorProvider.DataSetBindingSelector.Title.LinkModel" )//$NON-NLS-1$
								: Messages.getString( "BindingGroupDescriptorProvider.DataSetBindingSelector.Title.DataSet" ) ); //$NON-NLS-1$
				selector.setDataSet( info.getBindingValue( ), info.isDataSet( ) );
				Iterator bindings = getReportItemHandle( ).getColumnBindings( )
						.iterator( );
				List<String> columnNames = new ArrayList<String>( );
				while ( bindings.hasNext( ) )
				{
					columnNames.add( ( (ComputedColumnHandle) bindings.next( ) ).getName( ) );
				}
				if ( !columnNames.isEmpty( ) )
					selector.setColumns( columnNames.toArray( new String[0] ) );
				if ( selector.open( ) == Dialog.OK )
				{
					clearBinding( getReportItemHandle( ).getColumnBindings( ),
							(Object[]) ( (Object[]) selector.getResult( ) )[2] );
					Object[] columns = (Object[]) ( (Object[]) selector.getResult( ) )[1];
					dataSetProvider.generateBindingColumns( columns );
				}
			}

			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionUtil.handle( e );
		}
		section.load( );
	}

	private void clearBinding( PropertyHandle columnBindings, Object[] objects )
	{
		if ( objects != null && columnBindings.getItems( ) != null )
		{
			List list = Arrays.asList( objects );
			for ( int i = columnBindings.getItems( ).size( ) - 1; i >= 0; i-- )
			{
				ComputedColumnHandle handle = (ComputedColumnHandle) columnBindings.getAt( i );
				String name = handle.getName( );
				if ( list.contains( name ) )
				{
					try
					{
						columnBindings.removeItem( i );
					}
					catch ( PropertyValueException e )
					{
						ExceptionHandler.handle( e );
					}
				}
			}
		}

	}

	private void resetReference( Object value )
	{
		if ( value == null
				&& this.getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_DATA )
		{
			resetDataSetReference( null, true );
		}
		else
		{
			try
			{
				startTrans( Messages.getString( "DataColumBindingDialog.stackMsg.resetReference" ) ); //$NON-NLS-1$
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
				ExceptionUtil.handle( e );
			}
			section.load( );
		}
	}

	public ReportItemHandle getReportItemHandle( )
	{
		return (ReportItemHandle) DEUtil.getInputFirstElement( input );
	}

	public boolean isBindingReference( )
	{
		return getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
	}

	/**
	 * Gets the DE CommandStack instance
	 * 
	 * @return CommandStack instance
	 */
	protected CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	protected void startTrans( String name )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).startTrans( name );
		}
	}

	protected void commit( )
	{
		if ( isEnableAutoCommit( ) )
		{
			getActionStack( ).commit( );
		}
	}

	protected void rollback( )
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

	private transient boolean enableAutoCommit = true;

	private DataSetColumnBindingsFormHandleProvider dataSetProvider;

	public void setDependedProvider(
			DataSetColumnBindingsFormHandleProvider provider )
	{
		this.dataSetProvider = provider;
	}

	public DataSetColumnBindingsFormHandleProvider getDependedProvider( )
	{
		return dataSetProvider;
	}

	protected BindingGroupSection section;

	public void setRefrenceSection( BindingGroupSection section )
	{
		this.section = section;
	}

	public void bindingDialog( )
	{
		ParameterBindingDialog dialog = new ParameterBindingDialog( UIUtil.getDefaultShell( ),
				getReportItemHandle( ) );
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

	public String getText( int key )
	{
		switch ( key )
		{
			case 0 :
				return Messages.getString( "BindingPage.Dataset.Label" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "parameterBinding.title" ); //$NON-NLS-1$
			case 2 :
				return Messages.getString( "BindingPage.ReportItem.Label" ); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	public boolean enableBindingButton( )
	{
		return !NullDatasetChoice.bindingValue.equals( ( (BindingInfo) load( ) ).getBindingValue( ) );
	}
}
