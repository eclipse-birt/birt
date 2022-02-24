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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 */
public class DummyNameContext implements INameContext {

	/**
	 * 
	 */
	public boolean canContain(String elementName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getElements
	 * (int)
	 */
	public List<DesignElement> getElements(int level) {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#resolve(org
	 * .eclipse.birt.report.model.core.DesignElement, java.lang.String,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn,
	 * org.eclipse.birt.report.model.metadata.ElementDefn)
	 */
	public ElementRefValue resolve(DesignElement focus, String elementName, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#resolve(org
	 * .eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn,
	 * org.eclipse.birt.report.model.metadata.ElementDefn)
	 */
	public ElementRefValue resolve(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getNameSpace()
	 */
	public NameSpace getNameSpace() {
		return new NameSpace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#findElement
	 * (java.lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public DesignElement findElement(String elementName, IElementDefn elementDefn) {
		return null;
	}

	public DesignElement getElement() {
		return null;
	}

	public String getNameSpaceID() {
		return "";
	}

}
