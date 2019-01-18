function parkIdentifyCallback(param, res) {
    document.getElementById("result").innerText = res
}

function parkCutCallback(param, res) {
    img = document.getElementById("viewCut");
    var projectId = document.getElementById("projectId").innerText;
    img.src = "data:image/jpeg;base64," + res
    var cutPicture
    if (res.length > 100) {
        cutPicture = {"imageBase64":res, "projectId": projectId}
        document.getElementById("viewCut").style = ""
    } else {
        img = document.getElementById("viewCheck");
        image64 = img.src.split(",", 2)
        document.getElementById("viewCut").style = "display: none"
        cutPicture = {"imageBase64": image64[1], "projectId": projectId}
    }
    httpRequest("/project/identify", "POST", JSON.stringify(cutPicture), null, parkIdentifyCallback)
}

function  reg() {
    var inputBox = document.getElementById("checkFile");
    var projectId = document.getElementById("projectId").innerText;
    inputBox.addEventListener("change", function () {
        var reader = new FileReader();
        reader.readAsDataURL(inputBox.files[0]);//发起异步请求
        reader.onload = function () {
            img = document.getElementById("viewCheck");
            img.src = this.result
            image64=this.result.split(",", 2)
            var cutPicture = {"imageBase64": image64[1], "projectId": projectId}
            httpRequest("/image/cut", "POST", JSON.stringify(cutPicture), null, parkCutCallback)
        }
    })
}

function uploadCallback(param, res) {
    document.getElementById("result").innerHTML = res
}

function onUpload(dc) {
    var projectId = dc.getElementById("projectId").innerText;
    var categorySelect = document.getElementById("categoryId").value;
    if (categorySelect.length == 0)
        return
    var result = document.getElementById("result").innerText
    if (result.length < 1 || result.toString().indexOf("UNKNOWN") != -1) {
        document.getElementById("result").innerText = "图片校验结果不合法"
        return
    }
    cutImg = document.getElementById("viewCut")
    if (cutImg.src.length < 100) {
        img = document.getElementById("viewCheck");
    } else {
        img = cutImg
    }
    image64 = img.src.split(",", 2)
    if (image64 == '')
        return
    var up = {"imageBase64": image64[1], "projectId": projectId, "category": categorySelect}
    httpRequest("/project/uploadData", "POST", JSON.stringify(up), document, uploadCallback)
}

function  onUploadFile() {

}