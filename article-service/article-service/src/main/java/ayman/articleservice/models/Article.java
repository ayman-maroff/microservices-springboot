package ayman.articleservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class Article {

    private Integer id;
    private String name;
    private Date publishDate;
    private Integer authorId;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Article(Integer id, String name, Date publishDate, Integer authorId) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.authorId = authorId;
    }
}
