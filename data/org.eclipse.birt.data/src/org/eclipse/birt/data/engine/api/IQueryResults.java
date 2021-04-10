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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * A handle used to retrieve a data engine query's results. A query results
 * could contain multiple result sets. This is intended to be used by both
 * Factory and Presentation Engines in BIRT, including later releases when a
 * report document persists. Beyond Release 1, this would include methods to
 * save and restore results in a persisted Report Document.
 */
public interface IQueryResults extends IBaseQueryResults {
	/**
	 * Returns the {@link org.eclipse.birt.data.engine.api.IPreparedQuery} that
	 * produced this instance.
	 */
	public IPreparedQuery getPreparedQuery();

	/**
	 * Returns the metadata of the first or current result set <br>
	 * This method provides the result metadata without having to first fetch the
	 * result data.
	 * <p>
	 * Returns Null if the metadata is not available before fetching from an
	 * <code>IResultIterator</code>, or if it is ambiguous on which result set to
	 * reference. In such case, one should obtain the result metadata from a
	 * specific <code>IResultIterator<code>.
	 * 
	 * @return The metadata of the first result set's detail row in this
	 *         <code>IQueryResults<code>. Null if not available or ambiguous on
	 *         which result set to reference.
	 * @throws DataException if error occurs in Data Engine
	 */
	public IResultMetaData getResultMetaData() throws BirtException;

	/**
	 * Returns the current result's iterator. Repeated call of this method without
	 * having advanced to the next result would return the same iterator at its
	 * current state.
	 * 
	 * @return The current result's iterator.
	 * @throws DataException if error occurs in Data Engine
	 */
	public IResultIterator getResultIterator() throws BirtException;

	/**
	 * Cancel the current operation.
	 */
	public void cancel();
}