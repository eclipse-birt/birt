***********************************************org.eclipse.birt.report.data.oda.excel.ui(v 1.0)******************************************************
Plugin Name: org.eclipse.birt.report.data.oda.excel.ui
Contributor: Megha Nidhi Dahal (http://www.birt-exchange.org/org/forum/index.php/user/28183-arpan/)
Tags: Native Excel ODA, xls, xlsx, open data access, ODA Framework, Excel ODA, BIRT, Eclipse BIRT

Platform Dependencies: Eclipse v3.6.2 or higher
Plugin Dependencies: POI(http://poi.apache.org/), org.eclipse.ui, org.eclipse.core.runtime, org.eclipse.datatools.connectivity.oda.design.ui, org.eclipse.birt.report.data.oda.excel

*******************************************************PLUGIN DESCRIPTION*******************************************************************
org.eclipse.birt.report.data.oda.excel.ui ODA ui extension supports configuration of a data set over native excel data source. It provides the ui part of Excel ODA.
Supports selective columns(fields/cell) selection and computed columns.

Configuration can be done using only one sheet. However, multiple ';' delimited worksheet names can be passed to the Worksheet(s) parameter of the data set.

*******************************************************DEPLOYMENT***************************************************************************
The plugin is expected to be deployed along with 'org.eclipse.birt.report.data.oda.excel' plugin.
Import both of these plugin projects in excel and export using the plugin manifest file. The exported jars are to be placed in the dropins folder of eclipse installation.

Visit: http://wiki.eclipse.org/Birt_3.7_Migration_Guide#Custom_Extension_Point_Implementations

********************************************************************LIMITATIONS**************************************************************
Supports only one workbook per data set.
Sheets can be identified only using names.
Multiple sheet has to be delimited with ';', hence every ';' delimited text parameter linked to worksheets parameter of the data set will be treated as individual sheets.
Valid sheet names cannot contain ';'

************************************************************************ENHANCEMENTS*********************************************************
Support for multiple workbooks.
An option to indicate the ODA to consider sheet indexes instead of names.
Consider a fragment of data from every sheet, i.e. row start index and end index to be configurable.

***********************************************************************FAQS******************************************************************
Q1: Does this ODA support excel formats other than Microsoft's native format?
A: No.

Q2: Does this ODA support Microsoft Excel 2010 (xlsx) format?
A: Yes.

Q3: Can the listed enhancements be implemented by anyone interested?
A: Yes. There might be many more enhancements that can be implemented not just the ones listed.

Q4: Will this ODA work without the org.eclipse.birt.report.data.oda.excel extension?
A: No.

Q5: Where can be the author of this extension contacted?
A: The author of this plugin can be contacted at: http://www.birt-exchange.org/org/forum/index.php/user/28183-arpan/