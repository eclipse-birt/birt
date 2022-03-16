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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Run to line break point to support <CTRL + R >.It is a temp break point, The
 * VM server would delete it after thres resume ever time, so it don't need the
 * mark.
 */
//NOTE Because don't need the mark, but the IBreakPoint need a mark.About this, the script debug is defferert from the java debug.
//The java debug manager the RunToLinebreakPoint on the client.
public class RunToLinebreakPoint extends ScriptLineBreakpoint {
	/**
	 * file name
	 */
	private String name;

	/**
	 * Breal point ID
	 */
	private String subName;

	/**
	 * Line number
	 */
	private int lineNumber;

	/**
	 * Constructor
	 *
	 * @param resource
	 * @param name
	 * @param subName
	 * @param lineNumber
	 * @throws CoreException
	 */
	public RunToLinebreakPoint(final IResource resource, final String name, final String subName, final int lineNumber)
			throws CoreException {
		super();
		this.name = name;
		this.subName = subName;
		this.lineNumber = lineNumber;
	}

	/*
	 * Overwrite the method, return the name directly, not from the mark.
	 *
	 * @see
	 * org.eclipse.birt.report.debug.internal.script.model.ScriptLineBreakpoint#
	 * getSubName()
	 */
	@Override
	public String getSubName() {
		return subName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.debug.internal.script.model.ScriptLineBreakpoint#
	 * getFileName()
	 */
	@Override
	public String getFileName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.LineBreakpoint#getLineNumber()
	 */
	@Override
	public int getLineNumber() throws CoreException {
		return lineNumber;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.Breakpoint#isEnabled()
	 */
	@Override
	public boolean isEnabled() throws CoreException {
		return true;
	}
}
