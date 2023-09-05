#### 简介
`AsyMediaLib` 是一个可以将媒体数据库中的图片和视频信息同步到App本地的一个工具。当媒体库中的图片或视频信息发生变化，`AsyMediaLib` 会同步更新本地的媒体库信息。

#### 特点
1. 同步手机中媒体库中的图片到App本地
2. 监听并同步媒体库中图片和视频的变化并同步本地
3. 当媒体库中图片和视频信息非常庞大的时，同步不会造成OOM
4. 可以设置同步图片和视频的大小范围

   
### 使用方法
#### 将`AsyMediaLib` 添加到工程中
下载 `AsyMediaLib` ， 依赖到项目中， 很简单不介绍。

#### 实现本地媒体数据表的映射类
实现本地媒体数据映射类一定要继承`MediaInfo`， 在`MediaInfo` 中封装了系统媒体库中图片和视频的所有信息字段。   
`MediaInfo` 中包含的字段：

```
public class MediaInfo implements Parcelable {
    private int id;
    private int mediaId;
    private String name;
    private String title;
    private String path;
    private String type;
    private long time;
    private long duration;
    private String resolution;
    private int width;
    private int height;
    private long size;
    private String album;
    private String folder;
    
    ........
}
```
既然`MediaInfo` 都有这些信息为什么还要继承呢？ 主要是为了扩展考虑， 比如`Demo` 中需要添加一个创建时间的字段。

```
public class LocalMediaInfo extends MediaInfo {
    private long createTime;
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

```
很容易就添加上。

#### CursorWrapper
这里要先说一下`CursorWrapper` ，这个类包装了`Cursor`。    
`CursorWrapper` 具体实现如下：
```
public class CursorWrapper {
    public Cursor cursor;
    public void close() {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
    }

    public CursorWrapper(Cursor cursor) {
        this.cursor = cursor;
        this.cursor.moveToFirst();
    }

    public static CursorWrapper create(Cursor cursor) {
        return new CursorWrapper(cursor);
    }
}
```

#### 实现本地媒体数据表的控制类
实现本地媒体数据表的控制类要继承`AbsUDatabaseController<T>` ， 这个控制类主要是负责给`AsyMediaLib` 库提供对本地媒体数据操作具体实现。  

**必须实现下面几个方法：**
1. `public T createMediaInfo()`  
    创建本地数据表映射类   
    **返回值：**  
    `T` 映射对象
2. `public T cursorToMediaBean(Cursor cursor, T t)`   
    将`Cursor` 中的数据转换成映射对象  
    **参数：**  
    `cursor`: 数据库游标  
    `t`:映射对象  
    **返回值：**  
    `T`: 映射对象。该映射对象最好使用参数中传入的映射对象。
3. `public int getMediaId(CursorWrapper cursorWrapper)`  
    获取当前`Cursor` 中的`MediaId`, 这个`MediaId`, 必须和手机媒体库中的`_id` 相对应。   
    **参数：**  
    `cursorWrapper`: 是包装了`Cursor` 的实体对象。通过其中的`Cursor`对象获取本地数据库中对应的`Id`.

4. `public void insert(Context context, MediaInfo mediaInfo)`  
    向App应用中插入媒体数据 `MediaInfo`。   
    **参数：**
    `MediaInfo` 是从系统媒体库中获取的数据.
    
5. `public void deleteForList(Context context, List<MediaInfo> medias)`   
    批量删除媒体数据`MediaInfo`， 本地数据库中的数据和系统媒体数据库中的数据不同步时，会调用这个方法删除本地多余的媒体数据。
6. `public void updateForList(Context context, List<MediaInfo> medias)`   
    批量修改媒体数据`MediaInfo`，本地数据库中的数据和系统媒体数据库中的数据不同步时，会调用这个方修改本地变化的媒体数据。

7. `public void insertForList(Context context, List<MediaInfo> medias)`   
    批量插入媒体数据`MediaInfo`，本地数据库中的数据和系统媒体数据库中的数据不同步时，会调用这个方法插入新的媒体数据。
8. `public CursorWrapper queryImage(Context context, int startMediaId, int rowNum)`
    查询同步的本地媒体库中图片数据。   
    **参数：**   
    `startMediaId`:每次查询起始的`MediaId`  
    `rowNum`:每次查询的条数

9. `public CursorWrapper queryVideo(Context context, int startMediaId, int rowNum)`
    查询同步的本地媒体库中视频数据。   
    **参数：**   
    `startMediaId`:每次查询起始的`MediaId`  
    `rowNum`:每次查询的条数
10. `public CursorWrapper queryImageAndVideo(Context context, int startMediaId, int rowNum)`
    查询同步的本地媒体库中图片和视频数据。   
    **参数：**   
    `startMediaId`:每次查询起始的`MediaId`  
    `rowNum`:每次查询的条数

上面的方法必须按照要求实现。如果有不清楚请参考[Demo](https://github.com/MrChao1991/AsyMediaDemo)

下面介绍三个非必须实现的方法：
1. `public CursorWrapper queryImageAndVideo(Context context, int imageMinSize, int imageMaxSize, int videoMinSize, int videMaxSzie, int startMediaId, int rowNum)`
2. `public CursorWrapper queryImage(Context context, int minSuze, int maxSize, int startMediaId, int rowNum)`
3. `public CursorWrapper queryVideo(Context context, int minSuze, int maxSize, int startMediaId, int rowNum)`

这三个方法增加了对查询媒体文件信息最大和最小的限制。

#### 实现数据校验规则类
校验规则也要进行自定义，在实际开发中结合具体场景进行实现。  
自定义校验规则要实现`CheckRule` 接口，这个校验规则要必须实现。  
`CheckRule` 源码如下：

```
public interface CheckRule<T extends MediaInfo> {
    boolean checkUpdate(T localBean, MediaInfo mediaInfo);
}
```

#### 必要配置
`AsyMediaLib` 的配置也很简单，下面看一下[Demo](https://github.com/MrChao1991/AsyMediaDemo) 中的配置示例。

```
AsyMediaDatabase amd = AsyMediaDatabase
        .create(this)
        .setUDatabaseControl(new LocalDatabaseController())
        .setCheckRule(new CheckRule<LocalMediaInfo>() {
            @Override
            public boolean checkUpdate(LocalMediaInfo localBean, MediaInfo mediaInfo) {
                Log.e(TAG, "checkUpdate: " +
                        "  local id:" + localBean.getMediaId() +
                        "  media id:" + mediaInfo.getMediaId() +
                        "  update: " + !localBean.getPath().equals(mediaInfo.getPath()));
                return !localBean.getPath().equals(mediaInfo.getPath());
            }
        })
        .setQueryOnceRowNumber(10)
        .setCacheSizeDelete(20)
        .setCacheSizeUpdate(30)
        .setCacheSizeInsert(40)
        .setFilterMinImageSize(39999)
        .setFilterMaxImageSize(500000)
        .setFilterMinVideoSize(1024)
        .setFilterMaxVideoSize(1024 * 100000)
        .build();
```
所有的配置都在一个`Build`类中完成，`AsyMediaDatabase.create(this)` 创建一个`Build`, 最后调用 `build()`方法进行配置。实现方法不多介绍，感兴趣可以去看源码。

**设置API介绍**  
1. `setUDatabaseControl(AbsUDatabaseController UDatabaseControl)`      
    设置本地映射实体类。
2. `setCheckRule(CheckRule mCheckRule)`     
    设置本地和手机系统媒体信息校验规则
3. `setQueryOnceRowNumber(int mQueryOnceRowNumber)`  
    设置每次查询数据条数
4. `setFilterMinImageSize(int mFilterMinImageSize)`   
    设置查询时图片最小size
5. `setFilterMaxImageSize(int mFilterMaxImageSize)`   
    设置查询时图片最大size
6. `setFilterMinVideoSize(int mFilterMinVideoSize)`  
    设置查询时视频最小size
7. `setFilterMaxVideoSize(int mFilterMaxVideoSize)`  
    设置查询时视频最大size
8. `setCacheSizeInsert(int mCacheSizeInsert)`  ，`setCacheSizeUpdate(int mCacheSizeUpdate)` ，`setCacheSizeDelete(int mCacheSizeDelete)`    
设置在同步的时候，批量插入，修改，删除缓存的大小。设置的大小，为缓存的条数。
为什么会有这个缓存，因为在同步的时候，可能有很多数据需要插入，删除和修改的数据，不能发现一条，操作一条，所以，采用一个缓存的方式，达到这个缓存的极限后，会进行批量操作， 所以在实现本地数据库控制类的时候，在多条数据操作的时候记得使用事务进行操作。
9. `openDebug()`   
    开启debug。

#### 注册系统媒体库变化监听
注册监听非常简单， 上面的配置完成后，`register()`即可以启动监听。

```
AsyMediaDatabase amd = AsyMediaDatabase
        ..... // 省略配置代码
        .build();

amd.register();
```


#### 同步系统媒体库到本地

同步系统媒体库到本地非常简单， 上面的配置完成后，`asyMedia()`即可以启动同步。

```
AsyMediaDatabase amd = AsyMediaDatabase
        ..... // 省略配置代码
        .build();

amd.asyMedia();
```

上面是对`AsyMediaLib` 这个库使用的简单介绍，可能介绍不是很详细，请参考[Demo](https://github.com/MrChao1991/AsyMediaDemo)
