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

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * A runnable report design (i.e., not modifiable) that can be run in the BIRT engine
 */
public interface IReportRunnable {
	
	public final String TITLE_PROP = "title"; //$NON-NLS-1$
	/**
	 * Name of the "base" property.
	 */

	public static final String BASE_PROP = "base"; //$NON-NLS-1$
	/**
	 * returns an image stored in a report design file, or null if the image name does not exist
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
	 * returns the property value defined on a components in a report design. For example, 
	 * getProperty("/dataSets/dsName", "url") will return the url value for a data set with name
	 * dsName. 
	 * 
	 * @param propertyName the name of the property
	 * @param path a simplified XPath that allows access to properties for components in a report
	 * design. Only downward path is allowed, i.e., no .. in the path. 
	 * @return the property value for things like report description, title, etc.
	 */
	public abstract Object getProperty(String path, String propertyName);
	
	/**
	 * 
	 * returns the design element handle that design engine creates when opening the report
	 * 
	 * @return the design element handle that design engine creates when opening the report
	 */
	public abstract DesignElementHandle getDesignHandle();
	
	/**
	 * @return the name of the report
	 */
	public String getReportName();
	
	/**
	 * @return test configurations for the report
	 */
	public HashMap getConfigs();
	
}