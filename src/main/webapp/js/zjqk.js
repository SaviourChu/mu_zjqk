//格式化金额
function gshje(val){
	return val != null ? val.toFixed(2) : 0.00;
}

//提示信息
function promptMsg(){
	$.messager.show({
		title : '提示',
		msg : '请选择操作的记录',
		timeout : 3000,
		showType : 'slide'
	});
}

//有参数提示信息
function promptMsgParam(info){
	$.messager.show({
		title : '提示',
		msg : info,
		timeout : 3000,
		showType : 'slide'
	});
}

//文本渲染颜色
function renderColor(color, status){
	return "<span style='color:" + color +";'>" + status + "</span>";
}

//批量操作
function batchOperator(dg, status, info1, info2, url, s) {
	var rows = $('#'+dg).datagrid('getSelections');
	var ids = "";
	var j = rows.length;
	if (j > 0) {
		for(var i=0; i < j; i++){
			if(rows[i].status == status){
				ids += i == j-1 ? rows[i].id : rows[i].id + ";";
			}
		}
		if(ids != ""){
			$.messager.confirm("操作提示", info1, function(data){
				if(data){
					$.post(url,{"ids" : ids}, function(result){
						if (result.code != 12000) {
							promptMsgParam(result.msg);
						}else{
							eval(s);
						}
					}, "json");
				}
			});
		}else{
			promptMsgParam(info2);
		}
	}else {
		promptMsg();
	}
}

//批量删除操作
function batchDelete(dg, url, s){
	var rows = $('#'+dg).datagrid('getSelections');
	var ids = "";
	var j = rows.length;
	if (j > 0) {
		for(var i=0; i < j; i++){
			ids += i == j-1 ? rows[i].id : rows[i].id + ";";
		}
		$.messager.confirm("操作提示", "您确定要删除选中的记录吗？", function(data){
			if(data){
				$.post(url,{"ids" : ids}, function(result){
					if (result.code != 12000) {
						promptMsgParam(result.msg);
					}else{
						eval(s);
					}
				}, "json");
			}
		});
	}else {
		promptMsg();
	}
}

//批量提交操作
function batchSubmits(dg, url, s){
	var rows = $('#'+dg).datagrid('getSelections');
	var ids = "";
	var j = rows.length;
	if (j > 0) {
		for(var i=0; i < j; i++){
			if(rows[i].status == '未提交' || rows[i].status == '审批失败'){
				ids += i == j-1 ? rows[i].id : rows[i].id + ";";
			}
		}
		$.messager.confirm("操作提示", "您确定要提交已选择的记录吗？<br><span style='color:red;'>(只会处理<span style='color:blue;'>未提交</span>和<span style='color:blue;'>审批失败</span>的记录！)</span>", function(data){
			if(data){
				$.post(url,{"ids" : ids}, function(result){
					if (result.code != 12000) {
						promptMsgParam(result.msg);
					}else{
						eval(s);
					}
				}, "json");
			}
		});
	}else {
		promptMsg();
	}
}

//生成UUID
function uuid(len, radix) {
	var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
	var uuid = [], i;
	radix = radix || chars.length;
	if (len) {
		for (i = 0; i < len; i++)
			uuid[i] = chars[0 | Math.random() * radix];
	} else {
		var r;
		uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
		uuid[14] = '4';
		for (i = 0; i < 36; i++) {
			if (!uuid[i]) {
				r = 0 | Math.random() * 16;
				uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
			}
		}
	}
	return uuid.join('');
}

var LM = {
    alert: {}
}
LM.alert.Wechat = function(t){
    var t = t || '已收藏'
    $('#alertWechat').remove()
    clearTimeout(LM.alert.Time)
    var alertWechat = '<div id="alertWechat" style="position:fixed;bottom:50%;left:10px;right:10px;text-align:center;z-index:100"><span style="background-color:rgba(0,0,0,.8);padding:10px;min-width:100px;color:#fff;box-shadow:0 0 10px rgba(0,0,0,.6);display:inline-block;">'+t+'</span></div>'
    $('body').append(alertWechat)
    LM.alert.Time = setTimeout(function(){
        $('#alertWechat').remove()
    }, 2000)
}

function isMobile(mobile) {
    var searchStr = /^(1+\d{10})$/;
    if (!searchStr.test(mobile)) {
        return false;
    }
    return true;
}