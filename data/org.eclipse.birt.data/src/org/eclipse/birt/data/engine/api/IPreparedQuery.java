/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.Collection;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * A prepared data engine query ready for execution. An instance of this class
 * is compiled from the static definition of an
 * {@link org.eclipse.birt.data.engine.api.IQueryDefinition} object.
 */
public interface IPreparedQuery extends IBasePreparedQuery {
	/**
	 * Returns the same {@link org.eclipse.birt.data.engine.api.IQueryDefinition}
	 * used to prepare this instance, without any changes.
	 */
	public IQueryDefinition getReportQueryDefn();

	/**
	 * Returns a collection of
	 * {@link org.eclipse.birt.data.engine.api.IParameterMetaData} that each
	 * describes the meta-data of a parameter defined in this query. The sequence in
	 * the collection has no implied meaning. A parameter's position value, if
	 * defined, is specified in a <code>IParameterMetaData</code>. Each parameter
	 * can be of input and/or output mode.
	 * 
	 * @return The collection of <code>IParameterMetaData</code> to describe the
	 *         meta-data of all parameters defined in this prepared query. Returns
	 *         null if no parameters are defined, or if no parameter metadata is
	 *         available.
	 */
	public Collection getParameterMetaData() throws BirtException;

	/**
	 * Executes the prepared execution plan. This returns a
	 * {@link org.eclipse.birt.data.engine.api.IQueryResults} object which can be
	 * used to obtain the result set metadata and the result iterator.
	 * <p>
	 * The caller should create a separate Javascript scope, which uses the data
	 * engine's shared scope as its prototype, and pass that scope as a parameter to
	 * this method. The Data Engine is responsible for setting up necessary
	 * Javascript objects to facilitate evaluation of data related expressions
	 * (e.g., those that uses the Javascript "row" object).
	 * 
	 * @param queryScope The Javascript scope for evaluating query's script
	 *                   expressions. This is expected to be a top-level scope with
	 *                   the Data Engine's global scope at its top prototype chain.
	 */
	public IQueryResults execute(Scriptable queryScope) throws BirtException;

	/**
	 * Executes the prepared execution plan as an inner query that appears within
	 * the scope of another query. The outer query must have been prepared and
	 * executed, and its results given as a parameter to this method.
	 * 
	 * @param outerResults <code>IQueryResults</code> for the executed outer query
	 * @param queryScope   Javascript defined for this runtime instance of report
	 *                     query.
	 * @return The <code>IQueryResults</code> object for this report query
	 */
	public IQueryResults execute(IQueryResults outerResults, Scriptable queryScope) throws BirtException;

	/**
	 * Executes the prepared execution plan as an inner query that appears within
	 * the scope of another query. The outer query must have been prepared and
	 * executed, and its results given as a parameter to this method.
	 * 
	 * @param outerResults
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	public IQueryResults execute(IBaseQueryResults outerResults, Scriptable scope) throws DataException;

}
