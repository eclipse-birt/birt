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

package org.eclipse.birt.report.engine.api;


public interface IExcelRenderOption extends IRenderOption
{

	/**
	 * The option to decide if the text out will be wrapped
	 */
	public static final String WRAPPING_TEXT = "excelRenderOption.wrappingText";
	
	/**
	 * 
	 * @param wrappingText
	 */
	public void setWrappingText(boolean wrappingText);
	
	/**
	 * 
	 * @return if the text is wrapped
	 */
	public boolean getWrappingText();
}
