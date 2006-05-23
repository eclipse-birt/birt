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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.RenderingHints.Key;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.chart.device.svg.i18n.Messages;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class provides the graphic context to draw primitive svg drawing
 * elements such as lines, rectangles, polygons, etc.
 */
public class SVGGraphics2D extends Graphics2D
{

	protected Document dom;
	protected Paint paint;
	protected Color color;
	protected Font font;
	protected Shape clip;
	protected Stroke stroke;
	protected Color background;
	protected Element currentElement;
	protected Stack parentStack = new Stack( );
	protected Element currentParent;
	protected FontRenderContext fontRenderContext;
	protected AffineTransform transforms;
	protected List paints = new ArrayList( );
	protected Element definitions;
	protected Element styles;
	protected Element codeScript;
	protected String styleClass;
	protected String id;
	protected StringBuffer scriptBuffer = new StringBuffer();
	protected StringBuffer styleBuffer = new StringBuffer();
	protected Element deferStrokColor = null;	
	protected String primitiveId = null;
	private RenderingHints renderingHints = new RenderingHints(null);


	protected static final String defaultStyles = "fill:none;stroke:none"; //$NON-NLS-1$

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.device.svg/trace" ); //$NON-NLS-1$

	public SVGGraphics2D( Document dom )
	{
		this.dom = dom;
		fontRenderContext = new FontRenderContext( new AffineTransform( ),
				true,
				false );
		currentElement = dom.getDocumentElement( );
		parentStack.push( currentElement );
		currentParent = currentElement;
		// add default styles
		currentElement = dom.createElement( "g" ); //$NON-NLS-1$
		definitions = dom.createElement( "defs" ); //$NON-NLS-1$
		//give the outer group element an ID
		currentElement.setAttribute("id", "outerG"); //$NON-NLS-1$
		currentElement.appendChild( definitions );
		currentElement.setAttribute( "style", defaultStyles ); //$NON-NLS-1$
		pushParent( currentElement );

		transforms = new AffineTransform( );
		initializeScriptStyles( );
	}

	public void pushParent( Element parent )
	{
		appendChild( parent );
		parentStack.push( parent );
		currentParent = parent;
	}

	public Element popParent( )
	{
		Element popElement = null;
		if ( !parentStack.isEmpty( ) )
			popElement = (Element) parentStack.pop( );
		if ( !parentStack.isEmpty( ) )
			currentParent = (Element) parentStack.peek( );
		return popElement;
	}

	protected void appendChild( Element child )
	{
		currentParent.appendChild( child );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#draw(java.awt.Shape)
	 */
	public void draw( Shape shape )
	{
		currentElement = createGeneralPath( shape );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawImage(java.awt.Image,
	 *      java.awt.geom.AffineTransform, java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, AffineTransform arg1,
			ImageObserver arg2 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawImage.image" ) ) ); //$NON-NLS-1$
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawImage(java.awt.image.BufferedImage,
	 *      java.awt.image.BufferedImageOp, int, int)
	 */
	public void drawImage( BufferedImage arg0, BufferedImageOp arg1, int arg2,
			int arg3 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawImage.buffer" ) ) ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawRenderedImage(java.awt.image.RenderedImage,
	 *      java.awt.geom.AffineTransform)
	 */
	public void drawRenderedImage( RenderedImage arg0, AffineTransform arg1 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawRenderImage.RenderImage" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawRenderableImage(java.awt.image.renderable.RenderableImage,
	 *      java.awt.geom.AffineTransform)
	 */
	public void drawRenderableImage( RenderableImage arg0, AffineTransform arg1 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawRenderableImage.RenderableImage" ) ) ); //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawString(java.lang.String, int, int)
	 */
	public void drawString( String arg0, int arg1, int arg2 )
	{
		currentElement = this.createText(arg0);

		currentElement.setAttribute( "x", Integer.toString(arg1) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Integer.toString(arg2) ); //$NON-NLS-1$
		
		appendChild(currentElement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawString(java.lang.String, float, float)
	 */
	public void drawString( String arg0, float arg1, float arg2 )
	{
		currentElement = this.createText(arg0);

		currentElement.setAttribute( "x", Float.toString(arg1) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Float.toString(arg2) ); //$NON-NLS-1$
		
		appendChild(currentElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawString(java.text.AttributedCharacterIterator,
	 *      int, int)
	 */
	public void drawString( AttributedCharacterIterator arg0, int arg1, int arg2 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawString.AttributeInt" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawString(java.text.AttributedCharacterIterator,
	 *      float, float)
	 */
	public void drawString( AttributedCharacterIterator arg0, float arg1,
			float arg2 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.drawString.AttributeFloat" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#drawGlyphVector(java.awt.font.GlyphVector,
	 *      float, float)
	 */
	public void drawGlyphVector( GlyphVector glyph, float x, float y )
	{
		translate( x, y );
		Element currentElement = dom.createElement( "g" );//$NON-NLS-1$
		Element transElement = createElement( "g" ); //$NON-NLS-1$
		currentElement.appendChild(transElement);
		//need to defer clipping for each glyph
		setFillColor( transElement, true );
		for ( int idx = 0; idx < glyph.getNumGlyphs( ); idx++ )
		{
			Element glyphElem = createShape( glyph.getGlyphOutline( idx ) );
			
			transElement.appendChild( glyphElem );
		}
		//should add clipping to the group element that is not transformed
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );
		translate( -x, -y );
	}

	public void fill( Shape shape, boolean defered )
	{
		Element tempDeferred = null;
		if (!defered){
			tempDeferred = deferStrokColor;
			deferStrokColor = null;
		}
		currentElement = createGeneralPath( shape );
		appendChild( currentElement );
		setFillColor( currentElement );
		if (!defered){
			deferStrokColor = tempDeferred;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#fill(java.awt.Shape)
	 */
	public void fill( Shape shape )
	{
		currentElement = createGeneralPath( shape );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#hit(java.awt.Rectangle, java.awt.Shape, boolean)
	 */
	public boolean hit( Rectangle arg0, Shape arg1, boolean arg2 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.hit.Rectangle" ) ) ); //$NON-NLS-1$
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getDeviceConfiguration()
	 */
	public GraphicsConfiguration getDeviceConfiguration( )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.getDeviceConfig" ) ) ); //$NON-NLS-1$
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#setComposite(java.awt.Composite)
	 */
	public void setComposite( Composite arg0 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.setComposite" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#setRenderingHint(java.awt.RenderingHints.Key,
	 *      java.lang.Object)
	 */
	public void setRenderingHint( Key arg0, Object arg1 )
	{
		renderingHints.put(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getRenderingHint(java.awt.RenderingHints.Key)
	 */
	public Object getRenderingHint( Key arg0 )
	{
		return renderingHints.get(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#setRenderingHints(java.util.Map)
	 */
	public void setRenderingHints( Map arg0 )
	{
		renderingHints = new RenderingHints(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#addRenderingHints(java.util.Map)
	 */
	public void addRenderingHints( Map arg0 )
	{
		renderingHints.add(new RenderingHints(arg0));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getRenderingHints()
	 */
	public RenderingHints getRenderingHints( )
	{
		return renderingHints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#translate(int, int)
	 */
	public void translate( int arg0, int arg1 )
	{
		String transform = currentElement.getAttribute( "transform" ); //$NON-NLS-1$
		if ( transform == null )
			transform = ""; //$NON-NLS-1$
		currentElement.setAttribute( "transform", transform + " translate(" + arg0 + " " + arg1 + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#translate(double, double)
	 */
	public void translate( double arg0, double arg1 )
	{
		transforms.translate( arg0, arg1 );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#rotate(double)
	 */
	public void rotate( double arg0 )
	{
		transforms.rotate( arg0 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#rotate(double, double, double)
	 */
	public void rotate( double arg0, double arg1, double arg2 )
	{
		transforms.rotate( arg0, arg1, arg2 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#scale(double, double)
	 */
	public void scale( double arg0, double arg1 )
	{
		transforms.scale( arg0, arg1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#shear(double, double)
	 */
	public void shear( double arg0, double arg1 )
	{
		transforms.shear( arg0, arg1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#transform(java.awt.geom.AffineTransform)
	 */
	public void transform( AffineTransform arg0 )
	{
		transforms.concatenate((AffineTransform)arg0.clone());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#setTransform(java.awt.geom.AffineTransform)
	 */
	public void setTransform( AffineTransform transform )
	{
		this.transforms = transform;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getTransform()
	 */
	public AffineTransform getTransform( )
	{
		return transforms;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getPaint()
	 */
	public Paint getPaint( )
	{
		return paint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getComposite()
	 */
	public Composite getComposite( )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.getComposite" ) ) ); //$NON-NLS-1$
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#clip(java.awt.Shape)
	 */
	public void clip( Shape shape )
	{
		setClip( shape );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics2D#getFontRenderContext()
	 */
	public FontRenderContext getFontRenderContext( )
	{
		return fontRenderContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#create()
	 */
	public Graphics create( )
	{
		return new SVGGraphics2D( dom );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#setPaintMode()
	 */
	public void setPaintMode( )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.setPaintMode" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#setXORMode(java.awt.Color)
	 */
	public void setXORMode( Color xorColor )
	{
		if ((color == null) || (xorColor == null)) return;
		
		int newColor = ((xorColor.getRed() << 16) + (xorColor.getGreen() << 8) + xorColor.getBlue()) ^  ((color.getRed() << 16) + (color.getGreen() << 8) + color.getBlue());
		int r = newColor >> 16 & 0x0000FF;
		int g = (0x00FF00 & newColor) >> 8;
		int b = 0x0000FF & newColor;
		color = new Color(r, g, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#getFontMetrics(java.awt.Font)
	 */
	public FontMetrics getFontMetrics( Font arg0 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.getFontMetrics.Font" ) ) ); //$NON-NLS-1$
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#getClipBounds()
	 */
	public Rectangle getClipBounds( )
	{
		return clip.getBounds( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#clipRect(int, int, int, int)
	 */
	public void clipRect( int arg0, int arg1, int arg2, int arg3 )
	{
		Rectangle2D.Double rect = new Rectangle2D.Double( arg0,
				arg1,
				arg2,
				arg3 );
		setClip( rect );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 */
	public void setClip( int arg0, int arg1, int arg2, int arg3 )
	{
		Rectangle2D.Double rect = new Rectangle2D.Double( arg0,
				arg1,
				arg2,
				arg3 );
		setClip( rect );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#copyArea(int, int, int, int, int, int)
	 */
	public void copyArea( int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.CopyArea.Int" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawLine(int, int, int, int)
	 */
	public void drawLine( int arg0, int arg1, int arg2, int arg3 )
	{
		drawLine( (double) arg0, (double) arg1, (double) arg2, (double) arg3 );

	}

	public void drawLine( double arg0, double arg1, double arg2, double arg3 )
	{
		currentElement = createLine( arg0, arg1, arg2, arg3 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawRect(int, int, int, int)
	 */
	public void drawRect( int arg0, int arg1, int arg2, int arg3 )
	{
		drawRect( (double) arg0, (double) arg1, (double) arg2, (double) arg3 );
	}

	public void drawRect( double arg0, double arg1, double arg2, double arg3 )
	{
		currentElement = createRect( arg0, arg1, arg2, arg3 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillRect(int, int, int, int)
	 */
	public void fillRect( int arg0, int arg1, int arg2, int arg3 )
	{
		fillRect( (double) arg0, (double) arg1, (double) arg2, (double) arg3 );
	}

	public void fillRect( double arg0, double arg1, double arg2, double arg3 )
	{
		currentElement = createRect( arg0, arg1, arg2, arg3 );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#clearRect(int, int, int, int)
	 */
	public void clearRect( int arg0, int arg1, int arg2, int arg3 )
	{
		logger.log( new Exception( Messages.getString( "SVGGraphics2D.clearRect.Int" ) ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawRoundRect(int, int, int, int, int, int)
	 */
	public void drawRoundRect( int arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5 )
	{
		currentElement = createRoundRect( arg0, arg1, arg2, arg3, arg4, arg5 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillRoundRect(int, int, int, int, int, int)
	 */
	public void fillRoundRect( int arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5 )
	{
		currentElement = createRoundRect( arg0, arg1, arg2, arg3, arg4, arg5 );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawOval(int, int, int, int)
	 */
	public void drawOval( int arg0, int arg1, int arg2, int arg3 )
	{
		drawOval( (double) arg0, (double) arg1, (double) arg2, (double) arg3 );

	}

	public void drawOval( double arg0, double arg1, double arg2, double arg3 )
	{
		currentElement = createOval( arg0, arg1, arg2, arg3 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillOval(int, int, int, int)
	 */
	public void fillOval( int arg0, int arg1, int arg2, int arg3 )
	{
		fillOval( (double) arg0, (double) arg1, (double) arg2, (double) arg3 );

	}

	public void fillOval( double arg0, double arg1, double arg2, double arg3 )
	{
		currentElement = createOval( arg0, arg1, arg2, arg3 );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawArc(int, int, int, int, int, int)
	 */
	public void drawArc( int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5 )
	{
		drawArc( (double) arg0,
				(double) arg1,
				(double) arg2,
				(double) arg3,
				(double) arg4,
				(double) arg5 );
	}

	public void drawArc( double arg0, double arg1, double arg2, double arg3,
			double arg4, double arg5 )
	{
		currentElement = createArc( arg0, arg1, arg2, arg3, arg4, arg5 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}
	
	protected void setStrokeStyle( Element currentElement )
	{
		setStrokeStyle(currentElement, false);
	}
	/**
	 * Adds stroke color and style information to the element passed in.
	 * 
	 * @param currentElement the element to add style information to.
	 * @param isClipped boolean that determines whether to defer the clipping of the element
	 */
	protected void setStrokeStyle( Element currentElement, boolean deferClipped)

	{
		Element element = currentElement;
		if (deferStrokColor != null){
			//Need to get the parent element.  
			element = deferStrokColor;
		}
		
		String style = element.getAttribute( "style" ); //$NON-NLS-1$
		if ( style == null )
			style = ""; //$NON-NLS-1$
		if ( color != null )
		{
			style += "stroke:" + serializeToString( color ) + ";"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if ( ( stroke != null ) && ( stroke instanceof BasicStroke ) )
		{
			BasicStroke bs = (BasicStroke) stroke;
			if ( bs.getLineWidth( ) > 0 )
				style += "stroke-width:" + bs.getLineWidth( ) + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			if ( bs.getDashArray( ) != null )
			{
				String dashArrayStr = ""; //$NON-NLS-1$
				for ( int x = 0; x < bs.getDashArray( ).length; x++ )
				{
					dashArrayStr += " " + bs.getDashArray( )[x]; //$NON-NLS-1$
				}
				if ( !( dashArrayStr.equals( "" ) ) ) //$NON-NLS-1$
					style += "stroke-dasharray:" + dashArrayStr + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			style += "stroke-miterlimit:" + bs.getMiterLimit( ) + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			switch ( bs.getLineJoin( ) )
			{
				case BasicStroke.JOIN_BEVEL :
					style += "stroke-linejoin:bevel;"; //$NON-NLS-1$
					break;
				case BasicStroke.JOIN_ROUND :
					style += "stroke-linejoin:round;"; //$NON-NLS-1$
					break;
			}
			switch ( bs.getEndCap( ) )
			{
				case BasicStroke.CAP_ROUND :
					style += "stroke-linecap:round;"; //$NON-NLS-1$
					break;
				case BasicStroke.CAP_SQUARE :
					style += "stroke-linecap:square;"; //$NON-NLS-1$
					break;
			}

		}
		element.setAttribute( "style", style ); //$NON-NLS-1$
		if (styleClass != null)
			element.setAttribute("class", styleClass); //$NON-NLS-1$
		if (id != null)
			element.setAttribute("id", id); //$NON-NLS-1$
		if (( clip != null ) && (!deferClipped))
			element.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
	}
	
	protected void setFillColor( Element currentElement){
		setFillColor(currentElement, false);	
	}
	
	/**
	 * Adds no fill color and style information to the element passed in.
	 * 
	 * @param currentElement the element to add style information to.
	 */
	protected void setNoFillColor( Element currentElement)
	{
			//should set the fill color to none on the currentElement
			String style = currentElement.getAttribute( "style" ); //$NON-NLS-1$
			if ( style == null )
				style = ""; //$NON-NLS-1$
			currentElement.setAttribute( "style", style + "fill:none;" ); //$NON-NLS-1$ //$NON-NLS-2$			
	}
	
	/**
	 * Adds fill color and style information to the element passed in.
	 * 
	 * @param currentElement the element to add style information to.
	 * @param isClipped boolean that determines whether to defer the clipping of the element
	 */
	protected void setFillColor( Element currentElement, boolean deferClipped)
	{
		Element element = currentElement;
		if (deferStrokColor != null){
			//Need to get the parent element.  
			element = deferStrokColor;
		}
		String style = element.getAttribute( "style" ); //$NON-NLS-1$
		if ( style == null )
			style = ""; //$NON-NLS-1$
		if ( paint == null )
		{
			if ( color == null )
				return;
			String alpha = alphaToString( color );
			if ( alpha != null )
				style += "fill-opacity:" + alpha + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			element.setAttribute( "style", style + "fill:" + serializeToString( color ) + ";stroke:none;" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		else
		{
			if ( paint instanceof SVGGradientPaint )
				element.setAttribute( "style", style + "fill:url(#" + ( (SVGGradientPaint) paint ).getId( ) + ");stroke:none;" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		if (styleClass != null)
			element.setAttribute("class", styleClass); //$NON-NLS-1$
		if (id != null)
			element.setAttribute("id", id); //$NON-NLS-1$
		if (( clip != null ) && (!deferClipped))
			element.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
	}

	/**
	 * @returns the color definition in a string with the format: #RRGGBBAA:
	 *          RRGGBB are the color components in hexa in the range 00..FF AA
	 *          is the tranparency value in hexa in the range 00..FF ex: Solid
	 *          light gray : #777777
	 */
	protected String serializeToString( Color color )
	{

		String r = Integer.toHexString( color.getRed( ) );
		if ( color.getRed( ) <= 0xF )
			r = "0" + r; //$NON-NLS-1$
		String g = Integer.toHexString( color.getGreen( ) );
		if ( color.getGreen( ) <= 0xF )
			g = "0" + g; //$NON-NLS-1$
		String b = Integer.toHexString( color.getBlue( ) );
		if ( color.getBlue( ) <= 0xF )
			b = "0" + b; //$NON-NLS-1$

		String ret = "#" + r + g + b; //$NON-NLS-1$
		return ret;
	}

	protected String alphaToString( Color color )
	{
		double a = 1;
		if ( color.getAlpha( ) < 0xFF )
		{
			a = color.getAlpha( ) / 255.0;
		}
		return Double.toString( a );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillArc(int, int, int, int, int, int)
	 */
	public void fillArc( int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5 )
	{
		fillArc( (double) arg0,
				(double) arg1,
				(double) arg2,
				(double) arg3,
				(double) arg4,
				(double) arg5 );
	}

	public void fillArc( double arg0, double arg1, double arg2, double arg3,
			double arg4, double arg5 )
	{
		currentElement = createArc( arg0, arg1, arg2, arg3, arg4, arg5 );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawPolyline(int[], int[], int)
	 */
	public void drawPolyline( int[] arg0, int[] arg1, int arg2 )
	{
		currentElement = createPolyline( arg0, arg1, arg2 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	public void drawPolygon( int[] arg0, int[] arg1, int arg2 )
	{
		currentElement = createPolygon( arg0, arg1, arg2 );
		appendChild( currentElement );
		setStrokeStyle( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#fillPolygon(int[], int[], int)
	 */
	public void fillPolygon( int[] arg0, int[] arg1, int arg2 )
	{
		currentElement = createPolygon( arg0, arg1, arg2 );
		appendChild( currentElement );
		setFillColor( currentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int,
	 *      java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, ImageObserver arg3 )
	{
		SVGImage image = (SVGImage) arg0;
		
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "xlink:href", image.getUrl( ) ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Integer.toString(arg0.getWidth(arg3)) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Integer.toString(arg0.getHeight(arg3)) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		appendChild( currentElement );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int,
	 *      java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, int arg3,
			int arg4, ImageObserver arg5 )
	{
		SVGImage image = (SVGImage) arg0;
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "xlink:href", image.getUrl( ) ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Double.toString( arg3 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Double.toString( arg4 ) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int,
	 *      java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, Color arg3,
			ImageObserver arg4 )
	{
		SVGImage image = (SVGImage) arg0;
		image.getUrl( );
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Integer.toString(arg0.getWidth(arg4)) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Integer.toString(arg0.getHeight(arg4)) ); //$NON-NLS-1$
		currentElement.setAttribute( "fill", serializeToString( arg3 ) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int,
	 *      java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, int arg3,
			int arg4, Color arg5, ImageObserver arg6 )
	{
		SVGImage image = (SVGImage) arg0;
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "xlink:href", image.getUrl( ) ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Double.toString( arg3 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Double.toString( arg4 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "fill", serializeToString( arg5 ) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int,
	 *      int, int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7, int arg8, ImageObserver arg9 )
	{
		SVGImage image = (SVGImage) arg0;
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "xlink:href", image.getUrl( ) ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Double.toString( arg3 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Double.toString( arg4 ) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int,
	 *      int, int, int, java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage( Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7, int arg8, Color arg9,
			ImageObserver arg10 )
	{
		SVGImage image = (SVGImage) arg0;
		Element currentElement = createElement( "image" ); //$NON-NLS-1$
		currentElement.setAttribute( "xlink:href", image.getUrl( ) ); //$NON-NLS-1$
		currentElement.setAttribute( "x", Double.toString( arg1 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "y", Double.toString( arg2 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "width", Double.toString( arg3 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "height", Double.toString( arg4 ) ); //$NON-NLS-1$
		currentElement.setAttribute( "fill", serializeToString( arg9 ) ); //$NON-NLS-1$
		if ( clip != null )
			currentElement.setAttribute( "clip-path", "url(#clip" + clip.hashCode( ) + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		appendChild( currentElement );

		return true;
	}

	/**
	 * Unload any resources associated with the graphic context
	 */
	public void flush(){
		codeScript.appendChild( dom.createCDATASection( EventHandlers.content.append(scriptBuffer).toString( ) ) );
		styles.appendChild( dom.createCDATASection( EventHandlers.styles.append(styleBuffer).toString( ) ) );
		///clear buffer
		scriptBuffer = new StringBuffer();
		styleBuffer = new StringBuffer();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Graphics#dispose()
	 */
	public void dispose( )
	{
		paints.clear( );
	}

	/**
	 * @return Returns the background.
	 */
	public Color getBackground( )
	{
		return background;
	}

	/**
	 * @param backgroundColor
	 *            The backgroundColor to set.
	 */
	public void setBackground( Color background )
	{
		this.background = background;
	}

	/**
	 * @return Returns the clip.
	 */
	public Shape getClip( )
	{
		return clip;
	}

	/**
	 * @param clip
	 *            The clip to set.
	 */
	public void setClip( Shape clip )
	{
		this.clip = clip;
		if ( clip != null )
		{
			Element clipPath = dom.createElement( "clipPath" ); //$NON-NLS-1$
			clipPath.setAttribute( "id", "clip" + clip.hashCode( ) ); //$NON-NLS-1$ //$NON-NLS-2$
			clipPath.appendChild( createGeneralPath( clip ) );
			definitions.appendChild( clipPath );
		}

	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor( )
	{
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor( Color color )
	{
		this.color = color;
		this.paint = null;
	}

	/**
	 * @return Returns the font.
	 */
	public Font getFont( )
	{
		return font;
	}

	/**
	 * @param font
	 *            The font to set.
	 */
	public void setFont( Font font )
	{
		this.font = font;
	}

	/**
	 * @return Returns the stroke.
	 */
	public Stroke getStroke( )
	{
		return stroke;
	}

	/**
	 * @param stroke
	 *            The stroke to set.
	 */
	public void setStroke( Stroke stroke )
	{
		this.stroke = stroke;
	}

	/**
	 * @param paint
	 *            The paint to set.
	 */
	public void setPaint( Paint paint )
	{
		if ( paint instanceof GradientPaint )
		{
			SVGGradientPaint gp = new SVGGradientPaint( (GradientPaint) paint );
			int index = paints.indexOf( gp );
			if ( index == -1 )
			{
				paints.add( gp );
				definitions.appendChild( createGradientPaint( gp ) );
			}
			else
			{
				gp = (SVGGradientPaint) paints.get( index );
			}
			this.paint = gp;
		}
		else
			this.paint = paint;
	}

	/***************************************************************************
	 * Factory Methods
	 **************************************************************************/
	protected Element createGradientPaint( SVGGradientPaint paint )
	{
		Element elem = dom.createElement( "linearGradient" ); //$NON-NLS-1$
		elem.setAttribute( "id", paint.getId( ) ); //$NON-NLS-1$
		elem.setAttribute( "x1", Double.toString( paint.getPoint1( ).getX( ) ) ); //$NON-NLS-1$
		elem.setAttribute( "y1", Double.toString( paint.getPoint1( ).getY( ) ) ); //$NON-NLS-1$
		elem.setAttribute( "x2", Double.toString( paint.getPoint2( ).getX( ) ) ); //$NON-NLS-1$
		elem.setAttribute( "y2", Double.toString( paint.getPoint2( ).getY( ) ) ); //$NON-NLS-1$
		elem.setAttribute( "gradientUnits", "userSpaceOnUse" ); //$NON-NLS-1$ //$NON-NLS-2$
		if ( paint.isCyclic( ) )
			elem.setAttribute( "spreadMethod", "repeat" ); //$NON-NLS-1$ //$NON-NLS-2$
		Element startColor = dom.createElement( "stop" ); //$NON-NLS-1$
		startColor.setAttribute( "offset", "0%" ); //$NON-NLS-1$ //$NON-NLS-2$
		startColor.setAttribute( "stop-color", serializeToString( paint.getColor1( ) ) ); //$NON-NLS-1$
		elem.appendChild( startColor );
		Element endColor = dom.createElement( "stop" ); //$NON-NLS-1$
		endColor.setAttribute( "offset", "100%" ); //$NON-NLS-1$ //$NON-NLS-2$
		endColor.setAttribute( "stop-color", serializeToString( paint.getColor2( ) ) ); //$NON-NLS-1$
		elem.appendChild( endColor );
		return elem;
	}

	protected Element createLine( double arg0, double arg1, double arg2,
			double arg3 )
	{
		Element elem = createElement( "line" ); //$NON-NLS-1$
		elem.setAttribute( "x1", Double.toString( arg0 ) ); //$NON-NLS-1$
		elem.setAttribute( "y1", Double.toString( arg1 ) ); //$NON-NLS-1$
		elem.setAttribute( "x2", Double.toString( arg2 ) ); //$NON-NLS-1$
		elem.setAttribute( "y2", Double.toString( arg3 ) ); //$NON-NLS-1$
		return elem;
	}

	protected Element createShape( Shape shape )
	{
		PathIterator pathIter = shape.getPathIterator( null );
		String pathStr = ""; //$NON-NLS-1$
		while ( !pathIter.isDone( ) )
		{
			double[] points = new double[6];
			int TYPE = pathIter.currentSegment( points );
			switch ( TYPE )
			{
				case PathIterator.SEG_CLOSE :
					pathStr += " Z"; //$NON-NLS-1$
					break;
				case PathIterator.SEG_LINETO :
					pathStr += " L" + points[0] + " " + points[1]; //$NON-NLS-1$ //$NON-NLS-2$
					break;
				case PathIterator.SEG_QUADTO :
					pathStr += " Q" + points[0] + " " + points[1] + " " + points[2] + " " + points[3]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					break;
				case PathIterator.SEG_CUBICTO :
					pathStr += " C" + points[0] + " " + points[1] + " " + points[2] + " " + points[3] + " " + points[4] + " " + points[5]; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
					break;
				case PathIterator.SEG_MOVETO :
					pathStr += " M" + points[0] + " " + points[1]; //$NON-NLS-1$ //$NON-NLS-2$
					break;
			}
			pathIter.next( );
		}
		Element elem = dom.createElement( "path" ); //$NON-NLS-1$
		elem.setAttribute( "d", pathStr ); //$NON-NLS-1$
		return elem;
	}

	protected Element createGeneralPath( Shape path )
	{
		Element elem = createShape( path );
		if ( transforms.getType( ) != AffineTransform.TYPE_IDENTITY )
		{
			double[] matrix = new double[6];
			transforms.getMatrix( matrix );
			elem.setAttribute( "transform", "matrix(" + matrix[0] + "," + matrix[1] + "," + matrix[2] + "," + matrix[3] + "," + matrix[4] + "," + matrix[5] + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		}
		return elem;
	}
	protected Element createText( String text )
	{
		Element elem = dom.createElement( "text" ); //$NON-NLS-1$
		elem.appendChild( dom.createTextNode( text ) );
		switch (getFont().getStyle()){
			case Font.BOLD:
				elem.setAttribute( "font-weight", "bold"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case Font.ITALIC:
				elem.setAttribute( "font-style", "italic"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case (Font.BOLD+Font.ITALIC):
				elem.setAttribute( "font-style", "italic"); //$NON-NLS-1$ //$NON-NLS-2$
				elem.setAttribute( "font-weight", "bold"); //$NON-NLS-1$ //$NON-NLS-2$
				break;				
		}
		String textDecorator = null;
		Map attributes = getFont().getAttributes();
		if (attributes.get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON){			
			textDecorator = "underline"; //$NON-NLS-1$ 
		}
		if (attributes.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON){			
			if (textDecorator == null)
				textDecorator = "line-through"; //$NON-NLS-1$
			else
				textDecorator += ",line-through"; //$NON-NLS-1$
		}
		if (textDecorator != null)
			elem.setAttribute( "text-decoration", textDecorator); //$NON-NLS-1$
		
		elem.setAttribute( "stroke", "none"); //$NON-NLS-1$ //$NON-NLS-2$
		elem.setAttribute( "font-family", getFont().getFamily()); //$NON-NLS-1$
		elem.setAttribute( "font-size", Integer.toString(getFont().getSize())); //$NON-NLS-1$
		String style = getRenderingStyle(RenderingHints.KEY_TEXT_ANTIALIASING);
		if ( color != null ){
			String alpha = alphaToString( color );
			if ( alpha != null )
				style += "fill-opacity:" + alpha + ";"; //$NON-NLS-1$ //$NON-NLS-2$
			style += "fill:" + serializeToString( color ) + ";"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		elem.setAttribute("style", style);
		if ( transforms.getType( ) != AffineTransform.TYPE_IDENTITY )
		{
			double[] matrix = new double[6];
			transforms.getMatrix( matrix );
			elem.setAttribute( "transform", "matrix(" + matrix[0] + "," + matrix[1] + "," + matrix[2] + "," + matrix[3] + "," + matrix[4] + "," + matrix[5] + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		}
		
		return elem;
	}
	
	protected String getRenderingStyle(Object key){
		Object value = renderingHints.get(key);
		if (key.equals(RenderingHints.KEY_TEXT_ANTIALIASING)){
			if (value.equals(RenderingHints.VALUE_TEXT_ANTIALIAS_OFF))
				return "text-rendering:optimizeSpeed;";//$NON-NLS-1$ 
			else
				//SVG always turns on antialias
				return "";  //$NON-NLS-1$ 
		}
		return "";//$NON-NLS-1$ 	
	}

	protected Element createRect( double arg0, double arg1, double arg2,
			double arg3 )
	{
		Element elem = createElement( "rect" ); //$NON-NLS-1$
		elem.setAttribute( "x", Double.toString( arg0 ) ); //$NON-NLS-1$
		elem.setAttribute( "y", Double.toString( arg1 ) ); //$NON-NLS-1$
		elem.setAttribute( "width", Double.toString( arg2 ) ); //$NON-NLS-1$
		elem.setAttribute( "height", Double.toString( arg3 ) ); //$NON-NLS-1$
		return elem;
	}

	protected Element createRoundRect( double arg0, double arg1, double arg2,
			double arg3, double arg4, double arg5 )
	{
		Element elem = createElement( "rect" ); //$NON-NLS-1$
		elem.setAttribute( "x", Double.toString( arg0 ) ); //$NON-NLS-1$
		elem.setAttribute( "y", Double.toString( arg1 ) ); //$NON-NLS-1$
		elem.setAttribute( "width", Double.toString( arg2 ) ); //$NON-NLS-1$
		elem.setAttribute( "height", Double.toString( arg3 ) ); //$NON-NLS-1$
		elem.setAttribute( "rx", Double.toString( arg2 ) ); //$NON-NLS-1$
		elem.setAttribute( "ry", Double.toString( arg3 ) ); //$NON-NLS-1$
		return elem;
	}

	protected Element createOval( double arg0, double arg1, double arg2,
			double arg3 )
	{
		Element elem = createElement( "ellipse" ); //$NON-NLS-1$
		elem.setAttribute( "cx", Double.toString( arg0 ) ); //$NON-NLS-1$
		elem.setAttribute( "cy", Double.toString( arg1 ) ); //$NON-NLS-1$
		elem.setAttribute( "rx", Double.toString( arg2 ) ); //$NON-NLS-1$
		elem.setAttribute( "ry", Double.toString( arg3 ) ); //$NON-NLS-1$
		return elem;
	}

	protected Element createArc( double x, double y, double width,
			double height, double startAngle, double arcAngle )
	{
		Element elem = createElement( "path" ); //$NON-NLS-1$
		double startX = x * Math.cos( startAngle );
		double startY = y * Math.sin( startAngle );
		double endX = x * Math.cos( startAngle + arcAngle );
		double endY = y * Math.sin( startAngle + arcAngle );
		int sweepFlag = ( arcAngle < 0 ) ? 0 : 1;
		elem.setAttribute( "d", "M" + startX + "," + startY + " a" + width / 2 + "," + height / 2 + " " + Math.abs( arcAngle ) + " 0 " + sweepFlag + " " + endX + " " + endY ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
		return elem;
	}

	public Element createPolyline( int[] arg0, int[] arg1, int arg2 )
	{
		Element elem = createElement( "polyline" ); //$NON-NLS-1$
		StringBuffer pointsStr = new StringBuffer( );
		;
		for ( int x = 0; x < arg2; x++ )
		{
			pointsStr.append( arg0[x] )
					.append( "," ).append( arg1[x] ).append( " " ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		elem.setAttribute( "points", pointsStr.toString( ) ); //$NON-NLS-1$
		return elem;
	}

	public Element createPolygon( int[] arg0, int[] arg1, int arg2 )
	{
		Element elem = createElement( "polygon" ); //$NON-NLS-1$
		StringBuffer pointsStr = new StringBuffer( );
		;
		for ( int x = 0; x < arg2; x++ )
		{
			pointsStr.append( arg0[x] )
					.append( "," ).append( arg1[x] ).append( " " ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		elem.setAttribute( "points", pointsStr.toString( ) ); //$NON-NLS-1$
		return elem;
	}

	protected void initializeScriptStyles( )
	{
		codeScript = dom.createElement( "script" ); //$NON-NLS-1$
		appendChild( codeScript );
		styles = dom.createElement( "style" ); //$NON-NLS-1$
		styles.setAttribute( "type", "text/css" ); //$NON-NLS-1$ //$NON-NLS-2$
		appendChild( styles );

	}

	protected Element createElement( String id )
	{
		Element elem = dom.createElement( id );
		if (this.primitiveId != null)
			elem.setAttribute("id", primitiveId);
		if ( transforms.getType( ) != AffineTransform.TYPE_IDENTITY )
		{
			double[] matrix = new double[6];
			transforms.getMatrix( matrix );
			elem.setAttribute( "transform", "matrix(" + matrix[0] + "," + matrix[1] + "," + matrix[2] + "," + matrix[3] + "," + matrix[4] + "," + matrix[5] + ")" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		}
		return elem;

	}

	public void drawTooltip( String tooltip )
	{
		Element title = dom.createElement( "title" ); //$NON-NLS-1$
		title.appendChild( dom.createTextNode( tooltip ) );
		appendChild( title );
		currentParent.setAttribute( "onmouseout", "TM.remove()" ); //$NON-NLS-1$ //$NON-NLS-2$
		currentParent.setAttribute( "onmouseover", "TM.show(evt)" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @return Returns the currentParent.
	 */
	public Element getCurrentParent( )
	{
		return currentParent;
	}


	/**
	 * @return Returns the currentElement.
	 */
	public Element getCurrentElement( )
	{
		return currentElement;
	}
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
	
	public void addCSSStyle(String className, String styleName, String styleValue){
		styleBuffer.append(className).append("{").append(styleName).append(":").append(styleValue).append(";}");				 //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	/**
	 * Inlines the script code in the generated svg output
	 * 
	 * @param script the code that will be inlined in the generated svg output
	 */
	public void addScript(String script){
		scriptBuffer.append(script);	
	}
	
	/**
	 * Adds a script reference in the generated svg output.
	 * 
	 * @param ref the script reference that will be added to the generated svg output.
	 */
	public void addScriptRef(String ref){
		Element rootElem = dom.getDocumentElement( );
		Element scriptElem = dom.createElement( "script" ); //$NON-NLS-1$
		rootElem.appendChild( scriptElem );
		scriptElem.setAttribute( "language", "JavaScript" ); //$NON-NLS-1$ //$NON-NLS-2$
		scriptElem.setAttribute( "xlink:href", ref ); //$NON-NLS-1$		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Defer setting the stroke and color on an SVG element.
	 * 
	 * @return the state of the flag that ignores setting the stroke style and color on a svg element.
	 */
	public Element getDeferStrokColor() {
		return deferStrokColor;
	}

	/**
	 * Defer setting the stroke and color on an SVG element.
	 * @param deferStrokColor set to true if the stroke style and color should be ignored when drawing the svg element.
	 */
	public void setDeferStrokColor(Element deferStrokColor) {
		this.deferStrokColor = deferStrokColor;
	}
	

	/**
	 * Returns the current id that is used to identify a svg drawing primitive
	 * @return
	 */
	public String getPrimitiveId() {
		return primitiveId;
	}
	
	/**
	 * Sets the current primitive id
	 * @param primitiveId
	 */
	public void setPrimitiveId(String primitiveId) {
		this.primitiveId = primitiveId;
	}
		
	
}
