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

import java.io.FileInputStream;
import java.io.InputStream;

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

	private static final String REPORT_PATH = "src/org/eclipse/birt/chart/tests/script/api_test.rptdesign";

	protected void setUp( ) throws Exception
	{
		DesignEngine engine = new DesignEngine( new DesignConfig( ) );
		InputStream ins = new FileInputStream( REPORT_PATH );
		design = engine.openDesign( REPORT_PATH, ins, null );
		// SessionHandle sessionHandle = engine.newSessionHandle(
		// ULocale.getDefault( ) );
		// ReportDesignHandle designHandle = sessionHandle.openDesign(
		// REPORT_PATH );
		// ExtendedItemHandle eih = (ExtendedItemHandle)
		// designHandle.findElement( elementName );
		// return (IChart) eih.getReportItem( ).getSimpleElement( );
		cwa = (IChart) getReportDesign( ).getReportElement( "ChartWithAxes" );
		cwo = (IChart) getReportDesign( ).getReportElement( "ChartWithoutAxes" );
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
