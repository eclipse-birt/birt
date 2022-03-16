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

package org.eclipse.birt.report.designer.internal.ui.views.property.widgets;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.ibm.icu.util.Calendar;

/**
 * The Spinner component base on SWT,Fetch and setting the day value
 */
public class SpinnerTable extends Composite {

	private Table table = null;

	private TableCursor cursor = null;

	private static final int DAY_WEEK_COUNT = 7;

	private static final int ROW_COUNT = 7;

	private Calendar cale = null;

	private ArrayList listenerList = new ArrayList();

	private static final String SUN = Messages.getString("Commom.ShortDateTime.Sun"); //$NON-NLS-1$

	private static final String MON = Messages.getString("Commom.ShortDateTime.Mon"); //$NON-NLS-1$

	private static final String TUE = Messages.getString("Commom.ShortDateTime.Tue"); //$NON-NLS-1$

	private static final String WED = Messages.getString("Commom.ShortDateTime.Wed"); //$NON-NLS-1$

	private static final String THU = Messages.getString("Commom.ShortDateTime.Thu"); //$NON-NLS-1$

	private static final String FRI = Messages.getString("Commom.ShortDateTime.Fri"); //$NON-NLS-1$

	private static final String SAT = Messages.getString("Commom.ShortDateTime.Sat"); //$NON-NLS-1$

	/**
	 * sets the calendar
	 *
	 * @param calendar
	 */
	public void setCalendar(Calendar calendar) {
		this.cale = (Calendar) calendar.clone();
		Calendar tempcalendar = getOriCalendar(calendar);
		int dayCount = tempcalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int moreDayCount = tempcalendar.get(Calendar.DAY_OF_WEEK) - 1;

		// int count = table.getItemCount();

		for (int i = 0; i < ROW_COUNT; i++) {
			TableItem item = table.getItem(i);
			if (i == 0) {
				continue;
			}

			String[] values = new String[7];
			for (int j = 0; j < DAY_WEEK_COUNT; j++) {
				int index = (i - 1) * DAY_WEEK_COUNT + j;
				int dayIndex = index - moreDayCount + 1;
				if (index < moreDayCount || dayIndex > dayCount) {
					values[j] = ""; //$NON-NLS-1$
				} else {
					values[j] = String.valueOf(dayIndex);
				}
			}
			item.setText(values);

		}

		TableColumn[] cols = table.getColumns();
		int size = cols.length;
		for (int i = 0; i < size; i++) {
			cols[i].pack();
		}
		table.pack();

		setOriValue();
	}

	/**
	 * Constructs a new instance of this class given its parent and a style
	 *
	 * @param composite
	 * @param style
	 */
	public SpinnerTable(Composite composite, int style) {
		this(composite, style, Calendar.getInstance());
	}

	/**
	 * Constructs a new instance of this class given its parent , a style and a
	 * calendar
	 *
	 * @param composite
	 * @param style
	 * @param calendar
	 */
	public SpinnerTable(Composite composite, int style, Calendar calendar) {
		super(composite, style);
		table = new TimeTable(this, SWT.HIDE_SELECTION | style | SWT.BORDER);

		createColumn(table);

		cursor = new TimeTableCursor(table, SWT.NONE);
		cursor.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		cursor.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		cursor.addSelectionListener(new SelectionAdapter() {

			// when the TableEditor is over a cell, select the corresponding
			// row in
			// the table
			@Override
			public void widgetSelected(SelectionEvent e) {

				TableItem row = cursor.getRow();
				String value = row.getText(cursor.getColumn());
				int strValue = 0;
				try {
					strValue = Integer.parseInt(value);
				} catch (Exception ee) {

					setOriValue();
					return;
				}

				int oldValue = cale.get(Calendar.DAY_OF_MONTH);
				cale.set(Calendar.DAY_OF_MONTH, strValue);

				// source I think is a Object
				firePropertyListener(new PropertyChangeEvent(new Object(), "daychange", //$NON-NLS-1$
						Integer.valueOf(oldValue), Integer.valueOf(strValue)));

			}
		});

		createItems();
		setCalendar(calendar);

	}

	private void createColumn(Table table) {
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);
		new TimeTableColumn(table, SWT.NONE);

	}

	private Calendar getOriCalendar(Calendar calendar) {
		Calendar retValue = (Calendar) calendar.clone();
		retValue.set(Calendar.DAY_OF_MONTH, 1);
		return retValue;
	}

	private void setOriValue() {
		Calendar tempcalendar = getOriCalendar(cale);
		// int dayCount = tempcalendar.getActualMaximum( Calendar.DAY_OF_MONTH
		// );
		int moreDayCount = tempcalendar.get(Calendar.DAY_OF_WEEK) - 1;

		int selectDay = cale.get(Calendar.DAY_OF_MONTH) + moreDayCount + DAY_WEEK_COUNT;

		int week = selectDay - ((selectDay / DAY_WEEK_COUNT) * DAY_WEEK_COUNT) - 1;
		int row = selectDay / DAY_WEEK_COUNT;
		if (week == -1) {
			week = 6;
			row = row - 1;
		}

		cursor.setSelection(row, week);
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when
	 * the year value change, by sending it one of the messages defined in the
	 * IPropertyChangeListener interface.
	 *
	 * @param listener
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.add(listener);

	}

	/**
	 * Removes the listener from the collection of listeners who will be notified
	 * when the year value change
	 *
	 * @param listener
	 */
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		listenerList.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Widget#checkWidget()
	 */
	@Override
	protected void checkWidget() {

	}

	/**
	 * Fire the event when the year value change
	 *
	 * @param e
	 */
	public void firePropertyListener(PropertyChangeEvent e) {
		int size = listenerList.size();
		for (int i = 0; i < size; i++) {
			IPropertyChangeListener listener = (IPropertyChangeListener) listenerList.get(i);
			listener.propertyChange(e);
		}
	}

	private void createItems() {
		for (int i = 0; i < ROW_COUNT; i++) {
			TableItem item = new TimeTableItem(table, SWT.NONE);
			if (i == 0) {
				item.setText(new String[] { SUN, MON, TUE, WED, THU, FRI, SAT });
				item.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
				item.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			}

		}
	}

	/**
	 * Gets the day value
	 *
	 * @return day value
	 */
	public int getDay() {
		return cale.get(Calendar.DAY_OF_MONTH);
	}
	// dispose not finish
}

class TimeTable extends Table {

	public TimeTable(Composite composite, int style) {
		super(composite, style);
	}

	@Override
	protected void checkWidget() {

	}

	@Override
	protected void checkSubclass() {

	}
}

class TimeTableItem extends TableItem {

	public TimeTableItem(Table parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkWidget() {

	}

	@Override
	protected void checkSubclass() {

	}
}

class TimeTableColumn extends TableColumn {

	public TimeTableColumn(Table parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkWidget() {

	}

	@Override
	protected void checkSubclass() {

	}
}

class TimeTableCursor extends TableCursor {

	public TimeTableCursor(Table parent, int style) {
		super(parent, style);
	}

	@Override
	protected void checkWidget() {

	}

	@Override
	protected void checkSubclass() {

	}
}
