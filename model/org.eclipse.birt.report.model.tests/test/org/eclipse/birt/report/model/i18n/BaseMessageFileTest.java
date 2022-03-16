/*******************************************************************************
 * Copyright (c) 2004, 2005, 2006, 2007, 2008, 2009, 2010 Actuate Corporation.
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

package org.eclipse.birt.report.model.i18n;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Abstract class for base message file test case to test message file
 * consistency.
 *
 * 1. Checks if there are duplicated messages in the message file.
 *
 * 2. Checks if all the resource keys( value for the "displayNameID" attribute,
 * etc. ) needed by the rom file are contained in the message files.
 *
 */
public abstract class BaseMessageFileTest extends BaseTestCase {

	protected final static String DEFAULT_MESSAGE_FILE = "Messages.properties"; //$NON-NLS-1$
	protected static final String CHARSET = "8859_1"; //$NON-NLS-1$

	protected static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
	protected static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$
	protected static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$

	protected Properties props = new Properties();
	protected Map<String, String> resourceKeyMap = new LinkedHashMap<>();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.BaseTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		loadMessageFile();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.BaseTestCase#tearDown()
	 */
	@Override
	public void tearDown() {
		props.clear();
		resourceKeyMap.clear();
	}

	/**
	 * Loads all resource keys and values into properties from the message file.
	 */
	protected void loadMessageFile() throws IOException {
		loadProperties(getMessageFileInputStream());
	}

	/**
	 * Loads all resource keys and values into properties from the given input
	 * steam.
	 *
	 * @param is the input stream
	 */
	protected void loadProperties(InputStream is) throws IOException {
		props.load(is);
	}

	/**
	 * Gets the input stream of the message file.
	 *
	 * @return the input steam
	 */
	abstract protected InputStream getMessageFileInputStream();

	/**
	 * Loads resource keys from rom files.
	 *
	 * @throws IOException
	 */
	abstract protected void loadRomFile() throws IOException;

	/**
	 * Loads all the display names,tooltips and tags defined in the given file into
	 * the resource key map, keyed by themselves and valued by their description.
	 *
	 * @param fileName the file name
	 * @param is       the input stream of the file
	 * @throws IOException
	 */
	protected void loadResourceKeys(String fileName, InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String line = br.readLine();
		for (int lineCount = 1; line != null; line = br.readLine(), lineCount++) {
			String description = fileName + "@line " + lineCount; //$NON-NLS-1$
			String displayNameId = getResourceKey(line, DISPLAY_NAME_ID_ATTRIB);
			// Only recode when first occurs
			if (displayNameId != null && !resourceKeyMap.containsKey(displayNameId)) {
				resourceKeyMap.put(displayNameId, description);
			}

			String toolTipId = getResourceKey(line, TOOL_TIP_ID_ATTRIB);
			if (toolTipId != null && !resourceKeyMap.containsKey(toolTipId)) {
				resourceKeyMap.put(toolTipId, description);
			}

			String tagId = getResourceKey(line, TAG_ID_ATTRIB);
			if (tagId != null && !resourceKeyMap.containsKey(tagId)) {
				resourceKeyMap.put(tagId, description);
			}
		}

		br.close();
	}

	/**
	 * Find the resource key from a string.
	 *
	 * @param line the input line
	 * @param name the name of the resource key
	 * @return the id of the resource key
	 */
	private String getResourceKey(String line, String name) {
		int index1 = line.indexOf(name);
		if (index1 == -1) {
			return null;
		}

		// check to see if the first none-blank char after "displayNameID" is
		// '='
		// e.g. displayNameID ="abc"

		int index2 = line.indexOf('=', index1);
		if (index2 == -1) {
			return null;
		}

		String str = line.substring(index1, index2);
		if (!name.equalsIgnoreCase(str.trim())) {
			return null;
		}

		int start = line.indexOf('"', index1);
		int end = line.indexOf('"', start + 1);

		String id = line.substring(start + 1, end);

		return id;
	}

	/**
	 * Checks if there are duplicated messages in the message file.
	 */
	public void testDuplicateMessages() throws IOException {
		boolean success = true;

		InputStream is = getMessageFileInputStream();
		BufferedReader in = new BufferedReader(new InputStreamReader(is, CHARSET));

		Hashtable<String, String> collection = new Hashtable<>();

		String line = in.readLine();
		int lineIndex = 1;
		StringBuilder errorMessage = new StringBuilder();

		while (line != null) {
			if (StringUtil.isBlank(line) || line.startsWith("#")) //$NON-NLS-1$
			{
				line = in.readLine();
				lineIndex++;
				continue;
			}

			String[] data = line.split("="); //$NON-NLS-1$
			if (data.length != 2) {
				errorMessage.append("errors of i18n in line " + lineIndex); //$NON-NLS-1$
				errorMessage.append('\n');
				line = in.readLine();
				lineIndex++;
				continue;
			}

			if (collection.containsKey(data[0])) {
				errorMessage.append("duplicate messages in line " + lineIndex); //$NON-NLS-1$
				errorMessage.append('\n');
				success = false;
			} else {
				collection.put(data[0], data[1]);
			}

			line = in.readLine();
			lineIndex++;
		}

		assertTrue(errorMessage.toString(), success);
	}

	/**
	 * Tests if all the resourceKeys needed by the rom file are contained in the
	 * message file.
	 *
	 */
	public void testRom() throws Exception {
		loadRomFile();
		checkResourceKeyMap();
	}

	/**
	 * Check whether all resource keys is in the message file.
	 */
	protected void checkResourceKeyMap() {
		boolean success = true;
		StringBuilder errorMessage = new StringBuilder();
		for (Entry<String, String> entry : resourceKeyMap.entrySet()) {
			String resourceKey = entry.getKey();

			if (!props.containsKey(resourceKey)) {
				String description = entry.getValue();
				errorMessage.append(resourceKey + " in " + description + " not exist in message file"); //$NON-NLS-1$//$NON-NLS-2$
				errorMessage.append('\n');
				success = false;
			}
		}
		assertTrue(errorMessage.toString(), success);
	}

}
