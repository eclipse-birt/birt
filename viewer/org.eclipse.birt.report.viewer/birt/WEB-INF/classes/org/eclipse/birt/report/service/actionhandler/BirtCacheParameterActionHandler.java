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

import java.io.File;
import java.rmi.RemoteException;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.ParameterAccessor;

import com.ibm.icu.util.ULocale;

public class BirtCacheParameterActionHandler extends AbstractBaseActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtCacheParameterActionHandler( IContext context,
			Operation operation, GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void __execute( ) throws RemoteException
	{
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean( );
		assert attrBean != null;

		try
		{
			// get design file name
			String reportDesignName = attrBean.getReportDesignName( );

			// get design config file name
			String configFileName = ParameterAccessor
					.getConfigFileName( reportDesignName );
			
			if (configFileName == null)
			{
				handleUpdate( );
				return;
			}

			// Generate the session handle
			SessionHandle sessionHandle = DesignEngine.newSession( ULocale.US );

			File configFile = new File( configFileName );

			// if config file existed, then delete it
			if ( configFile != null && configFile.exists( )
					&& configFile.isFile( ) )
			{
				configFile.delete( );
			}

			// create a new config file
			ReportDesignHandle handle = sessionHandle.createDesign( );

			// get parameters from operation
			Oprand[] op = this.operation.getOprand( );
			if ( op != null )
			{
				for ( int i = 0; i < op.length; i++ )
				{
					ConfigVariable configVar = new ConfigVariable( );

					String paramName = op[i].getName( );
					String paramValue = op[i].getValue( );

					// find the parameter
					ScalarParameterHandle parameter = (ScalarParameterHandle) attrBean
							.findParameter( paramName );

					// convert the parameter from current locale to default
					// locale format
					if ( paramValue != null && parameter != null )
					{
						try
						{
							Object paramValueObj = ParameterValidationUtil
									.validate( parameter.getDataType( ),
											parameter.getPattern( ),
											paramValue, attrBean.getLocale( ) );

							paramValue = ParameterValidationUtil
									.getDisplayValue( parameter.getDataType( ),
											parameter.getPattern( ),
											paramValueObj, ULocale.US );

							// add parameter to config file
							configVar.setName( paramName
									+ "_" + parameter.getID( ) ); //$NON-NLS-1$
							configVar.setValue( paramValue );
							handle.addConfigVariable( configVar );
						}
						catch ( Exception err )
						{

						}
					}
				}
			}

			// save config file
			handle.saveAs( configFileName );
			handle.close( );

			handleUpdate( );
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"BirtCacheParameterActionHandler.__execute( )" ) ); //$NON-NLS-1$
			fault.setFaultReason( e.getLocalizedMessage( ) );
			throw fault;
		}
	}

	protected void handleUpdate( )
	{
		Data data = new Data( );
		data.setConfirmation( "Parameter value saved." ); //$NON-NLS-1$

		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "birtParameterDialog" ); //$NON-NLS-1$
		updateData.setData( data );

		Update update = new Update( );
		update.setUpdateData( updateData );
		response.setUpdate( new Update[]{update} );
	}

	protected IViewerReportService getReportService( )
	{
		return null;
	}
}