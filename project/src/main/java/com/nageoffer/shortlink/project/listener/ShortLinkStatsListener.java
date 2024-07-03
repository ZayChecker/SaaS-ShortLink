package com.nageoffer.shortlink.project.listener;

import cn.hutool.json.JSONUtil;
import com.nageoffer.shortlink.project.common.convention.exception.ServiceException;
import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.service.ShortLinkService;
import com.nageoffer.shortlink.project.util.MessageQueueIdempotentHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ShortLinkStatsListener {

    private final ShortLinkService shortLinkService;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "project.short-link.shortLinkStats"),
            exchange = @Exchange(name = "short-link.shortLinkStats.direct", type = ExchangeTypes.DIRECT),
            key = "short-link.shortLinkStats.success"
    ))
    public void sendToShortLinkStats(Map<String, String> producerMap){
        String fullShortUrl = producerMap.get("fullShortUrl");
        String gid = producerMap.get("gid");
        ShortLinkStatsRecordDTO statsRecord = JSONUtil.toBean(producerMap.get("statsRecord"), ShortLinkStatsRecordDTO.class);
        String keys = producerMap.get("keys");

        if(!messageQueueIdempotentHandler.isMessageProcessed(keys)){
            if(messageQueueIdempotentHandler.isAccomplish(keys)){
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            shortLinkService.shortLinkStats(fullShortUrl, gid, statsRecord);
        } catch (Throwable ex){
            messageQueueIdempotentHandler.delMessageProcessed(keys);
            log.error("记录短链接监控消费异常", ex);
        }
        messageQueueIdempotentHandler.setAccomplish(keys);
    }
}
