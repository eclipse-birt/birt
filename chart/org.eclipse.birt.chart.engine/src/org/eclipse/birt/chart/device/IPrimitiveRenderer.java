/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import java.util.EventListener;

import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.exception.ChartException;

/**
 * Provides low level primitive rendering notifications that are interpreted by
 * a device renderer.
 */
public interface IPrimitiveRenderer extends EventListener {

	/**
	 * This method is responsible for clipping an arbitrary area on the target
	 * rendering device's graphic context.
	 *
	 * @param cre Encapsulated information that defines the area to be clipped
	 */
	void setClip(ClipRenderEvent cre);

	/**
	 * This method is responsible for drawing an image on the target rendering
	 * device's graphic context.
	 *
	 * @param ire Encapsulated information that defines a polygon and its attributes
	 * @throws ChartException
	 */
	void drawImage(ImageRenderEvent ire) throws ChartException;

	/**
	 * This method is responsible for drawing a line on the target rendering
	 * device's graphic context.
	 *
	 * @param lre Encapsulated information that defines a line and its attributes
	 * @throws ChartException
	 */
	void drawLine(LineRenderEvent lre) throws ChartException;

	/**
	 * This method is responsible for drawing a rectangle on the target rendering
	 * device's graphic context.
	 *
	 * @param rre Encapsulated information that defines a rectangle and its
	 *            attributes
	 * @throws ChartException
	 */
	void drawRectangle(RectangleRenderEvent rre) throws ChartException;

	/**
	 * This method is responsible for filling a rectangle on the target rendering
	 * device's graphic context.
	 *
	 * @param rre Encapsulated information that defines a rectangle and its
	 *            attributes
	 * @throws ChartException
	 */
	void fillRectangle(RectangleRenderEvent rre) throws ChartException;

	/**
	 * This method is responsible for drawing a polygon on the target rendering
	 * device's graphic context.
	 *
	 * @param pre Encapsulated information that defines a polygon and its attributes
	 * @throws ChartException
	 */
	void drawPolygon(PolygonRenderEvent pre) throws ChartException;

	/**
	 * This method is responsible for filling a polygon on the target rendering
	 * device's graphic context.
	 *
	 * @param pre Encapsulated information that defines a polygon and its attributes
	 * @throws ChartException
	 */
	void fillPolygon(PolygonRenderEvent pre) throws ChartException;

	/**
	 * This method is responsible for drawing an elliptical arc on the target
	 * rendering device's graphic context.
	 *
	 * @param are Encapsulated information that defines the arc and its attributes
	 * @throws ChartException
	 */
	void drawArc(ArcRenderEvent are) throws ChartException;

	/**
	 * This method is responsible for filling an elliptical arc on the target
	 * rendering device's graphic context.
	 *
	 * @param are Encapsulated information that defines an arc and its attributes
	 * @throws ChartException
	 */
	void fillArc(ArcRenderEvent are) throws ChartException;

	/**
	 *
	 * @param ie
	 * @throws ChartException
	 */
	void enableInteraction(InteractionEvent ie) throws ChartException;

	/**
	 * This method is responsible for drawing a custom defined area on the target
	 * rendering device's graphic context.
	 *
	 * @param are Encapsulated information that defines the area and its attributes
	 * @throws ChartException
	 */
	void drawArea(AreaRenderEvent are) throws ChartException;

	/**
	 * This method is responsible for filling a custom defined area on the target
	 * rendering device's graphic context.
	 *
	 * @param are Encapsulated information that defines the area and its attributes
	 * @throws ChartException
	 */
	void fillArea(AreaRenderEvent are) throws ChartException;

	/**
	 * This method is responsible for drawing an oval area on the target rendering
	 * device's graphic context.
	 *
	 * @param ore Encapsulated information that defines the oval and its attributes
	 * @throws ChartException
	 */
	void drawOval(OvalRenderEvent ore) throws ChartException;

	/**
	 * This method is responsible for filling an oval area on the target rendering
	 * device's graphic context.
	 *
	 * @param ore Encapsulated information that defines the oval and its attributes
	 * @throws ChartException
	 */
	void fillOval(OvalRenderEvent ore) throws ChartException;

	/**
	 * This method renders text on the target rendering device's graphic context
	 * using one of the three methods:
	 *
	 * 1. Renders text (with optional insets, border, fill, etc) with the
	 * encapsulating container rectangle's corner or edge aligning against a given
	 * point 2. Renders a shadow offset with the encapsulating container rectangle's
	 * corner or edge aligning against a given point 3. Renders text (with optional
	 * insets, border, fill, etc) with the encapsulating container rectangle's
	 * bounding box aligned with a parent block's bounding box
	 *
	 * @param ore Encapsulated information that defines the text being rendered, its
	 *            position and various other attributes
	 * @throws ChartException
	 */
	void drawText(TextRenderEvent tre) throws ChartException;

	/**
	 * This method is capable of applying a global transformation on the device
	 * specific graphics context Available transformation types are: SCALE,
	 * TRANSLATE, ROTATE
	 *
	 * @param tev
	 * @throws ChartException
	 */
	void applyTransformation(TransformationEvent tev) throws ChartException;
}
