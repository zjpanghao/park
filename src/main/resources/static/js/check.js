
function get_picture(dc) {
    dc.getElementById("help").style = "display: none"
    var id = dc.getElementById("modelId").innerText
    httpRequest("/project/randRetrieveFile/" + id, "POST", null, dc, getPicturePathCallback)
}

function getPictureCallback(dc, res) {
    dc.getElementById("sample").src = res
}

function getPicturePathCallback(dc, res) {
    if (res == null || res.length == 0)
        return
    var identifyItem = JSON.parse(res);
    dc.getElementById("picturePath").innerHTML = identifyItem["path"]
    dc.getElementById("result").innerText = identifyItem["category"]
    dc.getElementById("score").innerText = identifyItem["score"]
    fname = identifyItem["path"]
    if (fname == null || fname == '') {
        return
    }

    dc.getElementById("file").innerHTML =window.atob(fname)
    var fd = new FormData();
    fd.append("path", fname);
    httpRequest("/project/image", "POST", fd, dc, getPictureCallback)
}

function category( ) {
    var md = document.getElementById("modelId").innerText
    var category =    document.getElementById("result").innerText
    var path = document.getElementById("picturePath").innerText
    var data = {"projectId":md, "category":category, "path":path}
    window.open('/park-1.0/project/category/' + md + "_" + category, '', height=200, width=200, top=0, left=0);
}

function correctAiCallback(param, res) {
    console.log(typeof res)
    var dc = window.opener.document
    if (res == "true") {
        flushPicture(dc)
    } else {
        dc.getElementById("correctResult").innerHTML = "纠正失败"
        return
    }
    window.open("","_self").close()
}

function getTotalCallback(param, res) {
    param.getElementById("total").innerText = res
}

function  getTotal(dc) {
   httpRequest("/project/filter/count/" + dc.getElementById("modelId").innerText, "POST", null, dc,getTotalCallback)
}

function  flushPicture(dc) {
    dc.getElementById("correctResult").innerHTML = ""
    getTotal(dc)
    if (dc.getElementById("total").innerText == "0")
        return
    var modelId = dc.getElementById("modelId").innerText
    var category = ""
    var path = dc.getElementById("picturePath").innerText
    var data = {"projectId":modelId, "category":category, "path":path}
    if (path.length > 0) {
        httpRequest("/project/correctAi", "POST", JSON.stringify(data), null, null)
    }
    get_picture(dc)
}

function correctAi() {
    var categorySelect = document.getElementById("categoryId").value;
    if (categorySelect == "") {
        return
    }
    var modelId = window.opener.document.getElementById("modelId").innerText
    var category =    window.opener.document.getElementById("result").innerText
    var path = window.opener.document.getElementById("picturePath").innerText
    if (category == categorySelect) {
        return
    }
    var data = {"projectId":modelId, "category":categorySelect, "path":path}
    httpRequest("/project/correctAi", "POST", JSON.stringify(data), null, correctAiCallback)
}