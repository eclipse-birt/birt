/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Represents a the design of a Grid in the scripting environment
 */
public interface IGrid extends IReportItem {

	/**
	 * Returns the number of columns in the Grid. The number is defined as the sum
	 * of columns described in the "column" slot.
	 *
	 * @return the number of columns in the grid.
	 */
	int getColumnCount();

	/**
	 * Gets the summary of this grid.
	 *
	 * @return the summary.
	 */
	String getSummary();

	/**
	 * Sets the summary of this grid.
	 *
	 * @param summary the summary
	 * @throws ScriptException
	 */
	void setSummary(String summary) throws ScriptException;

	/**
	 * Returns the caption text of this grid.
	 *
	 * @return the caption text
	 */

	String getCaption();

	/**
	 * Sets the caption text of this grid.
	 *
	 * @param caption the caption text
	 * @throws ScriptException
	 */

	void setCaption(String caption) throws ScriptException;

	/**
	 * Returns the resource key of the caption.
	 *
	 * @return the resource key of the caption
	 */

	String getCaptionKey();

	/**
	 * Sets the resource key of the caption.
	 *
	 * @param captionKey the resource key of the caption
	 * @throws ScriptException
	 */

	void setCaptionKey(String captionKey) throws ScriptException;
}
