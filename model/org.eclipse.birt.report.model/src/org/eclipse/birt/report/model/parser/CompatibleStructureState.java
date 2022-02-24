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
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * The compatible parser for parsing structure values. This is the base class
 * for all compatible structure parser.
 * 
 */

abstract class CompatibleStructureState extends StructureState {

	/**
	 * Constructs the compatible state for parsing the given structure member
	 * property value.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the design element
	 * @param propDefn   the property definition
	 * @param struct     the structure that has this property
	 */

	public CompatibleStructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, (Structure) struct);
	}

	/**
	 * Constructs the compatible state for parsing the given element property value.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the design element
	 */

	public CompatibleStructureState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Constructs the state of the structure which is in one structure list.
	 * 
	 * @param theHandler the design parser handler
	 * @param element    the element holding this structure
	 * @param propDefn   the definition of the property which holds this structure
	 * @param theList    the structure list
	 */

	CompatibleStructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn) {
		super(theHandler, element);

		assert propDefn != null;

		this.propDefn = propDefn;
		this.name = propDefn.getName();
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
