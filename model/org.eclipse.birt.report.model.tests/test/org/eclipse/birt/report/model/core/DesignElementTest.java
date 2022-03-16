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

package org.eclipse.birt.report.model.core;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.ExtendsEvent;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.InvalidParentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.namespace.AbstractNameHelper;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Unit test for Class DesignElement.
 *
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testAddRemoveListener}</td>
 * <td>add listener</td>
 * <td>contain listener</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>remove an unknown listener</td>
 * <td>don't contain listener</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>remove listener</td>
 * <td>don't contain listener</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>clears listeners when remove an element or add an element then undo.</td>
 * <td>listeners are cleaned.</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testBroadcast}</td>
 * <td>set element and their relationships , all elements subscribe
 * listener</td>
 * <td>all elements contain listener</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>check property of element</td>
 * <td>properties of elements are null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>broadcast all elements and check property of element</td>
 * <td>properties of elements are have value " listener"</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetPropertyDefn}</td>
 * <td>get propertyDefn from DesignElement</td>
 * <td>directly get ElementPropertyDefn</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get propertyDefn from UserPropertyDefn</td>
 * <td>get UserPropertyDefn through right name</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetProperty}</td>
 * <td>get local system property</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>all elements contain listener</td>
 * <td>get value "Hello" from system property</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get local user-defined property</td>
 * <td>get value "user" from "MyProperty" property</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get the user-defined property from the ancestor</td>
 * <td>get value "userParent" from "MyProperty" property</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get the system property from the ancestor</td>
 * <td>get value "English" from system property</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>get the property from the associated style</td>
 * <td>get value "red" from style property</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetLocalProperty}</td>
 * <td>get local system property</td>
 * <td>get value "Hello" from system property</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetPropertyValue}</td>
 * <td>get local system property</td>
 * <td>get value "Hello" which just set</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set local system property</td>
 * <td>get value "element" which just set</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testClearPropertyValue}</td>
 * <td>get local system property</td>
 * <td>get value "Hello" which just set</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>clear local system property</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDefn}</td>
 * <td>get definition</td>
 * <td>object is equal to element which get through element name</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testAddUserProperty}</td>
 * <td>add UserPropertyDefn and check it</td>
 * <td>get right Object</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add UserPropertyDefn to its parent and check it</td>
 * <td>get right Object from parent</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testDropUserProperty}</td>
 * <td>add UserPropertyDefn</td>
 * <td>get right Object</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop it</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetUserProperty}</td>
 * <td>add UserPropertyDefn and check it</td>
 * <td>get right Object</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add UserPropertyDefn to its parent and check it</td>
 * <td>get right Object from parent</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testValidatePropertyValue}</td>
 * <td>validate value when propName is not exist</td>
 * <td>throw out exception</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>validate value when propName is exist</td>
 * <td>validate right</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDisplayName}</td>
 * <td>set displayname property</td>
 * <td>get right value "Hello"</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDisplayNameID}</td>
 * <td>set displaynameid property</td>
 * <td>get right value "111"</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testAddDerived}</td>
 * <td>add parent</td>
 * <td>contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop parent</td>
 * <td>don't contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add parent</td>
 * <td>contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testDropDerived}</td>
 * <td>add parent</td>
 * <td>contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop parent</td>
 * <td>don't contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testSetExtends}</td>
 * <td>set parent</td>
 * <td>contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDescendents}</td>
 * <td>direct check arraylist size</td>
 * <td>0</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set parent</td>
 * <td>contain descendent</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set ancestor</td>
 * <td>size of ancestor's arraylist is 2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop parent , and check size of ancestor's arraylist</td>
 * <td>size of ancestor's arraylist is 1</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGatherDescendents}</td>
 * <td>no descendents</td>
 * <td>don't contain element</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add descendent</td>
 * <td>contain element</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add ancestor</td>
 * <td>contain two elements</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop ancestor and add two parents</td>
 * <td>both contain element</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testHasUserProperties}</td>
 * <td>add UserPropertyDefn</td>
 * <td>has not user property</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>drop UserPropertyDefn</td>
 * <td>has user property</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testHasLocalPropertyValues}</td>
 * <td>direct check propValues</td>
 * <td>false</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set display name value</td>
 * <td>true</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetUserProperties}</td>
 * <td>direct check arraylist size</td>
 * <td>0</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add UserPropertyDefn</td>
 * <td>1</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>add ancestor with UserPropertyDefn</td>
 * <td>2</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testIsKindOf}</td>
 * <td>set parent element and check element</td>
 * <td>isKindOf is true</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set ancestor element and check element</td>
 * <td>after set ancestor , isKindOf is true</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testHasDerived}</td>
 * <td>no derive</td>
 * <td>false</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>set derive</td>
 * <td>true</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGetDoubleProperty}</td>
 * <td>the property value is null and check float property</td>
 * <td>double value 0.0</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>the type of property value is integer and check float property</td>
 * <td>double value 2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>the property type is double</td>
 * <td>double value 2.5</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testLabelDisplayLabel}</td>
 * <td>label1 has resource-key ,label name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>label2 just has label name</td>
 * <td>label2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>label3 has resource-key and label name but hasnot value of
 * display-name</td>
 * <td>label3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has no label name but has resource-key and value of
 * display-name</td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has no label name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has text attribute and value is blue , it also has label name
 * hexingjie</td>
 * <td>hexingjie("blue")</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has text attribute and value is blue , but hasnot label
 * name</td>
 * <td>label("blue")</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this label has text attribute,value is blue and length more than 30 chars
 * </td>
 * <td>label("test asdf sadf sadf sdaf...")</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testTextDisplayLabel}</td>
 * <td>text1 has resource-key ,label name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>text2 just has label name</td>
 * <td>text2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>text3 has resource-key and label name but hasnot value of
 * display-name</td>
 * <td>text3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text hasnot label name but has resource-key and value of
 * display-name</td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text hasnot label name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text has static-text attribute , it also has text name</td>
 * <td>hexingjie("This is a sample bit of plain...")</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text has static-text attribute , but hasnot text name</td>
 * <td>text("hello,12345678910111213141adfa...")</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text has static-text attribute , and length is less than 30
 * chars</td>
 * <td>text("This is ")</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this text has static-text attribute , and length is more than 30
 * chars</td>
 * <td>text("a sample bit of plain...")</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testGridDisplayLabel}</td>
 * <td>gird1 has resource-key ,label name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>grid2 just has label name</td>
 * <td>grid2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>grid3 has resource-key and label name but hasnot value of
 * display-name</td>
 * <td>grid3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this grid hasnot label name but has resource-key and value of
 * display-name</td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this grid hasnot label name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this grid has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this grid has two columns and three rows attributes</td>
 * <td>hexingjie(3 x 2)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this grid has row attribute</td>
 * <td>grid(1 x 0)</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testListDisplayLabel}</td>
 * <td>list1 has resource-key ,list name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>list2 just has list name</td>
 * <td>list2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>list3 has resource-key and list name but hasnot value of
 * display-name</td>
 * <td>list3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list hasnot list name but has resource-key and value of display-name
 * </td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list hasnot list name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list has data-set attribute and has list name</td>
 * <td>hexingjie(firstDataSet)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list has data-set attribute but hasnot list name</td>
 * <td>list(secondDataSet)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this list has data-set attribute and length more than 30 chars</td>
 * <td>list(test asdf sadf sadf sdaf...)</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testImageDisplayLabel}</td>
 * <td>image1 has resource-key ,image name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>image2 just has list name</td>
 * <td>image2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>image3 has resource-key and image name but hasnot value of
 * display-name</td>
 * <td>image3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image hasnot image name but has resource-key and value of
 * display-name</td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image hasnot image name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has uri attribute</td>
 * <td>hexingjie1(Blue He)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has image-name attribute</td>
 * <td>hexingjie2(test image)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has value-expr attribute</td>
 * <td>image(haha)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has uri attribute and length more than 30 chars</td>
 * <td>image(test asdf sadf sadf sdaf...)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has image-name attribute and length more than 30 chars</td>
 * <td>image(test asdf sadf sadf...)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this image has value-expr attribute and length more than 30 chars</td>
 * <td>image(test asdf sadf...)</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testTableDisplayLabel}</td>
 * <td>table1 has resource-key ,table name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>table2 just has table name</td>
 * <td>table2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>table3 has resource-key and table name but hasnot value of
 * display-name</td>
 * <td>table3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table hasnot table name but has resource-key and value of
 * display-name</td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table hasnot table name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table has data-set attribute</td>
 * <td>hexingjie1(firstDataSet)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table has data-set attribute but hasnot name</td>
 * <td>table(secondDataSet)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this table has data-set attribute and length more than 30 chars</td>
 * <td>table(asdf sadf asdfa...)</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testDataDisplayLabel}</td>
 * <td>data1 has resource-key ,data name and value of display-name</td>
 * <td>he</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>data2 just has data name</td>
 * <td>data2</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>data3 has resource-key and data name but hasnot value of
 * display-name</td>
 * <td>data3</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data hasnot data name but has resource-key and value of display-name
 * </td>
 * <td>Ant</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data hasnot data name and value of display-name, just has
 * resource-key</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data has nothing attribute</td>
 * <td>null</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data has value-expr attribute and data name</td>
 * <td>hexingjie1(haha)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data has value-expr attribute</td>
 * <td>data(testtest)</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>this data has value-expr attribute and length more than 30 chars</td>
 * <td>data(test asdf sadf sadf sdaf...)</td>
 * </tr>
 *
 * </table>
 *
 *
 */

public class DesignElementTest extends BaseTestCase {

	// seven test files

	private final String DISPLAY_LABEL_FILE_NAME = "DesignElementTest_1.xml"; //$NON-NLS-1$
	private final String DISPLAY_TEXT_FILE_NAME = "DesignElementTest_2.xml"; //$NON-NLS-1$
	private final String DISPLAY_GRID_FILE_NAME = "DesignElementTest_3.xml"; //$NON-NLS-1$
	private final String DISPLAY_List_FILE_NAME = "DesignElementTest_4.xml"; //$NON-NLS-1$
	private final String DISPLAY_DATA_FILE_NAME = "DesignElementTest_5.xml"; //$NON-NLS-1$
	private final String DISPLAY_IMAGE_FILE_NAME = "DesignElementTest_6.xml"; //$NON-NLS-1$
	private final String DISPLAY_TABLE_FILE_NAME = "DesignElementTest_7.xml"; //$NON-NLS-1$

	// seven types

	private static final int LABEL = 0;
	private static final int TEXT = 1;
	private static final int GRID = 2;
	private static final int LIST = 3;
	private static final int DATA = 4;
	private static final int IMAGE = 5;
	private static final int TABLE = 6;

	private TextItem designElement = null;
	private MetaDataDictionary dd = null;

	/*
	 * @see TestCase#setUp()1
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		sessionHandle = engine.newSessionHandle(ULocale.ENGLISH);
		designElement = new TextItem();
		designElement.setName("element"); //$NON-NLS-1$

		designHandle = sessionHandle.createDesign("myDesign"); //$NON-NLS-1$
		design = (ReportDesign) designHandle.getModule();
		assertEquals(0, design.getAllErrors().size());

		// get the MetaDataDictionary instance

		dd = MetaDataDictionary.getInstance();
	}

	/**
	 * Test add listener and remove listener.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add listener</li>
	 * <li>remove an unknown listener</li>
	 * <li>remove listener</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain listener</li>
	 * <li>don't contain listener</li>
	 * <li>don't contain listener</li>
	 * </ul>
	 *
	 * @throws Exception
	 *
	 */
	public void testAddRemoveListener() throws Exception {
		assertNull(CoreTestUtil.getListeners(designElement));
		MyActionListener listener = new MyActionListener();

		// Add listner

		designElement.addListener(listener);
		assertTrue(CoreTestUtil.getListeners(designElement).contains(listener));

		// Remove listener

		designElement.removeListener(listener);
		assertFalse(CoreTestUtil.getListeners(designElement).contains(listener));

		// Remove a non-existing listener

		designElement.removeListener(listener);
		assertFalse(CoreTestUtil.getListeners(designElement).contains(listener));

		// remove all listeners.

		designElement.clearListeners();

		listener = new MyActionListener();
		DesignElementHandle handle = designElement.getHandle(design);
		handle.addListener(listener);

		// add an element then undo.

		designHandle.getBody().add(handle);
		assertEquals(1, CoreTestUtil.getListeners(designElement).size());

		designHandle.getCommandStack().undo();
		assertNull(CoreTestUtil.getListeners(designElement));

		designHandle.getCommandStack().redo();
		assertNull(CoreTestUtil.getListeners(designElement));

		designHandle.getCommandStack().undo();
		assertNull(CoreTestUtil.getListeners(designElement));

		// drop an element, listeners are removed.

		handle.addListener(listener);

		designHandle.getBody().add(handle);
		handle.dropAndClear();

		assertNull(CoreTestUtil.getListeners(designElement));

		designHandle.getCommandStack().undo();
		assertNull(CoreTestUtil.getListeners(designElement));

		designHandle.getCommandStack().redo();
		assertNull(CoreTestUtil.getListeners(designElement));
	}

	/**
	 * Test broadcast(NotificationEvent ). grand --> parent --> designElement.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>set element and their relationships , all elements subscribe listener
	 * </li>
	 * <li>check property of element</li>
	 * <li>broadcast all elements and check property of element</li>
	 * </ul>
	 *
	 * Excepted:
	 * <ul>
	 * <li>all elements contain listener</li>
	 * <li>properties of elements are null</li>
	 * <li>properties of elements are have value " listener"</li>
	 * </ul>
	 *
	 * @throws ContentException
	 * @throws NameException
	 * @throws ExtendsException
	 *
	 */
	public void testBroadcast() throws ContentException, NameException, ExtendsException {
		// set the derivation relationship and the listener.

		TextItemHandle parent = designHandle.getElementFactory().newTextItem("parent"); //$NON-NLS-1$
		TextItemHandle grand = designHandle.getElementFactory().newTextItem("grand"); //$NON-NLS-1$

		designHandle.getComponents().add(parent);
		designHandle.getComponents().add(grand);

		designElement.setExtendsElement(parent.getElement());
		parent.setExtends(grand);

		MyActionListener listener = new MyActionListener();
		MyActionListener parentListener = new MyActionListener();
		MyActionListener grandListener = new MyActionListener();

		designElement.addListener(listener);
		parent.addListener(parentListener);
		grand.addListener(grandListener);
		assertTrue(CoreTestUtil.getListeners(designElement).contains(listener));
		assertTrue(CoreTestUtil.getListeners(parent.getElement()).contains(parentListener));
		assertTrue(CoreTestUtil.getListeners(grand.getElement()).contains(grandListener));

		// test the original name of the elements.

		assertNull(designElement.getProperty(design, DesignElement.DISPLAY_NAME_PROP));
		assertNull(parent.getProperty(DesignElement.DISPLAY_NAME_PROP));
		assertNull(grand.getProperty(DesignElement.DISPLAY_NAME_PROP));

		// after sendEvent, we test the names again.

		NotificationEvent ev = new ExtendsEvent(grand.getElement());
		grand.getElement().broadcast(ev);

		assertTrue(listener.done);
		assertEquals(NotificationEvent.DESCENDENT, listener.path);
		assertTrue(parentListener.done);
		assertEquals(NotificationEvent.DESCENDENT, parentListener.path);
		assertTrue(grandListener.done);
		assertEquals(NotificationEvent.DIRECT, grandListener.path);
	}

	/**
	 * Test getPropertyDefn( ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>get propertyDefn from DesignElement</li>
	 * <li>get propertyDefn from UserPropertyDefn</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>directly get ElementPropertyDefn</li>
	 * <li>get UserPropertyDefn through right name</li>
	 * </ul>
	 *
	 */
	public void testGetPropertyDefn() {
		UserPropertyDefn uDefn = new UserPropertyDefn();
		Object o = designElement.getPropertyDefn(DesignElement.DISPLAY_NAME_PROP);
		assertTrue(o != null);

		o = designElement.getPropertyDefn(StyledElement.STYLE_PROP);
		assertTrue(o != null);

		uDefn.setName("MyProperty");//$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);
		o = designElement.getPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);

	}

	/**
	 * Test getProperty.
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>check if property name is not exist</li>
	 * <li>get local system property</li>
	 * <li>get local user-defined property</li>
	 * <li>get the user-defined property from the ancestor</li>
	 * <li>get the system property from the ancestor</li>
	 * <li>get the property from the associated style</li>
	 * <li>get the property from the selector</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>null</li>
	 * <li>get value "Hello" from system property</li>
	 * <li>get value "user" from "MyProperty" property</li>
	 * <li>get value "userParent" from "MyProperty" property</li>
	 * <li>get value "English" from system property</li>
	 * <li>get value "red" from style property</li>
	 * <li>get value "blue" from style property</li>
	 * </ul>
	 *
	 * @throws SemanticException
	 */
	public void testGetProperty() throws SemanticException {
		// if propName is not exist , then return null

		assertNull(designElement.getProperty(design, "123")); //$NON-NLS-1$

		// get the local system property

		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "Hello"); //$NON-NLS-1$
		Object o = designElement.getProperty(design, DesignElement.DISPLAY_NAME_PROP);
		assertEquals("Hello", o); //$NON-NLS-1$

		// get the local user-defined property

		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("MyProperty"); //$NON-NLS-1$
		uDefn.setType(dd.getPropertyType(PropertyType.STRING_TYPE));
		designElement.addUserPropertyDefn(uDefn);
		designElement.setProperty(uDefn, "user"); //$NON-NLS-1$
		o = designElement.getProperty(design, "MyProperty");//$NON-NLS-1$
		assertEquals("user", o);//$NON-NLS-1$

		// get the user-defined property from the ancestor

		designElement.clearProperty("MyProperty");//$NON-NLS-1$
		designElement.dropUserPropertyDefn(uDefn);
		TextItem parent = new TextItem();
		parent.setName("parent");//$NON-NLS-1$
		parent.addUserPropertyDefn(uDefn);
		parent.setProperty(uDefn, "userParent");//$NON-NLS-1$
		o = parent.getProperty(design, "MyProperty");//$NON-NLS-1$
		assertEquals("userParent", o);//$NON-NLS-1$
		o = designElement.getProperty(design, "MyProperty");//$NON-NLS-1$
		assertNull(o);

		// must let parent on the design tree if want to set extends
		design.add(parent, IReportDesignModel.BODY_SLOT);
		designElement.setExtendsElement(parent);
		o = designElement.getProperty(design, "MyProperty");//$NON-NLS-1$
		assertEquals("userParent", o);//$NON-NLS-1$

		// get the system property from the ancestor

		TextItem grand = new TextItem();
		grand.setName("grand");//$NON-NLS-1$
		grand.setProperty(DesignElement.DISPLAY_NAME_ID_PROP, "English");//$NON-NLS-1$
		o = parent.getProperty(design, DesignElement.DISPLAY_NAME_ID_PROP);
		assertNull(o);
		o = designElement.getProperty(design, DesignElement.DISPLAY_NAME_ID_PROP);
		assertNull(o);

		// must let parent on the design tree if want to set extends
		design.add(grand, IReportDesignModel.BODY_SLOT);

		parent.setExtendsElement(grand);
		o = designElement.getProperty(design, DesignElement.DISPLAY_NAME_ID_PROP);
		assertEquals("English", o);//$NON-NLS-1$
		o = parent.getProperty(design, DesignElement.DISPLAY_NAME_ID_PROP);
		assertEquals("English", o);//$NON-NLS-1$

		// get the property from the associated style

		Label label = new Label();
		StyleElement style = new Style("My-Style"); //$NON-NLS-1$
		design.getSlot(IReportDesignModel.STYLE_SLOT).add(style);
		style.setProperty(Style.COLOR_PROP, "red"); //$NON-NLS-1$
		o = style.getProperty(design, Style.COLOR_PROP);
		assertEquals("red", o); //$NON-NLS-1$
		label.setStyle(style);
		o = label.getProperty(design, Style.COLOR_PROP);
		assertEquals("red", o); //$NON-NLS-1$

		// get the property from the Label selector
		ElementFactory elemFactory = new ElementFactory(design);
		SharedStyleHandle labelSelector = elemFactory.newStyle("label"); //$NON-NLS-1$
		// labelSelector.setProperty( StyleHandle.FONT_SIZE_PROP,
		// DesignChoiceConstants.FONT_SIZE_X_LARGE ); //$NON-NLS-1$
		labelSelector.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "blue"); //$NON-NLS-1$
		designHandle.getStyles().add(labelSelector);
		LabelHandle label1 = elemFactory.newLabel(null);
		assertEquals("blue", label1.getStringProperty(StyleHandle.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

		// get backgroud color from the table-detail selector

		SharedStyleHandle tableDetailSelector = elemFactory.newStyle("table-detail"); //$NON-NLS-1$
		tableDetailSelector.setProperty(Style.BACKGROUND_COLOR_PROP, ColorPropertyType.AQUA);
		designHandle.getStyles().add(tableDetailSelector);

		TableHandle table = elemFactory.newTableItem(null, 2, 1, 2, 1);
		RowHandle row = (RowHandle) table.getDetail().get(0);

		designHandle.getBody().add(table);

		assertEquals(ColorPropertyType.AQUA, row.getProperty(Style.BACKGROUND_COLOR_PROP));
		assertNull(table.getProperty(Style.BACKGROUND_COLOR_PROP));

		// check footer row

		row = (RowHandle) table.getFooter().get(0);
		assertNull(row.getProperty(Style.BACKGROUND_COLOR_PROP));

		// get backgroud color from the list-detail selector

		SharedStyleHandle listDetailSelector = elemFactory.newStyle("list-detail"); //$NON-NLS-1$
		listDetailSelector.setProperty(Style.BACKGROUND_COLOR_PROP, ColorPropertyType.YELLOW);
		designHandle.getStyles().add(listDetailSelector);

		designHandle.getStyles().drop(labelSelector);

		ListHandle list = elemFactory.newList(null);
		LabelHandle listLabel = elemFactory.newLabel(null);
		list.getDetail().add(listLabel);

		designHandle.getBody().add(list);

		assertEquals(ColorPropertyType.YELLOW, listLabel.getProperty(Style.BACKGROUND_COLOR_PROP));
		assertNull(list.getProperty(Style.BACKGROUND_COLOR_PROP));

		// get background color from the parent

		TableHandle parentTable = designHandle.getElementFactory().newTableItem("parent"); //$NON-NLS-1$
		designHandle.getComponents().add(parentTable);
		parentTable.setProperty(IStyleModel.BACKGROUND_COLOR_PROP, "red"); //$NON-NLS-1$
		TableHandle childTable = designHandle.getElementFactory().newTableItem("child"); //$NON-NLS-1$
		designHandle.getBody().add(childTable);
		assertNull(childTable.getProperty(IStyleModel.BACKGROUND_COLOR_PROP));
		childTable.setExtends(parentTable);
		assertEquals("red", childTable.getProperty(IStyleModel.BACKGROUND_COLOR_PROP)); //$NON-NLS-1$

	}

	/**
	 * Test getLocalProperty( ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>get local system property</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get value "Hello" from system property</li>
	 * </ul>
	 */
	public void testGetLocalProperty() {
		PropertyDefn prop = designElement.getPropertyDefn(DesignElement.DISPLAY_NAME_PROP);
		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "Hello");//$NON-NLS-1$
		Object o = designElement.getLocalProperty(design, prop.getName());
		assertEquals("Hello", o);//$NON-NLS-1$
	}

	/**
	 * Test setPropertyValue( String , Object ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>set local system property</li>
	 * <li>set local system property</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get value "Hello" which just set</li>
	 * <li>get value "element" which just set</li>
	 * </ul>
	 */
	public void testSetPropertyValue() {
		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "Hello");//$NON-NLS-1$
		Object o = designElement.getLocalProperty(design, DesignElement.DISPLAY_NAME_PROP);
		assertEquals("Hello", o);//$NON-NLS-1$

		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "element");//$NON-NLS-1$
		o = designElement.getLocalProperty(design, DesignElement.DISPLAY_NAME_PROP);
		assertEquals("element", o);//$NON-NLS-1$
	}

	/**
	 * Test setPropertyValue( String , Object ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>set local system property</li>
	 * <li>clear local system property</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get value "Hello" which just set</li>
	 * <li>null</li>
	 * </ul>
	 */
	public void testClearPropertyValue() {
		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "Hello");//$NON-NLS-1$
		Object o = designElement.getLocalProperty(design, DesignElement.DISPLAY_NAME_PROP);
		assertEquals("Hello", o);//$NON-NLS-1$

		designElement.clearProperty(DesignElement.DISPLAY_NAME_PROP);
		o = designElement.getLocalProperty(design, DesignElement.DISPLAY_NAME_PROP);
		assertNull(o);
	}

	/**
	 * Test ElementDefn getDefn( ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>get defn</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>object is equal to element which get through element name</li>
	 * </ul>
	 */
	public void testGetDefn() {
		dd = MetaDataDictionary.getInstance();
		Object o = designElement.getDefn();
		assertEquals(dd.getElement(designElement.getElementName()), o);
	}

	/**
	 * Test addUserProperty( UserPropertyDefn ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add UserPropertyDefn and check it</li>
	 * <li>add UserPropertyDefn to its parent and check it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get right Object</li>
	 * <li>get right Object from parent</li>
	 * </ul>
	 */
	public void testAddUserProperty() {
		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("MyProperty");//$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);

		Object o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);

		// if we add property into ancestor
		// then the children can get the property.
		TextItem parent = new TextItem();
		parent.setName("parent");//$NON-NLS-1$
		designElement.dropUserPropertyDefn(uDefn);
		parent.addUserPropertyDefn(uDefn);
		designElement.setExtendsElement(parent);
		o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);
	}

	/**
	 * Test dropUserProperty( UserPropertyDefn ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add UserPropertyDefn</li>
	 * <li>drop it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get right Object</li>
	 * <li>null</li>
	 * </ul>
	 */
	public void testDropUserProperty() {
		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("MyProperty");//$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);

		Object o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);

		designElement.dropUserPropertyDefn(uDefn);
		o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertNull(o);
	}

	/**
	 * Test getUserProperty( String ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add UserPropertyDefn and check it</li>
	 * <li>add UserPropertyDefn to its parent and check it</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get right Object</li>
	 * <li>get right Object from parent</li>
	 * </ul>
	 */
	public void testGetUserProperty() {
		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("MyProperty");//$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);

		Object o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);
		o = designElement.getLocalUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);

		TextItem parent = new TextItem();
		parent.setName("parent");//$NON-NLS-1$
		designElement.dropUserPropertyDefn(uDefn);
		parent.addUserPropertyDefn(uDefn);
		designElement.setExtendsElement(parent);
		o = parent.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);
		o = designElement.getUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertEquals(uDefn, o);
		o = designElement.getLocalUserPropertyDefn("MyProperty");//$NON-NLS-1$
		assertNull(o);
	}

	/**
	 * Test validatePropertyValue( String, Object ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>validate value when propName is not exist</li>
	 * <li>validate value when propName is exist</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>throw out exception</li>
	 * <li>validate right</li>
	 * </ul>
	 *
	 * @throws Exception
	 */
	/*
	 * public void testValidatePropertyValue( ) throws Exception { try {
	 * designElement .validatePropertyValue( design, "Hello world", "hello" );
	 * //$NON-NLS-1$ //$NON-NLS-2$ } catch ( Exception e ) { assertTrue( e
	 * instanceof PropertyNameException ); }
	 *
	 * designElement.setProperty( DesignElement.DISPLAY_NAME_PROP, "report"
	 * );//$NON-NLS-1$ }
	 */

	/**
	 * Test getDisplayName( ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>set displayname property</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get right value "Hello"</li>
	 * </ul>
	 */
	public void testGetDisplayName() {
		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "Hello"); //$NON-NLS-1$
		Object o = designElement.getDisplayName();
		assertEquals("Hello", o); //$NON-NLS-1$
	}

	/**
	 * Test getDisplayNameID( ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>set displaynameid property</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>get right value "111"</li>
	 * </ul>
	 */
	public void testGetDisplayNameID() {
		designElement.setProperty(DesignElement.DISPLAY_NAME_ID_PROP, "111"); //$NON-NLS-1$
		Object o = designElement.getDisplayNameID();
		assertEquals("111", o); //$NON-NLS-1$
	}

	/**
	 * Test addDerived( DesignElement ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>add parent</li>
	 * <li>drop parent</li>
	 * <li>add parent</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain descendent</li>
	 * <li>don't contain descendent</li>
	 * <li>contain descendent</li>
	 * </ul>
	 */
	public void testAddDerived() {
		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		designElement.setExtendsElement(parent);
		Object o = parent.getDescendents().get(0);
		assertEquals(designElement, o);

		// drop parent

		parent.dropDerived(designElement);
		assertFalse(parent.getDescendents().contains(designElement));

		// add parent

		CoreTestUtil.addDerived(parent, designElement);
		assertTrue(parent.getDescendents().contains(designElement));
	}

	/**
	 * Test dropDerived( DesignElement ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>add parent</li>
	 * <li>drop parent</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain descendent</li>
	 * <li>don't contain descendent</li>
	 * </ul>
	 */
	public void testDropDerived() {
		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		designElement.setExtendsElement(parent);
		Object o = parent.getDescendents().get(0);
		assertEquals(designElement, o);

		// drop parent

		parent.dropDerived(designElement);
		assertFalse(parent.getDescendents().contains(designElement));
	}

	/**
	 * Test setExtends( DesignElement ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>set parent</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>contain descendent</li>
	 * </ul>
	 */
	public void testSetExtends() {
		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		designElement.setExtendsElement(parent);
		assertTrue(parent.getDescendents().contains(designElement));
	}

	/**
	 * Test getDescendents( ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>direct check arraylist size</li>
	 * <li>set parent</li>
	 * <li>set ancestor</li>
	 * <li>drop parent , and check size of ancestor's arraylist</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>0</li>
	 * <li>contain descendent</li>
	 * <li>size of ancestor's arraylist is 2</li>
	 * <li>size of ancestor's arraylist is 1</li>
	 * </ul>
	 */
	public void testGetDescendents() {
		assertEquals(0, designElement.getDescendents().size());

		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		designElement.setExtendsElement(parent);
		assertTrue(parent.getDescendents().contains(designElement));

		// set ancestor

		TextItem grand = new TextItem();
		grand.setName("grand"); //$NON-NLS-1$
		parent.setExtendsElement(grand);
		assertTrue(grand.getDescendents().contains(parent));
		assertTrue(grand.getDescendents().contains(designElement));
		assertTrue(parent.getDescendents().contains(designElement));
		assertEquals(1, parent.getDescendents().size());
		assertEquals(2, grand.getDescendents().size());

		// drop parent , and test size of arraylist

		grand.dropDerived(parent);
		designElement.setExtendsElement(grand);
		assertTrue(grand.getDescendents().contains(designElement));
		assertFalse(parent.getDescendents().contains(designElement));

		assertEquals(0, parent.getDescendents().size());
		assertEquals(1, grand.getDescendents().size());
	}

	/**
	 * Test gatherDescendents( ArrayList ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>no descendents</li>
	 * <li>add descendent</li>
	 * <li>add ancestor</li>
	 * <li>drop ancestor and add two parents</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>don't contain element</li>
	 * <li>contain element</li>
	 * <li>contain two elements</li>
	 * <li>both contain element</li>
	 * </ul>
	 */
	public void testGatherDescendents() {

		// no descendents

		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		ArrayList list = new ArrayList();
		parent.gatherDescendents(list);
		assertFalse(list.contains(designElement));

		// add descendent

		designElement.setExtendsElement(parent);
		parent.gatherDescendents(list);
		assertTrue(list.contains(designElement));

		// add ancestor

		TextItem grand = new TextItem();
		grand.setName("grand"); //$NON-NLS-1$
		parent.setExtendsElement(grand);
		list.clear();
		grand.gatherDescendents(list);
		assertTrue(list.contains(parent));
		assertTrue(list.contains(designElement));
		list.clear();
		parent.gatherDescendents(list);
		assertTrue(list.contains(designElement));

		// drop ancestor and add two parents

		designElement.setExtendsElement(grand);
		list.clear();
		grand.gatherDescendents(list);
		assertTrue(list.contains(designElement));
		list.clear();
		parent.gatherDescendents(list);
		assertFalse(list.contains(designElement));
	}

	/**
	 * Test hasUserProperties( ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>add UserPropertyDefn</li>
	 * <li>drop UserPropertyDefn</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>has not user property</li>
	 * <li>has user property</li>
	 * </ul>
	 */
	public void testHasUserProperties() {
		assertFalse(designElement.hasUserProperties());

		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("MyProperty"); //$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);
		assertTrue(designElement.hasUserProperties());

		designElement.dropUserPropertyDefn(uDefn);
		assertFalse(designElement.hasUserProperties());
	}

	/**
	 * Test hasLocalPropertyValues( ).
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>direct check propValues</li>
	 * <li>set display name value</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>false</li>
	 * <li>true</li>
	 * </ul>
	 */
	public void testHasLocalPropertyValues() {
		assertFalse(designElement.hasLocalPropertyValues());
		designElement.setProperty(DesignElement.DISPLAY_NAME_PROP, "hello"); //$NON-NLS-1$
		assertTrue(designElement.hasLocalPropertyValues());
	}

	/**
	 * Test getUserProperties( ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>direct check arraylist size</li>
	 * <li>add UserPropertyDefn</li>
	 * <li>add ancestor with UserPropertyDefn</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>0</li>
	 * <li>1</li>
	 * <li>2</li>
	 * </ul>
	 */
	public void testGetUserProperties() {
		assertEquals(0, designElement.getUserProperties().size());

		UserPropertyDefn uDefn = new UserPropertyDefn();
		uDefn.setName("property"); //$NON-NLS-1$
		designElement.addUserPropertyDefn(uDefn);
		Collection collection = designElement.getUserProperties();
		assertTrue(collection.size() == 1);

		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$

		UserPropertyDefn defn = new UserPropertyDefn();
		defn.setName("parent property"); //$NON-NLS-1$
		parent.addUserPropertyDefn(defn);
		designElement.setExtendsElement(parent);

		Collection collect = designElement.getUserProperties();
		assertTrue(collect.size() == 2);
	}

	/**
	 * Test isKindOf( DesignElement ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>set parent element and check element</li>
	 * <li>set ancestor element and check element</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>isKindOf is true</li>
	 * <li>after set ancestor , isKindOf is true</li>
	 * </ul>
	 */
	public void testIsKindOf() {
		// test parent

		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		designElement.setExtendsElement(parent);
		assertTrue(designElement.isKindOf(parent));

		// test ancestor

		TextItem grand = new TextItem();
		grand.setName("grand"); //$NON-NLS-1$
		assertFalse(designElement.isKindOf(grand));
		parent.setExtendsElement(grand);
		assertTrue(designElement.isKindOf(grand));
	}

	/**
	 * Test hasDerived( ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>no derive</li>
	 * <li>set derive</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>false</li>
	 * <li>true</li>
	 * </ul>
	 */
	public void testHasDerived() {
		// no derive

		TextItem parent = new TextItem();
		parent.setName("parent"); //$NON-NLS-1$
		assertFalse(parent.hasDerived());

		// set derive

		designElement.setExtendsElement(parent);
		assertTrue(parent.hasDerived());
	}

	/**
	 * Test getDoubleProperty( String ).
	 * <p>
	 * Test Cases:
	 * <ul>
	 * <li>the property value is null and check float property</li>
	 * <li>the type of property value is integer and check float property</li>
	 * <li>the property type is double</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>double value 0.0</li>
	 * <li>double value 2</li>
	 * <li>double value 2.5</li>
	 * </ul>
	 */
	public void testGetDoubleProperty() {
		// the property value is null
		assertNull(designElement.getProperty(design, ReportDesign.DISPLAY_NAME_PROP));
		double o = designElement.getFloatProperty(design, ReportDesign.DISPLAY_NAME_PROP);
		assertEquals("0.0", Double.toString(o));//$NON-NLS-1$

		// the property type is not double
		// ReportDesign design = new ReportDesign();
		design.setProperty(ReportDesign.REFRESH_RATE_PROP, new Integer(2));
		o = design.getFloatProperty(design, ReportDesign.REFRESH_RATE_PROP);

		assertEquals(Double.toString(2), Double.toString(o));

		// the property type is double
		ListGroup group = new ListGroup();
		group.setProperty(ListGroup.INTERVAL_RANGE_PROP, new Double(2.5));
		o = group.getFloatProperty(design, ListGroup.INTERVAL_RANGE_PROP);
		assertEquals(Double.toString(2.5), Double.toString(o));
	}

	/**
	 * Test get display label of labels.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>label1 has resource-key ,label name and value of display-name</li>
	 * <li>label2 just has label name</li>
	 * <li>label3 has resource-key and label name but hasnot value of
	 * display-name</li>
	 * <li>this label hasnot label name but has resource-key and value of
	 * display-name</li>
	 * <li>this label hasnot label name and value of display-name, just has
	 * resource-key</li>
	 * <li>this label has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this label has text attribute and value is blue , it also has label name
	 * hexingjie</li>
	 * <li>this label has text attribute and value is blue , but hasnot label
	 * name</li>
	 * <li>this label has text attribute and value is blue , and length more than 30
	 * chars</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>Label2</li>
	 * <li>Label3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>Label2</li>
	 * <li>Label3</li>
	 * <li>Ant</li>
	 * <li>label</li>
	 * <li>label</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>Label2</li>
	 * <li>Label3</li>
	 * <li>Ant</li>
	 * <li>label</li>
	 * <li>label</li>
	 * <li>hexingjie("blue")</li>
	 * <li>label("blue")</li>
	 * <li>label("test asdf sadf sadf sdaf...")</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testLabelDisplayLabel() throws Exception {
		openDesign(DISPLAY_LABEL_FILE_NAME, TEST_LOCALE);
		assertEquals(0, design.getErrorList().size());

		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LABEL, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LABEL, 1, "label2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LABEL, 2, "label3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LABEL, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LABEL, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.LABEL, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LABEL, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LABEL, 1, "label2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LABEL, 2, "label3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LABEL, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LABEL, 4, "\u6807\u7b7e", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.LABEL, 5, "\u6807\u7b7e", DesignElement.SHORT_LABEL); //$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LABEL, 0, "he", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LABEL, 1, "label2", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LABEL, 2, "label3", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LABEL, 3, "Ant", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LABEL, 4, "\u6807\u7b7e", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.LABEL, 5, "\u6807\u7b7e", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LABEL, 6, "hexingjie(\"blue\")", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LABEL, 7, "\u6807\u7b7e(\"blue\")", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 9
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LABEL, 8, "\u6807\u7b7e(\"test asdf sadf sadf sdaf...\")", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

	}

	/**
	 * Test get display label of texts.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>text1 has resource-key ,text name and value of display-name</li>
	 * <li>text2 just has text name</li>
	 * <li>text3 has resource-key and text name but hasnot value of display-name
	 * </li>
	 * <li>this text hasnot text name but has resource-key and value of
	 * display-name</li>
	 * <li>this text hasnot text name and value of display-name, just has
	 * resource-key</li>
	 * <li>this text has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this text has static-text attribute , it also has text name</li>
	 * <li>this text has static-text attribute , but hasnot text name</li>
	 * <li>this text has static-text attribute , and length is less than 30
	 * chars</li>
	 * <li>this text has static-text attribute , and length is more than 30
	 * chars</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>test2</li>
	 * <li>test3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>text2</li>
	 * <li>text3</li>
	 * <li>Ant</li>
	 * <li>text</li>
	 * <li>text</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>text2</li>
	 * <li>text3</li>
	 * <li>Ant</li>
	 * <li>text</li>
	 * <li>text</li>
	 * <li>hexingjie("This is a sample bit of plain...")</li>
	 * <li>text("hello,12345678910111213141adfa...")</li>
	 * <li>text("This is ")</li>
	 * <li>text("a sample bit of plain...")</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testTextDisplayLabel() throws Exception {
		openDesign(DISPLAY_TEXT_FILE_NAME, TEST_LOCALE);
		assertEquals(0, design.getErrorList().size());

		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TEXT, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TEXT, 1, "text2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TEXT, 2, "text3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TEXT, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TEXT, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.TEXT, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TEXT, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TEXT, 1, "text2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TEXT, 2, "text3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TEXT, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TEXT, 4, "\u6587\u672c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.TEXT, 5, "\u6587\u672c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TEXT, 0, "he", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TEXT, 1, "text2", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TEXT, 2, "text3", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TEXT, 3, "Ant", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TEXT, 4, "\u6587\u672c", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.TEXT, 5, "\u6587\u672c", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TEXT, 6, "hexingjie(\"This is a sample bit of plain...\")", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TEXT, 7, "\u6587\u672c(\"hello,12345678910111213141adfa...\")", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 9
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TEXT, 8, "\u6587\u672c(\"This is \")", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 10
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TEXT, 9, "\u6587\u672c(\"a sample bit of plain...\")", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

	}

	/**
	 * Test get display label of grids.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>grid1 has resource-key ,grid name and value of display-name</li>
	 * <li>grid2 just has label name</li>
	 * <li>grid3 has resource-key and grid name but hasnot value of display-name
	 * </li>
	 * <li>this grid hasnot grid name but has resource-key and value of
	 * display-name</li>
	 * <li>this grid hasnot grid name and value of display-name, just has
	 * resource-key</li>
	 * <li>this grid has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this grid has two columns and three rows attributes</li>
	 * <li>this grid has row attribute</li>
	 * <li>this grid has column attribute</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>grid2</li>
	 * <li>grid3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>grid2(0 x 0)</li>
	 * <li>grid3(0 x 0)</li>
	 * <li>Ant</li>
	 * <li>grid</li>
	 * <li>grid</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>grid2(0 x 0)</li>
	 * <li>grid3(0 x 0)</li>
	 * <li>Ant</li>
	 * <li>grid</li>
	 * <li>grid</li>
	 * <li>hexingjie(3 x 2)</li>
	 * <li>grid(1 x 0)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testGridDisplayLabel() throws Exception {
		openDesign(DISPLAY_GRID_FILE_NAME, TEST_LOCALE);
		assertEquals(0, design.getErrorList().size());

		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.GRID, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.GRID, 1, "grid2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.GRID, 2, "grid3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.GRID, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.GRID, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.GRID, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.GRID, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.GRID, 1, "grid2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.GRID, 2, "grid3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.GRID, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.GRID, 4, "\u7f51\u683c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.GRID, 5, "\u7f51\u683c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.GRID, 0, "he(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.GRID, 1, "grid2(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.GRID, 2, "grid3(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.GRID, 3, "Ant(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.GRID, 4, "\u7f51\u683c(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.GRID, 5, "\u7f51\u683c(0 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.GRID, 6, "hexingjie(3 x 2)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.GRID, 7, "\u7f51\u683c(1 x 0)", DesignElement.FULL_LABEL);//$NON-NLS-1$

	}

	/**
	 * Test get display label of lists.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>list1 has resource-key ,list name and value of display-name</li>
	 * <li>list2 just has label name</li>
	 * <li>list3 has resource-key and list name but hasnot value of display-name
	 * </li>
	 * <li>this list hasnot list name but has resource-key and value of
	 * display-name</li>
	 * <li>this list hasnot list name and value of display-name, just has
	 * resource-key</li>
	 * <li>this list has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this list has data-set attribute</li>
	 * <li>this list has data-set attribute but hasnot name</li>
	 * <li>this list has data-set attribute and length more than 30 chars</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>list2</li>
	 * <li>list3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>list2</li>
	 * <li>list3</li>
	 * <li>Ant</li>
	 * <li>list</li>
	 * <li>list</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>list2</li>
	 * <li>list3</li>
	 * <li>Ant</li>
	 * <li>list</li>
	 * <li>list</li>
	 * <li>hexingjie(firstDataSet)</li>
	 * <li>list(secondDataSet)</li>
	 * <li>list(test asdf sadf sadf sdaf...)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testListDisplayLabel() throws Exception {
		openDesign(DISPLAY_List_FILE_NAME, TEST_LOCALE);
		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LIST, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LIST, 1, "list2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LIST, 2, "list3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LIST, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LIST, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.LIST, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LIST, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LIST, 1, "list2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LIST, 2, "list3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LIST, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LIST, 4, "\u5217\u8868", DesignElement.SHORT_LABEL); //$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.LIST, 5, "\u5217\u8868", DesignElement.SHORT_LABEL); //$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.LIST, 0, "he(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.LIST, 1, "list2(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.LIST, 2, "list3(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.LIST, 3, "Ant(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.LIST, 4, "\u5217\u8868(firstDataSet)", DesignElement.FULL_LABEL); //$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.LIST, 5, "\u5217\u8868(firstDataSet)", DesignElement.FULL_LABEL); //$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LIST, 6, "hexingjie(firstDataSet)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LIST, 7, "\u5217\u8868(secondDataSet)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 9
		// just for FULL_LABEL

		matchSituation(DesignElementTest.LIST, 8, "\u5217\u8868(test asdf sadf sadf sdaf...)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

	}

	/**
	 * Test get display label of datas.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>data1 has resource-key ,data name and value of display-name</li>
	 * <li>data2 just has label name</li>
	 * <li>data3 has resource-key and data name but hasnot value of display-name
	 * </li>
	 * <li>this data hasnot data name but has resource-key and value of
	 * display-name</li>
	 * <li>this data hasnot data name and value of display-name, just has
	 * resource-key</li>
	 * <li>this data has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this data has value-expr attribute and data name</li>
	 * <li>this data has value-expr attribute</li>
	 * <li>this data has value-expr attribute and value-expr more than 30 chars</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>data2</li>
	 * <li>data3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>data2</li>
	 * <li>data3</li>
	 * <li>Ant</li>
	 * <li>data</li>
	 * <li>data</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>data2</li>
	 * <li>data3</li>
	 * <li>Ant</li>
	 * <li>data</li>
	 * <li>data</li>
	 * <li>hexingjie(haha)</li>
	 * <li>data��testtest)</li>
	 * <li>data(test asdf sadf sadf sdaf...)
	 * <li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testDataDisplayLabel() throws Exception {
		openDesign(DISPLAY_DATA_FILE_NAME, TEST_LOCALE);
		assertEquals(0, design.getErrorList().size());

		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.DATA, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.DATA, 1, "data2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.DATA, 2, "data3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.DATA, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.DATA, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.DATA, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.DATA, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.DATA, 1, "data2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.DATA, 2, "data3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.DATA, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.DATA, 4, "\u6570\u636e", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.DATA, 5, "\u6570\u636e", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.DATA, 0, "he", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.DATA, 1, "data2", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.DATA, 2, "data3", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.DATA, 3, "Ant", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.DATA, 4, "\u6570\u636e", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.DATA, 5, "\u6570\u636e", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.DATA, 6, "hexingjie(haha)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.DATA, 7, "\u6570\u636e(testtest)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		matchSituation(DesignElementTest.DATA, 7, "\u6570\u636e(testtest)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 9
		// just for FULL_LABEL

		matchSituation(DesignElementTest.DATA, 8, "\u6570\u636e(test asdf sadf sadf sdaf...)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);
	}

	/**
	 * Test get display label of images.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>image1 has resource-key ,image name and value of display-name</li>
	 * <li>image2 just has label name</li>
	 * <li>image3 has resource-key and image name but hasnot value of
	 * display-name</li>
	 * <li>this image hasnot image name but has resource-key and value of
	 * display-name</li>
	 * <li>this image hasnot image name and value of display-name, just has
	 * resource-key</li>
	 * <li>this image has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this image has uri attribute</li>
	 * <li>this image has image-name attribute</li>
	 * <li>this image has value-expr attribute</li>
	 * <li>this image has uri attribute and length more than 30 chars</li>
	 * <li>this image has image-name attribute and length more than 30 chars</li>
	 * <li>this image has value-expr attribute and length more than 30 chars</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>image2</li>
	 * <li>image3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he(null)</li>
	 * <li>image2(null)</li>
	 * <li>image3(null)</li>
	 * <li>Ant(null)</li>
	 * <li>image(null)</li>
	 * <li>image(null)</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he(null)</li>
	 * <li>image2(null)</li>
	 * <li>image3(null)</li>
	 * <li>Ant(null)</li>
	 * <li>image(null)</li>
	 * <li>image(null)</li>
	 * <li>hexingjie1(Blue He)</li>
	 * <li>hexingjie2(test image)</li>
	 * <li>image(haha)</li>
	 * <li>image(test asdf sadf sadf sdaf...)</li>
	 * <li>image(test asdf sadf sadf...)</li>
	 * <li>image(test asdf sadf...)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testImageDisplayLabel() throws Exception {
		openDesign(DISPLAY_IMAGE_FILE_NAME, TEST_LOCALE);
		// assertEquals( 0, design.getErrors( ).size( ) );

		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.IMAGE, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.IMAGE, 1, "image2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.IMAGE, 2, "image3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.IMAGE, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.IMAGE, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.IMAGE, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.IMAGE, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.IMAGE, 1, "image2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.IMAGE, 2, "image3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.IMAGE, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.IMAGE, 4, "\u56fe\u50cf", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.IMAGE, 5, "\u56fe\u50cf", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.IMAGE, 0, "he(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 2

		matchSituation(DesignElementTest.IMAGE, 1, "image2(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 3

		matchSituation(DesignElementTest.IMAGE, 2, "image3(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 4

		matchSituation(DesignElementTest.IMAGE, 3, "Ant(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 5

		matchSituation(DesignElementTest.IMAGE, 4, "\u56fe\u50cf(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 6

		matchSituation(DesignElementTest.IMAGE, 5, "\u56fe\u50cf(Blue He)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 6, "hexingjie1(Blue He)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 7, "hexingjie2(test image)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 9
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 8, "\u56fe\u50cf(haha)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 10
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 9, "\u56fe\u50cf(test asdf sadf sadf sdaf...)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 11
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 10, "\u56fe\u50cf(test asdf sadf sadf...)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 12
		// just for FULL_LABEL

		matchSituation(DesignElementTest.IMAGE, 11, "\u56fe\u50cf(test asdf sadf...)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

	}

	/**
	 * Test get display label of tables.
	 * <p>
	 * Test Step :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>table1 has resource-key ,table name and value of display-name</li>
	 * <li>table2 just has label name</li>
	 * <li>table3 has resource-key and table name but hasnot value of
	 * display-name</li>
	 * <li>this table hasnot table name but has resource-key and value of
	 * display-name</li>
	 * <li>this table hasnot table name and value of display-name, just has
	 * resource-key</li>
	 * <li>this table has nothing attribute
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>the steps are all the same as Test for USER_LABEL</li>
	 * </ul>
	 * </li>
	 * <li>Test for FULL_LABEL
	 * <ul>
	 * <li>the first six steps are the same as Test for USER_LABEL</li>
	 * <li>this table has data-set attribute</li>
	 * <li>this table has data-set attribute but hasnot name</li>
	 * <li>this table has data-set attribute and length of data-set is more than 30
	 * characters
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * Excepted :
	 * <ul>
	 * <li>Test for USER_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>table2</li>
	 * <li>table3</li>
	 * <li>Ant</li>
	 * <li>null</li>
	 * <li>null</li>
	 * </ul>
	 * </li>
	 * <li>Test for SHORT_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>table2</li>
	 * <li>table3</li>
	 * <li>Ant</li>
	 * <li>table</li>
	 * <li>table</li>
	 * </ul>
	 * </li>
	 * <li>Test for Full_LABEL
	 * <ul>
	 * <li>he</li>
	 * <li>table2</li>
	 * <li>table3</li>
	 * <li>Ant</li>
	 * <li>table</li>
	 * <li>table</li>
	 * <li>hexingjie(firstDataSet)</li>
	 * <li>table(secondDataSet)</li>
	 * <li>table(asdf sadf asdfa...)</li>
	 * </ul>
	 * </li>
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testTableDisplayLabel() throws Exception {
		openDesign(DISPLAY_TABLE_FILE_NAME, TEST_LOCALE);
		/* First test for USER_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TABLE, 0, "he", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TABLE, 1, "table2", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TABLE, 2, "table3", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TABLE, 3, "Ant", DesignElement.USER_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TABLE, 4, null, DesignElement.USER_LABEL);

		// situation 6

		matchSituation(DesignElementTest.TABLE, 5, null, DesignElement.USER_LABEL);

		/* First test for SHORT_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TABLE, 0, "he", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TABLE, 1, "table2", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TABLE, 2, "table3", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TABLE, 3, "Ant", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TABLE, 4, "\u8868\u683c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.TABLE, 5, "\u8868\u683c", DesignElement.SHORT_LABEL);//$NON-NLS-1$

		/* First test for FULL_LABEL */

		// situation 1
		matchSituation(DesignElementTest.TABLE, 0, "he(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 2

		matchSituation(DesignElementTest.TABLE, 1, "table2(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 3

		matchSituation(DesignElementTest.TABLE, 2, "table3(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 4

		matchSituation(DesignElementTest.TABLE, 3, "Ant(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 5

		matchSituation(DesignElementTest.TABLE, 4, "\u8868\u683c(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 6

		matchSituation(DesignElementTest.TABLE, 5, "\u8868\u683c(firstDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		// situation 7
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TABLE, 6, "hexingjie(firstDataSet)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 8
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TABLE, 7, "\u8868\u683c(secondDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		matchSituation(DesignElementTest.TABLE, 7, "\u8868\u683c(secondDataSet)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

		// situation 89
		// just for FULL_LABEL

		matchSituation(DesignElementTest.TABLE, 7, "\u8868\u683c(secondDataSet)", DesignElement.FULL_LABEL);//$NON-NLS-1$

		matchSituation(DesignElementTest.TABLE, 8, "\u8868\u683c(asdf sadf asdfa...)", //$NON-NLS-1$
				DesignElement.FULL_LABEL);

	}

	/**
	 * match result which the special type generates to excepted value.
	 *
	 * @param type        there are seven
	 *                    types:label,text,grid,list,table,image,data
	 * @param number      which sequence element should be selected
	 * @param exceptValue excepted value
	 * @param level       there are three levels 1,USER_LABEL , 2, SHORT_LABEL 3,
	 *                    FULL_LABEL
	 */
	private void matchSituation(int type, int number, String exceptValue, int level) {
		SlotHandle slotHandle = designHandle.getBody();
		SlotHandle reportSlotHandle = ((FreeFormHandle) slotHandle.get(0)).getReportItems();
		String displayLabel = null;

		switch (type) {
		case LABEL:
			LabelHandle lblHandle = (LabelHandle) reportSlotHandle.get(number);
			displayLabel = lblHandle.getDisplayLabel(level);
			break;
		case TEXT:
			TextItemHandle textHandle = (TextItemHandle) reportSlotHandle.get(number);
			displayLabel = textHandle.getDisplayLabel(level);
			break;
		case GRID:
			GridHandle gridHandle = (GridHandle) reportSlotHandle.get(number);
			displayLabel = gridHandle.getDisplayLabel(level);
			break;
		case LIST:
			ListHandle listHandle = (ListHandle) reportSlotHandle.get(number);
			displayLabel = listHandle.getDisplayLabel(level);
			break;
		case DATA:
			DataItemHandle dataHandle = (DataItemHandle) reportSlotHandle.get(number);
			displayLabel = dataHandle.getDisplayLabel(level);
			break;
		case IMAGE:
			ImageHandle imageHandle = (ImageHandle) reportSlotHandle.get(number);
			displayLabel = imageHandle.getDisplayLabel(level);
			break;
		case TABLE:
			TableHandle tableHandle = (TableHandle) reportSlotHandle.get(number);
			displayLabel = tableHandle.getDisplayLabel(level);
			break;
		default:
			break;
		}

		assertEquals(exceptValue, displayLabel);

		slotHandle = null;
		reportSlotHandle = null;
	}

	/**
	 * Tests the case that one styled element has no name, but has style. The value
	 * of name property should be from element not style.
	 */

	public void testGetIntrinsicProperty() {
		TableItem table = new TableItem();
		StyleElement style = new Style("MyStyle"); //$NON-NLS-1$

		table.setStyle(style);
		assertEquals(null, table.getProperty(null, TableItem.NAME_PROP));
	}

	/**
	 * Tests element IDs in the report design.
	 *
	 * @throws SemanticException if setting properties with errors.
	 */

	public void testID() throws SemanticException {
		setupDesign();

		assertEquals(design.getID(), 1);
		assertNotNull(design.getElementByID(1));

		// Test with IDs.

		MetaDataDictionary.getInstance().enableElementID();
		setupDesign();

		assertTrue(design.getID() != 0);

		assertEquals(design.getElementByID(design.getID()), design);

		FreeFormHandle container1 = (FreeFormHandle) designHandle.getComponents().get(0);
		long container1ID = container1.getID();

		assertTrue(container1ID != 0);
		DesignElementHandle handle = designHandle.getElementByID(container1ID);
		assertEquals(handle, container1);

		designHandle.getComponents().dropAndClear(container1);

		assertEquals(container1.getID(), container1ID);

		// after dropping, cannot be found.

		assertNull(designHandle.getElementByID(container1ID));
	}

	/**
	 * Tests element references in the report design.
	 *
	 * @throws SemanticException if setting properties with errors.
	 */

	public void testElementRef() throws SemanticException {
		setupDesign();

		LabelHandle label1 = (LabelHandle) designHandle.findElement("Label 1"); //$NON-NLS-1$
		DataSetHandle dataSet1 = designHandle.findDataSet("Data Set 1"); //$NON-NLS-1$

		label1.setProperty(ReportItem.DATA_SET_PROP, dataSet1.getName());

		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), dataSet1.getName());
		assertEquals(label1.getElementProperty(ReportItem.DATA_SET_PROP), dataSet1);

		// Rename element: reference should change

		dataSet1.setName("New Name"); //$NON-NLS-1$

		assertEquals(dataSet1.getName(), "New Name"); //$NON-NLS-1$
		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), dataSet1.getName());
		assertEquals(label1.getElementProperty(ReportItem.DATA_SET_PROP), dataSet1);

		// Delete element: reference should clear

		DataSourceHandle dataSource1 = designHandle.findDataSource("Data Source 1"); //$NON-NLS-1$
		DataSource dataSource = (DataSource) dataSource1.getElement();

		assertEquals(1, dataSource.getClientList().size());

		dataSet1.dropAndClear();

		assertEquals(0, dataSource.getClientList().size());

		assertNull(label1.getStringProperty(ReportItem.DATA_SET_PROP));
		assertNull(label1.getElementProperty(ReportItem.DATA_SET_PROP));

		// Set to unresolved

		label1.setProperty(ReportItem.DATA_SET_PROP, "Data Set 1"); //$NON-NLS-1$

		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), "Data Set 1"); //$NON-NLS-1$
		assertNull(label1.getElementProperty(ReportItem.DATA_SET_PROP));

		// Create new class for unresolved

		dataSet1 = designHandle.getElementFactory().newOdaDataSet("Data Set 1"); //$NON-NLS-1$

		designHandle.getDataSets().add(dataSet1);
		dataSet1.setDataSource(dataSource1.getName());

		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), "Data Set 1"); //$NON-NLS-1$

		// Do semantic check implicitly when getting the element

		assertEquals(label1.getElementProperty(ReportItem.DATA_SET_PROP), dataSet1);
		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), dataSet1.getName());

		// Ensure is now resolved by renaming

		dataSet1.setName("New Name"); //$NON-NLS-1$

		assertEquals(label1.getElementProperty(ReportItem.DATA_SET_PROP), dataSet1);
		assertEquals(label1.getStringProperty(ReportItem.DATA_SET_PROP), dataSet1.getName());
		assertEquals(1, dataSource.getClientList().size());

		dataSet1.dropAndClear();

		// data set is dropped. Related element and back references must be
		// cleaned.

		assertEquals(0, dataSource.getClientList().size());
		assertNull(label1.getDataSet());

		designHandle.getCommandStack().undo();

		// after undo, element and back references must be restored.

		assertEquals(dataSet1, label1.getDataSet());
		assertEquals(1, dataSource.getClientList().size());

		// another redo to drop the data set again.

		designHandle.getCommandStack().redo();

		assertNull(label1.getDataSet());
		assertEquals(0, dataSource.getClientList().size());

		dataSource1.dropAndClear();
	}

	/**
	 *
	 * @throws SemanticException
	 */
	public void testIsContentOf() throws SemanticException {
		setupDesign();

		LabelHandle label1 = (LabelHandle) designHandle.findElement("Label 1"); //$NON-NLS-1$

		assertTrue(label1.getElement().isContentOf(label1.getElement()));
		assertTrue(label1.getElement().isContentOf(designHandle.getElement()));
		assertFalse(designHandle.getElement().isContentOf(label1.getElement()));

	}

	/**
	 * Creates a design for <code>{@link #testID()}</code> and
	 * <code>{@link #testElementRef()}</code>.
	 *
	 * @throws SemanticException
	 */

	private void setupDesign() throws SemanticException {
		createDesign(null);

		ElementFactory factory = designHandle.getElementFactory();

		FreeFormHandle container1 = factory.newFreeForm("Container 1");//$NON-NLS-1$
		designHandle.getComponents().add(container1);

		LabelHandle label1 = factory.newLabel("Label 1");//$NON-NLS-1$
		container1.getReportItems().add(label1);
		LabelHandle label2 = factory.newLabel("Label 2");//$NON-NLS-1$
		container1.getReportItems().add(label2);
		LabelHandle label3 = factory.newLabel("Label 3");//$NON-NLS-1$
		container1.getReportItems().add(label3);

		FreeFormHandle container2 = factory.newFreeForm("Container 2");//$NON-NLS-1$
		designHandle.getBody().add(container2);

		FreeFormHandle container3 = factory.newFreeForm("Container 3");//$NON-NLS-1$
		designHandle.getBody().add(container3);

		FreeFormHandle container4 = factory.newFreeForm(null);
		designHandle.getBody().add(container4);

		StyleHandle style1 = factory.newStyle("Style-1");//$NON-NLS-1$
		designHandle.getStyles().add(style1);

		StyleHandle style2 = factory.newStyle("Style-2");//$NON-NLS-1$
		designHandle.getStyles().add(style2);

		StyleHandle style3 = factory.newStyle("Style-3");//$NON-NLS-1$
		designHandle.getStyles().add(style3);

		DataSourceHandle dataSource1 = factory.newOdaDataSource("Data Source 1");//$NON-NLS-1$
		designHandle.getDataSources().add(dataSource1);

		DataSourceHandle dataSource2 = factory.newOdaDataSource("Data Source 2");//$NON-NLS-1$
		designHandle.getDataSources().add(dataSource2);

		DataSetHandle dataSet1 = factory.newOdaDataSet("Data Set 1"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSet1);
		dataSet1.setDataSource(dataSource1.getName());

		MasterPageHandle masterPage1 = factory.newGraphicMasterPage("My Page"); //$NON-NLS-1$
		designHandle.getMasterPages().add(masterPage1);

		container1.setStyle((SharedStyleHandle) style2);
		label1.setStyle((SharedStyleHandle) style2);
		label1.setDataSet(dataSet1);

		design.setProperty(ReportDesign.AUTHOR_PROP, "Bob the Builder");//$NON-NLS-1$
		design.setProperty(ReportDesign.COMMENTS_PROP, "Sample design");//$NON-NLS-1$

	}

	/**
	 * Tests element identifier
	 *
	 * @throws Exception if any exception
	 */

	public void testElementIdentifier() throws Exception {
		createDesign();

		// Create one label without name

		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		assertEquals("Label", label.getElement().getIdentifier()); //$NON-NLS-1$

		// Add it into body

		designHandle.getBody().add(label);
		assertEquals("report.Body[0]", label.getElement().getIdentifier()); //$NON-NLS-1$

		// Create one table with name

		TableHandle table1 = designHandle.getElementFactory().newTableItem("table1", 3, 0, 3, 0); //$NON-NLS-1$
		assertEquals("Table(\"table1\")", table1.getElement().getIdentifier()); //$NON-NLS-1$

		RowHandle row = (RowHandle) table1.getDetail().get(0);
		CellHandle cell = (CellHandle) row.getCells().get(0);
		assertEquals("Table(\"table1\").Detail[0].Cells[0]", cell.getElement().getIdentifier()); //$NON-NLS-1$

		// Add it into body

		designHandle.getBody().add(table1);

		assertEquals("Table(\"table1\")", table1.getElement().getIdentifier()); //$NON-NLS-1$
		assertEquals("Table(\"table1\").Detail[0].Cells[0]", cell.getElement().getIdentifier()); //$NON-NLS-1$

		// Create one table without name

		TableHandle table2 = designHandle.getElementFactory().newTableItem(null, 3, 0, 3, 0);
		assertEquals("Table", table2.getElement().getIdentifier()); //$NON-NLS-1$

		row = (RowHandle) table2.getDetail().get(0);
		cell = (CellHandle) row.getCells().get(0);
		assertEquals("Table.Detail[0].Cells[0]", cell.getElement().getIdentifier()); //$NON-NLS-1$

		// Add it into body

		designHandle.getBody().add(table2);

		assertEquals("report.Body[2]", table2.getElement().getIdentifier()); //$NON-NLS-1$
		assertEquals("report.Body[2].Detail[0].Cells[0]", cell.getElement().getIdentifier()); //$NON-NLS-1$

	}

	/**
	 * Tests the resolveExtends() method and ElementExtendsValidator.
	 *
	 * @throws Exception
	 */

	public void testExtendsValidatorAndResolve() throws Exception {
		openDesign("DesignElementTest_8.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		// design image is un-resolved since the library is not found

		assertEquals(2, designHandle.getErrorList().size());
		ErrorDetail error = (ErrorDetail) designHandle.getErrorList().get(0);
		assertEquals(InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND, error.getErrorCode());
		error = (ErrorDetail) designHandle.getErrorList().get(1);
		assertEquals(InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND, error.getErrorCode());

		// add the library and find the design image is resolved

		designHandle.includeLibrary("Library_1.xml", "lib"); //$NON-NLS-1$//$NON-NLS-2$
		ImageHandle libImage = (ImageHandle) designHandle.getLibrary("lib").findElement("libImage"); //$NON-NLS-1$ //$NON-NLS-2$
		ImageHandle designImage = (ImageHandle) designHandle.findElement("designImage"); //$NON-NLS-1$
		designImage.setExtendsName("lib.libImage"); //$NON-NLS-1$
		assertEquals(libImage, designImage.getExtends());

	}

	/**
	 * Tests the name-manager.
	 *
	 * @throws Exception
	 */
	public void testNameManager() throws Exception {
		openDesign("DesignElementTest_1.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		ScalarParameterHandle param = designHandle.getElementFactory().newScalarParameter(null);
		assertNotNull(param.getName());
		assertEquals(param.getElement(), ((AbstractNameHelper) design.getNameHelper())
				.getCachedNameSpace(Module.PARAMETER_NAME_SPACE).getElement(param.getName()));

		// add the parameter into the design and find that
		// the cached name in the name manger is cleared.

		designHandle.getParameters().add(param);
		assertNull(((AbstractNameHelper) design.getNameHelper()).getCachedNameSpace(Module.PARAMETER_NAME_SPACE)
				.getElement(param.getName()));
	}

	/**
	 * Tests the local style property values.
	 */
	public void testStyleLocalPropertyValues() throws Exception {
		openDesign("LocalStylePropertyValuesTest.xml");//$NON-NLS-1$

		// the label in the report design contains one style and its parent in
		// the library contains different style.
		DesignElementHandle label1 = designHandle.findElement("NewLabel"); //$NON-NLS-1$
		assertTrue(label1.hasLocalProperties());

		// the label in the report design does not contain one style and its
		// parent in the library contains one style.
		DesignElementHandle label2 = designHandle.findElement("NewLabel1"); //$NON-NLS-1$
		assertFalse(label2.hasLocalProperties());

		// test this extended-item hasLocalValue return TRUE for it has other
		// local non-layout property set except layout property 'header'
		DesignElementHandle extendedItem = designHandle.findElement("test-box-1"); //$NON-NLS-1$
		assertTrue(extendedItem.hasLocalProperties());

		// this extended-item hasLocalValue return FALSE for it has no other
		// local value set except the layout property 'header'
		extendedItem = designHandle.findElement("test-box-2"); //$NON-NLS-1$
		assertFalse(extendedItem.hasLocalProperties());
	}

	/**
	 * Tests the display lable of the ROM defined selector.
	 *
	 * @throws Exception
	 */

	public void testSelectorDisplayLabel() throws Exception {
		ThreadResources.setLocale(TEST_LOCALE);
		createDesign(TEST_LOCALE);

		StyleHandle style = designHandle.getElementFactory().newStyle("grid"); //$NON-NLS-1$
		assertEquals("\u7f51\u683c", style.getDisplayLabel()); //$NON-NLS-1$

		style = designHandle.getElementFactory().newStyle("table"); //$NON-NLS-1$
		assertEquals("Table", style.getDisplayLabel()); //$NON-NLS-1$
	}

	/**
	 * Tests the default value for page-break-inside. bugzilla_292425. Row in table
	 * or table group, default is avoid; otherwise default is auto.
	 *
	 * @throws Exception
	 */
	public void testPageBreakInsideInRow() throws Exception {
		createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("testTalbe", 1); //$NON-NLS-1$

		// row in table, default is avoid
		RowHandle row = (RowHandle) table.getHeader().get(0);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));

		// row in table group, default is avoid
		TableGroupHandle groupHandle = factory.newTableGroup();
		row = factory.newTableRow();
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				row.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));
		groupHandle.getHeader().add(row);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));

		// row in grid, default is auto
		GridHandle grid = factory.newGridItem("testGrid"); //$NON-NLS-1$
		row = factory.newTableRow();
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				row.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));
		grid.getRows().add(row);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AUTO,
				row.getStringProperty(IStyleModel.PAGE_BREAK_INSIDE_PROP));
	}

	/**
	 * mock listener.
	 */

	class MyActionListener implements Listener {

		boolean done = false;
		int path = -1;

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.core.Listener#notify(org.eclipse.birt
		 * .report.model.core.DesignElement,
		 * org.eclipse.birt.report.model.activity.NotificationEvent)
		 */
		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			done = true;
			path = ev.getDeliveryPath();
		}
	}

}
