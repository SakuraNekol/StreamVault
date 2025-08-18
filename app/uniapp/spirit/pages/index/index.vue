<template>
	<view class="container">
		<view class="card">
			<view class="card-header">
				<text class="title">StreamVault</text>
			</view>
			<view class="card-content">
				<textarea class="input-area" 
					placeholder="请输入或粘贴视频分享链接" 
					placeholder-class="placeholder"
					v-model="originaladdress"
					@input="validateUrl">
				</textarea>
				<!-- 链接验证提示 -->
				<view v-if="urlValidationMessage" class="validation-message" :class="{error: !isValidUrl}">
					<text>{{urlValidationMessage}}</text>
				</view>
				<view class="button-group">
					<button class="submit-btn" @click="pushMessage()" :disabled="isSubmitting || !isValidUrl">
						<text class="btn-text">{{isSubmitting ? '提交中...' : '提交链接'}}</text>
					</button>
					<button class="clear-btn" @click="clearInput()">
						<text class="clear-btn-text">清空</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 历史记录卡片 -->
		<view class="history-card" v-if="submitHistory.length > 0">
			<view class="history-header">
				<text class="history-title">最近提交记录</text>
				<button class="clear-all-btn" @click="clearAllHistory()">
					<text class="clear-all-text">清空全部</text>
				</button>
			</view>
			<view class="history-list">
				<view class="history-item" v-for="(item, index) in submitHistory" :key="index">
					<view class="history-content">
						<text class="history-url">{{item.url}}</text>
						<text class="history-time">{{formatTime(item.timestamp)}}</text>
					</view>
					<view class="history-actions">
						<button class="use-btn" @click="useHistoryUrl(item.url)">
							<text class="use-btn-text">使用</text>
						</button>
						<button class="delete-btn" @click="deleteHistoryItem(index)">
							<uni-icons type="trash" size="16" color="#ff4757"></uni-icons>
						</button>
					</view>
				</view>
			</view>
		</view>
		
		<view class="server-card" @click="serverList()">
			<view class="server-info">
				<uni-icons type="gear-filled" size="20" color="#0284da"></uni-icons>
				<text class="server-text">当前服务器：{{servername || '未选择'}}</text>
			</view>
			<uni-icons type="right" size="16" color="#999"></uni-icons>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				originaladdress:"",
				servername:"",
				serveraddr:"",
				serverport:"",
				servertoken:"",
				// 新增数据
				submitHistory: [], // 提交历史记录
				isSubmitting: false, // 提交状态
				isValidUrl: false, // URL验证状态
				urlValidationMessage: "" // URL验证消息
			}
		},
		onLoad() {
			// this.initServer();
		},
		onShow() {
			this.loadServer();
			this.loadSubmitHistory();
			uni.getClipboardData({
				success: (res) => {
					this.originaladdress = res.data;
					// 自动验证剪贴板内容
					this.validateUrl();
				}
			});
		},
		methods: {
			/**
			 * 加载服务器配置
			 */
			loadServer:function(){
					var that = this;
					var serverlist = uni.getStorageSync('serverlist') || [];
					for(var i =0;i<serverlist.length;i++){
						if(serverlist[i].default == 'y'){
							that.servername = serverlist[i].servername
							that.serveraddr = serverlist[i].server
							that.serverport = serverlist[i].port
							that.servertoken = serverlist[i].token
						}
					}
					if(that.servername=="" && serverlist.length !=0){
						that.servername = serverlist[0].servername
						that.serveraddr = serverlist[0].server
						that.serverport = serverlist[0].port
						that.servertoken = serverlist[0].token
					}
					uni.setStorageSync('serveraddr',that.serveraddr)
					uni.setStorageSync('serverport',that.serverport)
					uni.setStorageSync('servertoken',that.servertoken)
			},
			
			/**
			 * 加载提交历史记录
			 */
			loadSubmitHistory: function() {
				const history = uni.getStorageSync('submitHistory') || [];
				this.submitHistory = history.slice(0, 5); // 只显示最近5条
			},
			
			/**
			 * 保存提交历史记录
			 */
			saveSubmitHistory: function(url) {
				const newRecord = {
					url: url,
					timestamp: Date.now()
				};
				
				// 检查是否已存在相同URL，如果存在则移除旧记录
				let history = uni.getStorageSync('submitHistory') || [];
				history = history.filter(item => item.url !== url);
				
				// 添加新记录到开头
				history.unshift(newRecord);
				
				// 保持最多10条记录
				if (history.length > 10) {
					history = history.slice(0, 10);
				}
				
				uni.setStorageSync('submitHistory', history);
				this.loadSubmitHistory();
			},
			
			/**
			 * 验证URL格式
			 */
			validateUrl: function() {
				const url = this.originaladdress.trim();
				
				if (!url) {
					this.isValidUrl = false;
					this.urlValidationMessage = "";
					return;
				}
				
				// 简单验证：只检查是否包含http或https
				if (url.includes('http://') || url.includes('https://')) {
					this.isValidUrl = true;
					this.urlValidationMessage = "链接格式正确";
				} else {
					this.isValidUrl = false;
					this.urlValidationMessage = "请输入包含http://或https://的有效链接";
				}
			},
			
			/**
			 * 清空输入框
			 */
			clearInput: function() {
				this.originaladdress = "";
				this.isValidUrl = false;
				this.urlValidationMessage = "";
			},
			
			/**
			 * 使用历史记录中的URL
			 */
			useHistoryUrl: function(url) {
				this.originaladdress = url;
				this.validateUrl();
			},
			
			/**
			 * 删除单条历史记录
			 */
			deleteHistoryItem: function(index) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除这条记录吗？',
					success: (res) => {
						if (res.confirm) {
							let history = uni.getStorageSync('submitHistory') || [];
							history.splice(index, 1);
							uni.setStorageSync('submitHistory', history);
							this.loadSubmitHistory();
							uni.showToast({
								title: '删除成功',
								icon: 'success'
							});
						}
					}
				});
			},
			
			/**
			 * 清空所有历史记录
			 */
			clearAllHistory: function() {
				uni.showModal({
					title: '确认清空',
					content: '确定要清空所有历史记录吗？',
					success: (res) => {
						if (res.confirm) {
							uni.removeStorageSync('submitHistory');
							this.submitHistory = [];
							uni.showToast({
								title: '清空成功',
								icon: 'success'
							});
						}
					}
				});
			},
			
			/**
			 * 格式化时间显示
			 */
			formatTime: function(timestamp) {
				const date = new Date(timestamp);
				const now = new Date();
				const diff = now - date;
				
				// 小于1分钟
				if (diff < 60000) {
					return '刚刚';
				}
				// 小于1小时
				else if (diff < 3600000) {
					return Math.floor(diff / 60000) + '分钟前';
				}
				// 小于1天
				else if (diff < 86400000) {
					return Math.floor(diff / 3600000) + '小时前';
				}
				// 超过1天
				else {
					const month = date.getMonth() + 1;
					const day = date.getDate();
					const hour = date.getHours();
					const minute = date.getMinutes();
					return `${month}月${day}日 ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}`;
				}
			},
			serverList:function(){
				uni.navigateTo({
					url:"/pages/server/serverlist"
				})
			},
			/**
			 * 提交链接处理
			 */
			pushMessage:function(){
				// 验证输入
				if(!this.originaladdress.trim()) {
					uni.showToast({
						title: '请输入视频链接',
						icon: 'none'
					});
					return;
				}
				
				if(!this.isValidUrl) {
					uni.showToast({
						title: '请输入有效的链接格式',
						icon: 'none'
					});
					return;
				}
				
				if(!this.serveraddr || !this.serverport || !this.servertoken) {
					uni.showToast({
						title: '请先配置服务器信息',
						icon: 'none'
					});
					return;
				}
				
				// 设置提交状态
				this.isSubmitting = true;
				
				var api = this.serveraddr + ":" + this.serverport + "/api/processingVideos";
				var option = {
					token: this.servertoken,
					video: this.originaladdress.trim()
				};
				
				uni.showLoading({
					title: "正在处理链接..."
				});
				
				const that = this;
				uni.request({
					url: api,
					method: "POST",
					header: {
						'content-type': 'application/x-www-form-urlencoded'
					},
					data: option,
					success(res) {
						uni.hideLoading();
						that.isSubmitting = false;
						
						if(res.data && res.data.resCode == "000001") {
							// 提交成功
							uni.showToast({
								title: res.data.message || '提交成功',
								duration: 2000,
								icon: 'success'
							});
							
							// 保存到历史记录
							that.saveSubmitHistory(that.originaladdress.trim());
							
							// 清空输入框
							that.clearInput();
						} else {
							// 提交失败
							uni.showToast({
								title: res.data?.message || '提交失败，请重试',
								icon: 'none',
								duration: 3000
							});
						}
					},
					fail(err) {
						uni.hideLoading();
						that.isSubmitting = false;
						
						console.error('网络请求失败:', err);
						uni.showToast({
							title: '网络连接失败，请检查网络设置',
							icon: 'none',
							duration: 3000
						});
					}
				});
			},
			saveServer:function(){
				if(this.serveraddr != ""){
					uni.setStorageSync('serveraddr',this.serveraddr)
				}
				if(this.serverport != ""){
					uni.setStorageSync('serverport',this.serverport)
				}
				if(this.servertoken != ""){
					uni.setStorageSync('servertoken',this.servertoken)
				}
				uni.showToast({
					title: '保存成功',
					duration: 2000
				});
			},
		}
	}
</script>

<style>
.container {
	min-height: 100vh;
	padding: 20rpx;
	background-color: #f5f5f5;
}

.card {
	background: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-top: 20rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.card-header {
	margin-bottom: 30rpx;
}

.title {
	font-size: 32rpx;
	font-weight: 600;
	color: #333;
}

.card-content {
	display: flex;
	flex-direction: column;
	gap: 30rpx;
}

.input-area {
	width: 100%;
	height: 200rpx;
	background: #f8f8f8;
	border-radius: 12rpx;
	padding: 20rpx;
	font-size: 28rpx;
	color: #333;
}

.placeholder {
	color: #999;
	font-size: 28rpx;
}

.btn-text {
	color: #fff;
	font-size: 30rpx;
	font-weight: 500;
}

.server-card {
	background: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-top: 30rpx;
	display: flex;
	align-items: center;
	justify-content: space-between;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.server-info {
	display: flex;
	align-items: center;
	gap: 16rpx;
}

.server-text {
	font-size: 28rpx;
	color: #333;
}

/* 新增样式 */
.validation-message {
	padding: 16rpx 20rpx;
	border-radius: 8rpx;
	margin-top: 16rpx;
	background: #e8f5e8;
	border: 1rpx solid #52c41a;
}

.validation-message.error {
	background: #fff2f0;
	border-color: #ff4d4f;
}

.validation-message text {
	font-size: 24rpx;
	color: #52c41a;
}

.validation-message.error text {
	color: #ff4d4f;
}

.button-group {
	display: flex;
	gap: 20rpx;
	margin-top: 20rpx;
}

.submit-btn {
	flex: 1;
	background: #0284da;
	height: 88rpx;
	border-radius: 44rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	transition: all 0.3s;
}

.submit-btn[disabled] {
	background: #0284da !important;
	opacity: 0.6;
	color: #fff;
}

.clear-btn {
	width: 120rpx;
	height: 88rpx;
	background: #f5f5f5;
	border: 1rpx solid #d9d9d9;
	border-radius: 44rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}

.clear-btn-text {
	color: #666;
	font-size: 28rpx;
}

.history-card {
	background: #fff;
	border-radius: 16rpx;
	padding: 30rpx;
	margin-top: 30rpx;
	box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.history-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 24rpx;
}

.history-title {
	font-size: 30rpx;
	font-weight: 600;
	color: #333;
}

.clear-all-btn {
	padding: 8rpx 16rpx;
	background: #fff2f0;
	border: 1rpx solid #ffccc7;
	border-radius: 20rpx;
	height: auto;
	line-height: 1;
	display: flex;
	align-items: center;
	justify-content: center;
}

.clear-all-text {
	font-size: 24rpx;
	color: #ff4d4f;
}

.history-list {
	display: flex;
	flex-direction: column;
	gap: 16rpx;
}

.history-item {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx;
	background: #fafafa;
	border-radius: 12rpx;
	border: 1rpx solid #f0f0f0;
}

.history-content {
	flex: 1;
	display: flex;
	flex-direction: column;
	gap: 8rpx;
	margin-right: 20rpx;
}

.history-url {
	font-size: 26rpx;
	color: #333;
	word-break: break-all;
	line-height: 1.4;
}

.history-time {
	font-size: 22rpx;
	color: #999;
}

.history-actions {
	display: flex;
	align-items: center;
	gap: 12rpx;
}

.use-btn {
	padding: 8rpx 16rpx;
	background: #e6f7ff;
	border: 1rpx solid #91d5ff;
	border-radius: 20rpx;
	height: auto;
	line-height: 1;
	display: flex;
	align-items: center;
	justify-content: center;
}

.use-btn-text {
	font-size: 24rpx;
	color: #0284da;
}

.delete-btn {
	width: 60rpx;
	height: 60rpx;
	background: #fff2f0;
	border: 1rpx solid #ffccc7;
	border-radius: 30rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 0;
	min-height: 60rpx;
}
</style>
