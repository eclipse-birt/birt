/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.parser;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTPropertyManagerFactory;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GraphicMasterPageDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.PageSequenceDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.RuleDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * visitor used to write the IR.
 * 
 */
public class ReportDesignWriter {

	public void write(OutputStream out, Report report) throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

		new ReportDumpVisitor(document).createDocument(report);

		Transformer tr = TransformerFactory.newInstance().newTransformer();
		tr.setOutputProperty(OutputKeys.STANDALONE, "yes");
		tr.setOutputProperty(OutputKeys.INDENT, "yes");
		tr.setOutputProperty(OutputKeys.METHOD, "xml");
		tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

		tr.transform(new DOMSource(document), new StreamResult(out));
	}

	private class ReportDumpVisitor extends DefaultReportItemVisitorImpl {

		Document document;
		Element element;

		/**
		 * constructor.
		 * 
		 * @param writer
		 */
		ReportDumpVisitor(Document document) {
			this.document = document;
		}

		/**
		 * report contains
		 * 
		 * @param report
		 */
		public void createDocument(Report report) {
			pushTag("report"); //$NON-NLS-1$

			attribute(report);

			popTag();
		}

		protected void attribute(String name, String value) {
			if (value != null && !"".equals(value)) //$NON-NLS-1$
			{
				String defaultValue = getDefaultAttrValue(element.getTagName(), name);
				if (!value.equals(defaultValue)) {
					element.setAttribute(name, value);
				}
			}
		}

		protected Stack<Element> elements = new Stack<Element>();

		protected void pushTag(String tag) {
			elements.push(element);
			Element child = document.createElement(tag);
			if (element != null) {
				element.appendChild(child);
			} else {
				document.appendChild(child);
			}
			element = child;
		}

		protected void popTag() {
			element = (Element) elements.pop();
		}

		private void outputMap(String name, Map<?, ?> map) {
			if (map.isEmpty()) {
				return;
			}
			pushTag(name);
			ArrayList<String> keys = new ArrayList<String>(map.size());
			keys.addAll((Collection<String>) map.keySet());
			Collections.sort(keys);
			for (String key : keys) {
				Object value = map.get(key);
				pushTag("entry");
				attribute("name", key.toString());
				if (value != null) {
					if (isPrimitiveType(value)) {
						attribute("value", value.toString());
					} else {
						String childName = toElementName(value.getClass());
						output(childName == null ? "value" : childName, value);
					}
				}
				popTag();
			}
			popTag();
		}

		private void outputCollection(String name, Collection<?> values) {
			if (values.isEmpty()) {
				return;
			}
			pushTag(name);
			for (Object v : values) {
				if (v != null) {
					String childName = toElementName(v.getClass());
					if (childName == null) {
						childName = "entry";
					}
					output(childName, v);

				}
			}
			popTag();
		}

		private void outputExpression(String name, Expression expr) {
			pushTag(name);
			switch (expr.getType()) {
			case Expression.SCRIPT:
				Expression.Script script = (Expression.Script) expr;
				attribute("expr", script.getScriptText());
				if (!"<inline>".equals(script.getFileName())) {
					attribute("file-name", script.getFileName());
				}
				if (!"javascript".equals(script.getLanguage())) {
					attribute("language", script.getLanguage());
				}
				if (1 != script.getLineNumber()) {
					attribute("line-number", Integer.toString(script.getLineNumber()));
				}
				break;

			case Expression.CONSTANT:
				Expression.Constant constant = (Expression.Constant) expr;
				if (-1 != constant.getValueType()) {
					attribute("value-type", Integer.toString(constant.getValueType()));
				}
				attribute("value", constant.getScriptText());
				break;
			}
			popTag();
		}

		private void outputStyle(String name, IStyle style) {
			pushTag(name);
			for (int i = 0; i < IStyle.NUMBER_OF_STYLE; i++) {
				Object v = style.getProperty(i);
				if (v != null) {
					attribute(getStyleName(i), v.toString());
				}
			}
			popTag();
		}

		private void output(String name, Object v) {
			if (v instanceof Collection<?>) {
				outputCollection(name, (Collection<?>) v);
			} else if (v instanceof Map<?, ?>) {
				outputMap(name, (Map<?, ?>) v);
			} else if (v instanceof Expression) {
				outputExpression(name, (Expression) v);
			} else if (v instanceof IStyle) {
				outputStyle(name, (IStyle) v);
			} else {
				pushTag(name);
				attribute(v);
				popTag();
			}
		}

		void attribute(Object object) {
			Method[] methods = object.getClass().getMethods();
			methods = sortMethods(methods);
			for (Method method : methods) {
				String name = method.getName();
				Class<?>[] params = method.getParameterTypes();
				Class<?> returnType = method.getReturnType();
				int modifier = method.getModifiers();
				if (Modifier.isPublic(modifier) && params.length == 0 && returnType != null && isGetMethod(name)
						&& !ignoreMethod(object.getClass(), name)) {
					// we only output the fields defined as a simple java class
					// or org.eclipse.birt.report.engine.ir package etc.

					try {
						Object v = method.invoke(object, new Object[] {});
						if (v != null) {
							String attrName = toAttrName(name);
							if (!canOutput(v)) {
								continue;
							}
							if (isPrimitiveType(v)) {
								attribute(attrName, v.toString());
							} else {
								output(attrName, v);
							}
						}
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	private boolean isGetMethod(String name) {
		if (name.startsWith("get")) {
			return true;
		}

		if (name.startsWith("is")) {
			return true;
		}

		if (name.startsWith("need")) {
			return true;
		}

		if (name.startsWith("has") && !name.startsWith("hash")) {
			return true;
		}
		return false;
	}

	private String toAttrName(String name) {
		if (name.startsWith("get")) {
			name = name.substring(3);
		}
		boolean breakWord = false;
		StringBuilder sb = new StringBuilder(name.length());
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if (Character.isUpperCase(ch)) {
				if (breakWord) {
					sb.append("-");
					breakWord = false;
				}
				sb.append(Character.toLowerCase(ch));
			} else {
				breakWord = true;
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	static HashMap<Class<?>, String> ELEMENT_NAMES = new HashMap<Class<?>, String>();
	static {
		ELEMENT_NAMES.put(PageSequenceDesign.class, "page-sequence");
		ELEMENT_NAMES.put(ColumnDesign.class, "column");
		ELEMENT_NAMES.put(GraphicMasterPageDesign.class, "graphic-master-page");
		ELEMENT_NAMES.put(SimpleMasterPageDesign.class, "simple-master-page");
		ELEMENT_NAMES.put(AutoTextItemDesign.class, "auto-text");
		ELEMENT_NAMES.put(BandDesign.class, "band");
		ELEMENT_NAMES.put(CellDesign.class, "cell");
		ELEMENT_NAMES.put(DataItemDesign.class, "data");
		ELEMENT_NAMES.put(DynamicTextItemDesign.class, "text-data");
		ELEMENT_NAMES.put(ExtendedItemDesign.class, "extended");
		ELEMENT_NAMES.put(FreeFormItemDesign.class, "free-form");
		ELEMENT_NAMES.put(GridItemDesign.class, "grid");
		ELEMENT_NAMES.put(GroupDesign.class, "group");
		ELEMENT_NAMES.put(ImageItemDesign.class, "image");
		ELEMENT_NAMES.put(LabelItemDesign.class, "label");
		ELEMENT_NAMES.put(ListItemDesign.class, "list");
		ELEMENT_NAMES.put(TableItemDesign.class, "table");
		ELEMENT_NAMES.put(RowDesign.class, "row");
		ELEMENT_NAMES.put(TemplateDesign.class, "template");
		ELEMENT_NAMES.put(TextItemDesign.class, "text");
		ELEMENT_NAMES.put(RuleDesign.class, "rule");
		ELEMENT_NAMES.put(Expression.class, "expr");
		ELEMENT_NAMES.put(IStyle.class, "style");
	}

	private String toElementName(Class<?> t) {

		String name = getElementName(t);
		if (name != null) {
			return name;
		}

		Class<?>[] interfaces = t.getInterfaces();
		if (interfaces != null) {
			for (Class<?> itf : interfaces) {
				name = getElementName(itf);
				if (name != null) {
					return name;
				}
			}
		}
		Class<?> parent = t.getSuperclass();
		while (parent != null) {
			name = getElementName(parent);
			if (name != null) {
				return name;
			}
			parent = parent.getSuperclass();
		}
		return null;
	}

	private String getElementName(Class<?> t) {
		return ELEMENT_NAMES.get(t);
	}

	static HashMap<Class<?>, String[]> IGNORE_METHODS = new HashMap<Class<?>, String[]>();
	static {
		IGNORE_METHODS.put(Report.class, new String[] { "getContentCount" });
		IGNORE_METHODS.put(PageSetupDesign.class, new String[] { "getMasterPageCount", "getPageSequenceCount" });
		IGNORE_METHODS.put(GraphicMasterPageDesign.class, new String[] { "getColumnCount" });
		IGNORE_METHODS.put(SimpleMasterPageDesign.class, new String[] { "getFooterCount", "getHeaderCount", });
		IGNORE_METHODS.put(BandDesign.class, new String[] { "getContentCount", "getGroup" });
		IGNORE_METHODS.put(TableBandDesign.class, new String[] { "getRowCount" });
		IGNORE_METHODS.put(CellDesign.class, new String[] { "getContentCount", "getColumn" });
		IGNORE_METHODS.put(FreeFormItemDesign.class, new String[] { "getItemCount" });
		IGNORE_METHODS.put(GridItemDesign.class, new String[] { "getColumnCount", "getRowCount" });
		IGNORE_METHODS.put(ListingDesign.class, new String[] { "getGroupCount" });
		IGNORE_METHODS.put(TableItemDesign.class, new String[] { "getColumnCount" });
		IGNORE_METHODS.put(RowDesign.class, new String[] { "getCellCount" });
		IGNORE_METHODS.put(MapDesign.class, new String[] { "getRuleCount" });
		IGNORE_METHODS.put(HighlightDesign.class, new String[] { "getRuleCount" });
		IGNORE_METHODS.put(VisibilityDesign.class, new String[] { "getRuleCount" });
	}

	boolean ignoreMethod(Class<?> t, String method) {
		if (isIgnore(t, method)) {
			return true;
		}
		// test if all the interfaces contains the ignore method
		Class<?>[] inters = t.getInterfaces();
		if (inters != null) {
			for (Class<?> inter : inters) {
				if (isIgnore(inter, method)) {
					return true;
				}
			}
		}
		// test if any of the parent contains the ignore method
		Class<?> p = t.getSuperclass();
		while (p != null) {
			if (isIgnore(p, method)) {
				return true;
			}
			p = p.getSuperclass();
		}
		return false;
	}

	private boolean isIgnore(Class<?> t, String method) {
		String[] ignoreMethods = IGNORE_METHODS.get(t);
		if (ignoreMethods != null) {
			for (String ignoreMethod : ignoreMethods) {
				if (ignoreMethod.equals(method)) {
					return true;
				}
			}
		}
		return false;
	}

	static HashMap<String, String> DEFAULT_VALUES = new HashMap<String, String>();
	{
		DEFAULT_VALUES.put("simple-master-page.is-floating-footer", "false");
		DEFAULT_VALUES.put("simple-master-page.is-show-footer-on-last", "true");
		DEFAULT_VALUES.put("simple-master-page.is-show-header-on-first", "true");
		DEFAULT_VALUES.put("simple-master-page.orientation", "auto");

		DEFAULT_VALUES.put("table.is-repeat-header", "false");
		DEFAULT_VALUES.put("table.page-break-interval", "-1");
		DEFAULT_VALUES.put("group.page-break-after", "auto");
		DEFAULT_VALUES.put("group.page-break-before", "auto");
		DEFAULT_VALUES.put("group.page-break-inside", "auto");
		DEFAULT_VALUES.put("column.has-data-items-in-detail", "true");
		DEFAULT_VALUES.put("column.is-column-header", "false");
		DEFAULT_VALUES.put("column.suppress-duplicate", "false");
		DEFAULT_VALUES.put("row.is-start-of-group", "false");
		DEFAULT_VALUES.put("row.repeatable", "true");
		DEFAULT_VALUES.put("cell.antidiagonal-number", "0");
		DEFAULT_VALUES.put("cell.col-span", "1");
		DEFAULT_VALUES.put("cell.diagonal-number", "0");
		DEFAULT_VALUES.put("cell.display-group-icon", "false");
		DEFAULT_VALUES.put("cell.has-diagonal-line", "false");
		DEFAULT_VALUES.put("cell.row-span", "1");
		DEFAULT_VALUES.put("cell.drop", "none");
		DEFAULT_VALUES.put("data.suppress-duplicate", "false");
		DEFAULT_VALUES.put("text.has-expression", "false");
	}

	private String getDefaultAttrValue(String element, String method) {
		return DEFAULT_VALUES.get(element + "." + method);
	}

	private boolean isPrimitiveType(Object value) {
		return isJavaPrimitiveType(value) || isBirtPrimitiveType(value);
	}

	private boolean isJavaPrimitiveType(Object value) {
		Class<?> returnType = value.getClass();
		if (returnType == Byte.TYPE || returnType == Character.TYPE || returnType == Short.TYPE
				|| returnType == Integer.TYPE || returnType == Long.TYPE || returnType == Float.TYPE
				|| returnType == Double.TYPE || returnType == String.class || returnType == Boolean.TYPE
				|| returnType == Short.class || returnType == Character.class || returnType == Byte.class
				|| returnType == Integer.class || returnType == Long.class || returnType == Float.class
				|| returnType == Double.class || returnType == Boolean.class)
			return true;

		return false;
	}

	private boolean isBirtPrimitiveType(Object v) {
		if (v instanceof DimensionType) {
			return true;
		}
		return false;
	}

	private boolean canOutput(Object value) {
		if (value == null) {
			return true;
		}
		if (isJavaPrimitiveType(value)) {
			return true;
		}
		if (isBirtPrimitiveType(value)) {
			return true;
		}
		Class<?> returnType = value.getClass();
		if (returnType.getName().startsWith("org.eclipse.birt.report.engine.ir")) {
			return true;
		}
		if (value instanceof IStyle) {
			return true;
		}
		if (value instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) value;
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				if (!canOutput(entry.getKey()) || !canOutput(entry.getValue())) {
					return false;
				}
			}
			return true;
		}
		if (value instanceof Collection<?>) {
			Collection<?> c = (Collection<?>) value;
			if (c.isEmpty()) {
				return false;
			}
			for (Object v : c) {
				if (!canOutput(v)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	static BIRTPropertyManagerFactory STYLE_FACTORY = new BIRTPropertyManagerFactory();

	private String getStyleName(int index) {
		return STYLE_FACTORY.getPropertyName(index);
	}

	private Method[] sortMethods(Method[] methods) {
		ArrayList<Method> list = new ArrayList<Method>(methods.length);
		Collections.addAll(list, methods);
		Collections.sort(list, new Comparator<Method>() {

			public int compare(Method o1, Method o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return list.toArray(new Method[methods.length]);
	}
}
