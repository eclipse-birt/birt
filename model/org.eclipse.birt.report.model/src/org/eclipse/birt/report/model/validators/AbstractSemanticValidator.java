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

package org.eclipse.birt.report.model.validators;


/**
 * Abstract class for semantic validator. It provides the validator name and the
 * target element on which the validation is performed.
 */

public class AbstractSemanticValidator
{

	/**
	 * The internal name of the validator.
	 */

	protected String name = null;

	/**
	 * Returns the validator name.
	 * 
	 * @return the validator name
	 */
	
	public String getName( )
	{
		return name;
	}

	/**
	 * Set the name of the validator, name is referenced by a property as key to
	 * the validator.
	 * 
	 * @param name
	 *            name of the validator, can not be <code>null</code>.
	 */

	public void setName( String name )
	{
		assert name != null;

		this.name = name;
	}

}
