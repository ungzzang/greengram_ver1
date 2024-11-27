package com.green.greengramver1.feed;

import com.green.greengramver1.common.MyFileUtils;
import com.green.greengramver1.feed.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedMapper mapper;
    private final MyFileUtils myFileUtils;

    public FeedPostRes postFeed(List<MultipartFile> pics, FeedPostReq p){
        int result = mapper.insFeed(p);

        // 리스트를 하나 만들어서 나중에 pics에 set으로 넣을꺼다.
        List<String> picsStr = new ArrayList<>();

        //파일저장
        //위치(middlePath): feed/${feedId}/
        long feedId = p.getFeedId();
        String middlePath = String.format("feed/%d", feedId);

        //폴더만들기
        myFileUtils.makeFolders(middlePath);

        //파일저장
        FeedPicDto feedPicDto = new FeedPicDto();
        //feedPicDto에 feedId값 넣어주세요.
        feedPicDto.setFeedId(feedId);

        for(MultipartFile pic : pics) { //사진 여러장 저장하려고 for문 돌림.
            String savedPicName = myFileUtils.makeRandomFileName(pic);
            String filePath = String.format("%s/%s", middlePath, savedPicName);

            try {
                myFileUtils.transferTo(pic, filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            feedPicDto.setPic(savedPicName);
            mapper.insFeedPic(feedPicDto); //(맵퍼와 연결하는용), 위에 mapper.은 튜플하나, 여기 mapper.은 튜플 여러개 넣으려고

            picsStr.add(savedPicName); //이름도 하나씩 들어가게
            //res.getPics().add(savedPicName);
        }

        FeedPostRes res = new FeedPostRes();

         res.setFeedId(p.getFeedId());
         res.setPics(picsStr);
         return res;
    }

    public List<FeedGetRes> getFeedList(FeedGetReq p){
        // DB에서 각 피드에 대한 정보 가져온다.(내용, 위치, feedId 등)
        List<FeedGetRes> list = mapper.selFeedList(p);

        //사진 매핑
        for(FeedGetRes res : list){// 1번 list의 1번 객체res, 2번 list의 2번 객체res 이런느낌)
            //DB에서 각 피드에 맞는 사진 정보를 가져온다.
            List<String> picList = mapper.selFeedPicList(res.getFeedId());
            //각 피드에 사진정보들이 담긴 picList를 pics에 넣는다.
            //(1번 res객체에 사진들(pics), 2번 res객체에 사진들(pics) 이런느낌)
            res.setPics(picList);
        }
        //N+1 이슈 (list받아오는거 한번 + list안의 N개 만큼 돌리니까), 별로 안좋음.
        //피드 셀렉트하고 피드하나가 가지고 있는 정보 셀렉트, 셀렉트 두번한다.

        return list;
    }
}
