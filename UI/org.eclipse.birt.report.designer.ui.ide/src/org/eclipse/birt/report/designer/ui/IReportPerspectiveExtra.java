/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui;

/**
 * IReportPerspectiveExtra
 */
public interface IReportPerspectiveExtra {

	int LAYOUT_TOP_LEFT = 1;

	int LAYOUT_BOTTOM_LEFT = 2;

	int LAYOUT_BOTTOM_RIGHT = 3;

	boolean obsoleteNewWizardShortcut(String id);

	boolean obsoleteShowViewShortcut(String id);

	boolean obsoleteLayoutView(int layoutPos, String id);

	/**
	 * Defines the extra new wizard shortcuts needed be added into the perspective
	 * 
	 * @return the wizard ids
	 */
	String[] getExtraNewWizardShortcut();

	/**
	 * Defines the extra show view shortcuts needed be added into the perspective
	 * 
	 * @return the view ids
	 */
	String[] getExtraShowViewShortcut();

	/**
	 * Defines the extra views needed be added into the perspective
	 * 
	 * @param layoutPos one of
	 *                  LAYOUT_TOP_LEFT,LAYOUT_BOTTOM_LEFT,LAYOUT_BOTTOM_RIGHT
	 * @return the view ids
	 */
	String[] getExtraLayoutView(int layoutPos);

	/**
	 * Defines the extra view placeholders needed be added into the perspective
	 * 
	 * @param layoutPos one of
	 *                  LAYOUT_TOP_LEFT,LAYOUT_BOTTOM_LEFT,LAYOUT_BOTTOM_RIGHT
	 * @return the view ids
	 */
	String[] getExtraLayoutPlaceholder(int layoutPos);

}
