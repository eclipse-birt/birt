/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

	@Override
	public String getHyperlink() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) {
	}

	@Override
	public String getHelpText() {
		return null;
	}

	@Override
	public void setHelpText(String helpText) {
	}

	@Override
	public IScriptStyle getStyle() {
		return null;
	}

	@Override
	public String getHorizontalPosition() {
		return null;
	}

	@Override
	public void setHorizontalPosition(String position) {
	}

	@Override
	public String getVerticalPosition() {
		return null;
	}

	@Override
	public void setVerticalPosition(String position) {
	}

	@Override
	public String getWidth() {
		return null;
	}

	@Override
	public void setWidth(String width) {
	}

	@Override
	public String getHeight() {
		return null;
	}

	@Override
	public void setHeight(String height) {
	}

	@Override
	public Object getNamedExpressionValue(String name) {
		return null;
	}

	@Override
	public Object getUserPropertyValue(String name) {
		return null;
	}

	@Override
	public void setUserPropertyValue(String name, Object value) throws ScriptException {
	}

	@Override
	public IReportElementInstance getParent() throws ScriptException {
		return null;
	}

	@Override
	public IRowData getRowData() throws ScriptException {
		return null;
	}

	@Override
	public String getAltText() {
		return content.getText();
	}

	@Override
	public void setAltText(String altText) {
	}

	@Override
	public String getAltTextKey() {
		return null;
	}

	@Override
	public void setAltTextKey(String altTextKey) {
	}

	@Override
	public String getURI() {
		return null;
	}

	@Override
	public void setURI(String uri) {
	}

	@Override
	public int getImageSource() {
		return 0;
	}

	@Override
	public String getImageName() {
		return null;
	}

	@Override
	public void setImageName(String imageName) {
	}

	@Override
	public byte[] getData() {
		return null;
	}

	@Override
	public void setData(byte[] data) {
	}

	@Override
	public String getMimeType() {
		return null;
	}

	@Override
	public void setMimeType(String type) {
	}

	@Override
	public void setURL(String url) {
	}

	@Override
	public String getURL() {
		return null;
	}

	@Override
	public void setFile(String file) {
	}

	@Override
	public String getFile() {
		return null;
	}

	@Override
	public IActionInstance createAction() {
		return null;
	}

	@Override
	public IActionInstance getAction() {
		return null;
	}

	@Override
	public void setAction(IActionInstance actionInstance) {
	}
}
