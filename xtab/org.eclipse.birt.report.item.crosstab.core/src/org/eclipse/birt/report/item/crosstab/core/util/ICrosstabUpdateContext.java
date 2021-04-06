/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
