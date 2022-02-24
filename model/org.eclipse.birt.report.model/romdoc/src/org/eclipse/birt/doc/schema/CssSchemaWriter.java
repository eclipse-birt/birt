/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.doc.schema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Write css schema
 *
 */
public class CssSchemaWriter implements ISchemaWriter {

	/**
	 * The output stream.
	 */

	protected PrintStream out = null;

	/**
	 * The default output encoding is UTF-8.
	 */

	protected final static String OUTPUT_ENCODING = "UTF-8"; //$NON-NLS-1$

	/**
	 * Constructor
	 *
	 * @param outputFile file name
	 * @throws java.io.IOException if write orror occurs
	 *
	 */
	public CssSchemaWriter(File outputFile) throws java.io.IOException {
		FileOutputStream stream = new FileOutputStream(outputFile);
		out = new PrintStream(stream, false, OUTPUT_ENCODING);
	}

	/**
	 * Constructor.
	 *
	 * @param outputFile the file to write
	 * @param signature  the UTF signature
	 * @throws java.io.IOException if write error occurs
	 */

	public CssSchemaWriter(File outputFile, String signature) throws java.io.IOException {
		FileOutputStream stream = new FileOutputStream(outputFile);
		out = new PrintStream(stream, false, signature);
	}

	/**
	 * Close the write at the completion of the file.
	 */

	@Override
	public void close() {
		out.close();
		out = null;
	}

	/**
	 * start writing schema
	 */
	@Override
	public void startHtml() {
		out.print("<html>"); //$NON-NLS-1$
		printLine();
		out.print("<body>"); //$NON-NLS-1$
		printLine();
		out.print("<h1>BIRT Css full property table");//$NON-NLS-1$
		printLine();
		initTable();
	}

	/**
	 * write table header.
	 *
	 */
	private void initTable() {
		out.print("<table border = 1> ");//$NON-NLS-1$
		printLine();
		out.print("<thead><tr align=center>");//$NON-NLS-1$
		out.print("<th>Name</th>");//$NON-NLS-1$
		out.print("<th>W3C Values</th>");//$NON-NLS-1$
		out.print("<th>Default Value</th>");//$NON-NLS-1$
		out.print("<th>BIRT Choice Values</th>");//$NON-NLS-1$
		out.print("</tr></thead>");//$NON-NLS-1$
		printLine();
	}

	/**
	 * close writing schema
	 */
	@Override
	public void closeHtml() {
		out.print("</table>");//$NON-NLS-1$
		printLine();
		out.print("</body>");//$NON-NLS-1$
		printLine();
		out.print("</html>");//$NON-NLS-1$
		printLine();
	}

	/**
	 * Write table row
	 *
	 * @param name
	 * @param allowedValue
	 * @param defaultValue
	 */
	@Override
	public void writeRow(CssType css) {
		assert css != null;

		String allowedValue = css.getBirtChoiceValues();
		String defaultValue = css.getInitialValues();
		String htmlValue = css.getValues();
		String name = css.getName();

		if (SchemaUtil.isBlank(name)) {
			return;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<tr><td>"); //$NON-NLS-1$
		buffer.append(name);

		appendColumn(buffer, htmlValue);
		appendColumn(buffer, defaultValue);
		appendColumn(buffer, allowedValue);

		out.print(buffer.toString());
		printLine();
	}

	/**
	 * append '
	 * <td>' tag with value.
	 *
	 * @param buffer
	 * @param value
	 */

	private void appendColumn(StringBuffer buffer, String value) {
		if (!SchemaUtil.isBlank(value)) {
			buffer.append("<td>");//$NON-NLS-1$
			buffer.append(value);
		} else {
			buffer.append("<td>&nbsp;");//$NON-NLS-1$
		}
	}

	/**
	 * Prints '\n', and plus the line conter.
	 */

	private void printLine() {
		out.print('\n');
	}

}
