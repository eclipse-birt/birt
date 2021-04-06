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

	public String getCaption() {
		return content.getCaption();
	}

	public String getCaptionKey() {
		return content.getCaptionKey();
	}

	public String getSummary() {
		return content.getSummary();
	}

	public boolean isRepeatColumnHeader() {
		return content.isHeaderRepeat();
	}

	public boolean isRepeatRowHeader() {
		// TODO wait content support
		return false;
	}

	public void setCaption(String caption) {
		content.setCaption(caption);
	}

	public void setCaptionKey(String key) {
		content.setCaptionKey(key);
	}

	public void setSummary(String summary) {
		content.setSummary(summary);
	}

	public void setRepeatColumnHeader(boolean repeat) {
		if (runningState == RunningState.CREATE) {
			content.setHeaderRepeat(repeat);
		} else if (runningState == RunningState.RENDER) {
			throw new UnsupportedOperationException("Repeat column header can't be set at render time.");
		} else {
			throw new UnsupportedOperationException("Repeat column header can't be set on page break.");
		}
	}

	public void setRepeatRowHeader(boolean repeat) {
		// TODO wait content support
	}

	public String getHeight() {
		DimensionType height = content.getHeight();
		if (height != null) {
			return height.toString();
		}
		return null;
	}

	public String getHelpText() {
		return content.getHelpText();
	}

	public String getHorizontalPosition() {
		DimensionType x = content.getX();
		if (x != null) {
			return x.toString();
		}
		return null;
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
		UserPropertyDefnHandle prop = modelHandle.getUserPropertyDefnHandle(name);
		if (prop != null) {
			return modelHandle.getProperty(prop.getName());
		}
		return null;
	}

	public String getVerticalPosition() {
		DimensionType y = content.getY();
		if (y != null) {
			return y.toString();
		}
		return null;
	}

	public String getWidth() {
		DimensionType width = content.getWidth();
		if (width != null) {
			return width.toString();
		}
		return null;
	}

	public void setHeight(String height) {
		content.setHeight(DimensionType.parserUnit(height));
	}

	public void setHelpText(String help) {
		content.setHelpText(help);
	}

	public void setHorizontalPosition(String position) {
		content.setX(DimensionType.parserUnit(position));
	}

	public void setName(String name) {
		content.setName(name);
	}

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

	public void setVerticalPosition(String position) {
		content.setY(DimensionType.parserUnit(position));
	}

	public void setWidth(String width) {
		String unit = content.getReportContent().getDesign().getReportDesign().getDefaultUnits();
		content.setWidth(DimensionType.parserUnit(width, unit));
	}

}
