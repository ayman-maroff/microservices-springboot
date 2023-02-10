package ayman.gatewayservice.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/")

public class FallBackMethod {
    @RequestMapping("article-fallback")
    public String getArticleFallback(){
        return "article Service is temporary unavailable, Please Try Again Later";
    }

    @RequestMapping("author-fallback")
    public String getAuthorFallback(){
        return "author Service is temporary unavailable, Please Try Again Later";
    }
    @RequestMapping("story-fallback")
    public String getStoryFallback(){
        return "story Service is temporary unavailable, Please Try Again Later";
    }

}
