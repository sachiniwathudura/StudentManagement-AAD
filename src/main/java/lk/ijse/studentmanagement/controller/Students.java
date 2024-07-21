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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(urlPatterns = "/student",loadOnStartup = 3)

public class Students extends HttpServlet {
    Connection connection;
    public static String SAVE_STUDENT = "INSERT INTO student(id,name,email,city,level) VALUES(?,?,?,?,?)";
    public static String GET_STUDENT = "SELECT * FROM student WHERE id=?";
    public static String UPDATE_STUDENT = "UPDATE student SET name=?,email=?,city=?,level=? WHERE id=?";
    public static  String DELETE_STUDENT = "DELETE FROM student WHERE id=?";
    @Override
    public void init() throws ServletException {
//        var initParameter = getServletContext().getInitParameter("myparam");
//        System.out.println(initParameter);
        try {
//            var dbClass = getServletContext().getInitParameter("db-class");
//            var dbUrl = getServletContext().getInitParameter("dburl");
//            var dbUsername = getServletContext().getInitParameter("db-username");
//            var dbPassword  = getServletContext().getInitParameter("db-password");
//            Class.forName(dbClass);
            var ctx = new InitialContext();
            DataSource pool = (DataSource) ctx.lookup("java:comp/env/jdbc/studentRegisPortal");
            this.connection = pool.getConnection();

        }catch (SQLException | NamingException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try(var writer = resp.getWriter()){
            StudentDTO studentDTO = new StudentDTO();
            Jsonb jsonb = JsonbBuilder.create();
            var studentId = req.getParameter("studentId");
            var ps = connection.prepareStatement(GET_STUDENT);
            ps.setString(1, studentId);
            var rst = ps.executeQuery();
            while (rst.next()){
                studentDTO.setId(rst.getString("id"));
                studentDTO.setName(rst.getString("name"));
                studentDTO.setEmail(rst.getString("email"));
                studentDTO.setCity(rst.getString("city"));
                studentDTO.setLevel(rst.getString("level"));
            }
            resp.setContentType("application/json");
            jsonb.toJson(studentDTO,writer);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo save student
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
            try(var writer = resp.getWriter()) {
                Jsonb jsonb = JsonbBuilder.create();
                StudentDTO student = jsonb.fromJson(req.getReader(), StudentDTO.class);
                student.setId(Util.idGenerate());
               //save data in db
                var ps = connection.prepareStatement(SAVE_STUDENT);
                ps.setString(1,student.getId());
                ps.setString(2,student.getName());
                ps.setString(3,student.getEmail());
                ps.setString(4,student.getCity());
                ps.setString(5,student.getLevel());

                if(ps.executeUpdate() != 0){
                    resp.setStatus(HttpServletResponse.SC_CREATED);
                    writer.write("save student successfully");
                }else{
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    writer.write("failed to save student");
                }
                //create response
//                resp.setContentType("application/json");
//                jsonb.toJson(student, writer);
            }catch (SQLException e){
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                e.printStackTrace();
            }
        }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo delete student
        try(var writer = resp.getWriter()){
            var studentId = req.getParameter("studentId");
            var ps = connection.prepareStatement(DELETE_STUDENT);
            ps.setString(1, studentId);
            if(ps.executeUpdate() != 0){
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }else {
                writer.write("Delete failed");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Todo update student
        try(var writer = resp.getWriter()){
         var studentId =  req.getParameter("studentId");
         Jsonb jsonb = JsonbBuilder.create();
         StudentDTO student = jsonb.fromJson(req.getReader(),StudentDTO.class);

         var ps = connection.prepareStatement(UPDATE_STUDENT);

            ps.setString(1,student.getName());
            ps.setString(2,student.getEmail());
            ps.setString(3,student.getCity());
            ps.setString(4,student.getLevel());
            ps.setString(5, studentId);
            if(ps.executeUpdate() != 0){
                resp.setStatus(HttpServletResponse.SC_CREATED);
                writer.write("update student successfully");
            }else{
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                writer.write("failed to update student");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
