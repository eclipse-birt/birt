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
 * The interface for Cell element to store the constants on Cell element.
 */
public interface ICellModel {

	/**
	 * Name of the property that gives the column in which the cell starts. Columns
	 * are numbered from 1.
	 */

	String COLUMN_PROP = "column"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the number of columns that this cell spans.
	 * Defaults to 1, meaning that the cell appears in only one column.
	 */

	String COL_SPAN_PROP = "colSpan"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the number of rows that this cell spans.
	 * Defaults to 1, meaning the cell appears in only one row. The row span is used
	 * to create drop content within a table. The special value of -1 means that the
	 * cell spans all rows for this particular table or group.
	 */

	String ROW_SPAN_PROP = "rowSpan"; //$NON-NLS-1$

	/**
	 * Name of the drop property that gives the drop options for cells. Controls how
	 * cells in one row overlap subsequent rows: None, detail or all.
	 */

	String DROP_PROP = "drop"; //$NON-NLS-1$

	/**
	 * Name of the height property that gives the height of the cell.
	 */

	String HEIGHT_PROP = "height"; //$NON-NLS-1$

	/**
	 * Name of the width property that gives the width of the cell.
	 */

	String WIDTH_PROP = "width"; //$NON-NLS-1$

	/**
	 * Property name for the reference to the shared style.
	 */

	String STYLE_PROP = "style"; //$NON-NLS-1$

	/**
	 * Name of the on-create property. It is for a script executed when the element
	 * is created in the Factory. Called after the item is created, but before the
	 * item is saved to the report document file.
	 */

	String ON_CREATE_METHOD = "onCreate"; //$NON-NLS-1$

	/**
	 * Name of the on-render property. It is for a script Executed when the element
	 * is prepared for rendering in the Presentation engine.
	 */

	String ON_RENDER_METHOD = "onRender"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare property. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	String ON_PREPARE_METHOD = "onPrepare"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds the page decoration.
	 */

	int CONTENT_SLOT = 0;

	/**
	 * Name of the diagonalNumber property that defines the number of the diagonal
	 * that is from top-left to bottom-right corner.
	 */
	String DIAGONAL_NUMBER_PROP = "diagonalNumber"; //$NON-NLS-1$

	/**
	 * Name of the antidiagonalNumber property that defines the number of the
	 * anti-diagonal that is from the top-right to bottom-left
	 */
	String ANTIDIAGONAL_NUMBER_PROP = "antidiagonalNumber"; //$NON-NLS-1$

	/**
	 * Name of the diagonalThickness property that defines the thickness of the
	 * diagonal that is from top-left to bottom-right corner.
	 */
	String DIAGONAL_THICKNESS_PROP = "diagonalThickness"; //$NON-NLS-1$

	/**
	 * Name of the antidiagonalThickness property that defines the thickness of the
	 * anti-diagonal that is from the top-right to bottom-left
	 */
	String ANTIDIAGONAL_THICKNESS_PROP = "antidiagonalThickness"; //$NON-NLS-1$

	/**
	 * Name of the diagonalStyle property that defines the style of the diagonal
	 * that is from top-left to bottom-right corner.
	 */
	String DIAGONAL_STYLE_PROP = "diagonalStyle"; //$NON-NLS-1$

	/**
	 * Name of the antidiagonalStyle property that defines the style of the
	 * anti-diagonal that is from the top-right to bottom-left
	 */
	String ANTIDIAGONAL_STYLE_PROP = "antidiagonalStyle"; //$NON-NLS-1$

	/**
	 * Name of the diagonalColor property that defines the color of the diagonal
	 * that is from top-left to bottom-right corner.
	 */
	String DIAGONAL_COLOR_PROP = "diagonalColor"; //$NON-NLS-1$

	/**
	 * Name of the antidiagonalColor property that defines the color of the
	 * anti-diagonal that is from the top-right to bottom-left
	 */
	String ANTIDIAGONAL_COLOR_PROP = "antidiagonalColor"; //$NON-NLS-1$

	/**
	 * Name of the scope property that defines the current cell provides header
	 * information for the container where the cell locates in.
	 */
	String SCOPE_PROP = "scope"; //$NON-NLS-1$

	/**
	 * Name of the book mark property which specifies the id of header cells.
	 */
	String BOOKMARK_PROP = "bookmark"; //$NON-NLS-1$

	/**
	 * Name of the display name property for bookmark
	 */
	String BOOKMARK_DISPLAY_NAME_PROP = "bookmarkDisplayName"; //$NON-NLS-1$

	/**
	 * Name of the headers property. User can choose book mark defined as headers of
	 * the data cell.
	 */
	String HEADERS_PROP = "headers"; //$NON-NLS-1$

	/**
	 * Name of the tag type property.
	 */
	String TAG_TYPE_PROP = "tagType"; //$NON-NLS-1$

	/**
	 * Name of the language property.
	 */
	String LANGUAGE_PROP = "language"; //$NON-NLS-1$

	/**
	 * Name of the altText property.
	 */
	String ALT_TEXT_PROP = "altText"; //$NON-NLS-1$

	/**
	 * Name of the altText key property.
	 */
	String ALT_TEXT_KEY_PROP = "altTextID"; //$NON-NLS-1$

}
