/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.query.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.olap.OLAPException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * AggregationRegisterTable class is to search measure index based on its
 * measure name
 *
 */
public class AggregationRegisterTable {
	private CalculatedMember[] membersFromQuery;

	// contain members from query and members from cube operation
	private List<CalculatedMember> allMembers = new ArrayList<>();

	/**
	 *
	 * @param members
	 */
	AggregationRegisterTable(CalculatedMember[] members) {
		membersFromQuery = members;
		allMembers.addAll(Arrays.asList(membersFromQuery));
	}

	/**
	 *
	 * @param index
	 * @return
	 * @throws OLAPException
	 */
	public String getAggrName(int index) throws DataException {
		if (index >= this.allMembers.size() || index < 0) {
			throw new DataException(ResourceConstants.MEASURE_NAME_NOT_FOUND);
		}
		return this.allMembers.get(index).getCubeAggrDefn().getName();
	}

	/**
	 *
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public int getAggregationResultID(String name) throws DataException {
		int rsID = -1;
		for (CalculatedMember member : allMembers) {
			if (member.getCubeAggrDefn().getName().equals(name)) {
				rsID = member.getRsID();
				break;
			}
		}
		if (rsID == -1) {
			throw new DataException(ResourceConstants.CANNOT_GET_MEASURE_VALUE, new Object[] { name });
		} else {
			return rsID;
		}
	}

	/**
	 *
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public int getAggregationIndex(int rsID, String name) throws DataException {
		int index = 0;
		if (rsID >= 0) {

			for (CalculatedMember member : allMembers) {
				if (member.getCubeAggrDefn().getName().equals(name)) {
					break;
				}
				if (member.getRsID() == rsID && !member.getCubeAggrDefn().getName().equals(name)) {
					index++;
				}
			}
		}
		return index;
	}

	/**
	 *
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public CalculatedMember getCalculatedMember(String name) throws DataException {
		for (CalculatedMember member : allMembers) {
			if (member.getCubeAggrDefn().getName().equals(name)) {
				return member;
			}
		}
		throw new DataException(ResourceConstants.CANNOT_GET_MEASURE_VALUE, new Object[] { name });
	}

	/**
	 *
	 * @return
	 */
	public CalculatedMember[] getCalculatedMembers() {
		return this.allMembers.toArray(new CalculatedMember[0]);
	}

	/**
	 * get the starting index based on existing calculated member.
	 */
	public int getBasedRsIndex() {
		int rsID = -1;
		for (CalculatedMember member : allMembers) {
			if (member.getRsID() > rsID) {
				rsID = member.getRsID();
			}
		}
		return rsID;
	}

	public void addCalculatedMembersFromCubeOperation(CalculatedMember[] newMembers) {
		if (newMembers != null) {
			this.allMembers.addAll(Arrays.asList(newMembers));
		}
	}

	public CalculatedMember[] getCalculatedMembersFromQuery() {
		return membersFromQuery;
	}
}
