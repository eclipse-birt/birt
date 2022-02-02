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

import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.api.PojoDataSetFromCollection;
import org.eclipse.birt.data.oda.pojo.testutil.PojoInstancesUtil;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * 
 */

public class TeacherDataSet extends PojoDataSetFromCollection {

	@SuppressWarnings("unchecked")
	@Override
	protected Collection fetchPojos() throws OdaException {
		return PojoInstancesUtil.createTeachers();
	}

	public List<Teacher> getTeachers() {
		return PojoInstancesUtil.createTeachers();
	}
}
