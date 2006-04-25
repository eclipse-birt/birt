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
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.engine.api.impl.ParameterDefnBase;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.service.BirtViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.CascadeParameter;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.SelectionList;
import org.eclipse.birt.report.soapengine.api.Vector;

public abstract class AbstractGetCascadeParameterActionHandler extends
		AbstractBaseActionHandler
{

	public AbstractGetCascadeParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		BaseAttributeBean attrBean = ( BaseAttributeBean ) context.getBean( );
		assert attrBean != null;

		Oprand[] params = operation.getOprand( );
		String reportDesignName = attrBean.getReportDesignName( );
		Map paramMap = new HashMap( );
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
				String name = ( String ) it.next( );
				selectionLists[i].setName( name );
				List selections = ( List ) cascParamMap.get( name );
				Vector vector = getVectorFromList( selections );
				selectionLists[i].setSelections( vector );
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
	private Vector getVectorFromList( List list )
	{
		Vector selectionList = new Vector( );
		selectionList.setValue( new String[list.size( )] );
		for ( int i = 0; i < list.size( ); i++ )
		{
			selectionList.setValue( i, ( String ) list.get( i ) );
		}
		return selectionList;
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

		String firstName = ( String ) params.keySet( ).iterator( ).next( );

		Collection paramDefs = getReportService( ).getParameterDefinitions(
				design, options );

		ParameterDefnBase paramDef = null;

		for ( Iterator it = paramDefs.iterator( ); it.hasNext( ); )
		{
			ParameterDefnBase temp = ( ParameterDefnBase ) it.next( );
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

		String groupName = null;
		ReportElementHandle element = paramDef.getHandle( );
		assert element != null;
		if ( element.getContainer( ) instanceof CascadingParameterGroupHandle )
		{
			CascadingParameterGroupHandle groupHandle = ( CascadingParameterGroupHandle ) element
					.getContainer( );
			groupName = groupHandle.getName( );
			if ( groupHandle.getParameters( ).getCount( ) > params.size( ) )
			{
				int remainingParams = groupHandle.getParameters( ).getCount( )
						- params.size( );
				for ( int i = 0; i < remainingParams; i++ )
				{
					remainingParamNames.add( groupHandle.getParameters( ).get(
							params.size( ) + i ).getName( ) );
				}
			}

			if ( groupName == null )
			{
				throw new ReportServiceException(
						"Can not find cascade parameter group name." );
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
						keyValue[params.size( ) + i] = listArray[i].get( 0 );
					}

					ret.put( remainingParamNames.get( k ),
							doQueryCascadeParameterSelectionList( design,
									groupName, keyValue ) );

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
			Object[] groupKeys ) throws RemoteException, ReportServiceException
	{
		List selectionList = new ArrayList( );

		Collection list = getReportService( )
				.getSelectionListForCascadingGroup( design, groupName,
						groupKeys );

		if ( list != null && list.size( ) > 0 )
		{
			Iterator iList = list.iterator( );
			int index = 0;
			while ( iList != null && iList.hasNext( ) )
			{
				IParameterSelectionChoice item = ( IParameterSelectionChoice ) iList
						.next( );
				if ( item != null && item.getValue( ) != null )
				{
					try
					{
						selectionList.add( index++, ( String ) DataTypeUtil
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
