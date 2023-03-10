package ayman.storyservice.controllers;
import ayman.storyservice.configuration.MQconfig;
import ayman.storyservice.exception.StoryNotFoundException;
import ayman.storyservice.models.Story;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ayman.storyservice.configuration.RestTamplateConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class StoryController {
    private List<Story> stories;

    public StoryController() {
        this.stories = new ArrayList<>();

        this.stories.add(new Story(1, "The Signal Man", new Date(), 1));
        this.stories.add(new Story(2, "The Happy Prince", new Date(), 2));
        this.stories.add(new Story(3, "The Magic Shop", new Date(), 2));
        this.stories.add(new Story(4, "The Gift of the Magi", new Date(), 1));
        this.stories.add(new Story(5, "Rip Van Winkle", new Date(), 1));
    }
    @Value("${api.key}")
    private String apiKey;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(path = "/book/kind/{kind}")

    public String authorName(@PathVariable("kind") final String kind){
        System.out.print(apiKey+kind);
        log.info(String.format("get external books service(%s)", kind));
        String responseEntity = restTemplate.getForObject("https://www.googleapis.com/books/v1/volumes?q="+kind+"&key="+apiKey, String.class);
        return responseEntity;
    }
    @GetMapping(path = "/{id}")
    public Story findById(@PathVariable("id") Integer id) {
        log.info(String.format("stories.findById(%d)", id));
        return this.stories.stream()
                .filter(article -> article.getId().intValue() == id.intValue())
                .findFirst()
                .orElseThrow(() -> new StoryNotFoundException("id : " + id));
    }

    @GetMapping(path = "/author/{authorId}")
    public List<Story> findByAuthor(@PathVariable("authorId") final Integer authorId) {
        log.info(String.format("stories.findByAuthor(%d)", authorId));
        return this.stories.stream().filter(article -> article.getAuthorId().intValue() == authorId.intValue()).collect(Collectors.toList());
    }
    @RabbitListener(queues = MQconfig.StoryReqQueue)
    public void respStory(Integer SId){

        log.info(String.format("Received StoryId: %s", SId));
        Story story= this.stories.stream()
                .filter(article -> article.getId().intValue() == SId.intValue())
                .findFirst()
                .orElseThrow(() -> new StoryNotFoundException("id : " + SId));


        rabbitTemplate.convertAndSend(MQconfig.EXCHANGE,MQconfig.Story_RES_ROUTING_KEY,story);


        log.info(String.format("Queued: Movie with id %s",story.getId()));
    }


    @GetMapping(path = "/")
    public List<Story> getAll() {
        log.info("stories.getAll()");
        return this.stories;
    }

    public List<Story> getAllCached() {
        log.info("stories.getAllCached()");
        log.warn("Return cached result here");

        return new ArrayList<>();
    }
}
