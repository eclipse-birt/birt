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

package org.eclipse.birt.data.engine.odi;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * A custom data set, whose implementation supports fetching its result objects,
 * one at a time. The operation of accessing the custom data set and its
 * associated data source is implementation specific. <br>
 * A custom data set would be supplied to an empty ICandidateQuery as candidate
 * result instances for further data transforms.
 * <p>
 * An ODI consumer would provide this implementation, e.g. for a Javascript
 * custom data set, and is responsible for instantiating the ICustomDataSet
 * object.
 */
public interface ICustomDataSet {
	/**
	 * Gets the metadata of the result objects that will be fetched by this custom
	 * data set. <br>
	 * All fetched result objects must be uniformed, i.e. defined with the same
	 * metadata.
	 * 
	 * @return The IResultClass instance that represents the metadata of the result
	 *         objects.
	 */
	public IResultClass getResultClass();

	/**
	 * Opens the custom data set. <br>
	 * Its operation is specific to individual implementation.
	 * 
	 * @throws DataException if open fails.
	 */
	public void open() throws DataException;

	/**
	 * Fetches the next result object in this custom data set. Null is returned if
	 * no more result object in the data set.
	 * 
	 * @return the next result object in this custom data set.
	 * @throws DataException if fetch data fails.
	 */
	public IResultObject fetch() throws DataException;

	/**
	 * Closes the custom data set. <br>
	 * Its operation is specific to individual implementation.
	 * 
	 * @throws DataException if close fails.
	 */
	public void close() throws DataException;
}
