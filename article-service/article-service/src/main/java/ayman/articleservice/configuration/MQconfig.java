package ayman.articleservice.configuration;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class MQconfig {
    public static final String EXCHANGE = "req_exchange";

    public static final String StoryReqQueue = "StoryReq_queue";
    public static final String StoryRespQueue = "StoryResp_queue";

    public static final String Story_REQ_ROUTING_KEY = "Story_REQ_RK";
    public static final String Story_RES_ROUTING_KEY = "Story_RES_RK";


    @Bean
    public Queue StoryReqQueue(){
        return new Queue(StoryReqQueue);
    }

    @Bean
    public Queue StoryRespQueue(){
        return new Queue(StoryRespQueue);
    }

    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding reqBinding(){
        return BindingBuilder.bind(StoryReqQueue()).to(topicExchange()).with(Story_REQ_ROUTING_KEY);
    }

    @Bean
    public Binding respBinding(){
        return BindingBuilder.bind(StoryRespQueue()).to(topicExchange()).with(Story_RES_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

}
