/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
