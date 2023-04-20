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

import com.MotorbikeStore.model.reviewModel;
import com.MotorbikeStore.service.IReviewService;
import com.MotorbikeStore.service.IpaymentService;


@WebServlet("/customerReview")
public class customerReviewController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Inject
    private IReviewService reviewService;
    @Inject
    private IpaymentService paymentService;
    
	

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		RequestDispatcher rd = null;
		String aMotorIdstr = request.getParameter("aMotorId");
		//check login
		if (session.getAttribute("name") != null && session.getAttribute("role") == "user") {
			
			// save review
			String message = request.getParameter("message");

			
			String userIdstr = null;
			if(session.getAttribute("userId") != null) {
				userIdstr = session.getAttribute("userId").toString();
			}
			int userId = 0;
			reviewModel modelReview = new reviewModel();
			
			if (message != null) {
				modelReview.setRvDesc(message);
				
			}

			if (aMotorIdstr != null) {
				modelReview.setaMotorId(Integer.parseInt(aMotorIdstr));
				
			}
			if (userIdstr != null) {
				userId = Integer.parseInt(userIdstr);
				modelReview.setUserId(Integer.parseInt(userIdstr));
				
			}
			if(paymentService.UserIsCustomer(userId)) {
				if (message != null) {
					int reviewId = reviewService.save(modelReview);
					
				}
			}
			
			
			// show view again
			response.sendRedirect("notaddshopDetail?id=" + Integer.parseInt(aMotorIdstr) +"");

		} else {

			if (aMotorIdstr != null) {
				response.sendRedirect("login?id1=10&id2=" + Integer.parseInt(aMotorIdstr) + "");
			}

		}
	}

}
