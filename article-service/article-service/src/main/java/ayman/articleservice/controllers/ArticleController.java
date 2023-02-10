package ayman.articleservice.controllers;
import ayman.articleservice.configuration.MQconfig;
import ayman.articleservice.exception.ArticleNotFoundException;
import ayman.articleservice.models.Article;
import ayman.articleservice.models.Author;
import ayman.articleservice.models.Story;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Component
@Slf4j
public class ArticleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);
    private List<Article> articles;

    public ArticleController() {
        this.articles = new ArrayList<>();

        this.articles.add(new Article(1, "First Article", new Date(), 1));
        this.articles.add(new Article(2, "Second Article", new Date(), 2));
        this.articles.add(new Article(3, "Third Article", new Date(), 2));
        this.articles.add(new Article(4, "Fourth Article", new Date(), 1));
        this.articles.add(new Article(5, "Fifth Article", new Date(), 1));
    }
    @Autowired
    RestTemplate restTemplate;


    @GetMapping(path = "/author-name/{authorId}")
    public Author authorName(@PathVariable("authorId") final Integer authorId){
        Author author =restTemplate.getForObject("http://author-service/"+authorId, Author.class);

        return author;
    }


    @GetMapping(path = "/{id}")
    public Article findById(@PathVariable("id") Integer id) {
        log.info(String.format("Articles.findById(%d)", id));
        return this.articles.stream()
                .filter(article -> article.getId().intValue() == id.intValue())
                .findFirst()
                .orElseThrow(() -> new ArticleNotFoundException("id : " + id));
    }

    @GetMapping(path = "/author/{authorId}")
    public List<Article> findByAuthor(@PathVariable("authorId") final Integer authorId) {
        log.info(String.format("Articles.findByAuthor(%d)", authorId));
        return this.articles.stream().filter(article -> article.getAuthorId().intValue() == authorId.intValue()).collect(Collectors.toList());
    }
    @GetMapping(path = "/")
    public List<Article> getAll() {
        log.info("Articles.getAll()");
        return this.articles;
    }

    public List<Article> getAllCached() {
        log.info("Articles.getAllCached()");
        log.warn("Return cached result here");

        return new ArrayList<>();
    }


    @Autowired
    private RabbitTemplate rabbitTemplate;
    @GetMapping("/ask/story/{SId}")
    public String askStory(@PathVariable("SId") Integer SId){

        rabbitTemplate.convertAndSend(MQconfig.EXCHANGE,MQconfig.Story_REQ_ROUTING_KEY,SId);
        LOGGER.info(String.format("Queued: Movie with id %s",SId));

        return "Your request queued in RabbitMQ ...";
    }
    @RabbitListener(queues = MQconfig.StoryRespQueue)
    public void respStory(Story story){

        LOGGER.info(String.format("Received story: %s, %s, %s, %s",story.getId(),story.getName(),story.getPublishDate(),story.getAuthorId()));
    }


}
