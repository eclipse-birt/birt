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
package org.eclipse.birt.report.model.elements.interfaces;

public interface IInternalReportItemModel {

	/**
	 * Name of the x position property.
	 */

	public static final String X_PROP = "x"; //$NON-NLS-1$
	/**
	 * Name of the y position property.
	 */

	public static final String Y_PROP = "y"; //$NON-NLS-1$
	/**
	 * Name of the height dimension property.
	 */

	public static final String HEIGHT_PROP = "height"; //$NON-NLS-1$
	/**
	 * Name of the width position property.
	 */

	public static final String WIDTH_PROP = "width"; //$NON-NLS-1$
	/**
	 * Name of the data set property. This references a data set within the report.
	 * Provides the scope for items that reference database data.
	 */

	public static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$
	/**
	 * Name of the property that references a cube element within the report.
	 */
	public static final String CUBE_PROP = "cube"; //$NON-NLS-1$
	/**
	 * Name of the bookmark property. The bookmark is the target of hyperlinks
	 * within the report.
	 */

	public static final String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$
	/**
	 * Name of the display name property for bookmark
	 */
	public static final String BOOKMARK_DISPLAY_NAME_PROP = "bookmarkDisplayName"; //$NON-NLS-1$
	/**
	 * Name of the TOC entry expression property.
	 */

	public static final String TOC_PROP = "toc"; //$NON-NLS-1$
	/**
	 * Name of the visibility property.
	 */

	public static final String VISIBILITY_PROP = "visibility"; //$NON-NLS-1$
	/**
	 * Name of the on-create property. It is for a script executed when the element
	 * is created in the Factory. Called after the item is created, but before the
	 * item is saved to the report document file.
	 */

	public static final String ON_CREATE_METHOD = "onCreate"; //$NON-NLS-1$
	/**
	 * Name of the on-render property. It is for a script Executed when the element
	 * is prepared for rendering in the Presentation engine.
	 */

	public static final String ON_RENDER_METHOD = "onRender"; //$NON-NLS-1$
	/**
	 * Name of the on-prepare property. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	public static final String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$
	/**
	 * The property name of the data set parameter binding elements that bind input
	 * parameters to expressions.
	 */

	public static final String PARAM_BINDINGS_PROP = "paramBindings"; //$NON-NLS-1$
	/**
	 * Name of the on-pageBreak property. It is for a script executed when the
	 * element is prepared for page breaking in the Presentation engine.
	 */

	public static final String ON_PAGE_BREAK_METHOD = "onPageBreak"; //$NON-NLS-1$
	/**
	 * The property name of the bound columns that bind the report element with the
	 * data set columns.
	 */

	public static final String BOUND_DATA_COLUMNS_PROP = "boundDataColumns"; //$NON-NLS-1$
	/**
	 * The property name of the reference of bound columns that bind the report
	 * element with the data set columns of another report item.
	 */

	public static final String DATA_BINDING_REF_PROP = "dataBindingRef"; //$NON-NLS-1$
	/**
	 * Name of the z-depth property.
	 */

	public static final String Z_INDEX_PROP = "zIndex"; //$NON-NLS-1$
	/**
	 * Name of the multiple views property.
	 */

	public static final String MULTI_VIEWS_PROP = "multiViews"; //$NON-NLS-1$
	/**
	 * A Boolean property set on report elements that can act as container to other
	 * report elements. If set to true (the default), a report element's ACL is
	 * automatically propagated to all its directly contained child elements and are
	 * added to their ACLs. This means that any user that is permitted to view the
	 * parent element is also allowed to view report element instances directly
	 * contained within the parent.
	 */

	public static final String CASCADE_ACL_PROP = "cascadeACL"; //$NON-NLS-1$
	/**
	 * A Java script expression which returns the ACL associated with the report
	 * element instance in a String.
	 */

	public static final String ACL_EXPRESSION_PROP = "ACLExpression"; //$NON-NLS-1$
	/**
	 * An option to hide specific data sets from the extractions box.
	 */
	public static final String ALLOW_EXPORT_PROP = "allowExport"; //$NON-NLS-1$
	/**
	 * Name of the isPushdown property. It will be used by advanced user to control
	 * Data Engine behavior against query execution.
	 */
	public static final String PUSH_DOWN_PROP = "pushDown"; //$NON-NLS-1$
	/**
	 * Name of the property that defines theme name applied for this element.
	 */
	public static final String THEME_PROP = ISupportThemeElementConstants.THEME_PROP;

	/**
	 * Name of the tag type property.
	 */
	public static final String TAG_TYPE_PROP = "tagType"; //$NON-NLS-1$

	/**
	 * Name of the language property.
	 */
	public static final String LANGUAGE_PROP = "language"; //$NON-NLS-1$

	/**
	 * Name of the altText property.
	 */
	public static final String ALTTEXT_PROP = "altText"; //$NON-NLS-1$

	/**
	 * Name of the altText key property.
	 */
	public static final String ALTTEXT_KEY_PROP = "altTextID"; //$NON-NLS-1$

	/**
	 * Name of the order property.
	 */
	public static final String ORDER_PROP = "order"; //$NON-NLS-1$

	/**
	 * Name of the url property.
	 */
	public static final String URL_PROP = "url"; //$NON-NLS-1$

}
