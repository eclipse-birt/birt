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

import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Parse the value of ParamBinding in BIRT 2.1M5 to BIRT 2.1 RC0.
 */

public class CompatibleParamBindingValueState extends CompatibleMiscExpressionState {

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler the handler to parse the design file.
	 * @param element    the data item
	 * @param propDefn
	 * @param struct
	 */

	CompatibleParamBindingValueState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		if (value == null)
			return;

		// keep the expression as same.

		doEnd(value, true);

		if (handler.versionNumber >= VersionUtil.VERSION_3_2_0)
			return;

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(value);
		} catch (BirtException e) {
			// do nothing
		}

		if (newExprs == null || newExprs.isEmpty()) {
			return;
		}

		DesignElement target = BoundDataColumnUtil.findTargetElementOfParamBinding(element, handler.getModule());

		addBoundColumnsToTarget(target, newExprs);
	}
}
