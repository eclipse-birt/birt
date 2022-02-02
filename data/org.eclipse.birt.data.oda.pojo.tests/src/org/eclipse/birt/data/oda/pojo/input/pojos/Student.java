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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class Student extends WithIdAndName {
	private boolean sex;
	private int age;
	private String stateCode;
	private List<Course> courses = new ArrayList<Course>();

	public Student(int id, String name) {
		super(id, name);
	}

	public void addCourse(Course c) {
		courses.add(c);
	}

	public List<Course> getCourses() {
		return courses;
	}

	public Teacher getTeacher(int courseId) {
		return new Teacher(50, "t1"); //$NON-NLS-1$
	}

	/**
	 * @return the sex
	 */
	public boolean isSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(boolean sex) {
		this.sex = sex;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the stateCode
	 */
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCode the stateCode to set
	 */
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

}
