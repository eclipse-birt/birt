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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.xml.sax.SAXException;

/**
 * Parses the ComputedColumn structure tag for compatibility.
 * <p>
 * Provide back-compatibility for original "boundDataColumns" member for
 * <code>GroupElement</code>.Delete 'boundDataColumns' property in ListingGroup,
 * when parsing design file if there is 'boundDataColumn' property in
 * ListingGroup in the old design file, bind the members of 'boundDataColumn'
 * property with the container of ListingGroup , either a Table or List.
 * <p>
 * The compatible version is equals or less than 3.2.1.
 * 
 */
public class CompatibleGroupBoundColumnsState extends CompatibleListPropertyState {

	/**
	 * The group element.
	 */

	private GroupElement group = null;

	/**
	 * Constructs the design parse state with the design file parser handler. This
	 * constructor is used when this list property to parse is a property of one
	 * element.
	 * 
	 * @param theHandler the design file parser handler
	 * @param element    the element which holds this property, in this place it
	 *                   must be a Table or a List.
	 * @param group
	 */

	CompatibleGroupBoundColumnsState(ModuleParserHandler theHandler, DesignElement element, GroupElement group) {
		super(theHandler, element);
		this.group = group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		if (struct != null) {
			// Ensure that the member is defined.

			PropertyDefn memberDefn = (PropertyDefn) struct.getDefn().getMember(name);
			struct.setProperty(memberDefn, list);
		} else {
			handler.tempValue.put(group, list);
		}
	}

	static class CompatibleGroupBoundColumnState extends CompatibleStructureState {

		CompatibleGroupBoundColumnState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				List list) {
			super(theHandler, element, propDefn);
			this.list = list;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.StructureState#end()
		 */

		public void end() throws SAXException {
			list.add(struct);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ListPropertyState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG)) {
			return new CompatibleGroupBoundColumnState(handler, element, propDefn, list);
		}

		return new AnyElementState(handler);
	}

}
