# Eclipse BIRT 
Introduction to use the specialist user properties of the PdfEmitter.

## Reason
The PdfEmitter provides a set of specialist user properties to optimize the excel output according of the reporting based requirements.

Each of the user properties starts with the master prefix "PdfEmitter".


### User properties of the PdfEmitter

The following list get an overview of all supported user properties, the content and the definition of use.

**PdfEmitter.PrependDocumentList**

	Content    	pdf documents which shall be added at the beginning of the produced pdf document,
              	the value must include the full filename in kind of javascript or list like [PDF1,PDF2,PDF...,PDFn]
	Location   	report
	Data type  	string
	Values     	file name with full path or list of file names
	Default    	empty (null)

**PdfEmitter.AppendDocumentList**

	Content    	pdf documents which shall be added at the end of the produced pdf document,
              	the value must include the full filename in kind of javascript or list like [PDF1,PDF2,PDF...,PDFn]
	Location   	report
	Data type  	string
	Values     	file name with full path or list of file names
	Default    	empty (null)

**PdfEmitter.Version**

	Content    	define the version of the created pdf document
	Location   	report
	Data type  	string
	Values     	version number: 1.3 - 1.7
	Default    	1.5

**PdfEmitter.Conformance**

	Content    	define the pdf conformance of the created pdf document, e.g. PDF.A1A
	Location   	report
	Data type  	string
	Values     	conformance values: PDF.Standard, PDF.X1A2001, PDF.X32002, PDF.A1A, PDF.A1B
	Default    	PDF.Standard
	Reference  	see PdfEmitter.IccColorType, PdfEmitter.IccProfileFile, PdfEmitter.PDFA.AddDocumentTitle, PdfEmitter.PDFA.FallbackFont

**PdfEmitter.IccColorType**

	Content    	define the icc color type of the pdf/a document
	Location   	report
	Data type  	string
	Values     	RGB, icc color profile type
             	CMYK, icc color profile type
	Default    	RGB (sRGB IEC61966-2.1)

**PdfEmitter.IccProfileFile**

	Content    	define the icc color file
	Location   	report
	Data type  	string
	Values     	file name with full path or list of file names
	Default    	empty (null)

**PdfEmitter.PDFA.FallbackFont**

	Content    	fall back font to create the pdf/a document correctly,
             	this font will be used if a report font isn't embeddable to the pdf document
	Location   	report
	Data type  	boolean
	Values     	true, add the pdf title
	          	false, avoid the pdf title
	Default    	false

**PdfEmitter.PDFA.AddDocumentTitle**

	Content    	add the report title as pdf title,
             	with the activated title setting the pdf document is not full pdf/a conform due to a openPDF-topic
	Location   	report
	Data type  	boolean
	Values     	true, add the pdf title
	          	false, avoid the pdf title
	Default    	false
