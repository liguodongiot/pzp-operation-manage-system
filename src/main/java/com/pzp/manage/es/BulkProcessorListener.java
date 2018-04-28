package com.pzp.manage.es;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/28 18:04 星期六
 */

public class BulkProcessorListener implements BulkProcessor.Listener{

    private static final Logger LOGGER = LoggerFactory.getLogger(BulkProcessorListener.class);

    /**
     * 批量提交之前执行，可以从BulkRequest中获取请求信息request.requests()或者请求数量request.numberOfActions()
     * @param executionId
     * @param request
     */
    @Override
    public void beforeBulk(long executionId, BulkRequest request) {
        LOGGER.info("executionId:[{}],request.numberOfActions:[{}].",executionId, request.numberOfActions());
    }

    /**
     * 在批量成功后执行，可以跟beforeBulk配合计算批量所需时间。
     * @param executionId
     * @param request
     * @param response
     */
    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        LOGGER.info("executionId:[{}],response.hasFailures:[{}]," ,executionId, response.hasFailures());
    }

    /**
     * 在批量失败后执行
     * @param executionId
     * @param request
     * @param failure
     */
    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        LOGGER.error("executionId:[{}] ,data bulk failed, reason :[{}]  .", executionId, failure);
    }
}
