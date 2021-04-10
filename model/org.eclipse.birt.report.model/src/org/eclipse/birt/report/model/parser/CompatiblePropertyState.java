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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * The compatible parser for parsing element properties or structure members.
 * This is the base class for all compatible property parser.
 * 
 */

abstract class CompatiblePropertyState extends PropertyState {

	/**
	 * Constructs the compatible state for parsing the given structure member
	 * property value.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the design element
	 * @param propDefn   the property definition
	 * @param struct     the structure that has this property
	 */

	public CompatiblePropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/**
	 * Constructs the compatible state for parsing the given element property value.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the design element
	 */

	public CompatiblePropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#generalJumpTo()
	 */

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

	protected AbstractParseState versionConditionalJumpTo() {
		return null;
	}
}
