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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;

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

	public ReportState( ModuleParserHandler theHandler )
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
			return new SlotState( IModuleModel.COMPONENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.BODY_TAG ) )
			return new BodyState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.SCRATCH_PAD_TAG ) )
			return new SlotState( IReportDesignModel.SCRATCH_PAD_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
			return new PropertyState( handler, getElement( ) );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITIONS_TAG ) )
			return new TemplateParameterDefinitionsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.CUBES_TAG ) )
			return new CubesState( IReportDesignModel.CUBE_SLOT );
		return super.startElement( tagName );
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
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.TEXT_TAG == tagValue )
				return new TextItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.GRID_TAG == tagValue  )
				return new GridItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.FREE_FORM_TAG == tagValue  )
				return new FreeFormState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.LIST_TAG == tagValue  )
				return new ListItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.TABLE_TAG == tagValue  )
				return new TableItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.LABEL_TAG == tagValue  )
				return new LabelState( handler, module, ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.IMAGE_TAG == tagValue  )
				return new ImageState( handler, module, ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.DATA_TAG == tagValue  )
				return new DataItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.INCLUDE_TAG == tagValue  )
				return new AnyElementState( handler );
			if ( ParserSchemaConstants.TOC_TAG == tagValue  )
				return new AnyElementState( handler );
			if ( ParserSchemaConstants.EXTENDED_ITEM_TAG == tagValue  )
				return new ExtendedItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.MULTI_LINE_DATA_TAG == tagValue 
					|| ParserSchemaConstants.TEXT_DATA_TAG == tagValue  )
				return new TextDataItemState( handler, module,
						ReportDesign.BODY_SLOT );
			if ( ParserSchemaConstants.TEMPLATE_REPORT_ITEM_TAG == tagValue  )
				return new TemplateReportItemState( handler, module, ReportDesign.BODY_SLOT );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of styles.
	 */

	private class StylesState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.STYLE_TAG == tagValue  )
				return new StyleState( handler );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses the contents of the list of data sources.
	 */

	class TemplateParameterDefinitionsState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			int tagValue = tagName.toLowerCase( ).hashCode( );
			if ( ParserSchemaConstants.TEMPLATE_PARAMETER_DEFINITION_TAG == tagValue )
				return new TemplateParameterDefinitionState( handler, module, ReportDesign.TEMPLATE_PARAMETER_DEFINITION_SLOT );
			return super.startElement( tagName );
		}
	}

}
