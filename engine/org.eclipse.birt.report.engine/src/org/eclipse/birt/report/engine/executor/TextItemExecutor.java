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
import org.eclipse.birt.report.engine.parser.DOMParser;
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
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
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
		/* text has no help text now */
		//textContent.setHelpText( getLocalizedString(
		//		textItem.getHelpTextKey( ), textItem.getHelpText( ) ) );
		setStyles( textContent, item );
		setVisibility( item, textContent );
		if ( textItem.getDomTree( ) == null )
		{
			String content = getLocalizedString( textItem.getContentKey( ),
					textItem.getContent( ) );
			textItem.setDomTree( new DOMParser( ).parse( content, textItem
					.getContentType( ) ) );
		}
		Document doc = textItem.getDomTree( );
		if ( doc != null )
		{
			evaluateEmbeddedExpression( doc.getFirstChild( ), textItem,
					textContent );
			new HTMLProcessor( context ).execute(
					(Element) doc.getFirstChild( ), textContent );
		}
		textContent.setDomTree( doc );
		String bookmarkStr = evalBookmark( textItem );
		if ( bookmarkStr != null )
			textContent.setBookmarkValue( bookmarkStr );

		textEmitter.start( textContent );
		textEmitter.end( );
		closeResultSet( rs );
	}

	/**
	 * Walks through the DOM tree to evaluate the embedded expression and
	 * format.
	 * <p>
	 * After evaluating,the second child node of embedded expression node holds
	 * the value if no error exists, otherwise it has not the second child node.
	 * <p>
	 * Note: After evaluating, a text-type node for text or a element-type node
	 * for image is created and then it is appended to a element node whose tag
	 * name is body. The "body" node is the node holding the value. The reason
	 * why add this node is that if there are two text-type nodes placed
	 * together they may be merged into one. So we must insert an element node
	 * to separate the text-type node holding the expression from the value
	 * node.
	 * 
	 * @param node
	 *            the node in the DOM tree
	 * @param text
	 *            the text item design used to get the prepared expression
	 * @param content
	 *            the text item content used to store the image if possible.
	 */
	private void evaluateEmbeddedExpression( Node node, TextItemDesign text,
			ITextContent content )
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
				// If there are more than one child node, it must have and only
				// have two children nodes, remove the last child node.
				if ( node.getChildNodes( ).getLength( ) > 1 )
				{
					node.removeChild( node.getLastChild( ) );
				}
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
					Node appendedNode = null;
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
							doc = new DOMParser( ).parse(
									new ByteArrayInputStream( blob ), "html" );
						}
						else
						{
							doc = new DOMParser( ).parse( value.toString( ),
									"html" );
						}
						appendedNode = node.getOwnerDocument( ).createElement(
								"body" );
						copyChildrenNodes( doc.getFirstChild( ), appendedNode );
					}
					else
					{
						StringBuffer formattedStr = new StringBuffer( );
						//Uses the ROM style of design because the
						// format-related styles are constant, which are the
						// same as those of the content.
						formatValue( value, format, text.getStyle( ),
								formattedStr );

						appendedNode = node.getOwnerDocument( ).createElement(
								"body" );
						appendedNode.appendChild( node.getOwnerDocument( )
								.createTextNode( formattedStr.toString( ) ) );
					}
					node.appendChild( appendedNode );
				}
				return;

			}
			else if ( node.getNodeName( ).equals( "image" ) )
			{
				//For the embedded image, this node does not have child node
				// before evaluating. But for the expression, the node has a
				// text-type node holding the expression. So we must find the
				// value node dependent on the different situations and remove
				// it.
				if ( node.getChildNodes( ).getLength( ) > 0
						&& node.getLastChild( ).getNodeType( ) == Node.ELEMENT_NODE )
				{
					node.removeChild( node.getLastChild( ) );
				}
				Node body = node.getOwnerDocument( ).createElement( "body" );
				Element imgNode = node.getOwnerDocument( )
						.createElement( "img" );

				IImageItemContent image = ContentFactory.createImageContent( null );

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

					//Note: Actually the type just means that the image is from
					// the data source and there is not an attribute indicating
					// the file type.
					/*
					 * fileExt = (String) context.evaluate( text .getExpression(
					 * imageType ) );
					 */
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
					node.appendChild( body );
				}
				else
				{
					if ( logger.isErrorEnabled( ) )
					{
						logger.error( "can't handle image element in HTML" );
					}
				}

				return;
			}
			// call recursively
			for ( Node child = node.getFirstChild( ); child != null; child = child
					.getNextSibling( ) )
			{
				evaluateEmbeddedExpression( child, text, content );
			}
		}
	}

	/**
	 * Copy the children nodes of source node to destination node.
	 * 
	 * There are only two types: <code>TEXT_NODE</code> and
	 * <code>ELEMENT_NODE</code> in source children nodes.
	 * 
	 * @param srcNode
	 * @param desNode
	 */
	private void copyChildrenNodes( Node srcNode, Node desNode )
	{
		assert srcNode != null && desNode != null;

		for ( Node child = srcNode.getFirstChild( ); child != null; child = child
				.getNextSibling( ) )
		{
			// The child node is a text node, and create it and return
			if ( child.getNodeType( ) == Node.TEXT_NODE )
			{
				Text txtNode = desNode.getOwnerDocument( ).createTextNode(
						child.getNodeValue( ) );
				desNode.appendChild( txtNode );
			}
			else if ( child.getNodeType( ) == Node.ELEMENT_NODE )
			{
				// copy the element node
				Element ele = null;
				ele = desNode.getOwnerDocument( ).createElement(
						child.getNodeName( ) );
				// copy the attributes
				for ( int i = 0; i < child.getAttributes( ).getLength( ); i++ )
				{
					Node attr = child.getAttributes( ).item( i );
					ele
							.setAttribute( attr.getNodeName( ), attr
									.getNodeValue( ) );
				}
				desNode.appendChild( ele );
				copyChildrenNodes( child, ele );
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

