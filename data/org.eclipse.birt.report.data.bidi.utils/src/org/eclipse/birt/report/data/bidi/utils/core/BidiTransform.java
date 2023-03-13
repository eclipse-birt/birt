/***********************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.data.bidi.utils.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This class performs various Bidi transformations.
 *
 * @author bidi_hcg
 *
 */

public abstract class BidiTransform // extends SQLKeywordScanner
{

	/**
	 * @param txt
	 * @param inFormat
	 * @param outFormat
	 * @return
	 *
	 * @author Lina Kemmel
	 */
	public static String transform(String txt, String inFormat, String outFormat) {
		return BidiEngine.INSTANCE.process(txt, inFormat, outFormat);
	}

	/**
	 * @param map
	 * @param inFormat
	 * @param outFormat
	 * @return
	 * @author Ira Fishbein
	 */
	public Map<String, String> transform(Map<String, String> map, String inFormat, String outFormat) {
		Map<String, String> resultMap = new HashMap<>();
		Iterator<String> keyIterator = map.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = keyIterator.next();
			String str = BidiEngine.INSTANCE.process(key, inFormat, outFormat);
			String value = map.get(key);
			resultMap.put(str, value);
		}
		return resultMap;
	}

	/**
	 * @param procName
	 * @param inFormat
	 * @param outFormat
	 * @param isSQLServer
	 * @return
	 *
	 * @author Ira Fishbein
	 */
	public String transformProcName(String procName, String inFormat, String outFormat, boolean isSQLServer) {
		String specialCaseStr = ";1"; //$NON-NLS-1$
		boolean isSpecialCase = isSQLServer && procName.endsWith(specialCaseStr);
		if (isSpecialCase) {
			procName = procName.substring(0, procName.length() - specialCaseStr.length());
		}
		procName = BidiEngine.INSTANCE.process(procName, inFormat, outFormat);
		if (isSpecialCase) {
			procName += specialCaseStr;
		}
		return procName;
	}

	/**
	 * @param urlStr
	 * @param inFormat
	 * @param outFormat
	 * @return
	 *
	 * @author Ira Fishbein
	 */
	public static String transformURL(String urlStr, String inFormat, String outFormat) {
		if (inFormat.equals(outFormat)) {
			return urlStr;
		}

		String str = urlStr;
		if (str.contains("db2")) { // DB2 DB
			int dbStrStart = str.lastIndexOf("/");
			if (dbStrStart < 0) {
				return urlStr;
			}
			String dbName = str.substring(dbStrStart + 1);
			dbName = BidiEngine.INSTANCE.process(dbName, inFormat, outFormat);
			urlStr = str.substring(0, dbStrStart + 1) + dbName;
		} else if (str.contains("sqlserver")) { // MS SQL DB
			int dbStrStart = -1;
			if (str.indexOf("databaseName") > 0) {
				dbStrStart = str.indexOf("databaseName") + "databaseName".length();
			} else if (str.indexOf("database") > 0) {
				dbStrStart = str.indexOf("database") + "database".length();
			} else {
				return urlStr;
			}
			String dbName = str.substring(dbStrStart);
			dbName = dbName.trim();
			dbName = dbName.substring(1).trim();
			int indx1 = dbName.indexOf(';');
			int indx2 = dbName.indexOf(',');
			int indx = -1;
			if (indx1 > -1) {
				indx = indx1;
			}
			if (indx2 > -1) {
				if (indx2 < indx || (indx == -1)) {
					indx = indx2;
				}
			}

			if (indx == -1) {
				indx = dbName.length();
			}

			dbName = dbName.substring(0, indx);
			String dbNameNew = BidiEngine.INSTANCE.process(dbName, inFormat, outFormat);
			urlStr = urlStr.replace(dbName, dbNameNew);
		} else if (str.contains("oracle")) { // ORACLE DB
			int dbStrStart = Math.max(str.lastIndexOf(':'), str.lastIndexOf('/'));
			if (dbStrStart < 0) {
				return urlStr;
			}
			String dbName = str.substring(dbStrStart + 1);
			dbName = BidiEngine.INSTANCE.process(dbName, inFormat, outFormat);
			urlStr = str.substring(0, dbStrStart + 1) + dbName;
		} else if (str.contains("derby")) { // DERBY DB
			int dbStrStart = str.lastIndexOf('/');
			if (dbStrStart < 0) {
				return urlStr;
			}
			String dbName = str.substring(dbStrStart + 1);
			dbName = BidiEngine.INSTANCE.process(dbName, inFormat, outFormat);
			urlStr = str.substring(0, dbStrStart + 1) + dbName;
		} else if (str.contains("mysql")) { // MYSQL DB
			if ((str.indexOf("//") < 0) || (str.substring(str.indexOf("//") + 2).indexOf('/') < 0)) {
				return urlStr;
			}
			int dbStrStart = str.indexOf("//") + 2 + str.substring(str.indexOf("//") + 2).indexOf('/') + 1;
			int dbStrEnd = str.length();
			if (str.substring(dbStrStart).contains("?")) {
				dbStrEnd = str.substring(dbStrStart).indexOf("?");
			}
			String dbName = str.substring(dbStrStart, dbStrStart + dbStrEnd);
			dbName = BidiEngine.INSTANCE.process(dbName, inFormat, outFormat);
			urlStr = str.substring(0, dbStrStart) + dbName + str.substring(dbStrStart + dbStrEnd);
		}
		return urlStr;
	}

}
