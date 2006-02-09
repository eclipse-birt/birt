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

import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.image.ImageObserver;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.ImageIcon;
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
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.event.StructureType;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedStructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.Base64;
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
	
	private Map componentPrimitives = new Hashtable();
	private List scripts = new Vector();
	protected List scriptRefList = null;
	protected List scriptCodeList = null;
	
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
		else if ( sProperty.equals( ISVGConstants.JAVASCRIPT_CODE_LIST ) )
		{
			scriptCodeList = (List)oValue;
		}
		else if ( sProperty.equals( ISVGConstants.JAVASCRIPT_URL_REF_LIST ) )
		{
			scriptRefList = (List)oValue;
		}
	}
	
	protected Element createHotspotLayer(Document dom){
		Element hotspot = dom.createElement("g"); //$NON-NLS-1$
		hotspot.setAttribute( "style", "fill-opacity:0.01;fill:#FFFFFF;" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return hotspot;		
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
	public final void after( ) throws ChartException
	{
		super.after( );
		addScripts();
		((SVGGraphics2D)_g2d).flush();		
		
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
		
		componentPrimitives.clear();
		scripts.clear();
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
		Document svgDocument = domImpl.createDocument( XMLNS, "svg", dType ); //$NON-NLS-1$
		svgDocument.getDocumentElement().setAttribute("xmlns", XMLNS);
		svgDocument.getDocumentElement().setAttribute("xmlns:xlink", XMLNSXINK);
		
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
					Messages.getString( "exception.missing.component.interaction", getLocale( ) ) ); //$NON-NLS-1$
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
		

		//////////////////////////////////////
		//Add event handling to the hotspot
		//////////////////////////////////////
		
		if (elm != null) {
			for (int x = 0; x < triggers.length; x++) {
				Trigger tg = triggers[x];
				
				final StructureSource src = (StructureSource) ie.getSource( );
				
				switch (tg.getAction().getType().getValue()) {
				case ActionType.SHOW_TOOLTIP:
					String tooltipText = ((TooltipValue) tg.getAction().getValue()).getText();
					//make sure the tooltip text is not empty
					if ((tooltipText != null) && (tooltipText.trim().length() > 0)){
						Element title = svggc.dom.createElement("title"); //$NON-NLS-1$
						title.appendChild(svggc.dom
								.createTextNode(tooltipText));
						elm.appendChild(title);
						elm.setAttribute("onmouseout", "TM.remove()"); //$NON-NLS-1$ //$NON-NLS-2$
						elm.setAttribute("onmouseover", "TM.show(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
					}
					break;
				case ActionType.URL_REDIRECT:
					URLValue urlValue = ((URLValue) tg.getAction().getValue());
					Element aLink = ((SVGGraphics2D)_g2d).createElement("a"); //$NON-NLS-1$
					aLink.setAttribute("xlink:href", urlValue.getBaseUrl()); //$NON-NLS-1$
					if (urlValue.getTarget() != null)
						aLink.setAttribute("target", urlValue.getTarget()); //$NON-NLS-1$ 
					aLink.appendChild(elm);
					elm = aLink;
					break;

				case ActionType.TOGGLE_VISIBILITY :
					
					if ( src.getType( ) == StructureType.SERIES )
					{
						final Series seRT = (Series) src.getSource( );					
						logger.log( ILogger.INFORMATION,
								Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
										getLocale() )
										+ seRT );
						Series seDT = null;
						try
						{
							// THE
							// CORRESPONDING
							// DESIGN-TIME
							// SERIES							
							seDT = findDesignTimeSeries( seRT ); // LOCATE
							List components = (List)componentPrimitives.get(seDT);
							if (components != null){
								Iterator iter = components.iterator();
								StringBuffer sb = new StringBuffer();
								sb.append(seDT.hashCode());
								if (iter.hasNext())
									sb.append(",new Array(");							 //$NON-NLS-1$
								while (iter.hasNext()){
									sb.append("'").append(iter.next()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
									if (iter.hasNext())
										sb.append(","); //$NON-NLS-1$
								}
								if (components.size() > 0)
									sb.append(")"); //$NON-NLS-1$
								elm.setAttribute("onmousedown", //$NON-NLS-1$
										"toggleVisibility(evt, " //$NON-NLS-1$
												+ sb.toString() + ")"); //$NON-NLS-1$							
								setCursor(elm);
	
								//should define style class and set the visibility to visible
								((SVGGraphics2D)_g2d).addCSSStyle(".class"+seDT.hashCode(), "visibility", "visible"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
							}
						}
						catch ( ChartException oosx )
						{
							logger.log( oosx );
							return;
						}
					}
					break;
				case ActionType.HIGHLIGHT :
					
					if ( src.getType( ) == StructureType.SERIES )
					{
						final Series seRT = (Series) src.getSource( );					
						logger.log( ILogger.INFORMATION,
								Messages.getString( "info.toggle.visibility", //$NON-NLS-1$
										getLocale() )
										+ seRT );
						String scriptEvent = getJsScriptEvent(tg.getCondition().getValue());
						if (scriptEvent != null){
							
							Series seDT = null;
							try
							{
								// THE
								// CORRESPONDING
								// DESIGN-TIME
								// SERIES							
								seDT = findDesignTimeSeries( seRT ); // LOCATE
								List components = (List)componentPrimitives.get(seDT);
								if (components != null){
									Iterator iter = components.iterator();
									StringBuffer sb = new StringBuffer();
									sb.append(seDT.hashCode());
									if (iter.hasNext())
										sb.append(",new Array(");							 //$NON-NLS-1$
									while (iter.hasNext()){
										sb.append("'").append(iter.next()).append("'"); //$NON-NLS-1$ //$NON-NLS-2$
										if (iter.hasNext())
											sb.append(","); //$NON-NLS-1$
									}
									if (components.size() > 0)
										sb.append(")"); //$NON-NLS-1$
									elm.setAttribute(scriptEvent, //$NON-NLS-1$
											"highlight(evt, " //$NON-NLS-1$
													+ sb.toString() + ")"); //$NON-NLS-1$							
									setCursor(elm);
		
									//should define style class and set the visibility to visible
									((SVGGraphics2D)_g2d).addCSSStyle(".class"+seDT.hashCode(), "visibility", "visible"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
								}
							}
							catch ( ChartException oosx )
							{
								logger.log( oosx );
								return;
							}
						}
					}
					break;					
				case ActionType.INVOKE_SCRIPT:
					final StructureSource sructSource = (StructureSource) ie.getSource( );
					String scriptEvent = getJsScriptEvent(tg.getCondition().getValue());
					if (scriptEvent != null){
						String script = ((ScriptValue) tg.getAction().getValue()).getScript();
						String callbackFunction = "callback"+Math.abs(script.hashCode())+"(evt,"+sructSource.getSource().hashCode()+");"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						elm.setAttribute(scriptEvent, callbackFunction); 
						setCursor(elm);
						if (!(scripts.contains(script))){
							
							((SVGGraphics2D)_g2d).addScript("function callback"+Math.abs(script.hashCode())+"(evt,source)" +"{"+script+"}"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
							scripts.add(script);
						}
					}
					break;

										
				}
			}

			hotspotLayer.appendChild( elm );
		}
		
	}
	private String getJsScriptEvent(int condition){
		switch(condition){
		case TriggerCondition.MOUSE_HOVER :
			return "onmouseover"; //$NON-NLS-1$
		case TriggerCondition.MOUSE_CLICK :
			return "onclick"; //$NON-NLS-1$
		case TriggerCondition.ONCLICK :
			return"onclick"; //$NON-NLS-1$
		case TriggerCondition.ONDBLCLICK :
			return"onclick"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEDOWN :
			return"onmousedown"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEUP :
			return"onmouseup"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEOVER :
			return"onmouseover"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEMOVE :
			return"onmousemove"; //$NON-NLS-1$
		case TriggerCondition.ONMOUSEOUT :
			return"onmouseout"; //$NON-NLS-1$
		case TriggerCondition.ONFOCUS :
			return"onfocusin"; //$NON-NLS-1$
		case TriggerCondition.ONBLUR :
			return"onfocusout"; //$NON-NLS-1$
		case TriggerCondition.ONKEYDOWN :
			return"onkeydown"; //$NON-NLS-1$
		case TriggerCondition.ONKEYPRESS :
			return"onkeypress"; //$NON-NLS-1$
		case TriggerCondition.ONKEYUP :
			return"onkeyup"; //$NON-NLS-1$
		case TriggerCondition.ONLOAD :
			return"onload"; //$NON-NLS-1$
		}
		return null;
		
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
	/**
	 * Locates a design-time series corresponding to a given cloned run-time
	 * series.
	 * 
	 * @param seRT
	 * @return
	 */
	private final Series findDesignTimeSeries( Series seRT )
			throws ChartException
	{

		Series seDT = null;

		final Chart cmRT = _iun.getRunTimeModel( );
		final Chart cmDT = _iun.getDesignTimeModel( );

		if ( cmDT instanceof ChartWithAxes )
		{
			final ChartWithAxes cwaRT = (ChartWithAxes) cmRT;
			final ChartWithAxes cwaDT = (ChartWithAxes) cmDT;

			Axis[] axaBase = cwaRT.getPrimaryBaseAxes( );
			Axis axBase = axaBase[0];
			Axis[] axaOrthogonal = cwaRT.getOrthogonalAxes( axBase, true );
			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = axaBase[0].getSeriesDefinitions( );
			for ( j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				elSE = sd.getSeries( );
				for ( k = 0; k < elSE.size( ); k++ )
				{
					se = (Series) elSE.get( k );
					if ( seRT == se )
					{
						bFound = true;
						break;
					}
				}
				if ( bFound )
				{
					break;
				}
			}

			if ( !bFound )
			{
				// LOCATE INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN RUN TIME
				// MODEL
				for ( i = 0; i < axaOrthogonal.length; i++ )
				{
					elSD = axaOrthogonal[i].getSeriesDefinitions( );
					for ( j = 0; j < elSD.size( ); j++ )
					{
						sd = (SeriesDefinition) elSD.get( j );
						elSE = sd.getSeries( );
						for ( k = 0; k < elSE.size( ); k++ )
						{
							se = (Series) elSE.get( k );
							if ( seRT == se )
							{
								bFound = true;
								break;
							}
						}
						if ( bFound )
						{
							break;
						}
					}
					if ( bFound )
					{
						break;
					}
				}
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale() ) );
			}

			// MAP TO INDEXES FOR AXIS/SERIESDEFINITION/SERIES IN DESIGN TIME
			// MODEL
			axaBase = cwaDT.getPrimaryBaseAxes( );
			axBase = axaBase[0];
			axaOrthogonal = cwaDT.getOrthogonalAxes( axBase, true );
			if ( i == -1 )
			{
				elSD = axaBase[0].getSeriesDefinitions( );
			}
			else
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}
		else if ( cmDT instanceof ChartWithoutAxes )
		{
			final ChartWithoutAxes cwoaRT = (ChartWithoutAxes) cmRT;
			final ChartWithoutAxes cwoaDT = (ChartWithoutAxes) cmDT;

			EList elSD, elSE;
			SeriesDefinition sd;
			Series se = null;
			int i = -1, j = 0, k = 0;
			boolean bFound = false;

			elSD = cwoaRT.getSeriesDefinitions( );
			for ( j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				elSE = sd.getSeries( );
				for ( k = 0; k < elSE.size( ); k++ )
				{
					se = (Series) elSE.get( k );
					if ( seRT == se )
					{
						bFound = true;
						break;
					}
				}
				if ( bFound )
				{
					break;
				}
			}

			if ( !bFound )
			{
				i = 1;
				elSD = ( (SeriesDefinition) cwoaRT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );

				for ( j = 0; j < elSD.size( ); j++ )
				{
					sd = (SeriesDefinition) elSD.get( j );
					elSE = sd.getSeries( );
					for ( k = 0; k < elSE.size( ); k++ )
					{
						se = (Series) elSE.get( k );
						if ( seRT == se )
						{
							bFound = true;
							break;
						}
					}
					if ( bFound )
					{
						break;
					}
				}
			}

			if ( !bFound )
			{
				throw new ChartException( ChartDeviceExtensionPlugin.ID,
						ChartException.OUT_OF_SYNC,
						"info.cannot.find.series", //$NON-NLS-1$
						new Object[]{
							seRT
						},
						ResourceBundle.getBundle( Messages.DEVICE_EXTENSION,
								getLocale() ) );
			}

			if ( i == -1 )
			{
				elSD = cwoaDT.getSeriesDefinitions( );
			}
			else
			{
				elSD = ( (SeriesDefinition) cwoaDT.getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );
			}
			sd = (SeriesDefinition) elSD.get( j );
			elSE = sd.getSeries( );
			seDT = (Series) elSE.get( k );
		}

		return seDT;
	}

	/**
	 * Helper function that will determine if the source object is a series component of the chart.
	 * 
	 * @param src StructureSource that is stored in the primitive render event.
	 * @return null if the source object is not a series otherwise it returns the StructureSource.
	 */
	private StructureSource isSeries(StructureSource src){
		if (src instanceof WrappedStructureSource){
			WrappedStructureSource wss = (WrappedStructureSource)src;
			while (wss != null){
				if (wss.getType() == StructureType.SERIES) {
					return wss;
				}
				if (wss.getParent().getType() == StructureType.SERIES){
					return wss.getParent();
				}
				if (wss.getParent() instanceof WrappedStructureSource)
					wss = (WrappedStructureSource)wss.getParent();
				else
					wss = null;
			}
		}
		else if (src.getType() == StructureType.SERIES) return src;
		return null;
	}

	/**
	 * Groups the svg drawing instructions that represents this primitive events.  Each group is 
	 * assigned an id that identifies the source object of the primitive event
	 * 
	 * @param pre primitive render event
	 * @param drawText TODO
	 */
	protected void groupPrimitive(PrimitiveRenderEvent pre, boolean drawText) {
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getLocale( ) ) ); //$NON-NLS-1$
			return;
		}		
		SVGGraphics2D svg_g2d = (SVGGraphics2D) _g2d;

		// For now only group series elements
		if (pre.getSource() instanceof StructureSource) {
			final StructureSource src = isSeries((StructureSource) pre
					.getSource());
			if (src != null) {
				try {
					Series seDT = findDesignTimeSeries((Series) src.getSource()); // LOCATE
					String id = Integer.toString(pre.hashCode());
					// svg_g2d.setStyleClass("class"+seDT.hashCode());
					List components = (List) componentPrimitives.get(seDT);
					if (components == null) {
						components = new ArrayList();
						componentPrimitives.put(seDT, components);
					}

					// May have to group drawing instructions that come from the
					// same primitive render events.
					String idTemp = id;
					int counter = 1;
					while (components.contains(idTemp)) {
						idTemp = id + "@" + counter; //$NON-NLS-1$
						counter++;
					}

					components.add(idTemp);

					// Create group element that will contain the drawing
					// instructions that corresponds to the event
					Element primGroup = svg_g2d.createElement("g"); //$NON-NLS-1$
					svg_g2d.pushParent(primGroup);
					primGroup
							.setAttribute("id", seDT.hashCode() + "_" + idTemp); //$NON-NLS-1$ //$NON-NLS-2$
					primGroup.setAttribute("style", "visibility:visible;"); //$NON-NLS-1$ //$NON-NLS-2$

					if (!drawText)
						svg_g2d.setDeferStrokColor(primGroup);

				} catch (ChartException e) {
					logger.log(e);
					return;
				}
			}
		}
	}
	
	/**
	 * UnGroups the svg drawing instructions that represents this primitive events. 
	 * 
	 * @param pre primitive render event
	 * @param drawText TODO
	 */
	protected void ungroupPrimitive(PrimitiveRenderEvent pre, boolean drawText){
		if ( _iun == null )
		{
			logger.log( ILogger.WARNING,
					Messages.getString( "exception.missing.component.interaction", getLocale( ) ) ); //$NON-NLS-1$
			return;
		}		
		SVGGraphics2D svg_g2d = (SVGGraphics2D)_g2d;
//		svg_g2d.setStyleClass(null);		
//		svg_g2d.setId(null);
		
		//For now only ungroup series elements
		if (pre.getSource() instanceof StructureSource) {
			final StructureSource src = isSeries((StructureSource) pre.getSource( ));
			if ( src != null ){
				if (!drawText)
					svg_g2d.setDeferStrokColor(null);
				svg_g2d.popParent();
			}
		}
	}
	
	public void drawArc(ArcRenderEvent are) throws ChartException {
		groupPrimitive(are, false);
		super.drawArc(are);
		ungroupPrimitive(are, false);
	}

	public void drawArea(AreaRenderEvent are) throws ChartException {
		groupPrimitive(are, false);
		super.drawArea(are);
		ungroupPrimitive(are, false);
	}

	public void drawImage(ImageRenderEvent pre) throws ChartException {
		groupPrimitive(pre, false);
		super.drawImage(pre);
		ungroupPrimitive(pre, false);
	}
	
	protected Image createImage( byte[] data )
	{
		return new SVGImage(super.createImage(data), null, data );
	}
	

	public void drawLine(LineRenderEvent lre) throws ChartException {
		groupPrimitive(lre, false);
		super.drawLine(lre);
		ungroupPrimitive(lre, false);
	}

	public void drawOval(OvalRenderEvent ore) throws ChartException {
		groupPrimitive(ore, false);
		super.drawOval(ore);
		ungroupPrimitive(ore, false);
	}

	public void drawPolygon(PolygonRenderEvent pre) throws ChartException {
		groupPrimitive(pre, false);
		super.drawPolygon(pre);
		ungroupPrimitive(pre, false);
	}

	public void drawRectangle(RectangleRenderEvent rre) throws ChartException {
		groupPrimitive(rre, false);
		super.drawRectangle(rre);
		ungroupPrimitive(rre, false);
	}

	public void drawText(TextRenderEvent tre) throws ChartException {
		groupPrimitive(tre, true);
		super.drawText(tre);
		ungroupPrimitive(tre, true);
	}

	public void fillArc(ArcRenderEvent are) throws ChartException {
		groupPrimitive(are, false);
		super.fillArc(are);
		ungroupPrimitive(are, false);
	}

	public void fillArea(AreaRenderEvent are) throws ChartException {
		groupPrimitive(are, false);
		super.fillArea(are);
		ungroupPrimitive(are, false);
	}

	public void fillOval(OvalRenderEvent ore) throws ChartException {
		groupPrimitive(ore, false);
		super.fillOval(ore);
		ungroupPrimitive(ore, false);
	}

	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		groupPrimitive(pre, false);
		super.fillPolygon(pre);
		ungroupPrimitive(pre, false);
	}

	public void fillRectangle(RectangleRenderEvent rre) throws ChartException {
		groupPrimitive(rre, false);
		super.fillRectangle(rre);
		ungroupPrimitive(rre, false);
	}
	
	
}