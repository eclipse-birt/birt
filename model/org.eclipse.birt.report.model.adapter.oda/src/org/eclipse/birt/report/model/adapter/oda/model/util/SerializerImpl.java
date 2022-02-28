/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda.model.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.adapter.oda.IConstants;
import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameters;
import org.eclipse.birt.report.model.adapter.oda.model.DesignValues;
import org.eclipse.birt.report.model.adapter.oda.model.DocumentRoot;
import org.eclipse.birt.report.model.adapter.oda.model.ModelFactory;
import org.eclipse.birt.report.model.adapter.oda.model.Serializer;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * SerializerImpl
 */
public class SerializerImpl implements Serializer {

	private static Serializer sz = null;

	/**
	 * Cannot invoke constructor; use instance() instead
	 */
	private SerializerImpl() {

	}

	/**
	 *
	 * @return A singleton instance of the chart serializer
	 */
	public static synchronized final Serializer instance() {
		if (sz == null) {
			sz = new SerializerImpl();
		}
		return sz;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.ISerialization#write(org.eclipse.birt.chart
	 * .model.Chart, java.io.OutputStream)
	 */

	@Override
	public void write(DesignValues cModel, OutputStream os) throws IOException {
		DocumentRoot documentRoot = ModelFactory.eINSTANCE.createDocumentRoot();
		documentRoot.setDesignValues(cModel);

		cModel.setVersion(IConstants.DESINGER_VALUES_VERSION);

		ModelXMLProcessor xmlProcessor = new ModelXMLProcessor();
		Resource resource = xmlProcessor.createResource(URI.createFileURI("test.designValue")); //$NON-NLS-1$
		resource.getContents().add(documentRoot);

		Map options = new HashMap();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$

		resource.save(os, options);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */
	protected DesignValues read(InputStream is) throws IOException {
		ModelXMLProcessor xmlProcessor = new ModelXMLProcessor();
		Resource resource = xmlProcessor.createResource(URI.createFileURI("test.designValue")); //$NON-NLS-1$

		try {
			Map options = new HashMap();
			options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$

			resource.load(is, options);
		} catch (IOException ex) {
			throw ex;
		}
		DocumentRoot docRoot = (DocumentRoot) resource.getContents().get(0);
		return docRoot.getDesignValues();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */

	@Override
	public DesignValues read(String values) throws IOException {
		if (values == null) {
			return null;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(values.getBytes(IConstants.CHAR_ENCODING));

		DesignValues retValues = read(bis);

		bis.close();
		convertDesignParametersToAdapterParameters(retValues);

		return retValues;
	}

	private void convertDesignParametersToAdapterParameters(DesignValues retValues) {
		if (retValues == null) {
			return;
		}

		org.eclipse.datatools.connectivity.oda.design.DataSetParameters designParams = retValues
				.getDataSetParameters1();
		if (designParams == null || designParams.eContents().isEmpty()) {
			return;
		}

		String version = retValues.getVersion();
		float floatVersion = Float.parseFloat(version);
		if (floatVersion > 1.5) {
			return;
		}

		DataSetParameters adapterParams = SchemaConversionUtil.convertToAdapterParameters(designParams);

		retValues.setDataSetParameters(adapterParams);
		retValues.setDataSetParameters1(null);

		retValues.setVersion(IConstants.DESINGER_VALUES_VERSION);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.model.ISerialization#read(java.io.InputStream)
	 */

	@Override
	public String write(DesignValues values) throws IOException {
		if (values == null) {
			return null;
		}

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		write(values, bos);

		String retValue = bos.toString(IConstants.CHAR_ENCODING);
		bos.close();

		return retValue;
	}
}
