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

package org.eclipse.birt.data.engine.api;

import java.util.Map;

/**
 * Describes the static design of a generic ODA (Open Data AccesS) Data Set. The
 * data set is to be retrieved by a query and/or properties defined in this
 * specialized interface.
 */
public interface IOdaDataSetDesign extends IBaseDataSetDesign, ICacheable {
	/**
	 * Gets the static query text of the data set. A data set might not have any
	 * query defined at all. If a query text is defined, it could be either static
	 * or dynamic.
	 * 
	 * @return The static query text for execution. Null if no static query is
	 *         defined.
	 */
	public abstract String getQueryText();

	/**
	 * Gets the unique id that identifies the type of the data set, assigned by the
	 * extension providing the implementation of this data set. The id is required
	 * if the ODA driver supports more than one types of data set.
	 * 
	 * @return The id fo the type of data set type as referenced by an ODA driver.
	 *         Null if none is defined.
	 */
	public abstract String getExtensionID();

	/**
	 * Gets the name of the primary result set retrieved by the query. This is
	 * required for a query that returns multiple result sets, each of which can be
	 * identified by name.
	 * 
	 * @return The name of the primary result set. Null if none is defined.
	 */
	public abstract String getPrimaryResultSetName();

	/**
	 * Gets the public data set property, in the form of a ( name[String],
	 * value[String] ) map
	 * 
	 * @return Public properties as a Map of String-String pairs. Null if none is
	 *         defined.
	 */
	public abstract Map getPublicProperties();

	/**
	 * Gets the private data set property, in the form of a ( name[String],
	 * value[String] ) map
	 * 
	 * @return private properties as a Map of String-String pairs. Null if none is
	 *         defined.
	 */
	public abstract Map getPrivateProperties();

	/**
	 * Get the number of the primary result set retrieved by the query. This is
	 * required b the query that returns multiple result sets, each of which can be
	 * identified by a number.
	 * 
	 * @return
	 */
	public abstract int getPrimaryResultSetNumber();

}
