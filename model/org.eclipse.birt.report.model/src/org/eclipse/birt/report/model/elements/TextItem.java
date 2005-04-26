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
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * This class represents a text item in the report.
 * 
 */

public class TextItem extends ReportItem implements ITextItemModel
{

	/**
	 * Constructs a text item.
	 */

	public TextItem( )
	{
	}

	/**
	 * Constructs a text item with the given name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public TextItem( String theName )
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
		visitor.visitTextItem( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.TEXT_ITEM;
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
	 * Returns an handle for this text element.
	 * 
	 * @param design
	 *            the report design
	 * 
	 * @return a handle for this element
	 */

	public TextItemHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new TextItemHandle( design, this );
		}
		return (TextItemHandle) handle;
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
			String text = getStringProperty( design, TextItem.CONTENT_PROP );

			if ( !StringUtil.isBlank( text ) )
			{
				text = limitStringLength( text );
				displayLabel += "(\"" + text + "\")"; //$NON-NLS-1$//$NON-NLS-2$
				return displayLabel;
			}

			String resourceKey = getStringProperty( design,
					TextItem.CONTENT_RESOURCE_KEY_PROP );
			if ( !StringUtil.isBlank( resourceKey ) )
			{
				resourceKey = limitStringLength( resourceKey );
				displayLabel += "(\"" + resourceKey + "\")"; //$NON-NLS-1$//$NON-NLS-2$
				return displayLabel;
			}
		}
		return displayLabel;
	}
}
