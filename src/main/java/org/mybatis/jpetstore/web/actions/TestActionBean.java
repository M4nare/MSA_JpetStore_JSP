/**
 *    Copyright 2010-2017 the original author or authors.
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

import java.util.List;


import javax.servlet.http.HttpServletRequest;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SessionScope;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Order;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;


/**
 * The Class CatalogActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class TestActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 5849523372175050635L;
  
  private static final String TEST = "/WEB-INF/jsp/test/test.jsp";
  private List<Order> orderList;
  private Account account = new Account();
  private List<Product> productList;


  
  public List<Order> getOrderList() {
	    return orderList;
	  }
  public Account getAccount() {
	    return this.account;
	  }
  public List<Product> getProductList() {
	    return productList;
	  }
  





  
  @SuppressWarnings("unchecked")
@DefaultHandler
public ForwardResolution test() throws ParseException {
	  HttpServletRequest request = context.getRequest();
	 String userid = request.getParameter("id");   

	String url="http://order:8082/jpetstore/actions/Order.action?listOrders&id="+userid;
  	String url1="http://account:8081/jpetstore/actions/Account.action?getUserinfo=&id="+userid;

  	RestTemplate restTemplate = new RestTemplate();
  	String resp = restTemplate.getForObject(url, String.class);
  	String resp1 = restTemplate.getForObject(url1, String.class);
	Gson gson = new Gson();

  	
  	// OrderList
  	JSONParser parser = new JSONParser();
  	JSONArray orl = (JSONArray)parser.parse(resp);
  	orderList = (List<Order>) orl;
  	
  	// Account info
  	JSONObject user = (JSONObject)parser.parse(resp1);
	String list = user.toString();
	account = gson.fromJson(list, Account.class);
	
	//Account favouriteCategory
	
	String fav = account.getFavouriteCategoryId();
	String url2="http://catalog:8080/jpetstore/actions/Catalog.action?getProductListByCategory=&id="+fav;
  	String resp2 = restTemplate.getForObject(url2, String.class);
	JSONArray pdl = (JSONArray)parser.parse(resp2);
	productList = (List<Product>) pdl;

  
	 
	  
    
	  return new ForwardResolution(TEST);
  }
  

}
