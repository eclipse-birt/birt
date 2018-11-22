/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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
import java.util.Map;

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

	protected boolean hasExpression;
    private boolean jTidy = true;

	protected HashMap<String, Expression> exprs = null;

	public HashMap<String, Expression> getExpressions( )
	{
		if ( !hasExpression( ) )
		{
			return null;
		}
		if ( text == null )
		{
			return null;
		}
		if ( exprs != null )
		{
			return exprs;
		}
		exprs = extractExpression( text, textType );
		return exprs;
	}

	public static HashMap<String, Expression> extractExpression(
			String textContent, String textType )
	{
		HashMap<String, Expression> expressions = new HashMap<String, Expression>( );
		if ( HTML_TEXT.equals( textType )
				|| ( AUTO_TEXT.equals( textType ) && startsWithIgnoreCase(
						textContent, "<html>" ) ) )
		{
			TextTemplate template = null;
			try
			{
				template = new TemplateParser( ).parse( textContent );
			}
			catch ( Throwable ignored )
			{
				// We must ignore the exceptions here and process it when text
				// item is executed, otherwise the exception will be thrown out
				// and stop the whole task.
			}
			if ( template != null && template.getNodes( ) != null )
			{
				Iterator itor = template.getNodes( ).iterator( );
				Object obj;
				while ( itor.hasNext( ) )
				{
					obj = itor.next( );
					if ( obj instanceof TextTemplate.ValueNode )
					{
						ValueNode valueNode = (TextTemplate.ValueNode) obj;
						addExpression( expressions, valueNode.getValue( ) );
						addExpression( expressions, valueNode
								.getFormatExpression( ) );
					}
					else if ( obj instanceof TextTemplate.ImageNode )
					{
						addExpression( expressions,
								( (TextTemplate.ImageNode) obj ).getExpr( ) );
					}

				}
			}
		}
		return expressions;
	}

	private static void addExpression( Map<String, Expression> expressions,
			String expression )
	{
		if ( expression != null )
		{
			expression = expression.trim( );
			if ( expression.length( ) > 0 )
			{
				expressions
						.put( expression, Expression.newScript( expression ) );
			}
		}
	}

	public static boolean startsWithIgnoreCase( String original, String pattern )
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
	 * @see
	 * org.eclipse.birt.report.engine.ir.ReportItemDesign#accept(org.eclipse
	 * .birt.report.engine.ir.IReportItemVisitor)
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

	public boolean hasExpression( )
	{
		return hasExpression;
	}

	public void setHasExpression( boolean hasExpression )
	{
		this.hasExpression = hasExpression;
	}

    public boolean isJTidy( )
    {
        return jTidy;
    }

    public void setJTidy( boolean jTidy )
    {
        this.jTidy = jTidy;
    }
}