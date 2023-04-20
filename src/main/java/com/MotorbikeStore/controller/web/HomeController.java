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
import com.MotorbikeStore.model.favoriteModel;
import com.MotorbikeStore.service.ICartService;
import com.MotorbikeStore.service.IPictureService;
import com.MotorbikeStore.service.IProductDetailService;

@WebServlet(urlPatterns = { "/web-main-page" })
public class HomeController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Inject
	private IProductDetailService productDetailService;
	@Inject
	private IPictureService PictureService;
	@Inject
	private ICartService cartService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// recent product
		ProductDetailModel model = new ProductDetailModel();

		List<ProductDetailModel> list = productDetailService.findNumberOfMotor(8);
		PictureModel model2 = new PictureModel();
		model2.setPicPath(request.getContextPath() + "/uploads/images/product/");
		for (ProductDetailModel o : list) {

			model2 = PictureService.findOneByAmotorId(o.getaMotorId());
			if (model2 != null) {
				o.setPicName(model2.getPicName());
			}

		}
		model.setListResult(list);
		request.setAttribute("model", model);

		// feature product
		ProductDetailModel modelFeature = new ProductDetailModel();

		List<ProductDetailModel> listFeature = productDetailService.findNumberOfMotorFeature(8, "xe đẹp");
		PictureModel modelpicfeature = new PictureModel();
		modelpicfeature.setPicPath(request.getContextPath() + "/uploads/images/product/");
		for (ProductDetailModel o : listFeature) {

			modelpicfeature = PictureService.findOneByAmotorId(o.getaMotorId());
			if (modelpicfeature != null) {
				o.setPicName(modelpicfeature.getPicName());
			}

		}
		modelFeature.setListResult(listFeature);
		request.setAttribute("modelFeature", modelFeature);

		// First in home
		ProductDetailModel model3 = new ProductDetailModel();
		List<ProductDetailModel> list2 = productDetailService.findNumberOfMotor(3);
		PictureModel model4 = new PictureModel();
		for (ProductDetailModel o : list2) {
			model4 = PictureService.findOneByAmotorId(o.getaMotorId());
			if (model4 != null) {
				o.setPicName(model4.getPicName());
			}

		}

		ProductDetailModel model5 = new ProductDetailModel();
		for (ProductDetailModel o : list2) {
			model5.setPicName(o.getPicName());
			model5.setMotors_Name(o.getMotors_Name());
			model5.setMotorsDecs(o.getMotorsDecs());
			model5.setaMotorId(o.getaMotorId());
			list2.remove(0);
			break;
		}
		model3.setListResult(list2);
		request.setAttribute("model3", model3);
		request.setAttribute("model5", model5);
		
		
		// show number favor in the header 
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
		
		
		//get and show number cart items in the header 
		
		HttpSession session = request.getSession();
		String userIdstr = null;
		if(session.getAttribute("userId") != null) {
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
		
		//show view 
		RequestDispatcher rd = request.getRequestDispatcher("views/web/home.jsp");
		rd.forward(request, response);
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
