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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;

/**
 * <code>ParameterGroupDefn</code> is an concrete subclass of
 * <code>ReportElementDesign</code> that implements the interface
 * <code>IParameterGroupDefn</code>. It is used to visually group report
 * parameters.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/04/27 03:11:13 $
 */
public class ParameterGroupDefn extends ParameterDefnBase implements IParameterGroupDefn
{
	

	protected ArrayList contents = new ArrayList();


	public void addParameter(IParameterDefnBase param)
	{
		contents.add(param);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterGroupDefn#getContents()
	 */
	public ArrayList getContents()
	{
		return contents;
	}
	
	public void setContents(ArrayList list)
	{
		contents = list;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException
	{
		ParameterGroupDefn newParam = (ParameterGroupDefn) super.clone();
		ArrayList list = newParam.getContents();
		if(list==null)
			return newParam;
		
		ArrayList newList = new ArrayList();
		for(int i=0; i<list.size(); i++)
		{
			ScalarParameterDefn p = (ScalarParameterDefn)list.get(i);
			newList.add(p.clone());
		}
		newParam.setContents(newList);
		return newParam;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api2.IParameterGroupDefn#displayExpanded()
	 */
	public boolean displayExpanded() {
		return true;
	}
}