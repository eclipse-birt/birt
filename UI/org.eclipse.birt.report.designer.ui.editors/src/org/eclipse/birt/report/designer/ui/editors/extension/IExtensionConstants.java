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

package org.eclipse.birt.report.designer.ui.editors.extension;

/**
 * Defines the constants used by extension framework
 */

public interface IExtensionConstants {

	/** Extension Point Id */
	String EXTENSION_MULTIPAGE_EDITOR_CONTRIBUTOR = "org.eclipse.birt.report.designer.ui.editors.multiPageEditorContributor"; //$NON-NLS-1$

	/** Element name */
	String ELEMENT_FORM_PAGE = "formPage"; //$NON-NLS-1$
	/** Attributes name */

	String ATTRIBUTE_NAME = "name"; //$NON-NLS-1$

	String ATTRIBUTE_TARGET_EDITOR_ID = "targetEditorId"; //$NON-NLS-1$

	String ATTRIBUTE_ID = "id"; //$NON-NLS-1$
	String ATTRIBUTE_DISPLAY_NAME = "displayName"; //$NON-NLS-1$
	String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$
	String ATTRIBUTE_ICON = "icon"; //$NON-NLS-1$
	String ATTRIBUTE_VISIBLE = "visible"; //$NON-NLS-1$
	String ATTRIBUTE_POSITION = "position"; //$NON-NLS-1$
	String ATTRIBUTE_RELATIVE = "relative"; //$NON-NLS-1$
	String ATTRIBUTE_PAGE_ACTION = "pageAction"; //$NON-NLS-1$
	String ATTRIBUTE_ACTION_BAR_CONTRIBUTOR = "actionBarContributor"; //$NON-NLS-1$
	String ATTRIBUTE_PRIORITY = "priority"; //$NON-NLS-1$

}
