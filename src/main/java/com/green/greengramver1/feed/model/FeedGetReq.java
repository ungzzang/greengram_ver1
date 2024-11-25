package com.green.greengramver1.feed.model;

import com.green.greengramver1.common.model.Paging;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FeedGetReq extends Paging {

    public FeedGetReq(Integer page, Integer size) {
        super(page, size);//부모의 생성자를 가져오겠다는 뜻인데 부모한테 없으면 빨간줄
        log.info("size: {}", getSize());

    }

}
