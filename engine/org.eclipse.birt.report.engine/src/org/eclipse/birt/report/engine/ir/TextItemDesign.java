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
import java.util.Iterator;

import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.core.template.TextTemplate.ValueNode;

/**
 * Text element captures a long string with internal formatting.
 * 
 */
public class TextItemDesign extends ReportItemDesign
{

	public static final String AUTO_TEXT = "auto"; //$NON-NLS-1$
	public static final String PLAIN_TEXT = "plain"; //$NON-NLS-1$
	public static final String HTML_TEXT = "html"; //$NON-NLS-1$
	public static final String RTF_TEXT = "rtf"; //$NON-NLS-1$

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

	protected HashMap<String, String> exprs = null;

	public HashMap<String, String> getExpressions( )
	{
		if ( text == null )
		{
			return null;
		}
		if ( exprs != null )
		{
			return exprs;
		}

		if ( HTML_TEXT.equals( textType )
				|| ( AUTO_TEXT.equals( textType ) && startsWithIgnoreCase(
						text, "<html>" ) ) )
		{
			exprs = new HashMap<String, String>( );
			TextTemplate template = new TemplateParser( ).parse( text );
			if( template != null && template.getNodes() != null )
			{
				Iterator itor = template.getNodes().iterator();
				Object obj;
				while( itor.hasNext( ) )
				{
					obj = itor.next();
					if ( obj instanceof TextTemplate.ValueNode )
					{
						ValueNode valueNode = (TextTemplate.ValueNode) obj;
						addExpression( valueNode.getValue( ) );
						addExpression( valueNode.getFormatExpression( ) );
					}
					else if ( obj instanceof TextTemplate.ImageNode )
					{
						addExpression( ( (TextTemplate.ImageNode) obj )
								.getExpr( ) );
					}

					
				}
			}
		}
		return exprs;
	}

	private void addExpression( String expression )
	{
		if ( expression != null && !expression.trim( ).equals( "" ) )
		{
			exprs.put( expression, expression );
		}
	}

	public boolean startsWithIgnoreCase( String original, String pattern )
	{
		int length = pattern.length( );
		if ( original == null || original.length( ) < length )
		{
			return false;
		}
		return original.substring( 0, length ).equalsIgnoreCase( pattern );
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
	public Object accept( IReportItemVisitor visitor, Object value )
	{
		return visitor.visitTextItem( this, value );
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
}