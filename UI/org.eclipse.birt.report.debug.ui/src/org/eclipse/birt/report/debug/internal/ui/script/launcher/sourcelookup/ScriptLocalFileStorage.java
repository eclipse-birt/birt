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

package org.eclipse.birt.report.debug.internal.ui.script.launcher.sourcelookup;

import java.io.File;

import org.eclipse.debug.core.sourcelookup.containers.LocalFileStorage;

/**
 * ScriptLocalFileStorage
 */
public class ScriptLocalFileStorage extends LocalFileStorage {

	private String modelIdentifier;

	/**
	 * Contructor
	 * 
	 * @param file
	 * @param id
	 */
	public ScriptLocalFileStorage(File file, String id) {
		super(file);
		this.modelIdentifier = id;
	}

	/**
	 * Gets the model identifier
	 * 
	 * @return
	 */
	public String getModelIdentifier() {
		return modelIdentifier;
	}

}
