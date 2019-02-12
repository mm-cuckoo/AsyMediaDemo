package com.cfox.asymediademo.db;

public interface IMediaDbData {

    String AUTHORITY = "com.smartisanos.fve.db";

    String TABLE_FILES = "files";
    String TABLE_VIDEO_PLAN = "video_plan";
    String TABLE_IMAGE_PLAN = "image_plan";
    String TABLE_VIDEO_GENERATE_PLAN = "video_generate_plan";
    String TABLE_VIDEO_GENERATE = "video_generate";

    String ID = "id";
    String FILE_PATH = "file_path";
    String MEDIA_ID = "media_id";
    String FILE_ID = "file_id";
    String TYPE = "type";
    String HIDDEN = "hidden";
    String START_TIME = "start_time";
    String STOP_TIME = "stop_time";
    String GENERATE_ID = "generate_id";
    String PLAN_ID = "plan_id";
    String PRIORITY = "priority";
    String NAME = "name";
    String CRATE_TIME = "create_time";

}
