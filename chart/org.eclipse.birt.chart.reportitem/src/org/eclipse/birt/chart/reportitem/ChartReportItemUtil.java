/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil extends ChartItemUtil
{


	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}
	

	public static <T> T getAdapter( Object adaptable, Class<T> adapterClass )
	{
		IAdapterManager adapterManager = Platform.getAdapterManager( );
		return adapterClass.cast( adapterManager.loadAdapter( adaptable,
				adapterClass.getName( ) ) );
	}

	public static Serializer instanceSerializer( ExtendedItemHandle handle )
	{

		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );

		if ( factory != null )
		{
			return factory.createSerializer( handle );
		}
		else
		{
			return SerializerImpl.instance( );
		}
	}
}
