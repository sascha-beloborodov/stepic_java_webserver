package servlets;

/**
 * Created by sascha on 14.02.16.
 */
import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;
import services.UserProfile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class SignUpServlet extends HttpServlet {
    private final DBService accountService;

    public SignUpServlet(DBService accountService) {
        this.accountService = accountService;
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UsersDataSet profile = null;
        try {
            profile = accountService.getUser(accountService.getUserId(login));
        } catch (DBException e) {
            e.printStackTrace();
        }
        if (profile != null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            accountService.addUser(login, pass);
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print("User created");
        } catch (DBException e) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
//            e.printStackTrace();
        }


    }
}