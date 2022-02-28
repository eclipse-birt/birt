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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IGrid;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Grid extends ReportItem implements IGrid {

	public Grid(GridHandle grid) {
		super(grid);
	}

	public Grid(org.eclipse.birt.report.model.api.simpleapi.IGrid gridImpl) {
		super(gridImpl);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getColumnCount()
	 */

	@Override
	public int getColumnCount() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).getColumnCount();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getSummary()
	 */
	@Override
	public String getSummary() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).getSummary();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGrid#setSummary(java.lang.
	 * String)
	 */
	@Override
	public void setSummary(String summary) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).setSummary(summary);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getCaption()
	 */
	@Override
	public String getCaption() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).getCaption();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGrid#setCaption(java.lang.
	 * String)
	 */
	@Override
	public void setCaption(String caption) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).setCaption(caption);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IGrid#getCaptionKey()
	 */
	@Override
	public String getCaptionKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).getCaptionKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IGrid#setCaptionKey(java.
	 * lang.String)
	 */
	@Override
	public void setCaptionKey(String captionKey) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IGrid) designElementImpl).setCaptionKey(captionKey);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
