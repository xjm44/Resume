var date = new Date();
const today = new Date();
var type = "";

if (document.getElementsByTagName("title")[0].innerHTML === "Your Coach"){
    type = "leader";
}else if(document.getElementsByTagName("title")[0].innerHTML === "CoachMainPage"){
    type = "coach";
}

    const months = [
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
    ];

    const days = [
        "Sun",
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
    ]

    const fulldays = [
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
    ]
let notdefault = document.querySelector(".not_default").getAttribute("value");
if (notdefault !== ' '){
    date = new Date(notdefault);

}
document.querySelector(".event h2").innerHTML = days[date.getDay()]+ ", " + date.getDate() + " " + months[date.getMonth()] + " " + date.getFullYear();
gen_calendar();

if (notdefault !== ' '){
    dateselected("d"+notdefault.split("-")[2]);
}else{
    getEvent();
}

function last(){
    date.setDate(1);
    date.setMonth(date.getMonth()-1);
    gen_calendar();
}

function next(){
    date.setDate(1);
    date.setMonth(date.getMonth()+1);
    gen_calendar();
}

function dateselected(id){
    date.setDate(id.slice(1,id.length));
    const old = document.querySelector(".days .selected");
    if (old !== null){
        old.classList.remove("selected");
    }
    document.getElementById(id).classList.add("selected");
    document.querySelector(".event h2").innerHTML = days[date.getDay()]+ ", " + date.getDate() + " " + months[date.getMonth()] + " " + date.getFullYear();
    getEvent();
}

function control_time_selection(){
    var start = document.getElementById('start');
    var start_vel= parseInt(start.options[start.selectedIndex].text.slice(0,2));
    var end = document.getElementById('end');
    var end_vel= parseInt(end.options[end.selectedIndex].text.slice(0,2));
    if (start_vel >= end_vel){
        window.alert("The end time must be after the begin time");
    }
}

function getEvent(){
    let ctime = document.querySelector(".coach_time").getAttribute("value")
    var formatDate = (date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate());
    let times = ``;
    if (ctime.length != 2){
        let coach_time = ctime.slice(2,-2).split("), (");
        for (let i = 0; i < coach_time.length; i++){
            let split = coach_time[i].split(', ');
            if (split[2].slice(1,-1) === formatDate){
                if (split[3] === '0'){
                    if (type === "leader"){
                        times += `<div class="timecard"><p>${split[0].slice(1,-1)}-${split[1].slice(1,-1)}</p><form method='post'><input type="hidden" name="form-name" value = "pick_time"></input><button type = 'submit' class="pickbutton" name='pick_button' value = "${coach_time[i]}">pick</button></form></div>`;
                    } else if (type === "coach"){
                        times += `<div class="timecard"><p>${split[0].slice(1,-1)}-${split[1].slice(1,-1)}</p><button disabled class="notpickbutton" >not picked</button></div>`;
                    }
                }else if (split[3] === '1'){
                    if (type === "leader"){
                        times += `<div class="timecard"><p>${split[0].slice(1,-1)}-${split[1].slice(1,-1)}</p><button disabled class="pickedbutton">picked</button></div>`;
                    } else if (type === "coach"){
                        times += `<div class="timecard"><p>${split[0].slice(1,-1)}-${split[1].slice(1,-1)}</p><button disabled class="pickedbutton" style="height:37px; margin-top:6.5px;">${split[5].slice(1,-1)} picked</button></div>`;
                    }
                }
            }
        }
    }
    if (type === "coach"){
        const monthdays = new Date(date.getFullYear(),date.getMonth()+1, 0).getDate();
        times += `<div class="add_time timecard">
                    <form method = 'post' style="margin-left:5%">
                      <input type="hidden" name="form-name" value = "add_time"></input>
                      <input type="hidden" name="weekday" value="${date.getDay()}"></input>
                      <input type="hidden" name="numdays" value="${monthdays}"></input>
                      <select name="start" id="start">
                        <option value="06:00">06:00</option>
                        <option value="07:00">07:00</option>
                        <option value="08:00">08:00</option>
                        <option value="09:00">09:00</option>
                        <option value="10:00">10:00</option>
                        <option value="11:00">11:00</option>
                        <option value="12:00">12:00</option>
                        <option value="13:00">13:00</option>
                        <option value="14:00">14:00</option>
                        <option value="15:00">15:00</option>
                        <option value="16:00">16:00</option>
                        <option value="17:00">17:00</option>
                        <option value="18:00">18:00</option>
                        <option value="19:00">19:00</option>
                        <option value="20:00">20:00</option>
                        <option value="21:00">21:00</option>
                        <option value="22:00">22:00</option>
                      </select>
                      <li style="display: inline-block"> - </li>
                      <select name="end" id="end">
                        <option value="06:00">06:00</option>
                        <option value="07:00">07:00</option>
                        <option value="08:00">08:00</option>
                        <option value="09:00">09:00</option>
                        <option value="10:00">10:00</option>
                        <option value="11:00">11:00</option>
                        <option value="12:00">12:00</option>
                        <option value="13:00">13:00</option>
                        <option value="14:00">14:00</option>
                        <option value="15:00">15:00</option>
                        <option value="16:00">16:00</option>
                        <option value="17:00">17:00</option>
                        <option value="18:00">18:00</option>
                        <option value="19:00">19:00</option>
                        <option value="20:00">20:00</option>
                        <option value="21:00">21:00</option>
                        <option value="22:00">22:00</option>
                      </select>
                      <button onclick = "control_time_selection()" class = "notpickbutton" name="addbutton" value="${formatDate}" style="margin-left:29%; width:80px;">Add time</button>
                      <div></div>
                      <input type="checkbox" id="check1" name="check1" value="check1" style="margin-top:10px;">
                      <label for="check1" style = "font-size:14px;"> apply the time for all days in this week </label>
                      <div></div>
                      <input type="checkbox" id="check2" name="check2" value="check2">
                      <label for="check2" style = "font-size:14px;"> apply the time for all ${fulldays[date.getDay()]} in this month </label>
                    </form>
                  </div>`;
    }
    document.querySelector(".cards").innerHTML = times;

}

function gen_calendar(){
    let coach_time = document.querySelector(".coach_time").getAttribute("value").slice(2,-2).split("), (");
    var formatDate = (date.getDate() + "-" + (date.getMonth() + 1) + "-" + date.getFullYear());

    document.querySelector(".date").innerHTML = `<h2>${months[date.getMonth()]}</h2>`;
    document.querySelector(".date").innerHTML += `<p>${days[today.getDay()]+ ", " + today.getDate() + " " + months[today.getMonth()] + " " + today.getFullYear()}</p>`;

    const monthdays = new Date(date.getFullYear(),date.getMonth()+1, 0).getDate();
    const thisweekdays = new Date(date.getFullYear(),date.getMonth(), 1).getDay();
    let lastdays = 0;
    if (thisweekdays === 0){
        lastdays = 6;
    }else{
        lastdays = thisweekdays-1;
    }
    const nextdays = 42-monthdays-lastdays

    let day = ``;

    for (let i = lastdays; i > 0; i--) {
        day += `<p class="notthismonth"> ${31-i+1} </p>`;
    };

    for (let i = 1; i < monthdays+1; i++){
        if (i === today.getDate() && date.getMonth() === today.getMonth() && date.getFullYear() === today.getFullYear() ){
            if (date.getDate() === today.getDate()){
                day += `<p id="d${i}" onclick="dateselected(this.id)" class="day today selected">${i}</p>`;
            }else{
                day += `<p id="d${i}" onclick="dateselected(this.id)" class="day today">${i}</p>`;
            }
        } else {
            day += `<p id="d${i}" onclick="dateselected(this.id)" class="day">${i}</p>`;
        }

    };

    for (let i = 1; i < nextdays+1 ; i++) {
        day += `<p class="notthismonth"> ${i} </p>`;
    };

    document.querySelector(".days").innerHTML=day;
};
