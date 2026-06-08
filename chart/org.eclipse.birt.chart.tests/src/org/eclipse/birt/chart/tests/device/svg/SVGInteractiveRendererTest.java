/*******************************************************************************
 * Copyright (c) 2006 Eclipse contributors and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.birt.chart.tests.device.svg;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.birt.chart.device.svg.SVGGraphics2D;
import org.eclipse.birt.chart.device.svg.SVGInteractiveRenderer;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.layout.impl.TitleBlockImpl;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import junit.framework.TestCase;

/**
 * Tests {@link SVGInteractiveRenderer} methods.
 */
public class SVGInteractiveRendererTest extends TestCase {

	private SVGInteractiveRenderer renderer;
	private Document document;

	@Override
	protected void setUp() throws Exception {
		renderer = new SVGInteractiveRenderer(null);
	}

	/**
	 * You must call {@link SVGInteractiveRenderer#createHotspotLayer(Document)}
	 * before adding interactivity events.
	 */
	public void testIsInteractiveWhenHotspotIsNull() {
		assertFalse(renderer.isInteractive());
	}

	/**
	 * An empty hotspot layer is useless in the end, because there is no
	 * interactivity to render.
	 *
	 * @throws ParserConfigurationException if the SVG document creation fails.
	 */
	public void testIsInteractiveAfterCreatingHotspot() throws ParserConfigurationException {
		document = createSVGDocument();
		renderer.createHotspotLayer(document);
		assertFalse(renderer.isInteractive());
	}

	/**
	 * Creates a new SVG document.
	 *
	 * @return a new SVG document.
	 * @throws ParserConfigurationException if a {@link DocumentBuilder} is
	 *                                      impossible to create.
	 */
	private Document createSVGDocument() throws ParserConfigurationException {
		final DocumentBuilderFactory documentBuilderFactory = SecurityUtil.newDocumentBuilderFactory();
		final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		final DOMImplementation domImplementation = documentBuilder.getDOMImplementation();
		final DocumentType documentType = domImplementation.createDocumentType("svg", "-//W3C//DTD SVG 1.0//EN",
				"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
		final Document svgDocument = domImplementation.createDocument("http://www.w3.org/2000/svg", "svg",
				documentType);
		return svgDocument;
	}

	/**
	 * {@link SVGInteractiveRenderer#prepareInteractiveEvent(Element, InteractionEvent, Trigger[])}
	 * must add an item in cached events.
	 * {@link SVGInteractiveRenderer#addInteractivity()} will handle those cached
	 * events and render interactivity.
	 *
	 * @throws ParserConfigurationException if the SVG document creation fails.
	 */
	public void testIsInteractiveWithAnEvent() throws ParserConfigurationException {
		document = createSVGDocument();
		renderer.setSVG2D(new SVGGraphics2D(document));
		final Element element = document.createElement("a");
		final InteractionEvent event = new InteractionEvent(
				StructureSource.createTitle((TitleBlock) TitleBlockImpl.createDefault()));
		Trigger[] triggers = new Trigger[] {
				TriggerImpl.create(TriggerCondition.ONCLICK_LITERAL, createURLRedirectAction()) };
		renderer.prepareInteractiveEvent(element, event, triggers);
		renderer.createHotspotLayer(document);
		renderer.addInteractivity();
		assertTrue(renderer.isInteractive());
	}

	/**
	 * Creates a URL redirect action.
	 *
	 * @return a URL redirect action.
	 */
	private Action createURLRedirectAction() {
		final ActionValue value = URLValueImpl.create("https://eclipse.org", "blank", "parameter", "value", "series");
		final Action urlRedirectAction = ActionImpl.create(ActionType.URL_REDIRECT_LITERAL, value);
		return urlRedirectAction;
	}
}
