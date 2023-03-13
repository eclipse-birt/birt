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

package org.eclipse.birt.report.engine.ir;

import java.util.HashMap;

/**
 * Page sequence. Page sequence associated master page with page role.
 */
public class PageSequenceDesign extends ReportElementDesign {
	/**
	 * hash map used to store the page sequence.
	 */
	protected HashMap pageRefs = new HashMap();

	/**
	 * add a page into the page sequence. role should be one of the following
	 * values: 1) first 2) body 3) even 4) odd
	 *
	 * @param role rule name
	 * @param page page
	 */
	public void setPage(String role, MasterPageDesign page) {
		assert role != null;
		assert page != null;
		this.pageRefs.put(role, page);
	}

	/**
	 * get the master page of role.
	 *
	 * @param role role name
	 * @return master page, null if not defined.
	 */
	public MasterPageDesign getPage(String role) {
		assert (role != null);
		return (MasterPageDesign) this.pageRefs.get(role);
	}
}
