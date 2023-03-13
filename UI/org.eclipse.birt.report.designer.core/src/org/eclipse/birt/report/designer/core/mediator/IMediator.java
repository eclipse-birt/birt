/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.mediator;

/**
 *
 */

public interface IMediator {

	void addColleague(IMediatorColleague colleague);

	void removeColleague(IMediatorColleague colleague);

	void notifyRequest(IMediatorRequest request);

	IMediatorState getState();

	void pushState();

	void popState();

	void restoreState();

	void setStateConverter(IMediatorStateConverter converter);

	IMediatorStateConverter getStateConverter();

	void dispose();
}
