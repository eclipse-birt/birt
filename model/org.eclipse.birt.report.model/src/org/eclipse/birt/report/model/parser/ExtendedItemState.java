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

import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.namespace.ModuleNameHelper;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Extended Item (extended item) tag.
 */

public class ExtendedItemState extends ReportItemState {

	/**
	 * The extended item being created.
	 */

	public ExtendedItem element;

	/**
	 * Constructs the extended item state with the design parser handler, the
	 * container element and the container slot of the extended item.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public ExtendedItemState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs extended item state with the design parser handler, the container
	 * element and the container property name of the report element.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public ExtendedItemState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new ExtendedItem();
		handler.addExtendedItem(element);

		parseExtensionName(attrs, true);

		// parse extension version
		String extensionVersion = attrs.getValue(DesignSchemaConstants.EXTENSION_VERSION_ATTRIB);
		setProperty(IExtendedItemModel.EXTENSION_VERSION_PROP, extensionVersion);

		boolean nameRequired = element.getDefn().getNameOption() == MetaDataConstants.REQUIRED_NAME;
		initElement(attrs, nameRequired);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#startElement(
	 * java.lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		if (element.getExtDefn() != null) {
			return super.startElement(tagName);
		}
		return ParseStateFactory.getInstance().createParseState(tagName, handler, element,
				element.getExtensibilityProvider().getContentTree());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end() throws SAXException {
		try {
			element.initializeReportItem(handler.module);
		} catch (ExtendedElementException e) {
			return;
		}

		Object reportItem = element.getExtendedElement();

		// try to resolve level and adjust aggregation on property
		if (handler.versionNumber < VersionUtil.VERSION_3_2_13) {
			IElementDefn levelDefn = MetaDataDictionary.getInstance().getElement(ReportDesignConstants.LEVEL_ELEMENT);
			IElementDefn eDefn = element.getDefn();
			List<IElementPropertyDefn> properties = eDefn.getProperties();
			for (int i = 0; i < properties.size(); i++) {
				ElementPropertyDefn defn = (ElementPropertyDefn) properties.get(i);
				if (defn.getTypeCode() != IPropertyType.ELEMENT_REF_TYPE)
					continue;
				if (!defn.getTargetElementType().isKindOf(levelDefn))
					continue;
				ElementRefValue value = (ElementRefValue) element.getLocalProperty(handler.module, defn);
				if (value != null && !value.isResolved()) {
					Level level = ((ModuleNameHelper) handler.module.getNameHelper()).findCachedLevel(value.getName());
					if (level != null) {
						value.resolve(level);
						level.addClient(element, defn.getName());
					}
				}
			}

			List columnBindings = (List) element.getLocalProperty(handler.module,
					IReportItemModel.BOUND_DATA_COLUMNS_PROP);
			if (columnBindings != null) {
				for (int i = 0; i < columnBindings.size(); i++) {
					ComputedColumn column = (ComputedColumn) columnBindings.get(i);
					List aggregationList = column.getAggregateOnList();
					if (aggregationList == null)
						continue;
					for (int j = 0; j < aggregationList.size(); j++) {
						String aggregationOn = (String) aggregationList.get(j);
						if (aggregationOn != null) {
							Level level = ((ModuleNameHelper) handler.module.getNameHelper())
									.findCachedLevel(aggregationOn);
							if (level != null)
								aggregationList.set(j, level.getFullName());
						}
					}
				}
			}
		}

		if (handler.versionNumber >= VersionUtil.VERSION_3_2_1) {
			if (reportItem != null && reportItem instanceof ICompatibleReportItem) {
				((ICompatibleReportItem) reportItem).handleCompatibilityIssue();
			}
			super.end();
			return;
		}

		if (reportItem != null && reportItem instanceof ICompatibleReportItem) {
			List<String> jsExprs = ((ICompatibleReportItem) reportItem).getRowExpressions();
			Map<String, String> updatedExprs = BoundDataColumnUtil.handleJavaExpression(jsExprs, element,
					handler.module, handler.tempValue);
			((ICompatibleReportItem) reportItem).updateRowExpressions(updatedExprs);
			((ICompatibleReportItem) reportItem).handleCompatibilityIssue();
		}

		super.end();
	}
}