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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.validators.ElementReferenceValidator;

/**
 * Base class for all report items. Represents anything that can be placed in a
 * layout container. Items have a size and position that are used in some of the
 * containers.
 *  
 */

public abstract class ReportItem extends StyledElement
{

	/**
	 * Name of the x position property.
	 */

	public static final String X_PROP = "x"; //$NON-NLS-1$

	/**
	 * Name of the y position property.
	 */

	public static final String Y_PROP = "y"; //$NON-NLS-1$

	/**
	 * Name of the height dimension property.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$

	/**
	 * Name of the width position property.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Name of the data set property. This references a data set within the
	 * report. Provides the scope for items that reference database data.
	 */

	public static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the bookmark property. The bookmark is the target of hyperlinks
	 * within the report.
	 */

	public static final String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$

	/**
	 * Name of the TOC entry expression property.
	 */

	public static final String TOC_PROP = "toc"; //$NON-NLS-1$

	/**
	 * Name of the visibility property. 
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$

	/**
	 * Name of the on-create property. It is for a script executed when the
	 * element is created in the Factory. Called after the item is created, but
	 * before the item is saved to the report document file.
	 */

	public static final String ON_CREATE_METHOD = "onCreate"; //$NON-NLS-1$

	/**
	 * Name of the on-render property. It is for a script Executed when the
	 * element is prepared for rendering in the Presentation engine.
	 */

	public static final String ON_RENDER_METHOD = "onRender"; //$NON-NLS-1$

	/**
	 * The property name of the data set parameter binding elements that bind
	 * input parameters to expressions.
	 */

	public static final String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public ReportItem( )
	{
	}

	/**
	 * Constructs the report item with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public ReportItem( String theName )
	{
		super( theName );
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param design
	 *            the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getDataSetElement( ReportDesign design )
	{
		ElementRefValue dataSetRef = (ElementRefValue) getProperty( design,
				DATA_SET_PROP );
		if ( dataSetRef == null )
			return null;
		return dataSetRef.getElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		// Check the element reference of dataSet property

		list.addAll( ElementReferenceValidator.getInstance().validate( design, this, DATA_SET_PROP ) );
		
		list.addAll( validateStructureList( design, PARAM_BINDINGS_PROP ) );

		return list;
	}
}
