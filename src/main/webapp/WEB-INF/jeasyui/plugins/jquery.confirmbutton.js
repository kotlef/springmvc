(function($) {
	function _1(_2, _3) {
		var _4 = $.data(_2, "confirmbutton").options;
		if (_3) {
			$.extend(_4, _3);
		}
		if (_4.width || _4.height || _4.fit) {
			var _5 = $(_2);
			var _6 = _5.parent();
			var _7 = _5.is(":visible");
			if (!_7) {
				var _8 = $("<div style=\"display:none\"></div>")
						.insertBefore(_2);
				var _9 = {
					position : _5.css("position"),
					display : _5.css("display"),
					left : _5.css("left")
				};
				_5.appendTo("body");
				_5.css({
							position : "absolute",
							display : "inline-block",
							left : -20000
						});
			}
			_5._size(_4, _6);
			var _a = _5.find(".l-btn-left");
			_a.css("margin-top", 0);
			_a.css("margin-top", parseInt((_5.height() - _a.height()) / 2)
							+ "px");
			if (!_7) {
				_5.insertAfter(_8);
				_5.css(_9);
				_8.remove();
			}
		}
	};
	function _b(_c) {
		var _d = $.data(_c, "confirmbutton").options;
		var t = $(_c).empty();
		t
				.addClass("l-btn")
				.removeClass("l-btn-plain l-btn-selected l-btn-plain-selected l-btn-outline");
		t.removeClass("l-btn-small l-btn-medium l-btn-large").addClass("l-btn-"
				+ _d.size);
		if (_d.plain) {
			t.addClass("l-btn-plain");
		}
		if (_d.outline) {
			t.addClass("l-btn-outline");
		}
		if (_d.selected) {
			t.addClass(_d.plain
					? "l-btn-selected l-btn-plain-selected"
					: "l-btn-selected");
		}
		t.attr("group", _d.group || "");
		t.attr("id", _d.id || "");
		/*
		 * 添加属性title，msg，fun
		 */
		t.attr("title", _d.title || "");
		t.attr("msg", _d.msg || "");

		var _e = $("<span class=\"l-btn-left\"></span>").appendTo(t);
		if (_d.text) {
			$("<span class=\"l-btn-text\"></span>").html(_d.text).appendTo(_e);
		} else {
			$("<span class=\"l-btn-text l-btn-empty\">&nbsp;</span>")
					.appendTo(_e);
		}
		if (_d.iconCls) {
			$("<span class=\"l-btn-icon\">&nbsp;</span>").addClass(_d.iconCls)
					.appendTo(_e);
			_e.addClass("l-btn-icon-" + _d.iconAlign);
		}
		t.unbind(".confirmbutton").bind("focus.confirmbutton", function() {
					if (!_d.disabled) {
						$(this).addClass("l-btn-focus");
					}
				}).bind("blur.confirmbutton", function() {
					$(this).removeClass("l-btn-focus");
				}).bind("click.confirmbutton", function() {
			if (!_d.disabled) {
				// 绑定单击事件
				$.messager.confirm("提示", "你确定要进行" + _d.text + "操作吗?",
						function(r) {
							if (r == true) {
								_d.fun.call(this);
							}
							if (r == false) {
								_d.nofun.call(this);
							}
						});
				if (_d.toggle) {
					if (_d.selected) {
						$(this).confirmbutton("unselect");
					} else {
						$(this).confirmbutton("select");
					}
				}
				_d.onClick.call(this);
			}
		});
		_f(_c, _d.selected);
		_10(_c, _d.disabled);
	};
	function _f(_11, _12) {
		var _13 = $.data(_11, "confirmbutton").options;
		if (_12) {
			if (_13.group) {
				$("a.l-btn[group=\"" + _13.group + "\"]").each(function() {
					var o = $(this).confirmbutton("options");
					if (o.toggle) {
						$(this)
								.removeClass("l-btn-selected l-btn-plain-selected");
						o.selected = false;
					}
				});
			}
			$(_11).addClass(_13.plain
					? "l-btn-selected l-btn-plain-selected"
					: "l-btn-selected");
			_13.selected = true;
		} else {
			if (!_13.group) {
				$(_11).removeClass("l-btn-selected l-btn-plain-selected");
				_13.selected = false;
			}
		}
	};

	function _10(_14, _15) {
		var _16 = $.data(_14, "confirmbutton");
		var _17 = _16.options;
		$(_14).removeClass("l-btn-disabled l-btn-plain-disabled");
		if (_15) {
			_17.disabled = true;
			var _18 = $(_14).attr("href");
			if (_18) {
				_16.href = _18;
				$(_14).attr("href", "javascript:void(0)");
			}
			if (_14.onclick) {
				_16.onclick = _14.onclick;
				_14.onclick = null;
			}
			_17.plain
					? $(_14).addClass("l-btn-disabled l-btn-plain-disabled")
					: $(_14).addClass("l-btn-disabled");
		} else {
			_17.disabled = false;
			if (_16.href) {
				$(_14).attr("href", _16.href);
			}
			if (_16.onclick) {
				_14.onclick = _16.onclick;
			}
		}
	};
	/**
	 * 插件定义
	 */
	$.fn.confirmbutton = function(options, param) {
		if (typeof options == "string") {
			return $.fn.confirmbutton.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
					var data = $.data(this, "confirmbutton");
					if (data) {
						$.extend(data.options, options);
					} else {
						$.data(this, "confirmbutton", {
									options : $.extend({},
											$.fn.confirmbutton.defaults,
											$.fn.confirmbutton
													.parseOptions(this),
											options)
								});
						$(this).removeAttr("disabled");
						$(this).bind("_resize", function(e, _1c) {
									if ($(this).hasClass("easyui-fluid") || _1c) {
										_1(this);
									}
									return false;
								});
					}
					_b(this);
					_1(this);
				});
	};

	$.fn.confirmbutton.methods = {
		options : function(jq) {
			return $.data(jq[0], "confirmbutton").options;
		},
		resize : function(jq, _1d) {
			return jq.each(function() {
						_1(this, _1d);
					});
		},
		enable : function(jq) {
			return jq.each(function() {
						_10(this, false);
					});
		},
		disable : function(jq) {
			return jq.each(function() {
						_10(this, true);
					});
		},
		select : function(jq) {
			return jq.each(function() {
						_f(this, true);
					});
		},
		unselect : function(jq) {
			return jq.each(function() {
						_f(this, false);
					});
		}
	}

	$.fn.confirmbutton.parseOptions = function(target) {
		var t = $(target);
		// 解析 data-options 中的初始化参数
		return $.extend({}, $.parser.parseOptions(target, ["id", "iconCls",
								"iconAlign", "group", "size", "text", "title",
								"msg", "isConfirm", {
									plain : "boolean",
									toggle : "boolean",
									selected : "boolean",
									outline : "boolean"
								}]), {
					disabled : (t.attr("disabled") ? true : undefined),
					text : ($.trim(t.html()) || undefined),
					iconCls : (t.attr("icon") || t.attr("iconCls"))
				});
	};
	// 默认的属性

	/*
	 * $.fn.confirmbutton.defaults=$.extend( {}, //$.fn.linkbutton.defaults,
	 * {fun:function(){},nofun:function(){} } );
	 */

	$.fn.confirmbutton.defaults = {
		id : null,
		disabled : false,
		toggle : false,
		selected : false,
		outline : false,
		fun : function() {
		},
		nofun : function() {
		},
		group : null,
		plain : false,
		text : "",
		iconCls : null,
		iconAlign : "left",
		size : "small",
		onClick : function() {
		}
	};
	// 将自定义的插件加入 easyui 的插件组
	$.parser.plugins.push('confirmbutton');

})(jQuery);