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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.XMLParserHandler;

/**
 * This class parses the contents of the list of parameters.
 * 
 */

public class ParametersState extends AbstractParseState
{

	private ModuleParserHandler handler;
	private DesignElement container;
	private int slotID;
	private boolean isReport = true;

	/**
	 * Constructs the parameters state with the design file parser handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	public ParametersState( ModuleParserHandler handler )
	{
		this.handler = handler;
		container = handler.getModule( );
		slotID = IModuleModel.PARAMETER_SLOT;
	}

	/**
	 * Constructs the parameters state with the design parser handler, the container
	 * element and the container slot of the parameters.
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
		this.handler = handler;
		this.container = container;
		this.slotID = slotID;
		isReport = container instanceof ReportDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( isReport )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.PARAMETER_GROUP_TAG ) )
				return new ParameterGroupState( handler );
			if( tagName.equalsIgnoreCase( DesignSchemaConstants.CASCADING_PARAMETER_GROUP_TAG ) )
				return new CascadingParameterGroupState( handler );
		}
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.SCALAR_PARAMETER_TAG ) )
			return new ScalarParameterState( handler, container, slotID );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.FILTER_PARAMETER_TAG ) )
			return new AnyElementState( handler );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.LIST_PARAMETER_TAG ) )
			return new AnyElementState( handler );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.TABLE_PARAMETER_TAG ) )
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
