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
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the action structure, provide compatability for the following members:
 * <p>
 * <table border="1">
 * <tr>
 * <th width="175">old</th>
 * <th width="134">new</th>
 * </tr>
 * <tr>
 * <td>bookmarklink</td>
 * <td rowspan="2">targetBookmark</td>
 * </tr>
 * <tr>
 * <td>drillThroughBookmarkLink</td>
 * </tr>
 * <tr>
 * <td>drillThroughParamBindings</td>
 * <td>paramBindings</td>
 * </tr>
 * <tr>
 * <td>drillThroughReportName</td>
 * <td>reportName</td>
 * </tr>
 * <tr>
 * <td>drillThroughSearch</td>
 * <td>search</td>
 * </tr>
 * <tr>
 * <td>drillThroughType</td>
 * <td>*dropped</td>
 * </tr>
 * <tr>
 * <td>hyperlink</td>
 * <td>uri</td>
 * </tr>
 * </table>
 * 
 */

public class ActionStructureState extends CompatibleStructureState {

	final static String DRILLTHROUGH_REPORT_NAME_MEMBER = "drillThroughReportName"; //$NON-NLS-1$
	final static String DRILLTHROUGH_BOOKMARK_LINK_MEMBER = "drillThroughBookmarkLink"; //$NON-NLS-1$
	final static String BOOKMARK_LINK_MEMBER = "bookmarkLink"; //$NON-NLS-1$
	final static String HYPERLINK_MEMBER = "hyperlink"; //$NON-NLS-1$
	final static String DRILLTHROUGH_SEARCH_MEMBER = "drillThroughSearch"; //$NON-NLS-1$
	final static String DRILLTHROUGH_PARAM_BINDINGS_MEMBER = "drillThroughParamBindings"; //$NON-NLS-1$

	/**
	 * 
	 * @param theHandler
	 * @param element
	 */
	public ActionStructureState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
		struct = new Action();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.EXPRESSION_TAG == tagValue)
			return new CompatibleActionExpressionState(handler, element, propDefn, struct);
		if (ParserSchemaConstants.PROPERTY_TAG == tagValue)
			return new CompatibleActionPropertyState(handler, element, propDefn, struct);
		if (ParserSchemaConstants.LIST_PROPERTY_TAG == tagValue)
			return new CompatibleActionListPropertyState(handler, element, propDefn, struct);

		return super.startElement(tagName);
	}

	/**
	 * Compatible for followings:
	 * <p>
	 * <li>drillThroughSearch => search
	 * <li>drillThroughParamBindings =>paramBindings
	 */

	static class CompatibleActionListPropertyState extends CompatibleListPropertyState {

		CompatibleActionListPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}
	}

	/**
	 * Compatible for followings:
	 * <p>
	 * <li>drillThroughReportName => reportName
	 */

	static class CompatibleActionPropertyState extends CompatiblePropertyState {

		CompatibleActionPropertyState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			if (DRILLTHROUGH_REPORT_NAME_MEMBER.equalsIgnoreCase(name)) {
				String value = text.toString();
				setMember(struct, propDefn.getName(), Action.REPORT_NAME_MEMBER, value);
				return;
			}

			super.end();
		}
	}

	/**
	 * Compatible for followings:
	 * <p>
	 * <li>hyperlink => uri
	 * <li>bookmarkLink/drillThroughBookmarkLink => targetBookmark
	 */

	static class CompatibleActionExpressionState extends CompatibleMiscExpressionState {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#
		 * versionConditionalJumpTo()
		 */

		protected AbstractParseState versionConditionalJumpTo() {
			if (handler.versionNumber < VersionUtil.VERSION_3_2_1 && (Action.URI_MEMBER.equalsIgnoreCase(name))) {
				CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(handler, element);
				state.setName(name);
				state.struct = struct;
				state.propDefn = propDefn;
				return state;
			}

			if (handler.versionNumber < VersionUtil.VERSION_3_2_1
					&& Action.TARGET_BOOKMARK_MEMBER.equalsIgnoreCase(name)) {
				CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(handler, element);
				state.setName(name);
				state.struct = struct;
				state.propDefn = propDefn;
				return state;
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.parser.CompatibleMiscExpressionState#end()
		 */

		public void end() throws SAXException {
			if (handler.versionNumber < VersionUtil.VERSION_3_2_0)
				super.end();
			else {
				String value = text.toString();

				if (StringUtil.isBlank(value))
					return;

				doEnd(value);
			}
		}

		CompatibleActionExpressionState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
				IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.model.parser.ExpressionState#parseAttrs(org.xml.sax.
		 * Attributes)
		 */

		public void parseAttrs(Attributes attrs) throws XMLParserException {
			super.parseAttrs(attrs);

			if (DRILLTHROUGH_REPORT_NAME_MEMBER.equalsIgnoreCase(name)) {
				name = Action.REPORT_NAME_MEMBER;
			} else if (BOOKMARK_LINK_MEMBER.equalsIgnoreCase(name)
					|| DRILLTHROUGH_BOOKMARK_LINK_MEMBER.equalsIgnoreCase(name)) {
				name = Action.TARGET_BOOKMARK_MEMBER;
			} else if (HYPERLINK_MEMBER.equalsIgnoreCase(name)) {
				name = Action.URI_MEMBER;
			}
		}
	}
}
