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

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
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

public class ParseStateFactory
{

	/**
	 * Creates a parse state with the given attributes.
	 * 
	 * @param tagName
	 *            xml tag name
	 * @param handler
	 *            module parser handler
	 * @param element
	 *            the design element
	 * @param parent
	 *            the parent content node
	 * @return a parse state
	 */

	public static AbstractParseState createParseState( String tagName,
			ModuleParserHandler handler, DesignElement element,
			ContentNode parent )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.LIST_PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.EXPRESSION_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.XML_PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG )
				|| tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.HTML_PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG )
				|| tagName
						.equalsIgnoreCase( DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG ) )
			return new PropertyContentState( handler, element, tagName, parent );

		return new ContentNodeState( tagName, handler, parent );
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

	public static AbstractParseState createParseState( String tagName,
			IElementDefn allowedElementDefn, ModuleParserHandler handler,
			DesignElement container, IPropertyDefn propDefn )
	{
		if ( propDefn == null || !( (PropertyDefn) propDefn ).isElementType( )
				|| allowedElementDefn == null )
			return null;

		String elementName = allowedElementDefn.getName( );
		// support that allowedElementDefn refers to extension definition
		// directly, so add this handler
		if ( MetaDataDictionary.getInstance( ).getExtension( elementName ) != null )
		{
			if ( allowedElementDefn instanceof ExtensionElementDefn )
			{
				ExtensionElementDefn extContentDefn = (ExtensionElementDefn) allowedElementDefn;
				if ( PeerExtensionLoader.EXTENSION_POINT
						.equalsIgnoreCase( extContentDefn.getExtensionPoint( ) ) )
					elementName = ReportDesignConstants.EXTENDED_ITEM;
			}
		}

		String propName = propDefn.getName( );
		if ( tagName.equalsIgnoreCase( ( (ElementDefn) allowedElementDefn )
				.getXmlName( ) ) )
		{
			if ( ReportDesignConstants.TEXT_ITEM.equalsIgnoreCase( elementName ) )
				return new TextItemState( handler, container, propName );
			if ( ReportDesignConstants.GRID_ITEM.equalsIgnoreCase( elementName ) )
				return new GridItemState( handler, container, propName );
			if ( ReportDesignConstants.FREE_FORM_ITEM
					.equalsIgnoreCase( elementName ) )
				return new FreeFormState( handler, container, propName );
			if ( ReportDesignConstants.LIST_ITEM.equalsIgnoreCase( elementName ) )
				return new ListItemState( handler, container, propName );
			if ( ReportDesignConstants.TABLE_ITEM
					.equalsIgnoreCase( elementName ) )
				return new TableItemState( handler, container, propName );
			if ( ReportDesignConstants.LABEL_ITEM
					.equalsIgnoreCase( elementName ) )
				return new LabelState( handler, container, propName );
			if ( ReportDesignConstants.IMAGE_ITEM
					.equalsIgnoreCase( elementName ) )
				return new ImageState( handler, container, propName );
			if ( ReportDesignConstants.DATA_ITEM.equalsIgnoreCase( elementName ) )
				return new DataItemState( handler, container, propName );
			if ( ReportDesignConstants.EXTENDED_ITEM
					.equalsIgnoreCase( elementName ) )
				return new ExtendedItemState( handler, container, propName );
			if ( ReportDesignConstants.TEXT_DATA_ITEM
					.equalsIgnoreCase( elementName ) )
				return new TextDataItemState( handler, container, propName );
			if ( ReportDesignConstants.TEMPLATE_REPORT_ITEM
					.equalsIgnoreCase( elementName ) )
				return new TemplateReportItemState( handler, container,
						propName );
			if ( ReportDesignConstants.DIMENSION_ELEMENT
					.equalsIgnoreCase( elementName ) )
				return new DimensionState( handler, container, propName );
			if ( ReportDesignConstants.HIERARCHY_ELEMENT
					.equalsIgnoreCase( elementName ) )
				return new HierarchyState( handler, container, propName );
			if ( ReportDesignConstants.LEVEL_ELEMENT
					.equalsIgnoreCase( elementName ) )
				return new LevelState( handler, container, propName );
			if ( ReportDesignConstants.MEASURE_GROUP_ELEMENT
					.equalsIgnoreCase( elementName ) )
				return new MeasureGroupState( handler, container, propName );
			if ( ReportDesignConstants.MEASURE_ELEMENT
					.equalsIgnoreCase( elementName ) )
				return new MeasureState( handler, container, propName );
			if ( ReportDesignConstants.ACCESS_CONTROL
					.equalsIgnoreCase( elementName ) )
				return new AccessControlState( handler, container, propName );
			if ( ReportDesignConstants.VALUE_ACCESS_CONTROL
					.equalsIgnoreCase( elementName ) )
				return new ValueAccessControlState( handler, container,
						propName );

		}
		return null;
	}

}
