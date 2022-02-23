var path = window.location.pathname;

function changeSection(){
    var dashboard = document.getElementsByClassName("Dashboard")[0];
    var coach = document.getElementsByClassName("Coach")[0];
    var client = document.getElementsByClassName("Client")[0];
    var addNewCoach = document.getElementsByClassName("addNewCoach")[0];
    var addNewClient = document.getElementsByClassName("addNewClient")[0];
    var match = document.getElementsByClassName("match")[0];
    var feedback = document.getElementsByClassName("feedback")[0];

  
    if (path == "/admin"){
      dashboard.style.display = "block";
      coach.style.display = "none";
      client.style.display = "none";
      addNewCoach.style.display = "none";
      addNewClient.style.display = "none";
      match.style.display = "none";
      feedback.style.display = "none";


    }
    else if (path == "/admin/Coach"){
      dashboard.style.display = "none";
      coach.style.display = "block";
      client.style.display = "none";
      addNewCoach.style.display = "none";
      addNewClient.style.display = "none";
      match.style.display = "none";
      feedback.style.display = "none";

    }
    else if (path == "/admin/Client"){
      dashboard.style.display = "none";
      coach.style.display = "none";
      client.style.display = "block";
      addNewCoach.style.display = "none";
      addNewClient.style.display = "none";
      match.style.display = "none";
      feedback.style.display = "none";

    }
    else if (path == "/admin/addNewCoach"){
      dashboard.style.display = "none";
      coach.style.display = "none";
      client.style.display = "none";
      addNewCoach.style.display = "flex";
      addNewClient.style.display = "none";
      match.style.display = "none";
      feedback.style.display = "none";

    }

    else if (path == "/admin/addNewClient"){
        dashboard.style.display = "none";
        coach.style.display = "none";
        client.style.display = "none";
        addNewCoach.style.display = "none";
        addNewClient.style.display = "block";
        match.style.display = "none";
        feedback.style.display = "none";

    }

    else if (path  == "/admin/match"){
        dashboard.style.display = "none";
        coach.style.display = "none";
        client.style.display = "none";
        addNewCoach.style.display = "none";
        addNewClient.style.display = "none";
        match.style.display = "block";
        feedback.style.display = "none";
        
    }

    else if (path == "/admin/Feedback"){
        dashboard.style.display = "none";
        coach.style.display = "none";
        client.style.display = "none";
        addNewCoach.style.display = "none";
        addNewClient.style.display = "none";
        match.style.display = "none";
        feedback.style.display = "block";
    }

    else{
        dashboard.style.display = "none";
        coach.style.display = "none";
        client.style.display = "none";
        addNewCoach.style.display = "none";
        addNewClient.style.display = "none";
        match.style.display = "none";
        feedback.style.display = "none";

    }
  }