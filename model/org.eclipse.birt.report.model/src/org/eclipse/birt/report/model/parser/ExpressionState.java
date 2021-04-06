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
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "expression" tag. If the element property or structure member is
 * expression type, then we will use the expression not the property tag.
 */

class ExpressionState extends PropertyState {

	private static final int HIGHLIGHT_TEST_EXPR = "highlightTestExpr" //$NON-NLS-1$
			.toLowerCase().hashCode();
	private static final int VALUE_EXPR = "valueExpr".toLowerCase().hashCode(); //$NON-NLS-1$
	private static final int IMAGE_NAME_PROP = ImageItem.IMAGE_NAME_PROP.toLowerCase().hashCode();

	private static final int EXPRESSION_MEMBER = ComputedColumn.EXPRESSION_MEMBER.toLowerCase().hashCode();

	private static final int CONTENT_TYPE_EXPR = "contentTypeExpr" //$NON-NLS-1$
			.toLowerCase().hashCode();
	private static final int MAP_TEST_EXPR = "mapTestExpr".toLowerCase() //$NON-NLS-1$
			.hashCode();
	private static final int TOC_PROP = IReportItemModel.TOC_PROP.toLowerCase().hashCode();

	private static final int GROUP_TOC_PROP = IGroupElementModel.TOC_PROP.toLowerCase().hashCode();

	/**
	 * 
	 * @param theHandler
	 * @param element
	 */
	ExpressionState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * AbstractPropertyState(DesignParserHandler theHandler, DesignElement element,
	 * String propName, IStructure struct)
	 */

	ExpressionState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn, IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);

		exprType = attrs.getValue(DesignSchemaConstants.TYPE_TAG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#generalJumpTo
	 * ()
	 */

	protected AbstractParseState generalJumpTo() {
		int nameValue = name.toLowerCase().hashCode();
		if ((element instanceof TextDataItem) && CONTENT_TYPE_EXPR == nameValue) {
			CompatibleRenamedPropertyState state = new CompatibleRenamedPropertyState(handler, element,
					"contentTypeExpr"); //$NON-NLS-1$
			state.setName(ITextDataItemModel.CONTENT_TYPE_PROP);
			return state;
		}
		if (MAP_TEST_EXPR == nameValue)
			return new CompatibleTestExpreState(handler, element, IStyleModel.MAP_RULES_PROP);
		return super.generalJumpTo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.parser.AbstractPropertyState#
	 * versionConditionalJumpTo()
	 */

	protected AbstractParseState versionConditionalJumpTo() {
		if (handler.versionNumber < VersionUtil.VERSION_3_2_10) {
			if (element instanceof ReportItem) {
				if (TOC_PROP == nameValue) {
					CompatibleTOCPropertyState state = new CompatibleTOCPropertyState(handler, element);
					state.setName(IReportItemModel.TOC_PROP);
					return state;
				}
			}
			if (element instanceof GroupElement) {
				if (GROUP_TOC_PROP == nameValue) {
					CompatibleTOCPropertyState state = new CompatibleTOCPropertyState(handler, element);
					state.setName(IGroupElementModel.TOC_PROP);
					return state;
				}
			}
		}
		if (HIGHLIGHT_TEST_EXPR == nameValue) {
			if (handler.isVersion(VersionUtil.VERSION_0) || handler.isVersion(VersionUtil.VERSION_1_0_0))
				return new CompatibleTestExpreState(handler, element, IStyleModel.HIGHLIGHT_RULES_PROP);
		}
		if (element instanceof DataItem && VALUE_EXPR == nameValue && struct == null
				&& handler.versionNumber < VersionUtil.VERSION_3_1_0) {
			CompatibleDataValueExprState state = new CompatibleDataValueExprState(handler, element);
			state.setName(IDataItemModel.RESULT_SET_COLUMN_PROP);
			return state;
		}
		if (propDefn == null)
			propDefn = element.getPropertyDefn(name);

		if (handler.versionNumber < VersionUtil.VERSION_3_2_1 && element instanceof ImageItem && struct == null
				&& IMAGE_NAME_PROP == nameValue) {
			PropertyState state = new PropertyState(handler, element);
			state.setName(name);
			return state;
		}

		if (struct instanceof ParamBinding && handler.versionNumber < VersionUtil.VERSION_3_2_3) {
			CompatibleParamBindingValueState state = new CompatibleParamBindingValueState(handler, element, propDefn,
					struct);
			state.setName(name);
			return state;
		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_3) {
			if (struct instanceof ComputedColumn && element instanceof ReportItem && EXPRESSION_MEMBER == nameValue) {
				CompatibleBoundColumnExprState state = new CompatibleBoundColumnExprState(handler, element, propDefn,
						struct);
				state.setName(name);
				return state;
			}

			CompatibleMiscExpressionState state = new CompatibleMiscExpressionState(handler, element);
			state.setName(name);
			state.struct = struct;
			state.propDefn = propDefn;
			return state;
		}
		return super.versionConditionalJumpTo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		doEnd(value);
	}

	/**
	 * Constructs an expression object if necessary. THe condition is
	 * context-related.
	 * 
	 * @param value the string value
	 */

	protected void doEnd(Object value) {
		Object toSet = value;

		// in some old design file, the property/expression tags may be messed
		// up.

		PropertyDefn tmpPropDefn = null;
		if (struct != null) {
			StructureDefn structDefn = (StructureDefn) struct.getDefn();
			tmpPropDefn = (StructPropertyDefn) structDefn.getMember(name);
		} else
			tmpPropDefn = element.getPropertyDefn(name);

		if (tmpPropDefn != null && tmpPropDefn.allowExpression())
			toSet = new Expression(value, exprType);

		super.doEnd(toSet);
	}

	@Override
	protected void backwardCompatible(Object value) {

	}
}
