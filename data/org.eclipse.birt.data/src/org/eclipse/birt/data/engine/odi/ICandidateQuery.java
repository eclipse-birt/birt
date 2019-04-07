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
 * A type of IQuery that supports data transforms
 * on a set of candidate result instances.
 * It does not retrieve persistent data from external
 * data sources.
 */
public interface ICandidateQuery extends IQuery
{
	/**
	 * Binds the given IResultIterator of result instances to this query.
	 * Such binding is valid only if the query does not already have
	 * any opened result iterator or data set.  
     * <p>
	 * Any data transforms specified on this query
	 * would be applied to the iterator's candidate result instances, 
	 * limited by the specified grouping level if greater than 0.  
	 * That is, the subset of candidate result instances applies to the 
	 * specified grouping level of the current
     * transformed group state in the associated IResultIterator. 
     * @param resultObjsIterator	An IResultIterator of result instances.
     * @param groupingLevel			The grouping level. 
     * 						A group level of 0 (default) applies to the 
     * 						entire result candidates associated to this query. 
     * @throws DataException	if given iterator is not in a valid state, 
     * 						or if given grouping level is invalid.
     */
    public void setCandidates( IResultIterator resultObjsIterator,
			int groupingLevel ) throws DataException;
    
    /**
	 * Binds the given custom data set to this query.
	 * Such binding is valid only if the query does not already have
	 * any opened result iterator or data set.  
     * <br>
	 * Any data transforms specified on this query
	 * would be applied to all the candidate result instances in the
	 * custom data set. 
     * @param customDataSet	Custom data set implementation.
     * @throws DataException
     */
    public void setCandidates( ICustomDataSet customDataSet )
			throws DataException;
    
    /**
     * Gets the metadata of the candidate result instances.
     * @return	The IResultClass instance that represents the
     * 			metadata of the query result instances.
     * 			Null if no candidates are specified yet.
     */
    public IResultClass getResultClass( ) throws DataException;
    
    /**
	 * Executes this query applying the specified transforms on the candidate
	 * result instances, and returns a secondary iterator of the result set.
	 * <p>
	 * If the candidate result instances are specified through an
	 * IResultIterator, its <b>current</b> group state is applied to identify
	 * the subset of candidate results for data transforms. <br>
	 * In other words, this query could be executed multiple times, each time
	 * applying to the current group of the result iterator.
	 * 
	 * @param eventHandler
	 * @param stopSign
	 * @return An IResultIterator of query result instances which the user can
	 *         iterate to get results.
	 * @throws DataException
	 *             if query execution error(s) occur.
	 */
	public IResultIterator execute( IEventHandler eventHandler )
			throws DataException;

    /**
     * Close the associated iterator and all resources of this query.
     * After this method, the associated iterator can no longer be used.
     */
    public void close();
}
