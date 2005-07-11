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

package org.eclipse.birt.report.engine.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;


/**
 * Defines an engine task that could be executed, debugged (runs step by step),
 * inform caller for progress, etc. 
 */
public abstract class EngineTask implements IEngineTask 
{
	protected static Logger log = Logger.getLogger( EngineTask.class.getName( ) );
	
	protected static int id = 0;
	
	/**
	 * the context for running this task
	 */
	protected Object renderContext;
	
	/**
	 * a reference to the report engine
	 */
	protected ReportEngine engine;
	
	/**
	 * Comment for <code>locale</code>
	 */
	protected Locale locale = Locale.getDefault();
	
	/**
	 * the execution context
	 */
	protected ExecutionContext executionContext;
	
	/**
	 * task identifier. Could be used for logging
	 */
	protected int taskID;
	
	protected IReportRunnable runnable;

	
	/**
	 * @param engine reference to report engine 
	 * @param context a user-defined object that capsulates the context for running a task. 
	 * The context object is passed to callback functions (i.e., functions 
	 * in image handlers, action handlers, etc. ) that are written by those who embeds 
	 * engine in their applications 
	 */
	protected EngineTask(ReportEngine engine, IReportRunnable runnable)
	{	
		this.runnable = runnable;
		this.engine = engine;
		taskID = id++;
		
		executionContext = new ExecutionContext( engine, taskID );
		executionContext.setRunnable(runnable);
		executionContext.registerBeans(runnable.getTestConfig());
	}
	
	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale() {
		return locale;
	}
	
	/**
	 * sets the task locale
	 * 
	 * @param locale the task locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
		executionContext.setLocale(locale);
	}
	
	/**
	 * sets the task context
	 * 
	 * @param context the task context
	 */
	public void setContext(Object context) {
		this.renderContext = context;
	}
	
	/**
	 * returns the  object that encapsulates the context for running the task
	 * 
	 * @return Returns the context.
	 */
	public Object getContext() {
		return renderContext;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getEngine()
	 */
	public ReportEngine getEngine()
	{
		return engine;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#addScriptableJavaObject(java.lang.String, java.lang.Object)
	 */
	public void addScriptableJavaObject(String jsName, Object obj)
	{
		executionContext.registerBean(jsName, obj);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getID()
	 */
	public int getID()
	{
		return taskID;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable()
	{
		return this.runnable;
	}
	
	/**
	 * @param params a collection of parameter definitions
	 */
	protected HashMap evaluateDefaults(Collection params) 
	{
		HashMap values = new HashMap();
		if (params != null)
		{
			Iterator iter = params.iterator();
			while (iter.hasNext())
			{
				IParameterDefnBase pBase = (IParameterDefnBase) iter.next();
				if (pBase instanceof ScalarParameterDefn) 
				{
					Object val = evaluateDefault((ScalarParameterDefn) pBase, ((ScalarParameterDefn) pBase).getDefaultValueExpr());
					values.put(pBase.getName(), val);
				}
				else if (pBase instanceof ParameterGroupDefn)
				{
					executionContext.pushReportItem(((ParameterGroupDefn) pBase).getHandle());
					Iterator iter2 = ((ParameterGroupDefn) pBase).getContents().iterator();
					while (iter2.hasNext())
					{
							IParameterDefnBase p = (IParameterDefnBase) iter2.next();
							if (p instanceof ScalarParameterDefn) 
							{
								Object val = evaluateDefault((ScalarParameterDefn) p,((ScalarParameterDefn) p).getDefaultValueExpr() );
								values.put(pBase.getName(), val);
							}
					}
					
					executionContext.popReportItem();
				}
			}
		}
		return values;
	}

	/**
	 * @param p the scalar parameter
	 * @param expr the default value expression 
	 */
	private Object evaluateDefault(ScalarParameterDefn p, String expr)
	{
		executionContext.pushReportItem(p.getHandle());
		Object value = null;
		int type = p.getDataType();
		
		// evaluate the default value expression
		if (expr != null)
		{
			value = executionContext.evaluate(expr);
			if( value == null && expr != null && expr.length() > 0)
				value = expr;
			try
			{
				switch (type)
				{
					case IScalarParameterDefn.TYPE_BOOLEAN :
						value = DataTypeUtil.toBoolean(value);
						break;
					case IScalarParameterDefn.TYPE_DATE_TIME :
						value = DataTypeUtil.toDate(value);
						break;
					case IScalarParameterDefn.TYPE_DECIMAL :
						value = DataTypeUtil.toBigDecimal(value);
						break;
					case IScalarParameterDefn.TYPE_FLOAT :
						value = DataTypeUtil.toDouble(value);
						break;
					case IScalarParameterDefn.TYPE_STRING:
						value = DataTypeUtil.toString(value);
						break;
					default:
						value = null;
						break;
				}
	
			}
			catch (BirtException e)
			{
				log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				value = null;
			}
		}
		executionContext.popReportItem();
		return value;
	}
}
