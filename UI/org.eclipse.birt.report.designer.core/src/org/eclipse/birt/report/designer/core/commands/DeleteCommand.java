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

package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.util.Assert;

/**
 * This command deletes an object from the model.
 * 
 *  
 */

public class DeleteCommand extends Command
{

	private DesignElementHandle model = null;

	/**
	 * Deletes the command
	 * 
	 * @param model
	 *            the model
	 */

	public DeleteCommand( Object model )
	{
		Assert.isTrue( model instanceof DesignElementHandle );
		this.model = (DesignElementHandle) model;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is
	 * not executable.
	 */

	public void execute( )
	{
		try
		{
			if ( model.getContainer( ) != null )
			{
				model.drop( );
			}
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
	}
}