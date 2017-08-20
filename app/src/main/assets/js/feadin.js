

function fadein(ele, opacity, speed) { 
if (ele) { 
var v = ele.style.filter.replace("alpha(opacity=", "").replace(")", "") || ele.style.opacity; 
v < 1 && (v = v * 100); 
var count = speed / 1000; 
var avg = count < 2 ? (opacity / count) : (opacity / count - 1); 
var timer = null; 
timer = setInterval(function() { 
    if (v < opacity) {
    v += avg;
    setOpacity(ele, v);
    } else {
    clearInterval(timer);
    }
}, 20);
} 
} 

window.onload = function () { 
setOpacity(document.getElementById('body'), 0);
fadein(document.getElementById('body'), 100, 5000);
//alert("From js:" );

}

function setOpacity(ele, opacity) {
if (ele.style.opacity != undefined) {
///兼容FF和GG和新版本IE
ele.style.opacity = opacity / 100;

} else {
///兼容老版本ie
ele.style.filter = "alpha(opacity=" + opacity + ")";
}
}





