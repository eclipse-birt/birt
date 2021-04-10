/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.querymodel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * A special <code>IColumnsMapping</code> that maps a class into multiple
 * columns. <br>
 * the type of its source result is generally another POJO class. A counterpart
 * of <code>ELEMENT_CLASS_COLUMN_MAPPINGS</code> element in POJO query text.
 */
public class ClassColumnMappings implements IColumnsMapping {

	private IMappingSource source;

	// internal IColumnsMapping list
	private List<IColumnsMapping> mappings = new ArrayList<IColumnsMapping>();

	public ClassColumnMappings(IMappingSource source) {
		if (source == null) {
			throw new NullPointerException("source is null"); //$NON-NLS-1$
		}
		this.source = source;
	}

	/**
	 * 
	 * @param mapping
	 * @throws NullPointerException if <code>mapping</code> is null.
	 */
	public void addColumnsMapping(IColumnsMapping mapping) {
		if (mapping == null) {
			throw new NullPointerException("mapping is null"); //$NON-NLS-1$
		}
		mappings.add(mapping);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#getSource()
	 */
	public IMappingSource getSource() {
		return source;
	}

	/**
	 * @return internal IColumnsMapping list
	 */
	public IColumnsMapping[] getColumnsMappings() {
		return mappings.toArray(new IColumnsMapping[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#getReferenceNode(
	 * org.eclipse.birt.data.oda.pojo.querymodel.RelayReferenceNode)
	 */
	public ReferenceNode createReferenceNode(RelayReferenceNode parent) {
		RelayReferenceNode result = new RelayReferenceNode(parent, getSource());
		for (IColumnsMapping mapping : mappings) {
			mapping.createReferenceNode(result);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping#createElement(org.
	 * w3c.dom.Document)
	 */
	public Element createElement(Document doc) {
		Element newElement = doc.createElement(Constants.ELEMENT_CLASSCOLUMNMAPPINGS);
		for (IColumnsMapping cm : getColumnsMappings()) {
			newElement.appendChild(cm.createElement(doc));
		}
		newElement.appendChild(getSource().createElement(doc));
		return newElement;
	}

}
