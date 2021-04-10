
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.input.pojos.Course;
import org.eclipse.birt.data.oda.pojo.input.pojos.Dean;
import org.eclipse.birt.data.oda.pojo.input.pojos.Student;
import org.eclipse.birt.data.oda.pojo.input.pojos.Teacher;
import org.eclipse.birt.data.oda.pojo.input.pojos.TeacherStudent;
import org.eclipse.birt.data.oda.pojo.input.pojos.TeacherStudentCourse;

/**
 * 
 */

public class PojoInstancesUtil {
	@SuppressWarnings("nls")
	public static List<Teacher> createTeachers() {
		Dean[] ds = new Dean[5];
		for (int i = 0; i < ds.length; i++) {
			ds[i] = new Dean(i + 1, "d" + (i + 1));
		}

		Student[] ss = new Student[10];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = new Student(i + 1, "s" + (i + 1));
		}

		Course[] cs = new Course[5];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = new Course(i + 1, "c" + (i + 1));
		}

		List<Teacher> result = new ArrayList<Teacher>();
		Teacher t = new Teacher(1, "t1");
		t.setDean(ds[0]);
		t.addStudent(ss[0]);
		t.addStudent(ss[1]);
		ss[1].addCourse(cs[0]);
		ss[1].addCourse(cs[1]);
		result.add(t);

		t = new Teacher(2, "t2");
		t.setDean(ds[1]);
		t.addStudent(ss[2]);
		ss[2].addCourse(cs[1]);
		t.addStudent(ss[3]);
		result.add(t);

		t = new Teacher(3, "t3");
		t.setDean(ds[1]);
		t.addStudent(ss[3]);
		t.addStudent(ss[4]);
		ss[4].addCourse(cs[2]);
		ss[4].addCourse(cs[3]);
		ss[4].addCourse(cs[4]);
		result.add(t);

		t = new Teacher(4, "t4");
		result.add(t);

		return result;
	}

	public static List<TeacherStudent> getTeacherStudentCompound(List<Teacher> ts) {
		List<TeacherStudent> result = new ArrayList<TeacherStudent>();
		for (Teacher t : ts) {
			result.addAll(t.getTeacherStudentCompound());
		}
		return result;
	}

	public static List<TeacherStudentCourse> getTeacherStudentCourseCompound(List<Teacher> ts) {
		List<TeacherStudentCourse> result = new ArrayList<TeacherStudentCourse>();
		for (Teacher t : ts) {
			result.addAll(t.getTeacherStudentCourseCompound());
		}
		return result;
	}

}
