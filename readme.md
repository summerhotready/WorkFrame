this is a frame for work
工作框架主要用于自定义View和Utils

# Views #

## SlideBlock ##
拖延到如今才完成的拖拽组件part1，组件目前还有拖拽时有黑条的问题，诸君有解决方式请告知，谢谢
然后说一下思路：
继承自LinearLayout，通过设置可改变显示模式，默认为row（排模式）
activity/fragment中
SlideBlock blocks = (SlideBlock)root.findViewById(R.id.blocks);
blocks.setDefaultTableMode(SlideBlock.COLUME);

拖拽部分通过监听组件的onTouchEvent计算修改view的值
### 效果展示 ###
排：defaultTableMode = ROW
此模式中先向父组件竖着添加rowSize个容器，再向容器中添加columnSize个view
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/slideblocks_%E5%88%97.gif )

列：defaultTableMode = COLUME
此模式中先向父组件横向添加rowSize个容器，再向容器中竖着添加columnSize个view
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/slideblocks_%E6%8E%92.gif )

## CheckStart ##
具有四种模式的小星星，打分专用，点击可控
app:Max 星星总数，不设置默认为5

app:StartMode="begin|only|range|which"
对应4种模式
### 效果展示 ###
begin：从头到选中点亮起
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/CheckStart-2.png)

only：单一选择，选中的点亮起
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/CheckStart-4.png)

which:多选，被点击的亮起或者暗掉
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/CheckStart-3.png)

range：范围选择，两次点击为一次选择，显示效果为第一次点击到第二次点击范围亮起；
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/CheckStart-1.png)

当第二次范围小于第一次时全灭，选中同一点全灭

app:Progress
选中到几，显示效果要结合StartMode

app:isChecked
是否可选,显示效果要结合StartMode
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/CheckStart-0.png)

app:StartOrientation="horizontal|vertical"
星星方向有横竖两种选择,默认为横向

app:StartSize
星星大小，不设置则自动分配，预设值默认为StartOrientation方向可容纳的最大尺寸且不超过StartOrientation相反方向的尺寸，如果设置尺寸超过预设值，则按照预设值分配

app:StartMargin
星星间隔，不设置默认自动分派margin，当StartSize未设置时，会占满StartOrientation方向

app:StartGravity="left|right|center|top|bottom"
默认为center，不为center时只有设置app:StartMargin才能看出效果

## ProgressShowView ##
基于之前的组件ProgressBGView，做了优化了修改，支持padding，支持背景色修改
这个组件为了解决复杂背景进度条的问题，思路是矩形绘制。
ProgressBackground 设置组件背景色
### 效果展示 ###
![Screenshot]( https://raw.githubusercontent.com/summerhotready/WorkFrame/master/images/ProgressShowView.png)
