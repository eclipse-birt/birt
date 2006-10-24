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
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.ICompatibleReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PeerExtensionLoader;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the Extended Item (extended item) tag.
 */

public class ExtendedItemState extends ReportItemState
{

	/**
	 * The extended item being created.
	 */

	public ExtendedItem element;

	/**
	 * Constructs the extended item state with the design parser handler, the
	 * container element and the container slot of the extended item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public ExtendedItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new ExtendedItem( );

		parseExtensionName( attrs, true );
		if ( element.getExtDefn( ) == null )
			element.initializeContentTree( );

		boolean nameRequired = element.getDefn( ).getNameOption( ) == MetaDataConstants.REQUIRED_NAME;
		initElement( attrs, nameRequired );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( element.getExtDefn( ) != null )
		{
			IElementDefn defn = element.getDefn( );

			// read the slot definition with the extension definition
			if ( defn != null && defn.isContainer( ) )
			{
				for ( int i = 0; i < defn.getSlotCount( ); i++ )
				{
					ISlotDefn slotDefn = defn.getSlot( i );
					if ( tagName.equalsIgnoreCase( slotDefn.getXmlName( ) ) )
						return new ExtendedItemSlotState( element, i );
				}
			}
			return super.startElement( tagName );
		}
		else
		{
			return ElementContentParseFactory.createParseState( tagName,
					handler, element, element.getContentTree( ) );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		if ( handler.versionNumber >= VersionUtil.VERSION_3_2_1 )
		{
			super.end( );
			return;
		}

		try
		{
			element.initializeReportItem( handler.module );
		}
		catch ( ExtendedElementException e )
		{
			return;
		}

		Object reportItem = element.getExtendedElement( );

		if ( reportItem != null && reportItem instanceof ICompatibleReportItem )
		{
			List jsExprs = ( (ICompatibleReportItem) reportItem )
					.getRowExpressions( );
			Map updatedExprs = DataBoundColumnUtil.handleJavaExpression(
					jsExprs, element, handler.module, handler.tempValue );
			( (ICompatibleReportItem) reportItem )
					.updateRowExpressions( updatedExprs );
		}

		super.end( );
	}

	/**
	 * Parses the contents in extended item slot.
	 */

	class ExtendedItemSlotState extends AbstractParseState
	{

		protected int slotId;

		protected ExtendedItem extendedItem;

		/**
		 * 
		 * @param container
		 * @param slot
		 */

		public ExtendedItemSlotState( ExtendedItem container, int slot )
		{
			extendedItem = container;
			slotId = slot;
		}

		public XMLParserHandler getHandler( )
		{
			return handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			IElementDefn extDefn = extendedItem.getDefn( );
			assert extDefn != null;

			ISlotDefn slotDefn = extDefn.getSlot( slotId );
			assert slotDefn != null;

			List allowedElements = slotDefn.getContentElements( );
			for ( int i = 0; i < allowedElements.size( ); i++ )
			{
				ElementDefn contentDefn = (ElementDefn) allowedElements.get( i );
				String elementName = contentDefn.getName( );
				// allow the slottype refers to extension definition directly,
				// so add this handler
				if ( MetaDataDictionary.getInstance( ).getElement( elementName ) == null )
				{
					if ( contentDefn instanceof ExtensionElementDefn )
					{
						ExtensionElementDefn extContentDefn = (ExtensionElementDefn) contentDefn;
						if ( PeerExtensionLoader.EXTENSION_POINT
								.equalsIgnoreCase( extContentDefn
										.getExtensionPoint( ) ) )
							elementName = ReportDesignConstants.EXTENDED_ITEM;
					}
				}
				if ( tagName.equalsIgnoreCase( contentDefn.getXmlName( ) ) )
				{
					if ( ReportDesignConstants.TEXT_ITEM
							.equalsIgnoreCase( elementName ) )
						return new TextItemState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.GRID_ITEM
							.equalsIgnoreCase( elementName ) )
						return new GridItemState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.FREE_FORM_ITEM
							.equalsIgnoreCase( elementName ) )
						return new FreeFormState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.LIST_ITEM
							.equalsIgnoreCase( elementName ) )
						return new ListItemState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.TABLE_ITEM
							.equalsIgnoreCase( elementName ) )
						return new TableItemState( handler, extendedItem,
								slotId );
					if ( ReportDesignConstants.LABEL_ITEM
							.equalsIgnoreCase( elementName ) )
						return new LabelState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.IMAGE_ITEM
							.equalsIgnoreCase( elementName ) )
						return new ImageState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.DATA_ITEM
							.equalsIgnoreCase( elementName ) )
						return new DataItemState( handler, extendedItem, slotId );
					if ( ReportDesignConstants.EXTENDED_ITEM
							.equalsIgnoreCase( elementName ) )
						return new ExtendedItemState( handler, extendedItem,
								slotId );
					if ( ReportDesignConstants.MULTI_LINE_DATA_ITEM
							.equalsIgnoreCase( elementName )
							|| ReportDesignConstants.TEXT_DATA_ITEM
									.equalsIgnoreCase( elementName ) )
						return new TextDataItemState( handler, extendedItem,
								slotId );
					if ( ReportDesignConstants.TEMPLATE_REPORT_ITEM
							.equalsIgnoreCase( elementName ) )
						return new TemplateReportItemState( handler,
								extendedItem, slotId );
				}
			}

			return super.startElement( tagName );
		}
	}
}