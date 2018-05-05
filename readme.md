# 简介 #
该SeekBar主要完成了普通的进度条（可修改进度条的颜色，游标颜色，二级进度条颜色等等），最后再在普通进度条的基础上实现了歌曲使用的进度条，可弹出窗口显示时间和歌词。该功能主要是在我的乐乐音乐播放器里面使用，其项目地址如下：
https://github.com/zhangliangming/HappyPlayer5.git 。

# 日志 #

## v2.5 ##
1. 添加混淆
2. 修改包名
3. 修复

## v2.1 ##
1. 修复onStopTrackingTouch

## v1.6 ##
1. 添加TrackingTouchSleepTime

## v1.5 ##
1. 修复bug

## v1.4 ##
1. view修改为SeekBar

## v1.3 ##
1. 修复滑动越界后，出现进度为负数的问题
## v1.2 ##
1. 修复弹出窗口报max == 0的问题
## v1.1 ##
1. 实现初始功能


# 截图 #

![](https://i.imgur.com/No0LrKB.png)

# Gradle #
1.root build.gradle

	`allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}`
	
2.app build.gradle

`dependencies {
	         compile 'com.github.zhangliangming:SeekBar:v2.5'
	}`

# 调用Demo #
链接: https://pan.baidu.com/s/1gg7jzWZ 密码: y3qd
# 调用用法 #
![](https://i.imgur.com/PxMZTpR.png)

![](https://i.imgur.com/3oPBqu6.png)

![](https://i.imgur.com/9Y6uVgF.png)

# API #
- setBackgroundPaintColor：设置背景颜色
- setProgressColor：设置进度颜色
- setSecondProgressColor：设置第二进度颜色
- setThumbColor：设置游标颜色
- setTimePopupWindowViewColor：设置时间弹窗颜色
- setTimeAndLrcPopupWindowViewColor：设置时间和歌词弹窗颜色
- OnMusicListener.getTimeText：获取时间标签，如果需要弹出窗口时，不能返回null。
- OnMusicListener.getLrcText：如果需要弹出歌词窗口时，不能返回null。


# 捐赠 #
如果该项目对您有所帮助，欢迎您的赞赏

- 微信

![](https://i.imgur.com/e3hERHh.png)

- 支付宝

![](https://i.imgur.com/29AcEPA.png)