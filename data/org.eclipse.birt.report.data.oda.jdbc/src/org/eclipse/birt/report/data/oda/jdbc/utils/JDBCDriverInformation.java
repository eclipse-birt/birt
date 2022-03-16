/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.utils;

import java.sql.Driver;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * This class maintains information about a specific JDBC driver such as its
 * name, version, class etc. It cannot be instantiated directly.
 *
 * call the {@link #getInstance(java.sql.Driver) getInstance} method to create
 * an instance
 *
 *
 * @version $Revision: 1.14.8.1 $ $Date: 2010/12/10 09:54:53 $
 */
public final class JDBCDriverInformation {
	private String driverClassName = null;
	private int majorVersion = 0;
	private int minorVersion = 0;
	private String urlFormat = null;
	private String selectorId = null;
	private String driverDisplayName = null;
	private boolean hide = false;
	private List<PropertyGroup> propertyGroup = new ArrayList<>();

	/**
	 * Since factory methods are provided, it is recommended to make construction
	 * method private.
	 */
	private JDBCDriverInformation() {
	}

	public static JDBCDriverInformation newInstance(Class driverClass) {
		try {
			Driver d = JDBCDriverManager.getInstance().getDriverInstance(driverClass, false);
			if (d != null) {
				JDBCDriverInformation info = newInstance(driverClass.getName());
				try {
					info.setMajorVersion(d.getMajorVersion());
					info.setMinorVersion(d.getMinorVersion());
				} catch (Throwable e) {
					Logger.getLogger(JDBCDriverInformation.class.getName()).log(Level.WARNING, e.getMessage(), e);
				}
				return info;
			}
		} catch (Throwable e) {
			Logger.getLogger(JDBCDriverInformation.class.getName()).log(Level.WARNING, e.getMessage(), e);
		}

		return null;
	}

	public static JDBCDriverInformation newInstance(String driverClassName) {
		JDBCDriverInformation info = new JDBCDriverInformation();
		info.setDriverClassName(driverClassName);
		return info;
	}

	/**
	 * @return Returns the driverClassName.
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * @param driverClassName The driverClassName to set.
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * @return Returns the majorVersion.
	 */
	public int getMajorVersion() {
		return majorVersion;
	}

	/**
	 * @param majorVersion The majorVersion to set.
	 */
	protected void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	/**
	 * @return Returns the minorVersion.
	 */
	public int getMinorVersion() {
		return minorVersion;
	}

	/**
	 * @param minorVersion The minorVersion to set.
	 */
	protected void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	/**
	 * @return Returns the urlFormat.
	 */
	public String getUrlFormat() {
		return urlFormat;
	}

	/**
	 * @return Returns the SelectorId.
	 */
	public String getSelectorId() {
		return selectorId;
	}

	public boolean hasProperty() {
		if (this.propertyGroup == null) {
			return false;
		}
		return this.propertyGroup.size() > 0;
	}

	/**
	 * @param urlFormat The urlFormat to set.
	 */
	public void setUrlFormat(String urlFormat) {
		this.urlFormat = urlFormat;
	}

	/**
	 * @param The selector_id to be set.
	 */
	public void setSelectorId(String selectorId) {
		this.selectorId = selectorId;
	}

	/**
	 * @return Returns the displayName.
	 */
	public String getDisplayName() {
		return driverDisplayName;
	}

	/**
	 * @param displayName The displayName to set.
	 */
	public void setDisplayName(String displayName) {
		this.driverDisplayName = displayName;
	}

	public boolean getHide() {
		return hide;
	}

	public void setHide(String hide) {
		if (hide != null) {
			this.hide = hide.equals("true") ? true : false;
		}
	}

	public void setHide(boolean flag) {
		this.hide = flag;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(driverClassName);
		if (majorVersion != 0 || minorVersion != 0) {
			buffer.append(" (");
			buffer.append(majorVersion);
			buffer.append(".");
			buffer.append(minorVersion);
			buffer.append(")");
		}
		return buffer.toString();
	}

	/**
	 * Gets a display-friendly string which has driver class, driver name and
	 * version
	 */
	public String getDisplayString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(driverClassName);
		if (majorVersion != 0 || minorVersion != 0 || driverDisplayName != null) {
			buffer.append(" (");
			if (driverDisplayName != null) {
				buffer.append(driverDisplayName);
			}
			if (majorVersion != 0 || minorVersion != 0) {
				if (driverDisplayName != null) {
					buffer.append(" ");
				}
				buffer.append("v");
				buffer.append(majorVersion);
				buffer.append(".");
				buffer.append(minorVersion);
			}
			buffer.append(")");
		}
		return buffer.toString();
	}

	/**
	 * Overwrite the equals() method
	 *
	 */
	@Override
	public boolean equals(Object anotherObj) {
		if (this == anotherObj) {
			return true;
		}
		if (!(anotherObj instanceof JDBCDriverInformation)) {
			return false;
		}
		JDBCDriverInformation info = (JDBCDriverInformation) anotherObj;
		if (this.driverClassName != null && this.driverClassName.equalsIgnoreCase(info.driverClassName)
				&& this.majorVersion == info.majorVersion && this.minorVersion == info.minorVersion) {
			return true;
		}
		return false;
	}

	/**
	 * Overwrite the hashCode() method
	 *
	 */
	@Override
	public int hashCode() {
		int hashcode = 0;
		if (this.driverClassName != null) {
			hashcode += this.driverClassName.hashCode() * 11;
		}
		return (hashcode + this.majorVersion * 13) + this.minorVersion * 17;
	}

	private PropertyElement populateProperty(IConfigurationElement configElement) {
		PropertyElement element = new PropertyElement();
		element.setAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_NAME,
				configElement.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_NAME));
		element.setAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DISPLAYNAME,
				configElement.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DISPLAYNAME));
		element.setAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DEC,
				configElement.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_DEC));
		element.setAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_ENCRYPT,
				configElement.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_ENCRYPT));
		element.setAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE,
				configElement.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_TYPE));
		return element;
	}

	public List<PropertyGroup> getPropertyGroup() {
		return propertyGroup;
	}

	public void populateProperties(IConfigurationElement configElement) {
		IConfigurationElement[] elements = configElement.getChildren(DriverInfoConstants.DRIVER_INFO_PROPERTY_GROUP);
		for (IConfigurationElement element : elements) {
			String name = element.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_GROUP_NAME);
			String desc = element.getAttribute(DriverInfoConstants.DRIVER_INFO_PROPERTY_GROUP_DEC);
			PropertyGroup group = new PropertyGroup(name, desc);
			IConfigurationElement[] propertiesConfigurationElements = element
					.getChildren(DriverInfoConstants.DRIVER_INFO_PROPERTY);
			List<PropertyElement> list = new ArrayList<>();
			for (IConfigurationElement prElement : propertiesConfigurationElements) {
				list.add(populateProperty(prElement));
			}
			group.setProperties(list);
			propertyGroup.add(group);
		}
	}
}
