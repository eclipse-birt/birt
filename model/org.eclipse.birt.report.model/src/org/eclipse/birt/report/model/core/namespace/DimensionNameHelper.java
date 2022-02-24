/*******************************************************************************
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
 *******************************************************************************/

package org.eclipse.birt.report.model.core.namespace;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.metadata.ElementDefn;

/**
 * 
 */
public class DimensionNameHelper extends AbstractNameHelper {

	protected Dimension dimension = null;

	/**
	 * 
	 * @param dimension
	 */
	public DimensionNameHelper(Dimension dimension) {
		super();
		this.dimension = dimension;
	}

	protected INameContext createNameContext(String name) {
		return NameContextFactory.createDimensionNameContext(dimension, Dimension.LEVEL_NAME_SPACE);
	}

	/**
	 * Adds a element to the cached name space.
	 * 
	 * @param element
	 */
	public void addElement(DesignElement element) {
		if (element == null || element.getName() == null)
			return;
		ElementDefn defn = (ElementDefn) element.getDefn();
		if (!dimension.getDefn().isKindOf(defn.getNameConfig().getNameContainer()))
			return;
		String id = defn.getNameSpaceID();
		NameSpace ns = getCachedNameSpace(id);
		if (!ns.contains(element.getName()))
			ns.insert(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameHelper#addContentName
	 * (int, java.lang.String)
	 */
	public void addContentName(String id, String name) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameHelper#getElement()
	 */
	public DesignElement getElement() {
		return dimension;
	}
}
