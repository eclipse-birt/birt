/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - support of timestamp, datetime, time, and date data types
  *    Actuate Corporation - support defining an Excel input file path or URI as part of the data source definition
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel;

public class ExcelODAConstants {

	public static final String CONN_FILE_URI_PROP = "URI"; //$NON-NLS-1$
	public static final String XLS_FORMAT = "xls"; //$NON-NLS-1$
	public static final String XLSX_FORMAT = "xlsx"; //$NON-NLS-1$
	public static final String UNSUPPORT_FORMAT = "unSupportFormat";
	public static final String CONN_INCLCOLUMNNAME_PROP = "INCLCOLUMNNAME"; //$NON-NLS-1$
	public static final String INC_COLUMN_NAME_YES = "YES"; //$NON-NLS-1$
	public static final String CONN_INCLTYPELINE_PROP = "INCLTYPELINE"; //$NON-NLS-1$
	public static final String INC_TYPE_LINE_YES = "YES"; //$NON-NLS-1$
	public static final String INC_TYPE_LINE_NO = "NO"; //$NON-NLS-1$
	public static final String INC_COLUMN_NAME_NO = "NO"; //$NON-NLS-1$

	public static final String CONN_WORKSHEETS_PROP = "WORKSHEETS"; //$NON-NLS-1$

	public static final String DELIMITER_SPACE = " "; //$NON-NLS-1$
	public static final char DELIMITER_DOUBLEQUOTE = '"';
	public static final String DELIMITER_COMMA = "COMMA"; //$NON-NLS-1$
	public static final String DELIMITER_COMMA_VALUE = ","; //$NON-NLS-1$
	public static final String DELIMITER_SEMICOLON_VALUE = ";"; //$NON-NLS-1$

	public static final String KEYWORD_SELECT = "SELECT"; //$NON-NLS-1$
	public static final String KEYWORD_ASTERISK = "*";//$NON-NLS-1$
	public static final String KEYWORD_AS = "AS"; //$NON-NLS-1$
	public static final String KEYWORD_FROM = "FROM"; //$NON-NLS-1$
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	public static final String ALL_SHEETS = "ALL"; //$NON-NLS-1$
	public static final int BLANK_LOOK_AHEAD = 5;

}
