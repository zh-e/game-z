"use strict"

/**
 * 创建Vue实例
 */
var Vm = new Vue({
    el: "#root",
    data: {
        consoleData: [], // 控制台日志
        messageData: [], // 消息记录
        instance: WebSocket, // ws instance
        address: 'ws://127.0.0.1:9501', // 链接地址
        alert: {
            class: 'success',
            state: false,
            content: '',
            timer: undefined
        },
        content: '',
        heartBeatSecond: 1,
        heartBeatContent: {
            sn: 1
        },
        cmd: 0,
        autoSend: false,
        autoTimer: undefined,
        sendClean: false,
        recvClean: false,
        recvDecode: false,
        connected: false,
        recvPause: false
    },
    created: function created () {
        this.canUseH5WebSocket()
        var address = localStorage.getItem('address');
        if (typeof address === 'string') this.address = address
        window.onerror = function (ev) {
            console.warn(ev)
        }
    },
    filters: {
        rStatus: function (value) {
            switch (value) {
                case undefined:
                    return '尚未创建'
                case 0 :
                    return '尚未开启'
                case 1:
                    return '连接成功'
                case 2:
                    return '正在关闭'
                case 3:
                    return '连接关闭'
            }
        }
    },
    methods: {
        showTips: function showTips (className, content) {
            clearTimeout(this.alert.timer);
            this.alert.state   = false;
            this.alert.class   = className;
            this.alert.content = content;
            this.alert.state   = true;
            this.alert.timer   = setTimeout(function () {
                Vm.alert.state = false;
            }, 3000);
        },
        autoWsConnect: function () {
            try {
                if (this.connected === false){
                    localStorage.setItem('address', this.address)
                    var wsInstance = new WebSocket(this.address);
                    var _this      = Vm
                    wsInstance.binaryType = 'arraybuffer'
                    wsInstance.onopen    = function (ev) {
                        console.warn(ev)
                        _this.connected = true
                        var service     = _this.instance.url.replace('ws://', '').replace('wss://', '');
                        service         = (service.substring(service.length - 1) === '/') ? service.substring(0, service.length - 1) : service;
                        _this.writeAlert('success', 'OPENED => ' + service);
                    }
                    wsInstance.onclose   = function (ev) {
                        console.warn(ev)
                        _this.autoSend = false;
                        clearInterval(_this.autoTimer);
                        _this.connected = false;
                        _this.writeAlert('danger', 'CLOSED => ' + _this.closeCode(ev.code));
                    }
                    wsInstance.onerror   = function (ev) {
                        console.warn(ev)
                        _this.writeConsole('danger', '发生错误 请打开浏览器控制台查看')
                    }
                    wsInstance.onmessage = function (ev) {
                        if (!_this.recvPause) {
                            let data = _this.arrayBufferToData(ev.data);

                            if (_this.recvClean)
                                _this.messageData = [];

                            if (data.cmd === 1000) {
                                _this.writeConsole('info', '收到消息:' + JSON.stringify(data))
                            } else {
                                _this.writeNews(0, data);
                            }

                        }
                    }
                    this.instance = wsInstance;
                }else {
                    this.instance.close(1000, 'Active closure of the user')
                }
            } catch (err) {
                console.warn(err)
                this.writeAlert('danger', '创建 WebSocket 对象失败 请检查服务器地址')
            }
        },
        autoHeartBeat: function () {
            var _this = Vm
            if (_this.autoSend === true) {
                _this.autoSend = false;
                clearInterval(_this.autoTimer);
            } else {
                _this.autoSend  = true
                _this.autoTimer = setInterval(function () {
                    _this.writeConsole('info', '循环发送: ' + JSON.stringify(_this.heartBeatContent))
                    _this.instance.send(Vm.dataToArrayBuffer(1000, _this.heartBeatContent))
                }, _this.heartBeatSecond * 1000);
            }
        },
        writeConsole: function (className, content) {
            this.consoleData.push({
                content: content,
                type: className,
                time: moment().format('HH:mm:ss')
            });
            this.$nextTick(function () {
                Vm.scrollOver(document.getElementById('console-box'));
            })
        },
        writeNews: function (direction, content, callback) {
            if (typeof callback === 'function') {
                content = callback(content);
            }

            this.messageData.push({
                direction: direction,
                content: content,
                time: moment().format('HH:mm:ss')
            });

            this.$nextTick(function () {
                if (!Vm.recvClean) {
                    Vm.scrollOver(document.getElementById('message-box'));
                }
            })
        },
        writeAlert: function (className, content) {
            this.writeConsole(className, content);
            this.showTips(className, content);
        },
        canUseH5WebSocket: function () {
            if ('WebSocket' in window) {
                this.writeAlert('success', '初始化完成')
            }
            else {
                this.writeAlert('danger', '当前浏览器不支持 H5 WebSocket 请更换浏览器')
            }
        },
        closeCode: function (code) {
            var codes = {
                1000: '1000 CLOSE_NORMAL',
                1001: '1001 CLOSE_GOING_AWAY',
                1002: '1002 CLOSE_PROTOCOL_ERROR',
                1003: '1003 CLOSE_UNSUPPORTED',
                1004: '1004 CLOSE_RETAIN',
                1005: '1005 CLOSE_NO_STATUS',
                1006: '1006 CLOSE_ABNORMAL',
                1007: '1007 UNSUPPORTED_DATA',
                1008: '1008 POLICY_VIOLATION',
                1009: '1009 CLOSE_TOO_LARGE',
                1010: '1010 MISSING_EXTENSION',
                1011: '1011 INTERNAL_ERROR',
                1012: '1012 SERVICE_RESTART',
                1013: '1013 TRY_AGAIN_LATER',
                1014: '1014 CLOSE_RETAIN',
                1015: '1015 TLS_HANDSHAKE'
            }
            var error = codes[code];
            if (error === undefined) error = '0000 UNKNOWN_ERROR 未知错误';
            return error;
        },
        sendData: function (raw) {
            var _this = Vm
            var data  = raw
            if (_this.cmd === '') {
                _this.writeAlert('danger', '消息CMD不可为空');
                return;
            }
            console.log(typeof data === 'object');
            if (typeof data === 'object') {
                data = _this.content;
            }
            try {
                _this.instance.send(Vm.dataToArrayBuffer(_this.cmd, data));
                _this.writeNews(1, data);
                if (_this.sendClean && typeof raw === 'object') {
                    _this.cmd = '';
                    _this.content = '';
                }
            } catch (err) {
                _this.writeAlert('danger', '消息发送失败 原因请查看控制台');
                throw err;
            }
        },
        scrollOver: function scrollOver (e) {
            if (e) {
                e.scrollTop = e.scrollHeight;
            }
        },
        cleanMessage: function () {
            this.messageData = [];
        },

        dataToArrayBuffer: function (cmd, data) {
            if (typeof data !== "object") {
                data = JSON.parse(data);
            }
            let dataArray = this.stringToByteArray(JSON.stringify(data));
            let arrayBuffer = new ArrayBuffer(10 + dataArray.length);
            let dataView = new DataView(arrayBuffer);
            dataView.setInt32(0, 6 + dataArray.length);
            dataView.setInt8(4, 0);
            dataView.setInt32(5, cmd);
            dataView.setInt8(9, 0);
            for (let i = 0, len = dataArray.length; i < len; i++) {
                dataView.setUint8(10 + i, dataArray[i]);
            }
            return arrayBuffer;
        },

        stringToByteArray: function (str) {
            let code = encodeURIComponent(str);
            let bytes = [];
            for (let i = 0; i < code.length; i++) {
                const c = code.charAt(i)
                if (c === '%') {
                    let hex = code.charAt(i + 1) + code.charAt(i + 2);
                    let hexVal = parseInt(hex, 16);
                    bytes.push(hexVal);
                    i += 2;
                } else {
                    bytes.push(c.charCodeAt(0))
                }
            }
            return bytes;
        },

        arrayBufferToData: function (data) {
            if (!data) {
                return null;
            }
            let dataView = new DataView(data);
            let byteArray = [];
            for (let i = 10, len = data.byteLength; i < len; i++) {
                byteArray.push(dataView.getUint8(i));
            }

            let cmdKey = dataView.getInt32(5);
            let jsonData = this.byteArrayToString(byteArray);
            if (jsonData.length > 0) {
                jsonData = JSON.parse(jsonData);
            }
            return {
                cmd: cmdKey,
                data: jsonData
            };
        },

        byteArrayToString: function (bytes) {
            let str = "";
            for (let i = 0; i < bytes.length; i++) {
                str += '%' + bytes[i].toString(16);
            }
            return decodeURIComponent(str);
        }

    }
});