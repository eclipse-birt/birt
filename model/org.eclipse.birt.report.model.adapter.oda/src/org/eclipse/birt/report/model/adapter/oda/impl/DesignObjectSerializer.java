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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.adapter.oda.IConstants;
import org.eclipse.datatools.connectivity.oda.design.util.DesignResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * The utility class to serialize EMF objects to string.
 * 
 */

public class DesignObjectSerializer {

	/**
	 * Constructs a string representation of this EMF object.
	 * 
	 * @param eObject the EMF object
	 * @return a string
	 */

	public static String toExternalForm(EObject eObject) {
		if (eObject == null)
			return null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		// Create and setup local ResourceSet

		ResourceSet rsOdaValues = new ResourceSetImpl();
		rsOdaValues.getResourceFactoryRegistry().getExtensionToFactoryMap().put("designValue", //$NON-NLS-1$
				new DesignResourceFactoryImpl());

		// Create resources to represent the disk files to be used to store the
		// models
		Resource rOdaValue = rsOdaValues.createResource(URI.createFileURI("test.designValue")); //$NON-NLS-1$

		// Add the EMF values to the resource

		rOdaValue.getContents().add(EcoreUtil.copy(eObject));

		Map options = new HashMap();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$

		// Save the resource to disk
		try {
			rOdaValue.save(bos, options);
		} catch (IOException e) {
			return IConstants.EMPTY_STRING;
		}

		String retValue = IConstants.EMPTY_STRING;
		try {
			retValue = bos.toString(IConstants.CHAR_ENCODING);
		} catch (UnsupportedEncodingException e) {
			return IConstants.EMPTY_STRING;
		} finally {
			try {
				bos.close();
				bos = null;
			} catch (IOException e) {
				bos = null;
			}
		}

		return retValue;
	}
}
