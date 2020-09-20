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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SessionScope;
import net.sourceforge.stripes.integration.spring.SpringBean;
import net.sourceforge.stripes.validation.Validate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.jpetstore.domain.Account;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

/**
 * The Class AccountActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class AccountActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 5499663666155758178L;

  private static final String NEW_ACCOUNT = "/WEB-INF/jsp/account/NewAccountForm.jsp";
  private static final String EDIT_ACCOUNT = "/WEB-INF/jsp/account/EditAccountForm.jsp";
  private static final String SIGNON = "/WEB-INF/jsp/account/SignonForm.jsp";

  private static final List<String> LANGUAGE_LIST;
  private static final List<String> CATEGORY_LIST;



  private Account account = new Account();
  private List<Product> myList;
  private boolean authenticated;

  static {
    LANGUAGE_LIST = Collections.unmodifiableList(Arrays.asList("english", "japanese"));
    CATEGORY_LIST = Collections.unmodifiableList(Arrays.asList("FISH", "DOGS", "REPTILES", "CATS", "BIRDS"));
  }

  public Account getAccount() {
    return this.account;
  }

  public String getUsername() {
    return account.getUsername();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setUsername(String username) {
    account.setUsername(username);
  }

  public String getPassword() {
    return account.getPassword();
  }

  @Validate(required = true, on = { "signon", "newAccount", "editAccount" })
  public void setPassword(String password) {
    account.setPassword(password);
  }

  public List<Product> getMyList() {
    return myList;
  }

  public void setMyList(List<Product> myList) {
    this.myList = myList;
  }

  public List<String> getLanguages() {
    return LANGUAGE_LIST;
  }

  public List<String> getCategories() {
    return CATEGORY_LIST;
  }

  
  
  public Resolution newAccountForm() {
    return new ForwardResolution(NEW_ACCOUNT);
  }

  /**
   * New account.
   *
   * @return the resolution
   */
 

  /**
   * Edits the account form.
   *
   * @return the resolution
 * @throws ParseException 
   */
  public Resolution editAccountForm() throws ParseException {
	  	HttpSession s = context.getRequest().getSession();
	  	String id = (String)s.getAttribute("id");
	  
	  	String url="http://account:8081/jpetstore/actions/Account.action?getUserinfo=&id="+ id;
	    RestTemplate restTemplate = new RestTemplate();
	    String resp = restTemplate.getForObject(url, String.class);
	    JSONParser parser = new JSONParser();	  	
	    JSONObject user = (JSONObject)parser.parse(resp);
    	String list = user.toString();
    	
    	Gson gson = new Gson();
    
		account = gson.fromJson(list, Account.class);
    return new ForwardResolution(EDIT_ACCOUNT);
  }

  /**
   * Edits the account.
   *
   * @return the resolution
   */


  /**
   * Signon form.
   *
   * @return the resolution
 * @throws ParseException 
   */
  @DefaultHandler
  public Resolution signonForm() throws ParseException {
    return new ForwardResolution(SIGNON);
  }
  
  public Resolution login() {

	  return new Resolution()
		{

			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				// TODO Auto-generated method stub
				HttpSession httpSession = request.getSession(true);
				String getid = request.getParameter("id");
				httpSession.setAttribute("id", getid);
	  			response.sendRedirect("http://localhost:8079/jpetstore/actions/Catalog.action");
		       
			}

		};
  }
  
  public Resolution logout() {

	  
	  return new Resolution()
		{

			@Override
			public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
				// TODO Auto-generated method stub
				HttpSession httpSession = request.getSession(true);
				httpSession.invalidate();
				response.sendRedirect("http://localhost:8079/jpetstore/actions/Catalog.action");		       
			}

		};

  }

  /**
   * Clear.
   */
  public void clear() {
    account = new Account();
    myList = null;
    authenticated = false;
  }

}
