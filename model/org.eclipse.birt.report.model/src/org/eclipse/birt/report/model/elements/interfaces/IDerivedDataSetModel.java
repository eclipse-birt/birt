/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * Interface to define the constants for property names in derive data set.
 * 
 */
public interface IDerivedDataSetModel {

	/**
	 * Name of the property that defines the list of the data sets that this derived
	 * data set includes.
	 */
	String INPUT_DATA_SETS_PROP = "inputDataSets"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the query text for this derived data set.
	 */
	String QUERY_TEXT_PROP = "queryText"; //$NON-NLS-1$
}
