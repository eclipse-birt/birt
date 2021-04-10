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

package org.eclipse.birt.report.engine.api;

import java.util.List;

/*
 * define a DynamicFilterParameter
 */
public interface IDynamicFilterParameterDefn extends IParameterDefn {

	// display type
	public static final int DISPLAY_TYPE_SIMPLE = 1;
	public static final int DISPLAY_TYPE_ADVANCED = 2;

	public int getDisplayType();

	public String getColumn();

	public List<String> getFilterOperatorList();

	public List<String> getFilterOperatorDisplayList();
}
