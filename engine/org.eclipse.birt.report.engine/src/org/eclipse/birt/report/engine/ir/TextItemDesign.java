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
 * Text element captures a long string with internal formatting.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/03/03 22:15:34 $
 */
public class TextItemDesign extends ReportItemDesign
{

	/**
	 * text type, supports "html", "auto", "rtf", and "plain"
	 */
	protected String textType;

	/**
	 * the text key
	 */
	protected String textKey;

	/**
	 * text content
	 */
	protected String text;

	/**
	 * the parsed tree for text
	 */
	protected Document domTree;
	/**
	 * the corresponding css style set.
	 */
	protected HashMap cssStyleSet;
	/**
	 * Indicates the DOM tree can be reused.
	 */
	protected boolean isReused;
	/**
	 * captures all expressions from <value-of>tags
	 */
	protected HashMap exprMap = new HashMap( );

	/**
	 * add an expression to expression collection
	 * 
	 * @param key
	 *            expression key
	 * @param expr
	 *            actual expression
	 */
	public void addExpression( String key, Expression expr )
	{
		exprMap.put( key, expr );
	}

	/**
	 * @param expressionKey
	 *            key for an expression
	 * @return whether the expression with a given key exists in HTML text
	 */
	public boolean hasExpression( String expressionKey )
	{
		return exprMap.containsKey( expressionKey );
	}

	/**
	 * get an embedded expression based on expression key
	 * 
	 * @param key
	 *            expression key
	 * @return the expression keyed by the given string
	 */
	public Expression getExpression( String key )
	{
		if ( exprMap.containsKey( key ) )
			return (Expression) exprMap.get( key );
		else
			return null;
	}

	/**
	 * @param textKey
	 *            the message key for the text
	 * @param text
	 *            the actual text
	 */
	public void setText( String textKey, String text )
	{
		this.textKey = textKey;
		this.text = text;
	}

	/**
	 * @return Returns the resourceKey.
	 */
	public String getTextKey( )
	{
		return textKey;
	}

	/**
	 * @return Returns the content.
	 */
	public String getText( )
	{
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse.birt.report.engine.ir.IReportItemVisitor)
	 */
	public void accept( IReportItemVisitor visitor )
	{
		visitor.visitTextItem( this );
	}

	/**
	 * @return Returns the encoding.
	 */
	public String getTextType( )
	{
		return textType;
	}

	/**
	 * @param encoding
	 *            The encoding to set.
	 */
	public void setTextType( String textType )
	{
		this.textType = textType;
	}

	/**
	 * @return Returns the domTree.
	 */
	public Document getDomTree( )
	{
		return domTree;
	}

	/**
	 * @param domTree
	 *            The domTree to set.
	 */
	public void setDomTree( Document domTree )
	{
		this.domTree = domTree;
	}

	/**
	 * @return Returns the cssStyleSet.
	 */
	public HashMap getCssStyleSet( )
	{
		return cssStyleSet;
	}

	/**
	 * @param cssStyleSet
	 *            The cssStyleSet to set.
	 */
	public void setCssStyleSet( HashMap cssStyleSet )
	{
		this.cssStyleSet = cssStyleSet;
	}

	/**
	 * @return Returns the isReused.
	 */
	public boolean isReused( )
	{
		return isReused;
	}

	/**
	 * @param isReused
	 *            The isReused to set.
	 */
	public void setReused( boolean isReused )
	{
		this.isReused = isReused;
	}
}