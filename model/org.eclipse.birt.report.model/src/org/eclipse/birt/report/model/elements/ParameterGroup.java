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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MultiElementSlot;

/**
 * This class represents a parameter group. A parameter group creates a visual
 * grouping of parameters. The developer controls the order that groups appear
 * in the UI, and the order in which parameters appear in the group. The
 * BIRT-provided runtime UI will may choose to allow the user to expand &
 * collapse parameter groups independently.
 *  
 */

public class ParameterGroup extends DesignElement
{

	public static final String START_EXPANDED_PROP = "startExpanded"; //$NON-NLS-1$
	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$
	public static final String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Constant for the Report Items slot within a container.
	 */

	public static final int PARAMETERS_SLOT = 0;

	/**
	 * The report items slot.
	 */

	MultiElementSlot parameters = new MultiElementSlot( );

	/**
	 * Default constructor.
	 */

	public ParameterGroup( )
	{
	}

	/**
	 * Makes a clone of this parameter group element.
	 * 
	 * @return the cloned paremeter group element.
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone( ) throws CloneNotSupportedException
	{
		ParameterGroup group = (ParameterGroup) super.clone( );
		group.parameters = (MultiElementSlot) parameters.copy( group,
				PARAMETERS_SLOT );
		return group;
	}

	/**
	 * Constructs the parameter group with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ParameterGroup( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert slot == PARAMETERS_SLOT;
		return parameters;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitParameterGroup( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.PARAMETER_GROUP_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle( ReportDesign design )
	{
		return handle( design );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param design
	 *            the report design
	 * @return an API handle for this element
	 */

	public ParameterGroupHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new ParameterGroupHandle( design, this );
		}
		return (ParameterGroupHandle) handle;
	}

}