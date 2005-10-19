package org.eclipse.birt.report.engine.api.impl;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.FORenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask
{
	/**
	 * options for rendering the report
	 */
	protected IRenderOption renderOption;

	/**
	 * options for runninging the report
	 */
	protected HashMap runOptions = new HashMap();

	/**
	 * the output stream for writing the output to
	 */
	protected OutputStream ostream;

	/**
	 * full path for the output file name
	 */
	protected String outputFileName;
	
	/**
	 * @param engine reference to the report engine
	 * @param runnable the runnable report design reference
	 */
	public RunAndRenderTask(ReportEngine engine, IReportRunnable runnable)
	{
		super(engine, runnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run() throws EngineException
	{
		if( !validateParameters() )
		{
			throw new EngineException( MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}
		
		//create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices(this);	
		services.setRenderOption(renderOption);
		
		EngineConfig config = engine.getConfig();
		if ( config != null )
			services.setEmitterConfig(engine.getConfig().getEmitterConfigs());
		services.setRenderContext(context);
		services.setReportRunnable(runnable);
				
		//register default parameters
		usingParameterValues();
		
		//setup runtime configurations
		//user defined configs are overload using system properties.
		executionContext.getConfigs().putAll(runnable.getTestConfig());
		executionContext.getConfigs().putAll(System.getProperties());
		
		// Set up rendering environment and check for supported format
		executionContext.setRenderOption(renderOption);
		String format = renderOption.getOutputFormat();
		if (format == null || format.length() == 0) // $NON-NLS-1
		{
			renderOption.setOutputFormat( "html" );	// $NON-NLS-1
			format = "html"; // $NON-NLS-1
		}
		else if ( renderOption != null
				&& format.equalsIgnoreCase( "fo" ) // $NON-NLS-1
				&& ( ( FORenderOption )renderOption ).getTailoredForFOP( ) )
		{
			format = "fop"; // $NON-NLS-1
		}
		
		if (!ExtensionManager.getInstance().getEmitterExtensions().containsKey(format))
		{
			log.log( Level.SEVERE, MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format);
			throw new EngineException(MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format);
		}

		IReportEmitter emitter = ExtensionManager.getInstance().createEmitter(format);
		if (emitter == null)
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.", format);	// $NON-NLS-1$
			throw new EngineException(MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION);			
		}

		// emitter is not null
		emitter.initialize(services);
		ReportExecutor executor = new ReportExecutor(executionContext, emitter);

		try
		{
			executor.execute(((ReportRunnable)runnable).getReport(), inputValues);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#setRenderOption(org.eclipse.birt.report.engine.api2.IRenderOption)
	 */
	public void setRenderOption(IRenderOption options)
	{
		renderOption = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#getRenderOption()
	 */
	public IRenderOption getRenderOption()
	{
		return renderOption;
	}
}