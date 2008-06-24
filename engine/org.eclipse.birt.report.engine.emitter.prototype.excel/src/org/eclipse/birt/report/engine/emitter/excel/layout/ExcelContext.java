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
	
	public void setWrappingText(boolean wrappingText)
	{
		this.wrappingText = wrappingText;
	}
	
	public boolean getWrappingText()
	{
		return wrappingText;
	}
}
