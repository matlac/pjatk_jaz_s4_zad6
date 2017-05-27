package rest;

import domain.Actor;
import domain.Comment;
import domain.Movie;
import domain.Rate;
import domain.services.MovieService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/movie")
@Stateless
public class MovieResource {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAll() {
        return em.createNamedQuery("movie.all", Movie.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Movie m){
        em.persist(m);
        em.flush();
        return Response.ok(m.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
        if(result==null){
            return Response.status(404).build();
        }
        return Response.ok(result).build();

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Movie m){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
        if(result==null){
            return Response.status(404).build();
        }
        result.setName(m.getName());
        result.setDescription(m.getDescription());
        result.setGenre(m.getGenre());
        result.setPremiere_date(m.getPremiere_date());

        em.persist(result);
        return Response.ok().build();
    }

    /*
        COMMENTS
     */

    @GET
    @Path("/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getComments(@PathParam("id") int id){
        Movie result = get_movie(id);
        if(result==null)
            return null;
        return result.getComments();
    }

    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int id, Comment comment){
        Movie result = get_movie(id);
        if(result==null)
            return Response.status(404).build();
        comment.setMovie(result);
        result.getComments().add(comment);
        em.persist(comment);
        //em.flush();
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/comments/{comment_id}")
    public Response removeComment(@PathParam("id") int id,@PathParam("comment_id") int comment_id){
        Movie result = get_movie(id);
        if(result == null)
            return Response.status(404).build();

        int i = 0;
        for(Comment el : result.getComments()){
            if(el.getId() == id) {
                result.getComments().remove(i);
                return Response.ok().build();
            }
            i++;
        }
        return Response.status(404).build();
    }

    /*
        ACTORS
     */

    @GET
    @Path("/{id}/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getActors(@PathParam("id") int id){
        Movie result = get_movie(id);
        if(result==null)
            return null;
        return result.getActors();
    }

    @POST
    @Path("/{id}/actors")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addActor(@PathParam("id") int id, Actor actor){
        Movie result = get_movie(id);
        if(result==null)
            return Response.status(404).build();
        result.getActors().add(actor);
        actor.getMovies().add(result);
        em.persist(actor);
        //em.flush();
        return Response.ok().build();
    }

    /*
        RATES
     */

    @GET
    @Path("/{id}/rate")
    @Produces(MediaType.APPLICATION_JSON)
    public Rate getRate(@PathParam("id") int id){
        Movie result = get_movie(id);
        if(result==null)
            return null;

        Rate rate = new Rate();
        rate.setRate(result.getRate());
        return rate;
    }

    @POST
    @Path("/{id}/rate")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRate(@PathParam("id") int id, Rate rate){
        Movie result = get_movie(id);
        if(result==null)
            return Response.status(404).build();
        result.increaseSum(rate.getRate());
        result.increaseCount();
        return Response.ok().build();
    }

    private Movie get_movie(int id){
        return em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
    }

}
