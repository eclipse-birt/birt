/*
 *************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.Collection;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * A type of IQuery that allows applications to obtain persistent instances from
 * an underlying data source, and supports further data transforms.
 * <p>
 * An IDataSourceQuery has 2 main elements: <br>
 * <ul>
 * <li>a query text and associated properties for execution, and
 * <li>data set hints for retrieving query results.
 * </ul>
 * <p>
 * Other optional elements are supported. <br>
 * Interface methods are provided to bind the optional elements prior to
 * execution. Optional elements include: <br>
 * <ul>
 * <li>result specification to project the fields that the query should return,
 * <li>input and output parameter hints,
 * </ul>
 */
public interface IDataSourceQuery extends IQuery {
	/**
	 * Gets the data source associated with this query instance.
	 * 
	 * @return The associated IDataSource instance
	 */
	// public IDataSource getDataSource();

	/**
	 * Gets the query text defined in this query, specified when this query was
	 * instantiated by IDataSource.newQuery.
	 * 
	 * @return The query text to be prepared and executed by the data source driver.
	 */
	// public String getQueryText();

	/**
	 * Gets the type of this query text.
	 * 
	 * @return The type of query text.
	 */
	// public String getQueryType();

	/**
	 * Specifies one or more field definition hints to the ODI Executor for data
	 * access. One is not required to provide hints for every expected result field.
	 * Any field hints provided will be applied only if the underlying data provider
	 * is not able to provide runtime metadata.
	 * 
	 * @param columnDefns One or more field/column hints as a collection of
	 *                    ColumnHint objects.
	 */
	public void setResultHints(Collection columnDefns);

	/**
	 * Return a collection of field definition hints associated with this query.
	 * 
	 * @return A collection of one or more ResultFieldHint objects provided to
	 *         assist in data access.
	 */
	// public Collection getResultHints();

	/**
	 * Specifies the result specification to project what the query should return.
	 * The result specification is made up of one or more result field names.
	 * <p>
	 * If setResultProjection() is not called, or is called with a null result
	 * specification, all result data elements retrieved by the query text are bound
	 * to the corresponding fields in the result objects. Otherwise, only those
	 * fields specified in setResultProjection() would be bound. Any remaining
	 * fields defined in the result hints would not be included in the result
	 * objects returned by the execute method.
	 * 
	 * @param fieldNames An ordered list of result field/column names.
	 * @throws DataException if given fieldNames are invalid.
	 */
	public void setResultProjection(String[] fieldNames) throws DataException;

	/**
	 * Returns the result specification to project the fields to return in a query
	 * result object.
	 * 
	 * @return An array of projected field names. Null if none is defined.
	 */
	// public String[] getResultProjection();

	/**
	 * Sets a collection of org.eclipse.birt.data.engine.odaconsumer.ParameterHint
	 * object to provide parameter hints, and bound input parameter values
	 */
	public void setParameterHints(Collection parameterHints);

	/**
	 * Return a collection of input/output parameter hints associated with this
	 * query.
	 * 
	 * @return A collection of one or more IParameterDefinition objects to assist in
	 *         data access.
	 */
	// public Collection getParameterHints();

	/**
	 * Adds the specified value to the named property. Multiple calls using the same
	 * property name may be allowed. Its processing is implementation-dependent on
	 * the underlying data source. Note: This should be called before prepare().
	 * 
	 * @param name  The name of property.
	 * @param value The value to add to the named property.
	 */
	public void addProperty(String name, String value) throws DataException;

	/**
	 * Dynamically declares an user-defined custom, modifiable field in the result
	 * set's IResultObject definition specifying its name and type. <br>
	 * Must be declared before preparing the query. <br>
	 * A declared custom field is automatically included in the projected result of
	 * an IResultObject instance. It contains null value, until an ODI consumer
	 * directly assigns a value to it.
	 * 
	 * @param fieldName The name of the custom field; must be unique in an result
	 *                  instance.
	 * @param dataType  The data type of the custom field; must be one defined in
	 *                  birt.data.engine.api.DataType. Its value can be ANY_TYPE if
	 *                  it is not known at this time.
	 * @throws DataException if the field cannot be added.
	 */
	public void declareCustomField(String fieldName, int dataType) throws DataException;

	/**
	 * Prepares the query instance for subsequent execution. This method requires
	 * the Query instance to validate any bound elements, such as the Result
	 * Projection against the underlying result metadata, and report any
	 * incompatibilities by throwing a chained OdiException.
	 * 
	 * @throws DataException if query validation error(s) occur.
	 */
	public IPreparedDSQuery prepare() throws DataException;

	/**
	 * Class to hold hints for a result set field. For use with setResultHints
	 * method.
	 */
	public static final class ResultFieldHint {
		public static final int UNKNOWN_NATIVE_DATA_TYPE = 0;

		private String alias;
		private String name;
		private int position = -1;
		private int dataType = -1;
		private int nativeDataType = UNKNOWN_NATIVE_DATA_TYPE;

		/**
		 * Constructs a hint for a column with provided name
		 */
		public ResultFieldHint(String name) {
			this.name = name;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getAlias() {
			return alias;
		}

		public void setDataType(int dataType) {
			this.dataType = dataType;
		}

		public int getDataType() {
			return dataType;
		}

		public void setNativeDataType(int nativeTypeCode) {
			nativeDataType = nativeTypeCode;
		}

		public int getNativeDataType() {
			return nativeDataType;
		}

		public String getName() {
			return name;
		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}
	}
}
