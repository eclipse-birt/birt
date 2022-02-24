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

package org.eclipse.birt.report.model.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.EncryptionUtil;

/**
 * Utility class to validate the property value.
 * 
 */

public class PropertyValueValidationUtil {

	/**
	 * Validates the values of the item members.
	 * 
	 * @param propDefn the property definition
	 * @param item     the structure to validate
	 * @param element  the element
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	private static Object validateStructure(DesignElementHandle element, ElementPropertyDefn propDefn, Object value)
			throws SemanticException {

		if (!(value instanceof IStructure))
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.STRUCT_TYPE);

		IStructure item = (IStructure) value;
		if (item.getDefn() != propDefn.getStructDefn())
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.STRUCT_TYPE);

		return doValidateStructure(element, propDefn, null, item);
	}

	/**
	 * Validates the values of the item members.
	 * 
	 * @param structDefn the structure property definition
	 * @param item       the structure to validate
	 * @param element    the element
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	private static IStructure doValidateStructure(DesignElementHandle element, IPropertyDefn propDefn,
			IStructure parentStruct, IStructure item) throws SemanticException {

		IStructureDefn structDefn = propDefn.getStructDefn();

		for (Iterator<IPropertyDefn> iter = structDefn.propertiesIterator(); iter.hasNext();) {
			PropertyDefn memberDefn = (PropertyDefn) iter.next();
			if (memberDefn.getTypeCode() == IPropertyType.STRUCT_TYPE && memberDefn.isList())
				validateList(element, propDefn, item, memberDefn,
						((Structure) item).getLocalProperty(element.getModule(), memberDefn));
			else
				item.setProperty(memberDefn, memberDefn.validateValue(element.getModule(), element.getElement(),
						((Structure) item).getLocalProperty(element.getModule(), memberDefn)));
		}

		if (item instanceof Structure) {
			StructureContext context = null;

			if (parentStruct == null)
				context = new StructureContext(element.getElement(), (ElementPropertyDefn) propDefn, (Structure) item);
			else
				context = new StructureContext(parentStruct, (PropertyDefn) propDefn, (Structure) item);

			((Structure) item).setContext(context);

			List<SemanticException> errorList = ((Structure) item).validate(element.getModule(), element.getElement());
			if (errorList.size() > 0) {
				throw errorList.get(0);
			}
		}

		return item;
	}

	/**
	 * Validates a structure list.
	 * 
	 * @param element    the element
	 * @param propDefn   the property definition
	 * @param item       the structure to validate
	 * @param memberDefn the structure member definition
	 * 
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	private static Object validateList(DesignElementHandle element, IPropertyDefn propDefn, IStructure item,
			IPropertyDefn memberDefn, Object value) throws SemanticException {
		if (!(value instanceof List))
			return null;

		// if the memberDefn is not null, use memberDefn to validate the
		// structure value.

		IPropertyDefn tmpPropDefn = propDefn;
		if (memberDefn != null)
			tmpPropDefn = memberDefn;

		assert tmpPropDefn.isList();

		List<IStructure> retList = new ArrayList<IStructure>();
		List list = (List) value;
		IStructureDefn structDefn = tmpPropDefn.getStructDefn();
		for (int i = 0; i < list.size(); i++) {
			IStructure tmpItem = (IStructure) list.get(i);
			if (tmpItem.getDefn() != structDefn) {
				if (memberDefn != null)
					throw new PropertyValueException(element.getElement(), propDefn, memberDefn, tmpItem,
							PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE);

				throw new PropertyValueException(element.getElement(), propDefn, tmpItem,
						PropertyValueException.DESIGN_EXCEPTION_WRONG_ITEM_TYPE);

			}

			retList.add(doValidateStructure(element, tmpPropDefn, item, tmpItem));
		}

		return retList;
	}

	/**
	 * Validates a structure list.
	 * 
	 * @param propDefn the property definition
	 * @param item     the structure to validate
	 * @param element  the element
	 * @throws SemanticException if the item has any member with invalid value or if
	 *                           the given structure is not of a valid type that can
	 *                           be contained in the list.
	 */

	private static Object validateList(DesignElementHandle element, IPropertyDefn propDefn, Object value)
			throws SemanticException {
		return validateList(element, propDefn, null, null, value);
	}

	/**
	 * Validates a value to be stored for the given property.
	 * 
	 * @param element   the element to store the property value
	 * @param propName  the property name
	 * @param propValue the value to check
	 * @return the translated value to be stored
	 * @throws SemanticException if <code>propValue</code> is invalid.
	 */

	public static Object validateProperty(DesignElementHandle element, String propName, Object propValue)
			throws SemanticException {

		ElementPropertyDefn propDefn = (ElementPropertyDefn) element.getPropertyDefn(propName);

		if (propDefn == null)
			throw new PropertyNameException(element.getElement(), propName);

		Object retValue = null;

		switch (propDefn.getTypeCode()) {
		case IPropertyType.EXTENDS_TYPE:
			throw new PropertyValueException(propValue, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					propDefn.getTypeCode());
		case IPropertyType.STRUCT_TYPE:
			if (propDefn.isList())
				retValue = validateList(element, propDefn, propValue);
			else
				retValue = validateStructure(element, propDefn, propValue);
			break;
		default:
			retValue = propDefn.validateValue(element.getModule(), element.getElement(), propValue);

			if (retValue != null && propDefn.isEncryptable()) {
				String encrptID = element.getElement().getEncryptionID(propDefn);
				retValue = EncryptionUtil.encrypt(propDefn, encrptID, retValue);
				element.setEncryption(propName, encrptID);
			}

		}

		return retValue;
	}
}
