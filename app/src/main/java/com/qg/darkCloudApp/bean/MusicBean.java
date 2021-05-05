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
    private String sDuration;//歌曲时长字符串
    private String path; //歌曲路径
    private long size;//歌曲大小
    private boolean isCheck;//当前歌曲选中

    public MusicBean() {
    }

    public MusicBean( String songName, String singer, String albumName, String sDuration,String path) {
        this.songName = songName;
        this.singer = singer;
        this.albumName = albumName;
        this.sDuration = sDuration;
        this.path = path;
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

    public String getsDuration() {
        return sDuration;
    }

    public void setsDuration(String sDuration) {
        this.sDuration = sDuration;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
