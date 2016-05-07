<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>四维E通</title>
   	<script language="JavaScript"  type="text/javascript"  src="jeasyui/jquery.min.js"></script>
</head>
	<script type="text/javascript">
		$(function() {
	      	 $.getJSON('./currentUserInfo',function(data){
	      	 	window.localStorage.setItem('currentUserInfo',JSON.stringify(data));
	      	 })
	      })
	 </script>
	<FRAMESET rows=50,* cols=* bordercolor='#006699' border=1 >
 		<FRAME id=topFrame name=topFrame src="/ynlxcloud/nav" noResize scrolling=no  target="fpage">
    	<#if homepage !="-1">
    	<FRAME name=main src="common/edb_bar/topIndex.html?urlStr=${homepage}">
    	<#else>
    	<FRAME name=main src="">
    	</#if>
 		<NOFRAMES>
 		</NOFRAMES>
	</FRAMESET>
</html>
