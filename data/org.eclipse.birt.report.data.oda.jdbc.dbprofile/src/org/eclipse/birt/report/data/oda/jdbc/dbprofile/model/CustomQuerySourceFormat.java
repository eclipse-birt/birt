/**
 *************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.model;

import org.eclipse.datatools.connectivity.sqm.core.definition.DatabaseDefinition;
import org.eclipse.datatools.modelbase.sql.query.util.SQLQuerySourceFormat;

/**
 * Extends DTP SQLQuerySourceFormat to include database definition that may be
 * used at runtime for customized formatting of the SQL query source.
 * 
 * @since 2.6.1
 */
public class CustomQuerySourceFormat extends SQLQuerySourceFormat {
	private DatabaseDefinition m_dbDefn;

	public CustomQuerySourceFormat(SQLQuerySourceFormat original, DatabaseDefinition dbDefn) {
		// calls the visible inherited constructor
		super(original.isPreserveSourceFormat(), original.getStatementTerminator(), original.getHostVariablePrefix(),
				original.getParameterMarker(), original.getDelimitedIdentifierQuote(), original.getOmitSchema(),
				original.getQualifyIdentifiers(), original.getPreserveComments(),
				original.isGenerateCommentsForStatementOnly());

		// make sure all inherited fields are copied, in case new ones got added
		// besides those specified in base class contructor
		copyFields(original, this);

		setDatabaseDefinition(dbDefn);
	}

	protected void setDatabaseDefinition(DatabaseDefinition dbDefn) {
		m_dbDefn = dbDefn;
	}

	public DatabaseDefinition getDatabaseDefinition() {
		return m_dbDefn;
	}

}
