/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.core.template.TextTemplate.ExpressionValueNode;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.util.ExpressionUtil;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;

public class TemplateExecutor implements TextTemplate.Visitor {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(TemplateExecutor.class.getName());

	protected StringBuffer buffer;
	protected HashMap<String, Object> values;
	protected ExecutionContext context;
	protected File imageFolder;
	protected HashMap imageCaches = new HashMap();

	public TemplateExecutor(ExecutionContext context) {
		this.context = context;
		String tmpDir = null;
		if (context != null) {
			IReportEngine engine = context.getEngine();
			if (engine != null) {
				EngineConfig config = engine.getConfig();
				if (config != null) {
					tmpDir = config.getTempDir();
				}
			}
		}
		if (tmpDir == null) {
			tmpDir = FileUtil.getJavaTmpDir();
		}
		if (tmpDir == null) {
			tmpDir = ".";
		}
		imageFolder = new File(tmpDir);
	}

	public String execute(TextTemplate template, HashMap<String, Object> values) {
		this.buffer = new StringBuffer();
		this.values = values;

		if (template == null) {
			return "";
		}

		ArrayList nodes = template.getNodes();
		Iterator iter = nodes.iterator();
		while (iter.hasNext()) {
			TextTemplate.Node node = (TextTemplate.Node) iter.next();
			node.accept(this, null);
		}
		return buffer.toString();
	}

	public Object visitNode(TextTemplate.Node node, Object value) {
		return value;
	}

	public Object visitText(TextTemplate.TextNode node, Object value) {
		buffer.append(node.getContent());
		return value;
	}

	public Object visitValue(TextTemplate.ValueNode node, Object value) {
		String expression = node.getValue();
		if (expression != null) {
			expression = expression.trim();
			if ("pageNumber".equals(expression) || "totalPage".equals(expression)) {
				Object result = ExpressionUtil.evaluate(context, Expression.newScript(expression));
				String text = formatValue(node, result);
				buffer.append(text);
			} else {
				Object result = null;
				if (values != null) {
					String keyExpr = node.getValue();
					if (keyExpr != null) {
						keyExpr = keyExpr.trim();
					}
					result = values.get(keyExpr);
				}

				String text = formatValue(node, result);
				buffer.append(text);
			}
		}

		return value;
	}

	public Object visitExpressionValue(ExpressionValueNode node, Object value) {
		String expression = node.getValue();
		if (expression != null) {
			expression = expression.trim();
			if (expression.length() > 0) {
				Object result = ExpressionUtil.evaluate(context, Expression.newScript(expression));
				String text = formatValue(node, result);
				buffer.append(text);
			}
		}
		return value;
	}

	private String formatValue(TextTemplate.ValueNode node, Object value) {
		String text = "";
		String format = node.getFormat();
		String formatExpression = node.getFormatExpression();
		if (format == null && formatExpression != null) {
			String keyExpr = formatExpression.trim();
			Object formatValue = values.get(keyExpr);
			if (formatValue != null) {
				format = formatValue.toString();
			}
		}
		if ("html".equalsIgnoreCase(format)) {
			if (value != null) {
				text = value.toString();
			}
		} else {
			if (value != null) {
				if (value instanceof Number) {
					NumberFormatter fmt = context.getNumberFormatter(format);
					text = fmt.format((Number) value);
				} else if (value instanceof String) {
					StringFormatter fmt = context.getStringFormatter(format);
					text = fmt.format((String) value);

				} else if (value instanceof Date) {
					DateFormatter fmt = context.getDateFormatter(format);
					text = fmt.format((Date) value);
				} else {
					text = value.toString();
				}
			}
			text = encodeHtmlText(text);
		}
		return text;
	}

	protected String encodeHtmlText(String text) {
		return text.replaceAll("<", "&lt;");
	}

	public Object visitImage(TextTemplate.ImageNode node, Object value) {
		String imageName = null;
		String imageExt = null;
		Object imageContent = null;
		if (TextTemplate.ImageNode.IMAGE_TYPE_EXPR == node.getType()) {
			imageContent = values.get(node.getExpr());
		} else {
			imageName = node.getImageName();
			if (context != null) {
				ModuleHandle design = context.getDesign();
				if (design != null) {
					EmbeddedImage image = design.findImage(imageName);
					if (image != null) {
						imageContent = image.getData(design.getModule());
						imageExt = FileUtil.getExtFromFileName(imageName);
					}
				}
			}
		}
		if (imageContent instanceof byte[]) {
			String src = saveToFile(imageName, imageExt, (byte[]) imageContent);
			if (src != null) {
				buffer.append("<img src=\"");
				buffer.append(src);
				buffer.append("\" ");
				Iterator iter = node.getAttributes().entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();

					Object attrName = entry.getKey();
					Object attrValue = entry.getValue();
					if (attrName != null && attrValue != null) {
						buffer.append(attrName.toString());
						buffer.append("=\"");
						buffer.append(attrValue.toString());
						buffer.append("\" ");
					}
				}
				buffer.append(">");
			}
		}
		return value;
	}

	protected String saveToFile(final String name, final String ext, final byte[] content) {
		if (name != null) {
			String file = (String) imageCaches.get(name);
			if (file != null) {
				return file;
			}
		}
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				try {
					File imageFile = File.createTempFile("img", ext, imageFolder);
					OutputStream out = new FileOutputStream(imageFile);
					out.write(content);
					out.close();
					String fileName = imageFile.toURL().toExternalForm();
					imageCaches.put(name, fileName);
					return fileName;
				} catch (IOException ex) {
					logger.log(Level.WARNING, ex.getMessage(), ex);
					context.addException(new EngineException(ex.getLocalizedMessage()));
				}
				return null;
			}
		});
	}
}
