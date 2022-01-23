/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.engine.script.internal;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.script.IScriptDataSetMetaDataDefinition;
import org.eclipse.birt.report.engine.api.script.IScriptedDataSetMetaData;

/**
 * Wrapper for data engine IScriptDataSetMetaDataDefinition
 */
class ScriptedDataSetMetaData implements IScriptedDataSetMetaData {
	protected static Logger log = Logger.getLogger(ScriptedDataSetMetaData.class.getName());

	private IScriptDataSetMetaDataDefinition dteMetaData;

	ScriptedDataSetMetaData(IScriptDataSetMetaDataDefinition dteMetaData) {
		this.dteMetaData = dteMetaData;
	}

	public void addColumn(String name, Class dataType) {
		try {
			dteMetaData.addColumn(name, dataType);
		} catch (BirtException e) {
			log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

}
