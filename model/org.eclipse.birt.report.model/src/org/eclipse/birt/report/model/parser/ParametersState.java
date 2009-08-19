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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserHandler;

/**
 * This class parses the contents of the list of parameters.
 * 
 */

public class ParametersState extends SlotState
{

	/**
	 * Constructs the parameters state with the design parser handler, the
	 * container element and the container slot of the parameters.
	 * 
	 * @param handler
	 *            the design file parser handler.
	 * @param container
	 *            the container of this the parameter and parameter group. Here,
	 *            the container can only be either ReportDesign or
	 *            ParameterGroup.
	 * @param slotID
	 *            the slot id of the slot where the parameter/parametergroup is
	 *            stored.
	 */

	public ParametersState( ModuleParserHandler handler,
			DesignElement container, int slotID )
	{
		super( handler, container, slotID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		int tagValue = tagName.toLowerCase( ).hashCode( );

		if ( ParserSchemaConstants.PARAMETER_GROUP_TAG == tagValue )
			return new ParameterGroupState( handler );
		if ( ParserSchemaConstants.CASCADING_PARAMETER_GROUP_TAG == tagValue )
			return new CascadingParameterGroupState( handler );
		if ( ParserSchemaConstants.SCALAR_PARAMETER_TAG == tagValue )
			return new ScalarParameterState( handler, container, slotID );
		if ( ParserSchemaConstants.DYNAMIC_FILTER_PARAMETER_TAG == tagValue )
			return new DynamicFilterParameterState( handler, container, slotID );
		if ( ParserSchemaConstants.FILTER_PARAMETER_TAG == tagValue )
			return new AnyElementState( handler );
		if ( ParserSchemaConstants.LIST_PARAMETER_TAG == tagValue )
			return new AnyElementState( handler );
		if ( ParserSchemaConstants.TABLE_PARAMETER_TAG == tagValue )
			return new AnyElementState( handler );
		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler( )
	{
		return handler;
	}
}
