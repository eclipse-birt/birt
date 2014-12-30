/*******************************************************************************
 * Copyright (c) 2005,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script;

import java.io.Serializable;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;

/**
 * An interface used to share information between the event methods in
 * scripting. Gives access to report parameters and configuration values. Also
 * provides a way for the report developer to register and retrieve custom
 * properties.
 */
public interface IReportContext
{
	static final String PAGE_VAR_PAGE_LABEL = "pageLabel";	

	/**
	 * return the report runnable used to create/render this report
	 * 
	 * @return
	 */
	IReportRunnable getReportRunnable( );
	/**
	 * 
	 * @param name
	 * @return
	 */
	Object getParameterValue( String name );

	/**
	 * 
	 * @param name
	 * @param value
	 */
	void setParameterValue( String name, Object value );

	/**
	 * 
	 * @param name
	 * @return
	 */
	Object getParameterDisplayText( String name );

	/**
	 * 
	 * @param name
	 * @param value
	 */
	void setParameterDisplayText( String name, String value );

	/**
	 * 
	 * @return
	 */
	Locale getLocale( );
	
	/**
	 * Get time zone informations.
	 * @return
	 */
	TimeZone getTimeZone( );

	/**
	 * 
	 * @return
	 */
	String getOutputFormat( );

	/**
	 * get the render options used to render the 
	 * report.
	 * @return
	 */
	IRenderOption getRenderOption( );
	
	/**
	 * Get the application context
	 */
	Map getAppContext( );

	/**
	 * Get the http servlet request object
	 * 
	 */
	Object getHttpServletRequest( );

	/**
	 * Add the object to runtime scope. This object can only be retrieved in the
	 * same phase, i.e. it is not persisted between generation and presentation.
	 */
	void setGlobalVariable( String name, Object obj );

	/**
	 * Remove an object from runtime scope.
	 */
	void deleteGlobalVariable( String name );

	/**
	 * Retireve an object from runtime scope.
	 */
	Object getGlobalVariable( String name );

	/**
	 * Add the object to report document scope. This object can be retrieved
	 * later. It is persisted between phases, i.e. between generation and
	 * presentation.
	 */
	void setPersistentGlobalVariable( String name, Serializable obj );

	/**
	 * Remove an object from report document scope.
	 */
	void deletePersistentGlobalVariable( String name );

	/**
	 * Retireve an object from report document scope.
	 */
	Object getPersistentGlobalVariable( String name );

	Object getPageVariable( String name );

	void setPageVariable( String name, Object value );

	/**
	 * Finds user-defined messages for the current thread's locale.
	 * 
	 * @param key
	 *            resource key of the user-defined message.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>null</code> if resoueceKey is blank.
	 */
	String getMessage( String key );

	/**
	 * Finds user-defined messages for the given locale.
	 * <p>
	 * First we look up in the report itself, then look into the referenced
	 * message file. Each search uses a reduced form of Java locale-driven
	 * search algorithm: Language&Country, language, default.
	 * 
	 * @param key
	 *            resource key of the user defined message.
	 * @param locale
	 *            locale of message, if the input <code>locale</code> is
	 *            <code>null</code>, the locale for the current thread will
	 *            be used instead.
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>null</code> if resoueceKey is blank.
	 */
	String getMessage( String key, Locale locale );

	/**
	 * Finds user-defined messages for the current thread's locale using parameters
	 * 
	 * @param key
	 *            resource key of the user-defined message.
	 * @param params
	 *            string arguments used to format error messages
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>null</code> if resoueceKey is blank.
	 */
	String getMessage( String key, Object[] params );

	/**
	 * Finds user-defined messages for the given locale using parameters
	 * <p>
	 * First we look up in the report itself, then look into the referenced
	 * message file. Each search uses a reduced form of Java locale-driven
	 * search algorithm: Language&Country, language, default.
	 * 
	 * @param key
	 *            resource key of the user defined message.
	 * @param locale
	 *            locale of message, if the input <code>locale</code> is
	 *            <code>null</code>, the locale for the current thread will
	 *            be used instead.
	 * @param params
	 *            string arguments used to format error messages
	 * @return the corresponding locale-dependent messages. Return
	 *         <code>null</code> if resoueceKey is blank.
	 */
	String getMessage( String key, Locale locale, Object[] params );
	
	/**
	 * Get the type of the current task.
	 * @return task type including:
	 * <li><b>0</b> for GetParameterDefinition Task</li>
	 * <li><b>1</b> for Run Task</li>
	 * <li><b>2</b> for Render Task</li>
	 * <li><b>3</b> for Run and Render Task</li>
	 * <li><b>4</b> for DataExtraction Task</li>
	 * <li><b>-1</b> default value for unknown task</li>
	 */
	int getTaskType();
	
	/**
	 * get the report design handle.
	 * @return report design handle
	 */
	ReportDesignHandle getDesignHandle( );
	
	/**
	 * get the URL for the resource.
	 * 
	 * The url can only be used in the server side.
	 * 
	 * @param resourceName
	 *            resource name
	 * @return resource URL
	 */
	URL getResource( String resourceName );

	/**
	 * get the render URL for a resource.
	 * 
	 * @param resourceName
	 *            resource name
	 * @return the URL which can be used in the client side.
	 */
	String getResourceRenderURL( String resourceName );
	
	/**
	 * evaluate the script with default script language.
	 * 
	 * @param script
	 * @return
	 * @throws BirtException
	 */
	Object evaluate( String script ) throws BirtException;

	/**
	 * evaluate the script with specified script language.
	 * 
	 * @param language
	 * @param script
	 * @return
	 * @throws BirtException
	 */
	Object evaluate( String language, String script ) throws BirtException;

	/**
	 * Evaluate the script.
	 * 
	 * @param script
	 * @return
	 * @throws BirtException
	 */
	Object evaluate( Expression script ) throws BirtException;

	/**
	 * get the application classLoader of the current report context
	 * 
	 * @return application classLoader
	 */
	ClassLoader getApplicationClassLoader( );

	/**
	 * cancel the current engine task
	 */
	void cancel( );
	
	/**
	 * cancel the current engine task
	 */
	void cancel( String reason );
	
	/**
	 * check if the report document generation is finished. It should be used at render time.
	 * The default value is false
	 * @return
	 */
	boolean isReportDocumentFinished( );
}
