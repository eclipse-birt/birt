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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * This class represents a multi-line data item element. The multi-line data
 * item displays blocks of text retrieved from the database, from a file, or
 * from an expression. The text can be plain text, HTML or RTF. The format of
 * the text can be fixed at design time, or can be dynamically selected at run
 * time to match the format of the incoming text. The user can search text
 * within the multi-line data item.
 *  
 */

public class TextDataItem extends ReportItem
{

	/**
	 * Name of the value expression property that gives an expression that
	 * provides the text. The expression is most often simply a reference to a
	 * query column. But, it can also reference a report parameter, a formula, a
	 * special value, a file, or other data item..
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the content type expression property. It is an optional property
	 * that defines the type of text. Applies to both static text and the value
	 * expression. The choices are:
	 * <ul>
	 * <li>¡°Auto¡± (default): BIRT will infer the format as explained above.
	 * <li>¡°Plain¡±: Plain text.
	 * <li>¡°HTML¡±: HTML format.
	 * <li>¡°RTF¡±: Rich Text Format.
	 * <li>Expression: an expression that returns one of the above strings.
	 * </ul>
	 * Note that the content type is an expression, not a choice. It the
	 * developer wishes to specify the format statically, the value must be
	 * enclosed in quotes to make it a valid expression.
	 *  
	 */

	public static final String CONTENT_TYPE_EXPR_PROP = "contentTypeExpr"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public TextDataItem( )
	{
	}

	/**
	 * Constructs the multi-line data with an optional name.
	 * 
	 * @param theName
	 *            optional item name
	 */

	public TextDataItem( String theName )
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
		visitor.visitTextDataItem( this );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.TEXT_DATA_ITEM;
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
	 * 
	 * @return an API handle for this element
	 */

	public TextDataHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new TextDataHandle( design, this );
		}
		return (TextDataHandle) handle;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(org.eclipse.birt.report.model.elements.ReportDesign)
	 */

	public List validate( ReportDesign design )
	{
		List list = super.validate( design );

		Object valueExpr = getProperty( design, VALUE_EXPR_PROP );
		if ( valueExpr == null )
		{
			list.add( new PropertyValueException( this, VALUE_EXPR_PROP,
					valueExpr, PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}
}