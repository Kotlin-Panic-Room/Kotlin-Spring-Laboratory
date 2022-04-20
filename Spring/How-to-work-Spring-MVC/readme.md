스프링 MVC 핵심 구성 요소

![](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F9cf1r%2FbtraP9TUPct%2FkvIE6igibVGKCws8pbKItK%2Fimg.png)

* DispatcherServlet : HandlerMapping, HandlerAdapter, ViewResolver, View와 같은 구성 요소 중앙에 위치하여 모든 연결을 담당한다.
*HandlerMapping : 클라이언트의 요청 경로를 이용해서 이를 처리할 컨트롤러 빈 객체를 검색하고 처리할 컨트롤러를 DispatcherServlet에게 전달하는 역할을 수행한다.
* Controller : 웹 브라우저가 원하는 처리 요청을 수행한다. 컨트롤러의 종류는 다음과 같다.
  * @Controller 애노테이션을 이용해서 구현한 컨트롤러
  * Controller 인터페이스를 구현한 컨트롤러
  * 특수 목적으로 사용되는 HttpRequestHandler 인터페이스를 구현한 클래스 컨트롤러
* HandlerAdapter : 다양한 Controller의 종류들을 동일한 방식으로 처리하기 위해 중간에 사용된다.
* DispatcherServlet 객체는 HandlerMapping이 찾아준 컨트롤러 객체를 처리할 수 있는 HandlerAdapter 빈에게 요청 처리를 위임한다.
* Controller 빈 객체가 처리한 결과를 DispatcherServlet 객체에게 ModelAndView(모델 및 뷰 이름 포함)로 변환해서 전달해주는 역할을 수행한다.
* ViewResolver : ModelAndView 요소를 활용하여 뷰 이름에 해당하는 View 객체를 찾거나 생성해서 리턴하는 역할을 수행한다.
* View : DispatherServelt 객체로부터 응답 결과 생성을 요청받는다. View 객체는 JSP를 실행함으로써 웹 브라우저에게 전상할 응답 결과를 생성하고 과정을 마친다.

스프링 MVC 구성 요소 예시

1. 웹 브라우저에서 요청을 하여 DispatcherServlet에게 전달된다.

2. DispatcherServlet은 해당 URL 요청 경로를 HandlerMapping에게 전달한다.

3. HandlerMapping은 해당 URL 요청 경로를 처리할 컨트롤러 빈 객체를 선택하고 DispatcherServlet에게 처리할 컨트롤러 객체를 전달한다.

4. DispatcherServlet은 HandlerAdapter에게 해당 컨트롤러 빈 객체에게 처리 요청을 전송한다.

5. HandlerAdapter는 메시지를 받은 컨트롤러 빈 객체를 수행시킨다. 그리고 처리 결과를 받는다. 처리 결과에는 ModelAndView(데이터 모델과 뷰 이름)이 담겨 있다.

6. ModelAndView를 전달받은 DispatcherServlet은 ViewResolver에게 전달한다.

7. 전달받은 ViewResolver는 뷰 이름을 활용하여 그에 맞는 View를 찾거나 생성해서 DispatcherServlet에게 전달한다.

8. DispatcherServlet은 전달받은 View 객체에게 응답 결과 생성을 요청한다.

9. JSP 페이지를 사용하는 경우 View 객체는 JSP를 실행함으로써 웹 브라우저에게 전송할 응답 결과를 생성하고 과정을 마친다.
