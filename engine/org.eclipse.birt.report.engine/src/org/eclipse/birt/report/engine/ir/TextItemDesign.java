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

import java.util.HashMap;

import org.w3c.dom.Document;

/**
 * Text element is a element which will show text on the report.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class TextItemDesign extends ReportItemDesign
{
	/**
	 * text content type, such as "html", "auto", "rtf", "plain" 
	 */
	protected String contentType;
	/**
	 * content text key
	 */
	protected String contentKey;
	/**
	 * text content
	 */
	protected String content;

	/**
	 * DOM tree, and IR do not set this value
	 */
	protected Document domTree;
	
	/**
	 * expression map in html 
	 */
	protected HashMap exprMap = new HashMap();
	
	/**
	 * add expression to hashmap
	 * @param key
	 * @param expr
	 */
	public void addExpression(String key, Expression expr)
	{
		exprMap.put(key, expr);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public boolean containExpr(String key)
	{
		return exprMap.containsKey(key);
	}
	/**
	 * get expression from map
	 * @param key
	 * @return
	 */
	public Expression getExpression(String key)
	{
		if(exprMap.containsKey(key))
		{
			return (Expression)exprMap.get(key);
		}
		else
		{
			return null;
		}
	}
	/**
	 * @param content
	 *            The content to set.
	 */
	public void setContent( String textKey, String text )
	{
		this.contentKey = textKey;
		this.content = text;
	}

	/**
	 * @return Returns the resourceKey.
	 */
	public String getContentKey( )
	{
		return contentKey;
	}

	/**
	 * @return Returns the content.
	 */
	public String getContent( )
	{
		return content;
	}

	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitTextItem( this );
	}
	
	/**
	 * @return Returns the encoding.
	 */
	public String getContentType( )
	{
		return contentType;
	}
	/**
	 * @param encoding The encoding to set.
	 */
	public void setContentType( String contentType )
	{
		this.contentType = contentType;
	}
	/**
	 * @return Returns the domTree.
	 */
	public Document getDomTree( )
	{
		return domTree;
	}
	/**
	 * @param domTree The domTree to set.
	 */
	public void setDomTree( Document domTree )
	{
		this.domTree = domTree;
	}
}
