package org.eclipse.birt.report.engine.api.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.EngineEmitterServices;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask
{
	/**
	 * The parameter values that the caller has set explicitly
	 */
	protected HashMap inputValues = new HashMap();
	
	/**
	 * The parameter values that will be used to run the report. It is a merged map between
	 * the input value and the default values.
	 */
	protected HashMap runValues = new HashMap();

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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#validateParameters()
	 */
	public boolean validateParameters()
	{
		// evaluate default and combines it with the parameter values that are set
		Collection paramDefns = ((ReportRunnable)runnable).getParameterDefns(false);
		runValues.putAll(evaluateDefaults(paramDefns));
		runValues.putAll(inputValues);
		
		// Validate each parameter. Ideally, we will return all validation failures. 
		// For now, just return false on first failure.
		Iterator iter = paramDefns.iterator();
		while(iter.hasNext())
		{
			IParameterDefnBase p = (IParameterDefnBase) iter.next();

			if ( ! validateParameter( p, runValues.get(p.getName()) ) )
				return false;
		}
		return true;
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
		services.setReportName(runnable.getReportName());
		services.setRenderOption(renderOption);
		
		EngineConfig config = engine.getConfig();
		if ( config != null )
			services.setEmitterConfig(engine.getConfig().getEmitterConfigs());
		services.setRenderContext(renderContext);
		services.setReportRunnable(runnable);
				
		//register default parameters
		if( runValues != null)
			executionContext.registerBean("params", runValues); //$NON-NLS-1$
		
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
	}

	/**
	 * validate whether the parameter value is a valid value for the parameter
	 * 
	 * @param p the parameter to be verified
	 * @param paramValue the value for the parameter
	 * @return true if the given parameter value is valid; false otherwise
	 */
	private boolean validateParameter(IParameterDefnBase p, Object paramValue)
	{
		// Only support validation for scalar parameter
		if (IParameterDefnBase.SCALAR_PARAMETER != p.getParameterType())
			return false;
		
		assert p instanceof IScalarParameterDefn;
		IScalarParameterDefn paramHandle = (IScalarParameterDefn) p;
		String 	paramName = paramHandle.getName();
		int 	type = paramHandle.getDataType();
		
		// Handle null parameter values
		if (paramValue == null)
		{
			if (paramHandle.allowNull())
				return true;

			log.log(Level.SEVERE, 
						"Parameter {0} doesn't allow a null value.",	//$NON-NLS-1$ 
						paramName); 
			return false;
		}

		/*
		 * Validate based on parameter type
		 */
		if (type == IScalarParameterDefn.TYPE_DECIMAL || type == IScalarParameterDefn.TYPE_FLOAT)
		{
			if (paramValue instanceof Number)
				return true;

			log.log(Level.SEVERE, "The supplied value {0} for parameter {1} is not a number.", new String[] {paramValue.toString(), paramName}); //$NON-NLS-1$
			return false;
		}
		else if (type == IScalarParameterDefn.TYPE_DATE_TIME)
		{
			if (paramValue instanceof Date)
				return true;
			log.log(Level.SEVERE, "The supplied value {0} for parameter {1} is not a valid date.", new String[] {paramValue.toString(), paramName}); //$NON-NLS-1$
			return false;
		}
		else if (type == IScalarParameterDefn.TYPE_STRING)
		{
			String value = paramValue.toString().trim();
			if (value.equals("") && !paramHandle.allowBlank()) //$NON-NLS-1$
			{
				log.log(Level.SEVERE, "parameter {0} can't be blank.", paramName); //$NON-NLS-1$
				return false;
			}
			return true;
		}
		else if (type == IScalarParameterDefn.TYPE_BOOLEAN)
		{
			if (paramValue instanceof Boolean)
				return true;
			log.log(Level.SEVERE, "The supplied value {0} for parameter {1} is not a boolean.", new String[] {paramValue.toString(), paramName}); //$NON-NLS-1$
			return false;
		}
		assert type == IScalarParameterDefn.TYPE_ANY;
		return true;
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#setParameterValues(java.util.HashMap)
	 */
	public void setParameterValues(HashMap params)
	{
		inputValues = params;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#getParameterValues()
	 */
	public HashMap getParameterValues()
	{
		return inputValues;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.IRunAndRenderTask#setParameterValue(java.lang.String, java.lang.Object)
	 */
	public void setParameterValue(String name, Object value) {
		inputValues.put(name, value);
	}
}