/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ooxml.util;

import org.eclipse.birt.report.engine.layout.emitter.Image;

public class OOXmlUtil {

	private static final int RATIO_POINT_TO_EMUS = 12700;

	public static Image getImageInfo(byte[] data) {
		if (data != null) {
			Image imageInfo = new Image();
			imageInfo.setInput(data);
			imageInfo.check();
			return imageInfo;
		}
		return null;
	}

	public static long convertPointerToEmus(double value) {
		return (long) (value * RATIO_POINT_TO_EMUS);
	}

	public static String getRelativeUri(String parent, String child) {
		if (!isAbsolute(child)) {
			return child;
		}
		if (!isAbsolute(parent)) {
			throw new IllegalArgumentException("Parent uri must be absolute when child uri is absolute.");
		}
		parent = parent.substring(1);
		child = child.substring(1);
		if (child.startsWith(parent)) {
			return child.substring(parent.length());
		} else {
			String[] parentPaths = parent.split("/");
			String[] currentPaths = child.split("/");
			int max = Math.max(parentPaths.length, currentPaths.length);
			int sameCount = 0;
			for (int i = 0; i < max; i++) {
				if (parentPaths[i].equals(currentPaths[i])) {
					sameCount++;
				} else {
					break;
				}
			}
			int upLevel = parentPaths.length - sameCount;
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < upLevel - 1; i++) {
				buffer.append("../");
			}
			for (int i = sameCount; i < currentPaths.length; i++) {
				buffer.append(currentPaths[i]);
				if (i != currentPaths.length - 1)
					buffer.append('/');
			}
			return buffer.toString();
		}
	}

	public static String getAbsoluteUri(String parent, String child) {
		if (parent == null || child == null) {
			return null;
		}
		if (isAbsolute(child)) {
			return child;
		}
		String[] parentPaths = parent.split("/");
		String[] childPaths = child.split("/");
		int start = parent.endsWith("/") ? 0 : 1;
		int upLevel = start;
		for (String childPath : childPaths) {
			String path = childPath.trim();
			if ("..".equals(path)) {
				upLevel++;
			} else {
				break;
			}
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append('/');
		for (int i = 0; i < parentPaths.length - upLevel; i++) {
			String path = parentPaths[i];
			if (!"".equals(path)) {
				buffer.append(path);
				buffer.append('/');
			}
		}
		for (int i = upLevel - start; i < childPaths.length; i++) {
			buffer.append(childPaths[i]);
			if (i != childPaths.length - 1) {
				buffer.append('/');
			}
		}
		return buffer.toString();
	}

	public static boolean isAbsolute(String uri) {
		if (uri == null) {
			return false;
		}
		return uri.startsWith("/");
	}

	public static String getRelationShipId(int id) {
		return "rId" + id;
	}
}
