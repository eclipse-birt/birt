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

package org.eclipse.birt.report.designer.ui;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

/**
 * This class provides the UI images for JRP platform.
 * 
 * 
 */
public class ReportPlatformUIImages {

	private static ImageRegistry imageRegistry = null;

	/**
	 * Declares Common paths
	 */

	public final static String ICONS_PATH = "icons/";//$NON-NLS-1$

	public final static String DOBJ16_PATH = "dobj16/";//$NON-NLS-1$
	public final static String DTOOL16_PATH = "dtool16/";//$NON-NLS-1$
	public final static String ETOOL16_PATH = "etool16/";//$NON-NLS-1$
	public final static String EVIEW16_PATH = "eview16/";//$NON-NLS-1$
	public final static String OBJ16_PATH = "obj16/";//$NON-NLS-1$
	public final static String PAL_PATH = "pal/";//$NON-NLS-1$
	public final static String PROGRESS_PATH = "progress/";//$NON-NLS-1$
	public final static String MISC_PATH = "misc/";//$NON-NLS-1$
	public final static String WIZBAN_PATH = "wizban/";//$NON-NLS-1$
	public final static String LINK_PATH = "lib/";//$NON-NLS-1$
	public final static String DATA_PATH = "data/"; //$NON-NLS-1$
	public final static String OTHERS_PATH = "others/"; //$NON-NLS-1$
	public final static String OVR16_PATH = "ovr16/"; //$NON-NLS-1$
	public final static String LAYOUT16_PATH = "layout16/"; //$NON-NLS-1$

	static {
		initializeImageRegistry();
	}

	/**
	 * Declares a workbench image given the path of the image file (relative to the
	 * workbench plug-in). This is a helper method that creates the image descriptor
	 * and passes it to the main <code>declareImage</code> method.
	 * 
	 * @param key  the symbolic name of the image
	 * @param path the path of the image file relative to the base of the workbench
	 *             plug-ins install directory
	 */
	private final static void declareImage(String key, String path) {
		URL url = null;
		try {
			url = new URL(ReportPlugin.getDefault().getBundle().getEntry("/"), //$NON-NLS-1$
					path);
		} catch (MalformedURLException e) {
			ExceptionHandler.handle(e);
		}
		ImageDescriptor desc = ImageDescriptor.createFromURL(url);
		declareImage(key, desc);
	}

	/**
	 * Declares all the workbench's images, including both "shared" ones and
	 * internal ones.
	 */
	private final static void declareImages() {

		// common icons
		declareImage(ISharedImages.IMG_OBJS_ERROR_TSK, ICONS_PATH + PROGRESS_PATH + "error_tsk.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_OBJ_FOLDER, ICONS_PATH + OBJ16_PATH + "fldr_obj.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_COPY, ICONS_PATH + ETOOL16_PATH + "copy_edit.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_CUT, ICONS_PATH + ETOOL16_PATH + "cut_edit.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_UNDO, ICONS_PATH + ETOOL16_PATH + "undo_edit.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_REDO, ICONS_PATH + ETOOL16_PATH + "redo_edit.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_PASTE, ICONS_PATH + ETOOL16_PATH + "paste_edit.gif"); //$NON-NLS-1$
		declareImage(ISharedImages.IMG_TOOL_DELETE, ICONS_PATH + ETOOL16_PATH + "delete_edit.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_TOOL_FILTER, ICONS_PATH + ETOOL16_PATH + "filter_ps.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_VIEW_MENU, ICONS_PATH + ETOOL16_PATH + "view_menu.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEMPLATEITEM,
				ICONS_PATH + OBJ16_PATH + "templatereportitem.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NEW_REPORT, ICONS_PATH + OBJ16_PATH + "new_report.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NEW_LIBRARY, ICONS_PATH + EVIEW16_PATH + "library.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NEW_FOLDER, ICONS_PATH + OBJ16_PATH + "new_folder.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NEW_TEMPLATE, ICONS_PATH + OBJ16_PATH + "new_template.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_FILE, ICONS_PATH + EVIEW16_PATH + "report.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_TEMPLATE_FILE, ICONS_PATH + EVIEW16_PATH + "template.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LIBRARY_FILE, ICONS_PATH + EVIEW16_PATH + "library.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DOCUMENT_FILE, ICONS_PATH + EVIEW16_PATH + "document.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_LOCK, ICONS_PATH + OBJ16_PATH + "icon_lock_node.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LOCK_MENU, ICONS_PATH + OBJ16_PATH + "icon_lock_menu.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_QUIK_EDIT, ICONS_PATH + EVIEW16_PATH + "quick_edit.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_PERSPECTIVE,
				ICONS_PATH + EVIEW16_PATH + "report_perspective.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_PROJECT, ICONS_PATH + OBJ16_PATH + "report_project.gif"); //$NON-NLS-1$

		// element icons
		declareImage(IReportGraphicConstants.ICON_ELEMENT_CELL, ICONS_PATH + OBJ16_PATH + "cell.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA, ICONS_PATH + OBJ16_PATH + "data.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_LARGE, ICONS_PATH + OBJ16_PATH + "data_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_SOURCE, ICONS_PATH + OBJ16_PATH + "data_source.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SOURCE, ICONS_PATH + OBJ16_PATH + "data_source.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_SCRIPT_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_SCRIPT_DATA_SOURCE,
				ICONS_PATH + OBJ16_PATH + "data_source.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATAMART_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATAMART_DATA_SOURCE,
				ICONS_PATH + OBJ16_PATH + "data_source.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_JOINT_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DERIVED_DATA_SET, ICONS_PATH + OBJ16_PATH + "data_set.gif");//$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_GRID, ICONS_PATH + PAL_PATH + "grid.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_GRID_LARGE, ICONS_PATH + PAL_PATH + "grid_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXTDATA, ICONS_PATH + PAL_PATH + "textdata.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXTDATA_LARGE, ICONS_PATH + PAL_PATH + "textdata_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_GROUP, ICONS_PATH + OBJ16_PATH + "group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_IMAGE, ICONS_PATH + PAL_PATH + "image.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_IMAGE_LARGE, ICONS_PATH + PAL_PATH + "image_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LABEL, ICONS_PATH + PAL_PATH + "label.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LABEL_LARGE, ICONS_PATH + PAL_PATH + "label_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LINE, ICONS_PATH + OBJ16_PATH + "line.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIST, ICONS_PATH + PAL_PATH + "list.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIST_LARGE, ICONS_PATH + PAL_PATH + "list_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIST_GROUP, ICONS_PATH + OBJ16_PATH + "list_group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMNET_MASTERPAGE, ICONS_PATH + OBJ16_PATH + "master_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMNET_GRAPHICMASTERPAGE,
				ICONS_PATH + EVIEW16_PATH + "master_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_PARAMETER, ICONS_PATH + OBJ16_PATH + "parameter.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_PARAMETER_GROUP,
				ICONS_PATH + OBJ16_PATH + "parameter_group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_CASCADING_PARAMETER_GROUP,
				ICONS_PATH + OBJ16_PATH + "parameter_group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_ROW, ICONS_PATH + OBJ16_PATH + "row.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_COLUMN, ICONS_PATH + OBJ16_PATH + "column.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_SCALAR_PARAMETER, ICONS_PATH + OBJ16_PATH + "parameter.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_VARIABLE, ICONS_PATH + OBJ16_PATH + "parameter.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_VARIABLE_REPORT,
				ICONS_PATH + OBJ16_PATH + "variable_report.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_VARIABLE_PAGE, ICONS_PATH + OBJ16_PATH + "variable_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMNET_SIMPLE_MASTERPAGE,
				ICONS_PATH + EVIEW16_PATH + "master_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_STYLE, ICONS_PATH + OBJ16_PATH + "style.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TABLE, ICONS_PATH + PAL_PATH + "table.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TABLE_LARGE, ICONS_PATH + PAL_PATH + "table_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TABLE_GROUP, ICONS_PATH + OBJ16_PATH + "table_group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXT, ICONS_PATH + PAL_PATH + "text.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXT_LARGE, ICONS_PATH + PAL_PATH + "text_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIBRARY, ICONS_PATH + EVIEW16_PATH + "library.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIBRARY_REFERENCED,
				ICONS_PATH + OBJ16_PATH + "library_referenced.gif"); //$NON-NLS-1$

		// Class path preference page
		declareImage(IReportGraphicConstants.ICON_NODE_VARIABLE, ICONS_PATH + OBJ16_PATH + "envvar_obj.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_NODE_EXTJAR, ICONS_PATH + OBJ16_PATH + "jar_l_obj.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_NODE_EXTFOL, ICONS_PATH + OBJ16_PATH + "cf_src_obj.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_NODE_JAR, ICONS_PATH + OBJ16_PATH + "jar_obj.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_NODE_FOL, ICONS_PATH + OBJ16_PATH + "packagefolder_obj.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_THEME, ICONS_PATH + OBJ16_PATH + "theme.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET, ICONS_PATH + OBJ16_PATH + "css.gif"); //$NON-NLS-1$

		// library element icons
		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_LINK, ICONS_PATH + LINK_PATH + "data_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET_LINK,
				ICONS_PATH + LINK_PATH + "css_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_STYLE_LINK, ICONS_PATH + LINK_PATH + "style_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_SET_LINK, ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATA_SOURCE_LINK,
				ICONS_PATH + LINK_PATH + "data_source_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SET_LINK,
				ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DERIVED_DATA_SET_LINK,
				ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_ODA_DATA_SOURCE_LINK,
				ICONS_PATH + LINK_PATH + "data_source_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_SCRIPT_DATA_SET_LINK,
				ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_SCRIPT_DATA_SOURCE_LINK,
				ICONS_PATH + LINK_PATH + "data_source_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATAMART_DATA_SET_LINK,
				ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_DATAMART_DATA_SOURCE_LINK,
				ICONS_PATH + LINK_PATH + "data_source_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_JOINT_DATA_SET_LINK,
				ICONS_PATH + LINK_PATH + "data_set_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_GRID_LINK, ICONS_PATH + LINK_PATH + "grid_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXTDATA_LINK, ICONS_PATH + LINK_PATH + "textdata_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_IMAGE_LINK, ICONS_PATH + LINK_PATH + "image_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LABEL_LINK, ICONS_PATH + LINK_PATH + "label_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_LIST_LINK, ICONS_PATH + LINK_PATH + "list_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TABLE_LINK, ICONS_PATH + LINK_PATH + "table_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TEXT_LINK, ICONS_PATH + LINK_PATH + "text_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SCALAR_PARAMETER_ELEMENT_LINK,
				ICONS_PATH + LINK_PATH + "parameter_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_PARAMETER_GROUP_ELEMENT_LINK,
				ICONS_PATH + LINK_PATH + "parameter_group_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SIMPLE_MASTER_PAGE_ELEMENT_LINK,
				ICONS_PATH + LINK_PATH + "simple_masterpage_link.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_CASCADING_PARAMETER_GROUP_ELEMENT_LINK,
				ICONS_PATH + LINK_PATH + "parameter_group_link.gif"); //$NON-NLS-1$

		// outline icons
		declareImage(IReportGraphicConstants.ICON_NODE_BODY, ICONS_PATH + OBJ16_PATH + "body_icon.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_MASTERPAGES, ICONS_PATH + OBJ16_PATH + "master_pages.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_STYLES, ICONS_PATH + OBJ16_PATH + "styles.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_HEADER, ICONS_PATH + OBJ16_PATH + "header.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_DETAILS, ICONS_PATH + OBJ16_PATH + "details.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_FOOTER, ICONS_PATH + OBJ16_PATH + "footer.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_GROUPS, ICONS_PATH + OBJ16_PATH + "group.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_GROUP_HEADER, ICONS_PATH + OBJ16_PATH + "group_header.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_GROUP_FOOTER, ICONS_PATH + OBJ16_PATH + "group_footer.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_LIBRARIES, ICONS_PATH + OBJ16_PATH + "library_folder.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_THEMES, ICONS_PATH + OBJ16_PATH + "themes.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_IMAGES, ICONS_PATH + OBJ16_PATH + "images.gif"); //$NON-NLS-1$

		// layout icons
		declareImage(IReportGraphicConstants.ICON_LAYOUT_NORMAL, ICONS_PATH + EVIEW16_PATH + "normal_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LAYOUT_MASTERPAGE, ICONS_PATH + EVIEW16_PATH + "master_page.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LAYOUT_RULER, ICONS_PATH + EVIEW16_PATH + "show_rulers.gif"); //$NON-NLS-1$

		// border icons
		declareImage(IReportGraphicConstants.ICON_BORDER_ALL, ICONS_PATH + OBJ16_PATH + "borders_frame.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_BORDER_BOTTOM, ICONS_PATH + OBJ16_PATH + "border_bottom.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_BORDER_TOP, ICONS_PATH + OBJ16_PATH + "border_top.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_BORDER_LEFT, ICONS_PATH + OBJ16_PATH + "border_left.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_BORDER_RIGHT, ICONS_PATH + OBJ16_PATH + "border_right.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_BORDER_NOBORDER, ICONS_PATH + OBJ16_PATH + "border_none.gif"); //$NON-NLS-1$

		// missing image icons
		declareImage(IReportGraphicConstants.ICON_MISSING_IMG, ICONS_PATH + PROGRESS_PATH + "missing_image.gif"); //$NON-NLS-1$

		// data explore icons
		declareImage(IReportGraphicConstants.ICON_DATA_EXPLORER_VIEW,
				ICONS_PATH + EVIEW16_PATH + "data_explorer_view.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_DATA_SETS, ICONS_PATH + OBJ16_PATH + "data_set_folder.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_DATA_SOURCES,
				ICONS_PATH + OBJ16_PATH + "data_source_folder.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_PARAMETERS, ICONS_PATH + OBJ16_PATH + "parameter_folder.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_NODE_VARIABLES, ICONS_PATH + OBJ16_PATH + "variable_folder.gif"); //$NON-NLS-1$

		// **********************************************************
		// expression icons
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_DATA_TABLE, ICONS_PATH + OBJ16_PATH + "data_table.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_DATA_COLUMN, ICONS_PATH + OBJ16_PATH + "data_column.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_INHERIT_COLUMN, ICONS_PATH + OBJ16_PATH + "inherit_column.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ALPHABETIC_SORT, ICONS_PATH + OBJ16_PATH + "alpha_sort.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_GROUP_SORT, ICONS_PATH + OBJ16_PATH + "group_sort.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_LOCAL_PROPERTIES, ICONS_PATH + OBJ16_PATH + "local_prop.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ALPHABETIC_SORT, ICONS_PATH + OBJ16_PATH + "alpha_sort.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_OPERATOR, ICONS_PATH + OBJ16_PATH + "operator.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_EXPRESSION_GLOBAL, ICONS_PATH + OBJ16_PATH + "global.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_CONSTRUCTOP, ICONS_PATH + OBJ16_PATH + "constructor.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_METHOD, ICONS_PATH + OBJ16_PATH + "method.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_STATIC_METHOD,
				ICONS_PATH + OBJ16_PATH + "static_method.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_MEMBER, ICONS_PATH + OBJ16_PATH + "property.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_STATIC_MEMBER,
				ICONS_PATH + OBJ16_PATH + "static_property.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_EXPRESSION_BUILDER, ICONS_PATH + OBJ16_PATH + "expression.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_EXPRESSION_VALIDATE, ICONS_PATH + OBJ16_PATH + "validate.gif"); //$NON-NLS-1$

		// data wizards
		declareImage(IReportGraphicConstants.ICON_WIZARD_DATASOURCE,
				ICONS_PATH + WIZBAN_PATH + "datasource_wizard.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_WIZARD_DATASET, ICONS_PATH + WIZBAN_PATH + "dataset_wizard.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_WIZARDPAGE_DATASETSELECTION,
				ICONS_PATH + WIZBAN_PATH + "dataset_wizard_table.gif"); //$NON-NLS-1$

		// DataSetEditor History ToolBar
		declareImage(IReportGraphicConstants.ICON_HISTORYTOOLBAR_BACKWARDDISABLED,
				ICONS_PATH + DATA_PATH + "backward_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_HISTORYTOOLBAR_BACKWARDENABLED,
				ICONS_PATH + DATA_PATH + "backward_enabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_HISTORYTOOLBAR_FORWARDDISABLED,
				ICONS_PATH + DATA_PATH + "forward_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_HISTORYTOOLBAR_FORWARDENABLED,
				ICONS_PATH + DATA_PATH + "forward_enabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_CHECKED, ICONS_PATH + DATA_PATH + "checked.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_UNCHECKED, ICONS_PATH + DATA_PATH + "unchecked.gif"); //$NON-NLS-1$

		// ///////////////////attribute image
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_FONT_WIDTH, ICONS_PATH + OBJ16_PATH + "bold.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_FONT_WIDTH + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "bold_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_FONT_STYLE, ICONS_PATH + OBJ16_PATH + "italic.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_FONT_STYLE + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "italic_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_UNDERLINE, ICONS_PATH + OBJ16_PATH + "underline.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_UNDERLINE + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "underline_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_LINE_THROUGH,
				ICONS_PATH + OBJ16_PATH + "lineSthrough.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_LINE_THROUGH + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "lineSthrough_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_NONE, ICONS_PATH + OBJ16_PATH + "border_none.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_FRAME, ICONS_PATH + OBJ16_PATH + "border_frame.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_LEFT, ICONS_PATH + OBJ16_PATH + "border_left.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_RIGHT, ICONS_PATH + OBJ16_PATH + "border_right.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_TOP, ICONS_PATH + OBJ16_PATH + "border_top.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_BOTTOM,
				ICONS_PATH + OBJ16_PATH + "border_bottom.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_CENTER,
				ICONS_PATH + OBJ16_PATH + "center_align.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_CENTER + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "center_align_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_JUSTIFY,
				ICONS_PATH + OBJ16_PATH + "justified_align.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_JUSTIFY + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "justified_align_disabled.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_LEFT,
				ICONS_PATH + OBJ16_PATH + "left_align.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_LEFT + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "left_align_disabled.gif");//$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_RIGHT,
				ICONS_PATH + OBJ16_PATH + "right_align.gif");//$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TEXT_ALIGN_RIGHT + IReportGraphicConstants.DIS,
				ICONS_PATH + DOBJ16_PATH + "right_align_disabled.gif");//$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TOP_MARGIN, ICONS_PATH + MISC_PATH + "top_margin.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_BOTTOM_MARGIN,
				ICONS_PATH + MISC_PATH + "bottom_margin.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_LEFT_MARGIN, ICONS_PATH + MISC_PATH + "left_margin.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_RIGHT_MARGIN, ICONS_PATH + MISC_PATH + "right_margin.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_ONE_COLUMN,
				ICONS_PATH + MISC_PATH + "master_one_column.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_TWO_COLUMNS,
				ICONS_PATH + MISC_PATH + "master_two_columns.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_THTREE_COLUMNS,
				ICONS_PATH + MISC_PATH + "master_three_columns.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_ATTRIBUTE_RIGHT_MARGIN, ICONS_PATH + MISC_PATH + "right_margin.gif"); //$NON-NLS-1$

		// **********************************************************
		// Preview icons
		declareImage(IReportGraphicConstants.ICON_PREVIEW_PARAMETERS,
				ICONS_PATH + PROGRESS_PATH + "params_ecl_show.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_PREVIEW_PARAMETERS_HIDE,
				ICONS_PATH + PROGRESS_PATH + "params_ecl_hide.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_PREVIEW_REFRESH, ICONS_PATH + ETOOL16_PATH + "refresh.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REFRESH, ICONS_PATH + ETOOL16_PATH + "refresh_nav.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REFRESH_DISABLE, ICONS_PATH + DTOOL16_PATH + "refresh_nav.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_TOGGLE_BREADCRUMB,
				ICONS_PATH + ETOOL16_PATH + "toggle_breadcrumb.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_TOGGLE_BREADCRUMB_DISABLE,
				ICONS_PATH + DTOOL16_PATH + "toggle_breadcrumb.gif"); //$NON-NLS-1$

		// Auto Text Icon
		declareImage(IReportGraphicConstants.ICON_AUTOTEXT, ICONS_PATH + OBJ16_PATH + "autotext.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_AUTOTEXT_LARGE, ICONS_PATH + OBJ16_PATH + "autotext_large.png"); //$NON-NLS-1$

		// Parameter dialog icon
		declareImage(IReportGraphicConstants.ICON_DEFAULT, ICONS_PATH + OBJ16_PATH + "default.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DEFAULT_NOT, ICONS_PATH + OBJ16_PATH + "not_default.gif"); //$NON-NLS-1$

		// Data editor dialog icons
		declareImage(IReportGraphicConstants.ICON_DATAEDIT_DLG_TITLE_BANNER,
				ICONS_PATH + OBJ16_PATH + "prop_dialog_title.gif"); //$NON-NLS-1$

		// Open file icon
		declareImage(IReportGraphicConstants.ICON_OPEN_FILE, ICONS_PATH + ETOOL16_PATH + "open_file.gif"); //$NON-NLS-1$

		declareImage("TableRowSelector", ICONS_PATH + OBJ16_PATH + "tablerowselector.gif"); //$NON-NLS-1$ //$NON-NLS-2$

		declareImage(IReportGraphicConstants.ICON_ENABLE_RESTORE_PROPERTIES,
				ICONS_PATH + OBJ16_PATH + "property_restore.gif");//$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DISABLE_RESTORE_PROPERTIES,
				ICONS_PATH + OBJ16_PATH + "property_restore_disabled.gif");//$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_TEMPLATE_NO_PREVIEW, ICONS_PATH + MISC_PATH + "no_preview.gif"); //$NON-NLS-1$

		// Script Icons
		declareImage(IReportGraphicConstants.ICON_SCRIPT_ERROR, ICONS_PATH + OBJ16_PATH + "script_error.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SCRIPTS_METHOD_NODE, ICONS_PATH + OBJ16_PATH + "script-method.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SCRIPT_NOERROR, ICONS_PATH + OBJ16_PATH + "script_noerror.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SCRIPT_RESET, ICONS_PATH + OBJ16_PATH + "script_reset.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_SCRIPT_HELP, ICONS_PATH + OBJ16_PATH + "script_help.gif"); //$NON-NLS-1$

		// Other icons
		declareImage(IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS,
				ICONS_PATH + OTHERS_PATH + "fx_disabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS,
				ICONS_PATH + OTHERS_PATH + "fx_enabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ENABLE_EXPRESSION_CONSTANT,
				ICONS_PATH + OTHERS_PATH + "fx_constant_enabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DISABLE_EXPRESSION_CONSTANT,
				ICONS_PATH + OTHERS_PATH + "fx_constant_disabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ENABLE_EXPRESSION_JAVASCRIPT,
				ICONS_PATH + OTHERS_PATH + "fx_js_enabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DISABLE_EXPRESSION_JAVASCRIPT,
				ICONS_PATH + OTHERS_PATH + "fx_js_disabled.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_AGGREGATION, ICONS_PATH + OBJ16_PATH + "aggregation.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_AGGREGATION_LARGE,
				ICONS_PATH + OBJ16_PATH + "aggregation_large.png"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TIMEPERIOD, ICONS_PATH + OBJ16_PATH + "relativetime.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ELEMENT_TIMEPERIOD_LARGE,
				ICONS_PATH + OBJ16_PATH + "relativetime_large.png"); //$NON-NLS-1$

		// Scripts Node Icon
		declareImage(IReportGraphicConstants.ICON_SCRIPTS_NODE, ICONS_PATH + OBJ16_PATH + "script-16.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LEVEL_ATTRI, ICONS_PATH + OBJ16_PATH + "levelAttribute-16.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ENABLE_EXPORT, ICONS_PATH + ETOOL16_PATH + "export_wiz.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_ENABLE_IMPORT, ICONS_PATH + ETOOL16_PATH + "import_wiz.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DISABLE_EXPORT, ICONS_PATH + DTOOL16_PATH + "export_wiz.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_DISABLE_IMPORT, ICONS_PATH + DTOOL16_PATH + "import_wiz.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_PROJECT_OVER,
				ICONS_PATH + OVR16_PATH + "report_project_over.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_REPORT_LOCAL_LIBRARY_OVER,
				ICONS_PATH + OVR16_PATH + "local_library_overwrite.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_REPORT_LIBRARY_OVER,
				ICONS_PATH + OVR16_PATH + "library_overwrite.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_TOOL_CALENDAR, ICONS_PATH + OBJ16_PATH + "calendar.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_STATUS_ERROR, ICONS_PATH + OBJ16_PATH + "error_obj.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_STYLE_MODIFIED, ICONS_PATH + OBJ16_PATH + "style_modified.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_STYLE_DEFAULT, ICONS_PATH + OBJ16_PATH + "style_default.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_STYLE_RESOTRE, ICONS_PATH + OBJ16_PATH + "style_restore.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LAYOUT_AUTO, ICONS_PATH + LAYOUT16_PATH + "autoLayout.gif"); //$NON-NLS-1$

		declareImage(IReportGraphicConstants.ICON_LAYOUT_FIXED, ICONS_PATH + LAYOUT16_PATH + "fixedLayout.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_LAYOUT_PREFERENCE,
				ICONS_PATH + LAYOUT16_PATH + "layoutPreference.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_COPY_FORMAT, ICONS_PATH + MISC_PATH + "copy_format.gif"); //$NON-NLS-1$
		declareImage(IReportGraphicConstants.ICON_PASTE_FORMAT, ICONS_PATH + MISC_PATH + "paste_format.gif"); //$NON-NLS-1$

	}

	/**
	 * Declares a workbench image.
	 * <p>
	 * The workbench remembers the given image descriptor under the given name, and
	 * makes the image available to plug-ins via {@link org.eclipse.ui.ISharedImages
	 * IWorkbench.getSharedImages()}. For "shared" images, the workbench remembers
	 * the image descriptor and will manages the image object create from it;
	 * clients retrieve "shared" images via
	 * {@link org.eclipse.ui.ISharedImages#getImage ISharedImages.getImage()}. For
	 * the other, "non-shared" images, the workbench remembers only the image
	 * descriptor; clients retrieve the image descriptor via
	 * {@link org.eclipse.ui.ISharedImages#getImageDescriptor
	 * ISharedImages.getImageDescriptor()} and are entirely responsible for managing
	 * the image objects they create from it. (This is made confusing by the
	 * historical fact that the API interface is called "ISharedImages".)
	 * </p>
	 * 
	 * @param symbolicName the symbolic name of the image
	 * @param descriptor   the image descriptor
	 */
	public static void declareImage(String symbolicName, ImageDescriptor descriptor) {
		imageRegistry.put(symbolicName, descriptor);
	}

	/**
	 * Returns the image stored in the workbench plugin's image registry under the
	 * given symbolic name. If there isn't any value associated with the name then
	 * <code>null</code> is returned.
	 * 
	 * The returned Image is managed by the workbench plugin's image registry.
	 * Callers of this method must not dispose the returned image.
	 * 
	 * This method is essentially a convenient short form of
	 * WorkbenchImages.getImageRegistry.get(symbolicName).
	 */
	public static Image getImage(String symbolicName) {
		return getImageRegistry().get(symbolicName);
	}

	/**
	 * Returns the image descriptor stored under the given symbolic name. If there
	 * isn't any value associated with the name then <code>null
	 * </code> is returned.
	 * 
	 * The class also "caches" commonly used images in the image registry. If you
	 * are looking for one of these common images it is recommended you use the
	 * getImage() method instead.
	 */
	public static ImageDescriptor getImageDescriptor(String symbolicName) {
		return imageRegistry.getDescriptor(symbolicName);
	}

	/**
	 * Gets the proper icon image for the given model
	 * 
	 * @param model the given model
	 * @return Returns the proper icon image for the given model, or null if no
	 *         proper one exists
	 */

	public static Image getImage(Object model) {
		Image image = null;
		if (model instanceof ExtendedItemHandle) {
			image = getImage(getIconSymbolName(((ExtendedItemHandle) model).getExtensionName(),
					IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON));
			if (image == null) {
				image = getImage(IReportGraphicConstants.ICON_ELEMENT_EXTENDED_ITEM);
			}
		} else if (model instanceof VariableElementHandle) {
			VariableElementHandle variable = (VariableElementHandle) model;
			if (DesignChoiceConstants.VARIABLE_TYPE_REPORT.equals(variable.getType())) {
				return getImage(IReportGraphicConstants.ICON_ELEMENT_VARIABLE_REPORT);
			} else {
				return getImage(IReportGraphicConstants.ICON_ELEMENT_VARIABLE_PAGE);
			}
		} else if (model instanceof DesignElementHandle) {// the icon name for elements is just the same as the element
															// name
//			if ( isLinkImg( (DesignElementHandle) model ) == true )
//			{
//				image = getImage( ( (DesignElementHandle) model ).getElement( )
//						.getDefn( )
//						.getName( )
//						+ "_" //$NON-NLS-1$
//						+ IReportGraphicConstants.LINK );
//			}
//			else
			{
				image = getImage(((DesignElementHandle) model).getElement().getDefn().getName());
			}
		} else if (model instanceof CssStyleSheetHandle) {
			if (isCSSLinkImg((CssStyleSheetHandle) model) == true) {
				image = getImage(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET + "_" //$NON-NLS-1$
						+ IReportGraphicConstants.LINK);
			} else {
				image = getImage(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET);
			}
		}

		return image;
	}

	private static boolean isCSSLinkImg(CssStyleSheetHandle handle) {
		if (handle.getContainerHandle() instanceof ReportDesignHandle
				|| handle.getContainerHandle() instanceof ThemeHandle) {
			return true;
		}
		return false;
	}

	private static boolean isLinkImg(DesignElementHandle handle) {
		if (!DEUtil.isLinkedElement(handle)) {
			return false;
		}

		if ((handle instanceof DataSourceHandle) || (handle instanceof DataSetHandle) || (handle instanceof GridHandle)
				|| (handle instanceof ImageHandle) || (handle instanceof DataItemHandle)
				|| (handle instanceof LabelHandle) || (handle instanceof ListingHandle)
				|| (handle instanceof TableHandle) || (handle instanceof TextItemHandle)
				|| (handle instanceof TextDataHandle) || (handle instanceof CascadingParameterGroupHandle)
				|| (handle instanceof ScalarParameterHandle) || (handle instanceof ParameterGroupHandle)
				|| (handle instanceof SimpleMasterPageHandle)) {
			return true;
		}

		// return false for other unavailable linked images
		return false;
	}

	/**
	 * Gets the proper icon image descriptor for the given model
	 * 
	 * @param model the given model
	 * @return Returns the proper icon image descriptor for the given model, or null
	 *         if no proper one exists
	 */

	public static ImageDescriptor getImageDescriptor(Object model) {
		ImageDescriptor imageDescriptor = null;
		if (model instanceof ExtendedItemHandle) {
			imageDescriptor = getImageDescriptor(getIconSymbolName(((ExtendedItemHandle) model).getExtensionName(),
					IExtensionConstants.ATTRIBUTE_KEY_OUTLINE_ICON));
			if (imageDescriptor == null) {
				imageDescriptor = getImageDescriptor(IReportGraphicConstants.ICON_ELEMENT_EXTENDED_ITEM);
			}
		} else if (model instanceof DesignElementHandle) {// the icon name for elements is just the same as the element
															// name
			if (isLinkImg((DesignElementHandle) model)) {
				imageDescriptor = getImageDescriptor(
						((DesignElementHandle) model).getElement().getDefn().getName() + "_" //$NON-NLS-1$
								+ IReportGraphicConstants.LINK);
			} else {
				imageDescriptor = getImageDescriptor(((DesignElementHandle) model).getElement().getDefn().getName());
			}
		} else if (model instanceof CssStyleSheetHandle) {
			if (isCSSLinkImg((CssStyleSheetHandle) model) == true) {
				imageDescriptor = getImageDescriptor(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET + "_" //$NON-NLS-1$
						+ IReportGraphicConstants.LINK);
			} else {
				imageDescriptor = getImageDescriptor(IReportGraphicConstants.ICON_ELEMENT_CSS_STYLE_SHEET);
			}
		}
		return imageDescriptor;
	}

	/**
	 * Returns the ImageRegistry.
	 */
	public static ImageRegistry getImageRegistry() {
		return imageRegistry;
	}

	/**
	 * Initialize the image registry by declaring all of the required graphics. This
	 * involves creating JFace image descriptors describing how to create/find the
	 * image should it be needed. The image is not actually allocated until
	 * requested.
	 * 
	 * Prefix conventions Wizard Banners WIZBAN_ Preference Banners PREF_BAN_
	 * Property Page Banners PROPBAN_ Enable toolbar ETOOL_ Disable toolbar DTOOL_
	 * Local enabled toolbar ELCL_ Local Disable toolbar DLCL_ Object large OBJL_
	 * Object small OBJS_ View VIEW_ Product images PROD_ Misc images MISC_
	 * 
	 * Where are the images? The images (typically gifs) are found in the same
	 * location as this plug-in class. This may mean the same package directory as
	 * the package holding this class. The images are declared using this.getClass()
	 * to ensure they are looked up via this plug-in class.
	 * 
	 */
	public static ImageRegistry initializeImageRegistry() {
		imageRegistry = ReportPlugin.getDefault().getImageRegistry();
		declareImages();
		return imageRegistry;
	}

	/**
	 * Gets the proper symbol name for the specified icon of the extended element
	 * 
	 * @param extensionName the extension name of the element
	 * @param attrbuteName  the name of the attribute which defines an icon
	 * 
	 * @return Returns the symbol name generated
	 */
	public static String getIconSymbolName(String extensionName, String attrbuteName) {
		return attrbuteName + "." + extensionName; //$NON-NLS-1$
	}
}
