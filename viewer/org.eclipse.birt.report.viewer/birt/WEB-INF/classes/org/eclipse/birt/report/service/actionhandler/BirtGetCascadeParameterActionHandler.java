/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.CascadeParameter;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.SelectItemChoice;
import org.eclipse.birt.report.soapengine.api.SelectionList;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtGetCascadeParameterActionHandler
		extends
			AbstractGetCascadeParameterActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtGetCascadeParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		assert attrBean != null;

		Oprand[] params = operation.getOprand( );
		Map paramMap = new LinkedHashMap( );

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		IViewerReportDesignHandle designHandle = attrBean
				.getReportDesignHandle( context.getRequest( ) );

		Map cascParamMap = null;

		try
		{

			for ( int i = 0; i < params.length; i++ )
			{
				Oprand param = params[i];

				String paramName = param.getName( );
				// convert parameter using standard format
				// Get Scalar parameter handle
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) attrBean
						.findParameter( paramName );

				if ( parameterHandle == null )
					continue;

				// Convert string to object using default local
				Object paramValue = ParameterValidationUtil.validate(
						parameterHandle.getDataType( ),
						ParameterValidationUtil.DEFAULT_DATETIME_FORMAT, param
								.getValue( ) );

				paramMap.put( paramName, paramValue );
			}

			cascParamMap = getParameterSelectionLists( designHandle, paramMap,
					options, attrBean );
		}
		catch ( ReportServiceException e )
		{
			// TODO: throw axis fault.
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName( "TODO" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getMessage( ) );
			throw fault;
		}
		catch ( ValidationValueException e1 )
		{
			// TODO Auto-generated catch block
			AxisFault fault = new AxisFault( e1.getLocalizedMessage( ) );
			fault.setFaultCode( new QName( "TODO" ) ); //$NON-NLS-1$
			fault.setFaultString( e1.getMessage( ) );
			throw fault;
		}

		/**
		 * prepare response.
		 */
		CascadeParameter cascadeParameter = new CascadeParameter( );
		if ( cascParamMap != null && cascParamMap.size( ) > 0 )
		{
			SelectionList[] selectionLists = new SelectionList[cascParamMap
					.size( )];
			int i = 0;
			for ( Iterator it = cascParamMap.keySet( ).iterator( ); it
					.hasNext( ); )
			{
				selectionLists[i] = new SelectionList( );
				String name = (String) it.next( );
				selectionLists[i].setName( name );
				List selections = (List) cascParamMap.get( name );
				SelectItemChoice[] SelectItemChoices = getVectorFromList( selections );
				selectionLists[i].setSelections( SelectItemChoices );
				i++;
			}
			cascadeParameter.setSelectionList( selectionLists );
		}
		handleUpdate( cascadeParameter );
	}

	/**
	 * Get vector from the list.
	 * 
	 * @param list
	 * @return
	 */
	private SelectItemChoice[] getVectorFromList( List list )
	{
		SelectItemChoice[] selectionList = new SelectItemChoice[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			SelectItemChoice item = (SelectItemChoice) list.get( i );
			selectionList[i] = new SelectItemChoice( item.getValue( ), item
					.getLabel( ) );
		}
		return selectionList;
	}

	private Map getParameterSelectionLists( IViewerReportDesignHandle design,
			Map params, InputOptions options, ViewerAttributeBean attrBean )
			throws ReportServiceException
	{
		if ( params == null || params.size( ) == 0 )
			return new HashMap( );

		List[] listArray = null;
		Map ret = new HashMap( );
		List remainingParamNames = new ArrayList( );

		String firstName = (String) params.keySet( ).iterator( ).next( );

		Collection paramDefs = getReportService( ).getParameterDefinitions(
				design, options, false );

		ParameterDefinition paramDef = null;

		for ( Iterator it = paramDefs.iterator( ); it.hasNext( ); )
		{
			ParameterDefinition temp = (ParameterDefinition) it.next( );
			if ( temp.getName( ).equals( firstName ) )
			{
				paramDef = temp;
				break;
			}
		}

		if ( paramDef == null )
		{
			throw new ReportServiceException(
					BirtResources
							.getFormattedString(
									ResourceConstants.REPORT_SERVICE_EXCEPTION_INVALID_PARAMETER,
									new String[]{firstName} ) );
		}

		ParameterGroupDefinition group = paramDef.getGroup( );

		if ( group != null )
		{
			if ( group.getParameterCount( ) > params.size( ) )
			{
				int remainingParams = group.getParameterCount( )
						- params.size( );
				for ( int i = 0; i < remainingParams; i++ )
				{
					ParameterDefinition def = (ParameterDefinition) group
							.getParameters( ).get( params.size( ) + i );
					remainingParamNames.add( def.getName( ) );
				}
			}
		}
		// Query all lists.
		try
		{
			if ( remainingParamNames.size( ) > 0 )
			{
				listArray = new List[remainingParamNames.size( )];

				for ( int k = 0; k < remainingParamNames.size( ); k++ )
				{
					Object[] keyValue = new Object[params.size( ) + k];

					Set values = params.keySet( );
					int i = 0;
					for ( Iterator it = values.iterator( ); it.hasNext( ); )
					{
						keyValue[i] = params.get( it.next( ) );
						i++;
					}

					for ( i = 0; i < k; i++ )
					{
						if ( listArray[i].isEmpty( ) )
						{
							keyValue[params.size( ) + i] = null;
						}
						else
						{
							keyValue[params.size( ) + i] = listArray[i].get( 0 );
						}
					}

					listArray[k] = doQueryCascadeParameterSelectionList(
							remainingParamNames.get( k ).toString( ), design,
							group.getName( ), keyValue, options, attrBean );
					ret.put( remainingParamNames.get( k ), listArray[k] );
				}
			}
		}
		catch ( RemoteException e )
		{
			throw new ReportServiceException( e.getLocalizedMessage( ) );
		}
		return ret;
	}

	private List doQueryCascadeParameterSelectionList( String paramName,
			IViewerReportDesignHandle design, String groupName,
			Object[] groupKeys, InputOptions options,
			ViewerAttributeBean attrBean ) throws RemoteException,
			ReportServiceException
	{
		List selectionList = new ArrayList( );

		Collection list = getReportService( )
				.getSelectionListForCascadingGroup( design, groupName,
						groupKeys, options );

		if ( list != null && list.size( ) > 0 )
		{
			// Get Scalar parameter handle
			ScalarParameterHandle parameterHandle = (ScalarParameterHandle) attrBean
					.findParameter( paramName );

			Iterator iList = list.iterator( );
			int index = 0;
			while ( iList != null && iList.hasNext( ) )
			{
				ParameterSelectionChoice item = (ParameterSelectionChoice) iList
						.next( );
				if ( item != null && item.getValue( ) != null )
				{
					SelectItemChoice selectItemChoice = new SelectItemChoice( );
					Object value = item.getValue( );
					String label = item.getLabel( );

					if ( value == null )
						continue;

					if ( label == null || label.length( ) <= 0 )
					{
						// If label is null or blank, then use the format
						// parameter
						// value for display
						label = ParameterValidationUtil.getDisplayValue( null,
								parameterHandle.getPattern( ), value, attrBean
										.getLocale( ) );
					}

					if ( label != null )
					{
						selectItemChoice.setLabel( label );
						selectItemChoice.setValue( ParameterValidationUtil
								.getDisplayValue( value ) );
						selectionList.add( index++, selectItemChoice );
					}
				}
			}
		}

		return selectionList;
	}

	protected void handleUpdate( CascadeParameter cascadeParameter )
	{
		Data data = new Data( );
		data.setCascadeParameter( cascadeParameter );

		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "birtParameterDialog" ); //$NON-NLS-1$
		updateData.setData( data );

		Update update = new Update( );
		update.setUpdateData( updateData );
		response.setUpdate( new Update[]{update} );
	}

	protected IViewerReportService getReportService( )
	{
		return BirtReportServiceFactory.getReportService( );
	}
}
