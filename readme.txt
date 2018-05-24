this is a frame for work
工作框架主要用于自定义View和Utils

Views
1、SlideBlock
拖延到如今才完成的拖拽组件part1，组件目前还有拖拽时有黑条的问题，诸君有解决方式请告知，谢谢
然后说一下思路：
继承自LinearLayout，通过设置可改变显示模式，默认为row（排模式）
activity/fragment中
SlideBlock blocks = (SlideBlock)root.findViewById(R.id.blocks);
blocks.setDefaultTableMode(SlideBlock.COLUME);

拖拽部分通过监听组件的onTouchEvent计算修改view的值
效果如图
排：defaultTableMode = ROW
此模式中先向父组件竖着添加rowSize个容器，再向容器中添加columnSize个view
![image](https://github.com/summerhotready/WorkFrame/blob/master/WorkFrame/images/slideblocks_%E5%88%97.gif )

列：defaultTableMode = COLUME
此模式中先向父组件横向添加rowSize个容器，再向容器中竖着添加columnSize个view
![image](https://github.com/summerhotready/WorkFrame/blob/master/WorkFrame/images/slideblocks_%E6%8E%92.gif )
