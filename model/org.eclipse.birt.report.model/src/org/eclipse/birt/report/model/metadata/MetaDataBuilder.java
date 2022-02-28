/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.metadata.validators.IValueValidator;

public class MetaDataBuilder {

	protected MetaDataDictionary dictionary = MetaDataDictionary.getInstance();

	public Choice createChoice() {
		return new Choice();
	}

	public ChoiceSet createChoiceSet() {
		return new ChoiceSet();
	}

	public void addChoiceSet(ChoiceSet choiceSet) throws MetaDataException {
		dictionary.addChoiceSet(choiceSet);
	}

	public ElementDefn createElementDefn() {
		return new ElementDefn();
	}

	public void addElementDefn(ElementDefn defn) throws MetaDataException {
		dictionary.addElementDefn(defn);
	}

	public SystemPropertyDefn createPropertyDefn() {
		return new SystemPropertyDefn();
	}

	public void addPropertyDefn(ElementDefn elementDefn, PropertyDefn propDefn) throws MetaDataException {
		elementDefn.addProperty(propDefn);
	}

	public ClassInfo createClassInfo() {
		return new ClassInfo();
	}

	public void addClassInfo(ClassInfo classInfo) throws MetaDataException {
		dictionary.addClass(classInfo);
	}

	public ArgumentInfo createArgumentInfo() {
		return new ArgumentInfo();
	}

	public MethodInfo createMethodInfo(boolean isConstructor) {
		return new MethodInfo(isConstructor);
	}

	public void addMethodInfo(ClassInfo classInfo, MethodInfo methodInfo) throws MetaDataException {
		if (methodInfo.isConstructor()) {
			if (classInfo.getConstructor() == null) {
				classInfo.setConstructor(methodInfo);
			}
		} else if (classInfo.findMethod(methodInfo.getName()) == null) {
			classInfo.addMethod(methodInfo);
		}
	}

	public MemberInfo createMemberInfo() {
		return new MemberInfo();
	}

	public void addMemberInfo(ClassInfo classInfo, MemberInfo memberInfo) throws MetaDataException {
		classInfo.addMemberDefn(memberInfo);
	}

	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	public void addValueValidator(IValueValidator validator) throws MetaDataException {
		dictionary.addValueValidator(validator);
	}
}
