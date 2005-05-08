/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
import java.util.Locale;

/**
 * 
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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFormat(java.lang.String)
	 */
	public abstract void setOutputFormat(String format);

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputStream(java.io.OutputStream)
	 */
	public abstract void setOutputStream(OutputStream ostream);

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setLocale(java.util.Locale)
	 */
	public abstract void setLocale(Locale locale);

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFileName(java.lang.String)
	 */
	public abstract void setOutputFileName(String outputFileName);
}