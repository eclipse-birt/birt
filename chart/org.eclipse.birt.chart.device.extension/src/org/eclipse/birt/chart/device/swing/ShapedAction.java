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

package org.eclipse.birt.chart.device.swing;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.device.g2d.G2dRendererBase;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;

/**
 * This class provides a shape definition and an associated action that is
 * invoked when interaction occurs with a chart rendered on a SWING device.
 */
public final class ShapedAction {

	private final StructureSource _oSource;

	private final Shape _sh;

	private final Map<TriggerCondition, Action> _triggers = new HashMap<TriggerCondition, Action>();

	private Cursor cursor;

	private int zOrder = 0;

	/**
	 * This constructor supports polygon shapes Future shapes (and corresponding
	 * constructors) will be added later
	 * 
	 * @param source
	 * @param loa
	 * @param clipping
	 */
	public ShapedAction(StructureSource oSource, Location[] loa, Shape clipping) {
		_oSource = oSource;
		if (clipping != null) {
			Area ar1 = new Area(clipping);
			Area ar2 = new Area(G2dRendererBase.getPolygon2D(loa));
			ar2.intersect(ar1);
			_sh = ar2;
		} else {
			_sh = G2dRendererBase.getPolygon2D(loa);
		}
	}

	/**
	 * This constructor supports shape definition via an ellipse
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param clipping
	 */
	public ShapedAction(StructureSource oSource, Bounds boEllipse, Shape clipping) {
		_oSource = oSource;
		if (clipping != null) {
			Area ar1 = new Area(clipping);
			Area ar2 = new Area(new Ellipse2D.Double(boEllipse.getLeft(), boEllipse.getTop(), boEllipse.getWidth(),
					boEllipse.getHeight()));
			ar2.intersect(ar1);
			_sh = ar2;
		} else {
			_sh = new Ellipse2D.Double(boEllipse.getLeft(), boEllipse.getTop(), boEllipse.getWidth(),
					boEllipse.getHeight());
		}
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param clipping
	 */
	public ShapedAction(StructureSource oSource, Bounds boEllipse, double dStart, double dExtent, int iArcType,
			Shape clipping) {
		_oSource = oSource;
		if (clipping != null) {
			Area ar1 = new Area(clipping);
			Area ar2 = new Area(new Arc2D.Double(boEllipse.getLeft(), boEllipse.getTop(), boEllipse.getWidth(),
					boEllipse.getHeight(), dStart, dExtent, iArcType));
			ar2.intersect(ar1);
			_sh = ar2;
		} else {
			_sh = new Arc2D.Double(boEllipse.getLeft(), boEllipse.getTop(), boEllipse.getWidth(), boEllipse.getHeight(),
					dStart, dExtent, iArcType);
		}
	}

	/**
	 * Returns the shape associated with current ShapedAction.
	 * 
	 * @return shape
	 */
	public final Shape getShape() {
		return _sh;
	}

	/**
	 * Returns the action associated with current ShapedAction.
	 * 
	 * @return action
	 */
	public final Action getActionForCondition(TriggerCondition condition) {
		return _triggers.get(condition);
	}

	/**
	 * Returns the source object associated with current ShapedAction.
	 * 
	 * @return source object
	 */
	public final StructureSource getSource() {
		return _oSource;
	}

	public void add(TriggerCondition tc, Action ac) {
		_triggers.put(tc, ac);

	}

	/**
	 * Returns cursor.
	 * 
	 * @return cursor
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Sets cursor.
	 * 
	 * @param cursor
	 */
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}

	/**
	 * Returns z-order
	 * 
	 * @return zOrder
	 */
	public int getZOrder() {
		return zOrder;
	}

	/**
	 * Sets z-order
	 * 
	 * @param zOrder
	 */
	public void setZOrder(int zOrder) {
		this.zOrder = zOrder;
	}
}
