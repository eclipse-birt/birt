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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Image;

public class RootNode extends ChildrenAllowedNode {
	private static Logger logger = Logger.getLogger(RootNode.class.getName());
	private static String ROOT_ICON = RootNode.class.getName() + ".RootIcon";
	static {
		ImageRegistry reg = JFaceResources.getImageRegistry();
		reg.put(ROOT_ICON, ImageDescriptor.createFromFile(JdbcPlugin.class, "icons/data_source.gif"));//$NON-NLS-1$
	}

	private String dataSourceName;
	private String[] prefetechedSchemas;

	public RootNode(String dataSourceName) {
		this(dataSourceName, null);
	}

	public RootNode(String dataSourceName, String[] allSchemaNames) {
		assert dataSourceName != null;
		this.dataSourceName = dataSourceName;
		this.prefetechedSchemas = allSchemaNames;
	}

	@Override
	protected IDBNode[] refetchChildren(FilterConfig fc) {
		String namePattern = fc.getNamePattern();
		boolean supportsSchema = JdbcMetaDataProvider.getInstance().isSupportSchema();
		boolean supportsProcedure = JdbcMetaDataProvider.getInstance().isSupportProcedure();
		if (namePattern.equals("%")) {
			if (supportsSchema) {
				// just list schemas
				if (fc.getSchemaName() != null) {
					IDBNode node = fetchSpecifiedSchema(fc.getSchemaName());
					return node == null ? new IDBNode[0] : new IDBNode[] { node };
				} else if (prefetechedSchemas == null) {
					return fetchChildrenFromSchema(fc.getMaxSchemaCount());
				} else {
					// already know all schema names
					List<SchemaNode> schemas = new ArrayList<>();
					int count = 0;
					for (String name : prefetechedSchemas) {
						if (count >= fc.getMaxSchemaCount()) {
							break;
						}
						schemas.add(new SchemaNode(name));
						count++;
					}
					return schemas.toArray(new IDBNode[0]);
				}
			} else // not support schema
			{
				// create a dummy schema node and reuse its refetchChildren() method
				return new SchemaNode(null).refetchChildren(fc);
			}
		} else // user input a filter string
		{
			Map<String, TablesAndProcedures> schemas = generateSchemaToTablesAndProceduresMap(fc, supportsProcedure);
			return fetchChildrenFromMap(schemas, fc.getMaxSchemaCount());
		}
	}

	private Map<String, TablesAndProcedures> generateSchemaToTablesAndProceduresMap(FilterConfig fc,
			boolean supportsProcedure) {
		Map<String, TablesAndProcedures> schemas = new HashMap<>();
		String[] tableTypes = fc.getTableTypesForJDBC();
		if (tableTypes != null) {
			if (tableTypes.length == 1 && TableType.NO_LIMIT.getTypeName().equals(tableTypes[0])) {
				tableTypes = null;
			}

			ResultSet rs = JdbcMetaDataProvider.getInstance().getAlltables(fc.getSchemaName(), fc.getNamePattern(),
					tableTypes);
			if (rs != null) {
				try {
					while (rs.next()) {
						String schemaName = rs.getString("TABLE_SCHEM");
						String tableName = rs.getString("TABLE_NAME");
						String type = rs.getString("TABLE_TYPE");//$NON-NLS-1$
						TableNode table = new TableNode(schemaName, tableName,
								FilterConfig.JDBC_TYPE_VIEW.equalsIgnoreCase(type));
						TablesAndProcedures tap = schemas.get(schemaName);
						if (tap == null) {
							tap = new TablesAndProcedures();
							schemas.put(schemaName, tap);
						}
						if (tap.getTableCount() < fc.getMaxTableCountPerSchema()) {
							tap.addTable(table);
						}
					}
				} catch (SQLException e) {
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}
		}
		if (supportsProcedure
				&& (fc.getType() == FilterConfig.Type.ALL || fc.getType() == FilterConfig.Type.PROCEDURE)) {
			ResultSet rs = JdbcMetaDataProvider.getInstance().getProcedures(fc.getSchemaName(), fc.getNamePattern());
			if (rs != null) {
				try {
					while (rs.next()) {
						String schemaName = rs.getString("PROCEDURE_SCHEM");
						String procedureName = rs.getString("PROCEDURE_NAME");
						ProcedureNode procedure = new ProcedureNode(schemaName, procedureName);
						TablesAndProcedures tap = schemas.get(schemaName);
						if (tap == null) {
							tap = new TablesAndProcedures();
							schemas.put(schemaName, tap);
						}
						tap.addProcedure(procedure);
					}
				} catch (SQLException e) {
					logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				}
			}
		}
		return schemas;
	}

	private IDBNode[] fetchChildrenFromMap(Map<String, TablesAndProcedures> schemas, int maxSchemaCount) {
		List<SchemaNode> result = new ArrayList<>();
		int count = 0;
		for (Entry<String, TablesAndProcedures> entry : schemas.entrySet()) {
			if (count >= maxSchemaCount) {
				break;
			}
			SchemaNode schema = new SchemaNode(entry.getKey());
			TablesAndProcedures tap = entry.getValue();
			List<IDBNode> children = new ArrayList<>();
			children.addAll(Arrays.asList(tap.getTables()));
			if (tap.getProcedureCount() > 0) {
				ProcedureFlagNode procedureFlagNode = new ProcedureFlagNode(entry.getKey());
				procedureFlagNode.setChildren(tap.getProcedures());
				children.add(procedureFlagNode);
			}
			schema.setChildren(children.toArray(new IDBNode[0]));
			result.add(schema);

			++count;
		}
		if (result.size() == 1 && result.get(0).getSchemaName() == null) {
			// not support schema;
			return result.get(0).getChildren();
		} else {
			Collections.sort(result);
			return result.toArray(new IDBNode[0]);
		}
	}

	private IDBNode[] fetchChildrenFromSchema(int maxSchemaCount) {
		List<SchemaNode> schemas = new ArrayList<>();
		ResultSet rs = JdbcMetaDataProvider.getInstance().getAllSchemas();
		if (rs != null) {
			int count = 0;

			try {
				while (rs.next() && count < maxSchemaCount) {
					String name = rs.getString("TABLE_SCHEM");
					SchemaNode schema = new SchemaNode(name);
					schemas.add(schema);
					count++;
				}
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}
		return schemas.toArray(new SchemaNode[0]);
	}

	private IDBNode fetchSpecifiedSchema(String schemaName) {
		assert schemaName != null;
		IDBNode result = null;
		ResultSet rs = JdbcMetaDataProvider.getInstance().getAllSchemas();
		if (rs != null) {
			try {
				while (rs.next()) {
					String name = rs.getString("TABLE_SCHEM");
					if (name.equals(schemaName)) {
						return new SchemaNode(name);
					}
				}
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}
		return result;
	}

	// bidi_hcg: add metadataBidiFormatStr parameter to allow Bidi transformations
	// (if required)
	@Override
	public String getDisplayName(String metadataBidiFormatStr) {
		return BidiTransform.transform(dataSourceName, metadataBidiFormatStr, BidiConstants.DEFAULT_BIDI_FORMAT_STR);
	}

	@Override
	public Image getImage() {
		return JFaceResources.getImageRegistry().get(ROOT_ICON);
	}

	/**
	 * Can't be a part of SQL text
	 */
	@Override
	public String getQualifiedNameInSQL(boolean useIdentifierQuoteString, boolean includeSchema,
			String metadataBidiFormatStr) {
		return null;
	}

	private static class TablesAndProcedures {
		private List<TableNode> tables = new ArrayList<>();
		private List<ProcedureNode> procedures = new ArrayList<>();

		public void addTable(TableNode table) {
			assert table != null;
			tables.add(table);
		}

		public void addProcedure(ProcedureNode procedure) {
			assert procedure != null;
			procedures.add(procedure);
		}

		public int getTableCount() {
			return tables.size();
		}

		public int getProcedureCount() {
			return procedures.size();
		}

		public TableNode[] getTables() {
			return tables.toArray(new TableNode[0]);
		}

		public ProcedureNode[] getProcedures() {
			return procedures.toArray(new ProcedureNode[0]);
		}
	}
}
