/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.style;

import java.util.Objects;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;

/**
 * A default implementaitn for IStyle.
 */
public final class SimpleStyle implements IStyle {

	private FontDefinition font;

	private ColorDefinition color;

	private ColorDefinition backcolor;

	private Image backimage;

	private Insets padding;

	private FormatSpecifier dateTimeFormat;

	private FormatSpecifier stringFormat;

	private FormatSpecifier numberFormat;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The constructor.
	 */
	public SimpleStyle() {
		super();
	}

	/**
	 * The constructor.
	 *
	 * @param font
	 * @param backcolor
	 * @param backimage
	 * @param padding
	 */
	public SimpleStyle(FontDefinition font, ColorDefinition color, ColorDefinition backcolor, Image backimage,
			Insets padding) {
		super();

		setFont(font);
		setColor(color);
		setBackgroundColor(backcolor);
		setBackgroundImage(backimage);
		setPadding(padding);
	}

	/**
	 * The constructor.
	 *
	 * @param src
	 */
	public SimpleStyle(IStyle src) {
		super();

		if (src != null) {
			if (src.getFont() != null) {
				setFont(goFactory.copyOf(src.getFont()));
			}
			if (src.getColor() != null) {
				setColor(goFactory.copyOf(src.getColor()));
			}
			if (src.getBackgroundColor() != null) {
				setBackgroundColor(goFactory.copyOf(src.getBackgroundColor()));
			}
			if (src.getBackgroundImage() != null) {
				setBackgroundImage(goFactory.copyOf(src.getBackgroundImage()));
			}
			if (src.getPadding() != null) {
				setPadding(goFactory.copyOf(src.getPadding()));
			}
			if (src.getDateTimeFormat() != null) {
				setDateTimeFormat(src.getDateTimeFormat().copyInstance());
			}
			if (src.getNumberFormat() != null) {
				setNumberFormat(src.getNumberFormat().copyInstance());
			}
			if (src.getStringFormat() != null) {
				setStringFormat(src.getStringFormat().copyInstance());
			}
		}
	}

	/**
	 * Returns a copy of current instance.
	 *
	 * @return
	 */
	public SimpleStyle copy() {
		SimpleStyle ss = new SimpleStyle();

		if (font != null) {
			ss.setFont(goFactory.copyOf(font));
		}
		if (color != null) {
			ss.setColor(goFactory.copyOf(color));
		}
		if (backcolor != null) {
			ss.setBackgroundColor(goFactory.copyOf(backcolor));
		}
		if (backimage != null) {
			ss.setBackgroundImage(goFactory.copyOf(backimage));
		}
		if (padding != null) {
			ss.setPadding(goFactory.copyOf(padding));
		}
		if (dateTimeFormat != null) {
			ss.setDateTimeFormat(dateTimeFormat.copyInstance());
		}
		if (numberFormat != null) {
			ss.setNumberFormat(numberFormat.copyInstance());
		}
		if (stringFormat != null) {
			ss.setStringFormat(stringFormat.copyInstance());
		}

		return ss;
	}

	/**
	 * Sets the font of current style.
	 *
	 * @param font
	 */
	public void setFont(FontDefinition font) {
		this.font = font;
	}

	/**
	 * Sets the color of current style.
	 *
	 * @param color
	 */
	public void setColor(ColorDefinition color) {
		this.color = color;
	}

	/**
	 * Sets the background color of current style.
	 *
	 * @param backcolor
	 */
	public void setBackgroundColor(ColorDefinition backcolor) {
		this.backcolor = backcolor;
	}

	/**
	 * Sets the background image of current style.
	 *
	 * @param backimage
	 */
	public void setBackgroundImage(Image backimage) {
		this.backimage = backimage;
	}

	/**
	 * Sets the padding of current style.
	 *
	 * @param padding
	 */
	public void setPadding(Insets padding) {
		this.padding = padding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyle#getFont()
	 */
	@Override
	public FontDefinition getFont() {
		return font;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyle#getBackgroundColor()
	 */
	@Override
	public ColorDefinition getBackgroundColor() {
		return backcolor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyle#getBackgroundImage()
	 */
	@Override
	public Image getBackgroundImage() {
		return backimage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyle#getPadding()
	 */
	@Override
	public Insets getPadding() {
		return padding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.style.IStyle#getColor()
	 */
	@Override
	public ColorDefinition getColor() {
		return color;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(backcolor, backimage, color, font, padding);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		SimpleStyle other = (SimpleStyle) obj;
		if (!Objects.equals(backcolor, other.backcolor)) {
			return false;
		}
		if (!Objects.equals(backimage, other.backimage)) {
			return false;
		}
		if (!Objects.equals(color, other.color)) {
			return false;
		}
		if (!Objects.equals(font, other.font)) {
			return false;
		}
		if (!Objects.equals(padding, other.padding)) {
			return false;
		}
		return true;
	}

	@Override
	public FormatSpecifier getDateTimeFormat() {
		return dateTimeFormat;
	}

	public void setDateTimeFormat(FormatSpecifier df) {
		this.dateTimeFormat = df;
	}

	@Override
	public FormatSpecifier getNumberFormat() {
		return numberFormat;
	}

	public void setNumberFormat(FormatSpecifier nf) {
		this.numberFormat = nf;
	}

	@Override
	public FormatSpecifier getStringFormat() {
		return stringFormat;
	}

	public void setStringFormat(FormatSpecifier sf) {
		this.stringFormat = sf;
	}
}
