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

package org.eclipse.birt.chart.tests.script;

import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.birt.chart.script.api.IChart;
import org.eclipse.birt.chart.script.api.IChartWithAxes;
import org.eclipse.birt.chart.script.api.IChartWithoutAxes;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;

/**
 * 
 */

public abstract class BaseChartTestCase extends TestCase
{

	private IChart cwa;
	private IChart cwo;
	private IReportDesign design;

	private static final String REPORT_PATH = "api_test.rptdesign"; //$NON-NLS-1$

	protected void setUp( ) throws Exception
	{
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		URL url = BaseChartTestCase.class.getResource( REPORT_PATH );
		if ( url != null )
		{
			design = engine.openDesign( url.getFile( ), url.openStream( ), null );
			// SessionHandle sessionHandle = engine.newSessionHandle(
			// ULocale.getDefault( ) );
			// ReportDesignHandle designHandle = sessionHandle.openDesign(
			// REPORT_PATH );
			// ExtendedItemHandle eih = (ExtendedItemHandle)
			// designHandle.findElement( elementName );
			// return (IChart) eih.getReportItem( ).getSimpleElement( );
			cwa = (IChart) getReportDesign( ).getReportElement( "ChartWithAxes" ); //$NON-NLS-1$
			cwo = (IChart) getReportDesign( ).getReportElement( "ChartWithoutAxes" ); //$NON-NLS-1$
		}
	}

	protected IReportDesign getReportDesign( )
	{
		return design;
	}

	protected IChartWithAxes getChartWithAxes( )
	{
		return (IChartWithAxes) cwa;
	}

	protected IChartWithoutAxes getChartWithoutAxes( )
	{
		return (IChartWithoutAxes) cwo;
	}

}
