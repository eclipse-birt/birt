/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the ducplicat group name in one table with data set.
 *
 * <h3>Rule</h3> The rule is that one listing element with data set doesn't
 * allow duplicate group name to appear in this element and its content listing
 * element without data set. But if the content listing element has data set,
 * the group name can be duplicate with that in container.
 *
 * <h3>Applicability</h3> This validator is only applied to
 * <code>ListingElement</code>.
 *
 */

public class GroupNameValidator extends AbstractElementValidator {

	private static GroupNameValidator instance = new GroupNameValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static GroupNameValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the group with the given name can be added into the given
	 * listing element.
	 *
	 * @param element   List/Table element
	 * @param groupName name of the group to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForAddingGroup(ListingHandle element, String groupName) {
		List<GroupElement> groupList = Collections.emptyList();

		// Collect all groups in this element.

		ListingElement targetElement = (ListingElement) element.getElement();
		if (targetElement == null) {
			targetElement = (ListingElement) element.getElement();
			groupList = getGroups(element.getModule(), (ListingElement) element.getElement());
		} else {
			groupList = getGroupsWithContents(element.getModule(), targetElement);
		}

		List<SemanticException> list = new ArrayList<>();

		// Check whether the given group name is in the group list.

		if (isDuplicateGroupName(element.getModule(), groupList, groupName)) {
			list.add(new NameException(targetElement, groupName, NameException.DESIGN_EXCEPTION_DUPLICATE));

			return list;
		}

		return list;
	}

	/**
	 * Validates whether the group can be renamed to the given name.
	 *
	 * @param element   List/Table element
	 * @param group     the group to rename
	 * @param groupName name of the group to add
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validateForRenamingGroup(ListingHandle element, GroupHandle group,
			String groupName) {
		if (group.getName() == groupName || (groupName != null && groupName.equals(group.getName()))) {
			return Collections.emptyList();
		}

		ListingElement targetElement = (ListingElement) element.getElement();
		if (targetElement == null) {
			return Collections.emptyList();
		}

		// Collect all group name in this element.

		List<GroupElement> groupList = getGroupsWithContents(element.getModule(), targetElement);

		List<SemanticException> list = new ArrayList<>();

		// Check whether the given group name is in the group name list.

		if (isDuplicateGroupName(element.getModule(), groupList, groupName)) {
			list.add(new NameException(targetElement, groupName, NameException.DESIGN_EXCEPTION_DUPLICATE));

			return list;
		}

		return list;
	}

	/**
	 * Validates whether the given element contains the duplicate group name. This
	 * check is applied to all listing element without data set.
	 *
	 * @param module  the module
	 * @param element the listing element to check
	 *
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof ListingElement)) {
			return Collections.emptyList();
		}

		return doValidate(module, (ListingElement) element);
	}

	private List<SemanticException> doValidate(Module module, ListingElement toValidate) {
		List<SemanticException> list = new ArrayList<>();

		// Collect all group name in this element.

		List<GroupElement> groupList = getGroupsWithContents(module, toValidate);

		// Check whether the duplicate group name exists in the group name list.

		Set<String> duplicateNames = null;
		int size = groupList.size();
		for (int i = 0; i < size - 1; i++) {
			GroupElement group1 = groupList.get(i);
			String groupName1 = group1.getStringProperty(module, IGroupElementModel.GROUP_NAME_PROP);
			assert groupName1 != null;

			// Let's see the case: 1, 3, 5, 1, 2, 1
			// We think the second "1" and third "1" duplicate the first "1",
			// this case has just two errors, instead of three. So the duplicate
			// name will be not checked again.

			if (duplicateNames != null && duplicateNames.contains(groupName1)) {
				continue;
			}

			for (int j = i + 1; j < size; j++) {
				GroupElement group2 = groupList.get(j);
				String groupName2 = group2.getStringProperty(module, IGroupElementModel.GROUP_NAME_PROP);
				assert groupName2 != null;

				if (groupName1.equalsIgnoreCase(groupName2)) {
					if (duplicateNames == null) {
						duplicateNames = new HashSet<>();
					}

					duplicateNames.add(groupName1);

					list.add(new SemanticError(group2.getContainer(), new String[] { groupName1 },
							SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME));
				}
			}
		}

		return list;
	}

	/**
	 * Validates whether the given element contains the duplicate group name. This
	 * check is applied to all listing element without data set.
	 *
	 * @param element the handle of the listing element to check
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validate(ListingHandle element) {
		return validate(element.getModule(), element.getElement());
	}

	/**
	 * Gets the list of group names in the given listing element.
	 *
	 * @param module  the module
	 * @param element the listing element from which the group names are retrieved.
	 *
	 * @return list of group names.
	 */

	private List<GroupElement> getGroups(Module module, ListingElement element) {
		List<GroupElement> list = new ArrayList<>();

		Iterator<DesignElement> iter = element.getGroups().iterator();
		while (iter.hasNext()) {
			GroupElement group = (GroupElement) iter.next();

			String groupName = group.getStringProperty(module, IGroupElementModel.GROUP_NAME_PROP);
			if (!StringUtil.isBlank(groupName)) {
				list.add(group);
			}
		}

		return list;
	}

	/**
	 * Gets the list of group names in the given element and its contents listing
	 * element. The group names in the listing element with data set are not in this
	 * list.
	 *
	 * @param module  the module
	 * @param element the listing element from which the group names are retrieved.
	 *
	 * @return list of group names.
	 */

	private List<GroupElement> getGroupsWithContents(Module module, DesignElement element) {
		List<GroupElement> list = new ArrayList<>();

		// Get group names from the given element.

		if (element instanceof ListingElement) {
			list.addAll(getGroups(module, (ListingElement) element));
		}

		return list;
	}

	/**
	 * Checks whether the given group name duplicates one of the list.
	 *
	 * @param module        the root module of the element to validate
	 * @param groupNameList the group name list
	 * @param groupName     the group name to check
	 * @return <code>true</code> if the given group name duplicates. Otherwise,
	 *         return <code>false</code>.
	 */

	private boolean isDuplicateGroupName(Module module, List<GroupElement> groupNameList, String groupName) {
		Iterator<GroupElement> iter = groupNameList.iterator();
		while (iter.hasNext()) {
			GroupElement group = iter.next();
			String tmpName = group.getStringProperty(module, IGroupElementModel.GROUP_NAME_PROP);
			assert tmpName != null;

			if (tmpName.equalsIgnoreCase(groupName)) {
				return true;
			}
		}

		return false;
	}
}
