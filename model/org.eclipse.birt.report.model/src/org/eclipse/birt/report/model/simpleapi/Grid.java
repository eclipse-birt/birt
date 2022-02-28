/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IGrid;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;

public class Grid extends ReportItem implements IGrid {

	public Grid(GridHandle grid) {
		super(grid);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getColumnCount()
	 */

	@Override
	public int getColumnCount() {
		return ((GridHandle) handle).getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#getSummary()
	 */
	@Override
	public String getSummary() {
		return ((GridHandle) handle).getSummary();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#setSummary(java.lang
	 * .String)
	 */
	@Override
	public void setSummary(String summary) throws SemanticException {
		setProperty(IGridItemModel.SUMMARY_PROP, summary);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#getCaption()
	 */
	@Override
	public String getCaption() {
		return ((GridHandle) handle).getCaption();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#setCaption(java.lang
	 * .String)
	 */
	@Override
	public void setCaption(String caption) throws SemanticException {
		setProperty(IGridItemModel.CAPTION_PROP, caption);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#getCaptionKey()
	 */
	@Override
	public String getCaptionKey() {
		return ((GridHandle) handle).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IGrid#setCaptionKey(java.
	 * lang.String)
	 */
	@Override
	public void setCaptionKey(String captionKey) throws SemanticException {
		setProperty(IGridItemModel.CAPTION_KEY_PROP, captionKey);

	}

}
