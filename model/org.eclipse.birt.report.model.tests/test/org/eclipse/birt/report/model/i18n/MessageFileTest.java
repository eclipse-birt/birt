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

package org.eclipse.birt.report.model.i18n;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IMemberInfo;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

import com.ibm.icu.util.ULocale;

/**
 * Test message file consistency.
 *
 * 1. Test to check if all the resourcekeys( value for the "displayNameID" attr
 * ) needed by the "rom.def" are contained in the message files.
 *
 * 2.Test to see if all the resourceKeys defined as contants in
 * <code>MessageConstants</code> are contained in the message file.
 *
 * 3. Checks if all the resourceKeys defined as constants in
 * <code>MessageConstants</code> are contained in the message file.
 *
 * 4. Checks if all the resourceKeys of properties, classes and methods defined
 * in meta data are contained in the message file.
 *
 * This test will test the messages based on "Messages.properties."
 *
 */
public class MessageFileTest extends BaseMessageFileTest {

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.i18n.BaseMessageFileTest#
	 * getMessageFileInputStream()
	 */
	@Override
	protected InputStream getMessageFileInputStream() {
		return ThreadResources.class.getResourceAsStream(DEFAULT_MESSAGE_FILE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.i18n.BaseMessageFileTest#loadRomFiles()
	 */
	@Override
	protected void loadRomFile() throws IOException {
		loadResourceKeys(ROM_DEF_NAME, ReportDesign.class.getResourceAsStream(ROM_DEF_NAME));
	}

	/**
	 * Loads resource keys from message constants.
	 */
	protected void loadMessageConstants() throws Exception {
		loadResourceKeys(MessageConstants.class);
	}

	/**
	 * Loads resource keys from meta data.
	 */
	protected void loadMetaDatas() {
		MetaDataDictionary metaData = MetaDataDictionary.getInstance();
		for (IPropertyType type : metaData.getPropertyTypes()) {
			resourceKeyMap.put(type.getDisplayNameKey(), type.getClass().getName());
		}
		for (IClassInfo classInfo : metaData.getClasses()) {
			resourceKeyMap.put(classInfo.getDisplayNameKey(), classInfo.getName());
			for (IMemberInfo member : classInfo.getMembers()) {
				resourceKeyMap.put(member.getDisplayNameKey(), member.getName());
			}
		}
		for (IMethodInfo methodInfo : metaData.getFunctions()) {
			resourceKeyMap.put(methodInfo.getDisplayNameKey(), methodInfo.getName());
		}
	}

	/**
	 * Loads resource keys from a constant class into the resource key map.
	 *
	 * @param constantsClass the class contains resource keys
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	protected void loadResourceKeys(Class<?> constantsClass) throws IllegalArgumentException, IllegalAccessException {
		int PUBLIC_FINAL_STATIC = Modifier.PUBLIC | Modifier.FINAL | Modifier.STATIC;
		Field[] fields = constantsClass.getFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];

			if (PUBLIC_FINAL_STATIC == field.getModifiers()) {
				String resourceKey = (String) fields[i].get(null);
				resourceKeyMap.put(resourceKey, constantsClass.getName() + "." //$NON-NLS-1$
						+ fields[i].getName());
			}
		}
	}

	/**
	 * Tests if all the resourceKeys of all the resourceKeys of properties, classes
	 * and methods defined in meta data are contained in the message file.
	 */
	public void testMetaDatas() {
		loadMetaDatas();
		checkResourceKeyMap();
	}

	/**
	 * Tests if all the resourceKeys defined in <code>MessageConstants</code> are
	 * contained in the message file.
	 *
	 * @throws Exception
	 */
	public void testMessageConstants() throws Exception {
		loadMessageConstants();
		checkResourceKeyMap();
	}

	/**
	 * Test to see if all the display name of tabular measure group.
	 *
	 * @throws Exception
	 */

	public void testDisplayNameOfTabularMeasureGroup() throws Exception {
		createDesign(ULocale.ENGLISH);
		TabularMeasureGroupHandle measureGroupHandle = designHandle.getElementFactory().newTabularMeasureGroup("");
		assertEquals("Summary Field", measureGroupHandle.getName());
		measureGroupHandle = designHandle.getElementFactory().newTabularMeasureGroup("");
		assertEquals("Summary Field1", measureGroupHandle.getName());
	}
}
