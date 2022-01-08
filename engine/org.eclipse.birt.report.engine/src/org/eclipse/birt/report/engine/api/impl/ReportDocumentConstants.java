/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

public interface ReportDocumentConstants {

	/**
	 * core stream of the report content. It will be lock during the write pharse.
	 */
	static final String CORE_STREAM = "/core";
	/**
	 * stream used to save the report design which create this report document.
	 */
	static final String DESIGN_STREAM = "/design"; //$NON-NLS-1$

	static final String ORIGINAL_DESIGN_STREAM = "/original_design";
	/**
	 * stream used to save the engine IR of the design
	 */
	static final String DESIGN_IR_STREAM = "/design.ir"; //$NON-NLS-1$
	/**
	 * stream saves the bookmark->page mapping. It will be mutiple copyies for
	 * different layout.
	 */
	static final String BOOKMARK_STREAM = "/bookmark"; //$NON-NLS-1$
	/**
	 * stream saves page hint. It describe the content in pages. There will be
	 * mutiple copies for different layout.
	 */
	static final String PAGEHINT_STREAM = "/pages"; //$NON-NLS-1$
	/**
	 * index of the page hint streams. There will be mutiple copies for different
	 * layout.
	 */
	static final String PAGEHINT_INDEX_STREAM = "/pages_index"; //$NON-NLS-1$
	/**
	 * toc structure. Each toc node is defined by a label and bookmark. The whole
	 * document has only one TOC stream (it isn't changed with the layout).
	 */
	static final String TOC_STREAM = "/toc"; //$NON-NLS-1$
	/**
	 * content saved in the folder
	 * 
	 * @deprecated use stream name directly.
	 */
	static final String CONTENT_FOLDER = "/content"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save instance id and offset of each streams. There is
	 * only one copy in one report document.
	 */
	static final String REPORTLET_ID_INDEX_STREAM = "/reportlet"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save bookmark and offset of each streams. There is
	 * only one copy in one report document.
	 */
	static final String REPORTLET_BOOKMARK_INDEX_STREAM = "/reportletBookmarks"; //$NON-NLS-1$
	/**
	 * report content data. one copy for one report document.
	 */
	static final String CONTENT_STREAM = "/content/content.dat";
	/**
	 * page content, there will be mutiple copies for different layout.
	 */
	static final String PAGE_STREAM = "/content/page.dat";

	static final String PAGE_INDEX_STREAM = "/content/page.idx";

	/**
	 * the stream saves the information about the reportlet document
	 */
	static final String REPORTLET_DOCUMENT_STREAM = "/reportletDocument";

	/**
	 * the relationships of the result sets, only one copy for each report document.
	 */
	static final String DATA_META_STREAM = "/Data/hierarchy";

	/**
	 * the relationships of the result sets, only one copy for each report document.
	 */
	static final String DATA_SNAP_META_STREAM = "/Data/snap_hierarchy";
	/**
	 * checkpoint stream of the report content. To be used to control load core
	 * stream.
	 */
	static final String CHECKPOINT_STREAM = "/checkpoint";

	/**
	 * report document tag. exist in the header of the core stream.
	 */
	static final String REPORT_DOCUMENT_TAG = "reportdocument";
	/**
	 * version number, following the document tag in the core stream, used by BIRT
	 * 2.0 before.
	 */
	static final String REPORT_DOCUMENT_VERSION_1_0_0 = "1.0.0";
	/**
	 * version number, used by BIRT 2.1 RC0 - 2.1RC4.
	 */
	static final String REPORT_DOCUMENT_VERSION_1_2_1 = "1.2.1";

	/**
	 * version number, used after BIRT 2.1RC5 before 2.2RC0
	 */
	static final String REPORT_DOCUMENT_VERSION_2_1_0 = "2.1.0";

	/**
	 * version used start from 2.1.3 and 2.2rc0
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
	/**
	 * version used in case there is no version tag in core stream
	 */
	static final String CORE_VERSION_UNKNOWN = CORE_VERSION_PREFIX + "UNKNOWN";
	/**
	 * the first version in the core stream
	 */
	static final String CORE_VERSION_0 = CORE_VERSION_PREFIX + 0;
	/**
	 * start from 2.2.1
	 */
	static final String CORE_VERSION_1 = CORE_VERSION_PREFIX + 1;

	/**
	 * start from 2.3.2
	 */
	static final String CORE_VERSION_2 = CORE_VERSION_PREFIX + 2;

	/**
	 * the engine version used to generate the report document
	 */
	static final String BIRT_ENGINE_VERSION_KEY = "BIRT ENGINE VERSION";
	// all supported versions are list here...
	/** the version value used before 2.0.0, 2.0.1, 2.0.2 */
	static final String BIRT_ENGINE_VERSION_2_0_0 = "2.0.0";
	/** the version value used by 2.1.0, 2.1.1, 2.1.2 */
	static final String BIRT_ENGINE_VERSION_2_1_0 = "2.1.0";
	/** the version value used by 2.1.3, 2.2.0 */
	static final String BIRT_ENGINE_VERSION_2_1_3 = "2.1.3";
	/** the version value used by 2.2.1/2.2.2/2.2.3/2.3.0/2.3.1 */
	static final String BIRT_ENGINE_VERSION_2_2_1 = "2.2.1";
	/** the version value used by 2.3.2 */
	static final String BIRT_ENGINE_VERSION_2_3_2 = "2.3.2";
	/** the version value used by 2.5.0 */
	static final String BIRT_ENGINE_VERSION_2_5_0 = "2.5.0";
	/** the version value used by 2.5.1 */
	static final String BIRT_ENGINE_VERSION_2_5_1 = "2.5.1";
	/** the version value used by 2.6.0 */
	static final String BIRT_ENGINE_VERSION_2_6_0 = "2.6.0";
	/** the version value used by 2.6.1 */
	static final String BIRT_ENGINE_VERSION_2_6_1 = "2.6.1";
	/** the current version */
	static final String BIRT_ENGINE_VERSION = BIRT_ENGINE_VERSION_2_6_1;
	/**
	 * extraction task version
	 */
	static final String DATA_EXTRACTION_TASK_VERSION_KEY = "extraction";
	static final String DATA_EXTRACTION_TASK_VERSION_0 = "0";
	static final String DATA_EXTRACTION_TASK_VERSION_1 = "1";

	/**
	 * page hint reader version
	 */
	static final String PAGE_HINT_VERSION_KEY = "page hint version";
	static final String PAGE_HINT_VERSION_1 = "1";
	static final String PAGE_HINT_VERSION_2 = "2";
	static final String PAGE_HINT_VERSION_3 = "3";
	static final String PAGE_HINT_VERSION_FIXED_LAYOUT = "4";

	/**
	 * build number, the number is get from the org.eclipse.birt.report.engine
	 */
	static final String BIRT_ENGINE_BUILD_NUMBER_KEY = "BIRT ENGINE BUILD NUMBER";

	static final int REPORTLET_DOCUMENT_VERSION_0 = 0;

	/**
	 * the extension used in the document, the value is "," separated extension id.
	 */
	static final String BIRT_ENGINE_EXTENSIONS = "BIRT_ENGINE_EXTENSIONS";

	/**
	 * the run status stream. To save the fatal exception thrown from run task.
	 */
	static final String RUN_STATUS_STREAM = "/runStatus"; //$NON-NLS-1$

}
