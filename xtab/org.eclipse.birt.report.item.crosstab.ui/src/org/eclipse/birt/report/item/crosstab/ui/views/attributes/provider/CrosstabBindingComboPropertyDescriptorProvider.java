/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.section.CrosstabBindingComboSection;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.CCombo;

/**
 * 
 */
public class CrosstabBindingComboPropertyDescriptorProvider extends PropertyDescriptorProvider {

	private static final String NONE = Messages.getString("BindingPage.None"); //$NON-NLS-1$

	public CrosstabBindingComboPropertyDescriptorProvider(String property, String element) {
		super(property, element);
	}

	public List<CubeHandle> getItems() {
		List<CubeHandle> items = null;

		Object selecteObj = input;
		if (input instanceof List) {
			selecteObj = ((List) input).get(0);
		}

		ExtendedItemHandle handle = (ExtendedItemHandle) selecteObj;
		if (!handle.getExtensionName().equals("Crosstab")) //$NON-NLS-1$
		{
			return Collections.EMPTY_LIST;
		}

		if (IReportItemModel.CUBE_PROP.equals(getProperty())) {
			items = UIUtil.getVisibleCubeHandles(handle.getModuleHandle());
		}

		Collections.sort(items, new Comparator<CubeHandle>() {

			public int compare(CubeHandle o1, CubeHandle o2) {
				return o1.getQualifiedName().compareTo(o2.getQualifiedName());
			}
		});

		items.add(0, null);
		return items;
	}

	public String[] getItemNames() {
		List<CubeHandle> cubes = getItems();
		String[] items = new String[cubes.size()];

		for (int i = 0; i < cubes.size(); i++) {
			CubeHandle cube = cubes.get(i);
			if (cube == null) {
				items[i] = NONE;
			} else {
				items[i] = cube.getQualifiedName();
				if (getExtendedItemHandle().getModuleHandle().findCube(items[i]) != cube) {
					items[i] += Messages.getString("CrosstabBindingComboPropertyDescriptorProvider.Flag.DataModel"); //$NON-NLS-1$
				}
			}
		}
		return items;
	}

	public String getDisplayName() {
		if (IReportItemModel.CUBE_PROP.equals(getProperty())) {
			return Messages.getString("Element.ReportElement.Cube"); //$NON-NLS-1$
		} else {
			return super.getDisplayName();
		}
	}

	public void save(Object value) throws SemanticException {
		int ret = 0;
		// If choose binding Cube as None
		if (getCube() != null) {
			if (getCube().equals(value))
				return;
			MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("CrosstabDataBinding.title.ChangeCube"), //$NON-NLS-1$
					null, Messages.getString("CrosstabDataBinding.message.changeCube"), //$NON-NLS-1$
					MessageDialog.INFORMATION,
					new String[] {
							org.eclipse.birt.report.designer.nls.Messages.getString("AttributeView.dialg.Message.Yes"), //$NON-NLS-1$
							org.eclipse.birt.report.designer.nls.Messages.getString("AttributeView.dialg.Message.No"), //$NON-NLS-1$
							org.eclipse.birt.report.designer.nls.Messages
									.getString("AttributeView.dialg.Message.Cancel") }, //$NON-NLS-1$
					0);

			ret = prefDialog.open();
			switch (ret) {
			// Clear binding info
			case 0:
				resetCubeReference((CubeHandle) value, true);
				break;
			// Doesn't clear binding info
			case 1:
				resetCubeReference((CubeHandle) value, false);
				break;
			// Cancel.
			case 2:
				int index = getItems().indexOf(getCube());
				if (index > -1) {
					((CCombo) section.getComboControl().getControl()).select(index);
				} else {
					((CCombo) section.getComboControl().getControl()).deselectAll();
				}
				break;
			}
		} else {
			resetCubeReference((CubeHandle) value, false);
		}
		// super.save( value );
	}

	public Object load() {
		return getCube();
	}

	private CubeHandle getCube() {
		return getExtendedItemHandle().getCube();
	}

	private void resetCubeReference(CubeHandle value, boolean clearHistory) {
		try {
			startTrans("Reset Reference"); //$NON-NLS-1$
			CubeHandle cubeHandle = null;
			if (value != null) {
				cubeHandle = getExtendedItemHandle().getModuleHandle().findCube(value.getQualifiedName());
			}
			if (value == null) {
				getExtendedItemHandle().setCube(cubeHandle);
				new LinkedDataSetAdapter().setLinkedDataModel(getExtendedItemHandle(), null);
			} else {
				if (cubeHandle != value) {
					getExtendedItemHandle().setCube(null);
					new LinkedDataSetAdapter().setLinkedDataModel(getExtendedItemHandle(), value.getQualifiedName());
				} else {
					new LinkedDataSetAdapter().setLinkedDataModel(getExtendedItemHandle(), null);
					getExtendedItemHandle().setCube(cubeHandle);
				}
			}
			if (clearHistory) {
				getExtendedItemHandle().getColumnBindings().clearValue();
				getExtendedItemHandle().getPropertyHandle(ReportItemHandle.PARAM_BINDINGS_PROP).clearValue();
			}
			commit();
		} catch (SemanticException e) {
			rollback();
			ExceptionUtil.handle(e);
		}
		load();
	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private ExtendedItemHandle getExtendedItemHandle() {
		return (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
	}

	private void startTrans(String name) {
		getActionStack().startTrans(name);
	}

	private void commit() {
		getActionStack().commit();
	}

	private void rollback() {
		getActionStack().rollback();
	}

	private CrosstabBindingComboSection section;

	public void setCrosstabSimpleComboSection(CrosstabBindingComboSection section) {
		this.section = section;
	}
}
