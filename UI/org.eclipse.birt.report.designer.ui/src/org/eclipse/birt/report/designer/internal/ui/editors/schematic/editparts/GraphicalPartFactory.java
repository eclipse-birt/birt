/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.IExtension;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * Factory to populate the edit part for given model type
 * 
 */
public class GraphicalPartFactory implements EditPartFactory {

	/**
	 * Constructor
	 * 
	 * @param handle the handle
	 */
	public GraphicalPartFactory() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart,
	 * java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = new DummyEditpart(model);

		if (model instanceof ReportItemHandle
				&& (!((ReportItemHandle) model).isValidLayoutForCompoundElement() || !checkLayout(model))) {
			return new DestroyEditPart(model);
		} else if (model instanceof ReportItemHandle && !(context instanceof MultipleEditPart)) {
			if (((ReportItemHandle) model).getViews().size() > 0) {
				return new MultipleEditPart(model);
			}
		}
		if (model instanceof ReportDesignHandle) {
			return new ReportDesignEditPart(model);
		}

		else if (model instanceof MasterPageHandle) {
			return new MasterPageEditPart(model);
		}

		else if (model instanceof ImageHandle) {
			return new ImageEditPart(model);
		}
		if (model instanceof TableHandle) {
			return new TableEditPart(model);
		}
		if (model instanceof CellHandle) {
			return new TableCellEditPart(model);
		}
		if (model instanceof AutoTextHandle) {
			return new AutoTextEditPart(model);
		}
		if (model instanceof LabelHandle) {
			return new LabelEditPart(model);
		}
		if (model instanceof TextItemHandle) {
			return new TextEditPart(model);
		}
		if (model instanceof DataItemHandle) {
			return new DataEditPart(model);
		}
		if (model instanceof TextDataHandle) {
			return new TextDataEditPart(model);
		}

		// if ( model instanceof ReportElementModel &&
		// (((ReportElementModel)model).getElementHandle() instanceof
		// SimpleMasterPageHandle))
		// {
		// return new AreaEditPart( model );
		// }
		if (model instanceof SlotHandle
				&& (((SlotHandle) model).getElementHandle() instanceof SimpleMasterPageHandle)) {
			return new AreaEditPart(model);
		}
		if (model instanceof GridHandle) {
			return new GridEditPart(model);
		}
		if (model instanceof ListHandle) {
			return new ListEditPart(model);
		}
		if (model instanceof ListBandProxy) {
			return new ListBandEditPart(model);
		}
		if (model instanceof TemplateElementHandle) {
			return new PlaceHolderEditPart(model);
		}

		EditPart eep = EditpartExtensionManager.createEditPart(context, model);
		if (eep != null)
			return eep;

		IExtension extension = new IExtension.Stub() {

			public String getExtendsionIdentify() {
				return GuiExtensionManager.DESIGNER_FACTORY;
			}
		};

		Object obj = GuiExtensionManager.doExtension(extension, model);
		if (obj != null) {
			return (EditPart) obj;
		}
		return editPart;
	}

	private boolean checkLayout(Object model) {
		Object[] checks = ElementAdapterManager.getAdapters(model, ILayoutCheck.class);
		if (checks == null) {
			return true;
		}
		for (int i = 0; i < checks.length; i++) {
			ILayoutCheck check = (ILayoutCheck) checks[i];
			if (!check.layoutCheck(model)) {
				return false;
			}
		}

		return true;
	}
}
