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
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class provides parser state for the top-level Report element.
 * 
 */

public class ReportState extends ModuleState
{

	/**
	 * Constructs the report state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            The design parser handler.
	 */

	public ReportState( DesignParserHandler theHandler )
	{
		super( theHandler );
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
        if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
            return new CompatibleReportPropertyState( handler, getElement() );
		return super.startElement( tagName );
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
				result = StringUtil.compareVersion(
						DesignSchemaConstants.REPORT_VERSION, version );
			}
			catch ( Exception ex )
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

		super.parseAttrs( attrs );
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
				return new TextItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.GRID_TAG ) )
				return new GridItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.FREE_FORM_TAG ) )
				return new FreeFormState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_TAG ) )
				return new ListItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TABLE_TAG ) )
				return new TableItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LABEL_TAG ) )
				return new LabelState( handler, module, ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.IMAGE_TAG ) )
				return new ImageState( handler, module, ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_TAG ) )
				return new DataItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.INCLUDE_TAG ) )
				return new AnyElementState( handler );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TOC_TAG ) )
				return new AnyElementState( handler );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.EXTENDED_ITEM_TAG ) )
				return new ExtendedItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.MULTI_LINE_DATA_TAG )
					|| tagName
							.equalsIgnoreCase( DesignSchemaConstants.TEXT_DATA_TAG ) )
				return new TextDataItemState( handler, module,
						ReportDesign.BODY_SLOT );
			return super.startElement( tagName );
		}
	}

	
}
