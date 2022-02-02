/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.archive.RAInputStream;

public class TOCVersionTest extends TOCTestCase {

	static final String TOC_TREE_V1 = "toc.v1.dat";
	static final String TOC_TREE_V2 = "toc.v2.dat";
	static final String TOC_TREE_V3 = "toc.v3.dat";

	static final String GOLDEN_TOC = "<entry nodeId=\"null\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <entry nodeId=\"__TOC_1\" tocValue=\"list\">"
			+ "        <entry nodeId=\"__TOC_1_0\" tocValue=\"list-header\"/>"
			+ "        <group nodeId=\"__TOC_1_1\" tocValue=\"group\">"
			+ "            <entry nodeId=\"__TOC_1_1_0\" tocValue=\"list-group-header\"/>"
			+ "            <entry nodeId=\"__TOC_1_1_1\" tocValue=\"detail\"/>"
			+ "            <entry nodeId=\"__TOC_1_1_2\" tocValue=\"group-footer\"/>" + "        </group>"
			+ "        <entry nodeId=\"__TOC_1_2\" tocValue=\"list-footer\"/>" + "    </entry>"
			+ "    <entry nodeId=\"__TOC_2\" tocValue=\"footer\"/>" + "</entry>";

	static final String GOLDEN_TOC_V3 = "<entry nodeId=\"/\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <entry nodeId=\"__TOC_1\" tocValue=\"list\">"
			+ "        <entry nodeId=\"__TOC_2\" tocValue=\"list-header\"/>"
			+ "        <group nodeId=\"__TOC_3\" tocValue=\"group\">"
			+ "            <entry nodeId=\"__TOC_4\" tocValue=\"list-group-header\"/>"
			+ "            <entry nodeId=\"__TOC_5\" tocValue=\"detail\"/>"
			+ "            <entry nodeId=\"__TOC_6\" tocValue=\"group-footer\"/>" + "        </group>"
			+ "        <entry nodeId=\"__TOC_7\" tocValue=\"list-footer\"/>" + "    </entry>"
			+ "    <entry nodeId=\"__TOC_8\" tocValue=\"footer\"/>" + "</entry>";

	public void testReadV1() throws IOException {
		RAInputStream in = openResource(TOC_TREE_V1);
		TOCReader reader = new TOCReader(in, this.getClass().getClassLoader());
		assertEquals(TOCReader.VERSION_V1, reader.getVersion());
		ITreeNode root = reader.readTree();
		String tocTree = toString(root);
		assertEquals(GOLDEN_TOC.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));
		reader.close();
		in.close();
	}

	public void testReadV2() throws IOException {
		RAInputStream in = openResource(TOC_TREE_V2);
		TOCReader reader = new TOCReader(in, this.getClass().getClassLoader());
		assertEquals(TOCReader.VERSION_V2, reader.getVersion());
		ITreeNode root = reader.readTree();
		String tocTree = toString(root);
		assertEquals(GOLDEN_TOC.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));
		reader.close();
		in.close();
	}

	public void testReadV3() throws IOException {
		RAInputStream in = openResource(TOC_TREE_V3);
		TOCReader reader = new TOCReader(in, this.getClass().getClassLoader());
		assertEquals(TOCReader.VERSION_V3, reader.getVersion());
		ITreeNode root = reader.readTree();
		String tocTree = toString(root);
		assertEquals(GOLDEN_TOC_V3.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));
		reader.close();
		in.close();
	}

	public void testStrComp() {
		compare("__TOC_0", "__TOC_1");
		compare("__TOC_0", "__TOC_9");
		compare("__TOC_0", "__TOC_10");
		compare("__TOC_9", "__TOC_10");
		compare("__TOC_10", "__TOC_11");
		compare("__TOC_3", "__TOC_11");
	}

	void compare(String s, String t) {
		int v = s.compareTo(t);
		if (v > 0) {
			System.out.println(s + " > " + t);
		} else if (v < 0) {
			System.out.println(s + " < " + t);

		} else {
			System.out.println(s + " = " + t);
		}
	}

	RAInputStream openResource(String resource) throws IOException {
		InputStream in = this.getClass().getResourceAsStream(resource);
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		byte[] buffer = new byte[1024];
		int size = in.read(buffer);
		while (size > 0) {
			out.write(buffer, 0, size);
			size = in.read(buffer);
		}
		in.close();
		return new ByteArrayRAInputStream(out.toByteArray());
	}

	protected byte[] writeTOC() throws IOException {
		ByteArrayRAOutputStream out = new ByteArrayRAOutputStream();
		TOCWriter writer = new TOCWriter(out);
		TOCBuilder builder = new TOCBuilder(writer);
		{
			TOCEntry reportHeader = builder.startEntry(null, "report-header", null, -1);
			builder.closeEntry(reportHeader);

			TOCEntry list = builder.startEntry(null, "list", null, -1);

			{
				TOCEntry listHeader = builder.startEntry(list, "list-header", null, -1);
				builder.closeEntry(listHeader);

				TOCEntry group = builder.startGroupEntry(list, "group", null, null, -1);
				{
					TOCEntry group21Header = builder.startEntry(group, "list-group-header", null, -1);
					builder.closeEntry(group21Header);

					TOCEntry detail = builder.startEntry(group, "detail", null, null, -1);
					builder.closeEntry(detail);

					TOCEntry group21Footer = builder.startEntry(group, "group-footer", null, null, -1);
					builder.closeEntry(group21Footer);
				}
				builder.closeGroupEntry(group);

				TOCEntry listFooter = builder.startEntry(list, "list-footer", null, null, -1);
				builder.closeEntry(listFooter);
			}
			builder.closeEntry(list);

			TOCEntry footer = builder.startEntry(null, "footer", null, -1);
			builder.closeEntry(footer);
		}
		writer.close();
		out.close();
		return out.toByteArray();
	}

}
