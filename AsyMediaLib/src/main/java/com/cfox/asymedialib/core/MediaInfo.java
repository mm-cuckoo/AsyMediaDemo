package com.cfox.asymedialib.core;

import android.os.Parcel;
import android.os.Parcelable;

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

    public MediaInfo() {
    }

    protected MediaInfo(Parcel in) {
        id = in.readInt();
        mediaId = in.readInt();
        name = in.readString();
        title = in.readString();
        path = in.readString();
        type = in.readString();
        time = in.readLong();
        duration = in.readLong();
        resolution = in.readString();
        width = in.readInt();
        height = in.readInt();
        size = in.readLong();
        album = in.readString();
        folder = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(mediaId);
        dest.writeString(name);
        dest.writeString(title);
        dest.writeString(path);
        dest.writeString(type);
        dest.writeLong(time);
        dest.writeLong(duration);
        dest.writeString(resolution);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeLong(size);
        dest.writeString(album);
        dest.writeString(folder);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaInfo> CREATOR = new Creator<MediaInfo>() {
        @Override
        public MediaInfo createFromParcel(Parcel in) {
            return new MediaInfo(in);
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    @Override
    public String toString() {
        return "MediaInfo{" +
                "id=" + id +
                ", mediaId=" + mediaId +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", duration=" + duration +
                ", resolution='" + resolution + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", size=" + size +
                ", album='" + album + '\'' +
                ", folder='" + folder + '\'' +
                '}';
    }
}
