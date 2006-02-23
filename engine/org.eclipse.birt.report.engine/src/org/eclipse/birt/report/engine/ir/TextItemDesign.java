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

import org.eclipse.birt.report.engine.executor.template.TemplateParser;
import org.eclipse.birt.report.engine.executor.template.TextTemplate;

/**
 * Text element captures a long string with internal formatting.
 * 
 * @version $Revision: 1.14 $ $Date: 2005/11/28 09:05:49 $
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

	protected HashMap exprs = null;

	public HashMap getExpressions( )
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
				|| ( AUTO_TEXT.equals( textType ) && text.startsWith( "<html>" ) ) )
		{
			exprs = new HashMap( );
			TextTemplate template = new TemplateParser( ).parse( text );
			if( template != null && template.getNodes() != null )
			{
				Iterator itor = template.getNodes().iterator();
				Object obj;
				String expression = null;
				while( itor.hasNext( ) )
				{
					obj = itor.next();
					if( obj instanceof TextTemplate.ValueNode )
				{
						expression = ( ( TextTemplate.ValueNode ) obj ).getValue( ); 
					}
					else if( obj instanceof TextTemplate.ImageNode )
					{
						expression = ( ( TextTemplate.ImageNode ) obj ).getExpr();
					}
					
					if( expression != null && !expression.trim( ).equals( "" ) )
					{
						exprs.put( expression, new Expression( expression ) );
						expression = null;
					}
				}
			}
		}
		return exprs;
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