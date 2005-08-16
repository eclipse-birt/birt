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
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Writes the design to an XML design file that follows the BIRT design schema.
 * Uses a visitor pattern to traverse each element. BIRT elements support
 * inheritance in several forms. Because of this, the design writer writes only
 * those properties "local" to the element being written -- it does not write
 * inherited properties.
 * <p>
 * Because the XML schema was designed for to be understood by humans, the
 * schema is not a literal representation of the model. Instead, properties are
 * named and grouped in a way that is easiest to explain and understand. This
 * means that the writer has to do a bit more work to write the design, the the
 * extra work here is well worth the savings to the many customers who will read
 * the design format.
 * 
 */

public class DesignWriter extends ModuleWriter
{

	/**
	 * The design context used to convert units.
	 */

	private ReportDesign design;

	/**
	 * Constructs a writer with the specified design.
	 * 
	 * @param design
	 *            the internal representation of the design
	 */

	public DesignWriter( ReportDesign design )
	{
		this.design = design;
	}


	/**
	 * Write the top-level Report tag, and the properties and contents of the
	 * report itself.
	 * 
	 * @param obj
	 *            the object to write
	 */

	public void visitReportDesign( ReportDesign obj )
	{
		writer.startElement( DesignSchemaConstants.REPORT_TAG );
		writer.attribute( DesignSchemaConstants.XMLNS_ATTRIB,
				DEFAULT_NAME_SPACE );
		writer.attribute( DesignSchemaConstants.VERSION_ATTRIB,
				DesignSchemaConstants.REPORT_VERSION );
		property( obj, ReportDesign.AUTHOR_PROP );
		property( obj, ReportDesign.HELP_GUIDE_PROP );
		property( obj, ReportDesign.CREATED_BY_PROP );
		property( obj, ReportDesign.UNITS_PROP );
		property( obj, ReportDesign.REFRESH_RATE_PROP );
		property( obj, ReportDesign.BASE_PROP );
		property( obj, ReportDesign.INCLUDE_RESOURCE_PROP );

		resourceKey( obj, ReportDesign.TITLE_ID_PROP, ReportDesign.TITLE_PROP );
		property( obj, ReportDesign.COMMENTS_PROP );

		resourceKey( obj, ReportDesign.DESCRIPTION_ID_PROP,
				ReportDesign.DESCRIPTION_PROP );

		property( obj, ReportDesign.INITIALIZE_METHOD );
		property( obj, ReportDesign.BEFORE_FACTORY_METHOD );
		property( obj, ReportDesign.AFTER_FACTORY_METHOD );
		property( obj, ReportDesign.BEFORE_OPEN_DOC_METHOD );
		property( obj, ReportDesign.AFTER_OPEN_DOC_METHOD );
		property( obj, ReportDesign.BEFORE_CLOSE_DOC_METHOD );
		property( obj, ReportDesign.AFTER_CLOSE_DOC_METHOD );
		property( obj, ReportDesign.BEFORE_RENDER_METHOD );
		property( obj, ReportDesign.AFTER_RENDER_METHOD );

		// include libraries and scripts

		writeStructureList( obj, ReportDesign.INCLUDE_LIBRARIES_PROP );
		writeSimpleStructureList( obj, ReportDesign.INCLUDE_SCRIPTS_PROP,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, ReportDesign.CONFIG_VARS_PROP );

		writeArrangedContents( obj, ReportDesign.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		List list = (List) obj.getLocalProperty( design,
				ReportDesign.COLOR_PALETTE_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					ReportDesign.COLOR_PALETTE_PROP );

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

		String[] resourceKeys = design.getTranslationResourceKeys( );
		if ( resourceKeys != null && resourceKeys.length > 0 )
		{
			writer.startElement( DesignSchemaConstants.TRANSLATIONS_TAG );

			for ( int i = 0; i < resourceKeys.length; i++ )
			{
				writer.startElement( DesignSchemaConstants.RESOURCE_TAG );
				writer.attribute( DesignSchemaConstants.KEY_ATTRIB,
						resourceKeys[i] );

				List translations = design.getTranslations( resourceKeys[i] );
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

		writeContents( obj, ReportDesign.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, ReportDesign.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, ReportDesign.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );
		writeContents( obj, ReportDesign.BODY_SLOT,
				DesignSchemaConstants.BODY_TAG );
		writeContents( obj, ReportDesign.SCRATCH_PAD_SLOT,
				DesignSchemaConstants.SCRATCH_PAD_TAG );

		// Embedded images

		list = (List) obj.getLocalProperty( design, ReportDesign.IMAGES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					ReportDesign.IMAGES_PROP );

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

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */
	
	protected Module getModule( )
	{
		return design;
	}
}