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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ChoicePropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;

/**
 * Handles recoverable errors during parsing the design file.
 */

class RecoverableError {

	/**
	 * Handles property value exceptions with the given value exception and the
	 * property value.
	 *
	 * @param handler        the handler for the design parser
	 * @param valueException the exception thrown by the parser
	 */

	protected static void dealInvalidPropertyValue(ModuleParserHandler handler, PropertyValueException valueException) {
		Object retValue = valueException.getInvalidValue();

		DesignElement element = valueException.getElement();
		String propName = valueException.getPropertyName();

		element.setProperty(propName, retValue);

		if (valueException.getErrorCode().equals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND)
				&& ChoicePropertyType.isDataTypeAny(element.getPropertyDefn(propName).getChoices(), retValue)) {
			return;
		}

		handler.getErrorHandler().semanticWarning(valueException);
	}

	/**
	 * Handles property value exceptions with the given value exception and the
	 * property value.
	 *
	 * @param handler        the handler for the design parser
	 * @param valueException the exception thrown by the parser
	 * @param structre       the structure that have this member value
	 * @param memberDefn     the member definition
	 */

	protected static void dealInvalidMemberValue(ModuleParserHandler handler, PropertyValueException valueException,
			IStructure structre, StructPropertyDefn memberDefn) {
		assert structre != null;

		Object retValue = valueException.getInvalidValue();
		structre.setProperty(memberDefn, retValue);

		if (valueException.getErrorCode().equals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND)
				&& ChoicePropertyType.isDataTypeAny(memberDefn.getChoices(), retValue)) {
			return;
		}

		handler.getErrorHandler().semanticWarning(valueException);
	}

	/**
	 * Handles design parser exceptions with the given parser exception.
	 *
	 * @param handler   the handler for the design parser
	 * @param exception the design parser exception to record
	 */

	protected static void dealUndefinedProperty(ModuleParserHandler handler, DesignParserException exception) {
		handler.getErrorHandler().semanticWarning(exception);
	}

	/**
	 * Handles the semantic error when an extended item has a invalid extension.
	 *
	 * @param handler   the handler for the design parser
	 * @param exception the exception thrown by the parser
	 */

	protected static void dealMissingInvalidExtension(ModuleParserHandler handler, SemanticError exception) {
		handler.getErrorHandler().semanticWarning(exception);
	}

}
