/**************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 **************************************************************************/
package org.eclipse.birt.data.engine.expression;

import java.util.Comparator;

import org.eclipse.birt.data.engine.api.BaseCompareHints;

/**
 * Code implementation moved to API class BaseCompareHints
 */
public class CompareHints extends BaseCompareHints {

	public CompareHints(Comparator comparator, String nullStringType) {
		super(comparator, nullStringType);
	}

}
