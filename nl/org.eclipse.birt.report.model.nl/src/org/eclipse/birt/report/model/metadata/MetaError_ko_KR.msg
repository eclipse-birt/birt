# Description errors in metadata
MISSING_ELEMENT_NAME=메타데이터 사전에 요소를 추가할 때 요소 이름이 누락되었습니다.

DUPLICATE_ELEMENT_NAME=메타데이터 사전에 요소 [{0}]을(를) 추가할 때 요소 이름이 중복됩니다.

MISSING_STYLE_NAME=메타데이터 사전에 미리 정의된 스타일을 추가할 때 스타일 이름이 누락되었습니다.

DUPLICATE_STYLE_NAME=메타데이터 사전에 미리 정의된 스타일 [{0}]을(를) 추가할 때 스타일 이름이 중복됩니다.

STYLE_TYPE_MISSING=메타데이터 사전에서 스타일 요소가 누락되었습니다.

STYLE_NOT_DEFINED=스타일은 스타일 항목 [{0}] 이전에 정의되어야 합니다.

DUPLICATE_PROPERTY=속성을 요소 [{0}]에 추가할 때 속성 이름 [{0}]이(가) 중복됩니다.

ELEMENT_PARENT_NOT_FOUND=요소 [{1}]에 대한 상위 [{0}]이(가) 없습니다.

ILLEGAL_STYLE_PROPS=요소 [{0}]에 요소에서 정의된 잘못된 스타일 속성이 있습니다. 가능한 실패 원인 두 가지: 1. 요소의 "hasStyle" 특성은 거짓 값이지만 요소에서 정의된 StyleProperties가 있습니다. 2. 요소의 "hasStyle" 특성이 참 값이며 컨테이너(슬롯 수>0)이지만 요소에서 정의된 StyleProperties가 있습니다.

ILLEGAL_ABSTRACT_ELEMENT=요소 [{0}]은(는) 추상이며 상위 [{1}]도 추상이어야 합니다.

INVALID_NAME_OPTION=요소 [{0}]에 대한 이름 옵션이 잘못되었습니다.

INVALID_NAME_SPACE=잘못된 네임스페이스입니다.

INVALID_STYLE_PROP_OPTION=온전성 검사에 실패했습니다. 요소가 스타일 요소가 아닐 경우 요소 [{0}]은(는) 스타일 속성 [{1}]을(를) 정의할 수 없습니다.

STYLE_PROP_NOT_FOUND=스타일 요소에 요소 [{1}]에 대한 스타일 속성 [{0}]이(가) 없습니다.

PROP_TYPE_ERROR=속성 [{0}]의 PropertyType이 설정되지 않았습니다. ( propDefn.getType() == null )

MISSING_PROP_CHOICES=선택 형식 속성 [{0}]에 대한 ChoiceSet가 누락되었습니다.

MISSING_SLOT_TYPE=SlotDefn [{0}]에 대한 슬롯 내용 형식이 누락되었습니다.

MISSING_SLOT_NAME=슬롯 [{0}]에 대한 displayNameID가 누락되었습니다.

INVALID_SLOT_TYPE=[{1}] 슬롯의 슬롯 내용 형식 [{0}]이 유효하지 않습니다.

ELEMENT_NAME_CONST=요소 [{0}]이(가) 정의되지 않았습니다.

MISSING_CHOICE_SET_NAME=메타데이터 사전에 선택 집합을 추가할 때 선택 집합 이름이 누락되었습니다.

DUPLICATE_CHOICE_SET_NAME=메타데이터 사전에 선택 집합 [{0}]을(를) 추가할 때 선택 집합 이름이 중복됩니다.

MISSING_STRUCT_NAME=메타데이터 사전에 구조를 추가할 때 구조 이름이 누락되었습니다.

DUPLICATE_STRUCT_NAME=메타데이터 사전에 구조 [{0}]을(를) 추가할 때 구조 이름이 중복됩니다.

MISSING_STRUCT_DEFN=[{1}]의 구조 목록 형식 속성 [{0}]에 대해 구조 정의가 누락되었습니다.

UNREFERENCABLE_STRUCT_DEFN=구조 참조 형식 속성 [{0}]에 대해 구조 정의를 참조할 수 없습니다.

MISSING_ELEMENT_TYPE=요소 참조 형식 속성 [{0}]에 대한 요소 참조가 누락되었습니다.

UNDEFINED_ELEMENT_TYPE=속성 [{1}]에서 참조한 요소 [{0}]이(가) 정의되지 않습니다.

UNNAMED_ELEMENT_TYPE=속성 [{1}]에서 참조한 요소 [{0}]은(는) 이름이 없는 요소(namespace==''no_namespace'')입니다.

INCONSISTENT_PROP_TYPE=일치하지 않는 속성 [{0}], 스타일 속성도 내장 함수가 될 수 없습니다.

MISSING_JAVA_CLASS=요소 [{0}]은(는) 추상이 아니며 Java 클래스 특성을 제공해야 합니다.

JAVA_CLASS_INITIALIZE_ERROR=요소 [{0}]에 대한 Java 클래스 특성이 잘못되었으며 클래스 이름 [{1}]을(를) 인스턴스화하는 데 실패했습니다. 인터페이스 또는 추상 클래스이거나 지정한 Java 클래스가 기본 생성자를 제공하지 않아서일 수 있습니다(또는 인수가 없는 생성자).

JAVA_CLASS_LOAD_ERROR=요소 [{0}]에 대해 지정한 Java 클래스 특성이 잘못되었으며 현재 클래스 경로에서 클래스 [{1}]을(를) 로드할 수 없습니다.

INVALID_ELEMENT_JAVA_CLASS=지정한 요소의 Java 클래스 [{0}]이(가) 디자인 요소가 아니며 클래스는 DesignElement의 종류가 아닙니다.

DUPLICATE_METHOD_NAME=요소 [{0}]에 동일한 이름 [{1}]을(를) 가진 두 가지 메서드가 있습니다.

DUPLICATE_ARGUMENT_NAME=메서드 [{0}]에 동일한 이름 [{1}]을(를) 가진 두 가지 인수가 있습니다.

INVALID_DEFAULT_VALUE=속성 {0}에 대한 기본값 {1}이(가) 잘못되었습니다.

MISSING_CLASS_NAME=메타데이터 사전에 클래스를 추가할 때 클래스 이름이 누락되었습니다.

DUPLICATE_CLASS_NAME=메타데이터 사전에 클래스 [{0}]을(를) 추가할 때 클래스 이름이 중복됩니다.

MISSING_METHOD_NAME=클래스에 메서드를 추가할 때 메서드 이름이 누락되었습니다.

CONSTRUCTOR_EXISTING=생성자가 이미 있습니다.

DUPLICATE_MEMBER_NAME=클래스에 구성원을 추가할 때 구성원 이름이 중복됩니다.

MISSING_MEMBER_NAME=클래스에 구성원을 추가할 때 구성원 이름이 누락되었습니다.

INVALID_LIST_TYPE=[{0}] 목록이 지원되지 않습니다.

VALIDATOR_NOT_FOUND=사전에 속성 [{1}]에 대한 검사기 [{0}]가 없습니다.

MISSING_VALIDATOR_NAME=검사기 이름이 누락되었습니다.

DUPLICATE_VALIDATOR_NAME=검사기 [{0}]의 이름이 기존 이름과 중복됩니다.

MISSING_EXTENSION_NAME=메타데이터 사전에 확장명을 추가할 때 확장명이 누락되었습니다.

DUPLICATE_EXTENSION_NAME=메타데이터 사전에 확장명 [{0}]을(를) 추가할 때 확장명이 중복됩니다.

EXTENSION_POINT_NOT_FOUND=확장점 [{0}]이(가) 없습니다.

FAILED_TO_CREATE_INSTANCE=클래스 {0}을(를) 인스턴스화할 수 없습니다.

VALUE_REQUIRED={0}의 값이 필요합니다.

DUPLICATE_CHOICE_NAME=이름이 [{0}]인 선택이 이미 있습니다.

INVALID_PROPERTY_TYPE=속성 형식 [{0}]이(가) 잘못되었습니다.

INVALID_UNIT=속성 "{0}"의 기본 단위 "{1}"이(가) 잘못되었습니다.

ENCYRPTION_EXTENSION_EXISTS=ID "{0}"에 포함된 암호화 확장명이 있습니다.

VISIBILITY_PROPERTY_NOT_FOUND=요소 [{0}]의 속성 표시 [{1}]가 기존 속성을 가리키지 않습니다.

REPORT_MISSING=루트 요소: 보고서 디자인이 정의되지 않았습니다.

INVALID_CHOICE_VALUE=속성 "{1}"의 선택 값 "{0}"이(가) 잘못되었습니다.

MISSING_SUB_TYPE=목록 속성 "{0}"에 하위 형식이 없습니다.

UNSUPPORTED_SUB_TYPE=목록 속성 "{0}"에 들어 있는 하위 형식 "{1}"은 지원되지 않습니다.

SUB_TYPE_FORBIDDEN=속성 "{0}"이(가) 목록 속성이 아니므로 하위 속성은 널(null)이어야 합니다.

MISSING_XML_NAME=구체적인 요소 "{0}"은(는) xml 이름을 정의하지 않습니다.

DUPLICATE_XML_NAME=요소 "{1}"의 XML 이름 "{0}"이(가) 기존 이름과 충돌합니다.

INVALID_ELEMENT_TYPE=요소 형식 "{0}" 이(가) 잘못되었습니다.

INVALID_CHOICE_PROPERTY=상세 정보 형식과 그 고유의 선택 목록 모두를 정의하므로 "{0}" 선택 속성("{1}" 확장명 내)이 잘못되었습니다.

EXTENDED_ITEM_MISSING=메타데이터 사전 내의 누락된 확장 항목

DEFAULT_ENCRYPTION_EXIST=기본 암호화 "{1}"이(가) 존재하므로 암호화 "{0}"은(는) 기본값으로 설정될 수 없습니다.

DEFAULT_EMPTY_STYLE_NAME=확장 요소	"{0}"의 기본 스타일은 비워둘 수 없습니다.

EMPTY_DELTA_ROM_STREAM=클래스 "{1}"에서 확장 요소 "{0}"(으)로부터 반환한 델타-롬 입력 스트림은 비워둘 수 없습니다.