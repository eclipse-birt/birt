/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IAdvancedQuery;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;
import org.eclipse.datatools.connectivity.oda.spec.util.QuerySpecificationHelper;

/**
 * 
 * This class serves to provide the updated ParameterMetaData and
 * ResultSetMetaData information according to the specified updated query text
 * 
 */
class MetaDataRetriever {
	private IResultSetMetaData resultMeta;
	private IParameterMetaData paramMeta;
	private IQuery query;

	private static Logger logger = Logger.getLogger(MetaDataRetriever.class.getName());

	MetaDataRetriever(OdaConnectionProvider odaConnectionProvider, DataSetDesign dataSetDesign) {
		try {
			IConnection connection = odaConnectionProvider.openConnection();
			query = connection.newQuery(dataSetDesign.getOdaExtensionDataSetId());
			QuerySpecification querySpec = new QuerySpecificationHelper((String) null).createQuerySpecification();
			Properties properties = dataSetDesign.getPublicProperties();
			if (properties != null) {
				for (Property prop : properties.getProperties()) {
					querySpec.setProperty(prop.getName(), prop.getValue());
				}
			}
			try {
				query.setSpecification(querySpec);
			} catch (UnsupportedOperationException ue) {
				// This method is not supported by CallStatement.
			}
			query.prepare(dataSetDesign.getQueryText());

			try {
				paramMeta = query.getParameterMetaData();
			} catch (OdaException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
			if (!(query instanceof IAdvancedQuery)) {
				resultMeta = query.getMetaData();
			}

		} catch (OdaException e) {
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Get the ParameterMetaData object
	 * 
	 * @return IParameterMetaData
	 */
	IParameterMetaData getParameterMetaData() {
		return this.paramMeta;
	}

	/**
	 * Get the ResultSetMetaData object
	 * 
	 * @return IResultSetMetaData
	 */
	IResultSetMetaData getResultSetMetaData() {
		return this.resultMeta;
	}

	/**
	 * Release
	 */
	void close() {
		try {
			if (query != null) {
				query.close();
			}
		} catch (OdaException e) {
			// ignore it
		} finally {
			query = null;
		}
	}
}
