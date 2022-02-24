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
import org.eclipse.birt.report.model.elements.olap.Dimension;

/**
 * 
 */
public class DimensionNameContext extends ElementNameContext {

	protected Dimension dimension = null;

	/**
	 * 
	 * @param dimension
	 */
	public DimensionNameContext(Dimension dimension) {
		super();
		this.dimension = dimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getElement()
	 */
	public DesignElement getElement() {
		return dimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getNameSpaceID
	 * ()
	 */
	public String getNameSpaceID() {
		return Dimension.LEVEL_NAME_SPACE;
	}

}
