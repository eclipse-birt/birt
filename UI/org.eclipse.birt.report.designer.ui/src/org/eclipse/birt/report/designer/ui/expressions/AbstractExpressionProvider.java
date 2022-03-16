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
import org.eclipse.birt.report.designer.ui.dialogs.Operator;
import org.eclipse.swt.graphics.Image;

/**
 * The adapter class for IExpressionProvider. For expression provider user
 * extension, it's recommended to extend from this class.
 *
 * @since 2.3.2
 */
public abstract class AbstractExpressionProvider implements IExpressionProvider {

	@Override
	public Object[] getCategory() {
		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return null;
	}

	@Override
	public String getDisplayText(Object element) {
		return null;
	}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getInsertText(Object element) {
		return null;
	}

	@Override
	public Operator[] getOperators() {
		return null;
	}

	@Override
	public String getTooltipText(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return false;
	}

}
