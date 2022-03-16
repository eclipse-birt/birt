/*******************************************************************************
 * Copyright (c) 2008, 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
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

public class SchemaNode extends ChildrenAllowedNode implements Comparable<SchemaNode> {

	private static Logger logger = Logger.getLogger(SchemaNode.class.getName());
	private static String SCHEMA_ICON = SchemaNode.class.getName() + ".SchemaIcon";
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(SCHEMA_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/schema.gif"));//$NON-NLS-1$
	}

	private String schemaName;

	public SchemaNode(String schemaName) {
		this.schemaName = schemaName;
	}

	@Override
	protected IDBNode[] refetchChildren(FilterConfig fc) {
		String[] tableTypes = fc.getTableTypesForJDBC();
		List<IDBNode> children = new ArrayList<>();
		if (tableTypes != null) {
			if (tableTypes.length == 1 && TableType.NO_LIMIT.getTypeName().equals(tableTypes[0])) {
				tableTypes = null;
			}

			ResultSet rs = JdbcMetaDataProvider.getInstance().getAlltables(schemaName, fc.getNamePattern(), tableTypes);

			if (rs != null) {
				int maxTableCountPerSchema = fc.getMaxTableCountPerSchema();
				int count = 0;
				try {
					while (rs.next() && count < maxTableCountPerSchema) {
						String tableName = rs.getString("TABLE_NAME");
						String type = rs.getString("TABLE_TYPE");//$NON-NLS-1$
						TableNode table = new TableNode(schemaName, tableName, "VIEW".equalsIgnoreCase(type));
						children.add(table);
						count++;
					}
				} catch (SQLException e) {
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}
		}
		if (JdbcMetaDataProvider.getInstance().isSupportProcedure()
				&& (fc.getType() == FilterConfig.Type.ALL || fc.getType() == FilterConfig.Type.PROCEDURE)) {
			children.add(new ProcedureFlagNode(schemaName));
		}
		return children.toArray(new IDBNode[0]);
	}

	@Override
	public int compareTo(SchemaNode o) {
		return schemaName.compareTo(o.schemaName);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	@Override
	public String getDisplayName(String metadataBidiFormatStr) {
		return BidiTransform.transform(schemaName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	@Override
	public Image getImage() {
		return JFaceResources.getImageRegistry().get(SCHEMA_ICON);
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	@Override
	public String getQualifiedNameInSQL(boolean useIdentifierQuoteString, boolean includeSchema,
			String metadataBidiFormatStr) {
		String quoteFlag = "";
		if (useIdentifierQuoteString) {
			quoteFlag = JdbcMetaDataProvider.getInstance().getIdentifierQuoteString();
		}
		// bidi_hcg: pass value of metadataBidiFormatStr
		return Utility.quoteString(
				BidiTransform.transform(schemaName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR),
				quoteFlag);
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}
}
