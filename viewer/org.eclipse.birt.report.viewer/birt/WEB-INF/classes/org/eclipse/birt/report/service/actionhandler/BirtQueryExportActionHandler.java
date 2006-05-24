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

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.ResultSets;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;

public class BirtQueryExportActionHandler
		extends
			AbstractQueryExportActionHandler
{

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtQueryExportActionHandler( IContext context, Operation operation,
			GetUpdatedObjectsResponse response )
	{
		super( context, operation, response );
	}

	protected void handleUpdate( ResultSets resultSets )
	{
		Data data = new Data( );
		data.setResultSets( resultSets );

		UpdateData updateData = new UpdateData( );
		updateData.setTarget( "birtSimpleExportDataDialog" ); //$NON-NLS-1$
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
