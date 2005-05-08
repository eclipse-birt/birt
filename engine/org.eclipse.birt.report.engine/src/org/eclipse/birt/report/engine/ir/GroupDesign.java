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
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class GroupDesign
{
    

	/**
	 * group expression
	 */
	protected String name;

	protected String onStart;
	protected String onRow;
	protected String onFinish;

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
	 * @return Returns the onFinish.
	 */
	public String getOnFinish( )
	{
		return onFinish;
	}
	/**
	 * @param onFinish The onFinish to set.
	 */
	public void setOnFinish( String onFinish )
	{
		this.onFinish = onFinish;
	}
	/**
	 * @return Returns the onRow.
	 */
	public String getOnRow( )
	{
		return onRow;
	}
	/**
	 * @param onRow The onRow to set.
	 */
	public void setOnRow( String onRow )
	{
		this.onRow = onRow;
	}
	/**
	 * @return Returns the onStart.
	 */
	public String getOnStart( )
	{
		return onStart;
	}
	/**
	 * @param onStart The onStart to set.
	 */
	public void setOnStart( String onStart )
	{
		this.onStart = onStart;
	}

	
}
