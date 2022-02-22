/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.core.util.mediator;

import java.util.List;

/**
 * Record the report mediatorthe state
 *
 * @deprecated Not used anymore, see
 *             {@link org.eclipse.birt.report.designer.core.mediator.IMediatorState}
 *             instead.
 */
@Deprecated
public interface IMediatorState {

	/**
	 * Gets the selection objects
	 *
	 * @return
	 */
	List getSelectionObject();

	/**
	 * Gets the source, the return value may be a interface in the future
	 *
	 * @return
	 */
	Object getSource();
}
