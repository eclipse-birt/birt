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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;
import org.eclipse.gef.commands.Command;

/**
 * This command set a generic property on a model object
 * 
 *  
 */

public class SetPropertyCommand extends Command
{

	private Object model;

	private Map extendsData;

	public SetPropertyCommand( Object model, Map extendsData )
	{
		this.model = model;
		this.extendsData = extendsData;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is
	 * not executable.
	 */

	public void execute( )
	{
		try
		{
			DesignElementHandle handle = (DesignElementHandle) model;
			List elementProperties = handle.getDefn( ).getProperties( );

			for ( Iterator it = elementProperties.iterator( ); it.hasNext( ); )
			{
				String key = ( (SystemPropertyDefn) it.next( ) ).getName( );
				Object value = null;
				if ( ( value = extendsData.get( DEUtil.getGUIPropertyKey( key ) ) ) != null )
				{
					handle.setProperty( key, value );
				}
			}
		}
		catch ( SemanticException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}
}