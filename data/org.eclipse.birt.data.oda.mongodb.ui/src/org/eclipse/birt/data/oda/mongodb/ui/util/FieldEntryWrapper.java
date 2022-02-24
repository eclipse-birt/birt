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

package org.eclipse.birt.data.oda.mongodb.ui.util;

import org.eclipse.datatools.connectivity.oda.OdaException;

import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.MDbMetaData.DocumentsMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties;

public class FieldEntryWrapper {

	private String collectionName;
	private MDbMetaData metaData;
	private DocumentsMetaData dmd;
	private int searchLimit;
	private QueryProperties queryProps;

	public FieldEntryWrapper() {
		collectionName = ""; //$NON-NLS-1$
		metaData = null;
		dmd = null;
	}

	public FieldEntryWrapper(String collectionName, MDbMetaData metaData, int searchLimit, QueryProperties queryProps) {
		this.collectionName = collectionName;
		this.metaData = metaData;
		this.searchLimit = searchLimit;
		this.setQueryProps(queryProps);
	}

	/**
	 * @return the collectionName
	 */
	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * @param collectionName the collectionName to set
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public void setSearchLimit(int searchLimit) {
		this.searchLimit = searchLimit;
	}

	/**
	 * @return the metaData
	 */
	public MDbMetaData getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData the metaData to set
	 */
	public void setMetaData(MDbMetaData metaData) {
		this.metaData = metaData;
	}

	public DocumentsMetaData getAvailableFields() throws OdaException {
		if (dmd == null && this.metaData != null) {
			dmd = this.metaData.getAvailableFields(collectionName, searchLimit, queryProps);
		}
		return dmd;
	}

	public void updateAvailableFields() throws OdaException {
		if (metaData != null) {
			dmd = null; // reset metadata, if exists
			getAvailableFields();
		}
	}

	/**
	 * @return the queryProps
	 */
	public QueryProperties getQueryProps() {
		return queryProps;
	}

	/**
	 * @param queryProps the queryProps to set
	 */
	public void setQueryProps(QueryProperties queryProps) {
		this.queryProps = queryProps;
	}

}
