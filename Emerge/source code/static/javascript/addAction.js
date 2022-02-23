var add_button_area = document.getElementsByClassName('add-button-area');
var addNew_content = document.getElementsByClassName('addNew_content');

function newAction() {
  add_button_area[0].style.display = "none";
  addNew_content[0].style.display = "block";
}

function newActionAdded(){
  add_button_area[0].style.display = "block";
  addNew_content[0].style.display = "none";
}
