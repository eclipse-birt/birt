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

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.property.AbstractTitlePropertyDialog;
import org.eclipse.birt.report.designer.data.ui.property.PropertyNode;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CubeBuilder extends AbstractTitlePropertyDialog implements IPreferencePageContainer {

	// public static final String MEASURESPAGE =
	// "org.eclipse.birt.datasource.editor.cubebuilder.measurespage";
	public static final String GROUPPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.grouppage"; //$NON-NLS-1$
	public static final String DATASETSELECTIONPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.datasetselectionpage"; //$NON-NLS-1$
	public static final String LINKGROUPSPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.linkgroupspage"; //$NON-NLS-1$
	private TabularCubeHandle input;

	public CubeBuilder(Shell parentShell, TabularCubeHandle input) {
		super(parentShell, input);
		addCommonPage(input);
		this.input = input;
	}

	protected boolean needRememberLastSize() {
		return true;
	}

	protected Control createDialogArea(Composite parent) {
		// UIUtil.bindHelp( parent, IHelpContextIds.CUBE_BUILDER_ID );
		return super.createDialogArea(parent);
	}

	private void addCommonPage(TabularCubeHandle model) {
		datasetNode = new PropertyNode(DATASETSELECTIONPAGE, Messages.getString("DatasetPage.Title"), //$NON-NLS-1$
				null, new DatasetSelectionPage(this, model));
		groupsNode = new PropertyNode(GROUPPAGE, Messages.getString("GroupsPage.Title"), //$NON-NLS-1$
				null, new GroupsPage(this, model));
		linkGroupNode = new PropertyNode(LINKGROUPSPAGE, Messages.getString("LinkGroupsPage.Title"), //$NON-NLS-1$
				null, new LinkGroupsPage(this, model));
		addNodeTo("/", datasetNode); //$NON-NLS-1$
		addNodeTo("/", groupsNode); //$NON-NLS-1$
		addNodeTo("/", linkGroupNode); //$NON-NLS-1$

		Object[] adapters = ElementAdapterManager.getAdapters(model, ICubePageNodeGenerator.class);
		if (adapters != null) {
			for (int i = 0; i < adapters.length; i++) {
				if (adapters[i] instanceof ICubePageNodeGenerator) {
					((ICubePageNodeGenerator) adapters[i]).createPropertyNode(this, model);
				}
			}
		}
	}

	private String showNodeId;

	public void showPage(String nodeId) {
		this.showNodeId = nodeId;
	}

	public boolean performCancel() {
		return true;
	}

	public boolean performOk() {
		if (checkCubeLink()) {
			clearInvalidGroup();
			clearInvalidJoin();
			return true;
		} else
			return false;
	}

	private void clearInvalidJoin() {
		Iterator iter = input.joinConditionsIterator();
		List<DimensionConditionHandle> invaildConditionList = new ArrayList<DimensionConditionHandle>();
		while (iter.hasNext()) {
			DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();
			TabularHierarchyHandle conditionHierarchy = (TabularHierarchyHandle) condition.getHierarchy();
			if (conditionHierarchy == null) {
				invaildConditionList.add(condition);
			} else {
				if (condition.getJoinConditions() != null) {
					Iterator iter1 = condition.getJoinConditions().iterator();
					List<DimensionJoinConditionHandle> invaildJoinList = new ArrayList<DimensionJoinConditionHandle>();
					while (iter1.hasNext()) {
						DimensionJoinConditionHandle join = (DimensionJoinConditionHandle) iter1.next();
						String primaryKey = join.getCubeKey();
						String hierarchyKey = join.getHierarchyKey();

						if (OlapUtil.getDataField(input.getDataSet(), primaryKey) == null) {
							invaildJoinList.add(join);
						} else if (OlapUtil.getDataField(conditionHierarchy.getDataSet(), hierarchyKey) == null) {
							invaildJoinList.add(join);
						}
					}
					if (!invaildJoinList.isEmpty()) {
						for (int i = 0; i < invaildJoinList.size(); i++) {
							try {
								invaildJoinList.get(i).drop();
							} catch (PropertyValueException e) {
								ExceptionHandler.handle(e);
							}
						}
					}
				}

				if (condition.getJoinConditions() == null || !condition.getJoinConditions().iterator().hasNext()) {
					invaildConditionList.add(condition);
				}
			}
		}
		if (!invaildConditionList.isEmpty()) {
			for (int i = 0; i < invaildConditionList.size(); i++) {
				try {
					invaildConditionList.get(i).drop();
				} catch (PropertyValueException e) {
					ExceptionHandler.handle(e);
				}
			}
		}
	}

	private void clearInvalidGroup() {
		if (input != null) {
			DimensionHandle[] dimensions = (DimensionHandle[]) input.getContents(ICubeModel.DIMENSIONS_PROP)
					.toArray(new DimensionHandle[0]);
			for (int i = 0; i < dimensions.length; i++) {
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimensions[i].getDefaultHierarchy();
				if (hierarchy == null || hierarchy.getLevelCount() == 0) {
					try {
						dimensions[i].drop();
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				}
			}
		}
	}

	private boolean checkCubeLink() {
		List childList = new ArrayList();
		if (input != null) {
			DimensionHandle[] dimensions = (DimensionHandle[]) input.getContents(ICubeModel.DIMENSIONS_PROP)
					.toArray(new DimensionHandle[0]);
			for (int i = 0; i < dimensions.length; i++) {
				TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) dimensions[i].getDefaultHierarchy();
				if (hierarchy != null && hierarchy.getDataSet() != null && hierarchy.getDataSet() != input.getDataSet())
					childList.add(hierarchy);
			}
		}
		if (childList.size() == 0)
			return true;
		else {
			boolean flag = true;

			HashMap conditionMap = new HashMap();
			for (int i = 0; i < childList.size(); i++) {
				flag = true;
				HierarchyHandle hierarchy = (HierarchyHandle) childList.get(i);
				Iterator iter = input.joinConditionsIterator();
				while (iter.hasNext()) {
					DimensionConditionHandle condition = (DimensionConditionHandle) iter.next();
					HierarchyHandle conditionHierarchy = condition.getHierarchy();
					if (ModuleUtil.isEqualHierarchiesForJointCondition(conditionHierarchy, hierarchy)) {
						if (condition.getJoinConditions() != null) {
							Iterator iter1 = condition.getJoinConditions().iterator();
							while (iter1.hasNext()) {
								iter1.next();
								int number = conditionMap.containsKey(conditionHierarchy)
										? ((Integer) conditionMap.get(conditionHierarchy)).intValue()
										: 0;
								conditionMap.put(conditionHierarchy, ++number);
								flag = false;
							}
						}
					}
				}
				if (flag)
					break;
			}
			if (flag) {
				conditionMap.clear();

				String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL };

				MessageDialog d = new MessageDialog(getShell(), Messages.getString("MissLinkDialog.Title"), //$NON-NLS-1$
						null, Messages.getString("MissLinkDialog.Question"), //$NON-NLS-1$
						MessageDialog.WARNING, buttons, 0);
				int result = d.open();
				if (result == 1)
					return true;
				else {
					this.showSelectionPage(getLinkGroupNode());
					return false;
				}
			}

			conditionMap.clear();
			return true;
		}
	}

	protected Control createContents(Composite parent) {
		String title = Messages.getString("CubeBuilder.Title"); //$NON-NLS-1$
		getShell().setText(title);

		if (showNodeId != null) {
			setDefaultNode(showNodeId);
		}

		Control control = super.createContents(parent);
		return control;
	}

	private boolean okEnable = true;
	private PropertyNode datasetNode;
	private PropertyNode groupsNode;
	private PropertyNode linkGroupNode;

	public void setOKEnable(boolean okEnable) {
		this.okEnable = okEnable;
		if (getOkButton() != null)
			getOkButton().setEnabled(this.okEnable);
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		getOkButton().setEnabled(this.okEnable);
	}

	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		if (getOkButton() != null) {
			if (((CubeHandle) getModel()).getName() != null && !((CubeHandle) getModel()).getName().trim().equals("")) //$NON-NLS-1$
			{
				getOkButton().setEnabled(true);
			} else
				getOkButton().setEnabled(false);
		}
	}

	public IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateButtons() {
		// TODO Auto-generated method stub

	}

	public void updateMessage() {
		// TODO Auto-generated method stub

	}

	public void updateTitle() {
		// TODO Auto-generated method stub

	}

	protected Point getDefaultSize() {
		return new Point(820, 600);
	}

	public PropertyNode getLinkGroupNode() {
		return linkGroupNode;
	}

	public PropertyNode getDatasetNode() {
		return datasetNode;
	}

	public PropertyNode getGroupsNode() {
		return groupsNode;
	}

}
