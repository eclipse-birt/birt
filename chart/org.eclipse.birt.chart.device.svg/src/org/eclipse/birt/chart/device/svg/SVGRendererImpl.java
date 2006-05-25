/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device.svg;

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

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
import org.eclipse.birt.chart.device.swing.SwingRendererImpl;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.StructureChangeEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
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
	protected List scriptRefList = null;
	protected List scriptCodeList = null;
	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.svg/trace" ); //$NON-NLS-1$

	/**
	 * 
	 */
	private IUpdateNotifier _iun = null;
	



	protected SVGInteractiveRenderer ivRenderer;
	
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
		
		ivRenderer = new SVGInteractiveRenderer( getULocale( ) );
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
	 * The SVG namespace http://www.w3.org/2000/svg
	 */
	static private final String XMLNS = "http://www.w3.org/2000/svg"; //$NON-NLS-1$
	/**
	 * The  xmlns:xlink is
	 * "http://www.w3.org/1999/xlink".
	 */
	static private final String XMLNSXINK = "http://www.w3.org/1999/xlink"; //$NON-NLS-1$
	
	/**
	 * 
	 */
	protected Object oOutputIdentifier = null;

	/**
	 * Document object that represents the SVG
	 */
	protected Document dom;
	
	/**
	 * SVG Graphic context
	 */
	protected SVGGraphics2D svggc;
	

	
	/**
	 * Property that determins if the SVG should resize to the containing element dimensions.
	 */
	protected boolean _resizeSVG = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setProperty( String sProperty, Object oValue )
	{
		super.setProperty( sProperty, oValue );
		if ( sProperty.equals( IDeviceRenderer.UPDATE_NOTIFIER ) )
		{
			_iun = (IUpdateNotifier) oValue;
			ivRenderer.setIUpdateNotifier( _iun );
			
		}		
		else if ( sProperty.equals( IDeviceRenderer.EXPECTED_BOUNDS ) )
		{
			final Bounds bo = (Bounds) oValue;
			try
			{
				dom = createSvgDocument( bo.getWidth( ), bo.getHeight( ) );
				svggc = new SVGGraphics2D( dom );
				ivRenderer.setSVG2D( svggc );
				//Create the hotspot layer
				ivRenderer.createHotspotLayer(dom);
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
		else if ( sProperty.equals( ISVGConstants.JAVASCRIPT_CODE_LIST ) )
		{
			scriptCodeList = (List)oValue;
		}
		else if ( sProperty.equals( ISVGConstants.JAVASCRIPT_URL_REF_LIST ) )
		{
			scriptRefList = (List)oValue;
		}
		else if ( sProperty.equals( ISVGConstants.RESIZE_SVG ) )
		{
			_resizeSVG = ((Boolean)oValue).booleanValue();
		}
	}
	

	
	protected void addScripts(){
		if (this.scriptCodeList != null){
			for (int x = 0; x< scriptCodeList.size(); x++){
				String code = (String)scriptCodeList.get(x);
				((SVGGraphics2D)_g2d).addScript(code);				
			}
		}
		if (this.scriptRefList != null){
			for (int x = 0; x< scriptRefList.size(); x++){
				String ref = (String)scriptRefList.get(x);
				((SVGGraphics2D)_g2d).addScriptRef(ref);				
			}
		}
	}

	/**
	 * 
	 * @param os
	 * @throws RenderingException
	 */
	public void after( ) throws ChartException
	{
		super.after( );
		
		ivRenderer.addInteractivity( );
			addScripts();
		((SVGGraphics2D)_g2d).flush();		
		
		//make sure we add the hotspot layer to the bottom layer of the svg
		dom.getDocumentElement().appendChild(ivRenderer.getHotspotLayer( ));
		
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
		
		ivRenderer.clear( );
		
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

			//need to check if we should use sun's implementation of the transform factory.  This is needed to work with jdk1.4 and jdk1.5 with tomcat
			checkForTransformFactoryImpl();
			TransformerFactory transFactory = TransformerFactory.newInstance( );
			Transformer transformer = transFactory.newTransformer( );

			transformer.transform( source, result );
		}

	}

	/**
	 * Check to see if we should change the implementation of the TransformFactory.
	 *
	 */
	private void checkForTransformFactoryImpl(){
		try {
			Class.forName("org.apache.xalan.processor.TransformerFactoryImpl");
		} catch (ClassNotFoundException e) {
			//Force using sun's implementation
			System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
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
		Document svgDocument = domImpl.createDocument( XMLNS, "svg", dType ); //$NON-NLS-1$
		svgDocument.getDocumentElement().setAttribute("xmlns", XMLNS); //$NON-NLS-1$
		svgDocument.getDocumentElement().setAttribute("xmlns:xlink", XMLNSXINK); //$NON-NLS-1$
	
		if (_resizeSVG)
			svgDocument.getDocumentElement().setAttribute("onload","resizeSVG(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
		
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
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getULocale( ) ) ); //$NON-NLS-1$
			return;
		}
		
		Trigger[] triggers = ie.getTriggers( );
		if ( triggers == null )
		{
			return;
		}

		///////////////////////////////////////////////
		//Create the hotspot and add the hotspot on 
		//the SVG hotspot layer
		///////////////////////////////////////////////
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
		else if ( pre instanceof RectangleRenderEvent )
		{
			final Bounds boRect = ( (RectangleRenderEvent) pre ).getBounds( );

			elm = svggc.createRect(boRect.getLeft(), boRect.getTop(), boRect.getWidth(), boRect.getHeight());
		}
		else if ( pre instanceof AreaRenderEvent )
		{		
			AreaRenderEvent are = (AreaRenderEvent)pre;
						
			final GeneralPath gp = new GeneralPath( );
			PrimitiveRenderEvent subPre;
			
			for ( int i = 0; i < are.getElementCount( ); i++ )
			{
				subPre = are.getElement( i );
				if ( subPre instanceof ArcRenderEvent )
				{
					final ArcRenderEvent acre = (ArcRenderEvent) subPre;
					final Arc2D.Double a2d = new Arc2D.Double( acre.getTopLeft( )
							.getX( ),
							acre.getTopLeft( ).getY( ),
							acre.getWidth( ),
							acre.getHeight( ),
							acre.getStartAngle( ),
							acre.getAngleExtent( ),
							toSwingArcType( acre.getStyle( ) ) );
					gp.append( a2d, true );
				}
				else if ( subPre instanceof LineRenderEvent )
				{
					final LineRenderEvent lre = (LineRenderEvent) subPre;
					final Line2D.Double l2d = new Line2D.Double( lre.getStart( )
							.getX( ),
							lre.getStart( ).getY( ),
							lre.getEnd( ).getX( ),
							lre.getEnd( ).getY( ) );
					gp.append( l2d, true );
				}
			}
			elm = svggc.createGeneralPath(gp);
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

//				Shape prevClip = _g2d.getClip( );
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
	
		ivRenderer.prepareInteractiveEvent( elm, ie, triggers );
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
	

	
	
		
	public void drawArc(ArcRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.drawArc(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	public void drawArea(AreaRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.drawArea(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	public void drawImage(ImageRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.drawImage(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}
	
	protected Image createImage( byte[] data )
	{
		return new SVGImage(super.createImage(data), null, data );
	}
	

	public void drawLine(LineRenderEvent lre) throws ChartException {
		ivRenderer.groupPrimitive(lre, false);
		super.drawLine(lre);
		ivRenderer.ungroupPrimitive(lre, false);
	}

	public void drawOval(OvalRenderEvent ore) throws ChartException {
		ivRenderer.groupPrimitive(ore, false);
		super.drawOval(ore);
		ivRenderer.ungroupPrimitive(ore, false);
	}

	public void drawPolygon(PolygonRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.drawPolygon(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}

	public void drawRectangle(RectangleRenderEvent rre) throws ChartException {
		ivRenderer.groupPrimitive(rre, false);
		super.drawRectangle(rre);
		ivRenderer.ungroupPrimitive(rre, false);
	}

	public void fillArc(ArcRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.fillArc(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	public void fillArea(AreaRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.fillArea(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	public void fillOval(OvalRenderEvent ore) throws ChartException {
		ivRenderer.groupPrimitive(ore, false);
		super.fillOval(ore);
		ivRenderer.ungroupPrimitive(ore, false);
	}

	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.fillPolygon(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}

	public void fillRectangle(RectangleRenderEvent rre) throws ChartException {
		ivRenderer.groupPrimitive(rre, false);
		super.fillRectangle(rre);
		ivRenderer.ungroupPrimitive(rre, false);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawText(org.eclipse.birt.chart.event.TextRenderEvent)
	 */
	public void drawText( TextRenderEvent tre ) throws ChartException
	{
		ivRenderer.groupPrimitive(tre, true);
		SVGTextRenderer tr = SVGTextRenderer.instance( (SVGDisplayServer) _ids );
		switch ( tre.getAction( ) )
		{
			case TextRenderEvent.UNDEFINED :
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.RENDERING,
						"exception.missing.text.render.action", //$NON-NLS-1$
						Messages.getResourceBundle( getULocale( ) ) );

			case TextRenderEvent.RENDER_SHADOW_AT_LOCATION :
				tr.renderShadowAtLocation( this,
						tre.getTextPosition( ),
						tre.getLocation( ),
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_AT_LOCATION :
				tr.renderTextAtLocation( this,
						tre.getTextPosition( ),
						tre.getLocation( ),
						tre.getLabel( ) );
				break;

			case TextRenderEvent.RENDER_TEXT_IN_BLOCK :
				tr.renderTextInBlock( this,
						tre.getBlockBounds( ),
						tre.getBlockAlignment( ),
						tre.getLabel( ) );
				break;
		}
		ivRenderer.ungroupPrimitive(tre, true);
	}	
	

}
