/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;

public interface IBirtConstants
{
	//Table Related Operations
	static public final String OPERATION_ADD_LITERAL 			= "add"; //$NON-NLS-1$
	static public final String OPERATION_DELETE_LITERAL 		= "delete"; //$NON-NLS-1$
	static public final String OPERATION_SORT_LITERAL 		= "sort"; //$NON-NLS-1$
	static public final String OPERATION_FILTER_LITERAL 		= "filter"; //$NON-NLS-1$
	static public final String OPERATION_EXPAND_LITERAL 		= "expand"; //$NON-NLS-1$
	static public final String OPERATION_COLLAPSE_LITERAL 	= "collapse"; //$NON-NLS-1$
	static public final String OPERATION_QUERYFONT_LITERAL	= "queryfont"; //$NON-NLS-1$
	///Chencn
	static public final String OPERATION_QUERYEXPORT_LITERAL	= "queryexport";
	static public final String OPERATION_EXPORT_LITERAL	= "export";
	///
	
	static public final int OPERATION_ADD 		= 1;
	static public final int OPERATION_DELETE 		= 2;
	static public final int OPERATION_SORT 		= 3;
	static public final int OPERATION_FILTER 		= 4;
	static public final int OPERATION_EXPAND 		= 5;
	static public final int OPERATION_COLLAPSE 	= 6;
	static public final int OPERATION_QUERYFONT 	= 7;
	///Chencn
	static public final int OPERATION_QUERYEXPORT 	= 8;
	static public final int OPERATION_EXPORT 	= 9;
	///
	
	//Document related operations
	static public final String OPERATOR_GETTOC_LITERAL					= "GetToc"; //$NON-NLS-1$
	static public final String OPERATOR_GETPAGE_LITERAL					= "GetPage"; //$NON-NLS-1$
	static public final String OPERATOR_GETCASCADINGPARAMETER_LITERAL		= "GetCascadingParameter"; //$NON-NLS-1$
	static public final String OPERATOR_CHANGEPARAMETER_LITERAL         = "CHANGEPARAMETER"; //$NON-NLS-1$

	static public final int OPERATOR_GETTOC					= 1;
	static public final int OPERATOR_GETPAGE					= 2;
	static public final int OPERATOR_GETCASCADINGPARAMETER	= 3;
	static public final int OPERATOR_CHANGEPARAMETER  = 4;
	
	//Oprand Name Value pair: Names
	static public final String OPRAND_PAGENO		= "page";	//$NON-NLS-1$
	static public final String OPRAND_BOOKMARK	= "bookmark";	//$NON-NLS-1$
	static public final String OPRAND_SVG			= "svg";	//$NON-NLS-1$
	static public final String OPRAND_PARAM		= "param"; //$NON-NLS-1$
	static public final String OPRAND_IID			= "iid"; //$NON-NLS-1$
	
	// Table column keywords
	static public final String OPRAND_COLUMNNAME		= "ColumnName"; //$NON-NLS-1$
	static public final String OPRAND_COLUMNS			= "Columns"; //$NON-NLS-1$
	static public final String OPRAND_COLUMNHEADER	= "ColumnHeader"; //$NON-NLS-1$
	static public final String OPRAND_COLUMNINDEX		= "ColumnIndex"; //$NON-NLS-1$
	static public final String OPRAND_COLUMNDATAFIELD	= "ColumnDataField"; //$NON-NLS-1$
	
	//Output format
	static public String MIME_TYPE 	= "text/xml"; //$NON-NLS-1$
	static public String RENDERFORMAT 	= HTMLRenderOption.OUTPUT_FORMAT_HTML ;

}