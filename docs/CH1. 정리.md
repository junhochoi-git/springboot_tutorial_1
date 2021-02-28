

# CH1. 인텔리제이로 스프링부트 시작하기

> html css 의 `ancher` 기능 이용하여 ch1 세부 목차로 이동할 수 있게 하여 후에 복습시 용이하게 함
>
> `ul` 에서 style="list-style:none;"으로 지정함
>
> `div` 태그 및 `<a>` 태그 이용
>
> 최종 업데이트 _ 2021 - 02 - 27



<h3>
    이번 챕터에서 배울 내용<hr>
</h3>
<ul style="list-style:none;">
    <li><a href="#sec_target1" style="text-decoration:none;">1. 왜 인텔리제이인가</a></li>
    <li><a href="#sec_target2" style="text-decoration:none;">2. 설치 요약</a></li>
    <li><a href="#sec_target3" style="text-decoration:none;">3. 이클립스와는 살짝 다른 워크스페이스</a></li>
    <li><a href="#sec_target4" style="text-decoration:none;">4. 그레이들 프로젝트를 스프링 부트 프로젝트로 변경하기</a></li>
    <li><a href="#sec_target5" style="text-decoration:none;">5. <b>인텔리제이에서 GIT 과 GITHUB 사용법 </b></a></li>
    <li><a href="#sec_target6" style="text-decoration:none;">부록. <b>새로 배운 IntelliJ 단축키</b></a></li></ul>









<div class="sec1">
    <a name="sec_target1"></a>
	<h3>1. 왜 인텔리제이를 쓰는가?</h3>
    <hr>
</div>


- 강력한 추천 기능
- 훨씬 더 다양한 ㅏ리팩토링과 디버깅 기능
- 이클립스 깃에 비해 훨씬 높은 자유도
- 프로젝트 시작시 인덱싱을 하여 빠르느 검색 속도
- HTML, CSS, JS 과 XML에 대한 강력한 기능 지원
- 자바, 스프링 부트 버전업에 대한 빠른 업데이트 가능 등등





<div class="sec2">
    <a name="sec_target2"></a>
	<h3>2. 설치 요약</h3>
    <hr>
</div>




- 나의 경우 학생 신분으로 Ultimate 버전 이용가능

- `Maximum Heap Size` : 메모리 할당 최대 범위를 지정함, 힙 메모리

  - 보통 개발 PC 4G이하를 가정하고 750 MB
  - 8G : 1024 ~ 2048
  - 16G: 2048~4096 추천

  



<div class="sec3">
    <a name="sec_target3"></a>
	<h3>3. 이클립스와는 살짝 다른 워크스페이스</h3>
    <hr>
</div>




|   이클립스   | 인텔리제이 |
| :----------: | :--------: |
| 워크스페이스 |  프로젝트  |
|   프로젝트   |    모듈    |

이클립스의 워크스페이스의 경우 단순히 여러 서로 무관한 프로젝트들을 모아놓은 디렉토리라면, 

인텔리제이는 하나의 창에 하나의 프로젝트를 지향 

- 모듈에 프로젝트를 모듈로 구성해 Root프로젝트 및의 하위 프로젝트 구조를 만들 수 있긴함.

참고 > https://jojoldu.tistory.com/334



<div class="sec4">
    <a name="sec_target4"></a>
	<h3>4. 그레이들 프로젝트를 스프링 부트 프로젝트로 변경하기</h3>
    <hr>
</div>


- `Gradle`로 프로젝트 생성 후, 

  - `GroupId`와 `ArtifactId` 등록. 기본적으로 ArtifactId는 프로젝트의 이름이 됨.

  - 결과로, 그레이들 기반의 자바 프로젝트가 생성됨

    - <u>**No candidates found for method call plugins :**</u> 

      Help> Find Action > Reload All Gradle Projects 하여 그래들 프로젝트들 리로드

      또는 인텔리 제이 리부팅하여 라이브러리 재로드하여 문제 해결 모색

      

- `스프링부트` 프로젝트로 변경 해주기 

  - 해당 저자는 독자의 이해를 위해 `스프링 이니셜라이저` (start.spring.io/)에서 만들지 않고 

    스프링 부트에 필요한 설정들을 하나씩 추가함

  - `필수 플러그인`

    ```
    buildscript{
        ext{
            springBootVersion = '2.1.7.RELEASE'
        }
        repositories {
            mavenCentral()
            jcenter()
        }
        dependencies{
            classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        }
    
    }
    
    apply plugin:'java'
    apply plugin:'eclipse'
    apply plugin:'org.springframework.boot'
    apply plugin:'io.spring.dependency-management'
    
    
    group 'com.junho.book'
    version '1.0-SNAPSHOT'
    sourceCompatibility=1.8
    
    ```

- `repository` : 각종 의존성(라이브러리)들을 어떤 원격 저장소에 받을지 정함.

  - 보통 기본적으로 `mavenCentral`을 많이 사용, 
  - 최근 `jcenter` 도 사용 난이도 때문 p35

- `dependencies` :  프로젝트 개발에 필요한 의존성들을 선언하는 곳



- 전체 코드

  ```
  buildscript{
      ext{
          springBootVersion = '2.1.7.RELEASE'
      }
      repositories {
          mavenCentral()
          jcenter()
      }
      dependencies{
          classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
      }
  
  }
  
  apply plugin:'java'
  apply plugin:'eclipse'
  apply plugin:'org.springframework.boot'
  apply plugin:'io.spring.dependency-management'
  
  
  group 'com.junho.book'
  version '1.0-SNAPSHOT'
  sourceCompatibility=1.8
  
  repositories {
      mavenCentral()
      jcenter()
  }
  
  dependencies {
      compile('org.springframework.boot:spring-boot-starter-web')
      testCompile('org.springframework.boot:spring-boot-starter-test')
  }
  
  test {
      useJUnitPlatform()
  }
  ```

  

- 단축키

  - `ctrl+shift+o`: gradle 자동 import , 
    - `build.gradle`에 변경이 있으니 반영하기 위해서, 

<div style="text-align: center;">
<img style="max-height:50%; max-width:50%;" src="https://github.com/junhochoi-git/springboot_tutorial/blob/main/img_src/ch1/pci1.JPG?raw=true">
    우측 gradle 탭 누르고 확인 가능
</div>





<div class="sec5">
    <a name="sec_target5"></a>
	<h3>5. 인텔리제이에서 GIT과 GITHUB 사용법</h3>
    <hr>
</div>


####  개발 상황에서 버전 관리는 필수

- 버전관리 툴 , `SVN` , `Subversion` , `GIT` 
  - 최근 대부분의 IT 서비스 회사는 깃을 통해 버전관리를 한다. 
  - `GIT`의 원격저장소 중 대표적인 것은 `GITHUB`, `GITLAB` 등이 있다.

#### 프로젝트와 깃허브를 연동하자

1. 인텔리제이 에서 `ctrl+shift+A` 단축키로 액션 검색 창 띄우고 "share project on github" 검색함
2. 깃허브 로그인
   - token으로 로그인시, github>상단 메뉴> `settings` > `Developer Settings` > `Personal access tokens` > 후 권한 부여 하기
3. `repository` 지정 및 깃허브 저장소와 동기화 



#### 인텔리제이에서 깃허브 저장소 가져오기

1. `ctrl+alt+s` : file> settings 의 단축키 

2. `version control` 에서 `Github` 연동 확인

3. 상단 메뉴 `VCS` > `Get from Version Control` 클릭 후 > 원하는 Github 레파지토리 선택 후 > `clone`

   

#### . ignore 플러그인 설치 필요시

1. `ctrl+alt+s` : 누르고 settings 에서 plugin 항목에 접근함
2. `Marketplace` 에서 `.ignore` 설치 



#### 깃허브로 푸시하기

1. `ctrl+ shift + K ` : push 단축키



<div class="sec5">
    <a name="sec_target6"></a>
	<h3>부록. 새로 배운 IntelliJ 단축키 _윈도우</h3>
    <hr>
</div>


1. `alt + insert ` : 파일 생성 
2. `ctrl + shift + A ` : 액션 검색 창 띄우기
3. `ctrl + alt + s ` : file > settings 단축키
4. `ctrl+ shift + K ` : push 단축키

5. `ctrl+shift+o`: gradle 자동 import , 
