/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.util;

import java.util.Map;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * ICrosstabUpdateContext
 */
public interface ICrosstabUpdateContext {

	void performDefaultCreation(int type, Object model, Map<String, Object> extras) throws SemanticException;

	void performDefaultValidation(int type, Object model, Map<String, Object> extras) throws SemanticException;
}
