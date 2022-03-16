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
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates the property whose type is element reference. If the value can
 * refer to an actual element, it will be resolved after validation.
 * <h3>Rule</h3> The rule is that the element reference value should refer to an
 * actual element in the same report.
 * <h3>Applicability</h3> This validator is only applied to the element
 * reference properties of <code>DesignElement</code>, except
 * <code>StyledElement.STYLE_PROP</code>. The
 * <code>StyledElement.STYLE_PROP</code> value should be validated with
 * {@link StyleReferenceValidator}.
 */

public class ElementReferenceValidator extends AbstractPropertyValidator {

	/**
	 * Name of this validator.
	 */

	public final static String NAME = "ElementReferenceValidator"; //$NON-NLS-1$

	private final static ElementReferenceValidator instance = new ElementReferenceValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static ElementReferenceValidator getInstance() {
		return instance;
	}

	/**
	 * Validates the element reference value can refer to an actual element.
	 *
	 * @param module   the module
	 * @param element  the element holding the element reference property
	 * @param propName the name of the element reference property
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element, String propName) {
		boolean flag = isInTemplateParameterDefinitionSlot(element);
		if (flag) {
			return Collections.emptyList();
		}

		List<SemanticException> list = new ArrayList<>();

		ElementPropertyDefn prop = element.getPropertyDefn(propName);

		if (prop.getTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			if (!checkElementReference(module, element, prop)) {
				Object value = element.getLocalProperty(module, propName);

				list.add(new SemanticError(element, new String[] { propName, ((ElementRefValue) value).getName() },
						SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF));
			}
		} else if (prop.getTypeCode() == IPropertyType.LIST_TYPE
				&& prop.getSubTypeCode() == IPropertyType.ELEMENT_REF_TYPE) {
			List<ElementRefValue> valueList = element.resolveElementReferenceList(module, prop);
			if (valueList != null) {
				for (int i = 0; i < valueList.size(); i++) {
					// check each reference value in the list

					ElementRefValue item = valueList.get(i);

					// check reference is resolved and not self-reference
					if (!item.isResolved() || item.getElement() == element) {
						list.add(new SemanticError(element, new String[] { propName, item.getName() },
								SemanticError.DESIGN_EXCEPTION_INVALID_ELEMENT_REF));
					}
				}
			}
		} else {
			assert false;
		}

		return list;
	}

	/**
	 * Attempts to resolve an element reference property. If the property is empty,
	 * or the reference is already resolved, return true. If the reference is not
	 * resolved, attempt to resolve it. If it cannot be resolved, return false.
	 *
	 * @param module              the module
	 * @param element             the element holding this element reference
	 *                            property
	 * @param containmentPropName the name of the property
	 * @return <code>true</code> if the property is resolved; <code>false</code>
	 *         otherwise.
	 */

	private boolean checkElementReference(Module module, DesignElement element, ElementPropertyDefn prop) {
		// This must be an element reference property

		assert IPropertyType.ELEMENT_REF_TYPE == prop.getTypeCode();

		// Attempt to resolve the reference.

		ElementRefValue ref = element.resolveElementReference(module, prop);
		if (ref == null) {
			return true;
		}

		// check reference is resolved and not self-reference
		return ref.isResolved() && element != ref.getElement();
	}

}
