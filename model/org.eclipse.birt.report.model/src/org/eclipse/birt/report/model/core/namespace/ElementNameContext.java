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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * 
 */

public abstract class ElementNameContext extends AbstractNameContext {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#getElements
	 * (int)
	 */
	public List<DesignElement> getElements(int level) {
		List<DesignElement> elements = new ArrayList<DesignElement>();

		if (level == NATIVE_LEVEL) {
			elements.addAll(namespace.getElements());
		} else {
			elements.addAll(namespace.getElements());
			int newLevel = level - 1;
			Dimension parent = (Dimension) getElement().getExtendsElement();
			if (parent == null)
				parent = (Dimension) getElement().getVirtualParent();

			while (parent != null && newLevel >= NATIVE_LEVEL) {
				elements.addAll(((AbstractNameHelper) parent.getNameHelper()).getNameContext(Module.ELEMENT_NAME_SPACE)
						.getElements(newLevel));
			}
		}
		return Collections.unmodifiableList(elements);
	}

	/**
	 * Resolves the given element name to element reference value within the given
	 * depth.
	 * 
	 * @param elementName the element name
	 * 
	 * @return the element reference value.
	 */

	private ElementRefValue resolve(String elementName) {
		String namespace = StringUtil.extractNamespace(elementName);
		String name = StringUtil.extractName(elementName);

		List<DesignElement> elements = getElements(NATIVE_LEVEL);
		for (int i = 0; i < elements.size(); i++) {
			DesignElement tmpElement = elements.get(i);
			if (tmpElement.getFullName().equals(name)) {
				return new ElementRefValue(namespace, tmpElement);
			}
		}

		// not resolved
		return new ElementRefValue(namespace, name);
	}

	/**
	 * Resolves the given element to element reference value within the given depth.
	 * 
	 * @param element the element
	 * 
	 * @return the element reference value.
	 */

	private ElementRefValue resolve(DesignElement element) {
		if (element == null)
			return null;

		return doResolveElement(getElements(NATIVE_LEVEL), element);
	}

	/**
	 * Resolves <code>element</code> in the given <code>elements</code>. If
	 * <code>element</code> is not in the list, a unresolved reference value is
	 * returned.
	 * <p>
	 * The namespace information may be lost.
	 * 
	 * @param elements
	 * @param element
	 */

	private ElementRefValue doResolveElement(List<DesignElement> elements, DesignElement element) {
		boolean isFound = false;

		for (int i = 0; i < elements.size(); i++) {
			DesignElement tmpElement = elements.get(i);
			if (tmpElement == element) {
				isFound = true;
				break;
			}
		}

		Module root = element.getRoot();
		String namespace = null;

		if (root instanceof Library)
			namespace = ((Library) root).getNamespace();

		if (!isFound)
			return new ElementRefValue(namespace, element.getFullName());

		// TODO: if the root is null, the module of the element should be used
		// to get the namespace.

		return new ElementRefValue(namespace, element);
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
		if (propDefn != null && IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName()))
			return resolve(element);

		return resolve(element);
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
		if (propDefn != null && IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName()))
			return resolve(elementName);
		// try to resolve and return
		return resolve(elementName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#findElement
	 * (java.lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public DesignElement findElement(String elementName, IElementDefn elementDefn) {
		return resolve(elementName).getElement();
	}
}
