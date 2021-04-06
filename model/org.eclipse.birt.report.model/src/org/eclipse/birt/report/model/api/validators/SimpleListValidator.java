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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates one list property of element. The property type should be simple
 * value list, not structure list.
 * 
 * <h3>Rule</h3> The rule is that
 * <ul>
 * <li>all items in this list property should be valid.
 * <li>the value in this list should be unique.
 * </ul>
 * 
 * <h3>Applicability</h3> This validator is only applied to the property whose
 * type is list of one <code>DesignElement</code>.
 */

public class SimpleListValidator extends AbstractPropertyValidator {

	/**
	 * Name of this validator.
	 */

	public final static String NAME = "SimpleListValidator"; //$NON-NLS-1$

	private static SimpleListValidator instance = new SimpleListValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static SimpleListValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether a new item can be added to the simple value list.
	 * 
	 * @param element  the element holding the value list
	 * @param propDefn definition of the list property
	 * @param list     the value list
	 * @param toAdd    the item to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAdding(DesignElementHandle element, IPropertyDefn propDefn,
			List<Object> list, Object toAdd) {
		return doCheckPropertyList(element.getModule(), element.getElement(), propDefn, list, toAdd);
	}

	/**
	 * Validates whether the list property specified by <code>propName</code> is
	 * invalid.
	 * 
	 * @param module   the module
	 * @param element  the element to validate
	 * @param propName the name of the list property to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validate(Module module, DesignElement element, String propName) {
		ElementPropertyDefn propDefn = element.getPropertyDefn(propName);

		assert propDefn.getTypeCode() == IPropertyType.LIST_TYPE;

		List<Object> list = (List<Object>) element.getLocalProperty(module, propDefn);

		return doCheckPropertyList(module, element, propDefn, list, null);

	}

	/**
	 * Checks all value item in the specific property whose type is list.
	 * 
	 * @param module   the module
	 * @param element  the design element to validate
	 * @param propDefn the property definition of the list property
	 * @param list     the simple value list to check
	 * @param toAdd    the value item to add. This parameter maybe is
	 *                 <code>null</code>.
	 * 
	 * @return the error list
	 */

	private List<SemanticException> doCheckPropertyList(Module module, DesignElement element, IPropertyDefn propDefn,
			List<Object> list, Object toAdd) {
		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.LIST_TYPE;

		if (list == null || list.size() == 0
				|| ((PropertyDefn) propDefn).getSubTypeCode() != IPropertyType.ELEMENT_REF_TYPE)
			return Collections.emptyList();

		List<SemanticException> errorList = new ArrayList<SemanticException>();

		// Get the unique member whose value should be unique in the
		// structure list. The type of unique member is name property type.
		// Note: The first unique member is considered.

		HashSet<String> values = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			ElementRefValue item = (ElementRefValue) list.get(i);
			String key = item.getQualifiedReference();
			if (values.contains(key)) {
				errorList.add(new PropertyValueException(element, propDefn, key,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
			} else {
				values.add(key);
			}
		}

		// check whether the toAdd is duplicate

		if (toAdd != null) {
			assert toAdd instanceof ElementRefValue;
			String key = ((ElementRefValue) toAdd).getQualifiedReference();
			if (values.contains(key)) {
				errorList.add(new PropertyValueException(element, propDefn, key,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
			}

		}

		return errorList;
	}

}
