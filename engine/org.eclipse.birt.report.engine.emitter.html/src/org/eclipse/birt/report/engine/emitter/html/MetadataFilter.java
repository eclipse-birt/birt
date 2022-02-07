/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.html;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.IMetadataFilter;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

/**
 * |-For extended item: | Output IID, bookmark and element type. Add the element
 * into the iid list. |-For list item: | Output IID, bookmark and element type.
 * Add the element into the iid list. |-For table item: | Output IID, bookmark
 * and element type. Add the element into the iid list. |-For column item: |
 * Output IID. |-For row item: | Output type and row type for table
 * header/footer row and group header/footer row. | Output group-id for all the
 * rows. |-For template item: | Output IID, bookmark and element type. Add the
 * element into the iid list. |-For text item and text data: | When the text
 * item and text data is a top level element, or in Table/Group Header/Footer, |
 * output IID, bookmark and element type. Add the element into the iid list.
 * |-For label item: | Output IID, bookmark and element type. Add the element
 * into the iid list. |-For data item: | When the data item is a top level
 * element or aggregatable( don't have a dataset, don't have a column binding,
 * must in table/group header/footer row ), | output IID, bookmark and element
 * type. Add the element into the iid list.
 */
public class MetadataFilter implements IMetadataFilter {

	static final String TYPE_EXTENDED = "EXTENDED";
	static final String TYPE_LIST = "LIST";
	static final String TYPE_TABLE = "TABLE";
	static final String TYPE_TEMPLATE = "TEMPLATE";
	static final String TYPE_TEXT = "TEXT";
	static final String TYPE_LABEL = "LABEL";
	static final String TYPE_DATA = "DATA";

	private HashMap dataItemCache = new HashMap();
	private HashMap rowHeaderFooterCache = new HashMap();

	public HashMap needMetaData(ReportElementHandle elementHandle) {
		HashMap resultMap = null;
		if (elementHandle instanceof ExtendedItemHandle) {
			// FIXME Please add the logic for crosstab cells and crosstab rows.
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			String elementType = elementHandle.getStringProperty("extensionName");
			if (elementType == null) {
				elementType = TYPE_EXTENDED;
			}
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, elementType);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		} else if (elementHandle instanceof ListHandle) {
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_LIST);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		} else if (elementHandle instanceof TableHandle) {
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_TABLE);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		}
		if (elementHandle instanceof ColumnHandle) {
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
		}
		if (elementHandle instanceof RowHandle) {
			DesignElementHandle rowParentHnadle = elementHandle.getContainer();
			if (rowParentHnadle instanceof TableHandle) {
				int slotID = ((TableHandle) rowParentHnadle).findContentSlot(elementHandle);
				if (TableHandle.HEADER_SLOT == slotID) {
					rowHeaderFooterCache.put(elementHandle, Boolean.TRUE);
					resultMap = new HashMap();
					resultMap.put(IMetadataFilter.KEY_ATTR_TYPE, "wrth");
					resultMap.put(IMetadataFilter.KEY_ATTR_ROW_TYPE, "header");
					resultMap.put(IMetadataFilter.KEY_OUTPUT_GOURP_ID, Boolean.TRUE);
				} else if (TableHandle.FOOTER_SLOT == slotID) {
					rowHeaderFooterCache.put(elementHandle, Boolean.TRUE);
					resultMap = new HashMap();
					resultMap.put(IMetadataFilter.KEY_ATTR_TYPE, "wrtf");
					resultMap.put(IMetadataFilter.KEY_ATTR_ROW_TYPE, "footer");
					resultMap.put(IMetadataFilter.KEY_OUTPUT_GOURP_ID, Boolean.TRUE);
				} else if (TableHandle.DETAIL_SLOT == slotID) {
					rowHeaderFooterCache.put(elementHandle, Boolean.FALSE);
					resultMap = new HashMap();
					resultMap.put(IMetadataFilter.KEY_OUTPUT_GOURP_ID, Boolean.TRUE);
				}
			} else if (rowParentHnadle instanceof TableGroupHandle) {
				TableGroupHandle groupHandle = (TableGroupHandle) rowParentHnadle;

				int groupLevel = -1;
				TableHandle tableHandle = (TableHandle) groupHandle.getContainer();
				SlotHandle groupSlot = tableHandle.getGroups();
				for (int i = 0; i < groupSlot.getCount(); i++) {
					if (groupHandle == groupSlot.get(i)) {
						groupLevel = i;
					}
				}

				int slotID = groupHandle.findContentSlot(elementHandle);
				if (TableGroupHandle.HEADER_SLOT == slotID) {
					rowHeaderFooterCache.put(elementHandle, Boolean.TRUE);
					resultMap = new HashMap();
					if (groupLevel != -1) {
						resultMap.put(IMetadataFilter.KEY_ATTR_TYPE, "wrgh" + groupLevel);
					}
					resultMap.put(IMetadataFilter.KEY_ATTR_ROW_TYPE, "group-header");
					resultMap.put(IMetadataFilter.KEY_OUTPUT_GOURP_ID, Boolean.TRUE);
				} else if (TableGroupHandle.FOOTER_SLOT == slotID) {
					rowHeaderFooterCache.put(elementHandle, Boolean.TRUE);
					resultMap = new HashMap();
					if (groupLevel != -1) {
						resultMap.put(IMetadataFilter.KEY_ATTR_TYPE, "wrgf" + groupLevel);
					}
					resultMap.put(IMetadataFilter.KEY_ATTR_ROW_TYPE, "group-footer");
					resultMap.put(IMetadataFilter.KEY_OUTPUT_GOURP_ID, Boolean.TRUE);
				}
			}
		} else if (elementHandle instanceof TemplateReportItemHandle) {
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_TEMPLATE);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		} else if ((elementHandle instanceof TextItemHandle) || (elementHandle instanceof TextDataHandle)) {
			if (isTopLevelElement(elementHandle) || isInHeaderFooter(elementHandle)) {
				resultMap = new HashMap();
				resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
				resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
				resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_TEXT);
				resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
			}
		} else if (elementHandle instanceof LabelHandle) {
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_LABEL);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		} else if (elementHandle instanceof DataItemHandle) {
			Boolean chacheResult = (Boolean) dataItemCache.get(elementHandle);
			if (chacheResult == null) {
				if (!isTopLevelElement(elementHandle)) {
					if (elementHandle.getStringProperty("dataSet") != null) {
						dataItemCache.put(elementHandle, Boolean.FALSE);
						return null;
					}
					if (((ReportItemHandle) elementHandle).getDataBindingReference() != null) {
						dataItemCache.put(elementHandle, Boolean.FALSE);
						return null;
					}
					if (!isInHeaderFooter(elementHandle)) {
						dataItemCache.put(elementHandle, Boolean.FALSE);
						return null;
					}
				}
				dataItemCache.put(elementHandle, Boolean.TRUE);
			} else {
				if (!chacheResult.booleanValue()) {
					return null;
				}
			}
			resultMap = new HashMap();
			resultMap.put(IMetadataFilter.KEY_OUTPUT_IID, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_OUTPUT_BOOKMARK, Boolean.TRUE);
			resultMap.put(IMetadataFilter.KEY_ATTR_ELEMENT_TYPE, TYPE_DATA);
			resultMap.put(IMetadataFilter.KEY_ADD_INTO_IID_LIST, Boolean.TRUE);
		}
		return resultMap;
	}

	/**
	 * Is the element is a top level element?
	 * 
	 * @param elementHandle
	 * @return
	 */
	private boolean isTopLevelElement(ReportElementHandle elementHandle) {
		DesignElementHandle containerHandle = elementHandle.getContainer();
		if (containerHandle instanceof ReportDesignHandle) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if a element is in a table header/footer row, or group header/footer
	 * row.
	 * 
	 * @param text
	 * @return
	 */
	private boolean isInHeaderFooter(ReportElementHandle elementHandle) {
		DesignElementHandle containerHandle = elementHandle.getContainer();
		while (containerHandle != null) {
			// FIXME: Please add the crosstab row part.
			if (containerHandle instanceof RowHandle) {
				Boolean rowResult = (Boolean) rowHeaderFooterCache.get(containerHandle);
				if (rowResult != null) {
					return rowResult.booleanValue();
				}

				RowHandle rowHandle = (RowHandle) containerHandle;
				DesignElementHandle rowParentHnadle = rowHandle.getContainer();
				if (rowParentHnadle instanceof TableHandle) {
					int slotID = ((TableHandle) rowParentHnadle).findContentSlot(rowHandle);
					if (TableHandle.HEADER_SLOT == slotID || TableHandle.FOOTER_SLOT == slotID) {
						// In table header or footer
						rowHeaderFooterCache.put(containerHandle, Boolean.TRUE);
						return true;
					}
				} else if (rowParentHnadle instanceof TableGroupHandle) {
					int slotID = ((TableGroupHandle) rowParentHnadle).findContentSlot(rowHandle);
					if (TableGroupHandle.HEADER_SLOT == slotID || TableGroupHandle.FOOTER_SLOT == slotID) {
						if (rowParentHnadle.getContainer() instanceof TableHandle) {
							// In group header or footer
							rowHeaderFooterCache.put(containerHandle, Boolean.TRUE);
							return true;
						}
					}
				} else if (!(rowParentHnadle instanceof GridHandle)) {
					rowHeaderFooterCache.put(containerHandle, Boolean.FALSE);
				}
			}

			// Return false when reach top level report.
			else if (containerHandle instanceof ReportDesignHandle) {
				return false;
			}

			// Return false if the handle has a dataSet/query.
			if (containerHandle.getStringProperty("dataSet") != null) {
				return false;
			}
			if (containerHandle instanceof ReportItemHandle) {
				if (((ReportItemHandle) containerHandle).getDataBindingReference() != null) {
					return false;
				}
			}

			containerHandle = containerHandle.getContainer();
		}
		return false;
	}

}
