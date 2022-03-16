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

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.gef.requests.CreationFactory;

/**
 * Returns type of element wants created.
 */
public class ReportElementFactory implements CreationFactory {

	private Object elementName;
	private Object newObject;

	/**
	 * Constructor
	 *
	 * @param name
	 */
	public ReportElementFactory(Object name) {
		elementName = name;
	}

	/**
	 * Constructor
	 *
	 * @param name
	 */
	public ReportElementFactory(Object name, Object newObject) {
		this.elementName = name;
		this.newObject = newObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	@Override
	public Object getNewObject() {
		return newObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	@Override
	public Object getObjectType() {
		return elementName;
	}

}
