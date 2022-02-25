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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.property.GroupPropertyHandleWrapper;
import org.eclipse.birt.report.designer.core.model.views.property.PropertySheetRootElement;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.AdvancePropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.memento.Memento;
import org.eclipse.birt.report.designer.internal.ui.views.memento.MementoElement;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.GroupPropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.Viewer;

/**
 *
 *
 */
public class AdvancePropertyDescriptorProvider extends AbstractDescriptorProvider {

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("AdvancePropertyDescriptorProvider.DisplayName"); //$NON-NLS-1$
	}

	@Override
	public Object load() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;

	}

	public boolean isEnable() {
		if (input == null || !DEUtil.getGroupElementHandle(DEUtil.getInputElements(input)).isSameType()) {
			return false;
		}
		return true;
	}

	public String getTitleDisplayName() {
		String displayName = null;
		DesignElementHandle element = (DesignElementHandle) DEUtil.getInputFirstElement(input);
		if (element != null) {
			displayName = getElementType();
		}

		if (!isEnable() || displayName == null || "".equals(displayName))//$NON-NLS-1$
		{
			displayName = Messages.getString("ReportPropertySheetPage.Root.Default.Title"); //$NON-NLS-1$
		}
		return displayName;
	}

	public final static int MODE_GROUPED = 0;
	public final static int MODE_ALPHABETIC = 1;
	public final static int MODE_LOCAL_ONLY = 2;

	public void selectViewMode(int modeIndex) {
		contentProvider.setViewMode(modeIndex);
	}

	public int getViewMode() {
		return contentProvider.getViewMode();
	}

	private AdvancedPropertyContentProvider contentProvider = new AdvancedPropertyContentProvider();
	private AdvancedPropertyValueLabelProvider valueLabelProvider = new AdvancedPropertyValueLabelProvider();
	private AdvancedPropertyNameLabelProvider nameLabelProvider = new AdvancedPropertyNameLabelProvider();

	public AdvancedPropertyContentProvider getContentProvier() {
		return contentProvider;
	}

	public AdvancedPropertyValueLabelProvider getValueLabelProvier() {
		return valueLabelProvider;
	}

	public AdvancedPropertyNameLabelProvider getNameLabelProvier() {
		return nameLabelProvider;
	}

	public boolean addNode(Memento element, MementoElement[] nodePath) {
		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			if (!memento.equals(nodePath[0])) {
				return false;
			}
			for (int i = 1; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null) {
					memento = child;
				} else {
					memento.addChild(nodePath[i]);
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public boolean removeNode(Memento element, MementoElement[] nodePath) {
		if (nodePath != null && nodePath.length > 0) {
			MementoElement memento = element.getMementoElement();
			if (!memento.equals(nodePath[0])) {
				return false;
			}
			for (int i = 1; i < nodePath.length; i++) {
				MementoElement child = getChild(memento, nodePath[i]);
				if (child != null) {
					memento = child;
				} else {
					return false;
				}
			}
			memento.getParent().removeChild(memento);
			return true;
		}
		return false;
	}

	private MementoElement getChild(MementoElement parent, MementoElement key) {
		MementoElement[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i].equals(key)) {
				return children[i];
			}
		}
		return null;
	}

	public MementoElement[] getNodePath(MementoElement node) {
		LinkedList pathList = new LinkedList();
		MementoElement memento = node;
		pathList.add(node);// add root
		while (memento.getChildren().length > 0) {
			pathList.add(memento.getChild(0));
			memento = (MementoElement) memento.getChild(0);
		}
		MementoElement[] paths = new MementoElement[pathList.size()];
		pathList.toArray(paths);
		return paths;
	}

	public String getElementType() {
		String displayName = ((DesignElementHandle) DEUtil.getInputFirstElement(input)).getDefn().getDisplayName();

		if (displayName == null || "".equals(displayName))//$NON-NLS-1$
		{
			displayName = ((DesignElementHandle) DEUtil.getInputFirstElement(input)).getDefn().getName();
		}

		return displayName;
	}

	public String getToolTipText(int mode) {
		switch (mode) {
		case MODE_GROUPED:
			return Messages.getString("AdvancePropertyDescriptorProvider.Tooltip.Group"); //$NON-NLS-1$
		case MODE_ALPHABETIC:
			return Messages.getString("AdvancePropertyDescriptorProvider.Tooltip.Alphabetic"); //$NON-NLS-1$
		case MODE_LOCAL_ONLY:
			return Messages.getString("AdvancePropertyDescriptorProvider.Tooltip.Local"); //$NON-NLS-1$
		}
		return "";//$NON-NLS-1$
	}

	class GroupSortingAction extends Action {

		private AdvancePropertyDescriptor control;

		GroupSortingAction(AdvancePropertyDescriptor control) {
			super(null, IAction.AS_CHECK_BOX);
			this.control = control;
			setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_GROUP_SORT));
			setToolTipText(AdvancePropertyDescriptorProvider.this
					.getToolTipText(AdvancePropertyDescriptorProvider.MODE_GROUPED));
		}

		@Override
		public void run() {
			control.updateSorting(MODE_GROUPED);
		}

		@Override
		public boolean isChecked() {
			return contentProvider.getViewMode() == MODE_GROUPED;
		}

		@Override
		public void setChecked(boolean check) {
			if (contentProvider.getViewMode() != MODE_GROUPED) {
				selectViewMode(MODE_GROUPED);
			}
			firePropertyChange(CHECKED, null, null);
		}
	}

	class AlphabeticSortingAction extends Action {

		private AdvancePropertyDescriptor control;

		AlphabeticSortingAction(AdvancePropertyDescriptor control) {
			super(null, IAction.AS_CHECK_BOX);
			this.control = control;
			setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_ALPHABETIC_SORT));
			setToolTipText(AdvancePropertyDescriptorProvider.this
					.getToolTipText(AdvancePropertyDescriptorProvider.MODE_ALPHABETIC));
		}

		@Override
		public void run() {
			control.updateSorting(MODE_ALPHABETIC);
		}

		@Override
		public boolean isChecked() {
			return contentProvider.getViewMode() == MODE_ALPHABETIC;
		}

		@Override
		public void setChecked(boolean check) {
			if (contentProvider.getViewMode() != MODE_ALPHABETIC) {
				selectViewMode(MODE_ALPHABETIC);
			}
			firePropertyChange(CHECKED, null, null);
		}
	}

	class LocalModelAction extends Action {

		private AdvancePropertyDescriptor control;

		LocalModelAction(AdvancePropertyDescriptor control) {
			super(null, IAction.AS_CHECK_BOX);
			this.control = control;
			setImageDescriptor(
					ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_LOCAL_PROPERTIES));
			setToolTipText(AdvancePropertyDescriptorProvider.this
					.getToolTipText(AdvancePropertyDescriptorProvider.MODE_LOCAL_ONLY));
		}

		@Override
		public void run() {
			control.updateSorting(MODE_LOCAL_ONLY);
		}

		@Override
		public boolean isChecked() {
			return contentProvider.getViewMode() == MODE_LOCAL_ONLY;
		}

		@Override
		public void setChecked(boolean check) {
			if (contentProvider.getViewMode() != MODE_LOCAL_ONLY) {
				selectViewMode(MODE_LOCAL_ONLY);
			}
			firePropertyChange(CHECKED, null, null);
		}
	}

	public Object getActions(AdvancePropertyDescriptor control) {
		return new Action[] { new GroupSortingAction(control), new AlphabeticSortingAction(control),
				new LocalModelAction(control) };
	}

}

class AdvancedPropertyNameLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	@Override
	public String getText(Object element) {
		String text = getStyledText(element).toString();
		return text;
	}

	@Override
	public StyledString getStyledText(Object element) {
		String value = null;
		if (element instanceof List) {
			GroupPropertyHandle property = ((GroupPropertyHandleWrapper) (((List) element).get(0))).getModel();
			value = property.getPropertyDefn().getGroupName();
		} else if (element instanceof PropertySheetRootElement) {
			value = ((PropertySheetRootElement) element).getDisplayName();
		} else {
			GroupPropertyHandle property = ((GroupPropertyHandleWrapper) element).getModel();
			value = property.getPropertyDefn().getDisplayName();
		}
		if (value == null) {
			value = ""; //$NON-NLS-1$
		}
		StyledString styledString = new StyledString();
		styledString.append(value);
		return styledString;
	}

}

class AdvancedPropertyValueLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

	private static final String PASSWORD_REPLACEMENT = "********";//$NON-NLS-1$

	@Override
	public String getText(Object element) {
		String text = getStyledText(element).toString();
		return text;
	}

	@Override
	public StyledString getStyledText(Object element) {
		String value = null;
		GroupPropertyHandle propertyHandle = null;
		if (element instanceof GroupPropertyHandleWrapper) {
			propertyHandle = ((GroupPropertyHandleWrapper) element).getModel();

			if (propertyHandle != null) {
				if (propertyHandle.getStringValue() != null) {
					if (propertyHandle.getPropertyDefn().isEncryptable()) {
						value = PASSWORD_REPLACEMENT;
					} else {
						value = propertyHandle.getDisplayValue();
					}
				}
			}
		}

		if (value == null) {
			if (showAuto(propertyHandle)) {
				value = Messages.getString("PropertyEditorFactory.Value.Auto"); //$NON-NLS-1$
			} else {
				value = ""; //$NON-NLS-1$
			}
		}

		StyledString styledString = new StyledString();
		styledString.append(value);
		if (propertyHandle != null && propertyHandle.getDisplayValue() != null
				&& propertyHandle.getLocalStringValue() == null) {
			styledString.append(" : " //$NON-NLS-1$
					+ Messages.getString("ReportPropertySheetPage.Value.Inherited"), //$NON-NLS-1$
					StyledString.DECORATIONS_STYLER);
		}
		return styledString;
	}

	private boolean showAuto(Object element) {
		if (element == null) {
			return false;
		}
		GroupPropertyHandleProvider handle = GroupPropertyHandleProvider.getInstance();

		// not editable property
		if (handle.isReadOnly(element)) {
			return false;
		}

		String[] values = getChoiceNames(element);

		if (handle.isBooleanProperty(element) || handle.isColorProperty(element) || handle.isDateTimeProperty(element)
				|| handle.isFontSizeProperty(element) || handle.isDimensionProperty(element)
				|| handle.isElementRefValue(element) || handle.isExpressionProperty(element)
				|| handle.isPassProperty(element) || handle.isBackgroundImageProperty(element)
				|| handle.isBackgroundImageProperty(element)) {
			return false;
		}

		if (values != null && values.length > 0) {
			if (!handle.isEditable(element)) {
				return true;
			} else {
				return false;
			}

		}

		return false;

	}

	private String[] getChoiceNames(Object o) {
		String[] values = null;

		if (o instanceof GroupPropertyHandle) {
			if (((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices() != null) {
				IChoice[] choices = ((GroupPropertyHandle) o).getPropertyDefn().getAllowedChoices().getChoices();
				if (choices.length > 0) {
					values = new String[choices.length];
					for (int i = 0; i < choices.length; i++) {
						// temp: displayname
						values[i] = choices[i].getName();
					}
				}
			}
		}
		if (values == null) {
			return new String[] {};
		}

		return values;
	}

}

class AdvancedPropertyContentProvider implements ITreeContentProvider {

	private static final String ROOT_DEFAUL_TITLE = Messages.getString("ReportPropertySheetPage.Root.Default.Title"); //$NON-NLS-1$

	private int viewMode = AdvancePropertyDescriptorProvider.MODE_GROUPED;

	public void setViewMode(int mode) {
		this.viewMode = mode;
	}

	public int getViewMode() {
		return this.viewMode;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof List) {
			return ((List) parentElement).toArray();
		}
		if (parentElement instanceof PropertySheetRootElement) {
			ArrayList items = new ArrayList();
			GroupElementHandle handle = (GroupElementHandle) ((PropertySheetRootElement) parentElement).getModel();

			if (viewMode == AdvancePropertyDescriptorProvider.MODE_GROUPED) {
				HashMap map = new HashMap();
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();
					IElementPropertyDefn defn = property.getPropertyDefn();
					if (defn.getGroupNameKey() == null) {
						items.add(new GroupPropertyHandleWrapper(property));
					} else {
						List group = (List) map.get(defn.getGroupNameKey());
						if (group == null) {
							group = new ArrayList();
							items.add(group);
							map.put(defn.getGroupNameKey(), group);
						}
						group.add(new GroupPropertyHandleWrapper(property));
					}
				}
			} else if (viewMode == AdvancePropertyDescriptorProvider.MODE_ALPHABETIC) {
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();

					items.add(new GroupPropertyHandleWrapper(property));
				}
			} else if (viewMode == AdvancePropertyDescriptorProvider.MODE_LOCAL_ONLY) {
				for (Iterator it = handle.visiblePropertyIterator(); it.hasNext();) {
					GroupPropertyHandle property = (GroupPropertyHandle) it.next();
					if (property != null && property.getLocalStringValue() != null) {
						items.add(new GroupPropertyHandleWrapper(property));
					}
				}
			}
			return items.toArray();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return ((element instanceof List && ((List) element).size() > 0)
				|| element instanceof PropertySheetRootElement);
	}

	PropertySheetRootElement[] roots = new PropertySheetRootElement[1];

	@Override
	public Object[] getElements(Object input) {
		GroupElementHandle inputElement = DEUtil.getGroupElementHandle(DEUtil.getInputElements(input));

		PropertySheetRootElement root = new PropertySheetRootElement(inputElement);

		String displayName = null;
		Object element = ((GroupElementHandle) inputElement).getElements().get(0);

		if (element instanceof DesignElementHandle) {
			displayName = ((DesignElementHandle) element).getDefn().getDisplayName();

			if (displayName == null || "".equals(displayName))//$NON-NLS-1$
			{
				displayName = ((DesignElementHandle) element).getDefn().getName();
			}
		}

		if (displayName == null || "".equals(displayName))//$NON-NLS-1$
		{
			displayName = ROOT_DEFAUL_TITLE;
		}
		root.setDisplayName(displayName);

		roots[0] = root;

		return roots;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
