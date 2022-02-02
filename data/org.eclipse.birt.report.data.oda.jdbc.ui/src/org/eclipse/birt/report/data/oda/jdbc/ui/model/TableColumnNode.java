/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

public class TableColumnNode implements IDBNode, Comparable<TableColumnNode> {
	private static String COLUMN_ICON = TableColumnNode.class.getName() + ".ColumnIcon";
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(COLUMN_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/column.gif"));//$NON-NLS-1$
	}

	private String schemaName;
	private String tableName;
	private String columnName;
	private String typeName;

	public TableColumnNode(String schemaName, String tableName, String columnName, String typeName) {
		assert columnName != null && tableName != null;
		this.columnName = columnName;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.typeName = typeName;
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public String getDisplayName(String metadataBidiFormatStr) {
		return BidiTransform.transform(columnName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR) + " ("
				+ typeName + ")";
	}

	public Image getImage() {
		return JFaceResources.getImage(COLUMN_ICON);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public String getQualifiedNameInSQL(boolean useIdentifierQuoteString, boolean includeSchema,
			String metadataBidiFormatStr) {
		StringBuffer sb = new StringBuffer();
		String quoteFlag = "";
		if (useIdentifierQuoteString) {
			quoteFlag = JdbcMetaDataProvider.getInstance().getIdentifierQuoteString();
		}
		// bidi_hcg: perform required Bidi transformations
		String schemaNameStr = schemaName;
		String tableNameStr = tableName;
		String columnNameStr = columnName;

		if (includeSchema && schemaName != null) {
			if (metadataBidiFormatStr != null) {
				schemaNameStr = BidiTransform.transform(schemaName, metadataBidiFormatStr,
						BidiConstants.DEFAULT_BIDI_FORMAT_STR);
				tableNameStr = BidiTransform.transform(tableNameStr, metadataBidiFormatStr,
						BidiConstants.DEFAULT_BIDI_FORMAT_STR);
				columnNameStr = BidiTransform.transform(columnNameStr, metadataBidiFormatStr,
						BidiConstants.DEFAULT_BIDI_FORMAT_STR);
			}
			sb.append(Utility.quoteString(schemaNameStr, quoteFlag)).append(".");
		}
		sb.append(Utility.quoteString(tableNameStr, quoteFlag)).append(".");
		sb.append(Utility.quoteString(columnNameStr, quoteFlag));
		return sb.toString();
	}

	public int compareTo(TableColumnNode o) {
		/**
		 * In our case, 2 <code>TableColumn</code> instances need to be compared
		 * <p>
		 * only when they belong to the same <schema, table>
		 */
		return this.columnName.compareTo(o.columnName);
	}

}
