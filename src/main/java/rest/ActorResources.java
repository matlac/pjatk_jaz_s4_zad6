package rest;


import domain.Actor;
import domain.Comment;
import domain.Movie;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/actor")
@Stateless
public class ActorResources {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Actor> getAll() {
        return em.createNamedQuery("actor.all", Actor.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Actor a){
        em.persist(a);
        em.flush();
        return Response.ok(a.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        Actor result = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", id)
                .getSingleResult();
        if(result==null){
            return Response.status(404).build();
        }
        return Response.ok(result).build();

    }

    @GET
    @Path("/{id}/movies")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getActors(@PathParam("id") int id){
        Actor result = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", id)
                .getSingleResult();
        if(result==null)
            return null;
        return result.getMovies();
    }

    @PUT
    @Path("/{id}/movies/{movie_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, @PathParam("movie_id") int movie_id, Movie m){
        Actor result = em.createNamedQuery("actor.id", Actor.class)
                .setParameter("actorId", id)
                .getSingleResult();
        if(result==null){
            return Response.status(404).build();
        }

        Movie movie_result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movie_id)
                .getSingleResult();
        if(result==null){
            return Response.status(404).build();
        }
        movie_result.setName(m.getName());
        movie_result.setDescription(m.getDescription());
        movie_result.setGenre(m.getGenre());
        movie_result.setPremiere_date(m.getPremiere_date());

        boolean found = false;
        for(Movie el : result.getMovies()){
            if(el.getId() == movie_id) {
                found = true;
                break;
            }
        }
        if(!found){
            result.getMovies().add(movie_result);
            movie_result.getActors().add(result);
        }

        em.persist(movie_result);
        return Response.ok().build();
    }

}
