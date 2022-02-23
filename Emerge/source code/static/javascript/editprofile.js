selected()

function selected(){

    var profile = document.querySelector(".profile").getAttribute("value").slice(1,-1).split(", ");
    var gender = profile[4].slice(1,-1);

    var gen = document.getElementById("gender");

    for (var i=0; i<gen.options.length; i++) {
        it = gen.options[i]

        if (it.value == gender){
            it.selected = true;
        }
    }

}