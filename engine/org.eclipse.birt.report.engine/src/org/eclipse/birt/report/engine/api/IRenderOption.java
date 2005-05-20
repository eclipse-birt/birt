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

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Defines render options for emitters
 */
public interface IRenderOption {
	
	/**
	 * returns the output format, i.e., html, pdf, etc. 
	 * 
	 * @return Returns the output format
	 */
	public abstract String getOutputFormat();

	/**
	 * returns the output settings
	 * 
	 * @return the output settings
	 */
	public abstract HashMap getOutputSetting();

	/**
	 * sets the output format
	 * 
	 * @param format the output format
	 */
	public abstract void setOutputFormat(String format);

	/**
	 * sets the stream for writing emitter output
	 * 
	 * @param ostream the output stream for writing emitter output
	 */
	public abstract void setOutputStream(OutputStream ostream);

	/**
	 * sets the output file name. The emitter uses the file for writing its ouput
	 * 
	 * @param outputFileName the output file name that emitter uses
	 */
	public abstract void setOutputFileName(String outputFileName);
}