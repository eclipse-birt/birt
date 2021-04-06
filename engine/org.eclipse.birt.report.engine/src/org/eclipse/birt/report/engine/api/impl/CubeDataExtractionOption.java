/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.Map;

import org.eclipse.birt.report.engine.api.DataExtractionOption;

/**
 * 
 *
 */
public class CubeDataExtractionOption extends DataExtractionOption {

	private static final String EXTRACTOR_ID = "ExtractorId";
	private static final String CUBE_NAME = "CubeName";

	/**
	 * 
	 * @param options
	 */
	public CubeDataExtractionOption(Map options) {
		super(options);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public void setCubeName(String name) {
		setOption(CUBE_NAME, name);
	}

	/**
	 * 
	 * @return
	 */
	public String getCubeName() {
		return (String) getOption(CUBE_NAME);
	}

	/**
	 * 
	 * @param extention
	 */
	public void setCubeExtractorId(String extention) {
		setOption(EXTRACTOR_ID, extention);
	}

	/**
	 * 
	 * @return
	 */
	public String getCubeExtractorId() {
		return (String) getOption(EXTRACTOR_ID);
	}

}
