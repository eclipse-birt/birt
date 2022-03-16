/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.Map;

import org.eclipse.birt.report.engine.api.script.instance.IActionInstance;
import org.eclipse.birt.report.engine.api.script.instance.IDrillThroughInstance;
import org.eclipse.birt.report.engine.content.IDrillThroughAction;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.DrillThroughAction;

/**
 *
 */

public class ActionInstance implements IActionInstance {

	IHyperlinkAction hyperlink;

	ActionInstance(IHyperlinkAction hyperlink) {
		this.hyperlink = hyperlink;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#getType()
	 */
	@Override
	public int getType() {
		return hyperlink.getType();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#getBookmark()
	 */
	@Override
	public String getBookmark() {
		if (getType() == org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_BOOKMARK) {
			return hyperlink.getBookmark();
		}
		throw new RuntimeException("The action type is not bookmark.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#setBookmark(
	 * java.lang.String )
	 */
	@Override
	public void setBookmark(String bookmark) {
		if (bookmark != null && !bookmark.equals("")) {
			hyperlink.setBookmark(bookmark);
		} else {
			throw new IllegalArgumentException("Bookmark can not be set to NULL or empty.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#getHyperlink()
	 */
	@Override
	public String getHyperlink() {
		if (getType() == org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_HYPERLINK) {
			return hyperlink.getHyperlink();
		}
		throw new RuntimeException("The action type is not hyperlink.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#setHyperlink(
	 * java.lang.String, java.lang.String )
	 */
	@Override
	public void setHyperlink(String hyperlink, String target) {
		if (hyperlink != null && !hyperlink.equals("")) {
			this.hyperlink.setHyperlink(hyperlink, target);
		} else {
			throw new IllegalArgumentException("Hyperlink can not be set to NULL or empty.");
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#getTargetWindow()
	 */
	@Override
	public String getTargetWindow() {
		if (getType() == org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_HYPERLINK
				|| getType() == org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_HYPERLINK) {
			return hyperlink.getTargetWindow();
		}
		throw new RuntimeException("The action type is not hyperlink or drillThrough.");
	}

	IDrillThroughInstance drillThroughInstance;

	/**
	 * @deprecated (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#setDrillThrough()
	 */
	@Deprecated
	@Override
	public IDrillThroughInstance createDrillThrough(String bookmark, boolean isBookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target, String format) {
		return createDrillThrough(bookmark, isBookmark, reportName, parameterBindings, searchCriteria, target, format,
				null);
	}

	/**
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#setDrillThrough()
	 */
	@Override
	public IDrillThroughInstance createDrillThrough(String bookmark, boolean isBookmark, String reportName,
			Map parameterBindings, Map searchCriteria, String target, String format, String targetFileType) {
		IDrillThroughAction drillThrough = new DrillThroughAction(bookmark, isBookmark, reportName, parameterBindings,
				searchCriteria, target, format, targetFileType);
		return new DrillThroughInstance(drillThrough);
	}

	@Override
	public IDrillThroughInstance createDrillThrough() {
		IDrillThroughAction drillThrough = new DrillThroughAction();
		return new DrillThroughInstance(drillThrough);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#getDrillThrough(
	 * )
	 */
	@Override
	public IDrillThroughInstance getDrillThrough() {
		if (getType() == org.eclipse.birt.report.engine.content.IHyperlinkAction.ACTION_DRILLTHROUGH) {
			IDrillThroughAction drillThrough = hyperlink.getDrillThrough();
			if (drillThrough != null) {
				if (drillThroughInstance == null) {
					drillThroughInstance = new DrillThroughInstance(drillThrough);
				}
			}
			return drillThroughInstance;
		}
		throw new RuntimeException("The action type is not drillThrough.");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.content.IActionInstance#setDrillThrough(
	 * )
	 */
	@Override
	public void setDrillThrough(IDrillThroughInstance drillThrough) {
		if (drillThrough != null) {
			if (drillThrough instanceof DrillThroughInstance) {
				hyperlink.setDrillThrough(((DrillThroughInstance) drillThrough).getDrillThroughAction());
				this.drillThroughInstance = drillThrough;
			} else {
				throw new IllegalArgumentException("IDrillThroughInstance is illegal.");
			}
		} else {
			throw new IllegalArgumentException("IDrillThroughInstance can not be set to NULL.");
		}
	}

	/**
	 * @return this.hyperlink, HyperlinkAction
	 */
	IHyperlinkAction getHyperlinkAction() {
		return hyperlink;
	}

	@Override
	public void setTooltip(String tooltip) {
		hyperlink.setTooltip(tooltip);
	}

	@Override
	public String getTooltip() {

		return hyperlink.getTooltip();

	}
}
