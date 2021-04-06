/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
