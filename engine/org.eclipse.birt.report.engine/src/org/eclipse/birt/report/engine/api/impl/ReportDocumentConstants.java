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
	String CORE_STREAM = "/core";
	/**
	 * stream used to save the report design which create this report document.
	 */
	String DESIGN_STREAM = "/design"; //$NON-NLS-1$

	String ORIGINAL_DESIGN_STREAM = "/original_design";
	/**
	 * stream used to save the engine IR of the design
	 */
	String DESIGN_IR_STREAM = "/design.ir"; //$NON-NLS-1$
	/**
	 * stream saves the bookmark->page mapping. It will be mutiple copyies for
	 * different layout.
	 */
	String BOOKMARK_STREAM = "/bookmark"; //$NON-NLS-1$
	/**
	 * stream saves page hint. It describe the content in pages. There will be
	 * mutiple copies for different layout.
	 */
	String PAGEHINT_STREAM = "/pages"; //$NON-NLS-1$
	/**
	 * index of the page hint streams. There will be mutiple copies for different
	 * layout.
	 */
	String PAGEHINT_INDEX_STREAM = "/pages_index"; //$NON-NLS-1$
	/**
	 * toc structure. Each toc node is defined by a label and bookmark. The whole
	 * document has only one TOC stream (it isn't changed with the layout).
	 */
	String TOC_STREAM = "/toc"; //$NON-NLS-1$
	/**
	 * content saved in the folder
	 *
	 * @deprecated use stream name directly.
	 */
	@Deprecated
	String CONTENT_FOLDER = "/content"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save instance id and offset of each streams. There is
	 * only one copy in one report document.
	 */
	String REPORTLET_ID_INDEX_STREAM = "/reportlet"; //$NON-NLS-1$
	/**
	 * reportlet index stream. save bookmark and offset of each streams. There is
	 * only one copy in one report document.
	 */
	String REPORTLET_BOOKMARK_INDEX_STREAM = "/reportletBookmarks"; //$NON-NLS-1$
	/**
	 * report content data. one copy for one report document.
	 */
	String CONTENT_STREAM = "/content/content.dat";
	/**
	 * page content, there will be mutiple copies for different layout.
	 */
	String PAGE_STREAM = "/content/page.dat";

	String PAGE_INDEX_STREAM = "/content/page.idx";

	/**
	 * the stream saves the information about the reportlet document
	 */
	String REPORTLET_DOCUMENT_STREAM = "/reportletDocument";

	/**
	 * the relationships of the result sets, only one copy for each report document.
	 */
	String DATA_META_STREAM = "/Data/hierarchy";

	/**
	 * the relationships of the result sets, only one copy for each report document.
	 */
	String DATA_SNAP_META_STREAM = "/Data/snap_hierarchy";
	/**
	 * checkpoint stream of the report content. To be used to control load core
	 * stream.
	 */
	String CHECKPOINT_STREAM = "/checkpoint";

	/**
	 * report document tag. exist in the header of the core stream.
	 */
	String REPORT_DOCUMENT_TAG = "reportdocument";
	/**
	 * version number, following the document tag in the core stream, used by BIRT
	 * 2.0 before.
	 */
	String REPORT_DOCUMENT_VERSION_1_0_0 = "1.0.0";
	/**
	 * version number, used by BIRT 2.1 RC0 - 2.1RC4.
	 */
	String REPORT_DOCUMENT_VERSION_1_2_1 = "1.2.1";

	/**
	 * version number, used after BIRT 2.1RC5 before 2.2RC0
	 */
	String REPORT_DOCUMENT_VERSION_2_1_0 = "2.1.0";

	/**
	 * version used start from 2.1.3 and 2.2rc0
	 */
	String REPORT_DOCUMENT_VERSION_2_1_3 = "2.1.3-2.2RC0";

	/**
	 * the current report document version
	 */
	String REPORT_DOCUMENT_VERSION = REPORT_DOCUMENT_VERSION_2_1_3;

	int CHECKPOINT_INIT = 0;
	int CHECKPOINT_END = -1;
	int PAGECOUNT_INIT = 0;

	/**
	 * merge the streams to one stream.
	 */
	String CORE_VERSION_PREFIX = "CORE_VERSION_";
	/**
	 * version used in case there is no version tag in core stream
	 */
	String CORE_VERSION_UNKNOWN = CORE_VERSION_PREFIX + "UNKNOWN";
	/**
	 * the first version in the core stream
	 */
	String CORE_VERSION_0 = CORE_VERSION_PREFIX + 0;
	/**
	 * start from 2.2.1
	 */
	String CORE_VERSION_1 = CORE_VERSION_PREFIX + 1;

	/**
	 * start from 2.3.2
	 */
	String CORE_VERSION_2 = CORE_VERSION_PREFIX + 2;

	/**
	 * the engine version used to generate the report document
	 */
	String BIRT_ENGINE_VERSION_KEY = "BIRT ENGINE VERSION";
	// all supported versions are list here...
	/** the version value used before 2.0.0, 2.0.1, 2.0.2 */
	String BIRT_ENGINE_VERSION_2_0_0 = "2.0.0";
	/** the version value used by 2.1.0, 2.1.1, 2.1.2 */
	String BIRT_ENGINE_VERSION_2_1_0 = "2.1.0";
	/** the version value used by 2.1.3, 2.2.0 */
	String BIRT_ENGINE_VERSION_2_1_3 = "2.1.3";
	/** the version value used by 2.2.1/2.2.2/2.2.3/2.3.0/2.3.1 */
	String BIRT_ENGINE_VERSION_2_2_1 = "2.2.1";
	/** the version value used by 2.3.2 */
	String BIRT_ENGINE_VERSION_2_3_2 = "2.3.2";
	/** the version value used by 2.5.0 */
	String BIRT_ENGINE_VERSION_2_5_0 = "2.5.0";
	/** the version value used by 2.5.1 */
	String BIRT_ENGINE_VERSION_2_5_1 = "2.5.1";
	/** the version value used by 2.6.0 */
	String BIRT_ENGINE_VERSION_2_6_0 = "2.6.0";
	/** the version value used by 2.6.1 */
	String BIRT_ENGINE_VERSION_2_6_1 = "2.6.1";
	/** the current version */
	String BIRT_ENGINE_VERSION = BIRT_ENGINE_VERSION_2_6_1;
	/**
	 * extraction task version
	 */
	String DATA_EXTRACTION_TASK_VERSION_KEY = "extraction";
	String DATA_EXTRACTION_TASK_VERSION_0 = "0";
	String DATA_EXTRACTION_TASK_VERSION_1 = "1";

	/**
	 * page hint reader version
	 */
	String PAGE_HINT_VERSION_KEY = "page hint version";
	String PAGE_HINT_VERSION_1 = "1";
	String PAGE_HINT_VERSION_2 = "2";
	String PAGE_HINT_VERSION_3 = "3";
	String PAGE_HINT_VERSION_FIXED_LAYOUT = "4";

	/**
	 * build number, the number is get from the org.eclipse.birt.report.engine
	 */
	String BIRT_ENGINE_BUILD_NUMBER_KEY = "BIRT ENGINE BUILD NUMBER";

	int REPORTLET_DOCUMENT_VERSION_0 = 0;

	/**
	 * the extension used in the document, the value is "," separated extension id.
	 */
	String BIRT_ENGINE_EXTENSIONS = "BIRT_ENGINE_EXTENSIONS";

	/**
	 * the run status stream. To save the fatal exception thrown from run task.
	 */
	String RUN_STATUS_STREAM = "/runStatus"; //$NON-NLS-1$

}
