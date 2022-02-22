/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

	@Override
	public String getDirection() {
		return ((SortElementHandle) handle).getDirection();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#getKey()
	 */
	@Override
	public String getKey() {
		return ((SortElementHandle) handle).getKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#setDirection
	 * (java.lang.String)
	 */

	@Override
	public void setDirection(String direction) throws SemanticException {
		setProperty(ISortElementModel.DIRECTION_PROP, direction);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.ISortElement#setKey(java.
	 * lang.String)
	 */

	@Override
	public void setKey(String key) throws SemanticException {
		setProperty(ISortElementModel.KEY_PROP, key);
	}

}
