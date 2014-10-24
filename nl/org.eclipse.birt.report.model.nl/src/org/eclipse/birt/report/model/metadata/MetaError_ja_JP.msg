# Description errors in metadata
MISSING_ELEMENT_NAME=要素をメタデータディクショナリに追加する際に、要素名が見つかりませんでした。

DUPLICATE_ELEMENT_NAME=要素 [{0}] をメタデータディクショナリに追加する際に、重複する要素名が見つかりました。

MISSING_STYLE_NAME=定義済みのスタイルをメタデータディクショナリに追加する際に、スタイル名が見つかりませんでした。

DUPLICATE_STYLE_NAME=定義済みのスタイル [{0}] をメタデータディクショナリに追加する際に、重複するスタイル名が見つかりました。

STYLE_TYPE_MISSING=メタデータディクショナリにスタイル要素が見つかりません。

STYLE_NOT_DEFINED=スタイル付きの要素 [{0}] を定義する前にスタイルを定義する必要があります。

DUPLICATE_PROPERTY=プロパティを要素 [{1}] に追加する際に、重複するプロパティ名 [{0}] が見つかりました。

ELEMENT_PARENT_NOT_FOUND=要素 [{1}] の親 [{0}] が見つかりません。

ILLEGAL_STYLE_PROPS=要素 [{0}] に定義されているスタイルのプロパティが無効です。考えられる原因は次の 2 つです : 1. 要素の "hasStyle" 属性が False になっているにもかかわらず、StyleProperties が定義されている。2. 要素の "hasStyle" 属性は True で、1 つ以上のスロットを持つコンテナとなっているにもかかわらず、StyleProperties が定義されている。

ILLEGAL_ABSTRACT_ELEMENT=要素 [{0}] は抽象のため、その親 [{1}] も抽象である必要があります。

INVALID_NAME_OPTION=要素 [{0}] の名前オプションが無効です。

INVALID_NAME_SPACE=名前領域が無効です。

INVALID_STYLE_PROP_OPTION=サニティチェックを行えませんでした。スタイル要素でない要素 [{0}] には、スタイルのプロパティ [{1}] を定義できません。

STYLE_PROP_NOT_FOUND=要素 [{1}] のスタイルのプロパティ [{0}] が、スタイル要素に見つかりませんでした。

PROP_TYPE_ERROR=プロパティ [{0}] の PropertyType は設定されていません (propDefn.getType() == null)。

MISSING_PROP_CHOICES=choice 型のプロパティ [{0}] の ChoiceSet が見つかりません。

MISSING_SLOT_TYPE=SlotDefn [{0}] のスロットコンテンツの型が見つかりません。

MISSING_SLOT_NAME=スロット [{0}] の displayNameID が見つかりません。

INVALID_SLOT_TYPE=スロット [{1}] のスロットコンテンツの型 [{0}] が無効です。

ELEMENT_NAME_CONST=要素 [{0}] が定義されていません。

MISSING_CHOICE_SET_NAME=ChoiceSet をメタデータディクショナリに追加する際に、ChoiceSet の名前が見つかりませんでした。

DUPLICATE_CHOICE_SET_NAME=ChoiceSet [{0}] をメタデータディクショナリに追加する際に、重複する ChoiceSet の名前が見つかりました。

MISSING_STRUCT_NAME=構造をメタデータディクショナリに追加する際に、構造名が見つかりませんでした。

DUPLICATE_STRUCT_NAME=構造 [{0}] をメタデータディクショナリに追加する際に、重複する構造名が見つかりました。

MISSING_STRUCT_DEFN=[{1}] 内に、構造リスト型プロパティ [{0}] の構造の定義が見つかりません。

UNREFERENCABLE_STRUCT_DEFN=構造の定義が、構造の参照型プロパティ [{0}] を参照できません。

MISSING_ELEMENT_TYPE=要素の参照型プロパティ [{0}] の要素の参照が見つかりません。

UNDEFINED_ELEMENT_TYPE=プロパティ [{1}] で参照している要素 [{0}] が定義されていません。

UNNAMED_ELEMENT_TYPE=プロパティ [{1}] で参照している要素 [{0}] には、名前が付いていません (名前空間 == ''no_namespace'')。

INCONSISTENT_PROP_TYPE=プロパティ [{0}] に不整合があります。スタイルのプロパティを同時に固有にすることはできません。

MISSING_JAVA_CLASS=要素 [{0}] は抽象ではありません。Java クラスの属性を指定する必要があります。

JAVA_CLASS_INITIALIZE_ERROR=要素 [{0}] に指定した Java クラスの属性が無効なため、クラス名 [{1}] をインスタンス化できませんでした。その理由として、1) 指定した Java クラスの属性がインターフェースである、2) 抽象クラスである、3) デフォルトのコンストラクタが指定されていない、または 4) 引数を受け付けないコンストラクタが指定されていることが考えられます。

JAVA_CLASS_LOAD_ERROR=要素 [{0}] に指定された Java クラスが無効なため、現在のクラスパスにクラス [{1}] を読み込めませんでした。

INVALID_ELEMENT_JAVA_CLASS=指定した要素の Java クラス [{0}] は、DesignElement クラスのインスタンスではありません。

DUPLICATE_METHOD_NAME=要素 [{0}] に同じ名前 [{1}] のメソッドが 2 つあります。

DUPLICATE_ARGUMENT_NAME=メソッド [{0}] に同じ名前 [{1}] の引数が 2 つあります。

INVALID_DEFAULT_VALUE=プロパティ {0} のデフォルト値 {1} は無効です。

MISSING_CLASS_NAME=クラスをメタデータディクショナリに追加する際に、クラス名が見つかりませんでした。

DUPLICATE_CLASS_NAME=クラス [{0}] をメタデータディクショナリに追加する際に、重複するクラス名が見つかりました。

MISSING_METHOD_NAME=メソッドをクラスに追加する際に、メソッド名が見つかりませんでした。

CONSTRUCTOR_EXISTING=コンストラクタはすでに存在します。

DUPLICATE_MEMBER_NAME=メンバをクラスに追加する際に、重複するメンバ名が見つかりました。

MISSING_MEMBER_NAME=メンバをクラスに追加する際に、メンバ名が見つかりませんでした。

INVALID_LIST_TYPE=[{0}] のリストはサポートされていません。

VALIDATOR_NOT_FOUND=プロパティ [{1}] の検証コンポーネント [{0}] がディクショナリに見つかりません。

MISSING_VALIDATOR_NAME=検証コンポーネントに名前が付いていません。

DUPLICATE_VALIDATOR_NAME=検証コンポーネントの名前 [{0}] はすでに使用されています。

MISSING_EXTENSION_NAME=拡張子をメタデータディクショナリに追加する際に、拡張子名が見つかりませんでした。

DUPLICATE_EXTENSION_NAME=拡張子 [{0}] をメタデータディクショナリに追加する際に、重複する拡張子名が見つかりました。

EXTENSION_POINT_NOT_FOUND=拡張ポイント [{0}] が見つかりません。

FAILED_TO_CREATE_INSTANCE=クラス [{0}] をインスタンス化できません。

VALUE_REQUIRED=[{0}] に値を指定する必要があります。

DUPLICATE_CHOICE_NAME=[{0}] という名前の選択項目は、すでに使用されています。

INVALID_PROPERTY_TYPE=プロパティの種類 [{0}] が無効です。

INVALID_UNIT=プロパティ "{0}" のデフォルトの単位 "{1}" は無効です。

ENCYRPTION_EXTENSION_EXISTS=暗号化拡張 (ID = "{0}") はすでに存在します。

VISIBILITY_PROPERTY_NOT_FOUND=Property visibility:[{1}] on element: [{0}] doesn't point to an exsiting property.

REPORT_MISSING=ルート要素 : レポートデザインが定義されていません。

INVALID_CHOICE_VALUE=プロパティ "{1}" に指定された選択値 "{0}" は無効です。

MISSING_SUB_TYPE="{0}" はリスト型のプロパティですが、サブタイプがありません。

UNSUPPORTED_SUB_TYPE=リスト型のプロパティ "{0}" にあるサブタイプ "{1}" は使用できません。

SUB_TYPE_FORBIDDEN="{0}" はリスト型のプロパティではないため、サブタイプを null にしてください。

MISSING_XML_NAME=具象要素 "{0}" で xml 名が定義されていません。

DUPLICATE_XML_NAME=要素 "{1}" の xml 名 "{0}" は既存の名前と重複しています。

INVALID_ELEMENT_TYPE=要素型 "{0}" が無効です。

INVALID_CHOICE_PROPERTY=拡張 "{1}" の choice プロパティ "{0}" が無効です。detailType と、独自の選択リストの両方が定義されています。

EXTENDED_ITEM_MISSING=メタデータディクショナリに拡張項目が見つかりません。

DEFAULT_ENCRYPTION_EXIST=デフォルトの暗号化はすでに "{1}" に指定されているため、"{0}" をデフォルトの暗号化には設定できません。

DEFAULT_EMPTY_STYLE_NAME=拡張要素 "{0}" のデフォルトのスタイルは空白にできません。

EMPTY_DELTA_ROM_STREAM=拡張子要素 "{0}" からクラス "{1}" で返された デルタ ROM 入力ストリームは空白にできません。