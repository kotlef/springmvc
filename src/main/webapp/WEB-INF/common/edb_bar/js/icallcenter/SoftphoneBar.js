hojo.provide("icallcenter.SoftphoneBar");
hojo.require("icallcenter.Phone");
hojo.require("icallcenter.hojotools");

hojo.declare("icallcenter.SoftphoneBar", null, {
	constructor: function(phone,srcNodeName) {
		this._phone = phone;
		this._srcNodeRef = hojo.byId(srcNodeName);
		this._srcNodePeerStateRef = hojo.byId(srcNodeName + ".peerState");
		this._srcNodePeerTimeStateRef = hojo.byId(srcNodeName + ".peerTimeState");
		this._srcNodeCallTimeStateRef = hojo.byId(srcNodeName + ".callTimeState");
		this._srcNodeSelectStateRef = hojo.byId(srcNodeName + ".select");
		this._srcNodeSelectMenuRef = hojo.byId(srcNodeName + ".selectMenu");
		this._srcNodePhoneNumRef = hojo.byId(srcNodeName + ".softPhoneNum");
		
		
		this._callStateDescription["stInnerDialing"] = "呼叫中";
		this._callStateDescription["stInnerTalking"] = "内部通话";
		this._callStateDescription["stInvalid"] = "空闲";
		this._callStateDescription["stAbate"] = "失效";
		this._callStateDescription["stBelling"] = "来电振铃";
		this._callStateDescription["stTalking"] = "普通通话";
		this._callStateDescription["stListening"] = "监听振铃";
		this._callStateDescription["stListened"] = "监听中";
		this._callStateDescription["stDialing"] = "呼叫中";
		this._callStateDescription["stDialTalking"] = "外呼通话";
		this._callStateDescription["stHold"] = "保持";
		this._callStateDescription["stInnerBelling"] = "来电振铃";
		this._callStateDescription["stThreeWayTalking"] = "三方通话";
		
		this._callStateColor["0"] = "#41b754";
		this._callStateColor["1"] = "#ff6400";
		this._callStateColor["2"] = "#ff6400";
		this._callStateColor["99"] = "#ff6400";
		this._callStateColor["call"] = "#FF33FF";
		
	    this._phone.register("EvtPeerToolBarChange", this, "onPeerToolBarStateChanged"); 
	    this._phone.register("EvtCallToolBarChange", this, "onCallToolBarStateChanged");
	    
	    //供保持恢复用
	    this._phone.register("EvtBarChange", this, "_render");
	    
	},
	
	_callStateColor: [],
	
    _phone: null,
    
    _srcNodeRef: null,
    
    _srcNodePeerStateRef: null,
    _srcNodePeerTimeStateRef: null,
    _srcNodeCallTimeStateRef: null,
    _srcNodeSelectStateRef: null,
    _srcNodeSelectMenuRef: null,
    _srcNodePhoneNumRef: null,
    
    //通话状态描述
    _callStateDescription: [],
    
    //计时器
    _peerCalculagraph: null,
    
	//座席状态的处理时间
	_peerSecond: "1",
	_peerMinute: "0",
	_peerHour: "0",

			//sip方式的toolbar状态
	sip_stInvalid: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleEnable", "RestDisable", "BusyDisable"],
	sip_stBusy: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestDisable", "BusyEnable"],
	sip_stRest: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestEnable", "BusyDisable"],
	sip_stDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stDialTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable","ConsultEnable"],
	sip_stHold:["DialDisable", "HangupDisable", "HoldGetEnable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stAbate: ["DialDisable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	sip_stInnerDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stInnerTalking: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stBelling: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable", "ConsultEnable"],
	sip_stListening: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stListened: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stInnerBelling: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	sip_stThreeWayTalking: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	sip_stConcultTalking: ["DialDisable", "HangupEnable", "ConsultThreeWayCallEnable", "ConsultTransferEnable","StopConsultEnable"],

	
	//gateway方式的toolbar状态
	gateway_stInvalid: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleEnable", "RestDisable", "BusyDisable"],
	gateway_stBusy: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestDisable", "BusyEnable"],
	gateway_stRest: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestEnable", "BusyDisable"],
	gateway_stDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stDialTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable","ConsultEnable"],
	gateway_stHold:["DialDisable", "HangupDisable", "HoldGetEnable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stAbate: ["DialDisable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	gateway_stInnerDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stInnerTalking: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stBelling: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable", "ConsultEnable"],
	gateway_stListening: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stListened: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stInnerBelling: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	gateway_stThreeWayTalking: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	gateway_stConcultTalking: ["DialDisable", "HangupEnable", "ConsultThreeWayCallEnable", "ConsultTransferEnable","StopConsultEnable"],

	
	//Local方式的toolbar状态
	Local_stInvalid: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleEnable", "RestDisable", "BusyDisable"],
	Local_stBusy: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestDisable", "BusyEnable"],
	Local_stRest: ["DialEnable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable", "IdleDisable", "RestEnable", "BusyDisable"],
	Local_stDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stDialTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable","ConsultEnable"],
	Local_stHold:["DialDisable", "HangupDisable", "HoldGetEnable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stAbate: ["DialDisable", "HangupDisable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	Local_stInnerDialing: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stInnerTalking: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stBelling: ["DialDisable", "HangupEnable", "HoldDisable", "ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stTalking: ["DialDisable", "HangupEnable", "HoldEnable", "ThreeWayCallDisable", "TransferEnable", "ConsultEnable"],
	Local_stListening: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stListened: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stInnerBelling: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable", "ConsultDisable"],
	Local_stThreeWayTalking: ["DialDisable", "HangupEnable", "HoldDisable","ThreeWayCallDisable", "TransferDisable","ConsultDisable"],
	Local_stConcultTalking: ["DialDisable", "HangupEnable", "ConsultThreeWayCallEnable", "ConsultTransferEnable","StopConsultEnable"],
	
    
    onPeerToolBarStateChanged: function(peerState) {
    	if(this._phone._peerState._curPeerStateKey == "99") {
			if(this._phone.autoBusyTime != "0") {
				if(this._phone.autoBusyTime == undefined) {
					return;
				}
				this.onAutoBusyTime(this._phone.autoBusyTime);
				return;
			}
    	}	
    	var state = this._phone._peerState._callStateValue[this._phone._peerState._curPeerStateKey]; 
    	this.onChangeState(state);
    	
    	if(this._srcNodePeerStateRef != null) {
    		this._srcNodePeerStateRef.innerHTML= "<span style=color:"+this._callStateColor[this._phone._peerState._curPeerStateKey]+">" + this._phone._peerState._get(peerState).value + "</span>";
    		if(peerState != "99") {
    			hojo.publish("EvtPeerStatusChanged", [peerState]);	
    		}
		}
		
    	//time status
		if(this._srcNodePeerTimeStateRef != null) {
			this._showTimer();
		}
		
	},
	
	onChangeState: function(state) {
	    var type = "sip";
	    var extenType = this._phone.extenType; 
	    if (extenType == "gateway" || extenType == "phone") {
	    	type = "gateway";
	    } else if (extenType == "Local") {
	    	type = "Local";
	    } 
	    var curState = type + "_" + state;
	    this._render(curState);
	},

	onCallToolBarStateChanged: function(state) {
		//toolbar status
		this.onChangeState(state);
		//peer status
		if(this._srcNodePeerStateRef != null && state != "stInvalid") {
			this._srcNodePeerStateRef.innerHTML = "<span style=color:"+this._callStateColor["call"]+">" + this._callStateDescription[state] + "</span>";
			hojo.publish("EvtCallStatusChanged", [state]);
		}
		//time status
		if(this._srcNodePeerTimeStateRef != null) {
			this._showTimer();
		}
		
		//hid peer status
		var peerStatus = hojo.byId('peerStatus');
		if(peerStatus == null) {
			return;
		}
		
		if (state == "stInvalid") {
			hojo.byId("icallcenter.dialout.input").disabled = false;
			this.onPeerToolBarStateChanged(this._phone._peerState._curPeerStateKey);
		} else {
			hojo.byId("icallcenter.dialout.input").disabled = true;
		}
	},
	
	_showTimer: function() {
		var self = this;
	    self._peerSecond = "1";
	    self._peerMinute = "0";
	    self._peerHour = "0";
	    if (self._peerCalculagraph != null) {
            window.clearInterval(self._peerCalculagraph);
        }
	    self._peerCalculagraph = window.setInterval(function () {
            self._srcNodePeerTimeStateRef.innerHTML = ((self._peerHour < 10) ? ("0" + self._peerHour) : self._peerHour) + ":" + ((self._peerMinute < 10) ? ("0" + self._peerMinute) : self._peerMinute) + ":" + ((self._peerSecond < 10) ? ("0" + self._peerSecond) : self._peerSecond);
            self._peerSecond++;
            if (self._peerSecond == 60) {
                self._peerMinute++;
                self._peerSecond = 0;
            }
            if (self._peerMinute == 60) {
                self._peerHour++;
                self._peerMinute = 0;
            }
        }, 1000);
	},
	
    _render: function (state) {
        var self = this;
        hojo.query("a", this._srcNodeRef).forEach(
	    		function (aElement) {
	    		    if (hojo.indexOf(self[state], aElement.id) < 0) {
	    		        aElement.style.display = "none";
	    		    } else {
	    		        aElement.style.display = "";
	    		    }
	    		}
	        );
    },
	
	onAutoBusyTime: function(autoBusyTime) {
    	if(this._srcNodePeerStateRef != null) {
			this._srcNodePeerStateRef.innerHTML = this._phone._peerState._get("99").value;
		}

    	var self = this;
		if(self._peerCalculagraph != null) {
			window.clearInterval(self._peerCalculagraph);
		}
		
		self._peerCalculagraph = window.setInterval(function(){
    		if(autoBusyTime >= 60*60) {
    			self._peerHour = parseInt(autoBusyTime / (60*60));
    			self._peerMinute = parseInt((autoBusyTime - self._peerHour*(60*60)) / (60));
    			self._peerSecond = autoBusyTime - self._peerHour*(60*60) - self._peerMinute* (60);
    		} else if(autoBusyTime >= 60 && (autoBusyTime < 60*60)) {
    			self._peerHour = "0";
    			self._peerMinute = parseInt(autoBusyTime / 60);
    			self._peerSecond = autoBusyTime - self._peerMinute* 60; 
    		} else if(autoBusyTime < 60) {
    			self._peerHour = "0";
    			self._peerMinute = "0";
    			self._peerSecond = autoBusyTime;
    		}
    		if(self._peerHour < 0) {
    			self._peerHour = 0;
    		} 
    		if(self._peerMinute < 0) {
    			self._peerMinute = 0;
    		}
    		if(self._peerSecond < 0) {
    			self._peerSecond = 0;
    		}
    		self._srcNodePeerTimeStateRef.innerHTML = ((self._peerHour<10)?("0"+self._peerHour):self._peerHour) + ":" + ((self._peerMinute<10)?("0"+self._peerMinute):self._peerMinute) + ":" + ((self._peerSecond<10)?("0"+self._peerSecond):self._peerSecond);
            autoBusyTime--;
		}, 1000);
	},
	
    dialout: function (phoneNum) {
        if (/^\d+$/.test(phoneNum)) {
            this._phone.dialout(phoneNum);
            return true;
        } else {
        	icallcenter.hojotools.error("请输入正确的电话号码");
            return false;
        }
    },
	    
    toTransfer: function() {
    	icallcenter.hojotools.input("transfer");
	},

	toConsult: function() {
    	icallcenter.hojotools.input("consult");
    },
	
    exTransfer: function(phoneNum) {
     	if (/^\d+$/.test(phoneNum)) {
     		icallcenter.hojotools.close('softphonebar'); 
            phone.transfer("9" + phoneNum, "external", {});
     	 } else {
     		icallcenter.hojotools.error("请输入正确的电话号码");
     	 }       
    },
    
    exConsult: function(phoneNum) {
    	if (/^\d+$/.test(phoneNum)) {
    		icallcenter.hojotools.close('softphonebar'); 
    		phone.consult("9" + phoneNum, "external");
    	 } else {
    		 icallcenter.hojotools.error("请输入正确的电话号码");
    	 }       
    }

});
