package com.MotorbikeStore.controller.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.MotorbikeStore.model.CartModel;
import com.MotorbikeStore.model.commentModel;
import com.MotorbikeStore.service.ICartService;


@WebServlet("/addCart")
public class ShoppingCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Inject
    private ICartService cartService;
   
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		
		
		
		
		
		
		
		int idpage = Integer.parseInt(request.getParameter("idpage"));
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		RequestDispatcher rd = null;
		 String aMotorIdstr = request.getParameter("aMotorId");
     	if(session.getAttribute("name") != null && session.getAttribute("role") == "user")
     	{
     		String aMotorIdStr = request.getParameter("aMotorId"); 
    		String userIdStr = request.getParameter("userId"); 
    		int aMotorId  = 0;
    		int userId = 0;
    		if(aMotorIdStr != null) {
    			aMotorId = Integer.parseInt(aMotorIdStr);
    		}
    		if(userIdStr != null) {
    			userId = Integer.parseInt(userIdStr);
    		}
    		
    		
    		CartModel cartModel = new CartModel();
    		cartModel.setaMotorId(aMotorId);
    		cartModel.setUserId(userId);
    		cartService.save(cartModel);
    		
    		if(idpage == 0) {
    			response.sendRedirect("web-main-page");
    		}else if(idpage == 1) {
    			response.sendRedirect("shop");
    		}else if(idpage == 2) {
    			response.sendRedirect("notaddshopDetail?id="+aMotorId+"");
    		}
     		
     		

     	}else
     	{
     		if(aMotorIdstr!= null) {
     			response.sendRedirect("login");
    		}
     		
     			
    		
     			
     	}
	}

}
