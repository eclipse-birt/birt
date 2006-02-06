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
import org.eclipse.birt.report.engine.api.script.element.IScriptStyleDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

public class DesignElement implements IDesignElement {

	protected DesignElementHandle handle;

	public DesignElement(DesignElementHandle handle) {
		this.handle = handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getStyle()
	 */
	public IScriptStyleDesign getStyle() {
		return new StyleDesign(handle.getPrivateStyle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getName()
	 */

	public String getName() {
		return handle.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#getQualifiedName()
	 */

	public String getQualifiedName() {

		return handle.getQualifiedName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IReportItem#setName(java.lang.String)
	 */

	public void setName(String name) throws ScriptException {
		try {
			handle.setName(name);
		} catch (NameException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public String getNamedExpression(String name) {
		UserPropertyDefnHandle propDefn = handle
				.getUserPropertyDefnHandle(name);
		Object userProp = getUserProperty(name);
		if (propDefn == null || userProp == null
				|| propDefn.getType() != IPropertyType.EXPRESSION_TYPE)
			return null;
		return userProp.toString();
	}

	public void setNamedExpression(String name, String exp)
			throws ScriptException {
		UserPropertyDefnHandle propDefn = handle
				.getUserPropertyDefnHandle(name);

		if (propDefn == null) {
			addUserProperty(name, IPropertyType.EXPRESSION_TYPE_NAME);
		} else if (propDefn.getType() != IPropertyType.EXPRESSION_TYPE)
			return;

		setUserProperty(name, exp);
	}

	public Object getUserProperty(String name) {
		return handle.getProperty(name);
	}

	public void setUserProperty(String name, String value)
			throws ScriptException {
		if (handle.getUserPropertyDefnHandle(name) == null)
			addUserProperty(name, IPropertyType.STRING_TYPE_NAME);
		try {
			handle.setProperty(name, value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public void setUserProperty(String name, Object value, String type)
			throws ScriptException {
		if (handle.getUserPropertyDefnHandle(name) == null)
			addUserProperty(name, type);
		try {
			handle.setProperty(name, value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	public IDesignElement getParent() {
		return ElementUtil.getElement(handle.getContainer());
	}

	private void addUserProperty(String name, String type)
			throws ScriptException {
		UserPropertyDefn newProp = new UserPropertyDefn();
		newProp.setName(name);
		newProp.setType(MetaDataDictionary.getInstance().getPropertyType(type));
		try {
			handle.addUserPropertyDefn(newProp);
		} catch (UserPropertyException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

}
