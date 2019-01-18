function  reloadViewData() {
    loadPics(document)
}
sizePerPage = 4
function loadPicsCallback(params, res) {
    var picItem = JSON.parse(res)
    for (var i = 0; i < picItem.length; i++) {
        var myImage = params.getElementById("l" + (i + 1));
        myImage.src = picItem[i]["imageBase64"]
        myImage.alt = picItem[i]["path"]
    }
}

function deleteCallback(param, res) {
    loadPics(document)
}

function deleteData(el) {
    var myImage = document.getElementById(el);
    httpRequest("/project/deleteData", "POST", myImage.alt, null, deleteCallback)
}

function prePage() {
    var page = document.getElementById("page").value;
    if (page < 2)
        return
    document.getElementById("page").value = parseInt(document.getElementById("page").value) - 1;
    loadPics(document)
}

function nextPage() {
    document.getElementById("page").value = parseInt(document.getElementById("page").value) + 1;
    loadPics(document)
}

function  loadPics(dc) {
    var categorySelect = dc.getElementById("categoryId").value;
    if (categorySelect.length == 0)
        return
    var projectId = dc.getElementById("projectId").innerText;
    var page = document.getElementById("page").value
    var picView = {"projectId": projectId, "page": page, "size": sizePerPage, "category": categorySelect}
    httpRequest("/project/newData/retrieveFile", "POST", JSON.stringify(picView),  dc, loadPicsCallback)
}