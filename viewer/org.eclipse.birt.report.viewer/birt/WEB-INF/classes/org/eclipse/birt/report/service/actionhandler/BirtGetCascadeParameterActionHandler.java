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

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;
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
import org.eclipse.birt.report.utility.DataUtil;
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

	protected void __execute( ) throws Exception
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

		for ( int i = 0; i < params.length; i++ )
		{
			Oprand param = params[i];

			String paramName = param.getName( );
			boolean isLocale = false;

			// if Null Value Parameter
			if ( ParameterAccessor.PARAM_ISNULL.equalsIgnoreCase( paramName ) )
			{
				paramMap.put( param.getValue( ), null );
				continue;
			}
			else if ( paramName.startsWith( ParameterAccessor.PREFIX_ISLOCALE ) )
			{
				// current parameter value is a locale string
				paramName = paramName.replaceFirst(
						ParameterAccessor.PREFIX_ISLOCALE, "" ); //$NON-NLS-1$
				isLocale = true;
			}

			// get parameter definition object
			ParameterDefinition parameter = attrBean
					.findParameterDefinition( paramName );
			if ( parameter == null )
				continue;

			// Convert parameter
			String format = parameter.getPattern( );
			String dataType = ParameterDataTypeConverter
					.ConvertDataType( parameter.getDataType( ) );
			Object paramValue = DataUtil.validate( dataType, format, param
					.getValue( ), attrBean.getLocale( ), isLocale );

			paramMap.put( paramName, paramValue );
		}

		cascParamMap = getParameterSelectionLists( designHandle, paramMap,
				options, attrBean );

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

		List paramList = null;
		Map ret = new HashMap( );
		List remainingParamNames = new ArrayList( );

		String firstName = (String) params.keySet( ).iterator( ).next( );

		// Get parameter definiations list
		Collection paramDefs = attrBean.getParameterDefList( );

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
							.getMessage(
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
		// Query only the next cascading parameter.
		try
		{
			if ( remainingParamNames.size( ) > 0 )
			{
				Object[] keyValue = new Object[params.size( )];

				Set values = params.keySet( );
				int i = 0;
				for ( Iterator it = values.iterator( ); it.hasNext( ); )
				{
					keyValue[i] = params.get( it.next( ) );
					i++;
				}

				paramList = doQueryCascadeParameterSelectionList(
						remainingParamNames.get( 0 ).toString( ), design, group
								.getName( ), keyValue, options, attrBean );
				ret.put( remainingParamNames.get( 0 ), paramList );
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
			// Get parameter definition object
			ParameterDefinition parameter = attrBean
					.findParameterDefinition( paramName );

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
								parameter.getPattern( ), value, attrBean
										.getLocale( ) );
					}
					else
					{
						// Format display text of dynamic parameter
						label = ParameterValidationUtil.getDisplayValue( null,
								parameter.getPattern( ), label, attrBean
										.getLocale( ) );
					}

					if ( label != null )
					{
						selectItemChoice.setLabel( label );
						selectItemChoice.setValue( DataUtil
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
