/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.style;

import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.birt.report.engine.content.IStyle;

/**
 * Style entry.
 *
 */
public class StyleEntry implements StyleConstant, Serializable, Cloneable {
	private static final long serialVersionUID = 6959747237392429540L;

	public static final String ENTRYNAME_HYPERLINK = "Hyperlink"; //$NON-NLS-1$

	private int type;

	private transient IStyle originalStyle;

	private String name = null;
	private Object[] props = null;
	private Integer hashCode;

	StyleEntry(StyleEntry entry) {
		this(entry.getType());

		for (int i = 0; i < props.length; i++) {
			props[i] = entry.props[i];
		}
		if (entry.hashCode != null) {
			hashCode = new Integer(entry.hashCode);
		}
		name = entry.name;
	}

	StyleEntry(IStyle originalStyle, int type) {
		this.originalStyle = originalStyle;
		this.type = type;
		props = new Object[StyleConstant.COUNT];
		name = null;
	}

	StyleEntry(int type) {
		this(null, type);
	}

	/**
	 * @deprecated use getProperty() instead
	 * @return
	 */
	@Deprecated
	public IStyle getStyle() {
		return originalStyle;
	}

	public void setProperty(int id, Object value) {
		props[id] = value;
		hashCode = null;
	}

	public Object getProperty(int id) {
		return props[id];
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof StyleEntry)) {
			return false;
		}

		StyleEntry tar = (StyleEntry) obj;

		/*
		 * if ( tar.type != this.type ) { return false; }
		 *
		 * for ( int i = 0; i < StyleConstant.COUNT; i++ ) { if ( props[i] != null ) {
		 * if ( !props[i].equals( tar.getProperty( i ) ) ) { return false; } } else { if
		 * ( props[i] != tar.getProperty( i ) ) { return false; } } } return true;
		 */
		return tar.hashCode() == this.hashCode();
	}

	@Override
	public int hashCode() {
		if (hashCode == null) {
			int code = 0;

			code += Integer.valueOf(type).hashCode() * 2 + 1;

			/*
			 * for ( int i = 0; i < StyleConstant.COUNT; i++ ) { int hashCode = props[i] ==
			 * null ? 0 : props[i].hashCode( ); code += hashCode * 2 + 1; }
			 */

			code += Arrays.deepHashCode(props);

			hashCode = code;
		}
		return hashCode.intValue();
	}

	public static boolean isNull(Object value) {
		if (value == null) {
			return true;
		}
		if (value instanceof String) {
			return StyleConstant.NULL.equalsIgnoreCase((String) value);
		}
		return false;
	}

	@Override
	public Object clone() {
		StyleEntry o = null;
		try {
			o = (StyleEntry) super.clone();
		} catch (CloneNotSupportedException e) {

		}

		for (int i = 0; i < props.length; i++) {
			o.setProperty(i, getProperty(i));
		}

		return o;
	}

	void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Double getDoubleProperty(int index) {
		Object o = getProperty(index);
		if (o instanceof Double) {
			return (Double) o;
		} else if (o instanceof Integer) {
			return (double) (Integer) o;
		}
		return null;
	}

	public Integer getIntegerProperty(int index) {
		Object o = getProperty(index);
		if (o instanceof Integer) {
			return (Integer) o;
		}
		return null;
	}

	public String getStringProperty(int index) {
		Object o = getProperty(index);
		if (o instanceof String) {
			return (String) o;
		}
		return null;
	}

	public Boolean getBoolProperty(int index) {
		Object o = getProperty(index);
		if (o instanceof Boolean) {
			return (Boolean) o;
		}
		return false;
	}

	/**
	 * Returns the style type
	 *
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * Returns whether this style entry has been added to the style manager.
	 *
	 * @return
	 */
	public boolean isAdded() {
		return name != null;
	}
}
