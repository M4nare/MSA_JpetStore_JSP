/**
 *    Copyright 2010-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.web.actions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Order;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;


/**
 * The Class OrderActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class OrderActionBean extends AbstractActionBean {

	  private static final long serialVersionUID = -6171288227470176272L;

	  private static final String CONFIRM_ORDER = "/WEB-INF/jsp/order/ConfirmOrder.jsp";
	  private static final String LIST_ORDERS = "/WEB-INF/jsp/order/ListOrders.jsp";
	  private static final String NEW_ORDER = "/WEB-INF/jsp/order/NewOrderForm.jsp";
	  private static final String SHIPPING = "/WEB-INF/jsp/order/ShippingForm.jsp";
	  private static final String VIEW_ORDER = "/WEB-INF/jsp/order/ViewOrder.jsp";

	  private static final List<String> CARD_TYPE_LIST;

	  private Order order = new Order();
	  
	  private boolean shippingAddressRequired;
	  private boolean confirmed;
	  private List<Order> orderList;

	  static {
	    CARD_TYPE_LIST = Collections.unmodifiableList(Arrays.asList("Visa", "MasterCard", "American Express"));
	  }

	  public int getOrderId() {
	    return order.getOrderId();
	  }

	  public void setOrderId(int orderId) {
	    order.setOrderId(orderId);
	  }

	  public Order getOrder() {
	    return order;
	  }

	  public void setOrder(Order order) {
	    this.order = order;
	  }

	  public boolean isShippingAddressRequired() {
	    return shippingAddressRequired;
	  }

	  public void setShippingAddressRequired(boolean shippingAddressRequired) {
	    this.shippingAddressRequired = shippingAddressRequired;
	  }

	  public boolean isConfirmed() {
	    return confirmed;
	  }

	  public void setConfirmed(boolean confirmed) {
	    this.confirmed = confirmed;
	  }

	  public List<String> getCreditCardTypes() {
	    return CARD_TYPE_LIST;
	  }

	  public List<Order> getOrderList() {
	    return orderList;
	  }

	  /**
	   * List orders.
	   *
	   * @return the resolution
	 * @throws ParseException 
	   */

	  String user;
	  @SuppressWarnings("unchecked")
	public Resolution listOrders() throws ParseException {
	    user = null;
	    HttpServletRequest request = context.getRequest();
	    String userid = request.getParameter("id");   
	    String url="http://order:8082/jpetstore/actions/Order.action?listOrders&id="+userid;
    	RestTemplate restTemplate = new RestTemplate();
    	String resp = restTemplate.getForObject(url, String.class);
    	JSONParser parser = new JSONParser();
    	JSONArray orl = (JSONArray)parser.parse(resp);
    	
    	orderList = (List<Order>) orl;
	    
	    
	    
    	//수정 필요 
	    //orderList = orderService.getOrdersByUsername(user);
	    
	    
	   
	   return new ForwardResolution(LIST_ORDERS);
	  }

	  /**
	   * New order form.
	   *
	   * @return the resolution
	 * @throws ParseException 
	   */
  
	  public Resolution newOrderForm() throws ParseException {
		  
		clear();
	   HttpSession session = context.getRequest().getSession();
	   CartActionBean cartBean = (CartActionBean) session.getAttribute("/actions/Cart.action");
	   String id = (String)session.getAttribute("id");	    
	   String url="http://account:8081/jpetstore/actions/Account.action?getUserinfo&id="+id;
	   RestTemplate restTemplate = new RestTemplate();
	   String resp = restTemplate.getForObject(url, String.class);
	   JSONParser parser = new JSONParser();
	   JSONObject user = (JSONObject)parser.parse(resp);
	    
	    if (id == null) {
	      return new Resolution()
	    		  {
	  		@Override
	  		public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	  			// TODO Auto-generated method stub
	  			
	  			response.sendRedirect("http://localhost:8079/jpetstore/actions/Account.action");
	  			
	  		}

	  	};
	    } else if (cartBean != null) {

	    	//User info
	    	Account account = new Account();
	    	
	    	String list = user.toString();
	    	
	    	Gson gson = new Gson();
	    
			account = gson.fromJson(list, Account.class);
			
	      order.initOrder(account, cartBean.getCart());
	      
	      return new ForwardResolution(NEW_ORDER);
	    } else {
	      setMessage("An order could not be created because a cart could not be found.");
	      return new ForwardResolution(ERROR);
	    }
	  }

	  /**
	   * New order.
	   *
	   * @return the resolution
	 * @throws IOException 
	   */
	public Resolution newOrder() throws IOException {
	    HttpSession session = context.getRequest().getSession();

	    if (shippingAddressRequired) {
	      shippingAddressRequired = false;
	      return new ForwardResolution(SHIPPING);
	    } else if (!isConfirmed()) {
	      return new ForwardResolution(CONFIRM_ORDER);
	    } else if (getOrder() != null) {
	    	
	    	
	    	
	    	Gson gson = new Gson();

			String jsonlist = gson.toJson(order);
			
			System.out.println(jsonlist);
	    	
	    	
	    	URL postUrl = new URL("http://order:8082/jpetstore/actions/Order.action?newOrder");
	    	HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
	    	connection.setDoOutput(true); 				// xml내용을 전달하기 위해서 출력 스트림을 사용
	    	connection.setInstanceFollowRedirects(false);  //Redirect처리 하지 않음
	    	connection.setRequestMethod("POST");
	    	connection.setRequestProperty("Content-Type", "application/json");
	    	OutputStream os= connection.getOutputStream();
	    	os.write(jsonlist.getBytes());
	    	os.flush();
	    		    	
	    	  BufferedReader br = new BufferedReader(new InputStreamReader(
	    			  (connection.getInputStream())));
	    			   
	    			  String output;
	    			  System.out.println("Output from Server .... \n");
	    			  while ((output = br.readLine()) != null) {
	    			  System.out.println(output);
	    			  }
	    	connection.disconnect();
	    	


	        CartActionBean cartBean = (CartActionBean) session.getAttribute("/actions/Cart.action");
	        cartBean.clear();
	    	
	    	
	      setMessage("Thank you, your order has been submitted.");
	      
	     

	      return new ForwardResolution(VIEW_ORDER);
	    } else {
	      setMessage("An error occurred processing your order (order was null).");
	      return new ForwardResolution(ERROR);
	    }
	  }

	  /**
	   * View order.
	   *
	   * @return the resolution
	 * @throws ParseException 
	   */

	  public Resolution viewOrder() throws ParseException {
		  
		
		    HttpServletRequest request = context.getRequest();
		    String orderid = request.getParameter("orderId");   
		    String url="http://order:8082/jpetstore/actions/Order.action?viewOrder&id="+orderid;
	    	RestTemplate restTemplate = new RestTemplate();
	    	String resp = restTemplate.getForObject(url, String.class);
	    	JSONParser parser = new JSONParser();
	    	JSONObject orl = (JSONObject)parser.parse(resp);
	    	
	    	String list = orl.toString();
	    	Gson gson = new Gson();
	    	
			order = gson.fromJson(list, Order.class);
			

	      return new ForwardResolution(VIEW_ORDER);
	  
	  }
	  

		
	 

	  /**
	   * Clear.
	   */
	  public void clear() {
		    order = new Order();
		    shippingAddressRequired = false;
		    confirmed = false;
		    orderList = null;
		  }
}

