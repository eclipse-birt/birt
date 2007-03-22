/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.util;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;

/**
 * Utility class for web component
 */

public class ChartWebHelper
{

	public static Chart parseChart( String strPath ) throws ChartException
	{
		Chart chartModel = null;
		final File chartFile = new File( strPath );
		// Reads the chart model
		try
		{
			if ( chartFile.exists( ) )
			{
				Serializer serializer = SerializerImpl.instance( );
				chartModel = serializer.read( new FileInputStream( chartFile ) );
			}
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.NOT_FOUND,
					e );
		}
		return chartModel;
	}

	public static boolean checkOutputType( String type )
	{
		try
		{
			return ChartUtil.isOutputFormatSupport( type );
		}
		catch ( ChartException e )
		{
			return false;
		}
	}
}
