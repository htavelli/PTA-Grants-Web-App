<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang = "en">
  <head>
		<meta charset="ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PTA Grant Submissions</title>
    <!-- bootstrap -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <!--fontawesome link -->
    <script src="https://kit.fontawesome.com/ac7913ff4e.js"></script>
    <!-- local CSS file -->
    <link rel="stylesheet" href = "css/pta.css">
    <link rel="stylesheet" href = "css/submissionPta.css">
    <script src="js/pta.js"></script>
    <script src="js/addItem.js"></script>
  </head>

  <body>
    <fmt:setBundle basename="com.hillarytavelli.resources.ApplicationResources" var="school" />
      <!--link to file with header-->
      <%@include file = "header.html" %>

    <c:if test = "${requestScope.submissionError != null }">
      <div class = "error">
    	   ${requestScope.submissionError }
       </div>
    </c:if>

    <section class="input-section bg-secondary">
      <c:choose>
          <c:when test = "${sessionScope.part_done == null  && requestScope.submissions == null}">
            <h2>Create a New Grant Application</h2>
            <form  method = "post" action = "submitNewSubmission1">
              <div class="row">
                <div class="col-md">
                	<label>Project Title:
                    <input type = "text" name = "project_name" required>
                  </label><br/><br/>
                	<label>Goals and Objectives:<br/>
                    <textarea rows="6" name = "goals_objectives" required></textarea>
                  </label><br/><br/>
                	<!-- TODO: add JS validation to make sure date is in the right format : YYYY-MM-DD -->
                	<label>Start Date:
                    <input type = "date" name = "start_date" placeholder = "YYYY-MM-DD" required>
                  </label><br/><br/>
                	<label>How many students will be impacted by this project?<br/>
                		<select name = "num_students" required>
                			<option value = "" disabled selected>Please select</option>
                			<option value = "1-25">1-25</option>
                			<option value = "26-50">26-50</option>
                			<option value = "51-75">51-75</option>
                			<option value = "76-150">76-150</option>
                			<option value = "151+"> 151+</option>
                		</select>
                	</label><br/><br/>
                </div>
                <div class="col-md">
                	<label>Will the items be used in future years?<br/>
                		<input type = "radio" name = "future_use" value = "true" required> Yes
                		<input type = "radio" name = "future_use" value = "false" required> No
                	</label><br/><br/>
                	<label for = "grade_levels">What grade levels will be involved?</label><br/>
                    <input type = "checkbox" onClick="toggleCheckbox(this, 'grade_levels')"> Select all<br/>
                    <input type = "checkbox" name = "grade_levels" value = "kindergarten"> Kindergarten<br/>
                    <input type = "checkbox" name = "grade_levels" value = "first"> First<br/>
                    <input type = "checkbox" name = "grade_levels" value = "second"> Second<br/>
                    <input type = "checkbox" name = "grade_levels" value = "third"> Third<br/>
                    <input type = "checkbox" name = "grade_levels" value = "fourth"> Fourth<br/>
                    <input type = "checkbox" name = "grade_levels" value = "fifth" > Fifth<br/>
                	<br/><br/>
                  <fmt:setBundle basename="com.hillarytavelli.resources.ApplicationResources"
                    var="school" />
                	<label>Are there similar projects already underway at <fmt:message bundle="${school}"
                    key="label.header.school_name"></fmt:message>? (If you answer yes, please explain in the "other
                	   info" section on a later screen.)<br/>
                		<input type = "radio" name = "similar_projects" value = "true" required>Yes
                		<input type = "radio" name = "similar_projects" value = "false" required>No
                	</label><br/><br/>
                </div>
              </div>
            	   <input type = "submit" onclick = " return validateAtLeastOneChecked('grade_levels')" value = "Continue">
            </form>
          </c:when>

          <c:when test = "${sessionScope.part_done == 1 }">
            <h2>Continue Your New Grant Application</h2>
            <br>
            <form method = "post" action = "submitItems">
            	<fieldset>
            		<legend>Item Information.  Please enter each item in its own section.</legend>
                  <div class="row">
                    <div class= "col-md">
                    	<label>Item Description: <input type = "text" name = "desc_0" required></label><br/><br/>
                    	<label>Vendor: <input type = "text" name = "vendor_0" required></label><br/><br/>
                    </div>
                    <div class= "col-md">
                    	<label>Quantity: <input type = "number" name = "quantity_0" placeholder = "1" min = "1" step = "1"
                        required></label><br/><br/>
                    	<label>Cost Per Item: <input type = "number" name = "cost_0" placeholder = "0.00" min = "1"
                        step = "any" required></label><br/><br/>
                    	<label>Web Link (if applicable): <input type = "text" name = "weblink_0"></label><br/><br/>
                    </div>
                  </div>
            	</fieldset>
              <div id = "addItemsContainer">
                <%--where additional fieldsets will populate--%>
              </div>

              <input type = "button" class = "btn btn-info" onclick = "addItemField()" value = "Add Another Item">
              <br/>
              <br/>

            	<input type = "submit" value = "Submit Items">
            </form>
            <br/>
            <c:if test = "${param['itemAdded'] != null}">
              <form method = "get" action = "goToNextScreen">
              		<input type = "submit" value = "I'm done entering items">
              </form>
            </c:if>

          </c:when>

          <c:when test = "${sessionScope.part_done == 1.5 }">
            <h2>Continue Your New Grant Application</h2>
            <form method = "post" action = "submitNewSubmission2">
            	<label>Other Information:  Please provide any other details we need to know.<br/>
                <textarea rows = "6" name = "other_info"></textarea></label><br/><br/>
            	<label>Total Shipping Cost:<input type = "number" name = "shipping_cost" placeholder = "0.00" min = "0"
                step = "any" required></label><br/><br/>
            	<label>Project Grand Total:<input type = "number" name = "total_cost" placeholder = "0.00" min = "1"
                step = "any" required></label><br/><br/>
            	<input type = "submit" value = "Submit">
            </form>
          </c:when>

            <c:when test = "${sessionScope.part_done == 2 }">
              <h2>Success! Your Completed Grant Application</h2>
              	<h3><span>Project Name:</span> ${sessionScope.submission['project_name'] } </h3>
                  <div class="row">
                    <div class="col-md">
                    	<p><span>Goals and Objectives:</span>  ${submission['goals_objectives'] } </p>
                    	<p><span>Start Date:</span>  ${sessionScope.submission['start_date'] }</p>
                    	<p><span>Number of Students Impacted:</span>  ${sessionScope.submission['num_students'] } </p>
                    	<p><span>Grade Levels Involved:</span> ${sessionScope.submission['grade_levels'] } </p>
                    </div>
                    <div class="col-md">
                    	<p><span>Project Will Be Used In the Future:</span> ${sessionScope.submission['future_use'] } </p>
                    	<p><span>Similar Projects are Underway:</span> ${sessionScope.submission['similar_projects'] }  </p>
                      <p><span>Other Information:</span>
                        <c:choose>
                          <c:when test = "${fn:length(submission['other_info']) > 0}">
                            ${item['other_info']}
                          </c:when>
                          <c:otherwise>
                            None provided
                          </c:otherwise>
                        </c:choose>
                      </p>
                      <p><span>Shipping Cost:</span> <fmt:formatNumber type = "number"
                        minFractionDigits = "2" value = "${sessionScope.submission['shipping_cost'] }" /> </p>
                      <p><span>Total Cost:</span> <fmt:formatNumber type = "number"
                        minFractionDigits = "2" value = "${sessionScope.submission['total_cost'] }" /> </p>
                    </div>
                  </div>

                      <c:if test = "${fn:length(sessionScope.submission['items']) > 0}">
                        <a data-toggle="collapse" href="#itemsTable" class="btn btn-danger" role="button" aria-expanded="false"
                          aria-controls="itemsTable">Project Items</a>
                          <br/><br/>
                        <table class="collapse" id="itemsTable">
                          <tr>
                            <th>Item Description</th>
                            <th>Vendor</th>
                            <th>Quantity</th>
                            <th>Cost per Item</th>
                            <th>Weblink</th>
                          </tr>
                          <c:forEach items = "${sessionScope.submission['items'] }" var = "item">
                            <tr>
                              <td>${item['item_desc']}</td>
                              <td>${item['vendor']}</td>
                              <td>${item['quantity']}</td>
                              <td><fmt:formatNumber type = "number"
                                minFractionDigits = "2" value = "${item['cost_per_item'] }" /></td>
                              <td>
                                 <c:choose>
                                   <c:when test = "${fn:length(item['web_link']) > 0}">
                                     ${item['web_link']}
                                   </c:when>
                                   <c:otherwise>
                                     None provided
                                   </c:otherwise>
                                 </c:choose>
                              </td>
                            </tr>
                          </c:forEach>
                        </table>
                      </c:if>

                <br/>

                <h5>The Grants committee will review your application and contact you soon.  Thank you for submitting a PTA grant.</h5>
            </c:when>

            <c:when test= "${requestScope.submissions != null}">
              <c:choose>
                <c:when test = "${fn:length(requestScope.submissions) > 0}">
                  <h2>Applications You've Submitted During This Grant Cycle</h2>

                    <c:forEach items = "${requestScope.submissions }" var = "submission">
                    	<h3>Project Name: ${submission['project_name'] } </h3>
                        <div class="row">
                          <div class="col-md">
                          	<p><span>Goals and Objectives:</span>  ${submission['goals_objectives'] } </p>
                          	<p><span>Start Date:</span>  ${submission['start_date'] }</p>
                          	<p><span>Number of Students Impacted:</span>  ${submission['num_students'] } </p>
                          	<p><span>Grade Levels Involved:</span> ${submission['grade_levels'] } </p>
                          </div>
                          <div class="col-md">
                          	<p><span>Project Will Be Used In the Future:</span> ${submission['future_use'] } </p>
                          	<p><span>Similar Projects are Underway:</span> ${submission['similar_projects'] }  </p>
                            <p><span>Other Information:</span>
                              <c:choose>
                                <c:when test = "${fn:length(submission['other_info']) > 0}">
                                  ${item['other_info']}
                                </c:when>
                                <c:otherwise>
                                  None provided
                                </c:otherwise>
                              </c:choose>
                            </p>
                            <p><span>Shipping Cost:</span> <fmt:formatNumber type = "number"
                              minFractionDigits = "2" value = "${submission['shipping_cost'] }" /> </p>
                            <p><span>Total Cost:</span> <fmt:formatNumber type = "number"
                              minFractionDigits = "2" value = "${submission['total_cost'] }" /> </p>
                          </div>
                        </div>

                        <c:if test = "${fn:length(submission['items']) > 0}">
                          <a data-toggle="collapse" href="#itemsTable" class="btn btn-danger" role="button" aria-expanded="false"
                            aria-controls="itemsTable">Project Items</a>
                            <br/><br/>
                          <table class="collapse" id="itemsTable">
                            <tr>
                              <th>Item Description</th>
                              <th>Vendor</th>
                              <th>Quantity</th>
                              <th>Cost per Item</th>
                              <th>Weblink</th>
                            </tr>
                            <c:forEach items = "${submission['items'] }" var = "item">
                              <tr>
                                <td>${item['item_desc']}</td>
                                <td>${item['vendor']}</td>
                                <td>${item['quantity']}</td>
                                <td><fmt:formatNumber type = "number"
                                  minFractionDigits = "2" value = "${item['cost_per_item'] }" /></td>
                                <td>
                                   <c:choose>
                                     <c:when test = "${fn:length(item['web_link']) > 0}">
                                       ${item['web_link']}
                                     </c:when>
                                     <c:otherwise>
                                       None provided
                                     </c:otherwise>
                                   </c:choose>
                                </td>
                              </tr>
                            </c:forEach>
                          </table>
                        </c:if>
                  <br/>
                </c:forEach>
                  <br/>
                  <h5>The Grants committee will review your application and contact you soon.  Thank you for submitting a PTA grant.</h5>
                </c:when>

                <c:otherwise>
                  <h2>No Applications on File for this Grant Cycle</h2>
                </c:otherwise>
              </c:choose>
          </c:when>

        <c:otherwise></c:otherwise>
      </c:choose>

    </section>

    <%@include file = "backResetButtons.html" %>

    <!--link to file with timeline and next steps-->
    <%@include file = "nextSteps.html" %>

    <!--link to file with footer -->
    <%@include file = "footer.html" %>

    <!--JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
  </body>
</html>
