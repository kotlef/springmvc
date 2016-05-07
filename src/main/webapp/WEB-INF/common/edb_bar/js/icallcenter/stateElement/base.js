hojo.provide("icallcenter.stateElement.base");

hojo.declare("icallcenter.stateElement.base", null , {
	constructor: function(phone) {
		//console.debug("进入到根状态");
		this._phone = phone;
	},
	
	//软电话状态
	_phone: null,
	
	//座席状态
	_curPeerState: null,
	
	//通话状态
	_curCallState: null,
	_oldCurCallState: null,
	
	_getHold: function() {
		if(this._phone._hold == null) {
			this._phone._hold = new icallcenter.stateElement.hold(this);	
		}
		return this._phone._hold;
	},
	
	_getInvalid: function() {
		if(this._phone._invalid == null) {
			this._phone._invalid = new icallcenter.stateElement.invalid(this);	
		}
		return this._phone._invalid;
	},
	
	_getAbate: function() {
		if(this._phone._abate == null) {
			this._phone._abate = new icallcenter.stateElement.abate(this);	
		}
		return this._phone._abate;
	},
	
	
	_getPeerState: function() {
		if(this._phone._peerState == null) {
			this._phone._peerState = new icallcenter.stateElement.peerState(this);	
		}
		return this._phone._peerState;
	},
	
	_getConsultationLink: function() {
		if(this._phone._consultationLink == null) {
			this._phone._consultationLink = new icallcenter.stateElement.link.consultationLink(this);	
		}
		return this._phone._consultationLink;
	},

	_getInnerLink: function() {
		if(this._phone._innerLink == null) {
			this._phone._innerLink = new icallcenter.stateElement.link.innerLink(this);	
		}
		return this._phone._innerLink;
	},

	_getDialoutLink: function() {
		if(this._phone._dialoutLink == null) {
			this._phone._dialoutLink = new icallcenter.stateElement.link.dialoutLink(this);	
		}
		return this._phone._dialoutLink;
	},

	_getListenLink: function() {
		if(this._phone._listenLink == null) {
			this._phone._listenLink = new icallcenter.stateElement.link.listenLink(this);	
		}
		return this._phone._listenLink;
	},

	_getNormalLink: function() {
		if(this._phone._normalLink == null) {
			this._phone._normalLink = new icallcenter.stateElement.link.normalLink(this);	
		}
		return this._phone._normalLink;
	},

	_getThreeWayCallLink: function() {
		if(this._phone._threeWayCallLink == null) {
			this._phone._threeWayCallLink = new icallcenter.stateElement.link.threeWayCallLink(this);	
		}
		return this._phone._threeWayCallLink;
	},
	
	_getInnerRing: function() {
		if(this._phone._innerRing == null) {
			this._phone._innerRing = new icallcenter.stateElement.ring.innerRing(this);	
		}
		return this._phone._innerRing;
	},
	
	_getNormalRing: function() {
		if(this._phone._normalRing == null) {
			this._phone._normalRing = new icallcenter.stateElement.ring.normalRing(this);	
		}
		return this._phone._normalRing;
	},

	_getConsultationRinging: function() {
		if(this._phone._consultationRinging == null) {
			this._phone._consultationRinging = new icallcenter.stateElement.ringring.consultationRinging(this);	
		}
		return this._phone._consultationRinging;
	},

	_getInnerRinging: function() {
		if(this._phone._innerRinging == null) {
			this._phone._innerRinging = new icallcenter.stateElement.ringring.innerRinging(this);	
		}
		return this._phone._innerRinging;
	},

	_getListenRing: function() {
		if(this._phone._listenRing == null) {
			this._phone._listenRing = new icallcenter.stateElement.ring.listenRing(this);	
		}
		return this._phone._listenRing;
	},
	
	_getNormalRinging: function() {
		if(this._phone._normalRinging == null) {
			this._phone._normalRinging = new icallcenter.stateElement.ringring.normalRinging(this);	
		}
		return this._phone._normalRinging;
	},

	_bussiness:function() {
		//console.debug("传递工单");
	},
	
	_switchState:function(evtJson) {
		//console.debug("开始执行根状态");
		//console.dir(evtJson);
		this._setCallObject(evtJson);
		this._setMonitorObjects(evtJson);
		if(evtJson.Event == "PeerStatus") {
			if(evtJson.Exten == this._phone.sipNo) {
        		if(evtJson.PeerStatus == "Registered" && (this._phone.extenType == "gateway" || this._phone.extenType == "sip")) {
        			if(this._curCallState == null) {
            			this._curCallState = this._getInvalid();
            			this._curCallState._changeToolBarState(this._curCallState);
        			} else if(this._curCallState._callState == "stAbate") {
        				this._curCallState = this._getInvalid();
        				this._curCallState._changeToolBarState(this._curCallState);
        			}
        		} else if(evtJson.PeerStatus != "Registered" && (this._phone.extenType == "gateway" || this._phone.extenType == "sip")) {
        			this._curCallState = this._getAbate();
        			this._curCallState._changeToolBarState(this._curCallState);
        		}
			}
		} else {
			if(this._curPeerState == null) {
				this._curPeerState = this._getPeerState();
			} 
			this._curPeerState._switchPeerState(evtJson);

			if(this._curCallState == null) {
				this._curCallState = this._getInvalid();	
				if(this._phone.extenType == "Local") {
					this._curCallState._changeToolBarState(this._curCallState);
				}
			}
			this._curCallState._switchCallState(evtJson);
			
		}
				
	},
	
	//通话变量的定义
	_setCallObject: function(evtJson) {
		if (evtJson.Event == "ChannelStatus") {
			if(evtJson.Exten == this._phone.sipNo) {
				if(evtJson.ChannelStatus == "Ring"){
					this._phone._curChannel = evtJson.Channel;
					if(evtJson.ChannelType == "listen"){
						
					} else if(evtJson.ChannelType == "dialout") {
						var callsheetid = "";
						if(evtJson.Data.CallSheetID) {
							callsheetid = evtJson.Data.CallSheetID;
						}
						this._phone.callObject = {
	        		        	callSheetId: callsheetid,
	    		                originCallNo: evtJson.FromCid,
	    		                originCalledNo: evtJson.FromDid,
	    		                callType: "dialout",
	    		                offeringTime: icallcenter.hojotools.dateParse(new Date(evtJson.Timestamp * 1000)),
	    		                data: evtJson.Data,
	    		        		status: "notDeal",
	    		        		monitorFilename: ""       
        				};
						
						if(this._phone.dialoutData){
	        		        	this._phone.callObject.data = this._phone.dialoutData;
	        		        	this._phone.dialoutData = null;
						} 
						hojo.publish("EvtDialing", [this._phone.callObject]);
					}
					
					
					
				} else if(evtJson.ChannelStatus == "Ringing"){
					this._phone._curChannel = evtJson.Channel;
					this._phone._otherChannel = evtJson.LinkedChannel.Channel;
					if(evtJson.LinkedChannel.ChannelType == "dialTransfer"
        				||evtJson.LinkedChannel.ChannelType == "transfer"){
						if(evtJson.Link){
							 var linkedChannel = evtJson.LinkedChannel;
	           				 var callsheetid = "";
	           				 if(linkedChannel.Data && linkedChannel.Data.CallSheetID)
	           					 callsheetid = linkedChannel.Data.CallSheetID;
	           				 this._phone._callId = linkedChannel.Uniqueid;
	           				 this._phone.callObject = {
	     	                		callSheetId: callsheetid,
	     	                        originId: linkedChannel.Uniqueid,
	     	                        originCallNo: linkedChannel.FromCid,
	     	                        originCalledNo: linkedChannel.FromDid,
	     	                        callType: linkedChannel.ChannelType,
	     	                        queue: linkedChannel.Queue,
	     	                        location: linkedChannel.Location,
	     	                        callId: linkedChannel.Uniqueid,
	     	                        skillgroupNo: linkedChannel.Queue,
	     	                        monitorFilename: "",
	     	                        offeringTime: icallcenter.hojotools.dateParse(new Date(evtJson.Timestamp * 1000)),
	     	                        data: {},

	     	                        agent: evtJson.Data.Agent,
  	           				 		status: "notDeal",
  	           				 		beginTime: "",
            	                    endTime: ""
	     	                    };
    	     	                if (linkedChannel.Data) {
    	     	                    //console.debug("当前振铃的同步数据");
    	     	                    //console.dir(linkedChannel.Data);
    	     	                    this._phone.callObject.data = linkedChannel.Data;
    	     	                    this._phone.callObject.data.callSheetId = callsheetid;
    	     	                }
	     	                    //console.dir(this._phone.callObject);
	     	                    hojo.publish("EvtRing", [this._phone.callObject]);
						}
					}
					if(evtJson.LinkedChannel.ChannelType == "normal") {
						if (evtJson.Link) {
							 var linkedChannel = evtJson.LinkedChannel;
                             console.info("_setCallObject============")
	            	         console.dir(linkedChannel);
	            	         if(this._phone._callId != linkedChannel.Uniqueid) {
	            	        	 this._phone._callId = linkedChannel.Uniqueid;
	   	           				 var callsheetid = "";
	   	           				 if(linkedChannel.Data && linkedChannel.Data.CallSheetID) {
	   	           					 callsheetid = linkedChannel.Data.CallSheetID;
	   	           				 }
	   	           				 this._phone.callObject = {
            	                		callSheetId: callsheetid,
            	                        originId: linkedChannel.Uniqueid,
            	                        originCallNo: linkedChannel.FromCid,
            	                        originCalledNo: linkedChannel.FromDid,
            	                        callType: linkedChannel.ChannelType,
            	                        callId: linkedChannel.Uniqueid,
            	                        queue: linkedChannel.Queue,
            	                        location: linkedChannel.Location,
            	                        skillgroupNo: linkedChannel.Queue,
            	                        monitorFilename: "",
            	                        offeringTime: icallcenter.hojotools.dateParse(new Date(evtJson.Timestamp * 1000)),
            	                        data: {},
            	                        beginTime: "",
            	                        endTime: "",
            	                        agent: evtJson.Data.Agent,
                                        ivrkey: linkedChannel.Data.IVRKEY,
                                        callerCity: linkedChannel.CallerCity,
                                        callerProvince: linkedChannel.CallerProvince,
                                        queueName: linkedChannel.QueueName,
  	           				 			status: "notDeal"
            	                 };
		   	           			 if (linkedChannel.Data) {
		           	            	  //console.debug("当前振铃的同步数据");
		   	     	                  //console.dir(linkedChannel.Data);
		   	     	                  this._phone.callObject.data = linkedChannel.Data;
		   	     	                  this._phone.callObject.data.callSheetId = callsheetid;
		   	           			 }
		   	           			 //console.dir(this._phone.callObject);
			   	           		 hojo.publish("EvtRing", [this._phone.callObject]);
	        	                 //console.debug("after ring");
	        	                 //console.dir(this._phone.callObject.data);
	            	         }
						}
					}
					
        			if(this._phone._isLooting){
        				this._phone._isLooting = false;
        				
        			}

					this._phone._curChannel = evtJson.Channel;
					
				} else if(evtJson.ChannelStatus == "Link") {
					this._phone._curChannel = evtJson.Channel;
					//console.debug("Link被叫通道号[%s], 当前通道号[%s]", this._phone._otherChannel, this._phone._curChannel);
					var linkedChannel = evtJson.LinkedChannel;
					this._phone._otherChannel = linkedChannel.Channel;
					this._phone.callObject.callType = evtJson.ChannelType;
	                if (!this._phone.callObject.beginTime) {
	                	this._phone.callObject.beginTime = icallcenter.hojotools.dateParse(new Date(evtJson.Timestamp * 1000));
	                }
	  				var callsheetid = "";
	      			if(linkedChannel.Data && linkedChannel.Data.CallSheetID) {
	      				callsheetid = linkedChannel.Data.CallSheetID;
	      			}
	      			this._phone.callObject.originCallNo = linkedChannel.FromCid;
	      			this._phone.callObject.originCalledNo = linkedChannel.FromDid;
	      			this._phone.callObject.callSheetId = callsheetid;
	      			this._phone.callObject.originId = linkedChannel.Uniqueid;
	      			this._phone.callObject.queue = linkedChannel.Queue;
	      			this._phone.callObject.location = linkedChannel.Location;
	      			this._phone.callObject.callId = linkedChannel.Uniqueid;
	      			this._phone.callObject.skillgroupNo = linkedChannel.Queue;
	      			
	      			this._phone.callObject.status = "dealing";
	                if(evtJson.RingTime) {
	                	this._phone.callObject.offeringTime = icallcenter.hojotools.dateParse(new Date(evtJson.RingTime * 1000));                	
	                }
	                
	                if (linkedChannel.Data) {
	                	//console.dir(linkedChannel.Data);
	                	this._phone.callObject.data = linkedChannel.Data;
	                	this._phone.callObject.data.callSheetId = callsheetid;
	                }
	                hojo.publish("EvtConnected", [this._phone.callObject]);
				} else if(evtJson.ChannelStatus == "Unlink") {
					this._phone._curChannel = evtJson.Channel;
					this._phone._callId = "";
	    		} else if(evtJson.ChannelStatus == "Hangup") {
                    console.info("Hangup============")
                    console.dir(evtJson);
	    			this._phone._curChannel = evtJson.Channel;
	    			this._phone._callId = "";
	    			//console.debug("挂机事件到达，对应的通道号: [%s], 当前状态: [%s]",evtJson.Channel, evtJson.C5Status);
	    			if(this._phone._curChannel == evtJson.Channel) {
	        			if (evtJson.ChannelType == "normal" || evtJson.ChannelType == "dialout"
	                      	 || evtJson.ChannelType == "dialTransfer"
	                      	 || evtJson.ChannelType == "transfer"
	                      	 || evtJson.ChannelType == "webcall" || evtJson.ChannelType == "inner") {
	                        this._phone.callObject.endTime = icallcenter.hojotools.dateParse(new Date(evtJson.Timestamp * 1000));
	                        this._phone.callObject.ringTime = icallcenter.hojotools.dateParse(new Date(evtJson.Data.RingTime * 1000));
	                        
	                        if(evtJson.ChannelType == "dialout" || evtJson.ChannelType == "dialTransfer") {
	                        	this._phone.callObject.data = evtJson.Data;
	                        }
	                        
	                        hojo.publish("EvtHangup", [this._phone.callObject]);
	           			} else if (evtJson.ChannelType == "listen") {
		                   	 //console.debug("监听通话结束");
		                   	 this._phone._otherChannel = "";
		                   	 hojo.publish("EvtEndListen", []);  
	           			}
	        			
	    			}
	    			
	    		}
				
			}
		}
	},
	
	
	//监控页面时间抛出处理
	_setMonitorObjects: function(evtJson) {
		if (evtJson.Event == "ChannelStatus") {
        	if(evtJson.ChannelStatus == "Hangup"){
        		if(evtJson.UserID == undefined) {
        			return;	
        		}
    		}

    		var peer = this._getUserViaSipNum(evtJson.Exten);
    		if(!peer){
    			return;
    		}
    		
    		if(evtJson.ChannelStatus == "Down"){
    			peer.callStatus = "Down";
    			peer.channel = evtJson.Channel;
    			this._updateQueueInfo();
    		} else if(evtJson.ChannelStatus == "Ring") {
    			peer.callStatus = "Ring";
                peer.called = false;
                peer.C5Status = evtJson.C5Status;
                peer.timestamp = evtJson.Timestamp;
                peer.channel = evtJson.Channel;
                if(evtJson.C5Status == "OutboundCall"
                    || evtJson.C5Status == "InboundCall"
                    || evtJson.C5Status == "listen"){
                    peer.callNo = evtJson.Data.ListenExten;
                } else if(evtJson.FromDid)
                    peer.callNo = evtJson.FromDid;
                hojo.publish("EvtMonitorPeer",[peer]);
    		} else if(evtJson.ChannelStatus == "Ringing"){
    			peer.called = true;
    			peer.callStatus = "Ringing";
    			peer.C5Status = evtJson.C5Status;
    			peer.channel = evtJson.Channel;
    			peer.linkedChannel = evtJson.LinkedChannel.Channel;
    			if(evtJson.ChannelType == "dialTransfer"){
    				peer.callNo = evtJson.FromDid;
    			} else {
    				peer.callNo = evtJson.FromCid;
    			}
    			peer.timestamp = evtJson.Timestamp;
    			hojo.publish("EvtMonitorPeer",[peer]);
    		} else if(evtJson.ChannelStatus == "Up") {
    			if(evtJson.ChannelType == "listen"){
    				peer.callNo = evtJson.Data.ListenExten;
    				peer.timestamp = evtJson.Timestamp;
    				peer.C5Status = evtJson.C5Status;
    				peer.callStatus = evtJson.ChannelType;
    				peer.linked = true;
    				peer.channel = evtJson.Channel;
    				hojo.publish("EvtMonitorPeer",[peer]);
    			}
    		} else if(evtJson.ChannelStatus == "Link") {
    			peer.timestamp = evtJson.Timestamp;
    			peer.C5Status = evtJson.C5Status;
    			linked = true;
    			peer.channel = evtJson.Channel;
    			peer.linkedChannel = evtJson.LinkedChannel.Channel;
    			peer.callStatus = evtJson.ChannelType;
    			if(evtJson.ChannelType == "dialout" || evtJson.ChannelType == "dialTransfer") {
    				peer.callNo = evtJson.LinkedChannel.FromDid;
    			} else {
    				peer.callNo = evtJson.LinkedChannel.FromCid;
    			}
    			hojo.publish("EvtMonitorPeer",[peer]);
    		} else if(evtJson.ChannelStatus == "Unlink") {
    			
    		} else if(evtJson.ChannelStatus == "Hangup"){
    			if(peer.channel == evtJson.Channel) {
        			//如果是监听中的通话挂断，需要结束监听者的通话
        			if(this._phone._otherChannel == evtJson.Channel
        					&& (this._curCallState._callState == "stListening" ||this._curCallState._callState == "stListened")) {
        				this._phone.hangup();
        			}
        			peer.C5Status = evtJson.C5Status;
        			peer.callNo = "";
        			peer.callStatus = "Idle";
        			peer.timestamp = evtJson.Timestamp;
        			linked = false;
    				peer.channel = "";
    				peer.linkedChannel = "";
    				hojo.publish("EvtMonitorPeer",[peer]);
    			}
    			this._updateQueueInfo();
    		}
        }
        
        else if(evtJson.Event == "QueueParams") {
        	//removed or update
        	//如果找到这个queue，更新这个对象里面的属性
        	//如果没有找到，新建一个，加入到queue[]
        	//把这个变化了的对象publish给界面更新
        	var queueItem={};
        	queueItem=this._queryQueueItem(evtJson);
        	if(queueItem) {
        		if(evtJson.Removed) {
         			queueItem.removed = true;
         		}
         		queueItem.queueName = evtJson.DisplayName;
         		queueItem.idleAgentCount=evtJson.Members - evtJson.BusyMembers;
         		queueItem.busyAgentCount=evtJson.BusyMembers;
         		queueItem.totalAgentCount=evtJson.Members;
         		queueItem.queueWaitCount=evtJson.Calls;
         		queueItem.abadonedCalls=evtJson.Abandoned;
         		queueItem.totalCalls=evtJson.TotalCalls;
         		queueItem.DisplayName = evtJson.DisplayName;
         		queueItem.members=[];
	        	for(var i in evtJson.QueueMember){
	        		var member = evtJson.QueueMember[i];
	        		queueItem.members[member] = member;
	        	}
         		hojo.publish("EvtMonitorQueue",[queueItem]);
        	} else {
        		queueItem = {
	        		queueName:evtJson.DisplayName,
	        		queueId:evtJson.Queue,
	        		idleAgentCount:evtJson.Members - evtJson.BusyMembers,
	        		busyAgentCount:evtJson.BusyMembers,
	        		totalAgentCount:evtJson.Members,
	        		queueWaitCount:evtJson.Calls,
	        		abadonedCalls:evtJson.Abandoned,
	        		DisplayName: evtJson.DisplayName,
	        		totalCalls:evtJson.TotalCalls,
	        		members:[],
	        		removed:false
        		};
        		for(var i in evtJson.QueueMember){
	        		var member = evtJson.QueueMember[i];
	        		queueItem.members[member] =member; //change
	        	}
				this._phone.monitorQueues[evtJson.Queue]= queueItem;
        	}
        	this._updateQueueInfo();
        } else if(evtJson.Event == "QueueMemberAdded") {
        	//座席签入
            //queue.member +1;
            //把这个变化了的对象publish给界面更新
        	var queueItem=this._queryQueueItem(evtJson);
        	if(queueItem) {
        		if(!queueItem.members[evtJson.Exten]){
        			queueItem.members[evtJson.Exten] = evtJson.Exten;  //change
            		queueItem.totalAgentCount++;
            		this._updateQueueInfo();
        		}
        	} else {
        		//console.debug('无此队列');	
        	}
        } else if(evtJson.Event == "QueueMemberRemoved"){
        	//座席签出
        	//queue.member -1;
        	//把这个变化了的对象publish给界面更新
        	var queueItem=this._queryQueueItem(evtJson);
        	if(queueItem){
    			if(queueItem.members[evtJson.Exten]){
    				delete queueItem.members[evtJson.Exten];
        			queueItem.totalAgentCount--;
        			this._updateQueueInfo();
    			} 
        	}else{
        		//console.debug('无此队列');	
        	}
        } else if(evtJson.Event == "QueueMemberPaused"){

        } else if(evtJson.Event == "Join"){
        	//来电
         	//如果找到这个queue，更新这个对象里面的属性
        	//如果没有找到，不处理
        	//把这个变化了的对象publish给界面更新
        	var queueItem = this._queryQueueItem(evtJson);
        	if(queueItem){
        		queueItem.queueWaitCount++;
        		hojo.publish("EvtMonitorQueue",[queueItem]);
        	} else {
        		//console.debug("找不到队列："+evtJson.Queue);
        	}
        	hojo.publish("EvtQueueEntryAdd", [evtJson]);
        } else if(evtJson.Event == "Leave"){
        	//来电离开：客户取消，被接听
        	var queueItem=this._queryQueueItem(evtJson);
        	if(queueItem){
        		queueItem.totalCalls++;
        		queueItem.queueWaitCount--;
        		if(queueItem.queueWaitCount < 0)
        			queueItem.queueWaitCount = 0;
        		hojo.publish("EvtMonitorQueue",[queueItem]);
        	} else {
        		//console.debug("找不到队列："+evtJson.Queue);
        	}
        	hojo.publish("EvtQueueEntryRemove", [evtJson]);
        } else if(evtJson.Event == "QueueCallerAbandon"){
        	//是客户取消的
        	var queueItem=this._queryQueueItem(evtJson);
        	if(queueItem){
        		queueItem.abadonedCalls++;
        		hojo.publish("EvtMonitorQueue",[queueItem]);
        	} else {
        		//console.debug("找不到队列："+evtJson.Queue);
        	}
        	hojo.publish("EvtQueueEntryRemove", [evtJson]);
        }
        
        //座席
        else if(evtJson.Event == "UserStatus") {
        	var isRegistered = false;
    		if(evtJson.PeerStatus == "Registered") {
    			isRegistered = true;
    		}
    		if(!this._phone.monitorPeers[evtJson.UserID]) {
    			var peer = {
        				exten:evtJson.Exten,
        				sipNo:evtJson.SipNum,
        				name:evtJson.User,
        				DisplayName: evtJson.DisplayName,
        				loginExten:evtJson.LoginExten,
        				peerStatus:evtJson.PeerStatus,
        				status:evtJson.Status,
        				C5Status:evtJson.C5Status,
        				busy:evtJson.Busy,
        				extenType:evtJson.ExtenType,
        				login:evtJson.Login,
        				userId:evtJson.UserID,
        				user:evtJson.User,
        				localNo:evtJson.Local,
        				register: isRegistered,
        				InCalls: evtJson.InCalls,
        				InComplete: evtJson.InComplete,
        				OutCalls: evtJson.OutCalls,
        				OutComplete: evtJson.OutComplete,
        				linked:false,
        				channel:"",
        				linkedChannel:"",
            			called:false,//是否是被呼
            			//Idle, Ring, Ringing, inner, normal, dialout, dialTransfer,transfer, listen, webcall
            			callStatus:"Idle",
        				callNo:"",
        				timestamp:evtJson.Login?(evtJson.BusyTimestamp):"",
        				busyTimestamp:evtJson.BusyTimestamp,
        				loginTimestamp:evtJson.LoginTimestamp,
        				busyType:evtJson.BusyType
        		};
    			//console.debug("处理座席状态");
    			this._phone.monitorPeers[evtJson.UserID] = peer;
    			hojo.publish("EvtMonitorPeer",[peer]);
    		} else {
    			var peer = this._phone.monitorPeers[evtJson.UserID];
    			peer.peerStatus = evtJson.PeerStatus;
    			peer.status = evtJson.Status;
    			peer.exten = evtJson.Exten;
				peer.sipNo = evtJson.SipNum;
    			peer.C5Status = evtJson.C5Status;
    			peer.busy = evtJson.Busy;
    			peer.extenType = evtJson.ExtenType;
    			peer.login = evtJson.Login;
    			peer.loginExten = evtJson.LoginExten;
    			peer.name = evtJson.User;
    			peer.DisplayName = evtJson.DisplayName;
    			peer.userId = evtJson.UserID;
    			peer.user = evtJson.User;
    			peer.localNo = evtJson.Local;
    			peer.register = isRegistered;
    			peer.InCalls = evtJson.InCalls;
    			peer.InComplete = evtJson.InComplete;
    			peer.OutCalls = evtJson.OutCalls;
    			peer.OutComplete = evtJson.OutComplete;
    			peer.busyTimestamp=evtJson.BusyTimestamp;
    			peer.loginTimestamp=evtJson.LoginTimestamp;
    			peer.busyType=evtJson.BusyType;
    			peer.timestamp = peer.login?(peer.busyTimestamp):"";
    			hojo.publish("EvtMonitorPeer",[peer]);
    			this._updateQueueInfo();
    		}	
        }
        
        else if(evtJson.Event == "UserBusy") {
        	if(this._phone.monitorPeers[evtJson.UserID]) {
        		var peer = this._phone.monitorPeers[evtJson.UserID];
        		peer.busy = evtJson.Busy;
        		peer.busyType=evtJson.BusyType;
        		peer.busyTimestamp = evtJson.BusyTimestamp;
        		peer.timestamp = peer.login?(peer.busyTimestamp):"";
        		peer.loginTimestamp=evtJson.LoginTimestamp;
        		hojo.publish("EvtMonitorPeer",[peer]);
    			this._updateQueueInfo();
        	}
        } 
        
//        else if(evtJson.Event == "UserLogin") {
//        	if(this._phone.monitorPeers[evtJson.UserID]) {
//        		var peer = this._phone.monitorPeers[evtJson.UserID];
//        		peer.login = evtJson.Login;
//        		peer.busyTimestamp = evtJson.BusyTimestamp;
//        		peer.timestamp = peer.login?(peer.busyTimestamp):"";
//        		peer.loginTimestamp=evtJson.LoginTimestamp;
//        		peer.sipNo = evtJson.SipNum;
//        		hojo.publish("EvtMonitorPeer",[peer]);
//    			this._updateQueueInfo();
//        	}
//        }
        
        else if(evtJson.Event == "UserCallsUpdate") {
        	if(this._phone.monitorPeers[evtJson.UserID]) {
        		var peer = this._phone.monitorPeers[evtJson.UserID];
        		peer.InCalls = evtJson.InCalls;
    			peer.InComplete = evtJson.InComplete;
    			peer.OutCalls = evtJson.OutCalls;
    			peer.OutComplete = evtJson.OutComplete;
        		hojo.publish("EvtMonitorPeer",[peer]);
    			this._updateQueueInfo();
        	}
        }
        
        else if(evtJson.Event == "UserSignIn") {
        	if(this._phone.monitorPeers[evtJson.UserID]) {
        		var peer = this._phone.monitorPeers[evtJson.UserID];
        		peer.extenType = evtJson.ExtenType;
        		peer.login = evtJson.Login;
        		peer.sipNo = evtJson.SipNum;
        		hojo.publish("EvtMonitorPeer",[peer]);
    			this._updateQueueInfo();
        	}
        }
        
        else if(evtJson.Event == "UserSignOut") {
        	if(this._phone.monitorPeers[evtJson.UserID]) {
        		var peer = this._phone.monitorPeers[evtJson.UserID];
        		peer.extenType = evtJson.ExtenType;
        		peer.sipNo = evtJson.SipNum;
        		peer.login = evtJson.Login;
        		hojo.publish("EvtMonitorPeer",[peer]);
    			this._updateQueueInfo();
        	}
        }
        
        //服务号事件
        else if(evtJson.Event == "TrunkStatus") {
        	if(!this._phone.monitorServiceNos[evtJson.ServiceNo]){
				var serviceNo = {
					serviceNo: evtJson.ServiceNo,
					inCalls: evtJson.InCalls,
					inLost: evtJson.InLost,
					inComplete: evtJson.InComplete,
					outCalls: 0,
					outComplete: 0
				};
				this._phone.monitorServiceNos[evtJson.ServiceNo] = serviceNo;
			} else {
				var serviceNo = this._phone.monitorServiceNos[evtJson.ServiceNo];
				serviceNo.inCalls = evtJson.InCalls,
				serviceNo.inLost = evtJson.InLost,
				serviceNo.inComplete = evtJson.InComplete,
				serviceNo.outCalls = 0,
				serviceNo.outComplete = 0
			}
        	hojo.publish("EvtMonitorServiceNo", [this._phone.monitorServiceNos[evtJson.ServiceNo]]);
        }
        
        else if(evtJson.Event == "PeerStatus") {
        	var isRegistered = false;
    		if(evtJson.PeerStatus == "Registered")
    			isRegistered = true;
			var peer = this._getUserViaSipNum(evtJson.Exten);
			if(peer){
				peer.register = isRegistered;
				peer.status = evtJson.Status;
    			hojo.publish("EvtMonitorPeer",[peer]);
    			//console.dir(peer);
    			this._updateQueueInfo();
			}
        }
        
        // account call status
        else if(evtJson.Event == "AccountStatus"){
        	this._phone.accountCalls = evtJson;
			hojo.publish("EvtAccountStatus", [evtJson]);
        }
        
	},
	
	_getUserViaExten:function(exten) {
		if(!this._phone.monitorPeers) return null;
		for(var i in this._phone.monitorPeers) {
			if(this._phone.monitorPeers[i].exten == exten){
				return this._phone.monitorPeers[i];
			}
		}
		return null;
	},

	
	_queryQueueItem:function(evtJson) {
		//取得该事件是否在队列中
		if(!this._phone.monitorQueues) {
			return null;	
		}
        for(var i in this._phone.monitorQueues) { 
        	if(this._phone.monitorQueues[i].queueId==evtJson.Queue) {
	        	return this._phone.monitorQueues[i];
        	}
    	}		
    	return null;
	},

	_getUserViaSipNum:function(sipNum) {
		if(!this._phone.monitorPeers) return null;
		for(var i in this._phone.monitorPeers) {
			var test = this._phone.monitorPeers[i].sipNo;
			if(this._phone.monitorPeers[i].sipNo == sipNum){
				return this._phone.monitorPeers[i];
			}
		}
		return null;
	},
	
	_getQueueInfo: function() {
		var info = "";
		for(var i in this._phone.monitorQueues) {
			var queue = this._phone.monitorQueues[i];
			if(queue == null) {
				continue;
			}
			var members = queue.members;
			for(var j in members) {
				var peer = this._getUserViaSipNum(members[j]);
				if(peer != null) {
					info += (peer.exten + "," + peer.busyType + ";");	
				}
			}
		}
		return info;  
	},
	
	_updateQueueInfo:function(){
		for(var i in this._phone.monitorQueues) {
			var queue = this._phone.monitorQueues[i];
			var members = queue.members;
			queue.busyAgentCount = 0;
			queue.idleAgentCount = 0;
			for(var j in members){
				var peer = this._getUserViaSipNum(members[j]);
				if(peer){
					//座席忙碌
					if(peer.extenType == "sip"){
						if(!peer.register
								|| !peer.login
								|| peer.busy
								|| peer.callStatus != "Idle"){
							queue.busyAgentCount++;
						}else {
							queue.idleAgentCount++;
						}
					} else if(peer.extenType == "gateway"){
						if(!peer.register
								|| peer.busy
								|| peer.callStatus != "Idle"){
							queue.busyAgentCount++;
						}else {
							queue.idleAgentCount++;
						}
					} else if(peer.extenType == "Local"){
						if(peer.busy
								|| peer.callStatus != "Idle"){
							queue.busyAgentCount++;
						} else {
							queue.idleAgentCount++;
						}
					} else {
						queue.busyAgentCount++;
					}
				} else {
					queue.idleAgentCount++;
				}
			}
			hojo.publish("EvtMonitorQueue",[queue]);
		}
	}
	
});