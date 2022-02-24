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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.PropertyBinding;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates one list property of element. The property type should structure
 * list.
 * 
 * <h3>Rule</h3> The rule is that
 * <ul>
 * <li>all structures in this list property should be valid.
 * <li>the value of the property with <code>NamePropertyType</code> should be
 * unique in the structure list.
 * </ul>
 * 
 * <h3>Applicability</h3> This validator is only applied to the property whose
 * type is structure list of one <code>DesignElement</code>.
 */

public class StructureListValidator extends AbstractPropertyValidator {

	/**
	 * Name of this validator.
	 */

	public final static String NAME = "StructureListValidator"; //$NON-NLS-1$

	private static StructureListValidator instance = new StructureListValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static StructureListValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether a new structure can be added to structure list.
	 * 
	 * @param element  the element holding the structure list
	 * @param propDefn definition of the list property
	 * @param list     the structure list
	 * @param toAdd    the structure to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAdding(DesignElementHandle element, IPropertyDefn propDefn,
			List<Object> list, IStructure toAdd) {
		return doCheckStructureList(element.getModule(), element.getElement(), propDefn, list, toAdd);
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

		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE && propDefn.isList();

		List<Object> list = (List<Object>) element.getLocalProperty(module, propDefn);

		return doCheckStructureList(module, element, propDefn, list, null);

	}

	/**
	 * Checks all structures in the specific property whose type is structure list
	 * property type.
	 * 
	 * @param module   the module
	 * @param element  the design element to validate
	 * @param propDefn the property definition of the list property
	 * @param list     the structure list to check
	 * @param toAdd    the structure to add. This parameter maybe is
	 *                 <code>null</code>.
	 * 
	 * @return the error list
	 */

	private List<SemanticException> doCheckStructureList(Module module, DesignElement element, IPropertyDefn propDefn,
			List<Object> list, IStructure toAdd) {
		boolean checkList = toAdd == null;

		List<SemanticException> errorList = new ArrayList<SemanticException>();

		if (list == null || list.size() == 0)
			return errorList;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;

		boolean checkID = propDefn.getStructDefn().getName().equals(PropertyBinding.PROPERTY_BINDING_STRUCT);

		if (!checkID) {
			// Get the unique member whose value should be unique in the
			// structure list.
			// The type of unique member is name property type.
			// Note: The first unique member is considered.

			PropertyDefn uniqueMember = null;

			Iterator<IPropertyDefn> iter = propDefn.getStructDefn().propertiesIterator();
			while (iter.hasNext()) {
				StructPropertyDefn memberDefn = (StructPropertyDefn) iter.next();

				if (memberDefn.getTypeCode() == IPropertyType.NAME_TYPE
						|| memberDefn.getTypeCode() == IPropertyType.MEMBER_KEY_TYPE) {
					uniqueMember = memberDefn;
					break;
				}
			}

			HashSet<String> values = new HashSet<String>();

			// Check whether there two structure has the same value of
			// the unique member.

			for (int i = 0; i < list.size(); i++) {
				Structure struct = (Structure) list.get(i);

				if (checkList)
					errorList.addAll(struct.validate(module, element));

				if (uniqueMember != null) {
					String value = (String) struct.getProperty(module, uniqueMember);
					if (values.contains(value)) {
						if (checkList)
							errorList.add(new PropertyValueException(element, propDefn, value,
									PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
					} else {
						values.add(value);
					}
				}
			}

			// If the toAdd structure is added the structure list, check whether
			// there is a structure in the list has the same value of the unique
			// member.

			if (uniqueMember != null && toAdd != null) {
				String value = (String) toAdd.getProperty(module, uniqueMember);
				if (values.contains(value)) {
					errorList.add(new PropertyValueException(element, propDefn.getName(), value,
							PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
				}
			}
		}
		// the list is property binding list, we check them by both name and id
		else {
			HashMap<BigDecimal, List<String>> values = new HashMap<BigDecimal, List<String>>();

			// Check whether there two property binding have the same name and
			// element id.

			for (int i = 0; i < list.size(); i++) {
				PropertyBinding struct = (PropertyBinding) list.get(i);

				if (checkList)
					errorList.addAll(struct.validate(module, element));

				String name = struct.getName();
				BigDecimal id = struct.getID();
				List<String> names = values.get(id);
				if (names != null) {
					if (names.contains(name)) {
						if (checkList)
							errorList.add(new PropertyValueException(element, propDefn, name,
									PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
					} else {
						names.add(name);
					}
				} else {
					names = new ArrayList<String>();
					names.add(name);
					values.put(id, names);
				}

			}

			// If the toAdd property binding is added the structure list, check
			// whether there is a structure in the list has the same name and
			// element id.

			if (toAdd != null) {
				assert toAdd instanceof PropertyBinding;

				String name = ((PropertyBinding) toAdd).getName();
				BigDecimal id = ((PropertyBinding) toAdd).getID();
				List<String> names = values.get(id);
				if (names != null && names.contains(name)) {
					errorList.add(new PropertyValueException(element, propDefn, name,
							PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
				}
			}
		}

		return errorList;
	}

	/**
	 * Validates whether a structure can be renamed to the given name.
	 * 
	 * @param element    the element holding the structure list
	 * @param propDefn   definition of the list property
	 * @param list       the structure list
	 * @param toRenamed  the structure to rename
	 * @param memberDefn the member definition
	 * @param newName    the new name
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForRenaming(DesignElementHandle element, IPropertyDefn propDefn,
			List<Object> list, IStructure toRenamed, IPropertyDefn memberDefn, String newName) {
		List<SemanticException> errorList = new ArrayList<SemanticException>();

		if (list == null || list.size() == 0)
			return errorList;

		assert propDefn != null;
		assert propDefn.getTypeCode() == IPropertyType.STRUCT_TYPE;
		assert memberDefn != null;

		// Check whether there two structure has the same value of
		// the unique member.

		Module module = element.getModule();

		for (int i = 0; i < list.size(); i++) {
			Structure struct = (Structure) list.get(i);
			String value = (String) struct.getProperty(module, (PropertyDefn) memberDefn);

			if (value.equals(newName) && struct != toRenamed) {
				errorList.add(new PropertyValueException(element.getElement(), propDefn, value,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS));
				break;
			}
		}

		return errorList;
	}
}
