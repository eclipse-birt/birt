/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.expressions;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * The adapter class for {@link IExpressionSupport}.
 */
public abstract class AbstractExpressionSupport implements IExpressionSupport {

	@Override
	public IExpressionBuilder createBuilder(Shell shl, Object expression) {
		return null;
	}

	@Override
	public String getDisplayName() {
		return null;
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public IExpressionConverter getConverter() {
		return null;
	}

}
