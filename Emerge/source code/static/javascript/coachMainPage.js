var setAction = document.getElementById('view-action-button');

var setActionPage = document.getElementsByClassName('addAction');
var addAction_cover = document.getElementsByClassName('addAction-cover');

function popupSetActionPage() {
  setActionPage[0].style.display = "block";
  addAction_cover[0].style.display = "block";
}

function cancel_popup(){
  setActionPage[0].style.display = "none";
  addAction_cover[0].style.display = "none";
}
