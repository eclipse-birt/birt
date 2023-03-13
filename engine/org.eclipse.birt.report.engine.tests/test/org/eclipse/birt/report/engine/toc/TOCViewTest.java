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

package org.eclipse.birt.report.engine.toc;

import java.io.IOException;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.TOCNode;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TOCViewTest extends TOCTestCase {

	public void testTocFind() throws Exception {
		ITreeNode tree = createTocNormal();
		TOCView view = new TOCView(tree, null, ULocale.ENGLISH, TimeZone.getTimeZone("GMT+08:00"), "html");

		checkTocNode(view, "/", null, 3);
		checkTocNode(view, "__TOC_0", "report header", 0);
		checkTocNode(view, "__TOC_1", "table", 2);
		checkTocNode(view, "__TOC_1_0", "group 1", 2);
		checkTocNode(view, "__TOC_1_0_0", "detail 1", 0);
		checkTocNode(view, "__TOC_1_0_1", "detail 2", 0);
		checkTocNode(view, "__TOC_1_1", "group 2", 2);
		checkTocNode(view, "__TOC_1_1_0", "detail 3", 0);
		checkTocNode(view, "__TOC_1_1_1", "detail 4", 0);
		checkTocNode(view, "__TOC_2_0", "chart 1", 0);
		checkTocNode(view, "__TOC_2_1", "chart 2", 0);
		assertTrue(view.findTOC("__TOC_0_0") == null);
		assertTrue(view.findTOC("__TOC_1_1_2") == null);
		assertTrue(view.findTOC("__TOC_1_1_1_0") == null);
		assertTrue(view.findTOC("__TOC_3") == null);
	}

	void checkTocNode(TOCView view, String id, String label, int children) {
		TOCNode node = view.findTOC(id);
		assertEquals(id, node.getNodeID());
		assertEquals(label, node.getDisplayString());
		assertEquals(children, node.getChildren().size());
	}

	public void testTocViewNormal() throws EngineException, IOException {
		ITreeNode tree = createTocNormal();
		System.out.println(toString(tree));
		TOCView view = new TOCView(tree, null, ULocale.ENGLISH, TimeZone.getTimeZone("GMT+08:00"), "html");
		String output = toString(view.getRoot());
		System.out.println(output);
		assertEquals(TOC_NORMAL_GOLDEN.replaceAll("\\s", ""), output.replaceAll("\\s", ""));
	}

	static final String TOC_NORMAL_GOLDEN = "<toc nodeId=\"/\">"
			+ "		<toc nodeId=\"__TOC_0\" displayText=\"report header\"/>"
			+ "    	<toc nodeId=\"__TOC_1\" displayText=\"table\">"
			+ "        <toc nodeId=\"__TOC_1_0\" displayText=\"group 1\">"
			+ "            <toc nodeId=\"__TOC_1_0_0\" displayText=\"detail 1\"/>"
			+ "            <toc nodeId=\"__TOC_1_0_1\" displayText=\"detail 2\"/>" + "        </toc>"
			+ "        <toc nodeId=\"__TOC_1_1\" displayText=\"group 2\">"
			+ "            <toc nodeId=\"__TOC_1_1_0\" displayText=\"detail 3\"/>"
			+ "            <toc nodeId=\"__TOC_1_1_1\" displayText=\"detail 4\"/>" + "        </toc>" + "    </toc>"
			+ "		<toc nodeId=\"__TOC_2\" displayText=\"\">"
			+ "			<toc nodeId=\"__TOC_2_0\" displayText=\"chart 1\"/>"
			+ "			<toc nodeId=\"__TOC_2_1\" displayText=\"chart 2\"/>" + "		</toc>" + "		</toc>";

	protected ITreeNode createTocNormal() {
		TOCBuilder builder = new TOCBuilder();
		{
			createEntry(builder, null, "report header");

			// a normal table
			TOCEntry table = startEntry(builder, null, "table");
			{
				TOCEntry group1 = startEntry(builder, table, "group 1");
				{
					createEntry(builder, group1, "detail 1");
					createEntry(builder, group1, "detail 2");
				}
				closeEntry(builder, group1);

				TOCEntry group2 = startEntry(builder, table, "group 2");
				{
					createEntry(builder, group2, "detail 3");
					createEntry(builder, group2, "detail 4");
				}
				closeEntry(builder, group2);
			}

			// dummy container
			TOCEntry dummyGird = startDummyEntry(builder, null, "pdf");
			{
				createEntry(builder, dummyGird, "chart 1");
				createEntry(builder, dummyGird, "chart 2");
			}
			closeEntry(builder, dummyGird);
		}
		return builder.getTOCTree();
	}

	public void testTocViewWithFormat() throws EngineException, IOException {
		ITreeNode tree = createTocWithFormat();
		System.out.println(toString(tree));
		TOCView view = new TOCView(tree, null, ULocale.ENGLISH, TimeZone.getTimeZone("GMT+08:00"), "html");
		String output = toString(view.getRoot());
		System.out.println(output);
		assertEquals(TOC_WITH_FORMAT_GOLDEN.replaceAll("\\s", ""), output.replaceAll("\\s", ""));
	}

	static final String TOC_WITH_FORMAT_GOLDEN = "<toc nodeId=\"/\">"
			+ "    <toc nodeId=\"__TOC_2\" displayText=\"chart 2\"/>" + "</toc>";

	protected ITreeNode createTocWithFormat() {
		TOCBuilder builder = new TOCBuilder();
		{
			// dummy container
			TOCEntry dummyGird = startDummyEntry(builder, null, "html");
			{
				createEntry(builder, dummyGird, "chart 1");
				createEntry(builder, dummyGird, "chart 2");
			}
			closeEntry(builder, dummyGird);

			createEntry(builder, null, "chart 1", "pdf,html");

			createEntry(builder, null, "chart 2", "pdf");
		}
		return builder.getTOCTree();
	}

	public void testTocViewWithEmptyGorup() throws EngineException, IOException {
		ITreeNode tree = createTocWithEmptyGroup();
		System.out.println(toString(tree));
		TOCView view = new TOCView(tree, null, ULocale.ENGLISH, TimeZone.getTimeZone("GMT+08:00"), "html");
		String output = toString(view.getRoot());
		System.out.println(output);
		assertEquals(TOC_WITH_EMPTY_GROUP_GOLDEN.replaceAll("\\s", ""), output.replaceAll("\\s", ""));

	}

	static final String TOC_WITH_EMPTY_GROUP_GOLDEN = "<toc nodeId=\"/\">"
			+ "    <toc nodeId=\"__TOC_0\" displayText=\"report header\"/>"
			+ "    <toc nodeId=\"__TOC_1\" displayText=\"table\">"
			+ "        <toc nodeId=\"__TOC_1_0\" displayText=\"\">"
			+ "            <toc nodeId=\"__TOC_1_0_0\" displayText=\"header\"/>"
			+ "            <toc nodeId=\"__TOC_1_0_1\" displayText=\"\">"
			+ "                <toc nodeId=\"__TOC_1_0_1_0\" displayText=\"detail 1\"/>"
			+ "                <toc nodeId=\"__TOC_1_0_1_1\" displayText=\"detail 2\"/>" + "            </toc>"
			+ "        </toc>" + "    <toc nodeId=\"__TOC_1_1\" displayText=\"\">"
			+ "            <toc nodeId=\"__TOC_1_1_0\" displayText=\"header 2\"/>"
			+ "            <toc nodeId=\"__TOC_1_1_1\" displayText=\"\">"
			+ "                <toc nodeId=\"__TOC_1_1_1_0\" displayText=\"detail 3\"/>"
			+ "                <toc nodeId=\"__TOC_1_1_1_1\" displayText=\"detail 4\"/>" + "            </toc>"
			+ "        </toc>" + "    </toc>" + "</toc>";

	protected ITreeNode createTocWithEmptyGroup() {
		TOCBuilder builder = new TOCBuilder();
		{
			createEntry(builder, null, "report header");

			// a normal table
			TOCEntry table = startEntry(builder, null, "table");
			{
				TOCEntry group1 = startGroupEntry(builder, table, null);
				{
					createEntry(builder, group1, "header");
					TOCEntry group = startGroupEntry(builder, group1, null);
					{
						createEntry(builder, group, "detail 1");
						createEntry(builder, group, "detail 2");
					}
					closeEntry(builder, group);
				}
				closeEntry(builder, group1);

				TOCEntry group2 = startGroupEntry(builder, table, null);
				{
					createEntry(builder, group2, "header 2");
					TOCEntry group = startGroupEntry(builder, group2, null);
					{
						createEntry(builder, group, "detail 3");
						createEntry(builder, group, "detail 4");
					}
					closeEntry(builder, group);
				}
				closeEntry(builder, group2);
			}
		}
		return builder.getTOCTree();
	}

	protected ITreeNode createToc() {
		TOCBuilder builder = new TOCBuilder();
		{
			createEntry(builder, null, "report header");

			// a normal table
			TOCEntry table = startEntry(builder, null, "table");
			{
				TOCEntry group1 = startEntry(builder, table, "gropu 1");
				{
					createEntry(builder, group1, "detail 1");
					createEntry(builder, group1, "detail 2");
				}
				closeEntry(builder, group1);

				TOCEntry group2 = startEntry(builder, table, "group 2");
				{
					createEntry(builder, group2, "detail 3");
					createEntry(builder, group2, "detail 4");
				}
				closeEntry(builder, group2);
			}

			TOCEntry hideTable = startEntry(builder, null, "table", "html");
			{
				createEntry(builder, hideTable, "group 1");
				createEntry(builder, hideTable, "group 2");
			}
			closeEntry(builder, hideTable);

			// empty group
			TOCEntry groupTable = startEntry(builder, null, "group table");
			{
				TOCEntry group1 = startEntry(builder, groupTable, null);
				{
					createEntry(builder, group1, "detail 1");
					createEntry(builder, group1, "detail 2");
				}
				closeEntry(builder, group1);

				TOCEntry group2 = startEntry(builder, groupTable, null);
				{
					createEntry(builder, group2, "detail 3");
					createEntry(builder, group2, "detail 4");
				}
				closeEntry(builder, group2);
			}
			closeEntry(builder, groupTable);

			// dummy container
			TOCEntry dummyTable = startDummyEntry(builder, null, "pdf");
			{
				TOCEntry group1 = startEntry(builder, dummyTable, "group 1");
				{
					createEntry(builder, group1, "detail 1");
					createEntry(builder, group1, "detail 2");
				}
				closeEntry(builder, group1);

				TOCEntry group2 = startEntry(builder, dummyTable, "group 2");
				{
					createEntry(builder, group2, "detail 3");
					createEntry(builder, group2, "detail 4");
				}
				closeEntry(builder, group2);
			}
			closeEntry(builder, dummyTable);
		}
		return builder.getTOCTree();
	}

}
