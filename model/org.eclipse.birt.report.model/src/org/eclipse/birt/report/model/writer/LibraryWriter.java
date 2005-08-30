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

package org.eclipse.birt.report.model.writer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Represents the writer for writing library file.
 */

public class LibraryWriter extends ModuleWriter
{

	private Library library = null;

	/**
	 * Contructs one library writer with the library instance.
	 * 
	 * @param library
	 *            the library to write
	 */

	public LibraryWriter( Library library )
	{
		this.library = library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */
	protected Module getModule( )
	{
		return library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitLibrary(org.eclipse.birt.report.model.elements.Library)
	 */
	public void visitLibrary( Library obj )
	{
		writer.startElement( DesignSchemaConstants.LIBRARY_TAG );
		writer.attribute( DesignSchemaConstants.XMLNS_ATTRIB,
				DEFAULT_NAME_SPACE );
		writer.attribute( DesignSchemaConstants.VERSION_ATTRIB,
				DesignSchemaConstants.REPORT_VERSION );
		property( obj, Library.AUTHOR_PROP );
		property( obj, Library.HELP_GUIDE_PROP );
		property( obj, Library.CREATED_BY_PROP );
		property( obj, Library.UNITS_PROP );
		property( obj, Library.BASE_PROP );
		property( obj, Library.INCLUDE_RESOURCE_PROP );

		resourceKey( obj, Library.TITLE_ID_PROP, Library.TITLE_PROP );
		property( obj, Library.COMMENTS_PROP );

		resourceKey( obj, Library.DESCRIPTION_ID_PROP, Library.DESCRIPTION_PROP );

		property( obj, Library.INITIALIZE_METHOD );

		// include libraries and scripts

		// Library including library is not supported.
		//
		// writeStructureList( obj, Library.INCLUDE_LIBRARIES_PROP );
		writeSimpleStructureList( obj, Library.INCLUDE_SCRIPTS_PROP,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, Library.CONFIG_VARS_PROP );

		writeArrangedContents( obj, Library.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, Library.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, Library.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		List list = (List) obj.getLocalProperty( getModule( ),
				Library.COLOR_PALETTE_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					Library.COLOR_PALETTE_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				CustomColor color = (CustomColor) list.get( i );

				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
				property( color, CustomColor.NAME_MEMBER );
				property( color, CustomColor.COLOR_MEMBER );
				resourceKey( color, CustomColor.DISPLAY_NAME_ID_MEMBER,
						CustomColor.DISPLAY_NAME_MEMBER );
				writer.endElement( );
			}
			writer.endElement( );
		}

		// Translations. ( Custom-defined messages )

		String[] resourceKeys = getModule( ).getTranslationResourceKeys( );
		if ( resourceKeys != null && resourceKeys.length > 0 )
		{
			writer.startElement( DesignSchemaConstants.TRANSLATIONS_TAG );

			for ( int i = 0; i < resourceKeys.length; i++ )
			{
				writer.startElement( DesignSchemaConstants.RESOURCE_TAG );
				writer.attribute( DesignSchemaConstants.KEY_ATTRIB,
						resourceKeys[i] );

				List translations = getModule( ).getTranslations(
						resourceKeys[i] );
				for ( int j = 0; j < translations.size( ); j++ )
				{
					writer.startElement( DesignSchemaConstants.TRANSLATION_TAG );

					Translation translation = (Translation) translations
							.get( j );

					writer.attribute( DesignSchemaConstants.LOCALE_ATTRIB,
							translation.getLocale( ) );
					writer.text( translation.getText( ) );
					writer.endElement( );
				}
				writer.endElement( );
			}
			writer.endElement( );
		}

		writeContents( obj, Library.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, Library.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, Library.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );

		// Embedded images

		list = (List) obj.getLocalProperty( getModule( ), Library.IMAGES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					Library.IMAGES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				EmbeddedImage image = (EmbeddedImage) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( image, EmbeddedImage.NAME_MEMBER );
				property( image, EmbeddedImage.TYPE_MEMBER );

				try
				{
					if ( image.getData( ) != null )
					{
						byte[] data = base.encode( image.getData( ) );
						String value = new String( data, EmbeddedImage.CHARSET );

						if ( value.length( ) < IndentableXMLWriter.MAX_CHARS_PER_LINE )
							writeEntry( DesignSchemaConstants.PROPERTY_TAG,
									EmbeddedImage.DATA_MEMBER, value, false );
						else
							writeLongIndentText(
									DesignSchemaConstants.PROPERTY_TAG,
									EmbeddedImage.DATA_MEMBER, value );
					}
				}
				catch ( UnsupportedEncodingException e )
				{
					assert false;
				}
				writer.endElement( );
			}
			writer.endElement( );
		}
		writer.endElement( );
	}
}
