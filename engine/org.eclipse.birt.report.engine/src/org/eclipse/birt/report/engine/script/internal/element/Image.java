/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.engine.api.script.element.IImage;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Image extends ReportItem implements IImage {

	public Image(ImageHandle image) {
		super(image);
	}

	public Image(org.eclipse.birt.report.model.api.simpleapi.IImage imageImpl) {
		super(imageImpl);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getScale()
	 */

	@Override
	public double getScale() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getScale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSize()
	 */

	@Override
	public String getSize() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getSize();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltText()
	 */

	@Override
	public String getAltText() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getAltText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setAltText(java.lang
	 * .String)
	 */
	@Override
	public void setAltText(String altText) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setAltText(altText);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltTextKey()
	 */

	@Override
	public String getAltTextKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getAltTextKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setAltTextKey(java.
	 * lang.String)
	 */
	@Override
	public void setAltTextKey(String altTextKey) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setAltTextKey(altTextKey);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSource()
	 */

	@Override
	public String getSource() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getSource();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setSource(java.lang.
	 * String)
	 */

	@Override
	public void setSource(String source) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setSource(source);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURI()
	 */

	@Override
	public String getURI() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getURI();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getTypeExpression()
	 */

	@Override
	public String getTypeExpression() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getTypeExpression();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getValueExpression()
	 */

	@Override
	public String getValueExpression() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getValueExpression();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getImageName()
	 */

	@Override
	public String getImageName() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getImageName();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setImageName(java.
	 * lang.String)
	 */

	@Override
	public void setImageName(String name) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setImageName(name);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * @deprecated (non-Javadoc)
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURI(java.lang.String)
	 */

	@Deprecated
	@Override
	public void setURI(String uri) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setURI(uri);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setScale(double)
	 */

	@Override
	public void setScale(double scale) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setScale(scale);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setSize(java.lang.
	 * String)
	 */

	@Override
	public void setSize(String size) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setSize(size);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setTypeExpression(
	 * java.lang.String)
	 */

	@Override
	public void setTypeExpression(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setTypeExpression(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setValueExpression(
	 * java.lang.String)
	 */

	@Override
	public void setValueExpression(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setValueExpression(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getHelpText()
	 */

	@Override
	public String getHelpText() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getHelpText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setHelpText(java.
	 * lang.String)
	 */

	@Override
	public void setHelpText(String helpText) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setHelpText(helpText);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getHelpTextKey()
	 */

	@Override
	public String getHelpTextKey() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getHelpTextKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setHelpTextKey(java.
	 * lang.String)
	 */

	@Override
	public void setHelpTextKey(String helpTextKey) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setHelpTextKey(helpTextKey);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public IAction getAction() {
		return new ActionImpl(((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getAction());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setFile()
	 */
	@Override
	public void setFile(String file) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setFile(file);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getFile()
	 */
	@Override
	public String getFile() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getFile();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURL()
	 */
	@Override
	public void setURL(String url) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).setURL(url);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURL()
	 */
	@Override
	public String getURL() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IImage) designElementImpl).getURL();
	}

	@Override
	public void addAction(IAction action) {
		// TODO Auto-generated method stub

	}

}
