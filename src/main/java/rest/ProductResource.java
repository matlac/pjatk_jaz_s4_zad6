package rest;

import domain.Comment;
import domain.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/product")
@Stateless
public class ProductResource {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAll() {
        return em.createNamedQuery("product.all", Product.class).getResultList();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getFiltered(@DefaultValue("0")@QueryParam("amount_from") int amount_from,
                                     @DefaultValue("0")@QueryParam("amount_to") int amount_to,
                                     @DefaultValue("")@QueryParam("name") String name,
                                     @DefaultValue("")@QueryParam("category") String category) {

        CriteriaBuilder qb = em.getCriteriaBuilder();
        CriteriaQuery cq = qb.createQuery();
        Root<Product> product = cq.from(Product.class);

        List<Predicate> predicates = new ArrayList<Predicate>();

        if (amount_from > 0) {
            predicates.add(
                    qb.greaterThan(product.get("amount"), amount_from));
        }
        if (amount_to > 0) {
            predicates.add(
                    qb.lessThan(product.get("amount"), amount_to));
        }
        if (!name.equals("")) {
            predicates.add(
                    qb.like(product.get("name"), "%"+name+"%"));
        }
        if (!category.equals("")) {
            predicates.add(
                    qb.like(product.get("category"), "%"+category+"%"));
        }


        cq.select(product)
                .where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(cq).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Product p){
        em.persist(p);
        em.flush();
        return Response.ok(p.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id){
        Product result = get_product(id);
        if(result==null){
            return Response.status(404).build();
        }
        return Response.ok(result).build();

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Product p){
        Product result = get_product(id);
        if(result==null){
            return Response.status(404).build();
        }
        result.setName(p.getName());
        result.setDescription(p.getDescription());
        result.setAmount(p.getAmount());

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
        Product result = get_product(id);
        if(result==null)
            return null;
        return result.getComments();
    }

    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int id, Comment comment){
        Product result = get_product(id);
        if(result==null)
            return Response.status(404).build();
        comment.setProduct(result);
        result.getComments().add(comment);
        em.persist(comment);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/comments/{comment_id}")
    public Response removeComment(@PathParam("id") int id,@PathParam("comment_id") int comment_id){
        Product result = get_product(id);
        if(result == null)
            return Response.status(404).build();

        int i = 0;
        for(Comment el : result.getComments()){
            if(el.getId() == comment_id) {
                result.getComments().remove(i);
                return Response.ok().build();
            }
            i++;
        }
        return Response.status(404).build();
    }


    private Product get_product(int id){
        return em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", id)
                .getSingleResult();
    }

}
