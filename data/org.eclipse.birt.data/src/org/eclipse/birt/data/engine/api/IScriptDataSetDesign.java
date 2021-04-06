/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.api;

/**
 * Describes the static design of a scripted Data Set. The data set is to be
 * retrieved by user-defined scripts defined in this specialized interface.
 */
public interface IScriptDataSetDesign extends IBaseDataSetDesign, ICacheable {
	/**
	 * Gets the Open script for opening the data set.
	 * 
	 * @return The Open script. Null if none is defined.
	 */
	public abstract String getOpenScript();

	/**
	 * Gets the Fetch script that creates, populates and returns an object of type
	 * DataRow.
	 * 
	 * @return The Fetch script.
	 */
	public abstract String getFetchScript();

	/**
	 * Gets the Close script for closing the data set.
	 * 
	 * @return The Close script. Null if none is defined.
	 */
	public abstract String getCloseScript();

	/**
	 * Gets the Describe script for describing the data set's result columns
	 * metadata.
	 * 
	 * @return The Describe script. Null if none is defined.
	 */
	public abstract String getDescribeScript();

}
