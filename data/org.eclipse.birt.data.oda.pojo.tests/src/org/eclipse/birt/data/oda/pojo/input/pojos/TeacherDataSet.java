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
