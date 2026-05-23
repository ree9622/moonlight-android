# Artemis BackSwipe / 아르테미스 백스와이프

Artemis BackSwipe는 [Artemis Android](https://github.com/ClassicOldSong/moonlight-android)를 기반으로 한 개인 포크입니다.  
원본 Artemis는 Apollo/Sunshine 기반 Windows 원격 스트리밍 클라이언트이고, 이 포크는 한국어 사용자와 Android 폴더블 + 외장 터치패드 키보드 환경을 더 잘 지원하기 위해 만들었습니다.

## 이 프로젝트를 만든 이유

이 포크의 목적은 두 가지입니다.

1. 앱 전체의 한국어 지원을 보강합니다.
2. Android 폴더블 기기와 외장 터치패드 키보드에서 아직 구현되지 않았거나 불완전한 원격 데스크톱 입력 기능을 직접 구현합니다.

특히 Galaxy Z Fold 계열에서 Artemis로 Windows PC에 접속하고, Nillkin 같은 접이식 키보드의 터치패드를 사용할 때 다음 문제가 있었습니다.

- 터치패드 좌우 스와이프로 Chrome/Edge 뒤로가기와 앞으로가기가 되지 않음
- 창 테두리, 슬라이더, UI 핸들처럼 왼쪽 버튼을 누른 채 움직여야 하는 조작이 불안정함
- 외장 키보드의 Command/Meta 키가 Windows 키처럼 전달되지 않는 경우가 있음
- 설정과 메뉴에 영어가 많이 남아 있어 기능 의미를 파악하기 어려움
- 매번 GitHub 페이지를 직접 열어 최신 APK가 있는지 확인해야 함

이 저장소는 위 문제를 빠르게 해결하기 위한 실사용 빌드입니다.

## 주요 변경사항

- 한국어 리소스 전수 보강
  - 기본 문자열 676개 대비 한국어 문자열 676개
  - 한국어 누락 0개
  - 설정 화면 참조 문자열 누락 0개

- 브라우저 뒤로/앞으로 제스처
  - 외장 터치패드의 강한 좌우 스와이프를 `Alt+Left`, `Alt+Right`로 변환
  - Chrome, Edge 등 Windows 브라우저에서 뒤로/앞으로로 동작
  - 설정에서 켜기/끄기, 방향 반전, 감도, 반복 간격 조절 가능

- 터치패드 드래그 개선
  - 터치패드 길게 누르기로 왼쪽 마우스 버튼 유지
  - 창 크기 조절, 슬라이더 조작, 파일 드래그 같은 UI 핸들 조작 개선
  - Android 터치패드 제스처 경로와 외장 마우스/포인터 입력 경로를 모두 보강

- Windows 키 전달 보강
  - 외장 키보드의 좌/우 Command 또는 Meta 키를 Windows 키 down/up 이벤트로 직접 전송
  - 단독 Windows 키 입력이 Start/Win 키처럼 동작하도록 개선

- 설정 설명 개선
  - 입력, 터치패드, 원격 데스크톱 관련 설정 문구를 한국어로 더 이해하기 쉽게 정리

- 앱 안에서 업데이트 확인
  - 설정 > 기타 설정 > 소프트웨어 업데이트에서 개인 포크의 최신 GitHub 릴리스를 조회
  - 최신 태그와 현재 빌드를 비교하고, 새 APK가 있으면 다운로드 링크를 바로 열 수 있음
  - Obtainium 추적 링크도 개인 포크 기준으로 변경

## 다운로드

최신 개인 빌드 릴리스:

- [v20.2.6-backswipe-ko-5](https://github.com/ree9622/moonlight-android/releases/tag/v20.2.6-backswipe-ko-5)
- [APK 직접 다운로드](https://github.com/ree9622/moonlight-android/releases/download/v20.2.6-backswipe-ko-5/artemis-backswipe-update-check-arm64-debug.apk)

Galaxy Z Fold 계열은 일반적으로 `arm64-v8a` APK를 사용하면 됩니다.

## 사용 팁

브라우저 뒤로/앞으로:

1. Windows Chrome 또는 Edge에서 커서를 페이지 위에 둡니다.
2. 터치패드에서 좌우로 강하게 스와이프합니다.
3. 방향이 반대로 느껴지면 설정에서 브라우저 뒤로/앞으로 방향 반전을 켭니다.
4. 너무 쉽게 실행되면 브라우저 제스처 감도를 올립니다.

창 크기 조절/슬라이더 드래그:

1. 창 테두리나 슬라이더 핸들 위에 커서를 올립니다.
2. 터치패드에 한 손가락을 올리고 잠깐 기다립니다.
3. 그대로 움직여 크기 조절이나 드래그가 되는지 확인합니다.
4. 작은 핸들이 잘 잡히지 않으면 드래그 전 이동 허용 범위를 올립니다.

## 한계와 다음 확인 필요 사항

Android/Samsung OS가 특정 키나 터치패드 이벤트를 앱에 전달하지 않으면 앱 레벨에서 처리할 수 없습니다.  
특정 키보드에서 여전히 동작하지 않는 입력이 있으면 실제 Android `KeyEvent` 또는 `MotionEvent`를 화면에 표시하는 진단 빌드로 확인해야 합니다.

## 빌드

```bash
git submodule update --init --recursive
JAVA_HOME="/Applications/Android Studio.app/Contents/jbr/Contents/Home" \
ANDROID_HOME="$HOME/Library/Android/sdk" \
ANDROID_SDK_ROOT="$HOME/Library/Android/sdk" \
./gradlew assembleNonRoot_gameDebug
```

빌드 결과는 보통 다음 경로에 생성됩니다.

```text
app/build/outputs/apk/nonRoot_game/debug/
```

## 원본 프로젝트

이 포크는 ClassicOldSong의 Artemis Android를 기반으로 합니다.

- Upstream: https://github.com/ClassicOldSong/moonlight-android
- Apollo: https://github.com/ClassicOldSong/Apollo
- Sunshine: https://github.com/LizardByte/Sunshine

원본 Artemis의 설명에 따르면 Artemis Android는 Apollo/Sunshine을 통해 Windows PC의 게임과 데스크톱을 Android 기기로 스트리밍하기 위한 오픈소스 클라이언트입니다.

## English Summary

Artemis BackSwipe is a personal fork of [Artemis Android](https://github.com/ClassicOldSong/moonlight-android).

This fork exists to:

1. Complete and improve Korean localization.
2. Implement missing or incomplete remote desktop input features for Android foldables and external trackpad keyboards.

Key changes:

- Full Korean string coverage: 667/667 default strings translated.
- Horizontal external trackpad swipes can be converted to browser back/forward via `Alt+Left` and `Alt+Right`.
- Long-press drag handling is improved for window resizing, sliders, and other UI handles.
- Physical Command/Meta keys are forwarded directly as Windows keys.
- Korean settings text for input, trackpad, and remote desktop usage has been rewritten for clarity.
- In-app update checking reads the latest GitHub release from the personal fork and opens the APK or release page.

Latest release:

- [v20.2.6-backswipe-ko-5](https://github.com/ree9622/moonlight-android/releases/tag/v20.2.6-backswipe-ko-5)
- [Direct APK download](https://github.com/ree9622/moonlight-android/releases/download/v20.2.6-backswipe-ko-5/artemis-backswipe-update-check-arm64-debug.apk)

If a device or Android build does not deliver a key or motion event to the app, it cannot be fixed purely at the app layer. In that case, a diagnostic build that displays raw `KeyEvent` and `MotionEvent` data is needed.
