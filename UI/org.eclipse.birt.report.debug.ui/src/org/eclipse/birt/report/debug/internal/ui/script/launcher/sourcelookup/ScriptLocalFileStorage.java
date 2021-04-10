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
