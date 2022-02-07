/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.tests.matrix;

import org.eclipse.birt.report.model.api.FactoryPropertyHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.extension.IStyleDeclaration;

public class MatrixStyle implements IStyleDeclaration {

	StyleHandle style = null;

	/**
	 * 
	 * @param name
	 */

	public MatrixStyle(StyleHandle style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getName()
	 */
	public String getName() {
		return style.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IStyleDeclaration#getProperty(
	 * java.lang.String)
	 */
	public Object getProperty(String name) {
		FactoryPropertyHandle factoryPropHandle = style.getFactoryPropertyHandle(name);
		if (factoryPropHandle == null)
			return null;

		return factoryPropHandle.getValue();
	}

}
