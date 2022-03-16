/*
 *************************************************************************
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
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.sqb;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DesignerState;
import org.eclipse.datatools.sqltools.sqlbuilder.SQLBuilder;
import org.eclipse.datatools.sqltools.sqlbuilder.input.SQLBuilderStorageEditorInput;
import org.eclipse.datatools.sqltools.sqlbuilder.util.SQLBuilderEditorInputUtil;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

/**
 * An internal class that represents the design state of the SQL Query Builder.
 * It is capable of saving and restoring its content to/from a serialized
 * format.
 */
public class SQLBuilderDesignState {
	private static final String SQB_STATE_CURRENT_VERSION = "1.0"; //$NON-NLS-1$
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private SQLBuilderStorageEditorInput m_sqbInput;
	private String m_preparableSQLText;
	private String m_version;

	/**
	 * Creates a SQB designer state based on the state of the specified SQLBuilder
	 * and data set design.
	 *
	 * @param dataSetDesign
	 * @param sqlBuilder
	 */
	SQLBuilderDesignState(String sqbInputName, final SQLBuilder sqlBuilder) {
		m_sqbInput = createSQBInput(sqbInputName, sqlBuilder);
		m_preparableSQLText = SQLQueryUtility.getPreparableSQL(sqlBuilder.getDomainModel().getSQLStatement());
	}

	/**
	 * Creates a SQB designer state from a previously saved ODA DesignerState.
	 *
	 * @param odaDesignerState
	 * @throws OdaException
	 */
	SQLBuilderDesignState(final DesignerState odaDesignerState) throws OdaException {
		if (odaDesignerState == null || odaDesignerState.getStateContent() == null) {
			throw new NullPointerException("SQLBuilderDesignState( DesignerState )"); //$NON-NLS-1$
		}

		// check the version compatibility of designerState
		m_version = odaDesignerState.getVersion();
		if (m_version == null || !m_version.equals(SQB_STATE_CURRENT_VERSION)) // currently supports a single version
																				// only
		{
			throw new OdaException(Messages.sqbDesignState_invalidSqbStateVersion);
		}

		// get the designer state content
		String designStateValue = odaDesignerState.getStateContent().getStateContentAsString();
		if (designStateValue == null) {
			return; // no state content
		}

		IMemento memento = SQLBuilderEditorInputUtil.readMementoFromString(designStateValue);

		DesignStateMemento.restoreState(memento, this);
	}

	/**
	 * Create a SQLBuilderStorageEditorInput and save the SQLStatement,
	 * ConnectionInfo OmitSchemaInfo InputUsageOptions and WindowStateInfo from the
	 * specified SQLBuilder.
	 *
	 * @param sqbInputName name of the created input instance
	 * @param sqlBuilder
	 * @return
	 */
	private static SQLBuilderStorageEditorInput createSQBInput(String sqbInputName, final SQLBuilder sqlBuilder) {
		SQLBuilderStorageEditorInput storageEditorInput = new SQLBuilderStorageEditorInput(sqbInputName,
				sqlBuilder.getSQL());
		storageEditorInput.setConnectionInfo(sqlBuilder.getConnectionInfo());
		storageEditorInput.setOmitSchemaInfo(sqlBuilder.getOmitSchemaInfo());
		storageEditorInput.setInputUsageOptions(sqlBuilder.getEditorInputUsageOptions());
		storageEditorInput.setWindowStateInfo(sqlBuilder.getWindowStateInfo());

		return storageEditorInput;
	}

	boolean hasSQBInput() {
		return m_sqbInput != null;
	}

	SQLBuilderStorageEditorInput getSQBStorageInput() {
		return m_sqbInput;
	}

	private void setSQBStorageInput(SQLBuilderStorageEditorInput sqbInput) {
		m_sqbInput = sqbInput;
	}

	String getPreparableSQL() {
		return m_preparableSQLText;
	}

	private void setPreparableSQL(String preparableSQLText) {
		m_preparableSQLText = preparableSQLText;
	}

	String getVersion() {
		if (m_version == null) {
			return SQB_STATE_CURRENT_VERSION;
		}
		return m_version;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if (m_sqbInput == null) {
			return EMPTY_STRING;
		}

		// Save the state to a XMLMemento
		XMLMemento memento = DesignStateMemento.saveState(this);

		// Write out memento to a string
		String sqbState = SQLBuilderEditorInputUtil.writeXMLMementoToString(memento);
		return sqbState;
	}

	/**
	 * Handler that saves and restores a design state instance to/from a serialized
	 * IMemento.
	 */
	static class DesignStateMemento {
		final static String KEY_PREPARABLE_SQL_TEXT = "preparableSQLText"; //$NON-NLS-1$

		static XMLMemento saveState(final SQLBuilderDesignState sqbState) {
			// Save the state of the SQLBuilderStorageEditorInput to a XMLMemento
			SQLBuilderStorageEditorInput sqbInput = sqbState.getSQBStorageInput();
			XMLMemento memento = SQLBuilderEditorInputUtil.saveSQLBuilderStorageEditorInput(sqbInput);

			// Save the data set design's preparable query text in the memento,
			// if it is syntactically different from the query text being edited in SQB
			String queryText = sqbState.getPreparableSQL();
			if (queryText != null && !SQLQueryUtility.isEquivalentSQL(queryText, sqbInput.getSQL())) {
				memento.putString(KEY_PREPARABLE_SQL_TEXT, queryText);
			}

			return memento;
		}

		static void restoreState(final IMemento memento, SQLBuilderDesignState sqbState) {
			sqbState.setSQBStorageInput(SQLBuilderEditorInputUtil.createSQLBuilderStorageEditorInput(memento));

			String queryText = memento.getString(KEY_PREPARABLE_SQL_TEXT);
			sqbState.setPreparableSQL(queryText); // could be null
		}
	}

}
