<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html lang = "en">
  <head>
    <meta charset="ISO-8859-1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>PTA Grants Admin Dashboard</title>
    <!-- bootstrap -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
		<!--fontawesome link -->
		<script src="https://kit.fontawesome.com/ac7913ff4e.js"></script>
		<!-- local CSS file -->
		<link rel="stylesheet" href = "css/pta.css">
    <link rel="stylesheet" href = "css/dashboardPta.css">
    <script src="js/pta.js"></script>
  </head>

  <body>

    <!--link to file with header-->
    <%@include file = "header.html" %>

    <!-- general info on awarding grants for grant committee members-->
    <main>
      The Grants Committee <span>awards grants</span> based on the number of students impacted and funds available, as well as the
      goals, objectives, and uniqueness of the project.
    </main>

    <!--error -->
    <c:if test = "${requestScope.adminError != null}">
      <div class = "error">
        ${requestScope.adminError}
      </div>
    </c:if>

    <!--message for successful added grant cycle-->
    <c:if test = "${param['cycle_added'] != null}">
      <section class = "input-section">
        <h2>Grant Cycle: ${param['cycle_added']} has been successfully added</h2>
      </section>
    </c:if>

    <!--message for successful deleted grant cycle-->
    <c:if test = "${param['cycle_deleted'] != null}">
      <section class = "input-section">
        <h2>Grant Cycle: ${param['cycle_deleted']} has been successfully deleted</h2>
      </section>
    </c:if>

    <!--message for successful date update for grantCycle-->
    <c:if test = "${param['grant_updated'] != null}">
      <section class = "input-section">
        <h2>Grant Cycle: ${param['grant_updated']} has been successfully updated</h2>
      </section>
    </c:if>

    <!--message for successfully marking round complete-->
    <c:if test = "${param['grant_now_complete'] != null}">
      <section class = "input-section">
        <h2>Grant Cycle: ${param['grant_now_complete']} now marked </span>complete</span></h2>
      </section>
    </c:if>

    <!--message for successfully marking round open-->
    <c:if test = "${param['grant_now_open'] != null}">
      <section class = "input-section">
        <h2>Grant Cycle: ${param['grant_now_open']} now marked </span>open</span></h2>
      </section>
    </c:if>

    <!--success message for grants approved-->
    <c:if test = "${paramValues['grant_approved'] != null}">
      <section class = "input-section">
        <h2>Thank you for approving the following grants:</h2>
          <c:forEach items = "${paramValues['grant_approved']}" var = "grant">
            <h4>${grant}</h4>
          </c:forEach>
          <h3>If this round is now complete, be sure to use the Admin Actions button below and
            mark it as complete in the "Open/Close Any Grant Cycle Section".</h3>
      </section>
    </c:if>

    <!--section to log in-->
    <c:choose>
      <c:when test = "${sessionScope.admin == null}">
        <section class = "input-section">
          <h2>Admin Login</h2>
          <br/>
            <!-- TODO: purchase SSL certificate if used in the real world-->
          <form method = "post" action = "login">
          	<label>Admin Username <input type = "text" name = "user_name" required></label>
          	<label>Admin Password <input type = "password" name = "password" required></label>
          	<input type = "submit" value = "Submit">
          </form>
          <h3>As this is an example webpage, login with:</h3>
          <h4><span>Username:</span> admin and <span>Password:</span> grantpassword4u<h4>
          <%--username: "admin" password: "grantpassword4u"  Thank you for looking at my project!--%>
        </section>
      </c:when>

      <c:otherwise>
            <c:choose>

              <%--next step in updating dates in grant cycle--%>
              <c:when test = "${requestScope.roundDatesToUpdate != null}">
                <section class = "input-section">
                  <h2>${roundDatesToUpdate}</h2>
                  <form method = "post" action = "updateGrantCycleDates">
                    <%--TODO: JS validation for date format --%>
                    <input type = "hidden" name = "cycle_name" value = "${roundDatesToUpdate}">
                      <label>New Start Date
                      <input type = "date" name = "start_date" placeholder = "YYYY-MM-DD" required>
                    </label><br/><br/>
                      <label>New End Date
                        <input type = "date" name = "end_date" placeholder = "YYYY-MM-DD" required>
                      </label><br/><br/>
                    <input type = "submit" value = "Submit">
                  </form>
                </section>
              </c:when>

              <%--viewing section for grants submitted in open grant cycles--%>
              <c:when test = "${requestScope.currentGrants != null}">
                    <section class = "input-section">
                      <c:choose>
                        <c:when test ="${fn:length(requestScope.currentGrants) > 0}">
                          <h2>Grant Applications from Open Grant Cycles</h2> <br/>
                            <p>Note:
                              If more than one grant cycle displays, there are multiple incomplete grant rounds.  To correct this,
                              go to "Open/Close Any Grant Cycle" by going to Admin Actions and marking complete grant rounds as "complete".
                            </p>

                          <c:forEach items = "${requestScope.currentGrants}" var = "currentGrant">
                            <h3 class = "proj_header"><span>Project Name</span>: ${currentGrant['project_name']}</h3>
                            <div class = "row">
                              <div class = "col-md">
                                <p><span>Grant Round</span>: ${currentGrant['grant_round']}<p>
                                <p><span>Applicant</span>: ${currentGrant['submitter']}</p>
                                <c:if test = "${currentGrant['goals_objectives'] != null}">
                                  <p><span>Goals and Objectives</span>: ${currentGrant['goals_objectives']}</p>
                                </c:if>
                                <c:if test = "${currentGrant['start_date'] != null}">
                                  <p><span>Start Date</span>: ${currentGrant['start_date']}</p>
                                </c:if>
                              </div>
                              <div class = "col-md">
                                <c:if test = "${currentGrant['future_use'] != null}">
                                  <p><span>Project Will Be Used In the Future</span>: ${currentGrant['future_use']}</p>
                                </c:if>
                                <c:if test = "${currentGrant['grade_levels'] != null}">
                                  <p><span>Grade Levels</span>: ${currentGrant['grade_levels']}</p>
                                </c:if>
                                <c:if test = "${currentGrant['num_students'] != null}">
                                  <p><span>Number of students impacted</span>: ${currentGrant['num_students']}</p>
                                </c:if>
                                <c:if test = "${currentGrant['similar_projects'] != null}">
                                  <p><span>Similar Projects are Underway</span>: ${currentGrant['similar_projects']}</p>
                                </c:if>
                                <c:if test = "${currentGrant['shipping_cost'] != null}">
                                  <p><span>Shipping cost</span>: <fmt:formatNumber type = "number"
                                    minFractionDigits = "2" value = "${currentGrant['shipping_cost'] }" /></p>
                                </c:if>
                                <c:if test = "${currentGrant['total_cost'] != null}">
                                  <p><span>Total cost</span>: <fmt:formatNumber type = "number"
                                    minFractionDigits = "2" value = "${currentGrant['total_cost'] }" /></p>
                                </c:if>
                                <c:if test = "${currentGrant['other_info'] != null}">
                                  <p><span>Other info</span>:
                                  <c:choose>
                                    <c:when test = "${currentGrant['other_info']}">
                                      ${currentGrant['other_info']}</p>
                                    </c:when>
                                    <c:otherwise>
                                      None provided
                                    </c:otherwise>
                                  </c:choose>
                                </c:if>
                              </div>
                            </div>
                              <c:if test = "${fn:length(currentGrant['items']) > 0}">
                                <a data-toggle="collapse" href="#itemsTable" class="btn btn-danger btn-block" role="button" aria-expanded="false"
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
                                  <c:forEach items = "${currentGrant['items']}" var = "item">
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
                                <br/>
                              </c:if>
                          </c:forEach>
                      </c:when>

                      <c:otherwise>
                        <h2>There are no current grant applications to view at this time.
                           If the grant cycle is still open and accepting new applications, check back later.
                         </h2>
                      </c:otherwise>

                    </c:choose>
                    <br/>
                  </section>
                </c:when>

              <%--next step to approving grants--%>
              <c:when test = "${requestScope.grantsToApprove != null}">
                <section class = "input-section">
                  <c:choose>
                    <c:when test = "${fn:length(requestScope.grantsToApprove) > 0}">
                      <h2>Approve grants below.  </h2>
                        <br/>
                        <p>If you choose "approved", be sure to enter the dollar amount awarded.  <br/>
                        Note:
                        If more than one grant cycle displays, there are multiple incomplete grant rounds.  To correct this,
                        go to "Open/Close Any Grant Cycle" by going Admin Actions and marking complete grant rounds as "complete"
                        before preceding with approval.</p>
                      <form method = "post" action = "approveGrants">
                        <table>
                          <caption>Grants to Approve</caption>
                          <th>Mark Approvals <br/>
                            <input type = "checkbox" onClick='toggleRadio(this, "radio")'> Approve all
                          </th>
                          <th>Dollar Amount Approved</th>
                          <th>Grant Cycle</th>
                          <th>Project Name</th>
                          <th>Teacher or Department</th>
                          <th>Total Cost</th>
                            <c:forEach items = "${requestScope.grantsToApprove}" var = "grant">
                              <tr>
                                <td>
                                  <label>
                                    <input type = "radio" name = "${grant['project_id']}_approve" value = "true" required> Approved
                                    <br/>
                                    <input type = "radio" name = "${grant['project_id']}_approve" value = "false"required> Not approved
                                  </label>
                                </td>
                                <td>
                                  <label>
                                    <input type = "number" class = "approveAmountInput" name = "${grant['project_id']}_amount" onblur = 'validateAmount("${grant['project_id']}")' min = "0" step = "any" value = "0.00">
                                  </label>
                                <td>${grant['grant_round']}</td>
                                <td>${grant['project_name']}</td>
                                <td>${grant['submitter']}</td>
                                <td><fmt:formatNumber type = "number"
                                  minFractionDigits = "2" value = "${grant['total_cost']}" /></td>
                              </tr>
                              <input type = "hidden" name = "${grant['project_id']}_id" value = "${grant.project_id}">
                              <input type = "hidden" name = "${grant['project_id']}_name" value = "${grant.project_name}">
                            </c:forEach>
                        </table>
                        <br/>
                        <input type = "submit" value = "Submit">
                      </form>
                    </c:when>

                    <c:otherwise>
                      <h2>There are no grants awaiting approval at this time.  Check back later, if the current grant round is still open
                        for new applications.</h2>
                    </c:otherwise>

                  </c:choose>
                </section>
              </c:when>

              <%--search results--%>
              <c:when test = "${requestScope.searchResults != null}">
                <section class = "input-section">
                  <c:choose>
                    <c:when test = "${fn:length(requestScope.searchResults) > 0}">
                      <h2>Search Results</h2>
                        <c:forEach items = "${requestScope.searchResults}" var = "submission">
                          <h3 class = "proj_header"><span>Project Name:</span> ${submission['project_name'] } </h3>
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
                              <a data-toggle="collapse" href="#itemsTable" class="btn btn-danger btn-block" role="button" aria-expanded="false"
                  							aria-controls="itemsTable">Project Items</a>
                  							<br>
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
                              <br/>
                          </c:if>
                      </c:forEach>
                    </c:when>

                    <c:otherwise>
                      <h3>No grants matched your search criteria.</h3>
                    </c:otherwise>

                  </c:choose>
                  <br/><br/>
                </section>
              </c:when>

              <%--main landing area for admin to take all actions once logged in--%>
              <c:otherwise>
                <section class = "input-section">
                  <h2>Admin Actions</h2>

                  <div class = "row">
                    <div class = "col-lg">
                      <h3>Manage Grant Cycles</h3>

                      <%--define a new grant cycle--%>
                      <a data-toggle="collapse" href="#defineCycle" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="defineCycle">Define a New Grant Cycle</a>
                        <br>
                      <form method = "post" action = "defineGrantCycle" class="collapse" id="defineCycle">
                        <label>Start Date<br/>
                          <%--TODO: add JS to validate date format --%>
                          <input type = "date" name = "cycle_start" placeholder = "YYYY-MM-DD" required>
                        </label><br/>
                        <label>End Date<br/>
                          <input type = "date" name = "cycle_end" placeholder = "YYYY-MM-DD" required>
                        </label><br/>
                        <label>Round Name (Use season and year)<br/>
                          <input type = "text" name = "cycle_name" required>
                        </label><br/>
                        <input type = "submit" value = "Submit">
                      </form>

                      <br/><br/>

                      <%-- delete a cycle--%>
                      <a data-toggle="collapse" href="#deleteCycle" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="deleteCycle">Delete a Grant Cycle</a>
                        <br>
                      <div class="collapse" id="deleteCycle">
                        <c:choose>
                          <c:when test = "${fn:length(sessionScope.openCycleList) > 0}">
                            <p>Note: You will not be able to delete the grant cycle if it is complete or
                              someone has submitted an application to it.
                            </p>
                            <form method = "post" action = "deleteGrantCycle">
                              <label>Grant Cycles<br/>
                                <select name = "grant_cycles" required>
                                    <option value = "" selected disabled>Please select</option>
                                    <c:forEach items="${sessionScope.openCycleList}" var="cycle">
                                        <option value="${cycle['cycle_name']}">${cycle['cycle_name']}</option>
                                    </c:forEach>
                                </select>
                              </label>
                              <input type = "submit" value = "Delete">
                            </form>
                          </c:when>

                          <c:otherwise>
                            <h3>No grant cycles can be deleted at this time.</h3>
                          </c:otherwise>
                        </c:choose>
                      </div>

                      <br/><br/>

                      <%--beginning of process to update cycle start and end dates--%>
                      <a data-toggle="collapse" href="#updateCycle" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="updateCycle">Update Dates for a Grant Cycle</a>
                        <br>
                      <div class="collapse" id="updateCycle">
                        <p>Only the start and end dates of a round can be updated.
                          If a cycle is not listed here, it is already marked as complete and dates cannot be changed.</p>
                          <c:choose>
                            <c:when test = "${fn:length(sessionScope.openCycleList) > 0}">
                              <form method = "get" action = "updateGrantCycleDates">
                                <label>Grant Cycles<br/>
                                  <select name = "grant_cycles" required>
                                      <option value = "" selected disabled>Please select</option>
                                      <c:forEach items="${sessionScope.openCycleList}" var="cycle">
                                          <option value="${cycle['cycle_name']}">${cycle['cycle_name']}</option>
                                      </c:forEach>
                                  </select>
                                </label>
                                <input type = "submit" value = "Update Dates">
                              </form>
                            </c:when>

                            <c:otherwise>
                              <h3>None available</h3>
                            </c:otherwise>
                          </c:choose>
                      </div>

                      <br/><br/>

                      <%--update completion status on any cycle--%>
                      <a data-toggle="collapse" href="#updateCompleteStatusAllRounds" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="updateCompleteStatusAllRounds">Open/Close Any Grant Cycle</a>
                        <br>
                      <div class="collapse" id="updateCompleteStatusAllRounds">
                        <p>Use this section with care.  If dates overlap between open grant cycles, there will be problems
                          when staff try to submit new applications.  It is advised that only one grant cycle should be open at any given time.</p>
                          <h4>Grant Cycle to Mark <span>Complete</span>:</h4>
                        <c:choose>
                            <c:when test = "${fn:length(sessionScope.openCycleList) > 0}">
                              <form method = "post" action = "markGrantCycleComplete">
                              <label>
                                <select name = "grant_cycles" required>
                                    <option value = "" selected disabled>Please select</option>
                                    <c:forEach items="${sessionScope.openCycleList}" var="cycle">
                                        <option value="${cycle['cycle_name']}">${cycle['cycle_name']}</option>
                                    </c:forEach>
                                </select>
                              </label>
                                <input type = "submit" value = "Close Cycle">
                            </form>
                          </c:when>

                          <c:otherwise>
                            <h3>None available</h3>
                          </c:otherwise>
                        </c:choose>

                        <br/><br/>

                        <h4>Grant Cycle To Mark <span>Open</span>:</h4>
                          <c:choose>
                            <c:when test = "${fn:length(sessionScope.completedCycleList) > 0}">
                              <form method = "post" action = "markGrantCycleOpen">
                                <label>
                                <select name = "grant_cycles" required>
                                    <option value = "" selected disabled>Please select</option>
                                    <c:forEach items="${sessionScope.completedCycleList}" var="cycle">
                                        <option value="${cycle['cycle_name']}">${cycle['cycle_name']}</option>
                                    </c:forEach>
                                </select>
                              </label>
                              <input type = "submit" value = "Open Cycle">
                            </form>
                          </c:when>

                          <c:otherwise>
                            <h3>None available</h3>
                          </c:otherwise>
                        </c:choose>
                      </div>

                      <br/><br/>
                    </div>

                    <div class = "col-lg">
                      <h3>Manage Grant Applications</h3>
                      <%--form for viewing all current submissions--%>
                      <a data-toggle="collapse" href="#viewCurrent" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="viewCurrent">View Current Applications</a>
                        <br>
                    	<form method = "get" action = "adminViewCurrentSubmissions" class="collapse" id="viewCurrent">
                        <label>Which fields do you want to view for each application?  Check all that apply.<br/>
                          <input type = "checkbox" onClick = 'toggleCheckbox(this, "appField")'> Select all<br/>
                          <input type = "checkbox" value = "project_name" checked disabled> Project Title<br/>
                          <input type = "checkbox" value = "submitter" checked disabled> Applicant<br/>
                          <input type = "checkbox" name = "appField" value = "goals_objectives"> Goals and objectives<br/>
                          <input type = "checkbox" name = "appField" value = "items"> Items requested<br/>
                          <input type = "checkbox" name = "appField" value = "start_date"> Start Date<br/>
                          <input type = "checkbox" name = "appField" value = "future_use"> Future use<br/>
                          <input type = "checkbox" name = "appField" value = "grade_levels"> Grade levels involved<br/>
                          <input type = "checkbox" name = "appField" value = "num_students"> Number of students impacted<br/>
                          <input type = "checkbox" name = "appField" value = "similar_projects"> Similar projects underway<br/>
                          <input type = "checkbox" name = "appField" value = "other_info"> Other information<br/>
                          <input type = "checkbox" name = "appField" value = "shipping_cost"> Shipping cost<br/>
                          <input type = "checkbox" name = "appField" value = "total_cost"> Total cost<br/>
                        </label><br/>
                        <input type = "hidden" name = "appField" value = "project_name">
                        <input type = "hidden" name = "appField" value = "submitter">
                    		<input type = "submit" value = "View Applications">
                    	</form>

                      <br/><br/>

                      <%--start process to award grants --%>
                      <form method = "get" action = "approveGrants">
                        <input type="submit" value="Award Grants" class="btn btn-danger">
                      </form>

                      <br/><br/>

                      <%--form for search--%>
                      <a data-toggle="collapse" href="#search" class="btn btn-danger" role="button" aria-expanded="false"
                        aria-controls="search">Search</a>
                        <br>
                        <div class="collapse" id = "search">
                          <h3>Search By:</h3>
                          <br>
                            <form method = "get" action = "searchAll" >
                              <label>Project Name or Item<br/>
                                <input type = "text" name = "proj_or_items">
                                <input type = "submit" value = "Search">
                              </label>
                            </form>
                            <br/>
                            <form method ="get" action = "searchAll">
                              <label>Teacher's Last Name<br/>
                                <input type = "text" name = "teacher_name"><br/>
                              </label>
                              <input type = "submit" value = "Search">
                            </form>
                            <br/>

                            <form method = "get" action = "searchAll">
                              <label>Department<br/>
                                <select name = "departments">
                                  <option value = "" selected disabled>Please select</option>
                                    <c:forEach items="${departmentList}" var="dept">
                                      <option value="${dept['name']}">${dept['name']}</option>
                                    </c:forEach>
                                </select>
                              </label>
                              <input type = "submit" value = "Search">
                            </form>
                            <br/>

                            <form method = "get" action = "searchAll">
                              <label>Grant Cycle<br/>
                                <select name = "grant_cycles">
                                    <option value = "" selected disabled>Please select</option>
                                  <c:forEach items="${sessionScope.openCycleList}" var="openCycle">
                                      <option value="${openCycle['cycle_name']}">${openCycle['cycle_name']}</option>
                                  </c:forEach>
                                  <c:forEach items="${sessionScope.completedCycleList}" var="completedCycle">
                                      <option value="${completedCycle['cycle_name']}">${completedCycle['cycle_name']}</option>
                                  </c:forEach>
                                </select>
                    		      </label>
                              <input type = "submit" value = "Search">
                            </form>
                            <br/>

                            <form method = "get" action = "searchAll">
                        		<label>Grade Level <br/>
                              <select name="grade_levels">
                                <option value = "" selected disabled>Please select</option>
                                <option value="Kindergarten">Kindergarten</option>
                                <option value="First">First</option>
                                <option value="Second">Second</option>
                                <option value="Third">Third</option>
                                <option value="Fourth">Fourth</option>
                                <option value="Fifth">Fifth</option>
                              </select>
                            </label>
                            <input type = "submit" value = "Search">
                        	</form>
                        </div>

                        <br/><br/>

                      </section>
                    </div>
                  </c:otherwise>
                </c:choose>
          </c:otherwise>
      </c:choose>

      <br/>

      <c:if test = "${sessionScope.admin !=null}">
        <%--takes user back but does not invalidate session--%>
        <%--reset button to invalidate session --%>
        <%@include file = "adminBackResetButtons.html" %>
      </c:if>

      <section class = "btn-secondary">
        <%@include file = "guidelinesToggle.html" %>
      </section>


    <!--link to file with footer -->
    <%@include file = "footer.html" %>

    <!--JS, Popper.js, and jQuery -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
  </body>

</html>
