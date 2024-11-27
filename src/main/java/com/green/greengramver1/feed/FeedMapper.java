package com.green.greengramver1.feed;

import com.green.greengramver1.feed.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FeedMapper {
    int insFeed(FeedPostReq p);
    List<FeedGetRes> selFeedList(FeedGetReq p);


    int insFeedPic(FeedPicDto p);
    List<String> selFeedPicList(long p);

}
