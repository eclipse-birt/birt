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

package org.eclipse.birt.report.model.api.util;

public interface IBookmarkInfo {

	public static final int CONSTANTS_TYPE = 1;

	public static final int EXPRESSION_TYPE = 2;

	String getBookmark();

	String getDisplayName();

	String getElementType();

	int getBookmarkType();
}
