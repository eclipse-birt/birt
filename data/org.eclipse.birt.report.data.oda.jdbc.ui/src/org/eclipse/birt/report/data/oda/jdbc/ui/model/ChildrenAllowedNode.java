/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

public abstract class ChildrenAllowedNode implements IDBNode
{
	private IDBNode[] children;
	
	public boolean isChildrenPrepared( )
	{
		return children != null;
	}
	
	public void prepareChildren( FilterConfig fc )
	{
		setChildren( refetchChildren( fc ) );
	}

	protected abstract IDBNode[] refetchChildren( FilterConfig fc );
	
	public IDBNode[] getChildren( )
	{
		return children;
	}
	
	protected void setChildren( IDBNode[] children )
	{
		this.children = children;
	}
}
