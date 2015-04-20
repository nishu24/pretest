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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
                        .add("blog_id", results.getString("blog_id"))
                        .add("user_id", results.getString("user_id"))
                        .add("blog_date_time", results.getString("blog_date_time"))
                        .add("title", results.getString("title"))
                        .add("content", results.getString("content"))
                );
            }
            json = array.build();
        } catch (SQLException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
}
