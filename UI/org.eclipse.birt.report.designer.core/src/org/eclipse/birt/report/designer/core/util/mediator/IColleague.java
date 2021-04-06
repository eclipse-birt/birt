/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
public interface IColleague {

	/**
	 * Perform logic for special request. Called by mediator.
	 * 
	 * @param request
	 */
	void performRequest(ReportRequest request);
}
