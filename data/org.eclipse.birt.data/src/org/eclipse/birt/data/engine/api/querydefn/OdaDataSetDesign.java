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

package org.eclipse.birt.data.engine.api.querydefn;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IOdaDataSetDesign} interface.
 * <p>
 */
public class OdaDataSetDesign extends BaseDataSetDesign implements IOdaDataSetDesign {
	private String queryText;
	private String extensionID;
	private String primaryResultSetName;
	private Map publicProps;
	private Map privateProps;
	private int resultSetNumber = -1;

	private QuerySpecification combinedQuerySpec;

	/**
	 * Constructs an instance with the given name
	 */
	public OdaDataSetDesign(String name) {
		super(name);
	}

	/**
	 * Constructs an instance with the given name and data source name
	 */
	public OdaDataSetDesign(String name, String dataSourceName) {
		super(name, dataSourceName);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getQueryText()
	 */
	@Override
	public String getQueryText() {
		return queryText;
	}

	/**
	 * Specifies the static query text.
	 *
	 * @param queryText Static query text.
	 */
	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getExtensionID()
	 */
	@Override
	public String getExtensionID() {
		return extensionID;
	}

	/**
	 * Specifies the extension ID for this type of data set
	 *
	 * @param extensionID The extension id for this data set type as assigned by the
	 *                    ODA driver
	 */
	public void setExtensionID(String extensionID) {
		this.extensionID = extensionID;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getPrimaryResultSetName()
	 */
	@Override
	public String getPrimaryResultSetName() {
		return primaryResultSetName;
	}

	/**
	 * Specifies the name of the primary result set.
	 *
	 * @param resultSetName
	 */
	public void setPrimaryResultSetName(String resultSetName) {
		primaryResultSetName = resultSetName == null ? null
				: (resultSetName.trim().length() == 0 ? null : resultSetName);
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getPublicProperties()
	 */
	@Override
	public Map getPublicProperties() {
		if (publicProps == null) {
			publicProps = new HashMap();
		}
		return publicProps;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getPrivateProperties()
	 */
	@Override
	public Map getPrivateProperties() {
		if (privateProps == null) {
			privateProps = new HashMap();
		}
		return privateProps;
	}

	/**
	 * Adds a public connection property, in the form of a (Name, value) string
	 * pair.
	 */
	public void addPublicProperty(String name, String value) {
		addProperty(getPublicProperties(), name, value);
	}

	/**
	 * Adds a private connection property, in the form of a (Name, value) string
	 * pair.
	 */
	public void addPrivateProperty(String name, String value) {
		addProperty(getPrivateProperties(), name, value);
	}

	/**
	 * Add given value to the set of values for named property in the given
	 * properties map.
	 */
	protected void addProperty(Map properties, String name, String value) {
		properties.put(name, value);
	}

	/**
	 * Set the primary result set number.
	 */
	public void setPrimaryResultSetNumber(int number) {
		this.resultSetNumber = number;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IOdaDataSetDesign#getPrimaryResultSetNumber(
	 * )
	 */
	@Override
	public int getPrimaryResultSetNumber() {
		return this.resultSetNumber;
	}

	public void setCombinedQuerySpecification(QuerySpecification querySpec) {
		this.combinedQuerySpec = querySpec;
	}

	public QuerySpecification getCombinedQuerySpecification() {
		return this.combinedQuerySpec;
	}
}
