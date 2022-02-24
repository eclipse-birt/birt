/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.sql.Types;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;

/**
 * The data set item used in data UI.
 *
 */
public class DataSetViewData {

	private static IChoice[] dataTypes = DEUtil.getMetaDataDictionary()
			.getStructure(ResultSetColumn.RESULT_SET_COLUMN_STRUCT).getMember(ResultSetColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices().getChoices();

	private transient String dataSetColumnName = null;

	private transient String name = null;

	private transient Object parent = null;

	private transient String dataTypeName = null;

	private transient int dataType = Types.VARCHAR;

	private transient int position = -1;

	private transient String alias = null;

	private transient String helpText = null;

	private transient boolean isComputedColumn = false;

	private transient String displayNameKey = null;

	private transient String analysis = null;

	private transient String format = null;

	private transient int displayLength = 0;

	private transient String heading = null;

	private transient String horizontalAlign = null;

	private transient String textFormat = null;

	private transient String description = null;

	private transient boolean wordWrap = false;

	private transient ExpressionHandle aclExpr = null;

	private transient String externalizedName = null;

	private transient String analysisColumn = null;

	private FormatValue formatValue = null;

	private transient boolean isIndexColumn = false;

	private boolean removeDuplicateValues = false;

	private ActionHandle actionHandle = null;

	public String getAnalysisColumn() {
		return analysisColumn;
	}

	public void setAnalysisColumn(String analysisColumn) {
		this.analysisColumn = analysisColumn;
	}

	/**
	 * @return the ExternalizedName.
	 */
	public final String getExternalizedName() {
		return externalizedName;
	}

	/**
	 * @param externalizedName The externalizedName to set.
	 */
	public final void setExternalizedName(String externalizedName) {
		this.externalizedName = externalizedName;
	}

	/**
	 * @return the dataType.
	 */
	public final int getDataType() {
		return dataType;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public final void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return The dataTypeName.
	 */
	public final String getDataTypeName() {
		return dataTypeName;
	}

	/**
	 * @return
	 */
	public final String getDataTypeDisplayName() {
		return getDataTypeDisplayName(this.getDataTypeName());
	}

	/**
	 * @param dataTypeName The dataTypeName to set.
	 */
	public final void setDataTypeName(String dataTypeName) {
		this.dataTypeName = dataTypeName;
	}

	private transient String displayName = null;

	/**
	 * This method returns the display name and if the display name is null it
	 * returns the name
	 *
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		if (displayName != null && displayName.trim().length() > 0) {
			return displayName;
		}

		if (alias != null && alias.trim().length() > 0) {
			return alias;
		}

		return getName();
	}

	/**
	 * This method just returns the display name. It doesn't do any null checks
	 *
	 * @return the display name
	 */
	public String getRealDisplayName() {
		return displayName;
	}

	/**
	 * This is equivalent to setDisplayName. It is just added for conveninence in
	 * case of java bean introspection.
	 *
	 * @param displayName
	 */
	public void setRealDisplayName(String displayName) {
		setDisplayName(displayName);
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		if (name == null || name.trim().length() == 0) {
			return getDataSetColumnName();
		}
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the parent.
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Object parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position The position to set.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return Returns the alias.
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias The alias to set.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return Returns the helpText.
	 */
	public String getHelpText() {
		return helpText;
	}

	/**
	 * @param helpText The helpText to set.
	 */
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	/**
	 * @return Returns the dataSetColumnName.
	 */
	public String getDataSetColumnName() {
		return dataSetColumnName;
	}

	/**
	 * @param dataSetColumnName The dataSetColumnName to set.
	 */
	public void setDataSetColumnName(String dataSetColumnName) {
		this.dataSetColumnName = dataSetColumnName;
	}

	/**
	 * @return Returns the isComputedColumn.
	 */
	public boolean isComputedColumn() {
		return isComputedColumn;
	}

	/**
	 * @param isComputedColumn The isComputedColumn to set.
	 */
	public void setComputedColumn(boolean isComputedColumn) {
		this.isComputedColumn = isComputedColumn;
	}

	/**
	 *
	 * @return
	 */
	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 *
	 * @param displayNameKey
	 */
	public void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}

	public static String getDataTypeDisplayName(String typeName) {
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].getName().equals(typeName)) {
				return dataTypes[i].getDisplayName();
			}
		}
		return typeName;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setACLExpression(ExpressionHandle accessExpr) {
		this.aclExpr = accessExpr;
	}

	public ExpressionHandle getACLExpression() {
		return aclExpr;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public void setDisplayLength(int displayLength) {
		this.displayLength = displayLength;
	}

	public int getDisplayLength() {
		return displayLength;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	public String getHeading() {
		return heading;
	}

	public void setHorizontalAlign(String horizontalAlign) {
		this.horizontalAlign = horizontalAlign;
	}

	public String getHorizontalAlign() {
		return horizontalAlign;
	}

	public void setTextFormat(String textFormat) {
		this.textFormat = textFormat;
	}

	public String getTextFormat() {
		return textFormat;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setWordWrap(boolean wordWrap) {
		this.wordWrap = wordWrap;
	}

	public boolean isWordWrap() {
		return wordWrap;
	}

	public void setFormatValue(FormatValue formatValue) {
		this.formatValue = formatValue;
	}

	public FormatValue getFormatValue() {
		return formatValue;
	}

	/**
	 * @param isColumnIndex the isColumnIndex to set
	 */
	public void setIndexColumn(boolean isIndexColumn) {
		this.isIndexColumn = isIndexColumn;
	}

	/**
	 * @return the isColumnIndex
	 */
	public boolean isIndexColumn() {
		return isIndexColumn;
	}

	/**
	 * @param removeDuplicateValues the removeDuplicateColumn to set
	 */
	public void setRemoveDuplicateValues(boolean removeDuplicateValues) {
		this.removeDuplicateValues = removeDuplicateValues;
	}

	/**
	 * @return the removeDuplicateColumn
	 */
	public boolean removeDuplicateValues() {
		return removeDuplicateValues;
	}

	/**
	 *
	 * @return the ActionHandle instance
	 */
	public ActionHandle getActionHandle() {
		return actionHandle;
	}

	/**
	 *
	 * @param actionHandle the ActionHandle to set
	 */
	public void setActionHandle(ActionHandle actionHandle) {
		this.actionHandle = actionHandle;
	}
}
