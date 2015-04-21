/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blog;

import database.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author chintan
 */
@Path("/blog")
public class Blog {

    @GET
    @Produces("application/json")

    public Response getAll() {
        return Response.ok(getResult("select * from blog")).build();

    }

    private static JsonArray getResult(String query, String... parameters) {
        JsonArray json = null;
        try {
            Connection con = Connect.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                pst.setString(i + 1, parameters[i]);
            }
            ResultSet results = pst.executeQuery();

            JsonArrayBuilder array = Json.createArrayBuilder();
            while (results.next()) {
                array.add(Json.createObjectBuilder()
                        .add("blogID", results.getString("blogID"))
                        .add("userID", results.getString("userID"))
                        .add("blogDateTime", results.getString("blogDateTime"))
                        .add("title", results.getString("title"))
                        .add("content", results.getString("content"))
                        .add("tags", results.getString("tags"))
                );
            }
            json = array.build();
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    @DELETE
    @Path("{id}")
    public Response deleteOne(@PathParam("id") String id) {
        int result = doUpdate("delete from blog where blogID=?", id);
        if (result <= 0) {
            return Response.status(500).build();
        } else {
            return Response.noContent().build();
        }

    }
//    @PUT
//    @Path("{id}")
//    public Response updateOne(@PathParam("id")String id,@PathParam("name")String name,@PathParam("description")String description,@PathParam("quantity")String quantity){
//    
//      int result = doUpdate("update product set name=?,description=?,quantity=? where productid=?", name,description,quantity,id);
//        if (result <= 0) {
//            return Response.status(500).build();
//        } else {
//            return Response.noContent().build();
//        }
//    }

    private int doUpdate(String query, String... params) {
        int numChanges = 0;
        try (Connection con = Connect.getConnection()) {
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 1; i <= params.length; i++) {
                pstmt.setString(i, params[i - 1]);
            }
            numChanges = pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(Blog.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return numChanges;
    }
}
