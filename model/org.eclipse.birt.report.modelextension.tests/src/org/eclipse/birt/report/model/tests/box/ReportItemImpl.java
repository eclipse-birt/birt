/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.tests.box;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.CompatibilityStatus;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IElementCommand;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.extension.UndefinedPropertyInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.scripts.MethodInfo;

/**
 * Implements <code>IReportItem</code> for testing.
 */

public class ReportItemImpl extends ReportItem implements IReportItem {

	public static final String USAGE_PROP = "usage"; //$NON-NLS-1$

	protected IReportItemFactory cachedDefn = null;
	protected ModuleHandle moduleHandle = null;
	protected DesignElementHandle extItemHandle = null;

	private String usage;

	private boolean refreshNeeded = false;

	/**
	 * Constructs an element.
	 * 
	 * @param extDefn
	 * @param elementHandle
	 */

	public ReportItemImpl(IReportItemFactory extDefn, DesignElementHandle elementHandle) {
		this.cachedDefn = extDefn;
		assert elementHandle != null;
		this.moduleHandle = elementHandle.getModuleHandle();
		this.extItemHandle = elementHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#serialize(java.lang
	 * .String)
	 */
	public ByteArrayOutputStream serialize(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#deserialize(java.lang
	 * .String, java.io.ByteArrayInputStream)
	 */
	public void deserialize(String propName, ByteArrayInputStream data) throws ExtendedElementException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#getProperty(java.lang
	 * .String)
	 */
	public Object getProperty(String propName) {
		if (USAGE_PROP.equalsIgnoreCase(propName)) {
			return usage;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#checkProperty(java.
	 * lang.String, java.lang.Object)
	 */
	public void checkProperty(String propName, Object value) throws ExtendedElementException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#setProperty(java.lang
	 * .String, java.lang.Object)
	 */
	public void setProperty(String propName, Object value) {
		moduleHandle.getCommandStack().execute(getElementCommand(propName, value));
	}

	public void doSetProperty(String propName, Object value) {
		if (value == null)
			return;

		if (USAGE_PROP.equalsIgnoreCase(propName)) {
			usage = value.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#validate()
	 */
	public List validate() {
		ExtendedElementException exception = new ExtendedElementException(extItemHandle.getElement(),
				"test.testingbox.plugin", "1", null);//$NON-NLS-1$ //$NON-NLS-2$
		exception.setProperty(ExtendedElementException.LINE_NUMBER, "15"); //$NON-NLS-1$
		exception.setProperty(ExtendedElementException.LOCALIZED_MESSAGE, "local actuate"); //$NON-NLS-1$
		List list = new ArrayList();
		list.add(exception);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#copy()
	 */
	public IReportItem copy() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.extension.IExtendedElement#getElementCommand(java
	 * .lang.String, java.lang.Object)
	 */
	public IElementCommand getElementCommand(String propName, Object value) {
		return new ElementCommandImpl(this, propName, value, extItemHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#getPropertyDefinitions()
	 */
	public IPropertyDefinition[] getPropertyDefinitions() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.IElement#refreshPropertyDefinition ()
	 */
	public boolean refreshPropertyDefinition() {
		if (refreshNeeded) {
			refreshNeeded = false;
			return true;
		}

		return false;
	}

	public DesignElementHandle getExtItemHandle() {

		return extItemHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.IReportItem#getMethods()
	 */

	public IPropertyDefinition[] getMethods() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getSimpleElement()
	 */

	public org.eclipse.birt.report.model.api.simpleapi.IReportItem getSimpleElement() {
		return new Box(this, (ExtendedItemHandle) extItemHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#getMethods(java
	 * .lang.String)
	 */

	public IMethodInfo[] getMethods(String methodName) {
		if ("onPrepare".equalsIgnoreCase(methodName)) //$NON-NLS-1$
			return null;

		if ("onRender".equalsIgnoreCase(methodName)) //$NON-NLS-1$
		{
			IMethodInfo[] info = new IMethodInfo[1];

			try {
				info[0] = new TmpMethodInfo(Box.class.getMethod("getMethod1", //$NON-NLS-1$
						null));
			} catch (NoSuchMethodException e) {
				assert false;
			}

			return info;
		}

		if ("onCreate".equalsIgnoreCase(methodName)) //$NON-NLS-1$
		{
			IMethodInfo[] info = new IMethodInfo[1];

			try {
				info[0] = new TmpMethodInfo(
						TmpOnCreate.class.getMethod("performOnCreate", new Class[] { Boolean.class })); //$NON-NLS-1$
			} catch (NoSuchMethodException e) {
				assert false;
			}

			return info;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#checkCompatibility ()
	 */
	public CompatibilityStatus checkCompatibility() {
		try {
			Map propMap = ((ExtendedItemHandle) extItemHandle).getUndefinedProperties();
			UndefinedPropertyInfo shape = (UndefinedPropertyInfo) propMap.get("shape"); //$NON-NLS-1$
			if (shape != null)
				extItemHandle.setProperty("shape", "cube"); //$NON-NLS-1$ //$NON-NLS-2$

			Map illegalContents = ((ExtendedItemHandle) extItemHandle).getIllegalContents();
			String propName = "header"; //$NON-NLS-1$
			List headerContents = (List) illegalContents.get(propName);
			if (headerContents != null && !headerContents.isEmpty()) {
				extItemHandle.clearProperty(propName);
				LabelHandle label = extItemHandle.getModuleHandle().getElementFactory().newLabel(null);
				extItemHandle.add(propName, label);
			}
		} catch (SemanticException e) {
			// do nothing
		}

		boolean hasCompatibilities = false;
		ExtendedItemHandle extHandle = (ExtendedItemHandle) extItemHandle;
		if (!extHandle.getUndefinedProperties().isEmpty() || !extHandle.getIllegalContents().isEmpty())
			hasCompatibilities = true;
		int type = hasCompatibilities ? CompatibilityStatus.CONVERT_COMPATIBILITY_TYPE : CompatibilityStatus.OK_TYPE;
		return new CompatibilityStatus(Collections.EMPTY_LIST, type);
	}

	private static class TmpMethodInfo extends MethodInfo {

		TmpMethodInfo(Method method) {
			super(method);
		}
	}

	static class TmpOnCreate {

		public String performOnCreate(Boolean flag) {
			return null;
		}
	}
}
