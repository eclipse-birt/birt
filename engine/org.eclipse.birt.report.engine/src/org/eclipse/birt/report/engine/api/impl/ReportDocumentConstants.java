package org.eclipse.birt.report.engine.api.impl;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

public interface ReportDocumentConstants
{
	/**
	 * core stream of the report content. 
	 * It will be lock during the write pharse. 
	 */
	static final String CORE_STREAM = "/core";
	/**
	 * stream used to save the report design which create
	 * this report document.
	 */
	static final String DESIGN_STREAM = "/design"; //$NON-NLS-1$
	/**
	 * stream used to save the engine IR of the design 
	 */
	static final String DESIGN_IR_STREAM = "/design.ir"; //$NON-NLS-1$
	/**
	 * stream saves the bookmark->page mapping.
	 * It will be mutiple copyies for different layout.
	 */
	static final String BOOKMARK_STREAM = "/bookmark"; //$NON-NLS-1$
	/**
	 * stream saves page hint. It describe the content in
	 * pages. There will be mutiple copies for different layout.
	 */
	static final String PAGEHINT_STREAM = "/pages"; //$NON-NLS-1$
	/**
	 * index of the page hint streams. 
	 * There will be mutiple copies for different layout.
	 */
	static final String PAGEHINT_INDEX_STREAM = "/pages_index"; //$NON-NLS-1$
	/**
	 * toc structure. Each toc node is defined by a 
	 * label and bookmark. The whole document has only one
	 * TOC stream (it isn't changed with the layout).
	 */
	static final String TOC_STREAM = "/toc"; //$NON-NLS-1$
	/**
	 * content saved in the folder
	 * @deprecated use stream name directly. 
	 */
	static final String CONTENT_FOLDER = "/content"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save instance id and offset of each streams.
	 * There is only one copy in one report document.
	 */
	static final String REPORTLET_ID_INDEX_STREAM = "/reportlet"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save bookmark and offset of each streams. There
	 * is only one copy in one report document.
	 */
	static final String REPORTLET_BOOKMARK_INDEX_STREAM = "/reportletBookmarks"; //$NON-NLS-1$
	/**
	 * report content data. one copy for one report document.
	 */
	static final String CONTENT_STREAM = "/content/content.dat";
	/**
	 * page content, there will be mutiple copies for
	 * different layout.
	 */
	static final String PAGE_STREAM = "/content/page.dat";
	
	/**
	 * the relationships of the result sets, only one copy for each
	 * report document. 
	 */
	static final String DATA_META_STREAM = "/Data/hierarchy";
	
	/**
	 * the relationships of the result sets, only one copy for each
	 * report document. 
	 */
	static final String DATA_SNAP_META_STREAM = "/Data/snap_hierarchy";
	/**
	 * checkpoint stream of the report content. 
	 * To be used to control load core stream. 
	 */
	static final String CHECKPOINT_STREAM = "/checkpoint";
	
	/**
	 * report document tag. exist in the header of the
	 * core stream.
	 */
	static final String REPORT_DOCUMENT_TAG = "reportdocument";
	/**
	 * version number, following the document tag in the 
	 * core stream, used by BIRT 2.0 before.
	 */
	static final String REPORT_DOCUMENT_VERSION_1_0_0 = "1.0.0";
	/**
	 * version number, used by BIRT 2.1.
	 */
	static final String REPORT_DOCUMENT_VERSION_1_2_1 = "1.2.1";
	
	/**
	 * version number, used after BIRT 2.1RC5.
	 */
	static final String REPORT_DOCUMENT_VERSION_2_1_0 = "2.1.0";
	
	/**
	 * version used before 2.1.3 and 2.2rc0
	 */
	static final String REPORT_DOCUMENT_VERSION_2_1_3 = "2.1.3-2.2RC0";
	
	/**
	 * the current report document version
	 */
	static final String REPORT_DOCUMENT_VERSION = REPORT_DOCUMENT_VERSION_2_1_3;

	static final int CHECKPOINT_INIT = 0;
	static final int CHECKPOINT_END = -1;
	static final int PAGECOUNT_INIT = 0;
	
	/**
	 * merge the streams to one stream.
	 */
	static final String CORE_VERSION_PREFIX = "CORE_VERSION_";
	static final String CORE_VERSION_0 = CORE_VERSION_PREFIX + 0;
}