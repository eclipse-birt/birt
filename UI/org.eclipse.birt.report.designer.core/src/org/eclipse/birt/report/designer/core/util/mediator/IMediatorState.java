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

import java.util.List;

/**
 * Record the report mediatorthe state
 * 
 * @deprecated Not used anymore, see
 *             {@link org.eclipse.birt.report.designer.core.mediator.IMediatorState}
 *             instead.
 */
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
