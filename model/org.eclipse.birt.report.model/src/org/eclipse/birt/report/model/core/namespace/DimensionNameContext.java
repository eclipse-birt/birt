/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
