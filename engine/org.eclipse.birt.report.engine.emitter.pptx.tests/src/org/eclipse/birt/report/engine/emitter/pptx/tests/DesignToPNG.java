
package org.eclipse.birt.report.engine.emitter.pptx.tests;

import org.apache.poi.xslf.util.PPTX2PNG;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;

public class DesignToPNG
{
	public void convert( String design, String outputFolder )
			throws Exception
	{
		IReportEngine engine = getReportEngine( );
		try
		{
			toDocument( engine, design, outputFolder + "/output.rptdocument" );
			toPPTX( engine, outputFolder + "/output.rptdocument", outputFolder
					+ "/output.pptx" );
			toPNG( outputFolder + "/output.pptx" );
		}
		finally
		{
			closeReportEngine( engine );
		}
	}

	protected void toPNG( String pptx ) throws Exception
	{
		PPTX2PNG.main( new String[]{pptx} );

	}

	protected void toDocument( IReportEngine engine, String reportDesign,
			String document ) throws BirtException
	{
		IReportRunnable runnable = engine.openReportDesign( reportDesign );
		IRunTask task = engine.createRunTask( runnable );
		try
		{
			task.run( document );
		}
		finally
		{
			task.close( );
		}
	}

	protected void toPPTX( IReportEngine engine, String reportDocument,
			String pptx ) throws EngineException
	{
		IReportDocument document = engine.openReportDocument( reportDocument );
		try
		{
			IRenderTask task = engine.createRenderTask( document );
			try
			{
				RenderOption option = new RenderOption( );
				option.setOutputFormat( "pptx" );
				option.setOutputFileName( pptx );
				task.setRenderOption( option );
				task.render( );;
			}
			finally
			{
				task.close( );
			}
		}
		finally
		{
			document.close( );
		}
	}

	protected IReportEngine getReportEngine( ) throws BirtException
	{
		EngineConfig config = new EngineConfig( );

		Platform.startup( config );

		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		IReportEngine engine = factory.createReportEngine( config );
		return engine;

	}

	protected void closeReportEngine( IReportEngine engine )
	{
		engine.destroy( );
		Platform.shutdown( );
	}
}
