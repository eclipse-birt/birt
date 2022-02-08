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

package org.eclipse.birt.chart.computation;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.datafeed.IDataPointEntry;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.impl.SizeImpl;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.DecimalFormat;

/**
 * Holds the information necessary to render a DataPoint Label
 */
public class DataPointHints {

	// in the moment
	// only datapoint of the mini slice in pie chart is virtual
	private boolean isVirtual = false;

	protected final RunTimeContext rtc;

	private Object oBaseValue;

	private Object oOrthogonalValue;

	private Double oStackedOrthogonalValue;

	private Object oSeriesValue;

	private Object oPercentileOrthogonalValue;

	private Map<String, Object> userValueMap;

	private int index;

	private final Location lo;

	private final double[] dSize;

	private final DataPoint dp;

	private boolean bOutside = false;

	protected final FormatSpecifier fsBase, fsOrthogonal, fsSeries, fsPercentile;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/computation"); //$NON-NLS-1$

	/**
	 * DataPointHints constructor.
	 * 
	 * @param _oBaseValue       Category data
	 * @param _oOrthogonalValue Value data
	 * @param _sSeriesValue     Value Series Name
	 * @param _dp               DataPoint for combined value retrieval
	 * @param _fsBase           Category Format Specifier
	 * @param _fsOrthogonal     Value Format Specifier
	 * @param _fsSeries         Value Series Name Format Specifier
	 * @param _idx              Category Series index
	 * @param _lo               Location
	 * @param _dSize            Size
	 * @param _rtc              Runtime Context
	 * 
	 */
	public DataPointHints(Object _oBaseValue, Object _oOrthogonalValue, Object _oSeriesValue, Object _oPercentileValue,
			DataPoint _dp, FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal, FormatSpecifier _fsSeries,
			FormatSpecifier _fsPercentile, int _idx, Location _lo, double _dSize, RunTimeContext _rtc)
			throws ChartException {

		dp = _dp;
		oBaseValue = _oBaseValue;
		oOrthogonalValue = _oOrthogonalValue;
		oSeriesValue = _oSeriesValue instanceof String ? _rtc.externalizedMessage((String) _oSeriesValue)
				: _oSeriesValue;
		oPercentileOrthogonalValue = _oPercentileValue;

		fsBase = _fsBase;
		fsOrthogonal = _fsOrthogonal;
		fsSeries = _fsSeries;
		fsPercentile = _fsPercentile;

		index = _idx;
		lo = _lo;
		rtc = _rtc;

		dSize = new double[2];
		dSize[0] = _dSize;
	}

	/**
	 * The constructor.
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _oSeriesValue
	 * @param _dp
	 * @param _fsBase
	 * @param _fsOrthogonal
	 * @param _fsSeries
	 * @param _idx              base Series index
	 * @param _lo
	 * @param _dSize
	 * @param _rtc
	 */
	public DataPointHints(Object _oBaseValue, Object _oOrthogonalValue, Object _oSeriesValue, Object _oPercentileValue,
			DataPoint _dp, FormatSpecifier _fsBase, FormatSpecifier _fsOrthogonal, FormatSpecifier _fsSeries,
			FormatSpecifier _fsPercentile, int _idx, Location _lo, double[] _dSize, RunTimeContext _rtc)
			throws ChartException {
		this(_oBaseValue, _oOrthogonalValue, _oSeriesValue, _oPercentileValue, _dp, _fsBase, _fsOrthogonal, _fsSeries,
				_fsPercentile, _idx, _lo, 0, _rtc);

		dSize[0] = _dSize[0];
		dSize[1] = _dSize[1];
	}

	/**
	 * Returns a copy of current DataPointHints object, which is virtual.
	 * 
	 * @return copy instance
	 * @throws ChartException
	 */
	public DataPointHints getVirtualCopy() throws ChartException {
		DataPointHints dph = new DataPointHints(oBaseValue, oOrthogonalValue, oSeriesValue, oPercentileOrthogonalValue,
				dp, fsBase, fsOrthogonal, fsSeries, fsPercentile, index, lo, dSize, rtc);

		dph.isVirtual = true;
		dph.userValueMap = this.userValueMap;

		return dph;
	}

	/**
	 * Accumulates values to current DataPointHintes.
	 * 
	 * @param _oBaseValue
	 * @param _oOrthogonalValue
	 * @param _oSeriesValue
	 */
	public void accumulate(Object _oBaseValue, Object _oOrthogonalValue, Object _oSeriesValue,
			Object _oPercentileOrthogonalValue) {
		if (oBaseValue instanceof Number) {
			if (_oBaseValue instanceof Number) {
				oBaseValue = new Double(((Number) oBaseValue).doubleValue() + ((Number) _oBaseValue).doubleValue());
			} else if (_oBaseValue instanceof NumberDataElement) {
				oBaseValue = new Double(
						((Number) oBaseValue).doubleValue() + ((NumberDataElement) _oBaseValue).getValue());
			}
		} else if (oBaseValue instanceof NumberDataElement) {
			if (_oBaseValue instanceof Number) {
				((NumberDataElement) oBaseValue)
						.setValue(((NumberDataElement) oBaseValue).getValue() + ((Number) _oBaseValue).doubleValue());
			} else if (_oBaseValue instanceof NumberDataElement) {
				((NumberDataElement) oBaseValue).setValue(
						((NumberDataElement) oBaseValue).getValue() + ((NumberDataElement) _oBaseValue).getValue());
			}
		}

		if (oOrthogonalValue instanceof Number) {
			if (_oOrthogonalValue instanceof Number) {
				oOrthogonalValue = new Double(
						((Number) oOrthogonalValue).doubleValue() + ((Number) _oOrthogonalValue).doubleValue());
			} else if (_oOrthogonalValue instanceof NumberDataElement) {
				oOrthogonalValue = new Double(
						((Number) oOrthogonalValue).doubleValue() + ((NumberDataElement) _oOrthogonalValue).getValue());
			}
		} else if (oOrthogonalValue instanceof NumberDataElement) {
			if (_oOrthogonalValue instanceof Number) {
				((NumberDataElement) oOrthogonalValue).setValue(
						((NumberDataElement) oOrthogonalValue).getValue() + ((Number) _oOrthogonalValue).doubleValue());
			} else if (_oOrthogonalValue instanceof NumberDataElement) {
				((NumberDataElement) oOrthogonalValue).setValue(((NumberDataElement) oOrthogonalValue).getValue()
						+ ((NumberDataElement) _oOrthogonalValue).getValue());
			}
		}

		if (oSeriesValue instanceof Number) {
			if (_oSeriesValue instanceof Number) {
				oSeriesValue = new Double(
						((Number) oSeriesValue).doubleValue() + ((Number) _oSeriesValue).doubleValue());
			} else if (_oSeriesValue instanceof NumberDataElement) {
				oSeriesValue = new Double(
						((Number) oSeriesValue).doubleValue() + ((NumberDataElement) _oSeriesValue).getValue());
			}
		} else if (oSeriesValue instanceof NumberDataElement) {
			if (_oSeriesValue instanceof Number) {
				((NumberDataElement) oSeriesValue).setValue(
						((NumberDataElement) oSeriesValue).getValue() + ((Number) _oSeriesValue).doubleValue());
			} else if (_oSeriesValue instanceof NumberDataElement) {
				((NumberDataElement) oSeriesValue).setValue(
						((NumberDataElement) oSeriesValue).getValue() + ((NumberDataElement) _oSeriesValue).getValue());
			}
		}

		if (oPercentileOrthogonalValue instanceof Number) {
			if (_oPercentileOrthogonalValue instanceof Number) {
				oPercentileOrthogonalValue = new Double(((Number) oPercentileOrthogonalValue).doubleValue()
						+ ((Number) _oPercentileOrthogonalValue).doubleValue());
			} else if (_oPercentileOrthogonalValue instanceof NumberDataElement) {
				oPercentileOrthogonalValue = new Double(((Number) oPercentileOrthogonalValue).doubleValue()
						+ ((NumberDataElement) _oPercentileOrthogonalValue).getValue());
			}
		} else if (oPercentileOrthogonalValue instanceof NumberDataElement) {
			if (_oPercentileOrthogonalValue instanceof Number) {
				((NumberDataElement) oPercentileOrthogonalValue)
						.setValue(((NumberDataElement) oPercentileOrthogonalValue).getValue()
								+ ((Number) _oPercentileOrthogonalValue).doubleValue());
			} else if (_oPercentileOrthogonalValue instanceof NumberDataElement) {
				((NumberDataElement) oPercentileOrthogonalValue)
						.setValue(((NumberDataElement) oPercentileOrthogonalValue).getValue()
								+ ((NumberDataElement) _oPercentileOrthogonalValue).getValue());
			}
		}

	}

	/**
	 * Returns the base value of current DataPointHintes.
	 * 
	 * @return base value
	 */
	public final Object getBaseValue() {
		return oBaseValue;
	}

	/**
	 * Returns the orthogonal value of current DataPointHintes.
	 * 
	 * @return orthogonal value
	 */
	public final Object getOrthogonalValue() {
		return oOrthogonalValue;
	}

	/**
	 * Sets orthogonal value.
	 * 
	 * @param value
	 * @since 2.5
	 */
	public final void setOrthogonalValue(Object value) {
		this.oOrthogonalValue = value;
	}

	/**
	 * Returns the stacked orthogonal value.
	 * 
	 * @return stacked value or null if not stacked
	 */
	public final Double getStackOrthogonalValue() {
		return oStackedOrthogonalValue;
	}

	public final void setStackOrthogonalValue(Double stackOrthogonalValue) {
		this.oStackedOrthogonalValue = stackOrthogonalValue;
	}

	/**
	 * Sets current data point is outside of plot area.
	 * 
	 */
	public final void markOutside() {
		this.bOutside = true;
	}

	/**
	 * Invalidates if current data point is outside of plot area.
	 * 
	 */
	public final boolean isOutside() {
		return this.bOutside;
	}

	/**
	 * Returns the series value of current DataPointHintes.
	 * 
	 * @return series value
	 */
	public final Object getSeriesValue() {
		return oSeriesValue;
	}

	/**
	 * Returns the percentile orthogonal value of current DataPointHintes.
	 * 
	 * @return percentile orthogonal value
	 */
	public final Object getPercentileOrthogonalValue() {
		return oPercentileOrthogonalValue;
	}

	/**
	 * Returns the location value of current DataPointHintes.
	 * 
	 * @return location
	 */
	public final Location getLocation() {
		return lo;
	}

	/**
	 * Returns the 3d location value of current DataPointHintes(only available in 3d
	 * mode).
	 * 
	 * @return location
	 */
	public final Location3D getLocation3D() {
		if (lo instanceof Location3D) {
			return (Location3D) lo;
		}

		return null;
	}

	/**
	 * Returns the index of current DataPointHints.
	 * 
	 * @return current index
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * Returns the size value of current DataPointHintes.
	 * 
	 * @return size value
	 */
	public final double getSize() {
		return dSize[0];
	}

	/**
	 * Returns the size value of current DataPointHintes(only available in 3d mode).
	 * 
	 * @return size value
	 */
	public final Size getSize2D() {
		return SizeImpl.create(dSize[0], dSize[1]);
	}

	/**
	 * Returns the user value of current DataPointHintes.
	 * 
	 * @param key
	 * @return user value
	 */
	public final Object getUserValue(String key) {
		if (userValueMap == null) {
			return null;
		}
		Object value = userValueMap.get(key);

		//
		if (value instanceof CDateTime) {
			return ((CDateTime) value).getDateTime();
		}
		return value;
	}

	/**
	 * Sets the user value of current DataPointHintes.
	 * 
	 * @param key
	 * @param value
	 */
	public final void setUserValue(String key, Object value) {
		if (userValueMap == null) {
			userValueMap = new HashMap<String, Object>();
		}

		userValueMap.put(key, value);
	}

	/**
	 * Returns the orthogonal display value of current DataPointHintes.
	 * 
	 * @return orthogonal value with format
	 */
	public final String getOrthogonalDisplayValue() {
		return getOrthogonalDisplayValue(fsOrthogonal);
	}

	/**
	 * Returns the base display value of current DataPointHintes.
	 * 
	 * @return base value with format
	 */
	public String getBaseDisplayValue() {
		return getBaseDisplayValue(fsBase);
	}

	/**
	 * Returns the series display value of current DataPointHintes.
	 * 
	 * @return series value with format
	 */
	public final String getSeriesDisplayValue() {
		return getSeriesDisplayValue(fsSeries);
	}

	/**
	 * Returns the percentile orthogonal display value of current DataPointHintes.
	 * 
	 * @return percentile orthogonal value with format
	 */
	public final String getPercentileOrthogonalDisplayValue() {
		return getPercentileOrthogonalDisplayValue(fsPercentile);
	}

	/**
	 * Returns the base display value of current DataPointHintes using given format
	 * specifier.
	 * 
	 * @param formatSpecifier format specifier of value to be displayed
	 * @return base value with format
	 */
	public final String getBaseDisplayValue(FormatSpecifier formatSpecifier) {
		FormatSpecifier fs = (formatSpecifier != null) ? formatSpecifier : fsBase;
		if (oBaseValue == null) {
			return IConstants.NULL_STRING;
		}

		// Format numerical category data with default pattern if no format
		// specified
		DecimalFormat df = null;
		if (fs == null && oBaseValue instanceof Number) {
			df = new DecimalFormat(ValueFormatter.getNumericPattern((Number) oBaseValue));
		}

		try {
			if (oBaseValue instanceof CDateTime && ((CDateTime) oBaseValue).isFullDateTime()
					&& rtc.getTimeZone() != null) {
				((CDateTime) oBaseValue).setTimeZone(rtc.getTimeZone());
			}
			return ValueFormatter.format(oBaseValue, fs, rtc.getULocale(), df);
		} catch (Exception ex) {
			logger.log(ILogger.ERROR, Messages.getString("exception.parse.value.format.specifier", //$NON-NLS-1$
					new Object[] { oBaseValue, fs }, rtc.getULocale()));
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * Returns the orthogonal display value of current DataPointHintes using given
	 * format specifier.
	 * 
	 * @param formatSpecifier format specifier of the value to be displayed
	 * @return orthogonal value with format
	 */
	public final String getOrthogonalDisplayValue(FormatSpecifier formatSpecifier) {
		FormatSpecifier fs = (formatSpecifier != null) ? formatSpecifier : fsOrthogonal;
		if (oOrthogonalValue == null) {
			return IConstants.NULL_STRING;
		}
		try {
			if (oOrthogonalValue instanceof CDateTime && ((CDateTime) oOrthogonalValue).isFullDateTime()
					&& rtc.getTimeZone() != null) {
				((CDateTime) oOrthogonalValue).setTimeZone(rtc.getTimeZone());
			}
			return ValueFormatter.format(oOrthogonalValue, fs, rtc.getULocale(), null);
		} catch (Exception ex) {
			logger.log(ILogger.ERROR, Messages.getString("exception.parse.value.format.specifier", //$NON-NLS-1$
					new Object[] { oOrthogonalValue, fs }, rtc.getULocale()));
		}
		return String.valueOf(oOrthogonalValue);
	}

	/**
	 * Returns the series display value of current DataPointHintes using given
	 * format specifier.
	 * 
	 * @param formatSpecifier format specifier of the value to be displayed
	 * @return series value with format
	 */
	public final String getSeriesDisplayValue(FormatSpecifier formatSpecifier) {
		FormatSpecifier fs = (formatSpecifier != null) ? formatSpecifier : fsSeries;
		if (oSeriesValue == null) {
			return IConstants.NULL_STRING;
		}
		try {
			if (oSeriesValue instanceof CDateTime && ((CDateTime) oSeriesValue).isFullDateTime()
					&& rtc.getTimeZone() != null) {
				((CDateTime) oSeriesValue).setTimeZone(rtc.getTimeZone());
			}
			return ValueFormatter.format(oSeriesValue, fs, rtc.getULocale(), null);
		} catch (Exception ex) {
			logger.log(ILogger.ERROR, Messages.getString("exception.parse.value.format.specifier", //$NON-NLS-1$
					new Object[] { oSeriesValue, fs }, rtc.getULocale()));
		}
		return IConstants.NULL_STRING;
	}

	/**
	 * Returns the percentile orthogonal display value of current DataPointHintes
	 * using given format specifier.
	 * 
	 * @param formatSpecifier format specifier of the value to be displayed
	 * @return percentile orthogonal value with format
	 */
	private final String getPercentileOrthogonalDisplayValue(FormatSpecifier formatSpecifier) {
		FormatSpecifier fs = (formatSpecifier != null) ? formatSpecifier : fsPercentile;
		if (oPercentileOrthogonalValue == null) {
			return IConstants.NULL_STRING;
		}
		try {
			return ValueFormatter.format(oPercentileOrthogonalValue, fs, rtc.getULocale(), null);
		} catch (Exception ex) {
			logger.log(ILogger.ERROR, Messages.getString("exception.parse.value.format.specifier", //$NON-NLS-1$
					new Object[] { oPercentileOrthogonalValue, fs }, rtc.getULocale()));
		}
		return String.valueOf(oPercentileOrthogonalValue);
	}

	/**
	 * Returns the display value of current DataPointHintes.
	 * 
	 * @return display value
	 */
	public final String getDisplayValue() {
		return getDisplayValue(null);
	}

	/**
	 * Returns the display value of current DataPointHintes.
	 * 
	 * @param fs format specifier of the value to be displayed
	 * @return display value
	 */
	public final String getDisplayValue(FormatSpecifier fs) {
		final StringBuffer sb = new StringBuffer();

		if (dp == null) {
			// Show orthogonal value by default.
			sb.append(getOrthogonalDisplayValue(fs));
		} else {
			final EList<DataPointComponent> el = dp.getComponents();

			if (dp.getPrefix() != null) {
				sb.append(dp.getPrefix());
			}
			DataPointComponent dpc;
			DataPointComponentType dpct;

			for (int i = 0; i < el.size(); i++) {
				dpc = el.get(i);
				dpct = dpc.getType();
				if (dpct == DataPointComponentType.BASE_VALUE_LITERAL) {
					sb.append(getBaseDisplayValue(fs));
				} else if (dpct == DataPointComponentType.ORTHOGONAL_VALUE_LITERAL) {
					String oType = dpc.getOrthogonalType();
					if (oType.length() == 0) {
						sb.append(getOrthogonalDisplayValue(fs));
					} else if (!(oOrthogonalValue instanceof IDataPointEntry)) {
						continue;
					} else {
						String str = ((IDataPointEntry) oOrthogonalValue).getFormattedString(oType,
								fs == null ? dpc.getFormatSpecifier() : fs, rtc.getULocale());
						if (str == null) {
							// Skip it if specific datapoint display is not
							// for current series
							continue;
						}
						sb.append(str);
					}
				} else if (dpct == DataPointComponentType.SERIES_VALUE_LITERAL) {
					sb.append(getSeriesDisplayValue(fs));
				} else if (dpct == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL) {
					sb.append(getPercentileOrthogonalDisplayValue(fs));
				}
				if (i < el.size() - 1) {
					sb.append(dp.getSeparator());
				}
			}
			if (dp.getSuffix() != null) {
				sb.append(dp.getSuffix());
			}
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return getDisplayValue();
	}

	/**
	 * Sets the base value.
	 * 
	 * @param newBaseValue the new base value
	 */
	public final void setBaseValue(Object newBaseValue) {
		oBaseValue = newBaseValue;
	}

	public final void setIndex(int index) {
		this.index = index;
	}

	public boolean isVirtual() {
		return isVirtual;
	}
}
