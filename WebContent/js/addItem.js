var counter = 1;

function addItemField() {
  const container = document.getElementById("addItemsContainer");

  var fieldSetElement = document.createElement("fieldset");
  var legendElement = document.createElement("legend");
  var divElement = document.createElement("div");
  var innerDivElement = document.createElement("div");
  var secondInnerDivElement = document.createElement("div");

  container.appendChild(fieldSetElement);

  //nest legend in fieldset
  legendElement.innerHTML = "Item Information.  Please enter each item in its own section.";
  fieldSetElement.appendChild(legendElement);

  //add delete anchor tag
  var deleteElement = document.createElement("a");
  deleteElement.innerHTML = "Delete this item set";
  deleteElement.setAttribute('href', '#deleteItems');
  deleteElement.setAttribute('class', 'btn btn-info');
  deleteElement.setAttribute('onclick', 'deleteThisItemSet(this)');
  fieldSetElement.appendChild(deleteElement);
  fieldSetElement.appendChild(document.createElement("br"));
  fieldSetElement.appendChild(document.createElement("br"));

  //set wrapper div class and nest in fieldset
  divElement.setAttribute('class', 'row');
  fieldSetElement.appendChild(divElement);

  //set div wrapper for bootstrap col name and nest in div
  innerDivElement.setAttribute('class', 'col-md');
  divElement.appendChild(innerDivElement);

  //add item desc label and input nested in div
  var labelElement = addLabel("Item Description: ");
  var inputElement = addInput( 'text', ('desc_' + counter));
  innerDivElement.appendChild(labelElement);
  labelElement.appendChild(inputElement);
  innerDivElement.appendChild(document.createElement("br"));
  innerDivElement.appendChild(document.createElement("br"));

  //add item vendor label and input nested in div
  labelElement = addLabel("Vendor: ");
  inputElement = addInput('text', ('vendor_' + counter));
  innerDivElement.appendChild(labelElement);
  labelElement.appendChild(inputElement);
  innerDivElement.appendChild(document.createElement("br"));
  innerDivElement.appendChild(document.createElement("br"));

  //add second div wrapper for bootstrap col name and nest in div
  secondInnerDivElement.setAttribute('class', 'col-md');
  divElement.appendChild(secondInnerDivElement);
  //add quantity label and input nested in div
  labelElement = addLabel("Quantity: ");
  inputElement = addInput('number', ('quantity_' + counter));
  inputElement.setAttribute('placeholder', "1");
  inputElement.setAttribute('min', "1");
  inputElement.setAttribute('step', "1");
  secondInnerDivElement.appendChild(labelElement);
  labelElement.appendChild(inputElement);
  secondInnerDivElement.appendChild(document.createElement("br"));
  secondInnerDivElement.appendChild(document.createElement("br"));

  //add cost label and input nested in div
  labelElement = addLabel("Cost Per Item: ");
  inputElement = addInput('number', ('cost_' + counter));
  inputElement.setAttribute('placeholder', "0.00");
  inputElement.setAttribute('min', "1");
  inputElement.setAttribute('step', "any");
  secondInnerDivElement.appendChild(labelElement);
  labelElement.appendChild(inputElement);
  secondInnerDivElement.appendChild(document.createElement("br"));
  secondInnerDivElement.appendChild(document.createElement("br"));

  labelElement = addLabel("Web Link (if applicable): ");
  inputElement = addInput('text', ('weblink_' + counter));
  secondInnerDivElement.appendChild(labelElement);
  labelElement.appendChild(inputElement);
  secondInnerDivElement.appendChild(document.createElement("br"));
  secondInnerDivElement.appendChild(document.createElement("br"));

  counter ++;

}

function addLabel(labelText) {
  let labelElement = document.createElement("label");
  labelElement.innerHTML = labelText;
  return labelElement;
}

function addInput(inputType, inputName) {
  var newInput = document.createElement('input');
  newInput.setAttribute('type', inputType);
  newInput.setAttribute('name', inputName);
  return newInput;
}

function deleteThisItemSet(source) {
  let node = source.parentNode;
  while (node.hasChildNodes()) {
    node.removeChild(node.lastChild);
  }
}
