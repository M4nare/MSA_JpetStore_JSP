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
import net.sourceforge.stripes.integration.spring.SpringBean;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.mybatis.jpetstore.domain.Category;
import org.mybatis.jpetstore.domain.Item;
import org.mybatis.jpetstore.domain.Product;
import org.springframework.web.client.RestTemplate;


/**
 * The Class CatalogActionBean.
 *
 * @author Eduardo Macarron
 */
@SessionScope
public class CatalogActionBean extends AbstractActionBean {

  private static final long serialVersionUID = 5849523372175050635L;

  private static final String MAIN = "/WEB-INF/jsp/catalog/Main.jsp";
  private static final String VIEW_CATEGORY = "/WEB-INF/jsp/catalog/Category.jsp";
  private static final String VIEW_PRODUCT = "/WEB-INF/jsp/catalog/Product.jsp";
  private static final String VIEW_ITEM = "/WEB-INF/jsp/catalog/Item.jsp";
  private static final String SEARCH_PRODUCTS = "/WEB-INF/jsp/catalog/SearchProducts.jsp";



  private String keyword;

  private String categoryId;
  private JSONObject category;
  private List<Category> categoryList;

  private String productId;
  private JSONObject product;
  private List<Product> productList;

  private String itemId;
  private JSONObject item;
  private List<Item> itemList;

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(String categoryId) {
    this.categoryId = categoryId;
  }

  public String getProductId() {
    return productId;
  }

  public void setProductId(String productId) {
    this.productId = productId;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public JSONObject getCategory() {
    return category;
  }

  public void setCategory(JSONObject category) {
    this.category = category;
  }

  public JSONObject getProduct() {
    return product;
  }

  public void setProduct(JSONObject product) {
    this.product = product;
  }

  public JSONObject getItem() {
    return item;
  }

  public void setItem(JSONObject item) {
    this.item = item;
  }

  public List<Category> getCategoryList() {
    return categoryList;
  }

  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public List<Product> getProductList() {
    return productList;
  }

  public void setProductList(List<Product> productList) {
    this.productList = productList;
  }

  public List<Item> getItemList() {
    return itemList;
  }

  public void setItemList(List<Item> itemList) {
    this.itemList = itemList;
  }

  @DefaultHandler
  public ForwardResolution viewMain() {
    return new ForwardResolution(MAIN);
  }

  /**
   * View category.
   *
   * @return the forward resolution
 * @throws ParseException 
   */
@SuppressWarnings("unchecked")
public ForwardResolution viewCategory() throws ParseException {
    if (categoryId != null) {    	
		    String url="http://catalog:8080/jpetstore/actions/Catalog.action?getProductListByCategory=&id="+categoryId;
		    String url2="http://catalog:8080/jpetstore/actions/Catalog.action?getcategory=&id="+categoryId;
		    RestTemplate restTemplate = new RestTemplate();
	    	String resp = restTemplate.getForObject(url, String.class);
	    	String resp2 = restTemplate.getForObject(url2, String.class);
	    	
	    	JSONParser parser = new JSONParser();
	    	
	    	JSONArray pdl = (JSONArray)parser.parse(resp);
	    	JSONObject ctr = (JSONObject)parser.parse(resp2);
	    	
	    	productList = (List<Product>) pdl;
	    	category = ctr;
	    	
	    
	    	
    }
    return new ForwardResolution(VIEW_CATEGORY);
  }

  /**
   * View product.
   *
   * @return the forward resolution
 * @throws ParseException 
   */
  
  
  @SuppressWarnings("unchecked")
public ForwardResolution viewProduct() throws ParseException {
    if (productId != null) {
    	
    	  	String url="http://catalog:8080/jpetstore/actions/Catalog.action?getItemListByProduct=&id="+productId;
		    String url2="http://catalog:8080/jpetstore/actions/Catalog.action?getproduct=&id="+productId;
		    RestTemplate restTemplate = new RestTemplate();
	    	String resp = restTemplate.getForObject(url, String.class);
	    	String resp2 = restTemplate.getForObject(url2, String.class);
	    	
	    	JSONParser parser = new JSONParser();
	    	
	    	JSONArray ilp = (JSONArray)parser.parse(resp);
	    	JSONObject ctr = (JSONObject)parser.parse(resp2);
	    	
      itemList = ilp;
      product = ctr;
    }
    return new ForwardResolution(VIEW_PRODUCT);
  }


  /**
   * View item.
   *
   * @return the forward resolution
 * @throws ParseException 
   */
  
  
  
  public ForwardResolution viewItem() throws ParseException {

  
    String url="http://catalog:8080/jpetstore/actions/Catalog.action?getitem=&item="+itemId;
    String url2="http://catalog:8080/jpetstore/actions/Catalog.action?getProductid=&item="+itemId;
    RestTemplate restTemplate = new RestTemplate();
	String resp = restTemplate.getForObject(url, String.class);
	String resp2 = restTemplate.getForObject(url2, String.class);
	
	JSONParser parser = new JSONParser();
	
	JSONObject gi = (JSONObject)parser.parse(resp);
	JSONObject gpi = (JSONObject)parser.parse(resp2);
	
	  item = gi;
	  product = gpi;
    return new ForwardResolution(VIEW_ITEM);
  }


  /**
   * Search products.
   *
   * @return the forward resolution
 * @throws ParseException 
   */
  
  
  @SuppressWarnings("unchecked")
public ForwardResolution searchProducts() throws ParseException {
    if (keyword == null || keyword.length() < 1) {
      setMessage("Please enter a keyword to search for, then press the search button.");
      return new ForwardResolution(ERROR);
    } else {
    	 	String url="http://catalog:8080/jpetstore/actions/Catalog.action?searchProducts=&search="+keyword.toLowerCase();
    	    RestTemplate restTemplate = new RestTemplate();
    		String resp = restTemplate.getForObject(url, String.class);
    		
    		JSONParser parser = new JSONParser();   		
    		JSONArray search = (JSONArray)parser.parse(resp);
    		productList = (List<Product>) search;
    		return new ForwardResolution(SEARCH_PRODUCTS);
    }
  }
  

  /**
   * Clear.
   */
  public void clear() {
    keyword = null;

    categoryId = null;
    category = null;
    categoryList = null;

    productId = null;
    product = null;
    productList = null;

    itemId = null;
    item = null;
    itemList = null;
  }

}
