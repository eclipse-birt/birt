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
package org.eclipse.birt.data.oda.pojo.querymodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.impl.Connection;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * A POJO Query
 */
public class PojoQuery {

	private String version;
	private String dataSetClass;
	private String appContextKey;

	private List<IColumnsMapping> mappings = new ArrayList<>();

	private ReferenceGraph rg;

	private QueryParameters qps;

	private Connection connection;

	public PojoQuery(String version, String dataSetClass, String appContextKey) {
		this.version = version;
		this.dataSetClass = dataSetClass;
		this.appContextKey = appContextKey;
		this.connection = null;
	}

	public String getVersion() {
		return version;
	}

	/**
	 * @return the dataSetClass
	 */
	public String getDataSetClass() {
		return dataSetClass;
	}

	/**
	 * @param dataSetClass the dataSetClass to set
	 */
	public void setDataSetClass(String dataSetClass) {
		this.dataSetClass = dataSetClass;
	}

	/**
	 * @return the appContextKey
	 */
	public String getAppContextKey() {
		return appContextKey;
	}

	/**
	 * @param appContextKey the appContextKey to set
	 */
	public void setAppContextKey(String appContextKey) {
		this.appContextKey = appContextKey;
	}

	/**
	 * @param mapping
	 * @throws NullPointerException if <code>mapping</code> is null
	 */
	public void addColumnsMapping(IColumnsMapping mapping) {
		if (mapping == null) {
			throw new NullPointerException("mapping is null"); //$NON-NLS-1$
		}
		mappings.add(mapping);
	}

	public IColumnsMapping[] getColumnsMappings() {
		return mappings.toArray(new IColumnsMapping[0]);
	}

	/**
	 * Should be called after all IColumnsMapping added
	 *
	 * @return
	 */
	public ReferenceGraph getReferenceGraph() {
		if (rg == null) {
			rg = ReferenceGraph.create(this);
		}
		return rg;
	}

	public void clearColumnMappings() {
		mappings.clear();
	}

	public QueryParameters getQueryParameters() throws OdaException {
		if (qps == null) {
			qps = QueryParameters.create(this);
		}
		return qps;
	}

	public void prepareParameterValues(Map<String, Object> inputValues, ClassLoader pojoClassLoader)
			throws OdaException {
		for (ReferenceNode rn : getReferenceGraph().getRoots()) {
			prepareParameterValues(rn, inputValues, pojoClassLoader);
		}
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public Connection getConnection() {
		return connection;
	}

	private static void prepareParameterValues(ReferenceNode rn, Map<String, Object> inputValues,
			ClassLoader pojoClassLoader) throws OdaException {
		rn.getReference().prepareParameterValues(inputValues, pojoClassLoader);
		if (rn instanceof RelayReferenceNode) {
			RelayReferenceNode rrn = (RelayReferenceNode) rn;
			for (ReferenceNode child : rrn.getChildren()) {
				prepareParameterValues(child, inputValues, pojoClassLoader);
			}
		}
	}
}
