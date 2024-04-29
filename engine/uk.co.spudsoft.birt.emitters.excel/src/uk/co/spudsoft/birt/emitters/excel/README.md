# Eclipse BIRT 
Introduction to use the specialist user properties of the ExcelEmitter.

## Reason
The ExelEmitter provides a set of specialist user properties to optimize the excel output according of the reporting based requirements.

Each of the user properties starts with the master prefix "ExcelEmitter".


### User properties of the ExcelEmitter

The following list get an overview of all supported user properties, the content and the definition of use.

**ExcelEmitter.DEBUG**

	Content   	activate the debug log during the excel file creation
	Location  	report
	Data type 	boolean
	Values	    	true, enable debug log
	          	false, disabled debug log
	Default   	false


**ExcelEmitter.RemoveBlankRows**

	Content   	handling of blank rows
	Location  	report, table, grid, list, row
	Data type   	boolean
	Values	    	true, blank rows will be removed
					false, keep blank rows
	Default   	false

**ExcelEmitter.Rotation**

	Content		rotation of the element
	Location  	text-element of cell
	Data type 	number, float
	Values 		degree value for the rotation of the content
	Default		empty (null)

**ExcelEmitter.ForceAutoColWidths**

	Content   	define if the column width will be calculated through content-length or based on the column width
	Location  	report
	Data type 	boolean
	Values    	true, column width will be calculated through content-lengt (ignore the column width definition)
	           	false, the configured report column width will be used
	Default   	false

**ExcelEmitter.SingleSheet**

	Content    	the created excel will included all data into one excel sheet
	Location   	report
	Data type 	boolean
	Values     	true, all data will be displayed at one sheet (page breaks for new sheets will be ignored)
	           	false, use page breaks to create different sheets
	Default   	false

**ExcelEmitter.SingleSheetWithPageBreaks**

	Content    	the created excel will included all data into one excel sheet
	Location   	report
	Data type 	boolean
	Values    	true, all data will be displayed at one sheet (page breaks for new sheets will be ignored)
	          	false, use page breaks to create different sheets
	Default   	false

**ExcelEmitter.InsertPrintBreakAfter**

	Content    	insert a page break after the marked row  
	Location  	row
	Data type  	boolean
	Values     	true, add page break after the marked row (= create new sheet)
	           	false, no additional page break, default behavior will be used
	Default    	false
	
**ExcelEmitter.DisableGrouping**

	Content   	option to disable the excel grouping	
	Location  	report
	Data type  	boolean
	Values    	true, hide the grouping section of excel
	          	false, show the grouping section of excel
	Default   	false

**ExcelEmitter.StructuredHeader**

	Content   	option to display the header and footer details on excel sheet level
	Location   	report
	Data type 	boolean
	Values    	true, the master page header will be display at first line and footer as last line of each sheet
	          	false, the master page header and footer will be displayed like excel header and footer
	Default   	false

**ExcelEmitter.CustomNumberFormat**

	Content   	define the number format of a numeric value for excel
	Location  	text-element of cell
	Data type  	string
	Values    	custom format which should be used to display the value on excel, e.g. "#,#00.00"
			    	the format use the form of the excel-defined forms (language version: en)
	Default   	empty (null)
	
**ExcelEmitter.AutoFilter**
	
	Content    	activate the auto-filter of excel
	Location   	report
	Data type   	boolean
	Values     	true, add the excel-filter on the first table header section on the last header row
	          	false, hide excel-filter
	Default    	false

**ExcelEmitter.SheetProtectPassword**

	Content   	set a password to an excel sheet
	Location   	table, list
	Data type  	string
	Values    	password to protect the excel file
	Default   	empty (null)

**ExcelEmitter.GroupSummaryHeader**

	Content   	flag indicating whether summary rows appear below detail in an outline, when applying an outline
	Location   	report, table
	Data type  	boolean
	Values    	true, a summary row is inserted below the detailed data being summarized and a new outline level is established on that row
	           	false, a summary row is inserted above the detailed data being summarized and a new outline level is established on that row.
	Default   	false

**ExcelEmitter.FreezePanes**

	Content   	freeze excel rows
	Location   	row
	Data type  	boolean
	Values    	true, based on the marked row the row will be frozen on excel
	            	false, no frozen panes
	Default   	false

**ExcelEmitter.BlankRowAfterTopLevelTable**

	Content   	option to add a blank row after the top level table
	Location  	table
	Data type  	boolean
	Values	    	true, add a blank row after a top level table
	          	false, no additional blank row
	Default    	false

**ExcelEmitter.SpannedRowHeight**

	Content   	define the row height of spanned cells
	Location   	cell
	Data type  	number, float
	Values     	row height of spanned cells
	Default   	0

**ExcelEmitter.NestedTableInLastCell**

	Content   	activate the special handling of nested tables of the last row-cell
	Location   	table
	Data type  	boolean
	Values    	true, activate the nested table handling of the last cell of a row
	          	false, no nested table at last cell of row
	Default   	false

**ExcelEmitter.PrintScale**

	Content   	define the scaling for the print output of the excel file
	Location   	report
	Data type  	number, short
	Values    	the scale value must be -1 (= off) or between 10 and 400
	Default    	-1
	
**ExcelEmitter.PrintPagesWide**

	Content   	configuration of the page width for the excel print out
	Location   	report
	Data type  	number, short
	Values    	page width for the excel print out
	Default   	-1
	
**ExcelEmitter.PrintPagesHigh**

	Content   	configuration of the page height for the excel print out
	Location   	report
	Data type  	number, short
	Values     	page height for the excel print out
	Default   	-1

**ExcelEmitter.DisplayFormulas**

	Content   	display the formulas as text without calculation
	Location   	report, table, grid
	Data type  	boolean
	Values    	true, show formula as text 
	           	false, evaluate the content like formula
	Default    	false

**ExcelEmitter.DisplayGridlines**

	Content   	option to hide the excel grid
	Location   	report
	Data type  	boolean
	Values    	true, show grid lines on excel
	          	false, hide grid lines on excel
	Default   	true

**ExcelEmitter.DisplayRowColHeadings**

	Content   	option to hide the row and column header labels
	Location  	table
	Data type   	boolean
	Values    	true, the column head and row line labels are shown on the excel sheet
	          	false, the column head and row line labels are hidden on the excel sheet
	Default   	true

**ExcelEmitter.DisplayZeros**

	Content   	option to display or hide zeros
	Location  	table
	Data type  	boolean
	Values    	true, show cell value of zero
	          	false, hide cell value of zero
	Default   	true

**ExcelEmitter.ValueAsFormula**

	Content   	the content on an element will be added as excel-formula, for calculation teh string must be start with "="-sign
	Location  	text-element of cell
	Data type   	boolean
	Values    	true, use content like formula
	           	false, use content like text
	Default   	false

**ExcelEmitter.Formula**

	Content   	the content of this property will be added as excel-formula, for calculation teh string must be start with "="-sign
	Location    	text-element of cell
	Data type  	string
	Values    	fixed text or formula for excel cell
	Default    	empty (null)

**ExcelEmitter.TemplateFile**

	Content   	define an excel-template to create the excel
	Location  	report
	Data type  	string
	Values    	full file path (path & filename) to an excel template which should be used for the excel creation
	Default    	empty (null)

**ExcelEmitter.StreamingXlsx**

	Content    	define the technical output method DOM- or streaming-handling
	Location   	report
	Data type  	boolean
	Values     	true, activate the streaming output method
	          	false, standard output handling based on DOM will be used
	Default    	false

**ExcelEmitter.ForceRecalculation**

	Content    	define that excel shall perform a full recalculation when the workbook is opened
	Location   	report
	Data type  	boolean
	Values     	true, activate the recalculation option
	          	false, no recalculation
	Default    	false

**ExcelEmitter.PrintGridlines**

	Content    	activate the print out of the grind lines
	Location   	page, table, list
	Data type  	boolean
	Values     	true, activate grid line print out
	          	false, without grid line print out
	Default    	false

**ExcelEmitter.PrintRowColHeadings**

	Content    	activate the print out of the row and column heading labels
	Location   	page, table, list
	Data type  	boolean
	Values     	true, activate print out of the row and column heading labels
	          	false, without print out of the row and column heading labels
	Default    	false
	
**ExcelEmitter.PrintFitToPage**

	Content    	the page will be fit to center of the print out
	Location   	page, table, list
	Data type  	boolean
	Values     	true, fit the page to center
	          	false, no fitting of the page
	Default    	false
	
**ExcelEmitter.DisplaySheetZoom**

	Content    	define the used scaling for the sheet zoom
	Location   	page, table, list
	Data type  	number, integer
	Values    	the zoom value must be -1 (= off) or between 10 and 400
	Default    	-1
