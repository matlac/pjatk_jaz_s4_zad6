package domain;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@Entity
@NamedQueries({
    @NamedQuery(name = "movie.all", query="SELECT m FROM Movie m"),
    @NamedQuery(name = "movie.id", query="FROM Movie m WHERE m.id=:movieId"),
})
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String premiere_date;
    private String description;
    private String genre;
    private int rate_sum = 0;
    private int rate_count = 0;

    private List<Comment> comments = new ArrayList<Comment>();
    private List<Actor> actors = new ArrayList<Actor>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPremiere_date() {
        return premiere_date;
    }

    public void setPremiere_date(String premiere_date) {
        this.premiere_date = premiere_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    /*
        COMMENTS
     */

    @XmlTransient
    @OneToMany(mappedBy="movie")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /*
        ACTORS
     */

    @XmlTransient
    @ManyToMany(targetEntity=Actor.class, mappedBy = "movies")
    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

     /*
        RATING
     */

    public int getRate_sum() {
        return rate_sum;
    }

    public void setRate_sum(int rate_sum) {
        this.rate_sum = rate_sum;
    }

    public void increaseSum(int sum) {
        this.rate_sum += sum;
    }

    public void increaseCount() {
        this.rate_count++;
    }

    public int getRate_count() {
        return rate_count;
    }

    public void setRate_count(int rate_count) {
        this.rate_count = rate_count;
    }

    public int getRate(){
        if(this.rate_count > 0)
            return Math.round(this.rate_sum / this.rate_count);
        else
            return 0;
    }
}
