/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.template;

import java.util.ArrayList;
import java.util.HashMap;

public class TextTemplate {

	ArrayList nodes = new ArrayList();

	public ArrayList getNodes() {
		return nodes;
	}

	public static interface Visitor {

		Object visitText(TextNode node, Object value);

		Object visitValue(ValueNode node, Object value);

		Object visitImage(ImageNode image, Object value);

		Object visitExpressionValue(ExpressionValueNode node, Object value);
	}

	abstract public static class Node {

		public abstract void accept(Visitor visitor, Object value);
	}

	public static class TextNode extends Node {

		String content;

		public String getContent() {
			return content;
		}

		public void accept(Visitor visitor, Object value) {
			visitor.visitText(this, value);
		}
	}

	public static class ImageNode extends Node {

		public static final String IMAGE_TYPE_EXPR = "expr";
		public static final String IMAGE_TYPE_EMBEDDED = "embedded";

		private HashMap attributes = new HashMap();
		private String imageType = IMAGE_TYPE_EMBEDDED;
		private String imageName;
		private String expression;

		public HashMap getAttributes() {
			return attributes;
		}

		public void setAttribute(String name, String value) {
			if ("type".equalsIgnoreCase(name)) {
				if (IMAGE_TYPE_EXPR.equalsIgnoreCase(value)) {
					imageType = IMAGE_TYPE_EXPR;
				} else {
					imageType = IMAGE_TYPE_EMBEDDED;
				}
			} else if ("name".equalsIgnoreCase(name)) {
				imageType = IMAGE_TYPE_EMBEDDED;
				imageName = value;
			} else {
				attributes.put(name, value);
			}
		}

		public String getExpr() {
			if (IMAGE_TYPE_EXPR == getType()) {
				return expression;
			}
			return null;
		}

		public void setExpr(String expr) {
			imageType = IMAGE_TYPE_EXPR;
			expression = expr;
		}

		public String getType() {
			return imageType;
		}

		public String getImageName() {
			if (IMAGE_TYPE_EMBEDDED == imageType) {
				return imageName;
			}
			return null;
		}

		public void accept(Visitor visitor, Object value) {
			visitor.visitImage(this, value);
		}
	}

	public static class ValueNode extends Node {

		String format;
		String value;
		String formatExpression;

		public String getFormat() {
			return format;
		}

		public String getFormatExpression() {
			return formatExpression;
		}

		public String getValue() {
			return value;
		}

		public void accept(Visitor visitor, Object value) {
			visitor.visitValue(this, value);
		}
	}

	public static class ExpressionValueNode extends ValueNode {

		public void accept(Visitor visitor, Object value) {
			visitor.visitExpressionValue(this, value);
		}
	}
}
