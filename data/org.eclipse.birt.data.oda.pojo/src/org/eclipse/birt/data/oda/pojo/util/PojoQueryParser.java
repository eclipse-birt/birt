/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.pojo.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.querymodel.ClassColumnMappings;
import org.eclipse.birt.data.oda.pojo.querymodel.Column;
import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.IMappingSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.OneColumnMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Parse a POJO query into a
 * <code>org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery</code> instance
 */
public class PojoQueryParser {
	private PojoQueryParser() {

	}

	/**
	 * Parse a POJO query into a
	 * <code>org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery</code> instance
	 * 
	 * @param query
	 * @return the parsed
	 *         <code>org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery</code>
	 *         instance
	 * @throws OdaException         if error/exception occurs during parsing
	 * @throws NullPointerException if <code>query</code> is null
	 */
	public static PojoQuery parse(String query) throws OdaException {
		if (query == null) {
			throw new NullPointerException("query is null"); //$NON-NLS-1$
		}
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(query.getBytes("UTF8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
			throw new OdaException(new Exception(Messages.getString("Query.FailedToParse", //$NON-NLS-1$
					query), e));
		}
		try {
			builder = dbf.newDocumentBuilder();
			doc = builder.parse(in);
			Element root = doc.getDocumentElement();
			if (root == null) {
				throw new OdaException(Messages.getString("Query.FailedToParse", //$NON-NLS-1$
						query));
			}
			String version = root.getAttribute(Constants.ATTR_POJOQUERY_VERSION);
			String dataSetClass = root.getAttribute(Constants.ATTR_POJOQUERY_DATASETCLASS);
			String appContextKey = root.getAttribute(Constants.ATTR_POJOQUERY_APPCONTEXTKEY);
			PojoQuery pojoQuery = new PojoQuery(version, dataSetClass, appContextKey);
			NodeList children = root.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				if (child instanceof Element) {
					Element mapping = (Element) child;
					if (Constants.ElEMENT_COLUMNMAPPING.equals(mapping.getTagName())) {
						pojoQuery.addColumnsMapping(createOneColumnMapping(mapping));
					} else if (Constants.ELEMENT_CLASSCOLUMNMAPPINGS.equals(mapping.getTagName())) {
						pojoQuery.addColumnsMapping(createClassColumnsMapping(mapping));
					}
				}
			}
			return pojoQuery;
		} catch (ParserConfigurationException e) {
			throw new OdaException(new Exception(Messages.getString("Query.FailedToParse", //$NON-NLS-1$
					query), e));
		} catch (SAXException e) {
			throw new OdaException(new Exception(Messages.getString("Query.FailedToParse", //$NON-NLS-1$
					query), e));
		} catch (IOException e) {
			throw new OdaException(new Exception(Messages.getString("Query.FailedToParse", //$NON-NLS-1$
					query), e));
		} finally {
			doc = null;
			builder = null;
			dbf = null;
		}
	}

	/**
	 * @param e a <code>ElEMENT_COLUMN_MAPPING</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IColumnsMapping createOneColumnMapping(Element e) throws OdaException {
		assert e != null;
		Column column = createColumn(e);
		IMappingSource source = createMappingSource(e);
		return new OneColumnMapping(source, column);
	}

	/**
	 * @param e a <code>ELEMENT_CLASS_COLUMN_MAPPINGS</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IColumnsMapping createClassColumnsMapping(Element e) throws OdaException {
		assert e != null;
		IMappingSource source = createMappingSource(e);
		ClassColumnMappings ccm = new ClassColumnMappings(source);
		NodeList children = e.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element mapping = (Element) child;
				if (Constants.ElEMENT_COLUMNMAPPING.equals(mapping.getTagName())) {
					ccm.addColumnsMapping(createOneColumnMapping(mapping));
				} else if (Constants.ELEMENT_CLASSCOLUMNMAPPINGS.equals(mapping.getTagName())) {
					ccm.addColumnsMapping(createClassColumnsMapping(mapping));
				}
			}
		}
		return ccm;
	}

	private static Element getFirstSubElementByTag(Element e, String tagName) {
		assert e != null && tagName != null;
		NodeList list = e.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i) instanceof Element) {
				String name = ((Element) list.item(i)).getTagName();
				if (tagName.equals(name)) {
					return (Element) list.item(i);
				}
			}
		}
		return null;
	}

	private static Element getMethodElement(Element e) {
		assert e != null;
		return getFirstSubElementByTag(e, Constants.ELEMENT_METHOD);
	}

	private static Element getFieldElement(Element e) {
		assert e != null;
		return getFirstSubElementByTag(e, Constants.ELEMENT_FIELD);
	}

	/**
	 * @param method a <code>ELEMENT_METHOD</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IMappingSource createMethodSource(Element method) throws OdaException {
		assert method != null;
		String methodName = method.getAttribute(Constants.ATTR_METHOD_NAME);
		if (methodName == null || methodName.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_METHOD_NAME, Constants.ELEMENT_METHOD));
		}
		return new MethodSource(methodName, createParameters(method));
	}

	/**
	 * @param method a <code>ELEMENT_METHOD</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IMethodParameter[] createParameters(Element method) throws OdaException {
		assert method != null;
		List<IMethodParameter> result = new ArrayList<IMethodParameter>();
		NodeList children = method.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child instanceof Element) {
				Element param = (Element) child;
				if (Constants.ELEMENT_CONSTANTPARMETER.equals(param.getTagName())) {
					result.add(createConstantParameter(param));
				} else if (Constants.ELEMENT_VARIABLEPARMETER.equals(param.getTagName())) {
					result.add(createVariableParameter(param));
				}
			}
		}
		return result.toArray(new IMethodParameter[0]);

	}

	/**
	 * @param e a <code>ELEMENT_CONSTANTPARMETER</code> element
	 * @return
	 * @throws OdaException
	 */
	private static ConstantParameter createConstantParameter(Element e) throws OdaException {
		assert e != null;
		String value = e.getAttribute(Constants.ATTR_PARMETER_VALUE);
		if (e.getAttributeNode(Constants.ATTR_PARMETER_VALUE) == null) {
			value = null;
		}
		String type = e.getAttribute(Constants.ATTR_PARAMETER_TYPE);
		if (type == null || type.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_PARAMETER_TYPE, Constants.ELEMENT_CONSTANTPARMETER));
		}
		return new ConstantParameter(value, type);
	}

	/**
	 * @param e a <code>ELEMENT_VARIABLEPARMETER</code> element
	 * @return
	 * @throws OdaException
	 */
	private static VariableParameter createVariableParameter(Element e) throws OdaException {
		assert e != null;
		String name = e.getAttribute(Constants.ATTR_VARIABLEPARMETER_NAME);
		if (name == null || name.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_COLUMN_NAME, Constants.ElEMENT_COLUMNMAPPING));
		}
		String type = e.getAttribute(Constants.ATTR_PARAMETER_TYPE);
		if (type == null || type.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_PARAMETER_TYPE, Constants.ELEMENT_VARIABLEPARMETER));
		}
		VariableParameter vp = new VariableParameter(name, type);
		String value = e.getAttribute(Constants.ATTR_PARMETER_VALUE);
		if (value != null)
			vp.setStringValue(value);
		return vp;
	}

	/**
	 * @param field a <code>ELEMENT_FIELD</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IMappingSource createFieldSource(Element field) throws OdaException {
		assert field != null;
		String fieldName = field.getAttribute(Constants.ATTR_FIELD_NAME);
		if (fieldName == null || fieldName.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_FIELD_NAME, Constants.ELEMENT_FIELD));
		}
		return new FieldSource(fieldName);
	}

	/**
	 * @param e a <code>ELEMENT_CLASS_COLUMN_MAPPINGS</code> element or a
	 *          <code>ELEMENT_CLASS_COLUMN_MAPPINGS</code> element
	 * @return
	 * @throws OdaException
	 */
	private static IMappingSource createMappingSource(Element e) throws OdaException {
		assert e != null;
		Element method = getMethodElement(e);
		if (method != null) {
			return createMethodSource(method);
		} else
		// Not find out "Method" element, then find "Field" element instead
		{
			Element field = getFieldElement(e);
			if (field != null) {
				return createFieldSource(field);
			} else
			// Neither "Method" or "Field" element is found out
			{
				throw new OdaException(Messages.getString("Query.Miss2Elements", //$NON-NLS-1$
						Constants.ELEMENT_METHOD, Constants.ELEMENT_FIELD, e.getTagName()));
			}
		}
	}

	/**
	 * @param e a <code>ElEMENT_COLUMN_MAPPING</code> element
	 * @return
	 * @throws OdaException
	 */
	private static Column createColumn(Element e) throws OdaException {
		assert e != null;
		String name = e.getAttribute(Constants.ATTR_COLUMN_NAME);
		if (name == null || name.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_COLUMN_NAME, Constants.ElEMENT_COLUMNMAPPING));
		}
		String odaType = e.getAttribute(Constants.ATTR_COLUMN_ODADATATYPE);
		if (odaType == null || odaType.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_COLUMN_ODADATATYPE, Constants.ElEMENT_COLUMNMAPPING));
		}
		String index = e.getAttribute(Constants.ATTR_COLUMN_INDEX);
		if (index == null || index.length() == 0) {
			throw new OdaException(Messages.getString("Query.MissAttributeInElement", //$NON-NLS-1$
					Constants.ATTR_COLUMN_INDEX, Constants.ElEMENT_COLUMNMAPPING));
		}
		int i = Integer.parseInt(index.trim());
		return new Column(name, odaType, i);
	}
}
