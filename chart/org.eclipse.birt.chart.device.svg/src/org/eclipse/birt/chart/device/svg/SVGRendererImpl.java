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
import java.awt.image.BufferedImage;
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
import org.eclipse.birt.chart.device.FontUtil;
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
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

/**
 * Provides a reference implementation of a SVG device renderer. It translates
 * chart primitives into standard SVG v1.1 rendering primitives.
 */
public class SVGRendererImpl extends SwingRendererImpl {

	protected List<String> scriptRefList = null;
	protected List<String> scriptCodeList = null;
	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.svg/trace"); //$NON-NLS-1$

	/**
	 * 
	 */
	private IUpdateNotifier _iun = null;

	protected SVGInteractiveRenderer ivRenderer;

	/**
	 * The SVG version is "-//W3C//DTD SVG 1.0//EN".
	 */
	static private final String SVG_VERSION = "-//W3C//DTD SVG 1.0//EN"; //$NON-NLS-1$
	/**
	 * The SVG DTD is "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd".
	 */
	static private final String SVG_DTD = "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd"; //$NON-NLS-1$

	/**
	 * The SVG namespace http://www.w3.org/2000/svg
	 */
	static private final String XMLNS = "http://www.w3.org/2000/svg"; //$NON-NLS-1$
	/**
	 * The xmlns:xlink is "http://www.w3.org/1999/xlink".
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
	 * Property that determines if the SVG should resize to the containing element
	 * dimensions.
	 */
	protected boolean _resizeSVG = false;

	/**
	 * Property that determines if the generated SVG output should contain embedded
	 * javascript code.
	 */
	public boolean _enableScript = true;

	@Override
	protected void init() {
		// Do not invoke super method.
		final PluginSettings ps = PluginSettings.instance();
		try {
			SVGDisplayServer ids = (SVGDisplayServer) ps.getDisplayServer("ds.SVG"); //$NON-NLS-1$
			SVGTextRenderer tr = new SVGTextRenderer(ids);
			tr.setTextLayoutFactory(ids);
			_ids = ids;
			_tr = tr;
			ivRenderer = new SVGInteractiveRenderer(this);
		} catch (ChartException pex) {
			logger.log(pex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IDeviceRenderer#setProperty(java.lang.String,
	 * java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setProperty(String sProperty, Object oValue) {
		super.setProperty(sProperty, oValue);
		if (sProperty.equals(IDeviceRenderer.UPDATE_NOTIFIER)) {
			_iun = (IUpdateNotifier) oValue;
			ivRenderer.setIUpdateNotifier(_iun);

		} else if (sProperty.equals(IDeviceRenderer.EXPECTED_BOUNDS)) {
			final Bounds bo = (Bounds) oValue;
			try {
				dom = createSvgDocument(bo.getWidth(), bo.getHeight());
				svggc = new SVGGraphics2D(dom, _enableScript);
				ivRenderer.setSVG2D(svggc);
				// Create the hotspot layer
				ivRenderer.createHotspotLayer(dom);
				super.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, svggc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sProperty.equals(IDeviceRenderer.FILE_IDENTIFIER)) {
			oOutputIdentifier = oValue;
		} else if (sProperty.equals(ISVGConstants.JAVASCRIPT_CODE_LIST)) {
			scriptCodeList = (List<String>) oValue;
		} else if (sProperty.equals(ISVGConstants.JAVASCRIPT_URL_REF_LIST)) {
			scriptRefList = (List<String>) oValue;
		} else if (sProperty.equals(ISVGConstants.RESIZE_SVG)) {
			_resizeSVG = ((Boolean) oValue).booleanValue();
		} else if (sProperty.equals(ISVGConstants.ENABLE_SCRIPT)) {
			_enableScript = ((Boolean) oValue).booleanValue();
		}
	}

	protected void addScripts() {
		if (this.scriptCodeList != null) {
			for (int x = 0; x < scriptCodeList.size(); x++) {
				String code = scriptCodeList.get(x);
				((SVGGraphics2D) _g2d).addScript(code);
			}
		}
		if (this.scriptRefList != null) {
			for (int x = 0; x < scriptRefList.size(); x++) {
				String ref = scriptRefList.get(x);
				((SVGGraphics2D) _g2d).addScriptRef(ref);
			}
		}
	}

	/**
	 * 
	 * @param os
	 * @throws ChartException
	 */
	@Override
	public void after() throws ChartException {
		super.after();

		ivRenderer.addInteractivity();
		addScripts();
		((SVGGraphics2D) _g2d).flush();

		// make sure we add the hotspot layer to the bottom layer of the svg
		dom.getDocumentElement().appendChild(ivRenderer.getHotspotLayer());

		if (oOutputIdentifier instanceof OutputStream) // OUTPUT STREAM
		{
			try {
				writeDocumentToOutputStream(dom, (OutputStream) oOutputIdentifier);
			} catch (Exception ex) {
				throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING, ex);
			}
		} else if (oOutputIdentifier instanceof String) {
			FileOutputStream fos = null;
			try {
				fos = SecurityUtil.newFileOutputStream((String) oOutputIdentifier);
				writeDocumentToOutputStream(dom, fos);
				fos.close();
			} catch (Exception ex) {
				throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING, ex);
			}
		} else {
			throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING,
					"SVGRendererImpl.exception.UnableToWriteChartImage", //$NON-NLS-1$
					new Object[] { oOutputIdentifier }, null);
		}

		ivRenderer.clear();

	}

	/**
	 * Writes the XML document to an output stream
	 * 
	 * @param svgDocument
	 * @param outputStream
	 * @throws Exception
	 */
	private void writeDocumentToOutputStream(Document svgDocument, OutputStream outputStream) throws Exception {
		if (svgDocument != null && outputStream != null) {
			OutputStreamWriter writer = null;

			writer = SecurityUtil.newOutputStreamWriter(outputStream, "UTF-8"); //$NON-NLS-1$

			DOMSource source = new DOMSource(svgDocument);
			StreamResult result = new StreamResult(writer);

			// need to check if we should use sun's implementation of the
			// transform factory. This is needed to work with jdk1.4 and jdk1.5
			// with tomcat
			checkForTransformFactoryImpl();
			TransformerFactory transFactory = SecurityUtil.newTransformerFactory();
			Transformer transformer = transFactory.newTransformer();

			transformer.transform(source, result);
		}

	}

	/**
	 * Check to see if we should change the implementation of the TransformFactory.
	 * 
	 */
	private void checkForTransformFactoryImpl() {
		try {
			Class.forName("org.apache.xalan.processor.TransformerFactoryImpl"); //$NON-NLS-1$
		} catch (ClassNotFoundException e) {
			// Force using sun's implementation
			SecurityUtil.setSysProp("javax.xml.transform.TransformerFactory", //$NON-NLS-1$
					"com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"); //$NON-NLS-1$
		}
	}

	/**
	 * Creates an SVG document and assigns width and height to the root "svg"
	 * element.
	 * 
	 * @return Document the SVG document
	 * @throws Exception
	 */
	protected Document createSvgDocument() throws Exception {
		DocumentBuilderFactory factory = SecurityUtil.newDocumentBuilderFactory();
		DocumentBuilder builder;

		builder = factory.newDocumentBuilder();
		DOMImplementation domImpl = builder.getDOMImplementation();
		DocumentType dType = domImpl.createDocumentType("svg", //$NON-NLS-1$
				SVG_VERSION, SVG_DTD);
		Document svgDocument = domImpl.createDocument(XMLNS, "svg", dType); //$NON-NLS-1$
		svgDocument.getDocumentElement().setAttribute("xmlns", XMLNS); //$NON-NLS-1$
		svgDocument.getDocumentElement().setAttribute("xmlns:xlink", XMLNSXINK); //$NON-NLS-1$

		if (_resizeSVG) {
			svgDocument.getDocumentElement().setAttribute("onload", "resizeSVG(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
			// the onload() effect could be inaccurate, call onreisze again to
			// ensure, Note onload() is still needed, because onresize may never
			// be called.
			svgDocument.getDocumentElement().setAttribute("onresize", "resizeSVG(evt)"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return svgDocument;
	}

	protected Document createSvgDocument(double width, double height) throws Exception {
		Document svgDocument = createSvgDocument();
		svgDocument.getDocumentElement().setAttribute("width", //$NON-NLS-1$
				SVGGraphics2D.toString(width));
		svgDocument.getDocumentElement().setAttribute("height", //$NON-NLS-1$
				SVGGraphics2D.toString(height));
		svgDocument.getDocumentElement().setAttribute("initialWidth", //$NON-NLS-1$
				SVGGraphics2D.toString(width));
		svgDocument.getDocumentElement().setAttribute("initialHeight", //$NON-NLS-1$
				SVGGraphics2D.toString(height));
		return svgDocument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.IStructureDefinitionListener#changeStructure(
	 * org.eclipse.birt.chart.event.StructureChangeEvent)
	 */
	@Override
	public void changeStructure(StructureChangeEvent scev) {
		// Object sourceObj = scev.getSource( );
		// switch ( scev.getEventType( ) )
		// {
		// case StructureChangeEvent.BEFORE :
		// addGroupStructure( sourceObj );
		// break;
		// case StructureChangeEvent.AFTER :
		// removeGroupStructure( sourceObj );
		// break;
		//
		// }
	}

	protected void removeGroupStructure(Object block) {
		if ((block instanceof TitleBlock) || (block instanceof Legend) || (block instanceof Plot)
				|| (block instanceof LabelBlock) || (block instanceof Series) || (block instanceof DataPointHints))
			svggc.popParent();
	}

	protected void addGroupStructure(Object block) {
		if (block instanceof TitleBlock) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "title"); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		} else if (block instanceof Legend) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "legend"); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		} else if (block instanceof Plot) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "plot"); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		} else if (block instanceof LabelBlock) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "label"); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		} else if (block instanceof Series) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "series"); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		} else if (block instanceof DataPointHints) {
			Element group = svggc.dom.createElement("g"); //$NON-NLS-1$
			group.setAttribute("id", "dp" + block.hashCode()); //$NON-NLS-1$ //$NON-NLS-2$
			svggc.pushParent(group);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IPrimitiveRenderer#enableInteraction(org.
	 * eclipse.birt.chart.event.InteractionEvent)
	 */
	@Override
	public void enableInteraction(InteractionEvent ie) throws ChartException {
		if (_iun == null) {
			logger.log(ILogger.WARNING, Messages.getString("exception.missing.component.interaction", getULocale())); //$NON-NLS-1$
			return;
		}

		Trigger[] triggers = ie.getTriggers();
		if (triggers == null) {
			return;
		}

		// /////////////////////////////////////////////
		// Create the hotspot and add the hotspot on
		// the SVG hotspot layer
		// /////////////////////////////////////////////
		final PrimitiveRenderEvent pre = ie.getHotSpot();
		Element elm = null;

		if (pre instanceof PolygonRenderEvent) {
			final Location[] loa = ((PolygonRenderEvent) pre).getPoints();

			double[][] pa = getCoordinates(loa);

			elm = svggc.createPolygon(pa[0], pa[1], pa[0].length);
		} else if (pre instanceof OvalRenderEvent) {
			final Bounds boEllipse = ((OvalRenderEvent) pre).getBounds();

			elm = svggc.createOval(boEllipse.getLeft() + boEllipse.getWidth() / 2,
					boEllipse.getTop() + boEllipse.getHeight() / 2, boEllipse.getWidth() / 2,
					boEllipse.getHeight() / 2);
		} else if (pre instanceof RectangleRenderEvent) {
			final Bounds boRect = ((RectangleRenderEvent) pre).getBounds();

			elm = svggc.createRect(boRect.getLeft(), boRect.getTop(), boRect.getWidth(), boRect.getHeight());
		} else if (pre instanceof AreaRenderEvent) {
			AreaRenderEvent are = (AreaRenderEvent) pre;

			final GeneralPath gp = new GeneralPath();
			PrimitiveRenderEvent subPre;

			for (int i = 0; i < are.getElementCount(); i++) {
				subPre = are.getElement(i);
				if (subPre instanceof ArcRenderEvent) {
					final ArcRenderEvent acre = (ArcRenderEvent) subPre;
					final Arc2D.Double a2d = new Arc2D.Double(acre.getTopLeft().getX(), acre.getTopLeft().getY(),
							acre.getWidth(), acre.getHeight(), acre.getStartAngle(), acre.getAngleExtent(),
							toG2dArcType(acre.getStyle()));
					gp.append(a2d, true);
				} else if (subPre instanceof LineRenderEvent) {
					final LineRenderEvent lre = (LineRenderEvent) subPre;
					final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(),
							lre.getEnd().getX(), lre.getEnd().getY());
					gp.append(l2d, true);
				}
			}
			elm = svggc.createGeneralPath(gp);
		} else if (pre instanceof LineRenderEvent) {
			final GeneralPath gp = new GeneralPath();
			final LineRenderEvent lre = (LineRenderEvent) pre;
			final Line2D.Double l2d = new Line2D.Double(lre.getStart().getX(), lre.getStart().getY(),
					lre.getEnd().getX(), lre.getEnd().getY());
			gp.append(l2d, true);
			elm = svggc.createGeneralPath(gp);
		} else if (pre instanceof ArcRenderEvent) {
			final ArcRenderEvent are = (ArcRenderEvent) pre;

			if (are.getInnerRadius() >= 0 && are.getOuterRadius() > 0 && are.getInnerRadius() < are.getOuterRadius()) {
				Shape outerArc = new Arc2D.Double(
						are.getTopLeft().getX() + (are.getWidth() - 2 * are.getOuterRadius()) / 2,
						are.getTopLeft().getY() + (are.getHeight() - 2 * are.getOuterRadius()) / 2,
						2 * are.getOuterRadius(), 2 * are.getOuterRadius(), are.getStartAngle(), are.getAngleExtent(),
						Arc2D.PIE);
				Shape innerArc = new Arc2D.Double(
						are.getTopLeft().getX() + (are.getWidth() - 2 * are.getInnerRadius()) / 2,
						are.getTopLeft().getY() + (are.getHeight() - 2 * are.getInnerRadius()) / 2,
						2 * are.getInnerRadius(), 2 * are.getInnerRadius(), are.getStartAngle(), are.getAngleExtent(),
						Arc2D.PIE);

				Area fArea = new Area(outerArc);
				fArea.exclusiveOr(new Area(innerArc));

				// Shape prevClip = _g2d.getClip( );
				// _g2d.setClip( fArea );
				elm = svggc.createGeneralPath(fArea);
				// _g2d.setClip( prevClip );
			} else {
				elm = svggc.createGeneralPath(new Arc2D.Double(are.getTopLeft().getX(), are.getTopLeft().getY(),
						are.getWidth(), are.getHeight(), are.getStartAngle(), are.getAngleExtent(),
						toG2dArcType(are.getStyle())));
			}

		}

		ivRenderer.prepareInteractiveEvent(elm, ie, triggers);
	}

	@Override
	public void drawArc(ArcRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.drawArc(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	@Override
	public void drawArea(AreaRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.drawArea(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	@Override
	public void drawImage(ImageRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.drawImage(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}

	@Override
	protected Image createImage(byte[] data) {
		return new SVGImage(super.createImage(data), null, data);
	}

	@Override
	public void drawLine(LineRenderEvent lre) throws ChartException {
		ivRenderer.groupPrimitive(lre, false);
		super.drawLine(lre);
		ivRenderer.ungroupPrimitive(lre, false);
	}

	@Override
	public void drawOval(OvalRenderEvent ore) throws ChartException {
		ivRenderer.groupPrimitive(ore, false);
		super.drawOval(ore);
		ivRenderer.ungroupPrimitive(ore, false);
	}

	@Override
	public void drawPolygon(PolygonRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.drawPolygon(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}

	@Override
	public void drawRectangle(RectangleRenderEvent rre) throws ChartException {
		ivRenderer.groupPrimitive(rre, false);
		super.drawRectangle(rre);
		ivRenderer.ungroupPrimitive(rre, false);
	}

	@Override
	public void fillArc(ArcRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.fillArc(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	@Override
	public void fillArea(AreaRenderEvent are) throws ChartException {
		ivRenderer.groupPrimitive(are, false);
		super.fillArea(are);
		ivRenderer.ungroupPrimitive(are, false);
	}

	@Override
	public void fillOval(OvalRenderEvent ore) throws ChartException {
		ivRenderer.groupPrimitive(ore, false);
		super.fillOval(ore);
		ivRenderer.ungroupPrimitive(ore, false);
	}

	@Override
	public void fillPolygon(PolygonRenderEvent pre) throws ChartException {
		ivRenderer.groupPrimitive(pre, false);
		super.fillPolygon(pre);
		ivRenderer.ungroupPrimitive(pre, false);
	}

	@Override
	public void fillRectangle(RectangleRenderEvent rre) throws ChartException {
		ivRenderer.groupPrimitive(rre, false);
		super.fillRectangle(rre);
		ivRenderer.ungroupPrimitive(rre, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.IPrimitiveRenderListener#drawText(org.eclipse.
	 * birt.chart.event.TextRenderEvent)
	 */
	@Override
	public void drawText(TextRenderEvent tre) throws ChartException {
		String fontName = convertFont(tre.getLabel().getCaption().getFont().getName());
		if (fontName != null) {
			tre.getLabel().getCaption().getFont().setName(fontName);
		}

		ivRenderer.groupPrimitive(tre, true);

		switch (tre.getAction()) {
		case TextRenderEvent.UNDEFINED:
			throw new ChartException(ChartDeviceExtensionPlugin.ID, ChartException.RENDERING,
					"exception.missing.text.render.action", //$NON-NLS-1$
					Messages.getResourceBundle(getULocale()));

		case TextRenderEvent.RENDER_SHADOW_AT_LOCATION:
			_tr.renderShadowAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_AT_LOCATION:
			_tr.renderTextAtLocation(this, tre.getTextPosition(), tre.getLocation(), tre.getLabel());
			break;

		case TextRenderEvent.RENDER_TEXT_IN_BLOCK:
			_tr.renderTextInBlock(this, tre.getBlockBounds(), tre.getBlockAlignment(), tre.getLabel());
			break;
		}
		ivRenderer.ungroupPrimitive(tre, true);
	}

	@Override
	public String getMimeType() {
		return "image/svg+xml"; //$NON-NLS-1$
	}

	@Override
	protected String convertFont(String fontFamily) {
		return FontUtil.getFontFamily(fontFamily);
	}

	@Override
	protected Image createImageFromModel(Fill imageModel) throws ChartException {
		Image img = super.createImageFromModel(imageModel);
		if (img != null && !(img instanceof SVGImage)) {
			img = new SVGImage(img, null);
		}
		return img;
	}

	@Override
	protected BufferedImage convertPatternImage(Image img) {
		if (img instanceof SVGImage) {
			img = ((SVGImage) img).image;
		}
		return super.convertPatternImage(img);
	}

	@Override
	protected Shape getPolygon(Location[] loa) {
		return getPolygon2D(loa);
	}
}
