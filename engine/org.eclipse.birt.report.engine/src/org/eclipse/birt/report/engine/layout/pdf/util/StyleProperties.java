/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;

public class StyleProperties {

	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	IStyle style;
	HashMap<String, Object> properties = new HashMap<>();

	public StyleProperties(IStyle style) {
		this.style = style;
	}

	public boolean isEmpty() {
		if (style == null) {
			return properties.isEmpty();
		} else {
			if (style.isEmpty()) {
				return properties.isEmpty();
			}
			return false;
		}
	}

	public IStyle getStyle() {
		return style;
	}

	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}

	public Object getProperty(String name) {
		return properties.get(name);
	}

	public void setProperties(IContent content) {
		Object w = properties.get(WIDTH);
		if (w != null) {
			content.setWidth((DimensionType) w);
		}
		Object h = properties.get(HEIGHT);
		if (h != null) {
			content.setHeight((DimensionType) h);
		}
	}

	public void setProperties(StringBuffer buffer) {
		Object w = properties.get(WIDTH);
		if (w != null) {
			buffer.append(WIDTH);
			buffer.append(":");
			buffer.append(w);
			buffer.append(";");
		}
		Object h = properties.get(HEIGHT);
		if (w != null) {
			buffer.append(HEIGHT);
			buffer.append(":");
			buffer.append(h);
			buffer.append(";");
		}
	}
}
