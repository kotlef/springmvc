/**
 * 模块说明
 * @module 房屋信息模块-初始化房屋界面上传控件
 */
$(function(){
	/**
	 * 上传户型图
	 */
	$('#queue_house').hide();
	var lastId = null, multi = false, uploadId = "btn_houseModel", queueID_houseModel = "queue_house";
    function getQueues() {
        var data = $("#" + uploadId).data("uploadify"), ret = [],
            files = (data && data.queueData && data.queueData.files) ? data.queueData.files : [];
        for (var i in files) {
            ret.push(files[i]);
        }
        return ret;
    };
    function getFileName(file) {
        if (!file || !file.name) { return "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)"; }
        return $.string.getByteLen(file.name) > 28 ? $.string.leftBytes(file.name, 25) + "..." : file.name;
    };
    $("#" + uploadId).uploadify({
    	swf:Swet.server.cloud+'/jeasyui/uploadify/uploadify.swf',
        uploader: Swet.server.cloud+"/files/upload/image?type=house",
        fileSizeLimit: "1MB",
        successTimeout: 3600,
        //formData: { folder: "E:/uploads" },
        fileObjName:'fileData',
        fileTypeDesc:'图片文件',
        fileTypeExts:'*.jpg;*.png;',
        buttonText:"选择文件",
        removeTimeout: 0,
        width: 76, height: 24,
        auto: false,
        multi: multi,
        onSelect: function (file) {
            if (lastId && !multi) {
                $("#" + uploadId).uploadify("cancel", lastId);
            }
            if (file) {
                $("#p_houseModel").progressbar("setText", getFileName(file) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success");
                lastId = file.id;
            } else {
                $("#p_houseModel").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success");
            }
        },
        queueID: queueID_houseModel,
        onCancel: function (file) {
            var queues = getQueues();
            if (!queues.length) {
                return $("#p_houseModel").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0);
            }
            var temps = $.array.clone(queues);
            var array = $.array.remove(temps, file, function (a, b) {
                return a.id == b.id;
            });
            if (array.length) {
                return $("#p_houseModel").progressbar("setText", getFileName(array[0]) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0);
            } else {
                return $("#p_houseModel").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0);
            }
        },
        onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
            var val = !bytesTotal ? 0 : (bytesUploaded / bytesTotal * 100).round(2);
            $("#p_houseModel").progressbar("setValue", val).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error");
            $("#p_houseModel").progressbar("setText", getFileName(file) + "(" + $.number.toFileSize(file.size) + ") - " + val + "%");
        },
        onUploadSuccess: function (file, data, response) {
            $("#p_houseModel").progressbar("setText", getFileName(file) + "(上传完成!)").find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-success");
            var res=JSON.parse(data);
            if(res.success){
                var path=res.rows;
                var fileNameArray = path.split("/");
                var name = fileNameArray[fileNameArray.length - 1];
                var names=name.split(".");
                var houseMode={docUrl:path,docType:'P',docClass:"P",note:"户型图"};
                Swet.request.save(Swet.server.mainArchive+"/document",houseMode,function(res){
                    $("#business_houseModelDid").val(res.rows.did);
                    $("#img_up_houseModel").attr('src',path);
                });
            }
        },
        onUploadError: function (file, errorCode, errorMsg, errorString) {
            $("#p_houseModel").progressbar("setText", getFileName(file) + "(上传失败:" + errorString + ")").find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-error");
        },
        onUploadComplete: function (file) {
            //if (errorString == "Cancelled" || errorString == "Stopped") { return; }
            $("#p_houseModel").progressbar("setValue", 100);
        }
    });
    //开始上传户型图按钮
    $("#btn_up_houseModel").click(function () {
        $("#" + uploadId).uploadify("upload");
    });
    
    /**
     * 上传设计图
     */
	var lastId1 = null, multi = false, uploadId1 = "btn_houseDesign", queueID_houseDesign = "queue_house";
    function getQueues1() {
        var data = $("#" + uploadId1).data("uploadify"), ret = [],
            files = (data && data.queueData && data.queueData.files) ? data.queueData.files : [];
        for (var i in files) {
            ret.push(files[i]);
        }
        return ret;
    };
    function getFileName(file) {
        if (!file || !file.name) { return "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)"; }
        return $.string.getByteLen(file.name) > 28 ? $.string.leftBytes(file.name, 25) + "..." : file.name;
    };
    $("#" + uploadId1).uploadify({
    	swf:Swet.server.cloud+'/jeasyui/uploadify/uploadify.swf',
        uploader: Swet.server.cloud+"/files/upload/image?type=house",
        fileSizeLimit: "1MB",
        successTimeout: 3600,
        multi: false,
        //formData: { folder: "E:/uploads" },
        fileObjName:'fileData',
        fileTypeDesc:'图片文件',
        fileTypeExts:'*.jpg;*.png;',
        buttonText:"选择文件",
        removeTimeout: 0,
        width: 76, height: 24,
        auto: false,
        multi: multi,
        onSelect: function (file) {
            if (lastId1 && !multi) {
                $("#" + uploadId1).uploadify("cancel", lastId1);
            }
            if (file) {
                $("#p_houseDesign").progressbar("setText", getFileName(file) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success");
                lastId1 = file.id;
            } else {
                $("#p_houseDesign").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success");
            }
        },
        queueID: queueID_houseDesign,
        onCancel: function (file) {
            var queues1 = getQueues1();
            if (!queues1.length) {
                return $("#p_houseDesign").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0);
            }
            var temps = $.array.clone(queues1);
            var array = $.array.remove(temps, file, function (a, b) {
                return a.id == b.id;
            });
            if (array.length) {
                return $("#p_houseDesign").progressbar("setText", getFileName(array[0]) + "(" + $.number.toFileSize(file.size) + ") - 0%").progressbar("setValue", 0);
            } else {
                return $("#p_houseDesign").progressbar("setText", "未选择文件(文件类型:*.jpg/*.png;文件大小:小于等于2M)").progressbar("setValue", 0);
            }
        },
        onUploadProgress: function (file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {
            var val = !bytesTotal ? 0 : (bytesUploaded / bytesTotal * 100).round(2);
            $("#p_houseDesign").progressbar("setValue", val).find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error");
            $("#p_houseDesign").progressbar("setText", getFileName(file) + "(" + $.number.toFileSize(file.size) + ") - " + val + "%");
        },
        onUploadSuccess: function (file, data, response) {
            $("#p_houseDesign").progressbar("setText", getFileName(file) + "(上传完成!)").find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-success");
            var res=JSON.parse(data);
            if(res.success){
                var path=res.rows;
                var fileNameArray = path.split("/");
                var name = fileNameArray[fileNameArray.length - 1];
                var names=name.split(".");
                var houseDesign={docUrl:path,docType:'P',docClass:"P",note:"设计图"};
                Swet.request.save(Swet.server.mainArchive+"/document",houseDesign,function(res){
                    $("#business_houseDesignDid").val(res.rows.did);
                    $("#img_up_houseDesign").attr('src',path);
                });
            }
        },
        onUploadError: function (file, errorCode, errorMsg, errorString) {
            $("#p_houseDesign").progressbar("setText", getFileName(file) + "(上传失败:" + errorString + ")").find(".progressbar-value .progressbar-text").removeClass("progressbar-text-success progressbar-text-error").addClass("progressbar-text-error");
        },
        onUploadComplete: function (file) {
            //if (errorString == "Cancelled" || errorString == "Stopped") { return; }
            $("#p_houseDesign").progressbar("setValue", 100);
        }
    });
    //开始上传设计图按钮
    $("#btn_up_houseDesign").click(function () {
        $("#" + uploadId1).uploadify("upload");
    });
});