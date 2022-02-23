var no_goal = document.getElementsByClassName("no-goal");
var new_goal = document.getElementsByClassName("new-goal");
var path = window.location.pathname;

function toAdd(i){
  no_goal[i].style.display = "none";
  new_goal[i].style.display = "block";
}

function added(i){
  no_goal[i].style.display = "none";
  new_goal[i].style.display = "none";
}


function changeSection(){
  var sessions_section = document.getElementsByClassName("sessions")[0];
  var goals_section = document.getElementsByClassName("goals")[0];
  var explore_section = document.getElementsByClassName("explore")[0];

  if (path == "/main"){
    sessions_section.style.display = "block";
    goals_section.style.display = "none";
    explore_section.style.display = "none";
  }
  else if (path == "/main/goals"){
    sessions_section.style.display = "none";
    goals_section.style.display = "block";
    explore_section.style.display = "none";
  }
  else if (path == "/main/explore"){
    sessions_section.style.display = "none";
    goals_section.style.display = "none";
    explore_section.style.display = "block";
  }
  else{
    sessions_section.style.display = "none";
    goals_section.style.display = "none";
    explore_section.style.display = "none";
  }
}
