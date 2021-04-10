/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

/**
 * ICrosstabCellInstance
 */
public interface ICrosstabCellInstance {

	String TYPE_HEADER = ICrosstabCell.TYPE_HEADER;

	String TYPE_AGGREGATION = ICrosstabCell.TYPE_AGGREGATION;

	long getCellID();

	String getCellType();

	Object getDataValue(String bindingName) throws BirtException;

	IScriptStyle getStyle();

	Object getNamedExpressionValue(String name);

	Object getUserPropertyValue(String name);

	void setUserPropertyValue(String name, Object value) throws ScriptException;
}
