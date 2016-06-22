/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.junit.Ignore;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 * TestPerformance
 */
@Ignore("performance testing shouldn't be define in utest level")
public class TestPerformance extends TestCase implements ICrosstabConstants
{

	public static final String PDF_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_PDF;
	public static final String HTML_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_HTML;

	private IReportEngine engine;
	private IDesignEngine designEngine;

	private String format = HTML_FORMAT;

	public void testReport( )
	{
		ThreadResources.setLocale( ULocale.ENGLISH );

		long span = System.currentTimeMillis( );

		if ( designEngine == null )
		{
			designEngine = new DesignEngine( new DesignConfig( ) );
		}

		if ( engine == null )
		{
			engine = new ReportEngine( new EngineConfig( ) );
		}

		System.out.println( "Engine initialization: "
				+ ( System.currentTimeMillis( ) - span ) );

		span = System.currentTimeMillis( );

		IReportRunnable report = null;
		try
		{
			report = engine.openReportDesign( "xtab.rptdesign",
					TestPerformance.class.getResourceAsStream( "input/xtab.rptdesign" ) );
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
			return;
		}

		System.out.println( "Open design: "
				+ ( System.currentTimeMillis( ) - span ) );

		span = System.currentTimeMillis( );
		// format = PDF_FORMAT;

		IRunAndRenderTask task = engine.createRunAndRenderTask( report );

		HTMLRenderOption options = new HTMLRenderOption( );
		options.setOutputFormat( format );
		options.setOutputFileName( "./target/utest." + format ); //$NON-NLS-1$
		options.setHtmlPagination( true );
		options.setImageHandler( new HTMLCompleteImageHandler( ) );
		options.setImageDirectory( "./target/images" ); //$NON-NLS-1$
		task.setRenderOption( options );

		HashMap params = new HashMap( );
		task.setParameterValues( params );
		task.validateParameters( );

		System.out.println( "Set engine options: "
				+ ( System.currentTimeMillis( ) - span ) );

		span = System.currentTimeMillis( );
		try
		{
			task.run( );

			System.out.println( "Task finined successfully." ); //$NON-NLS-1$
		}
		catch ( EngineException e )
		{
			e.printStackTrace( );
		}

		System.out.println( "Engine run: "
				+ ( System.currentTimeMillis( ) - span ) );

		span = System.currentTimeMillis( );
	}
}
