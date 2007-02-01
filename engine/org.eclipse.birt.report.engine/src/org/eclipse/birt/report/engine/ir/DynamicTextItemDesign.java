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
 * 
 */
public class DynamicTextItemDesign extends ReportItemDesign
{
	/**
	 * content type must be one of:
	 * html, plain, rtf or auto.
	 */
	protected String contentType;
	/**
	 * content 
	 */
	protected String content;
	
	
	
	/**
	 * @return Returns the content.
	 */
	public String getContent( )
	{
		return content;
	}
	/**
	 * @param content The content to set.
	 */
	public void setContent( String content )
	{
		this.content = content;
	}
	/**
	 * @return Returns the contentType.
	 */
	public String getContentType( )
	{
		return contentType;
	}
	/**
	 * @param contentType The contentType to set.
	 */
	public void setContentType( String contentType )
	{
		this.contentType = contentType;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.ReportItemVisitor)
	 */
	public Object accept( IReportItemVisitor visitor , Object value)
	{
		return visitor.visitDynamicTextItem(this, value);
	}

}
