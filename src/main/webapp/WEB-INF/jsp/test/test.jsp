<%--

       Copyright 2010-2019 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and 
       limitations under the License.

--%>
<%@ include file="../common/IncludeTop.jsp"%>


<h2>My info</h2>

<table>
	<tr>
		<td>First name:</td>
		<td>${actionBean.account.firstName}</td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td>${actionBean.account.lastName}</td>
	</tr>
	<tr>
		<td>Email:</td>
		<td>${actionBean.account.email}</td>
	</tr>
	<tr>
		<td>Phone:</td>
		<td>${actionBean.account.phone}</td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td>${actionBean.account.address1}</td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td>${actionBean.account.address2}</td>
	</tr>
	<tr>
		<td>City:</td>
		<td>${actionBean.account.city}</td>
	</tr>
	<tr>
		<td>State:</td>
		<td>${actionBean.account.state}</td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td>${actionBean.account.zip}</td>
	</tr>
	<tr>
		<td>Country:</td>
		<td>${actionBean.account.country}</td>
	</tr>

	
</table>

<h2>My Orders</h2>

<table>
	<tr>
		<th>Order ID</th>
		<th>Date</th>
		<th>Total Price</th>
	</tr>

	<c:forEach var="order" items="${actionBean.orderList}">
		<tr>
			<td><a href="http://localhost:8079/jpetstore/actions/Order.action?viewOrder=&orderId=${order.orderId}">	
			    ${order.orderId}
			  </a></td>
			<td><c:out value="${order.orderDate}"/></td>
			<td>$<fmt:formatNumber value="${order.totalPrice}"
				pattern="#,##0.00" /></td>
		</tr>
	</c:forEach>
</table>


<h2>Fav Category info</h2>

<table>
	<tr>
		<th>Product ID</th>
		<th>Name</th>
	</tr>
	<c:forEach var="product" items="${actionBean.productList}">
		<tr>
			<td><stripes:link
				beanclass="org.mybatis.jpetstore.web.actions.CatalogActionBean"
				event="viewProduct">
				<stripes:param name="productId" value="${product.productId}" />
				${product.productId}
			</stripes:link></td>
			<td>${product.name}</td>
		</tr>
	</c:forEach>
</table>

<%@ include file="../common/IncludeBottom.jsp"%>