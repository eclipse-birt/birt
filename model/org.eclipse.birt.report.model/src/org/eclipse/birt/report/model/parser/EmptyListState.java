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

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.xml.sax.SAXException;

/**
 * Parses the empty list. If the list property is element property and can be
 * inherited, this property value will be set as empty list.
 */
public class EmptyListState extends AbstractPropertyState {

	/**
	 * Construct.
	 * 
	 * @param theHandler the module parser handler.
	 * @param element    the design element.
	 * @param struct     the structure.
	 */
	EmptyListState(ModuleParserHandler theHandler, DesignElement element, IStructure struct) {
		super(theHandler, element);
		this.struct = struct;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.VALUE_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG)) {
			SemanticError ex = new SemanticError(handler.module, new String[] { name },
					SemanticError.DESIGN_EXCEPTION_VALUE_FORBIDDEN);
			handler.getErrorHandler().semanticWarning(ex);
			return new AnyElementState(getHandler());
		}
		return super.startElement(tagName);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		assert struct == null;
		ElementPropertyDefn defn = element.getPropertyDefn(name);

		assert defn != null;
		assert ModelUtil.canInherit(defn);

		element.setProperty(defn, new ArrayList());

	}

}
