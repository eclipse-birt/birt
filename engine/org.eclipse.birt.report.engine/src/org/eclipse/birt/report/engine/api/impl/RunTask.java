package org.eclipse.birt.report.engine.api.impl;

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportDocManager;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.HtmlPaginateEmitter;
import org.eclipse.birt.report.engine.presentation.ReportDocumentEmitter;


public class RunTask extends EngineTask implements IRunTask
{

	ReportDocument reportDoc;
	IPageHandler pageHandler;
	RunTask(ReportEngine engine, IReportRunnable runnable)
	{
		super(engine, runnable);
		
	}
	public void setPageHandler( IPageHandler callback )
	{
		this.pageHandler = callback;
	}

	public void run( IReportDocManager manager, String reportDocName )
			throws EngineException
	{
	}
	
	public void run(IReportDocument reportDoc) throws EngineException
	{
		setReportDocument(reportDoc);
	}
	
	protected void setReportDocument(IReportDocument reportDoc)
	{
		this.reportDoc = (ReportDocument)reportDoc;
	}
	
	protected void run() throws EngineException
	{
		if( !validateParameters() )
		{
			throw new EngineException( MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}
		//create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices(this);	

		EngineConfig config = engine.getConfig();
		if ( config != null )
			services.setEmitterConfig(engine.getConfig().getEmitterConfigs());
		
		services.setReportRunnable(runnable);
		
		//register default parameters
		usingParameterValues();
		
		//setup runtime configurations
		//user defined configs are overload using system properties.
		executionContext.getConfigs().putAll(runnable.getTestConfig());
		executionContext.getConfigs().putAll(System.getProperties());
		
		ReportExecutor executor = new ReportExecutor(executionContext);

		//we need output: TOC, BookMark, PageHint
		IContentEmitter emitter = new ReportDocumentEmitter(reportDoc);
		
		emitter = new HtmlPaginateEmitter(executor, emitter);
		
		// emitter is not null
		emitter.initialize(services);
		
		try
		{
			Report report = new ReportParser().parse( ((ReportRunnable)runnable).getReport() );
			reportDoc.saveDesign(report.getReportDesign());
			reportDoc.saveParamters(inputValues);
			executor.execute(report, emitter);
		}
		catch (Exception ex)
		{
			log.log(Level.SEVERE, "An error happened while running the report. Cause:", ex); //$NON-NLS-1$
		}
		catch (OutOfMemoryError err)
		{
			log.log(Level.SEVERE, "An OutOfMemory error happened while running the report."); //$NON-NLS-1$
			throw err;
		}
		
	}
}
