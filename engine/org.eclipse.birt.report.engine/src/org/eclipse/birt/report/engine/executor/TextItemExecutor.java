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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.impl.ImageItemContent;
import org.eclipse.birt.report.engine.content.impl.TextItemContent;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.IReportEmitter;
import org.eclipse.birt.report.engine.emitter.IReportItemEmitter;
import org.eclipse.birt.report.engine.executor.css.HTMLProcessor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * <code>DataItemExecutor</code> is a concrete subclass of
 * <code>StyledItemExecutor</code> that manipulates label/text items.
 * 
 * @version $Revision: 1.17 $ $Date: 2005/05/12 07:18:53 $
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
		TextItemContent textContent = (TextItemContent) ContentFactory
				.createTextContent( textItem, context.getContentObject( ) );

		IResultSet rs = null;
		try
		{
			rs = openResultSet( item );
			if ( rs != null )
			{
				rs.next( );
			}
			
			setStyles( textContent, item );
			setVisibility( item, textContent );

			//Checks if the content has been parsed before. If so, use the DOM
			// tree
			// directly to improve performance. In addition to the DOM tree, the
			// CSS
			// style is also a part of the parsed content. Or else, parse the
			// content and save the DOM tree and CSS style in the Text item
			// design.
			HTMLProcessor htmlProcessor = new HTMLProcessor( context );
			if ( textItem.getDomTree( ) == null )
			{
				//Only when the Text is in the master page.
				//Because ExpressionBuilder has parsed the content. And the
				// style
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
				//Checks if the DOM can be reused here. If no, then convert the
				// DOM
				// tree.
				if ( !textItem.isReused( ) )
				{
					htmlProcessor.execute( body, textContent );
					//Saves the CSS style associated with the original text
					// content
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
		}
		catch ( Throwable t )
		{
			logger.log( Level.SEVERE, "Error:", t );//$NON-NLS-1$
			context.addException( new EngineException(
					MessageConstants.TEXT_PROCESSING_ERROR,
									( item.getName( ) != null ? item
											.getName( ) : "" ), t ) );//$NON-NLS-1$
		}
		finally
		{
			closeResultSet( rs );
		}		
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
			TextItemContent content, HTMLProcessor htmlProcessor )
	{
		if ( node.getNodeType( ) == Node.ELEMENT_NODE )
		{
			Element ele = (Element) node;
			if ( node.getNodeName( ).equals( "img" ) ) //$NON-NLS-1$
			{
				String src = ele.getAttribute( "src" ); //$NON-NLS-1$
				if ( FileUtil.isLocalResource( src ) )
				{
					src = FileUtil.getAbsolutePath( context.getReport( )
							.getBasePath( ), src );
					ele.removeAttribute( "src" ); //$NON-NLS-1$
					ImageItemContent imgContent = (ImageItemContent)ContentFactory
							.createImageContent( null, content );
					content.addImageContent( node, imgContent );
					imgContent.setImageSource( ImageItemDesign.IMAGE_FILE );
					imgContent.setUri( src );
					imgContent.setExtension( FileUtil.getExtFromFileName( src,
							FileUtil.SEPARATOR_PATH ) );
				}
			}
			else if ( node.getNodeName( ).equals( "value-of" ) ) //$NON-NLS-1$
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
							.getAttribute( "format" ); //$NON-NLS-1$

					//TODO Only the type string and blob are done in the format
					// of HTML, or else only string type.
					if ( "html".equals( format ) ) //$NON-NLS-1$
					{
						//parse the value to get the DOM tree
						Document doc = null;
						if ( value.getClass( ).isArray( )
								&& value.getClass( ).getComponentType( ) == byte.class )
						{
							byte[] blob = (byte[]) value;
							doc = new TextParser( ).parse(
									new ByteArrayInputStream( blob ), "html" ); //$NON-NLS-1$
						}
						else
						{
							doc = new TextParser( ).parse( value.toString( ),
									"html" ); //$NON-NLS-1$
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
								content.addExpressionVal( node, doc );
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
								formattedStr, content );
						Element appendedNode = null;

						Document textDoc;
						try
						{
							textDoc = DocumentBuilderFactory.newInstance( )
									.newDocumentBuilder( ).newDocument( );
							if ( textDoc != null
									&& textDoc.getFirstChild( ) != null
									&& textDoc.getFirstChild( ) instanceof Element )
							{

								( (Element) ( textDoc.getFirstChild( ) ) )
										.setAttribute( "text-type", //$NON-NLS-1$
												TextParser.TEXT_TYPE_PLAIN );
							}

							appendedNode = textDoc.createElement( "body" ); //$NON-NLS-1$
							textDoc.appendChild( appendedNode );
							appendedNode
									.appendChild( textDoc
											.createTextNode( formattedStr
													.toString( ) ) );
							content.addExpressionVal( node, textDoc );
						}
						catch ( ParserConfigurationException e )
						{
							logger
									.log(
											Level.SEVERE,
											"[TextItemExecutor] Fails to create document for value-of", //$NON-NLS-1$
											e );
							context.addException( new EngineException(
									MessageConstants.TEXT_PROCESSING_ERROR,
													( text.getName( ) != null ? text
															.getName( ) : "" ), e ) );//$NON-NLS-1$

						}
						catch ( FactoryConfigurationError e )
						{
							logger
									.log(
											Level.SEVERE,
											"[TextItemExecutor] Fails to create document for value-of", //$NON-NLS-1$
											e );
							context.addException( new EngineException(
									MessageConstants.EMBEDDED_EXPRESSION_ERROR, "", e)); //$NON-NLS-1$


						}
					}
				}
				return;

			}
			else if ( node.getNodeName( ).equals( "image" ) ) //$NON-NLS-1$
			{

				ImageItemContent image = (ImageItemContent)ContentFactory
						.createImageContent( null, content );

				//Get the image content
				String imageType = ( (Element) ( node ) ).getAttribute( "type" ); //$NON-NLS-1$
				if ( "expr".equalsIgnoreCase( imageType ) ) //$NON-NLS-1$
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
					String name = ( (Element) ( node ) ).getAttribute( "name" ); //$NON-NLS-1$
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
					try
					{
						Document imgDoc;
						imgDoc = DocumentBuilderFactory.newInstance( )
								.newDocumentBuilder( ).newDocument( );
						Element body = imgDoc.createElement( "body" ); //$NON-NLS-1$
						Element imgNode = imgDoc.createElement( "img" ); //$NON-NLS-1$

						content.addImageContent( imgNode, image );
						// add image node to DOM tree
						body.appendChild( imgNode );

						imgDoc.appendChild( body );

						content.addExpressionVal( node, imgDoc );
					}
					catch ( ParserConfigurationException e )
					{
						logger
								.log(
										Level.SEVERE,
										"[TextItemExecutor] Fails to create document for img", //$NON-NLS-1$
										e );
						context.addException( new EngineException(
								MessageConstants.HTML_IMAGE_ERROR,  e));
					}
					catch ( FactoryConfigurationError e )
					{
						logger
								.log(
										Level.SEVERE,
										"[TextItemExecutor] Fails to create document for img", //$NON-NLS-1$
										e );
						context.addException( new EngineException(
								MessageConstants.HTML_IMAGE_ERROR,  e));
					}
				}
				else
				{
					logger.log( Level.SEVERE,
							"can't handle image element in HTML" ); //$NON-NLS-1$
					context.addException( new EngineException(
							MessageConstants.HTML_IMAGE_ERROR));

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

