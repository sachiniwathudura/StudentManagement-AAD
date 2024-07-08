package lk.ijse.studentmanagement.controller;

import jakarta.json.*;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.studentmanagement.dto.StudentDTO;
import lk.ijse.studentmanagement.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/student")

public class Students extends HttpServlet {
    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo get student
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo save student

//        var id = req.getParameter("id");
//        var name = req.getParameter("name");
//        var email = req.getParameter("email");
//        var level = req.getParameter("level");
//
//        System.out.println(id);
//        System.out.println(name);
//        System.out.println(email);
//        System.out.println(level);

        if(req.getContentType() == null ||
                !req.getContentType().toLowerCase().startsWith("application/json")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
//        BufferedReader reader = req.getReader();
//        StringBuilder sb = new StringBuilder();
//        reader.lines().forEach(line -> sb.append(line).append("\n"));
//        System.out.println(sb);

//        JsonReader reader = Json.createReader(req.getReader());
//        JsonObject jsonObject = reader.readObject();
//        String email = jsonObject.getString("email");
//        System.out.println(email);
//
//
//        var writer = resp.getWriter();
//        writer.write(email);
//        JsonArray jsonArray = reader.readArray();
//        for (int i = 0; i< jsonArray.size(); i++ ){
//            var jsonObject =  jsonArray.getJsonObject(i);
//            System.out.println(jsonObject.getString("name"));
//            System.out.println(jsonObject.getString("email"));
//        }
                // object bundling of the json

            Jsonb jsonb = JsonbBuilder.create();
            StudentDTO student = jsonb.fromJson(req.getReader(), StudentDTO.class);
            student.setId(Util.idGenerate());
            System.out.println(student);

            //create response
            resp.setContentType("application/json");
            jsonb.toJson(student, resp.getWriter());


        }





    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo delete student
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo update student
    }
}
