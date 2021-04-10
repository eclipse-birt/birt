/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NullDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.BigNumber;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

/**
 * An immutable class with convenience methods provided to retrieve data from
 * the dataset
 */
public final class DataSetIterator implements Iterator {

	private double[] da = null;

	private Double[] dda = null;

	private Calendar[] ca = null;

	private long[] la = null;

	private String[] sa = null;

	private Collection co = null;

	private int iDataType = IConstants.UNDEFINED;

	private int iContentType = IConstants.UNDEFINED;

	private int iCursor = 0;

	private Iterator it = null;

	private int iRowCount = 0;

	private Calendar cReused = null;

	private Object[] oa = null;

	private boolean isReverse = false;

	private BigNumber[] cnda;

	private Number[] nda;

	/**
	 * 
	 * @param ds
	 */
	public DataSetIterator(Double[] dda) {
		this.dda = dda;
		iDataType = IConstants.NUMERICAL;
		iContentType = IConstants.NON_PRIMITIVE_ARRAY;
		iRowCount = dda.length;
	}

	/**
	 * 
	 * @param sa
	 */
	public DataSetIterator(String[] sa) {
		this.sa = sa;
		iDataType = IConstants.TEXT;
		iContentType = IConstants.NON_PRIMITIVE_ARRAY;
		iRowCount = sa.length;
	}

	/**
	 * 
	 * @param sa
	 */
	public DataSetIterator(Calendar[] ca) throws ChartException {
		this.ca = ca;
		iDataType = IConstants.DATE_TIME;
		iContentType = IConstants.NON_PRIMITIVE_ARRAY;
		iRowCount = ca.length;
		updateDateTimeValues();
	}

	/**
	 * 
	 * @param ds
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	public DataSetIterator(Object oContent, int iDataType) throws IllegalArgumentException, ChartException {
		if ((iDataType & IConstants.LINEAR) == IConstants.LINEAR
				|| (iDataType & IConstants.LOGARITHMIC) == IConstants.LOGARITHMIC) {
			iDataType = IConstants.NUMERICAL;
		}
		this.iDataType = iDataType;
		if (iDataType == IConstants.NUMERICAL) {
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof double[]) {
				iContentType = IConstants.PRIMITIVE_ARRAY;
				da = (double[]) oContent;
			} else if (oContent instanceof Double[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				dda = (Double[]) oContent;
			} else if (oContent instanceof BigNumber[]) {
				iContentType = IConstants.BIG_NUMBER_PRIMITIVE_ARRAY;
				cnda = (BigNumber[]) oContent;
			} else if (oContent instanceof Number[]) {
				iContentType = IConstants.NUMBER_PRIMITIVE_ARRAY;
				nda = (Number[]) oContent;
			}
		} else if (iDataType == IConstants.DATE_TIME) {
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof long[]) {
				iContentType = IConstants.PRIMITIVE_ARRAY;
				la = (long[]) oContent;
				cReused = Calendar.getInstance();
			} else if (oContent instanceof Calendar[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				ca = (Calendar[]) oContent;
			}
			updateDateTimeValues();
		} else if (iDataType == IConstants.TEXT) {
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof String[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				sa = (String[]) oContent;
			}
		} else {
			// for other anonymous types
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof Object[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				oa = (Object[]) oContent;
			}
			iDataType = IConstants.UNDEFINED;
		}

		if (iContentType == IConstants.UNDEFINED) {
			throw new IllegalArgumentException(
					MessageFormat.format(Messages.getResourceBundle().getString("exception.process.content.type"), //$NON-NLS-1$
							new Object[] { oContent, Integer.valueOf(iDataType) })

			);
		}

		if (co != null) {
			it = co.iterator();
		}

		iRowCount = getRowCountInternal();
	}

	/**
	 * 
	 * @param ds
	 * @throws IllegalArgumentException
	 * @throws ChartException
	 */
	public DataSetIterator(DataSet ds) throws IllegalArgumentException, ChartException {
		Object oContent = ds.getValues();
		if (ds instanceof NumberDataSet) {
			iDataType = IConstants.NUMERICAL;
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof double[]) {
				iContentType = IConstants.PRIMITIVE_ARRAY;
				da = (double[]) oContent;
			} else if (oContent instanceof Double[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				dda = (Double[]) oContent;
			} else if (oContent instanceof Number) {
				iContentType = IConstants.PRIMITIVE_ARRAY;
				da = new double[] { ((Number) oContent).doubleValue() };
			} else if (oContent instanceof BigNumber[]) {
				iContentType = IConstants.BIG_NUMBER_PRIMITIVE_ARRAY;
				cnda = (BigNumber[]) oContent;
			} else if (oContent instanceof Number[]) {
				iContentType = IConstants.NUMBER_PRIMITIVE_ARRAY;
				nda = (Number[]) oContent;
			}
		} else if (ds instanceof DateTimeDataSet) {
			iDataType = IConstants.DATE_TIME;
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof long[]) {
				iContentType = IConstants.PRIMITIVE_ARRAY;
				la = (long[]) oContent;
				cReused = Calendar.getInstance();
			} else if (oContent instanceof Calendar[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				ca = (Calendar[]) oContent;
			}
			updateDateTimeValues();
		} else if (ds instanceof TextDataSet) {
			iDataType = IConstants.TEXT;
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof String[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				sa = (String[]) oContent;
			}
		} else if (ds instanceof NullDataSet) {
			iDataType = IConstants.OTHER;
			iContentType = IConstants.NON_PRIMITIVE_ARRAY;
			oa = (Object[]) oContent;
		} else {
			// for other anonymous types
			iDataType = IConstants.OTHER;
			if (oContent instanceof Collection) {
				iContentType = IConstants.COLLECTION;
				co = (Collection) oContent;
			} else if (oContent instanceof Object[]) {
				iContentType = IConstants.NON_PRIMITIVE_ARRAY;
				oa = (Object[]) oContent;
			}
			iDataType = IConstants.UNDEFINED;
		}

		if (iContentType == IConstants.UNDEFINED) {
			throw new IllegalArgumentException(
					MessageFormat.format(Messages.getResourceBundle().getString("exception.process.content.dataset"), //$NON-NLS-1$
							new Object[] { oContent, ds })

			);
		}

		if (co != null) {
			it = co.iterator();
		}

		iRowCount = getRowCountInternal();
	}

	/**
	 * @return
	 */
	public final boolean isEmpty() {
		return iRowCount <= 0;
	}

	/**
	 * @return
	 */
	private final int getRowCountInternal() {
		if (iContentType == IConstants.COLLECTION) {
			return co.size();
		} else if (iDataType == IConstants.TEXT) {
			return sa.length;
		} else if (iDataType == IConstants.NUMERICAL) {
			if (iContentType == IConstants.PRIMITIVE_ARRAY) {
				return da.length;
			} else if (iContentType == IConstants.NON_PRIMITIVE_ARRAY) {
				return dda.length;
			} else if (iContentType == IConstants.BIG_NUMBER_PRIMITIVE_ARRAY) {
				return cnda.length;
			} else if (iContentType == IConstants.NUMBER_PRIMITIVE_ARRAY) {
				return nda.length;
			}
		} else if (iDataType == IConstants.DATE_TIME) {
			if (iContentType == IConstants.PRIMITIVE_ARRAY) {
				return la.length;
			} else if (iContentType == IConstants.NON_PRIMITIVE_ARRAY) {
				return ca.length;
			}
		} else if (iDataType == IConstants.TEXT) {
			return sa.length;
		} else if (oa != null) {
			return oa.length;
		}
		return -1; // <<<=== SHOULD NEVER REACH HERE
	}

	/**
	 * @return
	 */
	public final double nextPrimitiveDouble() {
		return da[getIndex()];
	}

	/**
	 * @return
	 */
	public final Double nextDouble() {
		if (it != null) {
			iCursor++;
			return (Double) it.next();
		}
		return dda[getIndex()];
	}

	public final BigNumber nextBigNumber() {
		if (it != null) {
			iCursor++;
			return (BigNumber) it.next();
		}
		return cnda[getIndex()];
	}

	public final Number nextNumber() {
		if (it != null) {
			iCursor++;
			return (Number) it.next();
		}
		return nda[getIndex()];
	}

	/**
	 * @return
	 */
	public final Calendar nextDateTime() {
		if (it != null) {
			iCursor++;
			return (Calendar) it.next();
		}
		return ca[getIndex()];
	}

	/**
	 * @return
	 */
	public final String nextText() {
		if (it != null) {
			iCursor++;
			return (String) it.next();
		}
		return sa[getIndex()];
	}

	/**
	 * @return
	 */
	public final Object nextObject() {
		return oa[getIndex()];
	}

	/**
	 * @return
	 */
	public final Calendar nextPrimitiveDateTime() {
		cReused.setTimeInMillis(la[getIndex()]);
		return cReused;
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasNext() {
		if (it != null) {
			return it.hasNext();
		} else {
			return (iCursor < iRowCount);
		}
	}

	/**
	 * 
	 * @return
	 */
	public final Object next() {
		if (iCursor >= iRowCount) {
			throw new RuntimeException(
					new ChartException(ChartEnginePlugin.ID, ChartException.COMPUTATION, "exception.out.of.bounds", //$NON-NLS-1$
							Messages.getResourceBundle()));
		}

		if (it != null) {
			iCursor++;
			return it.next();
		}

		if (iDataType == IConstants.NUMERICAL) {
			if (iContentType == IConstants.NON_PRIMITIVE_ARRAY) {
				return nextDouble();
			} else if (iContentType == IConstants.PRIMITIVE_ARRAY) {
				return new Double(nextPrimitiveDouble());
			} else if (iContentType == IConstants.BIG_NUMBER_PRIMITIVE_ARRAY) {
				return nextBigNumber();
			} else if (iContentType == IConstants.NUMBER_PRIMITIVE_ARRAY) {
				return nextNumber();
			}
		} else if (iDataType == IConstants.DATE_TIME) {
			if (iContentType == IConstants.NON_PRIMITIVE_ARRAY) {
				return nextDateTime();
			} else if (iContentType == IConstants.PRIMITIVE_ARRAY) {
				return nextPrimitiveDateTime();
			}
		} else if (iDataType == IConstants.TEXT) {
			return nextText();
		} else
		// OTHER
		{
			return nextObject();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		// TODO Add remove operation support.
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return iRowCount;
	}

	/**
	 * 
	 */
	public final void reset() {
		iCursor = 0;
		resetIterator();
	}

	/**
	 * 
	 * @return
	 */
	public final Object first() {
		reset();
		return next();
	}

	/**
	 * 
	 * @return
	 */
	public final Object last() {
		// TBD: OPTIMIZE FOR DIRECT ACCESS TO LAST ELEMENT IN ARRAY
		reset();
		Object o = null;
		while (hasNext()) {
			o = next();
		}
		return o;
	}

	/**
	 * Frees all references to data held internally in this structure
	 */
	public final void clear() {
		dda = null;
		ca = null;
		da = null;
		la = null;
		oa = null;
		sa = null;
		if (co != null) {
			// co.clear();
			co = null;
		}
		iContentType = IConstants.UNDEFINED;
		iDataType = IConstants.UNDEFINED;
		iRowCount = 0;
		iCursor = 0;
		cReused = null;
		it = null;
	}

	/**
	 * 
	 */
	public final void notifyDataUpdate() {
		reset();
		iRowCount = getRowCountInternal();
	}

	/**
	 * 
	 */
	final void updateDateTimeValues() throws ChartException {
		iRowCount = getRowCountInternal();
		Calendar cValue;
		CDateTime[] cdta = new CDateTime[size()];
		reset();
		int i = 0;
		while (hasNext()) {
			cValue = (Calendar) next();
			cdta[i++] = (cValue == null) ? null : new CDateTime(cValue);
		}

		// Fix bugzilla: 120919
		// !Don't alter original data here, use format specifier to get expected
		// values instead.
		//
		// final int iUnit = CDateTime.computeUnit( cdta ); //
		// CDateTime.getDifference(
		// new CDateTime( caMin
		// ), new
		// CDateTime( caMax ) );
		// for ( i = 0; i < cdta.length; i++ )
		// {
		// cdta[i].clearBelow( iUnit );
		// }

		clear();
		ca = cdta;
		iDataType = IConstants.DATE_TIME;
		iContentType = IConstants.NON_PRIMITIVE_ARRAY;
		iRowCount = ca.length;
	}

	/**
	 * @return
	 */
	public final int getDataType() {
		return iDataType;
	}

	/**
	 * @return current index
	 */
	public final int getIndex() {
		return isReverse ? iRowCount - 1 - iCursor++ : iCursor++;
	}

	/**
	 * Reverses the series categories.
	 * 
	 * @param bReverse
	 */
	public void reverse(boolean bReverse) {
		this.isReverse = bReverse;
		if (bReverse) {
			// Reset iterator since it's reverse
			resetIterator();
		}
	}

	private void resetIterator() {
		if (co != null) {
			if (isReverse) {
				// Always create a new list to keep original collection
				// immutable
				List list = new ArrayList(co.size());
				list.addAll(co);
				Collections.reverse(list);
				it = list.iterator();
			} else {
				it = co.iterator();
			}
		}
	}

	/**
	 * Skips the next iCount rows
	 * 
	 * @param iCount
	 * @return number of actually skipped rows
	 */
	public int skip(int iCount) {
		int iSkipped = 0;

		while (iCount-- > 0 && hasNext()) {
			next();
			iSkipped++;
		}

		return iSkipped;
	}
}
