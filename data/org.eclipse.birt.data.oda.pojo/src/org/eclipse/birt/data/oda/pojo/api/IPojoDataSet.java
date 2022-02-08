/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.api;

import java.util.Map;

import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A POJO data set Customer implementation class must have a public constructor
 * without argument
 */
public interface IPojoDataSet {
	/**
	 * Open this POJO data set.
	 * <p>
	 * Generally for initialization work. Called before POJO ODA driver begins to
	 * fetch POJO objects
	 * 
	 * @param appContext:         application context used by this POJO data set
	 * @param dataSetParamValues: <name, value> parameter pairs of this data set. An
	 *                            empty map will be passed in if this data set is
	 *                            with no parameter.
	 * @throws OdaException
	 */
	void open(Object appContext, Map<String, Object> dataSetParamValues) throws OdaException;

	/**
	 * Fetch the next POJO object. Each POJO object returned should match
	 * Method/Field-to-Column Mappings defined in query text.
	 * 
	 * @return the next POJO object. null means no more POJO object available
	 */
	Object next() throws OdaException;

	/**
	 * Close this POJO data set
	 * <p>
	 * Generally for resource release work. Called after POJO ODA driver finishes
	 * fetching POJO objects
	 * 
	 * @throws OdaException
	 */
	void close() throws OdaException;
}
