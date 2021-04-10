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
package org.eclipse.birt.data.oda.pojo.testutil;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.querymodel.ClassColumnMappings;
import org.eclipse.birt.data.oda.pojo.querymodel.Column;
import org.eclipse.birt.data.oda.pojo.querymodel.ConstantParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.FieldSource;
import org.eclipse.birt.data.oda.pojo.querymodel.IColumnsMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.IMethodParameter;
import org.eclipse.birt.data.oda.pojo.querymodel.MethodSource;
import org.eclipse.birt.data.oda.pojo.querymodel.OneColumnMapping;
import org.eclipse.birt.data.oda.pojo.querymodel.PojoQuery;
import org.eclipse.birt.data.oda.pojo.querymodel.VariableParameter;

/**
 * 
 */

public class PojoQueryCreator {
	@SuppressWarnings("nls")
	public static PojoQuery createBasic() {
		PojoQuery query = new PojoQuery(null, null, "TeacherDataSet_Key");
		IColumnsMapping cm = new OneColumnMapping(new MethodSource("getId", null), new Column("Id", "Integer", 0));
		query.addColumnsMapping(cm);

		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("Name", "String", 1));
		query.addColumnsMapping(cm);

		cm = new OneColumnMapping(new FieldSource("age"), new Column("Age", "Integer", 2));
		query.addColumnsMapping(cm);

		ClassColumnMappings ccm = new ClassColumnMappings(new MethodSource("getDean", null));
		cm = new OneColumnMapping(new MethodSource("getId", null), new Column("DeanId", "Integer", 3));
		ccm.addColumnsMapping(cm);
		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("DeanName", "String", 4));
		ccm.addColumnsMapping(cm);
		query.addColumnsMapping(ccm);
		return query;
	}

	@SuppressWarnings("nls")
	public static PojoQuery createWithA1tonMap() {
		PojoQuery query = createBasic();

		ClassColumnMappings ccm = new ClassColumnMappings(new MethodSource("getStudents", null));
		IColumnsMapping cm = new OneColumnMapping(new MethodSource("getId", null),
				new Column("StudentId", "Integer", 5));
		ccm.addColumnsMapping(cm);
		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("StudentName", "String", 6));
		ccm.addColumnsMapping(cm);
		query.addColumnsMapping(ccm);

		return query;
	}

	@SuppressWarnings("nls")
	public static PojoQuery createWithMulti1tonMaps() {
		PojoQuery query = createBasic();

		ClassColumnMappings ccm = new ClassColumnMappings(new MethodSource("getStudents", null));
		IColumnsMapping cm = new OneColumnMapping(new MethodSource("getId", null),
				new Column("StudentId", "Integer", 5));
		ccm.addColumnsMapping(cm);
		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("StudentName", "String", 6));
		ccm.addColumnsMapping(cm);

		ClassColumnMappings nestCcm = new ClassColumnMappings(new MethodSource("getCourses", null));
		cm = new OneColumnMapping(new MethodSource("getId", null), new Column("StudentCourseId", "Integer", 7));
		nestCcm.addColumnsMapping(cm);
		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("StudentCourseName", "String", 8));
		nestCcm.addColumnsMapping(cm);

		ccm.addColumnsMapping(nestCcm);

		query.addColumnsMapping(ccm);

		return query;
	}

	@SuppressWarnings("nls")
	public static PojoQuery createWithParameters() {
		PojoQuery query = new PojoQuery(null, null, "TeacherDataSet_Key");

		ClassColumnMappings ccm = new ClassColumnMappings(new MethodSource("getStudents",
				new IMethodParameter[] { new ConstantParameter("18", Constants.PARAM_TYPE_int),
						new VariableParameter("sex", Constants.PARAM_TYPE_boolean),
						new ConstantParameter(null, Constants.PARAM_TYPE_String) }));
		IColumnsMapping cm = new OneColumnMapping(new MethodSource("getId", null),
				new Column("StudentId", "Integer", 2));
		ccm.addColumnsMapping(cm);
		cm = new OneColumnMapping(new MethodSource("getName", null), new Column("StudentName", "String", 3));
		ccm.addColumnsMapping(cm);
		query.addColumnsMapping(ccm);

		ccm = new ClassColumnMappings(new MethodSource("getStudentById",
				new IMethodParameter[] { new VariableParameter("id", Constants.PARAM_TYPE_int) }));
		cm = new OneColumnMapping(new MethodSource("getId", null), new Column("StudentId_Param", "Integer", 1));
		ccm.addColumnsMapping(cm);
		query.addColumnsMapping(ccm);
		return query;
	}
}
