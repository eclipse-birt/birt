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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses the text item.
 * 
 */

public class TextItemState extends ReportItemState
{

	/**
	 * The text item being created.
	 */

	protected TextItem element;

	/**
	 * Constructs the text item state with the design parser handler, the
	 * container element and the container slot of the text item.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param theContainer
	 *            the element that contains this one
	 * @param slot
	 *            the slot in which this element appears
	 */

	public TextItemState( ModuleParserHandler handler,
			DesignElement theContainer, int slot )
	{
		super( handler, theContainer, slot );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		element = new TextItem( );
		initElement( attrs );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.ReportItemState#end()
	 */

	public void end( ) throws SAXException
	{
		super.end( );

		if ( StringUtil.compareVersion( handler.getVersion( ), "3.2.1" ) >= 0 ) //$NON-NLS-1$
			return;

		String content = (String) element.getLocalProperty(
				handler.getModule( ), TextItem.CONTENT_PROP );
		if ( StringUtil.isBlank( content ) )
			return;

		List jsExprs = getExpressions( content );
		updateExpressions( content, handleJavaExpression( jsExprs ) );
	}

	/**
	 * Does backward compatiblility work for the text item from BIRT 2.1M5 to
	 * BIRT 2.1.0.
	 * <p>
	 * Parts of backward compatiblility work for the Text Item from BIRT 2.1M5
	 * to BIRT 2.1.0.
	 * 
	 * @param jsExprs
	 *            the expression from the extended item.
	 * @return a map containing updated expressions.
	 */

	private Map handleJavaExpression( List jsExprs )
	{
		List columns = new ArrayList( );
		Map retMap = new HashMap( );

		Iterator exprsIter = jsExprs.iterator( );

		while ( exprsIter.hasNext( ) )
		{
			String jsExpr = (String) exprsIter.next( );

			IColumnBinding boundColumn = ExpressionUtil
					.getColumnBinding( jsExpr );

			if ( boundColumn == null )
				continue;

			if ( !columns.contains( boundColumn ) )
				columns.add( boundColumn );

			retMap.put( jsExpr, ExpressionUtil.createRowExpression( boundColumn
					.getResultSetColumnName( ) ) );
		}

		DataBoundColumnUtil.setupBoundDataColumns( element, columns, handler
				.getModule( ) );
		return retMap;
	}

	/**
	 * Returns expressions in the given content text. The text has expressions
	 * only when its type is html or auto.
	 * <p>
	 * Parts of backward compatiblility work for the Text Item from BIRT 2.1M5
	 * to BIRT 2.1.0.
	 * 
	 * @param contentText
	 *            the text to check
	 * @return a list containing expressions.
	 */

	private List getExpressions( String contentText )
	{
		if ( contentText == null )
			return null;

		List exprs = new ArrayList( );
		String contentType = (String) element.getProperty(
				handler.getModule( ), TextItem.CONTENT_TYPE_PROP );

		if ( DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO
				.equalsIgnoreCase( contentType )
				|| ( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML
						.equalsIgnoreCase( contentType ) ) )
		{

			TextTemplate template = new TemplateParser( ).parse( contentText );
			if ( template != null && template.getNodes( ) != null )
			{
				Iterator itor = template.getNodes( ).iterator( );
				Object obj;
				String expression = null;
				while ( itor.hasNext( ) )
				{
					obj = itor.next( );
					if ( obj instanceof TextTemplate.ValueNode )
					{
						expression = ( (TextTemplate.ValueNode) obj )
								.getValue( );
					}
					else if ( obj instanceof TextTemplate.ImageNode )
					{
						expression = ( (TextTemplate.ImageNode) obj ).getExpr( );
					}

					if ( !StringUtil.isBlank( expression )
							&& !exprs.contains( expression ) )
					{
						exprs.add( expression );
						expression = null;
					}
				}
			}
		}

		return exprs;
	}

	/**
	 * Updated the given content with the input updated expressions.
	 * 
	 * @param contentText
	 *            the content text
	 * @param updatedExprs
	 *            a mapping containing updated expressions. The keys are existed
	 *            expressions, while, the values are the new expressions to
	 *            replace existed ones.
	 */

	private void updateExpressions( String contentText, Map updatedExprs )
	{
		if ( updatedExprs.isEmpty( ) )
			return;

		String contentType = (String) element.getProperty(
				handler.getModule( ), TextItem.CONTENT_TYPE_PROP );

		if ( !( DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO
				.equalsIgnoreCase( contentType ) || ( DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML
				.equalsIgnoreCase( contentType ) ) ) )
			return;

		TextTemplate template = new TemplateParser( ).parse( contentText );

		ContentVisitor templateVisitor = new ContentVisitor( template,
				updatedExprs );

		String content = templateVisitor.execute( );

		// reset the content.

		element.setProperty( TextItem.CONTENT_PROP, content );
	}

	/**
	 * Visits the content as a template visitor. Updated the expression for
	 * <value-of> and <image> nodes.
	 * <p>
	 * Parts of backward compatiblility work for the Text Item from BIRT 2.1M5
	 * to BIRT 2.1.0.
	 */

	private static class ContentVisitor implements TextTemplate.Visitor
	{

		private static final String VALUE_OF_START_TAG = "<value-of>"; //$NON-NLS-1$
		private static final String VALUE_OF_END_TAG = "</value-of>"; //$NON-NLS-1$
		private static final String IMAGE_START_TAG = "<image>"; //$NON-NLS-1$
		private static final String IMAGE_END_TAG = "</image>";//$NON-NLS-1$

		protected StringBuffer buffer;
		protected Map updatedValues;
		protected TextTemplate template;

		ContentVisitor( TextTemplate template, Map updatedValues )
		{
			this.updatedValues = updatedValues;
			this.template = template;

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitText(org.eclipse.birt.core.template.TextTemplate.TextNode,
		 *      java.lang.Object)
		 */

		public Object visitText( TextTemplate.TextNode node, Object value )
		{
			if ( value != null )
				buffer.append( value );
			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitValue(org.eclipse.birt.core.template.TextTemplate.ValueNode,
		 *      java.lang.Object)
		 */

		public Object visitValue( TextTemplate.ValueNode node, Object value )
		{

			String updatedValue = (String) updatedValues.get( value );
			if ( updatedValue != null )
			{
				buffer.append( VALUE_OF_START_TAG + updatedValue
						+ VALUE_OF_END_TAG );
				return updatedValue;
			}

			return value;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.core.template.TextTemplate.Visitor#visitImage(org.eclipse.birt.core.template.TextTemplate.ImageNode,
		 *      java.lang.Object)
		 */

		public Object visitImage( TextTemplate.ImageNode node, Object value )
		{
			String updatedValue = (String) updatedValues.get( value );
			if ( updatedValue != null )
			{
				buffer.append( IMAGE_START_TAG + updatedValue + IMAGE_END_TAG );
				return updatedValue;
			}

			return value;
		}

		/**
		 * Runs the visitor.
		 * 
		 * @return the updated content. Expressions in <value-of> and <image>
		 *         nodes are updated.
		 */

		public String execute( )
		{
			if ( template == null )
			{
				return ""; //$NON-NLS-1$
			}

			buffer = new StringBuffer( );
			ArrayList nodes = template.getNodes( );
			Iterator iter = nodes.iterator( );
			while ( iter.hasNext( ) )
			{
				TextTemplate.Node node = (TextTemplate.Node) iter.next( );

				String text = null;

				if ( node instanceof TextTemplate.ValueNode )
				{
					text = ( (TextTemplate.ValueNode) node ).getValue( );
				}
				else if ( node instanceof TextTemplate.ImageNode )
				{
					text = ( (TextTemplate.ImageNode) node ).getExpr( );
				}
				else if ( node instanceof TextTemplate.TextNode )
				{
					text = ( (TextTemplate.TextNode) node ).getContent( );
				}

				node.accept( this, text );
			}

			return buffer.toString( );
		}
	}

}
