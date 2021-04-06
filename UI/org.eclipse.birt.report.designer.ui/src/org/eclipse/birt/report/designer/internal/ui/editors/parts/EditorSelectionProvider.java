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

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

/**
 * Selection provider for editor.
 * <P>
 * WARNING: This interface is only used to delegate the calls from LayoutEditor
 * to GraphicalEditor. Future definition may vary.
 */

public interface EditorSelectionProvider {
	/**
	 * Updates current editor actions.
	 */
	void updateStackActions();
}