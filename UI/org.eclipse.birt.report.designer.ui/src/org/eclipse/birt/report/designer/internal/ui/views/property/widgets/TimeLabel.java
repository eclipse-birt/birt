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

import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

/**
 * The Spinner componnet base on SWT,show time
 */
public class TimeLabel extends Label {

	private long time;

	private String formatType = TimeFormat.DATA_ALL;

	private String id = TimeZone.getDefault().getID();

	/**
	 * Constructs a new instance of this class given its parent and a style value
	 * describing its behavior and appearance.
	 *
	 * @param parent
	 * @param style
	 */
	public TimeLabel(Composite parent, int style) {
		this(parent, style, TimeFormat.DATA_ALL);
	}

	/**
	 * Constructs a new instance of this class given its parent ,a style and type,
	 * type is the show the time format("yyyy-MM-dd",etc)
	 *
	 * @param parent
	 * @param style
	 * @param type
	 */
	public TimeLabel(Composite parent, int style, String type) {
		super(parent, style);
		if (!TimeFormat.getDefaultFormat().getSupportList().contains(type)) {
			throw new Error("Not support this Format"); //$NON-NLS-1$
		}
		this.formatType = type;
		setDate(new Date(System.currentTimeMillis()));
		// logger.info("gao222" + (new
		// Date(System.currentTimeMillis())).getYear());
		String text = getShowText(type);
		setText(text);
	}

	/**
	 * set date
	 *
	 * @param date
	 */
	public void setDate(Date date) {
		setTime(date.getTime());
	}

	/**
	 * Fetch the time
	 *
	 * @return time
	 */
	public long getTime() {
		return time;

	}

	/**
	 * Sets thr time
	 *
	 * @param time
	 */
	public void setTime(long time) {
		this.time = time;
		String text = getShowText(getFormatType());
		setText(text);
	}

	/**
	 * gets the show format
	 *
	 * @return the format type
	 */
	public String getFormatType() {
		return formatType;
	}

	/**
	 * sets the show format
	 *
	 * @param type
	 */
	public void setFormatType(String type) {
		this.formatType = type;
		String text = getShowText(formatType);
		setText(text);
	}

	/**
	 * when the TimeZone is changed ,update the time
	 *
	 * @param oldID
	 * @param newID
	 * @return true
	 */
	public boolean updateTimeForTimeZone(String oldID, String newID) {

		TimeZone oldZone = TimeZone.getTimeZone(oldID);
		TimeZone newZone = TimeZone.getTimeZone(newID);

		int oldOff = oldZone.getRawOffset();
		int newOff = newZone.getRawOffset();

		time = time + (newOff - oldOff);
		String text = getShowText(getFormatType());
		setText(text);
		return true;
	}

	private String getShowText(String type) {
		// StringBuffer retValue = new StringBuffer( );

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(id));
		calendar.setTimeInMillis(time);

		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		String result = formatter.format(calendar.getTime());
		return result;

	}

	/**
	 * Sets the TimeZone ID
	 *
	 * @param newID
	 */
	public void setTimeZoneID(String newID) {
		updateTimeForTimeZone(id, newID);
		this.id = newID;
	}

	/**
	 * Gets the TimeZone ID value
	 *
	 * @return id
	 */
	public String getTimeZone() {
		return id;
	}

	/**
	 * Sets the infomation. Infomation may be come from TimeOptionDialog
	 *
	 * @param dialogInfo
	 * @return true
	 */
	public boolean setInfo(DialogInfo dialogInfo) {
		TimeDialogInfo info = (TimeDialogInfo) dialogInfo;

		if (!getTimeZone().equals(info.getTimeZoneID()) && info.getTimeZoneID() != null) {
			setTimeZoneID(info.getTimeZoneID());
		}
		if (!info.getFormat().equals(getFormatType())) {
			setFormatType(info.getFormat());
		}
		if (info.getTime() != getTime()) {
			setTime(info.getTime());
		}
		return true;
	}

	/**
	 * Gets the infomation
	 *
	 * @return infomation
	 */
	public DialogInfo getInfo() {
		TimeDialogInfo info = new TimeDialogInfo();
		info.setTime(getTime());
		info.setTimeZoneID(getTimeZone());
		info.setFormat(formatType);
		return info;
	}

	/*
	 * This class don't pass th check,So this method must be overhide. (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Widget#checkSubclass()
	 */
	@Override
	protected void checkSubclass() {

	}

	// this class need add some funtions :
	// 1:load
	// 2: save
	// 3:i18n
	// 4:help
	// 5:need define some interface
	// maybe we need some dialog pool
}
