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
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.service.api.ParameterSelectionChoice;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.CascadeParameter;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.SelectionList;
import org.eclipse.birt.report.soapengine.api.Vector;

public abstract class AbstractGetCascadeParameterActionHandler
		extends
			AbstractBaseActionHandler
{

	public AbstractGetCascadeParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean( );
		assert attrBean != null;

		Oprand[] params = operation.getOprand( );
		String reportDesignName = attrBean.getReportDesignName( );
		Map paramMap = new LinkedHashMap( );
		for ( int i = 0; i < params.length; i++ )
		{
			Oprand param = params[i];
			paramMap.put( param.getName( ), param.getValue( ) );
		}

		InputOptions options = new InputOptions( );
		options.setOption( InputOptions.OPT_REQUEST, context.getRequest( ) );
		IViewerReportDesignHandle designHandle = new BirtViewerReportDesignHandle(
				null, reportDesignName );
		Map cascParamMap = null;
		try
		{
			cascParamMap = getParameterSelectionLists( designHandle, paramMap,
					options );
		}
		catch ( ReportServiceException e )
		{
			// TODO: throw axis fault.
			AxisFault fault = new AxisFault( );
			fault.setFaultCode( new QName( "TODO" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getMessage( ) );
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
				String[] selectionArray = new String[selections.size( )];
				selections.toArray( selectionArray );
				selectionLists[i].setSelections( selectionArray );
				i++;
			}
			cascadeParameter.setSelectionList( selectionLists );
		}
		handleUpdate( cascadeParameter );
	}

	protected abstract void handleUpdate( CascadeParameter cascadeParameter );

	private Map getParameterSelectionLists( IViewerReportDesignHandle design,
			Map params, InputOptions options ) throws ReportServiceException
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
			throw new ReportServiceException( "Invalid parameter: " + firstName );
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
							design, group.getName( ), keyValue, options );
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

	private List doQueryCascadeParameterSelectionList(
			IViewerReportDesignHandle design, String groupName,
			Object[] groupKeys, InputOptions options ) throws RemoteException,
			ReportServiceException
	{
		List selectionList = new ArrayList( );

		Collection list = getReportService( )
				.getSelectionListForCascadingGroup( design, groupName,
						groupKeys, options );

		if ( list != null && list.size( ) > 0 )
		{
			Iterator iList = list.iterator( );
			int index = 0;
			while ( iList != null && iList.hasNext( ) )
			{
				ParameterSelectionChoice item = (ParameterSelectionChoice) iList
						.next( );
				if ( item != null && item.getValue( ) != null )
				{
					try
					{
						selectionList.add( index++, (String) DataTypeUtil
								.convert( item.getValue( ),
										DataType.STRING_TYPE ) );
					}
					catch ( BirtException e )
					{
						throw new ReportServiceException( e
								.getLocalizedMessage( ) );
					}
				}
			}
		}

		return selectionList;
	}

}
