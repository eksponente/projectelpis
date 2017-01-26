/**
 * Created by rugile on 12/01/17.
 */

import ro.pippo.core.Application;
import ro.pippo.core.FileItem;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

public class ElpisApplication extends Application{
    @Override
    protected void onInit(){
        setUploadLocation("upload");
        GET("/", routeContext -> {
            List<Item> items;
            try {
                items = Item.fetchList(10, 0, "en" );
            } catch (SQLException e) {
                throw new RuntimeException("Problem with SQL: " + e.toString());
            }
            routeContext.setLocal("items", items.toArray());
            routeContext.render("itemList");
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
            String url1 = routeContext.getParameter("link1").toString();
            String url2 = routeContext.getParameter("link2").toString();
            FileItem file = routeContext.getRequest().getFile("file");
            File uploadedFile = new File("upload/" + file.getSubmittedFileName());
            try {
                file.write(uploadedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            titles.add(title);
            descriptions.add(description);
            langs.add("en");
            int id;
            try {
                id = Item.createNewItem(titles, descriptions, langs, category, true);
                Link.createNewLink(id, url1, true, "");
                Link.createNewLink(id, url2, true, "");
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
                throw new RuntimeException("Problem with SQL: " + e.toString());
            }
            routeContext.setLocal("title", it.title);
            routeContext.setLocal("body", it.description);
            routeContext.setLocal("id", it.id);
            routeContext.render("item");
        });


    }
}
