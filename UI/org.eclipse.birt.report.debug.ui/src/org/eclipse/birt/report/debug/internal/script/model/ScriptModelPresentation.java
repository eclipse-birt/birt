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

package org.eclipse.birt.report.debug.internal.script.model;

import java.io.File;

import org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsInput;
import org.eclipse.birt.report.debug.internal.ui.script.launcher.sourcelookup.ScriptLocalFileStorage;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IExpression;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorInput;

/**
 * Present the script debug element.
 */
public class ScriptModelPresentation extends LabelProvider implements IDebugModelPresentation {

	/**
	 * Editor id , define in the plugin.xml
	 */
	private static final String EDITOR_ID = "org.eclipse.birt.report.debug.internal.ui.script.editor.DebugJsEditor";//$NON-NLS-1$

	/**
	 * Constructor
	 */
	public ScriptModelPresentation() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.IDebugModelPresentation#computeDetail(org.eclipse.debug.
	 * core.model.IValue, org.eclipse.debug.ui.IValueDetailListener)
	 */
	@Override
	public void computeDetail(IValue value, IValueDetailListener listener) {
		// show the string when mouse hover at the value in the watch view.
		String detail = ""; //$NON-NLS-1$
		try {
			detail = value.getValueString();
		} catch (DebugException e) {
		}
		listener.detailComputed(value, detail);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.IDebugModelPresentation#setAttribute(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setAttribute(String attribute, Object value) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.ui.ISourcePresentation#getEditorId(org.eclipse.ui.
	 * IEditorInput, java.lang.Object)
	 */
	@Override
	public String getEditorId(IEditorInput input, Object element) {
		if (element instanceof ScriptLocalFileStorage || element instanceof ILineBreakpoint) {
			return EDITOR_ID;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.ui.ISourcePresentation#getEditorInput(java.lang.Object)
	 */
	@Override
	public IEditorInput getEditorInput(Object element) {
		if (element instanceof ScriptLocalFileStorage) {
			ScriptLocalFileStorage storage = (ScriptLocalFileStorage) element;
			return new DebugJsInput(storage.getFile(), storage.getModelIdentifier());
		} else if (element instanceof ScriptLineBreakpoint) {
			ScriptLineBreakpoint breakPoint = (ScriptLineBreakpoint) element;
			String str = breakPoint.getFileName();
			return new DebugJsInput(new File(str), breakPoint.getSubName());
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		try {
			if (element instanceof ScriptDebugElement) {
				return ((ScriptDebugElement) element).getDisplayName();
			} else if (element instanceof IExpression) {
				return getExpressionText((IExpression) element);
			} else if (element instanceof ScriptLineBreakpoint) {
				ScriptLineBreakpoint breakPoint = (ScriptLineBreakpoint) element;
				int index = breakPoint.getFileName().lastIndexOf(File.separator);
				String str = breakPoint.getFileName().substring(index + 1);
				return str + " [line: " //$NON-NLS-1$
						+ breakPoint.getScriptLineNumber() + "]" //$NON-NLS-1$
						+ " - " //$NON-NLS-1$
						+ breakPoint.getDisplayName();
			}
		} catch (DebugException e) {
			// do nothing
		}
		return super.getText(element);
	}

	/**
	 * Gets the expression display name
	 *
	 * @param expression
	 * @return
	 * @throws DebugException
	 */
	private String getExpressionText(IExpression expression) throws DebugException {
		StringBuilder buff = new StringBuilder();
		IValue javaValue = expression.getValue();

		buff.append('"' + expression.getExpressionText() + '"');

		if (javaValue != null) {
			String valueString = getValueText(javaValue);
			if (valueString.length() > 0) {
				buff.append("= "); //$NON-NLS-1$
				buff.append(valueString);
			}
		}
		return buff.toString();
	}

	/**
	 * @param value
	 * @return
	 * @throws DebugException
	 */
	private String getValueText(IValue value) throws DebugException {

		String refTypeName = value.getReferenceTypeName();
		String valueString = value.getValueString();
		boolean isString = false;

		// boolean isObject = true;
		// boolean isArray = value instanceof IJavaArray;
		StringBuilder buffer = new StringBuilder();
		// Always show type name for objects & arrays (but not Strings)
		if (!isString && (refTypeName.length() > 0)) {
			String qualTypeName = refTypeName;
			buffer.append(qualTypeName);
			buffer.append(' ');

		}

		// Put double quotes around Strings
		if (valueString != null && (isString || valueString.length() > 0)) {
			if (isString) {
				buffer.append('"');
			}
			buffer.append(valueString);
			if (isString) {
				buffer.append('"');
			}
		}

		return buffer.toString();
	}

	/**
	 * Gets the variable Text
	 *
	 * @param var
	 * @return
	 */
	public String getVariableText(ScriptValue var) {
		try {
			return var.getValueString();
		} catch (DebugException e) {
			return null;
		}
	}
}
