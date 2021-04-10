/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDesignElement;
import org.eclipse.birt.report.engine.api.script.element.IReportDesign;
import org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class DesignElement implements IDesignElement {

	protected org.eclipse.birt.report.model.api.simpleapi.IDesignElement designElementImpl;

	public DesignElement(DesignElementHandle handle) {
		designElementImpl = SimpleElementFactory.getInstance().getElement(handle);
	}

	public DesignElement(org.eclipse.birt.report.model.api.simpleapi.IDesignElement element) {
		designElementImpl = element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getStyle()
	 */
	public IScriptStyleDesign getStyle() {
		return new StyleDesign(designElementImpl.getStyle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * getQualifiedName()
	 */

	public String getQualifiedName() {
		return designElementImpl.getQualifiedName();
	}

	public String getNamedExpression(String name) {
		return designElementImpl.getNamedExpression(name);
	}

	public void setNamedExpression(String name, String exp) throws ScriptException {
		try {
			designElementImpl.setNamedExpression(name, exp);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public Object getUserProperty(String name) {
		return designElementImpl.getUserProperty(name);
	}

	public void setUserProperty(String name, String value) throws ScriptException {
		try {
			designElementImpl.setUserProperty(name, value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public void setUserProperty(String name, Object value, String type) throws ScriptException {
		try {
			designElementImpl.setUserProperty(name, value, type);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public IDesignElement getParent() {
		return ElementUtil.getElement(designElementImpl.getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IDesignElement#getReport()
	 */
	public IReportDesign getReport() {
		return (IReportDesign) ElementUtil.getElement(designElementImpl.getReport());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IDesignElement#
	 * getUserPropertyExpression(java.lang.String)
	 */
	public Object getUserPropertyExpression(String name) {
		return designElementImpl.getUserPropertyExpression(name);
	}

}
