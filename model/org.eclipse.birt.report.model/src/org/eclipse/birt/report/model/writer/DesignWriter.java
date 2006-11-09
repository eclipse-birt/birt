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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
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

		super.visitReportDesign( obj );

		property( obj, IReportDesignModel.REFRESH_RATE_PROP );
		property( obj, IModuleModel.INITIALIZE_METHOD );
		property( obj, IReportDesignModel.BEFORE_FACTORY_METHOD );
		property( obj, IReportDesignModel.AFTER_FACTORY_METHOD );
		property( obj, IReportDesignModel.BEFORE_RENDER_METHOD );
		property( obj, IReportDesignModel.AFTER_RENDER_METHOD );
		property( obj, IModuleModel.THEME_PROP );
		resourceKey( obj, IDesignElementModel.DISPLAY_NAME_ID_PROP,
				IDesignElementModel.DISPLAY_NAME_PROP );
		property( obj, IReportDesignModel.ICON_FILE_PROP );
		property( obj, IReportDesignModel.CHEAT_SHEET_PROP );
		property( obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP );

		// include libraries and scripts

		writeStructureList( obj, IModuleModel.LIBRARIES_PROP );
		writeSimpleStructureList( obj, IModuleModel.INCLUDE_SCRIPTS_PROP,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, IModuleModel.CONFIG_VARS_PROP );

		writeContents( obj, IReportDesignModel.TEMPLATE_PARAMETER_DEFINITION_SLOT,
				DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG );
		writeArrangedContents( obj, IModuleModel.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, IModuleModel.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, IModuleModel.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		writeCustomColors( obj );

		// Translations. ( Custom-defined messages )

		writeTranslations( obj );

		writeContents( obj, IReportDesignModel.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, IModuleModel.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, IModuleModel.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );
		writeContents( obj, IReportDesignModel.BODY_SLOT,
				DesignSchemaConstants.BODY_TAG );
		writeContents( obj, IReportDesignModel.SCRATCH_PAD_SLOT,
				DesignSchemaConstants.SCRATCH_PAD_TAG );

		// write thumbnail

		try
		{
			byte[] thumbnail = design.getThumbnail( );
			if ( thumbnail != null )
			{
				byte[] data = base.encode( design.getThumbnail( ) );
				String value = new String( data, OdaDesignerState.CHARSET );

				if ( value.length( ) < IndentableXMLWriter.MAX_CHARS_PER_LINE )
					writeEntry( DesignSchemaConstants.PROPERTY_TAG,
							IReportDesignModel.THUMBNAIL_PROP, value, false );
				else
					writeLongIndentText( DesignSchemaConstants.PROPERTY_TAG,
							IReportDesignModel.THUMBNAIL_PROP, value );
			}
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}

		// Embedded images

		writeEmbeddedImages( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */

	protected Module getModule( )
	{
		return design;
	}
}