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
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.ThemeStyleNameValidator;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.interfaces.IThemeModel;

/**
 * This class represents a theme in the library.
 * 
 */

public class Theme extends ReferenceableElement implements IThemeModel
{
	/**
	 * Constructor.
	 */

	public Theme( )
	{
		super( );
		initSlots( );
	}

	/**
	 * Constructor with the element name.
	 * 
	 * @param theName
	 *            the element name
	 */

	public Theme( String theName )
	{
		super( theName );
		initSlots( );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.report.model.elements.ElementVisitor)
	 */

	public void apply( ElementVisitor visitor )
	{
		visitor.visitTheme( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName( )
	{
		return ReportDesignConstants.THEME_ITEM;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle( Module module )
	{
		return handle( module );
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module
	 *            the report design of the row
	 * 
	 * @return an API handle for this element
	 */

	public ThemeHandle handle( Module module )
	{
		if ( handle == null )
		{
			handle = new ThemeHandle( module, this );
		}
		return (ThemeHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot( int slot )
	{
		assert ( slot == STYLES_SLOT );
		return slots[STYLES_SLOT];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.ReferenceableElement#adjustDeliveryPath(org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */

	protected void adjustDeliveryPath( NotificationEvent ev )
	{
		ev.setDeliveryPath( NotificationEvent.ELEMENT_CLIENT );
	}

	/**
	 * Returns the style with the given name.
	 * 
	 * @param styleName
	 *            the style name
	 * @return the corresponding style
	 */

	public StyleElement findStyle( String styleName )
	{
		StyleElement style = null;

		for ( int i = 0; i < slots[STYLES_SLOT].getCount( ); i++ )
		{
			StyleElement tmpStyle = (StyleElement) slots[STYLES_SLOT]
					.getContent( i );
			if ( tmpStyle.getName( ).equalsIgnoreCase( styleName ) )
			{
				style = tmpStyle;
				break;
			}
		}
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected List checkContent( Module module, DesignElement tmpContainer,
			int slotId, DesignElement content )
	{
		List tmpErrors = super.checkContent( module, tmpContainer, slotId, content );
		if ( !tmpErrors.isEmpty( ) )
			return tmpErrors;

		tmpErrors.addAll( ThemeStyleNameValidator.getInstance( )
				.validateForAddingStyle( (ThemeHandle) getHandle( module ),
						content.getName( ) ) );

		return tmpErrors;

	}
}
