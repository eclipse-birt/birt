/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.hive.ui.profile;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.data.oda.jdbc.ui.editors.SQLDataSetEditorPage;
import org.eclipse.birt.report.data.oda.jdbc.ui.model.TableType;
import org.eclipse.birt.report.data.oda.jdbc.ui.provider.JdbcMetaDataProvider;

public class HiveDataSetWizardPage extends SQLDataSetEditorPage {

	public HiveDataSetWizardPage(String pageName) {
		super(pageName);
	}

	protected List<TableType> getTableTypes(boolean supportsProcedure) {
		List<TableType> types = new ArrayList<TableType>();

		// Populate the Types of Data bases objects which can be retrieved
		types.add(TableType.NO_LIMIT);
		String[] tableTypes = JdbcMetaDataProvider.getInstance().getTableTypeNames(timeOutLimit * 1000);

		for (int i = 0; i < tableTypes.length; i++) {
			types.add(new TableType(null, tableTypes[i], tableTypes[i]));
		}

		return types;
	}

}
