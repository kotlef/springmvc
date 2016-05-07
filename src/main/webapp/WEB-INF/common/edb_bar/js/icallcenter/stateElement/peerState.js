hojo.provide("icallcenter.stateElement.peerState");

hojo.declare("icallcenter.stateElement.peerState", null, {
	_callStateValue: [],
	
	constructor: function(base) {
		this._base = base;
		if(this._states == '') {
			  var self = this;
			  hojo.forEach(self._base._phone.PhonebarConfig.split(","), function(items) {
				   self._put(items.split(":")[0],items.split(":")[1]);
	          });
		}

		this._callStateValue["0"] = "stInvalid";
		this._callStateValue["1"] = "stBusy";
		this._callStateValue["2"] = "stRest";
		this._callStateValue["99"] = "stSystemBusy";
		
	},
	
	//根状态
	_base: null,
	
	_curPeerStateKey: "0",
	
	//状态集合
	_states: new Array(),
	
	_changeToolBarState: function(obj) {
		hojo.publish("EvtPeerToolBarChange",[obj._curPeerStateKey]);
	},
	
	_setPeerState: function(curPeerStateKey) {
		this._curPeerStateKey = curPeerStateKey;
	},
	
	_put: function(_key,_value) {
		this._states.push({key:_key,value:_value});
	},
	
	_get: function(_key) {
		try{
			for(i = 0;i< this._states.length;i++) {
				if(this._states[i].key == _key) {
					return this._states[i];
				}
			}
		}catch(e) {
			return null;
		}
	},
	
	//转座席状态
	_switchPeerState: function(evtJson) {
		if(evtJson.Event == "UserStatus") {
			if(this._base._phone.userId == evtJson.UserID) {
				this._base._curPeerState = this._base._getPeerState();
				this._base._curPeerState._setPeerState(evtJson.BusyType);
				if(this._base._curCallState != null) {
					if(this._base._curCallState._callState == "stInvalid") {
						this._changeToolBarState(this._base._curPeerState);		
					}
				}
				
			}
		} else if(evtJson.Event == "UserBusy") {
			if(this._base._phone.userId == evtJson.UserID) {
				this._base._curPeerState = this._base._getPeerState();
				this._base._curPeerState._setPeerState(evtJson.BusyType);
				if(this._base._curCallState != null) {
					if(this._base._curCallState._callState == "stInvalid") {
						this._changeToolBarState(this._base._curPeerState);		
					}
				}
			}
		}
		
	},

	_publish:function() {
		alert("开始抛事件busy");
	}

});