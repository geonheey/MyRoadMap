# MyRoadApp
- 주어진 출발지 / 도착지에 따라 도로 상태 및 기타 정보를 보여주는 앱
- **NATIVE_APP_KEY**와 **AUTHORIZATION_KEY**는 `gradle.properties` 파일에 저장되어 있으며, `BuildConfig`를 통해 가져옵니다.

## 앱 구조

이 앱은 **MVVM** 아키텍처를 기반으로 설계되었습니다. 주요 패키지 구조는 다음과 같습니다:

### Data 패키지

- **model**: API의 응답을 나타내는 데이터 클래스를 포함합니다.
  - `DistanceTimeResponse`: 거리 및 시간 정보 응답 모델
  - `ErrorResponse`: 오류 응답 모델
  - `LocationResponse`: 출발지 및 도착지 리스트 & 출발지 / 도착지 정보 응답 모델
  - `RouteResponse`: 경로 정보 응답 모델

- **repository**: 데이터 소스를 관리하며, API 호출을 담당합니다.
  - `InfoRepository` 클래스는 API와의 통신을 관리하며, 데이터 소스에서 위치, 경로, 거리 및 시간 정보를 가져오는 역할을 합니다. 이 클래스는 **ApiService**를 통해 외부 API와 상호작용하며, 비동기적으로 데이터를 처리합니다.
    - `fetchLocations`: 인증 키를 사용하여 출발지 및 도착지 리스트를 비동기적으로 가져오는 메서드
    - `fetchRoutes`: 출발지와 도착지 정보를 사용하여 경로 정보를 가져오는 메서드
    - `fetchDistanceTime`: 출발지와 도착지의 거리 및 시간을 조회하는 메서드

- **service**: API와의 통신을 담당합니다.
  - `ApiService`: 출발지 / 도착지 리스트 API, 경로 조회 API, 시간 / 거리 조회 API 메서드가 포함되어 있습니다.
  - `RetrofitClient`: Retrofit을 사용하여 API 호출을 관리합니다.

### UI 패키지

- **component**: UI 구성 요소를 포함합니다.
- **screen**: 각 화면에 대한 UI를 정의합니다.
- **theme**: 앱의 전반적인 테마 및 스타일을 설정합니다.

### Utils 패키지

- **MapUtils**: 
#### 주요 메서드
1. **displayRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context)**:
   - 주어진 경로 리스트를 KakaoMap에 표시합니다.
   - 각 경로의 교통 상태에 따라 색상을 설정하여 경로를 시각적으로 구분합니다.
   - 경로의 포인트를 `LatLng` 객체로 변환하고, `RouteLineSegment`를 생성하여 KakaoMap의 경로 관리자에 추가합니다.
   - 경로가 없는 경우에는 아무 작업도 수행하지 않습니다.

2. **markRoutes(kakaoMap: KakaoMap?, routes: List<RouteResponse>, context: Context)**:
   - 주어진 경로의 출발지와 도착지를 KakaoMap에 마크합니다.
   - 출발지와 도착지에 각각의 마크를 추가하며, `displayRoutes` 메서드를 호출하여 경로를 표시합니다.

3. **detailRoutes(distanceInMeters: Int, timeInSeconds: Int): Pair<String, String>**:
   - 주어진 거리(미터)와 시간(초)을 읽기 쉬운 형식으로 변환하여 반환합니다.
   - 거리는 킬로미터 또는 미터로 표시되며, 시간은 시, 분, 초 형식으로 반환됩니다.

### ViewModel
- **InfoViewModel**: 

ViewModel은 UI 관련 데이터를 관리하고, UI와 데이터 간의 상호작용을 담당합니다.
 - 위치 정보를 비동기로 가져오는 메서드(`fetchLocations`)를 포함하고, 
 - 출발지와 도착지에 대한 경로(`fetchRoutes`) 및 거리 및 시간(`fetchDistanceTime`) 정보를 가져오는 메서드를 제공합니다.
 - 상태 흐름(StateFlow)을 사용하여 UI에 위치 리스트를 제공하며, API 호출 성공 시 로그에 결과를 기록합니다.

|![구현화면1_스플래쉬화면](https://github.com/user-attachments/assets/37ea6e99-3568-42f5-b8dc-d92250cfe8f1)|![구현화면2_경로목록화면](https://github.com/user-attachments/assets/48bc38f8-9a03-431f-921c-391610472083)|![구현화면3_경로조회성공](https://github.com/user-attachments/assets/1c471a75-3aed-4b6b-a520-350692acbbcd)
|-----------------------|-----------------------|-----------------------|
|![구현화면5_경로조회지도](https://github.com/user-attachments/assets/a3fb45d3-6e2a-48e5-98c8-1cf7ce174344)|![구현화면4_경로조회실패](https://github.com/user-attachments/assets/20ad3f53-3b22-4dde-85a4-45d91d29e66c)
