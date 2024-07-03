package com.nageoffer.shortlink.project.listener;

import com.nageoffer.shortlink.project.dto.biz.ShortLinkStatsRecordDTO;
import com.nageoffer.shortlink.project.service.ShortLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShortLinkStatsDelayListener {

    private final ShortLinkService shortLinkService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "project.short-link.delayShortLinkStats"),
            exchange = @Exchange(name = "short-link.shortLinkStats.delay.direct", type = ExchangeTypes.DIRECT),
            key = "short-link.shortLinkStats.delay.success"
    ))
    public void delayShortLinkStats(ShortLinkStatsRecordDTO statsRecord){
        shortLinkService.shortLinkStats(null, null, statsRecord);
    }
}
