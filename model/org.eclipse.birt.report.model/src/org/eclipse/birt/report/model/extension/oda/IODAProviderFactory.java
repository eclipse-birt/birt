/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.extension.oda;

import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Interface for all ODA provider factory.
 * 
 */
public interface IODAProviderFactory {

	public ODAProvider createODAProvider(DesignElement element, String extensionID);

	public IFilterExprDefinition createFilterExprDefinition();

	public IFilterExprDefinition createFilterExprDefinition(String birtFilterExpr);
}
