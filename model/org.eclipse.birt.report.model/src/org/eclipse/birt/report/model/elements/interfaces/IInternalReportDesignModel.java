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

package org.eclipse.birt.report.model.elements.interfaces;

import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * The interface for report design element to store the constants.
 */

public interface IInternalReportDesignModel {

	/**
	 * Name of the refresh rate property.
	 */

	public static final String REFRESH_RATE_PROP = "refreshRate"; //$NON-NLS-1$

	/**
	 * Name of the method called at the start of the Factory after the initialize( )
	 * method and before opening the report document (if any).
	 */

	public static final String BEFORE_FACTORY_METHOD = "beforeFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called at the end of the Factory after closing the report
	 * document (if any). This is the last method called in the Factory.
	 */

	public static final String AFTER_FACTORY_METHOD = "afterFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called before starting a presentation time action.
	 */

	public static final String BEFORE_RENDER_METHOD = "beforeRender"; //$NON-NLS-1$

	/**
	 * Name of the method called after starting a presentation time action.
	 */

	public static final String AFTER_RENDER_METHOD = "afterRender"; //$NON-NLS-1$

	/**
	 * Name of the property to store report design icon/thumbnail file path.
	 */

	public static final String ICON_FILE_PROP = "iconFile"; //$NON-NLS-1$

	/**
	 * Name of the property to store the cheet sheet file name.
	 */

	public static final String CHEAT_SHEET_PROP = "cheatSheet"; //$NON-NLS-1$

	/**
	 * Name of the property to store the thumbnail image for the design or template.
	 */

	public static final String THUMBNAIL_PROP = "thumbnail"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the layout format of this report design.
	 */
	public static final String LAYOUT_PREFERENCE_PROP = "layoutPreference"; //$NON-NLS-1$

	/**
	 * Css file property
	 */

	public static final String CSSES_PROP = "cssStyleSheets";//$NON-NLS-1$

	/**
	 * Encoding mode for the thumbnail image.
	 */

	public static final String CHARSET = "8859_1"; //$NON-NLS-1$

	// Design slots
	// See constants defined in the module class.

	/**
	 * Identifier of the slot that holds styles.
	 */

	public static final int STYLE_SLOT = 0;

	/**
	 * Identifier of the body slot that contains the report sections.
	 */

	public static final int BODY_SLOT = 6;

	/**
	 * Identifier of the scratch pad slot.
	 */

	public static final int SCRATCH_PAD_SLOT = 7;

	/**
	 * Identifier of the template parameter definition slot.
	 */

	public static final int TEMPLATE_PARAMETER_DEFINITION_SLOT = 8;

	/**
	 * Identifier of the slot that holds a collections of cube elements.
	 */

	public static final int CUBE_SLOT = 9;

	/**
	 * Identifier of the slot that holds themes.
	 */

	public static final int THEMES_SLOT = 10;

	/**
	 * Number of slots in the report design element.
	 */

	public static final int SLOT_COUNT = 11;

	/**
	 * bidi_hcg: Bidi orientation property
	 */
	public static final String BIDI_ORIENTATION_PROP = "bidiLayoutOrientation"; //$NON-NLS-1$

	/**
	 * Name of the property that determines whether to enable the ACL feature for
	 * this report design or not.
	 */

	public static final String ENABLE_ACL_PROP = "enableACL"; //$NON-NLS-1$

	/**
	 * A Boolean property set on report that can act as container to other report
	 * elements. If set to true (the default), a report's ACL is automatically
	 * propagated to all its directly contained child elements and are added to
	 * their ACLs. This means that any user that is permitted to view the parent
	 * element is also allowed to view report element instances directly contained
	 * within the parent.
	 */

	public static final String CASCADE_ACL_PROP = IReportItemModel.CASCADE_ACL_PROP;

	/**
	 * A Java script expression which returns the ACL associated with the report
	 * instance in a String.
	 */

	public static final String ACL_EXPRESSION_PROP = IReportItemModel.ACL_EXPRESSION_PROP;

	/**
	 * Name of the property to keep image in report design display as same size at
	 * design time as at run time.
	 */
	public static final String IMAGE_DPI_PROP = "imageDPI"; //$NON-NLS-1$

	/**
	 * Name of the property contains list of Variable element defined for page level
	 * page break.
	 */
	public static final String PAGE_VARIABLES_PROP = "pageVariables"; //$NON-NLS-1$

	/**
	 * Name of locale property. It defines the locale used to generate/render the
	 * report
	 */
	public static final String LOCALE_PROP = "locale"; //$NON-NLS-1$

	/**
	 * Name of the property that defines some external metadata.
	 */
	public static final String EXTERNAL_METADATA_PROP = "externalMetadata"; //$NON-NLS-1$

	/**
	 * Name of the method on page start.
	 */
	public static final String ON_PAGE_START_METHOD = "onPageStart"; //$NON-NLS-1$

	/**
	 * Name of the method on page end.
	 */
	public static final String ON_PAGE_END_METHOD = "onPageEnd"; //$NON-NLS-1$

	/**
	 * Name of the data object variable property.
	 */
	public static final String DATA_OBJECTS_PROP = "dataObjects"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare property. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	public static final String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$

	/**
	 * Name of the client-initialize property. The design can load java script
	 * libraries by the property.
	 */
	public static final String CLIENT_INITIALIZE_METHOD = "clientInitialize"; //$NON-NLS-1$

	/**
	 * Name of the language property.
	 */
	public static final String LANGUAGE_PROP = "language"; //$NON-NLS-1$

}
