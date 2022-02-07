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

import java.io.IOException;

public class TOCReadWriteTest extends TOCTestCase {

	public void testHiddenFormat4() throws IOException {
		ByteArrayRAOutputStream out = new ByteArrayRAOutputStream();
		TOCWriter writer = new TOCWriter(out);

		TOCBuilder builder = new TOCBuilder(writer);
		createTOCTest(builder);
		writer.close();
		out.close();

		ByteArrayRAInputStream in = new ByteArrayRAInputStream(out.toByteArray());
		TOCReader reader = new TOCReader(in, this.getClass().getClassLoader());

		ITreeNode treeNode = reader.readTree();
		String tocTree = toString(treeNode);
		reader.close();
		in.close();

		System.out.println(tocTree);
		assertEquals(GOLDEN_TOC_TEST.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));

	}

	final static String GOLDEN_TOC_TEST = "<entry nodeId=\"/\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <group nodeId=\"__TOC_1\" tocValue=\"\">"
			+ "        <entry nodeId=\"__TOC_1_0\" tocValue=\"\" hiddenFormats=\"html\">"
			+ "            <entry nodeId=\"__TOC_1_0_1\" tocValue=\"\" hiddenFormats=\"pdf\">"
			+ "                <entry nodeId=\"__TOC_1_0_1_0\" tocValue=\"label\"/>" + "            </entry>"
			+ "        </entry>" + "    </group>" + "</entry>";

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group
	 *                      list (hidden in html)
	 *                      	dummy detail 1 (html)
	 *                      		dummy label 1 (html)
	 *                      	dummy detail 2 (pdf)
	 *                      		label 2
	 * </pre>
	 */
	protected void createTOCTest(TOCBuilder builder) {
		// header
		createEntry(builder, null, "report-header");

		// group
		TOCEntry group = startGroupEntry(builder, null, null);
		{
			// dummy list
			TOCEntry list = startDummyEntry(builder, group, "html");
			{
				// dummy detail 1
				TOCEntry detail1 = startDummyEntry(builder, list, "pdf");
				{
					TOCEntry label = startDummyEntry(builder, detail1, "html");
					builder.closeEntry(label);
				}
				builder.closeEntry(detail1);

				// dummy detail 2
				TOCEntry detail2 = builder.startDummyEntry(list, "pdf");
				{
					createEntry(builder, detail2, "label");
				}
				builder.closeEntry(detail2);

			}
			builder.closeEntry(list);
		}
		builder.closeGroupEntry(group);
	}
}
