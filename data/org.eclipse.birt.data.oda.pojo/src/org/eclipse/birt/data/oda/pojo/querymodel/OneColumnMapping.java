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

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A special <code>IColumnsMapping</code> , the mapped result of which is only
 * one column. <br>
 * the type of its source result is generally basic, such as integer, double,
 * string, etc. A counterpart of <code>ElEMENT_COLUMN_MAPPING</code> element in
 * POJO query text.
 */
public class OneColumnMapping implements IColumnsMapping {

	private IMappingSource source;
	private Column column;

	/**
	 * @param source
	 * @param column
	 * @throws NullPointerException if <code>source</code> or <code>column</code> is
	 *                              null
	 */
	public OneColumnMapping(IMappingSource source, Column column) {
		if (source == null) {
			throw new NullPointerException("source is null"); //$NON-NLS-1$
		}
		if (column == null) {
			throw new NullPointerException("column is null"); //$NON-NLS-1$
		}
		this.source = source;
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#getSource()
	 */
	public IMappingSource getSource() {
		return source;
	}

	public Column getMappedColumn() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#getReferenceNode(
	 * org.eclipse.birt.data.oda.pojo.querymodel.RelayReferenceNode)
	 */
	public ReferenceNode createReferenceNode(RelayReferenceNode parent) {
		return new ColumnReferenceNode(parent, source, getMappedColumn());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#createElement(org.
	 * w3c.dom.Document)
	 */
	public Element createElement(Document doc) {
		Element newElement = doc.createElement(Constants.ElEMENT_COLUMNMAPPING);
		newElement.setAttribute(Constants.ATTR_COLUMN_NAME, getMappedColumn().getName());
		newElement.setAttribute(Constants.ATTR_COLUMN_ODADATATYPE, getMappedColumn().getOdaType());
		newElement.setAttribute(Constants.ATTR_COLUMN_INDEX, String.valueOf(getMappedColumn().getIndex()));
		newElement.appendChild(getSource().createElement(doc));
		return newElement;
	}

}
