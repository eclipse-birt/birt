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

package org.eclipse.birt.report.model.api.elements;

/**
 * Defines the method context that are available to the report item.
 * 
 */

public interface IReportItemMethodContext {

	/**
	 * Name of the on-create context. It is for a script executed when the element
	 * is created in the Factory. Called after the item is created, but before the
	 * item is saved to the report document file.
	 */

	String ON_CREATE_CONTEXT = "onCreate"; //$NON-NLS-1$

	/**
	 * Name of the on-render context. It is for a script Executed when the element
	 * is prepared for rendering in the Presentation engine.
	 */

	String ON_RENDER_CONTEXT = "onRender"; //$NON-NLS-1$

	/**
	 * Name of the on-prepare context. It is for a script startup phase. No data
	 * binding yet. The design of an element can be changed here.
	 */

	String ON_PREPARE_CONTEXT = "onPrepare"; //$NON-NLS-1$
}
