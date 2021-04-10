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
	public NameSpace getNameSpace() {
		return this.namespace;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#canContain(
	 * java.lang.String)
	 */
	public boolean canContain(String elementName) {
		return namespace.getElement(elementName) == null;
	}
}
