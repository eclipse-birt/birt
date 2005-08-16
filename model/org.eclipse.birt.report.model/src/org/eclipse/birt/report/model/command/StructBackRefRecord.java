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
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructRefValue;

/**
 * Records a change to the back reference of a structure.
 * 
 * @see org.eclipse.birt.report.model.core.ReferencableStructure
 */

public class StructBackRefRecord extends BackRefRecord
{

	/**
	 * The structure is referred by <code>reference</code>.
	 */

	protected ReferencableStructure referred = null;

	/**
	 * Constructor.
	 * 
	 * @param module
	 *            the module
	 * @param referred
	 *            the structure to change.
	 * @param reference
	 *            the element that refers to a structure.
	 * @param propName
	 *            the property name. The type of the property must be
	 *            <code>STRUCT_REF_TYPE</code>.
	 */

	public StructBackRefRecord( Module module,
			ReferencableStructure referred, DesignElement reference,
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

			// To add client is done in resolving structure reference.

			reference.resolveStructReference( module, propDefn );
		}
		else
		{
			StructRefValue value = (StructRefValue) reference.getLocalProperty(
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
		return module;
	}

}