/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;

public class ResourceLocator implements IResourceLocator {

	File rootDir;

	public ResourceLocator(File rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public URL findResource(ModuleHandle moduleHandle, String fileName, int type) {
		File result = new File(fileName);
		if (!result.exists()) {
			result = new File(rootDir + File.separator + fileName);
		}
		if (result.exists()) {
			try {
				return result.toURI().toURL();
			} catch (MalformedURLException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public URL findResource(ModuleHandle moduleHandle, String fileName, int type,
			@SuppressWarnings("rawtypes") Map appContext) {
		return findResource(moduleHandle, fileName, type);
	}

}
