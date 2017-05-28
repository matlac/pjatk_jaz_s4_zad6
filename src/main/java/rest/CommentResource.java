package rest;

import domain.Comment;
import domain.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/comment")
@Stateless
public class CommentResource {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getAll() {
        return em.createNamedQuery("comment.all", Comment.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Comment c){
        em.persist(c);
        em.flush();
        return Response.ok(c.getId()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removeComment(@PathParam("id") int id){
        Comment result = get_comment(id);
        if(result == null)
            return Response.status(404).build();

        em.remove(result);
        return Response.ok().build();
    }

    private Comment get_comment(int id){
        return em.createNamedQuery("comment.id", Comment.class)
                .setParameter("commentId", id)
                .getSingleResult();
    }

}
