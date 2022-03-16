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

import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.core.NameSpace;

/**
 *
 */
abstract public class AbstractNameContext implements INameContext, IAccessControl {

	protected NameSpace namespace = null;

	/**
	 *
	 */
	public AbstractNameContext() {
		initNameSpace();
	}

	/**
	 * Creates a name space. Generally, we create a NameSpace. However, for special
	 * elements, we must override this method to create special name space.
	 */
	protected void initNameSpace() {
		this.namespace = new NameSpace();
	}

	/**
	 *
	 */
	@Override
	public NameSpace getNameSpace() {
		return this.namespace;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#canContain(
	 * java.lang.String)
	 */
	@Override
	public boolean canContain(String elementName) {
		return namespace.getElement(elementName) == null;
	}
}
