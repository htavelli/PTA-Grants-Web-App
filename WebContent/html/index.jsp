<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang = "en">
	<fmt:setBundle basename="com.hillarytavelli.resources.ApplicationResources" var="school" />
	<head>
		<meta charset="ISO-8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title><fmt:message bundle="${school}" key="label.header.school_name"></fmt:message> PTA Grants</title>

		<!-- bootstrap -->
		<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
		<!--fontawesome link -->
		<script src="https://kit.fontawesome.com/ac7913ff4e.js"></script>
		<!-- local CSS file -->
		<link rel="stylesheet" href = "css/pta.css">
    <script src="js/pta.js"></script>
	</head>

	<body>

			<!--link to file with header-->
			<%@include file = "header.html" %>

			<!--general grant information -->
			<main>
					Each year, the <fmt:message bundle="${school}" key="label.header.school_name"></fmt:message>
					<span>PTA</span> allocates funds to provide <span>grants</span> to teachers and staff members of <fmt:message bundle="${school}"
					key="label.header.school_name"></fmt:message>.  During each grant cycle, any teacher or staff member is welcome to apply as long
		 			as the proposed grant will <span>directly impact students</span>.
			</main>

			<!--guidelines and reminders -->
			<%@include file = "guidelinesAndReminders.html" %>

			<!--error -->
			<c:if test = "${requestScope.submitterError != null}">
				<div class = "error">
					${requestScope.submitterError}
				</div>
			</c:if>

			<div class="row no-gutters" id = "getStarted">
				<div class="col-0 col-lg-8 img-div"></div>

		<!-- Section to start grant submission process  -->
				<c:if test = "${requestScope.teacher == null && requestScope.department == null && sessionScope.submitter == null && !requestScope.enterContact && !requestScope.updateTeacherName}" >
					<section class="col-12 col-lg-4 input-section">
						<div class = "teacher-dept">
								<h2>Teachers</h2>
								<h3>Have you submitted a grant to PTA before?<br>Let's find you!</h3>
								<form method = "get" action = "findTeacher">
									<label>Email Address:<br/><input type="email" name="email" required ></label>
									<input type="submit" value="Submit">
								</form>

								<a data-toggle="collapse" href="#newTeacher" class="btn btn-danger" role="button" aria-expanded="false"
									aria-controls="newTeacher">New to submitting grants?</a>
									<br>
							 	<form method = "post" action = "findTeacher" class="collapse" id="newTeacher">
							 		<label>Email Address<br/>
							 		<input type = "email" name = "email" required></label><br/>
							 		<label>First Name<br/>
							 		<input type = "text" name = "first_name" required></label><br/>
							 		<label>Last Name<br/>
							 		<input type = "text" name = "last_name" required></label>
							 		<input type="submit" value="Submit">
							 	</form>
							</div>

							<div class="teacher-dept">
								<h2>Departments</h2>
							 	<h3>Are you representing a team or department?</h3>
								<br>
									<form method="get" action = "findDepartment">
                    <label><select name = "departments" required>
                        <option value = "" selected disabled>Please select</option>
                      <c:forEach items="${departmentList}" var="dept">
                            <option value="${dept['name']}">${dept['name']}</option>
                        </c:forEach>
                    </select></label>
										<input type = "submit" value = "Submit">
									</form>

									<br>
							<a data-toggle="collapse" href="#newDept" class = "btn btn-danger" role="button" aria-expanded="false"
								aria-controls="newDept">Need to add your team/department?</a>
								<br>
									<form method = "post" action = "findDepartment" id="newDept" class="collapse">
										<h2>Add a new Department</h2>
										<label>Department Name<br/>
										<input type = "text" name = "department_name" required></label><br/>
										<label>Contact Name<br/>
										<input type = "text" name = "contact_name" required></label><br/>
										<label>Contact Email<br/>
										<input type = "email" name = "contact_email" required></label>
										<input type="submit" value="Submit">
									</form>
								</div>
								<%@include file = "backResetButtons.html" %>
						</section>
					</c:if>

		<!--verification/update of contact information section -->
				<c:if test = "${requestScope.teacher != null}" >
					<section class="col-12 col-lg-4 input-section">
						<div class="position-inner">
							<h2>Welcome Teacher</h2>
							<h3>Verify Your Information:</h3>
								<p><span> First Name:</span> ${teacher['first_name']}</p>
								<p><span> Last Name:</span> ${teacher['last_name']}</p>
								<form method = "get" action = "verifyTeacherInfo">
									<input type = "hidden" name = "email" value = "${teacher['email']}">
									<input type = "submit" name = "verify" value = "Verify and Continue">
									<input type = "submit" name = "change" value = "Update this Information">
								</form>
							<%@include file = "backResetButtons.html" %>
						</div>
					</section>
				</c:if>

				<c:if test = "${requestScope.department != null}">
					<section class="col-12 col-lg-4 input-section">
						<div class="position-inner">
							<h2>Welcome ${department['name'] }</h2>

							<c:if test = "${department['contact_name'] != null && department['contact_email'] != null}">
								<h3>Verify Contact Person's Information:</h3>
									<p><span> Contact Person's Name:</span> <br/>
										${department['contact_name']}
									</p>
									<p><span> Contact Person's Email:</span> <br/>
										${department['contact_email']}
									</p>
									<form class="btn  btn-block" method = "get" action = "verifyDepartmentInfo">
										<input type = "hidden" name = "dept_name" value = "${department['name']}">
										<input type = "submit" name = "verify" value = "Verify and Continue">
										<input type = "submit" name = "change" value = "Update Info">
									</form>
									<%@include file = "backResetButtons.html" %>
		           </c:if>

    					<c:if test ="${requestScope.department['contact_name'] == null || requestScope.department['contact_email'] == null }">
    							<h3>Add information for a contact person to continue.</h3>
    							<form method = "get" action = "verifyDepartmentInfo">
    								<input type = "hidden" name = "dept_name" value = "${department['name']}">
    								<input type = "submit" name = "change" value = "OK">
    							</form>
    							<%@include file = "backResetButtons.html" %>
    					</c:if>

            </div>
          </section>
				</c:if>

				<c:if test = "${requestScope.enterContact}">
					<section class="col-12 col-lg-4 input-section">
						<div class="position-inner">
							<h2>Enter Contact Person Information for ${requestScope.dept_name } </h2>
								<form method = "post" action = "addDepartmentContact">
									<input type = "hidden" name = "dept_name" value = "${requestScope.dept_name }">
									<label>Contact Name<br/>
								 		<input type = "text" name = "contact_name" required>
								 	</label><br/>
								 	<label>Contact Email<br/>
								 		<input type = "email" name = "contact_email" required>
								 	</label><br/>
								 		<input type="submit" value="Submit">
								</form>
								<%@include file = "backResetButtons.html" %>
						</div>
					</section>
				</c:if>

				<c:if test = "${requestScope.updateTeacherName}">
					<section class="col-12 col-lg-4 input-section">
						<div class="position-inner">
							<h2>Update Information for ${requestScope.teacher_email } </h2>
								<form method = "post" action = "updateTeacherInfo">
									<input type = "hidden" name = "teacher_email" value = "${requestScope.teacher_email }">
									<label>First Name<br/>
								 		<input type = "text" name = "first_name" required>
								 	</label><br/>
								 	<label>Last Name<br/>
								 		<input type = "text" name = "last_name" required>
								 	</label><br/>
								 		<input type="submit" value="Submit">
								</form>
								<%@include file = "backResetButtons.html" %>
						</div>
					</section>
				</c:if>

			<!--session is set and now person can choose their next action -->
				<c:if test = "${sessionScope.submitter != null }">
						<section class="col-12 col-lg-4 input-section">
							<div class="position-inner">
								<h2>Applicant: <br/><span>${sessionScope.submitter }</span> </h2>
								<form class="btn btn-block" method = "get" action = "viewCurrentSubmission">
									<input type = "submit" value = "View Open Applications">
								</form>
								<br/>
								<form class="btn btn-block" method = "get" action = "goToNewSubmission">
									<input type = "submit" value = "Start a New Application">
								</form>
								<br/>
								<%@include file = "backResetButtons.html" %>
							</div>
						</section>
				</c:if>
		</div>

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
