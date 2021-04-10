/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
