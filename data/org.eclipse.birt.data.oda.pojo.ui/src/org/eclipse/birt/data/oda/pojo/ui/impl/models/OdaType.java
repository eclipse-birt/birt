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

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;

/**
 *
 */

public enum OdaType {
	String(Constants.ODA_TYPE_String, Messages.getString("DataSet.DataType.String")), //$NON-NLS-1$
	Integer(Constants.ODA_TYPE_Integer, Messages.getString("DataSet.DataType.Integer")), //$NON-NLS-1$
	Double(Constants.ODA_TYPE_Double, Messages.getString("DataSet.DataType.Double")), //$NON-NLS-1$
	Decimal(Constants.ODA_TYPE_Decimal, Messages.getString("DataSet.DataType.Decimal")), //$NON-NLS-1$
	Date(Constants.ODA_TYPE_Date, Messages.getString("DataSet.DataType.Date")), //$NON-NLS-1$
	Time(Constants.ODA_TYPE_Time, Messages.getString("DataSet.DataType.Time")), //$NON-NLS-1$
	Timestamp(Constants.ODA_TYPE_Timestamp, Messages.getString("DataSet.DataType.Timestamp")), //$NON-NLS-1$
	Boolean(Constants.ODA_TYPE_Boolean, Messages.getString("DataSet.DataType.Boolean")), //$NON-NLS-1$
	Blob(Constants.ODA_TYPE_Blob, Messages.getString("DataSet.DataType.Blob")), //$NON-NLS-1$
	Object(Constants.ODA_TYPE_Object, Messages.getString("DataSet.DataType.Object")); //$NON-NLS-1$

	private String name;
	private String displayName;

	private static Map<String, OdaType> nameInstanceMap = new HashMap<>();
	static {
		for (OdaType ot : OdaType.values()) {
			nameInstanceMap.put(ot.getName(), ot);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return getDisplayName();
	}

	OdaType(String name, String displayName) {
		this.name = name;
		this.displayName = displayName;
	}

	public String getName() {
		return name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static OdaType getInstance(String name) {
		return nameInstanceMap.get(name);
	}

	public static class OdaTypeComparator implements Comparator<OdaType>, Serializable {
		private static final long serialVersionUID = 1L;

		@Override
		public int compare(OdaType o1, OdaType o2) {
			return o1.getDisplayName().compareTo(o2.getDisplayName());
		}
	}

}
