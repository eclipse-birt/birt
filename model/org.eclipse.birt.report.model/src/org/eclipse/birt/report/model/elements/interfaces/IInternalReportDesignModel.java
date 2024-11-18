/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for report design element to store the constants.
 */

public interface IInternalReportDesignModel {

	/**
	 * Name of the refresh rate property.
	 */

	String REFRESH_RATE_PROP = "refreshRate"; //$NON-NLS-1$

	/**
	 * Name of the method called at the start of the Factory after the initialize( )
	 * method and before opening the report document (if any).
	 */

	String BEFORE_FACTORY_METHOD = "beforeFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called at the end of the Factory after closing the report
	 * document (if any). This is the last method called in the Factory.
	 */

	String AFTER_FACTORY_METHOD = "afterFactory"; //$NON-NLS-1$

	/**
	 * Name of the method called before starting a presentation time action.
	 */

	String BEFORE_RENDER_METHOD = "beforeRender"; //$NON-NLS-1$

	/**
	 * Name of the method called after starting a presentation time action.
	 */

	String AFTER_RENDER_METHOD = "afterRender"; //$NON-NLS-1$

	/**
	 * Name of the property to store report design icon/thumbnail file path.
	 */

	String ICON_FILE_PROP = "iconFile"; //$NON-NLS-1$

	/**
	 * Name of the property to store the cheet sheet file name.
	 */

	String CHEAT_SHEET_PROP = "cheatSheet"; //$NON-NLS-1$

	/**
	 * Name of the property to store the thumbnail image for the design or template.
	 */

	String THUMBNAIL_PROP = "thumbnail"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the layout format of this report design.
	 */
	String LAYOUT_PREFERENCE_PROP = "layoutPreference"; //$NON-NLS-1$

	/**
	 * Css file property
	 */

	String CSSES_PROP = "cssStyleSheets";//$NON-NLS-1$

	/**
	 * Encoding mode for the thumbnail image.
	 */

	String CHARSET = "8859_1"; //$NON-NLS-1$

	// Design slots
	// See constants defined in the module class.

	/**
	 * Identifier of the slot that holds styles.
	 */

	int STYLE_SLOT = 0;

	/**
	 * Identifier of the body slot that contains the report sections.
	 */

	int BODY_SLOT = 6;

	/**
	 * Identifier of the scratch pad slot.
	 */

	int SCRATCH_PAD_SLOT = 7;

	/**
	 * Identifier of the template parameter definition slot.
	 */

	int TEMPLATE_PARAMETER_DEFINITION_SLOT = 8;

	/**
	 * Identifier of the slot that holds a collections of cube elements.
	 */

	int CUBE_SLOT = 9;

	/**
	 * Identifier of the slot that holds themes.
	 */

	int THEMES_SLOT = 10;

	/**
	 * Number of slots in the report design element.
	 */

	int SLOT_COUNT = 11;

	/**
	 * bidi_hcg: Bidi orientation property
	 */
	String BIDI_ORIENTATION_PROP = "bidiLayoutOrientation"; //$NON-NLS-1$

	/**
	 * Name of the property that determines whether to enable the ACL feature for
	 * this report design or not.
	 */

	String ENABLE_ACL_PROP = "enableACL"; //$NON-NLS-1$

	/**
	 * A Boolean property set on report that can act as container to other report
	 * elements. If set to true (the default), a report's ACL is automatically
	 * propagated to all its directly contained child elements and are added to
	 * their ACLs. This means that any user that is permitted to view the parent
	 * element is also allowed to view report element instances directly contained
	 * within the parent.
	 */

	String CASCADE_ACL_PROP = IInternalReportItemModel.CASCADE_ACL_PROP;

	/**
	 * A Java script expression which returns the ACL associated with the report
	 * instance in a String.
	 */

	String ACL_EXPRESSION_PROP = IInternalReportItemModel.ACL_EXPRESSION_PROP;

	/**
	 * Name of the property to keep image in report design display as same size at
	 * design time as at run time.
	 */
	String IMAGE_DPI_PROP = "imageDPI"; //$NON-NLS-1$

	/**
	 * Name of the property contains list of Variable element defined for page level
	 * page break.
	 */
	String PAGE_VARIABLES_PROP = "pageVariables"; //$NON-NLS-1$

	/**
	 * Name of locale property. It defines the locale used to generate/render the
	 * report
	 */
	String LOCALE_PROP = "locale"; //$NON-NLS-1$

	/**
	 * Name of the property that defines some external metadata.
	 */
	String EXTERNAL_METADATA_PROP = "externalMetadata"; //$NON-NLS-1$

	/**
	 * Name of the method on page start.
	 */
	String ON_PAGE_START_METHOD = "onPageStart"; //$NON-NLS-1$

	/**
	 * Name of the method on page end.
	 */
	String ON_PAGE_END_METHOD = "onPageEnd"; //$NON-NLS-1$

	/**
	 * Name of the data object variable property.
	 */
	String DATA_OBJECTS_PROP = "dataObjects"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare property. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$

	/**
	 * Name of the client-initialize property. The design can load java script
	 * libraries by the property.
	 */
	String CLIENT_INITIALIZE_METHOD = "clientInitialize"; //$NON-NLS-1$

	/**
	 * Name of the language property.
	 */
	String LANGUAGE_PROP = "language"; //$NON-NLS-1$

	/**
	 * Excel option, force auto column width calculation
	 */
	String EXCEL_FORCE_AUTO_COL_WIDTHS = "excelForceAutoColWidths"; //$NON-NLS-1$

	/**
	 * Excel option, create only singe sheet
	 */
	String EXCEL_SINGLE_SHEET = "excelSingleSheet"; //$NON-NLS-1$

	/**
	 * Excel option, disable grouping
	 */
	String EXCEL_DISABLE_GROUPING = "excelDisableGrouping"; //$NON-NLS-1$

	/**
	 * Excel option, display grid lines
	 */
	String EXCEL_DISPLAY_GRIDLINES = "excelDisplayGridlines"; //$NON-NLS-1$

	/**
	 * Excel option, add auto filter to header row
	 */
	String EXCEL_AUTO_FILTER = "excelAutoFilter"; //$NON-NLS-1$

	/**
	 * Excel option, force recalculation
	 */
	String EXCEL_FORCE_RECALCULATION = "excelForceRecalculation"; //$NON-NLS-1$

	/**
	 * Excel option, scale the images to cell dimension
	 */
	String EXCEL_IMAGE_SCALING_CELL_DIMENSION = "excelImageScalingToCellDimension"; //$NON-NLS-1$

	/**
	 * Excel option, create page break on single sheet
	 */
	String EXCEL_SINGLE_SHEET_WITH_PAGE_BREAK = "excelSingleSheetWithPageBreaks"; //$NON-NLS-1$

	/**
	 * Excel option, write data with streaming mode
	 */
	String EXCEL_STREAMING_XLSX = "excelStreamingXlsx"; //$NON-NLS-1$

	/**
	 * Excel option, display report header and footer at excel sheet
	 */
	String EXCEL_STRUCTURED_HEADER = "excelStructuredHeader"; //$NON-NLS-1$

	/**
	 * Excel option, set the sheet high to fit the page
	 */
	String EXCEL_PRINT_PAGES_HIGH = "excelPrintPagesHigh"; //$NON-NLS-1$

	/**
	 * Excel option, set the sheet wide to fit the page
	 */
	String EXCEL_PRINT_PAGES_WIDE = "excelPrintPagesWide"; //$NON-NLS-1$

	/**
	 * Excel option, set the page scale factor
	 */
	String EXCEL_PRINT_SCALE = "excelPrintScale"; //$NON-NLS-1$

	/**
	 * Excel option, set the template file for excel
	 */
	String EXCEL_TEMPLATE_FILE = "excelTemplateFile"; //$NON-NLS-1$

	/**
	 * PDF option, version number
	 */
	String PDF_VERSION = "pdfVersion"; //$NON-NLS-1$

	/**
	 * PDF option, conformance
	 */
	String PDF_CONFORMANCE = "pdfConformance"; //$NON-NLS-1$
	/**
	 * PDF option, PDF/UA conformance
	 */
	String PDF_UA_CONFORMANCE = "pdfUAConformance"; //$NON-NLS-1$

	/**
	 * PDF option, ICC color type profile (RGB/CMYK)
	 */
	String PDF_ICC_COLOR_TYPE = "pdfIccColorType"; //$NON-NLS-1$

	/**
	 * PDF option, ICC external color profile file
	 */
	String PDF_ICC_PROFILE_EXTERNAL = "pdfIccProfileExternal"; //$NON-NLS-1$

	/**
	 * PDF option, prepend document(s)
	 */
	String PDF_DOCUMENTS_PREPEND = "pdfDocuemntPrepend"; //$NON-NLS-1$

	/**
	 * PDF option, append document(s)
	 */
	String PDF_DOCUMENTS_APPEND = "pdfDocuemntAppend"; //$NON-NLS-1$

	/**
	 * PDF option, PDF/A fallback font
	 */
	String PDFA_FONT_FALLBACK = "pdfaFontFallback"; //$NON-NLS-1$

	/**
	 * PDF option, PDF/A embed CIDSet font stream
	 */
	String PDF_FONT_CID_SET = "pdfaFontCidEmbed"; //$NON-NLS-1$

	/**
	 * PDF option, PDF/A embed document title
	 */
	String PDFA_DOCUMENT_EMBED_TITLE = "pdfaDocumentTitleEmbed"; //$NON-NLS-1$

	/**
	 * Word option, use margin & padding for spacing (only without layout-grid)
	 */
	String WORD_COMBINE_MARGIN_PADDING = "wordCombineMarginPadding"; //$NON-NLS-1$
	/**
	 * Word option, list element, add empty line of cell end
	 */
	String WORD_LIST_CELL_ADD_EMPTY_PARA = "wordAddEmptyParagraphForListCell"; //$NON-NLS-1$
	/**
	 * Word option, text element, use layout-grid for margin & padding
	 */
	String WORD_WRAP_TABLE_FOR_MARGIN_PADDING = "wordWrappedTableForMarginPadding"; //$NON-NLS-1$
	/**
	 * Word option, page header & footer, use layout-grid
	 */
	String WORD_WRAP_TABLE_FOR_HEADER_FOOTER = "wordWrappedTableHeaderFooter"; //$NON-NLS-1$
}
