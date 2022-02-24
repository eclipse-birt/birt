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

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.parser.treebuild.ContentNode;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Factory class to create a parse state.
 */

class ParseStateFactoryImpl {

	/**
	 * Creates a parse state with the given attributes.
	 * 
	 * @param tagName xml tag name
	 * @param handler module parser handler
	 * @param element the design element
	 * @param parent  the parent content node
	 * @return a parse state
	 */

	public final AbstractParseState createParseState(String tagName, ModuleParserHandler handler, DesignElement element,
			ContentNode parent) {
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

	/**
	 * Creates a parse state with the given attributes. It is used to create a
	 * ReportElementState when parsing a element property value.
	 * 
	 * @param tagName
	 * @param allowedElementDefn
	 * @param handler
	 * @param container
	 * @param propDefn
	 * 
	 * @return a parse state
	 */

	public final AbstractParseState createParseState(String tagName, IElementDefn allowedElementDefn,
			ModuleParserHandler handler, DesignElement container, IPropertyDefn propDefn) {
		if (propDefn == null || !((PropertyDefn) propDefn).isElementType() || allowedElementDefn == null)
			return null;

		String elementName = allowedElementDefn.getName();
		// support that allowedElementDefn refers to extension definition
		// directly, so add this handler
		if (MetaDataDictionary.getInstance().getExtension(elementName) != null) {
			if (allowedElementDefn instanceof ExtensionElementDefn) {
				ExtensionElementDefn extContentDefn = (ExtensionElementDefn) allowedElementDefn;
				if (PeerExtensionLoader.EXTENSION_POINT.equalsIgnoreCase(extContentDefn.getExtensionPoint()))
					elementName = ReportDesignConstants.EXTENDED_ITEM;
			}
		}

		String propName = propDefn.getName();
		ElementDefn tagElementDefn = (ElementDefn) MetaDataDictionary.getInstance().getElementByXmlName(tagName);

		AbstractParseState state = null;

		if (tagName.equalsIgnoreCase(((ElementDefn) allowedElementDefn).getXmlName())) {
			state = createParseState(elementName, handler, container, propName);
		} else if (tagElementDefn != null && tagElementDefn.isKindOf(allowedElementDefn)) {
			state = createParseState(tagElementDefn.getName(), handler, container, propName);
		}

		return state;
	}

	protected AbstractParseState createParseState(String elementName, ModuleParserHandler handler,
			DesignElement container, String propName) {
		if (StringUtil.isBlank(elementName) || container == null || StringUtil.isBlank(propName))
			return null;
		if (ReportDesignConstants.TEXT_ITEM.equalsIgnoreCase(elementName))
			return new TextItemState(handler, container, propName);
		if (ReportDesignConstants.GRID_ITEM.equalsIgnoreCase(elementName))
			return new GridItemState(handler, container, propName);
		if (ReportDesignConstants.FREE_FORM_ITEM.equalsIgnoreCase(elementName))
			return new FreeFormState(handler, container, propName);
		if (ReportDesignConstants.LIST_ITEM.equalsIgnoreCase(elementName))
			return new ListItemState(handler, container, propName);
		if (ReportDesignConstants.TABLE_ITEM.equalsIgnoreCase(elementName))
			return new TableItemState(handler, container, propName);
		if (ReportDesignConstants.LABEL_ITEM.equalsIgnoreCase(elementName))
			return new LabelState(handler, container, propName);
		if (ReportDesignConstants.IMAGE_ITEM.equalsIgnoreCase(elementName))
			return new ImageState(handler, container, propName);
		if (ReportDesignConstants.DATA_ITEM.equalsIgnoreCase(elementName))
			return new DataItemState(handler, container, propName);
		if (ReportDesignConstants.EXTENDED_ITEM.equalsIgnoreCase(elementName))
			return new ExtendedItemState(handler, container, propName);
		if (ReportDesignConstants.TEXT_DATA_ITEM.equalsIgnoreCase(elementName))
			return new TextDataItemState(handler, container, propName);
		if (ReportDesignConstants.TEMPLATE_REPORT_ITEM.equalsIgnoreCase(elementName))
			return new TemplateReportItemState(handler, container, propName);
		if (ReportDesignConstants.TABULAR_DIMENSION_ELEMENT.equalsIgnoreCase(elementName))
			return new TabularDimensionState(handler, container, propName);
		if (ReportDesignConstants.TABULAR_HIERARCHY_ELEMENT.equalsIgnoreCase(elementName))
			return new TabularHierarchyState(handler, container, propName);
		if (ReportDesignConstants.TABULAR_LEVEL_ELEMENT.equalsIgnoreCase(elementName))
			return new TabularLevelState(handler, container, propName);
		if (ReportDesignConstants.TABULAR_MEASURE_GROUP_ELEMENT.equalsIgnoreCase(elementName))
			return new TabularMeasureGroupState(handler, container, propName);
		if (ReportDesignConstants.TABULAR_MEASURE_ELEMENT.equalsIgnoreCase(elementName))
			return new TabularMeasureState(handler, container, propName);
		if (ReportDesignConstants.ODA_DIMENSION_ELEMENT.equalsIgnoreCase(elementName))
			return new OdaDimensionState(handler, container, propName);
		if (ReportDesignConstants.ODA_HIERARCHY_ELEMENT.equalsIgnoreCase(elementName))
			return new OdaHierarchyState(handler, container, propName);
		if (ReportDesignConstants.ODA_LEVEL_ELEMENT.equalsIgnoreCase(elementName))
			return new OdaLevelState(handler, container, propName);
		if (ReportDesignConstants.ODA_MEASURE_GROUP_ELEMENT.equalsIgnoreCase(elementName))
			return new OdaMeasureGroupState(handler, container, propName);
		if (ReportDesignConstants.ODA_MEASURE_ELEMENT.equalsIgnoreCase(elementName))
			return new OdaMeasureState(handler, container, propName);
		if (ReportDesignConstants.MEMBER_VALUE_ELEMENT.equalsIgnoreCase(elementName))
			return new MemberValueState(handler, container, propName);
		if (ReportDesignConstants.FILTER_CONDITION_ELEMENT.equalsIgnoreCase(elementName))
			return new FilterConditionElementState(handler, container, propName);
		if (ReportDesignConstants.SORT_ELEMENT.equalsIgnoreCase(elementName))
			return new SortElementState(handler, container, propName);
		if (ReportDesignConstants.MULTI_VIEWS.equalsIgnoreCase(elementName))
			return new MultiViewsState(handler, container, propName);
		if (ReportDesignConstants.VARIABLE_ELEMENT.equalsIgnoreCase(elementName))
			return new VariableElementState(handler, container, propName);
		if (ReportDesignConstants.DATA_GROUP_ELEMENT.equalsIgnoreCase(elementName))
			return new DataGroupState(handler, container, propName);
		return null;
	}

	/**
	 * 
	 * @param tagName
	 * @param handler
	 * @param container
	 * @param propDefn
	 * @return
	 */
	public final AbstractParseState createParseState(String tagName, ModuleParserHandler handler,
			DesignElement container, IPropertyDefn propDefn) {
		String propName = propDefn.getName();
		ElementDefn tagElementDefn = (ElementDefn) MetaDataDictionary.getInstance().getElementByXmlName(tagName);
		String elementName = tagElementDefn == null ? null : tagElementDefn.getName();
		return createParseState(elementName, handler, container, propName);
	}

	/**
	 * Creates some data set state for some special data set.
	 * 
	 * @param tagName
	 * @param handler
	 * @param container
	 * @param slotID
	 * @return
	 */
	public AbstractParseState createDataSetState(int tagName, ModuleParserHandler handler, DesignElement container,
			int slotID) {
		return null;
	}

	/**
	 * Creates some data set state for some special data source.
	 * 
	 * @param tagName
	 * @param handler
	 * @param container
	 * @param slotID
	 * @return
	 */
	public AbstractParseState createDataSourceState(int tagName, ModuleParserHandler handler, DesignElement container,
			int slotID) {
		return null;
	}

	/**
	 * Creates some data set state for some special cube.
	 * 
	 * @param tagName
	 * @param handler
	 * @param container
	 * @param slotID
	 * @return
	 */
	public AbstractParseState createCubeState(int tagName, ModuleParserHandler handler, DesignElement container,
			int slotID) {
		return null;
	}

	public AbstractParseState createExternalMetaDataState(ModuleParserHandler handler, DesignElement element) {
		XmlPropertyState state = new XmlPropertyState(handler, element);
		state.setName(IReportDesignModel.EXTERNAL_METADATA_PROP);
		return state;
	}
}
