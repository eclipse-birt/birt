/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.util;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Write a PojoQuery instance into a XML
 */
public class PojoQueryWriter {
	public static String write(PojoQuery query) throws OdaException {
		if (query == null) {
			return null;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.newDocument();
			Element root = doc.createElement(Constants.ELEMENT_ROOT);
			doc.appendChild(root);
			if (query.getVersion() != null) {
				root.setAttribute(Constants.ATTR_POJOQUERY_VERSION, query.getVersion());
			}
			if (query.getAppContextKey() != null) {
				root.setAttribute(Constants.ATTR_POJOQUERY_APPCONTEXTKEY, query.getAppContextKey());
			}
			if (query.getDataSetClass() != null) {
				root.setAttribute(Constants.ATTR_POJOQUERY_DATASETCLASS, query.getDataSetClass());
			}
			for (IColumnsMapping cm : query.getColumnsMappings()) {
				root.appendChild(cm.createElement(doc));
			}

			TransformerFactory tff = TransformerFactory.newInstance();
			tff.setAttribute("indent-number", 4); //$NON-NLS-1$
			Transformer tf = tff.newTransformer();
			tf.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			StringWriter sw = new StringWriter();
			StreamResult sr = new StreamResult(sw);
			DOMSource source = new DOMSource(doc);
			tf.transform(source, sr);
			return sw.toString();
		} catch (ParserConfigurationException e) {
			throw new OdaException(e);
		} catch (TransformerConfigurationException e) {
			throw new OdaException(e);
		} catch (TransformerException e) {
			throw new OdaException(e);
		}
	}
}
