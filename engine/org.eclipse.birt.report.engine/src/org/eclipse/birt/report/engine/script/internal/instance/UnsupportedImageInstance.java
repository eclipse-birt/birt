/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IActionInstance;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * 
 */

public class UnsupportedImageInstance implements IImageInstance {

	private ITextContent content;

	public UnsupportedImageInstance(ITextContent content) {
		this.content = content;
	}

	public String getHyperlink() {
		return null;
	}

	public String getName() {
		return null;
	}

	public void setName(String name) {
	}

	public String getHelpText() {
		return null;
	}

	public void setHelpText(String helpText) {
	}

	public IScriptStyle getStyle() {
		return null;
	}

	public String getHorizontalPosition() {
		return null;
	}

	public void setHorizontalPosition(String position) {
	}

	public String getVerticalPosition() {
		return null;
	}

	public void setVerticalPosition(String position) {
	}

	public String getWidth() {
		return null;
	}

	public void setWidth(String width) {
	}

	public String getHeight() {
		return null;
	}

	public void setHeight(String height) {
	}

	public Object getNamedExpressionValue(String name) {
		return null;
	}

	public Object getUserPropertyValue(String name) {
		return null;
	}

	public void setUserPropertyValue(String name, Object value) throws ScriptException {
	}

	public IReportElementInstance getParent() throws ScriptException {
		return null;
	}

	public IRowData getRowData() throws ScriptException {
		return null;
	}

	public String getAltText() {
		return content.getText();
	}

	public void setAltText(String altText) {
	}

	public String getAltTextKey() {
		return null;
	}

	public void setAltTextKey(String altTextKey) {
	}

	public String getURI() {
		return null;
	}

	public void setURI(String uri) {
	}

	public int getImageSource() {
		return 0;
	}

	public String getImageName() {
		return null;
	}

	public void setImageName(String imageName) {
	}

	public byte[] getData() {
		return null;
	}

	public void setData(byte[] data) {
	}

	public String getMimeType() {
		return null;
	}

	public void setMimeType(String type) {
	}

	public void setURL(String url) {
	}

	public String getURL() {
		return null;
	}

	public void setFile(String file) {
	}

	public String getFile() {
		return null;
	}

	public IActionInstance createAction() {
		return null;
	}

	public IActionInstance getAction() {
		return null;
	}

	public void setAction(IActionInstance actionInstance) {
	}
}
