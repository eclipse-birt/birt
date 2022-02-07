/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.expressions;

import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;

/**
 * The default adapter class for <code>IContextExpressionProvider</code>. For
 * user who want to implement <code>IContextExpressionProvider</code>, it's
 * recommended to extend from this class.
 * 
 * @since 2.5
 */
public abstract class AbstractContextExpressionProvider implements IContextExpressionProvider {

	public ExpressionFilter getExpressionFilter(String contextName) {
		return null;
	}

	public IExpressionProvider getExpressionProvider(String contextName) {
		return null;
	}

}
