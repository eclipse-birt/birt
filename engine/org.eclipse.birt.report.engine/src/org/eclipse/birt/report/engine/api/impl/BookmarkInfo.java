/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IBookmarkInfo;

public class BookmarkInfo extends org.eclipse.birt.report.model.util.BookmarkInfo implements IBookmarkInfo {

	public BookmarkInfo(String bookmark, String displayName, String elementType) {
		super(bookmark, displayName, elementType);
	}
}
