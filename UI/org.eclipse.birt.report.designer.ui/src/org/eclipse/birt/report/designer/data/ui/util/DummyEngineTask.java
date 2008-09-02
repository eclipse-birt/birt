
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.designer.data.ui.util;

import java.util.List;

import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * 
 */

public class DummyEngineTask extends RunAndRenderTask
{	
	public DummyEngineTask( ReportEngine engine, IReportRunnable runnable, ModuleHandle moduleHandle )
	{
		super( engine, runnable );
		setEngineTaskParameters( this, moduleHandle );
	}
	
	public void run() throws EngineException
	{
		usingParameterValues( );
		loadDesign( );

	}

	/**
	 * Fetch the report parameter name/value pairs from the rptconfig file. 
	 * And also set all the parameters whose value is not null to the Engine task.
	 * 
	 * @param engineTask
	 */
	private void setEngineTaskParameters( DummyEngineTask engineTask, ModuleHandle moduleHandle )
	{
		List paramsList = moduleHandle.getAllParameters( );
		for ( int i = 0; i < paramsList.size( ); i++ )
		{
			Object parameterObject = paramsList.get( i );
			if ( parameterObject instanceof ScalarParameterHandle )
			{
				ScalarParameterHandle parameterHandle = (ScalarParameterHandle) parameterObject;
				Object value = DataAdapterUtil.getParamValueFromConfigFile( parameterHandle );
				if ( value != null )
				{
					engineTask.setParameter( parameterHandle.getName( ),
							value,
							parameterHandle.getDisplayName( ) );
				}
			}
		}
	}

}
