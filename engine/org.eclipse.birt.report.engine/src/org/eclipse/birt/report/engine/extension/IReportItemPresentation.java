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

import java.util.HashMap;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Represents the extended item presentation time extension. 
 *  
 * The calling sequence in presentation engine might work as follows:<p>
 * <li> Design engine creates a new instance of the extended item. This includes 
 * instantiation of the IPeer type.  
 * <li> Presentation engine, at execution time, detected that the element is an 
 * extended item. It dynamically creates an object with type IReportItemPresentation.
 * <li> The presentation engine calls initialize() to pass initialization parameters 
 * to the presentation peer. 
 * <li> If the extended item has serialized anything during generation time, restore
 * is called to allow the presentation peer to restore its states.
 * <li> Negotiate with the extension to know what the output format is. 
 * <li> Render the extended item
 * <li> Call finish() for cleanup.
 */

public interface IReportItemPresentation {
	public static String ITEM_BOUNDS 			= "bounds"; 	 //$NON-NLS-1$
	public static String RESOLUTION	 			= "dpi"; 		 //$NON-NLS-1$
	public static String SCALING_FACTOR 		= "scale";		 //$NON-NLS-1$
	public static String MODEL_OBJ				= "model";		 //$NON-NLS-1$
	public static String SUPPORTED_FILE_FORMATS	= "formats";		 //$NON-NLS-1$
	public static String OUTPUT_FORMAT			= "outputFormat";		//$NON-NLS-1$ 
	
    public static int OUTPUT_NONE = 0;     
    public static int OUTPUT_AS_IMAGE = 1;	// Only this format is supported for now
    public static int OUTPUT_AS_TEXT = 2;
    public static int OUTPUT_AS_HTML_TEXT = 3;
    public static int OUTPUT_AS_DRAWING = 4;
    public static int OUTPUT_AS_CUSTOM = 5;
    
    /**
     * Initializes the presentation peer before it is asked to perform any rendering.
     * 
     * @param parameters a collection of parameters that facilitate initialization of the
     * presentation peer. To support extension type OUTPUT_AS_IMAGE, the HashMap might contain 
     * the following parameters:
     * 	ITEM_BOUNDS		optional
     *  RESOLUTION		optional, but preferred. Otherwise, use implementer's default
     *  SCALING_FACTOR	optional, default is 1.0
     *  MODEL_OBJ		Required
     *  SUPPORTED_FILE_FORMATS	optional, coule be a number of formats separates by semi-colon 
     *  TARGET_OUTPUT_FORMAT	Required
     * 
     */
    public void initialize(HashMap parameters) throws BirtException;
    
    /**
     * De-serializes the peer state that was serialized during generation time. 
     * This function is currently not supported.
     * 
     * @param instream input stream so that the peer can de-serialize its state
     */
    public void restoreGenerationState(IReportItemSerializable genState);
    
    /**
     * @param format the output format for the request 
     * @param supportedTypes an array of supported output types that the engine 
     * can accormodate. This allows the extended item to choose the best type of output.
     * @param mimeType an out parameter that returns the MIME type of the output 
     * @return output type, for now OUTPUT_AS_IMAGE only
     */
    public int getOutputType(String format, String mimeType);
    
    /**
     * process the extended item in presentation environment. 
     * 
     * @return the returned value could be different depending on the type of the output.
     * For image, returns an input stream or byte array that the engine could retrieve data from;
     * For text and html text, a Java String; For drawing, returns ??; For custom format,
     * engine does not care and passes the Object to emitter.   
     */
    public Object process( ) throws BirtException;

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
     * Performs clean up
     */
    public void finish();
}

