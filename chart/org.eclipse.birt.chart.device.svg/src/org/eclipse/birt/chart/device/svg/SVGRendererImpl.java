/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.PluginSettings;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * Provides a reference implementation of a SVG device renderer. It translates
 * chart primitives into standard SVG v1.1 rendering primitives.
 */
public class SVGRendererImpl extends SwingRendererImpl
{

	/**
	 *  
	 */
	public SVGRendererImpl( )
	{
		if ( System.getProperty( "STANDALONE" ) == null )
		{
			final PluginSettings ps = PluginSettings.instance( );
			try
			{
				_ids = ps.getDisplayServer( "ds.SVG" ); //$NON-NLS-1$
			}
			catch ( ChartException pex )
			{
				DefaultLoggerImpl.instance( ).log( pex );
			}
		}
		else
			_ids = new SVGDisplayServer( );
	}
	/**
	 * The SVG version is "-//W3C//DTD SVG 1.0//EN".
	 */
	static private final String SVG_VERSION = "-//W3C//DTD SVG 1.0//EN";

	/**
	 * The SVG DTD is
	 * "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd".
	 */
	static private final String SVG_DTD = "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd";
	/**
	 *  
	 */
	private Object oOutputIdentifier = null;

	protected Document dom;
	protected SVGGraphics2D svggc;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public final void setProperty( String sProperty, Object oValue )
	{
		super.setProperty( sProperty, oValue );
		if ( sProperty.equals( IDeviceRenderer.EXPECTED_BOUNDS ) )
		{
			final Bounds bo = (Bounds) oValue;
			try
			{
				dom = createSvgDocument( bo.getWidth( ), bo.getHeight( ) );
				svggc = new SVGGraphics2D( dom );
				super.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, svggc );
			}
			catch ( Exception e )
			{
				e.printStackTrace( );
			}
		}
		else if ( sProperty.equals( IDeviceRenderer.FILE_IDENTIFIER ) )
		{
			oOutputIdentifier = oValue;
		}
	}

	/**
	 * 
	 * @param os
	 * @throws RenderingException
	 */
	public final void after( ) throws ChartException
	{
		super.after( );
		if ( oOutputIdentifier instanceof OutputStream ) // OUTPUT STREAM
		{
			try
			{
				writeDocumentToOutputStream( dom,
						(OutputStream) oOutputIdentifier );
			}
			catch ( Exception ex )
			{
				throw new ChartException( ChartException.RENDERING, ex );
			}
		}
		else if ( oOutputIdentifier instanceof String )
		{
			FileOutputStream fos = null;
			try
			{
				fos = new FileOutputStream( (String) oOutputIdentifier );
				writeDocumentToOutputStream( dom, fos );
				fos.close( );
			}
			catch ( Exception ex )
			{
				throw new ChartException( ChartException.RENDERING, ex );
			}
		}
		else
		{
			throw new ChartException( ChartException.RENDERING,
					"Unable to write chart image to GIF output handle defined by "
							+ oOutputIdentifier,
					null );
		}
	}

	/**
	 * Writes the XML document to an output stream
	 * 
	 * @param svgDocument
	 * @param outputStream
	 * @throws Exception
	 */
	private void writeDocumentToOutputStream( Document svgDocument,
			OutputStream outputStream ) throws Exception
	{
		if ( svgDocument != null && outputStream != null )
		{
			OutputStreamWriter writer = null;

			writer = new OutputStreamWriter( outputStream, "UTF-8" );

			DOMSource source = new DOMSource( svgDocument );
			StreamResult result = new StreamResult( writer );

			TransformerFactory transFactory = TransformerFactory.newInstance( );
			Transformer transformer = transFactory.newTransformer( );

			transformer.transform( source, result );
		}

	}

	/**
	 * Creates an SVG document and assigns width and height to the root "svg"
	 * element.
	 * 
	 * @return Document the SVG document
	 * @throws Exception
	 */
	protected Document createSvgDocument( ) throws Exception
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance( );
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder( );
		DOMImplementation domImpl = builder.getDOMImplementation( );
		DocumentType dType = domImpl.createDocumentType( "svg",
				SVG_VERSION,
				SVG_DTD );
		Document svgDocument = domImpl.createDocument( null, "svg", dType );
		return svgDocument;
	}

	protected Document createSvgDocument( double width, double height )
			throws Exception
	{
		Document svgDocument = createSvgDocument( );
		svgDocument.getDocumentElement( ).setAttribute( "width",
				Double.toString( width ) );
		svgDocument.getDocumentElement( ).setAttribute( "height",
				Double.toString( height ) );
		return svgDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IStructureDefinitionListener#changeStructure(org.eclipse.birt.chart.event.StructureChangeEvent)
	 */
	public void changeStructure( StructureChangeEvent scev )
	{
		Object sourceObj = scev.getSource( );
		switch ( scev.getEventType( ) )
		{
			case StructureChangeEvent.BEFORE :
				addGroupStructure( sourceObj );
				break;
			case StructureChangeEvent.AFTER :
				removeGroupStructure( sourceObj );
				break;

		}
	}

	protected void removeGroupStructure( Object block )
	{
		if ( ( block instanceof TitleBlock )
				|| ( block instanceof Legend )
				|| ( block instanceof Plot )
				|| ( block instanceof LabelBlock )
				|| ( block instanceof Series )
				|| ( block instanceof DataPointHints ) )
			svggc.popParent( );
	}

	protected void addGroupStructure( Object block )
	{
		if ( block instanceof TitleBlock )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "title" );
			svggc.pushParent( group );
		}
		else if ( block instanceof Legend )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "legend" );
			svggc.pushParent( group );
		}
		else if ( block instanceof Plot )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "plot" );
			svggc.pushParent( group );
		}
		else if ( block instanceof LabelBlock )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "label" );
			svggc.pushParent( group );
		}
		else if ( block instanceof Series )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "series" );
			svggc.pushParent( group );
		}
		else if ( block instanceof DataPointHints )
		{
			Element group = svggc.dom.createElement( "g" );
			group.setAttribute( "id", "dp" + block.hashCode( ) );
			svggc.pushParent( group );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#enableInteraction(org.eclipse.birt.chart.event.InteractionEvent)
	 */
	public void enableInteraction( InteractionEvent ie ) throws ChartException
	{
		Trigger[] triggers = ie.getTriggers( );
		for ( int x = 0; x < triggers.length; x++ )
		{
			Trigger tg = triggers[x];
			if ( tg.getAction( ).getType( ) == ActionType.SHOW_TOOLTIP_LITERAL )
			{
				svggc.drawTooltip( ( (TooltipValue) tg.getAction( ).getValue( ) ).getText( ) );
			}
			else if ( tg.getAction( ).getType( ) == ActionType.URL_REDIRECT_LITERAL )
			{
				svggc.getCurrentParent( )
						.setAttribute( "onmousedown",
								"parent.location='"
										+ ( (URLValue) tg.getAction( )
												.getValue( ) ).getBaseUrl( )
										+ "'" );
			}
		}
	}
}