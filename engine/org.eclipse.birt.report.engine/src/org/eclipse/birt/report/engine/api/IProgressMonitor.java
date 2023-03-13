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

package org.eclipse.birt.report.engine.api;

/**
 * This interface is to trace the progress of BIRT. As BIRT generates a report,
 * some events are notified of the current status. If a user defined monitor
 * (IProgressMonitor) is set (by IEngineTask.setProgressMonitor()), the monitor
 * is triggered.
 *
 */
public interface IProgressMonitor {

	/**
	 * the BIRT task starts event
	 */
	int START_TASK = 1;

	/**
	 * the BIRT task ends event
	 */
	int END_TASK = 2;

	/**
	 * start to generate a page
	 */
	int START_PAGE = 3;

	/**
	 * ending of a page
	 */
	int END_PAGE = 4;

	/**
	 * start to execute a DB query
	 */
	int START_QUERY = 5;

	/**
	 * ending of DB query execution
	 */
	int END_QUERY = 6;

	/**
	 * reading data from DB
	 */
	int FETCH_ROW = 7;

	/**
	 * The 'type' specifies this progress event and the 'value' specifies a proper
	 * value to the event. It's like:
	 * <table>
	 * <tr>
	 * <th align=left>event type
	 * <th align=left>event value
	 * <tr>
	 * <td>START_TASK
	 * <td>task type, see {@link IEngineTask}
	 * <tr>
	 * <td>END_TASK
	 * <td>task type, see {@link IEngineTask}
	 * <tr>
	 * <td>START_PAGE
	 * <td>page number
	 * <tr>
	 * <td>END_PAGE
	 * <td>page number
	 * <tr>
	 * <td>START_QUERY
	 * <td>the ID of the element on which a query is started
	 * <tr>
	 * <td>END_QUERY
	 * <td>the ID of the element on which a query is ended
	 * <tr>
	 * <td>FETCH_ROW
	 * <td>the row index
	 * </table>
	 */
	void onProgress(int type, int value);

}
