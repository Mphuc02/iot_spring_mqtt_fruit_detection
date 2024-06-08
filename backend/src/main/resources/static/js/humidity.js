function getHumidity(newHumidity){
    var humidityResult = document.querySelector("#doam-result")
    humidityResult.innerHTML = newHumidity + "%"

    //Thay đổi background
    var humidityBackground = document.querySelector("#humidity-background")
    var classes = "humidity"
    if(newHumidity >= 70)
        classes += " humidity-70"
    else if(newHumidity >= 50)
        classes += " humidity-50"
    else if(newHumidity >= 30)
        classes += " humidity-30"
    else
        classes += " humidity-0"
    humidityBackground.setAttribute("class", classes)
}