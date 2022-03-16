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

package org.eclipse.birt.data.engine.odi;

import java.util.Collection;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * A prepared IDataSourceQuery that is ready for input parameter bindings, and
 * subsequent execution. One can re-use the same prepared query, rebind
 * different input parameter values, and re-execute.
 */
public interface IPreparedDSQuery {
	/**
	 * Gets the metadata of the result objects expected to retrieve at query
	 * execution. Returns Null if the metadata is not available before execute, or
	 * if it is ambiguous on which result iterator to reference. In such case, one
	 * should proceed with execute, and gets the result class of a specific
	 * IResultIterator.
	 *
	 * @return The IResultClass instance that represents the expected metadata of
	 *         the query result objects.
	 */
	IResultClass getResultClass() throws DataException;

	/**
	 * Returns a collection of <code>IParameterMetaData</code> that describes the
	 * meta-data of each parameter defined in this prepared data source query. The
	 * sequence in the collection has no implied meaning. A parameter's position
	 * value, if defined, is specified in a IParameterMetaData. Each parameter can
	 * be of input and/or output mode.
	 *
	 * @return The collection of IParameterMetaData to describe the meta-data of all
	 *         parameters defined in this prepared query. Returns null if no
	 *         parameters are defined, or no parameter metadata is available.
	 */
	Collection getParameterMetaData() throws DataException;

	/**
	 * Retrieve utput parameters value.This method corresponds to the IAdvancedQuery
	 * of ODA, which might produce the output parameters when it is executed.
	 *
	 * @param index, parameter index, 1-based
	 * @return value of parameter
	 * @throws DataException
	 */
	Object getOutputParameterValue(int index) throws DataException;

	/**
	 * Retrieve utput parameters value.This method corresponds to the IAdvancedQuery
	 * of ODA, which might produce the output parameters when it is executed.
	 *
	 * @param name, parameter name
	 * @return value of parameter
	 * @throws DataException
	 */
	Object getOutputParameterValue(String name) throws DataException;

	/**
	 * Binds an input value to the query's input parameter. <br>
	 * These parameter values are used by the execute() method to execute the
	 * associated query in the data source.
	 *
	 * @param inputParamName The name of an input parameter. It can be either the
	 *                       name of the underlying data source parameter, or the
	 *                       name defined in IInputParamDefn, mapping to the
	 *                       position of the underlying data source parameter.
	 * @param paramValue     The input value to the parameter.
	 * @throws DataException if given input parameter name or value is invalid.
	 */
	/*
	 * public void setInputParamValue( String inputParamName, Object paramValue )
	 * throws DataException;
	 *
	 *//**
		 * Binds an input value to the query's input parameter. <br>
		 * These parameter values are used by the execute() method to execute the
		 * associated query in the data source.
		 *
		 * @param inputParamPos The Position of an input parameter.
		 * @param paramValue    The input value to the parameter.
		 * @throws DataException if given input parameter name or value is invalid.
		 *//*
			 * public void setInputParamValue( int inputParamPos, Object paramValue ) throws
			 * DataException;
			 */

	/**
	 * Executes this prepared query applying the specified transforms, and returns
	 * an iterator of the result set.
	 * <p>
	 *
	 * @param eventHandler
	 * @param stopSign
	 * @return An IResultIterator of query result instances which the user can
	 *         iterate to get results.
	 * @throws DataException if query execution error(s) occur.
	 */
	IResultIterator execute(IEventHandler eventHandler) throws DataException;

	/**
	 * Closes this query, if executed, and any associated resources, providing a
	 * hint that the consumer is done with this result, whose resources can be
	 * safely released as appropriate. This instance can no longer be executed after
	 * it is closed.
	 */
	void close();

	void setQuerySpecification(QuerySpecification spec);
}
