# Eclipse BIRT 
Introduction to use the specialized user properties of the WordEmitter.

## Reason
The WordEmitter provides a set of specialized user properties to optimize the word output of type DOCX.

Each of the user properties starts with the master prefix "WordEmitter".


### User properties of the WordEmitter

The following list get an overview of all supported user properties, the content and the definition of use.


**WordEmitter.CombineMarginPadding**

	Content   	use margin and padding combined to calculate the text indent for (dynamic) text elements,
	          	padding is an unsupported feature of MS Word.
	Location  	report
	Data type 	boolean
	Values     	true, margin and padding will combined to calculate the text indent
	          	false, margin will be used for text indent (without padding)
	Default   	true
	Since      	4.18
	Designer    	4.18

**WordEmitter.AddEmptyParagraphTableCell**

	Content   	use empty paragraph for the grid & table cell at the end independent of the cell content
	Location  	report
	Data type 	boolean
	Values     	true, add empty paragraph at grid & table cell end
	          	false, avoid empty paragraph at grid & table cell end
	Default   	true
	Relation   	WordEmitter.WrappedTableForMarginPadding
	Since      	4.18
	Designer    	4.19

**WordEmitter.AddEmptyParagraphForListCell**

	Content   	use empty paragraph for the list table cell at the end independent of the cell content (standard up to BIRT 4.17)
	Location  	report
	Data type 	boolean
	Values     	true, add empty paragraph at list table cell end
	          	false, avoid empty paragraph at list table cell end
	Default   	true
	Since      	4.18
	Designer    	4.18

**WordEmitter.WrappedTableForMarginPadding**

	Content   	text element and dynamic text element should have a wrapper layout table to simulate margin and padding style (standard up to BIRT 4.17)
	Location  	report
	Data type 	boolean
	Values     	true, use wrapper layout table for (dynamic) text elements
	          	false, use optimized handling and avoid wrapper layout table
	Default   	false
	Relation   	WordEmitter.AddEmptyParagraphForAllCells
	Since      	4.18
	Designer    	4.18


**WordEmitter.WrappedTableHeaderFooter**

	Content   	page header and footer should have a wrapper layout table (standard up to BIRT 4.17)
	Location  	report
	Data type 	boolean
	Values     	true, use wrapper layout table for page header and footer
	          	false, use optimized handling and avoid wrapper layout table for page header and footer
	Default   	false
	Since      	4.18
	Designer    	4.18
	