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

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * This class represents a data item element: one that displays the value of an
 * expression.
 * 
 */

public class DataItem extends ReportItem
{

	/**
	 * Name of the distinct property.
	 */

	public static final String DISTINCT_PROP = "distinct"; //$NON-NLS-1$

	/**
	 * Name of the distinct reset property.
	 */

	public static final String DISTINCT_RESET_PROP = "distinctReset"; //$NON-NLS-1$

	/**
	 * Name of the value expression property.
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the help text property.
	 */

	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text key property.
	 */

	public static final String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$

    public static final String ACTION_PROP = "action"; //$NON-NLS-1$
      
	/**
	 * Default constructor.
	 */

	public DataItem( )
	{
	}

	/**
	 * Constructs the data item with an optional name.
	 * 
	 * @param theName
	 *            optional item name
	 */

	public DataItem( String theName )
	{
		super( theName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitDataItem( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.DATA_ITEM;
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
	 * @return an API handle for this element.
	 */

	public DataItemHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new DataItemHandle( design, this );
		}
		return (DataItemHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getDisplayLabel(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      int)
	 */

	public String getDisplayLabel( ReportDesign design, int level )
	{
		String displayLabel = super.getDisplayLabel( design, level );
		if ( level == DesignElement.FULL_LABEL )
		{
			String valueExpr = handle( design ).getValueExpr( );
			if ( !StringUtil.isBlank( valueExpr ) )
			{
				valueExpr = limitStringLength( valueExpr );
				displayLabel += "(" + valueExpr + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return displayLabel;
	}
}
