单手播放控件说明
一，	控件效果及组成各部分示意图：
 
二，	控件主体架构设计UML示意图：
 
架构简要说明：
1.	一个CenSeekBar主要由Track与Thumb两部分构成，Track与Thumb分别是两个实体类，封装各自的样式等内容，可继承扩展。
2.	CenSeekBar借鉴了系统控件ProgramBar，将具体样式效果绘制委托给Drawable。
3.	CenSeekBar的Track（也就是进度条轨道）不固定，可以是水平的、垂直的、斜的、弯的……具体样式由子类决定，即扩展Track。Thumb同理。具体细节见源码注释。
