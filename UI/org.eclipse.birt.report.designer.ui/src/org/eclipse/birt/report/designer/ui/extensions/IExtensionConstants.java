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

package org.eclipse.birt.report.designer.ui.extensions;

/**
 * Defines the constants used by extension framework
 */

public interface IExtensionConstants
{

	/** Extension Point Id */
	String EXTENSION_REPORT_ITEM_UI = "org.eclipse.birt.report.designer.ui.reportitemUI"; //$NON-NLS-1$
	String EXTENSION_MENU_BUILDERS = "org.eclipse.birt.report.designer.ui.menuBuilders"; //$NON-NLS-1$
	String EXTENSION_PROVIDER_FACTORIES = "org.eclipse.birt.report.designer.ui.providerFactories"; //$NON-NLS-1$

	/** Element name */
	String ELEMENT_REPORT_ITEM_FIGURE_UI = "reportItemFigureUI"; //$NON-NLS-1$
	String ELEMENT_REPORT_ITEM_IMAGE_UI = "reportItemImageUI"; //$NON-NLS-1$
	String ELEMENT_REPORT_ITEM_LABEL_UI = "reportItemLabelUI"; //$NON-NLS-1$
	String ELEMENT_REPORT_ITEM_BUILDER_UI = "reportItemBuilderUI"; //$NON-NLS-1$
	String REPORT_ITEM_PROPERTY_EDIT_UI = "reportItemPropertyEditUI"; //$NON-NLS-1$
	String ELEMENT_PALETTE = "palette"; //$NON-NLS-1$
	String ELEMENT_EDITOR = "editor"; //$NON-NLS-1$
	String ELEMENT_OUTLINE = "outline"; //$NON-NLS-1$
	String ELEMENT_BUILDER = "builder";//$NON-NLS-1$
	String ELEMENT_DESCRIPTION = "description"; //$NON-NLS-1$
	String ELEMENT_PROPERTYEDIT = "propertyPage";//$NON-NLS-1$
	String ELEMENT_MODEL = "model";//$NON-NLS-1$

	String ELEMENT_MENU_BUILDER = "menuBuilder"; //$NON-NLS-1$
	String ELEMENT_PROVIDER_FACTORY = "providerFactory"; //$NON-NLS-1$

	/** Attributes name */
	String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$
	String ATTRIBUTE_EXTENSION_NAME = "extensionName"; //$NON-NLS-1$
	String ATTRIBUTE_NAME = "name"; //$NON-NLS-1$
	String ATTRIBUTE_ICON = "icon"; //$NON-NLS-1$
	String ATTRIBUTE_ICON_LARGE = "largeIcon"; //$NON-NLS-1$
	String ATTRIBUTE_PALETTE_CATEGORY = "category"; //$NON-NLS-1$
	String ATTRIBUTE_PALETTE_CATEGORY_DISPLAYNAME = "categoryDisplayName"; //$NON-NLS-1$
	String ATTRIBUTE_EDITOR_CAN_RESIZE = "canResize"; //$NON-NLS-1$
	String ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER = "showInDesigner"; //$NON-NLS-1$
	String ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER_BY_PREFERENCE = "showInDesignerByPreference"; //$NON-NLS-1$
	String ATTRIBUTE_EDITOR_SHOW_IN_MASTERPAGE = "showInMasterPage"; //$NON-NLS-1$
	String ATTRIBUTE_EDITOR_MENU_LABEL = "menuLabel"; //$NON-NLS-1$

	String ATTRIBUTE_ELEMENT_NAME = "elementName"; //$NON-NLS-1$

	/** Attribute keys */
	String ATTRIBUTE_KEY_PALETTE_ICON = "paletteIcon"; //$NON-NLS-1$
	String ATTRIBUTE_KEY_PALETTE_ICON_LARGE = "paletteIconLarge"; //$NON-NLS-1$
	String ATTRIBUTE_KEY_OUTLINE_ICON = "outlineIcon"; //$NON-NLS-1$
	String ATTRIBUTE_KEY_DESCRIPTION = "description"; //$NON-NLS-1$

}
