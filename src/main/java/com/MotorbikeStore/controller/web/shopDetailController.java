package com.MotorbikeStore.controller.web;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.MotorbikeStore.model.CartModel;
import com.MotorbikeStore.model.PictureModel;
import com.MotorbikeStore.model.ProductDetailModel;
import com.MotorbikeStore.model.commentModel;
import com.MotorbikeStore.model.favoriteModel;
import com.MotorbikeStore.model.reviewModel;
import com.MotorbikeStore.model.specificationModel;
import com.MotorbikeStore.service.ICartService;
import com.MotorbikeStore.service.ICommentService;
import com.MotorbikeStore.service.IPictureService;
import com.MotorbikeStore.service.IProductDetailService;
import com.MotorbikeStore.service.IReviewService;
import com.MotorbikeStore.service.ISpecificationService;

@WebServlet("/notaddshopDetail")
public class shopDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@Inject
	private IProductDetailService ProductDetailService;
	@Inject
	private IPictureService PictureService;
	@Inject
	private ICommentService commentService;
	@Inject
	private ISpecificationService specificationService;
	@Inject
	private IReviewService reviewService;
	@Inject
	private ICartService cartService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		float moneyPrepaid = 0f;
		int aMotorId = Integer.parseInt(request.getParameter("id"));
		ProductDetailModel model = new ProductDetailModel();
		model = ProductDetailService.findOneByAmotorId(aMotorId);
		moneyPrepaid = model.getPrice() * 0.25f;
		model.setMoneyPrepaid(moneyPrepaid);
		// Select product also like
		ProductDetailModel modelAlso = new ProductDetailModel();
		PictureModel modelpicAlso = new PictureModel();

		List<ProductDetailModel> listAlso = ProductDetailService.filterBranch(model.getBranch());
		for (ProductDetailModel o : listAlso) {

			modelpicAlso = PictureService.findOneByAmotorId(o.getaMotorId());
			if (modelpicAlso != null) {
				o.setPicName(modelpicAlso.getPicName());
			}

		}
		modelAlso.setListResult(listAlso);
		request.setAttribute("modelAlso", modelAlso);
		// show specification and product detail
		specificationModel modelSpe = new specificationModel();
		modelSpe = specificationService.findOne(model.getMotorsId());

		request.setAttribute("model", model);
		PictureModel model2 = new PictureModel();
		PictureModel model3 = new PictureModel();

		List<PictureModel> list = PictureService.findByAMotorId(aMotorId);

		for (PictureModel o : list) {
			model3.setPicName(o.getPicName());
			list.remove(0);
			break;
		}
		model2.setListResult(list);
		request.setAttribute("modelSpe", modelSpe);

		request.setAttribute("model3", model3);
		request.setAttribute("model2", model2);

		// show number in cart
		Cookie[] listCookie = request.getCookies();
		String valueCartCookie = "";
		if (listCookie != null) {
			for (Cookie o : listCookie) {
				if (o.getName().equals("cart")) {
					valueCartCookie += o.getValue();

				}
			}
		}
		favoriteModel favor = new favoriteModel();

		if (!valueCartCookie.isEmpty()) {
			String[] listAmotorId = new String[100];
			if (valueCartCookie != null && valueCartCookie.length() != 0) {
				listAmotorId = valueCartCookie.split("/");
			}
			favor.setQuantity(listAmotorId.length);

		}
		request.setAttribute("favorite", favor);

		// show discussion
		commentModel modelComment = new commentModel();
		commentModel modelComment2 = new commentModel();
		List<commentModel> listComment = commentService.findByaMotorId(aMotorId);
		modelComment2.setQuantity(listComment.size());
		modelComment.setListResult(listComment);

		request.setAttribute("modelComment", modelComment);
		request.setAttribute("modelComment2", modelComment2);

		// show review
		reviewModel modelReview = new reviewModel();
		reviewModel modelReview2 = new reviewModel();
		List<reviewModel> listReview = reviewService.findByaMotorId(aMotorId);
		modelReview2.setQuantity(listReview.size());
		modelReview.setListResult(listReview);

		request.setAttribute("modelReview", modelReview);
		request.setAttribute("modelReview2", modelReview2);

		// get and show number cart items in the header

		HttpSession session = request.getSession();
		String userIdstr = null;
		if (session.getAttribute("userId") != null) {
			userIdstr = session.getAttribute("userId").toString();
		}

		int userId = 0;

		if (userIdstr != null) {
			userId = Integer.parseInt(userIdstr);

		}
		List<CartModel> listCart = cartService.findByUserId(userId);
		CartModel cartModel = new CartModel();
		cartModel.setQuantity(listCart.size());
		request.setAttribute("cart", cartModel);
		// show view

		RequestDispatcher rd = request.getRequestDispatcher("views/web/shopDetail.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
