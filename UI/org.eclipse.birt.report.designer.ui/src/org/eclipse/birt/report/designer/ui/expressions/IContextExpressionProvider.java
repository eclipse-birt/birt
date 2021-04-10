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

package org.eclipse.birt.report.designer.ui.expressions;

import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;

/**
 * This interface provides context specific expression provider support
 * 
 * @since 2.3.0
 */
public interface IContextExpressionProvider {

	/**
	 * Returns the expression provider according to given context name
	 * 
	 * @param contextName
	 * @return
	 */
	IExpressionProvider getExpressionProvider(String contextName);

	/**
	 * Returns the expression filters which will be applied to the given context
	 * 
	 * @param contextName
	 * @return
	 * 
	 * @since 2.5
	 */
	ExpressionFilter getExpressionFilter(String contextName);
}
