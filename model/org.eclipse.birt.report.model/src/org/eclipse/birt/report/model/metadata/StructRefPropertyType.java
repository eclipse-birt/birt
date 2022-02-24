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

package org.eclipse.birt.report.model.metadata;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.util.ReferenceValueUtil;
import org.eclipse.birt.report.model.util.StructureRefUtil;

/**
 * Represents a reference to a structure. A structure reference is different
 * from a structure list property. A structure list <em>contains</em> a
 * structure. A structure reference simply <em>references</em> a structure
 * defined in the report design.
 * <p>
 * A structure reference can be in one of two states: resolved or unresolved. A
 * resolved reference points to an the "target" structure itself. An unresolved
 * reference gives only the name of the target structure, and the structure
 * itself may or may not exist.
 * <p>
 * Elements that contain properties of this type must provide code to perform
 * semantic checks on the reference property. This is done to avoid the need to
 * search the property list to find any properties that are of this type.
 * <p>
 * The reference value are stored as an <code>StructRefValue</code>
 * 
 * @see StructRefValue
 */

public class StructRefPropertyType extends PropertyType {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(StructRefPropertyType.class.getName());

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.structRef"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public StructRefPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyType#getTypeCode()
	 */

	public int getTypeCode() {
		return STRUCT_REF_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyType#getName()
	 */

	public String getName() {
		return STRUCT_REF_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}
		if (value instanceof String) {
			String name = StringUtil.trimString((String) value);
			return StructureRefUtil.resolve(module, defn, name);
		}
		if (value instanceof Structure) {
			Structure target = (Structure) value;
			return StructureRefUtil.resolve(module, defn, target);
		}

		// Invalid property value.
		logger.log(Level.SEVERE, "The value of the structure property: " + defn.getName() + " is invalid type"); //$NON-NLS-1$ //$NON-NLS-2$
		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				IPropertyType.ELEMENT_REF_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#toString(org.eclipse
	 * .birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value == null)
			return null;

		if (value instanceof String)
			return (String) value;

		return ReferenceValueUtil.needTheNamespacePrefix((StructRefValue) value, module);
	}

	/**
	 * Resolves a structure reference. Look up the name in the report design. If the
	 * target is found, replace the structure name with the cached structure.
	 * 
	 * @param module the report design
	 * @param defn   the definition of the structure ref property
	 * @param ref    the structure reference
	 */

	public void resolve(Module module, PropertyDefn defn, StructRefValue ref) {
		if (ref.isResolved() || module == null)
			return;
		StructureDefn targetDefn = (StructureDefn) defn.getStructDefn();
		Structure target = null;
		Module targetModule = null;
		if (ReferencableStructure.LIB_REFERENCE_MEMBER.equals(defn.getName())) {
			String namespace = ref.getLibraryNamespace();
			targetModule = module.getLibraryWithNamespace(namespace);
			if (targetModule != null) {
				target = StructureRefUtil.findStructure(targetModule, targetDefn, ref.getName());
				if (target != null)
					ref.resolve(target);
			}
		} else {
			StructRefValue retValue = StructureRefUtil.resolve(module, defn,
					ReferenceValueUtil.needTheNamespacePrefix(ref, module));
			target = retValue.getStructure();
			ref.libraryNamespace = retValue.getLibraryNamespace();
			ref.name = retValue.getName();
			if (target != null)
				ref.resolve(target);
		}
	}
}
