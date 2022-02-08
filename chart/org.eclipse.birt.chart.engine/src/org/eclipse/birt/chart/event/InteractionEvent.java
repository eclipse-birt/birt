/***********************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.event;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;

/**
 * An event type for Interactivity.
 */
public final class InteractionEvent extends ChartEvent {

	private static final long serialVersionUID = -3554746649816942383L;

	private PrimitiveRenderEvent _pre = null;

	private final LinkedHashMap<TriggerCondition, Action> _lhmTriggers = new LinkedHashMap<TriggerCondition, Action>();

	private transient Cursor cursor = null;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	private short zOrder = 0;

	/**
	 * The constructor.
	 */
	public InteractionEvent(Object source) {
		super(source);
		if (!(source instanceof StructureSource)) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * @return Returns the structur source of current event.
	 */
	public StructureSource getStructureSource() {
		return (StructureSource) super.getSource();
	}

	/**
	 * Sets the hotspot area defined by given rendering event for current event.
	 */
	public final void setHotSpot(PrimitiveRenderEvent pre) {
		_pre = pre;
	}

	/**
	 * @return Returns the rendering event defining current hotspot area.
	 */
	public final PrimitiveRenderEvent getHotSpot() {
		return _pre;
	}

	/**
	 * Adds trigger to current event.
	 */
	public final void addTrigger(Trigger t) {
		_lhmTriggers.put(t.getCondition(), t.getAction());
	}

	/**
	 * @return Returns the action for specific trigger condition.
	 */
	public final Action getAction(TriggerCondition tc) {
		return _lhmTriggers.get(tc);
	}

	/**
	 * @return Returns all triggers asscociated with current event.
	 */
	public final Trigger[] getTriggers() {
		if (_lhmTriggers.isEmpty()) {
			return null;
		}

		Trigger[] tga = new Trigger[_lhmTriggers.size()];
		int i = 0;

		for (Map.Entry<TriggerCondition, Action> entry : _lhmTriggers.entrySet()) {
			TriggerCondition tcKey = entry.getKey();
			Action acValue = entry.getValue();
			tga[i++] = goFactory.createTrigger(tcKey, acValue);
		}
		return tga;
	}

	public void reset() {
		_pre = null;
		_lhmTriggers.clear();
	}

	/**
	 * Reuses current event by given new source object.
	 */
	public final void reuse(StructureSource oNewSource) {
		source = oNewSource;
		_lhmTriggers.clear();
	}

	/**
	 * Returns mouse cursor.
	 * 
	 * @return cursor
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Set mouse cursor.
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
	public short getZOrder() {
		return zOrder;
	}

	/**
	 * Sets z-Order
	 * 
	 * @param zOrder
	 */
	public void setZOrder(short zOrder) {
		this.zOrder = zOrder;
	}
}
