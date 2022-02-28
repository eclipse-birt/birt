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
import java.util.List;

/**
 * Instances of this class represent a non-selectable
 */
public class TimeFormat {

	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss"; //$NON-NLS-1$

	public static final String DATA_ALL = "yyyy-MM-dd"; //$NON-NLS-1$

	public static final String TIME_ALL = "HH:mm:ss"; //$NON-NLS-1$

	public static final String SHORT_TIME = "yy-MM-dd"; //$NON-NLS-1$

	public static final String SHORT_TIMEUSA = "dd/MM/yy"; //$NON-NLS-1$

	private static final TimeFormat timeForamt = new TimeFormat();

	private ArrayList list = null;

	/**
	 * Constructs a new instance of this class,System has one instance
	 */
	private TimeFormat() {
		list = new ArrayList();
		list.add(DATE_TIME);
		list.add(DATA_ALL);
		list.add(TIME_ALL);
		list.add(SHORT_TIME);
		list.add(SHORT_TIMEUSA);
	}

	/**
	 * Gets the only one instance
	 *
	 * @return the default time format
	 */
	public static TimeFormat getDefaultFormat() {
		// logger.info("testaaaa==" + (timeForamt == null));
		return timeForamt;
	}

	/**
	 * Gets the format list
	 *
	 * @return the format list
	 */
	public List getSupportList() {
		return list;
	}

}
