package org.eclipse.birt.report.engine.api.impl;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IParameterDefn;
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
	 * a map that stored parameter values
	 */
	protected HashMap parameterValues = new HashMap();
	protected HashMap defaultValues = new HashMap();

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
		executionContext.setRunnable(runnable);
		executionContext.setLocale(locale);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#validateParameters()
	 */
	public boolean validateParameters()
	{
		Collection paramDefns = ((ReportRunnable)runnable).getParameterDefns(false);
		evaluateDefaults(paramDefns);
		//combine the values
		defaultValues.putAll(parameterValues);
		Iterator iter = paramDefns.iterator();
		while(iter.hasNext())
		{
			IParameterDefn paramHandle = (IParameterDefn) iter.next();

			String paramName = paramHandle.getName( );
			assert paramName != null;
			
			Object paramValue = defaultValues.get(paramName);

			if ( ! validateParameter( paramHandle, paramValue ) )
			{
				return false;
			}
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
			throw new EngineException("parameter validation failed!"); //$NON-NLS-1$
		}
		//create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices(this);
		
		services.setReportName(runnable.getReportName());
		services.setRenderOption(renderOption);
		EngineConfig config = engine.getConfig();
		if(config!=null)
		{
			services.setEmitterConfig(engine.getConfig().getEmitterConfigs());
			
		}
		services.setRenderContext(renderContext);
		services.setReportRunnable(runnable);
		
		
		//regiest default parameter
		if(defaultValues!=null)
		{
			executionContext.registerBean("params", defaultValues); //$NON-NLS-1$
		}
		//setup runtime configurations
		//user defined configs should be overload by system properties.
		executionContext.getConfigs().putAll(runnable.getConfigs());
		executionContext.getConfigs().putAll(System.getProperties());
		executionContext.setRenderOption(renderOption);
		String format = renderOption.getOutputFormat();
		if (!ExtensionManager.getInstance().getEmitterExtensions()
				.containsKey(format)) //$NON-NLS-1$
		{
			log
					.log(
							Level.SEVERE,
							"Unsupported format {0}!", format); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.INVALID_FORMAT_EXCEPTION,
					format);
		}

		IReportEmitter emitter = ExtensionManager.getInstance()
				.createEmitter(format);
		ReportExecutor executor;
		if (emitter != null)
		{
			 
			emitter.initialize(services);
			executor = new ReportExecutor(executionContext, emitter);

			try
			{
				executor.execute(((ReportRunnable)runnable).getReport(), parameterValues);
			}
			catch (Exception ex)
			{
				log.log(Level.SEVERE, "ERROR IN EXECUTION!", ex); //$NON-NLS-1$
			}
		}

	}

	private boolean validateParameter(IParameterDefn handle, Object result)
	{
		assert IParameterDefnBase.SCALAR_PARAMETER == handle.getParameterType();
		IScalarParameterDefn paramHandle = (IScalarParameterDefn) handle;

		String paramName = paramHandle.getName();
		int type = paramHandle.getDataType();

		// TODO: Note that if a parameter is not required, and it is not
		// defined,
		// the following code would add a null to it. We simply should
		// not add the
		// entry to the map.
		if (result == null)
		{
			return false;
		}
		
		if (result == null)
		{
			if (!paramHandle.allowNull())
			{
				log
						.log(
								Level.SEVERE,
								"{0} doesn't allow a null value or user doesn't input a proper parameter.", paramName); //$NON-NLS-1$
				return false;
			}
			return true;
		}
		else
		{
			/*if(type==IParameterDefn.TYPE_STRING && result.toString().length()==0 &&!parmHandle.allowBlank())
			{
				return false;
			}	*/		
		}

		/*
		 * Get value according to different type If it has a non-null
		 * value, we should validate whether this value has a correct
		 * type
		 */
		if (type == IScalarParameterDefn.TYPE_DECIMAL || type == IScalarParameterDefn.TYPE_FLOAT)
		{
			if (result instanceof Number)
				return true;

			log.log(Level.SEVERE,
							"{0} should be a number", paramName); //$NON-NLS-1$
			return false;
		}
		else if (type == IScalarParameterDefn.TYPE_DATE_TIME)
		{
			if (result instanceof Date)
				return true;
			log.log(Level.SEVERE,
								"The specified value of {0} must be date, or it cannot be parsed. Please check your date value.You should input the date value like \"9/13/08 8:01 PM\"", paramName); //$NON-NLS-1$
			return false;
		}
		else if (type == IScalarParameterDefn.TYPE_STRING)
		{
			String value = result.toString().trim();
			if (value.equals("") && !paramHandle.allowBlank()) //$NON-NLS-1$
			{
				log.log(Level.SEVERE,
								"{0} can't be blank", paramName); //$NON-NLS-1$
				return false;
			}
			return true;
		}
		else if (type == IScalarParameterDefn.TYPE_BOOLEAN)
		{
			if (result instanceof Boolean)
				return true;
			log.log(Level.SEVERE,
							"{0} should be a boolean value", paramName); //$NON-NLS-1$
			return false;
		}
		assert type == IScalarParameterDefn.TYPE_ANY;
		return true;
	}
	
	/**
	 * get value
	 * 
	 * @param value
	 * @param type
	 *                the data type
	 * @return
	 */
	protected Object getValue(String expr, int type)
	{
		Object value = executionContext.evaluate(expr);
		if(value==null && expr!=null && expr.length()>0)
		{
			value = expr;
		}
		if(expr==null)
		{
			return null;
		}
		try
		{
			switch (type)
			{
				case IScalarParameterDefn.TYPE_BOOLEAN :
					return DataTypeUtil.toBoolean(value);
				case IScalarParameterDefn.TYPE_DATE_TIME :
					return DataTypeUtil.toDate(value);
				case IScalarParameterDefn.TYPE_DECIMAL :
					return DataTypeUtil.toBigDecimal(value);
				case IScalarParameterDefn.TYPE_FLOAT :
					return DataTypeUtil.toDouble(value);
				default :
					return value;
			}

		}
		catch (BirtException e)
		{
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		}
	}
	
	private void evaluateDefaults(Collection params) 
	{
		if (params != null)
		{
			Iterator iter = params.iterator();
			while (iter.hasNext())
			{
				
				IParameterDefnBase pBase = (IParameterDefnBase) iter.next();
				if (pBase instanceof ScalarParameterDefn) 
				{
					setScalarParameterValue((ScalarParameterDefn) pBase, ((ScalarParameterDefn) pBase).getDefaultValueExpr());
				}
				else if (pBase instanceof ParameterGroupDefn)
				{
					Iterator iter2 = ((ParameterGroupDefn) pBase).getContents().iterator();
					while (iter2.hasNext())
					{
							IParameterDefnBase p = (IParameterDefnBase) iter2.next();
							if (p instanceof ScalarParameterDefn) 
							{
								setScalarParameterValue((ScalarParameterDefn) p,((ScalarParameterDefn) p).getDefaultValueExpr() );
							}
					}
				}
			}
		}
	}
	
	/**
	 * @param p
	 * @param strValue
	 */
	private void setScalarParameterValue(ScalarParameterDefn p, String strValue)
	{
		Object value =  getValue(strValue, p.getDataType());
		if(value != null)
			defaultValues.put(p.getName(), value);
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
		this.parameterValues = params;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#getParameterValues()
	 */
	public HashMap getParameterValues()
	{
		return parameterValues;
	}
}