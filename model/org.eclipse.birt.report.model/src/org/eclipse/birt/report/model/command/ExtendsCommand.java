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

package org.eclipse.birt.report.model.command;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.core.UserPropertyDefn;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Sets the "extends" attribute of an element.
 *  
 */

public class ExtendsCommand extends AbstractElementCommand
{

	/**
	 * Constructor.
	 * 
	 * @param design
	 *            the report design
	 * @param obj
	 *            the element to modify.
	 */

	public ExtendsCommand( ReportDesign design, DesignElement obj )
	{
		super( design, obj );
	}

	/**
	 * Sets the extends attribute of an element.
	 * 
	 * @param base
	 *            the name of the new parent element, or null to clear the
	 *            extends attribute.
	 * @throws ExtendsException
	 *             if the element can not be extended or the base element is not
	 *             on component slot.
	 */

	public void setExtendsName( String base ) throws ExtendsException
	{
		base = StringUtil.trimString( base );

		DesignElement parent = null;
		ElementDefn metaData = element.getDefn( );
		int ns = metaData.getNameSpaceID( );
		if ( base == null )
		{
			if ( !metaData.canExtend( ) )
				return;
		}
		else
		{
			// Verify that the symbol exists and is the right type.

			RootElement root = getRootElement( );
			if ( !metaData.canExtend( ) )
				throw new ExtendsException( element, base,
						ExtendsException.DESIGN_EXCEPTION_CANT_EXTEND );
			parent = root.getNameSpace( ns ).getElement( base );
			element.checkExtends( parent );

			if ( element.getDefn( ).getNameSpaceID( ) == RootElement.ELEMENT_NAME_SPACE
					&& ( parent.getContainer( ) != root || parent
							.getContainerSlot( ) != ReportDesign.COMPONENT_SLOT ) )
				throw new ExtendsException( element, base,
						ExtendsException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT );
		}

		// Ignore if the setting is the same as current.

		if ( parent == element.getExtendsElement( ) )
			return;

		// Make the change.

		ActivityStack stack = getActivityStack( );
		ExtendsRecord record = new ExtendsRecord( element, parent );
		stack.startTrans( record.getLabel( ) );

		adjustUserProperties( element, parent );

		stack.execute( record );
		stack.commit( );
	}

	/**
	 * Private method to remove values for any user properties that are defined
	 * by ancestors that will no longer be visible after the change of the
	 * parent element.
	 * 
	 * @param element
	 *            the element to adjust.
	 * @param parent
	 *            the new parent element.
	 */

	private void adjustUserProperties( DesignElement element,
			DesignElement parent )
	{
		ActivityStack stack = getActivityStack( );
		DesignElement ancestor = element.getExtendsElement( );
		while ( ancestor != null && ancestor != parent )
		{
			Collection props = ancestor.getUserProperties( );
			if ( props != null )
			{
				Iterator iter = props.iterator( );
				while ( iter.hasNext( ) )
				{
					UserPropertyDefn prop = (UserPropertyDefn) iter.next( );
					if ( element.getLocalProperty( design, prop ) != null )
					{
						PropertyRecord record = new PropertyRecord( element,
								prop.getName( ), null );
						stack.execute( record );
					}
				}
			}
			ancestor = ancestor.getExtendsElement( );
		}

	}

	/**
	 * Sets the extends attribute for an element given the new parent element.
	 * 
	 * @param parent
	 *            the new parent element.
	 * @throws ExtendsException
	 *             if the element can not be extended or the base element is not
	 *             on component slot, or the base element has no name.
	 */

	public void setExtendsElement( DesignElement parent )
			throws ExtendsException
	{
		String name = null;
		if ( parent != null )
		{
			name = parent.getName( );
			if ( StringUtil.isBlank( name ) )
				throw new ExtendsException( element, "", //$NON-NLS-1$
						ExtendsException.DESIGN_EXCEPTION_UNNAMED_PARENT );
		}
		setExtendsName( name );
	}

}