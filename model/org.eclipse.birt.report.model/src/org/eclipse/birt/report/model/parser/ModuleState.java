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

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class provides parser state for the top-level module element.
 */

public abstract class ModuleState extends DesignParseState
{

	/**
	 * 
	 */

	protected Module module = null;

	/**
	 * Constructs the module state with the module file parser handler.
	 * 
	 * @param theHandler
	 *            The module parser handler.
	 */

	public ModuleState( ModuleParserHandler theHandler )
	{
		super( theHandler );
		module = theHandler.getModule( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return module;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		String version = attrs.getValue( DesignSchemaConstants.VERSION_ATTRIB );

		if ( !StringUtil.isBlank( version ) )
		{
			int result;
			try
			{
				result = handler.versionUtil.compareVersion(
						DesignSchemaConstants.REPORT_VERSION, version );
			}
			catch ( NumberFormatException ex )
			{
				// The format of version string is invalid.

				DesignParserException e = new DesignParserException(
						new String[]{version},
						DesignParserException.DESIGN_EXCEPTION_INVALID_VERSION );
				throw new XMLParserException( e );
			}

			if ( result < 0 )
			{
				DesignParserException e = new DesignParserException(
						new String[]{version},
						DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_VERSION );
				throw new XMLParserException( e );
			}

			handler.setVersion( version );
		}

		module.getVersionManager( ).setVersion( handler.getVersion( ) );

		super.parseAttrs( attrs );
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
			if ( DesignSchemaConstants.SCRIPT_DATA_SOURCE_TAG
					.equalsIgnoreCase( tagName ) )
				return new ScriptDataSourceState( handler );
			if ( DesignSchemaConstants.ODA_DATA_SOURCE_TAG
					.equalsIgnoreCase( tagName )
					|| "extended-data-source".equalsIgnoreCase( tagName ) ) //$NON-NLS-1$
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
			if ( DesignSchemaConstants.SCRIPT_DATA_SET_TAG
					.equalsIgnoreCase( tagName ) )
				return new ScriptDataSetState( handler );
			if ( DesignSchemaConstants.ODA_DATA_SET_TAG
					.equalsIgnoreCase( tagName )
					|| "extended-data-set".equalsIgnoreCase( tagName ) ) //$NON-NLS-1$
			{
				return new OdaDataSetState( handler );
			}
			if ( DesignSchemaConstants.TEMPLATE_DATA_SET_TAG
					.equalsIgnoreCase( tagName ) )
				return new TemplateDataSetState( handler );
			if ( DesignSchemaConstants.JOINT_DATA_SET_TAG
					.equalsIgnoreCase( tagName ) )
				return new JointDataSetState( handler );
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
			if ( DesignSchemaConstants.RESOURCE_TAG.equalsIgnoreCase( tagName ) )
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

			if ( StringUtil.isEmpty( key ) )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
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
			if ( DesignSchemaConstants.TRANSLATION_TAG
					.equalsIgnoreCase( tagName ) )
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
			locale = attrs.getValue( DesignSchemaConstants.LOCALE_ATTRIB );
			locale = StringUtil.trimString( locale );

			// TODO: text format of the locale should be checked.
			// TODO: Should we define a ChoiceSet for the supported locales?
			// Also see ReportDesign.locale

			if ( module.findTranslation( resourceKey, locale ) != null )
			{
				handler
						.getErrorHandler( )
						.semanticError(
								new DesignParserException(
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
			module.addTranslation( new Translation( resourceKey, locale, text
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
			if ( DesignSchemaConstants.GRAPHIC_MASTER_PAGE_TAG
					.equalsIgnoreCase( tagName ) )
				return new GraphicMasterPageState( handler );
			if ( DesignSchemaConstants.SIMPLE_MASTER_PAGE_TAG
					.equalsIgnoreCase( tagName ) )
				return new SimpleMasterPageState( handler );
			if ( DesignSchemaConstants.PAGE_SEQUENCE_TAG
					.equalsIgnoreCase( tagName ) )
				return new AnyElementState( handler );
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
			if ( DesignSchemaConstants.BROWSER_CONTROL_TAG
					.equalsIgnoreCase( tagName ) )
				return new AnyElementState( handler );
			if ( DesignSchemaConstants.FREE_FORM_TAG.equalsIgnoreCase( tagName ) )
				return new FreeFormState( handler, module, slotID );
			if ( DesignSchemaConstants.DATA_TAG.equalsIgnoreCase( tagName ) )
				return new DataItemState( handler, module, slotID );
			if ( DesignSchemaConstants.EXTENDED_ITEM_TAG
					.equalsIgnoreCase( tagName ) )
				return new ExtendedItemState( handler, module, slotID );
			if ( DesignSchemaConstants.GRID_TAG.equalsIgnoreCase( tagName ) )
				return new GridItemState( handler, module, slotID );
			if ( DesignSchemaConstants.IMAGE_TAG.equalsIgnoreCase( tagName ) )
				return new ImageState( handler, module, slotID );
			if ( DesignSchemaConstants.INCLUDE_TAG.equalsIgnoreCase( tagName ) )
				return new AnyElementState( handler );
			if ( DesignSchemaConstants.LABEL_TAG.equalsIgnoreCase( tagName ) )
				return new LabelState( handler, module, slotID );
			if ( DesignSchemaConstants.TEXT_TAG.equalsIgnoreCase( tagName ) )
				return new TextItemState( handler, module, slotID );
			if ( DesignSchemaConstants.LINE_TAG.equalsIgnoreCase( tagName ) )
				return new LineItemState( handler, module, slotID );
			if ( DesignSchemaConstants.LIST_TAG.equalsIgnoreCase( tagName ) )
				return new ListItemState( handler, module, slotID );
			if ( DesignSchemaConstants.RECTANGLE_TAG.equalsIgnoreCase( tagName ) )
				return new RectangleState( handler, module, slotID );
			if ( DesignSchemaConstants.TABLE_TAG.equalsIgnoreCase( tagName ) )
				return new TableItemState( handler, module, slotID );
			if ( DesignSchemaConstants.TEXT_TAG.equalsIgnoreCase( tagName ) )
				return new TextItemState( handler, module, slotID );
			if ( DesignSchemaConstants.TOC_TAG.equalsIgnoreCase( tagName ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
					|| tagName
							.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
				return new TextDataItemState( handler, module, slotID );
			return super.startElement( tagName );
		}
	}

}
