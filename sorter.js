//================================================================================================
//===================本文件和本项目没有任何关系，防在这里只是为了方便用网络访问本文件===================
//================================================================================================
//=================这里的代码以通用性（代码放到哪个页面都能使用）为优先，因此会比较复杂=================
//================================================================================================
//=========================================神秘代码见下============================================
//================================================================================================

/*
var sorter = document.createElement('script');
sorter.type = "text/javascript";
sorter.src = "https://github.com/NightDW/SpareCash/blob/gh-pages/sorter.js";
document.getElementsByTagName('head')[0].appendChild(sorter);
sorter_initialize();
*/

//一个页面可能有多个table；后两个是数组，表示第i个table有几列几行
var tables, table_col_num, table_row_num;

//页面加载完毕后初始化
window.onload = function(){
	sorter_initialize();
}

//初始化
function sorter_initialize(){
	
	//引入JQuery和Loadash
	var jq = document.createElement('script');
	jq.type = "text/javascript";
	jq.src = "https://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.0/jquery.js";
	document.getElementsByTagName('head')[0].appendChild(jq);
	var lo = document.createElement('script');
	lo.type = "text/javascript";
	lo.src = "https://cdnjs.cloudflare.com/ajax/libs/lodash.js/3.10.1/lodash.js";
	document.getElementsByTagName('head')[0].appendChild(lo);
	
	//给所有的th标签添加click事件和并初始化为nosort类
	//在其它页面，由于缺少这里的sorter.css文件，因此设置为nosort、ascend、descend类并不会改变样式
	//但依然需要初始化为nosort类，方便后续判断要升序还是降序
	var ths = document.getElementsByTagName("th");
	for(var i = 0; i < ths.length; i++){
		ths[i].setAttribute("onclick", "sort(this)");
		ths[i].setAttribute("class", "nosort");
	}
	
	//初始化两个数组
	tables = document.getElementsByTagName("table");
	table_col_num = new Array(tables.length);
	table_row_num = new Array(tables.length);
	for(var i = 0; i < tables.length; i++){
		table_col_num[i] = tables[i].getElementsByTagName("th").length;
		table_row_num[i] = tables[i].getElementsByTagName("tr").length;
	}
}

function sort(obj){
	
	//获取到被点击的th标签是整个页面中的第几个
	var index = $("th").index(obj);
	
	//解析所点击的th标签是第几个table的第几列，并获取到指定table和指定th标签
	var json = parse(index);
	var table = tables[json.table_index];
	var ths = table.getElementsByTagName("th");
	var th = ths[json.th_index];
	
	//flag为true表示要进行降序排序，为false则升序
	var flag = obj.getAttribute("class") == "ascend";
	
	//把同table的所有th标签设为nosort类，再把选中的th标签根据flag设置为相应的类
	$(ths).attr("class", "nosort");
	$(th).attr("class", flag ? "descend" : "ascend");
	
	//获取所选table的所有行，注意第一行是标题，没用
	//_.drop(arr, num)方法：从头开始，删除arr的前num个元素，num默认为1
	//var cols = table.getElementsByTagName("tr");
	//cols = _.drop(cols);
	
	//获取所选table的tbody，进而获取到非标题的行
	var tbody = table.getElementsByTagName("tbody")[0];
	var cols = tbody.getElementsByTagName("tr");
	
	//升序排序
	cols = _.sortBy(cols, function(col) { return col.getElementsByTagName("td")[json.th_index].innerHTML; });
	
	//删除tbody的所有元素并重新把cols中的元素依次添加进去
	tbody.innerHTML = "";
	if(flag)
		for(var i = cols.length - 1; i >= 0; i--)
			tbody.appendChild(cols[i]);
	else
		for(var i = 0; i < cols.length; i++)
			tbody.appendChild(cols[i]);
}

//用于解析所点击的th标签是第几个table的第几列
function parse(index){
	if(index < table_col_num[0])
		return {table_index: 0, th_index: index};
	index -= table_col_num[0];
	for(var i = 1; i < table_col_num.length; i++){
		if(index < table_col_num[i])
			return {table_index: i, th_index: index};
		index -= table_col_num[i];
	}
}
