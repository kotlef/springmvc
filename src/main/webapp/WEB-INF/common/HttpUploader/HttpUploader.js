/*
	版权所有 2009-2015 荆门泽优软件有限公司
	保留所有权利
	官方网站：http://www.ncmem.com
	官方博客：http://www.cnblogs.com/xproer
	产品首页：http://www.ncmem.com/webplug/http-uploader2/
	在线演示：http://www.ncmem.com/products/http-uploader/demo2/index.html
	开发文档：http://www.cnblogs.com/xproer/archive/2011/03/15/1984950.html
	升级日志：http://www.cnblogs.com/xproer/archive/2011/03/15/1985091.html
	示例下载：/common/HttpUploader/asp.net/HttpUploader-demo.rar
	文档下载：/common/HttpUploader/HttpUploader-doc.rar
	联系邮箱：1085617561@qq.com
	联系QQ：1085617561
	更新记录：
		2009-11-05 创建
		2014-02-28 使用jquery优化代码并增强兼容性。
*/

//删除元素值
Array.prototype.remove = function(val)
{
	for (var i = 0, n = 0; i < this.length; i++)
	{
		if (this[i] != val)
		{
			this[n++] = this[i]
		}
	}
	this.length -= 1
}

//加截Chrome插件
function LoadChromeCtl(oid, mime, path)
{
    var acx = '<embed id="objHttpPartitionFF" type="' + mime + '" pluginspage="' + path + '" width="1" height="1"/>';
    if (oid.length < 1)
    {
        document.write(acx);
    }
    else {
        $("#" + oid).append(acx);
    }
}

function HttpUploaderMgr()
{
    var _this = this;
	//http://www.ncmem.com/
	this.Domain = "http://" + document.location.host;
	
	this.Config = {
		"EncodeType"		: "utf-8"//UTF-8,GB2312
		, "Version"			: "2,5,49,30849"
		, "Company"			: "荆门泽优软件有限公司"
		, "License"			: ""
		, "Debug"			: true//调试开关
		, "LogFile"			: "C:\\log.txt"//日志文件路径
		, "FileFilter"		: "*"//文件类型。所有类型：*。自定义类型：jpg,png,gif,bmp
		, "AllowMultiSelect": 1//多选开关。1:开启多选。2:关闭多选
		, "CryptoType"      : "md5"//文件校验方式：md5,crc,sha1
		, "InitDir"			: ""//初始路径。示例：D:\\Soft
		, "Compress"		: ""//是否开启压缩。值：zip
		, "AppPath"			: ""//网站虚拟目录名称。子文件夹 web
		, "FileFieldName"	: "fileData"//文件字段名称
		, "PostUrl"			: "/ynlxhealth/files/upload"
        //x86
		, "ClsidDroper"		: "4D2454F8-EB25-465f-B867-C2A3E9F3D4B4"
		, "ClsidUploader"	: "7AAE6FD3-C2F2-49d5-A790-1103848B3531"
		, "ClsidPartition"	: "6F3EB4AF-FC9C-4570-A686-88B4B427C6FE"
		, "CabPath"			: Swet.server.cloud+"/common/HttpUploader/HttpUploader.cab"
		//x64
		, "ClsidDroper64"	: "C9388115-887C-4d64-B175-F8F1AA5437BF"
		, "ClsidUploader64"	: "E95E03B2-8718-4871-B965-A9D21108DCD2"
		, "ClsidPartition64": "3AFFCB6D-55ED-4ada-A1EC-D7D87BA29E51"
		, "CabPath64"		: Swet.server.cloud+"/common/HttpUploader/HttpUploader64.cab"
		//Firefox
		, "MimeType"		: "application/npHttpUp2"
		, "XpiPath"	        : Swet.server.cloud+"/common/HttpUploader/HttpUploader2.xpi"
		//Chrome
		, "CrxName"			: "npHttpUp2"
		, "CrxType"		    : "application/npHttpUp2"
		, "CrxPath"	        : Swet.server.cloud+"/common/HttpUploader/HttpUploader2.crx"
		, "ExePath"			: Swet.server.cloud+"/common/HttpUploader/HttpUploader2.exe"
	};

	this.ActiveX = {
		  "Droper"      : "Xproer.HttpDroper2"
		, "Uploader"	: "Xproer.HttpUploader2"
		, "Partition"   : "Xproer.HttpPartition2"
		//64bit
		, "Droper64"	: "Xproer.HttpDroper2x64"
		, "Uploader64"	: "Xproer.HttpUploader2x64"
		, "Partition64"	: "Xproer.HttpPartition2x64"
	};
	
	//附加参数
	this.Fields = {
		 "uname": "test"
		,"upass": "123456"
	};

	this.FileFilter = new Array(); //文件过滤器
	this.UploaderListCount = 0; 	//上传项总数
	this.UploaderList = new Object(); //上传项列表
	this.UnUploaderIdList = new Array(); //未上传项ID列表
	this.UploadIdList = new Array(); //正在上传的ID列表
	this.CompleteList = new Array(); //已上传完的HttpUploader列表
	this.UploaderPool = new Array();
	this.UploaderPoolFF = new Array();
	this.Droper = null;

	var browserName = navigator.userAgent.toLowerCase();
	this.ie = browserName.indexOf("msie") > 0;
	//IE11
	this.ie = this.ie ? this.ie : browserName.search(/(msie\s|trident.*rv:)([\w.]+)/) != -1;
	this.firefox = browserName.indexOf("firefox") > 0;
	this.chrome = browserName.indexOf("chrome") > 0;
	
	this.CheckVersion = function()
	{
		//Win64
		if (window.navigator.platform == "Win64")
		{
			this.Config["CabPath"]			= this.Config["CabPath64"];
			this.Config["ClsidDroper"]		= this.Config["ClsidDroper64"];
			this.Config["ClsidUploader"]	= this.Config["ClsidUploader64"];
			this.Config["ClsidPartition"]	= this.Config["ClsidPartition64"];
			//
			this.ActiveX["Uploader"]		= this.ActiveX["Uploader64"];
			this.ActiveX["Partition"]		= this.ActiveX["Partition64"];
		}//Firefox
		else if (this.firefox)
		{
		    _this.Config["CabPath"] = _this.Config["XpiPath"];
		    _this.ActiveX["Uploader"] = _this.ActiveX["UploaderFF"];
		}
		else if (this.chrome)
		{
		    _this.Config["CabPath"] = _this.Config["CrxPath"];
		    _this.Config["MimeType"] = _this.Config["CrxType"];
		}
	};
	this.CheckVersion();
	
	//初始化路径
	this.InitPath = function()
	{
		this.Config["CabPath"] = this.Domain + this.Config["AppPath"] + this.Config["CabPath"];
		this.Config["PostUrl"] = this.Domain + this.Config["AppPath"] + this.Config["PostUrl"];
	};

	this.BrowserIE = {
	    plugin: null
        , Check: function ()
        {
            try
            {
                var com = new ActiveXObject(_this.ActiveX["Uploader"]);
                return true;
            }
            catch (e) { return false; }
        }
        , GetVersion: function ()
        {
            var obj = this.plugin;
            var v = "0";
            try
            {
                v = obj.Version;
            }
            catch (e) { }
            return v;
        }
        , NeedUpdate: function ()
        {
            return this.GetVersion() != _this.Config["Version"];
        }
        , GetFileSize: function (path)
        {
            return this.plugin.FileSize(path);
        }
        , GetFileLength: function (path)
        {
            return this.plugin.FileLength(path);
        }
        , Setup: function ()
        {
            //文件上传控件
            var acx = '<object id="objHttpUpLoader" classid="clsid:' + _this.Config["ClsidUploader"] + '"';
            acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';
            //文件夹选择控件
            acx += '<object id="objHttpPartition" classid="clsid:' + _this.Config["ClsidPartition"] + '"';
            acx += ' codebase="' + _this.Config["CabPath"] + '" width="1" height="1" ></object>';

            $("body").append(acx);
        }
        , OpenFileDialog: function ()
        {
            var obj = this.plugin;
            obj.FileFilter = _this.Config["FileFilter"];
            obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
            obj.InitDir = _this.Config["InitDir"];
            if (!obj.ShowDialog()) return;

            var list = obj.GetSelectedFiles();
            if (list == null) return;
            if (list.lbound(1) == null) return;

            for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
            {
                if (!_this.Exist(list.getItem(index)))
                {
                    _this.AddFile(list.getItem(index));
                }
            }
            _this.PostFirst();
        }
        , OpenFolderDialog: function ()
        {
            var obj = this.plugin;
            if (!obj.ShowFolder()) return;

            var list = obj.GetSelectedFiles();
            if (list == null) return;
            if (list.lbound(1) == null) return;

            for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
            {
                if (!_this.Exist(list.getItem(index)))
                {
                    _this.AddFile(list.getItem(index));
                }
            }
            _this.PostFirst();
        }
        , PasteFiles: function ()
        {
            var obj = this.plugin
            obj.FileFilter = _this.Config["FileFilter"];

            var list = obj.GetClipboardFiles();
            if (list == null) return;
            if (list.lbound(1) == null) return;

            for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
            {
                if (!_this.Exist(list.getItem(index)))
                {
                    _this.AddFile(list.getItem(index));
                }
            }
            _this.PostFirst();
        }
        , GetFiles: function ()
        {
            var obj = this.plugin;
            var list = obj.GetFiles(path, hasChild);
            if (list == null) return;
            if (list.lbound(1) == null) return;

            for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
            {
                if (!this.Exist(list.getItem(index)))
                {
                    _this.AddFile(list.getItem(index));
                }
            }
            _this.PostFirst();
        }
        , GetMacs: function ()
        {
            var obj = this.plugin;
            var list = obj.GetMacs();
            if (list == null) return null;
            if (list.lbound(1) == null) return null;
            var arr = new Array();

            for (var index = list.lbound(1) ; index <= list.ubound(1) ; index++)
            {
                arr.push(list.getItem(index));
            }
            return arr;
        }
        , Init: function ()
        {
            //this.plugin = document.getElementById("objHttpPartition");
            this.plugin = _this.partition;
        }
	};
	this.BrowserFF = {
	    plugin: null
        , Check: function ()
        {
            var mimetype = navigator.mimeTypes;
            if (typeof mimetype == "object" && mimetype.length)
            {
                for (var i = 0; i < mimetype.length; i++)
                {
                    if (mimetype[i].type == _this.Config["MimeType"].toLowerCase())
                    {
                        return mimetype[i].enabledPlugin;
                    }
                }
            }
            else
            {
                mimetype = [_this.Config["MimeType"]];
            }
            if (mimetype)
            {
                return mimetype.enabledPlugin;
            }
            return false;
        }
        , GetVersion: function ()
        {
            var obj = this.plugin;
            var v = "0";
            try
            {
                v = obj.GetVersion();
            }
            catch (e) { }
            return v;
        }
        , NeedUpdate: function ()
        {
            return this.GetVersion() != _this.Config["Version"];
        }
        , GetFileSize: function (path)
        {
            return this.plugin.FileSize(path);
        }
        , GetFileLength: function (path)
        {
            return this.plugin.FileLength(path);
        }
        , Setup: function ()
        {
            var xpi = new Object();
            xpi["Calendar"] = _this.Config["XpiPath"];
            InstallTrigger.install(xpi, function (name, result) { });
        }
        , OpenFileDialog: function ()
        {
            var obj = this.plugin;
            obj.FileFilter = _this.Config["FileFilter"];
            obj.FilesLimit = _this.Config["FilesLimit"];
            obj.AllowMultiSelect = _this.Config["AllowMultiSelect"];
            obj.InitDir = _this.Config["InitDir"];

            var files = obj.ShowDialog();
            if (files)
            {
                for (var i = 0, l = files.length; i < l; ++i)
                {
                    if (!_this.Exist(files[i]))
                    {
                        _this.AddFile(files[i]);
                    }
                }
                _this.PostFirst();
            }
        }
        , OpenFolderDialog: function ()
        {
            var obj = this.plugin;
            var list = obj.ShowFolder();
            if (list)
            {
                for (var i = 0, l = list.length; i < l; ++i)
                {
                    if (!_this.Exist(list[i]))
                    {
                        _this.AddFile(list[i]);
                    }
                }
                _this.PostFirst();
            }
        }
        , PasteFiles: function ()
        {
            var obj = this.plugin;
            var list = obj.GetClipboardFiles();
            if (list)
            {
                for (var i = 0, l = list.length; i < l; ++i)
                {
                    if (!_this.Exist(list[i]))
                    {
                        _this.AddFile(list[i]);
                    }
                }
                _this.PostFirst();
            }
        }
        , GetFiles: function ()
        {
            var obj = this.plugin;
            var list = obj.GetFiles(folder, hasChild);
            if (list)
            {
                for (var i = 0, l = list.length; i < l; ++i)
                {
                    if (!_this.Exist(list[i]))
                    {
                        _this.AddFile(list[i]);
                    }
                }
                _this.PostFirst();
            }
        }
        , GetMacs: function ()
        {
            var obj = this.plugin;
            var list = obj.GetMacs();
            return list;
        }
        , Init: function ()
        {
            //var atl = document.getElementById("objHttpPartitionFF");
            var atl = _this.partitionFF;
            //atl.FileSizeLimit = _this.Config["FileSizeLimit"];
            //atl.RangeSize = _this.Config["RangeSize"];
            atl.EncodeType      = _this.Config["EncodeType"];
            atl.Debug           = _this.Config["Debug"];
            atl.LogFile         = _this.Config["LogFile"];
            atl.License         = _this.Config["License"];
            atl.Company         = _this.Config["Company"];
            atl.CryptoType      = _this.Config["CryptoType"];
            atl.Compress        = _this.Config["Compress"];
            atl.FileFieldName   = _this.Config["FileFieldName"];
            atl.PostUrl         = _this.Config["PostUrl"];
            //atl.Authenticate = _this.Config["Authenticate"];
            //atl.AuthName = _this.Config["AuthName"];
            //atl.AuthPass = _this.Config["AuthPass"];
            atl.OnPost          = HttpUploader_Process;
            atl.OnStateChanged  = HttpUploader_StateChanged;
            this.plugin = atl;
        }
	};
	this.BrowserChr = this.BrowserFF;
	this.BrowserChr.Check = function(){
	    for (var i = 0, l = navigator.plugins.length; i < l; i++)
	    {
	        if (navigator.plugins[i].name == _this.Config["CrxName"])
	        {
	            return true;
	        }
	    }
	    return false;
	};
	this.BrowserChr.Setup = function ()
	{
	    document.write('<iframe style="display:none;" src="' + _this.Config["CrxPath"] + '"></iframe>');
	};

    //浏览器环境检查
	_this.Browser = _this.BrowserIE;
	if (_this.ie)
	{
	    //if (!_this.Browser.Check()) { window.open(_this.Config["SetupPath"], "_blank"); /*_this.Browser.Setup();*/ } 
	}
	else if (_this.firefox)
	{
	    _this.Browser = _this.BrowserFF;
	    //if (!_this.Browser.Check()) { window.open(_this.Config["SetupPath"], "_blank"); /*_this.Browser.Setup();*/ }
	} //Chrome
	else if (_this.chrome)
	{
	    _this.Browser = _this.BrowserChr;
	    //if (!_this.Browser.Check()) { window.open(_this.Config["SetupPath"], "_blank"); /*_this.Browser.Setup();*/ }
	}

	this.GetHtml = function ()
	{
	    //加载拖拽控件
	    var acx = '<object name="objDroper" id="objFileDroper" classid="clsid:' + this.Config["ClsidDroper"] + '"';
	    acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '" width="192" height="192" >';
	    acx += '</object>';
	    //自动安装CAB
	    acx += '<div style="display:none">';
	    //文件上传控件
	    acx += '<object name="objUploader" id="objUpLoader" classid="clsid:' + this.Config["ClsidUploader"] + '"';
	    acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '" width="1" height="1" ></object>';
	    //文件夹选择控件
	    acx += '<object name="objPartition" id="objPartition" classid="clsid:' + this.Config["ClsidPartition"] + '"';
	    acx += ' codebase="' + this.Config["CabPath"] + '#version=' + this.Config["Version"] + '" width="1" height="1" ></object>';
	    acx += '</div>';
	    //上传列表项模板
	    acx += '<div name="tmpItem" class="UploaderItem" id="UploaderTemplate">\
					<div class="UploaderItemLeft">\
						<div class="FileInfo">\
							<div name="fileName" class="FileName top-space">HttpUploader程序开发.pdf</div>\
							<div name="fileSize" class="FileSize" child="1">100.23MB</div>\
						</div>\
						<div class="ProcessBorder top-space"><div name="process" class="Process"></div></div>\
						<div name="msg" class="PostInf top-space">已上传:15.3MB 速度:20KB/S 剩余时间:10:02:00</div>\
					</div>\
					<div class="UploaderItemRight">\
						<div class="BtnInfo"><span name="btnCancel" class="Btn">取消</span>&nbsp;<span name="btnDel" class="Btn hide">删除</span></div>\
						<div name="percent" class="ProcessNum">35%</div>\
					</div>';
	    acx += '</div>';
	    //分隔线
	    acx += '<div name="spliter" class="spliter" id="FilePostLine"></div>';
	    //上传列表
	    acx += '<div name="pnlUpload" id="UploaderPanel">\
		            <div class="header">上传文件</div>\
		            <div class="toolbar">\
		                <input name="btnAddFiles" id="btnAddFiles" type="button" value="选择多个文件" />\
		                <input name="btnAddFolder" id="btnAddFolder" type="button" value="选择文件夹" />\
		                <input name="btnPasteFiles" id="btnPasteFiles" type="button" value="粘贴文件" />\
		            </div>\
		            <div class="content">\
		                <div name="pnlUploaderList" id="FilePostLister"></div>\
		            </div>\
		            <div class="footer">\
		                <a href="javascript:void(0)" class="Btn" name="btnClear" id="lnkClearComplete">清除已完成文件</a>\
		            </div>\
		        </div>';
	    return acx;
	};

    //在外部调用。
	this.Load = function ()
	{
	    var html = this.GetHtml();
	    html += '<embed name="objHttpPartitionFF" id="objHttpPartitionFF" type="' + this.Config["MimeType"] + '" pluginspage="' + this.Config["CabPath"] + '" width="1" height="1"/>';
	    var ui = $(document.body).append(html);
	    this.InitUI(ui);
	};

	this.LoadTo = function (oid)
	{
	    //$("#"+oid).append(this.GetHtml());
	    //LoadChromeCtl("", this.Config["MimeType"], this.Config["CabPath"]);

	    var html = this.GetHtml();
	    html += '<embed name="objHttpPartitionFF" id="objHttpPartitionFF" type="' + this.Config["MimeType"] + '" pluginspage="' + this.Config["CabPath"] + '" width="1" height="1"/>';
	    var ui = $("#" + oid).append(html);
	    this.InitUI(ui);
	};

	this.InitUI = function (ui/*jquery object*/)
	{
	    var pnlUpload   = ui.find('div[name="pnlUpload"]');
	    var lstUpload   = ui.find('div[name="pnlUploaderList"]');
	    var tmpItem     = ui.find('div[name="tmpItem"]');
	    var droper      = ui.find('object[name="objDroper"]').get(0);
	    var partition   = ui.find('object[name="objPartition"]').get(0);
	    var partitionFF = ui.find('embed[name="objHttpPartitionFF"]');
	    var btnAddFiles = ui.find('input[name="btnAddFiles"]');
	    var btnAddFd    = ui.find('input[name="btnAddFolder"]');
	    var btnPasteFs  = ui.find('input[name="btnPasteFiles"]');
	    var lnkClear    = ui.find('a[name="btnClear"]');
	    var spliter     = ui.find('div[name="spliter"]');

	    this.uiUploadList   = lstUpload;
	    this.uiItem         = tmpItem;
        this.uiSpliter      = spliter;
	    this.Droper         = droper;
        this.partition      = partition;
        this.partitionFF    = partitionFF;

	    btnAddFiles.click(function () { _this.Browser.OpenFileDialog(); });
	    btnAddFd.click(function () { _this.Browser.OpenFolderDialog(); });
	    btnPasteFs.click(function () { _this.Browser.PasteFiles(); });
	    lnkClear.click(function () { _this.ClearComplete(); });

	    //droper
	    if (null != droper) droper.OnFileDrop = function ()
	    {
	        var list = droper.GetSelectedFiles();
	        if (list == null) return;
	        if (list.lbound(1) == null) return;

	        for (var index = list.lbound(1) ; index <= list.ubound(1) ; ++index)
	        {
	            if (!_this.Exist(list.getItem(index)))
	            {
	                _this.AddFile(list.getItem(index));
	            }
	        }
	        _this.PostFirst();
	    };

	    this.Browser.Init(); //控件初始化
	};

	
	//清除已完成文件
	this.ClearComplete = function()
	{
		for(var i = 0 ; i < this.CompleteList.length ; ++i)
		{
			this.Delete(this.CompleteList[i].idLoc);
		}
		this.CompleteList.length = 0;
	};

	//上传队列是否已满
	this.IsPostQueueFull = function()
	{
		//目前只支持同时上传三个文件
		//注意：JSP服务器只支持1个文件上传
		if (this.UploadIdList.length > 2)
		{
			return true;
		}
		return false;
	};

	//添加一个上传ID
	this.AppendUploadId = function(fid)
	{
		this.UploadIdList.push(fid);
	};

	/*
	从当前上传ID列表中删除指定项。
	此函数将会重新构造一个Array
	*/
	this.RemoveUploadId = function(fid)
	{
		if (this.UploadIdList.length < 1) return;
		
		for (var i = 0, l = this.UploadIdList.length; i < l; ++i)
		{
			if (this.UploadIdList[i] == fid)
			{
				this.UploadIdList.remove(fid);
			}
		}
	};

	//停止所有上传项
	this.StopAll = function()
	{
		for (var i = 0, l = this.UploadIdList.length; i < l; ++i)
		{
			this.UploaderList[this.UploadIdList[i]].Stop();
		}
		this.UploadIdList.length = 0;
	};

	/*
	添加到上传列表
	参数
	fid 上传项ID
	uploaderItem 新的上传对象
	*/
	this.AppenToUploaderList = function(fid, uploaderItem)
	{
		this.UploaderList[fid] = uploaderItem;
		this.UploaderListCount++;
	};

	//传送当前队列的第一个文件
	this.PostFirst = function()
	{
		//上传列表不为空
		if (this.UnUploaderIdList.length > 0)
		{
			while (this.UnUploaderIdList.length > 0)
			{
				//上传队列已满
				if (this.IsPostQueueFull()) return;
				var index = this.UnUploaderIdList.shift();
				this.UploaderList[index].Post();
			}
		}
	};

	/*
	验证文件名是否存在
	参数:
	[0]:文件名称
	*/
	this.Exist = function()
	{
		var fn = arguments[0];

		for (a in this.UploaderList)
		{
			if (this.UploaderList[a].LocalFile == fn)
			{
				return true;
			}
		}
		return false;
	};

	/*
	根据ID删除上传任务
	参数:
	fid 上传项ID。唯一标识
	*/
	this.Delete = function (fid)
	{
		var obj = this.UploaderList[fid];
		if (null == obj) return;

		//删除div
		obj.div.remove();
		//删除分隔线
		obj.spliter.remove();
		obj.LocalFile = "";
	};

	/*
		设置文件过滤器
		参数：
			filter 文件类型字符串，使用逗号分隔(exe,jpg,gif)
	*/
	this.SetFileFilter = function(filter)
	{
		this.FileFilter.length = 0;
		this.FileFilter = filter.split(",");
	};

	/*
	判断文件类型是否需要过滤
	根据文件后缀名称来判断。
	*/
	this.NeedFilter = function(fname)
	{
		if (this.FileFilter.length == 0) return false;
		var exArr = fname.split(".");
		var len = exArr.length;
		if (len > 0)
		{
			for (var i = 0, l = this.FileFilter.length; i < l; ++i)
			{
				//忽略大小写
				if (this.FileFilter[i].toLowerCase() == exArr[len - 1].toLowerCase())
				{
					return true;
				}
			}
		}
		return false;
	};

	/**
	 * 新增--添加文件及回调函数
	 * @param {} filePath
	 * @param {} callback
	 */
	this.AddFile = function (filePath,callback,data){
		//本地文件名称存在
	    if (this.Exist(filePath)) return;
	    //此类型为过滤类型
	    if (this.NeedFilter(filePath)) return;

	    var fileNameArray = filePath.split("\\");
	    var fileName = fileNameArray[fileNameArray.length - 1];
	    var fid = this.UploaderListCount;
	    this.UnUploaderIdList.push(fid);

	    var uper = new HttpUploader(fid, filePath, this);
	    
	    uper.callback=callback;
	    uper.data=data;
	    var ui = this.uiItem.clone(true);
	    ui.css("display", "block");
	    ui.attr("id", "item" + fid);
	    this.uiUploadList.append(ui);//添加到文件列表

	    var divFileName = ui.find("div[name='fileName']");
	    var divfileSize = ui.find("div[name='fileSize']")
	    var divProcess  = ui.find("div[name='process']");
	    var divMsg      = ui.find("div[name='msg']");
	    var btnCancel   = ui.find("span[name='btnCancel']");
	    var btnDel      = ui.find("span[name='btnDel']");
	    var divPercent  = ui.find("div[name='percent']");

	    divFileName.text(fileName);
	    divFileName.attr("title", arguments[0]);
	    divfileSize.text(uper.FileSize);
	    uper.pProcess = divProcess;
	    uper.pPercent = divPercent;
	    uper.pMsg = divMsg;
	    uper.pMsg.text("");
	    uper.pButton = btnCancel;
	    uper.pButton.attr("fid", fid);
	    uper.pButton.attr("domid", "item" + fid);
	    uper.pButton.attr("lineid", "FilePostLine" + fid);
	    uper.pButton.click(function ()
	    {
	        var obj = $(this);
	        switch (obj.text())
	        {
	            case "暂停":
	            case "停止":
	                uper.Stop(fid);
	                break;
	            case "取消":
	                {
	                    uper.Stop();
	                    uper.div.remove();
	                    uper.spliter.remove();
	                }
	                break;
	            case "续传":
	            case "重试":
	                uper.Post();
	                break;
	        }
	    });
	    uper.pBtnDel = btnDel;
	    uper.pBtnDel.click(function () { uper.Delete(); });
	    uper.pPercent.text("0%");
	    uper.Manager = this;

	    //添加到上传列表
	    this.AppenToUploaderList(fid, uper);
	    //分隔线
	    var split = this.uiSpliter.clone();
	    split.attr("id", "FilePostLine" + fid);
	    split.css("display", "block");
	    this.uiUploadList.append(split);
	    uper.spliter = split;
	    uper.div = ui;
	    uper.Ready(); //准备
	};
}

var HttpUploaderErrorCode = {
	"0": "连接服务器错误"
	,"1": "发送数据错误"
	,"2": "接收数据错误"
	,"3": "未设置本地文件"
	,"4": "本地文件不存在"
	,"5": "打开本地文件错误"
	,"6": "不能读取本地文件"
	,"7": "公司未授权"
	,"8": "未设置IP"
	,"9": "域名未授权"
	//md5
	,"200":"无打打开文件"
	,"201":"文件大小为0"
};

var HttpUploaderState = {
	Ready: 0,
	Posting: 1,
	Stop: 2,
	Error: 3,
	GetNewID: 4,
	Complete: 5,
	WaitContinueUpload: 6,
	None: 7,
	Waiting: 8
};

//文件上传对象
function HttpUploader(idLoc, filePath, mgr)
{
	//this.pMsg;
	//this.pProcess;
	//this.pPercent;
	//this.pButton
	//this.div
	//this.split
	//this.idLoc
	this.Manager = mgr; //上传管理器指针
	this.Config = mgr.Config;
	this.Fields = mgr.Fields;
	this.ActiveX = mgr.ActiveX;
	this.Browser = mgr.Browser;
	this.firefox = mgr.firefox;
	this.chrome = mgr.chrome;
	this.UploaderPool = mgr.UploaderPool;
	this.UploaderPoolFF = mgr.UploaderPoolFF;
	this.State = HttpUploaderState.None;
	//this.ATL = new ActiveXObject(this.ActiveX["Uploader"]);
	//this.ATL.Object = this;
	//this.ATL.CompanyLicensed = this.Config["Company"];
	//this.ATL.Debug = this.Config["Debug"];
	//this.ATL.LogFile = this.Config["LogFile"];
	//this.ATL.License = this.Config["License"];
	//this.ATL.PostUrl = this.Config["PostUrl"];
	//this.ATL.EncodeType = this.Config["EncodeType"];
	//添加附加信息
	//this.ATL.ClearFields(); //清空附加字段
	//for (var field in this.Fields)
	//{
	//	this.ATL.AddField(field, this.Fields[field]);
	//}
	
	//this.ATL.OnPost = HttpUploader_Process;
	//this.ATL.OnStateChanged = HttpUploader_StateChanged;
	//this.ATL.LocalFile = filePath;
	this.FileLength = this.Browser.GetFileLength(filePath); //文件大小，以字节为单位。"1212"
	this.FileSize = this.Browser.GetFileSize(filePath);
	//this.ATL.idLoc = idLoc;
	this.LocalFile = filePath;
	this.idLoc = idLoc;
	this.MD5 = "";
	var _this = this;
    
    //初始化控件
	this.ATL = {
	    "Create": function ()
	    {
	        if (this.inited) return;
	        if (_this.UploaderPool.length > 0)
	        {
	            this.com = _this.UploaderPool.pop();
	        }
	        else
	        {
	            this.com = new ActiveXObject(_this.ActiveX["Uploader"]);
	        }
	        this.com.Object     = _this;
	        this.com.Debug      = _this.Config["Debug"];
	        this.com.LogFile    = _this.Config["LogFile"];
	        this.com.License    = _this.Config["License"];
	        this.com.Company    = _this.Config["Company"];
	        this.com.CryptoType = _this.Config["CryptoType"];
	        this.com.Compress   = _this.Config["Compress"];
	        this.com.FileFieldName = _this.Config["FileFieldName"];
	        this.com.PostUrl    = _this.Config["PostUrl"];
	        console.log("_this.ActiveX[Uploader]    "+_this.ActiveX["Uploader"]);
	        console.log("_this.Config[PostUrl]    "+_this.Config["PostUrl"]);
	        console.log("this.com.PostUrl    "+this.com.PostUrl);
	        //this.com.Authenticate = _this.Config["Authenticate"];
	        //this.com.AuthName = _this.Config["AuthName"];
	        //this.com.AuthPass = _this.Config["AuthPass"];
	        this.com.EncodeType = _this.Config["EncodeType"];
	        //this.com.FileSizeLimit = _this.Config["FileSizeLimit"];
	        //this.com.RangeSize = _this.Config["RangeSize"];
	        //this.com.PostedLength = _this.FileLengthSvr;
	        //this.com.MD5 = _this.MD5;
	        this.com.Cookie = document.cookie;
	        this.com.LocalFile = _this.LocalFile;
	        this.com.OnPost = HttpUploader_Process;
	        this.com.OnStateChanged = HttpUploader_StateChanged;
	        this.inited = true;
	    }
	    //get
		, "GetFileSize": function () { return this.com.FileSize; }
		, "GetFileLength": function () { return this.com.FileLength; }
		, "GetResponse": function () { return this.com.Response; }
		, "GetMD5": function () { return this.com.MD5; }
		, "GetMd5Percent": function () { return this.com.Md5Percent; }
		, "GetPostedLength": function () { return this.com.PostedLength; }
		, "GetErrorCode": function () { return this.com.ErrorCode; }
        , "GetResponse": function () { return this.com.Response;}
	    //methods
		, "CheckFile": function ()
		{
		    if (!this.inited)
		    {
		        this.Create();
		    }
		    this.com.CheckFile();
		}
		, "Post": function ()
		{
		    if (!this.inited)
		    {
		        this.Create();
		    }
		    _this.ResetFields();
		    this.com.Post();
		}
		, "Stop": function ()
		{
		    this.com.Stop();
		    this.inited = false;//
		    //_this.FileLengthSvr = this.com.FileLengthSvr;
		}
		, "ClearFields": function () { this.com.ClearFields(); }
		, "AddField": function (fn, fv) { this.com.AddField(fn, fv); }
		, "Dispose": function ()
		{
		    this.inited = false;//取消已初始化标识
		    //将资源交给缓存池，以便重复使用。
		    _this.UploaderPool.push(this.com);
		}
		, "IsPosting": function ()
		{
		    if (!this.inited) return false;
		    return this.com.IsPosting();
		}
	    //property
		, "com": null
        , "inited": false
	};
    //Firefox 插件
	this.ATLFF = {
	    "Create": function ()
	    {
	        if (this.inited) return;
	        if (_this.UploaderPoolFF.length > 0)
	        {
	            this.idSign = _this.UploaderPoolFF.pop();
	            this.Atl.SetLocalFile(this.idSign, _this.LocalFile);
	        }
	        else
	        {
	            this.idSign = this.Atl.AddFile(_this.LocalFile);
	        }
	        this.Atl.SetObject(this.idSign, _this);
	        //this.Atl.SetPostedLength(this.idSign, _this.FileLengthSvr);
	        this.Atl.SetCookie(this.idSign, document.cookie);
	        //this.Atl.SetMD5(this.idSign, _this.MD5);
	        this.inited = true;
	    }
	    //get
		, "GetFileSize": function () { return this.Atl.GetFileSize(this.idSign); }
		, "GetFileLength": function () { return this.Atl.GetFileLength(this.idSign); }
		, "GetResponse": function () { return this.Atl.GetResponse(this.idSign); }
		, "GetMD5": function () { return this.Atl.GetMD5(this.idSign); }
		, "GetMd5Percent": function () { return this.Atl.GetMd5Percent(this.idSign); }
		, "GetPostedLength": function () { return this.Atl.GetPostedLength(this.idSign); }
		, "GetErrorCode": function () { return this.Atl.GetErrorCode(this.idSign); }
        , "GetResponse": function () { return this.Atl.GetResponse(this.idSign);}
	    //methods
		, "CheckFile": function ()
		{
		    if (!this.inited)
		    {
		        this.Create();
		    }
		    this.Atl.CheckFile(this.idSign);
		}
		, "Post": function ()
		{
		    if (!this.inited)
		    {
		        this.Create();
		    }
		    _this.ResetFields();
		    this.Atl.Post(this.idSign);
		}
		, "Stop": function ()
		{
		    this.Atl.Stop(this.idSign);
		    this.inited = false;//
		    //_this.FileLengthSvr = this.Atl.GetFileLengthSvr(this.idSign);
		}
		, "ClearFields": function () { this.Atl.ClearFields(this.idSign); }
		, "AddField": function (fn, fv) { this.Atl.AddField(this.idSign, fn, fv); }
		, "Dispose": function ()
		{
		    this.inited = false;//
		    _this.UploaderPoolFF.push(this.idSign);
		}
		, "IsPosting": function ()
		{
		    if (!this.inited) return false;
		    return this.Atl.IsPosting(this.idSign);
		}
	    //property
		, "Atl": mgr.BrowserFF.plugin
        , "inited": false
		, "idSign": "0"//由控件分配的标识符，全局唯一。
	};
    //是Firefox或Chrome浏览器
	if (this.firefox || this.chrome) { this.ATL = this.ATLFF; }

	this.ResetFields = function ()
	{
	    //添加附加信息
	    this.ATL.ClearFields(); //清空附加字段
	    for (var field in this.Fields)
	    {
	        this.ATL.AddField(field, this.Fields[field].toString());
	    }
	};

	//准备
	this.Ready = function()
	{
		//this.pButton.style.display = "none";
		this.pMsg.text( "正在上传队列中等待..." );
		this.State = HttpUploaderState.Ready;
	};
	
	this.Post = function()
	{
		this.Manager.AppendUploadId(this.idLoc);
		//if (this.MD5.length > 0)
		{
			this.Upload();
		}
		//else
		//{
		//	this.CheckFile();
		//}
	};
	
	//上传
	this.Upload = function ()
	{
		this.pButton.show();
		this.pButton.text("停止");
		//this.pMsg.innerText = "正在连接服务器....";
		this.State = HttpUploaderState.Posting;

		this.ATL.Post();
	};
	
	//检查文件
	this.CheckFile = function()
	{
		this.State = HttpUploaderState.MD5Working;
		this.ATL.CheckFile();		
	};

	//启动下一个传输
	this.PostNext = function()
	{
		if (this.Manager.UnUploaderIdList.length > 0)
		{
			var index = this.Manager.UnUploaderIdList.shift();
			var obj = this.Manager.UploaderList[index];

			//空闲状态
			if (HttpUploaderState.Ready == obj.State)
			{
				obj.Post();
			}
		}
	};
	this.Delete = function ()
	{
	    this.Manager.Delete(this.idLoc);
	};
	
	/*
		方法-停止传输
		参数
		[0] idLoc
	*/
	this.Stop = function()
	{
		this.pButton.text( "重试" );
		this.pMsg.text( "传输已停止...." );
		this.ATL.Stop();
		this.State = HttpUploaderState.Stop;

		//从上传列表中删除
		this.Manager.RemoveUploadId(this.idLoc);
		//添加到未上传列表
		this.Manager.UnUploaderIdList.push(this.idLoc);
		//传输下一个
		this.PostNext();
	};
}

//上传错误
function HttpUploader_Error(obj)
{
    obj.pMsg.innerText = HttpUploaderErrorCode[obj.ATL.GetErrorCode()];
    obj.pButton.text("重试");
    obj.pBtnDel.show();
	obj.pMsg.text("上传错误");
	console.log("上传错误"); 
	obj.State = HttpUploaderState.Error;
	//从上传列表中删除
	obj.Manager.RemoveUploadId(obj.idLoc);
	obj.ATL.Dispose();
	obj.PostNext();
}

//上传完成
function HttpUploader_Complete(obj)
{
    obj.pButton.text("");
    obj.pBtnDel.show();
	obj.pProcess.css("width","100%");
	obj.pPercent.text("100%");
	obj.pMsg.text("上传完成");
	console.log("上传完成"); 
	obj.Manager.CompleteList.push(obj);
	obj.State = HttpUploaderState.Complete;
	//alert(obj.ATL.GetResponse());
	//从上传列表中删除
	obj.Manager.RemoveUploadId(obj.idLoc);
	obj.ATL.Dispose();
	if(obj.callback){
		obj.callback(obj.ATL.GetResponse(),obj.data,obj);
	}
	obj.PostNext();
}

//传输进度。频率为每秒调用一次
function HttpUploader_Process(obj, speed, postedLength, percent, time)
{
	obj.pPercent.text( percent );
	obj.pProcess.css("width", percent);
	var str = "已上传:" + postedLength + " 速度:" + speed + "/S 剩余时间:" + time;
	obj.pMsg.text( str );
}

//服务器连接成功
function HttpUploader_Connected(obj)
{
	obj.pMsg.text( "服务器连接成功" );
}

//MD5计算中
function HttpUploader_MD5_Working(obj)
{
	var msg = "扫描进度：" + obj.ATL.Md5Percent + "%";
	obj.pMsg.text( msg );
}

//MD5计算完毕
function HttpUploader_MD5_Complete(obj)
{
	obj.MD5 = obj.ATL.MD5;
	//在此处增加服务器验证代码。
	obj.pMsg.text( "MD5计算完毕，开始连接服务器...");
	
	obj.Upload();
}

/*
	HUS_Leisure			=0	//空闲
	,HUS_Uploading		=1	//上传中 
	,HUS_Stop  			=2	//停止 
	,HUS_UploadComplete	=3	//传输完毕 
	,HUS_Error 			=4	//错误 
	,HUS_Connected 		=5	//服务器已连接
	,HUS_Md5Working		=6	//MD5计算中
	,HUS_Md5Complete	=7	//MD5计算完毕
*/
function HttpUploader_StateChanged(obj,state)
{
	switch( state )
	{
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			HttpUploader_Complete(obj);
			break;
		case 4:
			HttpUploader_Error(obj);
			break;
		case 5:
			HttpUploader_Connected(obj);
			break;
		case 6:
			HttpUploader_MD5_Working(obj);
			break;
		case 7:
			HttpUploader_MD5_Complete(obj);
			break;
	}
}