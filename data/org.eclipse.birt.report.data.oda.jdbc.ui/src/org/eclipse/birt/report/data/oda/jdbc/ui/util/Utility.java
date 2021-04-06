/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ParameterMetaData;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.25 $ $Date: 2008/08/04 07:55:18 $
 */
public class Utility {
	// flag to indicate whether JarInfo and DriverInfo in preference page have
	// been updated from String[] to JarFile and DriverInfo
	private static boolean updatedOfJarInfo = false;
	private static boolean updatedOfDriverInfo = false;

	/**
	 * 
	 */
	private Utility() {
	}

	/**
	 * give the stored procedure's column type name from the type.
	 * 
	 * @param type
	 * @return
	 */
	public static String toModeType(int type) {
		switch (type) {
		case ParameterMetaData.parameterModeUnknown:
			return "Unknown";
		case ParameterMetaData.parameterModeIn:
			return "Input";
		case ParameterMetaData.parameterModeInOut:
			return "Input/Output";
		case ParameterMetaData.parameterModeOut:
			return "Output";
		case 5:
			return "Return Value";
		default:
			return "Unknown";
		}
	}

	/**
	 * Get Map from PreferenceStore by key
	 * 
	 * @param mapKey the key of the map
	 * @return Map
	 */
	public static Map getPreferenceStoredMap(String mapKey) {
		String driverMap64 = JdbcPlugin.getDefault().getPreferenceStore().getString(mapKey);
		try {
			if (driverMap64 != null) {
				byte[] bytes = Base64.decodeBase64(driverMap64.getBytes());

				ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
				Object obj = new ObjectInputStream(bis).readObject();

				if (obj instanceof Map) {
					return updatePreferenceMap((Map) obj, mapKey);
				}
			}
		} catch (IOException e) {
			// ignore
		} catch (ClassNotFoundException e) {
			ExceptionHandler.showException(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					JdbcPlugin.getResourceString("exceptionHandler.title.error"), e.getLocalizedMessage(), e);

		}

		return new HashMap();
	}

	/**
	 * Since the data type stored in this map has been changed,this method is design
	 * to surpport the former and the new preference
	 * 
	 * @param map
	 * @return
	 */
	private static Map updatePreferenceMap(Map map, String mapKey) {
		if (JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY.equals(mapKey)) {
			if (updatedOfDriverInfo)
				return map;

			updatedOfDriverInfo = true;
		} else if (JdbcPlugin.JAR_MAP_PREFERENCE_KEY.equals(mapKey)) {
			if (updatedOfJarInfo)
				return map;

			updatedOfJarInfo = true;
		} else {
			return map;
		}

		Set entrySet = map.entrySet();
		Iterator it = entrySet.iterator();
		if (!it.hasNext()) {
			// it is an empty Map
			return map;
		} else {
			Map.Entry entry = (Map.Entry) it.next();
			if ((entry.getValue() instanceof DriverInfo) || (entry.getValue() instanceof JarFile)) {
				return map;
			} else {
				it = entrySet.iterator();
				Map rMap = new HashMap();
				String[] entryValue;
				if (JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY.equals(mapKey)) {
					DriverInfo driverInfo;
					while (it.hasNext()) {
						entry = (Map.Entry) it.next();
						entryValue = (String[]) entry.getValue();
						driverInfo = new DriverInfo(entry.getKey().toString(), entryValue[0], entryValue[1]);
						rMap.put(entry.getKey(), driverInfo);
					}
				} else if (JdbcPlugin.JAR_MAP_PREFERENCE_KEY.equals(mapKey)) {
					JarFile jarFile;
					while (it.hasNext()) {
						entry = (Map.Entry) it.next();
						entryValue = (String[]) entry.getValue();
						jarFile = new JarFile(getFileNameFromFilePath((String) entryValue[0]), entryValue[0], "",
								false);
						rMap.put(entry.getKey(), jarFile);
					}
				}
				setPreferenceStoredMap(JdbcPlugin.DRIVER_MAP_PREFERENCE_KEY, rMap);
				return rMap;
			}
		}
	}

	private static String getFileNameFromFilePath(String filePath) {
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + File.separator.length());
		return fileName;
	}

	/**
	 * Put <tt>value</tt> with key <tt>keyInMap</tt>into the map whose key is
	 * <tt>keyOfPreference</tt>
	 * 
	 * @param keyOfPreference key of PreferenceStore Map
	 * @param keyInMap        key in the Map
	 * @param value           the value to be set
	 */
	public static void putPreferenceStoredMapValue(String keyOfPreference, String keyInMap, Object value) {
		Map map = getPreferenceStoredMap(keyOfPreference);
		map.put(keyInMap, value);
		setPreferenceStoredMap(keyOfPreference, map);
	}

	/**
	 * Removes map entry with key <tt>keyInMap</tt>from the map whose key is
	 * <tt>keyOfPreference</tt>
	 * 
	 * @param keyOfPreference key of PreferenceStore Map
	 * @param keyInMap        key in the Map
	 */
	public static void removeMapEntryFromPreferenceStoredMap(String keyOfPreference, String keyInMap) {
		Map map = getPreferenceStoredMap(keyOfPreference);
		if (map.containsKey(keyInMap)) {
			map.remove(keyInMap);
		}
		setPreferenceStoredMap(keyOfPreference, map);
	}

	/**
	 * Reset the map in PreferenceStored
	 * 
	 * @param keyOfPreference key in PreferenceStore
	 * @param map             the map to be set
	 */
	public static void setPreferenceStoredMap(String keyOfPreference, Map map) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			new ObjectOutputStream(bos).writeObject(map);

			byte[] bytes = bos.toByteArray();

			bytes = Base64.encodeBase64(bytes);

			JdbcPlugin.getDefault().getPreferenceStore().setValue(keyOfPreference, new String(bytes));
		} catch (IOException e) {
			// ignore
		}
	}

	/**
	 * 
	 * @param control
	 * @param contextId
	 */
	public static void setSystemHelp(Control control, String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, contextId);
	}

	public static String quoteString(String quoted, String quoteFlag) {
		assert quoteFlag != null;
		if (quoted == null) {
			return "";
		} else {
			return quoteFlag + quoted + quoteFlag;
		}
	}
}
