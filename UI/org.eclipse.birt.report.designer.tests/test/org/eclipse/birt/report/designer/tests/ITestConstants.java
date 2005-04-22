/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.tests;

/**
 * Defines all constants used by tests
 */

public interface ITestConstants
{

	String TEST_PLUGIN = "org.eclipse.birt.report.designer.tests";

	String PERSPECTIVE_ID = "org.eclipse.birt.report.designer.ui.ReportPerspective";
	String EDITOR_ID = "org.eclipse.birt.report.designer.ui.editors.ReportEditor";
	String OUTLINE_ID = "org.eclipse.ui.views.ContentOutline";
	String PALETTE_ID = "org.eclipse.gef.ui.palette_view";
	String ATTRIBUTES_VIEW_ID = "org.eclipse.birt.report.designer.ui.attributes.AttributeView";
	String DATA_EXPLORER_ID = "org.eclipse.birt.report.designer.ui.views.data.DataView";

	String PERSPECTIVE_NAME = "Report Design";
	String EDITOR_NAME = "Report Editor";
	String ATTRIBUTES_VIEW_NAME = "Property Edit View";
	String DATA_EXPLORER_NAME = "Data Explorer";
	String PLUGIN_PROVIDER = "Eclipse.org";

	String TEST_DESIGN_FILE = "test.rptdesign"; //$NON-NLS-1$
	
	String TEST_EXTENSION_NAME = "TestingBall"; //$NON-NLS-1$
}