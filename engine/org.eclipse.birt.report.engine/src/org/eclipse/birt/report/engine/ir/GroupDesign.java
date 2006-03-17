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

package org.eclipse.birt.report.engine.ir;


/**
 * Group type.
 * 
 * used by ListItem and TableItem.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/12/23 06:37:24 $
 */
public class GroupDesign
{
	/**
	 * group expression
	 */
	protected String name;

	/**
	 * group hideDetail
	 */
	protected boolean hideDetail;

	/**
	 * @return Returns the name.
	 */
	public String getName( )
	{
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName( String name )
	{
		this.name = name;
	}
	
	/**
	 * @param hide
	 *            The hideDetail to set.
	 */
	public void setHideDetail( boolean hide )
	{
		hideDetail = hide;
	}
	
	/**
	 * @return Returns the hideDetail.
	 */
	public boolean getHideDetail( )
	{
		return hideDetail;
	}
	
}
