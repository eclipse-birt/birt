/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.util;

import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;



public class VirtualField
{

	public final static String TYPE_MEASURE = "measure";
	public final static String TYPE_LEVEL = "level";
	private String type;

	public VirtualField( String type )
	{
		this.type = type;
	}

	public String getType( )
	{
		return type;
	}

	public void setType( String type )
	{
		this.type = type;
	}
	
	private Object model;

	public String toString( )
	{
		if ( type.equals( TYPE_MEASURE ) )
			return Messages.getString( "GroupsPage.VirtualField.Measure" );
		if ( type.equals( TYPE_LEVEL ) )
			return Messages.getString( "GroupsPage.VirtualField.Level" );
		return super.toString( );
	}

	
	public Object getModel( )
	{
		return model;
	}

	
	public void setModel( Object model )
	{
		this.model = model;
	}
}
