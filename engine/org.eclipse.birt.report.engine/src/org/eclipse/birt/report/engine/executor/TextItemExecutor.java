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

package org.eclipse.birt.report.engine.executor;

import java.io.ByteArrayInputStream;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IImageItemContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.executor.css.HTMLProcessor;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 * 
 * @version $Revision: 1.7 $ $Date: 2005/03/07 07:11:21 $
 */
public class TextItemExecutor extends StyledItemExecutor
{

	/**
	 * constructor
	 * 
	 * @param context
	 *            the executor context
	 * @param visitor
	 *            the report executor visitor
	 */
	public TextItemExecutor( ExecutionContext context,
			ReportExecutorVisitor visitor )
	{
		super( context, visitor );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExcutor#execute()
	 */
	public void execute( ReportItemDesign item, IReportEmitter emitter )
	{
		TextItemDesign textItem = (TextItemDesign) item;
		IReportItemEmitter textEmitter = emitter.getEmitter( "text" ); //$NON-NLS-1$
		if ( textEmitter == null )
		{
			return;
		}
		IResultSet rs = openResultSet( item );
		if ( rs != null )
		{
			rs.next( );
		}
		ITextContent textContent = ContentFactory.createTextContent( textItem );

		setStyles( textContent, item );
		setVisibility( item, textContent );

		//Checks if the content has been parsed before. If so, use the DOM tree
		// directly to improve performance. In addition to the DOM tree, the CSS
		// style is also a part of the parsed content. Or else, parse the
		// content and save the DOM tree and CSS style in the Text item design.
		HTMLProcessor htmlProcessor = new HTMLProcessor( context );
		if ( textItem.getDomTree( ) == null )
		{
			//Only when the Text is in the master page.
			//Because ExpressionBuilder has parsed the content. And the style
			// has not been extracted yet.
			String content = getLocalizedString( textItem.getTextKey( ),
					textItem.getText( ) );
			textItem.setDomTree( new TextParser( ).parse( content, textItem
					.getTextType( ) ) );
			textContent.setDomTree( textItem.getDomTree( ) );
		}
		Document doc = textItem.getDomTree( );
		Element body = null;
		if ( doc != null )
		{
			Node node = doc.getFirstChild( );
			//The following must be true
			if ( node instanceof Element )
			{
				body = (Element) node;
			}
		}
		if ( body != null )
		{
			//Checks if the DOM can be reused here. If no, then convert the DOM
			// tree.
			if ( !textItem.isReused( ) )
			{
				htmlProcessor.execute( body, textContent );
				//Saves the CSS style associated with the original text content
				textItem.setCssStyleSet( textContent.getCssStyleSet( ) );
				textItem.setReused( true );
			}
			evaluateEmbeddedExpression( body, textItem, textContent,
					htmlProcessor );
		}

		String bookmarkStr = evalBookmark( textItem );
		if ( bookmarkStr != null )
		{
			textContent.setBookmarkValue( bookmarkStr );
		}

		textEmitter.start( textContent );
		textEmitter.end( );
		closeResultSet( rs );
	}

	/**
	 * Walks through the DOM tree to evaluate the embedded expression and
	 * format.
	 * 
	 * @param node
	 *            the node in the DOM tree
	 * @param text
	 *            the text item design used to get the prepared expression
	 * @param content
	 *            the text item content used to store the image if possible.
	 */
	private void evaluateEmbeddedExpression( Node node, TextItemDesign text,
			ITextContent content, HTMLProcessor htmlProcessor )
	{
		if ( node.getNodeType( ) == Node.ELEMENT_NODE )
		{
			Element ele = (Element) node;
			if ( node.getNodeName( ).equals( "img" ) )
			{
				String src = ele.getAttribute( "src" );
				if ( FileUtil.isLocalResource( src ) )
				{
					src = FileUtil.getAbsolutePath( context.getReport( )
							.getBasePath( ), src );
					ele.removeAttribute( "src" );
					IImageItemContent imgContent = ContentFactory
							.createImageContent( null );
					content.addImageContent( node, imgContent );
					imgContent.setImageSource( ImageItemDesign.IMAGE_FILE );
					imgContent.setUri( src );
					imgContent.setExtension( FileUtil.getExtFromFileName( src,
							FileUtil.SEPARATOR_PATH ) );
				}
			}
			else if ( node.getNodeName( ).equals( "value-of" ) )
			{
				String strExpr = node.getFirstChild( ).getNodeValue( );
				IBaseExpression expr = text.getExpression( strExpr );
				Object value;
				if ( expr != null )
				{
					value = context.evaluate( expr );
				}
				else
				{
					value = context.evaluate( strExpr );
				}
				if ( value != null )
				{
					String format = ( (Element) ( node ) )
							.getAttribute( "format" );

					//TODO Only the type string and blob are done in the format
					// of HTML, or else only string type.
					if ( "html".equals( format ) )
					{
						//parse the value to get the DOM tree
						Document doc = null;
						if ( value.getClass( ).isArray( )
								&& value.getClass( ).getComponentType( ) == byte.class )
						{
							byte[] blob = (byte[]) value;
							doc = new TextParser( ).parse(
									new ByteArrayInputStream( blob ), "html" );
						}
						else
						{
							doc = new TextParser( ).parse( value.toString( ),
									"html" );
						}

						if ( doc != null )
						{
							Element body = null;
							if ( doc.getFirstChild( ) instanceof Element )
							{
								body = (Element) doc.getFirstChild( );
							}
							if ( body != null )
							{
								htmlProcessor.execute( body, content );
								content.addExpressionVal( node, body );
							}
						}
					}
					else
					{
						StringBuffer formattedStr = new StringBuffer( );
						//Uses the ROM style of design because the
						// format-related styles are constant, which are the
						// same as those of the content.
						formatValue( value, format, text.getStyle( ),
								formattedStr );
						Element appendedNode = null;
						appendedNode = node.getOwnerDocument( ).createElement(
								"body" );
						appendedNode.appendChild( node.getOwnerDocument( )
								.createTextNode( formattedStr.toString( ) ) );
						content.addExpressionVal( node, appendedNode );
					}
				}
				return;

			}
			else if ( node.getNodeName( ).equals( "image" ) )
			{
				Element body = node.getOwnerDocument( ).createElement( "body" );
				Element imgNode = node.getOwnerDocument( )
						.createElement( "img" );

				IImageItemContent image = ContentFactory
						.createImageContent( null );

				//Get the image content
				String imageType = ( (Element) ( node ) ).getAttribute( "type" );
				if ( "expr".equalsIgnoreCase( imageType ) )
				{
					image.setImageSource( ImageItemDesign.IMAGE_EXPRESSION );
					//The image is from data source
					String strExpression = node.getFirstChild( ).getNodeValue( );
					Object value = context.evaluate( text
							.getExpression( strExpression ) );
					if ( value != null
							&& value.getClass( ).isArray( )
							&& value.getClass( ).getComponentType( ) == byte.class )
					{
						image.setData( (byte[]) value );
					}
				}
				else
				{
					//The image is an embedded one.
					String name = ( (Element) ( node ) ).getAttribute( "name" );
					EmbeddedImage embeddedImage = context.getReport( )
							.getReportDesign( ).findImage( name );
					if ( embeddedImage != null )
					{
						image.setData( embeddedImage.getData( ) );
						image.setImageSource( ImageItemDesign.IMAGE_NAME );
						image.setUri( name );
						String extension = FileUtil
								.getExtFromType( embeddedImage.getType( ) );
						if ( extension == null )
						{
							extension = FileUtil.getExtFromFileName( name,
									FileUtil.SEPARATOR_PATH );
						}
						image.setExtension( extension );
					}
				}

				if ( image.getData( ) != null )
				{
					content.addImageContent( imgNode, image );
					// add image node to DOM tree
					body.appendChild( imgNode );
					content.addExpressionVal( node, body );
				}
				else
				{
					logger.log( Level.SEVERE,
							"can't handle image element in HTML" );
				}

				return;
			}
			// call recursively
			for ( Node child = node.getFirstChild( ); child != null; child = child
					.getNextSibling( ) )
			{
				evaluateEmbeddedExpression( child, text, content, htmlProcessor );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.ReportItemExecutor#reset()
	 */
	public void reset( )
	{
		// TODO Auto-generated method stub

	}
}

