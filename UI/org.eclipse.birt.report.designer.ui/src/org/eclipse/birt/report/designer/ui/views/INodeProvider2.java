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

package org.eclipse.birt.report.designer.ui.views;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.RetargetAction;

/**
 *
 */
public interface INodeProvider2 extends INodeProvider {

	RetargetAction[] getRetargetActions(Object object);

	Action[] getActions(Object object);

}
