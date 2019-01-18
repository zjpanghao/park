//var gurl = "http://localhost:8080/park-1.0"
var gurl = "http://180.164.59.226:8887/park-1.0"
function httpRequest(url, method, xhrParams, params, callback) {
    var xhr = new XMLHttpRequest()
    xhr.open(method, gurl + url, false)
    xhr.onload = function(e) {
        if(xhr.readyState == 4  && xhr.status == 200){
            callback(params, xhr.response)
        }
    };
    xhr.send(xhrParams)
}