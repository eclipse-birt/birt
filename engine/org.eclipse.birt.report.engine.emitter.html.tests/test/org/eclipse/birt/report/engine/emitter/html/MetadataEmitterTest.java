/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;

public class MetadataEmitterTest extends HTMLReportEmitterTestCase {
	static String noMetadata1 = "<div([^<>]* id=\"";
	static String noMetadata2 = "\"[^<>]*)>[^<>]*</div>";
	static String metadata1 = "<div([^<>]* id=\"";
	static String metadata2 = "\"[^<>]*)>[^<>]*";

	static String template1 = metadata1;
	static String template2 = "\"[^<>]*)>";
	static String table1 = "<table( [^<>]*id=\"";
	static String table2 = "\"[^<>]*)>";
	static String list1 = "<div([^<>]* id=\"";
	static String list2 = "\"[^<>]*)>";
	static String imageMetadata1 = "<img([^<>]* id=\"";
	static String imageMetadata2 = "\"[^<>]*)>[^<>]*";
	static String imageNoMetadata1 = "<div>[^<>]*<img([^<>]* id=\"";
	static String imageNoMetadata2 = "\"[^<>]*)>[^<>]*</div>";
	static String chartMetadata1 = "<embed([^<>]* id=\"";
	static String chartMetadata2 = "\"[^<>]*)>[^<>]*";

	@Override
	public String getWorkSpace() {
		return "./metadataTest";
	}

	/**
	 * Tests active ids output by HtmlReportEmitter. <br>
	 * <br>
	 * Ids of following items should be output:
	 * <li>label
	 * <li>table
	 * <li>list
	 * <li>chart
	 * <li>all template items
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testAllItems() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/emitter/html/ActiveId_Test.xml";
		String[][] bookmarksWithMetadata = { { "label", "LABEL" }, { "table", "TABLE" }, { "chart", "Chart" },
				{ "list", "LIST" }, { "HTML text", "TEXT" }, { "data", "DATA" }, { "AUTOGENBOOKMARK_1", "TEMPLATE" },
				{ "AUTOGENBOOKMARK_2", "TEMPLATE" }, { "AUTOGENBOOKMARK_3", "TEMPLATE" },
				{ "AUTOGENBOOKMARK_4", "TEMPLATE" }, { "AUTOGENBOOKMARK_5", "TEMPLATE" },
				{ "AUTOGENBOOKMARK_6", "TEMPLATE" }, { "AUTOGENBOOKMARK_7", "TEMPLATE" },
				{ "AUTOGENBOOKMARK_8", "TEMPLATE" }, { "AUTOGENBOOKMARK_9", "TEMPLATE" } };
		String[][] bookmarksWithoutMetadata = { { "grid", "GRID" }, { "dynamic text", "DYNAMIC_TEXT" },
				{ "image", "IMAGE" }, { "static text", "STATIC_TEXT" } };
		checkAllTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata);
	}

	/**
	 * Tests meatadata of data items in table hader/footer or table group
	 * header/footer and using the dataset of the table will be output.
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testDataItem() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/emitter/html/dataItemMetadata_Test.xml";
		String[][] bookmarksWithMetadata = { { "topLevelData", "DATA" }, { "dataInTableHeader", "DATA", "192" },
				{ "dataInGridInTableHeader", "DATA", "204" }, { "dataInTableGroupHeader", "DATA", "190" },
				{ "dataInGridInGridInTableGroupHeader", "DATA", "214" }, { "dataInTableGroupFooter", "DATA", "191" },
				{ "dataInTableFooter", "DATA", "193" } };
		String[][] bookmarksWithoutMetadata = { { "dataInTableHeaderWithDataSet", "DATA" },
				{ "dataInTableDetail", "DATA" }, { "dataInListHeader", "DATA" }, { "dataInListGroupHeader", "DATA" },
				{ "dataInListDetail", "DATA" }, { "dataInListGroupFooter", "DATA" }, { "dataInListFooter", "DATA" } };
		checkAllTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata);
	}

	public void testDataItemWithoutBookmark() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/emitter/html/dataItemWithoutBookmarkMetadata_Test.xml";
		String[][] bookmarksWithMetadata = { { "AUTOGENBOOKMARK_1", "DATA", "176" }, { "__bookmark_1", "TABLE", "177" },
				{ "AUTOGENBOOKMARK_2", "DATA", "192" }, { "AUTOGENBOOKMARK_4", "DATA", "204" },
				{ "AUTOGENBOOKMARK_5", "DATA", "190" }, { "AUTOGENBOOKMARK_8", "DATA", "214" },
				{ "AUTOGENBOOKMARK_9", "DATA", "191" }, { "AUTOGENBOOKMARK_10", "DATA", "193" },
				{ "__bookmark_2", "LIST", "228" } };
		String[][] bookmarksWithoutMetadata = {};
		checkAllTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata);
	}

	/**
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testDisplayGroupIcon() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/emitter/html/displayGroupIcon_Test.xml";
		// Tests group icon will or won't be output:
		// a. output by RenderTask with flag = true.
		checkDisplyGroupIcon(designFile, true, true);
		// b. output by RunAndRenderTask with flag = true.
		checkDisplyGroupIcon(designFile, true, false);
		// c. not output by RenderTask with flag = false.
		checkDisplyGroupIcon(designFile, false, true);
		// d. not output by RunAndRenderTask with flag = false.
		checkDisplyGroupIcon(designFile, false, false);
	}

	/**
	 * Test group icon will be displayed with group key even when the key is in a
	 * grid without query.
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testDisplayGroupIconWithGroupKey() throws EngineException, IOException {
		String designFile = "org/eclipse/birt/report/engine/emitter/html/displayGroupIcon_Test1.xml";

		// a. Test run and render task.
		HTMLRenderOption options = new HTMLRenderOption();
		options.setDisplayGroupIcon(true);
		String[] iconKeys = { "group key1 in data item", "group key2 in grid without query", "group key3 in data item",
				"group key4 with same expression" };
		String content = getRenderResult(designFile, false, options).content;
		checkAllGroupIconDisplayed(options, content, iconKeys);

		content = getRenderResult(designFile, true, options).content;
		checkAllGroupIconDisplayed(options, content, iconKeys);
	}

	/**
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testDefaultGroupIconPlace() throws EngineException, IOException {
		// the default cell to place the group icon is the first cell.
		String designFile = "org/eclipse/birt/report/engine/emitter/html/displayGroupIcon_Test3.xml";
		HTMLRenderOption options = new HTMLRenderOption();
		options.setDisplayGroupIcon(true);
		String content = getRenderResult(designFile, true, options).content;
		content = content.replaceAll("\n", "\"\n\"+\\\\n");
		String regex = "<table[^<>]*>[^<>]*<tr[^<>]*>[^<>]*<td[^<>]*>[^<>]*<img[^<>]* src=\"./images/iv/collapsexpand.gif\"[^<>]*>[^<>]*</img>"
				+ "[^<>]*</td[^<>]*>[^<>]*<td[^<>]*>[^<>]*<div[^<>]*>[^<>]*GroupHead";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());
	}

	/**
	 * 
	 * @throws EngineException
	 * @throws IOException
	 */
	public void testWrapTemplateTable() throws EngineException, IOException {
		// the default cell to place the group icon is the first cell.
		String designFile = "org/eclipse/birt/report/engine/emitter/html/wrapTemplateTable_test.xml";
		HTMLRenderOption options = new HTMLRenderOption();
		options.setWrapTemplateTable(true);
		String content = getRenderResult(designFile, true, options).content;
		content = content.replaceAll("\n", "\"\n\"+\\\\n");
		String regex = "<table[^<>]*>[^<>]*<tbody[^<>]*>[^<>]*<tr[^<>]*>[^<>]*<td[^<>]*>[^<>]*<img[^<>]* src=\"./bizRD/images/sidetab_active.png\"[^<>]*>";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());
	}

	private void checkAllGroupIconDisplayed(HTMLRenderOption options, String content, String[] keys)
			throws EngineException, IOException {
		for (int i = 0; i < keys.length; i++) {
			assertHasGroupkey(content, options, keys[i]);
		}
	}

	private void assertHasGroupkey(String content, HTMLRenderOption options, String identityString)
			throws EngineException, IOException {
		String prefix = "<td[^<>]*>[^<>]*<img src=\"./images/iv/collapsexpand.gif\"[^<>]*>[^<>]*</img>[^<>]*</td>[^<>]*<td[^<>]*>[^<>]*<div[^<>]*>";
		Pattern pattern = Pattern.compile(prefix + identityString);
		if (!pattern.matcher(content).find()) {
			fail("Group icon \"" + identityString + "\" is not displayed.");
		}
	}

	private void checkDisplyGroupIcon(String designFile, boolean displayFilterIcon, boolean isRenderTask)
			throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setDisplayGroupIcon(displayFilterIcon);
		String content = getRenderResult(designFile, isRenderTask, options).content;
		content = content.replaceAll("\n", "\"\n\"+\\\\n");
		String regex = "<table[^<>]*>[^<>]*<tr[^<>]*>[^<>]*<td[^<>]*>[^<>]*<img[^<>]* src=\"./images/iv/collapsexpand.gif\"[^<>]*>";
		assertString(displayFilterIcon, content, regex);
		String imageRegex = " src=\"./images/iv/collapsexpand.gif\"";
		assertString(displayFilterIcon, content, imageRegex);
	}

	private void assertString(boolean displayFilterIcon, String content, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(displayFilterIcon, matcher.find());
	}

	private void checkAllTask(String designFile, String[][] bookmarksWithMetadata, String[][] bookmarksWithoutMetadata)
			throws EngineException, IOException {
		checkRenderTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata);
		checkRunAndRenderTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata);
	}

	private void checkOutput(String content, List instanceIds, String[][] bookmarksWithMetadata,
			String[][] bookmarksWithoutMetadata) {
		for (int i = 0; i < bookmarksWithMetadata.length; i++) {
			if (bookmarksWithMetadata[0].length >= 3) {
				assertMetadataOutput(content, instanceIds, bookmarksWithMetadata[i][0], bookmarksWithMetadata[i][1],
						bookmarksWithMetadata[i][2]);
			} else {
				assertMetadataOutput(content, instanceIds, bookmarksWithMetadata[i][0], bookmarksWithMetadata[i][1],
						null);
			}
		}
		for (int i = 0; i < bookmarksWithoutMetadata.length; i++) {
			assertMetadataNotOutput(content, instanceIds, bookmarksWithoutMetadata[i][0],
					bookmarksWithoutMetadata[i][1]);
		}
	}

	private void checkRunAndRenderTask(String designFile, String[][] bookmarksWithMetadata,
			String[][] bookmarksWithoutMetadata) throws EngineException, IOException {
		checkTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata, true);
	}

	private void checkRenderTask(String designFile, String[][] bookmarksWithMetadata,
			String[][] bookmarksWithoutMetadata) throws EngineException, IOException {
		checkTask(designFile, bookmarksWithMetadata, bookmarksWithoutMetadata, true);
	}

	private void checkTask(String designFile, String[][] bookmarksWithMetadata, String[][] bookmarksWithoutMetadata,
			boolean isRenderTask) throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setSupportedImageFormats("PNG;GIF;JPG;BMP;SWF;SVG");
		RenderResult result = getRenderResult(designFile, isRenderTask, options);
		checkOutput(result.content, result.instanceIDs, bookmarksWithMetadata, bookmarksWithoutMetadata);
	}

	private RenderResult getRenderResult(String designFile, boolean isRenderTask, HTMLRenderOption options)
			throws EngineException, IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		List instanceIDs = new ArrayList();
		options.setInstanceIDs(instanceIDs);
		options.setOutputStream(output);
		options.setEnableMetadata(true);
		if (isRenderTask) {
			IRenderTask task = createRenderTask(designFile);
			task.setRenderOption(options);
			task.render();
			task.close();
		} else {
			IRunAndRenderTask task = createRunAndRenderTask(designFile);
			task.setRenderOption(options);
			task.run();
			task.close();
		}
		String content = new String(output.toByteArray());
		output.close();
		return new RenderResult(instanceIDs, content);
	}

	private void assertMetadataOutput(String content, List instanceIds, String bookmark, String type, String id) {
		assertEquals(1, getCount(instanceIds, bookmark, type, id));
		assertTrue(metaDataOutputInHtml(content, bookmark, type));
	}

	private void assertMetadataNotOutput(String content, List instanceIds, String bookmark, String type) {
		assertEquals(0, getCount(instanceIds, bookmark, type, null));
		assertTrue(metadataNotOutputInHtml(content, bookmark, type));
	}

	private boolean metaDataOutputInHtml(String content, String bookmark, String type) {
		String resultPattern = getMetaDataPattern(bookmark, type);
		Pattern pattern = Pattern.compile(resultPattern);
		Matcher matcher = pattern.matcher(content);
		boolean result = false;
		if (matcher.find()) {
			String attributes = matcher.group(1);
			String elementType = " element_type=\"" + type + "\"";
			if (attributes.indexOf(elementType) >= 0) {
				result = true;
			}
		}
		assertFalse(matcher.find());
		return result;
	}

	private boolean metadataNotOutputInHtml(String content, String bookmark, String type) {
		Pattern metadataPattern = Pattern.compile(getMetaDataPattern(bookmark, type));
		assertFalse(metadataPattern.matcher(content).matches());
		String patternString = getNoMetaDataPattern(bookmark, type);
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String attributes = matcher.group(1);
			String elementType = " element_type=\"" + type + "\"";
			if (attributes.indexOf(elementType) >= 0) {
				return false;
			}
		}
		return true;
	}

	private String getMetaDataPattern(String bookmark, String type) {
		String[] patternStrings = getMetadataString(type);
		String resultPattern = null;
		if (bookmark.startsWith("AUTOGENBOOKMARK_")) {
			resultPattern = patternStrings[0] + bookmark + "_.{8}-.{4}-.{4}-.{4}-.{12}" + patternStrings[1];
		} else {
			resultPattern = patternStrings[0] + bookmark + patternStrings[1];
		}
		return resultPattern;
	}

	private String getNoMetaDataPattern(String bookmark, String type) {
		String[] patternStrings = getNoMetadataString(type);
		String resultPattern = patternStrings[0] + bookmark + patternStrings[1];
		return resultPattern;
	}

	private int getCount(List list, String bookmark, String type, String id) {
		if (bookmark == null || type == null) {
			return 0;
		}
		int count = 0;
		for (int i = 0; i < list.size(); i++) {
			String value = (String) list.get(i);
			String[] fields = value.split(",");
			if (fields.length < 3) {
				return 0;
			}
			if (isSameBookmark(bookmark, fields[0]) && type.equals(fields[1]) && (id == null || fields[2].equals(id))) {
				++count;
			}
		}
		return count;
	}

	private boolean isSameBookmark(String golden, String bookmark) {
		if (golden.startsWith("AUTOGENBOOKMARK_") && bookmark.startsWith("AUTOGENBOOKMARK_")) {
			int cutPoint = bookmark.lastIndexOf('_');
			if (golden.equals(bookmark.substring(0, cutPoint))) {
				return true;
			}
		}
		if (bookmark.equals(golden)) {
			return true;
		}

		return false;
	}

	private String[] getMetadataString(String type) {
		if ("TABLE".equals(type) || "GRID".equals(type)) {
			return new String[] { table1, table2 };
		} else if ("LIST".equals(type)) {
			return new String[] { list1, list2 };
		} else if ("IMAGE".equals(type) || "EXTENDED".equals(type)) {
			return new String[] { imageMetadata1, imageMetadata2 };
		} else if ("Chart".equals(type)) {
			return new String[] { chartMetadata1, chartMetadata2 };
		} else if ("TEMPLATE".equals(type)) {
			return new String[] { template1, template2 };
		} else if ("LABEL".equals(type) || "DATA".equals(type) || "TEMPLATE".equals(type) || "TEXT".equals(type)
				|| "STATIC_TEXT".equals(type) || "DYNAMIC_TEXT".equals(type)) {
			return new String[] { metadata1, metadata2 };
		}
		fail();
		return null;
	}

	private String[] getNoMetadataString(String type) {
		if ("TABLE".equals(type) || "GRID".equals(type)) {
			return new String[] { table1, table2 };
		} else if ("LIST".equals(type)) {
			return new String[] { list1, list2 };
		} else if ("IMAGE".equals(type)) {
			return new String[] { imageNoMetadata1, imageNoMetadata2 };
		} else if ("LABEL".equals(type) || "DATA".equals(type) || "TEMPLATE".equals(type) || "STATIC_TEXT".equals(type)
				|| "DYNAMIC_TEXT".equals(type)) {
			return new String[] { metadata1, metadata2 };
		}
		fail();
		return null;
	}

	private class RenderResult {
		List instanceIDs;
		String content;

		public RenderResult(List instanceIDs, String content) {
			this.instanceIDs = instanceIDs;
			this.content = content;
		}
	}
}
