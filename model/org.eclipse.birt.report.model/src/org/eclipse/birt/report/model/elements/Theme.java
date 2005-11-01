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
import org.eclipse.birt.report.model.core.MultiElementSlot;
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
	}

	/**
	 * Holds the cells that reside directly on the row.
	 */

	protected ContainerSlot contents = new MultiElementSlot( );

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
		return contents;
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
	 * @param name
	 *            the style name
	 * @return the corresponding style
	 */

	public StyleElement findStyle( String name )
	{
		StyleElement style = null;

		for ( int i = 0; i < contents.getCount( ); i++ )
		{
			StyleElement tmpStyle = (StyleElement) contents.getContent( i );
			if ( tmpStyle.getName( ).equalsIgnoreCase( name ) )
			{
				style = tmpStyle;
				break;
			}
		}
		return style;
	}

	/**
	 * Makes a clone of this theme element. The cloned theme contains the cloned
	 * content which was in the original theme if any.
	 * 
	 * @return the cloned theme.
	 * 
	 * @see java.lang.Object#clone()
	 */

	public Object clone( ) throws CloneNotSupportedException
	{
		Theme newTheme = (Theme) super.clone( );
		newTheme.contents = (ContainerSlot) contents.copy( newTheme,
				STYLES_SLOT );
		return newTheme;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#checkContent(org.eclipse.birt.report.model.core.Module,
	 *      org.eclipse.birt.report.model.core.DesignElement, int,
	 *      org.eclipse.birt.report.model.core.DesignElement)
	 */

	protected List checkContent( Module module, DesignElement container,
			int slotId, DesignElement content )
	{
		List errors = super.checkContent( module, container, slotId, content );
		if ( !errors.isEmpty( ) )
			return errors;

		errors.addAll( ThemeStyleNameValidator.getInstance( )
				.validateForAddingStyle( (ThemeHandle) getHandle( module ),
						content.getName( ) ) );

		return errors;

	}
}
