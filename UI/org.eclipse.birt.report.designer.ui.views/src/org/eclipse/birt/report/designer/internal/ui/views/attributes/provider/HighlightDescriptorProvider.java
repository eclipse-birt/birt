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

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HighlightRuleBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.HighlightHandleProvider;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class HighlightDescriptorProvider extends HighlightHandleProvider implements PreviewPropertyDescriptorProvider {

	public HighlightDescriptorProvider() {
		super();
	}

	public HighlightDescriptorProvider(int expressionType) {
		super(expressionType);
	}

	class HighlightLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			return HighlightDescriptorProvider.this.getColumnText(element, 1);
		}

	}

	class HighlightContentProvider implements IStructuredContentProvider {

		private IModelEventProcessor listener;

		public HighlightContentProvider(IModelEventProcessor listener) {
			this.listener = listener;
		}

		@Override
		public Object[] getElements(Object inputElement) {
			Object[] elements = HighlightDescriptorProvider.this.getElements(inputElement);

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
		HighlightRuleHandle handle = (HighlightRuleHandle) element;

		switch (columnIndex) {
		case 0:
			return Messages.getString("HighlightHandleProvider.text.Preview") //$NON-NLS-1$
					+ DEUtil.getFontSize(handle.getFontSize().getStringValue()) + ")"; //$NON-NLS-1$

		case 1:
			// String exp = resolveNull( getTestExpression( ) )
			StringBuilder exp = new StringBuilder().append(resolveNull(handle.getTestExpression())).append(" " //$NON-NLS-1$
			).append(HighlightRuleBuilder.getNameForOperator(handle.getOperator()));

			int vv = HighlightRuleBuilder.determineValueVisible(handle.getOperator());

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
		PropertyHandle phandle = getDesignElementHandle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

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
		return new HighlightContentProvider(listener);
	}

	@Override
	public LabelProvider getLabelProvider() {
		return new HighlightLabelProvider();
	}

	private static final HighlightRuleHandle[] EMPTY = {};

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

			PropertyHandle highRules = elementHandle.getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

			ArrayList list = new ArrayList();

			for (Iterator itr = highRules.iterator(); itr.hasNext();) {
				Object o = itr.next();

				list.add(o);
			}

			return (HighlightRuleHandle[]) list.toArray(new HighlightRuleHandle[0]);
		}

		return EMPTY;
	}

	public static String getFontFamily(String fontFamily) {
		String destFontName = (String) DesignerConstants.familyMap.get(fontFamily);

		if (destFontName == null) {
			destFontName = fontFamily;
		}

		return destFontName;
	}

	/**
	 * Returns the style handle for current design element.
	 *
	 * @return
	 */
	@Override
	public StyleHandle getStyleHandle() {
		return getDesignElementHandle().getStyle();
	}

	@Override
	public boolean doDeleteItem(int pos) throws PropertyValueException {
		PropertyHandle phandle = getDesignElementHandle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		phandle.removeItem(pos);

		try {
			if (phandle.getListValue() == null || phandle.getListValue().size() == 0) {
				getDesignElementHandle().setProperty(StyleHandle.HIGHLIGHT_RULES_PROP, null);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}

		return true;
	}

	@Override
	public HighlightRuleHandle doAddItem(HighlightRule rule, int pos) {
		PropertyHandle phandle = getDesignElementHandle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);

		try {
			phandle.addItem(rule);
		} catch (SemanticException e) {
			ExceptionUtil.handle(e);
		}

		StructureHandle handle = rule.getHandle(phandle, pos);

		return (HighlightRuleHandle) handle;
	}

	@Override
	public boolean edit(Object input, int handleCount) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("HighlightsPage.trans.Edit")); //$NON-NLS-1$

			HighlightRuleBuilder builder = new HighlightRuleBuilder(UIUtil.getDefaultShell(),
					Messages.getString("HighlightsPage.Dialog.EditHighlight"), //$NON-NLS-1$
					this);

			HighlightRuleHandle handle = (HighlightRuleHandle) input;

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
			stack.startTrans(Messages.getString("HighlightsPage.trans.Add")); //$NON-NLS-1$

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

	protected HighlightRuleBuilder createAddDialog(int handleCount) {
		HighlightRuleBuilder builder = new HighlightRuleBuilder(UIUtil.getDefaultShell(),
				Messages.getString("HighlightsPage.Dialog.NewHighlight"), //$NON-NLS-1$
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
			stack.startTrans(Messages.getString("HighlightsPage.trans.Delete")); //$NON-NLS-1$

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
			stack.startTrans(Messages.getString("HighlightsPage.trans.MoveUp")); //$NON-NLS-1$

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
			stack.startTrans(Messages.getString("HighlightsPage.trans.MoveDown")); //$NON-NLS-1$

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
		return Messages.getString("HighlightsPage.Label.Highlights"); //$NON-NLS-1$
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

	public String getFontFamily(Object object) {
		HighlightRuleHandle hrHandle = (HighlightRuleHandle) object;
		// String rfm = hrHandle.getFontFamilyHandle( ).getStringValue( );
		String rfm = DEUtil.removeQuote(hrHandle.getFontFamilyHandle().getStringValue());

		if (rfm == null || rfm.length() == 0) {
			if (getDesignElementHandle() != null) {
				rfm = getDesignElementHandle().getPrivateStyle().getFontFamilyHandle().getStringValue();
			} else {
				rfm = DesignChoiceConstants.FONT_FAMILY_SERIF;
			}
		}

		return HighlightHandleProvider.getFontFamily(rfm);
	}

	public int getFontSize(Object object) {
		HighlightRuleHandle hrHandle = (HighlightRuleHandle) object;
		String rfs = hrHandle.getFontSize().getStringValue();

		if ((rfs == null || rfs.length() == 0) && getDesignElementHandle() != null) {
			rfs = getDesignElementHandle().getPrivateStyle().getFontSize().getStringValue();
		}

		return DEUtil.getFontSize(rfs);
	}

	public boolean isBold(Object object) {
		return DesignChoiceConstants.FONT_WEIGHT_BOLD.equals(((HighlightRuleHandle) object).getFontWeight());
	}

	public boolean isItalic(Object object) {
		return DesignChoiceConstants.FONT_STYLE_ITALIC.equals(((HighlightRuleHandle) object).getFontStyle());
	}

	public boolean isUnderline(Object object) {
		return DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals(((HighlightRuleHandle) object).getTextUnderline());
	}

	public boolean isLinethrough(Object object) {
		return DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH
				.equals(((HighlightRuleHandle) object).getTextLineThrough());
	}

	public boolean isOverline(Object object) {
		return DesignChoiceConstants.TEXT_OVERLINE_OVERLINE.equals(((HighlightRuleHandle) object).getTextOverline());
	}

	public Color getColor(Object object) {
		return ColorManager.getColor(((HighlightRuleHandle) object).getColor().getRGB());
	}

	public Color getBackgroundColor(Object object) {
		return ColorManager.getColor(((HighlightRuleHandle) object).getBackgroundColor().getRGB());
	}

	@Override
	public String getText(int key) {
		switch (key) {
		case 0:
			return Messages.getString("HighlightsPage.Label.Highlights"); //$NON-NLS-1$
		case 1:
			return Messages.getString("HighlightsPage.Button.Add"); //$NON-NLS-1$
		case 2:
			return Messages.getString("HighlightsPage.Button.Delete"); //$NON-NLS-1$
		case 3:
			return Messages.getString("FormPage.Button.Up"); //$NON-NLS-1$
		case 4:
			return Messages.getString("HighlightsPage.toolTipText.MoveUp"); //$NON-NLS-1$
		case 5:
			return Messages.getString("FormPage.Button.Down"); //$NON-NLS-1$
		case 6:
			return Messages.getString("HighlightsPage.toolTipText.MoveDown"); //$NON-NLS-1$
		case 7:
			return Messages.getString("HighlightsPage.TableColumn.Preview"); //$NON-NLS-1$
		case 8:
			return Messages.getString("HighlightsPage.TableColumn.Condition"); //$NON-NLS-1$
		case 9:
			return Messages.getString("HighlightRuleBuilderDialog.text.PreviewContent"); //$NON-NLS-1$
		case 10:
			return Messages.getString("HighlightsPage.label.duplicate"); //$NON-NLS-1$
		case 11:
			return Messages.getString("HighlightsPage.toolTipText.duplicate"); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
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
			stack.startTrans(Messages.getString("HighlightsPage.trans.Duplicate")); //$NON-NLS-1$
			PropertyHandle phandle = getDesignElementHandle().getPropertyHandle(StyleHandle.HIGHLIGHT_RULES_PROP);
			HighlightRule rule = (HighlightRule) phandle.getListValue().get(pos);
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
