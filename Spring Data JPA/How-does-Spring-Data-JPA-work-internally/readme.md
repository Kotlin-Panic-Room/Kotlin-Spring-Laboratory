스프링 데이터 리포지토리 메서드 이름에서 쿼리를 생성하기 위한 구문 분석 논리는 현재 `org.springframework.data.repository.query.parser`라는 패키지에 선언이 되어있습니다.

일반적으로 리포지토리 메서드 이름 문자열은 정의된 추상 쿼리 기준을 나타내는 부분을 포함하는 PartTree로 구문 분석됩니다.

그런 다음 PartTree를 사용하여 보다 구체적인 쿼리 개체를 만들 수 있습니다. 저장소 유형에 따라 JpaQueryCreator 또는 RedisQueryCreator를 사용합니다.