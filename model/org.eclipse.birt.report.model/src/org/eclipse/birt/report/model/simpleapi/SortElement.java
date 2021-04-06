/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.ISortElement;
import org.eclipse.birt.report.model.elements.interfaces.ISortElementModel;

/**
 * 
 */

public class SortElement extends DesignElement implements ISortElement {

	/**
	 * Default constructor.
	 * 
	 * @param handle
	 */

	public SortElement(SortElementHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#getDirection()
	 */

	public String getDirection() {
		return ((SortElementHandle) handle).getDirection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#getKey()
	 */
	public String getKey() {
		return ((SortElementHandle) handle).getKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#setDirection
	 * (java.lang.String)
	 */

	public void setDirection(String direction) throws SemanticException {
		setProperty(ISortElementModel.DIRECTION_PROP, direction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#setKey(java.
	 * lang.String)
	 */

	public void setKey(String key) throws SemanticException {
		setProperty(ISortElementModel.KEY_PROP, key);
	}

}
