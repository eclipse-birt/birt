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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a scalar parameter.
 * 
 */

public class ScalarParameterState extends AbstractScalarParameterState {

	/**
	 * The scalar parameter being created.
	 */

	protected ScalarParameter param;

	/**
	 * Constructs the scalar parameter state with the design parser handler, the
	 * container element and the container slot of the scalar parameter.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the container of this parameter.
	 * @param slot         the slot ID of the slot where the parameter is stored.
	 */

	public ScalarParameterState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		// First we create the ScalarParameter.

		param = new ScalarParameter();

		// Then we initialize the properties derived from the
		// the Report Item element. The name is required for a parameter.
		// <code>initElement</code> adds the parameter to the parameters slot
		// of the report design.

		initElement(attrs, true);
	}

	/**
	 * Returns the scalar parameter being built.
	 * 
	 * @return the parameter instance
	 */

	public DesignElement getElement() {
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */

	public void end() throws SAXException {
		if (handler.versionNumber < VersionUtil.VERSION_3_2_11) {
			Boolean[] allowValues = (Boolean[]) handler.tempValue.get(param);
			if (allowValues == null) {
				allowValues = new Boolean[2];
				allowValues[0] = Boolean.FALSE;
				allowValues[1] = Boolean.TRUE;
			} else
				// remove the element from the map
				handler.tempValue.remove(param);

			Boolean allowNull = allowValues[0];
			Boolean allowBlank = allowValues[1];

			String valueType = (String) param.getProperty(handler.module, ScalarParameter.DATA_TYPE_PROP);

			Boolean isRequired = null;
			if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(valueType)) {
				if ((allowBlank != null && allowBlank.booleanValue())
						|| (allowNull != null && allowNull.booleanValue()))
					isRequired = Boolean.FALSE;
				else
					isRequired = Boolean.TRUE;
			} else {
				// for other types, ignores allowBlank value

				if (allowNull != null && allowNull.booleanValue())
					isRequired = Boolean.FALSE;
				else
					isRequired = Boolean.TRUE;
			}

			if (isRequired != null)
				param.setProperty(IAbstractScalarParameterModel.IS_REQUIRED_PROP, isRequired);

		}

		// do back-compatibility about 'sortBy'
		if (handler.versionNumber < VersionUtil.VERSION_3_2_17) {
			String sortBy = param.getStringProperty(handler.module, IAbstractScalarParameterModel.SORT_BY_PROP);

			// if sortBy is set and parameter is dynamic or cascading, and the
			// sortByColumn is not set then do the compatibility
			if (!StringUtil.isBlank(sortBy) && isDynamicParam() && StringUtil.isBlank(
					param.getStringProperty(handler.module, IAbstractScalarParameterModel.SORT_BY_COLUMN_PROP))) {
				Object oldValue = null;
				if (DesignChoiceConstants.PARAM_SORT_VALUES_VALUE.equalsIgnoreCase(sortBy)) {
					oldValue = param.getProperty(handler.module, IAbstractScalarParameterModel.VALUE_EXPR_PROP);
				} else if (DesignChoiceConstants.PARAM_SORT_VALUES_LABEL.equalsIgnoreCase(sortBy)) {

					oldValue = param.getProperty(handler.module, IAbstractScalarParameterModel.LABEL_EXPR_PROP);
				}
				if (oldValue != null) {
					Expression newValue = null;
					if (oldValue instanceof Expression) {
						Expression oldExpr = (Expression) oldValue;
						newValue = new Expression(oldExpr.getExpression(), oldExpr.getUserDefinedType());
					} else {
						newValue = new Expression(oldValue, null);
					}
					param.setProperty(IAbstractScalarParameterModel.SORT_BY_COLUMN_PROP, oldValue);
				}
			}
		}

		super.end();
	}

	private boolean isDynamicParam() {
		String valueType = param.getStringProperty(handler.module, IAbstractScalarParameterModel.VALUE_TYPE_PROP);

		// if the parameter is set to be 'dynamic'
		if (DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC.equals(valueType))
			return true;

		// all parameter in the cascading parameter group is defined as
		// 'dynamic'
		DesignElement container = param.getContainer();
		if (container instanceof CascadingParameterGroup)
			return true;

		return false;
	}
}
