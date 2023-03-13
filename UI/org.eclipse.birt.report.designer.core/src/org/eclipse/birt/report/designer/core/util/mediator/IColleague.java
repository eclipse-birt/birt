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

import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;

/**
 * Provide interface for mediator. All implemented class can be register to
 * ReportMediator.
 *
 * @see org.eclipse.birt.report.designer.core.util.mediator.ReportMediator
 *
 * @deprecated Not used anymore, see {@link IMediatorColleague} instead.
 */
@Deprecated
public interface IColleague {

	/**
	 * Perform logic for special request. Called by mediator.
	 *
	 * @param request
	 */
	void performRequest(ReportRequest request);
}
