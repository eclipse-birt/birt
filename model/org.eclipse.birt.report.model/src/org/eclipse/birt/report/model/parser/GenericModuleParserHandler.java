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

package org.eclipse.birt.report.model.parser;

import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSessionImpl;
import org.eclipse.birt.report.model.elements.Library;

/**
 * Generic module parser handler, used to parse a design file or a library file.
 * 
 */

public class GenericModuleParserHandler extends GenericModuleParserHandlerImpl {

	GenericModuleParserHandler(DesignSessionImpl theSession, URL systemID, String fileName, ModuleOption options) {
		super(theSession, systemID, fileName, options);
	}

	GenericModuleParserHandler(DesignSessionImpl theSession, URL systemID, String fileName, ModuleOption options,
			Map<String, Library> reloadLibs) {
		super(theSession, systemID, fileName, options, reloadLibs);
	}
}
