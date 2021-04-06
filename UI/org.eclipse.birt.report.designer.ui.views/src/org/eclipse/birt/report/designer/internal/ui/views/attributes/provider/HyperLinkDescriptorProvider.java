
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class HyperLinkDescriptorProvider extends AbstractDescriptorProvider implements ITextDescriptorProvider {

	private static final String LABEL_LINK_TO = Messages.getString("HyperLinkPage.Label.LnikTo"); //$NON-NLS-1$

	private static final String LABEL_NONE = Messages.getString("HyperLinkPage.Label.None"); //$NON-NLS-1$

	public String getDisplayName() {
		// TODO Auto-generated method stub
		return LABEL_LINK_TO;
	}

	private Object oldValue;

	public Object load() {
		if (needRefresh) {
			if (getActionHandle() != null) {
				String previewString = null;
				if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(getActionHandle().getLinkType())) {
					previewString = getActionHandle().getURI();
				} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK
						.equals(getActionHandle().getLinkType())) {
					previewString = getActionHandle().getTargetBookmark();
				} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH
						.equals(getActionHandle().getLinkType())) {
					previewString = getActionHandle().getReportName();
					if (getActionHandle().getTargetBookmark() != null) {
						previewString += ":" + getActionHandle().getTargetBookmark(); //$NON-NLS-1$
					}
				}
				if (previewString == null) {
					oldValue = LABEL_NONE;
				} else
					oldValue = previewString;
			} else {
				oldValue = LABEL_NONE;
			}
		}
		return oldValue;
	}

	public void save(Object value) throws SemanticException {
		// TODO Auto-generated method stub

	}

	protected Object input;

	public void setInput(Object input) {
		this.input = input;
	}

	public boolean hyperLinkSelected() {
		boolean flag = true;
		HyperlinkBuilder dialog = new HyperlinkBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell());
		getActionStack().startTrans(Messages.getString("HyperLinkPage.Menu.Save")); //$NON-NLS-1$
		ActionHandle handle = getActionHandle();
		if (handle == null) {
			try {
				handle = DEUtil.setAction((ReportItemHandle) DEUtil.getInputFirstElement(input),
						StructureFactory.createAction());
			} catch (SemanticException e1) {
				getActionStack().rollback();
				ExceptionUtil.handle(e1);
				return false;
			}
		}
		dialog.setInput(handle);
		needRefresh = false;
		boolean isOK = dialog.open() == Dialog.OK;
		needRefresh = true;
		if (isOK) {
			getActionStack().commit();
			flag = true;
		} else {
			getActionStack().rollback();
			flag = false;
		}
		return flag;
	}

	private boolean needRefresh = true;

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	private ActionHandle getActionHandle() {
		return DEUtil.getActionHandle((ReportItemHandle) DEUtil.getInputFirstElement(input));
	}

	public boolean isEditable() {
		return false;
	}

	public boolean isEnable() {
		if (DEUtil.getInputSize(input) != 1)
			return false;
		else
			return true;
	}
}
