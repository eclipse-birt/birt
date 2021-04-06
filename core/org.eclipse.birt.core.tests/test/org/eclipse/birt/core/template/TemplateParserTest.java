/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.template;

import java.util.Iterator;

import org.eclipse.birt.core.template.TextTemplate.ExpressionValueNode;
import org.eclipse.birt.core.template.TextTemplate.ImageNode;
import org.eclipse.birt.core.template.TextTemplate.TextNode;
import org.eclipse.birt.core.template.TextTemplate.ValueNode;
import org.junit.Test;

import junit.framework.TestCase;

public class TemplateParserTest extends TestCase {
	@Test
	public void testValueOf() {
		String input = "<value-of>script</value-of>";
		String golden = "<value-of>script</value-of>";
		TextTemplate template = new TemplateParser().parse(input);
		assertEQ(golden, template);
	}

	@Test
	public void testViewTimeValueOf() {
		String input = "<viewtime-value-of>script</viewtime-value-of>";
		String golden = "<viewtime-value-of>script</viewtime-value-of>";
		TextTemplate template = new TemplateParser().parse(input);
		assertEQ(golden, template);
	}

	@Test
	public void testImage() {
		String input = "<image>script</image>";
		String golden = "<image>script</image>";
		TextTemplate template = new TemplateParser().parse(input);
		assertEQ(golden, template);
	}

	@Test
	public void testImageTag() {
		String input = "<image name=\"ABC\"/>";
		String golden = "<image name=\"ABC\"></image>";
		TextTemplate template = new TemplateParser().parse(input);
		assertEQ(golden, template);
	}

	@Test
	public void testText() {
		String input = "text any text";
		String golden = "<text>text any text</text>";
		TextTemplate template = new TemplateParser().parse(input);
		assertEQ(golden, template);
	}

	protected void assertEQ(String golden, TextTemplate template) {
		StringBuffer buffer = new StringBuffer();
		TextTemplateWriter.write(template, buffer);
		assertEquals(golden, buffer.toString());
	}

	static protected class TextTemplateWriter implements TextTemplate.Visitor {

		static void write(TextTemplate template, StringBuffer buffer) {
			TextTemplate.Visitor visitor = new TextTemplateWriter();
			Iterator iter = template.getNodes().iterator();
			while (iter.hasNext()) {
				TextTemplate.Node node = (TextTemplate.Node) iter.next();
				node.accept(visitor, buffer);
			}
		}

		public Object visitText(TextNode node, Object value) {
			StringBuffer buffer = (StringBuffer) value;
			buffer.append("<text>");
			buffer.append(node.getContent());
			buffer.append("</text>");
			return buffer;
		}

		public Object visitValue(ValueNode node, Object value) {
			StringBuffer buffer = (StringBuffer) value;
			buffer.append("<value-of");
			if (node.getFormat() != null) {
				buffer.append("format='");
				buffer.append(node.getFormat());
				buffer.append("'");
			}
			buffer.append(">");
			buffer.append(node.getValue());
			buffer.append("</value-of>");
			return buffer;
		}

		public Object visitImage(ImageNode image, Object value) {
			StringBuffer buffer = (StringBuffer) value;
			buffer.append("<image");
			String name = image.getImageName();
			if (name != null) {
				buffer.append(" name=\"");
				buffer.append(name);
				buffer.append("\"");
			}
			buffer.append(">");
			String expr = image.getExpr();
			if (expr != null) {
				buffer.append(image.getExpr());
			}
			buffer.append("</image>");
			return buffer;
		}

		public Object visitExpressionValue(ExpressionValueNode node, Object value) {
			StringBuffer buffer = (StringBuffer) value;
			buffer.append("<viewtime-value-of");
			if (node.getFormat() != null) {
				buffer.append("format='");
				buffer.append(node.getFormat());
				buffer.append("'");
			}
			buffer.append(">");
			buffer.append(node.getValue());
			buffer.append("</viewtime-value-of>");
			return buffer;
		}
	}

}
