# Description errors in metadata
MISSING_ELEMENT_NAME=将元素添加至元数据词典时缺少元素名称。

DUPLICATE_ELEMENT_NAME=将元素 [{0}] 添加至元数据词典时元素名称重复。

MISSING_STYLE_NAME=将预定义样式添加至元数据词典时缺少样式名称。

DUPLICATE_STYLE_NAME=将预定义样式 [{0}] 添加至元数据词典时样式名称重复。

STYLE_TYPE_MISSING=元数据词典中缺少样式元素。

STYLE_NOT_DEFINED=样式应在具有样式的元素 [{0}] 之前定义。

DUPLICATE_PROPERTY=将属性添加至元素 [{1}] 时与属性名称 [{0}] 重复。

ELEMENT_PARENT_NOT_FOUND=未找到元素 [{1}] 的父元素 [{0}]。

ILLEGAL_STYLE_PROPS=在元素 [{0}] 上定义了非法样式属性。 可能有两个原因：1. 元素的 "hasStyle" 属性为假，但在该元素上定义了 StyleProperties。 2. 元素的 "hasStyle" 属性为真，并且该元素是一个容器（属性框计数大于 0），但在该元素上定义了StyleProperties。

ILLEGAL_ABSTRACT_ELEMENT=元素 [{0}] 属于抽象类型，其父元素 [{1}] 也必须属于抽象类型。

INVALID_NAME_OPTION=元素 [{0}] 的名称选项无效。

INVALID_NAME_SPACE=名称空间无效。

INVALID_STYLE_PROP_OPTION=语义检查失败。 不能在元素 [{0}] 上定义样式属性 [{1}]，除非该元素是样式元素。

STYLE_PROP_NOT_FOUND=样式元素中未找到元素 [{1}] 的样式属性 [{0}]。

PROP_TYPE_ERROR=未设置属性 [{0}] 的属性类型。 ( propDefn.getType() == null )

MISSING_PROP_CHOICES=选项类型属性 [{0}] 缺少 ChoiceSet。

MISSING_SLOT_TYPE=SlotDefn [{0}] 缺少属性框内容类型。

MISSING_SLOT_NAME=属性框 [{0}] 缺少 displayNameID。

INVALID_SLOT_TYPE=属性框 [{1}] 的属性框内容类型 [{0}] 无效。

ELEMENT_NAME_CONST=未定义元素 [{0}]。

MISSING_CHOICE_SET_NAME=将 ChoiceSet 添加至元数据词典时缺少选项集名称。

DUPLICATE_CHOICE_SET_NAME=将选项集 [{0}] 添加至元数据词典时选项集名称重复。

MISSING_STRUCT_NAME=将结构添加至元数据词典时缺少结构名称。

DUPLICATE_STRUCT_NAME=将结构 [{0}] 添加至元数据词典时结构名称重复。

MISSING_STRUCT_DEFN=[{1}] 中的 [{0}]结构列表类型属性缺少结构定义。

UNREFERENCABLE_STRUCT_DEFN=结构定义不能被结构引用类型属性 [{0}] 引用。

MISSING_ELEMENT_TYPE=元素引用类型属性 [{0}] 缺少元素引用。

UNDEFINED_ELEMENT_TYPE=未定义由属性 [{1}] 引用的元素 [{0}]。

UNNAMED_ELEMENT_TYPE=未命名由属性 [{1}] 引用的元素 [{0}] (namespace==''no_namespace'')。

INCONSISTENT_PROP_TYPE=属性 [{0}] 不一致，样式属性不能也为内部属性。

MISSING_JAVA_CLASS=元素 [{0}] 不属于抽象类型，应提供 java 类属性。

JAVA_CLASS_INITIALIZE_ERROR=元素 [{0}] 的 java 类属性无效，并无法实例化类名称 [{1}]，可能由于它是一种接口或属于抽象类，或未提供默认构造函数的指定 java 类（或不带参数的构造函数）。

JAVA_CLASS_LOAD_ERROR=为元素 [{0}] 指定的 java 类属性无效，并无法在当前类路径上加载类 [{1}]。

INVALID_ELEMENT_JAVA_CLASS=指定的元素 java 类 [{0}] 不是设计元素，即此类不属于 DesignElement。

DUPLICATE_METHOD_NAME=元素 [{0}] 具有两个具有相同名称 [{1}] 的方法。

DUPLICATE_ARGUMENT_NAME=方法 [{0}] 具有两个具有相同名称 [{1}] 的参数。

INVALID_DEFAULT_VALUE=属性 {0} 的默认值 {1} 无效。

MISSING_CLASS_NAME=将类添加至元数据词典时缺少类名称。

DUPLICATE_CLASS_NAME=将类 [{0}] 添加至元数据词典时类名称重复。

MISSING_METHOD_NAME=将方法添加至类时缺少方法名称。

CONSTRUCTOR_EXISTING=构造函数已存在。

DUPLICATE_MEMBER_NAME=将成员添加至类时成员名称重复。

MISSING_MEMBER_NAME=将成员添加至类时缺少成员名称。

INVALID_LIST_TYPE=不支持 [{0}] 列表。

VALIDATOR_NOT_FOUND=未在词典中找到属性 [{1}] 的验证程序 [{0}]。

MISSING_VALIDATOR_NAME=缺少验证程序名称。

DUPLICATE_VALIDATOR_NAME=验证程序 [{0}] 的名称与现有名称重复。

MISSING_EXTENSION_NAME=将扩展名添加至元数据词典时缺少扩展名。

DUPLICATE_EXTENSION_NAME=将扩展名 [{0}] 添加至元数据词典时扩展名重复。

EXTENSION_POINT_NOT_FOUND=未找到扩展点 [{0}]。

FAILED_TO_CREATE_INSTANCE=无法实例化类 [{0}]。

VALUE_REQUIRED=要求提供 [{0}] 的值。

DUPLICATE_CHOICE_NAME=名称为 [{0}] 的选项已存在。

INVALID_PROPERTY_TYPE=属性类型 [{0}] 无效。

INVALID_UNIT=属性 "{0}" 的默认单位 "{1}" 无效。

ENCYRPTION_EXTENSION_EXISTS=存在具有 ID“{0}”的加密扩展。

VISIBILITY_PROPERTY_NOT_FOUND=元素 [{0}] 的属性可见性 [{1}] 不指向现有属性。

REPORT_MISSING=根元素: 未定义报表设计。

INVALID_CHOICE_VALUE=属性 "{1}" 的选择值 "{0}" 无效。

MISSING_SUB_TYPE=列表属性“{0}”没有子类型。

UNSUPPORTED_SUB_TYPE=不支持列表属性“{0}”中的子类型“{1}”。

SUB_TYPE_FORBIDDEN=属性“{0}”不是列表属性，因此子类型必须为空。

MISSING_XML_NAME=实际元素“{0}”未定义 xml 名称。

DUPLICATE_XML_NAME=元素“{1}”的 xml 名称“{0}”与现有名称冲突。

INVALID_ELEMENT_TYPE=属性类型“{0}”无效。

INVALID_CHOICE_PROPERTY=扩展“{1}”中的选项属性“{0}”无效，因为它同时定义了详细类型和它自己的选项列表。

EXTENDED_ITEM_MISSING=元数据词典中缺少扩展项。

DEFAULT_ENCRYPTION_EXIST=加密“{0}”不能设置为默认值，因为默认加密“{1}”存在。

DEFAULT_EMPTY_STYLE_NAME=扩展元素“{0}”的默认样式不应为空。

EMPTY_DELTA_ROM_STREAM=类“{1}”从扩展元素“{0}”返回的 delta-rom 输入流不应为空。