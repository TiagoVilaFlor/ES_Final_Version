package controllers;

import akka.actor.IO;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.api.mvc.Rendering;
import play.db.DB;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

//import play.db.DB;

//import static play.data.Form.form;

/**
 * Created by tiago on 06-11-2014.
 */
public class AppJava extends Controller {

    public static Result home(){
        return ok(views.html.home.render("We Care Services"));
    }

    public static Result logout(){
        session().clear();
        return redirect("/");
    }

    public static Result login(){

        Map<String, String[]> arguments = request().queryString();
        //int id_user = 14;
        int id_user = Integer.parseInt(arguments.get("id_user")[0]);
        System.out.println("ID: "+id_user);
        //session("email", "tiago.vf.lourenco@gmail.com");
        //session("perm", "A");

        try {
            // Open connection:
            Connection con = DB.getConnection();
            String query = "select * from user_wcs inner join user_identifiers on user_id=user_id_wcs where user_id= ?"; // security
            String t = id_user+"";
            Logger.debug(t);
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id_user);
            //session("patient", Json.newObject().put("email", email).put("password", passw));
            ResultSet rs = ps.executeQuery();
            if(rs != null)
                while(rs.next()){
                    session().clear();
                    session("email", rs.getString(10));
                    session("perm", rs.getString(5));
                    session("conta", rs.getString(11));
                    session("id", rs.getString(1));
                    session("bool", "0");                       //if 0 ainda nao associou a conta else ja associou a conta
                }
            else
                Logger.debug("Nao ligou a BD");
            con.close();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return redirect("/privat");
    }

    public static Result privat()
    {

        if(session("email") == null || session("perm") == null)
            return redirect("/");
        else
            return ok(privat.render(session("email"), session("perm"), session("conta"), session("id"), session("bool")));
    }

    public static Result profile(){
        if(session("email") == null || session("perm") == null)
            return redirect("/");
        else
            return ok(profile.render(session("email"), session("perm"), session("conta"), session("id"), session("bool")));
    }


    public static Result admin(){
        if(session("email") == null || session("perm") == null)
            return redirect("/");
        else
            return ok(views.html.admin.render(session("email"), session("perm"), session("conta"), session("id"), session("bool")));
    }

    public static Result fullCalendar(){
        return ok(fullCalendar.render());
    }

    public static Result testModal(){
        return ok(testModal.render());
    }

    //este codigo e so de exemplo(para eu perceber o envio de info por json)
    public static Result calendar(){
        return ok(testCharts.render());
    }


    public static Result testCharts(){
        return ok(testCharts.render());
    }

    public static Result JsonTest(){
        ObjectNode json = Json.newObject();
        json.put("teste", "value");

        return ok(views.html.test.render(json));
    }

    public static Result JsonTest2(){
        ObjectNode json = Json.newObject();
        ArrayNode arrayNode = json.arrayNode();
        json.put("teste1", "value");
        arrayNode.add(json);
        json.put("teste2", "Tuafo");
        arrayNode.add(json);
        return ok(views.html.teste2.render(arrayNode));
    }

    //termina aqui






}
