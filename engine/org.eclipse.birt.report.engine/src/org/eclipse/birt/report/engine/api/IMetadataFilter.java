/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.HashMap;
import org.eclipse.birt.report.model.api.ReportElementHandle;

/**
 * 
 */

public interface IMetadataFilter {

	/**
	 * Output the IID or not. Only use the key, not the value. If the key "iid"
	 * exists, the HTML emitter will output the iid.
	 */
	public static final String KEY_OUTPUT_IID = "iid";
	/**
	 * Output the bookmark or not. Only use the key, not the value. If the key
	 * "bookmark" exists, the HTML emitter will output the bookmark.
	 */
	public static final String KEY_OUTPUT_BOOKMARK = "bookmark";
	/**
	 * The property "element_type". The value in the HashMap must be a String.
	 */
	public static final String KEY_ATTR_ELEMENT_TYPE = "element_type";
	/**
	 * Only use the key, not the value. If the key "iid_list" exists, and the
	 * "iid","bookmark", "element_type" have all been output, the HTML emitter will
	 * add the element informations ( "iid","bookmark" and "element_type" ) into the
	 * output instance IDs list.
	 */
	public static final String KEY_ADD_INTO_IID_LIST = "iid_list";
	/**
	 * The property "type". The value in the HashMap must be a String.
	 */
	public static final String KEY_ATTR_TYPE = "type";
	/**
	 * The property "row-type". The value in the HashMap must be a String.
	 */
	public static final String KEY_ATTR_ROW_TYPE = "row-type";
	/**
	 * Output the group-id or not. Only use the key, not the value. If the key
	 * "group-id" exists, the HTML emitter will output the group-id.
	 */
	public static final String KEY_OUTPUT_GOURP_ID = "group-id";

	/**
	 * Output the raw_data or not. Only use the key, not the value. If the key
	 * "raw_data" exists, the HTML emitter will output the raw_data.
	 */
	public static final String KEY_OUTPUT_RAW_DATA = "raw_data";

	/**
	 * It is used to judge what metadata properties need to be output.
	 * 
	 * @param elementHandle
	 * @return a HashMap which contains the metadata properties outputting
	 *         requirement.
	 */
	public HashMap needMetaData(ReportElementHandle elementHandle);

}
