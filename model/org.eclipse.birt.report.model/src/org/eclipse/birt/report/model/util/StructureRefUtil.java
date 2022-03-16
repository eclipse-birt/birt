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

package org.eclipse.birt.report.model.util;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.metadata.StructureDefn;

/**
 * Utility class for the StructRefPropertyType.
 */

public class StructureRefUtil {

	/**
	 * Looks up the target structure with the given name.
	 *
	 * @param module     the module in which to search the target locally
	 * @param targetDefn the definition for the target structure
	 * @param name       the name of the target structure to search
	 * @return the structure with the given name in the module locally, otherwise
	 *         null
	 */

	public static Structure findNativeStructure(Module module, StructureDefn targetDefn, String name) {
		if (StringUtil.isBlank(name) || targetDefn == null) {
			return null;
		}

		IElementPropertyDefn defn = module.getReferencablePropertyDefn(targetDefn.getName());

		if (defn == null) {
			return null;
		}
		assert defn.getTypeCode() == IPropertyType.STRUCT_TYPE;

		if (defn.isList()) {
			List<Object> list = module.getListProperty(module, defn.getName());
			if (list == null) {
				return null;
			}
			for (int i = 0; i < list.size(); i++) {
				Structure struct = (Structure) list.get(i);
				if (name.equals(struct.getReferencableProperty())) {
					return struct;
				}
			}
		} else {
			Structure struct = (Structure) module.getProperty(module, defn.getName());
			if (name.equals(struct.getReferencableProperty())) {
				return struct;
			}
		}
		return null;
	}

	/**
	 * Looks up the target structure with the given name in the module and its
	 * directly including libraries.
	 *
	 * @param module     the module in which to search the target locally
	 * @param targetDefn the definition for the target structure
	 * @param name       the name of the target structure to search
	 * @return the structure with the given name in the module locally, otherwise
	 *         null
	 */

	public static Structure findStructure(Module module, StructureDefn targetDefn, String name) {
		Object retValue = resolveStructureWithName(module, targetDefn, name);

		if (retValue instanceof StructRefValue) {
			return ((StructRefValue) retValue).getStructure();
		}

		return (Structure) retValue;
	}

	/**
	 * Resolves a reference structure with the given name and the module scope.
	 * <p>
	 * For example, if "image.gif" is the name and lib1 is not included by the
	 * <code>module</code>, "image" is treated as the namespace and "gif" is treated
	 * as the name.
	 *
	 * @param module     the module where to start to find
	 * @param targetDefn the definition for the target structure
	 * @param name       the name of the target structure to search
	 * @return the structure reference value
	 */

	private static Object resolveStructureWithName(Module module, StructureDefn targetDefn, String name) {
		if (StringUtil.isBlank(name) || targetDefn == null || module == null) {
			return null;
		}

		// try to find it locally first.

		if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equalsIgnoreCase(targetDefn.getName())) {
			Structure emImage = StructureRefUtil.findNativeStructure(module, targetDefn, name);
			if (emImage != null) {
				String namespace = null;
				if (module instanceof Library) {
					namespace = ((Library) module).getNamespace();
				}

				StructRefValue refValue = new StructRefValue(namespace, emImage);
				return refValue;
			}
		}

		// if not find locally, uses namespace

		String namespace = StringUtil.extractNamespace(name);
		String structName = StringUtil.extractName(name);

		Module moduleToSearch = null;

		// for the embedded image, there is no need to search again.

		if (!EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equalsIgnoreCase(targetDefn.getName())) {
			moduleToSearch = module;
		}

		if (namespace != null) {
			moduleToSearch = module.getLibraryWithNamespace(namespace);
		}

		// find it in the library.

		if (moduleToSearch != null) {
			Structure retValue = findNativeStructure(moduleToSearch, targetDefn, structName);
			if (retValue != null) {
				if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equalsIgnoreCase(targetDefn.getName())) {
					return new StructRefValue(namespace, retValue);
				}

				return retValue;
			}
		} else if (module instanceof Library) {
			namespace = ((Library) module).getNamespace();
			structName = stripNamespace(name, namespace);
		} else {
			namespace = null;
			structName = name;
		}

		if (EmbeddedImage.EMBEDDED_IMAGE_STRUCT.equalsIgnoreCase(targetDefn.getName())) {
			return new StructRefValue(namespace, structName);
		}

		return null;

	}

	/**
	 * Removes the namespace from the name if appliable.
	 *
	 * @param name      the name
	 * @param namespace the name space
	 *
	 * @return the name without namespace
	 */

	private static String stripNamespace(String name, String namespace) {
		if (name == null || namespace == null) {
			return name;
		}

		String tmpNamespace = StringUtil.extractNamespace(name);
		if ((tmpNamespace == null) || !namespace.equalsIgnoreCase(tmpNamespace)) {
			return name;
		}

		return StringUtil.extractName(name);
	}

	/**
	 * Resolves the structure with the given name.
	 *
	 * @param module report design
	 * @param defn   the definition of the property or member to resolve
	 * @param name   structure name
	 * @return the resolved structure reference value
	 */

	public static StructRefValue resolve(Module module, PropertyDefn defn, String name) {
		if (StringUtil.isBlank(name) || defn == null || module == null) {
			return null;
		}

		assert defn.getTypeCode() == IPropertyType.STRUCT_REF_TYPE;
		StructureDefn targetDefn = (StructureDefn) defn.getStructDefn();
		assert targetDefn != null;

		Structure target = null;

		// the module which the target structure lies

		Module targetModule = null;

		// if the property is a structure reference like "imageName", then the
		// name should not contain "namespace" prefix

		// TODO: the embeddedImage has "." in the name which will cause the
		// nemaspace ambiguity.

		if (ReferencableStructure.LIB_REFERENCE_MEMBER.equals(defn.getName())) {
			String structName = StringUtil.extractName(name);
			String namespace = StringUtil.extractNamespace(name);
			targetModule = module.getLibraryWithNamespace(namespace);
			if (targetModule != null) {
				target = findStructure(targetModule, targetDefn, structName);
				if (target != null) {
					return new StructRefValue(namespace, target);
				}
			}

			// if the target module is null or target structure is null, then
			// value is unresolved

			return new StructRefValue(namespace, structName);
		}
		StructRefValue refValue = (StructRefValue) resolveStructureWithName(module, targetDefn, name);
		return refValue;

	}

	/**
	 * Validates the structure value.
	 *
	 * @param module report design
	 * @param defn   the property definition of the value to validate
	 * @param target target structure
	 * @return the resolved structure reference value
	 * @throws PropertyValueException if the type of target structure is not that
	 *                                target definition.
	 */

	public static StructRefValue resolve(Module module, PropertyDefn defn, Structure target)
			throws PropertyValueException {
		if (target == null || module == null || defn == null) {
			return null;
		}

		assert defn.getTypeCode() == IPropertyType.STRUCT_REF_TYPE;
		StructureDefn targetDefn = (StructureDefn) defn.getStructDefn();
		if (targetDefn != target.getDefn()) {
			throw new PropertyValueException(target.getReferencableProperty(),
					PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE, IPropertyType.STRUCT_REF_TYPE);
		}

		// TODO: target need the root namespace now
		// must pass two modules into this method. Otherwise, the element
		// context is lost.

		return resolve(module, defn, target.getReferencableProperty());
	}
}
