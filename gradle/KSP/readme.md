


얼마전까지는 코틀린의 자바 호환성을 등에 엎고 자바에서 사용하던 어노테이션 프로세서를 코틀린에서도 사용하였습니다. 오픈소스 라이브러리, 특히 어노테이션을 통한 코드 생성을 지원하는 라이브러리 사용 시 build.gradle 의존성 정의 부분에 implementation 또는 api 로 라이브러리 의존성을 추가하고, kapt 로 해당 라이브러리에서 제공하는 어노테이션 프로세서에 대한 의존성도 추가해 보았을 것 입니다. 이 자바 기반의 어노테이션 프로세서는 코틀린 기반의 코드에서 사용 시 자바 심볼 관련 정보를 유지하기 위해 자바 코드 변환이 발생해 컴파일 시간을 느리게 만들고, 코틀린만의 심볼들(Ex> extension functions, declaration-site variance, local functions, …)을 코드 생성 시 사용하는데 제약이 있습니다.

그리하여 KSP(Kotlin Symbol Processing)가 탄생하게 되었고 기존 자바 기반의 어노테이션 프로세서를 대체하여 더 빠른 코드 생성(약 25%라고 하네요! 😍)을 통한 컴파일 시간 단축과 앞서 언급한 코틀린 전용 심볼들도 코드 생성을 시 지원할 수 있게 되었습니다. 또한 JVM과 약한 의존성을 갖는 API 제공으로 컴파일러 업데이트에 따른 프로세서 서스테이닝이 용이합니다.

하지만 문제는 QueryDSL을 사용하는데 일어났습니다. 기존에 쓰던 kapt를 ksp로 교체를 하려고 하는데, 이 KSP는 감을 못잡겠습니다.

여러 예제를 보았는데, 어노테이션들을  `com.google.devtools.ksp.processing.SymbolProcessor`와 ` com.google.devtools.ksp.processing.SymbolProcessorProvider`를 상속 받아 구현해 직접 클래스를 만들어주는데, QueryDSL의 어노테이션을 그럼 내가 직접 구현해서 Q도메인 객체를 만들어줘야 한다는건지 너무 헷갈렸습니다.

그래서 결국 3시간동안 열심히 노력해봤지만 QueryDSL이나 깃허브 이슈를 들춰보았지만 KSP에 관한 주제가 없어서 그냥 다시 kapt를 쓰는것으로 설정은 해놨습니다. 아마 Gradle 8.0 부터는 kapt를 지원하지 않는거 같은데,

그 동안 어노테이션 프로세싱에 대해 더욱 공부해보도록 해야 겠습니다.

https://d2.naver.com/helloworld/6685007

