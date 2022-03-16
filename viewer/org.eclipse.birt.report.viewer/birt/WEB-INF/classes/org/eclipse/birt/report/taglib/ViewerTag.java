/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.taglib;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.taglib.util.BirtTagUtil;

/**
 * The viewer tag is to specify how to import and control BIRT Report Viewer
 * into JSP page. Use Ajax to preview report content. This tag needs browser
 * iframe support.
 *
 */
public class ViewerTag extends AbstractViewerTag {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8856230384196724409L;

	/**
	 * process tag function
	 *
	 * @see org.eclipse.birt.report.taglib.AbstractBaseTag#__process()
	 */
	@Override
	public void __process() throws Exception {
		// URI for viewer
		String uri = viewer.createURI(null, null);
		if (viewer.isHostPage()) {
			__handleIFrame(uri, null);
		} else {
			__handleIFrame(uri, viewer.getId());
		}
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		if (pattern == null || !IBirtConstants.VIEWER_RUN.equalsIgnoreCase(pattern)) {
			pattern = IBirtConstants.VIEWER_FRAMESET;
		}

		viewer.setPattern(pattern);
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		viewer.setTitle(title);
	}

	/**
	 * @param forceOverwriteDocument the forceOverwriteDocument to set
	 */
	public void setForceOverwriteDocument(String forceOverwriteDocument) {
		viewer.setForceOverwriteDocument(BirtTagUtil.convertBooleanValue(forceOverwriteDocument));
	}

	/**
	 * @param showTitle the showTitle to set
	 */
	public void setShowTitle(String showTitle) {
		viewer.setShowTitle(BirtTagUtil.convertBooleanValue(showTitle));
	}

	/**
	 * @param showToolBar the showToolBar to set
	 */
	public void setShowToolBar(String showToolBar) {
		viewer.setShowToolBar(BirtTagUtil.convertBooleanValue(showToolBar));
	}

	/**
	 * @param showNavigationBar the showNavigationBar to set
	 */
	public void setShowNavigationBar(String showNavigationBar) {
		viewer.setShowNavigationBar(BirtTagUtil.convertBooleanValue(showNavigationBar));
	}
}
