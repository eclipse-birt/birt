
package org.eclipse.birt.report.engine.executor.template;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

public class TemplateExecutor implements TextTemplate.Visitor
{
	protected StringBuffer buffer;
	protected HashMap values;
	protected ExecutionContext context;
	protected IHTMLImageHandler imageHandler;

	public TemplateExecutor( ExecutionContext context, IHTMLImageHandler imageHandler)
	{
		this.context = context;
		this.imageHandler = imageHandler;
/*		Object im = option.getOutputSetting().get(HTMLRenderOption.IMAGE_HANDLER);
		if ( im != null && im instanceof IHTMLImageHandler )
		{
			imageHandler = (IHTMLImageHandler) im;
		}*/
	}

	public String execute( TextTemplate template, HashMap values )
	{
		this.buffer = new StringBuffer( );
		this.values = values;

		if( template == null )
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
		Object result = values.get( node.getValue( ) );
		if ( result instanceof byte[] )
		{
			Image image = new Image( (byte[]) result, null );
			if ( imageHandler != null )
			{
				imageHandler.onCustomImage( image, null );
			}
		}
		return value;
	}
}
