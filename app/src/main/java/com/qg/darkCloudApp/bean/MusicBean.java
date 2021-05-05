package com.qg.darkCloudApp.bean;

/**
 * @Name：localMusicBean
 * @Description：本地音乐实体类
 * @Author：Suzy.Mo
 * @Date：2021/5/4 20:11
 */
public class MusicBean {
    private int songId;//歌曲id
    private String songName; //歌曲名称
    private String singer; //歌手名称
    private int albumId;//专辑id
    private String albumName; //专辑名称
    private long duration; //歌曲时长
    private String path; //歌曲路径
    private long size;//歌曲大小

    public MusicBean() {
    }

    public MusicBean(int songId, String songName, String singer, int albumId, String albumName, long duration, String path, long size) {
        this.songId = songId;
        this.songName = songName;
        this.singer = singer;
        this.albumId = albumId;
        this.albumName = albumName;
        this.duration = duration;
        this.path = path;
        this.size = size;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
