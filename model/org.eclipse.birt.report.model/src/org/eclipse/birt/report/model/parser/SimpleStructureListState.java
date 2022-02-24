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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list, each of which has only one member. So it
 * also can be considered as String List.
 */

public class SimpleStructureListState extends CompatibleListPropertyState {

	protected String memberName = null;

	SimpleStructureListState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Sets the member name which is the unique member in structure.
	 * 
	 * @param memberName the member name to set
	 */

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */
	public AbstractParseState startElement(String tagName) {
		// The unique member name should be specified.

		assert memberName != null;

		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.PROPERTY_TAG == tagValue) {
			AbstractPropertyState state = new SimpleStructureState(handler, element, propDefn);
			return state;
		}

		return super.startElement(tagName);
	}

	class SimpleStructureState extends StructureState {

		SimpleStructureState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn) {
			super(theHandler, element, propDefn);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			struct = createStructure((StructureDefn) propDefn.getStructDefn());
			assert struct != null;

			String value = text.toString();

			setMember(struct, propDefn.getName(), memberName, value);

			super.end();
		}
	}
}
