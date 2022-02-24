/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.PropertyStructure;

/**
 * The Action structure defines a hyperlink. There are five major kinds of
 * actions:
 * <ul>
 * <li>Hyperlink: To an external web site, e-mail system, etc.</li>
 * <li>Bookmark Link: To a location within this report as specified by a
 * bookmark.</li>
 * <li>Drill-through: To a location within another report as specified by a
 * bookmark.
 * </ul>
 * <p>
 * The drill-through link can link to an existing report, or request to run a
 * new report. It can optionally include:</li>
 * <ul>
 * <li>Parameters to pass when creating a new report.</li>
 * <li>A location within the target report as specified by a search.</li>
 * <li>A location within the target report as specified by a bookmark.</li>
 * </ul>
 * <p>
 * The Hyperlink property returns a standard web-style link with 'http:' or
 * 'mailto:' prefix. Expression hyperlinks can include special BIRT features
 * (search and so on), but only as encoded into a URL. If the link wants to jump
 * to a specific target within the document, encode that target within the URL
 * as defined by target document type. (For example:
 * http://foo.com/docs/myDoc.html#myTarget.) If the user provides a relative
 * URL, it is assumed to be relative to the same web server that is being used
 * to view the report. The exact semantics are implementation-specific in the
 * open source release. In the commercial release, the URL is relative to the
 * web server that hosts ActivePortal. A bookmark link simply identifies a
 * bookmark identified within this report. Use the Bookmark property of a report
 * item to create the target bookmark.
 * 
 */

public class Action extends PropertyStructure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public final static String ACTION_STRUCT = "Action"; //$NON-NLS-1$

	/**
	 * Property name of the hyperlink.
	 */

	public final static String URI_MEMBER = "uri"; //$NON-NLS-1$

	/**
	 * Property name of report name, when this action is drill-through action.
	 * 
	 */

	public final static String REPORT_NAME_MEMBER = "reportName"; //$NON-NLS-1$

	/**
	 * Link type of the Action. Only one of hyperlink, bookmark link or
	 * drill-through (below) can be supplied.
	 */

	public final static String LINK_TYPE_MEMBER = "linkType"; //$NON-NLS-1$

	/**
	 * Property name of the target browser window for the link. (Optional.)
	 */

	public final static String TARGET_WINDOW_MEMBER = "targetWindow"; //$NON-NLS-1$

	/**
	 * Property name of the tool tip.
	 */
	public final static String TOOLTIP_MEMBER = "toolTip"; //$NON-NLS-1$

	/**
	 * Property name of the target bookmark link.
	 */

	public final static String TARGET_BOOKMARK_MEMBER = "targetBookmark"; //$NON-NLS-1$

	/**
	 * Property name of parameter bindings, when this action is drill-through
	 * action.
	 */

	public final static String PARAM_BINDINGS_MEMBER = "paramBindings"; //$NON-NLS-1$

	/**
	 * Property name of search, when this action is drill-through action.
	 */

	public final static String SEARCH_MEMBER = "search"; //$NON-NLS-1$

	/**
	 * Property name of format.
	 */

	public final static String FORMAT_TYPE_MEMBER = "formatType"; //$NON-NLS-1$

	/**
	 * Property name of target type of linked file.
	 */

	public final static String TARGET_FILE_TYPE_MEMBER = "targetFileType"; //$NON-NLS-1$

	/**
	 * Property name of target bookmark type.
	 */

	public final static String TARGET_BOOKMARK_TYPE_MEMBER = "targetBookmarkType"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return ACTION_STRUCT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */

	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new ActionHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */

	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new ActionHandle(valueHandle.getElementHandle(), getContext());
	}
}
