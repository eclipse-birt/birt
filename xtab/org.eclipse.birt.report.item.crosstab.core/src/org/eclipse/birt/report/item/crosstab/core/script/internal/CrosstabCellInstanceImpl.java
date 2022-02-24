/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCellInstance;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * CrosstabCellInstanceImpl
 */
public class CrosstabCellInstanceImpl implements ICrosstabCellInstance {

	private IReportContext context;
	private ICellContent content;
	private ExtendedItemHandle modelHandle;
	private long id = -1;
	private String type = TYPE_HEADER;

	public CrosstabCellInstanceImpl(ICellContent content, ExtendedItemHandle modelHandle, IReportContext context) {
		this.content = content;
		this.modelHandle = modelHandle;
		this.context = context;

		if (modelHandle != null) {
			id = modelHandle.getID();

			try {
				IReportItem item = modelHandle.getReportItem();
				if (item instanceof AggregationCellHandle) {
					type = TYPE_AGGREGATION;
				}
			} catch (ExtendedElementException e) {
				e.printStackTrace();
			}
		}
	}

	public long getCellID() {
		return id;
	}

	public String getCellType() {
		return type;
	}

	public Object getDataValue(String bindingName) throws BirtException {
		if (context != null && bindingName != null) {
			return context.evaluate(ExpressionUtil.createJSDataExpression(bindingName));
		}

		return null;
	}

	public String getHelpText() {
		return content.getHelpText();
	}

	public String getName() {
		return content.getName();
	}

	public Object getNamedExpressionValue(String name) {
		// TODO need report context support
		return null;
	}

	public IScriptStyle getStyle() {
		return new StyleInstance(content.getStyle());
	}

	public Object getUserPropertyValue(String name) {
		if (modelHandle != null) {
			UserPropertyDefnHandle prop = modelHandle.getUserPropertyDefnHandle(name);
			if (prop != null) {
				return modelHandle.getProperty(prop.getName());
			}
		}
		return null;
	}

	public void setHelpText(String help) {
		content.setHelpText(help);
	}

	public void setName(String name) {
		content.setName(name);
	}

	public void setUserPropertyValue(String name, Object value) throws ScriptException {
		if (modelHandle != null) {
			UserPropertyDefnHandle prop = modelHandle.getUserPropertyDefnHandle(name);
			if (prop != null) {
				try {
					modelHandle.setProperty(prop.getName(), value);
				} catch (SemanticException e) {
					throw new ScriptException(e.getLocalizedMessage());
				}
			}
		}
	}

}
