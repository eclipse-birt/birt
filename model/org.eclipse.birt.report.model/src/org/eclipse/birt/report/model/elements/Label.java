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
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * This class represents a static label in the report. The label text can be
 * defined in the label, or the label can reference an external message file so
 * that the label can be localized.
 * 
 */

public class Label extends ReportItem
{

	/**
	 * Name of the text property. This property contains the non-localized text
	 * for the label.
	 */

	public static final String TEXT_PROP = "text"; //$NON-NLS-1$

	/**
	 * Name of the message ID property. This property contains the message ID
	 * used to localize the text of the label.
	 */

	public static final String TEXT_ID_PROP = "textID"; //$NON-NLS-1$

	/**
	 * Name of the help text property
	 */

	public static final String HELP_TEXT_PROP = "helpText"; //$NON-NLS-1$

	/**
	 * Name of the help text id property
	 */

	public static final String HELP_TEXT_ID_PROP = "helpTextID"; //$NON-NLS-1$

	/**
	 * Name of the action property
	 */
	
    public static final String ACTION_PROP = "action"; //$NON-NLS-1$
    
	/**
	 * Default constructor.
	 */

	public Label( )
	{
	}

	/**
	 * Constructs the label item with an optional name.
	 * 
	 * @param theName
	 *            the optional name
	 */

	public Label( String theName )
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
		visitor.visitLabel( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.LABEL_ITEM;
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

	public LabelHandle handle( ReportDesign design )
	{
		if ( handle == null )
		{
			handle = new LabelHandle( design, this );
		}
		return (LabelHandle) handle;
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
			String text = handle( design ).getText( );
			if ( !StringUtil.isBlank( text ) )
			{
				text = limitStringLength( text );
				displayLabel += "(\"" + text + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
				return displayLabel;
			}

			String resourceKey = handle( design ).getTextKey( );
			if ( !StringUtil.isBlank( resourceKey ) )
			{
				resourceKey = limitStringLength( resourceKey );
				displayLabel += "(" + resourceKey + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				return displayLabel;
			}
		}
		return displayLabel;
	}

}
