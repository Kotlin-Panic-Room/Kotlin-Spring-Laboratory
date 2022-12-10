
실무에서 코틀린 + 스프링을 처음 사용하면서 겪은 이슈 아닌 이슈이다.

흔히 코틀린 JPA ENTITY 설정 시 다음과 같이 작성한다.

```kotlin
class Parent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = -1,
    ...
) : BaseEntity(id)
```

보면 특이한 점이 있을 것이다. 바로 ID가 기본 값으로 -1로 적용 되어 있다는 것이다. 겪은 이슈는 단일 엔티티를 저장할때는 해당 부분이 오류가 나지 않는데, 자식 관계에 엮이게 되면 오류가 나게 된다는 것이다.

```kotlin
class Parent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = -1,
    ...
    
    @OneToMany
	@JoinColumn(name = "child_id")
    val child: Child
) : BaseEntity(id)

class Child(
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override val id: Long = -1,
)

```



우선 JPARepository의 구현체의 save 함수를 살펴보면

```kotlin
//SimpleJpaRepository.java
    @Transactional
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        if (this.entityInformation.isNew(entity)) {
            this.em.persist(entity);
            return entity;
        } else {
            return this.em.merge(entity);
        }
    }

//AbstractEntityInformation.java
	public boolean isNew(T entity) {

		ID id = getId(entity);
		Class<ID> idType = getIdType();

		if (!idType.isPrimitive()) {
			return id == null;
		}

		if (id instanceof Number) {
			return ((Number) id).longValue() == 0L;
		}

		throw new IllegalArgumentException(String.format("Unsupported primitive id type %s", idType));
	}
```

다음과 같이 되어있다. isNew 함수를 통해 엔티티를 판단할떄 ID가 0이거나 NULL이면 신규 엔티티로, ID 값이 있으면 이미 있는 엔티티로 판단한다. 실제로 ID가 -1로 설정이 되어있으면 isNew에서 false 값을 얻게되고 `em.persist`가 아닌 `em.merge` 함수가 실행된다. 근데 이게 단일 객체일때는 그냥 저장이 되는 거였다. 그래서 더 파고 들어가 보았다.

```kotlin
//DefualtMergeEventListener.java
final Object result = source.get( entityName, clonedIdentifier )

if ( result == null ) {
	//TODO: we should throw an exception if we really *know* for sure
	//      that this is a detached instance, rather than just assuming
	//throw new StaleObjectStateException(entityName, id);

	// we got here because we assumed that an instance
	// with an assigned id was detached, when it was
	// really persistent
	entityIsTransient( event, copyCache );
}

```

`DefualtMergeEventListener`에서 ID 값이 있으면 Select을 통해 엔티티를 들고오는데 result가 null일 경우 entityIsTransient을 통해 entity를 저장을 해준다... 주석을 읽어보면 JPA 개발자들도 merge 이벤트시 엔티티가 데이터베이스에 없으면 해당 엔티티가 진짜 detached 된 엔티티인지 판단할 수 없어서 그냥 세이브를 하는것 같다.

그래서 단일 엔티티가 저장될때는 오류가 나지 않았던 것이다.

그렇다면 자식 관계일 경우에는 왜 오류가 날까?

자식의 경우에는 ID 값이 있을 경우 이미 있는 엔티티라고 판단하고 `DefualtMergeEventListener`에서 `copyValues`를 통해 자식 엔티티를 가져오려 하는데 데이터베이스에 있을리가 없으니 오류가 나는 것이다.

```kotlin
	protected void copyValues(
			final EntityPersister persister,
			final Object entity,
			final Object target,
			final SessionImplementor source,
			final Map copyCache) {
		final Object[] copiedValues = TypeHelper.replace(
				persister.getPropertyValues( entity ),
				persister.getPropertyValues( target ),
				persister.getPropertyTypes(),
				source,
				target,
				copyCache
		);

		persister.setPropertyValues( target, copiedValues );
	}
```

끝.

> 틀린 부분이 있다면 지적해주세요!
