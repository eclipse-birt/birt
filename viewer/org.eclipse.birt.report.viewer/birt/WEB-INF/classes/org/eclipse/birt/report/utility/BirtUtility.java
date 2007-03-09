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

package org.eclipse.birt.report.utility;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.ParameterDefinition;
import org.eclipse.birt.report.service.api.ReportServiceException;

/**
 * Utilities for Birt Report Service
 * 
 */
public class BirtUtility
{

	/**
	 * Returns the parameter handle list
	 * 
	 * @param reportDesignHandle
	 * @return
	 * @throws ReportServiceException
	 */
	public static List getParameterList(
			IViewerReportDesignHandle reportDesignHandle )
			throws ReportServiceException
	{
		IReportRunnable runnable = (IReportRunnable) reportDesignHandle
				.getDesignObject( );
		if ( runnable == null )
			return null;

		ModuleHandle model = runnable.getDesignHandle( ).getModuleHandle( );
		if ( model == null )
			return null;

		return model.getFlattenParameters( );
	}

	/**
	 * find the parameter definition by parameter name
	 * 
	 * @param parameterList
	 * @param paramName
	 * @return
	 */
	public static ParameterDefinition findParameterDefinition(
			Collection parameterList, String paramName )
	{
		if ( parameterList == null || paramName == null )
			return null;

		// find parameter definition
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			ParameterDefinition parameter = (ParameterDefinition) iter.next( );
			if ( parameter == null )
				continue;

			String name = parameter.getName( );
			if ( paramName.equals( name ) )
			{
				return parameter;
			}
		}

		return null;
	}

	/**
	 * find the parameter handle by parameter name
	 * 
	 * @param reportDesignHandle
	 * @param paramName
	 * @return
	 * @throws ReportServiceException
	 */
	public static ParameterHandle findParameter(
			IViewerReportDesignHandle reportDesignHandle, String paramName )
			throws ReportServiceException
	{
		if ( paramName == null )
			return null;

		IReportRunnable runnable = (IReportRunnable) reportDesignHandle
				.getDesignObject( );
		if ( runnable == null )
			return null;

		// get module handle from report runnable
		ModuleHandle model = runnable.getDesignHandle( ).getModuleHandle( );
		if ( model == null )
			return null;

		return model.findParameter( paramName );
	}
}
