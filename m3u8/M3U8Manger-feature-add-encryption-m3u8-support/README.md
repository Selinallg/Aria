# M3U8Manger
M3U8管理器，M3U8格式视频下载，支持点播下载，直播缓存

fork了github上一个m3u8下载器（https://github.com/huangdali/M3U8Manger）
因为我的项目需要支持加密的m3u8片段下载。

这里查看 [原说明文档](https://github.com/huangdali/M3U8Manger/blob/v2.0.6/README.md ""). 

### 根据自己的项目做了几处改动。
####  1、增加key的下载并支持不下载重复key文件，多数m3u8文件的key其实多为一个文件，增加下载速度
####  2、下载后的m3u8 ts片段不进行合并，存放在一个目录，并生成一个m3u8播放文件，里面包含加密的key

