/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.views.outline;

import org.eclipse.birt.report.designer.util.ScriptedDesignVisitor;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.swt.graphics.Image;

public class ScriptsNode implements IScriptTreeNode {

	private ModuleHandle reportDesignHandle;

	public ScriptsNode(ModuleHandle handle) {
		this.reportDesignHandle = handle;
	}

	public Object[] getChildren() {
		if (this.reportDesignHandle != null) {
			ScriptedDesignVisitor visitor = new ScriptedDesignVisitor();
			return visitor.getScriptNodes(reportDesignHandle).toArray();

		}
		return new Object[0];
	}

	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParent() {
		return reportDesignHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof ScriptsNode) {
			return ((ScriptsNode) arg0).reportDesignHandle == reportDesignHandle;
		}
		return false;
	}

	public int hashCode() {
		int hashCode = 13;
		if (reportDesignHandle != null)
			hashCode += reportDesignHandle.hashCode() * 7;
		return hashCode;
	}
}
