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

package org.eclipse.birt.report.model.extension.oda;

import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Interface for all ODA provider factory.
 *
 */
public interface IODAProviderFactory {

	ODAProvider createODAProvider(DesignElement element, String extensionID);

	IFilterExprDefinition createFilterExprDefinition();

	IFilterExprDefinition createFilterExprDefinition(String birtFilterExpr);
}
