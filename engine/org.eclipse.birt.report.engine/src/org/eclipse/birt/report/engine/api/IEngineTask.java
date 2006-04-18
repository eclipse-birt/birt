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

package org.eclipse.birt.report.engine.api;

import java.util.HashMap;
import java.util.Locale;
import com.ibm.icu.util.ULocale;
import java.util.Map;

/**
 * defines common features for an engine task. A task captures a set of operations that engine
 * performs to get a unit of work done.  
 */
public interface IEngineTask {
	/**
	 * sets the task locale
	 * @param locale the task locale
	 */
	public abstract void setLocale(Locale locale);

	/**
	 * sets the task locale
	 * @param locale the task locale
	 */
	public abstract void setLocale(ULocale locale);

	/**
	 * sets the task context
	 * @param context - task contexts in a map. The map contains name-value pairs
	 */
	public abstract void setAppContext(Map context);
	
	/**
	 * returns the locale for running the task
	 * @return the locale for running the task
	 */
	public abstract Locale getLocale();
	
	/**
	 * returns the locale for running the task
	 * @return the locale for running the task
	 */
	public abstract ULocale getULocale();
	
	/**
	 * returns the context objects for the task
	 * @return the task contexts
	 */
	public abstract Map getAppContext();
	
	/**
	 * returns the report engine object
	 * @return the engine object
	 */
	public abstract IReportEngine getEngine();
	
	/**
	 * defines an additional Java object that is exposed to BIRT scripting at a per-task level
	 * @param jsName the name that the object is referenced in JavaScript
	 * @param obj the Java object that is wrapped and scripted
	 */
	public void addScriptableJavaObject(String jsName, Object obj);
	
	 /**
     * returns an identifier for the task. The identifier can be used to identify the
     * task, especially when writing logs in a multi-threaded environment.  
     * @return an identifier for the task.
     */
    public abstract int getID();

	/**
	 * returns the runnable report design object
	 * @return the runnable report design object
	 */
	public abstract IReportRunnable getReportRunnable();
	
	/**
	 * set all parameter valuess
	 * @param params a hash map with all parameters
	 */
	public abstract void setParameterValues(Map params);
	
	/**
	 * sets one parameter value
	 * @param name parameter name
	 * @param value parameter value
	 */
	public abstract void setParameterValue(String name, Object value);
	
	/**
	 * returns the parameter name/value collection
	 * @return the parameter names/values in a hash map
	 */
	public abstract HashMap getParameterValues();
	
	/**
     * returns the value of a parameter.
     * @return the parameter value.
     */
	public abstract Object getParameterValue(String name);
	
	/**
	 * @return whether the parameter validation succeeds <br>
	 */
	public boolean validateParameters( );
	
	/**
	 * Cancels the task.
	 */
	public void cancel();

	/**
	 * close the task, relese any resources. 
	 */
	public void close();
}