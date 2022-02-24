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

import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.editor.script.ScriptDocumentProvider;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

/**
 * ScriptLineBreakpoint
 */
public class ScriptLineBreakpoint extends LineBreakpoint {
	private static final Logger logger = Logger.getLogger(ScriptLineBreakpoint.class.getName());
	/**
	 * Break point type, script line break point
	 */
	public static final String LINEBREAKPOINT = "line break point";//$NON-NLS-1$
	/**
	 * Break point type, run to line break point (CTRL+R);
	 */
	public static final String RUNTOLINE = "run to line";//$NON-NLS-1$
	/**
	 * ID key
	 */
	public static final String SUBNAME = ScriptDocumentProvider.SUBNAME;
	private static final String FILENAME = ScriptDocumentProvider.FILENAME;
	private static final String DISPLAYNAME = "display name";//$NON-NLS-1$

	private String type = LINEBREAKPOINT;

	/**
	 * Constructor
	 */
	public ScriptLineBreakpoint() {
	}

	/**
	 * Constructor
	 * 
	 * @param resource
	 * @param name
	 * @param subName
	 * @param lineNumber
	 * @throws CoreException
	 */
	public ScriptLineBreakpoint(final IResource resource, final String name, final String subName, final int lineNumber,
			final String displayName) throws CoreException {
		assert resource != null;
		assert subName != null;
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {

			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = resource.createMarker(ScriptDocumentProvider.MARK_TYPE);// $NON-NLS-1$
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: "//$NON-NLS-1$
						+ resource.getName() + " [line: "//$NON-NLS-1$
						+ lineNumber + "]");//$NON-NLS-1$
				marker.setAttribute(SUBNAME, subName);
				marker.setAttribute(FILENAME, name);
				setDisplayName(displayName);
			}
		};
		run(getMarkerRule(resource), runnable);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
	public String getModelIdentifier() {
		return IScriptConstants.SCRIPT_DEBUG_MODEL;
	}

	/**
	 * Sets the display name;
	 * 
	 * @param name
	 */
	public void setDisplayName(String name) {
		try {
			getMarker().setAttribute(DISPLAYNAME, name);
		} catch (CoreException e) {

		}
	}

	/**
	 * Gets the display name.
	 * 
	 * @return
	 */
	public String getDisplayName() {
		String retValue = ""; //$NON-NLS-1$
		try {
			retValue = (String) getMarker().getAttribute(DISPLAYNAME);
		} catch (CoreException e) {
			// do nothing
		}
		if (retValue == null || retValue.length() == 0) {
			retValue = getSubName();
		}
		return retValue;
	}

	/**
	 * Gets the id.
	 * 
	 * @return
	 */
	public String getSubName() {
		try {
			return (String) getMarker().getAttribute(SUBNAME);
		} catch (CoreException e) {
			logger.warning(e.getMessage());
			throw new Error("Don't set the sub name");//$NON-NLS-1$
		}
	}

	/**
	 * Gets the file name.
	 * 
	 * @return
	 */
	public String getFileName() {
		try {
			return (String) getMarker().getAttribute(FILENAME);
		} catch (CoreException e) {
			return "";
		}
	}

	/**
	 * Gets the break point line number.
	 * 
	 * @return
	 */
	public int getScriptLineNumber() {
		try {
			return getLineNumber();
		} catch (CoreException e) {
			// return 1;
		}
		return 1;
	}

	/**
	 * Gets the break point type See LINEBREAKPOINT and RUNTOLINE
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the break point type.
	 * 
	 * @param type, LINEBREAKPOINT and RUNTOLINE
	 */
	public void setType(String type) {
		this.type = type;
	}

	public boolean shouldSkipBreakpoint() throws CoreException {
		DebugPlugin plugin = DebugPlugin.getDefault();
		return plugin != null && isRegistered() && !plugin.getBreakpointManager().isEnabled();
	}
}
