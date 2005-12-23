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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Records a change to the back reference of an element.
 * 
 * @see org.eclipse.birt.report.model.core.ReferenceableElement
 */

public class ElementBackRefRecord extends BackRefRecord
{

	/**
	 * The element is referred by <code>reference</code>.
	 */

	protected ReferenceableElement referred = null;

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param referred
	 *            the element to change
	 * @param reference
	 *            the element that refers to another element.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>PropertyType.ELEMENT_REF_TYPE</code>. Meanwhile, it
	 *            must not be <code>DesignElement.EXTENDS_PROP</code> and
	 *            <code>DesignElement.STYLE_PROP</code>
	 */

	public ElementBackRefRecord( Module module,
			ReferenceableElement referred, DesignElement reference,
			String propName )
	{
		super( module, reference, propName );
		this.referred = referred;

		assert referred != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.SimpleRecord#perform(boolean)
	 */

	protected void perform( boolean undo )
	{
		if ( undo )
		{
			ElementPropertyDefn propDefn = reference.getPropertyDefn( propName );

			// To add client is done in resolving element reference.

			reference.getLocalProperty( module, propDefn );
		}
		else
		{
			ElementRefValue value = (ElementRefValue) reference.getLocalProperty(
					module, propName );
			value.unresolved( value.getName( ) );

			referred.dropClient( reference );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.AbstractElementRecord#getTarget()
	 */

	public DesignElement getTarget( )
	{
		return reference;
	}

}