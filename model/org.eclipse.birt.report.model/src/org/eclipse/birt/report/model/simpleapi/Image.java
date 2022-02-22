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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.api.simpleapi.IImage;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

public class Image extends ReportItem implements IImage {

	public Image(ImageHandle image) {
		super(image);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getScale()
	 */

	@Override
	public double getScale() {
		return ((ImageHandle) handle).getScale();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSize()
	 */

	@Override
	public String getSize() {
		return ((ImageHandle) handle).getSize();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltText()
	 */
	@Override
	@Deprecated
	public String getAltText() {
		ExpressionHandle expr = ((ImageHandle) handle).getAltTextExpression();
		if (expr != null) {
			return expr.getStringExpression();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IImage#setAltText(java.lang
	 * .String)
	 */
	@Override
	@Deprecated
	public void setAltText(String altText) throws SemanticException {
		setProperty(IReportItemModel.ALTTEXT_PROP, new Expression(altText, ExpressionType.CONSTANT));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getAltTextKey()
	 */
	@Override
	public String getAltTextKey() {
		return ((ImageHandle) handle).getAltTextKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IImage#setAltTextKey(java
	 * .lang.String)
	 */
	@Override
	public void setAltTextKey(String altTextKey) throws SemanticException {
		setProperty(IReportItemModel.ALTTEXT_KEY_PROP, altTextKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getSource()
	 */

	@Override
	public String getSource() {
		return ((ImageHandle) handle).getSource();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setSource(java
	 * .lang.String)
	 */

	@Override
	public void setSource(String source) throws SemanticException {

		setProperty(IImageItemModel.SOURCE_PROP, source);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURI()
	 */

	@Override
	public String getURI() {
		return ((ImageHandle) handle).getURI();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getTypeExpression ()
	 */

	@Override
	public String getTypeExpression() {
		return ((ImageHandle) handle).getTypeExpression();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getValueExpression
	 * ()
	 */

	@Override
	public String getValueExpression() {
		return ((ImageHandle) handle).getValueExpression();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getImageName()
	 */

	@Override
	public String getImageName() {
		return ((ImageHandle) handle).getImageName();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setImageName
	 * (java.lang.String)
	 */

	@Override
	public void setImageName(String name) throws SemanticException {

		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);

		try {
			((ImageHandle) handle).setImageName(name);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();

	}

	/**
	 * @deprecated (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURI(java.lang.String)
	 */

	@Deprecated
	@Override
	public void setURI(String uri) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setURI(uri);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setScale(double)
	 */

	@Override
	public void setScale(double scale) throws SemanticException {

		setProperty(IImageItemModel.SCALE_PROP, Double.valueOf(scale));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setSize(java
	 * .lang.String)
	 */

	@Override
	public void setSize(String size) throws SemanticException {
		setProperty(IImageItemModel.SIZE_PROP, size);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setTypeExpression
	 * (java.lang.String)
	 */

	@Override
	public void setTypeExpression(String value) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setTypeExpression(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#setValueExpression
	 * (java.lang.String)
	 */

	@Override
	public void setValueExpression(String value) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setValueExpression(value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getHelpText()
	 */

	@Override
	public String getHelpText() {
		return ((ImageHandle) handle).getHelpText();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setHelpText(
	 * java.lang.String)
	 */

	@Override
	public void setHelpText(String helpText) throws SemanticException {

		setProperty(IImageItemModel.HELP_TEXT_PROP, helpText);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IImage#getHelpTextKey()
	 */

	@Override
	public String getHelpTextKey() {
		return ((ImageHandle) handle).getHelpTextKey();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setHelpTextKey
	 * (java.lang.String)
	 */

	@Override
	public void setHelpTextKey(String helpTextKey) throws SemanticException {
		setProperty(IImageItemModel.HELP_TEXT_ID_PROP, helpTextKey);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IImage#getAction()
	 */
	@Override
	public IAction getAction() {
		return new ActionImpl(((ImageHandle) handle).getActionHandle(), (ImageHandle) handle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setFile()
	 */
	@Override
	public void setFile(String file) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setFile(file);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getFile()
	 */
	@Override
	public String getFile() {
		return ((ImageHandle) handle).getFile();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURL()
	 */
	@Override
	public void setURL(String url) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setURL(url);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURL()
	 */
	@Override
	public String getURL() {
		return ((ImageHandle) handle).getURL();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.simpleapi.IImage#addAction(org.eclipse
	 * .birt.report.model.api.simpleapi.IAction)
	 */
	@Override
	public void addAction(IAction action) throws SemanticException {
		if (action == null) {
			return;
		}

		ActivityStack cmdStack = handle.getModule().getActivityStack();
		cmdStack.startNonUndoableTrans(null);
		try {
			((ImageHandle) handle).setAction((Action) action.getStructure());
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

}
