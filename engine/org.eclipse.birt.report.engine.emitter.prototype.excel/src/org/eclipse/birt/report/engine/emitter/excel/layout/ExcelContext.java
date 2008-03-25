/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel.layout;


public class ExcelContext
{
	private boolean wrappingText = true;
	
	private String officeVersion = "office2003";
	
	public void setWrappingText(boolean wrappingText)
	{
		this.wrappingText = wrappingText;
	}
	
	public void setOfficeVersion(String officeVersion)
	{
		this.officeVersion = officeVersion;
	}
	
	public boolean getWrappingText()
	{
		return wrappingText;
	}
	
	public String getOfficeVersion()
	{
		return officeVersion;
	}
	
}
