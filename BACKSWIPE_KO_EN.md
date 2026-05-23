# Artemis BackSwipe

## 한국어

이 포크는 Android 폴더블 기기와 외장 터치패드 키보드로 Windows 원격 데스크톱을 사용할 때 불편한 입력 문제를 줄이기 위한 Artemis Android 개인 빌드입니다.

주요 변경사항:

- 외장 터치패드의 강한 좌우 스와이프를 브라우저 뒤로/앞으로 동작으로 변환합니다.
- 브라우저 뒤로/앞으로 제스처는 `Alt+Left`, `Alt+Right` 키 입력으로 전송됩니다.
- 브라우저 제스처 기능은 설정에서 켜고 끌 수 있습니다.
- 좌우 방향이 반대로 느껴질 때 사용할 수 있는 방향 반전 옵션을 추가했습니다.
- 브라우저 제스처 감도와 반복 간격을 설정에서 조절할 수 있습니다.
- 터치패드 길게 눌러 드래그 기능을 개선했습니다.
- 창 테두리 크기 조절, 슬라이더 조작, 파일 드래그처럼 왼쪽 버튼을 누른 채 움직여야 하는 작업을 더 쉽게 할 수 있습니다.
- 입력/터치패드/원격 데스크톱 관련 설정 문구를 한국어로 더 이해하기 쉽게 정리했습니다.

추천 사용법:

1. Windows Chrome 또는 Edge에서 커서를 페이지 위에 둡니다.
2. 터치패드에서 좌우로 강하게 스와이프합니다.
3. 방향이 반대로 동작하면 설정에서 `브라우저 뒤로/앞으로 방향 반전`을 켭니다.
4. 너무 쉽게 뒤로/앞으로가 실행되면 `브라우저 제스처 감도` 값을 올립니다.
5. 창 크기를 조절할 때는 창 테두리 위에서 한 손가락을 잠시 누른 뒤 움직입니다.
6. 창 테두리가 잘 잡히지 않으면 `드래그 전 이동 허용 범위` 값을 올립니다.

빌드 메모:

- 기본 디버그 앱 이름은 `Artemis BackSwipe`입니다.
- 디버그 빌드는 기존 Artemis와 별도 패키지로 설치될 수 있습니다.
- Galaxy Z Fold 계열은 일반적으로 `arm64-v8a` APK를 사용하면 됩니다.

## English

This fork is a personal Artemis Android build focused on improving remote Windows desktop input from Android foldables and external keyboard trackpads.

Changes:

- Converts strong horizontal external trackpad swipes into browser back/forward actions.
- Browser navigation is sent as `Alt+Left` and `Alt+Right`.
- Browser swipe navigation can be enabled or disabled in settings.
- Added an invert option for devices or users that expect the opposite swipe direction.
- Added settings for browser navigation threshold and repeat delay.
- Improved long-press trackpad dragging.
- Makes window resizing, slider dragging, and other left-button-drag interactions easier.
- Improved Korean text for input, trackpad, and remote desktop settings.

Suggested usage:

1. Place the cursor over a Windows browser page.
2. Swipe horizontally on the trackpad.
3. If the direction feels reversed, enable the browser navigation invert option.
4. If navigation triggers too easily, increase the browser navigation threshold.
5. To resize a window, place the cursor on the window edge, hold one finger briefly on the trackpad, then move.
6. If small handles are hard to grab, increase the long-press drag movement tolerance.

Build notes:

- The debug app label is `Artemis BackSwipe`.
- Debug builds may install beside the official Artemis package.
- Galaxy Z Fold devices usually need the `arm64-v8a` APK.
