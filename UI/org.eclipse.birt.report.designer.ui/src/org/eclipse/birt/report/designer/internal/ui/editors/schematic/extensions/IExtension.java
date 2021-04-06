/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions;

/**
 * add comment here
 * 
 */
public interface IExtension {

	String getExtendsionIdentify();

	public static class Stub implements IExtension {

		public String getExtendsionIdentify() {
			return ""; //$NON-NLS-1$
		}
	}
}