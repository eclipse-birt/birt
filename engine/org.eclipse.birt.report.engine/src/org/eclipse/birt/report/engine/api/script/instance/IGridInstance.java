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
package org.eclipse.birt.report.engine.api.script.instance;

public interface IGridInstance extends IReportItemInstance {
	/**
	 * Get the caption.
	 *
	 */
	String getCaption();

	/**
	 * Set the caption
	 *
	 */
	void setCaption(String caption);

	/**
	 * Get the caption key
	 */
	String getCaptionKey();

	/**
	 * Set the caption key
	 */
	void setCaptionKey(String captionKey);

	/**
	 * Get the summary.
	 *
	 */
	String getSummary();

	/**
	 * Set the summary
	 *
	 */
	void setSummary(String summary);

}
