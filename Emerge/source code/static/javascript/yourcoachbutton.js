

function pressed_chat() {
  document.querySelector(".chat").style.display="flex";
  document.querySelector(".gap").style.opacity="0.2";
  document.getElementById("background").style.zIndex="-1";
  var out = document.getElementById("chat-background");
  out.scrollTop = out.scrollHeight - out.clientHeight;


  document.querySelector(".closebtn_chat").addEventListener("click",
  function(){
    document.querySelector(".chat").style.display="none";
    document.querySelector(".gap").style.opacity="1";
    document.getElementById("background").style.zIndex="1";
  })




}


function pressed_zoom() {

  document.querySelector(".zoom").style.display="flex";
  document.querySelector(".gap").style.opacity="0.2";
  document.getElementById("background").style.zIndex="-1";

  document.querySelector(".closebtn_zoom").addEventListener("click",
  function(){
    document.querySelector(".zoom").style.display="none";
    document.querySelector(".gap").style.opacity="1";
    document.getElementById("background").style.zIndex="1";
  })
}



function pressed_feedback() {

  document.querySelector(".feedbk1").style.display="flex";
  document.querySelector(".gap").style.opacity="0.2";
  document.getElementById("background").style.zIndex="-1";


  document.querySelector(".closebtn_fb1").addEventListener("click",
  function(){
    document.querySelector(".feedbk1").style.display="none";
    document.querySelector(".gap").style.opacity="1";
    document.getElementById("background").style.zIndex="1";
  })
}

function pressed_feedback1() {

  document.querySelector(".feedbk2").style.display="flex";
  document.querySelector(".gap").style.opacity="0.2";
  document.getElementById("background").style.zIndex="-1";

  document.querySelector(".closebtn_fb2").addEventListener("click",
  function(){
    document.querySelector(".feedbk2").style.display="none";
    document.querySelector(".gap").style.opacity="1";
    document.getElementById("background").style.zIndex="1";
  })
}



function pressed_pick() {
  var pp = document.getElementById("pick");
  pp.classList.toggle("pop_pick");
  // setTimeout("hide_pick()",1000);
  // clearTimeout(int);
}
