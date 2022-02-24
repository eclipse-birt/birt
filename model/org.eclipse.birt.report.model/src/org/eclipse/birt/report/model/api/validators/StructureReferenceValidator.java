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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates the property whose type is structure reference. If the value can
 * refer to an actual structure, it will be resolved after validation. The rule
 * is that the structure reference value should refer to an actual structure in
 * the same report design.
 */

public class StructureReferenceValidator extends AbstractPropertyValidator {
	/**
	 * Name of this validator.
	 */

	public final static String NAME = "StructureReferenceValidator"; //$NON-NLS-1$

	/**
	 * The singleton validator instance.
	 */

	private final static StructureReferenceValidator instance = new StructureReferenceValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static StructureReferenceValidator getInstance() {
		return instance;
	}

	/**
	 * Validates the structure reference value can refer to an actual structure.
	 * 
	 * @param module   the module
	 * @param element  the element holding the structure reference property
	 * @param propName the name of the structure reference property
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate(Module module, DesignElement element, String propName) {
		List list = new ArrayList();

		if (!checkStructureReference(module, element, propName)) {
			Object value = element.getLocalProperty(module, propName);

			list.add(new SemanticError(element, new String[] { propName, ((StructRefValue) value).getName() },
					SemanticError.DESIGN_EXCEPTION_INVALID_STRUCTURE_REF));
		}

		return list;
	}

	/**
	 * Attempts to resolve a structure reference property. If the property is empty,
	 * or the reference is already resolved, return true. If the reference is not
	 * resolved, attempt to resolve it. If it cannot be resolved, return false.
	 * 
	 * @param module   the module
	 * @param element  the element holding this structure reference property
	 * @param propName the name of the property
	 * @return <code>true</code> if the property is resolved; <code>false</code>
	 *         otherwise.
	 */

	private boolean checkStructureReference(Module module, DesignElement element, String propName) {
		assert !StringUtil.isBlank(propName);

		// Is the value set?

		Object value = element.getLocalProperty(module, propName);
		if (value == null)
			return true;

		// This must be a structure reference property.

		ElementPropertyDefn prop = element.getPropertyDefn(propName);
		assert IPropertyType.STRUCT_REF_TYPE == prop.getTypeCode();

		// Attempt to resolve the reference.

		StructRefValue ref = (StructRefValue) value;
		ref = element.resolveStructReference(module, prop);
		return ref.isResolved();
	}
}
