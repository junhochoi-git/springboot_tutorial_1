

# CH2. 스프링 부트에서 테스트 코드 작성하자

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
    <li><a href="#sec_target0" style="text-decoration:none;">0. 변경 사항 </a></li>
    <li><a href="#sec_target1" style="text-decoration:none;">1. 테스트 코드 소개 </a></li>
    <li><a href="#sec_target2" style="text-decoration:none;">2. Hello Controller 테스트 코드 작성</a></li>
    <li><a href="#sec_target3" style="text-decoration:none;">3. 롬복 소개 및 설치하기 </a></li>
    <li><a href="#sec_target4" style="text-decoration:none;">4. Hello Controller 코드를 롬복으로 전환</a></li>
    <li><a href="#sec_target5" style="text-decoration:none;">부록. <b>새로 배운 IntelliJ 단축키 및 어노테이션</b></a></li></ul>




<div class="sec0">
    <a name="sec_target0"></a>
	<h3>0.  변경 사항 </h3>
    <hr>
</div>


- Spring Boot 2.4.1

- Gradle 6.7.1

- JUnit5

- build.gradle 코드

  ```html
  <a href="https://jojoldu.tistory.com/539">관련 저자 링크</a>
  ```

  1. **build.gradle** 변경

     ```
     plugins { // (1)
         id 'org.springframework.boot' version '2.4.1' // RELEASE 삭제
         id 'io.spring.dependency-management' version '1.0.10.RELEASE'
         id 'java'
     }
     
     group 'com.jojoldu.book'
     version '1.0.4-SNAPSHOT-'+new Date().format("yyyyMMddHHmmss")
     sourceCompatibility = 1.8   
     
     repositories {
         mavenCentral()
         jcenter()
     }
     
     // for Junit 5
     test { // (2)
         useJUnitPlatform()
     }
     
     dependencies {
         //(3)
         implementation('org.springframework.boot:spring-boot-starter-web')
         implementation('org.springframework.boot:spring-boot-starter-mustache')
     
         // lombok
         implementation('org.projectlombok:lombok')
         annotationProcessor('org.projectlombok:lombok')
         testImplementation('org.projectlombok:lombok')
         testAnnotationProcessor('org.projectlombok:lombok')
     
         implementation('org.springframework.boot:spring-boot-starter-data-jpa')
         implementation("org.mariadb.jdbc:mariadb-java-client")
         implementation('com.h2database:h2')
     
         implementation('org.springframework.boot:spring-boot-starter-oauth2-client') // 권한 관련 ch5 이전 주석
         implementation('org.springframework.session:spring-session-jdbc') // 권한 관련 ch5 이전 주석
     
         testImplementation('org.springframework.boot:spring-boot-starter-test') // 
         
         testImplementation("org.springframework.security:spring-security-test") // 권한 관련 ch5 이전 주석
     }
     ```

     

     

     2. **Gradle 버전 업그레이드**

     `alt+f12`: 해당 프로젝트 기준 터미널 생성 단축키 이용 or 해당 프로젝트 경로로 이동 후, 

     `gradlew wrapper --gradle-version 0.00.0` : gradle 버전 변경








<div class="sec1">
    <a name="sec_target1"></a>
	<h3>1.  테스트 코드 소개 </h3>
    <hr>
</div>



#### 왜 테스트 코드를 작성하는가?

- 이전 - 테스트 코드의 부재로 직접 수정된 기능을 확인했어야했음.

  

  - 코드 작성, WAS 실행, Postman등 API 테스트 도구로 HTTP 요청, 요청 결과 확인, 문제 발견시 실행 중인 WAS 중지후 코드 수정후 반복
  - WAS(Web Application Server) 중 하나인 Tomcat를 재시작 하는 시간 수십초 ~ 1분 이상 수십번 수정시 1시간 이상 소요될 수도. 



- 테스트 코드 작성의 장점

  1) 

  2) 자동 검증이 가능하게 하여 작성된 단위테스트만 실행하여 검증하게 됨

  3) 새로운 기능 추가하면서, 기존 기능에 문제가 생겼을 시 조기 발견이 가능하게 됨. 즉 기존 기능이 잘 작동되는 것을 보장하여 개발자가 만든 기능을 안전하게 보호해줌.

  - 이외에도,

    - 개발단계 초기에 문제를 발견할 수 있게 도와줌

    - 코드를 리팩토링* 하거나 라이브러리 업그레이드시 기존 기능이 올바르게 동작하는지 확인 가능

    - 기능에 대한 불확실성 감소시킬 수 있음

    - 시스템에 대한 실제 문서 역할을 함 ( 기능 설명? )

      

      >리팩토링( Refactoring 이란? ) 
      >
      >"Refactoring is the process of changing a software system in such a way that it does not alter the external behavior of the code yet improves its internal structure. It is a disciplined way to clean up code that minimizes the chances of introducing bugs. In essence when you refactor you are improving the design of the code after it has been written." - Martin Fowler 
      >
      >- 외부 동작을 바꾸지 않으면서 내부 구조를 개선하여 소프트웨어 시스템을 변경하는 과정, 
      >
      > ex) 저자: 배민에서 정산시스템 개선 

  

- ##### 대표적인 xUnit 프레임워크 

  | JUnit   | Java |
  | ------- | ---- |
  | DBUnit  | DB   |
  | CppUnit | C++  |
  | NUnit   | .net |

  [포인트] 해당 교재에서 JUnit 4 버전을 썻다가 그레이들 버전 업그레이드와 함께 JUnit 5을 사용하게 됨.



##### TDD와 단위 테스트 ( Unit Test )란?

- TDD : 테스트 주도하는 개발이라는 개발 방법론
- 단위 테스트 : 기능 단위의 테스트 코드를 작성하는 행위 그 자체



##### TDD ( Test Driven Developement )

```html
<img src="http://hanwax.github.io/assets/tdd_flow.png">
- 레드 그린 사이클
```

- RED 항상 실패하는 테스트를 먼저 작성

- Green  테스트가 통과하는 프로덕션 코드를 작성

- Refractor 테스트가 통과하면 프로덕션 코드를 리팩토링

  저자 추천 관련 링크

  

```html
<a style="text-decoration:none;" href="https://repo.yona.io/doortts/blog/issue/1"
```





<div class="sec2">
    <a name="sec_target2"></a>
	<h3>2. Hello Controller 테스트 코드 작성</h3>
    <hr>
</div>

- 프로젝트 src > main > java 폴더 밑에서 new > package , `alt+insert` 

  (참고:  package는 폴더 모양에 점 있는 디자인 )

- 일반적으로 패키지 명은 웹사이트 주소의 역순으로 작성

  (ex. junho.com 일 경우 com.junho , 그리고 GroupId 로 사용,  ArtifactId : 일반적으로 프로젝트 이름 )

  

  #### Application 클래스 작성

  - 스프링 부트 자동 설정 및 스프링 Bean 읽기 생성 모두 자동으로 설정하게,

    `@SpringBootApplication`  어노테이션

    - 해당 어노테이션이 있는 위치부터 설정을 읽기 때문에 항상 프로젝트의 최상단에 위치해야함

  - `SpringApplication.run` 으로 내장 WAS 실행

    ```
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```

    

    ##### 내장 WAS 와 외장 WAS

    **내장 WAS** 는 외부에 Web Application Server를 두지 않고 어플리케이션을 실행할 때 내부에서 WAS를 실행함. 

    - 장점 : 항상 서버에 톰캣등을 설치할 필요가 없고, 스프링 부트로 만들어진 Jar 파일로 실행하면 됨

      (관련 성능상 이슈, p58 설명)

    

    **외부 WAS**의 경우 -  모든 서버의 WAS의 종류와 버전, 설정들을 일치시킨 환경을 구성해야함. 

      단점으로 , 새로운 서버 추가시 모든 서버가 같은 WAS 환경을 구축해야하면서, 서버 대수가 많아 지면, 

    실수 여지 및 작업 시간 증가 

    

  ### Controller 작성

  `RestController` : 컨트롤러를 JSON 데이터를 반환하는 컨트롤러로 만듬

  - 이전에는 `ResponseBody`를 각 메소드마다 선언했는데, 이것을 한번에 사용할 수 있게 해준다 생각하자

    > `@ResponseBody` : 일반적인 jsp 같은 view 전달하는 것이 아니라 데이터 자체를 전달하기 위한 용도로...
    >
    > 

  `@GetMapping` : HTTP Method인 Get의 요청을 받을 수 있는 API 만들어줌

  - 예전엔, `@ReqeustMapping(method=RequestMethod.GET)`으로 사용되었음. 

  => /hello로 요청이 오면 문자열 hello을 반환하는 기능 구현

  > @GetMapping(value="/getList")...의 기억

  

  ```
  import com.junho.book.springboot.web.dto.HelloResponseDto;
  import org.springframework.web.bind.annotation.GetMapping;
  import org.springframework.web.bind.annotation.RequestParam;
  import org.springframework.web.bind.annotation.RestController;
  
  @RestController
  public class HelloController {
  
      @GetMapping("/hello")
      public String hello(){
          return "hello";
      }
  
      @GetMapping("/hello/dto")
      public HelloResponseDto helloDto (@RequestParam("name") String name, @RequestParam("amount") int amount){
          return new HelloResponseDto(name, amount);
      }
      
  }
  ```

  - REST ( Reprentational State Transfer)

    >  '웹앱 서버에서 순수하게 데이터에 대한 처리를 목적으로 하게 됨 
    >
    >  하나의 URI 하나의 고유한 리소스 대로 설계 + 전송 방식
    >
    >  원하는 작업 지정 
    >
    >  URI+ GET/POST/PUT/DELETE 등
    >
    >  JSON/XML등에 대한 데이터 처리를 위한 라이브러리 jackson-databind, jackson-dataformat 등'
    >
    >  
    >
    >  -코드로 배우는 스프링 웹프로젝트 정리노트

    

  #### Controller 테스트 코드 작성

  

  `alt+f12`: 해당 프로젝트 기준 터미널 생성 단축키

  `gradlew wrapper --gradle-version 0.00.0` : gradle 버전 변경

  

  | 교재                           | 이유                                                         | 저자 업그레이드                     | 이유                                  |
  | ------------------------------ | ------------------------------------------------------------ | ----------------------------------- | ------------------------------------- |
  | `@RunWith(SpringRunner.class)` | 스프링 부트 테스트와 JUnit 사이 연결자 역할.                     테스트 진행시 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킴. 여기서는 `SpringRunner`라는 스프링 실행자를 사용함.  (`SpringJUnit4ClassRunner`썻던 경험 ) | `ExtendWith(SpringExtension.class)` | JUnit5 로 버전 업그레이드 되면서 바뀜 |

  - `@WebMvcTest` :  여러 스프링 테스트 어노테이션 중, Web(Spring MVC) 에 집중할 수 있는 어노테이션

    선언시, `@Controller`, `@ControllerAdvice` 등 사용 가능 

    단 `@Service` , `@Component`, `@Repository` 사용할 수 없음

    **컨트롤러만 사용하기 때문에 선언하였음.**

    

  - `@Autowired`  : 스프링이 관리하는 빈(Bean) 주입 

    

  - `private MockMvc mvc` : 웹 API 테스트시 사용, 스프링 MVC의 시작점

    해당 클래스 MockMvc 를 통해 HTTP GET, POST 등에 대한 API 테스트 가능함 

    

  - `mvc.perform(get("hello"))` : MockMvc 를 통해 /hello 주소로 HTTP GET 요청

    체이닝 지원되어 여러 검증 기능 이어서 선언 가능

    

  - `.andExpect(status().isOk())` : mvc.perform 의 결과 검증 기능

    HTTP Header의 Status 검증 ( 200 ok , 404 Not found , 500 internal server error )

    

  - `.andExpect(content().string(hello))` : mvc.perform 의 결과 검증 기능

    응답 본문 즉 Reposne의 Body 내용 검증

    Controller에서 "hello" 를 리턴하기 때문에 이 값이 맞는지 검증

    >요청과 응답의 내부 구조는..
    >
    >

    

    

  ```java
  import org.junit.jupiter.api.Test;
  import org.junit.jupiter.api.extension.ExtendWith;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
  /*import org.springframework.security.test.context.support.WithMockUser;*/
  import org.springframework.test.context.junit.jupiter.SpringExtension;
  import org.springframework.test.web.servlet.MockMvc;
  
  import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
  import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
  
  import static org.hamcrest.Matchers.is;
  
  //@RunWith(SpringRunner.class) // (1)
  @ExtendWith(SpringExtension.class) //(1)-2 JUNIT 5 으로 버전업
  @WebMvcTest(controllers = HelloController.class) // (2)
  public class HelloControllerTest {
  
      @Autowired // (3)
      private MockMvc mvc; // (4)
  
      /*@WithMockUser(roles="USER")*/
      @Test
      public void testHello() throws Exception {
          String hello = "hello";
  
          mvc.perform(get("/hello"))
                  .andExpect(status().isOk())
                  .andExpect(content().string(hello));
  
      }
      @Test
      public void testHelloDto() throws Exception{
          String name="hello";
          int amount =1000;
  
          mvc.perform(get("/hello/dto")
                  .param("name", name)
                  .param("amount",String.valueOf(amount)))
                  .andExpect(status().isOk())
                  .andExpect(jsonPath("$.name", is(name)))
                  .andExpect(jsonPath("$.amount", is(amount))
          );
  
      }
  }
  ```

  

  ##### 이클립스에서 테스트 코드 실행하는 경우,

    단위 테스팅을 위해 메소드 클릭후 JUnit에서 실행시키기 눌렀었다

  



<div class="sec3">
    <a name="sec_target3"></a>
	<h3>3. 롬복(Lombok) 소개 및 설치하기</h3>
    <hr>
</div>

##### Lombok 라이브러리 란?

자바 개발시 자주 사용하는 Getter, Setter, 기본 생성자, toString 등을 어노테이션으로 자동 생성해줌



- 이클립스 시절 롬복 설치

https://projectlombok.org/downlad 로 가서 `lombok.jar` 파일 다운로드 받아서 이클립스 설치 경로 탐색하였음.

Eclipse(STS) 실행 경로에 lombok.jar 파일 추가 된 것을 확인 가능했음

- 인텔리제이 롬복 설치

  - `build.gradle` 에서 

    - Gradle 6 이전 : compile('org.projectlombok:lombok') 추가해줌 

    - Gradle 6 이후 : `compile` -> `implementation`, `testcompile`-> `testImplementation` 으로 Soft Deprecate 됨.

      ```
       implementation('org.projectlombok:lombok')
       annotationProcessor('org.projectlombok:lombok') // 어노테이션 기반 코드 처리 성능 향상 목적(?)
       testImplementation('org.projectlombok:lombok')	// 테스트 환경에서도 사용하게 끔 추가하는 듯.
       testAnnotationProcessor('org.projectlombok:lombok')
      ```

      

  - `ctrl + alt + s ` 누르고, File>Settings 띄운 다음, Plugins에서 Lombok으로 검색하고 다운로드한 후 인텔리제이 재시작.

    





<div class="sec4">
    <a name="sec_target4"></a>
	<h3>4. Hello Controller 코드를 롬복으로 전환</h3>
    <hr>
</div>

##### 롬복으로 변경하고 문제가 생기는지는 테스트 코드를 돌려 확인!

- web.dto 패키지의 `HelloResponseDto`클래스에서 다음과 같이 작성

  ```java
  package com.junho.book.springboot.web.dto;
  
  import lombok.Getter;
  import lombok.RequiredArgsConstructor;
  
  @Getter
  @RequiredArgsConstructor // Arguments 가 있는 생성자 생성해주는 Annotation
  public class HelloResponseDto {
  
      private final String name;
      private final int amount;
  
  }
  
  ```

  ( `DTO`: Data Transfer Object, `DAO` : Data Access Object,  `VO` : Value Object )

  - `@Getter` : 선언된 모든 필드의 get메소드를 생성해줌 ( ex. getName() ...)

  - `@RequiredArgsConstructor` : 선언된 모든 final 필드가 포함된 생성자를 생성해 줌, final 이 없는 필드는 생성자에 포함되지 않음.

    

- 테스트 코드 작성 `HelloResponseTest`  클래스

  

  ```java
  package com.junho.book.springboot.web.dto;
  
  import org.junit.jupiter.api.Test;
  
  import static org.assertj.core.api.Assertions.assertThat;
  
  public class HelloResponseDtoTest {
  
      @Test
      public void lombokTest(){ // 2.3 Lombok
          //given
          String name = "test";
          int amount = 1000;
  
          //when
          HelloResponseDto dto = new HelloResponseDto(name, amount);
  
          //then
          assertThat(dto.getName()).isEqualTo(name); // 1, 2
          assertThat(dto.getAmount()).isEqualTo(amount);
      }
  }
  
  ```

  - `assertThat` : `assertj` 라는 테스트 검증 라이브러리의 검증 메소드. 검증하고 싶은 대상을 메소드의 인자로 받는다.

    ​	ex) dto.getName을 검증하고 싶어서 assertThat(dto.getName()) 처럼 씀.

    - 메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용 가능함.

    

  - `isEqualTo` : `assertj`의 동등 비교 메소드 assertThat에 있는 값과  isEqualTo의 값을 비교해서 같을 때만 성공임

    ```java
    assertThat(dto.getName()).isEqualTo(name); 
    ```

     

  #### JUnit 의 기본 assertThat 이 아니라 assertJ 의 assertThat을 사용함

  assertJ의 장점 :

  - CoreMatchers와 달리 추가적으로 라이브러리가 필요하지 않음, Junit의 assertThat의 경우 is() 와 같이 CoreMatchers 라이브러리가 필요함

  - 자동 완성이 좀 더 확실하게 지원됨. IDE에서 CoreMatchers와 같은`Matcher`라이브러리의 자동완성 지원이 약함

  

  관련 추천 링크 : http://bitly/30vm9Lg



#### HelloController에도 새로 만든 ResponseDto을 사용하도록 코드 추가

```java
@GetMapping("/hello/dto")
    public HelloResponseDto helloDto (@RequestParam("name") String name, @RequestParam("amount") int amount){
        return new HelloResponseDto(name, amount);
    }
```

- `@RequestParam` : 외부에서 API로 넘긴 파라미터를 가져오는 어노테이션.

  여기선 외부에서 name(@RequestParam("name")) 이란 이름으로 넘긴 파라미터를 메소드 파라미터 name(String name)에 저장하게 됨.



#### HelloControllerTest에 코드 추가

```java
@Test
    public void testHelloDto() throws Exception{
        String name="hello";
        int amount =1000;

        mvc.perform(get("/hello/dto")
                .param("name", name)
                .param("amount",String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount))
        );

    }
```



- `param` : API 테스트 할 때 사용될 요청 파라미터를 설정함. (단, 값은 String 문자열만!)

  그래서 숫자/날짜 등의 데이터를 사용하고자 할 땐, 문자열로 변환해야 사용 가능함 `String.valueof()` 를 주목!

- `jsonPath` : JSON 응답값을 필드별로 검증할 수 있는 메소드.

  $를 기준으로 필드명을 명시함. name과 amount를 검증하니 (`$.name`, `$.amount`) 로 검증한 것을 볼 수 있음.



#### 결과

JSON이 리턴되는 API 역시 정상적으로 테스트 통과 확인 가능





<div class="sec5">
    <a name="sec_target5"></a>
	<h3>부록. 새로 배운 IntelliJ 단축키 및 어노테이션</h3>
    <hr>
</div>




1. `alt + insert ` : 파일 생성 
2. `ctrl + shift + A ` : 액션 검색 창 띄우기
3. `ctrl + alt + s ` : file > settings 단축키
4. `ctrl+ shift + K ` : push 단축키
