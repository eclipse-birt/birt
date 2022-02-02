/***********************************************************************
 * Copyright (c) 2005 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.tests.device.svg;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import junit.framework.TestCase;
import org.eclipse.birt.chart.device.svg.SVGGradientPaint;

public class SVGGradientPaintTest extends TestCase {

	float x1 = 5;
	float x2 = 3;
	float x3 = 9;
	float x4 = 6;
	Point2D.Float p1 = new Point2D.Float(x1, x2);
	Point2D.Float p2 = new Point2D.Float(x3, x4);

	SVGGradientPaint sgp1 = new SVGGradientPaint(new GradientPaint(p1, Color.PINK, p2, Color.WHITE, true));

	SVGGradientPaint sgp2 = new SVGGradientPaint(new GradientPaint(p2, Color.PINK, p1, Color.BLACK, false));

	SVGGradientPaint sgp3 = new SVGGradientPaint(new GradientPaint(x1, x2, Color.PINK, x3, x4, Color.WHITE, true));

	public void testEaquls() {
		assertEquals(false, sgp1.equals(sgp2));
		assertEquals(true, sgp1.equals(sgp3));
	}

	public void testHashCode() {
		assertEquals(1917931015, sgp1.hashCode());
		assertEquals(-662038211, sgp2.hashCode());
		assertEquals(1917931015, sgp3.hashCode());
	}

	public void testGetColor() {
		assertEquals(Color.PINK, sgp1.getColor1());
		assertEquals(Color.WHITE, sgp1.getColor2());
	}

	public void testGetPoint() {
		assertEquals(p1, sgp3.getPoint1());
		assertEquals(p2, sgp3.getPoint2());
	}

	public void testGetTransparency() {
		assertEquals(Color.OPAQUE, sgp2.getTransparency());
	}

	public void testIsCyclic() {
		assertEquals(true, sgp1.isCyclic());
		assertEquals(false, sgp2.isCyclic());
	}
}
