/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.outline;

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

/**
 * Represents the script method node of a report element
 */
public class ScriptObjectNode implements IScriptTreeNode
{

	private IElementPropertyDefn parent;

	public ScriptObjectNode( IElementPropertyDefn parent )
	{
		this.parent = parent;
	}

	public Object[] getChildren( )
	{
		// TODO Auto-generated method stub
		return new Object[0];
	}

	public Object getParent( )
	{
		return this.parent;
	}

	public String getText( )
	{
		return parent.getMethodInfo( ).getName( );
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object arg0 )
	{
		if ( arg0 == this )
		{
			return true;
		}
		if ( arg0 instanceof ScriptObjectNode )
		{
			return ( (ScriptObjectNode) arg0 ).parent.getName( ) == parent.getName( );
		}
		return false;
	}
	
	public int hashCode( )
	{
		int hashCode = 13;
		if(parent!=null)
			hashCode +=  parent.hashCode( ) * 7;
		return hashCode;
	}

}