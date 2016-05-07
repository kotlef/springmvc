<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<title>四维E通</title>

   	<script language="JavaScript"  type="text/javascript"  src="jeasyui/jquery.min.js"></script>
    <script language="JavaScript" type="text/javascript" src="common/rsa/jsbn.js"></script>
	<script language="JavaScript" type="text/javascript" src="common/rsa/base64.js"></script>
	<script language="JavaScript" type="text/javascript" src="common/rsa/prng4.js"></script>
	<script language="JavaScript" type="text/javascript" src="common/rsa/rnd.js"></script>
	<script language="JavaScript" type="text/javascript" src="common/rsa/rsa.js"></script>
	<script language="JavaScript" type="text/javascript" src="common/rsa/encryption.js"></script>
	<script language="JavaScript"  type="text/javascript"  src="bootstrap/js/bootstrap.min.js"></script>

 	<!-- Bootstrap core CSS -->
    <link href="bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <link rel="stylesheet" type="text/css" href="css/login.css" />

    <!-- Just for debugging purposes. Don't actually copy these 2 lines! -->
    <!--[if lt IE 9]><script src="bootstrap/assets/ie8-responsive-file-warning.js"></script><![endif]-->
    <script src="bootstrap/assets/ie-emulation-modes-warning.js"></script>

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
      <script src="bootstrap/assets/html5shiv.min.js"></script>
      <script src="bootstrapassets/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>


     <div class="container">

      <form class="form-signin"  id="loginForm"  onsubmit="return loginHandler();"  method="POST">
        <h2 class="form-signin-heading">管理员登录</h2>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="text" id="loginString" name="loginString" class="form-control loginString" placeholder="邮箱/手机/身份证" required autofocus>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="inputPassword" class="form-control" placeholder="密码" required>
         <#if Session.showCaptcha?exists>
				<input type="text" class="form-control captcha" name="captcha" id="captcha" placeholder="验证码"  required/>
			   	<img id="captchaImg" src="./getCaptcha" alt="验证码" />
			   	<input type="hidden" id="capthchNumber" value="${Session["captcha"]} "/>
		       	<a href="#"  id ="captchaHref" class="btn btn-info btn-sm ">更换验证码</a><br/>
       	  </#if>
        <div class="checkbox">
          <label>
            <input type="checkbox" id="rememberMe" name="rememberMe" value="rememberMe"> 记住我
          </label>
        </div>
        <#if shiroLoginFailure?exists>  
		  <div class="alert alert-danger" style="background-image: none;">
		       ${shiroLoginFailure}
		  </div>
       </#if>
       
         ${request.loginFailure}
       <#if Request["loginFailure"]?exists>  
		  <div class="alert alert-danger" style="background-image: none;">
		       ${Request["loginFailure"]}
		  </div>
       </#if>
        <button class="btn btn-lg btn-primary btn-block" id="loginBtn"  type="submit">登录</button>
      </form>

    </div> <!-- /container -->


    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="bootstrap/assets/ie10-viewport-bug-workaround.js"></script>

  <script src="/ynlxcloud/common/js.cookie.js"></script>

    <script type="text/javascript">
		$(function() {

				//加密
		     $.get('./security');
		     $('#captchaHref').click(function() {
			     $('#captchaImg').attr('src','./getCaptcha?' + Math.floor(Math.random() * 100)).fadeIn();
		     	 //$("#capthchNumber").val(Cookies.get("captcha"));
		     	});
				//订单类型缓存到本地
				$.ajax({
					url:"http://localhost:8080/ynlxbusiness/billtype",
					success: function(result){
						for(var i=0;i<result.length;i++){
							var wordbook=result[i];
							var key=wordbook.billType;
							window.localStorage.setItem(key,wordbook.billTypeName);
						}
					}
				});
		     	//字典缓存
		    	  $.ajax({
						  url: "http://localhost:8080/ynlxmainarchive/wordbook",
						  success: function(result){
						  		var rows=result.rows;
								for(var i=0;i<rows.length;i++){
									var wordbook=rows[i];
									var key=wordbook.keyword+"_"+wordbook.wordValue;
									 window.localStorage.setItem(key,wordbook.wordDisplay);
								}
						  }
						});
	    });

	    function do_encrypt(password,n,e) {
   			var res= RSA.encrypt(password,n,e);
    		return  res;

		}

		function loginHandler() {
			var captcha = $("#captcha").val();
			var pe=Cookies.get("rId");
			var n=pe;
			var pw=$("#inputPassword").val();
			var password = do_encrypt(pw,n,'10001');
			$("#inputPassword").val(password);


		}

		if(Cookies.get("homepageId")==undefined)
		     	Cookies.set('homepageId', '0', { path: '/ynlxcloud' });

    </script>
</body>
</html>
