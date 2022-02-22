/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.models;

import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.swt.graphics.Image;

public class ClassPathElement {

	public static String RELATIVE_PATH = "RelativePath"; //$NON-NLS-1$
	public static String ABSOLUTE_PATH = "AbsolutePath"; //$NON-NLS-1$

	private static Image JAR_ICON = Utils.getJarIcon();

	private String value;
	private boolean isRelativePath;
	private String fullPath;

	public ClassPathElement(String value, String fullPath, boolean isRelativePath) {
		this.value = value;
		this.isRelativePath = isRelativePath;
		this.fullPath = fullPath;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getFullPath() {
		return this.fullPath;
	}

	public void setRelativePath(boolean isRelativePath) {
		this.isRelativePath = isRelativePath;
	}

	public boolean isRelativePath() {
		return isRelativePath;
	}

	public Image getIcon() {
		return JAR_ICON;
	}

//	public boolean equals( ClassPathElement another )
//	{
//		if ( another == null )
//			return false;
//
//		if ( this.value == null || another.getValue( ) == null )
//			return false;
//
//		if ( !this.value.equalsIgnoreCase( another.getValue( ) ) )
//			return false;
//
//		if ( this.fullPath == null || another.getFullPath( ) == null )
//			return false;
//
//		if ( !this.fullPath.equalsIgnoreCase( another.getFullPath( ) ) )
//			return false;
//
//		if ( this.isRelativePath != another.isRelativePath( ) )
//			return false;
//
//		return true;
//	}
//
//	public int hashCode( )
//	{
//		return ( 13 * 17 + 19 ) * 29;
//	}
}
