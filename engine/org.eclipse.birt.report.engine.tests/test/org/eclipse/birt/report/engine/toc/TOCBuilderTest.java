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

public class TOCBuilderTest extends TOCTestCase {

	public void testHiddenFormat1() {
		TOCBuilder builder = new TOCBuilder();
		createTOCTest1(builder);
		String tocTree = toString(builder.getTOCTree());
		System.out.println(tocTree);
		assertEquals(GOLDEN_TOC_TEST_1.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));
	}

	final static String GOLDEN_TOC_TEST_1 = "<entry nodeId=\"/\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <group nodeId=\"__TOC_1\" tocValue=\"\">"
			+ "        <entry nodeId=\"__TOC_1_0\" tocValue=\"\" hiddenFormats=\"html\" >"
			+ "            <entry nodeId=\"__TOC_1_0_0\" tocValue=\"detail1\"/>"
			+ "            <entry nodeId=\"__TOC_1_0_1\" tocValue=\"detail2\"/>" + "        </entry>" + "    </group>"
			+ "</entry>";

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                     the create toc node should be:
	 *                     
	 *                     report-header
	 *                     group
	 *                     		list(dummy, hidden in html)
	 *                     		    detail 1
	 *                     			detail 2
	 * </pre>
	 */
	void createTOCTest1(TOCBuilder builder) {
		// the report header
		TOCEntry reportHeader = builder.startEntry(null, "report-header", null, null, -1);
		builder.closeEntry(reportHeader);

		// the group
		{
			TOCEntry group = builder.startGroupEntry(null, null, null, null, -1);
			{
				// list with hidden format as "html"
				TOCEntry list = builder.startDummyEntry(group, "html");
				{
					// detail one
					TOCEntry detail1 = builder.startEntry(list, "detail1", null, null, -1);
					builder.closeEntry(detail1);
					// detail two
					TOCEntry detail2 = builder.startEntry(list, "detail2", null, null, -1);
					builder.closeEntry(detail2);
				}
				builder.closeEntry(list);
			}
			builder.closeGroupEntry(group);
		}
	}

	public void testHiddenFormat2() {
		TOCBuilder builder = new TOCBuilder();

		createTOCTest2(builder);
		String tocTree = toString(builder.getTOCTree());
		System.out.println(tocTree);
		assertEquals(GOLDEN_TOC_TEST_2.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));

	}

	final static String GOLDEN_TOC_TEST_2 = "<entry nodeId=\"/\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <group nodeId=\"__TOC_1\" tocValue=\"html\">"
			+ "        <entry nodeId=\"__TOC_1_0\" tocValue=\"detail1\"/>"
			+ "        <entry nodeId=\"__TOC_1_1\" tocValue=\"detail2\"/>" + "    </group>" + "</entry>";

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group(hidden in html)
	 *                  		    detail 1
	 *                  			detail 2
	 * </pre>
	 */
	protected void createTOCTest2(TOCBuilder builder) {
		// report header
		TOCEntry reportHeader = builder.startEntry(null, "report-header", null, null, -1);
		builder.closeEntry(reportHeader);

		// group hidden in HTML
		TOCEntry group = builder.startGroupEntry(null, "html", null, null, -1);

		{

			// detail 1
			TOCEntry detail1 = builder.startEntry(group, "detail1", null, null, -1);
			builder.closeEntry(detail1); // close detail

			// detail 2
			TOCEntry detail2 = builder.startEntry(group, "detail2", null, null, -1);
			builder.closeEntry(detail2); // close detai2
		}
		builder.closeGroupEntry(group);
	}

	public void testHiddenFormat3() {
		TOCBuilder builder = new TOCBuilder();
		createTOCTest3(builder);
		String tocTree = toString(builder.getTOCTree());
		System.out.println(tocTree);
		assertEquals(GOLDEN_TOC_TEST_3.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));

	}

	final static String GOLDEN_TOC_TEST_3 = "<entry nodeId=\"/\">"
			+ "    <entry nodeId=\"__TOC_0\" tocValue=\"report-header\"/>"
			+ "    <group nodeId=\"__TOC_1\" tocValue=\"\" hiddenFormats=\"pdf\" >"
			+ "        <entry nodeId=\"__TOC_1_0\" tocValue=\"\" hiddenFormats=\"html\" >"
			+ "            <entry nodeId=\"__TOC_1_0_0\" tocValue=\"detail1\" hiddenFormats=\"html\"/>"
			+ "            <entry nodeId=\"__TOC_1_0_1\" tocValue=\"detail2\"/>" + "        </entry>" + "    </group>"
			+ "</entry>";

	/**
	 * create the toc node. the design contains :
	 * 
	 * <pre>
	 *                    the create toc node should be:
	 *                    
	 *                    report-header
	 *                    group(hidden in pdf)
	 *                    		list(dummy, hidden in html)
	 *                    		    detail 1, hidden in html
	 *                    			detail 2
	 * </pre>
	 */
	protected void createTOCTest3(TOCBuilder builder) {
		TOCEntry reportHeader = builder.startEntry(null, "report-header", null, null, -1);
		builder.closeEntry(reportHeader);

		// group with hidden format as "pdf"
		TOCEntry group = builder.startGroupEntry(null, null, null, "pdf", -1);
		{
			// list with hidden format as "html"
			TOCEntry list = builder.startDummyEntry(group, "html");
			{
				TOCEntry detail1 = builder.startEntry(list, "detail1", null, "html", -1);
				builder.closeEntry(detail1);

				TOCEntry detail2 = builder.startEntry(list, "detail2", null, null, -1);
				builder.closeEntry(detail2);
			}
			builder.closeEntry(list);
		}
		builder.closeGroupEntry(group);
	}

	public void testHiddenFormat4() {
		TOCBuilder builder = new TOCBuilder();
		createTOCTest4(builder);

		String tocTree = toString(builder.getTOCTree());
		System.out.println(tocTree);
		assertEquals(GOLDEN_TOC_TEST_4.replaceAll("\\s", ""), tocTree.replaceAll("\\s", ""));

	}

	final static String GOLDEN_TOC_TEST_4 = "<entry nodeId=\"/\">"
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
	protected void createTOCTest4(TOCBuilder builder) {
		// header
		TOCEntry reportHeader = builder.startEntry(null, "report-header", null, null, -1);
		builder.closeEntry(reportHeader);

		// group
		TOCEntry group = builder.startGroupEntry(null, null, null, null, -1);
		{
			// dummy list
			TOCEntry list = builder.startDummyEntry(group, "html");
			{
				// dummy detail 1
				TOCEntry detail1 = builder.startDummyEntry(list, "html");
				{
					TOCEntry label = builder.startDummyEntry(detail1, "html");
					builder.closeEntry(label);
				}
				builder.closeEntry(detail1);

				// dummy detail 2
				TOCEntry detail2 = builder.startDummyEntry(list, "pdf");
				{
					TOCEntry label = builder.startEntry(detail2, "label", null, -1);
					builder.closeEntry(label);
				}
				builder.closeEntry(detail2);
			}
			builder.closeEntry(list);

		}
		builder.closeGroupEntry(group);
	}

}
