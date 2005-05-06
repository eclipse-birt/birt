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

import org.eclipse.birt.chart.log.DefaultLoggerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *This class provides the graphic context to draw primitive svg drawing elements
 *such as lines, rectangles, polygons, etc.
 */
public class SVGGraphics2D extends Graphics2D {
	protected Document dom;
	protected Paint paint;
	protected Color color;
	protected Font font;
	protected Shape clip;
	protected Stroke stroke;
	protected Color background;
	protected Element currentElement;
	protected Stack parentStack = new Stack();
	protected Element currentParent;
	protected FontRenderContext fontRenderContext; 
	protected AffineTransform transforms;
	protected List paints = new ArrayList();
	protected Element definitions;
	
	protected static final String defaultStyles= "fill:none;stroke:none";
	
	public SVGGraphics2D(Document dom){
		this.dom = dom;
		fontRenderContext  = new FontRenderContext(new AffineTransform(), true, false);
		currentElement = dom.getDocumentElement();
		parentStack.push(currentElement);
		currentParent = currentElement;
		//add default styles
		currentElement = dom.createElement("g");
		definitions = dom.createElement("defs");
		currentElement.appendChild(definitions);
		currentElement.setAttribute("style", defaultStyles);
		pushParent(currentElement);
		
		transforms = new AffineTransform();
		initializeScriptStyles();
	}
	
	public void pushParent(Element parent){
		appendChild(parent);
		parentStack.push(parent);
		currentParent = parent;
	}

	public Element popParent(){
		Element popElement = (Element)parentStack.pop();
		if (!parentStack.isEmpty())
			currentParent = (Element)parentStack.peek();
		return popElement;
	}
	
	protected void appendChild(Element child){
		currentParent.appendChild(child);
	}
	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#draw(java.awt.Shape)
	 */
	public void draw(Shape shape) {
		currentElement = createGeneralPath(shape);
		appendChild(currentElement);
		setStrokeStyle(currentElement);	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawImage(java.awt.Image, java.awt.geom.AffineTransform, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, AffineTransform arg1,
			ImageObserver arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer -drawImage(Image arg0, AffineTransform arg1, ImageObserver arg2) - unsupported"));
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawImage(java.awt.image.BufferedImage, java.awt.image.BufferedImageOp, int, int)
	 */
	public void drawImage(BufferedImage arg0, BufferedImageOp arg1, int arg2,
			int arg3) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer -drawImage(BufferedImage arg0, BufferedImageOp arg1, int arg2,	int arg3) - unsupported"));

	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawRenderedImage(java.awt.image.RenderedImage, java.awt.geom.AffineTransform)
	 */
	public void drawRenderedImage(RenderedImage arg0, AffineTransform arg1) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer -drawRenderedImage(RenderedImage arg0, AffineTransform arg1) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawRenderableImage(java.awt.image.renderable.RenderableImage, java.awt.geom.AffineTransform)
	 */
	public void drawRenderableImage(RenderableImage arg0, AffineTransform arg1) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawRenderableImage(RenderableImage arg0, AffineTransform arg1) - unsupported"));

	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawString(java.lang.String, int, int)
	 */
	public void drawString(String arg0, int arg1, int arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawString(String arg0, int arg1, int arg2) - unsupported"));

	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawString(java.lang.String, float, float)
	 */
	public void drawString(String arg0, float arg1, float arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawString(String arg0, float arg1, float arg2) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawString(java.text.AttributedCharacterIterator, int, int)
	 */
	public void drawString(AttributedCharacterIterator arg0, int arg1, int arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawString(AttributedCharacterIterator arg0, int arg1, int arg2) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawString(java.text.AttributedCharacterIterator, float, float)
	 */
	public void drawString(AttributedCharacterIterator arg0, float arg1,
			float arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawString(AttributedCharacterIterator arg0, float arg1, float arg2) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#drawGlyphVector(java.awt.font.GlyphVector, float, float)
	 */
	public void drawGlyphVector(GlyphVector glyph, float x, float y) {
		translate(x, y);
		currentElement = createElement("g");
		
		setFillColor(currentElement);
		for (int idx = 0; idx < glyph.getNumGlyphs(); idx++){
			Element glyphElem = createShape(glyph.getGlyphOutline(idx));
			currentElement.appendChild(glyphElem);			
		}
		appendChild(currentElement);			
		translate(-x, -y);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#fill(java.awt.Shape)
	 */
	public void fill(Shape shape) {
		currentElement = createGeneralPath(shape);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#hit(java.awt.Rectangle, java.awt.Shape, boolean)
	 */
	public boolean hit(Rectangle arg0, Shape arg1, boolean arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - hit(Rectangle arg0, Shape arg1, boolean arg2) - unsupported"));
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getDeviceConfiguration()
	 */
	public GraphicsConfiguration getDeviceConfiguration() {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - GraphicsConfiguration getDeviceConfiguration() - unsupported"));
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#setComposite(java.awt.Composite)
	 */
	public void setComposite(Composite arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - setComposite(Composite arg0) - unsupported"));
	}


	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#setRenderingHint(java.awt.RenderingHints.Key, java.lang.Object)
	 */
	public void setRenderingHint(Key arg0, Object arg1) {
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getRenderingHint(java.awt.RenderingHints.Key)
	 */
	public Object getRenderingHint(Key arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - getRenderingHint(Key arg0) - unsupported"));
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#setRenderingHints(java.util.Map)
	 */
	public void setRenderingHints(Map arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - setRenderingHints(Map arg0) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#addRenderingHints(java.util.Map)
	 */
	public void addRenderingHints(Map arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - ddRenderingHints(Map arg0) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getRenderingHints()
	 */
	public RenderingHints getRenderingHints() {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - getRenderingHints() - unsupported"));
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#translate(int, int)
	 */
	public void translate(int arg0, int arg1) {
		String transform = currentElement.getAttribute("transform");
		if (transform == null) transform = "";
		currentElement.setAttribute("transform", transform + " translate("+arg0+" "+arg1+")");
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#translate(double, double)
	 */
	public void translate(double arg0, double arg1) {
		transforms.translate(arg0, arg1);

	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#rotate(double)
	 */
	public void rotate(double arg0) {
		transforms.rotate(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#rotate(double, double, double)
	 */
	public void rotate(double arg0, double arg1, double arg2) {
		transforms.rotate(arg0, arg1, arg2);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#scale(double, double)
	 */
	public void scale(double arg0, double arg1) {
		transforms.scale(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#shear(double, double)
	 */
	public void shear(double arg0, double arg1) {
		transforms.shear(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#transform(java.awt.geom.AffineTransform)
	 */
	public void transform(AffineTransform arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - transform(AffineTransform arg0) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#setTransform(java.awt.geom.AffineTransform)
	 */
	public void setTransform(AffineTransform arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - setTransform(AffineTransform arg0) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getTransform()
	 */
	public AffineTransform getTransform() {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - AffineTransform getTransform() - unsupported"));
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getPaint()
	 */
	public Paint getPaint() {
		return paint;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getComposite()
	 */
	public Composite getComposite() {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - Composite getComposite() - unsupported"));
		return null;
	}


	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#clip(java.awt.Shape)
	 */
	public void clip(Shape shape) {
		setClip(shape);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics2D#getFontRenderContext()
	 */
	public FontRenderContext getFontRenderContext() {
		return fontRenderContext;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#create()
	 */
	public Graphics create() {
		return new SVGGraphics2D(dom);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#setPaintMode()
	 */
	public void setPaintMode() {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - setPaintMode() - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#setXORMode(java.awt.Color)
	 */
	public void setXORMode(Color arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - setXORMode(Color arg0) - unsupported"));
	}


	/* (non-Javadoc)
	 * @see java.awt.Graphics#getFontMetrics(java.awt.Font)
	 */
	public FontMetrics getFontMetrics(Font arg0) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - FontMetrics getFontMetrics(Font arg0) - unsupported"));
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#getClipBounds()
	 */
	public Rectangle getClipBounds() {
		return clip.getBounds();
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#clipRect(int, int, int, int)
	 */
	public void clipRect(int arg0, int arg1, int arg2, int arg3) {
		Rectangle2D.Double rect = new Rectangle2D.Double(arg0, arg1, arg2, arg3);
		setClip(rect);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#setClip(int, int, int, int)
	 */
	public void setClip(int arg0, int arg1, int arg2, int arg3) {
		Rectangle2D.Double rect = new Rectangle2D.Double(arg0, arg1, arg2, arg3);
		setClip(rect);
	}


	/* (non-Javadoc)
	 * @see java.awt.Graphics#copyArea(int, int, int, int, int, int)
	 */
	public void copyArea(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - CcopyArea(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawLine(int, int, int, int)
	 */
	public void drawLine(int arg0, int arg1, int arg2, int arg3) {
		drawLine((double)arg0, (double)arg1, (double)arg2, (double)arg3);

	}
	public void drawLine(double arg0, double arg1, double arg2, double arg3) {
		currentElement = createLine(arg0, arg1, arg2, arg3);
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}
	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawRect(int, int, int, int)
	 */
	public void drawRect(int arg0, int arg1, int arg2, int arg3) {
		drawRect((double)arg0, (double)arg1, (double)arg2, (double)arg3);
	}
	public void drawRect(double arg0, double arg1, double arg2, double arg3) {
		currentElement = createRect(arg0, arg1, arg2, arg3);
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}
	/* (non-Javadoc)
	 * @see java.awt.Graphics#fillRect(int, int, int, int)
	 */
	public void fillRect(int arg0, int arg1, int arg2, int arg3) {
		fillRect((double)arg0, (double)arg1, (double)arg2, (double)arg3);
	}
	public void fillRect(double arg0, double arg1, double arg2, double arg3) {
		currentElement = createRect(arg0, arg1, arg2, arg3);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#clearRect(int, int, int, int)
	 */
	public void clearRect(int arg0, int arg1, int arg2, int arg3) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - clearRect(int arg0, int arg1, int arg2, int arg3)  - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawRoundRect(int, int, int, int, int, int)
	 */
	public void drawRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5) {
		currentElement = createRoundRect(arg0, arg1, arg2, arg3, arg4, arg5);
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#fillRoundRect(int, int, int, int, int, int)
	 */
	public void fillRoundRect(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5) {
		currentElement = createRoundRect(arg0, arg1, arg2, arg3, arg4, arg5);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawOval(int, int, int, int)
	 */
	public void drawOval(int arg0, int arg1, int arg2, int arg3) {
		drawOval((double)arg0,(double)arg1,(double)arg2,(double)arg3);

	}
	public void drawOval(double arg0, double arg1, double arg2, double arg3) {
		currentElement = createOval(arg0, arg1, arg2, arg3);
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#fillOval(int, int, int, int)
	 */
	public void fillOval(int arg0, int arg1, int arg2, int arg3) {
		fillOval((double)arg0,(double)arg1,(double)arg2,(double)arg3);

	}
	public void fillOval(double arg0, double arg1, double arg2, double arg3) {
		currentElement = createOval(arg0, arg1, arg2, arg3);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawArc(int, int, int, int, int, int)
	 */
	public void drawArc(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5) {
		drawArc((double)arg0, (double)arg1, (double)arg2, (double)arg3, (double)arg4, (double)arg5);
	}
	public void drawArc(double arg0, double arg1, double arg2, double arg3, double arg4,
			double arg5) {
		currentElement = createArc(arg0, arg1, arg2, arg3, arg4, arg5);		
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}
	
	protected void setStrokeStyle(Element currentElement){
		String style = currentElement.getAttribute("style");
		if (style == null) style="";
		if (color != null){
			style+="stroke:"+serializeToString(color)+";";
		}
		if ((stroke != null) && (stroke instanceof BasicStroke)){
			BasicStroke bs = (BasicStroke)stroke;
			if (bs.getLineWidth() > 0)
				style+="stroke-width:"+bs.getLineWidth()+";";
			if (bs.getDashArray() != null){
				String dashArrayStr = "";
				for (int  x = 0; x< bs.getDashArray().length; x++){
					dashArrayStr += " " +bs.getDashArray()[x];
				}
				if (!(dashArrayStr.equals("")))
					style+="stroke-dasharray:"+dashArrayStr+";";
			}
			style+="stroke-miterlimit:"+bs.getMiterLimit()+";";
			switch (bs.getLineJoin()){
				case BasicStroke.JOIN_BEVEL:
					style+="stroke-linejoin:bevel;";
					break;
				case BasicStroke.JOIN_ROUND:
					style+="stroke-linejoin:round;";
					break;
			}
			switch (bs.getEndCap()){
			case BasicStroke.CAP_ROUND:
				style+="stroke-linecap:round;";
				break;
			case BasicStroke.CAP_SQUARE:
				style+="stroke-linecap:square;";
				break;
		}
			
		}
		currentElement.setAttribute("style", style);
	}
	
	protected void setFillColor(Element currentElement){
		String style = currentElement.getAttribute("style");
		if (style == null) style="";
		if (paint == null){
			if (color == null) return;
			String alpha = alphaToString(color);
			if (alpha != null)
				style+="fill-opacity:"+alpha+";";
			currentElement.setAttribute("style", style+"fill:"+serializeToString(color)+";");			
		}
		else{
			if (paint instanceof SVGGradientPaint)
				currentElement.setAttribute("style", style+"fill:url(#"+((SVGGradientPaint)paint).getId()+");");
		}
	}
	   /**
	    * @returns the color definition in a string with the format:
	    * #RRGGBBAA: RRGGBB are the color components in hexa in the range 00..FF
	    * AA is the tranparency value in hexa in the range 00..FF
	    * ex:
	    *    Solid light gray :  #777777
	    */
	   protected String serializeToString(Color color) {
	   
	      String r = Integer.toHexString(color.getRed());
	      if (color.getRed()<=0xF) r = "0" + r;
	      String g = Integer.toHexString(color.getGreen());
	      if (color.getGreen()<=0xF) g = "0" + g;
	      String b = Integer.toHexString(color.getBlue());
	      if (color.getBlue()<=0xF) b = "0" + b;
	      
	      String ret = "#" + r + g + b;
	      return ret;
	   }
	   
	   protected String alphaToString(Color color) {		 
	   	double a = 1;	
	      if (color.getAlpha() < 0xFF)
	      {
	           a = color.getAlpha()/255.0;
	      }
	      return Double.toString(a);
	   }
	

	/* (non-Javadoc)
	 * @see java.awt.Graphics#fillArc(int, int, int, int, int, int)
	 */
	public void fillArc(int arg0, int arg1, int arg2, int arg3, int arg4,
			int arg5) {
		fillArc((double)arg0, (double)arg1, (double)arg2, (double)arg3, (double)arg4, (double)arg5);
	}
	public void fillArc(double arg0, double arg1, double arg2, double arg3, double arg4,
			double arg5) {
		currentElement = createArc(arg0, arg1, arg2, arg3, arg4, arg5);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawPolyline(int[], int[], int)
	 */
	public void drawPolyline(int[] arg0, int[] arg1, int arg2) {
        DefaultLoggerImpl.instance().log(new Exception("SVG Renderer - drawPolyline(int[] arg0, int[] arg1, int arg2) - unsupported"));
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawPolygon(int[], int[], int)
	 */
	public void drawPolygon(int[] arg0, int[] arg1, int arg2) {
		currentElement = createPolygon(arg0, arg1, arg2);
		appendChild(currentElement);
		setStrokeStyle(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#fillPolygon(int[], int[], int)
	 */
	public void fillPolygon(int[] arg0, int[] arg1, int arg2) {
		currentElement = createPolygon(arg0, arg1, arg2);
		appendChild(currentElement);
		setFillColor(currentElement);
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, ImageObserver arg3) {
		SVGImage image = (SVGImage)arg0;
		Element currentElement = createElement("image");
		currentElement.setAttribute("xlink:href", image.getUrl().toExternalForm());
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", "100%");
		currentElement.setAttribute("height", "100%");
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
			int arg4, ImageObserver arg5) {
		SVGImage image = (SVGImage)arg0;
		Element currentElement = createElement("image");
		currentElement.setAttribute("xlink:href", image.getUrl().toExternalForm());
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", Double.toString(arg3));
		currentElement.setAttribute("height", Double.toString(arg4));
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, Color arg3,
			ImageObserver arg4) {
		SVGImage image = (SVGImage)arg0;
		image.getUrl().toExternalForm();
		Element currentElement = createElement("image");
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", "100%");
		currentElement.setAttribute("height", "100%");
		currentElement.setAttribute("fill", serializeToString(arg3));
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
			int arg4, Color arg5, ImageObserver arg6) {
		SVGImage image = (SVGImage)arg0;
		Element currentElement = createElement("image");
		currentElement.setAttribute("xlink:href", image.getUrl().toExternalForm());
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", Double.toString(arg3));
		currentElement.setAttribute("height", Double.toString(arg4));
		currentElement.setAttribute("fill", serializeToString(arg5));
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int, int, int, int, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7, int arg8, ImageObserver arg9) {
		SVGImage image = (SVGImage)arg0;
		Element currentElement = createElement("image");
		currentElement.setAttribute("xlink:href", image.getUrl().toExternalForm());
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", Double.toString(arg3));
		currentElement.setAttribute("height", Double.toString(arg4));
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#drawImage(java.awt.Image, int, int, int, int, int, int, int, int, java.awt.Color, java.awt.image.ImageObserver)
	 */
	public boolean drawImage(Image arg0, int arg1, int arg2, int arg3,
			int arg4, int arg5, int arg6, int arg7, int arg8, Color arg9,
			ImageObserver arg10) {
		SVGImage image = (SVGImage)arg0;
		Element currentElement = createElement("image");
		currentElement.setAttribute("xlink:href", image.getUrl().toExternalForm());
		currentElement.setAttribute("x", Double.toString(arg1));
		currentElement.setAttribute("y", Double.toString(arg2));
		currentElement.setAttribute("width", Double.toString(arg3));
		currentElement.setAttribute("height", Double.toString(arg4));
		currentElement.setAttribute("fill", serializeToString(arg9));
		if (clip != null)
			currentElement.setAttribute("clip-rule","url(clip"+clip.hashCode()+")");
		appendChild(currentElement);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see java.awt.Graphics#dispose()
	 */
	public void dispose() {
		paints.clear();
	}

	/**
	 * @return Returns the background.
	 */
	public Color getBackground() {
		return background;
	}
	/**
	 * @param backgroundColor The backgroundColor to set.
	 */
	public void setBackground(Color background) {
		this.background = background;
	}
	/**
	 * @return Returns the clip.
	 */
	public Shape getClip() {
		return clip;
	}
	/**
	 * @param clip The clip to set.
	 */
	public void setClip(Shape clip) {
		this.clip = clip;
		if (clip != null){
		Element clipPath = dom.createElement("clipPath");
		clipPath.setAttribute("id", "clip"+clip.hashCode());		
		clipPath.appendChild(createGeneralPath(clip));
		appendChild(clipPath);
		}
		
	}
	/**
	 * @return Returns the color.
	 */
	public Color getColor() {
		return color;
	}
	/**
	 * @param color The color to set.
	 */
	public void setColor(Color color) {
		this.color = color;
		this.paint = null;
	}
	/**
	 * @return Returns the font.
	 */
	public Font getFont() {
		return font;
	}
	/**
	 * @param font The font to set.
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	/**
	 * @return Returns the stroke.
	 */
	public Stroke getStroke() {
		return stroke;
	}
	/**
	 * @param stroke The stroke to set.
	 */
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	/**
	 * @param paint The paint to set.
	 */
	public void setPaint(Paint paint) {
		if  (paint instanceof GradientPaint){
			SVGGradientPaint gp = new SVGGradientPaint((GradientPaint)paint);
			int index = paints.indexOf(gp);
			if (index == -1){
				paints.add(gp);
				definitions.appendChild(createGradientPaint(gp));
			}
			else{
				gp = (SVGGradientPaint)paints.get(index);
			}
			this.paint = gp;
		}
		else
			this.paint = paint;
	}
	
	/*****************************************
	 * Factory Methods
	 *****************************************/
	protected Element createGradientPaint(SVGGradientPaint paint){
		Element elem = dom.createElement("linearGradient");
		elem.setAttribute("id", paint.getId());
		elem.setAttribute("x1", Double.toString(paint.getPoint1().getX()));
		elem.setAttribute("y1", Double.toString(paint.getPoint1().getY()));
		elem.setAttribute("x2", Double.toString(paint.getPoint2().getX()));
		elem.setAttribute("y2", Double.toString(paint.getPoint2().getY()));
		elem.setAttribute("gradientUnits", "userSpaceOnUse");
		if (paint.isCyclic())
			elem.setAttribute("spreadMethod", "repeat");
		Element startColor = dom.createElement("stop");
		startColor.setAttribute("offset", "0%");
		startColor.setAttribute("stop-color", serializeToString(paint.getColor1()));
		elem.appendChild(startColor);
		Element endColor = dom.createElement("stop");
		endColor.setAttribute("offset", "100%");
		endColor.setAttribute("stop-color", serializeToString(paint.getColor2()));
		elem.appendChild(endColor);
		return elem;		
	}
	protected Element createLine(double arg0, double arg1, double arg2, double arg3) {
		Element elem = createElement("line");
		elem.setAttribute("x1", Double.toString(arg0));
		elem.setAttribute("y1", Double.toString(arg1));
		elem.setAttribute("x2", Double.toString(arg2));
		elem.setAttribute("y2", Double.toString(arg3));
		return elem;
	}
	
	protected Element createShape(Shape shape){
		PathIterator pathIter = shape.getPathIterator(null);
		String pathStr = "";
		while (!pathIter.isDone()){
			double[] points = new double[6];
			int TYPE = pathIter.currentSegment(points);
			switch (TYPE){
				case PathIterator.SEG_CLOSE:
					pathStr += " Z";
					break;
				case PathIterator.SEG_LINETO:
					pathStr += " L"+points[0]+" "+points[1];
				break;
				case PathIterator.SEG_QUADTO:
					pathStr += " Q"+points[0]+" "+points[1]+" " +points[2]+" "+points[3];
				break;
				case PathIterator.SEG_CUBICTO:
					pathStr += " C"+points[0]+" "+points[1]+" " +points[2]+" "+points[3]+" " +points[4]+" "+points[5];
				break;
				case PathIterator.SEG_MOVETO:
					pathStr += " M"+points[0]+" "+points[1];
				break;
			}
			pathIter.next();			
		}
		Element elem = dom.createElement("path");
		elem.setAttribute("d", pathStr);
		return elem;
	}
	
	protected Element createGeneralPath(Shape path){
		Element elem  = createShape(path);
		if (transforms.getType() != AffineTransform.TYPE_IDENTITY){
			double[] matrix = new double[6];
			transforms.getMatrix(matrix);
			elem.setAttribute("transform", "matrix("+matrix[0]+","+matrix[1]+","+matrix[2]+","+matrix[3]+","+matrix[4]+","+matrix[5]+")");
		}
		return elem;
	}
	
	protected Element createRect(double arg0, double arg1, double arg2, double arg3) {
		Element elem = createElement("rect");
		elem.setAttribute("x", Double.toString(arg0));
		elem.setAttribute("y", Double.toString(arg1));
		elem.setAttribute("width", Double.toString(arg2));
		elem.setAttribute("height", Double.toString(arg3));
		return elem;
	}
	protected Element createRoundRect(double arg0, double arg1, double arg2, double arg3, double arg4,
			double arg5) {
		Element elem = createElement("rect");
		elem.setAttribute("x", Double.toString(arg0));
		elem.setAttribute("y", Double.toString(arg1));
		elem.setAttribute("width", Double.toString(arg2));
		elem.setAttribute("height", Double.toString(arg3));
		elem.setAttribute("rx", Double.toString(arg2));
		elem.setAttribute("ry", Double.toString(arg3));
		return elem;
	}
	protected Element createOval(double arg0, double arg1, double arg2, double arg3) {
		Element elem = createElement("ellipse");
		elem.setAttribute("cx", Double.toString(arg0));
		elem.setAttribute("cy", Double.toString(arg1));
		elem.setAttribute("rx", Double.toString(arg2));
		elem.setAttribute("ry", Double.toString(arg3));
		return elem;
	}
	
	protected Element createArc(double x, double y, double width, double height, double startAngle,
			double arcAngle) {
		Element elem = createElement("path");
		double startX = x*Math.cos(startAngle);
		double startY = y*Math.sin(startAngle);
		double endX = x*Math.cos(startAngle+arcAngle);
		double endY = y*Math.sin(startAngle+arcAngle);
		int sweepFlag = (arcAngle < 0) ?0:1;
		elem.setAttribute("d", "M"+startX + ","+ startY+" a"+width/2 +","+height/2+ " " + Math.abs(arcAngle)+ " 0 " + sweepFlag+" " +endX + " " + endY);
		return elem;
	}
	
	public Element createPolygon(int[] arg0, int[] arg1, int arg2) {
		Element elem = createElement("polygon");
		StringBuffer pointsStr = new StringBuffer();;
		for (int x = 0; x < arg2; x++){
			pointsStr.append(arg0[x]).append(",").append(arg1[x]).append(" ");
		}
		elem.setAttribute("points", pointsStr.toString());
		return elem;
	}
	
	protected void initializeScriptStyles(){
		Element codeScript = dom.createElement("script");
		codeScript.appendChild(dom.createCDATASection(EventHandlers.content.toString()));
		appendChild(codeScript);		
		Element styles = dom.createElement("style");
		styles.setAttribute("type","text/css");
		styles.appendChild(dom.createCDATASection(EventHandlers.styles.toString()));
		appendChild(styles);		
		
	}
	protected Element createElement(String id){
		Element elem = dom.createElement(id);
		if (transforms.getType() != AffineTransform.TYPE_IDENTITY){
			double[] matrix = new double[6];
			transforms.getMatrix(matrix);
			elem.setAttribute("transform", "matrix("+matrix[0]+","+matrix[1]+","+matrix[2]+","+matrix[3]+","+matrix[4]+","+matrix[5]+")");
		}
		return elem;
		
	}
	
	public void drawTooltip(String tooltip){
		Element title = dom.createElement("title");
		title.appendChild(dom.createTextNode(tooltip));
		appendChild(title);
		currentParent.setAttribute("onmouseout", "TM.remove()");
		currentParent.setAttribute("onmouseover", "TM.show(evt)");
	}
	
	/**
	 * @return Returns the currentParent.
	 */
	public Element getCurrentParent() {
		return currentParent;
	}
}
