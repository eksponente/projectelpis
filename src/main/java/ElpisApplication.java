/**
 * Created by rugile on 12/01/17.
 */

import ro.pippo.core.Application;
import java.sql.*;

public class ElpisApplication extends Application{
    @Override
    protected void onInit(){
        GET("/", routeContext -> {
//            try {
//
//            } catch (SQLException e) {
//                throw new RuntimeException("Problem with SQL.");
//            }
            routeContext.send("Working");
        });
        GET("/newItem", routeContext -> {
            routeContext.render("itemForm");
        });
        POST("/newItem", routeContext -> {
            int id = routeContext.getParameter("id").toInt();
            String title = routeContext.getParameter("title").toString();
            String body = routeContext.getParameter("body").toString();
            boolean fault;
            try {
                fault = Item.createNewItem(id, title, body);
            } catch (SQLException e) {
                throw new RuntimeException("Problem with SQL.");
            }
            if(!fault){
                routeContext.setLocal("message", "A new item with id " + id + " has been created.");
            }else{
                routeContext.setLocal("message", "A new item has not been created.");
            }
            routeContext.render("itemForm");

        });
        GET("/{id}", routeContext -> {
            int id = routeContext.getParameter("id").toInt();
            Item it;
            try {
                it = Item.fetchUniqueItem(id);
            } catch (SQLException e) {
                throw new RuntimeException("Problem with SQL.");
            }
            routeContext.setLocal("title", it.title);
            routeContext.setLocal("body", it.body);
            routeContext.setLocal("id", it.id);
            routeContext.render("item");
        });


    }
}
