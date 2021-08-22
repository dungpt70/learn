package net.javaguides.springboot.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor{
	@Autowired
	private HttpSession session;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("PRE REQUEST : "+ request.getRequestURI());
		if (session.getAttribute("username") != null && session.getAttribute("user") != null) {
			System.out.println("In true");
			return true;
		} else {
			System.out.println("In false" + request.getContextPath() +"/login");
			//session.setAttribute("redirect-url", request.getRequestURI());
			//response.sendRedirect(request.getContextPath() +"http://localhost:8080/login");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login");
			requestDispatcher.forward(request, response);
			return false;
		}
	}
	
}
