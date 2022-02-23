var path = window.location.pathname;


function formSub(leader_num){
  // document.getElementsByClassName("leader_board")[0].style.display = "block";
  document.getElementById('choose_leader'+ leader_num).submit();
}

function changeSection(){
  var sessions_section = document.getElementsByClassName("sessions")[0];
  var leaders_section = document.getElementsByClassName("leaders")[0];
  var schedule_section = document.getElementsByClassName("schedule")[0];
  var header = document.getElementsByClassName("header")[0];

  if (path == "/coachMain"){
    sessions_section.style.display = "block";
    leaders_section.style.display = "none";
    header.style.display = "block";
  }
  else if (path == "/coachMain/leaders"){
    sessions_section.style.display = "none";
    leaders_section.style.display = "block";
    leaders_section.style.top = "40px";
    header.style.display = "none";
  }
  else if (path == "/coachMain/schedule"){
    sessions_section.style.display = "none";
    leaders_section.style.display = "none";
    schedule_section.style.display = "block";
    header.style.display = "none";
  }
  else{
    sessions_section.style.display = "none";
    leaders_section.style.display = "none";
  }
}

function pressed_chat() {
  document.querySelector(".chat").style.display="flex";
  document.querySelector(".main-section").style.opacity="0.2";
  document.querySelector(".main-section").style.zIndex="-1";
  var out = document.getElementById("chat-background");
  out.scrollTop = out.scrollHeight - out.clientHeight;


  document.querySelector(".closebtn_chat").addEventListener("click",
  function(){
    document.querySelector(".chat").style.display="none";
    document.querySelector(".main-section").style.opacity="1";
    document.querySelector(".main-section").style.zIndex="1";
  })

}
