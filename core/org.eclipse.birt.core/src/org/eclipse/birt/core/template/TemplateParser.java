/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.core.template;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TemplateParser {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(TemplateParser.class.getName());

	public TextTemplate parse(String text) {

		if (text == null || text.trim().equals("")) {
			return null;
		}
		try {
			ASTTemplate templateNode = new Parser().parse(new StringReader(text));
			TextTemplate template = new TextTemplate();
			new ASTVisitor(template).visit(templateNode, null);
			return template;
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.getMessage());
		}
		return null;
	}

	protected static class ASTVisitor implements ParserVisitor {

		TextTemplate template;

		ASTVisitor(TextTemplate template) {
			this.template = template;
		}

		@Override
		public Object visit(SimpleNode node, Object data) {
			return data;
		}

		@Override
		public Object visit(ASTAttribute node, Object data) {
			if (data instanceof TextTemplate.ImageNode) {
				TextTemplate.ImageNode image = (TextTemplate.ImageNode) data;
				image.setAttribute(node.getName(), getAttributeValue(node.getValue()));
			} else if (data instanceof TextTemplate.ValueNode) {
				TextTemplate.ValueNode value = (TextTemplate.ValueNode) data;
				if ("format".equalsIgnoreCase(node.getName())) {
					value.format = getAttributeValue(node.getValue());
				} else if ("format-expr".equalsIgnoreCase(node.getName())) {
					value.formatExpression = getAttributeValue(node.getValue());
				}
			} else if (data instanceof TextTemplate.TextNode) {
				StringBuilder attribute = new StringBuilder();
				attribute.append(" " + node.getName() + ":" + node.getValue() + " ");
				return attribute.toString();
			}
			return data;
		}

		@Override
		public Object visit(ASTValueOf node, Object data) {
			TextTemplate.ValueNode value = new TextTemplate.ValueNode();
			node.childrenAccept(this, value);
			template.nodes.add(value);
			return data;
		}

		@Override
		public Object visit(ASTViewTimeValueOf node, Object data) {
			TextTemplate.ExpressionValueNode value = new TextTemplate.ExpressionValueNode();
			node.childrenAccept(this, value);
			template.nodes.add(value);
			return data;
		}

		@Override
		public Object visit(ASTImage node, Object data) {
			TextTemplate.ImageNode image = new TextTemplate.ImageNode();
			node.childrenAccept(this, image);
			template.nodes.add(image);
			return data;
		}

		@Override
		public Object visit(ASTText node, Object data) {
			TextTemplate.TextNode text = new TextTemplate.TextNode();
			text.content = node.getContent();
			template.nodes.add(text);
			return data;
		}

		@Override
		public Object visit(ASTTemplate node, Object data) {
			return node.childrenAccept(this, data);
		}

		@Override
		public Object visit(ASTEbody_content node, Object data) {
			if (data instanceof TextTemplate.ImageNode) {
				TextTemplate.ImageNode image = (TextTemplate.ImageNode) data;
				String expr = getText(node);
				if (expr != null && expr.length() > 0) {
					image.setExpr(expr);
				}
			} else if (data instanceof TextTemplate.ValueNode) {
				TextTemplate.ValueNode value = (TextTemplate.ValueNode) data;
				value.value = getText(node);
			} else {
				return getText(node);
			}
			return data;
		}

		protected String getText(ASTEbody_content node) {
			if (node == null || node.children == null) {
				return null;
			}
			Object obj;
			StringBuilder text = new StringBuilder();
			for (int n = 0; n < node.children.length; n++) {
				obj = node.children[n];
				if (obj instanceof ASTText) {
					text.append(((ASTText) obj).getContent());
				}

			}
			return text.toString();
		}

		private String getAttributeValue(String value) {
			if (value != null) {
				int length = value.length();
				if (length > 2) {
					if (value.charAt(0) == '"' || value.charAt(0) == '\'') {
						// remove the first and last quote
						value = value.substring(1, length - 1);
					}
				}
			}
			return value;
		}
	}

}
