/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.MapRuleBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.MapHandleProvider;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;

public class MapDescriptorProvider extends MapHandleProvider implements PreviewPropertyDescriptorProvider {

	public MapDescriptorProvider() {
		super();
	}

	public MapDescriptorProvider(int expressionType) {
		super(expressionType);
	}

	class MapLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return MapDescriptorProvider.this.getColumnText(element, 1);
		}

	}

	class MapContentProvider implements IStructuredContentProvider {

		private IModelEventProcessor listener;

		public MapContentProvider(IModelEventProcessor listener) {
			this.listener = listener;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] elements = MapDescriptorProvider.this.getElements(inputElement);

			deRegisterEventManager();
			registerEventManager();

			return elements;
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
			deRegisterEventManager();
		}

		protected void deRegisterEventManager() {
			if (UIUtil.getModelEventManager() != null) {
				UIUtil.getModelEventManager().removeModelEventProcessor(listener);
			}
		}

		/**
		 * Registers model change listener to DE elements.
		 */
		protected void registerEventManager() {
			if (UIUtil.getModelEventManager() != null) {
				UIUtil.getModelEventManager().addModelEventProcessor(listener);
			}
		}

	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		MapRuleHandle handle = (MapRuleHandle) element;

		switch (columnIndex) {
		case 0:
			String pv = handle.getDisplay();

			return pv == null ? "" : pv; //$NON-NLS-1$

		case 1:
			// String exp = resolveNull( getTestExpression( ) )
			StringBuilder exp = new StringBuilder().append(resolveNull(handle.getTestExpression())).append(" " //$NON-NLS-1$
			).append(MapRuleBuilder.getNameForOperator(handle.getOperator()));

			int vv = MapRuleBuilder.determineValueVisible(handle.getOperator());

			if (vv == 1) {
				exp.append(" ").append(resolveNull(handle.getValue1())); //$NON-NLS-1$
			} else if (vv == 2) {
				exp.append(" " //$NON-NLS-1$
				).append(resolveNull(handle.getValue1())).append(" , " //$NON-NLS-1$
				).append(resolveNull(handle.getValue2()));
			} else if (vv == 3) {
				exp.append(" "); //$NON-NLS-1$
				int count = handle.getValue1List().size();
				for (int i = 0; i < count; i++) {
					if (i == 0) {
						exp.append(handle.getValue1List().get(i).toString());
					} else {
						exp.append("; ").append(handle.getValue1List().get(i).toString()); //$NON-NLS-1$
					}
				}
			}

			return exp.toString();

		default:
			return ""; //$NON-NLS-1$
		}
	}

	private String resolveNull(String src) {
		if (src == null) {
			return ""; //$NON-NLS-1$
		}

		return src;
	}

	@Override
	public boolean doSwapItem(int pos, int direction) throws PropertyValueException {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.MAP_RULES_PROP);

		if (direction < 0) {
			phandle.moveItem(pos, pos - 1);
		} else {
			/**
			 * Original code: phandle.moveItem( pos, pos + 1 );
			 *
			 * Changes due to model api changes. since property handle now treats moving
			 * from 0-0, 0-1 as the same.
			 */
			phandle.moveItem(pos, pos + 1);
		}

		return true;
	}

	@Override
	public IStructuredContentProvider getContentProvider(IModelEventProcessor listener) {
		return new MapContentProvider(listener);
	}

	@Override
	public LabelProvider getLabelProvider() {
		return new MapLabelProvider();
	}

	private static final MapRuleHandle[] EMPTY = {};

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof List) {
			if (((List) inputElement).size() > 0) {
				inputElement = ((List) inputElement).get(0);
			} else {
				inputElement = null;
			}
		}

		if (inputElement instanceof DesignElementHandle) {
			elementHandle = (DesignElementHandle) inputElement;

			PropertyHandle mapRules = elementHandle.getPropertyHandle(StyleHandle.MAP_RULES_PROP);

			ArrayList list = new ArrayList();

			for (Iterator itr = mapRules.iterator(); itr.hasNext();) {
				Object o = itr.next();

				list.add(o);
			}

			return (MapRuleHandle[]) list.toArray(new MapRuleHandle[0]);
		}

		return EMPTY;
	}

	@Override
	public boolean doDeleteItem(int pos) throws PropertyValueException {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.MAP_RULES_PROP);

		phandle.removeItem(pos);
		try {
			if (phandle.getListValue() == null || phandle.getListValue().size() == 0) {
				elementHandle.setProperty(StyleHandle.MAP_RULES_PROP, null);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		return true;
	}

	@Override
	public MapRuleHandle doAddItem(MapRule rule, int pos) {
		PropertyHandle phandle = elementHandle.getPropertyHandle(StyleHandle.MAP_RULES_PROP);

		try {
			phandle.addItem(rule);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}

		StructureHandle handle = rule.getHandle(phandle, pos);

		return (MapRuleHandle) handle;
	}

	@Override
	public boolean edit(Object input, int handleCount) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.editMapRule")); //$NON-NLS-1$

			MapRuleBuilder builder = new MapRuleBuilder(UIUtil.getDefaultShell(), MapRuleBuilder.DLG_TITLE_EDIT, this);

			MapRuleHandle handle = (MapRuleHandle) input;

			builder.updateHandle(handle, handleCount);

			builder.setDesignHandle(getDesignElementHandle());

			DesignElementHandle reportElement = getDesignElementHandle();
			while (reportElement instanceof RowHandle || reportElement instanceof ColumnHandle
					|| reportElement instanceof CellHandle) {
				DesignElementHandle designElement = reportElement.getContainer();
				if (designElement instanceof ReportItemHandle) {
					reportElement = (ReportItemHandle) designElement;
				} else if (designElement instanceof GroupHandle) {
					reportElement = (ReportItemHandle) ((GroupHandle) designElement).getContainer();
				} else {
					reportElement = designElement;
				}
				if (reportElement == null) {
					break;
				}
			}

			if (reportElement instanceof ReportItemHandle) {
				builder.setReportElement((ReportItemHandle) reportElement);
			} else if (reportElement instanceof GroupHandle) {
				builder.setReportElement((ReportItemHandle) ((GroupHandle) reportElement).getContainer());
			}

			if (builder.open() == Window.OK) {
				result = true;
			}
			stack.commit();

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean add(int handleCount) {
		boolean result = false;

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.addMapRule")); //$NON-NLS-1$

			Dialog dialog = createAddDialog(handleCount);

			if (dialog.open() == Window.OK) {
				result = true;
			}

			stack.commit();

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	protected MapRuleBuilder createAddDialog(int handleCount) {
		MapRuleBuilder builder = new MapRuleBuilder(UIUtil.getDefaultShell(), MapRuleBuilder.DLG_TITLE_NEW, // $NON-NLS-1$
				this);

		builder.updateHandle(null, handleCount);

		builder.setDesignHandle(getDesignElementHandle());

		DesignElementHandle reportElement = getDesignElementHandle();
		while (reportElement instanceof RowHandle || reportElement instanceof ColumnHandle
				|| reportElement instanceof CellHandle) {
			DesignElementHandle designElement = reportElement.getContainer();
			if (designElement instanceof ReportItemHandle) {
				reportElement = (ReportItemHandle) designElement;
			} else if (designElement instanceof GroupHandle) {
				reportElement = (ReportItemHandle) ((GroupHandle) designElement).getContainer();
			} else {
				reportElement = designElement;
			}
			if (reportElement == null) {
				break;
			}
		}
		if (reportElement instanceof ReportItemHandle) {
			builder.setReportElement((ReportItemHandle) reportElement);
		} else if (reportElement instanceof GroupHandle) {
			builder.setReportElement((ReportItemHandle) ((GroupHandle) reportElement).getContainer());
		}

		return builder;
	}

	@Override
	public boolean delete(int index) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.deleteMapRule")); //$NON-NLS-1$

			doDeleteItem(index);

			stack.commit();

			result = true;

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean moveUp(int index) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.moveUpMapRule")); //$NON-NLS-1$

			doSwapItem(index, -1);

			stack.commit();

			result = true;

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	@Override
	public boolean moveDown(int index) {

		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.moveDownRule")); //$NON-NLS-1$

			doSwapItem(index, 1);

			stack.commit();

			result = true;

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	protected Object input;

	@Override
	public void setInput(Object input) {
		this.input = input;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("MapPage.label.mapList"); //$NON-NLS-1$
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

	@Override
	public String getText(int key) {
		switch (key) {
		case 0:
			return Messages.getString("MapPage.label.mapList"); //$NON-NLS-1$
		case 1:
			return Messages.getString("MapPage.label.add"); //$NON-NLS-1$
		case 2:
			return Messages.getString("MapPage.label.delete"); //$NON-NLS-1$
		case 3:
			return Messages.getString("FormPage.Button.Up"); //$NON-NLS-1$
		case 4:
			return Messages.getString("MapPage.toolTipText.moveUp"); //$NON-NLS-1$
		case 5:
			return Messages.getString("FormPage.Button.Down"); //$NON-NLS-1$
		case 6:
			return Messages.getString("MapPage.toolTipText.moveDown"); //$NON-NLS-1$
		case 7:
			return Messages.getString("MapPage.label.displayValue"); //$NON-NLS-1$
		case 8:
			return Messages.getString("MapPage.label.condition"); //$NON-NLS-1$
		case 9:
			return Messages.getString(""); //$NON-NLS-1$
		case 10:
			return Messages.getString("MapPage.label.duplicate"); //$NON-NLS-1$
		case 11:
			return Messages.getString("MapPage.toolTipText.duplicate"); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	public String getDisplayText(Object handle) {
		return ((MapRuleHandle) handle).getDisplay();
	}

	private boolean canReset = false;

	@Override
	public boolean canReset() {
		return canReset;
	}

	public void enableReset(boolean canReset) {
		this.canReset = canReset;
	}

	@Override
	public void reset() throws SemanticException {
		if (canReset()) {
			save(null);
		}
	}

	@Override
	public boolean duplicate(int pos) {
		boolean result = false;

		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.Duplicate")); //$NON-NLS-1$
			PropertyHandle phandle = getDesignElementHandle().getPropertyHandle(StyleHandle.MAP_RULES_PROP);
			MapRule rule = (MapRule) phandle.getListValue().get(pos);
			phandle.addItem(rule.copy());

			stack.commit();
			result = true;
		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}
}
