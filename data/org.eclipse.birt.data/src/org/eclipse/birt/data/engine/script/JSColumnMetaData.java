/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.script;

import java.util.logging.Logger;

import org.eclipse.birt.data.engine.odi.IResultClass;
import org.mozilla.javascript.NativeArray;

/**
 * Implements a Javascript array of ColumnDefn objects which wraps around an odi
 * IResultClass
 */
public class JSColumnMetaData extends NativeArray {
	private IResultClass resultClass;

	private static Logger logger = Logger.getLogger(JSColumnMetaData.class.getName());
	private static final long serialVersionUID = 4836558843807755596L;

	public JSColumnMetaData(IResultClass resultClass) {
		super(resultClass.getFieldCount());

		logger.entering(JSColumnMetaData.class.getName(), "JSColumnMetaData");
		this.resultClass = resultClass;
		int fieldCount = resultClass.getFieldCount();
		for (int i = 0; i < fieldCount; i++) {
			this.put(i, this, new JSColumnDefn(resultClass, i + 1));
		}

		// This object is not modifiable in any way
		sealObject();
	}

	public IResultClass getResultClass() {
		return resultClass;
	}
}
