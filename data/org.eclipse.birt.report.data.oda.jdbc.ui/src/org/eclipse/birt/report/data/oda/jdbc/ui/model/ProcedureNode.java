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

public class ProcedureNode extends ChildrenAllowedNode implements Comparable<ProcedureNode> {
	private static Logger logger = Logger.getLogger(ProcedureNode.class.getName());
	private static String PROCEDURE_ICON = ProcedureNode.class.getName() + ".ProcedureIcon";
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(PROCEDURE_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/column.gif"));//$NON-NLS-1$
	}

	private String schemaName;
	private String procedureName;

	public ProcedureNode(String schemaName, String procedureName) {
		assert procedureName != null;
		this.schemaName = schemaName;
		this.procedureName = procedureName;
	}

	@Override
	public int compareTo(ProcedureNode o) {
		/**
		 * In our case, 2 <code>ProcedureNode</code> instances need to be compared
		 * <p>
		 * only when they belong to the same schema
		 */
		return this.procedureName.compareTo(o.procedureName);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	@Override
	public String getDisplayName(String metadataBidiFormatStr) {
		return BidiTransform.transform(procedureName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	@Override
	public Image getImage() {
		return JFaceResources.getImageRegistry().get(PROCEDURE_ICON);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	@Override
	public String getQualifiedNameInSQL(boolean useIdentifierQuoteString, boolean includeSchema,
			String metadataBidiFormatStr) {
		StringBuilder sb = new StringBuilder();
		String quoteFlag = "";
		if (useIdentifierQuoteString) {
			quoteFlag = JdbcMetaDataProvider.getInstance().getIdentifierQuoteString();
		}
		if (includeSchema && schemaName != null) {
			sb.append(Utility.quoteString(
					BidiTransform.transform(schemaName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR),
					quoteFlag)).append(".");
		}
		sb.append(Utility.quoteString(
				BidiTransform.transform(procedureName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR),
				quoteFlag));
		return sb.toString();
	}

	@Override
	protected IDBNode[] refetchChildren(FilterConfig fc) {
		List<ProcedureColumnNode> columns = new ArrayList<>();
		ResultSet rs = JdbcMetaDataProvider.getInstance().getProcedureColumns(schemaName, procedureName, null);
		if (rs != null) {
			try {
				int n = 0;
				while (rs.next()) {
					String columnName = rs.getString("COLUMN_NAME");
					if (columnName == null) {
						// if the column name cannot retrieved ,give the unique name for this column
						n++;
						columnName = "param" + n;
					}
					String mode = Utility.toModeType(rs.getInt("COLUMN_TYPE"));
					String type = rs.getString("TYPE_NAME");
					ProcedureColumnNode column = new ProcedureColumnNode(columnName, type, mode);
					columns.add(column);
				}
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}
		return columns.toArray(new ProcedureColumnNode[0]);
	}
}
