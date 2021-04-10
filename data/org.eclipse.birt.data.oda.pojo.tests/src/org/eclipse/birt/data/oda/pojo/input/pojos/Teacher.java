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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class Teacher extends WithIdAndName {

	public int age;
	private Dean dean;

	private List<Student> students = new ArrayList<Student>();
	private List<Course> courses = new ArrayList<Course>();

	public Teacher(int id, String name) {
		super(id, name);
		age = 30;
	}

	/**
	 * @return the dean
	 */
	public Dean getDean() {
		return dean;
	}

	/**
	 * @param dean the dean to set
	 */
	public void setDean(Dean dean) {
		this.dean = dean;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	public void addStudent(Student s) {
		students.add(s);
	}

	@SuppressWarnings("nls")
	public Student getStudentById(int id) {
		Student s = new Student(id, this.getName() + "_s" + id);
		s.setAge(age);
		s.setSex(false);
		s.setStateCode("state");
		return s;
	}

	public Student[] getStudents() {
		return students.toArray(new Student[0]);
	}

	public List<TeacherStudent> getTeacherStudentCompound() {
		List<TeacherStudent> result = new ArrayList<TeacherStudent>();
		if (students.size() == 0) {
			result.add(new TeacherStudent(this, null));
			return result;
		}
		for (Student s : students) {
			result.add(new TeacherStudent(this, s));
		}
		return result;
	}

	public List<TeacherStudentCourse> getTeacherStudentCourseCompound() {
		List<TeacherStudentCourse> result = new ArrayList<TeacherStudentCourse>();
		if (students.size() == 0) {
			result.add(new TeacherStudentCourse(this, null, null));
			return result;
		}
		for (Student s : students) {
			if (s.getCourses().size() == 0) {
				result.add(new TeacherStudentCourse(this, s, null));
			} else {
				for (Course c : s.getCourses()) {
					result.add(new TeacherStudentCourse(this, s, c));
				}
			}
		}
		return result;
	}

	public void addCourse(Course c) {
		courses.add(c);
	}

	public List<Course> getCourses() {
		return courses;
	}

	@SuppressWarnings("nls")
	public Student[] getStudents(int age, boolean sex, String stateCode) {
		List<Student> result = new ArrayList<Student>();
		Student s;
		for (int i = 0; i < 10; i++) {
			s = new Student(i + 1, this.getName() + "_s" + (i + 1));
			s.setAge(age);
			s.setSex(sex);
			s.setStateCode(stateCode);
			result.add(s);
		}
		return result.toArray(new Student[0]);
	}

	public Object testObjectParam(ThreadGroup tg) {
		return tg;
	}

	public String getStudentName(Student s) {
		return "s1"; //$NON-NLS-1$
	}
}
