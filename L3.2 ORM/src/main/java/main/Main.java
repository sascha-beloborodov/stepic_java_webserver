package main;


import dbService.DBException;
import dbService.DBService;
import dbService.dataSets.UsersDataSet;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.SignInServlet;
import servlets.SignUpServlet;

/**
 * @author v.chibrikov
 *         <p>
 *         Пример кода для курса на https://stepic.org/
 *         <p>
 *         Описание курса и лицензия: https://github.com/vitaly-chibrikov/stepic_java_webserver
 */
public class Main {
    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        try {
            long userId = dbService.addUser("tully", "querty");
            System.out.println("Added user id: " + userId);

            UsersDataSet dataSet = dbService.getUser(userId);
            System.out.println("User data set: " + dataSet);

        } catch (DBException e) {
            e.printStackTrace();
        }


        createServlets(dbService);
    }

    public static void createServlets(DBService dbService) throws Exception{

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new SignUpServlet(dbService)), "/signup");
        context.addServlet(new ServletHolder(new SignInServlet(dbService)), "/signin");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{context});

        Server server = new Server(8090);
        server.setHandler(handlers);

        server.start();
        System.out.println("Server started");
        server.join();
    }
}
