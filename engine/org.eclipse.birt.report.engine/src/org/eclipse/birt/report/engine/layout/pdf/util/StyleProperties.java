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

/**
 * Style property class
 *
 * @since 3.3
 *
 */
public class StyleProperties {

	/** property: width */
	public static final String WIDTH = "width";

	/** property: height */
	public static final String HEIGHT = "height";

	IStyle style;
	HashMap<String, Object> properties = new HashMap<>();

	/**
	 * Constructor
	 *
	 * @param style style
	 */
	public StyleProperties(IStyle style) {
		this.style = style;
	}

	/**
	 * Is empty
	 *
	 * @return true, when the property is empty
	 */
	public boolean isEmpty() {
		if (style == null) {
			return properties.isEmpty();
		}
		if (style.isEmpty()) {
			return properties.isEmpty();
		}
		return false;
	}

	/**
	 * Get the style
	 *
	 * @return the style
	 */
	public IStyle getStyle() {
		return style;
	}

	/**
	 * Add the property
	 *
	 * @param name  property name
	 * @param value property value
	 */
	public void addProperty(String name, Object value) {
		properties.put(name, value);
	}

	/**
	 * Get the property
	 *
	 * @param name property name
	 * @return the property
	 */
	public Object getProperty(String name) {
		return properties.get(name);
	}

	/**
	 * Set the property to content
	 *
	 * @param content content element
	 */
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

	/**
	 * Set the properties of width and height
	 *
	 * @param buffer content buffer
	 */
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
