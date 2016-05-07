hojo.provide("icallcenter.stateElement.ringring.consultationRinging");

hojo.declare("icallcenter.stateElement.ringring.consultationRinging", null, {
	constructor: function(base) {
		//console.debug("进入到咨询振铃状态");
		this._base = base;
	},

	//根状态
	_base: null,
	
	_changeState: function() {
		//console.debug("改变咨询振铃状态");
	},
	
	_switchState: function(evtJson) {
		//console.debug("开始转换状态");
		if(evtJson.Event == "ChannelStatus") {
			if(evtJson.ChannelStatus == "Hangup") {
				//console.debug("挂机事件到达");
				this._base._curCallState = this._base._getInvalid();
			} else if(evtJson.ChannelStatus == "Link") {
				if(evtJson.LinkedChannel.ChannelType == "consultation") {
					this._base._curCallState = this._base._getConsultationLink();
				} 
			}
		}
	},


	_publish:function() {
		//console.debug("开始抛事件");
	}

});