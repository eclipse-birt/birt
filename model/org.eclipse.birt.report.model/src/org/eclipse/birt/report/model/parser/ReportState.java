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

import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class provides parser state for the top-level Report element.
 * 
 */

public class ReportState extends DesignParseState
{

	/**
	 * The report design being built.
	 */

	protected ReportDesign design = null;

	/**
	 * Temporary copy of the list of custom colors. Used when parsing the custom
	 * color list.
	 */

	protected ArrayList colorList;

	/**
	 * Base64 encoding/decoding tools for embedded image.
	 */

	protected Base64 base = new Base64( );

	/**
	 * Constructs the report state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            The design parser handler.
	 */

	public ReportState( DesignParserHandler theHandler )
	{
		super( theHandler );
		design = theHandler.getDesign( );
	}

	/**
	 * Returns the design element being built.
	 * 
	 * @return the design element being built
	 */

	public DesignElement getElement( )
	{
		return design;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TRANSLATIONS_TAG ) )
			return new TranslationsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PARAMETERS_TAG ) )
			return new ParametersState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SOURCES_TAG ) )
			return new DataSourcesState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SETS_TAG ) )
			return new DataSetsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STYLES_TAG ) )
			return new StylesState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PAGE_SETUP_TAG ) )
			return new PageSetupState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COMPONENTS_TAG ) )
			return new SlotState( ReportDesign.COMPONENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.BODY_TAG ) )
			return new BodyState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.SCRATCH_PAD_TAG ) )
			return new SlotState( ReportDesign.SCRATCH_PAD_SLOT );
		return super.startElement( tagName );
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the Report
	 * tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	/**
	 * Parses the contents of the list of styles.
	 */

	class StylesState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STYLE_TAG ) )
				return new StyleState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of data sources.
	 */

	class DataSourcesState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.SCRIPT_DATA_SOURCE_TAG ) )
				return new ScriptDataSourceState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.ODA_DATA_SOURCE_TAG )
					|| tagName.equalsIgnoreCase( "extended-data-source" ) ) //$NON-NLS-1$
			{
				return new OdaDataSourceState( handler );
			}
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of data sets.
	 */

	class DataSetsState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.SCRIPT_DATA_SET_TAG ) )
				return new ScriptDataSetState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.ODA_DATA_SET_TAG )
					|| tagName.equalsIgnoreCase( "extended-data-set" ) ) //$NON-NLS-1$
			{
				return new OdaDataSetState( handler );
			}
			return super.startElement( tagName );
		}
	}

	/**
	 * Parse the contents of translations.
	 */
	class TranslationsState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.RESOURCE_TAG ) )
				return new ResourceState( );

			return super.startElement( tagName );
		}
	}

	/**
	 * Parse one user-defined Message.
	 */
	class ResourceState extends InnerParseState
	{

		private String key = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			key = attrs.getValue( DesignSchemaConstants.KEY_ATTRIB );

			if ( StringUtil.isBlank( key ) )
			{
				handler
						.semanticError( new DesignParserException(
								DesignParserException.DESIGN_EXCEPTION_MESSAGE_KEY_REQUIRED ) );
				return;
			}

			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.TRANSLATION_TAG ) )
				return new TranslationState( key );

			return super.startElement( tagName );
		}
	}

	/**
	 * Parse one entry for the user-defined Message.
	 */
	class TranslationState extends InnerParseState
	{

		private String resourceKey = null;

		private String locale = null;

		TranslationState( String key )
		{
			this.resourceKey = key;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			this.locale = attrs.getValue( DesignSchemaConstants.LOCALE_ATTRIB ); //$NON-NLS-1$
			if ( StringUtil.isBlank( locale ) )
			{
				// Translation without a locale or the locale is just a blank
				// string
				// is keyed by a null.
				this.locale = null;
			}

			// TODO: text format of the locale should be checked.
			// TODO: Should we define a ChoiceSet for the supported locales?
			// Also see ReportDesign.locale

			if ( design.findTranslation( resourceKey, locale ) != null )
			{
				handler
						.semanticError( new DesignParserException(
								DesignParserException.DESIGN_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE ) );
				return;
			}

			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			design.addTranslation( new Translation( resourceKey, locale, text
					.toString( ) ) );
			super.end( );
		}
	}

	/**
	 * Parses the contents of the page setup tag.
	 */

	class PageSetupState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.GRAPHIC_MASTER_PAGE_TAG ) )
				return new GraphicMasterPageState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.SIMPLE_MASTER_PAGE_TAG ) )
				return new SimpleMasterPageState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.PAGE_SEQUENCE_TAG ) )
				return new AnyElementState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the body tag that contains the list of top-level
	 * sections.
	 */

	class BodyState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
				return new GridItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
				return new FreeFormState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_TAG ) )
				return new ListItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
				return new TableItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
				return new LabelState( handler, design, ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( handler, design, ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
				return new DataItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new ExtendedItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
					|| tagName
							.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
				return new TextDataItemState( handler, design,
						ReportDesign.BODY_SLOT );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the components tag that contains the list of
	 * shared elements, which can be derived from.
	 */

	class SlotState extends InnerParseState
	{

		private int slotID;

		/**
		 * Constructor
		 * 
		 * @param slotID
		 *            the slot ID of the element
		 */

		public SlotState( int slotID )
		{
			this.slotID = slotID;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.BROWSER_CONTROL_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
				return new FreeFormState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
				return new DataItemState( handler, design, slotID );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new ExtendedItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
				return new GridItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
				return new LabelState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LINE_TAG ) )
				return new LineItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_TAG ) )
				return new ListItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.RECTANGLE_TAG ) )
				return new RectangleState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
				return new TableItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
					|| tagName
							.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
				return new TextDataItemState( handler, design, slotID );
			return super.startElement( tagName );
		}
	}
}
