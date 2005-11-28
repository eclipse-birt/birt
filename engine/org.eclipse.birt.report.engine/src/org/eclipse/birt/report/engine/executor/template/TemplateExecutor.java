
package org.eclipse.birt.report.engine.executor.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

public class TemplateExecutor implements TextTemplate.Visitor
{

	protected StringBuffer buffer;
	protected HashMap values;
	protected ExecutionContext context;
	protected IHTMLImageHandler imageHandler;
	protected Object renderContext;

	public TemplateExecutor( ExecutionContext context,
			IHTMLImageHandler imageHandler, Object renderContext )
	{
		this.context = context;
		this.imageHandler = imageHandler;
		this.renderContext = renderContext;
		if( imageHandler == null)
		{
			this.imageHandler = new HTMLCompleteImageHandler( );			
		}

		/*
		 * Object im =
		 * option.getOutputSetting().get(HTMLRenderOption.IMAGE_HANDLER); if (
		 * im != null && im instanceof IHTMLImageHandler ) { imageHandler =
		 * (IHTMLImageHandler) im; }
		 */
	}

	public String execute( TextTemplate template, HashMap values )
	{
		this.buffer = new StringBuffer( );
		this.values = values;

		if ( template == null )
		{
			return "";
		}
		
		ArrayList nodes = template.getNodes( );
		Iterator iter = nodes.iterator( );
		while ( iter.hasNext( ) )
		{
			TextTemplate.Node node = (TextTemplate.Node) iter.next( );
			node.accept( this, null );
		}

		return buffer.toString( );
	}

	public Object visitNode( TextTemplate.Node node, Object value )
	{
		return value;
	}

	public Object visitText( TextTemplate.TextNode node, Object value )
	{
		buffer.append( node.getContent( ) );
		return value;
	}

	public Object visitValue( TextTemplate.ValueNode node, Object value )
	{
		String text = "";
		String format = node.getFormat( );
		Object result = values.get( node.getValue( ) );

		if ( "html".equalsIgnoreCase( format ) )
		{
			if ( result != null )
			{
				text = result.toString( );
			}
		}
		else
		{
			if ( result != null )
			{
				if ( result instanceof Number )
				{
					NumberFormatter fmt = context.getNumberFormatter( format );
					text = fmt.format( (Number) result );
				}
				else if ( result instanceof String )
				{
					StringFormatter fmt = context.getStringFormatter( format );
					text = fmt.format( (String) result );

				}
				else if ( result instanceof Date )
				{
					DateFormatter fmt = context.getDateFormatter( format );
					text = fmt.format( (Date) result );
				}
				else
				{
					text = result.toString( );
				}
			}
			text = encodeHtmlText( text );
		}
		buffer.append( text );
		return value;
	}

	protected String encodeHtmlText( String text )
	{
		return text.replaceAll( "<", "&gt;" );
	}

	public Object visitImage( TextTemplate.ImageNode node, Object value )
	{
		Object imageContent = null;
		if ( TextTemplate.ImageNode.IMAGE_TYPE_EXPR == node.getType( ) )
		{
			imageContent = values.get( node.getExpr( ) );
		}
		else
		{
			EmbeddedImage image = context.getDesign( ).findImage(
					node.getImageName( ) );
			if ( image != null )
			{
				imageContent = image.getData( );
			}
		}
		if ( imageContent instanceof byte[] )
		{
			Image image = new Image( (byte[]) imageContent, null );
			image.setRenderOption( context.getRenderOption() );
			image.setReportRunnable( context.getRunnable() );

			if ( imageHandler != null )
			{
				String src = imageHandler.onCustomImage( image, renderContext );
				if( src != null )
				{
					buffer.append( "<img src=\"" );
					buffer.append( src );
					buffer.append( "\" " );
					Iterator iter = node.getAttributes( ).entrySet( ).iterator( );
					while ( iter.hasNext( ) )
					{
						Map.Entry entry = (Map.Entry) iter.next( );
	
						Object attrName = entry.getKey( );
						Object attrValue = entry.getValue( );
						if ( attrName != null && attrValue != null )
						{
							buffer.append( attrName.toString( ) );
							buffer.append( "=\"" );
							buffer.append( attrValue.toString( ) );
							buffer.append( "\" " );
						}
					}
					buffer.append( ">" );
				}
			}
		}
		return value;
	}
}
