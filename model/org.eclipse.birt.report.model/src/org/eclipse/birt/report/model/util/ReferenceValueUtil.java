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

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IReferencableElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefPropertyType;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.ReferenceValue;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefPropertyType;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Collection of <code>ReferenceValue</code> utilities.
 */

public class ReferenceValueUtil {

	/**
	 * Resolves a property element reference. The reference is the value of a
	 * property of type property element reference.
	 *
	 * @param structure structure
	 * @param module    the module information needed for the check, and records any
	 *                  errors
	 * @param prop      the property whose type is element reference
	 * @param value     element ref value
	 * @return the element reference value is always returned, which contains the
	 *         information of element resolution.
	 */

	public static ElementRefValue resolveElementReference(Structure structure, Module module, StructPropertyDefn prop,
			Object value) {
		if (prop.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE) {
			return null;
		}

		assert value == null || value instanceof ElementRefValue;

		if (value == null || module == null) {
			return (ElementRefValue) value;
		}

		ElementRefValue ref = (ElementRefValue) value;
		if (ref.isResolved()) {
			return ref;
		}

		// The element exist and is not resolved. Try to resolve it.
		// If it is now resolved, cache the back pointer.
		// Note that this is a safe operation to do without the
		// use of the command stack. We are not changing the meaning
		// of the property: we are only changing the form: from name
		// to element pointer.

		ElementRefPropertyType refType = (ElementRefPropertyType) prop.getType();

		refType.resolve(module, structure.getElement(), prop, ref);

		if (!ref.isResolved()) {
			return ref;
		}

		DesignElement me = structure.getElement();

		// if it is recursively reference, not resolve it.

		if (me instanceof ReferenceableElement
				&& ModelUtil.isRecursiveReference(ref.getElement(), (ReferenceableElement) me)) {
			ref.unresolved(ref.getName());
			return ref;
		}

		// how to handle back reference.

		ref.getTargetElement().addClient(structure, prop.getName());
		return ref;
	}

	/**
	 * Gets the property value with the name prefix. This method is just used for
	 * the element/structure reference type property. If the report design element
	 * is extended from a library element and the retrieved value comes from the
	 * parent, the namespace of the library should be added before the value. This
	 * is used to allocate the referenced element/structure within the correct
	 * scope.
	 *
	 * @param refValue the reference value
	 * @param root     the module that the element attached.
	 * @param module   the module that holds the ActivityStack. For the case that
	 *                 element is not on the library/design tree.
	 * @return the value of the property. The type of the returned object should be
	 *         strings.
	 *
	 */

	public static String needTheNamespacePrefix(ReferenceValue refValue, Module root, Module module) {
		if (refValue == null) {
			return null;
		}

		String namespace = refValue.getLibraryNamespace();
		String name = refValue.getName();

		if (namespace == null) {
			return name;
		}
		Module theRoot = module;
		if (root != null) {
			theRoot = root;
		}

		if (theRoot instanceof Library) {
			if (!namespace.equals(((Library) theRoot).getNamespace())) {
				name = namespace + ReferenceValue.NAMESPACE_DELIMITER + name;
			}
		} else {
			name = namespace + ReferenceValue.NAMESPACE_DELIMITER + name;
		}

		return name;

	}

	/**
	 * Gets the correct element name for the specified module. If the
	 * <code>module</code> is the root element of <code>element</code>, no libray
	 * namespace is in the return string. If the root element of
	 * <code>element</code> is not <code>module</code> and it is a library with the
	 * namespace, the return value contains the namespace.
	 *
	 * <p>
	 * This is used to allocate the referenced element/structure within the correct
	 * scope.
	 *
	 * @param element the design element.
	 * @param root
	 * @param module  the module.
	 * @return the element name. It contains the library namespace if above criteria
	 *         applies.
	 *
	 */

	public static String needTheNamespacePrefix(DesignElement element, Module root, Module module) {
		if (element == null) {
			return null;
		}

		String nameSpace = null;
		if (root instanceof Library) {
			nameSpace = ((Library) root).getNamespace();
		}

		String name = element.getFullName();

		if (root != module) {
			name = nameSpace + ReferenceValue.NAMESPACE_DELIMITER + name;
		}
		return name;
	}

	/**
	 * Gets the property value with the name prefix. This method is just used for
	 * the element/structure reference type property. If the report design element
	 * is extended from a library element and the retrieved value comes from the
	 * parent, the namespace of the library should be added before the value. This
	 * is used to allocate the referenced element/structure within the correct
	 * scope.
	 *
	 * @param refValue the reference value
	 * @param root     the module that the element attached.
	 * @return the value of the property. The type of the returned object should be
	 *         strings.
	 *
	 */

	public static String needTheNamespacePrefix(ReferenceValue refValue, Module root) {
		return needTheNamespacePrefix(refValue, root, null);
	}

	/**
	 * Resolves the parent element reference.
	 *
	 * @param module     the module information needed for the check
	 * @param element    design element
	 * @param extendsRef extended reference
	 */

	public static void resloveExtends(Module module, DesignElement element, ElementRefValue extendsRef) {

		if (extendsRef == null || module == null || extendsRef.isResolved()) {
			return;
		}

		// The parent exist and is not resolved. Try to resolve it.
		// If it is now resolved, cache the back pointer.
		// Note that this is a safe operation to do without the
		// use of the command stack. We are not changing the meaning
		// of the property: we are only changing the form: from name
		// to element pointer.

		ElementDefn metaData = (ElementDefn) element.getDefn();
		PropertyDefn propDefn = (PropertyDefn) metaData.getProperty(IDesignElementModel.EXTENDS_PROP);
		DesignElement resolvedParent = module.resolveElement(element,
				ReferenceValueUtil.needTheNamespacePrefix(extendsRef, module), propDefn, metaData);

		try {
			element.checkExtends(resolvedParent);
			if (resolvedParent != null) {
				extendsRef.resolve(resolvedParent);
				resolvedParent.addDerived(element);
			}
		} catch (ExtendsException e) {
			// Do nothing.
		}
	}

	/**
	 * Resolves a property element reference. The reference is the value of a
	 * property of type property element reference.
	 *
	 * @param module  the module information needed for the check, and records any
	 *                errors
	 * @param element design element
	 * @param prop    the property whose type is element reference
	 * @param value   the element reference value to resolve
	 * @return the element reference value is always returned, which contains the
	 *         information of element resolution.
	 */

	public static ElementRefValue resolveElementReference(Module module, DesignElement element,
			ElementPropertyDefn prop, ElementRefValue value) {
		ElementRefValue ref = value;
		if (ref.isResolved()) {
			return ref;
		}

		// The element exist and is not resolved. Try to resolve it.
		// If it is now resolved, cache the back pointer.
		// Note that this is a safe operation to do without the
		// use of the command stack. We are not changing the meaning
		// of the property: we are only changing the form: from name
		// to element pointer.

		// property may be a list type of element reference or the element
		// reference type

		assert prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE
				|| prop.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE;

		ElementRefPropertyType refType = null;
		if (prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			refType = (ElementRefPropertyType) prop.getType();
		} else {
			refType = (ElementRefPropertyType) prop.getSubType();
		}

		refType.resolve(module, element, prop, ref);
		if (ref.isResolved()) {
			ref.getTargetElement().addClient(element, prop.getName());
		}

		return ref;
	}

	/**
	 * Resolves a property structure reference. The reference is the value of a
	 * property of type property structure reference.
	 *
	 * @param module  the module information needed for the check, and records any
	 *                errors
	 * @param element design element
	 * @param prop    the property whose type is structure reference
	 * @param value   structure property value
	 * @return the resolved value if the resolve operation is successful, otherwise
	 *         the unresolved value
	 */

	public static StructRefValue resolveStructReference(Module module, DesignElement element, ElementPropertyDefn prop,
			StructRefValue value) {
		StructRefValue ref = value;
		if (ref.isResolved()) {
			return ref;
		}

		// The element exist and is not resolved. Try to resolve it.
		// If it is now resolved, cache the back pointer.
		// Note that this is a safe operation to do without the
		// use of the command stack. We are not changing the meaning
		// of the property: we are only changing the form: from name
		// to element pointer.

		StructRefPropertyType refType = (StructRefPropertyType) prop.getType();
		refType.resolve(module, prop, ref);
		if (ref.isResolved()) {
			ref.getTargetStructure().addClient(element, prop.getName());
		}

		return ref;
	}

	/**
	 * Implements to cache a back-pointer from referenced structure or referenced
	 * element.
	 *
	 * @param element design element
	 * @param oldRef  the old reference, if any
	 * @param newRef  the new reference, if any
	 * @param prop    definition of the property
	 */

	public static void updateReference(DesignElement element, ReferenceValue oldRef, ReferenceValue newRef,
			ElementPropertyDefn prop) {
		if (oldRef == null && newRef == null) {
			return;
		}
		if (oldRef instanceof ElementRefValue || newRef instanceof ElementRefValue) {
			IReferencableElement target;
			// Drop the old reference. Clear the back pointer from the
			// referenced
			// element to this element.

			if (oldRef != null) {
				target = ((ElementRefValue) oldRef).getTargetElement();
				if (target != null) {
					target.dropClient(element);
				}
			}

			// Add the new reference. Cache a back pointer from the referenced
			// element to this element. Include the property name so we know
			// which
			// property to adjust it the target is deleted.

			if (newRef != null) {
				target = ((ElementRefValue) newRef).getTargetElement();
				if (target != null) {
					target.addClient(element, prop.getName());
				}
			}
		}

		if (oldRef instanceof StructRefValue || newRef instanceof StructRefValue) {
			ReferencableStructure target;
			// Drop the old reference. Clear the back pointer from the
			// referenced
			// structure to this element.

			if (oldRef != null) {
				target = ((StructRefValue) oldRef).getTargetStructure();
				if (target != null) {
					target.dropClient(element);
				}
			}

			// Add the new reference. Cache a back pointer from the referenced
			// element to this element. Include the property name so we know
			// which
			// property to adjust it the target is deleted.

			if (newRef != null) {
				target = ((StructRefValue) newRef).getTargetStructure();
				if (target != null) {
					target.addClient(element, prop.getName());
				}
			}
		}
	}
}
