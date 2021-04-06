/*
 *************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
