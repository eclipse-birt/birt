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

package org.eclipse.birt.data.oda.mongodb.nls;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.birt.data.oda.mongodb.nls.messages"; //$NON-NLS-1$

	public static String mDbConnection_failedToOpenConn;
	public static String mDbConnection_invalidDatabaseName;
	public static String mDbConnection_missingValueDBName;
	public static String mDbConnection_noConnection;

	public static String mDbDriver_missingValueServerHost;
	public static String mDbDriver_nonDefinedDataType;
	public static String mDbDriver_readPrefNearest;
	public static String mDbDriver_readPrefPrimary;
	public static String mDbDriver_readPrefPrimaryPreferred;
	public static String mDbDriver_readPrefSecondary;
	public static String mDbDriver_readPrefSecondaryPreferred;
	public static String mDbConnection_missingValueJaasConf;
	public static String mDbConnection_missingValueKrbConf;
	public static String mDbConnection_missingValueKrbPrinc;

	public static String mDbDSMetaData_dataSourceName;

	public static String mDbMetaData_invalidCollectionName;
	public static String mDbMetaData_missingCmdExprText;

	public static String mDbQuery_invalidQueryExecQuery;
	public static String mDbQuery_invalidQueryGetMD;

	public static String driverUtil_invalidExpr;
	public static String driverUtil_parsingError;

	public static String mDbOp_aggrCmdFailed;
	public static String mDbOp_dbCmdFailed;
	public static String mDbOp_invalidQueryExpr;
	public static String mDbOp_mapReduceCmdFailed;
	public static String mDbOp_noCmdResults;

	public static String mDbResultSet_cannotConvertFieldData;

	public static String queryModel_emptyExprErrorMsg;
	public static String queryModel_invalidCollectionName;
	public static String queryModel_invalidDbCmdKeyValue;
	public static String queryModel_invalidModelToExec;
	public static String queryModel_invalidModelToPrepare;
	public static String queryModel_invalidPipelineOp;
	public static String queryModel_invalidQuerySortExpr;
	public static String queryModel_invalidSortAggrValue;
	public static String queryModel_missingCollectionName;
	public static String queryModel_missingGroupAggrKey;
	public static String queryModel_missingMapReduceKey;
	public static String queryModel_missingMapReduceValue;
	public static String queryModel_missingQueryProps;
	public static String queryModel_nonSupportedDbCmd;

	public static String queryProperties_aggrCmdName;
	public static String queryProperties_dbCmdName;
	public static String queryProperties_errDeSerializeDBObject;
	public static String queryProperties_mapReduceCmdName;

	public static String resultDataHandler_invalidFieldName;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
