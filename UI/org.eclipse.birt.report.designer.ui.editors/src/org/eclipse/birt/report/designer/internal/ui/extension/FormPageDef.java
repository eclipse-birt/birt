/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.actions.PageSetAction;
import org.eclipse.birt.report.designer.ui.editors.extension.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorActionBarContributor;

/**
 * FormPageDef
 */
public class FormPageDef implements IExtensionConstants {

	private IConfigurationElement element;

	public String id;
	public String targetEditor;
	public String displayName;
	public String pageClass;
	public boolean visible = true;
	public String relative;
	public int position;
	public IAction pageAction;
	public int priority;

	FormPageDef(IConfigurationElement element) {
		this.element = element;
		id = loadStringAttribute(element, ATTRIBUTE_ID);
		displayName = loadStringAttribute(element, ATTRIBUTE_DISPLAY_NAME);
		pageClass = loadStringAttribute(element, ATTRIBUTE_CLASS);
		visible = loadBooleanAttribute(element, ATTRIBUTE_VISIBLE);
		relative = loadStringAttribute(element, ATTRIBUTE_RELATIVE);
		position = loadPosition(element, ATTRIBUTE_POSITION);
		this.priority = loadPriority(element, ATTRIBUTE_PRIORITY);

		if (loadStringAttribute(element, ATTRIBUTE_PAGE_ACTION) != null) {
			pageAction = (IAction) loadClass(element, ATTRIBUTE_PAGE_ACTION);
		}
		if (pageAction == null) {
			pageAction = new PageSetAction(displayName, id);
		}
	}

	private int loadPriority(IConfigurationElement element, String attributeName) {
		String attribute = element.getAttribute(attributeName);
		if ("normal".equals(attribute)) { //$NON-NLS-1$
			return 1;
		} else if ("high".equals(attribute)) { //$NON-NLS-1$
			return 2;
		} else if ("low".equals(attribute)) { //$NON-NLS-1$
			return 0;
		}
		return 1;
	}

	private int loadPosition(IConfigurationElement element, String attributeName) {
		String attribute = element.getAttribute(attributeName);
		if ("left".equals(attribute)) //$NON-NLS-1$
		{
			return 0;
		} else if ("right".equals(attribute)) //$NON-NLS-1$
		{
			return 1;
		}
		// default as right
		return 1;
	}

	private String loadStringAttribute(IConfigurationElement element, String attributeName) {
		return element.getAttribute(attributeName);
	}

	private boolean loadBooleanAttribute(IConfigurationElement element, String attributeName) {
		String value = element.getAttribute(attributeName);
		if (value != null) {
			return Boolean.valueOf(value).booleanValue();
		}
		return true;
	}

	private Object loadClass(IConfigurationElement element, String attributeName) {
		Object clazz = null;
		try {
			if (element.getAttribute(attributeName) != null) {
				clazz = element.createExecutableExtension(attributeName);
			}
		} catch (CoreException e) {
			ExceptionUtil.handle(e);
		}
		return clazz;
	}

	public IReportEditorPage createPage() {
		Object def = loadClass(element, ATTRIBUTE_CLASS);
		if (def instanceof IReportEditorPage) {
			return (IReportEditorPage) def;
		}
		return null;
	}

	public IEditorActionBarContributor createActionBarContributor() {
		Object def = loadClass(element, ATTRIBUTE_ACTION_BAR_CONTRIBUTOR);
		if (def instanceof IEditorActionBarContributor) {
			return (IEditorActionBarContributor) def;
		}
		return null;
	}
}
