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

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a group tag in list or table.
 * 
 */

abstract class GroupState extends ReportElementState {

	protected GroupElement group = null;

	/**
	 * Constructs the group state with the design parser handler, the container
	 * element and the container slot of the group element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public GroupState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return group;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		// get the "id" of the element

		initSimpleElement(attrs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */

	public void end() throws SAXException {
		if (handler.versionNumber < VersionUtil.VERSION_3_2_8 && handler.versionNumber > VersionUtil.VERSION_3_0_0) {
			Object tmpValue = group.getLocalProperty(handler.module, IGroupElementModel.KEY_EXPR_PROP);
			if (tmpValue == null) {
				super.end();
				return;
			}

			String keyExpr = ((Expression) tmpValue).getStringExpression();
			if (!StringUtil.isBlank(keyExpr)) {
				TOC toc = (TOC) group.getLocalProperty(handler.module, IGroupElementModel.TOC_PROP);
				if (toc == null) {
					toc = StructureFactory.createTOC();
					group.setProperty(IGroupElementModel.TOC_PROP, toc);
				}

				if (toc.getExpression() == null)
					try {
						toc.setExpression(keyExpr);
					} catch (SemanticException e) {
						assert false;
					}

			}
		}

		super.end();
	}
}
