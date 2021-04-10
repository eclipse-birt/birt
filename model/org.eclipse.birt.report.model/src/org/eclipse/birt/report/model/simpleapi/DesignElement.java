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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.UserPropertyDefnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IReportDesign;
import org.eclipse.birt.report.model.api.simpleapi.IStyle;
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
	public IStyle getStyle() {
		return new Style(handle.getPrivateStyle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.engine.api.script.element.IReportItem#
	 * getQualifiedName()
	 */

	public String getQualifiedName() {

		return handle.getQualifiedName();
	}

	public String getNamedExpression(String name) {
		UserPropertyDefnHandle propDefn = handle.getUserPropertyDefnHandle(name);
		Object userProp = getUserProperty(name);
		if (propDefn == null || userProp == null || propDefn.getType() != IPropertyType.EXPRESSION_TYPE)
			return null;
		return userProp.toString();
	}

	public void setNamedExpression(String name, String exp) throws SemanticException {
		UserPropertyDefnHandle propDefn = handle.getUserPropertyDefnHandle(name);

		if (propDefn == null) {
			addUserProperty(name, IPropertyType.EXPRESSION_TYPE_NAME);
		} else if (propDefn.getType() != IPropertyType.EXPRESSION_TYPE)
			return;

		setUserProperty(name, exp);
	}

	public Object getUserProperty(String name) {
		return handle.getProperty(name);
	}

	public void setUserProperty(String name, String value) throws SemanticException {
		if (handle.getUserPropertyDefnHandle(name) == null)
			addUserProperty(name, IPropertyType.STRING_TYPE_NAME);

		handle.setProperty(name, value);
	}

	public void setUserProperty(String name, Object value, String type) throws SemanticException {
		if (handle.getUserPropertyDefnHandle(name) == null)
			addUserProperty(name, type);

		handle.setProperty(name, value);

	}

	public IDesignElement getParent() {
		return ElementUtil.getElement(handle.getContainer());
	}

	private void addUserProperty(String name, String type) throws SemanticException {
		UserPropertyDefn newProp = new UserPropertyDefn();
		newProp.setName(name);
		newProp.setType(MetaDataDictionary.getInstance().getPropertyType(type));

		handle.addUserPropertyDefn(newProp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#getReport()
	 */
	public IReportDesign getReport() {
		if (handle == null)
			return null;

		ModuleHandle root = handle.getRoot();
		if (!(root instanceof ReportDesignHandle))
			return null;

		return new ReportDesign((ReportDesignHandle) root);
	}

	/**
	 * Sets the property of the design element.
	 * 
	 * @param propName the property name
	 * @param value    the value
	 * @throws SemanticException
	 */

	protected void setProperty(String propName, Object value) throws SemanticException {
		ActivityStack cmdStack = handle.getModule().getActivityStack();

		cmdStack.startNonUndoableTrans(null);
		try {
			handle.setProperty(propName, value);
		} catch (SemanticException e) {
			cmdStack.rollback();
			throw e;
		}

		cmdStack.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IDesignElement#
	 * getUserPropertyExpression(java.lang.String)
	 */
	public Object getUserPropertyExpression(String name) {
		return handle.getProperty(name);
	}

}
