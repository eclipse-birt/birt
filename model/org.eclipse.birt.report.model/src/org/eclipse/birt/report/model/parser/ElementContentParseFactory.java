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
import org.eclipse.birt.report.model.parser.treebuild.ContentNode;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Factory class to create a parse state.
 */

public class ElementContentParseFactory {

	/**
	 * Creates a parse state with the given attributes.
	 * 
	 * @param tagName xml tag name
	 * @param handler module parser handler
	 * @param element the design element
	 * @param parent  the parent content node
	 * @return a parse state
	 */

	public static AbstractParseState createParseState(String tagName, ModuleParserHandler handler,
			DesignElement element, ContentNode parent) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.LIST_PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.EXPRESSION_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.XML_PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.STRUCTURE_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.METHOD_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.TEXT_PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.HTML_PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG)
				|| tagName.equalsIgnoreCase(DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG))
			return new PropertyContentState(handler, element, tagName, parent);

		return new ContentNodeState(tagName, handler, parent);
	}

}
