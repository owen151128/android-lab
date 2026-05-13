# 안드로이드 앱 개발 학습 로드맵 by Claude

> Kotlin 마스터를 위한 로드맵
> **총 권장 기간: 8~10주 (주 10~15시간 기준)** · 풀타임이면 4~5주

---

## Phase 1. Compose 멘탈 모델 (1주)

**이게 진짜 핵심이고, 다른 모든 게 여기서 갈립니다. 절대 건너뛰지 마세요.**

- `@Composable` 함수의 본질: recomposition이 뭐고 언제 일어나는지
- **State**: `remember`, `mutableStateOf`, `derivedStateOf`, `rememberSaveable`의 차이
- **State hoisting** 원칙 ("State down, Events up")
- **Side effects**: `LaunchedEffect`, `DisposableEffect`, `rememberCoroutineScope`, `produceState` — 각각 언제 쓰는지
- `Modifier` 체인 순서가 결과를 바꾸는 이유
- 레이아웃: `Row`/`Column`/`Box`, `LazyColumn`/`LazyRow`, `Box`의 alignment, `weight`
- `@Preview` + `@PreviewParameter`
- **Material 3 기본 + 디자인 시스템 입문**
  - `MaterialTheme`의 전파 메커니즘 (`colorScheme`/`typography`/`shapes`)
  - `MaterialTheme.colorScheme.primary` 같은 토큰 접근
  - `isSystemInDarkTheme()`로 다크모드 전환
  - 이 단계에선 토큰 분리는 아직 안 함 — 기본 사용법만

**자료**: [Jetpack Compose Pathway](https://developer.android.com/courses/jetpack-compose/course) (공식, 무료) — 이거 하나면 충분

**Deliverable**:
1. 카운터 앱 (10분)
2. **인메모리 할 일 앱** — Room 없이, ViewModel 없이 순수 Compose만. 추가/삭제/체크. state hoisting 직접 해보기. 다크모드 토글까지

---

## Phase 2. ViewModel + StateFlow + UDF (3~4일)

- `ViewModel`의 라이프사이클, `viewModelScope`
- `StateFlow` vs `SharedFlow`, 언제 어느 걸
- `collectAsStateWithLifecycle()` — `collectAsState()`와의 차이
- `stateIn(viewModelScope, WhileSubscribed(5_000), initial)` — 이 마법 주문이 왜 필요한지 (configuration change + Flow의 cold/hot)
- **Sealed UiState 패턴**: `Loading` / `Success(data)` / `Error`
- **Route vs Screen** 분리 패턴 (NIA의 `BookmarksRoute` / `BookmarksScreen` 구조)

**Deliverable**: Phase 1의 할 일 앱을 ViewModel + StateFlow로 리팩토링. 회전(rotation) 시켜보고 상태 유지되는지 확인

---

## Phase 3. Room + DataStore (3~4일)

- Room: `@Entity`, `@Dao`, `@Database`, type converter
- **Flow를 반환하는 DAO 쿼리** (이게 reactive한 핵심)
- `DataStore Preferences`로 설정값 저장
- **Repository 패턴 도입** — ViewModel이 DAO 직접 안 부르고 Repository 경유

**Deliverable**: 할 일 앱에 Room 붙이기. 다크모드 토글을 DataStore로. 앱 죽였다 켜도 데이터/설정 유지

---

## Phase 4. Retrofit + kotlinx.serialization (3~4일)

- Retrofit + `suspend fun` 인터페이스
- `kotlinx.serialization`으로 JSON 파싱
- OkHttp interceptor (reverse engineering 경험 살릴 수 있음)
- **Offline-first lite**: 네트워크 → DB 저장 → UI는 DB만 관찰

**Deliverable**: 공개 API 하나 잡아서 뉴스 리더 또는 날씨 앱 만들기. 비행기 모드에서도 캐시된 데이터 보여야 함
- 무료 API: [JSONPlaceholder](https://jsonplaceholder.typicode.com), [Open-Meteo](https://open-meteo.com) 등

---

## Phase 5. Hilt DI (2~3일)

ECDSA license 시스템 설계 경험 덕분에 DI 개념 자체는 빠르게 들어올 거예요.

- `@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`
- `@Module` + `@Provides` vs `@Binds`
- 스코프: `@Singleton`, `@ViewModelScoped`, `@ActivityRetainedScoped`
- 인터페이스/구현 분리해서 테스트 doubles 끼우는 패턴

**Deliverable**: Phase 4 앱을 Hilt로 리팩토링. 모든 `new` 또는 수동 의존성 주입 제거

---

## Phase 6. Navigation-Compose (2~3일)

- `NavHost`, `composable(route)`, `navController.navigate()`
- **Type-safe Navigation** (Kotlin Serialization 기반 신규 API) — 이게 현재 표준
- 인자 전달, deep link
- Nested graph, bottom navigation

**Deliverable**: 3~5개 화면 있는 앱. 리스트 → 상세 → 편집 화면 흐름. 뒤로가기 동작 명확히 이해

---

## Phase 7. Clean Architecture 3-layer (1주)

지금까지는 UI ↔ Repository 직결이었는데, 이제 도메인 레이어를 끼웁니다.

- **Domain layer**: `UseCase` (`operator fun invoke`)
- Repository **인터페이스는 domain**, 구현은 data
- 의존성 방향: `ui → domain ← data` (둘 다 domain에 의존)
- 어떤 로직이 ViewModel에 남고, 어떤 게 UseCase로 빠지는가 — 판단 기준

**Deliverable**: Phase 6 앱을 3-layer로 리팩토링. 패키지 구조부터 갈아엎기

---

## Phase 8. 멀티모듈 + 디자인 시스템 (1주 — 가장 빠르게 갈 수 있는 단계)

**모듈 분리 전략 + convention plugin + 디자인 시스템**에 집중하면 됩니다.

### 모듈 구조

- `:core:designsystem` ← **가장 먼저 만들기**
- `:core:data`, `:core:database`, `:core:network`, `:core:ui`, `:core:domain`
- `:feature:foryou`, `:feature:bookmarks` 등 feature 모듈
- **Convention plugin** (`build-logic/` 아래 자체 플러그인) — NIA가 모범 사례
- `api` vs `implementation` 의존성 노출 정책
- 모듈 의존성 그래프 검증 (`./gradlew :app:dependencies`)

### 디자인 시스템 모듈 구성 (`:core:designsystem`)

```
:core:designsystem/
  ├── theme/
  │   ├── Color.kt          ← 색상 토큰
  │   ├── Type.kt           ← 타이포 토큰
  │   ├── Shape.kt          ← 모양 토큰
  │   └── Theme.kt          ← MaterialTheme 래핑
  ├── component/
  │   ├── AppButton.kt      ← Material 버튼 래퍼
  │   ├── AppTopAppBar.kt
  │   ├── AppFilterChip.kt
  │   └── AppLoadingWheel.kt
  └── icon/
      └── AppIcons.kt
```

### 핵심 규칙 (이걸 안 지키면 디자인 시스템은 의미 없음)

- **`:feature:*` 모듈은 Material 컴포넌트를 직접 import 금지** — 오직 `:core:designsystem`의 래퍼만 사용
- **색상/타이포/spacing은 모두 토큰화** — 매직 넘버 금지
- 색상 변경, 다크모드 변경, 폰트 변경 — 모두 `:core:designsystem` 한 곳에서만 끝나야 함

**Deliverable**: Phase 7 앱을 멀티모듈로 분리. `:core:designsystem` 먼저 만들고, `:feature:*` 모듈이 디자인 시스템만 의존하도록 강제. convention plugin 직접 작성

---

## Phase 9. NIA 정독 (1~2주) ← 여기서 처음 NIA를 펼침

이제 NIA가 "정답지"로 보입니다. 각 결정이 왜 그렇게 됐는지 비교하면서 읽기:

- `docs/ArchitectureLearningJourney.md`, `docs/ModularizationLearningJourney.md` 정독
- 한 feature(`:feature:bookmarks` 추천 — 가장 작음) 골라서 코드 따라가기
- **Offline-first sync 패턴** 정독: `SyncWorker`, `Synchronizer`, `ChangeListVersions`
- **`:core:designsystem` 정독** — 내 디자인 시스템과 비교. `NiaTheme`, `NiaButton`, `NiaIcons` 구성 방식
- **`:app-nia-catalog` 확인** — 디자인 시스템 컴포넌트 카탈로그 앱 (Storybook과 유사). 큰 팀에선 거의 필수
- **Test doubles 패턴** — mock 안 쓰는 테스트 방식
- 내 앱과 NIA의 **차이점**을 노트로 정리 (이게 가장 학습 효과 큼)

### 세부 진행 가이드

1. **1일차**: `ArchitectureLearningJourney.md`, `ModularizationLearningJourney.md`만 읽기. 코드는 아직 안 봄
2. **2일차**: 루트 `settings.gradle.kts`와 `build-logic/` 디렉토리만 보기. convention plugin 구조 파악
3. **3일차**: `:core:designsystem`과 `:app-nia-catalog` 정독 — 디자인 시스템 구성 방식 흡수
4. **4~6일**: `:feature:bookmarks` 하나만 골라서 UI → ViewModel → UseCase → Repository → DAO 순으로 따라가기. **내 앱의 같은 기능 코드와 나란히 띄워놓고 비교**
5. **7~9일**: `:sync:work` 모듈과 `OfflineFirstNewsRepository` 정독. offline-first 메커니즘이 가장 배울 게 많은 부분
6. **10~11일**: 테스트 코드 보기. mock 안 쓰는 test double 패턴이 어떻게 작동하는지
7. **12~14일**: 내 앱에 가져올 패턴 2~3개 골라서 실제로 도입

**Deliverable**: NIA에서 좋아 보이는 패턴 2~3개를 내 앱에 도입

---

## Phase 10. 프로덕션 마무리 (선택, 1주)

- Compose **테스트**: `createComposeRule()`, semantics matcher
- ViewModel 단위 테스트 (`turbine` 라이브러리로 Flow 검증)
- **Baseline Profile** 생성 — 시작 성능 개선
- **Strong skipping mode** 최적화 (Compose 1.7+)
- R8 keep 룰 작성
- Macrobenchmark

---

## 시간 압축 팁

| Phase | 압축 가능 여부 | 이유 |
|---|---|---|
| Phase 1 (Compose) | ❌ **절대 압축 X** | recomposition 멘탈 모델은 시간이 걸려야 박힘 |
| Phase 2 (ViewModel + Flow) | ⚠️ 가능 | Flow 기본기가 있으므로 빠르게 |
| Phase 3 (Room + DataStore) | ⚠️ 가능 | 라이브러리 사용법 위주 |
| Phase 4 (Retrofit) | ⚠️ 가능 | 네트워크 다뤄본 경험 활용 |
| Phase 5 (Hilt) | ✅ 절반 시간 | DI 개념 익숙 |
| Phase 6 (Navigation) | ⚠️ 가능 | API 학습 위주 |
| Phase 7 (Clean Architecture) | ❌ **절대 압축 X** | 의존성 방향은 한 번 잘못 박히면 평생 감 |
| Phase 8 (멀티모듈 + 디자인 시스템) | ✅ 절반 시간 | Gradle/AGP 전문 영역 |
| Phase 9 (NIA 정독) | — | 이 시점에 와야 효과 |
| Phase 10 (프로덕션) | ✅ 절반 시간 | R8 keep 룰은 본업 |

### 핵심 원칙

- **Phase 1과 7은 절대 압축하지 말 것** — Compose의 recomposition 멘탈 모델과 Clean Architecture의 의존성 방향은 머리에 박히는 데 시간이 걸리고, **잘못 박히면 평생 갑니다**
- **NIA는 Phase 9 전에는 펼치지 말 것** — 일찍 보면 카피-페이스트 머신이 되고, 직접 시행착오로 배워야 할 것들을 건너뛰게 됨
- **각 Phase의 Deliverable은 반드시 직접 코드를 쳐서** — 읽는 것 ≠ 배우는 것
- **디자인 시스템은 Phase 8에서 본격 도입** — Phase 1에선 `MaterialTheme` 기본만, Phase 8에서 모듈로 분리

---

## 추가로 봐도 좋은 자료 (선택)

우선순위 순:

1. **공식 Architecture Guide** ([developer.android.com/topic/architecture](https://developer.android.com/topic/architecture)) — Phase 2~7 진행하면서 곁들이기
2. **Modern App Architecture codelab** — Phase 7 끝나고 한 번
3. **Material 3 Expressive** — 디자인 시스템 깊이 들어가고 싶을 때 (2025~2026 출시)
4. **Android 보안 최신 변화** — Play Integrity API, Credential Manager, Privacy Sandbox (본업 직결)
5. **(나중에) Kotlin Multiplatform + Compose Multiplatform** — Phase 10 이후 관심 있으면

---

## 진행 체크리스트

- [ ] Phase 1: Compose 멘탈 모델 + 인메모리 할 일 앱 (다크모드 포함)
- [ ] Phase 2: ViewModel + StateFlow 리팩토링
- [ ] Phase 3: Room + DataStore
- [ ] Phase 4: Retrofit + offline-first lite
- [ ] Phase 5: Hilt DI
- [ ] Phase 6: Navigation-Compose
- [ ] Phase 7: Clean Architecture 3-layer
- [ ] Phase 8: 멀티모듈 + `:core:designsystem` 분리 + Convention plugin
- [ ] Phase 9: NIA 정독 + 패턴 도입
- [ ] Phase 10: (선택) 프로덕션 마무리
