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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSetBindingSelector;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BindingGroupDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;

public class ChartBindingGroupDescriptorProvider extends
		BindingGroupDescriptorProvider
{

	private static final String DATA_CUBES_DEFAULT = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartBindingGroupDescriptorProvider.DataCubes.Default" ); //$NON-NLS-1$

	private static final String DATA_SETS_DEFAULT = org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "ChartBindingGroupDescriptorProvider.DataSets.Default" ); //$NON-NLS-1$
	
	@SuppressWarnings("unchecked")
	protected List<ReportItemHandle> getAvailableDataBindingReferenceList(
			ReportItemHandle element )
	{
		List<ReportItemHandle> availableList = element.getAvailableDataBindingReferenceList( );
		List<ReportItemHandle> referenceList = new ArrayList<ReportItemHandle>( );
		for ( ReportItemHandle handle : availableList )
		{
			if ( handle instanceof ExtendedItemHandle )
			{
				String extensionName = ( (ExtendedItemHandle) handle ).getExtensionName( );
				if ( !ChartReportItemUtil.isAvailableExtensionToReferenceDataBinding( extensionName ) )
				{
					continue;
				}
			}
			referenceList.add( handle );
		}
		return referenceList;
	}


	
	@SuppressWarnings("rawtypes")
	public Object load( )
	{
		ReportItemHandle element = getReportItemHandle( );
		boolean isNotDataModel = false;;
		int type = element.getDataBindingType( );
		List<ReportItemHandle> referenceList = getAvailableDataBindingReferenceList( element );
		String[] references = new String[referenceList.size( ) + 1];
		references[0] = NONE;
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			references[i + 1] = referenceList.get( i ).getQualifiedName( );
		}
		setReferences( references );
		String value;
		switch ( type )
		{
			case ReportItemHandle.DATABINDING_TYPE_DATA :
				DataSetHandle dataset = ChartReportItemHelper.instance( )
						.getBindingDataSetHandle( element );
				CubeHandle cube = ChartReportItemHelper.instance( )
						.getBindingCubeHandle( element );
				if ( dataset == null && cube == null )
				{
					value = NullDatasetChoice.getBindingValue( );
					isNotDataModel = true;
				}
				else if ( dataset != null )
				{
					List datasets = element.getModuleHandle( ).getAllDataSets( );
					if ( datasets != null )
					{
						for ( int i = 0; i < datasets.size( ); i++ )
						{
							if ( datasets.get( i ) == dataset )
							{
								isNotDataModel = true;
								break;
							}
						}
					}
					value = dataset.getQualifiedName( );
				}
				else
				{
					List cubes = element.getModuleHandle( ).getAllCubes( );
					if ( cubes != null )
					{
						for ( int i = 0; i < cubes.size( ); i++ )
						{
							if ( cubes.get( i ) == cube )
							{
								isNotDataModel = true;
								break;
							}
						}
					}
					value = cube.getName( );
				}
				break;
			case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
				ReportItemHandle reference = element.getDataBindingReference( );
				if ( reference == null )
					value = NONE;
				else
					value = reference.getQualifiedName( );
				break;
			default :
			{
				value = NullDatasetChoice.getBindingValue( );
				isNotDataModel = true;
			}
		}

		BindingInfo info = new BindingInfo( type, value, isNotDataModel );

		// set correct binding info for chart in multi-view case.
		if ( ChartReportItemUtil.isChildOfMultiViewsHandle( getReportItemHandle( ) ) )
		{
			String name = element.getContainer( )
					.getContainer( )
					.getQualifiedName( );
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF );
			name = ( name == null ) ? NONE : name;
			info.setBindingValue( name );
			info.setReadOnly( true );
		}
		if ( ChartCubeUtil.isPlotChart( element )
				|| ChartCubeUtil.isAxisChart( element ) )
		{
			info.setBindingType( ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF );
			info.setReadOnly( true );
		}

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
					else if ( value.equals( DATA_SETS_DEFAULT ) )
					{
						value = ChoiceSetFactory.getDataSets( )[0];
					}
					else if ( value.equals( DATA_CUBES_DEFAULT ) )
					{
						value = ChoiceSetFactory.getCubes( )[0];
					}
					int ret = 0;
					if ( !NONE.equals( ( (BindingInfo) load( ) ).getBindingValue( )
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
							if ( getAvailableDatasets( ).contains( value ) )
							{
								resetDataSetReference( value, info, true );
							}
							else
							{
								resetCubeReference( value, true );
							}
							break;
						// Doesn't clear binding info
						case 1 :
							if ( getAvailableDatasets( ).contains( value ) )
							{
								resetDataSetReference( value,  info, false );
							}
							else
								resetCubeReference( value, false );
							break;
						// Cancel.
						case 2 :
							section.load( );
					}
					break;
				case ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF :
					if ( value.equals( NONE ) )
					{
						value = null;
					}
					int ret1 = 0;
					if ( !NONE.equals( ( (BindingInfo) load( ) ).getBindingValue( )
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
							section.load( );
					}
			}
		}
	}

	private void resetCubeReference( Object value, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			CubeHandle cubeHandle = null;
			if ( value != null )
			{
				cubeHandle = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findCube( value.toString( ) );
			}
			if ( getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				getReportItemHandle( ).setDataBindingReference( null );
			}
			getReportItemHandle( ).setDataSet( null );
			
			if ( cubeHandle == null && value != null )
			{
				getReportItemHandle( ).setDataSet( null );
				new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						value );
			}
			else
			{
				new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						null );
				getReportItemHandle( ).setCube( cubeHandle );
			}
			
			getReportItemHandle( ).setCube( cubeHandle );
			if ( clearHistory )
			{
				getReportItemHandle( ).getColumnBindings( ).clearValue( );
				getReportItemHandle( ).getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
			}
			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		section.load( );
	}

	private List getAvailableDatasets( )
	{
		return Arrays.asList( ChoiceSetFactory.getDataSets( ) );
	}

	/**
	 * Gets all the Cubes available.
	 * 
	 * @return A String array contains all the Cubs.
	 */
	private String[] getCubes( )
	{
		ArrayList list = new ArrayList( );

		ModuleHandle handle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		for ( Iterator iterator = handle.getVisibleCubes( ).iterator( ); iterator.hasNext( ); )
		{
			CubeHandle CubeHandle = (CubeHandle) iterator.next( );
			list.add( CubeHandle.getQualifiedName( ) );
		}

		return (String[]) list.toArray( new String[0] );
	}

	public BindingInfo[] getAvailableDatasetItems( )
	{
		ModuleHandle handle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		List<DataSetHandle> dataSets = UIUtil.getVisibleDataSetHandles( handle );
		List<CubeHandle> cubes = handle.getVisibleCubes( );
		int length = 1;
		if ( dataSets.size( ) > 0 )
			length += ( dataSets.size( ) + 1 );
		if ( cubes.size( ) > 0 )
			length += ( cubes.size( ) + 1 );
		BindingInfo[] newList = new BindingInfo[length];
		newList[0] = NullDatasetChoice;
		if ( dataSets.size( ) > 0 )
		{
			newList[1] = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA, DATA_SETS_DEFAULT, true);
			for(int i=0;i<dataSets.size( );i++)
			{
				DataSetHandle dataSet = dataSets.get( i );
				if(handle.findDataSet( dataSet.getQualifiedName( ) ) == dataSet)
				{
					newList[i+2] = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA, dataSet.getQualifiedName( ), true);
				}
				else
				{
					newList[i+2] = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA, dataSet.getQualifiedName( ), false);
				}
			}
		}
		if ( cubes.size( ) > 0 )
		{
			newList[newList.length - cubes.size( ) - 1] = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA, DATA_CUBES_DEFAULT, true);;
			for(int i=0;i<cubes.size( );i++)
			{
				CubeHandle cube = cubes.get( i );
				newList[i+ newList.length - cubes.size( )] = new BindingInfo( ReportItemHandle.DATABINDING_TYPE_DATA, cube.getQualifiedName( ), true);
			}
		}
		return newList;
	}

	private void resetDataSetReference( Object value, BindingInfo info, boolean clearHistory )
	{
		try
		{
			startTrans( "" ); //$NON-NLS-1$
			getReportItemHandle( ).setDataBindingReference( null );
			DataSetHandle dataSet = null;
			if ( value != null && info != null && info.isDataSet( ) )
			{
				dataSet = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findDataSet( value.toString( ) );
			}
			if ( getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				getReportItemHandle( ).setDataBindingReference( null );
			}
			getReportItemHandle( ).setCube( null );
			
			boolean isExtendedDataModel = false;
			if ( dataSet == null && value != null )
			{
				getReportItemHandle( ).setDataSet( null );
				isExtendedDataModel = new LinkedDataSetAdapter( ).setLinkedDataModel( getReportItemHandle( ),
						value );
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
					getDependedProvider( ).generateBindingColumns( columns );
				}
			}

			commit( );
		}
		catch ( SemanticException e )
		{
			rollback( );
			ExceptionHandler.handle( e );
		}
		section.load( );
	}

	private void resetReference( Object value, boolean clearHistory )
	{
		if ( value == null
				&& this.getReportItemHandle( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_DATA )
		{
			resetDataSetReference( null, null, true );
		}
		else
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
			section.load( );
		}
	}

	public String getText( int key )
	{
		switch ( key )
		{
			case 0 :
				return org.eclipse.birt.chart.reportitem.ui.i18n.Messages.getString( "BindingPage.Data.Label" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "parameterBinding.title" ); //$NON-NLS-1$
			case 2 :
				return Messages.getString( "BindingPage.ReportItem.Label" ); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	public boolean enableBindingButton( )
	{
		return getAvailableDatasets( ).contains( ( (BindingInfo) load( ) ).getBindingValue( ) );
	}
}
