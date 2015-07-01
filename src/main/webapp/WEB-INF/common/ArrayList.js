// ArrayList.

function ArrayList() {
	this.index = -1;
	this.array = new Array();
}

ArrayList.prototype.clear = function() // 清空
{
	this.index = -1;
	this.array = new Array();
}

ArrayList.prototype.add = function(obj) // 添加
{
	if (this.getIndex(obj) == -1) {
		this.index = this.index + 1;
		this.array[eval(this.index)] = obj;
	}
}

ArrayList.prototype.get = function(index) // 根据索引取值
{
	return this.array[eval(index)];
}

ArrayList.prototype.size = function() // 获取ArrayList长度
{
	return this.index + 1;
}

ArrayList.prototype.getIndex = function(obj) // 根据值取出在ArrayList中索引
{
	var index = -1;
	for (var i = 0; i < this.array.length; i++) {
		if (this.array[i] == obj) {
			index = i;
			break;
		}
	}
	return index;
}

ArrayList.prototype.remove = function(index) // 根据索引删除
{
	var j = 0;
	var arrThis = this.array;
	var arrTemp = new Array();
	for (w = 0; w < arrThis.length; w++) {
		if (eval(index) != eval(w)) {
			arrTemp[j] = arrThis[w];
			j++;
		}
	}
	this.array = arrTemp;
	this.index = eval(j - 1);
}

ArrayList.prototype.removeValue = function(obj) // 根据值删除
{
	var j = 0;
	var arrThis = this.array;
	var arrTemp = new Array();
	for (w = 0; w < arrThis.length; w++) {
		if (obj != arrThis[w]) {
			arrTemp[j] = arrThis[w];
			j++;
		}
	}
	this.array = arrTemp;
	this.index = eval(j - 1);
}

ArrayList.prototype.toString = function() // 转换为字符串，中间用','分开
{
	var strResult = "";
	for (var i = 0; i < this.array.length; i++) {
		if (strResult == "")
			strResult = this.array[i];
		else
			strResult = strResult + "," + this.array[i];
	}
	return strResult;
}