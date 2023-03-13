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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * The compatible parser for parsing element expression properties. This is the
 * base class for all compatible expression parser.
 *
 */

abstract class CompatibleExpressionState extends ExpressionState {

	/**
	 * Constructs the compatible state for parsing the given structure member
	 * property value.
	 *
	 * @param theHandler the parser handler
	 * @param element    the design element
	 * @param propDefn   the property definition
	 * @param struct     the structure that has this property
	 */

	public CompatibleExpressionState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/**
	 * Constructs the compatible state for parsing the given element property value.
	 *
	 * @param theHandler the parser handler
	 * @param element    the design element
	 */

	public CompatibleExpressionState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.PropertyState#generalJumpTo()
	 */

	@Override
	protected AbstractParseState generalJumpTo() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.parser.ExpressionState#versionConditionalJumpTo
	 * ()
	 */

	@Override
	protected AbstractParseState versionConditionalJumpTo() {
		return null;
	}
}
