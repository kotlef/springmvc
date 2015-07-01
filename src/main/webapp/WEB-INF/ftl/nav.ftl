<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>四维E通</title>

   	<script language="JavaScript"  type="text/javascript"  src="jeasyui/jquery.min.js"></script>
   	<script language="JavaScript"  type="text/javascript"  src="bootstrap/js/bootstrap.min.js"></script>
 	<!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/nav_tab.css" rel="stylesheet">

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="bootstrap/assets/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="bootstrap/assets/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="bootstrap/assets/html5shiv.min.js"></script>
      <script src="bootstrap/assets/respond.min.js"></script>
    <![endif]-->
    <script language="JavaScript"  type="text/javascript">
     function initNavMore(size){
		var width=120;//单个菜单宽度
		var menuWidths=size*width;
		var moreWidth=menuWidths+60;
	    $('#nav_more').width(moreWidth+'px');
		$('#more_menu_ul').css('marginLeft',menuWidths+'px');
	 }
	 $(function(){
		initNavMore(3);
	    $('#more_menu_ul').click(function() {
	        var objA=$('#more_href');
	        var objB=$('#icon_more');
		    var $parentItem = objA.parent(),
		        slideAmt = objA.next().width(),
		        direction;

		    if (parseInt($parentItem.css('marginLeft'), 10) < 0) {
		      direction = '+=';
		      objA.removeClass('expanded');
		      objB.removeClass('glyphicon glyphicon-triangle-right');
		      objB.addClass('glyphicon glyphicon-triangle-left');
		    } else {
		      objA.addClass('expanded');
		      objB.removeClass('glyphicon glyphicon-triangle-left');
		      objB.addClass('glyphicon glyphicon-triangle-right');
		      direction = '-=';
		    }
		    $parentItem.animate({marginLeft: direction + slideAmt}, 400);
		    return false;
		});
	});
    </script>
  </head>
  <body>
  <div class="container-fluid">
 <div class="row">
  <div class="col-md-6">
					<div class="navbar-header">
						 <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">系统导航</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#">四维E通</a>
					</div>
				<div style="margin-top: 5px;">
						<ul class="nav nav-pills">
							<#list homepageList as hp>
							<li  role="presentation" <#if homepageId==hp.homepageId> class="active" </#if> onclick='currentPage(${hp.homepageId})' id="${hp.homepageId}">
								<a href="${hp.homepageUrl}" target="main">${hp.homepageName}</a>
							</li>
							</#list>
						</ul>
						</div>
  </div>
  <div class="col-md-6">
  		<ul class="nav navbar-nav navbar-right">
                   	 <li>
		                   <div id="nav_more" class="nav_more">
										<ul  id="more_menu_ul">
											<li>
												<a id="more_href" href="javascript:void(0);"><b id="icon_more" class="glyphicon glyphicon-triangle-left">更多</b></a>
												<ul>
													<li><a href="#">菜单一</a></li>
													<li><a href="#">菜单二</a></li>
													<li><a href="#">菜单三</a></li>
												</ul>
											</li>
										</ul>
								</div>
						</li>
						<li>
							<a href="#">修改密码</a>
						</li>
                        <li>
							<a href="/ynlxcloud/logout" target="_parent">退出</a>
						</li>
					</ul>
  </div>
</div>

</div>



    <script src="common/js.cookie.js"></script>
    <script src="common/json2.js"></script>
    <script type="text/javascript">
      function currentPage(homepageId){
	      Cookies.set('homepageId', homepageId, { path: '/ynlxcloud' })
	      $(".active").removeClass("active")
	      $("#"+homepageId).addClass("active");
      }
      $(function() {
      	 Cookies.remove("rId", { path: '/ynlxcloud' });
      	 $.getJSON('./currentUserInfo',function(data){
      	 	window.localStorage.setItem('currentUserInfo',JSON.stringify(data));
      	 })
      })
    </script>
</body>
</html>
