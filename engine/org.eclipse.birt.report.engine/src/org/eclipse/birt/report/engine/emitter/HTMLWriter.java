/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

/**
 * <code>HTMLWriter</code> is a concrete subclass of <code>XMLWriter</code> that
 * outputs the HTML content.
 * 
 */
public class HTMLWriter extends XMLWriter {

	/**
	 * Creates a HTMLWriter using this constructor.
	 */
	public HTMLWriter() {
		// set bImplicitCloseTag here, because IE will treat <div/> as <div>
		// we must use <div></div> as the empty DIV tag.
		bImplicitCloseTag = false;
	}

	/**
	 * Outputs the style.
	 * 
	 * @param name  The style name.
	 * @param value The style values.
	 */
	public void style(String name, String value) {
		assert name != null && name.length() > 0;
		if (value == null || value.length() == 0) {
			return;
		}

		if (!bPairedFlag) {
			print('>');
			bPairedFlag = true;
		}

		if (bIndent) {
			indentCount++;
			println();
			print(indent());
			indentCount--;
		}

		print(name);
		print(" {"); //$NON-NLS-1$
		print(value);
		print('}');
	}

	/**
	 * Outputs java script code.
	 * 
	 * @param code a line of code
	 */
	public void writeCode(String code) {
		if (!super.bPairedFlag) {
			print('>');
			super.bPairedFlag = true;
		}

		if (super.bIndent) {
			println();
			print(super.indent());
		}

		print(code);
	}

	/**
	 * Output the document type.
	 */
	public void outputDoctype() {
		print("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"); //$NON-NLS-1$
	}

	public void comment(String value) {
		if (value == null || value.length() == 0) {
			return;
		}
		if (!bPairedFlag) {
			print('>');
			bPairedFlag = true;
		}

		print("<!--");
		print(HTMLEncodeUtil.encodeCdata(value));
		print("-->");
		bText = true;// bText is useless.
	}

	public void text(String value) {
		text(value, true);
	}

	public void text(String value, boolean whitespace) {
		if (value == null || value.length() == 0) {
			return;
		}
		if (!bPairedFlag) {
			print('>');
			bPairedFlag = true;
		}

		String stringToPrint = HTMLEncodeUtil.encodeText(value, whitespace);
		print(stringToPrint);
		bText = true;
	}

	protected String encodeText(String text) {
		return HTMLEncodeUtil.encodeText(text, false);
	}

	protected String encodeText(String text, boolean whitespace) {
		return HTMLEncodeUtil.encodeText(text, whitespace);
	}
}