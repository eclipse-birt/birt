###########################################################
#Solution reports descriptions

#Listing
ProductCatalog=打印 Classic Models 产品目录（按产品类别分组）。提供产品名称、成本和说明信息。示范某一个级别的分组并使用表行内的网格构建间距。此外还显示所包含的图像，及如何使用文本项中的 <value-of> 标记以包括数据库列的内容。最后，报表利用样式简化维护，实现外观一致。 
TopSellingProducts=显示一个饼图，该图显示每个生产线的收益。列出最畅销的产品并按收入排序。示范如何使用饼图并对结果集排序。此外还显示所包含的图像以及如何使用网格和表组织报表内容。最后，报表利用样式简化维护，实现外观一致。 
TopNPercent=利用在 BIRT 2.0 中添加的前 n 个且百分比最佳的筛选器的性能，此报表显示的条形图包括收入最高的前 N 个客户，并根据收入按客户名称排序列出了前 M% 的客户。此过程要求多个数据通道，但所有这些皆由 BIRT 报表引擎“在无形之下”完成了。N 和 M 被作为参数传递到报表以允许报表用户选择运行时的数量和百分比。 
StaticCrosstab=本示例说明如何使用 BIRT Total 函数生成静态交叉表报表。报表按季度显示 Classic Models Inc 当前销售的每个生产线的年度销售额。此外，总计通过水平和垂直维数聚合。BIRT Total 函数可接受筛选聚合数据的参数作为第二个参数。本示例中，此功能仅用于显示适合产品供应的值，如“火车”。“总计”函数中的第一个参数为要聚合的值，在本示例中该值被设置为已售数量乘以各个生产线的价格。第二个参数为过滤器。因此，如果筛选器被指定为 dataSetRow["Products::PRODUCTLINE"]=="Motorcycles"，将仅对摩托车生产线进行聚合。 

#Business Forms
SalesInvoice=打印选定订单的发票，包括客户和发票详细信息以及所订购的产品。示范如何使用参数选择要开票的订单及如何使用多个计算字段（包括折扣和订单总计）的表达式。使用表达式生成客户地址字符串，说明如何使用 javascript 函数替换抑制数据库字段中的 null。此外还显示包括的图像，以及如何准确使用网格和表组织报表内容。最后，报表利用样式简化维护，实现外观一致。 

###########################################################
#Reporting Feature Examples descriptions

#XML Data Source
XMLDS_After=一个示范 XML 数据源用法的简单报表。 

#Libraries
Newsfeeds=本示例示范如何使用 BIRT 生成可重复使用的组件报表库。本示例通过创建库开始入手。利用 URI 对 Eclipse RSS Newsfeed 的数据作为报表的数据源并将其存储在库中。然后，将库发布到某个资源文件夹上。本示例说明如何在库中创建不可见项。请注意，可见项也可在库中创建和存储。 
Newsfeeds_After=本示例示范如何使用 BIRT 生成可重复使用的组件报表库。本示例通过创建库开始入手。利用 URI 对 Eclipse RSS Newsfeed 的数据作为报表的数据源并将其存储在库中。然后，将库发布到某个资源文件夹上。本示例说明如何在库中创建不可见项。请注意，可见项也可在库中创建和存储。

#Combination Chart
CustomerOrdersFinal=本示例示范如何在 BIRT 内创建“组合图”。此外，本示例还说明如何使用内部书签和“图表交互编辑器”添加明细数据。组合图以“条形图”格式显示了前 5 个带有未清债务的客户，每个拆线图系列表示一个客户的重叠信用额度。位于图表内的线将与每个客户的订单详细信息链接。 

#Cascading
cascade=本示例示范如何生成 BIRT 报表使用层叠参数组。层叠参数组允许一组参数交互链接，其中第一个参数值的选择会影响到随后参数的选择。层叠参数可与一个或多个数据集连接。数据集将填充各个层叠层。通过与数据集参数组合，将为报表返回数据的筛选提供极大灵活性。

#Drill to details
MasterDetailOneReport=此示例说明如何生成在一个报表中具有主要和详细信息的报表。
OrderMasterAfter=本示例示范如何生成主报表和详细报表。主报表列出所有订单并允许订单用户单击特定订单号钻取选定订单的详细报表。本示例说明如何使用简单参数对详细报表中的结果进行筛选，以及如何使用主报表内的超级链接属性将此参数传输到详细报表。主报表内的超级链接基于查询检索的结果而动态生成。
OrderDetailAfter=详细报表通过单击主报表进行钻取。

#Dynamic Report Parameter
ParameterToDataSetParameter=此示例说明如何将报表参数链接到数据集参数。

#Expressions
Expressions=此示例说明如何生成使用函数的 BIRT 报表。

#Grouping
CustomerListAfter_Grouping=本示例示范如何在 BIRT 中生成分组报表。示例报表显示客户列表并按国家对这些客户进行分组。本示范包含的主要 BIRT 功能是分组功能、分页符功能、目录功能和每组聚合数据或总体聚合数据功能。 

#Highlighting and Conditional Formatting
ProductListAfter=本示例示范如何将突出显示和条件格式添加到 BIRT 报表。开发的“产品”列表报表能显示全部公司产品。示例报表使用替代行颜色并突出显示表示数量低的产品的行。此报表也说明如何添加脚本到行的 onCreate 事件，以便将具有高标记值产品的行的样式修改为斜体。 

#Mapping and Sorting 
CustomerListAfter_Mapping=本示例示范如何使用 BIRT 的“映射”和“排序”功能。映射允许 BIRT 报表元素显示基于表达式的另一个值。例如如果查询返回（1-12）月的数值字段，则可使用此映射功能显示一月 - 十二月。在此示例中我们使用映射显示 Classic Models 客户的信用等级。在 BIRT 中排序用的场合很多而且很灵活。在此示例中我们示范如何将排序表达式添加到与报表参数相联系的“表”元素中。该参数允许输入搜索字符串，此字符串然后用于排序表达式以设置表的排序顺序。 

#Parallel Report 
EmployeeAfter=本示例示范如何用平行区域生成报表。它也使用采集已显示数据的表筛选器进行说明。报表使用网格元素对列进行布局。在每列中添加一组嵌套表以检索员工信息并按位置层次进行分组。这些表格按照办公室代码进行筛选。 

#Query Modification
NullParameter=此示例说明如何使用数据集事件根据报表参数修改查询。例如，如果为报表参数提供了一个值，查询将被修改。 
OrderDetailsAfter=本示例示范如何使用“属性绑定”或“脚本”修改 JDBC 查询。“属性绑定”是 BIRT 中的一个功能，该功能允许对“数据源”和“数据集”公共属性的运行时间进行修改。这可用于将上下文相关的信息添加到任一个元素。作为示例，可对 where 子句进行修改以从会话中检索用户名，或者可根据报表参数在开发和生产间切换数据库 URL。 

#Report Elements
ProductLinesAfter=本示例示范如何在报表设计中使用“标签”、“文本”、“动态文本”、“数据”和“图像元素”。也会对元素间的主要功能和差异进行标识。本示例也说明如何从数据源检索 BLOB 和 CLOB 数据。 

#Sub Report
OrdersAfter=本示例示范如何生成使用嵌套表的报表。绑定到内部表的查询已被参数化以返回外部表中给定订单号的订单细节。本示例也说明如何在嵌套表中引用外部表列。这允许将 BIRT 报表设计为将主明细信息包含在相同的报表中。当外部表正在处理行的时候，会根据来自外部表的信息再执行内部表查询。 

#Sorting
SortTableByReportParameter=本示例说明如何生成使用嵌套表的报表。绑定到内部表的查询已被参数化以返回外部表中给定订单号的订单细节。本示例还说明如何在嵌套表中引用外部表列。这允许将 BIRT 报表设计为将主明细信息包含在相同的报表中。当外部表正在处理其行时，根据来自外部表的信息重新执行内部表查询。 

#Cross tab
CrosstabSampleRevenue=此示例说明如何生成具有交叉表报表项(基于 BIRT 多维数据集)的简单报表。它创建具有沿列区域的产品系列以及沿行区域的年->季度的交叉表。
###########################################################
#Contribute Samples
Contribute_Samples=请将 bugzilla 条目提交到 https://bugs.eclipse.org/bugs/enter_bug.cgi?product=BIRTb&bug_severity=enhancement，附加报表设计文件和报表输出抓图（.png 图像格式、800*600 分辨率、不超过 250 KB）

###########################################################
#Scripting

#Dynamic Map Rule
DynamicMapRule=此示例说明如何调用 beforeFactory 脚本内部的设计引擎 API 将映射规则添加到报表元素。  

#Group Separator
GroupSeparator=此示例说明如何在组中每 3 行添加一个分隔符。  

#Retrieve Session Information
Application_Session_IntegrationBF=此示例说明如何使用 JavaScript 从会话中检索信息。

#Scripted Data Source
ScriptedDatasourceExampleAfter=该部分示例集中在 BIRT 内的脚本上。使用简单报表说明脚本功能。大多数示例都将有可下载的前后报表。此外，示例采用 Flash 演示文稿进行说明。要加载已导出的 java 对象并允许报表使用它，请在打开此示例报表后重新启动 BIRT。  

#Visibility Rule
Addvisrule=此示例说明如何在脚本中添加可见性规则。  

#Chart
SimpleChartAPI=此示例说明简单图表 API 中的许多可用 java 脚本方法。  它们调用自报表的 beforeFactory 事件处理程序。

###########################################################
#Extending BIRT

#Aggreate function extension point
optim=该部分的示例集中在“扩展 BIRT”。此处的示例集中在实施 BIRT 提供的扩展点、使 BIRT 开发人员能够添加到或改变设计者或运行时引擎的默认行为。这包括添加图表类型、聚合函数、数据源和报表项。 


 
