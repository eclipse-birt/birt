/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.ColumnEditPart;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.JoinConditionEditPart;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;

public class DeleteJoinAction extends AbstractViewAction {

	JoinConditionEditPart editPart = null;

	/**
	 * @param root           : The Query model object
	 * @param editPart       : The edit part currently selected
	 * @param selectedObject : The selected Object
	 */
	public DeleteJoinAction(JoinConditionEditPart editPart, Object selectedObject) {
		super(selectedObject);
		this.editPart = editPart;
	}

	public void run() {
		if (this.editPart == null)
			return;
		try {
			TabularCubeHandle cube = ((ColumnEditPart) editPart.getTarget()).getCube();
			Iterator iter = cube.joinConditionsIterator();
			while (iter.hasNext()) {
				DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();

				Iterator conditionIter = condition.getJoinConditions().iterator();

				List<DimensionJoinCondition> conditionList = new ArrayList<DimensionJoinCondition>();
				while (conditionIter.hasNext()) {
					DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIter.next();
					if (joinCondition.equals(editPart.getModel())) {
						conditionList.add((DimensionJoinCondition) joinCondition.getStructure());
					}
				}
				for (int i = 0; i < conditionList.size(); i++) {
					condition.removeJoinCondition(conditionList.get(i));
				}
			}
			editPart.setFocus(false);
			editPart.setSelected(0);
			editPart.setSource(null);
			editPart.setTarget(null);
			editPart.refresh();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}

	}

}
