/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.engine;

import org.eclipse.birt.data.engine.api.IDataQueryDefinition;

/**
 * the interface for the user to extend the ability to extends the data
 * processing features. The user can modify the query dynamically. After the
 * processing, the query is executed by the data engine
 */
public interface IDataExtension {

	void prepareQuery(IDataQueryDefinition query);

	void close();
}
