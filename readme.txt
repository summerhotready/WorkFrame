this is a frame for work
工作框架主要用于自定义View和Utils

Views
1、SlideBlock
拖延到如今才完成的拖拽组件part1，组件目前还有延迟的小问题，以及拖拽时有黑条的问题，诸君有解决方式请告知，谢谢
然后说一下思路：
继承自LinearLayout，通过设置可改变行列（目前不能通过组件设置）
拖拽部分通过监听组件的onTouchEvent计算修改view的值
效果如图
列：defaultTableMode = COLUME
![img](https://github.com/summerhotready/WorkFrame/blob/master/WorkFrame/images/slideblocks_%E5%88%97.gif)
排：defaultTableMode = ROW
![img](https://github.com/summerhotready/WorkFrame/blob/master/WorkFrame/images/slideblocks_%E6%8E%92.gif)
