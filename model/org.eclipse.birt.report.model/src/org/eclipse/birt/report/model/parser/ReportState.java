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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.elements.structures.CustomColor;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		setProperty( ReportDesign.AUTHOR_PROP, attrs,
				DesignSchemaConstants.AUTHOR_ATTRIB );
		setProperty( ReportDesign.HELP_GUIDE_PROP, attrs,
				DesignSchemaConstants.HELP_GUIDE_ATTRIB );
		setProperty( ReportDesign.CREATED_BY_PROP, attrs,
				DesignSchemaConstants.CREATED_BY_ATTRIB );
		setProperty( ReportDesign.UNITS_PROP, attrs,
				DesignSchemaConstants.UNITS_ATTRIB );
		setProperty( ReportDesign.REFRESH_RATE_PROP, attrs,
				DesignSchemaConstants.REFRESH_RATE_ATTRIB );
		setProperty( ReportDesign.BASE_PROP, attrs,
				DesignSchemaConstants.BASE_ATTRIB );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TITLE_TAG ) )
			return new ExternalTextState( handler, design,
					ReportDesign.TITLE_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COMMENTS_TAG ) )
			return new TextState( handler, design, ReportDesign.COMMENTS_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DESCRIPTION_TAG ) )
			return new ExternalTextState( handler, design,
					ReportDesign.DESCRIPTION_PROP );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TRANSLATIONS_TAG ) )
			return new TranslationsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.KEYWORDS_TAG ) )
			return new AnyElementState( handler );
		if ( DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase( tagName ) )
			return new MethodState( handler, design );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CUSTOM_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEMPLATE_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDES_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEST_CONFIGS_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PARAMETERS_TAG ) )
			return new ParametersState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SOURCES_TAG ) )
			return new DataSourcesState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SETS_TAG ) )
			return new DataSetsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COLOR_PALETTE_TAG ) )
			return new ColorPaletteState( );
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
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CONFIG_VARS_TAG ) )
			return new ConfigVarsState( );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.MESSAGE_CATALOG_TAG ) )
			return new AnyElementState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGES_TAG ) )
			return new ImagesState( );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_LIBRARY_TAG ) )
			return new IncludeLibraryState( );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_SCRIPT_TAG ) )
			return new IncludeScriptState( );
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
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_DATA_SOURCE_TAG ) )
				return new ExtendedDataSourceState( handler );
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
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_DATA_SET_TAG ) )
				return new ExtendedDataSetState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the custom color list.
	 */

	class ColorPaletteState extends InnerParseState
	{

		ColorPaletteState( )
		{
			colorList = (ArrayList) design.getLocalProperty( handler.design,
					ReportDesign.COLOR_PALETTE_PROP );
			if ( colorList == null )
				colorList = new ArrayList( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.CUSTOM_COLOR_TAG ) )
				return new CustomColorState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			design.setProperty( ReportDesign.COLOR_PALETTE_PROP, colorList );
			colorList = null;
			super.end( );
		}
	}

	/**
	 * Parses the content of the include library, and adds the created include
	 * library into the include library list.
	 */

	class IncludeLibraryState extends TextState
	{

		IncludeLibraryState( )
		{
			super( ReportState.this.handler, null,
					IncludeLibrary.FILE_NAME_MEMBER );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			String value = text.toString( );
			IncludeLibrary lib = new IncludeLibrary( );
			valueSet = lib;
			PropertyDefn prop = valueSet.getObjectDefn( ).findProperty(
					valueName );
			assert prop != null;
			valueSet.setProperty( prop, value );

			ArrayList includeLibraries = null;
			includeLibraries = (ArrayList) design.getLocalProperty(
					handler.design, ReportDesign.INCLUDE_LIBRARIES );
			if ( includeLibraries == null )
				includeLibraries = new ArrayList( );
			includeLibraries.add( lib );
			design.setProperty( ReportDesign.INCLUDE_LIBRARIES,
					includeLibraries );
		}
	}

	/**
	 * Parses one include script.
	 */

	class IncludeScriptState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs
					.getValue( DesignSchemaConstants.FILE_NAME_ATTRIB );

			IncludeScript script = new IncludeScript( name );

			ArrayList includeScripts = null;
			includeScripts = (ArrayList) design.getLocalProperty(
					handler.design, ReportDesign.INCLUDE_SCRIPTS );
			if ( includeScripts == null )
				includeScripts = new ArrayList( );
			includeScripts.add( script );
			design.setProperty( ReportDesign.INCLUDE_SCRIPTS, includeScripts );
		}
	}

	/**
	 * Parses one custom color.
	 */

	class CustomColorState extends InnerParseState
	{

		protected CustomColor color;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */

		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			String name = attrs.getValue( DesignSchemaConstants.NAME_ATTRIB );
			if ( StringUtil.isBlank( name ) )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.COLOR_NAME_REQUIRED ) );
				return;
			}
			String colorValue = getAttrib( attrs,
					DesignSchemaConstants.COLOR_ATTRIB );
			if ( colorValue == null )
			{
				handler.semanticError( new DesignParserException(
						DesignParserException.RGB_REQUIRED ) );
				return;
			}
			color = new CustomColor( name, colorValue );
			colorList.add( color );

			super.parseAttrs( attrs );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( color == null )
				return new AnyElementState( getHandler( ) );

			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.DISPLAY_NAME_TAG ) )
				return new ExternalTextState( handler, color,
						CustomColor.DISPLAY_NAME_MEMBER );

			return super.startElement( tagName );
		}

	}

	/**
	 * Parses the contents of the config variable list.
	 */

	class ConfigVarsState extends InnerParseState
	{

		ArrayList configVars = null;

		ConfigVarsState( )
		{
			configVars = (ArrayList) design.getLocalProperty( handler.design,
					ReportDesign.CONFIG_VARS_PROP );
			if ( configVars == null )
				configVars = new ArrayList( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.CONFIG_VAR_TAG ) )
				return new ConfigVarState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			design.setProperty( ReportDesign.CONFIG_VARS_PROP, configVars );
			super.end( );
		}

		/**
		 * Parses one config variable.
		 */

		class ConfigVarState extends TextState
		{

			ConfigVarState( )
			{
				super( ReportState.this.handler, null,
						ConfigVariable.VALUE_MEMBER );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
			 */

			public void parseAttrs( Attributes attrs )
					throws XMLParserException
			{
				String name = attrs
						.getValue( DesignSchemaConstants.NAME_ATTRIB );
				ConfigVariable configVar = new ConfigVariable( );
				configVar.setName( name );
				valueSet = configVar;
				configVars.add( configVar );
				super.parseAttrs( attrs );
			}
		}
	}

	/**
	 * Parses the contents of the embedded images list.
	 */

	class ImagesState extends InnerParseState
	{

		ArrayList images = null;

		ImagesState( )
		{
			images = (ArrayList) design.getLocalProperty( handler.design,
					ReportDesign.IMAGES_PROP );
			if ( images == null )
				images = new ArrayList( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			design.setProperty( ReportDesign.IMAGES_PROP, images );
			super.end( );
		}

		/**
		 * Parses one embedded image.
		 */

		class ImageState extends TextState
		{

			ImageState( )
			{
				super( ReportState.this.handler, null,
						EmbeddedImage.DATA_MEMBER );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
			 */

			public void parseAttrs( Attributes attrs )
					throws XMLParserException
			{
				String name = attrs
						.getValue( DesignSchemaConstants.NAME_ATTRIB );
				String type = attrs
						.getValue( DesignSchemaConstants.TYPE_ATTRIB );
				EmbeddedImage image = new EmbeddedImage( );
				valueSet = image;
				setMember( image, ReportDesign.IMAGES_PROP,
						EmbeddedImage.NAME_MEMBER, name );
				setMember( image, ReportDesign.IMAGES_PROP,
						EmbeddedImage.TYPE_MEMBER, type );
				images.add( image );
				super.parseAttrs( attrs );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
			 */

			public void end( ) throws SAXException
			{
				String value = text.toString( );
				PropertyDefn prop = valueSet.getObjectDefn( ).findProperty(
						valueName );
				assert prop != null;
				if ( value != null )
				{
					byte[] data = null;

					try
					{
						data = base.decode( value
								.getBytes( EmbeddedImage.CHARSET ) );
						valueSet.setProperty( prop, new String( data,
								EmbeddedImage.CHARSET ) );
					}
					catch ( UnsupportedEncodingException e )
					{
						valueSet.setProperty( prop, null );
						assert false;
					}

				}
				else
					valueSet.setProperty( prop, null );
			}
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
				handler.semanticError( new DesignParserException(
						DesignParserException.MESSAGE_KEY_REQUIRED ) );
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
				handler.semanticError( new DesignParserException(
						DesignParserException.DUPLICATE_TRANSLATION_LOCALE ) );
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
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CHART_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.MATRIX_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new ExtendedItemState( handler, design,
						ReportDesign.BODY_SLOT );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG ) )
				return new MultiLineDataItemState( handler, design,
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
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CHART_TAG ) )
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
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.MATRIX_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.RECTANGLE_TAG ) )
				return new RectangleState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
				return new TableItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_TAG ) )
				return new TextItemState( handler, design, slotID );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.TOGGLE_IMAGE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG ) )
				return new MultiLineDataItemState( handler, design, slotID );
			return super.startElement( tagName );
		}
	}
}

