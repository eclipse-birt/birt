/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.swt.widgets.Listener;

public interface IMenuButtonHelper {

	void addClassPathElements(ClassPathElement[] elements, boolean current);

	void setProvider(IMenuButtonProvider provider);

	void setListener(Listener listener);

	void setMenuButton(ClassSelectionButton button);

	void notifyExpressionChangeEvent(String oldExpression, String newExpression);

	void setProperty(String key, Object value);

	Object getPropertyValue(String key);

}
