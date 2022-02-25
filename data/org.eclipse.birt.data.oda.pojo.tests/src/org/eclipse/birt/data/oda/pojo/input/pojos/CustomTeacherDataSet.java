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
package org.eclipse.birt.data.oda.pojo.input.pojos;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.data.oda.pojo.testutil.PojoInstancesUtil;

/**
 * A class contains open()/next()/close() methods
 */
public class CustomTeacherDataSet {
	@SuppressWarnings("unchecked")
	Iterator teachers;

	public void open(Object appContext, Map<String, Object> dataSetParamValues) {
		teachers = PojoInstancesUtil.createTeachers().iterator();
	}

	public Object next() {
		if (teachers.hasNext()) {
			return teachers.next();
		}
		return null;
	}

	public void close() {
		teachers = null;
	}
}
