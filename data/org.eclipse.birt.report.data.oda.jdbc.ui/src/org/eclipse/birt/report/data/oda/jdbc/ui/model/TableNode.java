/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Utility;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

public class TableNode extends ChildrenAllowedNode implements Comparable<TableNode> {
	private static Logger logger = Logger.getLogger(TableNode.class.getName());
	private static String TABLE_ICON = TableNode.class.getName() + ".TableIcon";
	private static String VIEW_ICON = TableNode.class.getName() + ".ViewIcon";
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(TABLE_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/table.gif"));//$NON-NLS-1$
		reg.put(VIEW_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/view.gif"));//$NON-NLS-1$
	}

	private String schemaName;
	private String tableName;
	private boolean isView;

	public TableNode(String schemaName, String tableName, boolean isView) {
		assert tableName != null;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.isView = isView;
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	public String getDisplayName(String metadataBidiFormatStr) {
		return BidiTransform.transform(tableName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	public Image getImage() {
		return isView ? JFaceResources.getImage(VIEW_ICON) : JFaceResources.getImage(TABLE_ICON);
	}

	public int compareTo(TableNode o) {
		/**
		 * In our case, 2 <code>TableNode</code> instances need to be compared
		 * <p>
		 * only when they belong to the same schema
		 */
		return this.tableName.compareTo(o.tableName);
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
		// bidi_hcg: perform Bidi transformations on schema and table names
		String schemaNameStr = schemaName;
		String tableNameStr = tableName;
		if (includeSchema && schemaName != null) {
			schemaNameStr = BidiTransform.transform(schemaName, metadataBidiFormatStr,
					BidiConstants.DEFAULT_BIDI_FORMAT_STR);
			sb.append(Utility.quoteString(schemaNameStr, quoteFlag)).append(".");
		}
		tableNameStr = BidiTransform.transform(tableNameStr, metadataBidiFormatStr,
				BidiConstants.DEFAULT_BIDI_FORMAT_STR);
		sb.append(Utility.quoteString(tableNameStr, quoteFlag));
		return sb.toString();
	}

	@Override
	protected IDBNode[] refetchChildren(FilterConfig fc) {
		List<TableColumnNode> columns = new ArrayList<TableColumnNode>();
		ResultSet rs = JdbcMetaDataProvider.getInstance().getTableColumns(schemaName, tableName, null);
		if (rs != null) {
			try {
				while (rs.next()) {
					String columnName = rs.getString("COLUMN_NAME");
					String typeName = rs.getString("TYPE_NAME");
					TableColumnNode column = new TableColumnNode(schemaName, tableName, columnName, typeName);
					columns.add(column);
				}
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
			}
		}
		return columns.toArray(new TableColumnNode[0]);
	}
}
