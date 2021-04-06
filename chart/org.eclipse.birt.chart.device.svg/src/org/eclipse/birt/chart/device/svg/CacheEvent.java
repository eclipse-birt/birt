/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.svg;

import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.data.Trigger;
import org.w3c.dom.Element;

/**
 * CacheEvent class, used to cache elements and associated events and triggers.
 */

public class CacheEvent {

	private Element elm;
	private StructureSource src;
	private Trigger[] triggers;
	private Cursor cursor;

	public CacheEvent(Element elm, StructureSource src, Trigger[] triggers, Cursor cursor) {
		this.elm = elm;
		this.src = src;
		this.triggers = triggers;
		this.cursor = cursor;
	}

	public Trigger[] getTriggers() {
		return triggers;
	}

	public Element getElement() {
		return elm;
	}

	public StructureSource getSource() {
		// TODO Auto-generated method stub
		return src;
	}

	/**
	 * Returns cursor.
	 * 
	 * @return
	 */
	public Cursor getCursor() {
		return cursor;
	}
}
