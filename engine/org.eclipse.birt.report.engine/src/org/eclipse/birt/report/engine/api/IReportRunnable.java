/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * A runnable report design (i.e., not modifiable) that can be run in the BIRT
 * engine
 */
public interface IReportRunnable extends IRunnable {

	public static final String TITLE = "title"; //$NON-NLS-1$
	public static final String AUTHOR = "author"; //$NON-NLS-1$
	public static final String DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String COMMENTS = "comments"; //$NON-NLS-1$
	public static final String CREATEDBY = "createdBy"; //$NON-NLS-1$
	public static final String HELP_GUIDE = "helpGuide"; //$NON-NLS-1$
	public static final String BASE_PROP = "base"; //$NON-NLS-1$
	public static final String UNITS = "units"; // $NON-NLS-1$
	public static final String REFRESH_RATE = "refreshRate"; //$NON-NLS-1$

	/**
	 * returns an image stored in a report design file, or null if the image name
	 * does not exist
	 * 
	 * @param name the image name for the embedded image
	 */
	public abstract IImage getImage(String name);

	/**
	 * returns the property value for things like report description, title, etc.
	 * 
	 * @param propertyName the name of the property
	 * @return the property value for things like report description, title, etc.
	 */
	public abstract Object getProperty(String propertyName);

	/**
	 * returns the property value defined on a components in a report design. For
	 * example, getProperty("/dataSets/dsName", "url") will return the url value for
	 * a data set with name dsName.
	 * 
	 * @param propertyName the name of the property
	 * @param path         a simplified XPath that allows access to properties for
	 *                     components in a report design. Only downward path is
	 *                     allowed, i.e., no .. in the path.
	 * @return the property value for things like report description, title, etc.
	 * 
	 * @deprecated this method is never used ,suggest use Model API to access all
	 *             properties on elements.
	 */
	public abstract Object getProperty(String path, String propertyName);

	/**
	 * returns the design element handle that design engine creates when opening the
	 * report
	 * 
	 * @return the design element handle that design engine creates when opening the
	 *         report
	 */
	public abstract DesignElementHandle getDesignHandle();

	/**
	 * sets a new report design handle before rendering the report
	 * 
	 * @param handle a new report design handle
	 */
	public abstract void setDesignHandle(DesignElementHandle handle);

	/**
	 * returns report name
	 * 
	 * @return the name of the report
	 */
	public abstract String getReportName();

	/**
	 * returns test configuration for the report
	 * 
	 * @return test configurations for the report
	 */
	public abstract HashMap getTestConfig();

	/**
	 * returns the report engine
	 * 
	 * @return the report engine
	 */
	public abstract IReportEngine getReportEngine();

	/**
	 * Returns the report design
	 * 
	 * @return the report design
	 */

	public abstract IReportDesign getDesignInstance();
}
