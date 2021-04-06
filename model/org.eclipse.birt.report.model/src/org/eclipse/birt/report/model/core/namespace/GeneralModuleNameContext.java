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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Used for all name spaces except styles.
 * 
 */
public class GeneralModuleNameContext extends AbstractModuleNameContext {

	/**
	 * 
	 * @param module
	 * @param nameSpaceID
	 */
	public GeneralModuleNameContext(Module module, String nameSpaceID) {
		super(module, nameSpaceID);
	}

	/**
	 * Returns all elements in the module this module namespace is assocaited and
	 * those in the included modules within the given depth.
	 * 
	 * @param level the depth of libraries that are included in the module
	 * 
	 * @return all element in this namespace.
	 */

	public List<DesignElement> getElements(int level) {
		List<DesignElement> elements = new ArrayList<DesignElement>();
		elements.addAll(getNativeElements());

		NameSpace ns = null;
		List<Library> libraries = module.getLibraries(level);

		for (int i = 0; i < libraries.size(); i++) {
			Library library = libraries.get(i);
			if (library.isValid()) {
				ns = library.getNameHelper().getNameSpace(nameSpaceID);
				elements.addAll(ns.getElements());
			}
		}

		return elements;
	}

	/**
	 * Resolves the given element name to element reference value within the given
	 * depth.
	 * 
	 * @param elementName the element name
	 * @param level       the depth of libraries that are included in the module
	 * 
	 * @return the element reference value.
	 */

	protected ElementRefValue resolve(String elementName, int level) {
		String namespace = StringUtil.extractNamespace(elementName);
		String name = StringUtil.extractName(elementName);

		Module moduleToSearch = module;
		if (namespace != null)
			moduleToSearch = module.getLibraryWithNamespace(namespace, level);

		// check whether the root is library, get the namespace of the library.

		else if (moduleToSearch instanceof Library)
			namespace = ((Library) moduleToSearch).getNamespace();

		if (moduleToSearch != null) {
			NameSpace ns = moduleToSearch.getNameHelper().getNameSpace(nameSpaceID);
			DesignElement target = ns.getElement(name);
			if (target != null) {
				return new ElementRefValue(namespace, target);
			}
		}

		// Return the unresolved reference value

		return new ElementRefValue(namespace, name);
	}

	/**
	 * Resolves the given element to element reference value within the given depth.
	 * 
	 * @param element the element
	 * @param level   the depth of libraries that are included in the module
	 * 
	 * @return the element reference value.
	 */

	private ElementRefValue resolve(DesignElement element, int level) {
		if (element == null)
			return null;

		return doResolveElement(getElements(level), element);
	}

	/**
	 * Returns all elements in the module.
	 * 
	 * @return all element in this namespace.
	 */

	private List<DesignElement> getNativeElements() {
		List<DesignElement> elements = new ArrayList<DesignElement>();

		NameSpace ns = namespace;
		elements.addAll(ns.getElements());

		return Collections.unmodifiableList(elements);
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
		// if the root is null, the module of the element should be used
		// to get the namespace.
		if (root == null) {
			root = element.getHandle(module).getModule();
		}
		String namespace = null;

		if (root instanceof Library)
			namespace = ((Library) root).getNamespace();

		if (!isFound)
			return new ElementRefValue(namespace, element.getFullName());

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
		if (propDefn != null && (IModuleModel.THEME_PROP.equalsIgnoreCase(propDefn.getName())
				|| IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName())))
			return resolve(element, DIRECTLY_INCLUDED_LEVEL);

		return resolve(element, ARBITARY_LEVEL);
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
		if (propDefn != null && (IModuleModel.THEME_PROP.equalsIgnoreCase(propDefn.getName())
				|| IDesignElementModel.EXTENDS_PROP.equalsIgnoreCase(propDefn.getName())))
			return resolve(elementName, DIRECTLY_INCLUDED_LEVEL);

		return resolve(elementName, ARBITARY_LEVEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.INameContext#findElement
	 * (java.lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */
	public DesignElement findElement(String elementName, IElementDefn elementDefn) {
		DesignElement element = resolve(elementName, ARBITARY_LEVEL).getElement();
		if (element == null)
			return null;
		if (elementDefn == null)
			return element;
		return element.getDefn().isKindOf(elementDefn) ? element : null;
	}
}
