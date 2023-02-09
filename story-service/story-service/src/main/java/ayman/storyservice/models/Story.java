package ayman.storyservice.models;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class Story {
    private Integer id;
    private String name;
    private Date publishDate;
    private Integer authorId;

    public void setId(Integer id) {
        this.id = id;
    }

    public Story(Integer id, String name, Date publishDate, Integer authorId) {
        this.id = id;
        this.name = name;
        this.publishDate = publishDate;
        this.authorId = authorId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Integer getAuthorId() {
        return authorId;
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
}
