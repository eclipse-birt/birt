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
package org.eclipse.birt.report.engine.extension;

import java.io.OutputStream;
import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.report.engine.data.IDataEngine;

/**
 * Represents the extended item generation extension, which performs tasks such as:<p>
 * <ul> 
 * <li> Prepare report query
 * <li> process the extended item 
 * </ul>
 * 
 * <p>The calling sequence in factory might work as follows:<p>
 * <li> Design engine creates a new instance of the extended item. This includes an object
 * of the IReportItem type.  
 * <li> Factory detects that the element is an extended item. It dynamically creates the
 * IReportItemGeneration object.
 * <li> initialization method is called on the object. 
 * <li> Whan factory prepares report query, it calls getReportQuery to retrieve a report 
 *    query for the extended item. For chart, the report query is relevant; for other
 *    extended items where a report query does not make sense, simply return null.
 * <li> Factory calls IFactorypeer::process when it executes the item.
 * <li> If there is a need for serialization, call serialize.
 * <li> Call finish() for cleanup.
 */
public interface IReportItemGeneration {
	public static String ITEM_BOUNDS 				= "bounds"; 		// $NON-NLS-1$
	public static String RESOLUTION	 				= "dpi"; 			// $NON-NLS-1$
	public static String SCALING_FACTOR 			= "scale";			// $NON-NLS-1$
	public static String MODEL_OBJ					= "model";			// $NON-NLS-1$
	public static String GENERATION_STAGE			= "generationStage";		// $NON-NLS-1$
	
	public static String GENERATION_STAGE_PREPARATION		= "preparation";	// $NON-NLS-1$
	public static String GENERATION_STAGE_EXECUTION			= "execution";		// $NON-NLS-1$
	
    /**
     * Initializes the generation object before it processes the extended item. The 
     * hash table parameter allows new parameters to be added without changing this 
     * interface. If in the future more formats (i.e., extended item with formatted 
     * text) need to be supported, more parameters can be added. 
     * 
     * @param parameters a collection of parameters that facilitates initialization of the
     * factory object. To support extension type OUTPUT_AS_IMAGE, the HashMap might contain 
     * the following parameters:
     * 	ITEM_BOUNDS				optional
     *  RESOLUTION				optional, but preferred. Otherwise, use implementer's default
     *  SCALING_FACTOR			optional, default is 1.0
     *  MODEL_OBJ				Required
     *  GENERATION_STAGE		Required
     */
    public void initialize(HashMap parameters) throws BirtException;
    
    /**
     * @return an IReportQueryDefn object that the fatory can pass to DTE 
     */
    public IBaseQueryDefinition nextQuery(IBaseQueryDefinition parent) throws BirtException;   
    
	/**
	 * @param query a prepared query
	 */
	public void pushPreparedQuery(IBaseQueryDefinition query, IPreparedQuery preparedQuery);
	
    /**
     * @param dataEngine a data engine instance on which the extension developer calls <code>execute</code> method  
     */
    public void process(IDataEngine dataEngine) throws BirtException;

    /**
     * Get the size of the extended item. The size is a Dimension object. The width and height
     * can only be in absolute units (inch, mm, etc.) or pixel. It can not be a relative size
     * such as 150% or 1.2em. Notice that an extended item can obtain its design-time size 
     * information by querying DE. This function is needed because the actual size may not be
     * the same as the design-time size. 
     * 
     * @return the size of the extended item. Return null if the size does not matter or can 
     * not be determined.
     */
    public Size getSize();
    
    /**
     * Serializes the extended item to report document. This function is currently not supported.
     * Factory guarantees that the same data in the stream will be returned to the
     * presentation engine at presentation time.   
     * 
     * @param ostream output stream so that the peer can serialize its content
     * @return the number of bytes that the extended item has written 
     */
    public long serialize(OutputStream ostream) throws BirtException;
    
    /**
     * Performs clean up
     */
    public void finish();
}
