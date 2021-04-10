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
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.olap.Cube;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * The special case for the elements stored in cube name space, such as cube,
 * hierarchy, measure group and measure.
 */
public class CubeNameContext extends GeneralModuleNameContext {

	private static final IElementDefn HIERARCHY_DEFN = MetaDataDictionary.getInstance()
			.getElement(ReportDesignConstants.HIERARCHY_ELEMENT);

	private static final IElementDefn DIMENSION_DEFN = MetaDataDictionary.getInstance()
			.getElement(ReportDesignConstants.DIMENSION_ELEMENT);

	private static final int NAMESPACE_INDEX = 0;
	private static final int NAME_INDEX = 1;

	/**
	 * Constructs one cube element name space.
	 * 
	 * @param module      the attached module
	 * @param nameSpaceID the name space ID
	 */

	CubeNameContext(Module module, String nameSpaceID) {
		super(module, nameSpaceID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.namespace.IModuleNameSpace#resolve
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve(DesignElement focus, DesignElement element, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		if (element == null)
			return null;

		ElementDefn targetDefn = getTargetDefn(propDefn, elementDefn);
		if (targetDefn == null || isCubeReferred(targetDefn))
			return super.resolve(focus, element, propDefn, elementDefn);

		// dimension to shared dimension case
		String nameSpaceID = targetDefn.getNameSpaceID();

		if (Module.DIMENSION_NAME_SPACE.equals(nameSpaceID)) {
			if (focus instanceof Dimension)
				return super.resolve(focus, element, propDefn, elementDefn);
		}

		String namespace = null;
		Module root = element.getRoot();
		if (root instanceof Library) {
			namespace = root.getNamespace();
		}
		String name = element.getName();

		// the focus is data object cube.
		if (focus != null && focus.canDynamicExtends()) {
			Cube referredCube = (Cube) focus.getDynamicExtendsElement(focus.getRoot());
			if (referredCube == null)
				return new ElementRefValue(namespace, name);
		}

		Cube cube = findTarget(focus);
		if (cube == null)
			return super.resolve(focus, element, propDefn, elementDefn);
		else if (cube.canDynamicExtends()) {
			Cube referredCube = (Cube) cube.getDynamicExtendsElement(cube.getRoot());

			// referred tabular cube is not resolved in data mart cube
			if (referredCube == null)
				return new ElementRefValue(namespace, name);

			// find local element in data mart cube
			DesignElement retElement = cube.findLocalElement(name, targetDefn);
			if (retElement != null)
				return new ElementRefValue(namespace, retElement);

			return new ElementRefValue(namespace, name);
		}

		if (targetDefn.isKindOf(HIERARCHY_DEFN) || targetDefn.isKindOf(DIMENSION_DEFN)) {
			String tmpName = name;
			if (namespace != null) {
				Module tmpRoot = cube.getRoot();
				if (tmpRoot instanceof Library) {
					if (namespace.equals(((Library) tmpRoot).getNamespace())) {
						tmpName = name;
					} else
						// different name spaces.
						return super.resolve(focus, element, propDefn, elementDefn);
				} else
					// root is report design. but want to find library OLAP.
					return super.resolve(focus, element, propDefn, elementDefn);
			}

			DesignElement retElement = cube.findLocalElement(tmpName, targetDefn);
			if (retElement != null)
				return new ElementRefValue(namespace, retElement);

			return new ElementRefValue(namespace, name);
		}

		return super.resolve(focus, element, propDefn, elementDefn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.IModuleNameSpace#resolveNative
	 * (java.lang.String, org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */

	public ElementRefValue resolve(DesignElement focus, String elementName, PropertyDefn propDefn,
			ElementDefn elementDefn) {
		if (StringUtil.isBlank(elementName))
			return null;

		ElementDefn targetDefn = getTargetDefn(propDefn, elementDefn);
		if (targetDefn == null || isCubeReferred(targetDefn))
			return super.resolve(focus, elementName, propDefn, elementDefn);

		// dimension to shared dimension case
		String nameSpaceID = targetDefn.getNameSpaceID();

		if (Module.DIMENSION_NAME_SPACE.equals(nameSpaceID)) {
			if (focus instanceof Dimension)
				return super.resolve(focus, elementName, propDefn, elementDefn);
		}

		String[] rets = splitName(elementName, IAccessControl.ARBITARY_LEVEL);
		String namespace = rets[NAMESPACE_INDEX];
		String name = rets[NAME_INDEX];

		// the focus is data object cube.
		if (focus != null && focus.canDynamicExtends()) {
			Cube referredCube = (Cube) focus.getDynamicExtendsElement(focus.getRoot());
			if (referredCube == null)
				return new ElementRefValue(namespace, name);
		}

		Cube cube = findTarget(focus);
		if (cube == null)
			return super.resolve(focus, elementName, propDefn, elementDefn);
		else if (cube.canDynamicExtends()) {
			Cube referredCube = (Cube) cube.getDynamicExtendsElement(cube.getRoot());

			// referred tabular cube is not resolved in data mart cube
			if (referredCube == null)
				return new ElementRefValue(namespace, name);

			// find local element in data mart cube
			DesignElement retElement = cube.findLocalElement(name, targetDefn);
			if (retElement != null)
				return new ElementRefValue(namespace, retElement);

			return new ElementRefValue(namespace, name);
		}

		if (targetDefn.isKindOf(HIERARCHY_DEFN) || targetDefn.isKindOf(DIMENSION_DEFN)) {
			String tmpName = elementName;
			if (namespace != null) {
				Module tmpRoot = cube.getRoot();
				if (tmpRoot instanceof Library) {
					if (namespace.equals(((Library) tmpRoot).getNamespace())) {
						tmpName = name;
					} else
						// different name spaces.
						return super.resolve(focus, elementName, propDefn, elementDefn);
				} else
					// root is report design. but want to find library OLAP.
					return super.resolve(focus, elementName, propDefn, elementDefn);
			}

			DesignElement retElement = cube.findLocalElement(tmpName, targetDefn);
			if (retElement != null)
				return new ElementRefValue(namespace, retElement);

			return new ElementRefValue(namespace, name);
		}

		return super.resolve(focus, elementName, propDefn, elementDefn);
	}

	private boolean isCubeReferred(IElementDefn targetDefn) {
		assert targetDefn != null;

		if (targetDefn.isKindOf(MetaDataDictionary.getInstance().getElement(ReportDesignConstants.CUBE_ELEMENT)))
			return true;

		return false;
	}

	private Cube findTarget(DesignElement focus) {
		if (focus == null)
			return null;

		// if the focus referred a cube or it is a cube and the cube has dynamic
		// extends, then do some special resolve
		DesignElement element = focus;
		while (element != null) {
			if (element instanceof Cube)
				return (Cube) element;
			if (element instanceof ReportItem) {
				ReportItem item = (ReportItem) element;
				Cube cube = (Cube) item.getCubeElement(item.getRoot());
				if (cube != null)
					return cube;
			}

			element = element.getContainer();
		}

		return null;
	}

	private ElementDefn getTargetDefn(PropertyDefn propDefn, ElementDefn elementDefn) {
		if (elementDefn != null)
			return elementDefn;
		return (ElementDefn) (propDefn == null ? null : propDefn.getTargetElementType());
	}

	private String[] splitName(String elementName, int level) {
		String namespace = StringUtil.extractNamespace(elementName);
		String name = StringUtil.extractName(elementName);

		Module moduleToSearch = module;
		if (namespace != null)
			moduleToSearch = module.getLibraryWithNamespace(namespace, level);

		// check whether the root is library, get the namespace of the library.

		else if (moduleToSearch instanceof Library)
			namespace = ((Library) moduleToSearch).getNamespace();

		String[] names = new String[2];
		names[NAMESPACE_INDEX] = namespace;
		names[NAME_INDEX] = name;
		return names;
	}
}
