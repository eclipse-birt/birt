/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.computation;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;

/**
 * LegendItemHints
 */
public final class LegendItemHints {

	public enum Type {
		LG_GROUPNAME, LG_ENTRY, LG_MINSLICE, LG_SEPERATOR
	}

	private final Type type;
	private int index = -1;
	private SeriesDefinition sed = null;
	private Series series = null;

	private String sItem = null;
	private int validItemLen;

	private String sValue = null;
	private int validValueLen;

	private double left = 0;
	private double top = 0;
	private double width = 0d;
	private double itemHeight;
	private double valueHeight = 0d;

	private LegendItemHints(Type type, String sItem) {
		this.type = type;
		this.sItem = sItem;
	}

	private LegendItemHints(Type type, String sItem, String sValue, SeriesDefinition sed, Series series, int index) {
		this.type = type;
		this.sItem = sItem;
		this.sValue = sValue;
		this.sed = sed;
		this.series = series;
		this.index = index;
	}

	public static LegendItemHints newGroupNameEntry(String name) {
		return new LegendItemHints(Type.LG_GROUPNAME, name);
	}

	public static LegendItemHints newEntry(String sItem, String sValue, SeriesDefinition sed, Series se, int index) {
		return new LegendItemHints(Type.LG_ENTRY, sItem, sValue, sed, se, index);
	}

	public static LegendItemHints newCategoryEntry(String sItem, SeriesDefinition sed, Series se, int index) {
		return new LegendItemHints(Type.LG_ENTRY, sItem, null, sed, se, index);
	}

	public static LegendItemHints newMinSliceEntry(String sItem, SeriesDefinition sed, Series se, int index) {
		return new LegendItemHints(Type.LG_MINSLICE, sItem, null, sed, se, index);
	}

	public static LegendItemHints createSeperator() {
		return new LegendItemHints(Type.LG_SEPERATOR, null);
	}

	public LegendItemHints left(double left) {
		this.left = left;
		return this;
	}

	public LegendItemHints top(double top) {
		this.top = top;
		return this;
	}

	public LegendItemHints validItemLen(int validItemLen) {
		this.validItemLen = validItemLen;
		return this;
	}

	public LegendItemHints validValueLen(int validValueLen) {
		this.validValueLen = validValueLen;
		return this;
	}

	public LegendItemHints width(double width) {
		this.width = width;
		return this;
	}

	public LegendItemHints itemHeight(double itemHeight) {
		this.itemHeight = itemHeight;
		return this;
	}

	public LegendItemHints valueHeight(double valueHeight) {
		this.valueHeight = valueHeight;
		return this;
	}

	public Type getType() {
		return type;
	}

	/**
	 * @return Returns the series.
	 */
	public Series getSeries() {
		return series;
	}

	/**
	 * @return Returns the seriesDefinition.
	 */
	public SeriesDefinition getSeriesDefinition() {
		return sed;
	}

	/**
	 * @return Returns the index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @return Returns the sItem.
	 */
	public String getItemText() {
		return sItem;
	}

	/**
	 * @return legend item value.
	 */
	public Object getItemValue() {
		return series.getSeriesIdentifier();
	}

	/**
	 * Set the item text.
	 *
	 * @param itemText
	 */
	public void setItemText(String itemText) {
		this.sItem = itemText;
	}

	/**
	 * @return Returns the sValue.
	 */
	public String getValueText() {
		return sValue;
	}

	/**
	 * @param series The series to set.
	 */
	public void setSeries(Series series) {
		this.series = series;
	}

	/**
	 * @return Returns the left.
	 */
	public double getLeft() {
		return left;
	}

	/**
	 * @return Returns the top.
	 */
	public double getTop() {
		return top;
	}

	/**
	 * @return Returns the width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * @return Returns the itemHeight.
	 */
	public double getItemHeight() {
		return itemHeight;
	}

	/**
	 * @return Returns the valueHeight.
	 */
	public double getValueHeight() {
		return valueHeight;
	}

	/**
	 * @return Returns the validItemLen.
	 */
	public int getValidItemLen() {
		return validItemLen;
	}

	/**
	 * @return Returns the validValueLen.
	 */
	public int getValidValueLen() {
		return validValueLen;
	}

}
