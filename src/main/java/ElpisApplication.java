/**
 * Created by rugile on 12/01/17.
 */

import ro.pippo.core.Application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            List<String> titles = new ArrayList<String>();
            List<String> descriptions = new ArrayList<String>();
            List<String> langs = new ArrayList<String>();
            String title = routeContext.getParameter("title").toString();
            String category = routeContext.getParameter("category").toString();
            String description = routeContext.getParameter("description").toString();

            titles.add(title);
            descriptions.add(description);
            langs.add("en");
            int id;
            try {
                id = Item.createNewItem(titles, descriptions, langs, category, true);
            } catch (SQLException e) {
                throw new RuntimeException("Problem with SQL: " + e.toString());
            }
            if(id >= 0){
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
                it = Item.fetchUniqueItem(id, "en");
            } catch (SQLException e) {
                throw new RuntimeException("Problem with SQL.");
            }
            routeContext.setLocal("title", it.title);
            routeContext.setLocal("body", it.description);
            routeContext.setLocal("id", it.id);
            routeContext.render("item");
        });


    }
}
