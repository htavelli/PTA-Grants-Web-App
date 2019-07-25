/**
 * simple js functionality for PTA Grants webpage
 */

function toggleCheckbox(source, name) {
  var checkboxes = document.getElementsByName(name);
  for (var i=0, n=checkboxes.length; i<n; i++) {
    checkboxes[i].checked = source.checked;
  }
}

function toggleRadio(source, typeName) {
  let checkboxes = document.querySelectorAll( 'input[type=' + typeName + ']' );
  let length = checkboxes.length;
  for (let i=0; i < length; i++) {
    if (checkboxes[i].value == "true") {
      checkboxes[i].checked = source.checked;
    }
  }
}

function validateAmount(projectId) {
  let dollarAmount = document.getElementsByName(projectId + "_amount")[0].value;
  if (document.getElementsByName(projectId + "_approve")[0].checked && dollarAmount <= 0) {
    alert("Approval amount must be greater than $0.00 for approved grants.");
  }
  else{
    if (document.getElementsByName(projectId + "_approve")[1].checked && dollarAmount > 0) {
      alert("Unapproved grants must have approval amount = $0.00.");
    }
  }
}

//Start of function to validate date but haven't yet worked through what datatype value is and how to convert to string to check regex
// function validateDate(name) {
//   let re = /\d\d\d\d-\d\d-\d\d/;
//   let value = document.getElementsByName(name)[0].value;
//   if (! value.test(re)) {
//     alert("Date must be in YYYY-MM-DD format.");
//   }
//   else {
//     alert("got a match");
//   }
// }

function validateAtLeastOneChecked(name) {
  let checkboxes = document.getElementsByName(name);
  let length = checkboxes.length;
  let someChecked = false;
  for (let i = 0; i < length; i++) {
    if(checkboxes[i].checked) {
      someChecked = true;
      break;
    }
  }
  if (! someChecked) {
    alert("You must select at least one checkbox.");
    return false;
  }
}

function clickedBack() {
  let backMessage = "When you go back, you will lose any unsubmitted changes.  Are you sure you want to go back?";
  return sendConfirm(backMessage);
}

function clickedLogout() {
  let logoutMessage = "When you logout, you will lose any unsubmitted changes.  Are you sure you want to logout?";
  return sendConfirm(logoutMessage);
}

function sendConfirm(message) {
  return confirm(message);
}
