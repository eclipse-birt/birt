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

import java.awt.Shape;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.device.extension.i18n.Messages;
import org.eclipse.birt.chart.device.plugin.ChartDeviceExtensionPlugin;
import org.eclipse.birt.chart.device.svg.plugin.ChartDeviceSVGPlugin;
import org.eclipse.birt.chart.device.swing.SwingEventHandler;
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
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

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.svg/trace" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private IUpdateNotifier _iun = null;
	
	/**
	 * 
	 */
	public SVGRendererImpl( )
	{
		if ( System.getProperty( "STANDALONE" ) == null ) //$NON-NLS-1$
		{
			final PluginSettings ps = PluginSettings.instance( );
			try
			{
				_ids = ps.getDisplayServer( "ds.SVG" ); //$NON-NLS-1$
			}
			catch ( ChartException pex )
			{
				logger.log( pex );
			}
		}
		else
			_ids = new SVGDisplayServer( );
	}
	/**
	 * The SVG version is "-//W3C//DTD SVG 1.0//EN".
	 */
	static private final String SVG_VERSION = "-//W3C//DTD SVG 1.0//EN"; //$NON-NLS-1$

	/**
	 * The SVG DTD is
	 * "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd".
	 */
	static private final String SVG_DTD = "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"; //$NON-NLS-1$
	/**
	 * 
	 */
	private Object oOutputIdentifier = null;

	/**
	 * Document object that represents the SVG
	 */
	protected Document dom;
	
	/**
	 * SVG Graphic context
	 */
	protected SVGGraphics2D svggc;
	
	/**
	 * Element that represents the hot spot layer
	 */
	protected Element hotspotLayer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public final void setProperty( String sProperty, Object oValue )
	{
		super.setProperty( sProperty, oValue );
		if ( sProperty.equals( IDeviceRenderer.UPDATE_NOTIFIER ) )
		{
			_iun = (IUpdateNotifier) oValue;
		}		
		else if ( sProperty.equals( IDeviceRenderer.EXPECTED_BOUNDS ) )
		{
			final Bounds bo = (Bounds) oValue;
			try
			{
				dom = createSvgDocument( bo.getWidth( ), bo.getHeight( ) );
				svggc = new SVGGraphics2D( dom );
				//Create the hotspot layer
				hotspotLayer = createHotspotLayer(dom);
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
	
	protected Element createHotspotLayer(Document dom){
		Element hotspot = dom.createElement("g");
		hotspot.setAttribute( "style", "fill-opacity:0.01;fill:#FFFFFF;" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return hotspot;		
	}

	/**
	 * 
	 * @param os
	 * @throws RenderingException
	 */
	public final void after( ) throws ChartException
	{
		super.after( );
		
		//make sure we add the hotspot layer to the bottom layer of the svg
		dom.getDocumentElement().appendChild(hotspotLayer);
		
		if ( oOutputIdentifier instanceof OutputStream ) // OUTPUT STREAM
		{
			try
			{
				writeDocumentToOutputStream( dom,
						(OutputStream) oOutputIdentifier );
			}
			catch ( Exception ex )
			{
				throw new ChartException( ChartDeviceSVGPlugin.ID,
						ChartException.RENDERING,
						ex );
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
				throw new ChartException( ChartDeviceSVGPlugin.ID,
						ChartException.RENDERING,
						ex );
			}
		}
		else
		{
			throw new ChartException( ChartDeviceSVGPlugin.ID,
					ChartException.RENDERING,
					"SVGRendererImpl.exception.UnableToWriteChartImage", //$NON-NLS-1$
					new Object[]{
						oOutputIdentifier
					},
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

			writer = new OutputStreamWriter( outputStream, "UTF-8" ); //$NON-NLS-1$

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
		DocumentType dType = domImpl.createDocumentType( "svg", //$NON-NLS-1$
				SVG_VERSION, SVG_DTD );
		Document svgDocument = domImpl.createDocument( null, "svg", dType ); //$NON-NLS-1$
		return svgDocument;
	}

	protected Document createSvgDocument( double width, double height )
			throws Exception
	{
		Document svgDocument = createSvgDocument( );
		svgDocument.getDocumentElement( ).setAttribute( "width", //$NON-NLS-1$
				Double.toString( width ) );
		svgDocument.getDocumentElement( ).setAttribute( "height", //$NON-NLS-1$
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
//		Object sourceObj = scev.getSource( );
//		switch ( scev.getEventType( ) )
//		{
//			case StructureChangeEvent.BEFORE :
//				addGroupStructure( sourceObj );
//				break;
//			case StructureChangeEvent.AFTER :
//				removeGroupStructure( sourceObj );
//				break;
//
//		}
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
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "title" ); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent( group );
		}
		else if ( block instanceof Legend )
		{
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "legend" ); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent( group );
		}
		else if ( block instanceof Plot )
		{
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "plot" ); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent( group );
		}
		else if ( block instanceof LabelBlock )
		{
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "label" ); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent( group );
		}
		else if ( block instanceof Series )
		{
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "series" ); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent( group );
		}
		else if ( block instanceof DataPointHints )
		{
			Element group = svggc.dom.createElement( "g" ); //$NON-NLS-1$
			group.setAttribute( "id", "dp" + block.hashCode( ) ); //$NON-NLS-1$ //$NON-NLS-2$
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
		if ( triggers == null )
		{
			return;
		}

		final PrimitiveRenderEvent pre = ie.getHotSpot( );
		Element elm = null;

		if ( pre instanceof PolygonRenderEvent )
		{
			final Location[] loa = ( (PolygonRenderEvent) pre ).getPoints( );

			int[][] pa = getCoordinatesAsInts( loa );

			elm = svggc.createPolygon( pa[0], pa[1], pa[0].length );
		}
		else if ( pre instanceof OvalRenderEvent )
		{
			final Bounds boEllipse = ( (OvalRenderEvent) pre ).getBounds( );

			elm = svggc.createOval( boEllipse.getLeft( ),
					boEllipse.getTop( ),
					boEllipse.getWidth( ),
					boEllipse.getHeight( ) );
		}
		else if ( pre instanceof ArcRenderEvent )
		{
			final ArcRenderEvent are = (ArcRenderEvent) pre;
			
			if ( are.getInnerRadius( ) >= 0
					&& are.getOuterRadius( ) > 0
					&& are.getInnerRadius( ) < are.getOuterRadius( ) )
			{
				Shape outerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getOuterRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getOuterRadius( ) )
								/ 2,
						2 * are.getOuterRadius( ),
						2 * are.getOuterRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );
				Shape innerArc = new Arc2D.Double( are.getTopLeft( ).getX( )
						+ ( are.getWidth( ) - 2 * are.getInnerRadius( ) )
						/ 2,
						are.getTopLeft( ).getY( )
								+ ( are.getHeight( ) - 2 * are.getInnerRadius( ) )
								/ 2,
						2 * are.getInnerRadius( ),
						2 * are.getInnerRadius( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						Arc2D.PIE );

				Area fArea = new Area( outerArc );
				fArea.exclusiveOr( new Area( innerArc ) );

				Shape prevClip = _g2d.getClip( );
//				_g2d.setClip( fArea );
				elm = svggc.createGeneralPath( fArea );
//				_g2d.setClip( prevClip );
			}
			else
			{
				elm = svggc.createGeneralPath( new Arc2D.Double( are.getTopLeft( ).getX( ),
						are.getTopLeft( ).getY( ),
						are.getWidth( ),
						are.getHeight( ),
						are.getStartAngle( ),
						are.getAngleExtent( ),
						toSwingArcType( are.getStyle( ) ) ) );
			}
			
		}
		

		if (elm != null) {
			for (int x = 0; x < triggers.length; x++) {
				Trigger tg = triggers[x];
				
				switch (tg.getAction().getType().getValue()) {
				case ActionType.SHOW_TOOLTIP:
					Element title = svggc.dom.createElement("title"); //$NON-NLS-1$
					title.appendChild(svggc.dom
							.createTextNode(((TooltipValue) tg.getAction()
									.getValue()).getText()));
					elm.appendChild(title);
					elm.setAttribute("onmouseout", "TM.remove()"); //$NON-NLS-1$ //$NON-NLS-2$
					elm.setAttribute("onmouseover", "TM.show(evt)"); //$NON-NLS-1$ //$NON-NLS-2$		
					break;
				case ActionType.URL_REDIRECT:
					elm.setAttribute("onmousedown", //$NON-NLS-1$
							"parent.location='" //$NON-NLS-1$
									+ ((URLValue) tg.getAction().getValue())
											.getBaseUrl() + "'"); //$NON-NLS-1$
					setCursor(elm);
					break;
/*
				case ActionType.TOGGLE_VISIBILITY :
					final StructureSource src = (StructureSource) pre.getSource( );
					System.out.println(" " + src.getType() + " "  + StructureType.LEGEND);
					if ( src.getType( ) == StructureType.LEGEND )
					{						
						final Legend seRT = (Legend)src.getSource( );
//						
//						logger.log( ILogger.INFORMATION,
//								Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
//										getLocale() )
//										+ seRT );
//						Series seDT = null;
//						try
//						{
//							seDT = findDesignTimeSeries( seRT ); // LOCATE
							// THE
							// CORRESPONDING
							// DESIGN-TIME
							// SERIES
							elm.setAttribute("onmousedown", //$NON-NLS-1$
									"toggleVisibility('" //$NON-NLS-1$
											+ seRT.hashCode() + "')"); //$NON-NLS-1$
							setCursor(elm);
//						}
//						catch ( ChartException oosx )
//						{
//							logger.log( oosx );
//							return;
//						}
					}
					break;
*/										
				}
			}

			hotspotLayer.appendChild( elm );
		}
		
	}
	
	protected void setCursor( Element currentElement )
	{
		String style = currentElement.getAttribute( "style" ); //$NON-NLS-1$
		if ( style == null )
		{
			style = ""; //$NON-NLS-1$
		}
		currentElement.setAttribute( "style", style + "cursor:pointer;" ); //$NON-NLS-1$ //$NON-NLS-2$
	}
		

	/**
	 * 
	 * @param iArcStyle
	 * @return
	 */
	private static final int toSwingArcType( int iArcStyle )
	{
		switch ( iArcStyle )
		{
			case ArcRenderEvent.OPEN :
				return Arc2D.OPEN;
			case ArcRenderEvent.CLOSED :
				return Arc2D.CHORD;
			case ArcRenderEvent.SECTOR :
				return Arc2D.PIE;
		}
		return -1;
	}
	
}