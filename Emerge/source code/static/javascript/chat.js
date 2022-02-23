function getUsername(username) {
    let cookies = document.cookie.split("; ");
    for (let x = 0; x < cookies.length; x++) {
        let name = cookies[x].split("=")[0];
        if (name === username) {
            return cookies[x].split("=")[1];
        }
    }
    return ""
}

if ( window.history.replaceState ) {
  window.history.replaceState( null, null, window.location.href );
}
// --------------------------------------------------------------------------------------------
// load chat chat history
// TODO
// function load_history(){
//   // var out = document.getElementById("chat-background");
//   // out.scrollTop = out.scrollHeight ;
//
// }
var x = true;

function canPost()   //check all required fields
{       if(x){
  return false;
}else{
  console.log(x);
  return true;
}
}
// var db = window.openDatabase("test", "1.0", "Test DB", 1000000);


// --------------------------------------------------------------------------------------------
function chat(){

  var msg = document.getElementById("chat_msg").value;
  // alert(msg);
  // document.cookie = msg;
  canPost();
  // load(msg);
  var out = document.getElementById("chat-background");
  out.scrollTop = out.scrollHeight - out.clientHeight;;
  document.getElementById("chat_msg").value = "";

  // fetch("/yourcoach/${message}",{
  //   method: "POST",
  //         headers: {
  //           'Accept': 'application/json',
  //           'Content-Type': 'application/json'
  //         },
  //         body: JSON.stringify({
  //             msg: msg
  //             })
  //           })
  // .then(function(res){
  //   return res.json()
  // })
  // .then(function(data){
  //   // console.log(data)
  // })
}
// --------------------------------------------------------------------------------------------
// var x = 0;
function load(msg){
  var out = document.getElementById("chat-background");
  // let content = msg;
  // content += `
  // <article class="message_receiver" id="message_receiver">
  //   <div class="message_box">
  //     <div class="label">
  //         <p class="msg" id="msg_r">
  //           `+ x++ +`
  //         </p>
  //
  //       <span class="timestamp">
  //         <span class="sent_time">3 minute ago</span>
  //       </span>
  //     </div>
  //   </div>
  // </article>`;
  // content += `
  // <article class="message_sender" id="message_sender">
  //   <div class="message_box">
  //     <div class="label">
  //         <p class="msg" id="msg_s">
  //           `+msg+`
  //         </p>
  //       <span class="timestamp">
  //         <span class="sent_time"> {{sender_time}} </span>
  //       </span>
  //     </div>
  //   </div>
  // </article>`;
  //
  // out.innerHTML += content;
}




// --------------------------------------------------------------------------------------------
// when entered
document.querySelector(".aa").addEventListener("keydown", function (event) {
    if (event.keyCode === 13) {
        event.preventDefault();
        // get the msg
        const element = document.getElementById("chat_btn").click();
        // when pressed send
        // alert(msg);
        x=false;

    }
});
const element = document.getElementById("chat_btn");
// when pressed send
element.addEventListener("click", () => {
  // alert('hello');
  x=false;
  // chat();
});
// --------------------------------------------------------------------------------------------
// setInterval(function () { //load() //send msg -> db -> load
//   // var out = document.getElementById("chat-background");
//   // out.scrollTop = out.scrollHeight ;
// }, 500);
