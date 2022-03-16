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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;

/**
 * Expression property type. Expressions are stored as strings. Expressions are
 * not validated at design time; instead we rely on the runtime script engine
 * for validation. This allows the user to temporarily store incorrect
 * expressions while they work on a report. Doing so is like storing an invalid
 * Java file while writing code.
 */

public class ExpressionPropertyType extends TextualPropertyType {

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.expression"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ExpressionPropertyType() {
		super(DISPLAY_NAME_KEY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		return EXPRESSION_TYPE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */

	@Override
	public String getName() {
		return EXPRESSION_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
	@Override
	public Object validateValue(Module module, DesignElement element, PropertyDefn defn, Object value)
			throws PropertyValueException {
		if (value == null) {
			return null;
		}

		int trimOption = defn.getTrimOption();
		if (value instanceof Expression) {
			String expr = ((Expression) value).getStringExpression();
			String tmpType = ((Expression) value).getUserDefinedType();

			String trimExpr = trimString(expr, trimOption);

			if (trimExpr == null) {
				if (tmpType == null) {
					return null;
				}

				return new Expression(null, tmpType);
			}

			return value;
		}

		if (value instanceof String) {
			String expr = trimString((String) value, trimOption);
			if (expr == null) {
				return null;
			}

			return new Expression(expr, null);
		}

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, EXPRESSION_TYPE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.TextualPropertyType#toString(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	@Override
	public String toString(Module module, PropertyDefn defn, Object value) {
		if (value instanceof Expression) {
			return ((Expression) value).getStringExpression();
		} else if (value == null || value instanceof String) {
			return (String) value;
		}

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#toDisplayString(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */

	@Override
	public String toDisplayString(Module module, PropertyDefn defn, Object value) {
		return toString(module, defn, value);
	}
}
