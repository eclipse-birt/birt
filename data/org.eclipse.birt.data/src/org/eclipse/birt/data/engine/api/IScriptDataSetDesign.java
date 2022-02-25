/*
 *************************************************************************
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
	String getOpenScript();

	/**
	 * Gets the Fetch script that creates, populates and returns an object of type
	 * DataRow.
	 *
	 * @return The Fetch script.
	 */
	String getFetchScript();

	/**
	 * Gets the Close script for closing the data set.
	 *
	 * @return The Close script. Null if none is defined.
	 */
	String getCloseScript();

	/**
	 * Gets the Describe script for describing the data set's result columns
	 * metadata.
	 *
	 * @return The Describe script. Null if none is defined.
	 */
	String getDescribeScript();

}
