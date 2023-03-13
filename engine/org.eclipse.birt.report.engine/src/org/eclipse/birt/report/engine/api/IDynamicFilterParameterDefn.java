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

package org.eclipse.birt.report.engine.api;

import java.util.List;

/*
 * define a DynamicFilterParameter
 */
public interface IDynamicFilterParameterDefn extends IParameterDefn {

	// display type
	int DISPLAY_TYPE_SIMPLE = 1;
	int DISPLAY_TYPE_ADVANCED = 2;

	int getDisplayType();

	String getColumn();

	List<String> getFilterOperatorList();

	List<String> getFilterOperatorDisplayList();
}
