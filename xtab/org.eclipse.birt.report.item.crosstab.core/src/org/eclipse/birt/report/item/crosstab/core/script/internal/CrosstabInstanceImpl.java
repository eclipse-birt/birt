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

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabInstance;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * CrosstabInstanceImpl
 */
public class CrosstabInstanceImpl implements ICrosstabInstance {

	private ITableContent content;
	private DesignElementHandle modelHandle;
	private RunningState runningState;

	public CrosstabInstanceImpl(ITableContent content, DesignElementHandle modelHandle, RunningState runningState) {
		this.content = content;
		this.modelHandle = modelHandle;
		this.runningState = runningState;
	}

	@Override
	public String getCaption() {
		return content.getCaption();
	}

	@Override
	public String getCaptionKey() {
		return content.getCaptionKey();
	}

	@Override
	public String getSummary() {
		return content.getSummary();
	}

	@Override
	public boolean isRepeatColumnHeader() {
		return content.isHeaderRepeat();
	}

	@Override
	public boolean isRepeatRowHeader() {
		// TODO wait content support
		return false;
	}

	@Override
	public void setCaption(String caption) {
		content.setCaption(caption);
	}

	@Override
	public void setCaptionKey(String key) {
		content.setCaptionKey(key);
	}

	@Override
	public void setSummary(String summary) {
		content.setSummary(summary);
	}

	@Override
	public void setRepeatColumnHeader(boolean repeat) {
		if (runningState == RunningState.CREATE) {
			content.setHeaderRepeat(repeat);
		} else if (runningState == RunningState.RENDER) {
			throw new UnsupportedOperationException("Repeat column header can't be set at render time.");
		} else {
			throw new UnsupportedOperationException("Repeat column header can't be set on page break.");
		}
	}

	@Override
	public void setRepeatRowHeader(boolean repeat) {
		// TODO wait content support
	}

	@Override
	public String getHeight() {
		DimensionType height = content.getHeight();
		if (height != null) {
			return height.toString();
		}
		return null;
	}

	@Override
	public String getHelpText() {
		return content.getHelpText();
	}

	@Override
	public String getHorizontalPosition() {
		DimensionType x = content.getX();
		if (x != null) {
			return x.toString();
		}
		return null;
	}

	@Override
	public String getName() {
		return content.getName();
	}

	@Override
	public Object getNamedExpressionValue(String name) {
		// TODO need report context support
		return null;
	}

	@Override
	public IScriptStyle getStyle() {
		return new StyleInstance(content.getStyle());
	}

	@Override
	public Object getUserPropertyValue(String name) {
		UserPropertyDefnHandle prop = modelHandle.getUserPropertyDefnHandle(name);
		if (prop != null) {
			return modelHandle.getProperty(prop.getName());
		}
		return null;
	}

	@Override
	public String getVerticalPosition() {
		DimensionType y = content.getY();
		if (y != null) {
			return y.toString();
		}
		return null;
	}

	@Override
	public String getWidth() {
		DimensionType width = content.getWidth();
		if (width != null) {
			return width.toString();
		}
		return null;
	}

	@Override
	public void setHeight(String height) {
		content.setHeight(DimensionType.parserUnit(height));
	}

	@Override
	public void setHelpText(String help) {
		content.setHelpText(help);
	}

	@Override
	public void setHorizontalPosition(String position) {
		content.setX(DimensionType.parserUnit(position));
	}

	@Override
	public void setName(String name) {
		content.setName(name);
	}

	@Override
	public void setUserPropertyValue(String name, Object value) throws ScriptException {
		UserPropertyDefnHandle prop = modelHandle.getUserPropertyDefnHandle(name);
		if (prop != null) {
			try {
				modelHandle.setProperty(prop.getName(), value);
			} catch (SemanticException e) {
				throw new ScriptException(e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void setVerticalPosition(String position) {
		content.setY(DimensionType.parserUnit(position));
	}

	@Override
	public void setWidth(String width) {
		String unit = content.getReportContent().getDesign().getReportDesign().getDefaultUnits();
		content.setWidth(DimensionType.parserUnit(width, unit));
	}

}
