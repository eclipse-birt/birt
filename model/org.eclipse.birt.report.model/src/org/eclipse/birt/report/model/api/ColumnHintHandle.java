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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;

/**
 * Represents the handle of column hint. The column hint provides the hint about
 * result set columns.
 * 
 * <dl>
 * <dt><strong>Column Name </strong></dt>
 * <dd>a column hint has a required name. It keys the column hint to a column
 * within the result set.</dd>
 * 
 * <dt><strong>Alias </strong></dt>
 * <dd>a column hint has an optional alias. It provides an 'alias' name used for
 * the column within the report.</dd>
 * 
 * <dt><strong>Searching </strong></dt>
 * <dd>a column hint has an optional searching. It indicates how the column will
 * be used when searching.</dd>
 * 
 * <dt><strong>Export </strong></dt>
 * <dd>a column hint has an optional export. It determines how the column will
 * be used when exporting data.</dd>
 * 
 * <dt><strong>Analysis </strong></dt>
 * <dd>a column hint has an optional analysis. It determines how the column is
 * used when exporting the data to an OLAP cube.</dd>
 * 
 * <dt><strong>Parent Level </strong></dt>
 * <dd>a column hint has an optional parent level. It is used when a column's
 * <code>ANALYSIS_MEMBER</code> property is set to
 * <code>ANALYSIS_TYPE_DIMENSION</code> or <code>ANALYSIS_TYPE_DETAIL</code>.
 * For <code>ANALYSIS_TYPE_DIMENSION</code>, this property establishes the
 * dimension hierarchy.</dd>
 * 
 * <dt><strong>Format </strong></dt>
 * <dd>a column hint has an optional format. It is used to format the column
 * data when displaying the value in the viewing UI, especially within the
 * search results.</dd>
 * 
 * <dt><strong>Display Name </strong></dt>
 * <dd>a column hint has an optional display name. It provides the an optional
 * localizable display name for the column.</dd>
 * 
 * <dt><strong>Display Name ID </strong></dt>
 * <dd>a column hint has an optional display name ID. It provides the key to
 * localize the display name.</dd>
 * 
 * <dt><strong>Help Text </strong></dt>
 * <dd>a column hint has an optional help text. It provides optional localizable
 * descriptive text that explains the column to the end user.</dd>
 * 
 * <dt><strong>Help Text ID </strong></dt>
 * <dd>a column hint has an optional help text ID. It provides the key to
 * localize the help text.</dd>
 * </dl>
 * 
 */
public class ColumnHintHandle extends StructureHandle {

	/**
	 * Constructs the handle of computed column.
	 * 
	 * @param valueHandle the value handle for computed column list of one property
	 * @param index       the position of this computed column in the list
	 */

	public ColumnHintHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Returns the alias name of this column.
	 * 
	 * @return the alias name of this column
	 */

	public String getAlias() {
		return getStringProperty(ColumnHint.ALIAS_MEMBER);
	}

	/**
	 * Sets the alias name of this column.
	 * 
	 * @param alias the alias name to set
	 */

	public void setAlias(String alias) {
		setPropertySilently(ColumnHint.ALIAS_MEMBER, alias);
	}

	/**
	 * Returns the analysis option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>ANALYSIS_TYPE_DIMENSION
	 * <li>ANALYSIS_TYPE_ATTRIBUTE
	 * <li>ANALYSIS_TYPE_MEASURE
	 * </ul>
	 * 
	 * @return the analysis option
	 */

	public String getAnalysis() {
		return getStringProperty(ColumnHint.ANALYSIS_MEMBER);
	}

	/**
	 * Sets the analysis option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>ANALYSIS_TYPE_DIMENSION
	 * <li>ANALYSIS_TYPE_ATTRIBUTE
	 * <li>ANALYSIS_TYPE_MEASURE
	 * </ul>
	 * 
	 * @param analysis the analysis option to set
	 * @throws SemanticException if the analysis is not in the choice list.
	 */

	public void setAnalysis(String analysis) throws SemanticException {
		setProperty(ColumnHint.ANALYSIS_MEMBER, analysis);
	}

	/**
	 * Returns the column name.
	 * 
	 * @return the column name
	 */

	public String getColumnName() {
		return getStringProperty(ColumnHint.COLUMN_NAME_MEMBER);
	}

	/**
	 * Sets the column name.
	 * 
	 * @param columnName the column name to set
	 * @throws SemanticException value required exception.
	 * 
	 */

	public void setColumnName(String columnName) throws SemanticException {
		setProperty(ColumnHint.COLUMN_NAME_MEMBER, columnName);
	}

	/**
	 * Returns the display name.
	 * 
	 * @return the display name
	 */

	public String getDisplayName() {
		return getStringProperty(ColumnHint.DISPLAY_NAME_MEMBER);
	}

	/**
	 * Sets the display name.
	 * 
	 * @param displayName the display name to set
	 */

	public void setDisplayName(String displayName) {
		setPropertySilently(ColumnHint.DISPLAY_NAME_MEMBER, displayName);
	}

	/**
	 * Returns the resource key for display name.
	 * 
	 * @return the resource key for display name
	 */

	public String getDisplayNameKey() {
		return getStringProperty(ColumnHint.DISPLAY_NAME_ID_MEMBER);
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param displayNameResourceKey the resource key to set
	 */

	public void setDisplayNameKey(String displayNameResourceKey) {
		setPropertySilently(ColumnHint.DISPLAY_NAME_ID_MEMBER, displayNameResourceKey);
	}

	/**
	 * Returns the export option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>EXPORT_TYPE_NONE
	 * <li>EXPORT_TYPE_IF_REALIZED
	 * <li>EXPORT_TYPE_ALWAYS
	 * </ul>
	 * 
	 * @return the export option
	 */

	public String getExport() {
		return getStringProperty(ColumnHint.EXPORT_MEMBER);
	}

	/**
	 * Sets the export option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>EXPORT_TYPE_NONE
	 * <li>EXPORT_TYPE_IF_REALIZED
	 * <li>EXPORT_TYPE_ALWAYS
	 * </ul>
	 * 
	 * @param export the export option to set
	 * @throws SemanticException if the export is not in the choice list.
	 */

	public void setExport(String export) throws SemanticException {
		setProperty(ColumnHint.EXPORT_MEMBER, export);
	}

	/**
	 * Returns the format option.
	 * 
	 * @return the format option
	 * @deprecated
	 */

	public String getFormat() {
		return getStringProperty(ColumnHint.FORMAT_MEMBER);
	}

	/**
	 * Sets the format option.
	 * 
	 * @param format the format option to set
	 * @deprecated
	 */

	public void setFormat(String format) {
		setPropertySilently(ColumnHint.FORMAT_MEMBER, format);
	}

	/**
	 * Returns the help text.
	 * 
	 * @return the help text.
	 */

	public String getHelpText() {
		return getStringProperty(ColumnHint.HELP_TEXT_MEMBER);
	}

	/**
	 * Sets the help text.
	 * 
	 * @param helpText the help text to set
	 */

	public void setHelpText(String helpText) {
		setPropertySilently(ColumnHint.HELP_TEXT_MEMBER, helpText);
	}

	/**
	 * Returns the resource key for help text.
	 * 
	 * @return the resource key for help text
	 */

	public String getHelpTextKey() {
		return getStringProperty(ColumnHint.HELP_TEXT_ID_MEMBER);
	}

	/**
	 * Sets the resource key for help text.
	 * 
	 * @param helpTextResourceKey the resource key to set
	 */

	public void setHelpTextKey(String helpTextResourceKey) {
		setPropertySilently(ColumnHint.HELP_TEXT_ID_MEMBER, helpTextResourceKey);
	}

	/**
	 * Returns the parent level.
	 * 
	 * @return the parent level
	 */

	public String getParentLevel() {
		return getStringProperty(ColumnHint.PARENT_LEVEL_MEMBER);
	}

	/**
	 * Sets the parent level.
	 * 
	 * @param parentLevel the parent level to set
	 */

	public void setParentLevel(String parentLevel) {
		setPropertySilently(ColumnHint.PARENT_LEVEL_MEMBER, parentLevel);
	}

	/**
	 * Returns the searching option. The possible values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>SEARCH_TYPE_ANY
	 * <li>SEARCH_TYPE_INDEXED
	 * <li>SEARCH_TYPE_NONE
	 * </ul>
	 * 
	 * @return the searching option
	 */

	public String getSearching() {
		return getStringProperty(ColumnHint.SEARCHING_MEMBER);
	}

	/**
	 * Sets the searching option. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, and
	 * they are
	 * <ul>
	 * <li>SEARCH_TYPE_ANY
	 * <li>SEARCH_TYPE_INDEXED
	 * <li>SEARCH_TYPE_NONE
	 * </ul>
	 * 
	 * @param searching the searching option to set
	 * @throws SemanticException if the searching is not in the choice list.
	 */

	public void setSearching(String searching) throws SemanticException {
		setProperty(ColumnHint.SEARCHING_MEMBER, searching);
	}

	/**
	 * Gets the hint if the dimension data element should be layout on column.
	 * 
	 * @return true if the dimension data element should be layout on column, or
	 *         false the dimension data element should be layout on row.
	 */
	public boolean isOnColumnLayout() {
		Boolean onColumnLayout = (Boolean) getProperty(ColumnHint.ON_COLUMN_LAYOUT_MEMBER);
		if (onColumnLayout != null) {
			return onColumnLayout.booleanValue();
		}
		return false;
	}

	/**
	 * Sets the hint if the dimension data element should be layout on column.
	 * 
	 * @param onColumnLayout the hint if the dimension data element should be layout
	 *                       on column.
	 * @throws SemanticException
	 */
	public void setOnColumnLayout(boolean onColumnLayout) throws SemanticException {
		setProperty(ColumnHint.ON_COLUMN_LAYOUT_MEMBER, onColumnLayout);
	}

	/**
	 * Gets the heading of the column.
	 * 
	 * @return the heading
	 * 
	 */
	public String getHeading() {
		return getStringProperty(ColumnHint.HEADING_MEMBER);
	}

	/**
	 * Sets the heading of the column.
	 * 
	 * @param heading the new heading
	 * 
	 */
	public void setHeading(String heading) {
		setPropertySilently(ColumnHint.HEADING_MEMBER, heading);
	}

	/**
	 * Gets the resource key of the heading of the column.
	 * 
	 * @return the resource key of the heading
	 * 
	 */
	public String getHeadingKey() {
		return getStringProperty(ColumnHint.HEADING_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the heading of the column.
	 * 
	 * @param headingID the new resource key of the heading
	 * 
	 */
	public void setHeadingKey(String headingID) {
		setPropertySilently(ColumnHint.HEADING_ID_MEMBER, headingID);
	}

	/**
	 * Gets the display length of the column.
	 * 
	 * @return the display length
	 * @deprecated
	 */
	public int getDisplayLength() {
		Object displayLength = getProperty(ColumnHint.DISPLAY_LENGTH_MEMBER);
		if (displayLength instanceof Integer) {
			return ((Integer) displayLength).intValue();
		}
		return 0;
	}

	/**
	 * Sets the display length of the column.
	 * 
	 * @param displayLength the new display length
	 * @deprecated
	 */
	public void setDisplayLength(int displayLength) {
		setPropertySilently(ColumnHint.DISPLAY_LENGTH_MEMBER, displayLength);
	}

	/**
	 * Gets the horizontal alignment of the column. The returned value may be one of
	 * the constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @return the horizontal alignment
	 */
	public String getHorizontalAlign() {
		return getStringProperty(ColumnHint.HORIZONTAL_ALIGN_MEMBER);
	}

	/**
	 * Sets the horizontal alignment of the column. The value should be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * <ul>
	 * <li><code>TEXT_ALIGN_LEFT</code>
	 * <li><code>TEXT_ALIGN_RIGHT</code>
	 * <li><code>TEXT_ALIGN_CENTER</code>
	 * <li><code>TEXT_ALIGN_JUSTIFY</code>
	 * </ul>
	 * 
	 * @param align the new horizontal alignment
	 * @throws SemanticException if the align is not defined.
	 */
	public void setHorizontalAlign(String align) throws SemanticException {
		setProperty(ColumnHint.HORIZONTAL_ALIGN_MEMBER, align);
	}

	/**
	 * Gets the hint if the word needs to wrap.
	 * 
	 * @return true if the word needs to wrap, otherwise false.
	 * @deprecated
	 */
	public boolean wordWrap() {
		Boolean isWordWrap = (Boolean) getProperty(ColumnHint.WORD_WRAP_MEMBER);
		if (isWordWrap != null) {
			return isWordWrap.booleanValue();
		}
		return false;
	}

	/**
	 * Sets the hint if the word needs to wrap.
	 * 
	 * @param wordWrap the hint value indicates if the word needs to wrap.
	 * @deprecated
	 */
	public void setWordWrap(boolean wordWrap) {
		setPropertySilently(ColumnHint.WORD_WRAP_MEMBER, wordWrap);
	}

	/**
	 * Gets the text format of the column. The returned value may be one of the
	 * constants defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>STRING_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>STRING_FORMAT_TYPE_UPPERCASE</code>
	 * <li><code>STRING_FORMAT_TYPE_LOWERCASE</code>
	 * <li><code>STRING_FORMAT_TYPE_CUSTOM</code>
	 * <li><code>STRING_FORMAT_TYPE_ZIP_CODE</code>
	 * <li><code>STRING_FORMAT_TYPE_ZIP_CODE_4</code>
	 * <li><code>STRING_FORMAT_TYPE_PHONE_NUMBER</code>
	 * <li><code>STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER</code>
	 * </ul>
	 * 
	 * @return the text format of the column
	 * @deprecated
	 */
	public String getTextFormat() {
		return getStringProperty(ColumnHint.TEXT_FORMAT_MEMBER);
	}

	/**
	 * Sets the text format of the column. The value should be one of the constants
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>STRING_FORMAT_TYPE_UNFORMATTED</code>
	 * <li><code>STRING_FORMAT_TYPE_UPPERCASE</code>
	 * <li><code>STRING_FORMAT_TYPE_LOWERCASE</code>
	 * <li><code>STRING_FORMAT_TYPE_CUSTOM</code>
	 * <li><code>STRING_FORMAT_TYPE_ZIP_CODE</code>
	 * <li><code>STRING_FORMAT_TYPE_ZIP_CODE_4</code>
	 * <li><code>STRING_FORMAT_TYPE_PHONE_NUMBER</code>
	 * <li><code>STRING_FORMAT_TYPE_SOCIAL_SECURITY_NUMBER</code>
	 * </ul>
	 * 
	 * @param format the new text format
	 * @throws SemanticException if the format is not defined.
	 * @deprecated
	 */
	public void setTextFormat(String format) throws SemanticException {
		setProperty(ColumnHint.TEXT_FORMAT_MEMBER, format);
	}

	/**
	 * Gets the description of the column.
	 * 
	 * @return the description
	 * @deprecated
	 */
	public String getDescription() {
		return getStringProperty(ColumnHint.DESCRIPTION_MEMBER);
	}

	/**
	 * Sets the description of the column.
	 * 
	 * @param description the new description
	 * @deprecated
	 */
	public void setDescription(String description) {
		setPropertySilently(ColumnHint.DESCRIPTION_MEMBER, description);
	}

	/**
	 * Gets the resource key of the description of the column.
	 * 
	 * @return the resource key of the description
	 * @deprecated
	 */
	public String getDescriptionKey() {
		return getStringProperty(ColumnHint.DESCRIPTION_ID_MEMBER);
	}

	/**
	 * Sets the resource key of the description of the column.
	 * 
	 * @param descriptionID the new resource key of the description
	 * @deprecated
	 */
	public void setDescriptionKey(String descriptionID) {
		setPropertySilently(ColumnHint.DESCRIPTION_ID_MEMBER, descriptionID);
	}

	/**
	 * Returns a handle to work with the action property, action is a structure that
	 * defines a hyperlink.
	 * 
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the column hint; otherwise return null
	 * @see ActionHandle
	 */

	public ActionHandle getActionHandle() {
		MemberHandle memberHandle = getMember(ColumnHint.ACTION_MEMBER);
		Action action = (Action) memberHandle.getValue();

		if (action == null)
			return null;

		return (ActionHandle) action.getHandle(memberHandle);
	}

	/**
	 * Set an action on the image.
	 * 
	 * @param action new action to be set on the image, it represents a bookmark
	 *               link, hyperlink, and drill through etc.
	 * @return a handle to the action property, return <code>null</code> if the
	 *         action has not been set on the image.
	 * 
	 * @throws SemanticException if member of the action is not valid.
	 */

	public ActionHandle setAction(Action action) throws SemanticException {
		MemberHandle memberHandle = getMember(ColumnHint.ACTION_MEMBER);
		memberHandle.setValue(action);

		Action actionValue = (Action) memberHandle.getValue();
		if (actionValue == null)
			return null;
		return (ActionHandle) actionValue.getHandle(memberHandle);
	}

	/**
	 * Gets the expression handle for the <code>ACLExpression</code> member.
	 * 
	 * @return
	 */
	public ExpressionHandle getACLExpression() {
		return getExpressionProperty(ColumnHint.ACL_EXPRESSION_MEMBER);
	}

	/**
	 * Gets the analysis column.
	 * 
	 * @return the analysis column.
	 */
	public String getAnalysisColumn() {
		return getStringProperty(ColumnHint.ANALYSIS_COLUMN_MEMBER);
	}

	/**
	 * Sets the analysis column.
	 * 
	 * @param column the column to set
	 * @throws SemanticException
	 */
	public void setAnalysisColumn(String column) throws SemanticException {
		setProperty(ColumnHint.ANALYSIS_COLUMN_MEMBER, column);
	}

	/**
	 * Gets the value format of the column.
	 * 
	 * @return the value format
	 */
	public FormatValue getValueFormat() {
		return (FormatValue) getProperty(ColumnHint.VALUE_FORMAT_MEMBER);
	}

	/**
	 * Sets the value format of the column.
	 * 
	 * @param format the value format to set
	 * @throws SemanticException
	 */
	public void setValueFormat(FormatValue format) throws SemanticException {
		setProperty(ColumnHint.VALUE_FORMAT_MEMBER, format);
	}

	/**
	 * Gets the flag which indicates whether the column should be generated with
	 * index.
	 * 
	 * @return true if the column should be generated with index, otherwise false
	 */
	public boolean isIndexColumn() {
		Boolean indexColumn = (Boolean) getProperty(ColumnHint.INDEX_COLUMN_MEMBER);
		if (indexColumn != null) {
			return indexColumn.booleanValue();
		}
		return false;
	}

	/**
	 * Sets the flag which indicates whether the column should be generated with
	 * index.
	 * 
	 * @param indexColumn the new flag to set
	 * @throws SemanticException
	 */
	public void setIndexColumn(boolean indexColumn) throws SemanticException {
		setProperty(ColumnHint.INDEX_COLUMN_MEMBER, indexColumn);
	}

	/**
	 * Gets the flag which indicates whether the column needs to be compressed.
	 * 
	 * @return true if the column needs to be compressed, otherwise false.
	 */
	public boolean isCompressed() {
		Boolean compressed = (Boolean) getProperty(ColumnHint.COMPRESSED_MEMBER);
		if (compressed != null) {
			return compressed.booleanValue();
		}
		return false;
	}

	/**
	 * Sets the flag which indicates whether the column needs to be compressed.
	 * 
	 * @param compressed the new flag to set
	 * @throws SemanticException
	 */
	public void setCompresssed(boolean compressed) throws SemanticException {
		setProperty(ColumnHint.COMPRESSED_MEMBER, compressed);
	}
}
